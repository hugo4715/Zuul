package pkg_game.pkg_entity;

public interface EntityMoving {
    void move();

    /**
     * Get the chance of this entitiy moving
     * @return a double between 0 and 1, 1 being 100% chance of moving each tick
     */
    double moveChance();
}
