package satm.controller;


import satm.exception.*;
import satm.model.BankAccount;
import satm.model.UserSession;
import satm.views.Satm;

import java.util.*;

public class ViewController {

    public static enum ACTION{START,AUTHENTICATE, TRANSACTION_HOME,DEPOSIT,CHECK_BALANCE,WITHDRAW,ACCOUNT_TYPE,
        TRANSACTION,ERROR}
    public static enum TRANSACTION_TYPE{BALANCE,DEPOSIT,WITHDRAW}
    private static Satm INSTANCE=null;
    private static ModelController modelController=new ModelController();
    private static UserSession currentSession=null;
    private static java.util.List<ExceptionListener> errorListeners=new ArrayList<ExceptionListener>();
    public static void startup(){
        if(INSTANCE==null)
            INSTANCE=new Satm();
        else {
            currentSession=null;
            errorListeners=new ArrayList<ExceptionListener>();
            INSTANCE.reset();
        }
    }

    public static void performAction(ACTION action){
        switch (action){
            case START:
                startup();
                break;
            case AUTHENTICATE:
                actionAuthenticate();
                break;
            case TRANSACTION_HOME:
                actionTransactionHome();
                break;
            case TRANSACTION:
                actionTransaction();
                break;
            case ACCOUNT_TYPE:
                actionAccountType();
                break;
            default:
                handleException(new InvalidActionException());

        }
    }

    private static void actionAuthenticate(){
        if(currentSession==null||currentSession.getAccountNumber()==null) {
            handleException(new InvalidActionException());
            return;
        }
        INSTANCE.switchToCardIndex(ACTION.AUTHENTICATE.toString());
    }

    private static void actionTransactionHome(){
        if(validSession()) {
            //these fields will be updated in later stages
            currentSession.setTransactionType(null);
            currentSession.setSessionAccountType(null);
            INSTANCE.switchToCardIndex(ACTION.TRANSACTION_HOME.toString());
        }
    }

    private static void actionAccountType(){
        if(validSession() && currentSession.getTransactionType()!=null){
            INSTANCE.switchToCardIndex(ACTION.ACCOUNT_TYPE.toString());
        }else{
            handleException(new InvalidActionException());
        }
    }

    private static void actionTransaction(){
        //infer transaction from account and transaction selection in session
        if(validSession() && currentSession.getTransactionType()!=null
                && currentSession.getSessionAccountType()!=null ){
            switch (currentSession.getTransactionType()){
                case BALANCE:
                    INSTANCE.switchToCardIndex(ACTION.CHECK_BALANCE.toString());
                    break;
                case DEPOSIT:
                    if(TerminalUnitController.INSTANCE.isUnitOk(TerminalUnitController.ExternalUnits.ENVELOPE_DEPOSIT))
                        INSTANCE.switchToCardIndex(ACTION.DEPOSIT.toString());
                    else
                        handleException(new DepositSlotException());

                    break;
                case WITHDRAW:
                    INSTANCE.switchToCardIndex(ACTION.WITHDRAW.toString());
                    break;
                default:
                    //shouldn't reach here ideally
                    handleException(new InvalidActionException());
            }
        }else{
            handleException(new InvalidActionException());
        }


    }

    public static String getBalance(){
        try {
            if(validSession() && currentSession.getTransactionType()!=null
                    && currentSession.getSessionAccountType()!=null
                    && modelController.hasAccountOfType(currentSession.getAccountNumber(),currentSession.getSessionAccountType())){
                return modelController.getBalance(currentSession.getAccountNumber(),currentSession.getSessionAccountType());
            }else{
                throw new InvalidActionException();
            }
        } catch (InvalidAccountException e) {
           handleException(e);
        }catch (InvalidActionException e){
            handleException(e);
        }
        return "0";
    }

    public static boolean  hasAccountOfType(BankAccount.TYPE accountType){
        try {
            if(validSession() &&
                    modelController.hasAccountOfType(currentSession.getAccountNumber(),accountType))
                return true;
        } catch (InvalidAccountException e) {
            handleException(e);
        }
        return false;
    }


    public static boolean isSessionActive(){
        if(currentSession==null || currentSession.getLastActivity()==null ||
                //(new Date().getTime() - currentSession.getLastActivity().getTime())> 60000 ||
                 currentSession.getLogin()==null || currentSession.getSessionId()==null ||
                currentSession.getSessionId()!=INSTANCE.getSessionId()) {
            currentSession=null;
            return false;
        }

        return true;
    }

    public static boolean verifyAccount(String accNo){
        try{
            if(modelController.validateCard(accNo)){
                currentSession=new UserSession();
                currentSession.setAccountNumber(accNo);
                return true;
            }
        }catch (Exception ex) {
            handleException(ex);
        }
        return false;
    }

    public static boolean verifyPin(String pin){
        if(currentSession==null||currentSession.getAccountNumber()==null)
            return false;

        try {
            if(modelController.validatePIN(currentSession.getAccountNumber(),pin)){
                currentSession.setLogin(new Date());
                currentSession.setLastActivity(new Date());
                currentSession.setSessionId(new Date().getTime());
                modelController.registerUserSession(currentSession.getAccountNumber());
                //consistency checker
                INSTANCE.setSessionId(currentSession.getSessionId());
                return true;
            }
        } catch (InvalidAccountException e) {
            handleException(e);
        } catch (InvalidIdentificationException e) {
            handleException(e);
        }
        return false;

    }

    public static void handleException(Exception ex){
        ExceptionEvent exceptionEvent;
        if(ex instanceof InvalidIdentificationException|| ex instanceof  InvalidAccountException ||
                ex instanceof InvalidActionException || ex instanceof InvalidSessionException) {
            exceptionEvent = new ExceptionEvent(ex, true);

        }else if(ex instanceof DispenserTerminalException|| ex instanceof DepositSlotException){
            exceptionEvent=new ExceptionEvent(ex,true,true);
        } else
            exceptionEvent=new ExceptionEvent(ex,false );
        for (ExceptionListener listener:errorListeners){
            listener.handleException(exceptionEvent);
        }
        INSTANCE.switchToCardIndex(ACTION.ERROR.toString());
    }

    public static void addExceptionListener(ExceptionListener listener){
        errorListeners.add(listener);
    }

    public static void registerSessionAccountType(BankAccount.TYPE accountType){
        if(validSession())
            currentSession.setSessionAccountType(accountType);
    }

    public static void registerSessionTransactionType(TRANSACTION_TYPE transactionType){
        if(validSession())
            currentSession.setTransactionType(transactionType);
    }

    private static boolean validSession(){
        if(!isSessionActive()){
            handleException(new InvalidSessionException());
            return false;
        }
        return true;
    }

    public static String getSessionAccountNumber(){
        if(currentSession.getAccountNumber()!=null)
            return currentSession.getAccountNumber();
        validSession();
        return "";
    }

    public static boolean validateWithdrawal(String amount){

        try {

            if(TerminalUnitController.INSTANCE.isUnitOk(TerminalUnitController.ExternalUnits.CASH_DISPENSER))
                return modelController.hasSufficientFundInAccount(currentSession.getAccountNumber(),
                        currentSession.getSessionAccountType(),amount);
            else
                throw new DispenserTerminalException();
        } catch (InvalidAccountException e) {
            handleException(e);
        } catch (InvalidActionException e) {
            handleException(e);
        } catch (DispenserTerminalException e) {
            handleException(e);
        }

        return false;
    }

    public static void withdawAmount(String amount){
        try {
            modelController.withdrawAmount(currentSession.getAccountNumber(),
                    currentSession.getSessionAccountType(),amount);
        } catch (InvalidAccountException e) {
            handleException(e);
        } catch (InvalidActionException e) {
            handleException(e);
        }
    }

    public static void depositAmount(String amount){
        try{
            modelController.depositAmount(currentSession.getAccountNumber(),currentSession.getSessionAccountType(),amount);
        }catch (InvalidAccountException e) {
            handleException(e);
        } catch (InvalidActionException e) {
            handleException(e);
        }

    }

    public static Satm getSatmInstance(){
        return INSTANCE;
    }



}
