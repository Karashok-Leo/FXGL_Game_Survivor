package dev.csu.survivor.component;

import com.almasb.fxgl.core.math.Vec2;
import dev.csu.survivor.enums.Direction;
import javafx.geometry.Point2D;

public class PlayerAnimationComponent extends AnimationComponent
{
    public PlayerAnimationComponent()
    {
        super(
                new AnimationMap(
                        "player",
                        new StateEntry("idle", 2, new int[]{4, 12, 12, 12}),
                        new StateEntry("run", 0.8, 8),
                        new StateEntry("hurt", 0.5, 5),
                        new StateEntry("death", 0.9, 7)
                ),
                "idle",
                Direction.DOWN
        );
    }

    @Override
    public void onUpdate(double tpf)
    {
        Vec2 velocity = entity.getComponent(MotionComponent.class).getVelocity();
        boolean isIdle = velocity.isNearlyEqualTo(Point2D.ZERO);

        // TODO: hurt & death

        if (isIdle) loopAnimation(animationMap.get("idle", direction));
        else loopAnimation(animationMap.get("run", direction));
    }
}
