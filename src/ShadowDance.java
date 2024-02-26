import bagel.*;

/**
 * ShadowDance game.
 * @Shayan Azari Pour
 */
public class ShadowDance extends AbstractGame  {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final Image BACKGROUND_IMAGE = new Image(
                            "res/background.png");
    private final static String LEVEL_1_CSV = "res/level1.csv";
    private final static String LEVEL_2_CSV = "res/level2.csv";
    private final static String LEVEL_3_CSV = "res/level3.csv";
    public final static String FONT_FILE = "res/FSO8BITR.TTF";
    private final static int TITLE_X = 220;
    private final static int TITLE_Y = 250;
    private final static int INS_X_OFFSET = 100;
    private final static int INS_Y_OFFSET = 190;
    private final static int SCORE_LOCATION = 35;
    private final static int RETURN_TO_LVL_SELECTION_Y = 500;
    private final static int LVL_1 = 1;
    private final static int LVL_2 = 2;
    private final static int LVL_3 = 3;
    private final Font TITLE_FONT = new Font(FONT_FILE, 64);
    private final Font INSTRUCTION_FONT = new Font(FONT_FILE, 24);
    private final Font SCORE_FONT = new Font(FONT_FILE, 30);
    private static final String INSTRUCTIONS =
            "   SELECT LEVELS WITH\n        NUMBER KEYS\n\n" +
            "            1  2  3";
    private static final int CLEAR_SCORE_LVL_1 = 150;
    private static final int CLEAR_SCORE_LVL_2 = 400;
    private static final int CLEAR_SCORE_LVL_3 = 350;
    private static final String CLEAR_MESSAGE = "CLEAR!";
    private static final String TRY_AGAIN_MESSAGE = "TRY AGAIN";
    private static final String RETURN_TO_LVL_SELECTION_MSG =
            "PRESS SPACE TO RETURN TO LEVEL SELECTION";
    private final Accuracy accuracy = new Accuracy();
    private final Level[] levels = new Level[3];
    private int currLvlIndex;
    private int currLvl;
    private int score = 0;
    private static int currFrame = 0;
    private boolean started = false;
    private boolean finished = false;

    /**
     * This method is the class constructor
     */
    public ShadowDance(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * This method is the entry point for the program.
     * Runs the game.
     * @param args String[] Not used
     */
    public static void main(String[] args) {
        ShadowDance game = new ShadowDance();
        game.run();
    }

    /**
     * This method reads a level CSV file
     * @param levelIndex This is the index
     * of the level that needs to be read
     */
    private void readLevel(int levelIndex) {
        switch (levelIndex) {
            case (LVL_1 - 1):
                levels[levelIndex] = new Level(LEVEL_1_CSV);
                break;
            case (LVL_2 - 1):
                levels[levelIndex] = new Level(LEVEL_2_CSV);
                break;
            case (LVL_3 - 1):
                levels[levelIndex] = new Level(LEVEL_3_CSV);
                break;
        }
    }

    /**
     * This method performs a state update.
     * Allows level selection once level complete.
     * @param input This is the keyboard input entered by the user
     */
    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0,
                Window.getHeight()/2.0);

        if (!started) {
            startScreen(input);

        } else if (finished) {
            finishScreen(input);

        } else {
            SCORE_FONT.drawString("Score " + score, SCORE_LOCATION,
                    SCORE_LOCATION);

            currFrame++;
            score += levels[currLvlIndex].update(input, accuracy, currLvl,
                    currFrame);

            finished = levels[currLvlIndex].checkFinished();
            accuracy.update(finished);
        }
    }

    /**
     * This method handles the start screen of the game
     * @param input This is the keyboard input entered by the user
     */
    public void startScreen (Input input) {

        currFrame = 0;
        score = 0;

        TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
        INSTRUCTION_FONT.drawString(INSTRUCTIONS,
                TITLE_X + INS_X_OFFSET, TITLE_Y + INS_Y_OFFSET);

        if (input.wasPressed(Keys.NUM_1) || input.wasPressed(Keys.NUM_2) ||
                input.wasPressed(Keys.NUM_3)) {
            started = true;
            if (input.wasPressed(Keys.NUM_1)) currLvlIndex = LVL_1 - 1;
            else if (input.wasPressed(Keys.NUM_2)) currLvlIndex = LVL_2 - 1;
            else if (input.wasPressed(Keys.NUM_3)) currLvlIndex = LVL_3 - 1;
            currLvl = currLvlIndex + 1;
            readLevel(currLvlIndex);
        }
    }

    /**
     * This method handles the finish screen of the game
     * @param input This is the keyboard input entered by the user
     */
    public void finishScreen (Input input) {

        int clearScore = (currLvl == LVL_1) ? CLEAR_SCORE_LVL_1 :
                (currLvl == LVL_2) ? CLEAR_SCORE_LVL_2 : CLEAR_SCORE_LVL_3;
        if (score >= clearScore) {
            TITLE_FONT.drawString(CLEAR_MESSAGE, WINDOW_WIDTH/2 -
                    TITLE_FONT.getWidth(CLEAR_MESSAGE)/2, WINDOW_HEIGHT/2);
        } else {
            TITLE_FONT.drawString(TRY_AGAIN_MESSAGE, WINDOW_WIDTH/2 -
                  TITLE_FONT.getWidth(TRY_AGAIN_MESSAGE)/2, WINDOW_HEIGHT/2);
        }
        INSTRUCTION_FONT.drawString(RETURN_TO_LVL_SELECTION_MSG,
            WINDOW_WIDTH/2 - INSTRUCTION_FONT.getWidth
                    (RETURN_TO_LVL_SELECTION_MSG)/2, RETURN_TO_LVL_SELECTION_Y);
        if (input.wasPressed(Keys.SPACE)) {
            started = false;
            finished = false;
        }
    }

    /**
     * This method returns the current frame the game is on
     * @return int This returns the current frame the game is on
     */
    public static int getCurrFrame() {
        return currFrame;
    }
}