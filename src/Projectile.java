import bagel.*;
import bagel.util.*;
import java.util.List;

/**
 * This is a class for projectile entity
 */
public class Projectile extends Entity implements Collidable {

    private static final int SPEED = 6;
    private static final int COLLISION_DISTANCE = 62;
    private final Vector2 velocity;
    private final double radians;
    private final Level level;

    /**
     * This method is the constructor class
     * @param name This is the name of the entity
     * @param x This is the x-coordinate of Guardian
     * @param y his is the y-coordinate of Guardian
     * @param target This is the enemy location the projectile moves towards
     * @param level This is the current level
     */
    public Projectile(String name, double x, double y, Point target,
                      Level level) {
        super(name, x, y);
        this.velocity = calculateVelocity(target);
        this.level = level;
        radians = Math.atan2(target.y - y, target.x - x);
    }

    /**
     * calculates the velocity vector of the projectile
     */
    private Vector2 calculateVelocity(Point target) {
        Vector2 direction = target.asVector().sub(getPosition().
                                        asVector()).normalised();
        return direction.mul(SPEED);
    }


    @Override
    public void update(Input input) {
        if (isActive()) {
            List<Enemy> enemies = level.getEnemies();
            Point newPosition = getPosition().asVector().
                                    add(velocity).asPoint();
            setPosition(newPosition.x, newPosition.y);
            x += SPEED * Math.cos(radians);
            y += SPEED * Math.sin(radians);
            image.draw(x, y, new DrawOptions().setRotation(radians));
            for (Enemy enemy : enemies) {
                if (isCollidingWith(enemy)) {
                    enemy.disappear();
                }
            }
        }
    }

    @Override
    public boolean isCollidingWith(Collidable other) {
        return other.getPosition().distanceTo(getPosition())
                <= COLLISION_DISTANCE;
    }
}