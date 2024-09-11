package dev.csu.survivor.component.enemy;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.state.StateComponent;
import dev.csu.survivor.component.base.MotionComponent;
import dev.csu.survivor.enums.EntityStates;
import dev.csu.survivor.world.SurvivorGameWorld;
import javafx.geometry.Point2D;

/**
 * Should be added after StateComponent and MotionComponent
 */
public class MeleeEnemyComponent extends Component
{
    protected StateComponent state;
    protected MotionComponent motion;

    public MeleeEnemyComponent()
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
        if (state.isIn(EntityStates.IDLE))
            state.changeState(EntityStates.RUN);
        if (state.isIn(EntityStates.RUN, EntityStates.HURT))
        {
            Point2D target = SurvivorGameWorld
                    .getPlayer()
                    .getPosition();
            Point2D subtract = target.subtract(entity.getPosition());
            motion.addVelocity(subtract.getX(), subtract.getY());
        }
    }
}
