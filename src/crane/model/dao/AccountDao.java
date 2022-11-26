package crane.model.dao;

import crane.model.bean.Account;
import crane.model.jdbc.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Crane Resigned
 */
public class AccountDao implements DaoMethod {

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    @Override
    public List<Account> select(String searchText) {
        String sql;
        if ("".equals(searchText)) {
            System.out.println("空查询");
            sql = "SELECT * FROM `account` ORDER BY `account_name` ASC;";
        } else {
            System.out.println("模糊查询，关键字为：" + searchText);
            sql = "SELECT * FROM `account` WHERE `account_name` LIKE '%" + searchText + "%' OR `username` LIKE '%" + searchText + "%' OR `password` LIKE '%" + searchText + "%' OR `other` LIKE '%" + searchText + "%';";
        }

        LinkedList<Account> list = new LinkedList<>();
        try {
            connection = JDBCConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account();
                account.setAccountId(resultSet.getInt("account_id"));
                account.setAccountName(resultSet.getString("account_name"));
                account.setUsername(resultSet.getString("username"));
                account.setPassword(resultSet.getString("password"));
                account.setOther(resultSet.getString("other"));
                list.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCConnection.close(preparedStatement, connection, resultSet);
        }
        return list;
    }

    @Override
    public Boolean add(Account account) {
        String sql = "INSERT INTO `account` (`account_name`, `username`, `password`, `other`) VALUES (?, ?, ?, ?);";

        try {
            connection = JDBCConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            allSet(preparedStatement, account);
            //return false is true
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCConnection.close(preparedStatement, connection, null);
        }
        return null;
    }

    @Override
    public Integer update(Account account) {
        String sql = "UPDATE `account` SET `account_name` = ?, `username` = ?, `password` = ?, `other` = ? WHERE `account_id` = ?;";

        try {
            connection = JDBCConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            allSet(preparedStatement, account);
            preparedStatement.setInt(5, account.getAccountId());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCConnection.close(preparedStatement, connection, null);
        }
        return null;
    }

    /**
     * 删除功能不建议开启，不安全
     *
     * @Author Crane Resigned
     * @Date 2022-06-11 22:57:39
     */
    @Override
    public Boolean delete(Account account) {
        String sql = "DELETE FROM `account` WHERE `account_id` = " + account.getAccountId();
        try {
            connection = JDBCConnection.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCConnection.close(preparedStatement, connection, null);
        }
        return null;
    }

    private void allSet(PreparedStatement preparedStatement, Account account) throws SQLException {
        preparedStatement.setString(1, account.getAccountName());
        preparedStatement.setString(2, account.getUsername());
        preparedStatement.setString(3, account.getPassword());
        preparedStatement.setString(4, account.getOther());
    }
}
