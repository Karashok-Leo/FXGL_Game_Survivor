package dev.csu.survivor.factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.dsl.components.KeepOnScreenComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.*;
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
                .with(new MotionComponent(Constants.GameData.PLAYER_SPEED))
                .with(ComponentFactory.newPlayerAnimationComponent())
                .with(new HealthIntComponent(Constants.GameData.PLAYER_INITIAL_MAX_HEALTH))
                .with(new HurtComponent())
                .with(new KeepOnScreenComponent())
                .bbox(new HitBox(BoundingShape.circle(Constants.GameData.PLAYER_HIT_BOX_RADIUS)))
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
                .with(ComponentFactory.newRandomMotionComponent(Constants.GameData.ENEMY_MIN_SPEED, Constants.GameData.ENEMY_MAX_SPEED))
                .with(ComponentFactory.newEnemyAnimationComponent())
                .with(new HealthIntComponent(Constants.GameData.ENEMY_INITIAL_MAX_HEALTH))
                .with(new HealthBarComponent(-36, -64, Constants.GameData.ENEMY_INITIAL_MAX_HEALTH))
                .with(new AttackComponent())
                .with(new HurtComponent())
                .with(new EnemyComponent())
                .bbox(new HitBox(BoundingShape.circle(Constants.GameData.ENEMY_HIT_BOX_RADIUS)))
                .collidable()
                .buildAndAttach();
    }
}
