package dev.csu.survivor.ui;

import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.ui.Position;
import com.almasb.fxgl.ui.ProgressBar;
import dev.csu.survivor.enums.EntityType;
import javafx.scene.paint.Color;

public class SurvivorGameHud
{
    public void init(GameScene scene)
    {
        ProgressBar healthBar = new ProgressBar();
        healthBar.setWidth(200);
        healthBar.setHeight(20);
        healthBar.setLayoutX(8);
        healthBar.setLayoutY(8);
        healthBar.setLabelVisible(true);
        healthBar.setLabelPosition(Position.RIGHT);
        healthBar.setLabelFill(Color.GREEN);
        healthBar.setFill(Color.GREEN);

        HealthIntComponent hp = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).getFirst().getComponent(HealthIntComponent.class);

        healthBar.maxValueProperty().bind(hp.maxValueProperty());
        // The max value binding should be done before the current value binding so that the progress of the bar works fine
        healthBar.currentValueProperty().bind(hp.valueProperty());

        healthBar.setMinValue(0);

        // Add to the scene graph
        scene.addUINode(healthBar);
    }
}
