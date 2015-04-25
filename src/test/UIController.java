package test;

public class UIController extends Thread implements DashboardDelegate {
	private UIDashboard dashboard;
	private int frequence;
	private boolean running;
	private boolean simulationEnabled;
	private Simulation simulation;

	public UIController(){
		dashboard = new UIDashboard(this);
		simulation = new Simulation();
		simulation.start();
		frequence = 0;
		simulationEnabled = false;
		initSimulation();
	}

	public void run(){
		running = true;
		while(running){
			try {
				if(simulationEnabled){
					dashboard.updateDataset(frequence);
					simulation.setFrequence(frequence);
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void initSimulation() {
		simulationEnabled = false;
		dashboard.clearChart();
		dashboard.addSerie("% Charge");
		
		dashboard.startButton.setEnabled(true);
		dashboard.stopButton.setEnabled(false);
	}

	@Override
	public void startSimulation() {
		if(!simulationEnabled){
			System.out.println("Starting simulation");
			simulation.startSimulation();
			simulationEnabled = true;
			
			dashboard.startButton.setEnabled(false);
			dashboard.stopButton.setEnabled(true);	
		}
	}

	@Override
	public void stopSimulation() {
		if(simulationEnabled){
			System.out.println("Stopped simulation");
			simulation.stopSimulation();
			simulationEnabled = false;
			
			dashboard.startButton.setEnabled(true);
			dashboard.stopButton.setEnabled(false);
		}
	}

	@Override
	public void setFrequence(int _freq) {
		System.out.println("Changing frequence " + _freq);
		this.frequence = _freq;
		simulation.setFrequence(_freq);
	}

	public void kill(){
		this.running = false;
	}
}
