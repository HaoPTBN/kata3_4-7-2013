package org.qsoft.entity;

/**
 * Created by IntelliJ IDEA.
 * User: haopt
 * Date: 7/4/13
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class BankAccount {
    private String accountNumber;
    private String description;
    private double balance;

    public BankAccount(String accountNumber, String description, double balance) {
        this.accountNumber = accountNumber;
        this.description = description;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
