package pkg_game.pkg_room;

import pkg_game.GameEngine;
import java.util.Collection;
import java.util.Random;

public class TransporterRoom extends Room {

    private Room[] rooms;
    /**
     * Create a new transporter room
     * @param rooms The rooms where this transporter can get to
     */
    public TransporterRoom(final GameEngine engine, final String name, final String description, final String image, final Collection<Room> rooms) {
        super(engine, name, description, image);
        this.rooms = rooms.toArray(new Room[0]);
    }

    @Override
    public Door getExit(final String exitName) {
        Random random = engine.getRandom();
        return new Door(this,rooms[random.nextInt(rooms.length)]);
    }
}
