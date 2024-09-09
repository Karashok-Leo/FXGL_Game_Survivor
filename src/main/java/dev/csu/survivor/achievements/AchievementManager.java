package dev.csu.survivor.achievements;

import dev.csu.survivor.util.JDBCUtil;
import dev.csu.survivor.achievements.Achievement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AchievementManager {
    private final List<Achievement> achievements;

    public AchievementManager() {
        this.achievements = new ArrayList<>();
        loadAchievementsFromDatabase();
    }

    private void loadAchievementsFromDatabase() {
        String sql = "SELECT achievement_id, name, description, image_path FROM achievements";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("achievement_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String imageName = rs.getString("image_name");

                Achievement achievement = new Achievement(id, name, description, imageName);
                achievements.add(achievement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public Achievement getAchievementById(int id) {
        for (Achievement achievement : achievements) {
            if (achievement.getId() == id) {
                return achievement;
            }
        }
        return null;
    }

    public Achievement getAchievementByName(String name) {
        for (Achievement achievement : achievements) {
            if (achievement.getName().equals(name)) {
                return achievement;
            }
        }
        return null;
    }
}


