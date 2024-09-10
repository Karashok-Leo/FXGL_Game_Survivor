package dev.csu.survivor.item;

import com.almasb.fxgl.dsl.components.RechargeableIntComponent;
import com.almasb.fxgl.entity.Entity;

public interface IRechargeableComponentItem<T extends RechargeableIntComponent> extends Item
{
    T getComponent();

    Class<T> getComponentClass();

    @Override
    default void onApply(Entity entity)
    {
        if (entity.hasComponent(getComponentClass()))
            entity.getComponent(getComponentClass()).restore(1);
        else entity.addComponent(getComponent());
    }

    @Override
    default void onRemove(Entity entity)
    {
        if (entity.hasComponent(getComponentClass()))
        {
            T component = entity.getComponent(getComponentClass());
            component.damage(1);
            if (component.isZero())
                entity.removeComponent(getComponentClass());
        } else throw new AssertionError();
    }
}
