package dev.csu.survivor.ui;

import com.almasb.fxgl.app.scene.FXGLScene;
import com.almasb.fxgl.dsl.FXGL;
import dev.csu.survivor.achievements.UserAchieveVO;
import dev.csu.survivor.user.User;
import dev.csu.survivor.util.JDBCUtil;
import javafx.scene.layout.Pane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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


        User user = User.getInstance();

        try
        {
            Connection conn = JDBCUtil.getConnection();

            ResultSet rs = getAchievementsAchieved(conn, user);

            int totalAchievements = getTotalAchievements(conn);

            int achievedCount = 0;

            List<UserAchieveVO> vos = new ArrayList<>();

            // 检查查询结果是否为空
            if (!rs.isBeforeFirst())
            {
                logger.log(Level.INFO, "No achievements found for user {0}", user.getUsername());
                FXGL.getDialogService()
                        .showMessageBox("No achievements found for user!" + user.getUsername());
            } else
            {
                // 遍历查询结果，创建成就显示
                while (rs.next())
                {
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    String imagePath = rs.getString("image_path");
                    String unlockTime = rs.getString("earned_at");

                    System.out.println(name + " " + description + " " + imagePath + " " + unlockTime);

                    logger.log(Level.INFO, "Loading achievement: {0}, Description: {1}", new Object[]{name, description});

                    vos.add(new UserAchieveVO(name, description, imagePath, unlockTime));

                    achievedCount++;
                }

            }

            conn.close();

            AchievementPane achievementPane = new AchievementPane(vos, achievedCount, totalAchievements);

            contentRoot.getChildren().add(achievementPane); // 将GridPane添加到容器中

        } catch (SQLException e)
        {
            logger.log(Level.SEVERE, "Database query exception", e);
        } catch (Exception e)
        {
            logger.log(Level.SEVERE, "Other exception", e);
        }

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
        return totalRs.getInt("total");
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
}


