package satm.model;

import satm.exception.InvalidAccountException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Accounts {

    private static HashMap<String,BankCustomer> accounts=new HashMap<String, BankCustomer>();
    private static String accountDataFile="map.ser";

    public static void loadDemoDataFromFile(){
        try {
            FileInputStream fis = new FileInputStream(accountDataFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            accounts.clear();
            accounts.putAll((HashMap<String, BankCustomer>) ois.readObject());
            ois.close();
        } catch (FileNotFoundException e){
            //cannot read file for somer reason. attempt regenerating data
            generateDemoData();
            writeDemoToFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public static void writeDemoToFile(){
        try {
            FileOutputStream fos = new FileOutputStream(accountDataFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(accounts);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void persistChangesToFile(){
        writeDemoToFile();
    }

    public static void generateDemoData(){
        //create demo data
        accounts.clear();
        BankCustomer customer=new BankCustomer("John Smith","90000012","7612");
        customer.setSavingsAccount(new BankAccount(10000d, BankAccount.TYPE.SAVING));
        customer.setCheckingAccount(new BankAccount(10005d, BankAccount.TYPE.CHECKING));
        accounts.put(customer.getAccountNumber(), customer);
        customer=new BankCustomer("Marc Antony","90000024","0076");
        customer.setSavingsAccount(new BankAccount(20000d, BankAccount.TYPE.SAVING));
        customer.setCheckingAccount(new BankAccount(20005d, BankAccount.TYPE.CHECKING));
        accounts.put(customer.getAccountNumber(), customer);
        customer=new BankCustomer("Lizy Abraham","90000011","9735");
        customer.setSavingsAccount(new BankAccount(30000d, BankAccount.TYPE.SAVING));
        customer.setCheckingAccount(new BankAccount(30005d, BankAccount.TYPE.CHECKING));
        accounts.put(customer.getAccountNumber(),customer);
    }

    public static boolean validateAccountNumber(String accountNumber) throws InvalidAccountException {
        if(accounts.containsKey(accountNumber))
            return true;
        throw new InvalidAccountException();
    }

    public static BankCustomer getCustomerForAccount(String accountNumber) throws InvalidAccountException {
        if(!accounts.containsKey(accountNumber))
            throw new InvalidAccountException();
        else
            return accounts.get(accountNumber);
    }
}
