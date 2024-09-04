package dev.csu.survivor.action;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.input.UserAction;
import dev.csu.survivor.component.MotionComponent;
import dev.csu.survivor.enums.Direction;
import dev.csu.survivor.enums.EntityStates;
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
        StateComponent state = FXGL.getGameWorld()
                .getEntitiesByType(EntityType.PLAYER)
                .getFirst()
                .getComponent(StateComponent.class);
//        if (state.isIn(EntityStates.IDLE))
        state.changeState(EntityStates.RUN);
    }

    @Override
    protected void onAction()
    {
        FXGL.getGameWorld()
                .getEntitiesByType(EntityType.PLAYER)
                .getFirst()
                .getComponent(MotionComponent.class)
                .addVelocity(direction.vector);
    }
}
