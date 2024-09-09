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

    default List<Text> getTooltip(Entity entity)
    {
        return List.of();
    }
}
