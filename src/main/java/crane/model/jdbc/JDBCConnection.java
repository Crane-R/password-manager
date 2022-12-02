package crane.model.jdbc;

import crane.view.LookFrame;

import java.sql.*;

/**
 * @author Crane Resigned
 */
public class JDBCConnection {

    private static String URL = "jdbc:mysql://106.55.196.224:3306/password_manager4.0?useUnicode=true&characterEncoding=utf-8";
    private static String USER = "root";
    private static String PASSWORD = "yun12345678@";

    static {
        //本地
        if (LookFrame.isLocal.isSelected()) {
            URL = "jdbc:mysql://127.0.0.1:3306/password_manager4.0?useUnicode=true&characterEncoding=utf-8";
            USER = "root";
            PASSWORD = "12345678";
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
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
