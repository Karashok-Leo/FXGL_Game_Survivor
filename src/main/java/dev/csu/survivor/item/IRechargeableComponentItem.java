package dev.csu.survivor.item;

import com.almasb.fxgl.dsl.components.RechargeableIntComponent;
import com.almasb.fxgl.entity.Entity;

/**
 * 对 {@link RechargeableIntComponent} 进行修改的物品
 * <p>
 * 该物品被添加至物品栏时，若该实体拥有 T 类型的 Component，则其值加一，否则调用 createComponent 方法创建一个该类型的组件并添加到实体上
 * </p>
 * <p>
 * 该物品从物品栏移除时，将该实体拥有 T 类型的 Component 的值减一，若该值归零，则从该实体上移除该组件
 * </p>
 *
 * @param <T>
 */
public interface IRechargeableComponentItem<T extends RechargeableIntComponent> extends Item
{
    T createComponent();

    Class<T> getComponentClass();

    @Override
    default void onApply(Entity entity)
    {
        if (entity.hasComponent(getComponentClass()))
            entity.getComponent(getComponentClass()).restore(1);
        else entity.addComponent(createComponent());
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
