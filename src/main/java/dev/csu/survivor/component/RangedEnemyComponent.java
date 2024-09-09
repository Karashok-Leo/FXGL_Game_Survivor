package dev.csu.survivor.component;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.state.StateComponent;
import dev.csu.survivor.Constants;
import dev.csu.survivor.enums.EntityStates;
import dev.csu.survivor.world.SurvivorGameWorld;
import javafx.geometry.Point2D;

public class RangedEnemyComponent extends Component
{
    protected StateComponent state;
    protected MotionComponent motion;
    protected RangedAttackComponent rangedAttack;

    public RangedEnemyComponent()
    {
    }

    @Override
    public void onAdded()
    {
        state = entity.getComponent(StateComponent.class);
        motion = entity.getComponent(MotionComponent.class);
        rangedAttack = entity.getComponent(RangedAttackComponent.class);
    }

    @Override
    public void onUpdate(double tpf)
    {

        if (state.isIn(EntityStates.DEATH))
        {
            return;
        }

        Point2D target = SurvivorGameWorld.getPlayer().getPosition();
        double distance = entity.distance(SurvivorGameWorld.getPlayer());

        if (state.isIn(EntityStates.IDLE))
        {
            state.changeState(EntityStates.RUN);
        }

        if (state.isIn(EntityStates.RUN, EntityStates.HURT))
        {
            if (distance > Constants.Common.RANGED_ENEMY_ATTACK_RANGE)
            {
                Vec2 direction = new Vec2(target.subtract(entity.getPosition())).normalize();

                motion.addVelocity(direction);

            } else
            {
                rangedAttack.attack(SurvivorGameWorld.getPlayer());

                Vec2 direction = new Vec2(target.subtract(entity.getPosition()));
                entity.getComponent(AnimationComponent.class).updateDirection(direction);
            }
        }

        if (state.isIn(EntityStates.ATTACK) && distance > Constants.Common.RANGED_ENEMY_ATTACK_RANGE)
        {
            state.changeState(EntityStates.RUN);
        }
    }
}
