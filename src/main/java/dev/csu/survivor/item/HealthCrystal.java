package dev.csu.survivor.item;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;

public record HealthCrystal(int additionalHealth) implements Item {
    @Override
    public void onApply(Entity entity) {
        HealthIntComponent hp = entity.getComponent(HealthIntComponent.class);
        hp.setMaxValue(hp.getMaxValue() + additionalHealth);
    }

    @Override
    public void onRemove(Entity entity) {
        HealthIntComponent hp = entity.getComponent(HealthIntComponent.class);
        hp.setMaxValue(hp.getMaxValue() - additionalHealth);
    }
}
