package pkg_game.pkg_room;

import java.util.Collection;
import java.util.Random;

public class TransporterRoom extends Room {

    private final Collection<Room> rooms;
    private final Random random;

    /**
     * Create a new transporter room
     * @param rooms The rooms where this transporter can get to
     */
    public TransporterRoom(String name, String description, String image, Collection<Room> rooms, Random random) {
        super(name, description, image);
        this.rooms = rooms;
        this.random = random;
    }

    @Override
    public Door getExit(String exitName) {
        return new Door(this,rooms.stream().skip(random.nextInt(rooms.size())).findFirst().orElse(null));
    }
}
