package dev.csu.survivor.ui;

import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.ui.Position;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.base.HealthComponent;
import dev.csu.survivor.ui.menu.GameOverMenu;
import dev.csu.survivor.ui.menu.ShopMenu;
import dev.csu.survivor.util.StyleUtil;
import dev.csu.survivor.world.SurvivorGameWorld;
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

    private final LazyValue<ShopMenu> shopMenu = new LazyValue<>(ShopMenu::new);

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

    public ShopMenu getShopMenu()
    {
        return shopMenu.get();
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

        HealthComponent hp = SurvivorGameWorld
                .getPlayer()
                .getComponent(HealthComponent.class);

        healthBar.maxValueProperty().bind(hp.maxValueProperty());
        // The max value binding should be done before the current value binding so that the progress of the bar works fine
        healthBar.currentValueProperty().bind(hp.valueProperty());
        healthBar.setMinValue(0);

        Label label = healthBar.getLabel();
        StyleUtil.setLabelStyle(label, Color.rgb(64, 238, 67));

        return healthBar;
    }

    private GoldView createGoldView()
    {
        GoldView goldView = new GoldView();
        goldView.setTranslateX(14);
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
        StyleUtil.setTextStyle(text, Color.GOLD);

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
