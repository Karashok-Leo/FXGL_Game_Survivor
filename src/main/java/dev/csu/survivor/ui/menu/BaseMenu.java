package dev.csu.survivor.ui.menu;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.FXGLDefaultMenu;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.particle.ParticleSystem;
import com.almasb.fxgl.texture.Texture;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Copy from FXGLDefaultMenu
 */
public class BaseMenu extends FXGLMenu
{
    protected final ParticleSystem particleSystem = new ParticleSystem();
    protected final SimpleObjectProperty<Color> titleColor = new SimpleObjectProperty<>(Color.WHITE);
    protected double t = 0.0;
    protected final Pane menuRoot = new Pane();
    protected final Pane menuContentRoot = new Pane();
    protected final FXGLDefaultMenu.MenuContent EMPTY = new FXGLDefaultMenu.MenuContent();
    protected final MenuBox menu;

    public BaseMenu(@NotNull MenuType type)
    {
        super(type);

        menu = createMenuBodyGameMenu();

        double menuX = 50.0;
        double menuLayoutHeight = menu.getChildren().size() * 10.0;
        double menuY = getAppHeight() / 2.0 - menuLayoutHeight / 2;

        menuRoot.setTranslateX(menuX);
        menuRoot.setTranslateY(menuY);

        menuContentRoot.setTranslateX(getAppWidth() - 500.0);
        menuContentRoot.setTranslateY(menuY);

        initParticles();

        menuRoot.getChildren().addAll(menu);
        menuContentRoot.getChildren().add(EMPTY);

        this.getContentRoot().getChildren().addAll(
                createBackground(getAppWidth(), getAppHeight()),
                createTitleView("Game Over"),
                particleSystem.getPane(),
                menuRoot,
                menuContentRoot
        );
    }

    protected ArrayList<Animation<?>> animations = new ArrayList<>();

    @Override
    public void onCreate()
    {
        animations.clear();

        for (int i = 0; i < menu.getChildren().size(); i++)
        {
            Node node = menu.getChildren().get(i);

            node.setTranslateX(-250.0);

            Animation<?> animation = FXGL.animationBuilder()
                    .delay(Duration.seconds(i * 0.07))
                    .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                    .duration(Duration.seconds(0.66))
                    .translate(node)
                    .from(new Point2D(-250.0, 0.0))
                    .to(new Point2D(0.0, 0.0))
                    .build();

            animations.add(animation);

            animation.stop();
            animation.start();
        }
    }

    @Override
    protected void onUpdate(double tpf)
    {
        animations.forEach(animation -> animation.onUpdate(tpf));

        double frequency = 1.7;

        t += tpf * frequency;

        particleSystem.onUpdate(tpf);

        Color color = Color.color(1.0, 1.0, 1.0, FXGLMath.noise1D(t));
        titleColor.set(color);
    }

    @Override
    public void onDestroy()
    {
        // the scene is no longer active so reset everything
        // so that next time scene is active everything is loaded properly
        switchMenuTo(menu);
        switchMenuContentTo(EMPTY);
    }

    protected void switchMenuTo(Node menu)
    {
        Node oldMenu = menuRoot.getChildren().getFirst();

        FadeTransition ft = new FadeTransition(Duration.seconds(0.33), oldMenu);
        ft.setToValue(0.0);
        ft.setOnFinished(event ->
        {
            menu.setOpacity(0.0);
            menuRoot.getChildren().set(0, menu);
            oldMenu.setOpacity(1.0);

            FadeTransition ft2 = new FadeTransition(Duration.seconds(0.33), menu);
            ft.setToValue(1.0);
            ft2.play();
        });

        ft.play();
    }

    protected void switchMenuContentTo(Node content)
    {
        menuContentRoot.getChildren().set(0, content);
    }

    protected void initParticles()
    {
        // particle smoke
        Texture t = FXGL.texture("particles/smoke.png", 128.0, 128.0).brighter().brighter();

        ParticleEmitter emitter = ParticleEmitters.newFireEmitter();

        emitter.setBlendMode(BlendMode.SRC_OVER);
        emitter.setSourceImage(t.getImage());
        emitter.setSize(150.0, 220.0);
        emitter.setNumParticles(10);
        emitter.setEmissionRate(0.01);
        emitter.setVelocityFunction(i -> new Point2D(FXGL.random() * 2.5, -FXGL.random() * FXGL.random(80, 120)));
        emitter.setExpireFunction(i -> Duration.seconds(FXGL.random(4, 7)));
        emitter.setScaleFunction(i -> new Point2D(0.15, 0.10));
        emitter.setSpawnPointFunction(i -> new Point2D(FXGL.random(0.0, getAppWidth() - 200.0), 120.0));

        particleSystem.addParticleEmitter(emitter, 0.0, getAppHeight());
    }

    /**
     * Core method
     */
    protected void initMenuBox(MenuBox menuBox)
    {
        menuBox.add(createResumeButton());
        menuBox.add(createExitButton());
    }

    protected MenuBox createMenuBodyGameMenu()
    {
        MenuBox box = new MenuBox();
        initMenuBox(box);
        return box;
    }

    protected MenuButton createResumeButton()
    {
        MenuButton itemResume = new MenuButton("menu.resume");
        itemResume.setOnAction(e -> fireResume());
        return itemResume;
    }

    protected MenuButton createExitButton()
    {
        if (FXGL.getSettings().isMainMenuEnabled())
        {
            MenuButton itemExit = new MenuButton("menu.mainMenu");
            itemExit.setOnAction(e -> fireExitToMainMenu());
            return itemExit;
        } else
        {
            MenuButton itemExit = new MenuButton("menu.exit");
            itemExit.setOnAction(e -> fireExit());
            return itemExit;
        }
    }

    protected Node createBackground(double width, double height)
    {
        Rectangle bg = new Rectangle(width, height);
        bg.setFill(Color.rgb(10, 1, 1, 0.5));
        return bg;
    }

    protected Node createTitleView(String title)
    {
        Text text = FXGL.getUIFactoryService().newText(title.substring(0, 1), 50.0);
        text.setFill(null);
        text.strokeProperty().bind(titleColor);
        text.setStrokeWidth(1.5);

        Text text2 = FXGL.getUIFactoryService().newText(title.substring(1), 50.0);
        text2.setFill(null);
        text2.setStroke(titleColor.get());
        text2.setStrokeWidth(1.5);

        double textWidth = text.getLayoutBounds().getWidth() + text2.getLayoutBounds().getWidth();

        Rectangle border = new Rectangle(textWidth + 30, 65.0, null);
        border.setStroke(Color.WHITE);
        border.setStrokeWidth(4.0);
        border.setArcWidth(25.0);
        border.setArcHeight(25.0);

        ParticleEmitter emitter = ParticleEmitters.newExplosionEmitter(50);
        emitter.setBlendMode(BlendMode.ADD);
        emitter.setSourceImage(FXGL.image("particles/trace_horizontal.png", 64.0, 64.0));
        emitter.setMaxEmissions(Integer.MAX_VALUE);
        emitter.setSize(18.0, 22.0);
        emitter.setNumParticles(2);
        emitter.setEmissionRate(0.2);
        emitter.setVelocityFunction(i -> (i % 2 == 0) ? new Point2D(FXGL.random(-10.0, 0.0), 0.0) : new Point2D(FXGL.random(0.0, 10.0), 0.0));
        emitter.setExpireFunction(i -> Duration.seconds(FXGL.random(4.0, 6.0)));
        emitter.setScaleFunction(i -> new Point2D(-0.03, -0.03));
        emitter.setSpawnPointFunction(i -> Point2D.ZERO);
        emitter.setAccelerationFunction(() -> new Point2D(FXGL.random(-1.0, 1.0), FXGL.random(0.0, 0.0)));

        HBox box = new HBox(text, text2);
        box.setAlignment(Pos.CENTER);

        StackPane titleRoot = new StackPane(border, box);
        titleRoot.setTranslateX(getAppWidth() / 2.0 - (textWidth + 30) / 2);
        titleRoot.setTranslateY(50.0);

        if (!FXGL.getSettings().isNative())
            particleSystem.addParticleEmitter(emitter, getAppWidth() / 2.0 - 30, titleRoot.getTranslateY() + border.getHeight() - 16);

        return titleRoot;
    }

    protected class MenuButton extends Pane
    {
        private MenuBox parent = null;
        private FXGLDefaultMenu.MenuContent cachedContent = null;
        private final Polygon p = new Polygon(0.0, 0.0, 220.0, 0.0, 250.0, 35.0, 0.0, 35.0);
        private final Button btn;
        private boolean isAnimating = false;

        public MenuButton(String stringKey)
        {
            super();
            this.btn = FXGL.getUIFactoryService().newButton(FXGL.localizedStringProperty(stringKey));
            btn.setAlignment(Pos.CENTER_LEFT);
            btn.setStyle("-fx-background-color: transparent");

            p.setMouseTransparent(true);

            LinearGradient g = new LinearGradient(0.0, 1.0, 1.0, 0.2, true, CycleMethod.NO_CYCLE,
                    new Stop(0.6, Color.color(1.0, 0.8, 0.0, 0.34)),
                    new Stop(0.85, Color.color(1.0, 0.8, 0.0, 0.74)),
                    new Stop(1.0, Color.WHITE));

            p.fillProperty().bind(
                    Bindings.when(btn.pressedProperty())
                            .then(((Paint) Color.color(1.0, 0.8, 0.0, 0.75)))
                            .otherwise(g)
            );

            p.setStroke(Color.color(0.1, 0.1, 0.1, 0.15));

            if (!FXGL.getSettings().isNative())
                p.setEffect(new GaussianBlur());

            p.visibleProperty().bind(btn.hoverProperty());

            this.getChildren().addAll(btn, p);

            btn.focusedProperty().addListener((observable, oldValue, newValue) ->
            {
                if (newValue)
                {
                    boolean isOK = animations.stream().noneMatch(Animation::isAnimating) && !isAnimating;
                    if (isOK)
                    {
                        isAnimating = true;

                        FXGL.animationBuilder()
                                .onFinished(() -> isAnimating = false)
                                .bobbleDown(this)
                                .buildAndPlay(BaseMenu.this);
                    }
                }
            });
        }

        public void setOnAction(EventHandler<ActionEvent> handler)
        {
            btn.setOnAction(handler);
        }

        public void setParent(MenuBox menu)
        {
            parent = menu;
        }

        public void setMenuContent(Supplier<FXGLDefaultMenu.MenuContent> contentSupplier, boolean isCached)
        {
            btn.addEventHandler(ActionEvent.ACTION, actionEvent ->
            {
                if (cachedContent == null || !isCached)
                    cachedContent = contentSupplier.get();

                switchMenuContentTo(cachedContent);
            });
        }

        public void setChild(MenuBox menu)
        {
            MenuButton back = new MenuButton("menu.back");
            menu.getChildren().addFirst(back);

            back.addEventHandler(ActionEvent.ACTION, actionEvent -> switchMenuTo(this.parent));

            btn.addEventHandler(ActionEvent.ACTION, actionEvent -> switchMenuTo(menu));
        }
    }

    protected class MenuBox extends VBox
    {
        public double layoutHeight;

        public MenuBox(MenuButton... items)
        {
            this.add(items);
            layoutHeight = 10 * this.getChildren().size();
        }

        public void add(MenuButton... items)
        {
            for (MenuButton item : items)
                item.setParent(this);
            this.getChildren().addAll(items);
        }
    }
}
