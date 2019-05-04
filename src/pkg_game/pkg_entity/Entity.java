package pkg_game.pkg_entity;

import pkg_game.pkg_room.Room;

import java.io.Serializable;

public interface Entity extends Serializable {
    String getName();
    Room getRoom();
}
