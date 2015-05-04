import test.UIController;
import utils.Resources;
import docker.APIWrapper;
import docker.RequestManager;
import json.JSONArray;
import json.JSONObject;


public class Application {

	public static void main(String[] args) {
		if(args.length < 1){showHelpCommand();return;}
		if(args[0].equals("cloud")){
			Resources.initResources();
			System.out.println("Cleaning Docker...");
			JSONArray array = APIWrapper.getAllContainers();
			for(int i=0 ; i < array.length() ; i++)
				APIWrapper.stopContainer((String)( (JSONObject) (array.get(i)) ).get("Id"));
			
			System.out.println("Starting Server...");
			RequestManager manager = new RequestManager(Resources.numberInstances);
			manager.start();
		}else if(args[0].equals("test") && args.length >= 2){
			System.out.println("Starting Simulator...");
			UIController controller = new UIController(args[1]);
			controller.start();
		}else{
			showHelpCommand();return;
		}
	}
	
	public static void showHelpCommand(){
		System.out.println("To use DockerCloud follow the instructions from the GitHub page");
		System.out.println("Run : java -jar DockerCloud.jar [parameter]");
		System.out.println("Parameter's value depends on what you want to start");
		System.out.println("Use \"cloud\" without the quote to start the cloud part");
		System.out.println("Use \"test\" without the quote to start the test part");
		System.out.println("When using the test part don't forget to add the url of the server you want to send your requests to.");
		System.out.println("For example, run : java -jar DockerCloud test http://192.168.1.78:8080/index.php");
	}
}
