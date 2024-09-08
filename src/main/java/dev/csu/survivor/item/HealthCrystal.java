package dev.csu.survivor.item;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.AttributeComponent;

public record HealthCrystal(String itemId, int additionalHealth) implements Item {

    @Override
    public void onApply(Entity entity) {
        HealthIntComponent hp = entity.getComponent(HealthIntComponent.class);
        AttributeComponent attributeComponent = entity.getComponent(AttributeComponent.class);

        attributeComponent.addHealthModifier(itemId, additionalHealth);

        hp.setMaxValue(Constants.Common.PLAYER_INITIAL_MAX_HEALTH + attributeComponent.calculateTotalHealthAddition());

        int newHealthValue = hp.getValue() + additionalHealth;
        if (newHealthValue > hp.getMaxValue()) {
            newHealthValue = hp.getMaxValue();
        }
        hp.setValue(newHealthValue);
    }

    @Override
    public void onRemove(Entity entity) {
        HealthIntComponent hp = entity.getComponent(HealthIntComponent.class);
        AttributeComponent attributeComponent = entity.getComponent(AttributeComponent.class);

        attributeComponent.removeHealthModifier(itemId);

        hp.setMaxValue(Constants.Common.PLAYER_INITIAL_MAX_HEALTH + attributeComponent.calculateTotalHealthAddition());

        if (hp.getValue() > hp.getMaxValue()) {
            hp.setValue(hp.getMaxValue());
        }
    }
}
