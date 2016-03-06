package satm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BankCustomer implements Serializable {

    private static final long serialVersionUID = 7526472295622776147L;

    private String accountNumber;
    private String customerName;
    private int depositsPerMonth = 0;
    private int withDrawalsPerMonth;
    private int noOfAtmSessions;
    private String lastTransacDate;
    private String PIN;
    private int noOfFailedLoginAttempts=0;
    private Date lastAtmLoginTime;
    private BankAccount savingsAccount;
    private BankAccount checkingAccount;


    public BankCustomer(String name, String accNo, String Pin) {
        this.accountNumber = accNo;
        this.customerName = name;
        this.lastTransacDate = new Date().toString();
        this.PIN = Pin;

    }

    public BankAccount getSavingsAccount() {
        return savingsAccount;
    }

    public void setSavingsAccount(BankAccount savingsAccount) {
        if(savingsAccount.getAccountType()== BankAccount.TYPE.SAVING)
            this.savingsAccount = savingsAccount;
    }

    public BankAccount getCheckingAccount() {
        return checkingAccount;
    }

    public void setCheckingAccount(BankAccount checkingAccount) {
        if(checkingAccount.getAccountType()== BankAccount.TYPE.CHECKING)
            this.checkingAccount = checkingAccount;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String pIN) {
        PIN = pIN;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getDepositsPerMonth() {
        return depositsPerMonth;
    }

    public void setDepositsPerMonth(int depositsPerMonth) {
        this.depositsPerMonth = depositsPerMonth;
    }

    public int getWithDrawalsPerMotnh() {
        return withDrawalsPerMonth;
    }

    public void setWithDrawalsPerMotnh(int withDrawalsPerMotnh) {
        this.withDrawalsPerMonth = withDrawalsPerMotnh;
    }

    public int getNoOfAtmSessions() {
        return noOfAtmSessions;
    }

    public void setNoOfAtmSessions(int noOfAtmSessions) {
        this.noOfAtmSessions = noOfAtmSessions;
    }

    public String getLastTransacDate() {
        return lastTransacDate;
    }

    public void setLastTransacDate(String lastTransacDate) {
        this.lastTransacDate = lastTransacDate;
    }

    public int getNoOfFailedLoginAttempts() {
        return noOfFailedLoginAttempts;
    }

    public void setNoOfFailedLoginAttempts(int noOfFailedLoginAttempts) {
        this.noOfFailedLoginAttempts = noOfFailedLoginAttempts;
    }

    public boolean validatePin(String pin) {
        if (pin != null && pin.equals(getPIN()))
            return true;
        else
            return false;
    }

    public Date getLastAtmLoginTime() {
        return lastAtmLoginTime;
    }

    public void setLastAtmLoginTime(Date lastAtmLoginTime) {
        this.lastAtmLoginTime = lastAtmLoginTime;
    }

    public BankAccount getAccountOfType(BankAccount.TYPE accountType){
        if(accountType== BankAccount.TYPE.CHECKING)
            return checkingAccount;
        else if(accountType== BankAccount.TYPE.SAVING)
            return savingsAccount;
        else
            return null;
    }
}
