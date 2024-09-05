package dev.csu.survivor.component;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.time.LocalTimer;
import com.almasb.fxgl.time.Timer;
import dev.csu.survivor.Constants;
import dev.csu.survivor.enums.EntityStates;
import dev.csu.survivor.enums.EntityType;
import dev.csu.survivor.ui.menu.GameOverMenu;

public class HurtComponent extends Component
{
    protected final Timer timer;
    protected final LocalTimer hurtCooldown;
    protected StateComponent state;
    protected HealthIntComponent health;

    public HurtComponent()
    {
        timer = new Timer();
        hurtCooldown = FXGL.newLocalTimer();
    }

    @Override
    public void onAdded()
    {
        state = entity.getComponent(StateComponent.class);
        health = entity.getComponent(HealthIntComponent.class);
    }

    @Override
    public void onUpdate(double tpf)
    {
        timer.update(tpf);
    }

    public void hurt(Entity attacker)
    {
        // Add cooldown for hurt
        if (hurtCooldown.elapsed(Constants.Common.HURT_DURATION))
        {
            hurtCooldown.capture();

            state.changeState(EntityStates.HURT);

            health.damage(1);

            if (health.isZero())
            {
                state.changeState(EntityStates.DEATH);
                timer.runOnceAfter(() ->
                {
                    if (entity.isType(EntityType.PLAYER))
                        FXGL.getWindowService().pushSubScene(new GameOverMenu());
                    else entity.removeFromWorld();
                }, Constants.Common.DEATH_DELAY);

            } else timer.runOnceAfter(() -> state.changeState(EntityStates.IDLE), Constants.Common.HURT_DURATION);
        }
    }
}
