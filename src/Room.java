
import java.util.*;

/**
 * A class representing a room
 */
public class Room {
    private String name;
    private String description;
    private HashMap<String, Room> exits;
    private String imageName;
    private List<Item> items;

    /**
     * Create a new room
     *
     * @param name
     * @param description The room description
     * @param image The room image
     */
    public Room(String name, final String description, final String image) {
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        this.imageName = image;
        this.items = new ArrayList<>();
    }

    public String getName() {
        return name;
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
        StringBuilder msg = new StringBuilder("You are " + this.getDescription()
                + "\n" + this.getExitString());
        if(this.items.isEmpty()) {
            msg.append("\nNo item here");
        }else{
            msg.append("\nItems: ");
            for(int i = 0; i < items.size();i++){
                Item item = items.get(i);
                msg.append("\n  [");
                msg.append(i);
                msg.append("] ");
                msg.append(item.getLongDescription());
            }
        }
        return msg.toString();
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }
}
