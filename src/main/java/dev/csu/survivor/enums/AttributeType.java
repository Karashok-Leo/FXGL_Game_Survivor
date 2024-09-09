package dev.csu.survivor.enums;

import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import dev.csu.survivor.util.StringUtil;

public enum AttributeType
{
    MAX_HEALTH,

    SPEED,

    DAMAGE,

    /**
     * NYI
     */
    ARMOR,

    /**
     * The health value that the entity regenerate per second.
     */
    REGENERATION,
    ;

    /**
     * The identifier of the attribute, default to be lowercase of the enum attribute.
     * The localized name of the attribute will be found by key "attribute.{id}"
     */
    public final String id;

    private final double minValue;

    /**
     * If maxValue <= minValue, then maxValue = Double.INFINITY
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

    public double getMinValue()
    {
        return minValue;
    }

    public double getMaxValue()
    {
        return maxValue;
    }

    public double clamp(double value)
    {
        return Math.clamp(value, minValue, maxValue);
    }

    public String getName()
    {
        return lazyName.get();
    }

    public String getDescription()
    {
        return lazyDesc.get();
    }
}
