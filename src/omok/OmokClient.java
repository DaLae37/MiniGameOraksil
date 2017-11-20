package omok;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import interfaces.GameBoard;
import interfaces.GameClient;

public class OmokClient extends Frame implements GameClient, Runnable, ActionListener {
	public String developer = "Minjae";
	
	private TextArea msgView = new TextArea("", 1, 1, 1);
	private TextField sendBox = new TextField("");
	private TextField nameBox = new TextField();
	private TextField roomBox = new TextField("0");

	private Label pInfo = new Label("Waitingroom :  users");

	private java.awt.List pList = new java.awt.List();
	private Button startButton = new Button("Game Start");
	private Button stopButton = new Button("Drop out");
	private Button enterButton = new Button("Enter");
	private Button exitButton = new Button("Go to Waitingroom");

	private Label infoView = new Label("Omok Game - Made by " + developer, 1);

	private OmokBoard board = new OmokBoard(15, 30);
	private BufferedReader reader;
	private PrintWriter writer;
	private Socket socket;
	private int roomNumber = -1;
	private String userName = null;

	public OmokClient(String title) {
		super(title);
		setLayout(null);
		
		msgView.setEditable(false);
		infoView.setBounds(10, 30, 480, 30);
		infoView.setBackground(new Color(200, 200, 255));
		board.setLocation(10, 70);
		add(infoView);
		add(board);
		Panel p = new Panel();
		p.setBackground(new Color(200, 255, 255));
		p.setLayout(new GridLayout(3, 3));
		p.add(new Label("Name :", 2));
		p.add(nameBox);
		p.add(new Label("RoomNum :", 2));
		p.add(roomBox);
		p.add(enterButton);
		p.add(exitButton);
		enterButton.setEnabled(false);
		p.setBounds(500, 30, 250, 70);

		Panel p2 = new Panel();
		p2.setBackground(new Color(255, 255, 100));
		p2.setLayout(new BorderLayout());
		Panel p2_1 = new Panel();
		p2_1.add(startButton);
		p2_1.add(stopButton);
		p2.add(pInfo, "North");
		p2.add(pList, "Center");
		p2.add(p2_1, "South");
		startButton.setEnabled(false);
		stopButton.setEnabled(false);
		p2.setBounds(500, 110, 250, 180);

		Panel p3 = new Panel();
		p3.setLayout(new BorderLayout());
		p3.add(msgView, "Center");
		p3.add(sendBox, "South");
		p3.setBounds(500, 300, 250, 250);

		add(p);
		add(p2);
		add(p3);

		sendBox.addActionListener(this);
		enterButton.addActionListener(this);
		exitButton.addActionListener(this);
		startButton.addActionListener(this);
		stopButton.addActionListener(this);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == sendBox) {
			String msg = sendBox.getText();
			if (msg.length() == 0)
				return;
			if (msg.length() >= 30)
				msg = msg.substring(0, 30);
			try {
				writer.println("[MSG]" + msg);
				sendBox.setText("");
			} catch (Exception ie) {
			}
		}

		else if (ae.getSource() == enterButton) {
			try {

				if (Integer.parseInt(roomBox.getText()) < 1) {
					infoView.setText("Wrong RoomNum. Min : 1");
					return;
				}
				writer.println("[ROOM]" + Integer.parseInt(roomBox.getText()));
				msgView.setText("");
			} catch (Exception ie) {
				infoView.setText("Error performed.");
			}
		}

		else if (ae.getSource() == exitButton) {
			try {
				goToWaitRoom();
				startButton.setEnabled(false);
				stopButton.setEnabled(false);
			} catch (Exception e) {
			}
		}

		else if (ae.getSource() == startButton) {
			try {
				writer.println("[START]");
				infoView.setText("Wait for other's input");
				startButton.setEnabled(false);
			} catch (Exception e) {
			}
		}

		else if (ae.getSource() == stopButton) {
			try {
				writer.println("[DROPGAME]");
				endGame("You dropped out.");
			} catch (Exception e) {
			}
		}
	}

	void goToWaitRoom() {
		if (userName == null) {
			String name = nameBox.getText().trim();
			if (name.length() <= 2 || name.length() > 10) {
				infoView.setText("Wrong Name. Use 3 ~ 10 Characters");
				nameBox.requestFocus();
				return;
			}
			userName = name;
			writer.println("[NAME]" + userName);
			nameBox.setText(userName);
			nameBox.setEditable(false);
		}
		msgView.setText("");
		writer.println("[ROOM]0");
		infoView.setText("Entered Waitingroom");
		roomBox.setText("0");
		enterButton.setEnabled(true);
		exitButton.setEnabled(false);
	}

	public void run() {
		String msg;
		try {
			while ((msg = reader.readLine()) != null) {

				if (msg.startsWith("[STONE]")) {
					String temp = msg.substring(7);
					int x = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
					int y = Integer.parseInt(temp.substring(temp.indexOf(" ") + 1));
					board.putOpponent(x, y);
					board.setEnable(true);
				}

				else if (msg.startsWith("[ROOM]")) {
					if (!msg.equals("[ROOM]0")) {
						enterButton.setEnabled(false);
						exitButton.setEnabled(true);
						infoView.setText("Entered Gameroom No." + msg.substring(6));
					} else
						infoView.setText("Entered Watingroom.");

					roomNumber = Integer.parseInt(msg.substring(6));

					if (board.isRunning()) {
						board.stopGame();
					}
				}

				else if (msg.startsWith("[FULL]")) {
					infoView.setText("The Room already Fulled");
				}

				else if (msg.startsWith("[PLAYERS]")) {
					nameList(msg.substring(9));
				}

				else if (msg.startsWith("[ENTER]")) {
					pList.add(msg.substring(7));
					playersInfo();
					msgView.append("[" + msg.substring(7) + "] entered.\n");
				} else if (msg.startsWith("[EXIT]")) {
					pList.remove(msg.substring(6));
					playersInfo();
					msgView.append("[" + msg.substring(6) + "] entered to other Gameroom.\n");
					if (roomNumber != 0)
						endGame("Other user got out the room.");
				}

				else if (msg.startsWith("[DISCONNECT]")) {
					pList.remove(msg.substring(12));
					playersInfo();
					msgView.append("[" + msg.substring(12) + "] disconnected.\n");
					if (roomNumber != 0)
						endGame("Other user got out the room.");
				}

				else if (msg.startsWith("[COLOR]")) {
					String color = msg.substring(7);
					board.startGame(color);
					if (color.equals("BLACK"))
						infoView.setText("You are BLACK");
					else
						infoView.setText("You are WHITE");
					stopButton.setEnabled(true);
				}

				else if (msg.startsWith("[DROPGAME]"))
					endGame("Other user dropped out.");

				else if (msg.startsWith("[WIN]"))
					endGame("You WIN.");

				else if (msg.startsWith("[LOSE]"))
					endGame("You lose.");

				else
					msgView.append(msg + "\n");
			}
		} catch (IOException ie) {
			msgView.append(ie + "\n");
		}
		msgView.append("Disconnected.");
	}

	private void endGame(String msg) {
		infoView.setText(msg);
		startButton.setEnabled(false);
		stopButton.setEnabled(false);

		try {
			Thread.sleep(2000);
		} catch (Exception e) {
		}

		if (board.isRunning())
			board.stopGame();
		if (pList.getItemCount() == 2)
			startButton.setEnabled(true);
	}

	private void playersInfo() {
		int count = pList.getItemCount();
		if (roomNumber == 0)
			pInfo.setText("Waitingroom: " + count + "users");
		else
			pInfo.setText("Room no." + roomNumber + count + "users");

		if (count == 2 && roomNumber != 0)
			startButton.setEnabled(true);
		else
			startButton.setEnabled(false);
	}

	private void nameList(String msg) {
		pList.removeAll();
		StringTokenizer st = new StringTokenizer(msg, "\t");
		while (st.hasMoreElements())
			pList.add(st.nextToken());
		playersInfo();
	}

	void connect() {
		try {
			msgView.append("Request connection to server.\n");
			socket = new Socket("210.105.52.148", 1111);
			msgView.append("Connection Success.\n");
			msgView.append("Input your name.\n");
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);
			new Thread(this).start();
			board.setWriter(writer);
		} catch (Exception e) {
			msgView.append(e + "\n\nFailed to connect.\n");
		}
	}
}

class OmokBoard extends Canvas implements GameBoard {
	public static final int BLACK = 1, WHITE = -1;
	private int[][] map;
	private int size;
	private int cell;
	private String info = "pause";
	private int color = BLACK;

	private boolean enable = false;

	private boolean running = false;
	private PrintWriter writer;
	private Graphics gboard, gbuff;
	private Image buff;

	OmokBoard(int s, int c) {
		this.size = s;
		this.cell = c;

		map = new int[size + 2][];
		for (int i = 0; i < map.length; i++)
			map[i] = new int[size + 2];

		setBackground(new Color(200, 200, 100));
		setSize(size * (cell + 1) + size, size * (cell + 1) + size);

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				if (!enable)
					return ;

				int x = (int) Math.round(me.getX() / (double) cell);
				int y = (int) Math.round(me.getY() / (double) cell);

				if (x == 0 || y == 0 || x == size + 1 || y == size + 1)
					return ;

				if (map[x][y] == BLACK || map[x][y] == WHITE)
					return ;

				writer.println("[STONE]" + x + " " + y);

				map[x][y] = color;

				if (check(new Point(x, y), color)) {
					info = "You WIN.";
					writer.println("[WIN]");
				}

				else
					info = "Waiting for other's input";
				repaint();

				enable = false;
			}
		});
	}

	public boolean isRunning() {
		return running;
	}

	public void startGame(String col) {
		running = true;
		if (col.equals("BLACK")) {
			enable = true;
			color = BLACK;
		} else {
			enable = false;
			color = WHITE;
		}
		info = "Game Started, Black First.";
	}

	public void stopGame() {
		reset();
		writer.println("[STOPGAME]");
		enable = false;
		running = false;
	}

	public void putOpponent(int x, int y) {
		map[x][y] = -color;
		info = "Your turn!";
		repaint();
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		if (gbuff == null) {
			buff = createImage(getWidth(), getHeight());
			gbuff = buff.getGraphics();
		}
		drawBoard(g);
	}

	public void reset() {
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[i].length; j++)
				map[i][j] = 0;
		info = "pause";
		repaint();
	}

	private void drawLine() {
		gbuff.setColor(Color.black);
		for (int i = 1; i <= size; i++) {
			gbuff.drawLine(cell, i * cell, cell * size, i * cell);
			gbuff.drawLine(i * cell, cell, i * cell, cell * size);
		}
	}

	private void drawBlack(int x, int y) {
		Graphics2D gbuff = (Graphics2D) this.gbuff;
		gbuff.setColor(Color.black);
		gbuff.fillOval(x * cell - cell / 2, y * cell - cell / 2, cell, cell);
		gbuff.setColor(Color.white);
		gbuff.drawOval(x * cell - cell / 2, y * cell - cell / 2, cell, cell);
	}

	private void drawWhite(int x, int y) {
		gbuff.setColor(Color.white);
		gbuff.fillOval(x * cell - cell / 2, y * cell - cell / 2, cell, cell);
		gbuff.setColor(Color.black);
		gbuff.drawOval(x * cell - cell / 2, y * cell - cell / 2, cell, cell);
	}

	private void drawStones() {
		for (int x = 1; x <= size; x++)
			for (int y = 1; y <= size; y++) {
				if (map[x][y] == BLACK)
					drawBlack(x, y);
				else if (map[x][y] == WHITE)
					drawWhite(x, y);
			}
	}

	private void drawBoard(Graphics g) {
		gbuff.clearRect(0, 0, getWidth(), getHeight());
		drawLine();
		drawStones();
		gbuff.setColor(Color.red);
		gbuff.drawString(info, 20, 15);
		g.drawImage(buff, 0, 0, this);
	}

	private boolean check(Point p, int col) {
		if (count(p, 1, 0, col) + count(p, -1, 0, col) == 4)
			return true;
		if (count(p, 0, 1, col) + count(p, 0, -1, col) == 4)
			return true;
		if (count(p, -1, -1, col) + count(p, 1, 1, col) == 4)
			return true;
		if (count(p, 1, -1, col) + count(p, -1, 1, col) == 4)
			return true;
		return false;
	}

	private int count(Point p, int dx, int dy, int col) {
		int i = 0;
		for (; map[p.x + (i + 1) * dx][p.y + (i + 1) * dy] == col; i++)
			;
		return i;
	}
}