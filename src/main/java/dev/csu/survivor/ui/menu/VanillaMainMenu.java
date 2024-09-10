package dev.csu.survivor.ui.menu;

import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import dev.csu.survivor.ui.AchievementView;
import dev.csu.survivor.ui.login.LoginUI;
import dev.csu.survivor.user.User;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class VanillaMainMenu extends BaseMenu
{
    private static final double MENU_POSITION_X = 700;
    private static final double MENU_POSITION_Y = 500;
    private static Stage loginStage;
    private Pane achievementPane;

    public VanillaMainMenu()
    {
        super(MenuType.MAIN_MENU);

        // 监听用户登录事件
        FXGL.getEventBus().addEventHandler(LoginUI.CloseLoginWindowEvent.USER_LOGIN, event ->
        {
            System.out.println("85252 ");
        });

        //监听返回事件
        FXGL.getEventBus().addEventHandler(AchievementView.BackEvent.USER_BACK, event ->
        {
            System.out.println("jianting ");
            showMainMenu();
        });
    }

    @Override
    protected void initMenuBox(MenuBox menuBox)
    {
        MenuButton itemNewGame = new MenuButton("menu.newGame");
        itemNewGame.setOnAction(e -> fireNewGame());

        MenuButton itemOptions = new MenuButton("menu.options");
        itemOptions.setChild(createOptionsMenu());

        MenuButton itemLogin = new MenuButton("menu.login");
        itemLogin.setOnAction(e -> openLoginWindow());

        MenuButton itemAchievement = new MenuButton("menu.achievement");
        itemAchievement.setOnAction(e -> showAchievementView());

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
        Rectangle bg = new Rectangle(width, height);
        bg.setFill(Color.rgb(10, 1, 1, 1.0));
        return bg;
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
                loginStage.setOnCloseRequest(event ->
                {
                    loginStage = null;
//                    enableButtons(true);
                });
//                enableButtons(false);
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    // 展示主菜单
    public void showMainMenu()
    {
        // 隐藏成就页面
        if (achievementPane != null)
        {
            achievementPane.setVisible(false);
        }
    }

    // 展示成就页面
    public void showAchievementView()
    {
        if (!isUserLoggedIn())
        {
            // 用户未登录，弹出提示框
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Please log in to view achievements.",
                    ButtonType.OK);
            alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK)
            {
                showMainMenu();
            }
            return;
        }

        AchievementView achievementView = new AchievementView();
        achievementPane = achievementView.getContentRootPane();

        achievementPane.setLayoutX(MENU_POSITION_X);
        achievementPane.setLayoutY(MENU_POSITION_Y);

        getContentRoot().getChildren().add(achievementPane); // 将Pane添加到主菜单的内容中
    }

    // 检查用户是否已登录
    private boolean isUserLoggedIn()
    {
        // 检查用户登录状态的逻辑
        User currentUser = User.getInstance();
        return currentUser != null && currentUser.isLoggedIn(); // 根据具体的User类实现进行判断
    }
}
