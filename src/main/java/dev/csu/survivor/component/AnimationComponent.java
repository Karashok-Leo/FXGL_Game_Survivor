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
import java.util.HashMap;
import java.util.Map;

public class AnimationComponent extends Component
{
    private final AnimatedTexture texture;
    private final AnimationMap animationMap;
    private Direction direction = Direction.DOWN;

    public AnimationComponent()
    {
        this.animationMap = new AnimationMap(
                "player",
                new StateEntry("idle", 2, new int[]{4, 12, 12, 12}),
                new StateEntry("run", 0.8, 8),
                new StateEntry("hurt", 0.5, 5),
                new StateEntry("death", 0.9, 7)
        );
//        this.animationMap = new AnimationMap(
//                "enemy",
//                new StateEntry("run", 0.8, 8),
//                new StateEntry("attack", 0.8, 8),
//                new StateEntry("hurt", 0.6, 6),
//                new StateEntry("death", 1.2, 8)
//        );

        texture = new AnimatedTexture(animationMap.get("idle", Direction.DOWN));
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

        // TODO: update direction

        if (isIdle) loopAnimation(animationMap.get("idle", direction));
        else loopAnimation(animationMap.get("run", direction));
    }

    private void loopAnimation(AnimationChannel channel)
    {
        if (texture.getAnimationChannel() != channel)
            texture.loopAnimationChannel(channel);
    }

    public record StateEntry(String state, double duration, EnumMap<Direction, Integer> nums)
    {
        public StateEntry(String state, double duration, int[] nums)
        {
            this(state, duration, new EnumMap<>(Direction.class));
            if (nums.length != Direction.values().length) throw new IllegalArgumentException();
            for (Direction direction : Direction.values())
                this.nums.put(direction, nums[direction.ordinal()]);
        }

        public StateEntry(String state, double duration, int num)
        {
            this(state, duration, new EnumMap<>(Direction.class));
            for (Direction direction : Direction.values())
                this.nums.put(direction, num);
        }
    }

    public record AnimationMap(Map<String, EnumMap<Direction, AnimationChannel>> animations)
    {
        AnimationMap(String owner, StateEntry... statesEntries)
        {
            this(new HashMap<>());
            for (StateEntry entry : statesEntries)
            {
                EnumMap<Direction, AnimationChannel> map = new EnumMap<>(Direction.class);
                for (Direction direction : Direction.values())
                    map.put(
                            direction,
                            new AnimationChannel(
                                    FXGL.image("%s/%s/%s.png".formatted(owner, entry.state, direction.name)),
                                    Duration.seconds(entry.duration),
                                    entry.nums.get(direction)
                            )
                    );
                animations.put(entry.state, map);
            }
        }

        public AnimationChannel get(String state, Direction direction)
        {
            return animations.get(state).get(direction);
        }
    }
}
