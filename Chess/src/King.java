public class King extends Piece {
    public King(Location location, int color) {
        super(location, color);
    }

    @Override
    boolean move(Location targetLocation, ChessBoard chessBoard) {
        return isMoveAvailable(targetLocation, chessBoard) && super.move(targetLocation, chessBoard);
    }

    @Override
    boolean isMoveAvailable(Location targetLocation, ChessBoard chessBoard) {
        // 1 square to any direction
        int differenceX = Math.abs(targetLocation.getPositionX() - location.getPositionX());
        int differenceY = Math.abs(targetLocation.getPositionY() - location.getPositionY());
        return super.isMoveAvailable(targetLocation, chessBoard)
                && differenceX <= 1
                && differenceY <= 1;
    }

    @Override
    void print() {
        System.out.print("K");
    }
}