package dev.csu.survivor.factory;

import com.almasb.fxgl.core.math.FXGLMath;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.base.AttributeComponent;
import dev.csu.survivor.component.base.MultiAnimationComponent;
import dev.csu.survivor.component.player.InventoryComponent;
import dev.csu.survivor.enums.AttributeType;
import dev.csu.survivor.world.attribute.AttributeInstance;

public class ComponentFactory
{
    /**
     * @return a new AnimationComponent for the animations of player
     */
    public static MultiAnimationComponent newPlayerAnimationComponent()
    {
        return new MultiAnimationComponent(Constants.AnimationMaps.PLAYER_ANIMATION_MAP);
    }

    /**
     * @return a new AnimationComponent for the animations of melee enemy
     */
    public static MultiAnimationComponent newMeleeEnemyAnimationComponent()
    {
        return new MultiAnimationComponent(Constants.AnimationMaps.MELEE_ENEMY_ANIMATION_MAP);
    }

    /**
     * @return a new AnimationComponent for the animations of ranged enemy
     */
    public static MultiAnimationComponent newRangedEnemyAnimationComponent()
    {
        return new MultiAnimationComponent(Constants.AnimationMaps.RANGED_ENEMY_ANIMATION_MAP);
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

    public static AttributeComponent newEnemyAttributeComponent(int wave)
    {
        return new AttributeComponent(
                new AttributeInstance(
                        AttributeType.MAX_HEALTH,
                        Constants.Common.ENEMY_INITIAL_MAX_HEALTH + wave
                ),
                new AttributeInstance(
                        AttributeType.SPEED,
                        FXGLMath.random(
                                Constants.Common.ENEMY_INITIAL_MIN_SPEED + wave * 0.1,
                                Constants.Common.ENEMY_INITIAL_MAX_SPEED + wave * 0.1
                        )
                ),
                new AttributeInstance(
                        AttributeType.DAMAGE,
                        Constants.Common.ENEMY_INITIAL_DAMAGE + wave
                )
        );
    }

    public static InventoryComponent newPlayerInventoryComponent()
    {
        return new InventoryComponent();
    }
}
