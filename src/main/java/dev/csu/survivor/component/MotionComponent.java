package dev.csu.survivor.component;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.state.StateComponent;
import dev.csu.survivor.enums.EntityStates;
import javafx.geometry.Point2D;

public class MotionComponent extends Component
{
    protected final double speed;
    protected final Vec2 velocity = new Vec2();
    protected StateComponent state;

    public MotionComponent(double speed)
    {
        this.speed = speed;
    }

    public Vec2 getVelocity()
    {
        return velocity;
    }

    @Override
    public void onAdded()
    {
        state = entity.getComponent(StateComponent.class);
    }

    @Override
    public void onUpdate(double tpf)
    {
        if (state.getCurrentState() != EntityStates.ATTACK)
            state.changeState(
                    velocity.isNearlyEqualTo(Point2D.ZERO) ?
                            EntityStates.IDLE : EntityStates.RUN
            );
        entity.translate(velocity.normalize().mulLocal(speed));
    }
}
