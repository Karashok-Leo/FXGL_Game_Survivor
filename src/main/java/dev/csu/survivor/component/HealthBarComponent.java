package dev.csu.survivor.component;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.dsl.components.view.GenericBarViewComponent;
import dev.csu.survivor.Constants;
import javafx.scene.paint.Color;

public class HealthBarComponent extends GenericBarViewComponent
{
    public HealthBarComponent(double x, double y, double maxHealth)
    {
        super(x, y, Color.RED, maxHealth, maxHealth, Constants.GameUI.ENEMY_HEALTH_BAR_WIDTH, Constants.GameUI.ENEMY_HEALTH_BAR_HEIGHT);
    }

    @Override
    public void onAdded()
    {
        HealthIntComponent hp = this.entity.getComponent(HealthIntComponent.class);
        this.valueProperty().bind(hp.valueProperty());
        super.onAdded();
    }
}
