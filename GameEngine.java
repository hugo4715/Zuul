import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 *  This class is part of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.
 * 
 *  This class creates all rooms, creates the parser and starts
 *  the game.  It also evaluates and executes the commands that 
 *  the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0 (Jan 2003)
 */
public class GameEngine {

    private Parser parser;
    private Room currentRoom;
    private Stack<Room> lastRooms;
    private UserInterface gui;
    private Map<String,Room> rooms;

    /**
     * Constructor for objects of class GameEngine
     */
    public GameEngine(){
        this.rooms = new HashMap<>();
        parser = new Parser();
        this.lastRooms = new Stack<>();
        createRooms();
    }

    public void setGUI(UserInterface userInterface){
        gui = userInterface;
        printWelcome();
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome(){
        gui.print("\n");
        gui.println("Welcome to the World of Zuul!");
        gui.println("World of Zuul is a new, incredibly boring adventure game.");
        gui.println("Type 'help' if you need help.");
        gui.print("\n");
        gui.println(currentRoom.getLongDescription());
        gui.showImage(currentRoom.getImageName());
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms(){
        String vDefaultImage = "default.png";


        Item itemBattery = new Item("battery",1);
        Item itemScrewdriver = new Item("screwdriver",1);

        Room vPrison = new Room("locked inside a small prison cell.\nThe power just went off and the door in front off you just openned, you can now get out of this cell. ",vDefaultImage);
        vPrison.addItem(itemBattery);
        vPrison.addItem(itemScrewdriver);

        this.rooms.put("prison",vPrison);

        Room vCorridor1 = new Room("now inside a small corridor. \nYou can see a two doors, but there are some heavy creates in front of one, you will need to find something to move them. ",vDefaultImage);
        this.rooms.put("corridor1",vCorridor1);

        Room vCorridor2 = new Room("now in another corridor, you can see two closed doors.",vDefaultImage);
        this.rooms.put("corridor2",vCorridor2);

        Room vCafeteria = new Room("in inside what look like a cafeteria, you can get some food supply here, \nbut right now, that's not your priority.",vDefaultImage);
        this.rooms.put("cafetaria",vCafeteria);

        Room vLabo = new Room("now inside a laboratory. \nIt's empty but you can see some strange animals floating inside an aquarium. You can see on a table something lighting up. From the papers on the desks, it's a gravity gun. ",vDefaultImage);
        this.rooms.put("labo",vLabo);

        vPrison.setExit("east", vCorridor1);
        vCorridor1.setExit("west",vPrison);

        vCorridor1.setExit("south", vCorridor2);
        vCorridor2.setExit("north", vCorridor1);

        vCorridor2.setExit("east", vCafeteria);
        vCafeteria.setExit("west", vCorridor2);

        vLabo.setExit("down",vCafeteria);
        vCafeteria.setExit("up", vLabo);

        this.currentRoom = vPrison;
    }

    /**
     * Given a command, process (that is: execute) the command.
     * If this command ends the game, true is returned, otherwise false is
     * returned.
     */
    public void interpretCommand(final String commandLine) {
        gui.println(commandLine);
        Command command = parser.getCommand(commandLine);

        if(command.isUnknown()) {
            gui.println("I don't know what you mean...");
            return;
        }

        switch (command.getCommandWord()){
            case "help":
                printHelp();
                break;
            case "go":
                goRoom(command);
                break;
            case "quit":
                if(command.hasSecondWord()) {
                    gui.println("Quit what?");
                } else {
                    endGame();
                }
                break;
            case "eat":
                eat(command);
                break;
            case "look":
                look(command);
                break;
            case "back":
                goBack(command);
                break;
            case "test":
                test(command);
                break;
            default:
                gui.println("I don't know what you mean...");

        }
    }

    // implementations of user commands:


    private void test(Command command) {
        if(command.hasSecondWord()){
            File file = new File(command.getSecondWord() + ".txt");

            if(file.exists()){
                try {
                    Files.readAllLines(file.toPath()).forEach(this::interpretCommand);
                } catch (IOException e) {
                    e.printStackTrace();
                    gui.println("Could not read " + file.getName() + "!");
                }
            }else{
                gui.println("Unknown file " + file.getName());
            }

        }else{
            gui.println("Please provide a file name!");
        }
    }

    private void goBack(Command command) {
        if(lastRooms.isEmpty()){
            gui.println("There is no room to go back to!");
        }else{
            currentRoom = lastRooms.pop();
            gui.println(currentRoom.getLongDescription());
            if(currentRoom.getImageName() != null)
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
    private void goRoom(final Command command) {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            gui.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null)
            gui.println("There is no door!");
        else {
            lastRooms.push(currentRoom);
            currentRoom = nextRoom;
            gui.println(currentRoom.getLongDescription());
            if(currentRoom.getImageName() != null)
                gui.showImage(currentRoom.getImageName());
        }
    }

    private void printLocationInfo(){
        this.gui.println(this.currentRoom.getLongDescription());
    }

    /**
     * Handle the look command
     * @param command The Command to handle
     */
    private void look(final Command command){
        this.printLocationInfo();
    }

    /**
     * Handle the eat command
     * @param command Handle the eat command
     */
    private void eat(final Command command){
        this.gui.println("You have eaten now and you are not hungry any more.");
    }


    private void endGame(){
        gui.println("Thank you for playing.  Good bye.");
        gui.setInputEnabled(false);
    }

}
