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
        healthBar.setLayoutX(0);
        healthBar.setLayoutY(0);
        healthBar.setLabelVisible(true);
        healthBar.setLabelPosition(Position.RIGHT);
        healthBar.setFill(Color.GREEN);

        HealthIntComponent hp = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).getFirst().getComponent(HealthIntComponent.class);

        healthBar.setMinValue(0);
        healthBar.currentValueProperty().bind(hp.valueProperty());
        healthBar.maxValueProperty().bind(hp.maxValueProperty());

        // Add to the scene graph
        scene.addUINode(healthBar);
    }
}
