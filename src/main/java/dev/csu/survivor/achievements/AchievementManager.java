package dev.csu.survivor.achievements;


import dev.csu.survivor.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AchievementManager
{
    private final List<Achievement> achievements;

    public AchievementManager()
    {
        this.achievements = new ArrayList<>();
        loadAchievementsFromDatabase();
    }

    private void loadAchievementsFromDatabase()
    {
        String sql = "SELECT achievement_id, name, description, image_path FROM achievements";

        // 使用日志记录
        Logger logger = Logger.getLogger(getClass().getName());

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {

            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    // 从 ResultSet 中获取数据
                    int id = rs.getInt("achievement_id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    String imageName = rs.getString("image_path");

                    // 创建新的 Achievement 对象并添加到成就列表中
                    Achievement achievement = new Achievement(id, name, description, imageName);
                    achievements.add(achievement);
                }
            }

        } catch (SQLException e)
        {
            // 使用日志记录异常，提供清晰调试信息
            logger.log(Level.SEVERE, "从数据库加载成就时出错", e);
            e.printStackTrace();
        }
    }


    public List<Achievement> getAchievements()
    {
        return achievements;
    }

    public Achievement getAchievementById(int id)
    {
        for (Achievement achievement : achievements)
        {
            if (achievement.getId() == id)
            {
                return achievement;
            }
        }
        return null;
    }

    public Achievement getAchievementByName(String name)
    {
        for (Achievement achievement : achievements)
        {
            if (achievement.getName().equals(name))
            {
                return achievement;
            }
        }
        return null;
    }
}


