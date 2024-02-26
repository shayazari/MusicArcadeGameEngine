import bagel.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a class for the levels of the game
 */
public class Level {

    // one special lane in level 2 & 3 + max 4 normal lanes in each level
    private final Lane[] lanes = new Lane[5];
    private final String CSV_FILE;
    private int numLanes = 0;
    private final static int NORMAL_Y = 100;
    private final static int HOLD_Y = 24;
    private final static int LEVEL_3 = 3;
    private final static int GUARDIAN_X = 800;
    private final static int GUARDIAN_Y = 600;
    private final static int ENEMY_X_Y_MIN = 100;
    private final static int ENEMY_X_MAX = 900;
    private final static int ENEMY_Y_MAX = 500;
    private final static int FRAMES_PER_ENEMY = 600;
    private int leftLaneX;
    private int rightLaneX;
    private int upLaneX;
    private int downLaneX;
    private boolean guardianSpawned = false;
    private final List<Enemy> enemies = new ArrayList<>();
    Guardian guardian = null;

    /**
     * This method is the class constructor
     * @param CSV_FILE This is the CSV file address
     */
    public Level(String CSV_FILE) {
        this.CSV_FILE = CSV_FILE;
        readCsv();
    }

    private void readCsv() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String textRead;
            while ((textRead = br.readLine()) != null) {
                String[] splitText = textRead.split(",");

                if (splitText[0].equals("Lane")) {
                    // reading lanes
                    readLanes (splitText);

                } else {
                    // reading notes
                    readNotes(splitText);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * This method performs a state update.
z
     * @param input This is the keyboard input entered by the user
     * @param accuracy This is the Accuracy class
     * @param level This is the current level number
     * @param currFrame This is the current frame of the game
     * @return int This returns the score added/deducted (if any)
     */
    protected int update(Input input, Accuracy accuracy, int level,
                                    int currFrame) {
        int score = 0;
        for (int i = 0; i < numLanes; i++) {
            score += lanes[i].update(input, accuracy);
        }
        // Declare guardian outside the block to ensure its scope persists
        if (level == LEVEL_3) {
            if (!guardianSpawned) {
                // Create guardian only if it has not been spawned before
                guardian = new Guardian("guardian", GUARDIAN_X,
                        GUARDIAN_Y, this);
                guardianSpawned = true;
            }

            // Check if it's time to create a new enemy
            if (currFrame % FRAMES_PER_ENEMY == 0) {
                Enemy enemy = new Enemy("enemy", generateRandEnemyX(),
                        generateRandEnemyY());
                enemies.add(enemy);
            }
        }

        // Update the guardian if it has been spawned
        if (guardianSpawned && guardian != null) {
            guardian.update(input);
        }

        // Update other enemies
        updateEnemies(enemies, input);

        return score;
    }

    /**
     * This method checks if all lanes are finished
     * @return boolean This returns true if all lanes are finished
     */
    public boolean checkFinished() {
        for (int i = 0; i < numLanes; i++) {
            if (!lanes[i].isFinished()) {
                return false;
            }
        }
        return true;
    }

    private int generateRandEnemyX() {
        /* Generate random x value between ENEMY_X_Y_MIN (inclusive)
           and ENEMY_X_MAX (inclusive) */
        return ENEMY_X_Y_MIN + (int) (Math.random() * ((ENEMY_X_MAX -
                                                ENEMY_X_Y_MIN) + 1));
    }

    private int generateRandEnemyY() {
        /* Generate random y value between ENEMY_X_Y_MIN (inclusive)
            and ENEMY_Y_MAX (inclusive) */
        return ENEMY_X_Y_MIN + (int) (Math.random() * ((ENEMY_Y_MAX -
                                                ENEMY_X_Y_MIN) + 1));
    }

    private void updateEnemies(List<Enemy> enemies, Input input) {
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (enemy.isActive()) {
                enemy.update(input);
            } else {
                enemies.remove(i);
                i--;
            }
        }
    }

    private void readLanes (String[] splitText) {
        String laneType = splitText[1];
        int pos = Integer.parseInt(splitText[2]);
        Lane lane = new Lane(laneType, pos, this);
        lanes[numLanes++] = lane;

        // Store the x-coordinate based on lane type
        switch (laneType) {
            case "Down":
                downLaneX = pos;
                break;
            case "Up":
                upLaneX = pos;
                break;
            case "Right":
                rightLaneX = pos;
                break;
            case "Left":
                leftLaneX = pos;
                break;
        }
    }

    private void readNotes (String[] splitText) {
        String dir = splitText[0];
        Lane lane = null;
        for (int i = 0; i < numLanes; i++) {
            if (lanes[i].getType().equals(dir)) {
                lane = lanes[i];
            }
        }

        if (lane != null) {
            // Variable to store the note's x-coordinate
            int noteX = 0;

            // Determine note's x-coordinate based on lane type
            switch (dir) {
                case "Down":
                    noteX = downLaneX;
                    break;
                case "Up":
                    noteX = upLaneX;
                    break;
                case "Right":
                    noteX = rightLaneX;
                    break;
                case "Left":
                    noteX = leftLaneX;
                    break;
            }
            switch (splitText[1]) {
                case "Normal":
                    NormalLaneNote normalNote = new NormalNote(dir,
                            Integer.parseInt(splitText[2]), noteX, NORMAL_Y);
                    lane.addNormalLaneNote(normalNote);
                    break;
                case "Hold":
                    NormalLaneNote holdNote = new HoldNote(dir,
                            Integer.parseInt(splitText[2]), noteX, HOLD_Y);
                    lane.addNormalLaneNote(holdNote);
                    break;
                case "SpeedUp":
                    dir = "SpeedUp";
                    SpecialLaneNote speedUpNote = new SpeedUpNote(dir,
                            Integer.parseInt(splitText[2]), noteX, NORMAL_Y);
                    lane.addSpecialLaneNote(speedUpNote);
                    break;
                case "SlowDown":
                    dir = "SlowDown";
                    SpecialLaneNote slowDownNote = new SlowDownNote(dir,
                            Integer.parseInt(splitText[2]), noteX, NORMAL_Y);
                    lane.addSpecialLaneNote(slowDownNote);
                    break;
                case "DoubleScore":
                    dir = "2x"; // named '2x' in /res folder
                    SpecialLaneNote doubleScoreNote = new DoubleScoreNote(dir,
                            Integer.parseInt(splitText[2]), noteX, NORMAL_Y);
                    lane.addSpecialLaneNote(doubleScoreNote);
                    break;
                case "Bomb":
                    String bombLane = dir;
                    dir = "Bomb";
                    if (bombLane.equals("Special")) {
                        SpecialLaneNote bombNote = new BombNoteSpecialLane(dir,
                               Integer.parseInt(splitText[2]), noteX, NORMAL_Y);
                        lane.addSpecialLaneNote(bombNote);
                    }
                    else {
                        NormalLaneNote bombNote = new BombNoteNormalLane
                    (bombLane, Integer.parseInt(splitText[2]), noteX, NORMAL_Y);
                        lane.addNormalLaneNote(bombNote);
                    }
                    break;
            }
        }
    }

    /**
     * This method returns all active enemies
     * @return List<Enemy> This returns the ArrayList of enemies
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }
}