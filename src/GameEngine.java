import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class creates all rooms, creates the parser and starts
 * the game.  It also evaluates and executes the commands that
 * the parser returns.
 */
public class GameEngine {

    private Parser parser;
    private UserInterface gui;
    private Map<String, Room> rooms;
    private Player player;

    /**
     * Constructor for objects of class GameEngine
     */
    public GameEngine() {
        this.rooms = new HashMap<>();
        this.parser = new Parser();
        this.player = new Player();
        createRooms();

        player.goRoom(this.rooms.get("prison"));
    }

    public void setGUI(UserInterface userInterface) {
        gui = userInterface;
        printWelcome();
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome() {
        gui.print("\n");
        gui.println("Welcome to Lost In Space!");
        gui.println("This is a text-based spatial adventure game");
        gui.println("Type 'help' if you need help.");
        gui.print("\n");

        Room currentRoom = player.getCurrentRoom();
        gui.println(currentRoom.getLongDescription());
        gui.showImage(currentRoom.getImageName());
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms() {
        String vDefaultImage = "default.png";


        Item itemBattery = new Item("Battery", "An old laptop battery", 9);
        Item itemScrewdriver = new Item("Screwdriver", "A small screwdriver, it looks quite old but could be used", 5);

        Room vPrison = new Room("prison", "locked inside a small prison cell.\nThe power just went off and the door in front off you just openned, you can now get out of this cell. ", vDefaultImage);
        vPrison.getItems().addItem(itemBattery);
        vPrison.getItems().addItem(itemScrewdriver);

        this.rooms.put("prison", vPrison);

        Room vCorridor1 = new Room("corridor", "now inside a small corridor. \nYou can see a two doors, but there are some heavy creates in front of one, you will need to find something to move them. ", vDefaultImage);
        this.rooms.put("corridor1", vCorridor1);

        Room vCorridor2 = new Room("corridor", "now in another corridor, you can see two closed doors.", vDefaultImage);
        this.rooms.put("corridor2", vCorridor2);

        Room vCafeteria = new Room("cafeteria", "in inside what handleLook like a cafeteria, you can get some food supply here, \nbut right now, that's not your priority.", vDefaultImage);
        this.rooms.put("cafetaria", vCafeteria);

        Room vLabo = new Room("labo", "now inside a laboratory. \nIt's empty but you can see some strange animals floating inside an aquarium. You can see on a table something lighting up. From the papers on the desks, it's a gravity gun. ", vDefaultImage);
        this.rooms.put("labo", vLabo);

        vPrison.setExit("east", vCorridor1);
        vCorridor1.setExit("west", vPrison);

        vCorridor1.setExit("south", vCorridor2);
        vCorridor2.setExit("north", vCorridor1);

        vCorridor2.setExit("east", vCafeteria);
        vCafeteria.setExit("west", vCorridor2);

        vLabo.setExit("down", vCafeteria);
        vCafeteria.setExit("up", vLabo);
    }

    /**
     * Given a command, process (that is: execute) the command.
     * If this command ends the game, true is returned, otherwise false is
     * returned.
     */
    public void interpretCommand(final String commandLine) {
        gui.println(commandLine);
        Command command = parser.getCommand(commandLine);

        if (command.isUnknown()) {
            gui.println("I don't know what you mean...");
            return;
        }

        switch (command.getCommandWord()) {
            case "help":
                printHelp();
                break;
            case "go":
                handleGo(command);
                break;
            case "quit":
                if (command.hasSecondWord()) {
                    gui.println("Quit what?");
                } else {
                    endGame();
                }
                break;
            case "eat":
                handleEat(command);
                break;
            case "look":
                handleLook(command);
                break;
            case "back":
                handleBack(command);
                break;
            case "test":
                handleTest(command);
                break;
            case "take":
                handleTake(command);
                break;
            case "drop":
                handleDrop(command);
                break;
            case "items":
                handleItems();
                break;

            default:
                gui.println("I don't know what you mean...");

        }
    }

    // implementations of user commands:

    private void handleItems() {
        if(!player.getItems().isEmpty()){
            StringBuilder sb = new StringBuilder("You items: ");
            for (Item item : player.getItems().getAllItems()) {
                sb.append("\n  - ");
                sb.append(item.getName());
            }
            gui.println(sb.toString());
        }else{
            gui.println("You don't have any items.");
        }
    }

    private void handleDrop(final Command command) {

        if (command.hasSecondWord()) {
            Item chosen = player.getItems().getItem(command.getSecondWord());
            if (chosen != null) {
                player.drop(chosen);
                gui.println("You dropped " + chosen.getName());
            } else {
                gui.println("You don't have " + command.getSecondWord());
            }
        } else {
            if (player.getItems().isEmpty()) {
                gui.println("You don't have anything to drop.");
            } else {
                StringBuilder sb = new StringBuilder("Drop what? ");
                for (Item item : player.getItems().getAllItems()) {
                    sb.append("\n - ");
                    sb.append(item.getName());
                }
                gui.println(sb.toString());
            }

        }
    }

    /**
     * Handle the take command
     * Command can be either:
     * - take <number> (take the n item in the room)
     * - take <item name> (take the item with the specified name in the room)
     */
    private void handleTake(final Command command) {
        if (command.hasSecondWord()) {
            Item chosen = player.getCurrentRoom().getItems().getItem(command.getSecondWord());

            if (chosen != null) {
                if(player.canTake(chosen)){
                    player.takeItem(chosen);
                    gui.println("You picked up a " + chosen.getName());
                }else{
                    gui.println("It's too heavy! You may need to drop some items.");
                }

            } else {
                gui.println("'" + command.getSecondWord() + "' is not an item name!");
            }
        } else {
            gui.println("Take what?");
        }
    }

    private void handleTest(final Command command) {
        if (command.hasSecondWord()) {
            File file = new File(command.getSecondWord() + ".txt");

            if (file.exists()) {
                try {
                    Files.readAllLines(file.toPath()).forEach(this::interpretCommand);
                } catch (IOException e) {
                    e.printStackTrace();
                    gui.println("Could not read " + file.getName() + "!");
                }
            } else {
                gui.println("Unknown file " + file.getName());
            }

        } else {
            gui.println("Please provide a file name!");
        }
    }

    private void handleBack(final Command command) {
        if (!player.goBack()) {
            gui.println("There is no room to go back to!");
        } else {
            Room currentRoom = player.getCurrentRoom();
            gui.println(currentRoom.getLongDescription());
            if (currentRoom.getImageName() != null)
                gui.showImage(currentRoom.getImageName());
        }
    }

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the
     * command words.
     */
    private void printHelp() {
        gui.println("You are lost. You are alone in the spaceship");
        gui.print("Your command words are: " + parser.getCommands());
    }

    /**
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void handleGo(final Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            gui.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        Room currentRoom = player.getCurrentRoom();
        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null)
            gui.println("There is no door!");
        else {
            player.goRoom(nextRoom);
            gui.println(nextRoom.getLongDescription());
            if (nextRoom.getImageName() != null)
                gui.showImage(nextRoom.getImageName());
        }
    }

    private void printLocationInfo() {
        this.gui.println(player.getCurrentRoom().getLongDescription());
    }

    /**
     * Handle the handleLook command
     *
     * @param command The Command to handle
     */
    private void handleLook(final Command command) {
        this.printLocationInfo();
    }

    /**
     * Handle the handleEat command
     *
     * @param command Handle the handleEat command
     */
    private void handleEat(final Command command) {
        this.gui.println("You have eaten now and you are not hungry any more.");
    }


    private void endGame() {
        gui.println("Thank you for playing.  Good bye.");
        gui.setInputEnabled(false);
    }

}
