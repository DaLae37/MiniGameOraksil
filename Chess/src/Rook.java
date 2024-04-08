public class Rook extends Piece {
    public Rook(Location location, int color) {
        super(location, color);
    }

    @Override
    boolean move(Location targetLocation, ChessBoard chessBoard) {
        return isMoveAvailable(targetLocation, chessBoard) && super.move(targetLocation, chessBoard);
    }

    @Override
    boolean isMoveAvailable(Location targetLocation, ChessBoard chessBoard) {
        // only horizontal and vertical
        int differenceX = targetLocation.getPositionX() - location.getPositionX();
        int differenceY = targetLocation.getPositionY() - location.getPositionY();
        boolean isMoveHorizontal = differenceY == 0
                && targetLocation.getPositionX() != location.getPositionX();
        boolean isMoveVertical = differenceX == 0
                && targetLocation.getPositionY() != location.getPositionY();
        return super.isMoveAvailable(targetLocation, chessBoard) && (isMoveHorizontal || isMoveVertical);
    }

    @Override
    void print() {
        System.out.print("R");
    }
}