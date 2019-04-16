import java.util.Stack;

public class Player {
    private Room currentRoom;
    private Stack<Room> lastRooms;
    private Item item;

    public Player() {
        this.lastRooms = new Stack<>();
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Get the player item
     * @return The item, or null if the player doesn't have any item
     */
    public Item getItem() {
        return item;
    }

    /**
     * Make the player go to the last room he was in, if there is one
     * @return True if the player went back to another room, false if there was no room to get back to
     */
    public boolean goBack() {
        if(!lastRooms.isEmpty()){
            currentRoom = lastRooms.pop();
            return true;
        }
        return false;
    }

    public void goRoom(final Room room){
        lastRooms.push(currentRoom);
        currentRoom = room;
    }

    public void setItem(Item newItem) {
        this.item = newItem;
    }

    /**
     * Take the item from the specified item from the current room
     * @param item
     */
    public void takeItem(final Item item) {
        setItem(item);
        getCurrentRoom().getItems().remove(item);
    }
}
