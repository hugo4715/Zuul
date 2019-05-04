public class CommandDrop extends Command {
    protected CommandDrop(GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(Player player) {
        if (hasSecondWord()) {
            Item chosen = player.getItems().getItem(getSecondWord());
            if (chosen != null) {
                player.drop(chosen);
                gui.println("You dropped " + chosen.getName());
            } else {
                gui.println("You don't have " + getSecondWord());
            }
        } else {
            if (player.getItems().isEmpty()) {
                gui.println("You don't have anything to drop.");
            } else {
                gui.println("Drop what? " + player.getItems().getMessage());
            }

        }
    }
}
