package com.crane.model.jdbc;

import com.crane.model.service.AccountService;
import com.crane.view.tools.PathTool;
import com.crane.view.tools.ShowMessage;
import com.crane.view.frame.LockFrame;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * @author Crane Resigned
 */
@Slf4j
public class JdbcConnection {

    /**
     * 配置文件加载流
     * Author: Crane Resigned
     * Date: 2022-12-27 23:10:29
     */
    private static final String LOCAL = "config/jdbc/local_jdbc.properties";
    private static final String SERVER = "config/jdbc/server_jdbc.properties";
    private static final String LOCAL_TEST_NAME = "config/jdbc/local_jdbc_test.properties";

    /**
     * 是否是测试/开发环境
     * Author: Crane Resigned
     * Date: 2022-12-27 23:31:58
     */
    public static final boolean IS_TEST = true;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    public static Connection getConnection() throws SQLException {
        Properties config = new Properties();
        try {
            //加载测试or本地or服务器
            String sureConfig = IS_TEST ? LOCAL_TEST_NAME : LockFrame.isLocal.isSelected() ? LOCAL : SERVER;
            log.info("加载数据库配置：{}", sureConfig);
            config.load(PathTool.getResources2InputStream(sureConfig));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(config.getProperty("url"), config.getProperty("user"), config.getProperty("password"));
        } catch (SQLSyntaxErrorException e) {
            log.error(e.getMessage());
            //数据库连接失败
            ShowMessage.showErrorMessage(e.getMessage(),"数据库连接失败，请检查数据库配置");
            //将按钮状态切换回来
            AccountService.toggleStatus(false);
        }
        return connection;
    }

    public static void close(PreparedStatement preparedStatement, Connection connection, ResultSet resultSet) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException throwAbles) {
                log.error(throwAbles.getMessage());
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwAbles) {
                log.error(throwAbles.getMessage());
            }
        }
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException throwAbles) {
                log.error(throwAbles.getMessage());
            }
        }
    }
}
