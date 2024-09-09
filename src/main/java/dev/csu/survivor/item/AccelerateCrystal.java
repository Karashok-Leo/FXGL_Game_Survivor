package dev.csu.survivor.item;

import com.almasb.fxgl.entity.Entity;
import dev.csu.survivor.component.AttributeComponent;
import dev.csu.survivor.enums.AttributeType;
import dev.csu.survivor.world.attribute.AttributeModifier;
import dev.csu.survivor.world.attribute.ModifierOperation;

import java.util.UUID;

public record AccelerateCrystal(String itemId, double accelerateDegree) implements Item {

    public AccelerateCrystal(double accelerateDegree) {
        this(UUID.randomUUID().toString(), accelerateDegree);
    }

    @Override
    public void onApply(Entity entity) {
        AttributeComponent attributeComponent = entity.getComponent(AttributeComponent.class);
        AttributeModifier modifier = new AttributeModifier(ModifierOperation.MULTIPLICATION, accelerateDegree);
        attributeComponent.addModifier(AttributeType.SPEED, itemId, modifier);
    }

    @Override
    public void onRemove(Entity entity) {
        AttributeComponent attributeComponent = entity.getComponent(AttributeComponent.class);
        attributeComponent.removeModifier(AttributeType.SPEED, itemId);
    }
}
