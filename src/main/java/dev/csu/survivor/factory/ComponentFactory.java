package dev.csu.survivor.factory;

import com.almasb.fxgl.core.math.FXGLMath;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.AnimationComponent;
import dev.csu.survivor.component.AttributeComponent;
import dev.csu.survivor.component.InventoryComponent;
import dev.csu.survivor.enums.AttributeType;
import dev.csu.survivor.enums.ItemType;
import dev.csu.survivor.world.attribute.AttributeInstance;

public class ComponentFactory
{
    /**
     * @return a new AnimationComponent for the animations of player
     */
    public static AnimationComponent newPlayerAnimationComponent()
    {
        return new AnimationComponent(Constants.AnimationMaps.PLAYER_ANIMATION_MAP);
    }

    /**
     * @return a new AnimationComponent for the animations of melee enemy
     */
    public static AnimationComponent newMeleeEnemyAnimationComponent()
    {
        return new AnimationComponent(Constants.AnimationMaps.MELEE_ENEMY_ANIMATION_MAP);
    }

    /**
     * @return a new AnimationComponent for the animations of ranged enemy
     */
    public static AnimationComponent newRangedEnemyAnimationComponent()
    {
        return new AnimationComponent(Constants.AnimationMaps.RANGED_ENEMY_ANIMATION_MAP);
    }

    public static AttributeComponent newPlayerAttributeComponent()
    {
        return new AttributeComponent(
                new AttributeInstance(AttributeType.MAX_HEALTH, Constants.Common.PLAYER_INITIAL_MAX_HEALTH),
                new AttributeInstance(AttributeType.SPEED, Constants.Common.PLAYER_INITIAL_SPEED),
                new AttributeInstance(AttributeType.DAMAGE, Constants.Common.PLAYER_INITIAL_DAMAGE),
                new AttributeInstance(AttributeType.REGENERATION, 0)
        );
    }

    public static AttributeComponent newMeleeEnemyAttributeComponent()
    {
        return new AttributeComponent(
                new AttributeInstance(AttributeType.MAX_HEALTH, Constants.Common.MELEE_ENEMY_INITIAL_MAX_HEALTH),
                new AttributeInstance(AttributeType.SPEED, FXGLMath.random(Constants.Common.MELEE_ENEMY_INITIAL_MIN_SPEED, Constants.Common.MELEE_ENEMY_INITIAL_MAX_SPEED)),
                new AttributeInstance(AttributeType.DAMAGE, Constants.Common.MELEE_ENEMY_INITIAL_DAMAGE)
        );
    }

    public static AttributeComponent newRangedEnemyAttributeComponent()
    {
        return new AttributeComponent(
                new AttributeInstance(AttributeType.MAX_HEALTH, Constants.Common.RANGED_ENEMY_INITIAL_MAX_HEALTH),
                new AttributeInstance(AttributeType.SPEED, FXGLMath.random(Constants.Common.RANGED_ENEMY_INITIAL_MIN_SPEED, Constants.Common.RANGED_ENEMY_INITIAL_MAX_SPEED)),
                new AttributeInstance(AttributeType.DAMAGE, Constants.Common.RANGED_ENEMY_INITIAL_DAMAGE)
        );
    }

    public static InventoryComponent newPlayerInventoryComponent()
    {
        InventoryComponent component = new InventoryComponent();
        component.getInventory().add(new InventoryComponent.ItemEntry(ItemType.BOOMERANG_ATTACK));
        for (int i = 0; i < 25; i++)
            component.getInventory().add(new InventoryComponent.ItemEntry(ItemType.HEALING_POTION));
        return component;
    }
}
