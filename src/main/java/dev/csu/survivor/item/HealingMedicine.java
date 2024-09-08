package dev.csu.survivor.item;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;

public record HealingMedicine(int HealingHealth) implements Item
{
    @Override
    public void onApply(Entity entity) {

        HealthIntComponent hp = entity.getComponent(HealthIntComponent.class);

        hp.setValue(hp.getValue() + HealingHealth);
    }
}
