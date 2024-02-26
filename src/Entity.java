import bagel.*;
import bagel.util.*;

/**
 * This is an abstract class for entity
 */
public abstract class Entity {
    protected double x;
    protected double y;
    protected Point position;
    protected final Image image;
    private boolean active = true;

    public Entity(String name, double x, double y) {
        this.x = x;
        this.y = y;
        this.position = new Point(x, y);
        image = new Image("res/" + name + ".png");
    }

    public void deactivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public abstract void update(Input input);

    public Point getPosition() {
        return new Point(x, y);
    }

    public void setPosition(double x, double y) {
        this.position = new Point(x, y);
    }

}