package docker;
import java.text.DecimalFormat;
import java.util.ArrayList;
import utils.FileManager;


public class RequestManager extends Thread{

	private ApacheManager apacheManager;
	private int maxDocker;
	private boolean running;

	public RequestManager(int n){
		this.maxDocker = n;
		apacheManager = new ApacheManager();
		System.out.println("Starting an Apache container");
		apacheManager.startDocker();
		FileManager.cleanLog();
		
		FileManager.writeFileWithData(apacheManager.getAllIp());

		System.out.println("Starting Nginx container");
		NginxManager.initNginx();
		NginxManager.startNginx();
		NginxManager.updateNginx();
		NginxManager.reloadNginx();
	}

	private void addContainer(){
		if(apacheManager.getNumberOfDocker()<maxDocker){
			System.out.println("Starting an Apache container");
			apacheManager.startDocker();
			FileManager.writeFileWithData(apacheManager.getAllIp());
			NginxManager.updateNginx();
			NginxManager.reloadNginx();
		}
	}

	private void removeContainer(){
		if(apacheManager.getNumberOfDocker()>1){
			System.out.println("Stopping an Apache container");
			apacheManager.stopDocker();
			FileManager.writeFileWithData(apacheManager.getAllIp());
			NginxManager.updateNginx();
			NginxManager.reloadNginx();
		}
	}

	public void run(){
		this.running = true;
		System.out.println("Running server");
		System.out.println("iteration;average_request_time;number_of_instances");
		int i = 0;	
		int adding = 0;
		int deleting = 0;
		boolean updated = false;
		while(running){
			try {
				if(updated){
					updated=false;
					Thread.sleep(8000);
				}
				Thread.sleep(2000);
				int nbInstance = apacheManager.getNumberOfDocker();
				
				float average = computeAverageResponseTime(FileManager.readDataFromLog());
				System.out.println(++i + ";" + new DecimalFormat("#.###").format(average) + ";" + nbInstance);
				
				if(average > 0.6 && average != 0){
					adding++; 
					deleting = 0;
				}else if(average < 0.4 && average != 0){
					deleting++;
					adding = 0;
				}else{
					adding = 0;
					deleting = 0;
				}
		
				if(adding > 2){
					addContainer();
					adding = 0;
					deleting = 0;
				}
				if(deleting > 2){
					removeContainer();
					adding = 0;
					deleting = 0;
				}
				FileManager.cleanLog();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private float computeAverageResponseTime(ArrayList<Float> data){
		float res = 0;
		for(Float f : data)
			res += f;
		return res/(data.size()>0 ? data.size() : 1);

	}

	public void kill(){
		this.running = false;
	}


}
