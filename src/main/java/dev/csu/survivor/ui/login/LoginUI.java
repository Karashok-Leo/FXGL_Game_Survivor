package dev.csu.survivor.ui.login;

import dev.csu.survivor.util.JDBCUtil;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginUI extends Application {
    // 创建标签和文本框，用于输入用户名和密码
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

    @Override
    public void start(Stage stage) {
        // 设置用户名输入部分的布局
        user.getChildren().addAll(nameLabel, tfUser);
        user.setAlignment(Pos.CENTER);
        user.setSpacing(20);

        // 设置密码输入部分的布局
        password.getChildren().addAll(passwordLabel, tfPassword);
        password.setAlignment(Pos.CENTER);
        password.setSpacing(20);

        // 设置按钮部分的布局
        h3.setAlignment(Pos.CENTER);
        h3.getChildren().addAll(btLogIn, btSignIn);
        h3.setSpacing(20);

        // 设置整体布局
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(20);
        pane.getChildren().addAll(user, password, h3);

        // 设置窗口场景和标题
        stage.setScene(new Scene(pane, 400, 250));
        stage.setTitle("Welcome!");
        stage.show();

        // 登录按钮点击事件
        btLogIn.setOnAction(e -> {
            if (!userExist()) {
                // 如果用户不存在，显示错误信息
                showErrorDialog("User does not exist.");
            } else if (userRight() && detectionInformation()) {
                // 如果用户名和密码正确且信息完整，隐藏当前窗口，显示登录成功信息
                stage.hide();
                System.out.println("Login successful");
            }
        });

        // 注册按钮点击事件
        btSignIn.setOnAction(e -> {
            if (userExist()) {
                // 如果用户已存在，显示错误信息
                showErrorDialog("User already exists.");
            } else if (detectionInformation()) {
                // 如果用户不存在且信息完整，执行注册操作
                registerUser();
                showSuccessDialog();
            }
        });
    }

    // 判断用户是否存在
    public boolean userExist() {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try {
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tfUser.getText()); // 设置查询的用户名
            ResultSet rs = ps.executeQuery();
            rs.next();
            boolean exists = rs.getInt(1) == 1; // 检查结果是否为1（用户存在）
            JDBCUtil.close(conn, ps, rs); // 关闭数据库资源
            return exists;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return false; // 默认返回用户不存在
    }

    // 验证用户名和密码是否正确
    public boolean userRight() {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password_hash = ?";
        try {
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tfUser.getText()); // 设置查询的用户名
            ps.setString(2, tfPassword.getText()); // 设置查询的密码
            ResultSet rs = ps.executeQuery();
            rs.next();
            boolean correct = rs.getInt(1) == 1; // 检查结果是否为1（用户名和密码正确）
            JDBCUtil.close(conn, ps, rs); // 关闭数据库资源
            if (!correct) {
                showErrorDialog("Incorrect username or password.");
            }
            return correct;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return false;
    }

    // 检查输入信息是否完整
    public boolean detectionInformation() {
        if (tfUser.getText().isEmpty() || tfPassword.getText().isEmpty()) {
            // 如果用户名或密码为空，显示错误信息
            showErrorDialog("Incomplete information.");
            return false;
        }
        return true;
    }

    // 执行用户注册操作
    private void registerUser() {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        try {
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tfUser.getText()); // 设置注册的用户名
            ps.setString(2, tfPassword.getText()); // 设置注册的密码
            ps.executeUpdate(); // 执行SQL语句
            JDBCUtil.close(conn, ps); // 关闭数据库资源
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    // 显示错误信息对话框
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // 显示成功信息对话框
    private void showSuccessDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Registration successful!");
        alert.showAndWait();
    }
}
