public class ChessBoard {
    private int boardWidth;

    private int boardHeight;

    private Piece pieces[][];

    public ChessBoard(int boardWidth, int boardHeight) {
        if (boardWidth >= 8) {
            // should be bigger than 0
            this.boardWidth = boardWidth;
        } else {
            this.boardWidth = 8;
        }
        if (boardHeight >= 8) {
            // should be bigger than 0
            this.boardHeight = boardHeight;
        } else {
            this.boardHeight = 8;
        }

        this.pieces = new Piece[this.boardWidth][this.boardHeight];
    }

    int getBoardWidth() {
        return boardWidth;
    }

    int getBoardHeight() {
        return boardHeight;
    }

    Piece[][] getPieces() {
        return pieces;
    }

    void print() {
        System.out.println("Chess board");
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                Piece piece = pieces[j][i];
                if (piece == null) {
                    System.out.print(". ");
                } else {
                    piece.print();
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    void printMoveAvailable(Location location) {
        Piece selectedPiece = pieces[location.getPositionX() - 1][boardHeight - location.getPositionY()];
        if (selectedPiece == null) {
            System.out.println("\nNo piece selected");
        } else {
            System.out.println("\nAvailable movement");
        }
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                Piece piece = pieces[j][i];
                if (selectedPiece != null
                        && selectedPiece.isMoveAvailable(new Location(j + 1, i + 1), this)) {
                    // available
                    System.out.print("- ");
                } else {
                    if (piece == null) {
                        System.out.print(". ");
                    } else {
                        piece.print();
                        System.out.print(" ");
                    }
                }
            }
            System.out.println();
        }
    }

    void setPiece(Location location, Piece piece) {
        if (pieces[location.getPositionX() - 1][location.getPositionY() - 1] != null) {
            pieces[location.getPositionX() - 1][location.getPositionY() - 1].kill();
        }
        pieces[location.getPositionX() - 1][location.getPositionY() - 1] = piece;
    }

    void setPiece(Piece piece) {
        if (pieces[piece.getLocation().getPositionX() - 1][piece.getLocation().getPositionY() - 1] != null) {
            pieces[piece.getLocation().getPositionX() - 1][piece.getLocation().getPositionY() - 1].kill();
        }
        pieces[piece.getLocation().getPositionX() - 1][piece.getLocation().getPositionY() - 1] = piece;
    }

    void movePiece(Location startLocation, Location finishLocation) {
        Piece movingPiece = getPiece(startLocation);
        setPiece(finishLocation, movingPiece);
        setPiece(startLocation, null);
    }

    Piece getPiece(Location location) {
        if (location.getPositionX() <= this.boardWidth
                && location.getPositionY() <= this.boardHeight) {
            return pieces[location.getPositionX() - 1][location.getPositionY() - 1];
        } else {
            return null;
        }
    }

    void initializeChessBoard() {
        /** initialize the chess board for new game */
        // get the starting x position
        int startX = (boardWidth / 2) - 4;
        // white side, bottom
        setPiece(new Rook  (new Location(startX + 1, 1), 0));
        setPiece(new Knight(new Location(startX + 2, 1), 0));
        setPiece(new Bishop(new Location(startX + 3, 1), 0));
        setPiece(new King  (new Location(startX + 4, 1), 0));
        setPiece(new Queen (new Location(startX + 5, 1), 0));
        setPiece(new Bishop(new Location(startX + 6, 1), 0));
        setPiece(new Knight(new Location(startX + 7, 1), 0));
        setPiece(new Rook  (new Location(startX + 8, 1), 0));
        for (int i = 1; i <= 8; i++) {
            setPiece(new Pawn(new Location(startX + i, 2), 0));
        }
        // black side, top
        setPiece(new Rook  (new Location(startX + 1, boardHeight), 1));
        setPiece(new Knight(new Location(startX + 2, boardHeight), 1));
        setPiece(new Bishop(new Location(startX + 3, boardHeight), 1));
        setPiece(new King  (new Location(startX + 4, boardHeight), 1));
        setPiece(new Queen (new Location(startX + 5, boardHeight), 1));
        setPiece(new Bishop(new Location(startX + 6, boardHeight), 1));
        setPiece(new Knight(new Location(startX + 7, boardHeight), 1));
        setPiece(new Rook  (new Location(startX + 8, boardHeight), 1));
        for (int i = 1; i <= 8; i++) {
            setPiece(new Pawn(new Location(startX + i, boardHeight - 1), 1));
        }
    }
}