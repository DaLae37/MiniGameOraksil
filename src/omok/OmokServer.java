package omok;

import java.io.IOException;
import java.net.ServerSocket;

public class OmokServer {

	private ServerSocket socket;
	private BManager manager = new BManager();
	
	void startServer() throws Exception {
		socket = new ServerSocker(7777);
	}
	
	public static void main(String[] args) {
		OmokServer server = new OmokServer();
		//server.starServer();
	}

}
