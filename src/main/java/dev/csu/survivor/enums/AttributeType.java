package dev.csu.survivor.enums;

import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import dev.csu.survivor.util.StringUtil;

/**
 * 属性种类
 */
public enum AttributeType
{
    /**
     * 最大生命值
     */
    MAX_HEALTH,

    /**
     * 移动速度
     */
    SPEED,

    /**
     * 伤害
     */
    DAMAGE,

    /**
     * 护甲值 (NYI)
     */
    ARMOR,

    /**
     * 生命再生
     * <p>实体每秒回复的生命值</p>
     * <p>The health value that the entity regenerate per second</p>
     */
    REGENERATION,
    ;

    /**
     * <p>属性种类的标识符，默认为枚举名称的小写字符串</p>
     * <p>该类属性的本地化键名为 <code>attribute.{id}</code></p>
     * <p>The identifier of the attribute, default to be lowercase of the enum attribute</p>
     * <p>The localized name of the attribute will be found by key <code>attribute.{id}</code></p>
     */
    public final String id;

    /**
     * 该类属性的最小值
     */
    private final double minValue;

    /**
     * 该类属性的最大值
     * <p>如果最大值小于或等于最小值，意味着最大值等于无穷大，即无上限</p>
     * <p>If maxValue <= minValue, then maxValue = Double.INFINITY</p>
     */
    private final double maxValue;

    // Lazy initialized fields
    private final LazyValue<String> lazyName;
    private final LazyValue<String> lazyDesc;

    /**
     * An AttributeType without maxValue and will always be positive.
     */
    AttributeType()
    {
        this(0);
    }

    /**
     * An AttributeType with minValue but without maxValue.
     */
    AttributeType(double minValue)
    {
        this(minValue, minValue);
    }

    AttributeType(double minValue, double maxValue)
    {
        this.id = StringUtil.lowercase(this.name());
        this.minValue = minValue;
        this.maxValue = minValue >= maxValue ? Double.MAX_VALUE : maxValue;
        this.lazyName = new LazyValue<>(() -> FXGL.localize("attribute.%s".formatted(this.id)));
        this.lazyDesc = new LazyValue<>(() -> FXGL.localize("attribute.%s.desc".formatted(this.id)));
    }

    /**
     * @return 该类属性的最小值
     */
    public double getMinValue()
    {
        return minValue;
    }

    /**
     * @return 该类属性的最大值
     */
    public double getMaxValue()
    {
        return maxValue;
    }

    /**
     * 对属性值进行最值边界校验
     *
     * @param value 需要校验的值
     * @return 经过边界校验后的属性值
     */
    public double clamp(double value)
    {
        return Math.clamp(value, minValue, maxValue);
    }

    /**
     * @return 该类属性的本地化名称
     */
    public String getName()
    {
        return lazyName.get();
    }

    /**
     * @return 该类属性的描述信息
     */
    public String getDescription()
    {
        return lazyDesc.get();
    }
}
