package dev.csu.survivor.component.base;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.RechargeableDoubleComponent;
import com.almasb.fxgl.time.LocalTimer;
import dev.csu.survivor.enums.AttributeType;
import javafx.util.Duration;

/**
 * Should be added after AttributeComponent
 */
public class HealthComponent extends RechargeableDoubleComponent
{
    protected final LocalTimer regenTimer;
    protected AttributeComponent attribute;

    public HealthComponent()
    {
        super(0, 0);
        this.regenTimer = FXGL.newLocalTimer();
    }

    @Override
    public void onAdded()
    {
        this.regenTimer.capture();
        this.attribute = this.entity.getComponent(AttributeComponent.class);

        // If max health increase, then set current health to max health
        this.maxValueProperty().addListener((observableValue, oldValue, newValue) ->
        {
            if (newValue.doubleValue() > oldValue.doubleValue())
                this.restoreFully();
        });
        
        this.maxValueProperty().bind(attribute.getAttributeInstance(AttributeType.MAX_HEALTH).valueProperty());
    }

    @Override
    public void onUpdate(double tpf)
    {
        if (this.isZero()) return;
        double regen = attribute.getAttributeValue(AttributeType.REGENERATION);
        if (regenTimer.elapsed(Duration.seconds(1 / regen)))
        {
            regenTimer.capture();
            this.restore(1);
        }
    }
}
