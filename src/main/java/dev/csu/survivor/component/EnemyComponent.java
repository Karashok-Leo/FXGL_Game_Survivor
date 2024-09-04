package dev.csu.survivor.component;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.state.StateComponent;
import dev.csu.survivor.enums.EntityStates;
import dev.csu.survivor.enums.EntityType;
import javafx.geometry.Point2D;

public class EnemyComponent extends Component
{
    protected StateComponent state;
    protected MotionComponent motion;

    public EnemyComponent()
    {
    }

    @Override
    public void onAdded()
    {
        state = entity.getComponent(StateComponent.class);
        motion = entity.getComponent(MotionComponent.class);
    }

    @Override
    public void onUpdate(double tpf)
    {
        if (!state.isIn(EntityStates.ATTACK))
        {
            state.changeState(EntityStates.RUN);
            Point2D target = FXGL.getGameWorld()
                    .getEntitiesByType(EntityType.PLAYER)
                    .getFirst()
                    .getPosition();
            Point2D subtract = target.subtract(entity.getPosition());
            motion.addVelocity(subtract.getX(), subtract.getY());
        }
    }
}
