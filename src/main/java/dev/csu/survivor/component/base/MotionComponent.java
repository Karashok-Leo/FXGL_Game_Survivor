package dev.csu.survivor.component.base;


import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.state.StateComponent;
import dev.csu.survivor.enums.AttributeType;
import dev.csu.survivor.enums.EntityStates;
import javafx.geometry.Point2D;

/**
 * 控制实体移动的组件
 * <p>依赖的组件: {@link StateComponent} {@link AttributeComponent} </p>
 */
public class MotionComponent extends Component
{
    protected final Vec2 velocity = new Vec2();
    protected StateComponent state;
    protected AttributeComponent attribute;

    /**
     * 向实体指定方向的动量
     * 实际的移动速度由实体的移动速度属性决定，而与 v 的长度无关
     * 施加的动量会在每帧被清除，所以保持移动需要持续调用该方法
     *
     * @param v 动量方向
     */
    public void addVelocity(Vec2 v)
    {
        this.addVelocity(v.x, v.y);
    }

    /**
     * 向实体指定方向的动量
     * 实际的移动速度由实体的移动速度属性决定，而与 vx，vy 无关
     * 施加的动量会在每帧被清除，所以保持移动需要持续调用该方法
     *
     * @param vx 动量方向的x分量
     * @param vy 动量方向的y分量
     */
    public void addVelocity(double vx, double vy)
    {
        this.velocity.addLocal(vx, vy);
    }

    @Override
    public void onAdded()
    {
        this.state = this.entity.getComponent(StateComponent.class);
        this.attribute = this.entity.getComponent(AttributeComponent.class);
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
            entity.getComponent(MultiAnimationComponent.class)
                    .updateDirection(velocity);

        // Move
        double speed = attribute.getAttributeValue(AttributeType.SPEED);

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
