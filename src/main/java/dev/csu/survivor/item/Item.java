package dev.csu.survivor.item;

import com.almasb.fxgl.entity.Entity;

public interface Item {

    default void onApply(Entity entity){}

    default void onRemove(Entity entity){}
}
