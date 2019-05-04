
import java.io.Serializable;
import java.util.*;

/**
 * A class representing a room
 */
public class Room implements Serializable {
    private String name;
    private String description;
    private HashMap<String, Door> exits;
    private String imageName;
    private ItemList items;
    private boolean endGame;

    /**
     * Create a new room
     *
     * @param name
     * @param description The room description
     * @param image The room image
     */
    public Room(final String name, final String description, final String image) {
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        this.imageName = image;
        this.items = new ItemList();
    }

    /**
     * Set if the room should end the game when the player steps in it
     * You can set that for example for traps or the end rooms
     * @param endGame True if the room is the end of the game
     */
    public void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }

    /**
     * Check if the room is an end game room
     */
    public boolean isEndGame() {
        return endGame;
    }

    /**
     * Get the name of the rooms
     */
    public String getName() {
        return name;
    }

    /**
     * Change the door with the specified name/direction
     *
     * @param direction The direction of the exit
     * @param door      The door which the exit is pointing to
     */
    public void setExit(final String direction, final Door door) {
        this.exits.put(direction, door);
    }

    /**
     * Get the room description
     *
     * @return The room description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Get the exit with the specified name
     *
     * @param exitName The exit name
     * @return A Door, of null if there isn't any exit with the specified name
     */
    public Door getExit(final String exitName) {
        return this.exits.get(exitName);
    }

    /**
     * Get a string representing all the available exits in the room
     *
     * @return A String with the exits
     */
    public String getExitString() {
        StringBuilder vString = new StringBuilder("Exits: ");
        for (String key : this.exits.keySet()) {
            vString.append(key);
            vString.append(' ');
        }
        return vString.toString();
    }

    /**
     * Get the image name
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * Return the long description of the room
     *
     * @return The long description of this room (eg. 'You are in [...]. Exits: [...]
     */
    public String getLongDescription() {
        StringBuilder msg = new StringBuilder("You are " + this.getDescription()
                + "\n" + this.getExitString());
        if(this.items.isEmpty()) {
            msg.append("\nNo item here");
        }else{
            msg.append("\nItems: ");
            msg.append(items.getMessage());
        }
        return msg.toString();
    }

    /**
     * Get the items that are in this room
     * @return ItemList The list of items
     */
    public ItemList getItems() {
        return items;
    }

    /**
     * Check if this room can lead to the specified room
     * @return True if there is a door leading to the specified room
     */
    public boolean leadsTo(final Room room){
        for(Door door : this.exits.values()){
            if(room.equals(door.getBack()) || room.equals(door.getFront())){
                return true;
            }
        }
        return false;
    }

}
