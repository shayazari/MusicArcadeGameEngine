import bagel.*;

/**
 * Class for dealing with accuracy of pressing the notes
 */
public class Accuracy {

    /**
     * This is the amount of points for a perfect score.
     */
    public static final int PERFECT_SCORE = 10;

    /**
     * This is the amount of points for a good score.
     */
    public static final int GOOD_SCORE = 5;

    /**
     * This is the amount of points for a bad score.
     */
    public static final int BAD_SCORE = -1;

    /**
     * This is the amount of points for a miss score.
     */
    public static final int MISS_SCORE = -5;

    /**
     * This indicates note is not scored.
     */
    public static final int NOT_SCORED = 0;

    /**
     * This is the amount of points for
     * speed up and slow down notes being activated.
     */
    public static final int ACTIVATED_SCORE = 15;

    /**
     * This is the perfect score message.
     */
    public static final String PERFECT = "PERFECT";

    /**
     * This is the good score message.
     */
    public static final String GOOD = "GOOD";

    /**
     * This is the bad score message.
     */
    public static final String BAD = "BAD";

    /**
     * This is the miss score message.
     */
    public static final String MISS = "MISS";

    /**
     * This is the speed up activated message.
     */
    public static final String SPEED_UP = "SPEED UP";

    /**
     * This is the slow down activated message.
     */
    public static final String SLOW_DOWN = "SLOW DOWN";

    /**
     * This is the double score activated message.
     */
    public static final String DOUBLE_SCORE = "DOUBLE SCORE";

    /**
     * This is the bomb exploded message.
     */
    public static final String LANE_CLEAR = "LANE CLEAR";

    private static final int PERFECT_RADIUS = 15;
    private static final int GOOD_RADIUS = 50;
    private static final int BAD_RADIUS = 100;
    private static final int MISS_RADIUS = 200;
    private static final int ACTIVATED_RADIUS = 50;
    private static final Font ACCURACY_FONT = new
                Font(ShadowDance.FONT_FILE, 40);
    private static final int RENDER_FRAMES = 30;
    private static final int DOUBLE_SCORE_RENDER_FRAMES = 480;
    private static final int IS_SPEED_UP = 1;
    private static final int IS_SLOW_DOWN = 2;
    private static final int IS_DOUBLE_SCORE = 3;
    private static final int IS_BOMB = 4;
    private final int DEFAULT_SPEED = 2;
    private String currAccuracy = null;
    private String currDoubleScoreAccuracy = null;
    private int frameCount = 0;
    private int doubleScoreFrameCount = 0;
    private boolean doubleScoreActive = false;

    /**
     * This is true if double score is activated.
     */
    public static boolean doubleScoreActivated = false;

    /**
     * This is true if bomb exploded.
     */
    public static boolean bombExploded = false;

    /**
     * This is true if a special note is missed
     * (as there is no miss score for special
     * notes and some special notes do not
     * add to the score).
     */
    public static boolean specialMissed = false;

    /**
     * This class sets the accuracy message of the note and
     * initialises the frame count for accuracy message.
     * @param accuracy This is the accuracy message.
     */
    public void setAccuracy(String accuracy) {
        currAccuracy = accuracy;
        frameCount = 0;
    }

    /**
     * This class sets the accuracy message for double score and
     * initialises the frame count for double score being active.
     * @param accuracy This is the accuracy message of double score.
     */
    public void setDoubleScoreActive(String accuracy) {
        currDoubleScoreAccuracy = accuracy;
        doubleScoreFrameCount = 0;
    }

    /**
     * This class evaluates scores of normal notes.
     * @param noteY This is the node's y-coordinate when user pressed key.
     * @param targetY This is the target y-coordinate.
     * @param triggered This is true if user pressed key.
     * @return int his returns the score added/deducted (if any).
     */
    public int evaluateScore(int noteY, int targetY, boolean triggered) {

        int distance = Math.abs(noteY - targetY);

        if (triggered) {
            if (distance <= PERFECT_RADIUS) {
                setAccuracy(PERFECT);
                return scoreOutput(PERFECT_SCORE);
            } else if (distance <= GOOD_RADIUS) {
                setAccuracy(GOOD);
                return scoreOutput(GOOD_SCORE);
            } else if (distance <= BAD_RADIUS) {
                setAccuracy(BAD);
                return scoreOutput(BAD_SCORE);
            } else if (distance <= MISS_RADIUS) {
                setAccuracy(MISS);
                return scoreOutput(MISS_SCORE);
            }

        } else if (noteY >= (Window.getHeight())) {
            setAccuracy(MISS);
            return scoreOutput(MISS_SCORE);
        }

        return NOT_SCORED;

    }

    /**
     * This class evaluates scores and effects of special notes.
     * @param noteY This is the node's y-coordinate when user pressed key.
     * @param targetY This is the target y-coordinate.
     * @param triggered This is true if user pressed key.
     * @param noteType This is an arbitrary indicator for note type.
     * @return int his returns the score added/deducted (if any).
     */
    public int evaluateSpecialScore(int noteY, int targetY, boolean triggered,
     int noteType) {

        doubleScoreActivated = false;
        bombExploded = false;
        specialMissed = false;

        int distance = Math.abs(noteY - targetY);

        if (triggered) {
            if (distance <= ACTIVATED_RADIUS) {
                switch (noteType) {
                    case (IS_SPEED_UP):
                        setAccuracy(SPEED_UP);
                        return scoreOutput(ACTIVATED_SCORE);
                    case (IS_SLOW_DOWN):
                        setAccuracy(SLOW_DOWN);
                        return scoreOutput(ACTIVATED_SCORE);
                    case (IS_DOUBLE_SCORE):
                        doubleScoreActivated = true;
                        setAccuracy(DOUBLE_SCORE);
                        setDoubleScoreActive(DOUBLE_SCORE);
                        return NOT_SCORED;
                    case (IS_BOMB):
                        bombExploded = true;
                        setAccuracy(LANE_CLEAR);
                        return NOT_SCORED;
                }
            }

        } else if (noteY >= (Window.getHeight())) {
            specialMissed = true;
            return NOT_SCORED;
        }

        return NOT_SCORED;

    }

    /**
     * This method performs a state update.
     * Updates score and prints accuracy message.
     * Also keeps track of if double score is active.
     * @param finished This stops all accuracy updates if true.
     */
    public void update(boolean finished) {
        frameCount++;
        if (finished) {
            frameCount = RENDER_FRAMES;
            Note.speed = DEFAULT_SPEED;
        }
        if (currAccuracy != null && frameCount < RENDER_FRAMES) {
                ACCURACY_FONT.drawString(currAccuracy,
                        Window.getWidth() / 2 -
                                ACCURACY_FONT.getWidth(currAccuracy) / 2,
                                    Window.getHeight() / 2);
            }
            doubleScoreFrameCount++;
            doubleScoreActive = (currDoubleScoreAccuracy != null &&
                    doubleScoreFrameCount < DOUBLE_SCORE_RENDER_FRAMES);
        }

    private int scoreOutput(int score) {
        if (doubleScoreActive) {
            return score * 2;
        }
        return score;
    }
}