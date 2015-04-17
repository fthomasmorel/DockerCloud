import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class FileManager {

	public static void writeFileWithData(ArrayList<String> data){
		try {
			PrintWriter writer = new PrintWriter(Resources.file_url, "UTF-8");
			writer.println("http {");
			writer.println("upstream myapp1 {");
			writer.println("least_conn;");
			for(String ip : data){
				writer.println("server " + ip + ";");
			}
			writer.println("}");

			writer.println("server {");

			writer.println("listen 80;");

			writer.println("location / {");
			writer.println("proxy_pass http://myapp1;");
			writer.println("}");

			writer.println("log_format timing '$upstream_response_time '");
			writer.println("'$request_time';");

			writer.println("access_log /opt/nginx/" + Resources.log_file + " timing;");

			writer.println("location /nginx_status {");
			writer.println("stub_status on;");
			writer.println("}");
			writer.println("}");
			writer.println("}");

			writer.println("events {");
			writer.println("worker_connections  1024;");
			writer.println("}");

			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}


	public static ArrayList<Float> readDataFromLog(int n){
		try {
			ArrayList<Float> result = new ArrayList<Float>();
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec("tail -n " + n + " " + Resources.log_file_url);
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				if(line.split(" ").length==2)
					result.add(Float.parseFloat(line.split(" ")[1]));
			}
			br.close();
			isr.close();
			is.close();
			process.destroy();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
