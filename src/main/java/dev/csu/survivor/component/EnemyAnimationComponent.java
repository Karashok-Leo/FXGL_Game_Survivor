package dev.csu.survivor.component;

import com.almasb.fxgl.core.math.Vec2;
import dev.csu.survivor.enums.Direction;
import dev.csu.survivor.util.MathUtil;

public class EnemyAnimationComponent extends AnimationComponent
{
    public EnemyAnimationComponent()
    {
        super(
                new AnimationMap(
                        "enemy",
                        new StateEntry("run", 0.8, 8),
                        new StateEntry("attack", 0.8, 8),
                        new StateEntry("hurt", 0.6, 6),
                        new StateEntry("death", 1.2, 8)
                ),
                "run",
                Direction.DOWN
        );
    }

    @Override
    public void onUpdate(double tpf)
    {
        Vec2 velocity = entity.getComponent(EnemyMotionComponent.class).getVelocity();
        direction = MathUtil.getDirectionByVec2(velocity);
        // TODO: attack & hurt & death

        loopAnimation(animationMap.get("run", direction));
    }
}
