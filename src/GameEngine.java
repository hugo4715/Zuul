import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
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
    private Map<String, ICommandHandler> commands;

    /**
     * Constructor for objects of class GameEngine
     */
    public GameEngine() {
        this.rooms = new HashMap<>();
        this.parser = new Parser();
        this.player = new Player();
        createRooms();
        registerCommands();

        player.goRoom(this.rooms.get("prison"));
    }

    private void registerCommands() {
        commands = new HashMap<>();

        //specify handler for each command
        commands.put("go", this::handleGo);
        commands.put("help", this::handleHelp);
        commands.put("back", this::handleBack);
        commands.put("drop", this::handleDrop);
        commands.put("eat", this::handleEat);
        commands.put("look", this::handleLook);
        commands.put("items", this::handleItems);
        commands.put("quit", this::handleQuit);
        commands.put("test", this::handleTest);
        commands.put("take", this::handleTake);
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
        String vDefaultImage = "default.jpg";

        Item itemBattery = new Item("Battery", "An old laptop battery", 9);
        Item itemScrewdriver = new Item("Screwdriver", "A small screwdriver, it looks quite old but could be used", 5);
        Item itemMagicCookie = new Item("MagicCookie", "A cookie in space, eating it might give you superpower", 1);

        Room vPrison = new Room("prison", "locked inside a small prison cell.\nThe power just went off and the door in front off you just openned, you can now get out of this cell. ", "img/prison.jpg");
        vPrison.getItems().addItem(itemBattery);
        vPrison.getItems().addItem(itemScrewdriver);
        vPrison.getItems().addItem(itemMagicCookie);

        this.rooms.put("prison", vPrison);

        Room vCorridor1 = new Room("corridor", "now inside a small corridor. \nYou can see a two doors, but there are some heavy creates in front of one, you will need to find something to move them. ", "img/corridor1.jpg");
        this.rooms.put("corridor1", vCorridor1);

        Room vCorridor2 = new Room("corridor", "now in another corridor, you can see two closed doors.", "img/corridor2.jpg");
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
        }else{
            ICommandHandler handler = commands.get(command.getCommandWord().toLowerCase());

            if (handler != null) {
                handler.handle(command);
            }
        }



    }

    // implementations of user commands:

    /**
     * Handle the help command
     */
    private void handleHelp(Command command) {
        printHelp();
    }

    /**
     * Handle the quit command
     */
    private void handleQuit(Command command) {
        if (command.hasSecondWord()) {
            gui.println("Quit what?");
        } else {
            gui.println("Thank you for playing.  Good bye.");
            gui.setInputEnabled(false);
        }
    }

    /**
     * Handle the items command
     * This command print the list of items the player currently has
     */
    private void handleItems(final Command command) {
        if (!player.getItems().isEmpty()) {
            StringBuilder sb = new StringBuilder("Your items: ");
            sb.append(player.getItems().getMessage());
            sb.append("\nTotal weight: ");
            sb.append(player.countWeight());
            sb.append("/");
            sb.append(player.getMaxWeight());
            gui.println(sb.toString());
        } else {
            gui.println("You don't have any items.");
        }
    }

    /**
     * Handle the drop command
     * Commands is:
     *   - drop <item name>
     */
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
                sb.append(player.getItems().getMessage());
                gui.println(sb.toString());
            }

        }
    }

    /**
     * Handle the take command
     * Command is:
     * - take <item name> (take the item with the specified name in the room)
     */
    private void handleTake(final Command command) {
        if (command.hasSecondWord()) {

            //search the specified item
            Item chosen = player.getCurrentRoom().getItems().getItem(command.getSecondWord());

            if (chosen != null) {
                if (player.canTake(chosen)) {
                    player.takeItem(chosen);
                    gui.println("You picked up a " + chosen.getName());
                } else {
                    gui.println("It's too heavy! You may need to drop some items.");
                }

            } else {
                gui.println("'" + command.getSecondWord() + "' is not an item name!");
            }
        } else {
            gui.println("Take what?");
        }
    }

    /**
     * Handle the test command
     * It reads the file specified as the first argument, and execute all the commands one after the other
     */
    private void handleTest(final Command command) {
        if (command.hasSecondWord()) {

            //search for the specified file
            File file = new File(command.getSecondWord() + ".txt");

            if (file.exists()) {
                try {
                    //read all lines and execute commands
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

    /**
     * Handle the back command
     */
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
     * Here we print a list of the available command words.
     */
    private void printHelp() {
        gui.println("You are lost. You are alone in the spaceship");
        gui.print("Your command words are: " + parser.getCommands());
    }

    /**
     * Handle the go command
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

    /**
     * Print the location of the player is the map
     */
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
    public void handleEat(final Command command) {

        if (command.hasSecondWord()) {
            Item chosen = player.getItems().getItem(command.getSecondWord());

            if (chosen != null) {

                //i'm using a switch here because we will need to add other eatable items
                switch (chosen.getName()) {
                    case "MagicCookie":
                        player.increaseMaxWeight(10);
                        player.getItems().removeItem(chosen);
                        gui.println("You ate a Magic Cookie! You are feeling a lot stronger now.");
                        break;
                    default:
                        gui.println("you can't eat " + chosen.getName());
                }
            } else {
                gui.println("You don't have " + command.getSecondWord());
            }
        } else {
            gui.println("What do you want to eat?");
        }
    }
}
