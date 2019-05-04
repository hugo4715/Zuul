package pkg_game;

import java.io.Serializable;
import java.util.*;

public class ItemList implements Serializable {

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

    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        for (Item item : items.values()) {
            sb.append("\n - ");
            sb.append(item.getName());
        }
        return sb.toString();
    }

    public int totalWeight(){
        return items.values().stream().mapToInt(Item::getWeight).sum();
    }

    public boolean contains(Item item) {
        return items.containsValue(item);
    }
}
