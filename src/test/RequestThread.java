package test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestThread extends Thread{

	private String url;
	
	public RequestThread(String _url){
		this.url = _url;
	}
	
	public void run(){
		try { 
	        URL url = new URL(this.url); 
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
	        connection.setDoOutput(true); 
	        connection.setInstanceFollowRedirects(true); 
	        connection.setRequestMethod("GET"); 
//	        connection.setRequestProperty("Content-Type", "application/json"); 

	        InputStream is = connection.getInputStream(); 
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			
			while(br.readLine() != null);
			
			br.close();
			isr.close();
			is.close();
	        connection.disconnect();
			
	    } catch(Exception e) {
	    	e.printStackTrace();
	    } 
	}
}
