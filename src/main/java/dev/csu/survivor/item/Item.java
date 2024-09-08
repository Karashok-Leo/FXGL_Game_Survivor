package dev.csu.survivor.item;

import com.almasb.fxgl.entity.Entity;
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
}
