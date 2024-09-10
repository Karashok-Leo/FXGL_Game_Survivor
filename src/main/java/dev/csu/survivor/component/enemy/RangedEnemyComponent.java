package dev.csu.survivor.component.enemy;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.time.TimerAction;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.base.AnimationComponent;
import dev.csu.survivor.component.base.MotionComponent;
import dev.csu.survivor.enums.EntityStates;
import dev.csu.survivor.world.SurvivorGameWorld;
import javafx.geometry.Point2D;

public class RangedEnemyComponent extends Component
{
    protected StateComponent state;
    protected MotionComponent motion;
    protected RangedAttackComponent rangedAttack;
    private TimerAction attackCooldown;

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
        if (state.isIn(EntityStates.DEATH)) return;

        Point2D target = SurvivorGameWorld.getPlayer().getPosition();
        double distance = entity.distance(SurvivorGameWorld.getPlayer());

        if (state.isIn(EntityStates.IDLE))
            state.changeState(EntityStates.RUN);

        if (state.isIn(EntityStates.RUN, EntityStates.HURT))
        {
            if (distance > Constants.Common.RANGED_ENEMY_ATTACK_RANGE)
            {
                Point2D subtract = target.subtract(entity.getPosition());
                motion.addVelocity(subtract.getX(), subtract.getY());
            }
            else
            {
                if (attackCooldown == null || attackCooldown.isExpired()) {
                    rangedAttack.attack(SurvivorGameWorld.getPlayer());

                    Vec2 direction = new Vec2(target.subtract(entity.getPosition()));
                    entity.getComponent(AnimationComponent.class).updateDirection(direction);

                    attackCooldown = FXGL.getGameTimer().runOnceAfter(() -> {}, Constants.Common.RANGED_ENEMY_ATTACK_COOLDOWN);
                }
            }
        }

        if (state.isIn(EntityStates.ATTACK) && distance > Constants.Common.RANGED_ENEMY_ATTACK_RANGE)
        {
            state.changeState(EntityStates.RUN);
        }
    }
}
