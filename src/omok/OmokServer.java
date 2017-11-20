package omok;

import java.io.IOException;
import java.net.ServerSocket;

public class OmokServer {

	private ServerSocket serversocket;
	private BManager bMan = new BManager();
	
	void startServer() {
		try {
			serversocket = new ServerSocket(/*1234*/);
			System.out.println("Created Server Socket : ");
			Socket socket = server.accept();
			OmokTread ot = new OmokThread(socket);
			//ot.start();
			//bMan.add(ot);
			System.out.println("Connected : " + bMan.size());
		} catch (Exeption e) {
			System.out.println(e.toString);
		}
	}
	
	public static void main(String[] args) {
		OmokServer server = new OmokServer();
		//server.starServer();
	}

}
