 
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * A class representing a room
 */
public class Room
{
    private String aDescription;
    private HashMap<String,Room> aExits;
    
    /**
     * Create a new room
     * @param pDescription The room description
     */
    public Room(final String pDescription){
        this.aDescription = pDescription;
        this.aExits = new HashMap();
    }

    /**
     * Change the exit with the specified name/direction
     * @param pDirection The direction of the exit
     * @param pRoom The room which the exit is pointing to
     */
    public void setExit(final String pDirection,final Room pRoom){
        this.aExits.put(pDirection,pRoom);
    }
    
    /**
     * Get the room description
     * @return The room description
     */
    public String getDescription() {
        return this.aDescription;
    }
    
    /**
     * Get the exit with the specified name
     * @param pExitName The exit name
     * @return A Room, of null if there isn't any exit with the specified name
     */
    public Room getExit(String pExitName){
        return this.aExits.get(pExitName);
    }
    
    /**
     * Get a string representing all the available exits in the room
     * @return A String with the exits
     */
    public String getExitString(){        
        StringBuilder vString = new StringBuilder("Exits: ");
        for(String key : this.aExits.keySet()){
            vString.append(key);
            vString.append(' ');
        }
        return vString.toString();
    }
    
    /**
     * Return the long description of the room
     * @return The long description of this room (eg. 'You are in [...]. Exits: [...]
     */
    public String getLongDescription(){
        return "You are " + this.getDescription() + "\n" + this.getExitString();
    }
} // Room
