package smartDict;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
//import org.jboss.netty.handler.codec.http.CookieEncoder;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;

import java.util.regex.Pattern;
import java.util.regex.Matcher;


/**
 * A simple HTTP client that prints out the content of the HTTP response to
 * {@link System#out} to test {@link HttpServer}.
 *
 * @author <a href="http://www.jboss.org/netty/">The Netty Project</a>
 * @author Andy Taylor (andy.taylor@jboss.org)
 * @author <a href="http://gleamynode.net/">Trustin Lee</a>
 *
 * @version $Rev: 2226 $, $Date: 2010-03-31 11:26:51 +0900 (Wed, 31 Mar 2010) $
 */
public class HttpClient {
	
	private static final String WORD_PATTERN = "<div class=\"trans-container\">.*<div id=\"webTrans\" class=\"trans-wrapper trans-tab\">" ;//(.*?)<div id=\"webTrans\" class=\"trans-wrapper trans-tab\">";
	private static final String WORD_ATTR_PATTERN = "<li>(.*)</li>";
	private static final Pattern wordPattern = Pattern.compile(WORD_PATTERN, Pattern.MULTILINE | Pattern.DOTALL);
	private static final Pattern wordAttrPattern = Pattern.compile(WORD_ATTR_PATTERN);
	//private static final String REGEX = "<li>.*</li>";

	StringBuilder body;
	String url;
	HttpClient(String url){
		this.url = url;
		this.body = new StringBuilder();
	}
	public String getResponse(){
		URI uri = null;
		try {
			uri = new URI(this.url);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
		String host = uri.getHost() == null ? "localhost" : uri.getHost();
		int port = uri.getPort();
		if (port == -1) {
			if (scheme.equalsIgnoreCase("http")) {
				port = 80;
			} else if (scheme.equalsIgnoreCase("https")) {
				port = 443;
			}
		}
		if (!scheme.equalsIgnoreCase("http")
				&& !scheme.equalsIgnoreCase("https")) {
			System.err.println("Only HTTP(S) is supported.");
			return null;
		}
		boolean ssl = scheme.equalsIgnoreCase("https");
		// Configure the client.
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));
		// Set up the event pipeline factory.
		bootstrap.setPipelineFactory(new HttpClientPipelineFactory(ssl, this.body));
		// Start the connection attempt.
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
				port));
		// Wait until the connection attempt succeeds or fails.
		Channel channel = future.awaitUninterruptibly().getChannel();
		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			bootstrap.releaseExternalResources();
			return null;
		}
		// Prepare the HTTP request.
		HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1,
				HttpMethod.GET, uri.toASCIIString());
		request.setHeader(HttpHeaders.Names.HOST, host);
		request.setHeader(HttpHeaders.Names.CONNECTION,
				HttpHeaders.Values.CLOSE);
		request.setHeader(HttpHeaders.Names.ACCEPT_ENCODING,
				HttpHeaders.Values.GZIP);
		// Set some example cookies.
//		CookieEncoder httpCookieEncoder = new CookieEncoder(false);
//		httpCookieEncoder.addCookie("my-cookie", "foo");
//		httpCookieEncoder.addCookie("another-cookie", "bar");
//		request.setHeader(HttpHeaders.Names.COOKIE, httpCookieEncoder.encode());
		// Send the HTTP request.
		channel.write(request);
		// Wait for the server to close the connection.
		channel.getCloseFuture().awaitUninterruptibly();
		// Shut down executor threads to exit.
		bootstrap.releaseExternalResources();
		return this.body.toString();
	}
	
	public String getMean(String body) {
		StringBuffer words = new StringBuffer();
		Matcher word = wordPattern.matcher(body);
		//System.out.println(m.toString());
		String mean = null;
//		System.out.println("match start");
		while (word.find()){
			//System.out.println(word.group());
			Matcher attrs = wordAttrPattern.matcher(word.group());
			while (attrs.find()){
//				System.out.println(attrs.group(1));
				words.append(attrs.group(1)+";");
			}
			//m.appendReplacement(words, ":");
		}
		//m.appendTail(words);
//		System.out.println(words.toString());
//		System.out.println("match end");
		mean = words.toString();
		return mean;
	}

		public static String ClientRun(String word){
			HttpClient httpcli = new HttpClient("http://dict.youdao.com/search?len=eng&q="+word+"&keyfrom=dict.top");
			String mean = httpcli.getMean(httpcli.getResponse());
		//System.out.println(httpcli.getResponse());
			return mean;
		}
}
