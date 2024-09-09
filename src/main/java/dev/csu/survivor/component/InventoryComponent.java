package dev.csu.survivor.component;

import com.almasb.fxgl.entity.component.Component;
import dev.csu.survivor.enums.ItemType;
import dev.csu.survivor.item.Item;

import java.util.ArrayList;
import java.util.List;

public class InventoryComponent extends Component
{
    public record ItemEntry(ItemType type, Item item)
    {
        public ItemEntry(ItemType type)
        {
            this(type, type.createItem());
        }
    }

    private final List<ItemEntry> inventory;

    public InventoryComponent(ItemEntry... entries)
    {
        inventory = new ArrayList<>(List.of(entries));
    }

    @Override
    public void onAdded()
    {
        inventory.forEach(entry -> entry.item.onApply(entity));
    }

    @Override
    public void onRemoved()
    {
        inventory.forEach(entry -> entry.item.onRemove(entity));
    }

    public List<ItemEntry> getInventory()
    {
        return inventory;
    }

    /**
     * Should be called after the component has been added to the entity.
     */
    public void addItem(ItemType type, Item item)
    {
        item.onApply(entity);
        inventory.add(new ItemEntry(type, item));
    }

    public void addItem(ItemType type)
    {
        this.addItem(type, type.createItem());
    }

    public void removeItem(ItemType type)
    {
        inventory.stream()
                .filter(entry -> entry.type == type)
                .findFirst()
                .ifPresent(entry ->
                {
                    entry.item.onRemove(entity);
                    inventory.remove(entry);
                });
    }
}
