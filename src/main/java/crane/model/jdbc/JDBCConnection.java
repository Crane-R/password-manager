package crane.model.jdbc;

import crane.view.LockFrame;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * @author Crane Resigned
 */
@Slf4j
public class JDBCConnection {

    /**
     * 配置文件加载流
     * Author: Crane Resigned
     * Date: 2022-12-27 23:10:29
     */
    private static final String LOCAL = "config/local_jdbc.properties";
    private static final String SERVER = "config/server_jdbc.properties";
    private static final String LOCAL_TEST_NAME = "config/local_jdbc_test.properties";

    /**
     * 是否是测试环境
     * Author: Crane Resigned
     * Date: 2022-12-27 23:31:58
     */
    public static final boolean IS_TEST = true;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        Properties config = new Properties();
        try {
            //加载测试or本地or服务器
            String sureConfig = IS_TEST ? LOCAL_TEST_NAME : LockFrame.isLocal.isSelected() ? LOCAL : SERVER;
            log.info("加载数据库配置：" + sureConfig);
            config.load(ClassLoader.getSystemResourceAsStream(sureConfig));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(config.getProperty("url"), config.getProperty("user"), config.getProperty("password"));
    }

    public static void close(PreparedStatement preparedStatement, Connection connection, ResultSet resultSet) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException throwAbles) {
                throwAbles.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwAbles) {
                throwAbles.printStackTrace();
            }
        }
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException throwAbles) {
                throwAbles.printStackTrace();
            }
        }
    }
}
