package dev.csu.survivor.factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.dsl.components.KeepOnScreenComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.*;
import dev.csu.survivor.enums.EntityStates;
import dev.csu.survivor.enums.EntityType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SurvivorEntityFactory implements EntityFactory
{
    @Spawns("player")
    public Entity newPlayer(SpawnData data)
    {
        return FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(data.getX(), data.getY())
                .bbox(new HitBox(BoundingShape.circle(Constants.Common.PLAYER_HIT_BOX_RADIUS)))
                .with(new StateComponent(EntityStates.IDLE))
                .with(new MotionComponent(Constants.Common.PLAYER_SPEED))
                .with(ComponentFactory.newPlayerAnimationComponent())
                .with(new HealthIntComponent(Constants.Common.PLAYER_INITIAL_MAX_HEALTH))
                .with(new HurtComponent())
                .with(new GoldComponent())
                .with(new InventoryComponent())
                .with(new KeepOnScreenComponent())
                .collidable()
                .build();
    }

    @Spawns("land")
    public Entity newLand(SpawnData data)
    {
        return FXGL.entityBuilder()
                .with(new RandomLandComponent())
                .build();
    }

    @Spawns("bush")
    public Entity newBush(SpawnData data)
    {
        return FXGL.entityBuilder()
                .at(data.getX(), data.getY())
                .with(new RandomBushComponent())
                .build();
    }

    @Spawns("enemy")
    public Entity newEnemy(SpawnData data)
    {
        return FXGL.entityBuilder()
                .type(EntityType.ENEMY)
                .at(data.getX(), data.getY())
                .bbox(new HitBox(BoundingShape.circle(Constants.Common.ENEMY_HIT_BOX_RADIUS)))
                .with(new StateComponent(EntityStates.IDLE))
                .with(ComponentFactory.newRandomMotionComponent(Constants.Common.ENEMY_MIN_SPEED, Constants.Common.ENEMY_MAX_SPEED))
                .with(ComponentFactory.newEnemyAnimationComponent())
                .with(new HealthIntComponent(Constants.Common.ENEMY_INITIAL_MAX_HEALTH))
                .with(new HealthBarViewComponent(-16, -32 - 14, Constants.Client.ENEMY_HEALTH_BAR_WIDTH, Constants.Client.ENEMY_HEALTH_BAR_HEIGHT, Color.RED))
                .with(new AttackComponent())
                .with(new HurtComponent())
                .with(new EnemyComponent())
                .collidable()
                .build();
    }

    @Spawns("gold")
    public Entity newGold(SpawnData data)
    {
        return FXGL.entityBuilder()
                .type(EntityType.GOLD)
                .at(data.getX(), data.getY())
                .bbox(new HitBox(BoundingShape.circle(Constants.Common.GOLD_HIT_BOX_RADIUS)))
                .with(new GoldAnimationComponent())
                .collidable()
                .build();
    }

    @Spawns("test_bullet")
    public Entity newTestBullet(SpawnData data)
    {
        Point2D position = data.get("position");
        Point2D target = data.get("target");
        Entity bullet = FXGL.entityBuilder()
                .type(EntityType.BULLET)
                .viewWithBBox(new Rectangle(80, 30, Color.RED))
                .with(new ProjectileComponent(target.subtract(position), 600))
                .with(new OffscreenCleanComponent())
                .collidable()
                .build();
        BoundingBoxComponent bbox = bullet.getBoundingBoxComponent();
        bullet.setPosition(position.subtract(bbox.getWidth() / 2, bbox.getHeight() / 2));
        return bullet;
    }
}
