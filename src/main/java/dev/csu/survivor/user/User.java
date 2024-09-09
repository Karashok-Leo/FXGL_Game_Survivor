package dev.csu.survivor.user;

import java.sql.Date;

public class User {
    private int userId;
    private String username;
    private Date registerDate;
    private Date lastLoginDate;
    private boolean isLoggedIn;

    // 私有构造方法，防止外部实例化
    private User() {
        this.isLoggedIn = false;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    // 静态内部类，持有单例实例
    private static class UserHolder {
        private static final User INSTANCE = new User();
    }

    // 获取单例实例
    public static User getInstance() {
        return UserHolder.INSTANCE;
    }

    // Getter 和 Setter 方法
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    // 清除用户信息
    public void logout() {
        this.userId = 0;
        this.username = null;
        this.registerDate = null;
        this.lastLoginDate = null;
        this.isLoggedIn = false;
        User.getInstance().logout();
    }
}
