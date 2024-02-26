import bagel.*;
import java.util.Random;

/**
 * This is a class for enemy entity
 */
public class Enemy extends Entity implements Collidable {

    private final static int LEFT_X_BOUNDARY = 100;
    private final static int RIGHT_X_BOUNDARY = 900;
    private final int SPEED = 1;
    private int direction; // 1 for right, -1 for left

    /**
     *
     * This method is the constructor class
     * @param name This is the name of the entity
     * @param x This is the x-coordinate of Guardian
     * @param y his is the y-coordinate of Guardian
     */
    public Enemy(String name, int x, int y) {
        super(name, x, y);
        direction = new Random().nextBoolean() ? 1 : -1; /* Randomly choose
                                                            initial direction */
    }

    @Override
    public void update(Input input) {

        if (isActive()) {
            // Update enemy position based on direction and speed
            x += direction * SPEED;

            // Reverse direction if enemy reaches the screen boundaries
            if (x <= LEFT_X_BOUNDARY || x >= RIGHT_X_BOUNDARY) {
                direction = -direction;
            }
            image.draw(x, y);
        }
    }

    public void disappear() {
        deactivate();
    }

    @Override
    public boolean isCollidingWith(Collidable other) {
        return false;
    }

}

