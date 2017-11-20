package omok;

public class Omok {
	
	public static void main(String[] args) {
		OmokClient client = new OmokClient("Omok");
		client.setSize(1024, 768);
		client.setVisible(true);
		client.connect();
	}
	
}
