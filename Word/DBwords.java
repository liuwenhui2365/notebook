package smartDict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.HashMap;

public class DBwords {

	public static void main(String[] args) {
		
//		HashMap<String, String> words = new HashMap<String, String>(2000);
//		��ȡ��������
//		System.out.println(words.get(word));
		File wordFile = new File("F://word.txt");
		BufferedReader reader = null;
//      ���ݿ������
		Connection con = null;
		Statement st = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
//			����ʹ��/����ʹ��\\
//			System.out.println("project: " + Thread.currentThread().getContextClassLoader().getResource("").getPath());
//			String dbPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			String path = "jdbc:sqlite:"+ dbPath +"word.db";
			String DBpath = path.toLowerCase();
			System.out.println(path);
//			char n =path.charAt(13); 
//			System.out.println((String)(path.charAt(13)).toLowerCase());
			con = DriverManager.getConnection(DBpath);
			System.out.println("���ӳɹ�");
			st = con.createStatement();
			
			reader = new BufferedReader(new FileReader(wordFile));
			String readWord = reader.readLine();	
			st.execute("create table knownWords(word varchar(20),mean varchar(50),times int)");
			while(readWord != null){
				String word = readWord;
				String mean = HttpClient.ClientRun(word);
				System.out.println(readWord);
				System.out.println(mean);
//				�ѵ��ʺ���˼�������
				st.executeUpdate("insert into knownWords values('"+readWord+"','"+mean+"',1)");
//              
//				��ȡ��һ������
				readWord = reader.readLine();
//				�����Ϊʲô��null
//				System.out.println(reader.readLine());
			}
			
			ResultSet rs = st.executeQuery("select * from knownWords");
			while(rs.next()){
				String w = rs.getString(1);
				String m = rs.getString(2);
				int n = rs.getInt(3);
				
				System.out.println(w + "\t" + m + "\t" + n + "\t");
			}
		}catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(reader != null){
					reader.close();
				}
				if(st != null){
					st.close();
				}
				if(con != null){
					con.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
