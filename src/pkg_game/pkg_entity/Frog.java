package pkg_game.pkg_entity;

import pkg_game.pkg_room.Door;
import pkg_game.pkg_room.Room;

import java.io.Serializable;

public class Frog implements Serializable,Entity, EntityTalkable, EntityMoving {
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

    @Override
    public void move() {
        Door door = room.getRandomExit();
        if(door != null && !door.isLocked()) {
            room = door.getOtherSide(room);
            System.out.println(getName() + "moved to " + room.getName());//debug
        }
    }

    @Override
    public double moveChance() {
        return 0.25;
    }
}
