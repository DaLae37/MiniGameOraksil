package omok;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class OmokTread extends Thread {
	private int roomNum = -1;
	private String userName = null;
	private Socket socket;
	
	private boolean isReady = false;
	
	private BufferedReader reader;
	private PrintWriter writer;
	
	public OmokTread(Socket socket) {
		this.socket = socket;
	}

	public int getRoomNum() {
		return roomNum;
	}
	
	public String getUserNmae() {
		return userName;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public boolean isReady() {
		return isReady();
	}
	
	public void run() {
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);
			
			String msg;
			
			while ( (msg = reader.readLine() ) != null ) {
				// default game settings
			}
			
		} catch (Exception e) {
		} finally {
			
		}
	}
	
}
