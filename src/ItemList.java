import java.util.*;

public class ItemList{

    private Map<String,Item> items;

    public ItemList() {
        this.items = new HashMap<>();
    }

    public void addItem(final Item item){
        items.put(item.getName().toLowerCase(),item);
    }

    public Item getItem(final String itemName){
        return items.get(itemName.toLowerCase());
    }

    public void removeItem(final Item item){
        removeItem(item.getName());
    }

    public void removeItem(final String itemName){
        items.remove(itemName.toLowerCase());
    }
    public boolean isEmpty(){
        return items.isEmpty();
    }

    public Collection<Item> getAllItems() {
        return items.values();
    }
}
