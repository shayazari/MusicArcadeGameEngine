import bagel.*;

/**
 * Class for normal lane bomb notes
 */
public class BombNoteNormalLane extends NormalLaneNote {

    private final static int IS_BOMB = 4;

    /**
     * This method is the constructor class
     * @param dir This is the name/type of the note
     * @param appearanceFrame This is the frame the note appears form
     * @param x This is the x-coordinate of the note
     * @param y This is the initial y-coordinate of the note
     */
    public BombNoteNormalLane(String dir, int appearanceFrame, int x, int y) {
        super("Bomb", appearanceFrame, x, y);
    }

    @Override
    public int processInput(Input input, Accuracy accuracy, int targetY,
                            Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.evaluateSpecialScore(y, targetY,
                    input.wasPressed(relevantKey), IS_BOMB);

            if (Accuracy.bombExploded || Accuracy.specialMissed) {
                deactivate();
                return score;
            }
        }
        return 0;
    }
}