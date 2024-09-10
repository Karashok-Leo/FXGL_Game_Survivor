package dev.csu.survivor.component;

import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

import java.util.function.Supplier;

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
