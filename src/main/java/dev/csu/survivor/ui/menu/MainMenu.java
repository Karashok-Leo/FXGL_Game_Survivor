package dev.csu.survivor.ui.menu;

import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import dev.csu.survivor.ui.AchievementView;
import dev.csu.survivor.ui.login.LoginUI;
import dev.csu.survivor.user.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;
import java.util.Optional;

public class MainMenu extends BaseMenu {

    private static Stage loginStage;
    private static final double BUTTON_WIDTH = 200;
    private static final double BUTTON_HEIGHT = 50;
    private static final double MENU_POSITION_X = 700;
    private static final double MENU_POSITION_Y = 500;
    private final Button newGameButton;
    private final Button loginButton;
    private final Button exitButton;
    private final Button optionsButton;
    private final Button achievementButton;
    private final VBox box;
    private VBox optionsMenu;
    private Pane achievementPane;

    public MainMenu() {
        super(MenuType.MAIN_MENU);

        // 创建新游戏按钮，设置点击后的事件
        newGameButton = createButton("NEWGAME.png", "NEWGAME_p.png");
        newGameButton.setOnAction(event -> {
            handleButtonClick(newGameButton, "NEWGAME_p.png", "NEWGAME.png", 500);
            FXGL.getGameController().startNewGame();
        });

        // 创建登录按钮，点击后打开登录窗口
        loginButton = createButton("LOGIN.png", "LOGIN_p.png");
        loginButton.setOnAction(event -> {
            handleButtonClick(loginButton, "LOGIN_p.png", "LOGIN.png", 500);
            openLoginWindow();
        });

        // 创建退出按钮，点击后退出游戏
        exitButton = createButton("EXIT.png", "EXIT_p.png");
        exitButton.setOnAction(event -> {
            handleButtonClick(exitButton, "EXIT_p.png", "EXIT.png", 500);
            FXGL.getGameController().exit();
        });

        // 创建选项按钮，点击后打开FXGL原生的选项菜单
        optionsButton = createButton("OPTIONS.png", "OPTIONS_p.png");
        optionsButton.setOnAction(event -> {
            handleButtonClick(optionsButton, "OPTIONS_p.png", "OPTIONS.png", 500);
            showOptionsMenu();
        });

        // 创建成就按钮，点击后显示成就视图
        achievementButton = createButton("ACHIEVEMENT.png", "ACHIEVEMENT_p.png");
        achievementButton.setOnAction(event -> {
            handleButtonClick(achievementButton, "ACHIEVEMENT_p.png", "ACHIEVEMENT.png", 500);
            showAchievementView();
        });

        // 创建按钮布局
        box = new VBox(0, newGameButton, loginButton, optionsButton, achievementButton, exitButton);
        box.setAlignment(Pos.CENTER);
        box.setLayoutX(MENU_POSITION_X);
        box.setLayoutY(MENU_POSITION_Y);
        getContentRoot().getChildren().add(box);

        // 绑定按钮的宽度和高度到布局大小
        newGameButton.prefWidthProperty().bind(getContentRoot().widthProperty().multiply(0.1));
        loginButton.prefWidthProperty().bind(getContentRoot().widthProperty().multiply(0.1));
        exitButton.prefWidthProperty().bind(getContentRoot().widthProperty().multiply(0.1));
        optionsButton.prefWidthProperty().bind(getContentRoot().widthProperty().multiply(0.1));

        newGameButton.prefHeightProperty().bind(getContentRoot().heightProperty().multiply(0.05));
        loginButton.prefHeightProperty().bind(getContentRoot().heightProperty().multiply(0.05));
        exitButton.prefHeightProperty().bind(getContentRoot().heightProperty().multiply(0.05));
        optionsButton.prefHeightProperty().bind(getContentRoot().heightProperty().multiply(0.05));

        // 设置背景图片
        setBackgroundImage("BG.png");

        // 监听用户登录事件
        FXGL.getEventBus().addEventHandler(LoginUI.CloseLoginWindowEvent.USER_LOGIN, event -> {
            System.out.println("85252 ");
            enableButtons(event.isClosed());
        });

        //监听返回事件
        FXGL.getEventBus().addEventHandler(AchievementView.BackEvent.USER_BACK,event -> {
            System.out.println("jianting ");
           showMainMenu();
        });

        // 注册按键事件
        FXGL.getInput().addAction(new BackAction("GoTo"),KeyCode.K);
    }

    @Override
    protected String getTitle() {
        return "Survivor";
    }

    @Override
    protected void initMenuBox(MenuBox menuBox) {
        // 这里可以初始化菜单框内容
    }

    // 创建按钮，设置默认和点击时的图片
    private Button createButton(String defaultImagePath, String clickedImagePath) {
        Button button = new Button();
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/textures/menu/" + defaultImagePath))));
        imageView.setFitWidth(BUTTON_WIDTH);
        imageView.setFitHeight(BUTTON_HEIGHT);
        button.setGraphic(imageView);

        button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setPadding(javafx.geometry.Insets.EMPTY);
        button.setStyle("-fx-background-color: transparent;");
        button.setBorder(null);

        return button;
    }

    protected MenuBox createOptionsMenu() {
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
                FXGL.getDialogService().showConfirmationBox(FXGL.localize("menu.settingsRestore"), yes -> {
                    if (yes) {
                        switchMenuContentTo(EMPTY);
                        restoreDefaultSettings();
                    }
                })
        );

        MenuButton itemBack = new MenuButton("menu.back");
        itemBack.setOnAction(e -> showMainMenu()); // 点击“Back”按钮时显示主菜单

        return new MenuBox(itemGameplay, itemControls, itemVideo, itemAudio, itemRestore, itemBack);
    }

    // 处理按钮点击事件，显示点击后的图片并延时恢复默认图片
    private void handleButtonClick(Button button, String clickedImagePath, String defaultImagePath, int delay) {
        ImageView clickedImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/textures/menu/" + clickedImagePath))));
        clickedImageView.setFitWidth(BUTTON_WIDTH);
        clickedImageView.setFitHeight(BUTTON_HEIGHT);
        button.setGraphic(clickedImageView);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(delay), event -> {
            ImageView defaultImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/textures/menu/" + defaultImagePath))));
            defaultImageView.setFitWidth(BUTTON_WIDTH);
            defaultImageView.setFitHeight(BUTTON_HEIGHT);
            button.setGraphic(defaultImageView);
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    // 设置背景图片
    private void setBackgroundImage(String imagePath) {
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/textures/menu/" + imagePath)));
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(getContentRoot().getWidth());
        backgroundView.setFitHeight(getContentRoot().getHeight());
        getContentRoot().getChildren().addFirst(backgroundView);
    }

    // 打开登录窗口
    private void openLoginWindow() {
        if (loginStage == null || !loginStage.isShowing()) {
            loginStage = new Stage();
            loginStage.initModality(Modality.APPLICATION_MODAL); // 先设置模态性

            LoginUI loginUI = new LoginUI();
            try {
                loginUI.start(loginStage); // 然后启动 loginStage
                loginStage.setOnCloseRequest(event -> {
                    loginStage = null;
                    enableButtons(true);
                });
                enableButtons(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // 关闭登录窗口
    private void closeLoginWindow() {
        enableButtons(true);
    }

    // 展示选项页面
    private void showOptionsMenu() {
        if (box != null) {
            box.setVisible(false); // 隐藏主菜单
        }

        optionsMenu = createOptionsMenu();

        optionsMenu.setAlignment(Pos.CENTER);
        optionsMenu.setLayoutX(MENU_POSITION_X);
        optionsMenu.setLayoutY(MENU_POSITION_Y);

        optionsMenu.setVisible(true); // 显示选项菜单
        if (!getContentRoot().getChildren().contains(optionsMenu)) {
            getContentRoot().getChildren().add(optionsMenu);
        }
    }

    // 展示主菜单
    public void showMainMenu() {
        // 隐藏选项菜单
        if (optionsMenu != null) {
            optionsMenu.setVisible(false);
        }

        // 隐藏成就页面
        if (achievementPane != null) {
            achievementPane.setVisible(false);
        }

        // 显示主菜单
        if (box != null) {
            box.setVisible(true);
        }
    }

    // 展示成就页面
    public void showAchievementView() {
        if (!isUserLoggedIn()) {
            // 用户未登录，弹出提示框
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Please log in to view achievements.",
                    ButtonType.OK);
            alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                showMainMenu();
            }
            return;
        }

        AchievementView achievementView = new AchievementView();
        achievementPane = achievementView.getContentRootPane();

        // 隐藏主菜单
        if (box != null) {
            box.setVisible(false);
        }

        achievementPane.setLayoutX(MENU_POSITION_X);
        achievementPane.setLayoutY(MENU_POSITION_Y);

        getContentRoot().getChildren().add(achievementPane); // 将Pane添加到主菜单的内容中
    }

    // 检查用户是否已登录
    private boolean isUserLoggedIn() {
        // 检查用户登录状态的逻辑
        User currentUser = User.getInstance();
        return currentUser != null && currentUser.isLoggedIn(); // 根据具体的User类实现进行判断
    }

    // 启用或禁用所有按钮
    private void enableButtons(boolean enable) {
        newGameButton.setDisable(!enable);
        loginButton.setDisable(!enable);
        exitButton.setDisable(!enable);
        optionsButton.setDisable(!enable);
        achievementButton.setDisable(!enable);
    }

    public class BackAction extends UserAction {

        public BackAction(String name) {
            super(name);
        }

        public void onAction() {
            showMainMenu();
        }
    }
}















