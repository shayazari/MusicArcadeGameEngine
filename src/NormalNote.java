import bagel.*;
import bagel.util.Point;

/**
 * Class for normal notes
 */
public class NormalNote extends NormalLaneNote implements Collidable {

    private static final int COLLISION_DISTANCE = 104;

    /**
     * This method is the constructor class
     * @param dir This is the name/type of the note
     * @param appearanceFrame This is the frame the note appears from
     * @param x This is the x-coordinate of the note
     * @param y This is the initial y-coordinate of the note
     */
    public NormalNote(String dir, int appearanceFrame, int x, int y) {
        super(dir, appearanceFrame, x, y);
    }

    @Override
    public int processInput(Input input, Accuracy accuracy, int targetY,
                            Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.evaluateScore(y, targetY,
                    input.wasPressed(relevantKey));

            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return score;
            }
        }
        return 0;
    }

    @Override
    public Point getPosition() {
        return new Point(x, y);
    }

    @Override
    public boolean isCollidingWith(Collidable other) {
        return (other.getPosition().distanceTo(getPosition())
                <= COLLISION_DISTANCE);
    }
}