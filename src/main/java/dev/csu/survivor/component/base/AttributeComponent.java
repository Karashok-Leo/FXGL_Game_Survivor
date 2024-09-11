package dev.csu.survivor.component.base;

import com.almasb.fxgl.entity.component.Component;
import dev.csu.survivor.enums.AttributeType;
import dev.csu.survivor.world.attribute.AttributeInstance;
import dev.csu.survivor.world.attribute.AttributeModifier;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * 实体的属性组件
 * 通过该组件可以获取或修改实体的各项属性
 */
public class AttributeComponent extends Component
{
    private final Map<AttributeType, AttributeInstance> attributes = new EnumMap<>(AttributeType.class);

    public AttributeComponent(AttributeInstance... attributes)
    {
        for (AttributeInstance attribute : attributes)
            putAttributeInstance(attribute);
    }

    /**
     * 获取实体指定属性类型的属性实例
     * 适用于要对属性进行细致操作时
     *
     * @param type 属性种类
     * @return 传入属性种类对应的属性实例
     * @throws NullPointerException 当实体不拥有指定属性时
     */
    public AttributeInstance getAttributeInstance(AttributeType type)
    {
        return Objects.requireNonNull(attributes.get(type), "Attribute " + type + " not found");
    }

    /**
     * 向实体添加属性种类
     *
     * @param instance 属性实例
     * @see AttributeInstance
     */
    public void putAttributeInstance(AttributeInstance instance)
    {
        attributes.put(instance.getType(), instance);
    }

    /**
     * 向实体添加属性修饰符
     *
     * @param type     属性种类
     * @param modifier 属性修饰符
     */
    public void addModifier(AttributeType type, AttributeModifier modifier)
    {
        AttributeInstance instance = attributes.get(type);
        if (instance != null) instance.addModifier(modifier);
    }

    /**
     * 使实体按 UUID 移除属性修饰符
     *
     * @param type 属性种类
     * @param uuid 属性修饰符的 UUID
     */
    public void removeModifier(AttributeType type, UUID uuid)
    {
        AttributeInstance instance = attributes.get(type);
        if (instance != null) instance.removeModifier(uuid);
    }

    /**
     * 使实体按类别名称移除属性修饰符
     *
     * @param type      属性种类
     * @param className 类别名称
     */
    public void removeModifierByClass(AttributeType type, String className)
    {
        AttributeInstance instance = attributes.get(type);
        if (instance != null) instance.removeModifierByClass(className);
    }

    /**
     * 获取实体的指定属性值
     *
     * @param type 属性种类
     * @return 经过所有属性修饰符作用过后的属性值，如果实体不拥有该属性，则返回 0
     */
    public double getAttributeValue(AttributeType type)
    {
        AttributeInstance instance = attributes.get(type);
        return instance != null ? instance.getTotalValue() : 0;
    }
}




