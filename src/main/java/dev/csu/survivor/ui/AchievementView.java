package dev.csu.survivor.ui;

import com.almasb.fxgl.app.scene.FXGLScene;
import com.almasb.fxgl.dsl.FXGL;
import dev.csu.survivor.user.User;
import dev.csu.survivor.util.JDBCUtil;
import dev.csu.survivor.util.StyleUtil;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 成就展示界面
 */
public class AchievementView extends FXGLScene
{
    private static final int ACHIEVEMENTS_PER_ROW = 3; // 每行显示三个成就
    private static final Logger logger = Logger.getLogger(AchievementView.class.getName()); // 定义日志记录器
    private final Pane contentRoot; // 用于返回给主菜单的容器

    public AchievementView()
    {
        super(1600, 900);

        contentRoot = new Pane();
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setLayoutX(-225);
        gridPane.setLayoutY(-100);

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(925);

        VBox progressContainer = new VBox(10);
        progressContainer.setAlignment(Pos.CENTER);
        BorderPane borderPane = getBorderPane(progressContainer);

        User user = User.getInstance();

        try
        {
            Connection conn = JDBCUtil.getConnection();

            ResultSet rs = getAchievementsAchieved(conn, user);

            int totalAchievements = getTotalAchievements(conn);

            int achievedCount = 0;

            // 检查查询结果是否为空
            if (!rs.isBeforeFirst())
            {
                logger.log(Level.INFO, "No achievements found for user {0}", user.getUsername());
                FXGL.getDialogService()
                        .showMessageBox("No achievements found for user!" + user.getUsername());
            } else
            {
                // 设定位置
                int column = 0;
                int row = 0;

                // 遍历查询结果，创建成就显示
                while (rs.next())
                {
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    String imagePath = rs.getString("image_path");
                    String unlockTime = rs.getString("earned_at");

                    System.out.println(name + " " + description + " " + imagePath + " " + unlockTime);

                    logger.log(Level.INFO, "Loading achievement: {0}, Description: {1}", new Object[]{name, description});

                    VBox achievementBox = createAchievementBox(name, description, imagePath, unlockTime);

                    BorderStackPane borderStackPane = new BorderStackPane(300, 400, achievementBox);

                    gridPane.add(borderStackPane, column, row);

                    // 控制成就展示为每行3个
                    column++;
                    if (column >= ACHIEVEMENTS_PER_ROW)
                    {
                        column = 0;
                        row++;
                    }

                    achievedCount++;
                }
            }

            conn.close();

            double progress = (double) achievedCount / totalAchievements;
            progressBar.setProgress(progress);

            Label progressLabel = new Label("Achieved  " + achievedCount + "  out of " + totalAchievements + "  achievements  (" + (int) (progress * 100) + "%)");

            StyleUtil.setLabelStyle(progressLabel, 18);

            progressContainer.getChildren().addAll(progressLabel, progressBar);

            StackPane progressPane = new StackPane(borderPane);

            contentRoot.getChildren().addAll(progressPane);  // 添加包裹的进度条

        } catch (SQLException e)
        {
            logger.log(Level.SEVERE, "Database query exception", e);
        } catch (Exception e)
        {
            logger.log(Level.SEVERE, "Other exception", e);
        }

        contentRoot.getChildren().add(gridPane); // 将GridPane添加到容器中

        // 设置背景透明
        contentRoot.setStyle("-fx-background-color: transparent;");

        contentRoot.getStyleClass().add("achievement-view");
    }

    private static ResultSet getAchievementsAchieved(Connection conn, User user) throws SQLException
    {
        // 连接数据库查询数据
        String query = "SELECT name, description, image_path, earned_at FROM achievements NATURAL JOIN user_achievements WHERE user_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);

        stmt.setString(1, String.valueOf(user.getUserId()));

        logger.log(Level.INFO, "Querying achievements for user {0}", user.getUsername());
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    private static int getTotalAchievements(Connection conn) throws SQLException
    {
        String totalAchievementsQuery = "SELECT COUNT(*) AS total FROM achievements";
        PreparedStatement totalStmt = conn.prepareStatement(totalAchievementsQuery);
        ResultSet totalRs = totalStmt.executeQuery();
        totalRs.next();
        int totalAchievements = totalRs.getInt("total");
        return totalAchievements;
    }

    private static @NotNull BorderPane getBorderPane(VBox progressContainer)
    {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(progressContainer);
        borderPane.setStyle(
                "-fx-border-color: white; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 10; " +
                "-fx-background-color: rgba(0, 0, 0, 0.2);"
        );
        borderPane.setTranslateX(-225);
        borderPane.setTranslateY(-200);
        return borderPane;
    }


    /**
     * 获取成就视图的内容容器
     *
     * @return Pane 容器
     */
    public Pane getContentRootPane()
    {
        return contentRoot;
    }

    // 创建单个成就的显示框，包含成就图片、名称、描述和达成时间
    private VBox createAchievementBox(String name, String description, String imagePath, String unlockTime)
    {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        try
        {
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
            StyleUtil.setLabelStyle(timeLabel, 16);

            // 空白标签用于分隔上下内容
            Label spacer1 = new Label();
            Label spacer2 = new Label();
            spacer1.setMinHeight(50);
            spacer2.setMinHeight(30);

            box.getChildren().addAll(imageView, nameLabel, spacer1, descriptionLabel, spacer2, timeLabel);
        } catch (NullPointerException e)
        {
            logger.log(Level.WARNING, "Null pointer exception while loading image, image path: {0}", imagePath);
        }

        return box;
    }

    //返回事件
    public static class BackEvent extends Event
    {

        public static final EventType<BackEvent> USER_BACK = new EventType<>(Event.ANY, "USER_BACK");

        public BackEvent()
        {
            super(USER_BACK);
        }
    }
}


