public class Pawn extends Piece {
    private int direction;

    public Pawn(Location location, int color) {
        super(location, color);
        direction = color;
    }

    @Override
    boolean move(Location targetLocation, ChessBoard chessBoard) {
        return isMoveAvailable(targetLocation, chessBoard) && super.move(targetLocation, chessBoard);
    }

    @Override
    boolean isMoveAvailable(Location targetLocation, ChessBoard chessBoard) {
        boolean isPawnMoveAvailable;
        int differenceX = targetLocation.getPositionX() - location.getPositionX();
        int differenceY = targetLocation.getPositionY() - location.getPositionY();
        if (direction % 2 == 1) {
            // black side, go down
            isPawnMoveAvailable = (differenceX == 0 && differenceY == -1)
                    || ((differenceX == 1 || differenceX == -1) && differenceY == -1 && chessBoard.getPiece(targetLocation) != null);
            if (location.getPositionY() == chessBoard.getBoardHeight() - 1) {
                // starting position
                isPawnMoveAvailable = isPawnMoveAvailable || (differenceX == 0 && differenceY == -2);
            }
        } else {
            // white side, go up
            isPawnMoveAvailable = (differenceX == 0 && differenceY == 1)
                    || ((differenceX == 1 || differenceX == -1) && differenceY == 1 && chessBoard.getPiece(targetLocation) != null);
            if (location.getPositionY() == 2) {
                // starting position
                isPawnMoveAvailable = isPawnMoveAvailable || (differenceX == 0 && differenceY == 2);
            }
        }
        return super.isMoveAvailable(targetLocation, chessBoard)
                && isPawnMoveAvailable;
    }

    @Override
    void print() {
        System.out.print("P");
    }
}