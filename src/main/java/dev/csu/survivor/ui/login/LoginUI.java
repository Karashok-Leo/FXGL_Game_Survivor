package dev.csu.survivor.ui.login;

import com.almasb.fxgl.dsl.FXGL;
import dev.csu.survivor.user.UserManager;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@Deprecated
public class LoginUI extends Application
{
    Label nameLabel = new Label("User Name :");
    Label passwordLabel = new Label("Password  : ");

    HBox user = new HBox(); // 用户名输入框的容器
    HBox password = new HBox(); // 密码输入框的容器
    TextField tfUser = new TextField(); // 用户名输入框
    PasswordField tfPassword = new PasswordField(); // 密码输入框
    Button btLogIn = new Button("Log in"); // 登录按钮
    Button btSignIn = new Button("Sign in"); // 注册按钮
    HBox h3 = new HBox(); // 按钮的容器
    VBox pane = new VBox(); // 整体布局容器

    // UserManager 实例
    UserManager userManager = new UserManager();

    @Override
    public void start(Stage stage)
    {
        user.getChildren().addAll(nameLabel, tfUser);
        user.setAlignment(Pos.CENTER);
        user.setSpacing(20);

        password.getChildren().addAll(passwordLabel, tfPassword);
        password.setAlignment(Pos.CENTER);
        password.setSpacing(20);

        h3.setAlignment(Pos.CENTER);
        h3.getChildren().addAll(btLogIn, btSignIn);
        h3.setSpacing(20);

        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(20);
        pane.getChildren().addAll(user, password, h3);

        stage.setScene(new Scene(pane, 400, 250));
        stage.setTitle("Welcome!");
        stage.show();

        btLogIn.setOnAction(e -> handleLogin(stage));

        btSignIn.setOnAction(e -> handleSignIn());
    }

    protected boolean tryLogin(String username, String password)
    {
        if (userManager.login(username, password))
        {
            FXGL.getEventBus().fireEvent(new CloseLoginWindowEvent(true));

            System.out.println("Login successful");
            return true;
        }
        return false;
    }

    // 处理登录逻辑
    private void handleLogin(Stage stage)
    {
        String username = tfUser.getText();
        String password = tfPassword.getText();

        if (!userManager.userExist(username))
        {
            showErrorDialog("User does not exist.");
        } else if (tryLogin(username, password))
        {
            stage.close();
        } else
        {
            showErrorDialog("Incorrect username or password.");
        }
    }

    // 处理注册逻辑
    private void handleSignIn()
    {
        try
        {
            String username = tfUser.getText();
            String password = tfPassword.getText();

            if (username.isEmpty() || password.isEmpty())
            {
                showErrorDialog("Incomplete information.");
                return;
            }

            if (userManager.userExist(username))
            {
                showErrorDialog("User already exists.");
            } else
            {
                userManager.registerUser(username, password);
                showSuccessDialog();
            }
        } catch (Exception ignore)
        {
        }
    }

    private void showErrorDialog(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessDialog()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Registration successful!");
        alert.showAndWait();
    }

    public static class CloseLoginWindowEvent extends Event
    {

        public static final EventType<CloseLoginWindowEvent> USER_LOGIN = new EventType<>(Event.ANY, "USER_LOGIN");

        private final boolean isClosed;

        public CloseLoginWindowEvent(boolean isClosed)
        {
            super(USER_LOGIN);
            this.isClosed = isClosed;
        }

        public boolean isClosed()
        {
            return isClosed;
        }
    }
}
