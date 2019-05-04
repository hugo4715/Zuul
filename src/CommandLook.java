public class CommandLook extends Command {
    protected CommandLook(GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(Player player) {
        engine.printLocationInfo();
    }
}
