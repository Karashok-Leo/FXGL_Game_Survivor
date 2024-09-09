package dev.csu.survivor.enums;

import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import dev.csu.survivor.Constants;
import dev.csu.survivor.item.AttributeItem;
import dev.csu.survivor.item.ComponentItem;
import dev.csu.survivor.item.Item;
import dev.csu.survivor.util.StringUtil;
import dev.csu.survivor.world.attribute.AttributeModifier;

import java.util.function.Supplier;

public enum ItemType
{
    HEALTH_CRYSTAL(
            () -> new AttributeItem(
                    AttributeType.MAX_HEALTH,
                    new AttributeModifier("HealthCrystal", AttributeModifier.Operation.ADDITION, Constants.Common.HEALTH_CRYSTAL_VALUE)
            ),
            3
    ),

    ACCELERATE_FEATHER(
            () -> new AttributeItem(
                    AttributeType.SPEED,
                    new AttributeModifier("AccelerateFeather", AttributeModifier.Operation.MULTIPLICATION, Constants.Common.ACCELERATE_FEATHER_DEGREE)
            )
    ),
    HEALING_POTION(
            () -> new AttributeItem(
                    AttributeType.REGENERATION,
                    new AttributeModifier("HealingPotion", AttributeModifier.Operation.ADDITION, Constants.Common.HEALING_POTION_VALUE)
            )
    ),
    BOOMERANG_ATTACK(
            () -> new ComponentItem(
                    EntityType.BOOMERANG
            )
    )
    ;

    /**
     * The identifier of the item, default to be lowercase of the enum name.
     * The texture of the item will be located at "assets/textures/item/{id}.png"
     * The texture must be 32x32
     * The localized name of the item will be found by key "item.{id}"
     */
    public final String id;

    public interface ItemFactory extends Supplier<Item>
    {
    }

    /**
     * Used to create a new item every time the shop refreshes
     */
    private final ItemFactory itemFactory;

    /**
     * The higher the rarity, the less likely it is to be sold in the shop
     */
    public final int rarity;

    /**
     * If an item is stackable, it means that the player can hold multiple of the item, and the shop will also repeat the item
     */
    public final boolean stackable;

    // Lazy initialized fields
    private LazyValue<String> lazyName;
    private LazyValue<Texture> lazyTexture;

    ItemType(ItemFactory itemFactory)
    {
        this(itemFactory, 0);
    }

    ItemType(ItemFactory itemFactory, int rarity)
    {
        this(itemFactory, rarity, true);
    }

    ItemType(ItemFactory itemFactory, int rarity, boolean stackable)
    {
        this.id = StringUtil.lowercase(this.name());
        this.itemFactory = itemFactory;
        this.rarity = rarity;
        this.stackable = stackable;

        initLazyValues();
    }

    ItemType(String id, ItemFactory itemFactory, int rarity, boolean stackable)
    {
        this.id = id;
        this.itemFactory = itemFactory;
        this.rarity = rarity;
        this.stackable = stackable;

        initLazyValues();
    }

    private void initLazyValues()
    {
        this.lazyName = new LazyValue<>(() -> FXGL.localize("item.%s".formatted(this.id)));
        this.lazyTexture = new LazyValue<>(() -> FXGL.texture("item/%s.png".formatted(id)));
    }

    public String getName()
    {
        return lazyName.get();
    }

    public Texture getTexture()
    {
        return lazyTexture.get();
    }

    public Item createItem()
    {
        return itemFactory.get();
    }
}
