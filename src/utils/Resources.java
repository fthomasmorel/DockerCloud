package utils;

public class Resources {
	
	public final static String conf_file_url 			= System.getProperty("user.dir") + "/nginx/";
	public final static String conf_file				= "nginx.conf";
	
	public final static String log_file_url 			= System.getProperty("user.dir") + "/nginx/";
	public final static String log_file					= "access_time.log";
	
	public final static String server_docker 			= "apache_v1";
	public final static String load_balancer_docker 	= "nginx_v1";
	
	public final static String api_ip_server 			= "192.168.59.103";
	public final static String api_ip_port 				= "2375";
}