package dev.csu.survivor.ui;

import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.scene.SubScene;
import com.almasb.fxgl.ui.Position;
import com.almasb.fxgl.ui.ProgressBar;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.GoldComponent;
import dev.csu.survivor.enums.EntityType;
import dev.csu.survivor.ui.menu.GameOverMenu;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.lang.reflect.Field;

public class SurvivorGameHud
{
    private SubScene gameOver;

    public void init(GameScene scene)
    {
        gameOver = new GameOverMenu();
        // Add to the scene graph
        ProgressBar healthBar = createHealthBar();
        GoldView goldView = createGoldView();
        VBox box = new VBox(healthBar, goldView);
        box.setPadding(new Insets(Constants.Client.HUD_PADDING, Constants.Client.HUD_PADDING, Constants.Client.HUD_PADDING, Constants.Client.HUD_PADDING));
        box.setSpacing(Constants.Client.HUD_SPACING);
        box.setAlignment(Pos.TOP_LEFT);
        // Add goldView as update listener to the scene
        scene.addListener(goldView);
        scene.addUINode(box);
    }

    private ProgressBar createHealthBar()
    {
        ProgressBar healthBar = new ProgressBar();
        healthBar.setWidth(Constants.Client.PLAYER_HEALTH_BAR_WIDTH);
        healthBar.setHeight(Constants.Client.PLAYER_HEALTH_BAR_HEIGHT);
        healthBar.setTranslateX(-10);
        healthBar.setLabelVisible(true);
        healthBar.setLabelPosition(Position.RIGHT);
        healthBar.setFill(Color.GREEN.brighter());
        healthBar.setTraceFill(Color.WHITE.brighter());
        healthBar.setLabelFill(Color.GREEN.brighter());

        HealthIntComponent hp = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).getFirst().getComponent(HealthIntComponent.class);

        healthBar.maxValueProperty().bind(hp.maxValueProperty());
        // The max value binding should be done before the current value binding so that the progress of the bar works fine
        healthBar.currentValueProperty().bind(hp.valueProperty());
        healthBar.setMinValue(0);

        try
        {
            Field field = ProgressBar.class.getDeclaredField("label");
            field.setAccessible(true);
            Label label = (Label) field.get(healthBar);
            label.setFont(Font.font(Constants.Client.HUD_FONT));
        } catch (Exception e)
        {
            throw new AssertionError(e);
        }

        return healthBar;
    }

    private GoldView createGoldView()
    {
        GoldView goldView = new GoldView();
        goldView.setTranslateX(14);
        goldView.bindGolds(
                FXGL.getGameWorld()
                        .getEntitiesByType(EntityType.PLAYER)
                        .getFirst()
                        .getComponent(GoldComponent.class).valueProperty()
        );
        return goldView;
    }

    public void switchToGameOverScene()
    {
        FXGL.getWindowService().pushSubScene(gameOver);
    }
}
