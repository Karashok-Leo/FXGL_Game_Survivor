package dev.csu.survivor.util;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

import java.util.function.Consumer;

public class ComponentUtil
{
    /**
     * 查询一个实体上拥有的继承自某一类的所有组件，并调用这些组件父类的方法
     * <p>
     * 由于 FXGL 中只提供了查找指定类型的 Component 的方法，不足以满足面向对象继承结构的需求，故提供了该方法。
     * </p>
     *
     * @param entity   要查询的实体
     * @param clazz    组件父类
     * @param consumer 对组件对象调用的方法
     * @see Entity#getComponent(Class)
     */
    @SuppressWarnings("unchecked")
    public static <C extends Component> void findAndConsumeComponentByClass(Entity entity, Class<C> clazz, Consumer<C> consumer)
    {
        entity.getComponents()
                .stream()
                .filter(component -> clazz.isAssignableFrom(component.getClass()))
                .forEach(component -> consumer.accept((C) component));
    }
}
