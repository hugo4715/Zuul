import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * This class creates all rooms, creates the parser and starts
 * the game.  It also evaluates and executes the commands that
 * the parser returns.
 */
public class GameEngine implements Serializable {
    private static final int TIME_LIMIT = 20;//the number of minutes the player has before the game is over

    private Parser parser;
    private transient UserInterface gui;
    private Map<String, Room> rooms;
    private Player player;
    private transient Map<String, ICommandHandler> commands;
    private int elapsedTime;//the number of minute elapsed

    /**
     * Constructor for objects of class GameEngine
     */
    public GameEngine() {
        this.rooms = new HashMap<>();
        this.parser = new Parser();
        this.elapsedTime = 0;
        createRooms();
        this.player = new Player(this.rooms.get("prison"));
        registerCommands();
        registerTimer();
    }

    /**
     * Schedule the timer task to count the time left
     */
    private void registerTimer() {
        Timer timer = new Timer();
        TimeLimitTask timeLimitTask = new TimeLimitTask();

        long minuteMs = TimeUnit.MINUTES.toMillis(1);
        timer.scheduleAtFixedRate(timeLimitTask,minuteMs, minuteMs);
    }

    public void registerCommands() {
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
        commands.put("save",this::handleSave);
        commands.put("load", this::handleLoad);
        commands.put("use",this::handleUse);
        commands.put("fire",this::handleFire);
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
        String defaultImage = "default.jpg";

        Item itemBattery = new Item("Battery", "An old laptop battery", 9);
        Item itemMagicCookie = new Item("MagicCookie", "A cookie, eating it might give you superpowers", 1);
        Item itemBeamer = new Item("Beamer", "The beamer", 1);
        Item itemScrewdriver = new Item("Screwdriver", "A small screwdriver, it looks quite old but could be used", 5);

        Room prison = new Room("prison", "locked inside a small prison cell.\nThe power just went off and the door in front off you just openned, you can now get out of this cell. ", "img/prison.jpg");
        prison.getItems().addItem(itemBattery);
        prison.getItems().addItem(itemMagicCookie);
        this.rooms.put("prison", prison);

        Room mainCorridor1 = new Room("corridor", "now inside a small corridor. \nYou can see a two doors, but there are some heavy creates in front of one, you will need to find something to move them. ", "img/corridor1.jpg");
        mainCorridor1.getItems().addItem(itemBeamer);
        this.rooms.put("corridor1", mainCorridor1);

        Room secondaryCorridor = new Room("corridor", "now in another corridor, you can see two closed doors.", "img/corridor2.jpg");
        this.rooms.put("corridor2", secondaryCorridor);

        Room cafeteria = new Room("cafeteria", "in inside what handleLook like a cafeteria, you can get some food supply here, \nbut right now, that's not your priority.", defaultImage);
        this.rooms.put("cafetaria", cafeteria);

        Room laboratory = new Room("labo", "now inside a laboratory. \nIt's empty but you can see some strange animals floating inside an aquarium. You can see on a table something lighting up. From the papers on the desks, it's a gravity gun. ", defaultImage);
        this.rooms.put("labo", laboratory);

        Room engineRoom = new Room("engine", "now inside the engine room, it's really loud.", defaultImage);
        engineRoom.getItems().addItem(itemScrewdriver);
        this.rooms.put("engine", engineRoom);

        Room mainCorridor2 = new Room("corridor","now inside a corridor.",defaultImage);
        this.rooms.put("corridor3",mainCorridor2);

        Room meeting = new Room("meeting","now inside a meeting room with a dozen of seats.",defaultImage);
        this.rooms.put("meeting", meeting);

        Room escapePods = new Room("escape","now inside the escape pods room! \nYou just run to the last pod available. \nYou hear the flames burning the ship down and blast of into space, you're safe. \nYOU WON THE GAME!",defaultImage);
        escapePods.setEndGame(true);
        this.rooms.put("escape",escapePods);

        Room cockpit = new Room("cockpit", "now inside the ship's cockpit. You can see that the ship if really starting to break down to pieces. You better find your way out quickly.",defaultImage);
        this.rooms.put("cockpit",cockpit);

        Door door1 = new Door(engineRoom,secondaryCorridor);
        engineRoom.setExit("east",door1);
        secondaryCorridor.setExit("west",door1);

        Door door2 = new Door(cafeteria,secondaryCorridor);
        cafeteria.setExit("up",door1);
        secondaryCorridor.setExit("down",door1);

        Door door3 = new Door(secondaryCorridor,mainCorridor1);
        mainCorridor1.setExit("south", door3);
        secondaryCorridor.setExit("north",door3);

        Door door4 = new Door(prison,mainCorridor1);
        prison.setExit("east",door4);
        mainCorridor1.setExit("west",door4);

        Door door5 = new Door(mainCorridor1,laboratory);
        laboratory.setExit("west",door5);
        mainCorridor1.setExit("east",door5);

        Door door6 = new Door(mainCorridor1,mainCorridor2);
        door6.setLocked(true);
        door6.setKey(itemScrewdriver);//TODO
        mainCorridor1.setExit("north",door6);
        mainCorridor2.setExit("south",door6);

        Door door7 = new Door(escapePods,mainCorridor2);
        mainCorridor2.setExit("west", door7);

        Door door8 = new Door(cockpit,mainCorridor2);
        cockpit.setExit("south",door8);
        mainCorridor2.setExit("north",door8);

        Door door9 = new Door(mainCorridor2,meeting);
        meeting.setExit("west",door9);
        mainCorridor2.setExit("east",door9);
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

    private void handleUse(Command command){
        if(!command.hasSecondWord()){
            gui.println("Use what?");
            return;
        }

        Item item = player.getItems().getItem(command.getSecondWord());
        if(item != null){

            //it's a switch so we can add more usable stuff later
            switch (item.getName()){
                case "Beamer":
                    player.setBeamerTarget(player.getCurrentRoom());
                    gui.println("Your beamer is charged");
                    break;
                    default:
                        gui.println("You can't use " + item.getName());
            }
        }else{
            gui.println("You don't have " + command.getSecondWord());
        }
    }


    private void handleFire(Command command){
        if(!command.hasSecondWord()){
            gui.println("Fire what?");
            return;
        }

        Item item = player.getItems().getItem(command.getSecondWord());
        if(item != null){
            //it's a switch so we can add more firable stuff later
            switch (item.getName()){
                case "Beamer":
                    Room target = player.getBeamerTarget();
                    if(target != null){
                        player.setCurrentRoom(player.getBeamerTarget());
                        player.setBeamerTarget(null);
                        gui.println("You just teleported to another room!");
                        printLocationInfo();
                    }else{
                        gui.println("You beamer isn't charged yet");
                    }

                    break;
                default:
                    gui.println("You can't fire " + item.getName());
            }
        }else{
            gui.println("You don't have " + command.getSecondWord());
        }
    }

    private void handleLoad(Command command){
        if(!command.hasSecondWord()){
            gui.println("Please specify the name of the save.");
            return;
        }
        File file = new File("saves" + File.separator + command.getSecondWord() + ".save");
        try {
            Game.getGame().load(file);
            gui.println("Loaded game successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            gui.println("Could not load file: " + e.getMessage());
        }
    }

    private void handleSave(Command command){
        if(!command.hasSecondWord()){
            gui.println("Please specify the name of the save.");
            return;
        }
        File file = new File("saves" + File.separator + command.getSecondWord() + ".save");
        try {
            Game.getGame().save(file);
            gui.println("Saved game successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            gui.println("Could not save file: " + e.getMessage());
        }
    }

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
            gui.println("You can't go back");
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
        Door door = currentRoom.getExit(direction);

        if (door == null)
            gui.println("There is no door!");
        else {

            if(door.isLocked()){
                if(door.getKey() == null){
                    gui.println("This door is locked.");
                }else{
                    if(player.getItems().contains(door.getKey())){
                        door.setLocked(false);
                        gui.println("You unlocked the door using " + door.getKey().getName());
                    }else{
                        gui.println("This door is locked, you need '" + door.getKey().getName() +"' to open it.");
                    }
                }
                return;
            }

            //get the room at the other side of this door
            Room nextRoom = door.getOtherSide(player.getCurrentRoom());

            if(nextRoom == null){
                gui.println("This door cannot be openned from this side!");
                return;
            }

            player.goRoom(nextRoom);
            gui.println(nextRoom.getLongDescription());

            if (nextRoom.getImageName() != null) {
                gui.showImage(nextRoom.getImageName());
            }
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

    private class TimeLimitTask extends TimerTask{
        @Override
        public void run() {
            elapsedTime++;
            gui.println("You have " + (TIME_LIMIT-elapsedTime) + " minutes left.");

            if(elapsedTime > TIME_LIMIT){
                gui.println("You were too slow! \nThe ship just exploded, but you were still inside... GAME OVER");
                gui.setInputEnabled(false);
                this.cancel();
            }
        }
    }
}
