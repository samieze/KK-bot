import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

//
public class login_get {
	
	  private static HttpsURLConnection conn;
	  private static List <String> cookies= new ArrayList<String>();
	  static String url = "https://www.kleiderkreisel.de/member/general/login.json?ref_url=%2F";
	
	
	public static void main(String[] args) throws Exception {
		
		//get request schicken... (loginpage)
		String token = extract_token_value(establish_connection());
		
		CookieHandler.setDefault(new CookieManager());
		String token_urlencoded = URLEncoder.encode(token, "UTF-8");
		String postParams = "utf8=%E2%9C%93&authenticity_token="+token_urlencoded+"&login=samieze&password=Apfelstrudel1!&commit=Los+geht's!";
		
		sendPost(url,postParams,token);
	}
	
	
	static String extract_token_value(String s) throws FileNotFoundException{
		String value = "";
		int index_start, index_ende;
		//index_start = s.indexOf("csrf-param")+10;
        index_start = s.indexOf("content",s.indexOf("csrf-param"))+9;
        index_ende  = s.indexOf("name",index_start )-2;
        value = s.substring(index_start, index_ende); 
        return value;
        		
        
        
	}
	
	
//Schickt get an loginseite und gibt die antwort zuruck
static String establish_connection() throws IOException{
	   // Stream zum Einlesen der Daten von der URL
	   

	   BufferedReader URLinput; 
	   File file = new File("kk.txt");
	   System.out.println("File  "+file.getCanonicalPath()+"  wurde soeben erstellt");
	   FileWriter fw = null;
	   StringBuilder response = new StringBuilder();
	   try {
			 fw = new FileWriter(file);
		} catch (IOException e1) {
			
			e1.printStackTrace();
			
		} 
	   // Entsprechende URLConnection zur URL
	   HttpURLConnection httpcon;  
	   try {
	     // Anlegen der URL
	     URL url = new URL("https://www.kleiderkreisel.de/member/general/login?ref_url=%2F");
	     try {
	       // URLConnection anlegen
	       httpcon = (HttpURLConnection)url.openConnection();
	       	if(httpcon.getResponseCode() != HttpURLConnection.HTTP_OK) {
	       		//System.out.println(httpcon.getResponseMessage());
	       		}
	       URLinput = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
	       
	       String line;
	       	
	       	while((line=URLinput.readLine())!=null){
	       	response.append(line);
	       	fw.write(line);}
	       	
	       	fw.close();
	      // System.out.println(response.toString());
	       URLinput.close();
	   } 	 	
	   catch (IOException ex) { 
	   	ex.printStackTrace();
	         }        
	       }
	       catch(MalformedURLException e) {
	         e.printStackTrace();
	       }
	   return response.toString();
	     }
			

	




/**
 * Erzeugt http post nachricht...
 * 
 * @param url
 * @param postParams
 * @param token_value
 * @throws Exception
 */
 static void sendPost(String url, String postParams, String token_value) throws Exception {
	 

	 
		URL obj = new URL(url);
		conn = (HttpsURLConnection) obj.openConnection();
	 
			
		
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Host","www.kleiderkreisel.de");
		conn.setRequestProperty("accept-encoding", "gzip,deflate,sdch");
		conn.setRequestProperty("x-csrf-token", token_value);

		conn.setRequestProperty("content-type","application/x-www-form-urlencoded; charset=UTF-8");
		conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));
		
		//headerfelder die in den requests normnalerweise drinstehn
		conn.setRequestProperty("accept-language","en-US,en;q=0.8");
		conn.setRequestProperty("x-requested-with", "XMLHttpRequest");
		conn.setRequestProperty("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.114 Safari/537.36");
		conn.setRequestProperty("accept","*;q=0.5, text/javascript, application/javascript, application/ecmascript, application/x-ecmascript");
		conn.setRequestProperty("scheme", "https");
		conn.setRequestProperty("url","/member/general/login.json?ref_url=%2");
		conn.setRequestProperty("version" ,"HTTP/1.1");
		for (String cookie : cookies) {
				conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
			}	
		conn.setRequestProperty("Connection", "keep-alive");
		
		

		conn.setDoOutput(true);
		conn.setDoInput(true);
	 
		//Packt den String postParams in den Contentteil des POST request und schickt das ab.
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();
	 
		int responseCode = conn.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + postParams);
		System.out.println("Response Code : " + responseCode);
		
	 
	
		
		
		BufferedReader in = 
	             new BufferedReader(new InputStreamReader(new GZIPInputStream(conn.getInputStream())));
		String inputLine;
		StringBuffer response = new StringBuffer();
	 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		File file = new File("response_post.html");
		FileWriter fw = null;
		 try {
			 fw = new FileWriter(file);
		} catch (IOException e1) {
			
			e1.printStackTrace();
		} 
		fw.write(response.toString());
		fw.close();
		 System.out.println("Writing"+file.getCanonicalPath()+".."+response.toString());
	 
		 
	  }
 
 
 public List<String> getCookies() {
		return cookies;
	  }
	 
	  public void setCookies(List<String> cookies) {
		login_get.cookies = cookies;
	  }
	 
	
 
	 
}


