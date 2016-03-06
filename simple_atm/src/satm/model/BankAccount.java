package satm.model;

import java.io.Serializable;

public class BankAccount implements Serializable{
    private static final long serialVersionUID = 6227761477526472295L;

    public static enum TYPE {CHECKING,SAVING}
    private Double balance;
    private TYPE accountType;

    public BankAccount(Double balance, TYPE accountType) {
        this.balance = balance;
        this.accountType = accountType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public TYPE getAccountType() {
        return accountType;
    }

    public void setAccountType(TYPE accountType) {
        this.accountType = accountType;
    }
}
