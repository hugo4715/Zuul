import java.util.Collection;
import java.util.Random;

public class TransporterRoom extends Room {

    private final Collection<Room> rooms;
    /**
     * Create a new transporter room
     * @param rooms The rooms where this transporter can get to
     */
    public TransporterRoom(String name, String description, String image, Collection<Room> rooms) {
        super(name, description, image);
        this.rooms = rooms;
    }

    @Override
    public Door getExit(String exitName) {
        Random random = Game.getGame().getRandom();
        return new Door(this,rooms.stream().skip(random.nextInt(rooms.size())).findFirst().orElse(null));
    }
}
