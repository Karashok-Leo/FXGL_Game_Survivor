package dev.csu.survivor;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import dev.csu.survivor.component.AnimationComponent;
import dev.csu.survivor.component.MotionComponent;
import dev.csu.survivor.enums.EntityType;

public class SurvivorEntityFactory implements EntityFactory
{
    @Spawns("player")
    public Entity newPlayer(SpawnData data)
    {
        return FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .with(new MotionComponent(), new AnimationComponent())
                .collidable()
                .buildAndAttach();
    }
}
