package dev.csu.survivor.item;

import com.almasb.fxgl.entity.Entity;
import dev.csu.survivor.component.AttributeComponent;
import dev.csu.survivor.component.MotionComponent;

public record AccelerateCrystal(double accelerateDegree) implements Item
{
    @Override
    public void onApply(Entity entity) {
        MotionComponent motionComponent = entity.getComponent(MotionComponent.class);
        AttributeComponent attributeComponent = entity.getComponent(AttributeComponent.class);

        double newSpeed = motionComponent.getSpeed() * attributeComponent.calculateTotalSpeedMultiplier();
        motionComponent.setSpeed(newSpeed);
    }

    @Override
    public void onRemove(Entity entity) {
        MotionComponent motionComponent = entity.getComponent(MotionComponent.class);
        AttributeComponent attributeComponent = entity.getComponent(AttributeComponent.class);

        double originalSpeed = motionComponent.getSpeed() / attributeComponent.calculateTotalSpeedMultiplier();
        motionComponent.setSpeed(originalSpeed);
    }

    @Override
    public void attributeChange(Entity entity) {
        AttributeComponent attributeComponent = entity.getComponent(AttributeComponent.class);

        attributeComponent.addSpeedModifier(this.getClass().getName(), accelerateDegree);
    }
}
