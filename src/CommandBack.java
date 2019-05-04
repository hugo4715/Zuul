public class CommandBack extends Command {
    protected CommandBack(GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(Player player) {
        if (!player.goBack()) {
            gui.println("You can't go back");
        } else {
            Room currentRoom = player.getCurrentRoom();
            gui.println(currentRoom.getLongDescription());
            if (currentRoom.getImageName() != null)
                gui.showImage(currentRoom.getImageName());
        }
    }
}
