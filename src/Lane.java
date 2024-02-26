import bagel.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a class for the lanes which notes fall down
 */
public class Lane {
    private static final int LANE_Y = 384;
    private static final int TARGET_Y = 657;
    private final String type;
    private final Image image;
    private final List<NormalLaneNote> normalLaneNotes = new ArrayList<>();
    private final List<SpecialLaneNote> specialLaneNotes = new ArrayList<>();
    private Keys relevantKey;
    private final int location;
    private int currNormalLaneNote = 0;
    private int currSpecialLaneNote = 0;
    private final String SPECIAL_LANE = "Special";
    private final static String BOMB_NOTE = "Bomb";
    private final Level level;

    /**
     * This method is the class constructor
     * @param dir This is the type of lane
     * @param location This is the y-coordinate of the lane
     * @param level This is the current level
     */
    public Lane(String dir, int location, Level level) {
        this.type = dir;
        this.location = location;
        this.level = level;
        image = new Image("res/lane" + dir + ".png");
        switch (dir) {
            case "Left":
                relevantKey = Keys.LEFT;
                break;
            case "Right":
                relevantKey = Keys.RIGHT;
                break;
            case "Up":
                relevantKey = Keys.UP;
                break;
            case "Down":
                relevantKey = Keys.DOWN;
                break;
            case "Special":
                relevantKey = Keys.SPACE;
                break;
        }
    }

    public String getType() {
        return type;
    }

    /**
     * This method updates all the notes in the lane
     * @param input This is the keyboard input entered by the user
     * @param accuracy This is the Accuracy class
     * @return int This returns the score added/deducted (if any)
     */
    public int update(Input input, Accuracy accuracy) {
        draw();

        for (int i = currNormalLaneNote; i < normalLaneNotes.size(); i++) {
            checkNormalNoteCollision(i);
            normalLaneNotes.get(i).update();
        }

        for (int i = currSpecialLaneNote; i < specialLaneNotes.size(); i++) {
            specialLaneNotes.get(i).update();
        }

        if (currNormalLaneNote < normalLaneNotes.size()) {
            return normalLaneScore(input, accuracy);
        }

        if (currSpecialLaneNote < specialLaneNotes.size()) {
            return specialLaneScore(input, accuracy);
        }

        return Accuracy.NOT_SCORED;
    }

    public void addNormalLaneNote(NormalLaneNote nln) {
        normalLaneNotes.add(nln);
    }

    public void addSpecialLaneNote(SpecialLaneNote sln) {
        specialLaneNotes.add(sln);
    }

    /**
     *This method checks if all notes are finished
     * @return boolean This returns true if all notes are finished
     */
    public boolean isFinished() {

        for (NormalLaneNote normalLaneNote : normalLaneNotes) {
            if (!normalLaneNote.isCompleted()) {
                return false;
            }
        }

        for (SpecialLaneNote specialLaneNote : specialLaneNotes) {
            if (!specialLaneNote.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method draws the lane and the notes
     */
    public void draw() {
        image.draw(location, LANE_Y);

        for (int i = currNormalLaneNote; i < normalLaneNotes.size(); i++) {
            normalLaneNotes.get(i).draw(location);
        }

        for (int i = currSpecialLaneNote; i < specialLaneNotes.size(); i++) {
            specialLaneNotes.get(i).draw(location);
        }
    }

    /**
     * explodes all notes in the exploded bomb's lane
     */
    private void explodeNotes (String lane) {
        if (lane.equals(SPECIAL_LANE)) {
            for (int i = specialLaneNotes.size() - 1; i >
                                currSpecialLaneNote; i--) {
                if (specialLaneNotes.get(i).isActive()) {
                    specialLaneNotes.get(i).deactivate();
                }
            }
        }
        else {
            for (int i = normalLaneNotes.size() - 1; i > currNormalLaneNote; i--) {
                if (lane.equals(normalLaneNotes.get(i).getNoteType()) &&
                                    normalLaneNotes.get(i).isActive()) {
                    normalLaneNotes.get(i).deactivate();
                }
            }
        }
    }

    /**
     * checks if normal note collides with a possible enemy
     */
    private void checkNormalNoteCollision (int index) {
        // deactivates normal note if it has collided with an enemy
        if (normalLaneNotes.get(index) instanceof NormalNote) {
            List<Enemy> enemies = level.getEnemies();
            for (Enemy enemy : enemies) {
                if (normalLaneNotes.get(index).isActive()) {
                    if (((NormalNote) normalLaneNotes.get(index)).
                                        isCollidingWith(enemy)) {
                        normalLaneNotes.get(index).deactivate();
                    }
                }
            }
        }
    }

    private int normalLaneScore (Input input, Accuracy accuracy) {
        int score = normalLaneNotes.get(currNormalLaneNote).
                processInput(input, accuracy, TARGET_Y, relevantKey);

        // checks if bomb note has exploded
        if (normalLaneNotes.get(currNormalLaneNote).
                    getNoteType().equals(BOMB_NOTE)) {
            if (Accuracy.bombExploded) {
                String bombLane = formatLaneTypeString(relevantKey.name());
                explodeNotes(bombLane);
            }
        }
        if (normalLaneNotes.get(currNormalLaneNote).isCompleted()) {
            currNormalLaneNote++;
        }
        return score;
    }

    private int specialLaneScore (Input input, Accuracy accuracy) {
        int score = specialLaneNotes.get(currSpecialLaneNote).
                processInput(input, accuracy, TARGET_Y, relevantKey);

        // checks if bomb note has exploded
        if (specialLaneNotes.get(currSpecialLaneNote).
                        getNoteType().equals(BOMB_NOTE)) {
            if (Accuracy.bombExploded) {
                explodeNotes(SPECIAL_LANE);
            }
        }
        if (specialLaneNotes.get(currSpecialLaneNote).isCompleted()) {
            currSpecialLaneNote++;
        }
        return score;
    }

    /**
     * formats input string to start with capital letter
     * and end with lowercase letters
     */
    private String formatLaneTypeString(String input) {
        return input.charAt(0) + input.substring(1).toLowerCase();
    }

}