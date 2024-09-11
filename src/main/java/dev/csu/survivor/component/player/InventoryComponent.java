package dev.csu.survivor.component.player;

import com.almasb.fxgl.entity.component.Component;
import dev.csu.survivor.enums.ItemType;
import dev.csu.survivor.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 控制实体物品栏的组件
 */
public class InventoryComponent extends Component
{
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

    /**
     * 获取实体物品栏的所有物品
     * @return 实体物品栏的所有物品，返回值是一个不可变的列表
     */
    public List<ItemEntry> getInventory()
    {
        return Collections.unmodifiableList(inventory);
    }

    /**
     * 向物品栏中添加物品
     * <p>
     *     自动调用物品的 onApply 方法
     * </p>
     * <p>
     *     应该在
     * </p>
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

    public record ItemEntry(ItemType type, Item item)
    {
        public ItemEntry(ItemType type)
        {
            this(type, type.createItem());
        }
    }
}
