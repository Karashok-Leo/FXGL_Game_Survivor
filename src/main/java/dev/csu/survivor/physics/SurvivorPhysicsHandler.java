package dev.csu.survivor.physics;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import dev.csu.survivor.component.MeleeAttackComponent;
import dev.csu.survivor.component.GoldComponent;
import dev.csu.survivor.component.HurtComponent;
import dev.csu.survivor.component.AttributeComponent;
import dev.csu.survivor.enums.AttributeType;
import dev.csu.survivor.enums.EntityType;
import dev.csu.survivor.world.SurvivorGameWorld;

public class SurvivorPhysicsHandler
{
    public void init(PhysicsWorld physicsWorld)
    {
        physicsWorld.setGravity(0, 0);

        physicsWorld.addCollisionHandler(
                new CollisionHandler(
                        EntityType.PLAYER,
                        EntityType.MELEE_ENEMY
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
                        EntityType.BULLET,
                        EntityType.MELEE_ENEMY
                )
                {
                    @Override
                    protected void onCollision(Entity a, Entity b)
                    {
                        applyPlayerDamage(a, b);
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

        physicsWorld.addCollisionHandler(
                new CollisionHandler(
                        EntityType.BULLET,
                        EntityType.RANGED_ENEMY
                )
                {
                    @Override
                    protected void onCollision(Entity a, Entity b)
                    {
                        applyPlayerDamage(a, b);
                    }
                }
        );

        physicsWorld.addCollisionHandler(
                new CollisionHandler(
                        EntityType.ENEMY_BULLET,
                        EntityType.PLAYER
                )
                {
                    @Override
                    protected void onCollision(Entity a, Entity b)
                    {
                        b.getComponent(HurtComponent.class).hurt(a, 2);
                    }
                }
        );

        physicsWorld.addCollisionHandler(
                new CollisionHandler(
                        EntityType.BOOMERANG,
                        EntityType.MELEE_ENEMY
                )
                {
                    @Override
                    protected void onCollision(Entity a, Entity b)
                    {
                        applyPlayerDamage(a, b);
                    }
                }
        );

        physicsWorld.addCollisionHandler(
                new CollisionHandler(
                        EntityType.BOOMERANG,
                        EntityType.RANGED_ENEMY
                )
                {
                    @Override
                    protected void onCollision(Entity a, Entity b)
                    {
                        applyPlayerDamage(a, b);
                    }
                }
        );
    }

    private void applyPlayerDamage(Entity entity, Entity enemy)
    {
        Entity player = SurvivorGameWorld.getPlayer();

        AttributeComponent attributeComponent = player.getComponent(AttributeComponent.class);

        enemy.getComponent(HurtComponent.class).hurt(entity, attributeComponent.getAttributeValue(AttributeType.DAMAGE));
    }
}
