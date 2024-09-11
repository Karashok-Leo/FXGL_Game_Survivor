package dev.csu.survivor.achievements;

import dev.csu.survivor.user.User;
import dev.csu.survivor.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AchievementChecker {
    private final AchievementManager achievementManager;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public AchievementChecker(AchievementManager achievementManager) {
        this.achievementManager = achievementManager;
    }

    public boolean checkForAchievement(int achievementId, String condition) {
        List<Achievement> achievements = achievementManager.getAchievements();

        for (Achievement achievement : achievements) {
            if (achievement.getId() == achievementId && checkCondition(condition)) {
                awardAchievement(achievementId);
                return true;
            }
        }
        return false;
    }

    // 判断是否满足条件
    private boolean checkCondition(String condition) {
        User user = User.getInstance();

        // 根据不同条件进行判断
        return switch (condition) {
            case "first_login" -> user.getRegisterDate() == null; // 用户从未登录过
            case "first_game" ->
                // 检查是否是用户第一次游玩游戏（比如检查游戏游玩记录）
                    true; // 这里简化处理，具体逻辑依赖于你的游戏系统
            case "played_30_minutes" ->
                // 假设你有方法获取用户的游戏时间
                    user.getUserId()>= 30 * 60; // 用户游戏时间超过30分钟
            default -> false;
        };
    }

    // 如果达成成就，则将其插入到数据库
    private void awardAchievement(int achievementId) {
        User user = User.getInstance();
        int userId = user.getUserId();

        String sql = "INSERT INTO user_achievements (user_id, achievement_id, earned_at) VALUES (?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, achievementId);
            ps.executeUpdate();

            logger.info("成就已达成并插入数据库: 成就ID=" + achievementId + ", 用户ID=" + userId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "插入成就数据时出错", e);
        }
    }
}


