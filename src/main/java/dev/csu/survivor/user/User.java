package dev.csu.survivor.user;

import com.almasb.fxgl.dsl.FXGL;
import javafx.event.Event;
import javafx.event.EventType;

import java.sql.Timestamp;

public class User
{
    private int userId;
    private String username;
    private Timestamp registerDate;
    private Timestamp lastLoginDate;
    private boolean isLoggedIn;

    // 私有构造方法，防止外部实例化
    private User()
    {
        this.isLoggedIn = false;
    }

    // 获取单例实例
    public static User getInstance()
    {
        return UserHolder.INSTANCE;
    }

    public Timestamp getLastLoginDate()
    {
        return lastLoginDate;
    }

    public void setLastLoginDate(Timestamp lastLoginDate)
    {
        this.lastLoginDate = lastLoginDate;
    }

    public Timestamp getRegisterDate()
    {
        return registerDate;
    }

    public void setRegisterDate(Timestamp registerDate)
    {
        this.registerDate = registerDate;
    }

    // Getter 和 Setter 方法
    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public boolean isLoggedIn()
    {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn)
    {
        isLoggedIn = loggedIn;
    }

    // 清除用户信息
    public void logout()
    {
        this.userId = 0;
        this.username = null;
        this.registerDate = null;
        this.lastLoginDate = null;
        this.isLoggedIn = false;
        FXGL.getEventBus().fireEvent(new UserLogoutEvent());
    }

    // 静态内部类，持有单例实例
    private static class UserHolder
    {
        private static final User INSTANCE = new User();
    }

    public static class UserLoginEvent extends Event
    {
        public static final EventType<UserLoginEvent> USER_LOGIN = new EventType<>(Event.ANY, "USER_LOGIN");

        public UserLoginEvent()
        {
            super(USER_LOGIN);
        }
    }

    public static class UserLogoutEvent extends Event
    {
        public static final EventType<UserLogoutEvent> USER_LOGOUT = new EventType<>(Event.ANY, "USER_LOGOUT");

        public UserLogoutEvent()
        {
            super(USER_LOGOUT);
        }
    }
}
