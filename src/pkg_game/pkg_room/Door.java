package pkg_game.pkg_room;

import pkg_game.Item;

import java.io.Serializable;

public class Door implements Serializable {
    private Room front;
    private Room back;

    private boolean locked;
    private Item key;

    public Door(Room front, Room back) {
        this.front = front;
        this.back = back;
    }

    public Room getFront() {
        return front;
    }

    public Room getBack() {
        return back;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Item getKey() {
        return key;
    }

    public void setKey(Item key) {
        this.key = key;
    }

    /**
     * Get the room at the other side of this door
     * @param from Where you are standing
     * @return A room, or null
     */
    public Room getOtherSide(final Room from){
        if(from.equals(front))return back;
        if(from.equals(back))return front;
        return null;
    }
}
