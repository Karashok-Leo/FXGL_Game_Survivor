package dev.csu.survivor.component.misc;

import com.almasb.fxgl.dsl.components.view.ChildViewComponent;
import com.almasb.fxgl.ui.Position;
import dev.csu.survivor.component.base.HealthComponent;
import dev.csu.survivor.ui.SurvivorProgressBar;
import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;

/**
 * Partly copy from GenericBarViewComponent.java
 */
public class HealthBarViewComponent extends ChildViewComponent
{
    protected SurvivorProgressBar bar = new SurvivorProgressBar();

    public HealthBarViewComponent(double x, double y, double width, double height, Color color)
    {
        super(x, y, false);
        this.bar.setWidth(width);
        this.bar.setHeight(height);
        this.bar.setLabelVisible(false);
        this.bar.setLabelPosition(Position.RIGHT);
        this.bar.setFill(color);
        this.bar.setTraceFill(inc -> inc ? Color.GREEN.brighter() : Color.RED.brighter());
        this.getViewRoot().getChildren().add(bar);
    }

    @Override
    public void onAdded()
    {
        // bind to health component
        HealthComponent hp = this.entity.getComponent(HealthComponent.class);
        this.maxValueProperty().bind(hp.maxValueProperty());
        this.valueProperty().bind(hp.valueProperty());
        this.bar.setMinValue(0);
        super.onAdded();
    }

    /**
     * 获取该组件内部的SurvivorProgressBar对象
     * @return 内部的SurvivorProgressBar对象
     */
    public SurvivorProgressBar getBar()
    {
        return bar;
    }

    public DoubleProperty valueProperty()
    {
        return bar.currentValueProperty();
    }

    public DoubleProperty maxValueProperty()
    {
        return bar.maxValueProperty();
    }
}
