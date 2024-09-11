package dev.csu.survivor.component.enemy;

import com.almasb.fxgl.dsl.FXGL;
import dev.csu.survivor.component.base.HurtComponent;

public class EnemyHurtComponent extends HurtComponent
{
    @Override
    public void onDeath()
    {
        FXGL.getGameWorld().spawn("gold", entity.getPosition());
        super.onDeath();
    }
}
