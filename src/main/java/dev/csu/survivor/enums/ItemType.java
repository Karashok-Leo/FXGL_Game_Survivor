package dev.csu.survivor.enums;

import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import dev.csu.survivor.Constants;
import dev.csu.survivor.item.AccelerateCrystal;
import dev.csu.survivor.item.HealingMedicine;
import dev.csu.survivor.item.HealthCrystal;
import dev.csu.survivor.item.Item;
import dev.csu.survivor.util.StringUtil;

import java.util.function.Supplier;

public enum ItemType
{
    HEALTH_CRYSTAL(() -> new HealthCrystal(Constants.Common.HEALTH_CRYSTAL_VALUE), 3),
    ACCELERATE_FEATHER(() -> new AccelerateCrystal(Constants.Common.ACCELERATE_CRYSTAL_DEGREE)),
    HEALING_POTION(() -> new HealingMedicine(Constants.Common.HEALING_MEDICINE_VALUE));

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
    public final ItemFactory itemFactory;

    /**
     * The higher the rarity, the less likely it is to be sold in the shop
     */
    public final int rarity;

    /**
     * If an item is stackable, it means that the player can hold multiple of the item, and the shop will also repeat the item
     */
    public final boolean stackable;

    // Lazy initialized fields
    private LazyValue<String> lazyItemName;
    private LazyValue<Texture> lazyItemTexture;

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
        this.lazyItemName = new LazyValue<>(() -> FXGL.localize("item.%s".formatted(this.id)));
        this.lazyItemTexture = new LazyValue<>(() -> FXGL.texture("item/%s.png".formatted(id)));
    }

    public String getItemName()
    {
        return lazyItemName.get();
    }

    public Texture getItemTexture()
    {
        return lazyItemTexture.get();
    }
}
