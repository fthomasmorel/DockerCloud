package docker;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

import utils.FileManager;


public class RequestManager extends Thread{

	private DockerManager dockerManager;
	private int maxDocker;
	private boolean running;

	public RequestManager(int n){
		this.maxDocker = n;
		System.out.println("Create dockerManager");
		dockerManager = new DockerManager();
		System.out.println("Start an Apache container");
		dockerManager.startDocker();
		System.out.println("Apache container started with IP<" + dockerManager.getAllIp().get(0)+">");

		System.out.println("Cleaning log");
		FileManager.cleanLog();

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
		if(dockerManager.getNumberOfDocker()<maxDocker){
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
	}

	public void removeContainer(){
		if(dockerManager.getNumberOfDocker()>1){
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
	}

	public void run(){
		this.running = true;
		Scanner sc = new Scanner(System.in);
		sc.nextInt();
		System.out.println("Running");
		while(running){
			try {
				Thread.sleep(5000);
				int nbInstance = dockerManager.getNumberOfDocker();
				System.out.println("max => " + (nbInstance+1) * 0.03);
				System.out.println("min => " + (nbInstance+1) * 0.02);
				
				float average = computeAverageResponseTime(FileManager.readDataFromLog());
				System.out.println(new DecimalFormat("#.###").format(average) + " with " + nbInstance + " instance(s)");
				
				if(average > (nbInstance+1) * 0.03 && average != 0) addContainer();
				else if(average < (nbInstance+1) * 0.02 && average != 0) removeContainer();
				
				FileManager.cleanLog();
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
