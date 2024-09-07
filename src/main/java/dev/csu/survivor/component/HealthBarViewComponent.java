package dev.csu.survivor.component;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.dsl.components.view.ChildViewComponent;
import com.almasb.fxgl.ui.Position;
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
        this.bar.setTraceFill(inc -> inc ? Color.GREEN : Color.RED);
        this.getViewRoot().getChildren().add(bar);
    }

    @Override
    public void onAdded()
    {
        // bind to health component
        HealthIntComponent hp = this.entity.getComponent(HealthIntComponent.class);
        this.maxValueProperty().bind(hp.maxValueProperty());
        this.valueProperty().bind(hp.valueProperty());
        this.bar.setMinValue(0);
        super.onAdded();
    }

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
