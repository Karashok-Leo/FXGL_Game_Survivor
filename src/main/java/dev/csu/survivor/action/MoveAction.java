package dev.csu.survivor.action;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import dev.csu.survivor.component.MotionComponent;
import dev.csu.survivor.component.PlayerAnimationComponent;
import dev.csu.survivor.enums.Direction;
import dev.csu.survivor.enums.EntityType;

public class MoveAction extends UserAction
{
    private final Direction direction;

    public MoveAction(Direction direction)
    {
        super(direction.name);
        this.direction = direction;
    }

    @Override
    protected void onActionBegin()
    {
        FXGL.getGameWorld()
                .getEntitiesByType(EntityType.PLAYER)
                .getFirst()
                .getComponent(MotionComponent.class)
                .getVelocity()
                .addLocal(direction.vector);
    }

    @Override
    protected void onAction()
    {
        FXGL.getGameWorld()
                .getEntitiesByType(EntityType.PLAYER)
                .getFirst()
                .getComponent(PlayerAnimationComponent.class)
                .setDirection(direction);
    }

    @Override
    protected void onActionEnd()
    {
        FXGL.getGameWorld()
                .getEntitiesByType(EntityType.PLAYER)
                .getFirst()
                .getComponent(MotionComponent.class)
                .getVelocity()
                .subLocal(direction.vector);
    }
}
