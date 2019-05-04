public class CommandHelp extends Command {
    protected CommandHelp(GameEngine engine) {
        super(engine);
    }

    @Override
    public void execute(Player player) {
        engine.printHelp();
    }
}
