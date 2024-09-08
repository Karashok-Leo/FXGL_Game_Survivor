package dev.csu.survivor.item;

import com.almasb.fxgl.entity.Entity;
import dev.csu.survivor.component.AttributeComponent;
import javafx.scene.text.Text;
import java.util.List;

public interface Item
{
    default void onApply(Entity entity)
    {
    }

    default void onRemove(Entity entity)
    {
    }

    default List<Text> getTooltip()
    {
        return List.of();
    }

    default void attributeChange(Entity entity) {
        AttributeComponent attributeComponent = entity.getComponent(AttributeComponent.class);

        if (attributeComponent != null) {
            applyAttributeChange(attributeComponent);
        }
    }

    default void applyAttributeChange(AttributeComponent attributeComponent) {}
}
