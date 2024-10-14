public class NavigationController {
    private int currentX;
    private int currentY;
    private ActivityLogger activityLogger;

    public NavigationController(int startX, int startY) {
        this.currentX = startX;
        this.currentY = startY;
        this.activityLogger = new ActivityLogger();
    }

    public void moveRight() {
        currentX++;
        updatePosition();
    }

    public void moveLeft() {
        currentX--;
        updatePosition();
    }

    public void moveDown() {
        currentY++;
        updatePosition();
    }

    public int[] getCurrentPosition() {
        return new int[]{currentX, currentY};
    }

    private void updatePosition() {
        activityLogger.logMovement(currentX, currentY, "moved");
        // Removed any rendering calls from here
    }
}
