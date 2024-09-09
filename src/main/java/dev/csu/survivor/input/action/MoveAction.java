package dev.csu.survivor.input.action;

import com.almasb.fxgl.input.UserAction;
import dev.csu.survivor.component.MotionComponent;
import dev.csu.survivor.enums.Direction;
import dev.csu.survivor.util.StringUtil;
import dev.csu.survivor.world.SurvivorGameWorld;

public class MoveAction extends UserAction
{
    private final Direction direction;

    public MoveAction(Direction direction)
    {
        super(StringUtil.uppercaseFirstLetter(direction.name));
        this.direction = direction;
    }

    @Override
    protected void onAction()
    {
        SurvivorGameWorld.getPlayer()
                .getComponent(MotionComponent.class)
                .addVelocity(direction.vector);
    }
}
