import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.List;

/**
 * This is an abstract class for notes
 */
public abstract class Note {
    private final Image image;
    private final int appearanceFrame;
    protected static int speed = 2;
    protected int x;
    protected int y;
    private boolean active = false;
    private boolean completed = false;
    private final String type;
    private final static int NORMAL_Y = 100;

    /**
     *
     * @param dir This is the note name
     * @param appearanceFrame This is the frame the note appears from
     * @param x This is the x-coordinate of the note
     * @param y This is the initial y-coordinate of the note
     */
    public Note(String dir, int appearanceFrame, int x, int y) {
        this.x = x;
        this.y = y;
        this.type = dir;
        this.appearanceFrame = appearanceFrame;
        if (y == NORMAL_Y) {
            image = new Image("res/note" + dir + ".png");
        }
        else {
            image = new Image("res/holdNote" + dir + ".png");
        }
    }

    /**
     * This method returns if note is active or not
     * @return boolean This returns true of note is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * This method returns if note is completed or not
     * @return boolean This returns true if note is completed
     */
    public boolean isCompleted() { return completed; }

    /**
     * This method returns the note type (or lane if normal lane note)
     * @return String This returns true if note is completed
     */
    public String getNoteType() { return type; }

    /**
     * This method deactivates the note
     */
    public void deactivate() {
        active = false;
        completed = true;
    }

    /**
     * This method updates all the notes in the lane
     */
    public void update() {
        if (active) {
            y += speed;
        }

        if (ShadowDance.getCurrFrame() >= appearanceFrame && !completed) {
            active = true;
        }
    }

    /**
     * This method draws the note given its coordinates
     * @param x This is the x-coordinate of the note
     */
    public void draw(int x) {
        if (active) {
            image.draw(x, y);
        }
    }

    /**
     * This abstract method processes the input of the note
     * @param input This is the keyboard input entered by the user
     * @param accuracy This is the Accuracy class
     * @param targetY This is the target y-coordinate
     * @param relevantKey This is the keyboard key needing
     *                    to be pressed to activate the note
     * @return int This returns the score added or deducted
     *         from the note (if any)
     */
    public abstract int processInput(Input input, Accuracy accuracy,
                                     int targetY, Keys relevantKey);
}