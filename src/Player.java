import java.util.Stack;

public class Player {
    private Room currentRoom;
    private Stack<Room> lastRooms;
    private ItemList items;
    private int maxWeight;


    public Player() {
        this.lastRooms = new Stack<>();
        this.items = new ItemList();
        this.maxWeight = 10;
    }

    public Room getCurrentRoom() {
        return currentRoom;
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

    public ItemList getItems() {
        return items;
    }

    /**
     * Check if the player can take this item
     * @return True if the player can carryh this item, false if he can't because it's too heavy (he may need to drop some other items)
     */
    public boolean canTake(final Item item){
        return countWeight() + item.getWeight() <= maxWeight;
    }

    public int countWeight(){
        return items.totalWeight();
    }

    /**
     * Take the item from the specified item from the current room
     * @param item
     */
    public void takeItem(final Item item) {
        items.addItem(item);
        getCurrentRoom().getItems().removeItem(item);
    }

    public void drop(Item chosen) {
        items.removeItem(chosen);
        getCurrentRoom().getItems().addItem(chosen);
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void increaseMaxWeight(int amount) {
        this.maxWeight += amount;
    }
}
