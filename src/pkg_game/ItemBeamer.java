package pkg_game;

import pkg_game.pkg_room.Room;

public class ItemBeamer extends Item {

    private Room room;

    public ItemBeamer() {
        super("beamer", "The beamer", 1);
    }

    public boolean isCharged(){
        return room != null;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }
}
