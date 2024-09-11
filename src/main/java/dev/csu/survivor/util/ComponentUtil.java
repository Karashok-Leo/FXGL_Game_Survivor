package dev.csu.survivor.util;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

import java.util.function.Consumer;

public class ComponentUtil
{
    @SuppressWarnings("unchecked")
    public static <C extends Component> void findAndConsumeComponentByClass(Entity entity, Class<C> clazz, Consumer<C> consumer)
    {
        entity.getComponents()
                .stream()
                .filter(component -> clazz.isAssignableFrom(component.getClass()))
                .forEach(component -> consumer.accept((C) component));
    }
}
