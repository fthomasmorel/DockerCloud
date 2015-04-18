import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintWriter;

import json.JSONArray;
import json.JSONObject;

import java.net.URL;


public class APIWrapper {
	
	private static final String url_base = Resources.api_ip_server + ":" + Resources.api_ip_port;

	public static String createContainer(ContainerType _type){
		try { 
			JSONObject body;
			if(_type == ContainerType.Application){
				body = getApplicationJSONBody();
			}else{
				body = getLoadBalancerJSONBody();
			}
			
			URL url = new URL("http://" + url_base + "/containers/create"); 
			HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
			connection.setDoOutput(true); 
			connection.setInstanceFollowRedirects(false); 
			connection.setRequestMethod("POST"); 
			connection.setRequestProperty("Content-Type", "application/json");

			OutputStream os = connection.getOutputStream();
			PrintWriter out = new PrintWriter(os);
			out.write(body.toString());
			out.flush();

			InputStream is = connection.getInputStream(); 
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			String res = "";
			while ((line = br.readLine()) != null){
				System.out.println(line);
				res += line;
			}
			
			String result = (String) new JSONObject(res).get("Id");
			connection.disconnect(); 
			return result;
		} catch(Exception e) { 
			throw new RuntimeException(e); 
		}
	}
	
	

	public static boolean startContainer(String _id){
		try { 
			URL url = new URL("http://" + url_base + "/containers/" + _id + "/start"); 
			HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
			connection.setDoOutput(true); 
			connection.setInstanceFollowRedirects(false); 
			connection.setRequestMethod("POST"); 
			connection.setRequestProperty("Content-Type", "application/json");
			boolean res = (connection.getResponseCode() >= 200 && connection.getResponseCode() <= 299);
			connection.disconnect(); 
			return res;
		} catch(Exception e) { 
			throw new RuntimeException(e); 
		} 	
	}
	
	public static boolean stopContainer(String _id){
		try { 
			URL url = new URL("http://" + url_base + "/containers/" + _id + "/stop"); 
			HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
			connection.setDoOutput(true); 
			connection.setInstanceFollowRedirects(false); 
			connection.setRequestMethod("POST"); 
			connection.setRequestProperty("Content-Type", "application/json");
			boolean res = (connection.getResponseCode() >= 200 && connection.getResponseCode() <= 299);
			connection.disconnect(); 
			return res; 
		} catch(Exception e) { 
			throw new RuntimeException(e); 
		} 
	}
	
	public static String getIpFromContainer(String _id){
		try { 
			URL url = new URL("http://" + url_base + "/containers/" + _id + "/json"); 
			HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
			connection.setDoOutput(true); 
			connection.setInstanceFollowRedirects(false); 
			connection.setRequestMethod("GET"); 
			connection.setRequestProperty("Content-Type", "application/json"); 

			InputStream is = connection.getInputStream(); 
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			String res = "";
			while ((line = br.readLine()) != null) res += line;
			
			JSONObject tmp = new JSONObject(res);
			tmp = (JSONObject)tmp.get("NetworkSettings");
			String ip = (String) tmp.get("IPAddress");
			connection.disconnect();
			return ip;
		} catch(Exception e) { 
			throw new RuntimeException(e); 
		} 
	}
	
	public static boolean executeCommand(String _id, String[] _command){
		JSONObject nginx_exec = new JSONObject()
		.put("AttachStdin", false)
		.put("AttachStdout", true)
		.put("AttachStderr",true)
		.put("Tty", false)
		.put("Cmd", new JSONArray(_command));
		    
		try { 
			URL url = new URL("http://" + url_base + "/containers/" + _id + "/exec"); 
			HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
			connection.setDoOutput(true); 
			connection.setInstanceFollowRedirects(true); 
			connection.setRequestMethod("POST"); 
			connection.setRequestProperty("Content-Type", "application/json");			
			
			OutputStream os = connection.getOutputStream();
			PrintWriter out = new PrintWriter(os);
			out.write(nginx_exec.toString());
			out.flush();

			InputStream is = connection.getInputStream(); 
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			String res = "";
			while ((line = br.readLine()) != null) res += line;
			
			connection.disconnect();
			
			return execute((String) new JSONObject(res).get("Id"));
		} catch(Exception e) { 
			throw new RuntimeException(e); 
		} 
	}
	
	private static boolean execute(String _id){
		JSONObject nginx_exec = new JSONObject()
		.put("Detach", false)
		.put("Tty", false);
		    
		try { 
			URL url = new URL("http://" + url_base + "/exec/" + _id + "/start"); 
			HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
			connection.setDoOutput(true); 
			connection.setInstanceFollowRedirects(true); 
			connection.setRequestMethod("POST"); 
			connection.setRequestProperty("Content-Type", "application/json");			
			
			OutputStream os = connection.getOutputStream();
			PrintWriter out = new PrintWriter(os);
			out.write(nginx_exec.toString());
			out.flush();

			InputStream is = connection.getInputStream(); 
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			
			while ((line = br.readLine()) != null) System.out.println(line);

			boolean res = (connection.getResponseCode() >=200 && connection.getResponseCode() <=299); 
			connection.disconnect();
			return res;
		} catch(Exception e) { 
			throw new RuntimeException(e); 
		} 
	}
	
	public static JSONArray getAllContainers(){
		try { 
	        URL url = new URL("http://" + url_base + "/containers/json"); 
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
	        connection.setDoOutput(true); 
	        connection.setInstanceFollowRedirects(false); 
	        connection.setRequestMethod("GET"); 
	        connection.setRequestProperty("Content-Type", "application/json"); 

	        InputStream is = connection.getInputStream(); 
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			String res = "";
			while ((line = br.readLine()) != null){
				res += line;
			}
			JSONArray array = new JSONArray(res);
	        connection.disconnect();
	        return array;
	    } catch(Exception e) { 
	        throw new RuntimeException(e); 
	    } 
	}
	
	private static JSONObject getLoadBalancerJSONBody() {
		return new JSONObject()
		.put("Hostname", "")
		.put("Domainname", "")
		.put("User", "")
		.put("Memory", 0)
		.put("MemorySwap", 0)
		.put("CpuShares", 512)
		.put("Cpuset", "0,1")
		.put("AttachStdin", false)
		.put("AttachStdout", true)
		.put("AttachStderr", true)
		.put("Tty", false)
		.put("OpenStdin", false)
		.put("StdinOnce", false)
		.put("Env", JSONObject.NULL)
		.put("Cmd",new JSONArray().put("/bin/sh").put("-c").put("while true; do sleep 1000; done"))
		.put("Entrypoint", "")
		.put("Image", Resources.load_balancer_docker)
		.put("Volumes", new JSONObject())
		.put("WorkingDir", "")
		.put("NetworkDisabled", false)
		.put("MacAddress", "12:34:56:78:9a:bc")
		.put("ExposedPorts", new JSONObject().put("80/tcp", new JSONObject()))
		.put("SecurityOpts", new JSONArray().put(""))
		.put("HostConfig", new JSONObject()		
			.put("Binds", new JSONArray().put(Resources.conf_file_url+":/opt/nginx/:rw"))
			.put("Links", new JSONArray())
			.put("LxcConf", new JSONObject())
			.put("PortBindings", new JSONObject().put("80/tcp", new JSONArray().put(new JSONObject().put("HostPort", "80"))))
			.put("PublishAllPorts", false)
			.put("Privileged", false)
			.put("ReadonlyRootfs", false)
			.put("Dns", new JSONArray().put("8.8.8.8"))
			.put("DnsSearch", new JSONArray().put(""))
			.put("ExtraHosts", JSONObject.NULL)
			.put("VolumesFrom", new JSONArray())
		);
	}

	private static JSONObject getApplicationJSONBody() {
		return new JSONObject()
		.put("Hostname","")
		.put("User","")
		.put("Memory",0)
		.put("MemorySwap",0)
		.put("AttachStdin",false)
		.put("AttachStdout",true)
		.put("AttachStderr",true)
		.put("PortSpecs",JSONObject.NULL)
		.put("Privileged", false)
		.put("Tty",false)
		.put("OpenStdin",false)
		.put("StdinOnce",false)
		.put("Env",JSONObject.NULL)
		.put("Cmd",new JSONArray().put("/usr/sbin/apache2ctl").put("-D").put("FOREGROUND"))
		.put("Dns",JSONObject.NULL)
		.put("Image", Resources.server_docker)
		.put("Volumes",new JSONObject())
		.put("VolumesFrom","")
		.put("WorkingDir","");
	}
}
