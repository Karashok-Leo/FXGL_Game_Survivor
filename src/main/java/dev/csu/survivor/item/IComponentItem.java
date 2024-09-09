package dev.csu.survivor.item;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

public interface IComponentItem<T extends Component> extends Item
{
    T getComponent();

    @Override
    default void onApply(Entity entity)
    {
        entity.addComponent(getComponent());
    }

    @Override
    default void onRemove(Entity entity)
    {
        entity.removeComponent(getComponent().getClass());
    }
}
