package dev.csu.survivor.ui;

import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.scene.SubScene;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.ui.Position;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.GoldComponent;
import dev.csu.survivor.enums.EntityType;
import dev.csu.survivor.ui.menu.GameOverMenu;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class SurvivorGameHud
{
    public static SurvivorGameHud INSTANCE;

    private GameScene gameScene;
    private SubScene gameOver;
    private GoldView goldView;

    public void init(GameScene scene)
    {
        INSTANCE = this;
        this.gameScene = scene;
        this.gameOver = new GameOverMenu();
        // Add to the scene graph
        SurvivorProgressBar healthBar = createHealthBar();
        this.goldView = createGoldView();
        VBox box = new VBox(healthBar, goldView);
        box.setPadding(new Insets(Constants.Client.HUD_PADDING));
        box.setSpacing(Constants.Client.HUD_SPACING);
        box.setAlignment(Pos.TOP_LEFT);
        // Add goldView as update listener to the scene
        scene.addListener(goldView);
        scene.addUINode(box);
    }

    private SurvivorProgressBar createHealthBar()
    {
        SurvivorProgressBar healthBar = new SurvivorProgressBar();
        healthBar.setWidth(Constants.Client.PLAYER_HEALTH_BAR_WIDTH);
        healthBar.setHeight(Constants.Client.PLAYER_HEALTH_BAR_HEIGHT);
        healthBar.setTranslateX(-10);
        healthBar.setLabelVisible(true);
        healthBar.setLabelPosition(Position.RIGHT);
        healthBar.setFill(Color.GREEN.brighter());
        healthBar.setTraceFill(inc -> inc ? Color.GREEN.brighter() : Color.RED.brighter());
        healthBar.setLabelFill(Color.BLACK);

        HealthIntComponent hp = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).getFirst().getComponent(HealthIntComponent.class);

        healthBar.maxValueProperty().bind(hp.maxValueProperty());
        // The max value binding should be done before the current value binding so that the progress of the bar works fine
        healthBar.currentValueProperty().bind(hp.valueProperty());
        healthBar.setMinValue(0);

        Label label = healthBar.getLabel();
        label.setFont(Font.font(Constants.Client.HUD_FONT));
        label.setStyle("""
                -fx-background-color: rgba(255, 255, 255, 0.4);
                -fx-background-radius: 10;
                -fx-border-radius: 10;
                -fx-padding: 2, 6;
                """);

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

    public void createGoldCollectingAnimation(AnimatedTexture texture, double x, double y)
    {
        gameScene.addChild(texture);

        TranslateTransition ttGold = new TranslateTransition(Duration.seconds(1.0), texture);
        ttGold.setToX(goldView.getLayoutX() - x);
        ttGold.setToY(goldView.getLayoutY() - y);
        ttGold.setOnFinished(event -> gameScene.removeChild(texture));
        ttGold.play();

        Text text = new Text("+1");
        text.setTranslateX(x);
        text.setTranslateY(y);
        text.setFill(Color.WHITE);
        text.setFont(Font.font(24));

        text.setStroke(Color.GOLD);
        text.setStrokeWidth(2);
        DropShadow shadow = new DropShadow(10, Color.BLACK);
        shadow.setInput(new Glow(0.3));
        text.setEffect(shadow);

        gameScene.addChild(text);

        TranslateTransition ttText = new TranslateTransition(Constants.Client.TEXT_FADE_DURATION, text);
        ttText.setByY(-20);
        ttText.play();

        FadeTransition ft = new FadeTransition(Constants.Client.TEXT_FADE_DURATION, text);
        ft.setToValue(0);

        ft.setOnFinished(event -> gameScene.removeChild(text));
        ft.play();

    }

    public void switchToGameOverScene()
    {
        FXGL.getWindowService().pushSubScene(gameOver);
    }
}
