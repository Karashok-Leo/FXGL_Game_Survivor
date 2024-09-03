package dev.csu.survivor.action;

import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import dev.csu.survivor.component.MotionComponent;
import dev.csu.survivor.component.PlayerAnimationComponent;
import dev.csu.survivor.enums.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class MoveAction extends UserAction
{
    private final LazyValue<Entity> playerGetter;
    private final Direction direction;

    public MoveAction(@NotNull String name, Supplier<Entity> playerSupplier, Direction direction)
    {
        super(name);
        this.playerGetter = new LazyValue<>(playerSupplier);
        this.direction = direction;
    }

    @Override
    protected void onActionBegin()
    {
        playerGetter.get().getComponent(MotionComponent.class).getVelocity().addLocal(direction.vector);
    }

    @Override
    protected void onAction()
    {
        playerGetter.get().getComponent(PlayerAnimationComponent.class).setDirection(direction);
    }

    @Override
    protected void onActionEnd()
    {
        playerGetter.get().getComponent(MotionComponent.class).getVelocity().subLocal(direction.vector);
    }
}
