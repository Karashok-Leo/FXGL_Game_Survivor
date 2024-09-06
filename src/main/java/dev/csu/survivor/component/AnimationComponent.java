package dev.csu.survivor.component;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.entity.state.EntityState;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import dev.csu.survivor.enums.Direction;
import dev.csu.survivor.enums.EntityStates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Dimension2D;
import javafx.util.Duration;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static dev.csu.survivor.util.MathUtil.SQRT2;

public class AnimationComponent extends Component
{
    protected final AnimationMap animationMap;
    protected MotionComponent motion;
    protected AnimatedTexture texture;
    protected Dimension2D dimension;

    protected ReadOnlyObjectProperty<EntityState> stateProperty;
    protected SimpleObjectProperty<Direction> directionProperty;
    protected SimpleObjectProperty<AnimationChannel> channelProperty;

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
        this.directionProperty.set(direction);
    }

    public void updateDirection(Vec2 velocity)
    {
        Vec2 normalize = velocity.normalize();
        if (normalize.x < -SQRT2) setDirection(Direction.LEFT);
        else if (normalize.x > SQRT2) setDirection(Direction.RIGHT);
        else if (normalize.y < -SQRT2) setDirection(Direction.UP);
        else if (normalize.y > SQRT2) setDirection(Direction.DOWN);
    }

    @Override
    public void onAdded()
    {
        this.initProperties();

        this.motion = entity.getComponent(MotionComponent.class);
        AnimationChannel defaultChannel = this.getCurrentAnimationChannel();
        this.texture = new AnimatedTexture(defaultChannel);
        this.dimension = new Dimension2D(
                defaultChannel.getFrameWidth(0),
                defaultChannel.getFrameHeight(0)
        );
        this.entity.getViewComponent().addChild(texture);

        BoundingBoxComponent bbox = this.entity.getBoundingBoxComponent();
        this.texture.setTranslateX((bbox.getWidth() - dimension.getWidth()) / 2.0);
        this.texture.setTranslateY((bbox.getHeight() - dimension.getHeight()) / 2.0);

        // Bind zIndex to yPos
        this.entity.getViewComponent().zIndexProperty().bind(entity.yProperty());

        this.texture.loop();
        // TODO: dimension scaling
        this.entity.setScaleX(2);
        this.entity.setScaleY(2);
    }

    protected void initProperties()
    {
        this.stateProperty = entity.getComponent(StateComponent.class).currentStateProperty();
        this.directionProperty = new SimpleObjectProperty<>(Direction.DOWN);
        this.channelProperty = new SimpleObjectProperty<>();

        this.channelProperty.bind(
                Bindings.createObjectBinding(
                        () -> animationMap.get(stateProperty.get(), directionProperty.get()),
                        stateProperty, directionProperty
                )
        );

        this.channelProperty.addListener((observableValue, oldState, newState) -> updateAnimation());
    }

    protected void updateAnimation()
    {
        AnimationChannel channel = this.getCurrentAnimationChannel();
        if (stateProperty.get() == EntityStates.DEATH) playAnimation(channel);
        else loopAnimation(channel);
    }

    private AnimationChannel getCurrentAnimationChannel()
    {
        return this.channelProperty.get();
    }

    protected void playAnimation(AnimationChannel channel)
    {
        if (texture.getAnimationChannel() != channel)
            texture.playAnimationChannel(channel);
    }

    protected void loopAnimation(AnimationChannel channel)
    {
        if (texture.getAnimationChannel() != channel)
            texture.loopAnimationChannel(channel);
    }

    public record StateEntry(EntityState state, Duration duration, EnumMap<Direction, Integer> nums)
    {
        public StateEntry(EntityState state, Duration duration, int[] nums)
        {
            this(state, duration, new EnumMap<>(Direction.class));
            if (nums.length != Direction.values().length) throw new IllegalArgumentException();
            for (Direction direction : Direction.values())
                this.nums.put(direction, nums[direction.ordinal()]);
        }

        public StateEntry(EntityState state, Duration duration, int num)
        {
            this(state, duration, new EnumMap<>(Direction.class));
            for (Direction direction : Direction.values())
                this.nums.put(direction, num);
        }

        public StateEntry(EntityState state, double durationSeconds, int num)
        {
            this(state, Duration.seconds(durationSeconds), new EnumMap<>(Direction.class));
            for (Direction direction : Direction.values())
                this.nums.put(direction, num);
        }
    }

    public record AnimationMap(Map<EntityState, EnumMap<Direction, AnimationChannel>> animations)
    {
        public AnimationMap(String owner, StateEntry... statesEntries)
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
                                    entry.duration,
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
