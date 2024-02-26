import bagel.*;
import bagel.util.*;
import java.util.*;

/**
 * This is a class for guardian entity
 */
public class Guardian extends Entity {
    private final Keys relevantKey;
    private final List<Projectile> projectiles = new ArrayList<>();
    private final Level level;

    /**
     * This method is the constructor class
     * @param name This is the name of the entity
     * @param x This is the x-coordinate of Guardian
     * @param y his is the y-coordinate of Guardian
     * @param level This is the current level
     */
    public Guardian(String name, int x, int y, Level level) {
        super(name, x, y);
        relevantKey = Keys.LEFT_SHIFT;
        this.level = level;
    }

    @Override
    public void update(Input input) {
        image.draw(x, y);

        if (input.wasPressed(relevantKey)) {
            Enemy closestEnemy = findClosestEnemy();
            if (closestEnemy != null) {
                Point enemyPosition = closestEnemy.getPosition();
                Projectile projectile = new Projectile("arrow", x, y,
                                                enemyPosition, level);
                projectiles.add(projectile);
            }
        }
        shootProjectile(input);

    }

    /**
     * finds the closest enemy based on
     * distance to guardian
     */
    private Enemy findClosestEnemy() {
        List<Enemy> enemies = level.getEnemies();
        if (enemies.isEmpty()) {
            return null;
        }

        Enemy closestEnemy = enemies.get(0);
        double closestDistance = position.distanceTo(closestEnemy.
                                                    getPosition());

        for (Enemy enemy : enemies) {
            double distance = position.distanceTo(enemy.getPosition());
            if (distance < closestDistance) {
                closestDistance = distance;
                closestEnemy = enemy;
            }
        }
        return closestEnemy;
    }

    /**
     * shoots projectiles (and updates their movement)
     * by calling up Projectile class update method
     */
    private void shootProjectile(Input input) {
        Iterator<Projectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            if (projectile.isActive()) {
                projectile.update(input);
            } else {
                iterator.remove();
            }
        }

    }
}
