package dev.csu.survivor.component;

import com.almasb.fxgl.entity.component.Component;
import dev.csu.survivor.enums.AttributeType;
import dev.csu.survivor.world.attribute.AttributeInstance;
import dev.csu.survivor.world.attribute.AttributeModifier;

import java.util.EnumMap;
import java.util.Map;

public class AttributeComponent extends Component
{
    private final Map<AttributeType, AttributeInstance> attributes = new EnumMap<>(AttributeType.class);

    public AttributeComponent(AttributeInstance... attributes)
    {
        for (AttributeInstance attribute : attributes)
            putAttributeInstance(attribute);
    }

    public AttributeInstance getAttributeInstance(AttributeType type)
    {
        return attributes.get(type);
    }

    public void putAttributeInstance(AttributeInstance instance)
    {
        attributes.put(instance.getType(), instance);
    }

    public void addModifier(AttributeType type, AttributeModifier modifier)
    {
        AttributeInstance instance = attributes.get(type);
        if (instance != null) instance.addModifier(modifier);
    }

    public void removeModifier(AttributeType type, String className)
    {
        AttributeInstance instance = attributes.get(type);
        if (instance != null) instance.removeModifier(className);
    }

    public double getAttributeValue(AttributeType type)
    {
        AttributeInstance instance = attributes.get(type);
        return instance != null ? instance.getTotalValue() : 0;
    }

    private void onAttributeUpdate(AttributeInstance instance)
    {
    }
}




