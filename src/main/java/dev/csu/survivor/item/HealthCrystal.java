package dev.csu.survivor.item;

import com.almasb.fxgl.entity.Entity;
import dev.csu.survivor.component.AttributeComponent;
import dev.csu.survivor.enums.AttributeType;
import dev.csu.survivor.world.attribute.AttributeModifier;
import dev.csu.survivor.world.attribute.ModifierOperation;

import java.util.UUID;

public record HealthCrystal(String itemId, int additionalHealth) implements Item {

    public HealthCrystal(int additionalHealth) {
        this(UUID.randomUUID().toString(), additionalHealth);
    }

    @Override
    public void onApply(Entity entity) {
        AttributeComponent attributeComponent = entity.getComponent(AttributeComponent.class);
        AttributeModifier modifier = new AttributeModifier(ModifierOperation.ADDITION, additionalHealth);
        attributeComponent.addModifier(AttributeType.HEALTH, itemId, modifier);
    }

    @Override
    public void onRemove(Entity entity) {
        AttributeComponent attributeComponent = entity.getComponent(AttributeComponent.class);
        attributeComponent.removeModifier(AttributeType.HEALTH, itemId);
    }
}
