import bagel.util.Point;

/**
 * This is an interface for collidable classes
 */
public interface Collidable {

    // Returns the center coordinate of the collidable object

    /**
     * This method returns the center coordinate of the collidable object
     * @return Point This returns the Point(x, y) of the collidable object
     */
    Point getPosition();

    /**
     * This method checks if the object is colliding with another collidable object
     * @param other This is the other collidable object that may be colliding
     * @return boolean This returns true if the two collidable objects collided
     */
    boolean isCollidingWith(Collidable other);
}
