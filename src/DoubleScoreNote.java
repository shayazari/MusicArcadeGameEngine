import bagel.*;

/**
 * Class for speed up notes
 */
public class DoubleScoreNote extends SpecialLaneNote {

    private final static int IS_DOUBLE_SCORE = 3;

    /**
     * This method is the constructor class
     * @param dir This is the name/type of the note
     * @param appearanceFrame This is the frame the note appears form
     * @param x This is the x-coordinate of the note
     * @param y This is the initial y-coordinate of the note
     */
    public DoubleScoreNote(String dir, int appearanceFrame, int x, int y) {
        super(dir, appearanceFrame, x, y);
    }

    @Override
    public int processInput(Input input, Accuracy accuracy, int targetY,
                            Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.evaluateSpecialScore(y, targetY,
                    input.wasPressed(relevantKey), IS_DOUBLE_SCORE);

            if (Accuracy.doubleScoreActivated || Accuracy.specialMissed) {
                deactivate();
                return score;
            }
        }
        return 0;
    }
}