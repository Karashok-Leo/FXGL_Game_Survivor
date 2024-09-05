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

    public void addVelocity(Vec2 v)
    {
        this.addVelocity(v.x, v.y);
    }

    public void addVelocity(double vx, double vy)
    {
        this.velocity.addLocal(vx, vy);
    }

    @Override
    public void onAdded()
    {
        this.state = this.entity.getComponent(StateComponent.class);
    }

    @Override
    public void onUpdate(double tpf)
    {
        if (state.isIn(EntityStates.DEATH)) return;

        if (isUnmoving())
        {
            if (state.isIn(EntityStates.RUN))
                state.changeState(EntityStates.IDLE);
        } else
        {
            if (state.isIn(EntityStates.IDLE))
                state.changeState(EntityStates.RUN);
        }

        if (!state.isIn(EntityStates.IDLE))
            entity.getComponent(AnimationComponent.class)
                    .updateDirection(velocity);

        // Move
        Vec2 vector = velocity.normalize().mulLocal(speed);
        entity.translate(vector);

        // Reset velocity
        velocity.reset();
    }

    protected boolean isUnmoving()
    {
        return velocity.isNearlyEqualTo(Point2D.ZERO);
    }
}
