package org.qsoft.dao;

import org.qsoft.entity.BankAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: haopt
 * Date: 7/3/13
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */

public class BankAccountDao {

    private Connection connection = null;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void updateBankAccount(BankAccount bankAccount) throws SQLException {
        String sql = "UPDATE SAVINGS_ACCOUNT SET Balance=" + bankAccount.getBalance() + " WHERE  Account_Number = '" + bankAccount.getAccountNumber() + "'";
        int updateCount = connection.createStatement().executeUpdate(sql);
    }

    public void depositBankAccount(BankAccount bankAccount, double amount) throws SQLException {
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        updateBankAccount(bankAccount);
    }

    public void insertBankAccount(BankAccount bankAccount) throws SQLException {
        String queryString = "INSERT INTO SAVINGS_ACCOUNT ('" + bankAccount.getAccountNumber() + "','" + bankAccount.getDescription() + "'," + bankAccount.getBalance() + ")";

        PreparedStatement pstmt = connection.prepareStatement("insert into SAVINGS_ACCOUNT(Account_Number, Description,Balance) values (?, ?,?)");
        pstmt.setString(1, bankAccount.getAccountNumber());
        pstmt.setString(2, bankAccount.getDescription());
        pstmt.setString(3, String.valueOf(bankAccount.getBalance()));
        pstmt.executeUpdate();

    }

    public int selectTotalRow() throws SQLException {
        String queryString = "SELECT COUNT(*) FROM SAVINGS_ACCOUNT";
        ResultSet resultSet = connection.createStatement().executeQuery(queryString);
        resultSet.next();
        return resultSet.getInt(1);
    }

    public BankAccount findByAccountNumber(String accountNumber) throws SQLException {
        String queryString = "SELECT * FROM SAVINGS_ACCOUNT WHERE ACCOUNT_NUMBER='" + accountNumber + "'";
        ResultSet resultSet = connection.createStatement().executeQuery(queryString);
        if (resultSet.next()) {
            return new BankAccount(accountNumber, resultSet.getString("description"), resultSet.getDouble("balance"));
        } else {
            return null;
        }
    }
}
