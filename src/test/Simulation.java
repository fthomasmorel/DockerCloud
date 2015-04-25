package test;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Simulation extends Thread{

	private final static String url = "http://192.168.59.103/index.php";
	private boolean running;
	private boolean simulationEnabled;
	private static int frequence;

	public Simulation(){
		super();
		running = false;
		frequence = 0;
		simulationEnabled = false;
	}

	public void setFrequence(int _frequence){
		//if (0<= _frequence &&  _frequence <= 100)
		frequence = _frequence;
	}

	public void run(){
		running = true;
		while(running){			
			if(simulationEnabled){
				try {
					Thread.sleep(200);
					for(int i = 0; i < (frequence/5) ; i++){
						new RequestThread(url).start();
					}
					
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}

		}
	}

	public void startSimulation(){
		simulationEnabled = true;
	}

	public void stopSimulation(){
		simulationEnabled = false;
	}

	public void kill(){
		running = false;
	}

}
