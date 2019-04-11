
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * A class representing a room
 */
public class Room {
    private String description;
    private HashMap<String, Room> exits;
    private String imageName;
    private Set<Item> items;

    /**
     * Create a new room
     *
     * @param description The room description
     * @param image The room image
     */
    public Room(final String description, final String image) {
        this.description = description;
        this.exits = new HashMap<>();
        this.imageName = image;
        this.items = new HashSet<>();
    }

    /**
     * Change the exit with the specified name/direction
     *
     * @param direction The direction of the exit
     * @param room      The room which the exit is pointing to
     */
    public void setExit(final String direction, final Room room) {
        this.exits.put(direction, room);
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
     * @return A Room, of null if there isn't any exit with the specified name
     */
    public Room getExit(String exitName) {
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

    public String getImageName() {
        return imageName;
    }

    /**
     * Return the long description of the room
     *
     * @return The long description of this room (eg. 'You are in [...]. Exits: [...]
     */
    public String getLongDescription() {
        String msg = "You are " + this.getDescription()
                + "\n" + this.getExitString();
        if(this.items.isEmpty()) {
            msg += "\nNo item here";
        }else{
            msg += "\nItems: ";
            for(Item item : this.items){
                msg += "\n  - " + item.getDescription();
            }
        }
        return msg;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public Set<Item> getItems() {
        return items;
    }
}
