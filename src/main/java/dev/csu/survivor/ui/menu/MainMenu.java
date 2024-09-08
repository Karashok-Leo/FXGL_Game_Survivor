package dev.csu.survivor.ui.menu;

import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import dev.csu.survivor.ui.login.LoginUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Objects;

public class MainMenu extends BaseMenu {

    private static Stage loginStage;
    private static final double BUTTON_WIDTH = 200;
    private static final double BUTTON_HEIGHT = 50;
    private final Button newGameButton;
    private final Button loginButton;
    private final Button exitButton;
    private final Button optionsButton;

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
        // 在MainMenu的构造函数中修改选项按钮的点击事件
        optionsButton.setOnAction(event -> {
            handleButtonClick(optionsButton, "OPTIONS_p.png", "OPTIONS.png", 500);
            // 调用FXGL的选项菜单
            VBox optionsMenu = createOptionsMenu();  // 创建选项菜单
            getContentRoot().getChildren().add(optionsMenu);  // 添加到内容根布局中
        });


        // 创建按钮布局
        VBox box = new VBox(0, newGameButton, loginButton, optionsButton, exitButton);
        box.setAlignment(Pos.CENTER);
        box.setLayoutX(700);
        box.setLayoutY(500);
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
    }

    @Override
    protected String getTitle() {
        return "Main Menu";
    }

    @Override
    protected void initMenuBox(MenuBox menuBox) {

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

    // 启用或禁用所有按钮
    private void enableButtons(boolean enable) {
        newGameButton.setDisable(!enable);
        loginButton.setDisable(!enable);
        exitButton.setDisable(!enable);
        optionsButton.setDisable(!enable);
    }
}














