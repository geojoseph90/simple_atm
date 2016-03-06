package satm.model;

import satm.controller.ViewController;

import java.util.Date;

public class UserSession {

    String accountNumber;
    Date login;
    Date lastActivity;
    Long sessionId;
    BankAccount.TYPE sessionAccountType;
    ViewController.TRANSACTION_TYPE transactionType;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Date getLogin() {
        return login;
    }

    public void setLogin(Date login) {
        this.login = login;
    }

    public Date getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(Date lastActivity) {
        this.lastActivity = lastActivity;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public BankAccount.TYPE getSessionAccountType() {
        return sessionAccountType;
    }

    public void setSessionAccountType(BankAccount.TYPE sessionAccountType) {
        this.sessionAccountType = sessionAccountType;
    }

    public ViewController.TRANSACTION_TYPE getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(ViewController.TRANSACTION_TYPE transactionType) {
        this.transactionType = transactionType;
    }
}
