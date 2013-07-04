package org.qsoft.dao;

import org.qsoft.entity.BankAccount;
import org.qsoft.entity.Transaction;
import sun.awt.windows.ThemeReader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: haopt
 * Date: 7/4/13
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransactionDao {
    private Connection connection = null;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public int selectTotalRow() throws SQLException {
        String queryString = "SELECT COUNT(*) FROM SAVINGS_TRANSACTION";
        ResultSet resultSet = connection.createStatement().executeQuery(queryString);
        resultSet.next();
        return resultSet.getInt(1);
    }

    public void insertTransaction(Transaction transaction) throws SQLException {
//        String queryString = "INSERT INTO SAVINGS_TRANSACTION ('" + transaction.getAccountNumber() + "'," + transaction.getTimeStamp() + "," + transaction.getAmount() + ",'" + transaction.getDescription() + "')";

        PreparedStatement pstmt = connection.prepareStatement("insert into SAVINGS_TRANSACTION(ACCOUNT_NUMBER, TIMETRACSACTION,AMOUNT,DESCRIPTION) values (?, ?, ?, ?)");
        pstmt.setString(1, transaction.getAccountNumber());
        pstmt.setString(2, String.valueOf(transaction.getTimeStamp()));
        pstmt.setString(3, String.valueOf(transaction.getAmount()));
        pstmt.setString(4, transaction.getDescription());
        pstmt.executeUpdate();
    }
}
