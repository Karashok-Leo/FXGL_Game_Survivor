package dev.csu.survivor.component.base;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.RechargeableDoubleComponent;
import com.almasb.fxgl.time.LocalTimer;
import dev.csu.survivor.enums.AttributeType;
import javafx.util.Duration;

/**
 * 实体的生命组件
 * 添加该组件时最大生命值自动查询实体身上的属性组件，获取并绑定该实体的最大生命值属性
 * 同时每次更新时获取实体的生命恢复属性，自动恢复生命值
 * 当最大生命值增加时，当前生命值会直接恢复至最大生命值
 * 依赖的组件: AttributeComponent
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
