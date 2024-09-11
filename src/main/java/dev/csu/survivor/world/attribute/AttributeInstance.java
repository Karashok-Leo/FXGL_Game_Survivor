package dev.csu.survivor.world.attribute;

import dev.csu.survivor.enums.AttributeType;
import javafx.beans.property.SimpleDoubleProperty;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * 属性实例
 *
 * @see AttributeType
 * @see AttributeModifier
 */
public class AttributeInstance
{
    /**
     * 属性种类
     */
    private final AttributeType type;

    /**
     * 属性基础值
     */
    private final SimpleDoubleProperty baseValue;

    /**
     * UUID 到属性修饰符的映射
     */
    private final Map<UUID, AttributeModifier> idToModifier = new HashMap<>();

    /**
     * 属性最终值（经过属性修饰符作用后）
     */
    private final SimpleDoubleProperty value;

    /**
     * 属性值更新时的回调方法
     */
    @Nullable
    private final Consumer<AttributeInstance> updateCallback;

    /**
     * 是否需要更新最终值
     */
    private boolean dirty = true;

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

    /**
     * 标记为需要更新的状态
     */
    public void markDirty()
    {
        this.dirty = true;
    }

    /**
     * @return 该属性实例的属性种类
     */
    public AttributeType getType()
    {
        return type;
    }

    /**
     * @return 该属性实例的基础值
     */
    public double getBaseValue()
    {
        return baseValue.getValue();
    }

    /**
     * 设置基础值
     *
     * @param baseValue 基础值
     */
    public void setBaseValue(double baseValue)
    {
        if (baseValue != this.baseValue.getValue())
        {
            this.baseValue.setValue(baseValue);
            this.markDirty();
            this.calculateTotalValue();
        }
    }

    /**
     * 添加属性修饰符
     *
     * @param modifier 要添加的属性修饰符
     */
    public void addModifier(AttributeModifier modifier)
    {
        idToModifier.put(modifier.uuid(), modifier);
        markDirty();
        calculateTotalValue();
    }

    /**
     * 移除属性修饰符
     *
     * @param modifier 要移除的属性修饰符
     */
    public void removeModifier(AttributeModifier modifier)
    {
        this.removeModifier(modifier.uuid());
    }

    /**
     * 移除属性修饰符
     *
     * @param uuid 要移除的属性修饰符的 UUID
     */
    public void removeModifier(UUID uuid)
    {
        idToModifier.remove(uuid);
        markDirty();
        calculateTotalValue();
    }

    /**
     * 按类别名称移除属性修饰符
     *
     * @param className 类别名称
     */
    public void removeModifierByClass(String className)
    {
        idToModifier.values()
                .stream()
                .filter(modifier -> modifier.className().equals(className))
                .forEach(modifier -> this.idToModifier.remove(modifier.uuid()));
        markDirty();
        calculateTotalValue();
    }

    /**
     * 获取属性的最终值
     *
     * @return 属性的最终值
     */
    public double getTotalValue()
    {
        if (dirty)
        {
            this.calculateTotalValue();
            this.dirty = false;
        }
        return value.getValue();
    }

    /**
     * 作用所有属性修饰符
     */
    private void calculateTotalValue()
    {
        double totalValue = getBaseValue();

        for (AttributeModifier modifier : idToModifier.values())
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