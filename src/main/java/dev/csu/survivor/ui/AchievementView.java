package dev.csu.survivor.ui;

import com.almasb.fxgl.app.scene.FXGLScene;
import dev.csu.survivor.user.User;
import dev.csu.survivor.util.JDBCUtil;
import dev.csu.survivor.util.StyleUtil;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AchievementView extends FXGLScene {

    private static final int ACHIEVEMENTS_PER_ROW = 3; // 每行显示三个成就
    private static final Logger logger = Logger.getLogger(AchievementView.class.getName()); // 定义日志记录器
    private final Pane contentRoot; // 用于返回给主菜单的容器

    public AchievementView() {
        super(1600, 900);

        contentRoot = new Pane();
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setLayoutX(-225);
        gridPane.setLayoutY(-250);

        User user = User.getInstance();

        try {
            // 连接数据库查询数据
            String query = "SELECT name, description, image_path, created_at FROM achievements NATURAL JOIN user_achievements WHERE user_id = ?";
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, String.valueOf(user.getUserId()));

            logger.log(Level.INFO, "Querying achievements for user {0}", user.getUsername());
            ResultSet rs = stmt.executeQuery();

            // 检查查询结果是否为空
            if (!rs.isBeforeFirst()) {
                logger.log(Level.INFO, "No achievements found for user {0}", user.getUsername());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No achievements found for user " + user.getUsername(), ButtonType.OK);
                alert.showAndWait();
            } else {
                // 设定位置
                int column = 0;
                int row = 0;

                // 遍历查询结果，创建成就显示
                while (rs.next()) {
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    String imagePath = rs.getString("image_path");
                    String unlockTime = rs.getString("created_at");

                    System.out.println(name + " " + description + " " + imagePath + " " + unlockTime);

                    logger.log(Level.INFO, "Loading achievement: {0}, Description: {1}", new Object[]{name, description});

                    // 创建成就的显示组件
                    VBox achievementBox = createAchievementBox(name, description, imagePath, unlockTime);

                    BorderStackPane borderStackPane = new BorderStackPane(300, 400, achievementBox);

                    gridPane.add(borderStackPane, column, row);

                    // 控制成就展示为每行3个
                    column++;
                    if (column >= ACHIEVEMENTS_PER_ROW) {
                        column = 0;
                        row++;
                    }
                }
            }

            conn.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database query exception", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Other exception", e);
        }

        contentRoot.getChildren().add(gridPane); // 将GridPane添加到容器中

        // 设置背景透明
        contentRoot.setStyle("-fx-background-color: transparent;");

        contentRoot.getStyleClass().add("achievement-view");
    }


    /**
     * 获取成就视图的内容容器
     *
     * @return Pane 容器
     */
    public Pane getContentRootPane() {
        return contentRoot;
    }

    // 创建单个成就的显示框，包含成就图片、名称、描述和达成时间
    private VBox createAchievementBox(String name, String description, String imagePath, String unlockTime) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        try {
            // 成就图片
            ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/textures/achievements/" + imagePath))));
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);

            // 成就名称
            Label nameLabel = new Label(name);
            StyleUtil.setLabelStyle(nameLabel, 24);

            // 成就描述
            Label descriptionLabel = new Label(description);
            StyleUtil.setLabelStyle(descriptionLabel, 18);
            descriptionLabel.setMaxWidth(270);
            descriptionLabel.setWrapText(true);

            // 达成时间
            Label timeLabel = new Label("Unlock Time: \n" + unlockTime);
            StyleUtil.setLabelStyle(timeLabel , 16);

            // 空白标签用于分隔上下内容
            Label spacer1 = new Label();
            Label spacer2 = new Label();
            spacer1.setMinHeight(50);
            spacer2.setMinHeight(30);

            box.getChildren().addAll(imageView, nameLabel, spacer1, descriptionLabel, spacer2, timeLabel);
        } catch (NullPointerException e) {
            logger.log(Level.WARNING, "Null pointer exception while loading image, image path: {0}", imagePath);
        }

        return box;
    }

    //返回事件
    public static class BackEvent extends Event {

        public static final EventType<BackEvent> USER_BACK = new EventType<>(Event.ANY, "USER_BACK");
        public BackEvent() {
            super(USER_BACK);
        }
    }
}




