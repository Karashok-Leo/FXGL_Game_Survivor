package dev.csu.survivor.achievements;

import dev.csu.survivor.enums.AchievementType;
import dev.csu.survivor.user.User;
import dev.csu.survivor.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AchievementChecker
{
    private static final AchievementChecker checker = new AchievementChecker(AchievementManager.getManager());

    public static AchievementChecker getInstance()
    {
        return checker;
    }

    private final AchievementManager achievementManager;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public AchievementChecker(AchievementManager achievementManager)
    {
        this.achievementManager = achievementManager;
    }

    public boolean checkForAchievement(AchievementType achievementType)
    {
        List<Achievement> achievements = achievementManager.getAchievements();

        for (Achievement achievement : achievements)
        {
            if (achievement.getId() == achievementType.ordinal() &&
                achievementType.checkCondition())
            {
                awardAchievement(achievementType.ordinal());
                // 风格大相径庭，不考虑
//                FXGL.getNotificationService().pushNotification("New achievement unlocked!");
                return true;
            }
        }
        return false;
    }

    // 如果达成成就，则将其插入到数据库
    private void awardAchievement(int achievementId)
    {
        User user = User.getInstance();
        int userId = user.getUserId();

        String sql = "INSERT INTO user_achievements (user_id, achievement_id, earned_at) VALUES (?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            ps.setInt(2, achievementId);
            ps.executeUpdate();

            logger.info("成就已达成并插入数据库: 成就ID=" + achievementId + ", 用户ID=" + userId);
        } catch (SQLException e)
        {
            logger.log(Level.SEVERE, "插入成就数据时出错", e);
        }
    }
}


