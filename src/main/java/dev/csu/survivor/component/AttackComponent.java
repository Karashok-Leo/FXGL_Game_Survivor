package dev.csu.survivor.component;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.time.Timer;
import dev.csu.survivor.Constants;
import dev.csu.survivor.enums.EntityStates;

public class AttackComponent extends Component
{
    protected final Timer timer;
    protected StateComponent state;

    public AttackComponent()
    {
        timer = new Timer();
    }

    @Override
    public void onAdded()
    {
        state = entity.getComponent(StateComponent.class);
    }

    @Override
    public void onUpdate(double tpf)
    {
        timer.update(tpf);
    }

    public void attack(Entity target)
    {
        if (state.isIn(EntityStates.ATTACK, EntityStates.DEATH)) return;

        state.changeState(EntityStates.ATTACK);

        timer.runOnceAfter(() -> target.getComponent(HurtComponent.class).hurt(entity), Constants.Common.HURT_DELAY);

        timer.runOnceAfter(() -> state.changeState(EntityStates.IDLE), Constants.Common.ATTACK_DURATION);
    }
}
