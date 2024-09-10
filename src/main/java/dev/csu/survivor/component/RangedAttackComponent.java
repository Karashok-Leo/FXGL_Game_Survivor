package dev.csu.survivor.component;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.time.Timer;
import javafx.util.Duration;
import dev.csu.survivor.Constants;
import dev.csu.survivor.enums.EntityStates;
import javafx.geometry.Point2D;

public class RangedAttackComponent extends Component {
    protected final Timer timer;
    protected StateComponent state;
    private boolean isAttacking;
    private final Duration attackInterval;

    public RangedAttackComponent(Duration attackInterval) {
        timer = new Timer();
        isAttacking = false;
        this.attackInterval = attackInterval;
    }

    @Override
    public void onAdded() {
        state = entity.getComponent(StateComponent.class);
    }

    @Override
    public void onUpdate(double tpf) {
        timer.update(tpf);
    }

    public void attack(Entity target) {
        if (state.isIn(EntityStates.ATTACK,EntityStates.DEATH)) {
            return;
        }

        if (isAttacking) {
            return;
        }

        isAttacking = true;

        state.changeState(EntityStates.ATTACK);

        timer.runOnceAfter(() -> {
            if (!state.isIn(EntityStates.DEATH)) {
                GameWorld world = FXGL.getGameWorld();

                SpawnData data = new SpawnData();
                data.put("position", entity.getCenter());
                data.put("target", new Point2D(target.getX(), target.getY()));

                world.spawn("enemy_bullet", data);
            }
        }, Constants.Common.HURT_DELAY);

        timer.runOnceAfter(() -> {
            if (!state.isIn(EntityStates.DEATH)) {
                state.changeState(EntityStates.IDLE);
            }
            isAttacking = false;
        }, attackInterval);
    }
}
