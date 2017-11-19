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
				if (msg.startsWith("[NAME]"))
				{
		            //userName=msg.substring(6);
		        } 
				
				else if (msg.startsWith("[ROOM]")) 
		        {
		        	int roomNum=Integer.parseInt(msg.substring(6));
		            if( !bMan.isFull(roomNum))
		            {
		              if(roomNumber!=-1)
		            	  bMan.sendToOthers(this, "[EXIT]"+userName);
		              roomNumber=roomNum;
		              writer.println(msg);
		              writer.println(bMan.getNamesInRoom(roomNumber));
		              bMan.sendToOthers(this, "[ENTER]"+userName);
		            } 
		            else 
		            	writer.println("[FULL]");
		        }
				
				else if(roomNumber>=1 && msg.startsWith("[STONE]"))
		            bMan.sendToOthers(this, msg);
				
				else if(msg.startsWith("[MSG]"))
		            bMan.sendToRoom(roomNumber,
		                              "["+userName+"]: "+msg.substring(5));
				
				else if(msg.startsWith("[START]"))
				{
			        ready=true;
			        if( bMan.isReady(roomNumber) ) 
			        {
			            int a=rnd.nextInt(2);
			             if(a==0)
			             {
			               writer.println("[COLOR]BLACK");
			               bMan.sendToOthers(this, "[COLOR]WHITE");
			             }
			             else
			             {
			              writer.println("[COLOR]WHITE");
			              bMan.sendToOthers(this, "[COLOR]BLACK");
			             }
			        }
				}
				
				else if(msg.startsWith("[STOPGAME]"))
		            ready=false;
				
				else if(msg.startsWith("[DROPGAME]"))
				{
		            ready=false;
		            bMan.sendToOthers(this, "[DROPGAME]");
		        }
				
				else if(msg.startsWith("[WIN]"))
				{
		            ready=false;
		            writer.println("[WIN]");
		            bMan.sendToOthers(this, "[LOSE]");
		          }  
			
		} catch (Exception e) {
		} finally {
			bMan.remove(this);
	        if(reader!=null) reader.close();
	        if(writer!=null) writer.close();
	        if(socket!=null) socket.close();
	        reader=null; writer=null; socket=null;
	        System.out.println(userName+"님이 접속을 끊었습니다.");
	        System.out.println("접속자 수: "+bMan.size());
	        bMan.sendToRoom(roomNumber,"[DISCONNECT]"+userName);
		}
	}
	
}
