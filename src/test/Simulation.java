package test;

public class Simulation extends Thread{

	private String url = "";
	private boolean running;
	private boolean simulationEnabled;
	private static int frequence;

	public Simulation(String _url){
		super();
		running = false;
		frequence = 0;
		simulationEnabled = false;
		url = _url;
	}

	public void setFrequence(int _frequence){
		frequence = _frequence;
	}

	public void run(){
		running = true;
		while(running){			
			if(simulationEnabled){
				try {
					Thread.sleep(1000);
					for(int i = 0; i < frequence*1.4 ; i++){
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
