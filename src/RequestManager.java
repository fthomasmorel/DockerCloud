import java.text.DecimalFormat;
import java.util.ArrayList;


public class RequestManager extends Thread{

	private DockerManager dockerManager;
	private boolean running;

	public RequestManager(){
		System.out.println("Create dockerManager");
		dockerManager = new DockerManager();
		System.out.println("Start an Apache container");
		dockerManager.startDocker();
		System.out.println("Apache container started with IP<" + dockerManager.getAllIp().get(0)+">");

		System.out.println("First init of nginx.conf");
		FileManager.writeFileWithData(dockerManager.getAllIp());

		System.out.println("Start Nginx container");
		NginxManager.initNginx();
		NginxManager.startNginx();
		NginxManager.updateNginx();
		NginxManager.reloadNginx();
		System.out.println("Nginx container started successfully");
	}

	public void addContainer(){
		System.out.println("Start an Apache container");
		dockerManager.startDocker();
		System.out.println("Apache container started with IP<" + dockerManager.getAllIp().get(0)+">");

		System.out.println("Update of nginx.conf");
		FileManager.writeFileWithData(dockerManager.getAllIp());

		System.out.println("Reload Nginx container");
		NginxManager.updateNginx();
		NginxManager.reloadNginx();
		System.out.println("Nginx container reloaded successfully");
	}

	public void removeContainer(){
		System.out.println("Stop an Apache container");
		dockerManager.stopDocker();
		System.out.println("An Apache container has been stopped");

		System.out.println("Update of nginx.conf");
		FileManager.writeFileWithData(dockerManager.getAllIp());

		System.out.println("Reload Nginx container");
		NginxManager.updateNginx();
		NginxManager.reloadNginx();
		System.out.println("Nginx container reloaded successfully");
	}

	public void run(){
		this.running = true;
		while(running){
			try {
				System.out.println(new DecimalFormat("#.###").format(computeAverageResponseTime(FileManager.readDataFromLog(100))));
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public float computeAverageResponseTime(ArrayList<Float> data){
		float res = 0;
		for(Float f : data)
			res += f;
		return res/(data.size()>0 ? data.size() : 1);

	}

	public void kill(){
		this.running = false;
	}



}
