import bagel.*;

/**
 * Class for normal lane notes
 */
public abstract class NormalLaneNote extends Note {

    /**
     * This method is the constructor class
     * @param dir This is the name/type of the note
     * @param appearanceFrame This is the frame the note appears from
     * @param x This is the x-coordinate of the note
     * @param y This is the initial y-coordinate of the note
     */
    public NormalLaneNote(String dir, int appearanceFrame, int x, int y) {
        super(dir, appearanceFrame, x, y);
    }

    @Override
    public abstract int processInput(Input input, Accuracy accuracy,
                                     int targetY, Keys relevantKey);
}