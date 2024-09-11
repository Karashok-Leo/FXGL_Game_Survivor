package dev.csu.survivor.ui.login;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.DialogService;
import com.almasb.fxgl.ui.UIFactoryService;
import dev.csu.survivor.user.UserManager;
import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LoginService
{
    private final DialogService dialogService = FXGL.getDialogService();
    private final UIFactoryService uiFactory = FXGL.getUIFactoryService();
    // UserManager 实例
    private final UserManager userManager = new UserManager();

    private final TextField tfUser = new TextField(); // 用户名输入框
    private final PasswordField tfPassword = new PasswordField(); // 密码输入框

    public void showLoginBox()
    {
        var btnLogin = uiFactory.newButton(FXGL.localizedStringProperty("dialog.login"));
        var btnRegister = uiFactory.newButton(FXGL.localizedStringProperty("dialog.register"));
        var btnCancel = uiFactory.newButton(FXGL.localizedStringProperty("dialog.cancel"));

        btnLogin.setOnAction(e -> handleLogin());
        btnRegister.setOnAction(e -> handleRegister());

        dialogService.showBox(
                "Please input your username and password",
                createLoginContent(),
                btnLogin, btnRegister, btnCancel
        );
    }

    protected VBox createLoginContent()
    {
        HBox user = new HBox(); // 用户名输入框的容器
        HBox password = new HBox(); // 密码输入框的容器

        Text usernameText = uiFactory.newText("Username:", 18.0);
        Text passwordText = uiFactory.newText("Password:", 18.0);

        tfUser.setFont(uiFactory.newFont(18.0));
        tfUser.setPrefWidth(200);
        tfPassword.setFont(uiFactory.newFont(18.0));
        tfPassword.setPrefWidth(200);

        user.getChildren().addAll(usernameText, tfUser);
        user.setAlignment(Pos.CENTER);
        user.setSpacing(20);

        password.getChildren().addAll(passwordText, tfPassword);
        password.setAlignment(Pos.CENTER);
        password.setSpacing(20);

        VBox pane = new VBox(); // 整体布局容器

        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(20);
        pane.getChildren().addAll(user, password);

        return pane;
    }

    protected boolean tryLogin(String username, String password)
    {
        if (userManager.login(username, password))
        {
            FXGL.getEventBus().fireEvent(new LoginUI.CloseLoginWindowEvent(true));

            return true;
        }
        return false;
    }

    // 处理登录逻辑
    private void handleLogin()
    {
        String username = tfUser.getText();
        String password = tfPassword.getText();
        if (!userManager.userExist(username))
        {
            showErrorDialog("User does not exist.");
        } else if (tryLogin(username, password))
        {
            System.out.println("Login successful");
        } else
        {
            showErrorDialog("Incorrect username or password.");
        }
    }

    // 处理注册逻辑
    private void handleRegister()
    {
        String username = tfUser.getText();
        String password = tfPassword.getText();
        try
        {
            if (username.isEmpty() || password.isEmpty())
            {
                showErrorDialog("Incomplete information.");
            } else if (userManager.userExist(username))
            {
                showErrorDialog("User already exists.");
            } else
            {
                userManager.registerUser(username, password);
                showSuccessRegisterDialog();
            }
        } catch (Exception ignore)
        {
        }
    }

    private void showErrorDialog(String message)
    {
        dialogService.showErrorBox(new Throwable()
        {
            @Override
            public String toString()
            {
                return message;
            }
        });
    }

    private void showSuccessRegisterDialog()
    {
        dialogService.showMessageBox("Registration successful!", () ->
        {
        });
    }
}
