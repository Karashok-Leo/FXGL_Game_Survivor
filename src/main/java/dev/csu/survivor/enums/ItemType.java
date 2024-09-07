package dev.csu.survivor.enums;

import dev.csu.survivor.Constants;
import dev.csu.survivor.item.AccelerateCrystal;
import dev.csu.survivor.item.HealingMedicine;
import dev.csu.survivor.item.Item;
import dev.csu.survivor.item.HealthCrystal;

public enum ItemType {
    HealthCrystal(new HealthCrystal(Constants.Common.HEALTH_CRYSTAL_VALUE)),
    AccelerateCrystal(new AccelerateCrystal(Constants.Common.ACCELERATE_CRYSTAL_DEGREE)),
    HealingMedicine(new HealingMedicine(Constants.Common.HEALING_MEDICINE_VALUE));

    private final Item item;

    ItemType(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
