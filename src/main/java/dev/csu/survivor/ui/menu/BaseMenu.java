package dev.csu.survivor.ui.menu;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.FXGLDefaultMenu;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.InputModifier;
import com.almasb.fxgl.input.Trigger;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.view.TriggerView;
import com.almasb.fxgl.localization.Language;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.particle.ParticleSystem;
import com.almasb.fxgl.scene.SubScene;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.FXGLScrollPane;
import dev.csu.survivor.ui.BorderStackPane;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * 拷贝自 {@link FXGLDefaultMenu}
 */
public abstract class BaseMenu extends FXGLMenu
{
    protected final ParticleSystem particleSystem = new ParticleSystem();
    protected final SimpleObjectProperty<Color> titleColor = new SimpleObjectProperty<>(Color.WHITE);
    protected final Pane menuRoot = new Pane();
    protected final Pane menuContentRoot = new Pane();
    protected final FXGLDefaultMenu.MenuContent EMPTY = new FXGLDefaultMenu.MenuContent();
    protected final PressAnyKeyState pressAnyKeyState = new PressAnyKeyState();
    protected final MenuBox menu;
    protected double t = 0.0;
    protected ArrayList<Animation<?>> animations = new ArrayList<>();

    public BaseMenu(@NotNull MenuType type)
    {
        super(type);

        menu = createMenuBodyGameMenu();

        double menuX = 50.0;
        double menuLayoutHeight = menu.getLayoutHeight();
        double menuY = getAppHeight() / 2.0 - menuLayoutHeight / 2;

        menuRoot.setTranslateX(menuX);
        menuRoot.setTranslateY(menuY);

        menuContentRoot.setTranslateX(getAppWidth() - 500.0);
        menuContentRoot.setTranslateY(menuY);

        initParticles();

        menuRoot.getChildren().addAll(menu);
        menuContentRoot.getChildren().add(createMenuContent());

        this.getContentRoot().getChildren().addAll(
                createBackground(getAppWidth(), getAppHeight()),
                createTitleView(getTitle()),
                particleSystem.getPane(),
                menuRoot,
                menuContentRoot
        );
    }

    /**
     * 该菜单显示的标题
     */
    protected String getTitle()
    {
        return FXGL.getSettings().getTitle();
    }

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
        // extract hardcoded string
//        if (type == MenuType.MAIN_MENU && getSettings().isUserProfileEnabled && getSettings().profileName.value == "DEFAULT")
//            showProfileDialog()

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

    /**
     * 跳转菜单至指定节点
     */
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
            ft2.setToValue(1.0);
            ft2.play();
        });

        ft.play();
    }

    /**
     * 跳转菜单内容至指定 MenuContent
     *
     * @see com.almasb.fxgl.app.scene.FXGLDefaultMenu.MenuContent
     */
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
     * 初始化菜单按钮集
     *
     * @see MenuBox
     */
    protected abstract void initMenuBox(MenuBox menuBox);

    /**
     * 创建初始菜单内容
     *
     * @see com.almasb.fxgl.app.scene.FXGLDefaultMenu.MenuContent
     */
    protected FXGLDefaultMenu.MenuContent createMenuContent()
    {
        return EMPTY;
    }

    /**
     * 创建菜单按钮集
     */
    protected MenuBox createMenuBodyGameMenu()
    {
        MenuBox box = new MenuBox();
        initMenuBox(box);
        return box;
    }

    /**
     * 创建继续游戏按钮
     */
    protected MenuButton createResumeButton()
    {
        MenuButton itemResume = new MenuButton("menu.resume");
        itemResume.setOnAction(e -> fireResume());
        return itemResume;
    }

    /**
     * 创建退出按钮
     */
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

    /**
     * 创建背景
     *
     * @param width  窗口宽度
     * @param height 窗口高度
     * @return 背景节点
     */
    protected Node createBackground(double width, double height)
    {
        Rectangle bg = new Rectangle(width, height);
        bg.setFill(Color.rgb(10, 1, 1, 0.5));
        return bg;
    }

    /**
     * 创建菜单标题
     *
     * @param title 标题文本
     * @return 菜单标题节点
     */
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

        HBox titleBox = new HBox(text, text2);
        titleBox.setTranslateX(5);
        titleBox.setAlignment(Pos.CENTER);

        double titleWidth = text.getLayoutBounds().getWidth() + text2.getLayoutBounds().getWidth() + 128;
        double titleHeight = text.getLayoutBounds().getHeight() + 32;

        BorderStackPane titleRoot = new BorderStackPane(titleWidth, titleHeight, titleBox);
        titleRoot.setAlignment(Pos.CENTER);

        titleRoot.setTranslateX((getAppWidth() - titleWidth) / 2);
        titleRoot.setTranslateY(50.0);

        if (!FXGL.getSettings().isNative())
            particleSystem.addParticleEmitter(emitter, getAppWidth() / 2.0 - 30, titleRoot.getTranslateY() + titleHeight - 16);

        return titleRoot;
    }

    /**
     * 创建选项菜单按钮集
     *
     * @return 选项菜单按钮集
     */
    protected MenuBox createOptionsMenu()
    {
        MenuButton itemGameplay = new MenuButton("menu.gameplay");
        itemGameplay.setMenuContent(this::createContentGameplay, true);

        MenuButton itemControls = new MenuButton("menu.controls");
        itemControls.setMenuContent(this::createContentControls, true);

        MenuButton itemVideo = new MenuButton("menu.video");
        itemVideo.setMenuContent(this::createContentVideo, true);

        MenuButton itemAudio = new MenuButton("menu.audio");
        itemAudio.setMenuContent(this::createContentAudio, true);

        MenuButton itemRestore = new MenuButton("menu.restore");
        itemRestore.setOnAction(e ->
                FXGL.getDialogService().showConfirmationBox(FXGL.localize("menu.settingsRestore"), yes ->
                {
                    if (yes)
                    {
                        switchMenuContentTo(EMPTY);
                        restoreDefaultSettings();
                    }
                })
        );

        return new MenuBox(itemGameplay, itemControls, itemVideo, itemAudio, itemRestore);
    }

    /**
     * @return menu content with difficulty and playtime
     */
    protected FXGLDefaultMenu.MenuContent createContentGameplay()
    {
        return new FXGLDefaultMenu.MenuContent();
    }

    /**
     * @return menu content containing input mappings (action -> key/mouse)
     */
    protected FXGLDefaultMenu.MenuContent createContentControls()
    {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10.0);
        grid.setVgap(10.0);
        grid.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
        grid.getColumnConstraints().add(new ColumnConstraints(200.0, 200.0, 200.0, Priority.ALWAYS, HPos.LEFT, true));
        grid.getRowConstraints().add(new RowConstraints(40.0, 40.0, 40.0, Priority.ALWAYS, VPos.CENTER, true));

        // row 0
        grid.setUserData(0);

        FXGL.getInput().getAllBindings().forEach((action, trigger) -> addNewInputBinding(action, trigger, grid));

        FXGLScrollPane scroll = new FXGLScrollPane(grid);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scroll.setMaxHeight(getAppHeight() / 2.5);

        HBox hbox = new HBox(scroll);
        hbox.setAlignment(Pos.CENTER);

        return new FXGLDefaultMenu.MenuContent(hbox);
    }

    /**
     * 创建修改按键设置的 TriggerView
     */
    protected void addNewInputBinding(UserAction action, Trigger trigger, GridPane grid)
    {
        Text actionName = FXGL.getUIFactoryService().newText(action.getName(), Color.WHITE, 18.0);

        TriggerView triggerView = new TriggerView(trigger);
        triggerView.triggerProperty().bind(FXGL.getInput().triggerProperty(action));

        triggerView.setOnMouseClicked(event ->
        {
            if (pressAnyKeyState.isActive) return;

            pressAnyKeyState.isActive = true;
            pressAnyKeyState.actionContext = action;
            FXGL.getSceneService().pushSubScene(pressAnyKeyState);
        });

        HBox hBox = new HBox();
        hBox.setPrefWidth(100.0);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(triggerView);

        int controlsRow = (int) grid.getUserData();
        grid.addRow(controlsRow++, actionName, hBox);
        grid.setUserData(controlsRow);
    }

    /**
     * <a href="https://github.com/AlmasB/FXGL/issues/493">...</a>
     *
     * @return menu content with video settings
     */
    protected FXGLDefaultMenu.MenuContent createContentVideo()
    {
        var languageBox = FXGL.getUIFactoryService().newChoiceBox(FXCollections.observableArrayList(FXGL.getSettings().getSupportedLanguages()));
        languageBox.setValue(FXGL.getSettings().getLanguage().getValue());
        languageBox.setConverter(new StringConverter<>()
        {
            @Override
            public String toString(Language language)
            {
                return language.getNativeName();
            }

            @Override
            public Language fromString(String s)
            {
                return FXGL.getSettings().getSupportedLanguages().stream().filter(language -> language.getNativeName().equals(s)).findFirst().orElse(Language.NONE);
            }
        });

        FXGL.getSettings().getLanguage().bindBidirectional(languageBox.valueProperty());

        VBox vbox = new VBox();

        if (FXGL.getSettings().isFullScreenAllowed())
        {
            var cbFullScreen = FXGL.getUIFactoryService().newCheckBox();
            cbFullScreen.selectedProperty().bindBidirectional(FXGL.getSettings().getFullScreen());

            vbox.getChildren().add(new HBox(25.0, FXGL.getUIFactoryService().newText(FXGL.localize("menu.fullscreen") + ": "), cbFullScreen));
        }

        return new FXGLDefaultMenu.MenuContent(
                new HBox(25.0, FXGL.getUIFactoryService().newText(FXGL.localizedStringProperty("menu.language").concat(":")), languageBox),
                vbox
        );
    }

    /**
     * @return menu content containing music and sound volume sliders
     */
    protected FXGLDefaultMenu.MenuContent createContentAudio()
    {
        Slider sliderMusic = FXGL.getUIFactoryService().newSlider();
        sliderMusic.setMin(0.0);
        sliderMusic.setMax(1.0);
        sliderMusic.valueProperty().bindBidirectional(FXGL.getSettings().globalMusicVolumeProperty());

        Text textMusic = FXGL.getUIFactoryService().newText(FXGL.localizedStringProperty("menu.music.volume").concat(": "));
        Text percentMusic = FXGL.getUIFactoryService().newText("");
        percentMusic.textProperty().bind(sliderMusic.valueProperty().multiply(100).asString("%.0f"));

        Slider sliderSound = FXGL.getUIFactoryService().newSlider();
        sliderSound.setMin(0.0);
        sliderSound.setMax(1.0);
        sliderSound.valueProperty().bindBidirectional(FXGL.getSettings().globalSoundVolumeProperty());

        Text textSound = FXGL.getUIFactoryService().newText(FXGL.localizedStringProperty("menu.sound.volume").concat(": "));
        Text percentSound = FXGL.getUIFactoryService().newText("");
        percentSound.textProperty().bind(sliderSound.valueProperty().multiply(100).asString("%.0f"));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(15.0);
        gridPane.addRow(0, textMusic, sliderMusic, percentMusic);
        gridPane.addRow(1, textSound, sliderSound, percentSound);

        return new FXGLDefaultMenu.MenuContent(gridPane);
    }

    /**
     * 菜单按钮
     * <p>
     * 自带 Hover、Focus 时效果
     * </p>
     */
    protected class MenuButton extends Pane
    {
        private final Button btn;
        private MenuBox parent = null;
        private FXGLDefaultMenu.MenuContent cachedContent = EMPTY;
        private boolean isAnimating = false;

        public MenuButton(String stringKey)
        {
            super();
            this.btn = FXGL.getUIFactoryService().newButton(FXGL.localizedStringProperty(stringKey));
            btn.setAlignment(Pos.CENTER_LEFT);
            btn.setStyle("-fx-background-color: transparent");
            btn.setMinWidth(250);

            Polygon p = new Polygon(0.0, 0.0, 220.0, 0.0, 250.0, 35.0, 0.0, 35.0);
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

        /**
         * 获取该菜单按钮内置的按钮对象
         *
         * @return 该菜单按钮内置的按钮对象
         */
        public Button getBtn()
        {
            return btn;
        }

        /**
         * 设置该菜单按钮的行为
         */
        public void setOnAction(EventHandler<ActionEvent> handler)
        {
            btn.setOnAction(handler);
        }

        /**
         * 设置该菜单按钮的父级按钮集
         */
        public void setParent(MenuBox menu)
        {
            parent = menu;
        }

        /**
         * 设置该菜单按钮点击后显示的菜单内容
         *
         * @param contentSupplier 菜单内容
         * @param isCached        是否缓存菜单内容
         */
        public void setMenuContent(Supplier<FXGLDefaultMenu.MenuContent> contentSupplier, boolean isCached)
        {
            btn.addEventHandler(ActionEvent.ACTION, actionEvent ->
            {
                if (cachedContent == EMPTY || !isCached)
                    cachedContent = contentSupplier.get();

                switchMenuContentTo(cachedContent);
            });
        }

        /**
         * 设置该菜单按钮的子按钮集
         */
        public void setChild(MenuBox menu)
        {
            MenuButton back = new MenuButton("menu.back");
            menu.getChildren().addFirst(back);

            back.addEventHandler(ActionEvent.ACTION, actionEvent ->
            {
                switchMenuTo(MenuButton.this.parent);
                switchMenuContentTo(cachedContent);
            });

            btn.addEventHandler(ActionEvent.ACTION, actionEvent -> switchMenuTo(menu));
        }
    }

    /**
     * 菜单按钮集
     */
    protected class MenuBox extends VBox
    {
        public MenuBox(MenuButton... items)
        {
            super();
            this.add(items);
        }

        public int getLayoutHeight()
        {
            return 10 * this.getChildren().size();
        }

        /**
         * 向该菜单按钮集中添加菜单按钮
         *
         * @param items 要添加的菜单按钮
         */
        public void add(MenuButton... items)
        {
            for (MenuButton item : items)
                item.setParent(this);
            this.getChildren().addAll(items);
        }
    }

    protected class PressAnyKeyState extends SubScene
    {
        protected UserAction actionContext;

        protected boolean isActive = false;

        public PressAnyKeyState()
        {
            super();
            PressAnyKeyState.this.getInput().addEventFilter(KeyEvent.KEY_PRESSED, e ->
            {
                if (Input.isIllegal(e.getCode()))
                    return;

                boolean rebound = FXGL.getInput().rebind(actionContext, e.getCode(), InputModifier.from(e));

                if (rebound)
                {
                    FXGL.getSceneService().popSubScene();
                    isActive = false;
                }
            });

            PressAnyKeyState.this.getInput().addEventFilter(MouseEvent.MOUSE_PRESSED,
                    e ->
                    {
                        boolean rebound = FXGL.getInput().rebind(actionContext, e.getButton(), InputModifier.from(e));

                        if (rebound)
                        {
                            FXGL.getSceneService().popSubScene();
                            isActive = false;
                        }
                    });

            Rectangle rect = new Rectangle(250.0, 100.0);
            rect.setStroke(Color.color(0.85, 0.9, 0.9, 0.95));
            rect.setStrokeWidth(10.0);
            rect.setArcWidth(15.0);
            rect.setArcHeight(15.0);

            Text text = FXGL.getUIFactoryService().newText("", 24.0);
            text.textProperty().bind(FXGL.localizedStringProperty("menu.pressAnyKey"));

            StackPane pane = new StackPane(rect, text);
            pane.setTranslateX(getAppWidth() / 2.0 - 125);
            pane.setTranslateY(getAppHeight() / 2.0 - 50);

            getContentRoot().getChildren().add(pane);
        }
    }
}
