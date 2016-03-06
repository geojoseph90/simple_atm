package satm.controller;

import satm.exception.InvalidAccountException;
import satm.exception.InvalidActionException;
import satm.exception.InvalidIdentificationException;
import satm.model.Accounts;
import satm.model.BankAccount;
import satm.model.BankCustomer;

import java.util.Date;

public class ModelController {

    public boolean validateCard(String accNo) throws InvalidAccountException {
        return Accounts.validateAccountNumber(accNo);
    }

    public boolean validatePIN(String accNo,String Pin) throws InvalidAccountException, InvalidIdentificationException {
        BankCustomer customer=Accounts.getCustomerForAccount(accNo);
        if(customer.getNoOfFailedLoginAttempts()<2 && customer.validatePin(Pin)) {
            customer.setNoOfFailedLoginAttempts(0);
            Accounts.persistChangesToFile();
            return true;
        }else if(customer.getNoOfFailedLoginAttempts()>=2){
            throw new InvalidIdentificationException();
        }else{
            customer.setNoOfFailedLoginAttempts(customer.getNoOfFailedLoginAttempts()+1);
            Accounts.persistChangesToFile();
        }


        return false;
    }

    public String getBalance(String accNo,BankAccount.TYPE accountType) throws InvalidAccountException {
        BankCustomer customer=Accounts.getCustomerForAccount(accNo);
        Double accountBalance=new Double(0);
        BankAccount account;
        if((account=customer.getAccountOfType(accountType))!=null)
            accountBalance=account.getBalance();
        return accountBalance.toString();
    }

    public boolean registerUserSession(String accNo) throws InvalidAccountException{
        BankCustomer customer=Accounts.getCustomerForAccount(accNo);
        customer.setNoOfAtmSessions(customer.getNoOfFailedLoginAttempts()+1);
        customer.setLastAtmLoginTime(new Date());
        Accounts.persistChangesToFile();
        return true;
    }

    public boolean hasAccountOfType(String accNo,BankAccount.TYPE accountType) throws InvalidAccountException {
        BankCustomer customer=Accounts.getCustomerForAccount(accNo);
        if(customer.getAccountOfType(accountType)!=null)
            return true;
        else
            return false;
    }

    public boolean hasSufficientFundInAccount(String accNo,BankAccount.TYPE accountType
        ,String withdrawAmount) throws InvalidAccountException, InvalidActionException {
        try {
            Double balance = Double.valueOf(getBalance(accNo, accountType));
            Double withdrawAmt = Double.valueOf(withdrawAmount);
            if(balance-withdrawAmt>=0)
                return true;
        }catch (NumberFormatException ex) {
            throw new InvalidActionException();
        }
        return false;
    }

    public void withdrawAmount(String accNo,BankAccount.TYPE accountType
            ,String withdrawAmount) throws InvalidAccountException, InvalidActionException {
        try {
            BankCustomer customer=Accounts.getCustomerForAccount(accNo);
            Double balance = Double.valueOf(getBalance(accNo, accountType));
            Double withdrawAmt = Double.valueOf(withdrawAmount);
            if(hasSufficientFundInAccount(accNo,accountType,withdrawAmount) &&
                    customer.getAccountOfType(accountType)!=null)
                customer.getAccountOfType(accountType).setBalance(balance-withdrawAmt);
            Accounts.persistChangesToFile();
        }catch (NumberFormatException ex) {
            throw new InvalidActionException();
        }

    }

    public void depositAmount(String accNo,BankAccount.TYPE accountType,String depositAmount) throws InvalidAccountException, InvalidActionException {
        try {
            BankCustomer customer=Accounts.getCustomerForAccount(accNo);
            Double balance = Double.valueOf(getBalance(accNo, accountType));
            Double depositAmt = Double.valueOf(depositAmount);
            if(customer.getAccountOfType(accountType)!=null)
                customer.getAccountOfType(accountType).setBalance(balance+depositAmt);
            Accounts.persistChangesToFile();
        }catch (NumberFormatException ex) {
            throw new InvalidActionException();
        }

    }



}
