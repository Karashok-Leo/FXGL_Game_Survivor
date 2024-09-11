package dev.csu.survivor.component.base;

import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

import java.util.function.Supplier;

/**
 * 描述实体的所有者的组件
 * <p>
 * 在游戏场景中，有一些实体是被其他实体所拥有的。<br>
 * 如：<br>
 * 玩家投出的飞镖被玩家所拥有<br>
 * 远程敌人挥出的剑气被远程敌人所拥有<br>
 * 通过该组件可以获取某一实体的所有者
 * </p>
 */
public class OwnableComponent extends Component
{
    protected final LazyValue<Entity> owner;

    public OwnableComponent(Supplier<Entity> ownerSupplier)
    {
        this.owner = new LazyValue<>(ownerSupplier);
    }

    public Entity getOwner()
    {
        return owner.get();
    }
}
