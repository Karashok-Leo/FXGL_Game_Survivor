package dev.csu.survivor.item;

import com.almasb.fxgl.entity.Entity;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.AttributeComponent;
import dev.csu.survivor.component.MotionComponent;

import java.util.UUID;

public record AccelerateCrystal(String itemId, double accelerateDegree) implements Item
{
    @Override
    public void onApply(Entity entity) {
        MotionComponent motionComponent = entity.getComponent(MotionComponent.class);
        AttributeComponent attributeComponent = entity.getComponent(AttributeComponent.class);

        attributeComponent.addSpeedModifier(itemId, accelerateDegree);
        double newSpeed = Constants.Common.PLAYER_SPEED * attributeComponent.calculateTotalSpeedMultiplier();
        motionComponent.setSpeed(newSpeed);
        System.out.println(motionComponent.getSpeed());
    }

    @Override
    public void onRemove(Entity entity) {
        MotionComponent motionComponent = entity.getComponent(MotionComponent.class);
        AttributeComponent attributeComponent = entity.getComponent(AttributeComponent.class);

        attributeComponent.removeSpeedModifier(itemId);
        double newSpeed = Constants.Common.PLAYER_SPEED  * attributeComponent.calculateTotalSpeedMultiplier();
        motionComponent.setSpeed(newSpeed);
    }

    @Override
    public void attributeChange(Entity entity) {
        AttributeComponent attributeComponent = entity.getComponent(AttributeComponent.class);

        attributeComponent.addSpeedModifier(itemId, accelerateDegree);
    }
}
