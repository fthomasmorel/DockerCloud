package docker;
public class NginxManager {
	
	public static String id_container;

	public static void initNginx(){
		id_container = APIWrapper.createContainer(ContainerType.LoadBalancer);
		if(!APIWrapper.startContainer(id_container)) id_container = null;
	}
	
	public static void startNginx(){
		APIWrapper.executeCommand(id_container, new String[]{"nginx"});
	}

	public static void reloadNginx(){
		APIWrapper.executeCommand(id_container, new String[]{"nginx", "-s", "reload"});
	}
	
	public static void updateNginx(){
		APIWrapper.executeCommand(id_container, new String[]{"cp","/opt/nginx/nginx.conf","/etc/nginx/nginx.conf"});
	}

}
