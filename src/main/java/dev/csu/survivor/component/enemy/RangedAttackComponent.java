package dev.csu.survivor.component.enemy;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import dev.csu.survivor.component.base.AttackComponent;
import dev.csu.survivor.enums.EntityStates;
import javafx.geometry.Point2D;

public class RangedAttackComponent extends AttackComponent
{
    @Override
    protected void doAttack(Entity target)
    {
        if (!state.isIn(EntityStates.DEATH))
        {
            GameWorld world = FXGL.getGameWorld();

            SpawnData data = new SpawnData();
            data.put("position", entity.getCenter());
            data.put("target", new Point2D(target.getX(), target.getY()));

            world.spawn("enemy_bullet", data);
        }
    }
}
