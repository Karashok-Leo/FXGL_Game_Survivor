package dev.csu.survivor.item;

import com.almasb.fxgl.entity.Entity;
import dev.csu.survivor.component.AttributeComponent;

public interface Item {

    default void onApply(Entity entity) {}

    default void onRemove(Entity entity) {}

    default void attributeChange(Entity entity) {
        AttributeComponent attributeComponent = entity.getComponent(AttributeComponent.class);

        if (attributeComponent != null) {
            applyAttributeChange(attributeComponent);
        }
    }

    default void applyAttributeChange(AttributeComponent attributeComponent) {}
}
