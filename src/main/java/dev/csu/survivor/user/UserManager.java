package dev.csu.survivor.user;

import dev.csu.survivor.achievements.AchievementChecker;
import dev.csu.survivor.achievements.AchievementManager;
import dev.csu.survivor.util.JDBCUtil;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserManager {

    // 创建 Logger 实例
    private static final Logger logger = Logger.getLogger(UserManager.class.getName());

    // 登录方法
    public boolean login(String username, String password) {
        String query = "SELECT user_id, password_hash, register_date, last_login FROM users WHERE username = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // 加密密码
                    String storedHash = rs.getString("password_hash");
                    String inputHash = SHA256Hash(password);

                    if (storedHash.equals(inputHash)) {
                        // 登录成功，更新 User 对象
                        User user = User.getInstance();
                        user.setUserId(rs.getInt("user_id"));
                        user.setUsername(username);
                        user.setLoggedIn(true);
                        user.setRegisterDate(rs.getTimestamp("register_date"));
                        user.setLastLoginDate(rs.getTimestamp("last_login"));

                        // 判断是否获得初次登录成就
                        new AchievementChecker(new AchievementManager()).checkForAchievement(1, "first_login");

                        user.setLastLoginDate(Timestamp.valueOf(LocalDateTime.now()));

                        // 更新数据库中的最后登录日期
                        String updateQuery = "UPDATE users SET last_login = ? WHERE user_id = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setTimestamp(1, user.getLastLoginDate());
                            updateStmt.setInt(2, user.getUserId());
                            updateStmt.executeUpdate();
                        }

                        logger.log(Level.INFO, "User {0} logged in successfully", username);
                        return true;
                    } else {
                        logger.log(Level.WARNING, "User {0} login failed, password mismatch", username);
                    }
                } else {
                    logger.log(Level.WARNING, "User {0} does not exist", username);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL exception occurred while checking user login", e);
        }
        return false;
    }

    // 检查用户是否存在
    public boolean userExist(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                boolean exists = rs.getInt(1) == 1;
                if (exists) {
                    logger.log(Level.INFO, "User {0} exists", username);
                } else {
                    logger.log(Level.INFO, "User {0} does not exist", username);
                }
                return exists;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL exception occurred while checking if user exists", e);
        }
        return false;
    }

    // 注册用户
    public void registerUser(String username, String password) {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, SHA256Hash(password));
            ps.executeUpdate();

            logger.log(Level.INFO, "User {0} registered successfully", username);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL exception occurred while registering user", e);
        }
    }

    // 生成 SHA-256 哈希
    private String SHA256Hash(String input) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Exception occurred while generating SHA-256 hash", ex);
            throw new RuntimeException(ex);
        }
    }

    // 登出
    public void logout() {
        User user = User.getInstance();
        user.logout();
        logger.log(Level.INFO, "User {0} has logged out", user.getUsername());
    }
}




