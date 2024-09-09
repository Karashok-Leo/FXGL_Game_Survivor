package dev.csu.survivor.component;

import com.almasb.fxgl.entity.component.Component;
import dev.csu.survivor.item.Item;

import java.util.ArrayList;
import java.util.List;

public class InventoryComponent extends Component
{
    private final List<Item> inventory;

    public InventoryComponent()
    {
        inventory = new ArrayList<>();
    }

    public List<Item> getInventory()
    {
        return inventory;
    }

    public void addItem(Item item)
    {
        inventory.add(item);
        item.onApply(entity);
    }

    public void removeItem(Item item)
    {
        inventory.remove(item);
        item.onRemove(entity);
    }
}
