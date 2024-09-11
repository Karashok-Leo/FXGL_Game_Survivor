package dev.csu.survivor.component.base;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.time.LocalTimer;
import com.almasb.fxgl.time.Timer;
import dev.csu.survivor.Constants;
import dev.csu.survivor.enums.EntityStates;
import org.jetbrains.annotations.Nullable;

/**
 * 控制实体受伤及死亡的组件
 * 继承并重写 onDeath 方法以实现更复杂的死亡逻辑
 * <p>依赖的组件: {@link StateComponent} {@link HealthComponent} </p>
 */
public class HurtComponent extends Component
{
    protected final Timer timer;
    protected final LocalTimer hurtCooldown;
    protected StateComponent state;
    protected HealthComponent health;

    public HurtComponent()
    {
        timer = new Timer();
        hurtCooldown = FXGL.newLocalTimer();
    }

    @Override
    public void onAdded()
    {
        state = entity.getComponent(StateComponent.class);
        health = entity.getComponent(HealthComponent.class);
    }

    @Override
    public void onUpdate(double tpf)
    {
        timer.update(tpf);
    }

    /**
     * 实体死亡时调用
     */
    public void onDeath()
    {
        this.entity.removeFromWorld();
    }

    /**
     * 实体受伤时调用
     * <p>
     * 如玩家使用飞镖攻击敌人时，attacker为玩家，damage为玩家的伤害属性值，source为飞镖
     * </p>
     *
     * @param attacker 攻击者，间接的伤害来源
     * @param damage   实体受到的伤害
     * @param source   直接的伤害来源
     */
    public void hurt(Entity attacker, double damage, @Nullable Entity source)
    {
        // Add cooldown for hurt
        if (hurtCooldown.elapsed(Constants.Common.HURT_COOLDOWN) &&
            !health.isZero())
        {
            hurtCooldown.capture();

            state.changeState(EntityStates.HURT);

            health.damage(damage);

            if (health.isZero())
            {
                state.changeState(EntityStates.DEATH);
                timer.runOnceAfter(this::onDeath, Constants.Common.DEATH_DELAY);

            } else timer.runOnceAfter(() -> state.changeState(EntityStates.IDLE), Constants.Common.HURT_DURATION);
        }
    }

    public void hurt(Entity attacker, double damage)
    {
        this.hurt(attacker, damage, null);
    }
}
