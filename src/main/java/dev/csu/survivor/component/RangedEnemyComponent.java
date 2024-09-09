package dev.csu.survivor.component;

import dev.csu.survivor.Constants;
import dev.csu.survivor.enums.EntityStates;
import dev.csu.survivor.world.SurvivorGameWorld;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.state.StateComponent;
import javafx.geometry.Point2D;

public class RangedEnemyComponent extends Component {
    protected StateComponent state;
    protected MotionComponent motion;
    protected RangedAttackComponent rangedAttack;

    public RangedEnemyComponent() {
    }

    @Override
    public void onAdded() {
        state = entity.getComponent(StateComponent.class);
        motion = entity.getComponent(MotionComponent.class);
        rangedAttack = entity.getComponent(RangedAttackComponent.class);
    }

    @Override
    public void onUpdate(double tpf) {

        if (state.isIn(EntityStates.DEATH)) {
            return;
        }

        Point2D target = SurvivorGameWorld.getPlayer().getPosition();
        double distance = entity.distance(SurvivorGameWorld.getPlayer());

        if (state.isIn(EntityStates.IDLE)) {
            state.changeState(EntityStates.RUN);
        }

        if (state.isIn(EntityStates.RUN, EntityStates.HURT)) {
            if (distance > Constants.Common.RANGED_ENEMY_ATTACK_RANGE) {

                Point2D direction = target.subtract(entity.getPosition()).normalize();
                motion.addVelocity(direction.getX(), direction.getY());
            } else {
                rangedAttack.attack(SurvivorGameWorld.getPlayer());
            }
        }

        if (state.isIn(EntityStates.ATTACK) && distance > Constants.Common.RANGED_ENEMY_ATTACK_RANGE) {
            state.changeState(EntityStates.RUN);
        }
    }
}
