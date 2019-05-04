package pkg_game.pkg_room;

import pkg_game.Game;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.Set;

public class TransporterRoom extends Room {

    private Room[] rooms;
    /**
     * Create a new transporter room
     * @param rooms The rooms where this transporter can get to
     */
    public TransporterRoom(String name, String description, String image, Collection<Room> rooms) {
        super(name, description, image);
        this.rooms = rooms.toArray(new Room[0]);
    }

    @Override
    public Door getExit(String exitName) {
        Random random = Game.getGame().getRandom();
        return new Door(this,rooms[random.nextInt(rooms.length)]);
    }
}
