package docker;
import java.util.ArrayList;
import java.util.Stack;


public class DockerManager {
	private Stack<Couple> dockerStack;
	
	public DockerManager(){
		dockerStack = new Stack<Couple>();
	}
	
	public void startDocker(){
		String id = APIWrapper.createContainer(ContainerType.Application);
		if(APIWrapper.startContainer(id)) dockerStack.push(new Couple(id, APIWrapper.getIpFromContainer(id)));
	}
	
	public void stopDocker(){
		APIWrapper.stopContainer(dockerStack.pop().id);
	}
	
	public ArrayList<String> getAllIp() {
		ArrayList<String> res = new ArrayList<String>();
		for(Couple c : dockerStack){
			res.add(c.ip);
		}
		return res;
		
	}
	
	public int getNumberOfDocker(){
		return dockerStack.size();
	}
	
	private class Couple{
		public String id;
		public String ip;
		
		public Couple(String _id, String _ip){
			this.id = _id;
			this.ip = _ip;
		}
	}
	
}
