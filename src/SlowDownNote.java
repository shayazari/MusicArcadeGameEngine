import bagel.*;

/**
 * Class for slow down notes
 */
public class SlowDownNote extends SpecialLaneNote {

    private final static int IS_SLOW_DOWN = 2;
    private final int SLOW_DOWN = -1;

    /**
     * This method is the constructor class
     * @param dir This is the name/type of the note
     * @param appearanceFrame This is the frame the note appears form
     * @param x This is the x-coordinate of the note
     * @param y This is the initial y-coordinate of the note
     */
    public SlowDownNote(String dir, int appearanceFrame, int x, int y) {
        super(dir, appearanceFrame, x, y);
    }

    @Override
    public int processInput(Input input, Accuracy accuracy, int targetHeight,
                            Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.evaluateSpecialScore(y, targetHeight,
                    input.wasPressed(relevantKey), IS_SLOW_DOWN);

            if (score == Accuracy.ACTIVATED_SCORE || Accuracy.specialMissed) {
                if (score == Accuracy.ACTIVATED_SCORE) {
                    speed += SLOW_DOWN;
                }
                deactivate();
                return score;
            }
        }
        return 0;
    }
}