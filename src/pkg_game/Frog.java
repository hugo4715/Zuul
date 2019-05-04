package pkg_game;

import pkg_game.pkg_room.Room;

import java.io.Serializable;

public class Frog implements Serializable,Entity, EntityTalkable {
    private String name;
    private String text;

    private Room room;

    public Frog(Room room, String name, String text) {
        this.name = name;
        this.text = text;
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    @Override
    public Room getRoom() {
        return room;
    }
}
