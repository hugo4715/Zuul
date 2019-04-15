public class Item {
    private String description;
    private int weight;

    public Item(final String description, final int weight) {
        this.description = description;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public String getDescription() {
        return description;
    }
}
