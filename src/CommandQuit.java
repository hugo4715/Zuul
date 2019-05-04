public class CommandQuit extends Command {
    protected CommandQuit(GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(Player player) {
        if (hasSecondWord()) {
            gui.println("Quit what?");
        } else {
            gui.println("Thank you for playing.  Good bye.");
            gui.setInputEnabled(false);
        }
    }
}
