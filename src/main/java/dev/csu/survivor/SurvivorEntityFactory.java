package dev.csu.survivor;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import dev.csu.survivor.component.EnemyAnimationComponent;
import dev.csu.survivor.component.MotionComponent;
import dev.csu.survivor.component.PlayerAnimationComponent;
import dev.csu.survivor.enums.EntityType;

public class SurvivorEntityFactory implements EntityFactory
{
    @Spawns("player")
    public Entity newPlayer(SpawnData data)
    {
        return FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(data.getX(), data.getY())
                .with(new MotionComponent(3), new PlayerAnimationComponent())
                .collidable()
                .buildAndAttach();
    }

    @Spawns("enemy")
    public Entity newEnemy(SpawnData data)
    {
        return FXGL.entityBuilder()
                .type(EntityType.ENEMY)
                .at(data.getX(), data.getY())
                .with(new MotionComponent(2), new EnemyAnimationComponent())
                .collidable()
                .buildAndAttach();
    }
}
