import bagel.*;

/**
 * Class for special lane notes
 */
public abstract class SpecialLaneNote extends Note {
    public SpecialLaneNote(String dir, int appearanceFrame, int x, int y) {
        super(dir, appearanceFrame, x, y);
    }

    @Override
    public abstract int processInput(Input input, Accuracy accuracy,
                                     int targetY, Keys relevantKey);
}