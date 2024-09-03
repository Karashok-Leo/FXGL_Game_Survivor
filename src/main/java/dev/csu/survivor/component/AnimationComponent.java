package dev.csu.survivor.component;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import dev.csu.survivor.enums.Direction;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.EnumMap;

public class AnimationComponent extends Component
{
    private Direction direction = Direction.DOWN;
    private final AnimatedTexture texture;
    private final EnumMap<Direction, AnimationChannel> animIdle = new EnumMap<>(Direction.class);
    private final EnumMap<Direction, AnimationChannel> animRun = new EnumMap<>(Direction.class);

    public AnimationComponent()
    {
        animIdle.put(Direction.UP, new AnimationChannel(FXGL.image("Unarmed_Idle_back.png"), Duration.seconds(2), 4));
        animIdle.put(Direction.DOWN, new AnimationChannel(FXGL.image("Unarmed_Idle_front.png"), Duration.seconds(2), 12));
        animIdle.put(Direction.LEFT, new AnimationChannel(FXGL.image("Unarmed_Idle_side_left.png"), Duration.seconds(2), 12));
        animIdle.put(Direction.RIGHT, new AnimationChannel(FXGL.image("Unarmed_Idle_side_right.png"), Duration.seconds(2), 12));

        animRun.put(Direction.UP, new AnimationChannel(FXGL.image("Unarmed_Run_back.png"), Duration.seconds(0.8), 8));
        animRun.put(Direction.DOWN, new AnimationChannel(FXGL.image("Unarmed_Run_front.png"), Duration.seconds(0.8), 8));
        animRun.put(Direction.LEFT, new AnimationChannel(FXGL.image("Unarmed_Run_side_left.png"), Duration.seconds(0.8), 8));
        animRun.put(Direction.RIGHT, new AnimationChannel(FXGL.image("Unarmed_Run_side_right.png"), Duration.seconds(0.8), 8));

        texture = new AnimatedTexture(animIdle.get(Direction.DOWN));
    }

    public void setDirection(Direction direction)
    {
        this.direction = direction;
    }

    @Override
    public void onAdded()
    {
        entity.getViewComponent().addChild(texture);
        entity.setScaleX(2);
        entity.setScaleY(2);
    }

    @Override
    public void onUpdate(double tpf)
    {
        Vec2 velocity = entity.getComponent(MotionComponent.class).getVelocity();
        boolean isIdle = velocity.isNearlyEqualTo(Point2D.ZERO);

        if (isIdle) loopAnimation(animIdle.get(direction));
        else loopAnimation(animRun.get(direction));
    }

    private void loopAnimation(AnimationChannel channel)
    {
        if (texture.getAnimationChannel() != channel)
            texture.loopAnimationChannel(channel);
    }
}
