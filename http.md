#HTTP协议

 http（超文本传输协议）是一个基于请求与响应模式的、无状态的、应用层的协议，常基于TCP的连接方式，HTTP1.1版本中给出一种持续连接的机制，绝大多数的Web开发，都是构建在HTTP协议之上的Web应用。


- HTTP URL (URL是一种特殊类型的URI，包含了用于查找某个资源的足够的信息)的格式如下：
       http://host[":"port][abs_path]
>http表示要通过HTTP协议来定位网络资源；host表示合法的Internet主  机域名或者IP地址；port指定一个端口号，为空则使用缺省端口80；abs_path指定请求资源的URI；如果URL中没有给出abs_path，那么当它作为请求URI时，必须以“/”的形式给出，通常这个工作浏览器自动帮我们完成。
 
- http请求由三部分组成，分别是：请求行、消息报头、请求正文
>请求方法（所有方法全为大写）有多种，各个方法的解释如下：
	
>- GET     请求获取Request-URI所标识的资源
>- POST    在Request-URI所标识的资源后附加新的数据
>- HEAD    请求获取由Request-URI所标识的资源的响应消息报头
>- PUT     请求服务器存储一个资源，并用Request-URI作为其标识
>- DELETE  请求服务器删除Request-URI所标识的资源
>- TRACE   请求服务器回送收到的请求信息，主要用于测试或诊断
>- CONNECT 保留将来使用
>- OPTIONS 请求查询服务器的性能，或者查询与资源相关的选项和需求

- GET方法：在浏览器的地址栏中输入网址的方式访问网页时，浏览器采用		  GET方法向服务器获取资源，

- POST方法要求被请求服务器接受附在请求后面的数据，常用于提交表单。

- HEAD方法与GET方法几乎是一样的，对于HEAD请求的回应部分来说，它的		 HTTP头部中包含的信息与通过GET请求所得到的信息是相同的。		利用这个方法，不必传输整个资源内容，就可以得到Request-		URI所标识的资源的信息。该方法常用于测试超链接的有效性，是		否可以访问，以及最近是否更新。

- HTTP响应也是由三个部分组成，分别是：状态行、消息报头、响应正文
  
- HTTP消息由客户端到服务器的请求和服务器到客户端的响应组成。请求消	息和响应消息都是由开始行（对于请求消息，开始行就是请求行，对于	响应消息，开始行就是状态行），消息报头（可选），空行（只有	CRLF的行），消息正文（可选）组成。

- HTTP消息报头包括普通报头、请求报头、响应报头、实体报头。每一个报	头域都是由名字+“：”+空格+值 组成，消息报头域的名字是大小写无	关的。

- 中介由三种：代理(Proxy)、网关(Gateway)和通道(Tunnel).

>- 一个代理根据URI的绝对格式来接受请求，重写全部或部分消息，通过 
   URI的标识把已格式化过的请求发送到服务器。网关是一个接收代理，作为一些其它服务器的上层，并且如果必须的话，可以把请求翻译给下层的服务器协议。
>- 一个通道作为不改变消息的两个连接之间的中继点。当通讯需要通过一
   个中介(例如：防火墙等)或者是中介不能识别消息的内容时，通道经常被使用。

- 代理(Proxy)：一个中间程序，它可以充当一个服务器，也可以充当一个	客户机，为其它客户机建立请求。请求是通过可能的翻译在内部或经过	传递到其它的 服务器中。一个代理在发送请求信息之前，必须解释并	且如果可能重写它。代理经常作为通过防火墙的客户机端的门户，代理	还可以作为一个帮助应用来通过协议处 理没有被用户代理完成的请	求。

- 网关(Gateway)：一个作为其它服务器中间媒介的服务器。与代理不同	的是，网关接受请求就好象对被请求的资源来说它就是源服务器；发出	请求的客户机并没有意识到它在同网关打交道。
>网关经常作为通过防火墙的服务器端的门户，网关还可以作为一个协议翻译器以便存取那些存储在非HTTP系统中的资源。

- 通道(Tunnel)：是作为两个连接中继的中介程序。一旦激活，通道便被	认为不属于HTTP通讯，尽管通道可能是被一个HTTP请求初始化的。当	被中继的连接两端关闭时，通道便消失。当一个门户(Portal)必须存	在或中介(Intermediary)不能解释中继的通讯时通道被经常使用。

- Http指纹识别工具Httprint,它通过运用统计学原理,组合模糊的逻辑学	技术,能很有效的确定Http服务器的类型.它可以被用来收集和分析不	同Http服务器产生的签名。