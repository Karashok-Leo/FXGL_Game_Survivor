package dev.csu.survivor.enums;

public enum AttributeType
{
    MAX_HEALTH(),
    SPEED(),
    DAMAGE(),
    REGENERATION(),
    ;

    private final double minValue;

    /**
     * If maxValue <= minValue, then maxValue = Double.INFINITY
     */
    private final double maxValue;

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
        this.minValue = minValue;
        this.maxValue = minValue >= maxValue ? Double.MAX_VALUE : maxValue;
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
}
