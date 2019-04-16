
public class Item {
    private String name;
    private String description;
    private int weight;

    public Item(String name, final String description, final int weight) {
        this.name = name;
        this.description = description;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public String getDescription() {
        return description;
    }

    public String getLongDescription() {
        return getName() + "( " + getDescription() + ")";
    }
}
