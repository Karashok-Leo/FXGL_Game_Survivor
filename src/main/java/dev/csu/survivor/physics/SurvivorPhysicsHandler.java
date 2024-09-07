package dev.csu.survivor.physics;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import dev.csu.survivor.component.AttackComponent;
import dev.csu.survivor.component.GoldComponent;
import dev.csu.survivor.component.HurtComponent;
import dev.csu.survivor.component.InventoryComponent;
import dev.csu.survivor.enums.EntityType;
import dev.csu.survivor.enums.ItemType;

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
        physicsWorld.addCollisionHandler(
                new CollisionHandler(
                        EntityType.BULLET,
                        EntityType.ENEMY
                )
                {
                    @Override
                    protected void onCollision(Entity a, Entity b)
                    {
                        b.getComponent(HurtComponent.class).hurt(a);
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
                        //b.getComponent(InventoryComponent.class).addItemToList(b,ItemType.HealingMedicine.getItem());
                        //b.getComponent(InventoryComponent.class).addItemToList(b,ItemType.AccelerateCrystal.getItem());
                        //b.getComponent(InventoryComponent.class).addItemToList(b,ItemType.HealthCrystal.getItem());
                        b.getComponent(GoldComponent.class).collect(a);
                    }
                }
        );
    }
}
