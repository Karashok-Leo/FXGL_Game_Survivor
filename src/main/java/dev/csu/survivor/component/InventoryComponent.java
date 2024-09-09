package dev.csu.survivor.component;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import dev.csu.survivor.item.Item;

import java.util.ArrayList;
import java.util.List;

public class InventoryComponent extends Component
{
    private final List<Item> itemList;

    public InventoryComponent() {
        itemList = new ArrayList<>();
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void addItemToList(Entity entity, Item item) {
        itemList.add(item);
        item.onApply(entity);
    }

    public void removeItemFromList(Entity entity, Item item) {
        itemList.remove(item);
        item.onRemove(entity);
    }
}
