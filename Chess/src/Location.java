public class Location {
    private int positionX;

    private int positionY;

    public Location(int positionX, int positionY) {
        if (positionX > 0) {
            this.positionX = positionX;
        } else {
            this.positionX = 1;
        }
        if (positionY > 0) {
            this.positionY = positionY;
        } else {
            this.positionY = 1;
        }
    }

    int getPositionX() {
        return positionX;
    }

    int getPositionY() {
        return positionY;
    }

    int[] getPosition() {
        return new int[] {positionX, positionY};
    }

    boolean equals(Location location) {
        return positionX == location.getPositionX() &&
                positionY == location.getPositionY();
    }
}