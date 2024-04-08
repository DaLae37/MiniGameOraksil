public class Piece {
    protected Location location;

    private int color;

    private boolean isAlive;

    public Piece(Location location, int color) {
        this.location = location;
        this.color = color;
        this.isAlive = true;
    }

    boolean move(Location targetLocation, ChessBoard chessBoard) {
        if (isMoveAvailable(targetLocation, chessBoard)) {
            chessBoard.movePiece(this.location, targetLocation);
            return true;
        } else {
            return false;
        }
    }

    public Location getLocation() {
        return location;
    }

    public int getColor() {
        return color;
    }

    public boolean isAlive() {
        return isAlive;
    }

    boolean isMoveAvailable(Location targetLocation, ChessBoard chessBoard) {
        return chessBoard.getBoardWidth() >= targetLocation.getPositionX()
                && chessBoard.getBoardHeight() >= targetLocation.getPositionY()
                && !location.equals(targetLocation);
    }

    void kill() {
        this.isAlive = false;
    }

    void print() {
        System.out.print("0");
    }
}