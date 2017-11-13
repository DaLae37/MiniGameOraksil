public class Knight extends Piece {
    public Knight(Location location, int color) {
        super(location, color);
    }

    @Override
    boolean move(Location targetLocation, ChessBoard chessBoard) {
        return isMoveAvailable(targetLocation, chessBoard) && super.move(targetLocation, chessBoard);
    }

    @Override
    boolean isMoveAvailable(Location targetLocation, ChessBoard chessBoard) {
        int differenceX = targetLocation.getPositionX() - location.getPositionX();
        int differenceY = targetLocation.getPositionY() - location.getPositionY();
        return super.isMoveAvailable(targetLocation, chessBoard)
                && ((Math.abs(differenceX) == 2 || Math.abs(differenceX) == 1)
                && Math.abs(differenceX * differenceY) == 2);
    }

    @Override
    void print() {
        System.out.print("k");
    }
}