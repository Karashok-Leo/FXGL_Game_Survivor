package dev.csu.survivor.factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.KeepOnScreenComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import dev.csu.survivor.component.AttackHurtComponent;
import dev.csu.survivor.component.EnemyComponent;
import dev.csu.survivor.component.MotionComponent;
import dev.csu.survivor.enums.EntityStates;
import dev.csu.survivor.enums.EntityType;

public class SurvivorEntityFactory implements EntityFactory
{
    @Spawns("player")
    public Entity newPlayer(SpawnData data)
    {
        return FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(data.getX(), data.getY())
                .with(new StateComponent(EntityStates.IDLE))
                .with(new MotionComponent(2))
                .with(ComponentFactory.newPlayerAnimationComponent())
                .with(new AttackHurtComponent())
                .with(new KeepOnScreenComponent())
                .bbox(new HitBox(BoundingShape.circle(7)))
                .collidable()
                .buildAndAttach();
    }

    @Spawns("enemy")
    public Entity newEnemy(SpawnData data)
    {
        return FXGL.entityBuilder()
                .type(EntityType.ENEMY)
                .at(data.getX(), data.getY())
                .with(new StateComponent(EntityStates.RUN))
                .with(new MotionComponent(0.8))
                .with(ComponentFactory.newEnemyAnimationComponent())
                .with(new AttackHurtComponent())
                .with(new EnemyComponent())
                .bbox(new HitBox(BoundingShape.circle(9)))
                .collidable()
                .buildAndAttach();
    }
}
