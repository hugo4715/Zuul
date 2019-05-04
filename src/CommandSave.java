import java.io.File;
import java.io.IOException;

public class CommandSave extends Command {
    protected CommandSave(GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(Player player) {
        if(!hasSecondWord()){
            gui.println("Please specify the name of the save.");
            return;
        }
        File file = new File("saves" + File.separator + getSecondWord() + ".save");
        try {
            Game.getGame().save(file);
            gui.println("Saved game successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            gui.println("Could not save file: " + e.getMessage());
        }
    }
}
