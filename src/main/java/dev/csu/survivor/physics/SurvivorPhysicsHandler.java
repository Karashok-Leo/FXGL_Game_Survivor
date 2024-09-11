package dev.csu.survivor.physics;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import dev.csu.survivor.component.base.AttributeComponent;
import dev.csu.survivor.component.base.HurtComponent;
import dev.csu.survivor.component.base.OwnableComponent;
import dev.csu.survivor.component.enemy.MeleeAttackComponent;
import dev.csu.survivor.component.player.GoldComponent;
import dev.csu.survivor.enums.AttributeType;
import dev.csu.survivor.enums.EnemyType;
import dev.csu.survivor.enums.EntityType;
import dev.csu.survivor.util.ComponentUtil;

/**
 * 所有实体碰撞的初始化位置
 */
public class SurvivorPhysicsHandler
{
    public void init(PhysicsWorld physicsWorld)
    {
        physicsWorld.setGravity(0, 0);

        physicsWorld.addCollisionHandler(
                new CollisionHandler(
                        EntityType.PLAYER,
                        EnemyType.MELEE_ENEMY
                )
                {
                    @Override
                    protected void onCollision(Entity a, Entity b)
                    {
                        b.getComponent(MeleeAttackComponent.class).attack(a);
                    }
                }
        );

        physicsWorld.addCollisionHandler(
                new CollisionHandler(
                        EntityType.GOLD,
                        EntityType.PLAYER
                )
                {
                    @Override
                    protected void onCollision(Entity a, Entity b)
                    {
                        b.getComponent(GoldComponent.class).collect(a);
                    }
                }
        );

        for (EnemyType enemyType : EnemyType.values())
        {
            physicsWorld.addCollisionHandler(
                    new CollisionHandler(
                            EntityType.PLAYER_BULLET,
                            enemyType
                    )
                    {
                        @Override
                        protected void onCollision(Entity bullet, Entity enemy)
                        {
                            applyOwnerDamage(bullet, enemy);
                        }
                    }
            );
            physicsWorld.addCollisionHandler(
                    new CollisionHandler(
                            EntityType.BOOMERANG,
                            enemyType
                    )
                    {
                        @Override
                        protected void onCollision(Entity boomerang, Entity enemy)
                        {
                            applyOwnerDamage(boomerang, enemy);
                        }
                    }
            );
        }

        physicsWorld.addCollisionHandler(
                new CollisionHandler(
                        EntityType.ENEMY_BULLET,
                        EntityType.PLAYER
                )
                {
                    @Override
                    protected void onCollision(Entity bullet, Entity player)
                    {
                        applyOwnerDamage(bullet, player);
                    }
                }
        );
    }

    private void applyOwnerDamage(Entity ownable, Entity hurt)
    {
        Entity owner = ownable.getComponent(OwnableComponent.class).getOwner();

        AttributeComponent attributeComponent = owner.getComponent(AttributeComponent.class);

        double damage = attributeComponent.getAttributeValue(AttributeType.DAMAGE);

        ComponentUtil.findAndConsumeComponentByClass(
                hurt,
                HurtComponent.class,
                hurtComponent -> hurtComponent.hurt(ownable, damage)
        );
    }
}
