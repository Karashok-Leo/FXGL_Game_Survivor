package dev.csu.survivor.ui.menu;

import com.almasb.fxgl.app.scene.FXGLDefaultMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import dev.csu.survivor.ui.AchievementView;
import dev.csu.survivor.ui.login.LoginUI;
import dev.csu.survivor.user.User;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VanillaMainMenu extends BaseMenu
{
    private static Stage loginStage;
    private Pane achievementPane;
    private SimpleBooleanProperty isLoggedIn;

    public VanillaMainMenu()
    {
        super(MenuType.MAIN_MENU);

        // 监听用户登录事件
        FXGL.getEventBus().addEventHandler(LoginUI.CloseLoginWindowEvent.USER_LOGIN, event -> isLoggedIn.set(true));

        FXGL.getEventBus().addEventHandler(User.UserLogoutEvent.USER_LOGOUT, event -> isLoggedIn.set(false));
    }

    @Override
    protected void initMenuBox(MenuBox menuBox)
    {
        isLoggedIn = new SimpleBooleanProperty(false);

        MenuButton itemNewGame = new MenuButton("menu.newGame");
        itemNewGame.setOnAction(e -> fireNewGame());

        MenuButton itemOptions = new MenuButton("menu.options");
        itemOptions.setMenuContent(() -> EMPTY, false);
        itemOptions.setChild(createOptionsMenu());

        MenuButton itemLogin = new MenuButton("menu.login");
        itemLogin.setMenuContent(() -> EMPTY, false);
        itemLogin.getBtn().onActionProperty().bind(
                Bindings.when(isLoggedIn)
                        .then((EventHandler<ActionEvent>) e -> User.getInstance().logout())
                        .otherwise(e -> openLoginWindow())
        );
        itemLogin.getBtn().textProperty().bind(
                Bindings.when(isLoggedIn)
                        .then(FXGL.localizedStringProperty("menu.logout"))
                        .otherwise(FXGL.localizedStringProperty("menu.login"))
        );

        MenuButton itemAchievement = new MenuButton("menu.achievement");
        itemAchievement.setMenuContent(this::createAchievementContent, false);

        MenuButton itemExit = new MenuButton("menu.exit");
        itemExit.setOnAction(e -> fireExit());

        menuBox.add(
                itemNewGame,
                itemOptions,
                itemLogin,
                itemAchievement,
                itemExit
        );
    }

    @Override
    protected Node createBackground(double width, double height)
    {
        Image image = FXGL.image("menu/BG.png");
        ImageView bg = new ImageView(image);
        bg.setFitWidth(width);
        bg.setFitHeight(height);
        bg.setPreserveRatio(true);

        // 创建GaussianBlur效果
        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(10); // 设置模糊半径

        // 将模糊效果应用到ImageView
        bg.setEffect(gaussianBlur);

        Rectangle cover = new Rectangle(width, height);
        cover.setFill(Color.rgb(10, 1, 1, 0.7));

        return new StackPane(bg, cover);
    }

    // 打开登录窗口
    private void openLoginWindow()
    {
        if (loginStage == null || !loginStage.isShowing())
        {
            loginStage = new Stage();
            loginStage.initModality(Modality.APPLICATION_MODAL); // 先设置模态性

            LoginUI loginUI = new LoginUI();
            try
            {
                loginUI.start(loginStage); // 然后启动 loginStage
                loginStage.setOnCloseRequest(event -> loginStage = null);
            } catch (Exception ignore)
            {
            }
        }
    }

    // 展示成就页面
    protected FXGLDefaultMenu.MenuContent createAchievementContent()
    {
        if (!isLoggedIn.get())
        {
            // 用户未登录，弹出提示框
            FXGL.getDialogService()
                    .showMessageBox("Please log in to view achievements.");
            return EMPTY;
        }

        AchievementView achievementView = new AchievementView();
        achievementPane = achievementView.getContentRootPane();

        achievementPane.setTranslateX(-590);
        achievementPane.setTranslateY(-100);

        achievementPane.setScaleX(0.8);
        achievementPane.setScaleY(0.8);

        return new FXGLDefaultMenu.MenuContent(achievementPane);
    }
}
