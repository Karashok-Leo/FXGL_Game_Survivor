package dev.csu.survivor.factory;

import com.almasb.fxgl.dsl.FXGL;
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
import dev.csu.survivor.component.base.HealthComponent;
import dev.csu.survivor.component.base.MotionComponent;
import dev.csu.survivor.component.base.OwnableComponent;
import dev.csu.survivor.component.base.SimpleAnimationComponent;
import dev.csu.survivor.component.enemy.*;
import dev.csu.survivor.component.misc.HealthBarViewComponent;
import dev.csu.survivor.component.misc.land.RandomBushComponent;
import dev.csu.survivor.component.misc.land.RandomLandComponent;
import dev.csu.survivor.component.player.GoldComponent;
import dev.csu.survivor.component.player.PlayerHurtComponent;
import dev.csu.survivor.enums.EnemyType;
import dev.csu.survivor.enums.EntityStates;
import dev.csu.survivor.enums.EntityType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

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
                .with(ComponentFactory.newPlayerAttributeComponent())
                .with(new MotionComponent())
                .with(ComponentFactory.newPlayerAnimationComponent())
                .with(new HealthComponent())
                .with(new PlayerHurtComponent())
                .with(new GoldComponent())
                .with(ComponentFactory.newPlayerInventoryComponent())
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

    @Spawns("melee_enemy")
    public Entity newMeleeEnemy(SpawnData data)
    {
        int wave = data.get("wave");
        return FXGL.entityBuilder()
                .type(EnemyType.MELEE_ENEMY)
                .at(data.getX(), data.getY())
                .bbox(new HitBox(BoundingShape.circle(Constants.Common.ENEMY_HIT_BOX_RADIUS)))
                .with(new StateComponent(EntityStates.IDLE))
                .with(ComponentFactory.newEnemyAttributeComponent(wave))
                .with(new MotionComponent())
                .with(ComponentFactory.newMeleeEnemyAnimationComponent())
                .with(new HealthComponent())
                .with(new HealthBarViewComponent(-16, -32 - 14, Constants.Client.ENEMY_HEALTH_BAR_WIDTH, Constants.Client.ENEMY_HEALTH_BAR_HEIGHT, Color.RED))
                .with(new MeleeAttackComponent())
                .with(new EnemyHurtComponent())
                .with(new MeleeEnemyComponent())
                .collidable()
                .build();
    }

    @Spawns("ranged_enemy")
    public Entity newRangedEnemy(SpawnData data)
    {
        int wave = data.get("wave");
        return FXGL.entityBuilder()
                .type(EnemyType.RANGED_ENEMY)
                .at(data.getX(), data.getY())
                .bbox(new HitBox(BoundingShape.circle(Constants.Common.ENEMY_HIT_BOX_RADIUS)))
                .with(new StateComponent(EntityStates.IDLE))
                .with(ComponentFactory.newEnemyAttributeComponent(wave))
                .with(new MotionComponent())
                .with(ComponentFactory.newRangedEnemyAnimationComponent())
                .with(new HealthComponent())
                .with(new HealthBarViewComponent(-16, -32 - 14, Constants.Client.ENEMY_HEALTH_BAR_WIDTH, Constants.Client.ENEMY_HEALTH_BAR_HEIGHT, Color.RED))
                .with(new EnemyHurtComponent())
                .with(new RangedAttackComponent())
                .with(new RangedEnemyComponent())
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
                .with(new SimpleAnimationComponent(Constants.AnimationMaps.COIN))
                .collidable()
                .build();
    }

    @Spawns("player_bullet")
    public Entity newPlayerBullet(SpawnData data)
    {
        Point2D position = data.get("position");
        Point2D target = data.get("target");
        Entity owner = data.get("owner");
        Entity bullet = FXGL.entityBuilder()
                .type(EntityType.PLAYER_BULLET)
                .bbox(new HitBox(BoundingShape.circle(Constants.Common.PLAYER_HIT_BOX_RADIUS)))
                .view("bullet.png")
                .with(new ProjectileComponent(target.subtract(position), Constants.Common.PLAYER_BULLET_SPEED))
                .with(new OffscreenCleanComponent())
                .with(new OwnableComponent(() -> owner))
                .collidable()
                .build();
        bullet.setScaleX(1.5);
        bullet.setScaleY(1.5);
        BoundingBoxComponent bbox = bullet.getBoundingBoxComponent();
        bullet.setPosition(position.subtract(bbox.getWidth() / 2, bbox.getHeight() / 2));
        return bullet;
    }

    @Spawns("enemy_bullet")
    public Entity newEnemyBullet(SpawnData data)
    {
        Point2D position = data.get("position");
        Point2D target = data.get("target");
        Entity owner = data.get("owner");
        Entity bullet = FXGL.entityBuilder()
                .type(EntityType.ENEMY_BULLET)
                .bbox(BoundingShape.box(20, 40))
                .with(new SimpleAnimationComponent(Constants.AnimationMaps.ENEMY_BULLET))
                .with(new OwnableComponent(() -> owner))
                .with(new ProjectileComponent(target.subtract(position), Constants.Common.RANGED_ENEMY_BULLET_SPEED))
                .with(new OffscreenCleanComponent())
                .collidable()
                .build();
        BoundingBoxComponent bbox = bullet.getBoundingBoxComponent();
        bullet.setPosition(position.subtract(bbox.getWidth() / 2, bbox.getHeight() / 2));
        return bullet;
    }
}
