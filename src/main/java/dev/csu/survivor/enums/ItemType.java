package dev.csu.survivor.enums;

import dev.csu.survivor.Constants;
import dev.csu.survivor.item.AccelerateCrystal;
import dev.csu.survivor.item.HealingMedicine;
import dev.csu.survivor.item.HealthCrystal;
import dev.csu.survivor.item.Item;

import java.util.Locale;

public enum ItemType
{
    HEALTH_CRYSTAL(new HealthCrystal(Constants.Common.HEALTH_CRYSTAL_VALUE), 3, true),
    ACCELERATE_CRYSTAL(new AccelerateCrystal(Constants.Common.ACCELERATE_CRYSTAL_DEGREE)),
    HEALING_MEDICINE(new HealingMedicine(Constants.Common.HEALING_MEDICINE_VALUE));

    /**
     * The identifier of the item.
     * The texture of the item will be located at "assets/textures/item/{id}.png"
     */
    public final String id;

    public final Item item;

    /**
     * The higher the rarity, the less likely it is to be sold in the shop
     */
    public final int rarity;

    /**
     * If an item is stackable, it means that the player can hold multiple of the item, and the shop will also repeat the item
     */
    public final boolean stackable;

    ItemType(Item item)
    {
        this(item, 0);
    }

    ItemType(Item item, int rarity)
    {
        this(item, rarity, true);
    }

    ItemType(Item item, int rarity, boolean stackable)
    {
        this.id = this.name().toLowerCase(Locale.ROOT);
        this.item = item;
        this.rarity = rarity;
        this.stackable = stackable;
    }
}
