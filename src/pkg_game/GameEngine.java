package pkg_game;

import pkg_game.pkg_command.Command;
import pkg_game.pkg_room.Door;
import pkg_game.pkg_room.Room;
import pkg_game.pkg_room.TransporterRoom;

import java.io.Serializable;
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
    private boolean isTesting;
    private Random random;

    /**
     * Constructor for objects of class pkg_game.GameEngine
     */
    public GameEngine() {
        this.isTesting = false;
        this.elapsedTime = 0;
        createRooms();
        this.parser = new Parser(this);
        registerTimer();
        this.player = new Player(this.rooms.get("prison"));
    }

    public void setRandomSeed(long seed){
        random.setSeed(seed);
    }

    public Random getRandom() {
        return random;
    }

    public UserInterface getGui() {
        return gui;
    }

    public boolean isTesting() {
        return isTesting;
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

    public Map<String, Room> getRooms() {
        return rooms;
    }

    public void setGUI(UserInterface userInterface) {
        gui = userInterface;
        parser.setGui(userInterface);
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
        this.rooms = new HashMap<>();
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

        Room transporter = new TransporterRoom("transporter", "a strange tele-transporter.", defaultImage, this.rooms.values(), random);

        Door door10 = new Door(transporter, secondaryCorridor);
        secondaryCorridor.setExit("south",door10);
        transporter.setExit("north", door10);

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

        if (command == null) {
            gui.println("I don't know what you mean...");
        }else{
            command.execute(player);
        }
    }

    public void setTesting(boolean testing) {
        isTesting = testing;
    }

    /**
     * Print out some help information.
     * Here we print a list of the available command words.
     */
    public void printHelp() {
        gui.println("You are lost. You are alone in the spaceship");
        gui.print("Your command words are: " + parser.getCommands());
    }

    /**
     * Print the location of the player is the map
     */
    public void printLocationInfo() {
        this.gui.println(player.getCurrentRoom().getLongDescription());
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
