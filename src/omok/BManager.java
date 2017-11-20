package omok;

public class BManager extends Vector {
    BManager(){}
    
    void add(Omok_Thread ot) {
    	super.add(ot);
    }
    
    void remove(Omok_Thread ot) {
    	super.remove(ot);
    }
    
    OmokThread getOT(int i) {
    	return (Omok_Thread)elementAt(i);
    }
    
    Socket getSocket(int i) {
    	return getOT(i).getSocket();
    }
    
    void sendTo(int i, String msg) {
    	try {
    		PrintWriter pw= new PrintWriter(getSocket(i).getOutputStream(), true);
    		pw.println(msg);
    	} catch(Exception e) { 
    		// TODO
    	}  
    }
    
    int getRoomNumber(int i) {
    	return getOT(i).getRoomNumber();
    }
    
    boolean isFull(int roomNum) {
    	if(roomNum==0)return false;
    	int count=0;
    	for(int i=0;i<size();i++)
        if(roomNum==getRoomNumber(i))count++;
    	if(count>=2)
    		return true;
    	
    	return false;
    }
    
    void sendToRoom(int roomNum, String msg) {
    	for(int i=0;i<size();i++)
        if(roomNum==getRoomNumber(i))
        	sendTo(i, msg);
    }
    
    void sendToOthers(Omok_Thread ot, String msg) {
    	for(int i=0;i<size();i++)
        if(getRoomNumber(i)==ot.getRoomNumber() && getOT(i)!=ot)
          sendTo(i, msg);
    }
    
    boolean isReady(int roomNum) {
    	int count=0;
    	for(int i=0;i<size();i++)
        if(roomNum==getRoomNumber(i) && getOT(i).isReady())
        	count++;
    	if(count==2)
    		return true;
    	
    	return false;
    }
    
    String getNamesInRoom(int roomNum) {
    	StringBuffer sb=new StringBuffer("[PLAYERS]");
    	for(int i=0;i<size();i++)
        if(roomNum==getRoomNumber(i))
        	sb.append(getOT(i).getUserName()+"\t");
    	
    	return sb.toString();
    }
    
  }

}
