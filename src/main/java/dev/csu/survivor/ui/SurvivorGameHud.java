package dev.csu.survivor.ui;

import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.scene.SubScene;
import com.almasb.fxgl.ui.Position;
import com.almasb.fxgl.ui.ProgressBar;
import dev.csu.survivor.Constants;
import dev.csu.survivor.enums.EntityType;
import dev.csu.survivor.ui.menu.GameOverMenu;
import javafx.scene.paint.Color;

public class SurvivorGameHud
{
    private SubScene gameOver;

    public void init(GameScene scene)
    {
        gameOver = new GameOverMenu();
        // Add to the scene graph
        scene.addUINode(createHealthBar());
    }

    private ProgressBar createHealthBar()
    {
        ProgressBar healthBar = new ProgressBar();
        healthBar.setWidth(Constants.Client.PLAYER_HEALTH_BAR_WIDTH);
        healthBar.setHeight(Constants.Client.PLAYER_HEALTH_BAR_HEIGHT);
        healthBar.setLayoutX(Constants.Client.PLAYER_HEALTH_BAR_OFFSET);
        healthBar.setLayoutY(Constants.Client.PLAYER_HEALTH_BAR_OFFSET);
        healthBar.setLabelVisible(true);
        healthBar.setLabelPosition(Position.RIGHT);
        healthBar.setLabelFill(Color.GREEN);
        healthBar.setFill(Color.GREEN);

        HealthIntComponent hp = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).getFirst().getComponent(HealthIntComponent.class);

        healthBar.maxValueProperty().bind(hp.maxValueProperty());
        // The max value binding should be done before the current value binding so that the progress of the bar works fine
        healthBar.currentValueProperty().bind(hp.valueProperty());
        healthBar.setMinValue(0);

        return healthBar;
    }

    public void switchToGameOverScene()
    {
        FXGL.getWindowService().pushSubScene(gameOver);
    }
}
