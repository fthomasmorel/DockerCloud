import java.util.Scanner;

import docker.APIWrapper;
import docker.RequestManager;
import json.JSONArray;
import json.JSONObject;


public class Application {

	public static void main(String[] args) {
		
		System.out.println("Cleaning Docker...");
		JSONArray array = APIWrapper.getAllContainers();
		for(int i=0 ; i < array.length() ; i++)
			APIWrapper.stopContainer((String)( (JSONObject) (array.get(i)) ).get("Id"));

		RequestManager manager = new RequestManager();
		manager.start();
		
		System.out.println("Veuillez saisir un entier (1 = add, 2 = remove) :");
		Scanner sc = new Scanner(System.in);
		int str;
		while((str = sc.nextInt()) != 0){
			switch(str){
			case 1:
				manager.addContainer();
				break;
			case 2:
				manager.removeContainer();
				break;
			}
		}
	}
}
