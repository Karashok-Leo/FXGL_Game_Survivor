package dev.csu.survivor.component;

import com.almasb.fxgl.core.math.Vec2;
import dev.csu.survivor.enums.EntityStates;
import dev.csu.survivor.util.MathUtil;

public class EnemyAnimationComponent extends AnimationComponent
{
    public EnemyAnimationComponent()
    {
        super(
                new AnimationMap(
                        "enemy",
                        new StateEntry(EntityStates.RUN, 0.8, 8),
                        new StateEntry(EntityStates.ATTACK, 0.8, 8),
                        new StateEntry(EntityStates.HURT, 0.6, 6),
                        new StateEntry(EntityStates.DEATH, 1.2, 8)
                )
        );
    }

    @Override
    public void onUpdate(double tpf)
    {
        Vec2 velocity = entity.getComponent(EnemyMotionComponent.class).getVelocity();
        direction = MathUtil.getDirectionByVec2(velocity);
        // TODO: attack & hurt & death

        super.onUpdate(tpf);
    }
}
