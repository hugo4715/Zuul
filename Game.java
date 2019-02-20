 
/**
 * A class with all the logic to make our game run
 */
public class Game
{
    private Room aCurrentRoom;
    private Parser aParser;

    /**
     * Create a new game
     */
    public Game() {
        this.aParser = new Parser();
        this.createRooms();
    }

    /**
     * Play the game and blocks while the game isn't finished
     */
    public void play(){
        this.printWelcome();
        boolean vFinished = false;

        while(!vFinished){
            Command vCommand = aParser.getCommand();
           
            vFinished = this.processCommand(vCommand);
        }

        System.out.println("Thank you for playing.  Good bye.");
    }
    
    /**
     * Create all the rooms and set the exits
     */
    private void createRooms(){
        Room vPrison = new Room("locked inside a small prison cell.\nThe power just went off and the door in front off you just openned, you can now get out of this cell. ");        
        Room vCorridor1 = new Room("now inside a small corridor. \nYou can see a two doors, but there are some heavy creates in front of one, you will need to find something to move them. ");
        Room vCorridor2 = new Room("now in another corridor, you can see two closed doors.");
        Room vCafeteria = new Room("in inside what look like a cafeteria, you can get some food supply here, \nbut right now, that's not your priority.");
        Room vLabo = new Room("now inside a laboratory. \nIt's empty but you can see some strange animals floating inside an aquarium. You can see on a table something lighting up. From the papers on the desks, it's a gravity gun. ");
        
        vPrison.setExit("east", vCorridor1);
        vCorridor1.setExit("west",vPrison);
        
        vCorridor1.setExit("south", vCorridor2);
        vCorridor2.setExit("north", vCorridor1);
        
        vCorridor2.setExit("east", vCafeteria);
        vCafeteria.setExit("west", vCorridor2);
        
        vLabo.setExit("down",vCafeteria);

        this.aCurrentRoom = vPrison;
    }

    /**
     * Print the welcome message to the room
     */
    private void printWelcome(){
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("Type 'help' if you need help.");
        System.out.println();

        this.printLocationInfo();
    }

    /**
     * Print the current location to the screen
     */
    private void printLocationInfo(){
        System.out.println(this.aCurrentRoom.getLongDescription());
    }

    /**
     * Print the help to the screen
     */
    private void printHelp(){
        System.out.println("You are lost. You are alone.");
        System.out.println("You wander around at the university.");
        System.out.println();
        System.out.println("Your command words are: ");
        this.aParser.showCommands(); 
    }
    
    public void eat(final Command pCommand){
        System.out.println("You have eaten now and you are not hungry any more.");
    }

    /**
     * Handle the quit command
     * @param pCommand The command to handle
     */
    private boolean quit(final Command pCommand){
        if(pCommand.hasSecondWord()){
            System.out.println("Quit what ?");
            return false;
        }
        return true;
    }

    /**
     * Process a command and dispatch it to the appropriated method
     * @param pCommand The command to process
     */
    private boolean processCommand(final Command pCommand){
        if(pCommand == null || pCommand.getCommandWord() == null){
            System.out.println("I don't know what you mean...");
            this.printHelp();
            return false;
        }
        switch (pCommand.getCommandWord()){
            case "help":
                printHelp();
                break;
            case "go":
                goRoom(pCommand);
                break;
            case "quit":
                return quit(pCommand);
            case "look":
                look(pCommand);    
                break;
            case "eat":
                eat(pCommand);
                break;
            default:
                System.out.println("I don't know what you mean...");
        }
        return false;
    }

    public void look(final Command pCommand){
        this.printLocationInfo();
    }

    /**
     * Handle the 'go' command
     * @param pCommand The command to handle
     */
    public void goRoom(final Command pCommand){
        if(!pCommand.hasSecondWord()){
            System.out.println("Go Where ?");
            return;
        }
        
        String vDirection = pCommand.getSecondWord();
        Room vNextRoom = this.aCurrentRoom.getExit(vDirection);   

        if(vNextRoom == null){
            System.out.println("Unknown direction !");
        }else{
            this.aCurrentRoom = vNextRoom;
            this.printLocationInfo();
        }
    }
} // Game
