package omok;

import java.io.*;
import java.net.*;
import java.util.*;

import interfaces.GameManager;
import interfaces.GameServer;
import interfaces.GameThread;

public class OmokServer implements GameServer {

	private ServerSocket serversocket;
	private BManager bMan = new BManager();

	public OmokServer() {
	}

	void startServer() {
		try {
			serversocket = new ServerSocket(1111);
			System.out.println("Created Server Socket : ");
			while (true) {
				Socket socket = serversocket.accept();
				OmokThread ot = new OmokThread(socket);
				ot.start();
				bMan.add(ot);
				System.out.println("Connected : " + bMan.size());
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public static void main(String[] args) {
		OmokServer server = new OmokServer();
		server.startServer();
	}

	
	public class OmokThread extends Thread implements GameThread {
		private int roomNum = -1;
		private String userName = null;
		private Socket socket;

		private boolean isReady = false;

		private BufferedReader reader;
		private PrintWriter writer;

		public OmokThread(Socket socket) {
			this.socket = socket;
		}

		public int getRoomNum() {
			return roomNum;
		}

		public String getUserName() {
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

				while ((msg = reader.readLine()) != null) {
					if (msg.startsWith("[NAME]")) {
						userName=msg.substring(6);
					}

					else if (msg.startsWith("[ROOM]")) {
						int roomNum = Integer.parseInt(msg.substring(6));
						if (!bMan.isFull(roomNum)) {
							if (roomNum != -1)
								bMan.sendToOthers(this, "[EXIT]" + userName);
							roomNum = roomNum;
							writer.println(msg);
							writer.println(bMan.getNamesInRoom(roomNum));
							bMan.sendToOthers(this, "[ENTER]" + userName);
						} else
							writer.println("[FULL]");
					}

					else if (roomNum >= 1 && msg.startsWith("[STONE]"))
						bMan.sendToOthers(this, msg);

					else if (msg.startsWith("[MSG]"))
						bMan.sendToRoom(roomNum, "[" + userName + "]: " + msg.substring(5));

					else if (msg.startsWith("[START]")) {
						isReady = true;
						if (bMan.isReady(roomNum)) {
							Random random = new Random();
							int col = random.nextInt(2);
							if (col == 0) {
								writer.println("[COLOR]BLACK");
								bMan.sendToOthers(this, "[COLOR]WHITE");
							} else {
								writer.println("[COLOR]WHITE");
								bMan.sendToOthers(this, "[COLOR]BLACK");
							}
						}
					}

					else if (msg.startsWith("[STOPGAME]"))
						isReady = false;

					else if (msg.startsWith("[DROPGAME]")) {
						isReady = false;
						bMan.sendToOthers(this, "[DROPGAME]");
					}

					else if (msg.startsWith("[WIN]")) {
						isReady = false;
						writer.println("[WIN]");
						bMan.sendToOthers(this, "[LOSE]");
					}
				}

			} catch (Exception e) {
			} finally {
				try {
					bMan.remove(this);
					if (reader != null)
						reader.close();
					if (writer != null)
						writer.close();
					if (socket != null)
						socket.close();
					reader = null;
					writer = null;
					socket = null;
					System.out.println(userName + "disconnected.");
					System.out.println(+ bMan.size() + "users");
					bMan.sendToRoom(roomNum, "[DISCONNECT]" + userName);
				} catch (Exception e) {
					// TODO
				}
			}
		}

	}

	
	public class BManager extends Vector implements GameManager {
		public BManager() {}

		void add(OmokThread ot) {
			super.add(ot);
		}

		void remove(OmokThread ot) {
			super.remove(ot);
		}

		OmokThread getOT(int i) {
			return (OmokThread) elementAt(i);
		}

		Socket getSocket(int i) {
			return getOT(i).getSocket();
		}

		void sendTo(int i, String msg) {
			try {
				PrintWriter pw = new PrintWriter(getSocket(i).getOutputStream(), true);
				pw.println(msg);
			} catch (Exception e) {
				// TODO
			}
		}

		int getroomNum(int i) {
			return getOT(i).getRoomNum();
		}

		boolean isFull(int roomNum) {
			if (roomNum == 0)
				return false;
			int count = 0;
			for (int i = 0; i < size(); i++)
				if (roomNum == getroomNum(i))
					count++;
			if (count >= 2)
				return true;

			return false;
		}

		void sendToRoom(int roomNum, String msg) {
			for (int i = 0; i < size(); i++)
				if (roomNum == getroomNum(i))
					sendTo(i, msg);
		}

		void sendToOthers(OmokThread ot, String msg) {
			for (int i = 0; i < size(); i++)
				if (getroomNum(i) == ot.getRoomNum() && getOT(i) != ot)
					sendTo(i, msg);
		}

		boolean isReady(int roomNum) {
			int count = 0;
			for (int i = 0; i < size(); i++)
				if (roomNum == getroomNum(i) && getOT(i).isReady())
					count++;
			if (count == 2)
				return true;

			return false;
		}

		String getNamesInRoom(int roomNum) {
			StringBuffer sb = new StringBuffer("[PLAYERS]");
			for (int i = 0; i < size(); i++)
				if (roomNum == getroomNum(i))
					sb.append(getOT(i).getUserName() + "\t");

			return sb.toString();
		}

	}

}
