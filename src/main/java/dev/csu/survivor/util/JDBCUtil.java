package dev.csu.survivor.util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JDBCUtil
{
    //获取数据连接对象时数据写死，怎么办，传参数呗，把参数放进文件里，这样就比较灵活
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    static {
        try {
            // 给这些变量赋值，用到配置文件
            Properties p = new Properties();
            // 将文件中的数据加载到集合里(classpath路径下)
            InputStream inStream = JDBCUtil.class.getClassLoader().getResourceAsStream("db.properties");

            // 检查 inStream 是否为 null
            if (inStream == null) {
                throw new FileNotFoundException("Property file 'db.properties' not found in the classpath");
            }

            p.load(inStream);

            driver = p.getProperty("driver");
            url = p.getProperty("url");
            username = p.getProperty("username");
            password = p.getProperty("password");

            // 注册驱动
            Class.forName(driver);

        } catch (FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn=DriverManager.getConnection(url,username,password);
        return conn;
    }

    //增删改
    public static void close(Connection conn, PreparedStatement ps) {

        close(conn, ps, null);
    }

    //查
    public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
        //如果不进行非空校验，很容易出现空指针异常
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

