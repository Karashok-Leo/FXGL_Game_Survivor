package dev.csu.survivor.world.attribute;

import dev.csu.survivor.enums.AttributeType;
import javafx.beans.property.SimpleDoubleProperty;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AttributeInstance
{
    private final AttributeType type;

    private final SimpleDoubleProperty baseValue;

    /**
     * K: className
     * V: AttributeModifier
     */
    private final Map<String, AttributeModifier> modifiers = new HashMap<>();

    private boolean dirty = true;

    private final SimpleDoubleProperty value;

    @Nullable
    private final Consumer<AttributeInstance> updateCallback;

    public AttributeInstance(AttributeType type)
    {
        this(type, null);
    }

    public AttributeInstance(AttributeType type, double baseValue)
    {
        this(type, baseValue, null);
    }

    public AttributeInstance(AttributeType type, @Nullable Consumer<AttributeInstance> updateCallback)
    {
        this(type, type.getMinValue(), updateCallback);
    }

    public AttributeInstance(AttributeType type, double baseValue, @Nullable Consumer<AttributeInstance> updateCallback)
    {
        this.type = type;
        this.baseValue = new SimpleDoubleProperty(baseValue);
        this.value = new SimpleDoubleProperty(baseValue);
        this.updateCallback = updateCallback;
    }

    public void markDirty()
    {
        this.dirty = true;
    }

    public AttributeType getType()
    {
        return type;
    }

    public double getBaseValue()
    {
        return baseValue.getValue();
    }

    public void setBaseValue(double baseValue)
    {
        if (baseValue != this.baseValue.getValue())
        {
            this.baseValue.setValue(baseValue);
            this.markDirty();
            this.calculateTotalValue();
        }
    }

    public void addModifier(AttributeModifier modifier)
    {
        modifiers.put(modifier.className(), modifier);
        markDirty();
        calculateTotalValue();
    }

    public void removeModifier(AttributeModifier modifier)
    {
        this.removeModifier(modifier.className());
    }

    public void removeModifier(String className)
    {
        modifiers.remove(className);
        markDirty();
        calculateTotalValue();
    }

    public double getTotalValue()
    {
        if (dirty)
        {
            this.calculateTotalValue();
            this.dirty = false;
        }
        return value.getValue();
    }

    private void calculateTotalValue()
    {
        double totalValue = getBaseValue();

        for (AttributeModifier modifier : modifiers.values())
        {
            if (modifier.operation() == AttributeModifier.Operation.ADDITION)
            {
                totalValue += modifier.value();
            } else if (modifier.operation() == AttributeModifier.Operation.MULTIPLICATION)
            {
                totalValue *= modifier.value();
            }
        }

        this.value.setValue(type.clamp(totalValue));
        if (updateCallback != null)
            updateCallback.accept(this);
    }

    public SimpleDoubleProperty baseValueProperty()
    {
        return baseValue;
    }

    public SimpleDoubleProperty valueProperty()
    {
        return value;
    }
}