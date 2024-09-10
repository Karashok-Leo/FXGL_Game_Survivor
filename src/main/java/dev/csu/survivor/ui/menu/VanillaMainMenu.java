package dev.csu.survivor.ui.menu;

import com.almasb.fxgl.app.scene.FXGLDefaultMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import dev.csu.survivor.ui.AchievementView;
import dev.csu.survivor.ui.login.LoginUI;
import dev.csu.survivor.user.User;
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
    private  MenuButton itemLogout;
    private  MenuButton itemLogin;

    public VanillaMainMenu()
    {
        super(MenuType.MAIN_MENU);

        // 监听用户登录事件
        FXGL.getEventBus().addEventHandler(LoginUI.CloseLoginWindowEvent.USER_LOGIN, event ->
        {
            System.out.println("85252 ");
            itemLogout.setVisible(true);
            itemLogin.setVisible(false);
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
        itemOptions.setMenuContent(() -> EMPTY, false);
        itemOptions.setChild(createOptionsMenu());

        itemLogin = new MenuButton("menu.login");
        itemLogin.setOnAction(e -> openLoginWindow());

        itemLogout = new MenuButton("menu.logout");
        itemLogout.setOnAction(e->{
            Logout();
            itemLogin.setVisible(true);
            itemLogout.setVisible(false);
        });
        itemLogout.setVisible(false);


        MenuButton itemAchievement = new MenuButton("menu.achievement");
        itemAchievement.setMenuContent(this::createAchievementContent, false);

        MenuButton itemExit = new MenuButton("menu.exit");
        itemExit.setOnAction(e -> fireExit());

        User user = User.getInstance();
        if (user.isLoggedIn()){

        }
        menuBox.add(
                itemNewGame,
                itemOptions,
                itemLogin,
                itemLogout,
                itemAchievement,
                itemExit
        );
    }

    private void Logout() {
        User user = User.getInstance();
        user.logout();
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
                loginStage.setOnCloseRequest(event ->
                {
                    loginStage = null;
//                  enableButtons(true);
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
    protected FXGLDefaultMenu.MenuContent createAchievementContent()
    {
        if (!isUserLoggedIn())
        {
            // 用户未登录，弹出提示框
            FXGL.getDialogService()
                    .showMessageBox("Please log in to view achievements.");
            return EMPTY;
        }

        AchievementView achievementView = new AchievementView();
        achievementPane = achievementView.getContentRootPane();

        achievementPane.setTranslateX(-400);
        achievementPane.setTranslateY(130);

        return new FXGLDefaultMenu.MenuContent(achievementPane);
    }

    // 检查用户是否已登录
    private boolean isUserLoggedIn()
    {
        // 检查用户登录状态的逻辑
        User currentUser = User.getInstance();
        return currentUser != null && currentUser.isLoggedIn(); // 根据具体的User类实现进行判断
    }
}
