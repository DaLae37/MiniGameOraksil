import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
//        print("Chess board width  = ");
//        int boardWidth = scanner.nextInt();
//        print("Chess board height = ");
//        int boardHeight = scanner.nextInt();
        ChessBoard chessBoard = new ChessBoard(8, 8);
        chessBoard.initializeChessBoard();
        chessBoard.print();

        int playCount = 0;

        while (true) {
            if (playCount % 2 == 0) {
                // white turn
                println("White turn");
            } else {
                println("Black turn");
            }
            playCount++;
            println("Select a piece to move");
            print("X = ");
            int locationX = scanner.nextInt();
            print("Y = ");
            int locationY = scanner.nextInt();
            chessBoard.printMoveAvailable(new Location(locationX, locationY));
        }
    }

    public static void print(Object obj) {
        System.out.print(obj.toString());
    }

    public static void println(Object obj) {
        System.out.println(obj.toString());
    }
}