public class Bishop extends Piece {
    public Bishop(Location location, int color) {
        super(location, color);
    }

    @Override
    boolean move(Location targetLocation, ChessBoard chessBoard) {
        return isMoveAvailable(targetLocation, chessBoard) && super.move(targetLocation, chessBoard);
    }

    @Override
    boolean isMoveAvailable(Location targetLocation, ChessBoard chessBoard) {
        // x and y difference is same, which means diagonal movement
        int differenceX = targetLocation.getPositionX() - location.getPositionX();
        int differenceY = targetLocation.getPositionY() - location.getPositionY();
        return super.isMoveAvailable(targetLocation, chessBoard)
                && (Math.abs(differenceX) == Math.abs(differenceY));
    }

    @Override
    void print() {
        System.out.print("B");
    }
}