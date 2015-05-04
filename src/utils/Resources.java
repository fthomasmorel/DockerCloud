package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Resources {

	public final static String conf_file_url 			= System.getProperty("user.dir") + "/nginx/";
	public final static String conf_file				= "nginx.conf";

	public final static String log_file_url 			= System.getProperty("user.dir") + "/nginx/";
	public final static String log_file					= "access_time.log";

	public static String instance_docker 				= "apache_v1";
	public static String load_balancer_docker 			= "nginx_v1";

	public static String api_ip_server 					= "192.168.59.103";
	public static String api_ip_port 					= "2375";
	
	public static float trigger_max						= (float) 0.6;
	public static float trigger_min						= (float) 0.4;
	
	public static int numberInstances					= 3;

	public static void initResources(){
		try {
			Properties prop = new Properties();
			FileInputStream in;
			in = new FileInputStream("dockercloud.conf");
			prop.load(in);
			
			api_ip_server = prop.getProperty("api_ip_server");
			api_ip_port = prop.getProperty("api_ip_port");
			
			instance_docker = prop.getProperty("instance_name");
			load_balancer_docker = prop.getProperty("loadbalancer_namer");
			
			trigger_min = (float) Double.parseDouble(prop.getProperty("trigger_min"));
			trigger_max = (float) Double.parseDouble(prop.getProperty("trigger_max"));
			
			numberInstances = Integer.parseInt(prop.getProperty("numberInstances"));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
