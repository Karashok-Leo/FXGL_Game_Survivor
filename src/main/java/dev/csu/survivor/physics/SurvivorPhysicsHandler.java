package dev.csu.survivor.physics;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import dev.csu.survivor.component.AttackComponent;
import dev.csu.survivor.enums.EntityType;

public class SurvivorPhysicsHandler
{
    public void init(PhysicsWorld physicsWorld)
    {
        physicsWorld.setGravity(0, 0);
        physicsWorld.addCollisionHandler(
                new CollisionHandler(
                        EntityType.PLAYER,
                        EntityType.ENEMY
                )
                {
                    @Override
                    protected void onCollision(Entity a, Entity b)
                    {
                        b.getComponent(AttackComponent.class).attack(a);
                    }
                }
        );
    }
}
