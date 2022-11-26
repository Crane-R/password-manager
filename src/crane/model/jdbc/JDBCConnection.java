package crane.model.jdbc;

import java.sql.*;

/**
 * @author Crane Resigned
 */
public class JDBCConnection {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/password_management_system?useUnicode=true&characterEncoding=utf-8";
    private static final String USER = "root";
    private static final String PASSWORD = "12345678";

    static {
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
