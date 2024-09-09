package dev.csu.survivor.physics;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import dev.csu.survivor.component.AttackComponent;
import dev.csu.survivor.component.GoldComponent;
import dev.csu.survivor.component.HurtComponent;
import dev.csu.survivor.enums.EntityType;

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
                        b.getComponent(AttackComponent.class).attack(a);
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
                        b.getComponent(HurtComponent.class).hurt(a, 2);
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
                        b.getComponent(HurtComponent.class).hurt(a, 2);
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
    }
}
