package dev.csu.survivor.item;

import com.almasb.fxgl.entity.Entity;
import dev.csu.survivor.component.MotionComponent;

public record AccelerateCrystal(double accelerateDegree) implements Item {

    @Override
    public void onApply(Entity entity) {

        MotionComponent motionComponent = entity.getComponent(MotionComponent.class);

        motionComponent.setSpeed(motionComponent.getSpeed() * accelerateDegree);
    }

    @Override
    public void onRemove(Entity entity) {

        MotionComponent motionComponent = entity.getComponent(MotionComponent.class);

        motionComponent.setSpeed(motionComponent.getSpeed() / accelerateDegree);
    }
}
