
/**
 * A class with all the logic to make our game run
 */
public class Game {

    private UserInterface userInterface;
    private GameEngine gameEngine;

    /**
     * Create and initialize a new game
     */
    public Game() {
        this.gameEngine = new GameEngine();
        this.userInterface = new UserInterface(gameEngine);
        gameEngine.setGUI(userInterface);
    }

    public static void main(String[] args) {
        new Game();
    }
}
