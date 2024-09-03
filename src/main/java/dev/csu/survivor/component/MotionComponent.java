package dev.csu.survivor.component;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;

public class MotionComponent extends Component
{
    public static final double SPEED = 3;

    private final Vec2 velocity = new Vec2();

    public Vec2 getVelocity()
    {
        return velocity;
    }

    @Override
    public void onUpdate(double tpf)
    {
        entity.translate(velocity.normalize().mulLocal(SPEED));
    }
}
