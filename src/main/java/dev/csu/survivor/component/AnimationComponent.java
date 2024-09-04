package dev.csu.survivor.component;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.state.EntityState;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import dev.csu.survivor.enums.Direction;
import javafx.geometry.Dimension2D;
import javafx.util.Duration;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public abstract class AnimationComponent extends Component
{
    protected final AnimationMap animationMap;
    protected StateComponent state;
    protected AnimatedTexture texture;
    protected Dimension2D dimension;
    protected Direction direction = Direction.DOWN;

    public AnimationComponent(AnimationMap animationMap)
    {
        this.animationMap = animationMap;
    }

    public Dimension2D getDimension()
    {
        return this.dimension;
    }

    public void setDirection(Direction direction)
    {
        this.direction = direction;
    }

    @Override
    public void onAdded()
    {
        this.state = entity.getComponent(StateComponent.class);
        this.texture = new AnimatedTexture(animationMap.get(state.getCurrentState(), direction));
        this.dimension = new Dimension2D(
                texture.getAnimationChannel().getFrameWidth(0),
                texture.getAnimationChannel().getFrameHeight(0)
        );
        this.entity.getViewComponent().addChild(texture);
        this.texture.setTranslateX(-dimension.getWidth() / 2.0);
        this.texture.setTranslateY(-dimension.getHeight() / 2.0);
        this.texture.loop();
        // TODO: dimension scaling
        this.entity.setScaleX(2);
        this.entity.setScaleY(2);
    }

    @Override
    public void onUpdate(double tpf)
    {
        loopAnimation(animationMap.get(state.getCurrentState(), direction));
    }

    protected void loopAnimation(AnimationChannel channel)
    {
        if (texture.getAnimationChannel() != channel)
            texture.loopAnimationChannel(channel);
    }

    public record StateEntry(EntityState state, double duration, EnumMap<Direction, Integer> nums)
    {
        public StateEntry(EntityState state, double duration, int[] nums)
        {
            this(state, duration, new EnumMap<>(Direction.class));
            if (nums.length != Direction.values().length) throw new IllegalArgumentException();
            for (Direction direction : Direction.values())
                this.nums.put(direction, nums[direction.ordinal()]);
        }

        public StateEntry(EntityState state, double duration, int num)
        {
            this(state, duration, new EnumMap<>(Direction.class));
            for (Direction direction : Direction.values())
                this.nums.put(direction, num);
        }
    }

    public record AnimationMap(Map<EntityState, EnumMap<Direction, AnimationChannel>> animations)
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

        public AnimationChannel get(EntityState state, Direction direction)
        {
            return animations.get(state).get(direction);
        }
    }
}
