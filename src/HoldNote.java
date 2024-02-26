import bagel.*;

/**
 * Class for hold notes
 */
public class HoldNote extends NormalLaneNote {
    private static final int HEIGHT_OFFSET = 82;
    private boolean holdStarted = false;

    /**
     * This method is the constructor class
     * @param dir This is the name/type of the note
     * @param appearanceFrame This is the frame the note appears form
     * @param x This is the x-coordinate of the note
     * @param y This is the initial y-coordinate of the note
     */
    public HoldNote(String dir, int appearanceFrame, int x, int y) {
        super(dir, appearanceFrame, x, y);
    }

    public void startHold() {
        holdStarted = true;
    }

    /**
     * scored twice, once at the start of the hold and once at the end
     */
    @Override
    public int processInput(Input input, Accuracy accuracy, int targetY,
                            Keys relevantKey) {
        if (isActive() && !holdStarted) {
            int score = accuracy.evaluateScore(getBottomHeight(), targetY,
                    input.wasPressed(relevantKey));

            if (score == Accuracy.MISS_SCORE) {
                deactivate();
                return score;
            } else if (score != Accuracy.NOT_SCORED) {
                startHold();
                return score;
            }
        } else if (isActive() && holdStarted) {
            int score = accuracy.evaluateScore(getTopHeight(), targetY,
                    input.wasReleased(relevantKey));

            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return score;
            } else if (input.wasReleased(relevantKey)) {
                deactivate();
                accuracy.setAccuracy(Accuracy.MISS);
                return Accuracy.MISS_SCORE;
            }
        }
        return 0;
    }

    /**
     * gets the location of the start of the note
     */
    private int getBottomHeight() {
        return y + HEIGHT_OFFSET;
    }

    /**
     * gets the location of the end of the note
     */
    private int getTopHeight() {
        return y - HEIGHT_OFFSET;
    }
}
