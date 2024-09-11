package dev.csu.survivor.item;

import com.almasb.fxgl.entity.Entity;
import dev.csu.survivor.component.base.AttributeComponent;
import dev.csu.survivor.enums.AttributeType;
import dev.csu.survivor.world.attribute.AttributeInstance;
import dev.csu.survivor.world.attribute.AttributeModifier;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;

/**
 * 可以对实体属性进行修改的物品
 *
 * @param modifiers 属性种类到修饰符的映射
 */
public record AttributeItem(EnumMap<AttributeType, Set<AttributeModifier>> modifiers) implements Item
{
    /**
     * 快捷构造方法
     * <p>适用于只对一种属性进行修改的物品</p>
     *
     * @param type      属性种类
     * @param modifiers 属性修饰符
     */
    public AttributeItem(AttributeType type, AttributeModifier... modifiers)
    {
        this();
        this.modifiers.put(type, Set.of(modifiers));
    }

    public AttributeItem()
    {
        this(new EnumMap<>(AttributeType.class));
    }

    @Override
    public void onApply(Entity entity)
    {
        AttributeComponent attributeComponent = entity.getComponent(AttributeComponent.class);
        modifiers.forEach((type, modifiers) ->
        {
            AttributeInstance attributeInstance = attributeComponent.getAttributeInstance(type);
            modifiers.forEach(attributeInstance::addModifier);
        });
    }

    @Override
    public void onRemove(Entity entity)
    {
        AttributeComponent attributeComponent = entity.getComponent(AttributeComponent.class);
        modifiers.forEach((type, modifiers) ->
        {
            AttributeInstance attributeInstance = attributeComponent.getAttributeInstance(type);
            modifiers.forEach(attributeInstance::removeModifier);
        });
    }

    @Override
    public List<Text> getDescription(Entity entity)
    {
        List<Text> tooltip = new ArrayList<>();
        modifiers.forEach((type, modifiers) ->
        {
            Text attributeName = new Text(type.getName() + ":");
            attributeName.setFill(Color.WHITE);
            tooltip.add(attributeName);
            tooltip.addAll(
                    modifiers.stream().map(
                            modifier ->
                            {
                                Text text = new Text(modifier.operation().formatModifier(modifier.value()));
                                text.setFill(Color.WHITE);
                                return text;
                            }
                    ).toList()
            );
        });
        return tooltip;
    }
}
