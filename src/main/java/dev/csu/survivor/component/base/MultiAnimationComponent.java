package dev.csu.survivor.component.base;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.entity.state.EntityState;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.FrameData;
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

/**
 * 控制实体动画的组件
 * 适用于实体具有四个方向（上下左右）与多个状态（如站立、奔跑、攻击、受伤等）的动画的情况
 * <p>
 * 该组件会自行处理动画纹理以及碰撞箱之间的位置关系，确保实体的中心点位于同时动画纹理和碰撞箱的中心。
 * 同时该组件还将实体显示的Z轴位置绑定至实体在场景中的y轴位置，这是为了自然地实现俯视角的伪3D效果
 * </p>
 * <p>依赖的组件: {@link StateComponent} {@link BoundingBoxComponent} </p>
 */
public class MultiAnimationComponent extends Component
{
    protected static final double SQRT2 = FXGLMath.sqrt(2) / 2;
    protected final AnimationMap animationMap;
    protected AnimatedTexture texture;
    protected ReadOnlyObjectProperty<EntityState> stateProperty;
    protected SimpleObjectProperty<Direction> directionProperty;
    protected SimpleObjectProperty<AnimationChannel> channelProperty;

    public MultiAnimationComponent(AnimationMap animationMap)
    {
        this.animationMap = animationMap;
    }

    /**
     * 设置实体面向的方向
     *
     * @param direction 方向
     */
    public void setDirection(Direction direction)
    {
        this.directionProperty.set(direction);
    }

    /**
     * 根据传入的向量更新实体面向的方向
     *
     * @param velocity 方向向量
     */
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

        AnimationChannel defaultChannel = this.getCurrentAnimationChannel();
        this.texture = new AnimatedTexture(defaultChannel);

        this.entity.getViewComponent().addChild(texture);

        BoundingBoxComponent bbox = this.entity.getBoundingBoxComponent();
        this.texture.setTranslateX((bbox.getWidth() - defaultChannel.getFrameWidth(0)) / 2.0);
        this.texture.setTranslateY((bbox.getHeight() - defaultChannel.getFrameHeight(0)) / 2.0);

        // Bind zIndex to yPos
        this.entity.getViewComponent().zIndexProperty().bind(entity.yProperty());

        this.texture.loop();
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

    /**
     * 获取当前正在播放的动画
     *
     * @return 当前正在播放的动画
     */
    private AnimationChannel getCurrentAnimationChannel()
    {
        return this.channelProperty.get();
    }

    /**
     * 获取当前动画的尺寸
     *
     * @return 当前动画的尺寸
     */
    public Dimension2D getDimension()
    {
        FrameData frameData = getCurrentAnimationChannel().getFrameData(0);
        return new Dimension2D(frameData.getWidth(), frameData.getHeight());
    }

    /**
     * 播放指定动画频道
     * 仅播放一次
     *
     * @param channel 动画频道
     */
    protected void playAnimation(AnimationChannel channel)
    {
        if (texture.getAnimationChannel() != channel)
            texture.playAnimationChannel(channel);
    }


    /**
     * 播放指定动画频道
     * 循环播放
     *
     * @param channel 动画频道
     */
    protected void loopAnimation(AnimationChannel channel)
    {
        if (texture.getAnimationChannel() != channel)
            texture.loopAnimationChannel(channel);
    }

    /**
     * 用于表示单个状态四种方向的动画
     *
     * @param state    实体状态
     * @param duration 该状态下动画的时长
     * @param nums     每种方向对应的动画帧数
     */
    public record StateEntry(EntityState state, Duration duration, EnumMap<Direction, Integer> nums)
    {
        /**
         * 快捷构造方法
         * 适用于每种方向对应的动画帧数不同时
         *
         * @param state    实体状态
         * @param duration 该状态下动画的时长
         * @param nums     每种方向对应的动画帧数，必须是长度为 4 的整型数组，顺序为上下左右
         */
        public StateEntry(EntityState state, Duration duration, int[] nums)
        {
            this(state, duration, new EnumMap<>(Direction.class));
            if (nums.length != Direction.values().length) throw new IllegalArgumentException();
            for (Direction direction : Direction.values())
                this.nums.put(direction, nums[direction.ordinal()]);
        }

        /**
         * 快捷构造方法
         * 适用于每种方向对应的动画帧数相同时
         *
         * @param state    实体状态
         * @param duration 该状态下动画的时长
         * @param num      每种方向对应的动画帧数
         */
        public StateEntry(EntityState state, Duration duration, int num)
        {
            this(state, duration, new EnumMap<>(Direction.class));
            for (Direction direction : Direction.values())
                this.nums.put(direction, num);
        }


        /**
         * 快捷构造方法
         * 适用于每种方向对应的动画帧数相同时
         *
         * @param state           实体状态
         * @param durationSeconds 该状态下动画的时长，单位为秒
         * @param num             每种方向对应的动画帧数
         */
        public StateEntry(EntityState state, double durationSeconds, int num)
        {
            this(state, Duration.seconds(durationSeconds), new EnumMap<>(Direction.class));
            for (Direction direction : Direction.values())
                this.nums.put(direction, num);
        }
    }

    /**
     * 用于表示一种实体在不同状态下的所有动画
     *
     * @param animations 实体状态及方向到动画频道的映射
     */
    public record AnimationMap(Map<EntityState, EnumMap<Direction, AnimationChannel>> animations)
    {
        /**
         * 快捷构造方法
         * <p>
         * 如<br>
         * <code>
         * new AnimationMap(
         * "player",
         * new MultiAnimationComponent.StateEntry(
         * EntityStates.IDLE,
         * Duration.seconds(2),
         * new int[]{4, 12, 12, 12}
         * )
         * );
         * </code><br>
         * 实际会加载<br>
         * <code>assets/textures/player/idle/up.png</code><br>
         * <code>assets/textures/player/idle/down.png</code><br>
         * <code>assets/textures/player/idle/left.png</code><br>
         * <code>assets/textures/player/idle/right.png</code><br>
         * </p>
         *
         * @param owner         实体种类名称
         * @param statesEntries 多种状态的动画集
         */
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

        /**
         * 根据传入的实体状态和方向获取对应的动画频道
         *
         * @param state     实体状态
         * @param direction 实体方向
         * @return 对应的动画频道
         */
        public AnimationChannel get(EntityState state, Direction direction)
        {
            return animations.get(state).get(direction);
        }
    }
}
