package dev.csu.survivor.component;

import com.almasb.fxgl.dsl.components.RechargeableDoubleComponent;
import dev.csu.survivor.enums.AttributeType;

/**
 * Should be added after AttributeComponent
 */
public class HealthComponent extends RechargeableDoubleComponent
{
    public HealthComponent(double maxValue)
    {
        super(maxValue, maxValue);
    }

    @Override
    public void onAdded()
    {
        AttributeComponent attributeComponent = this.entity.getComponent(AttributeComponent.class);
        this.maxValueProperty().bind(attributeComponent.getAttributeInstance(AttributeType.MAX_HEALTH).valueProperty());
    }
}
