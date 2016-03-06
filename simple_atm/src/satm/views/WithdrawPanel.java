package satm.views;

import satm.controller.ViewController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WithdrawPanel extends ParentPanel{

    public WithdrawPanel(){
        super();

    }

    @Override
    public void init() {
        insertCard.setText("");
        depositEnvelope.setText("");
        printReceipt.setText("");
        inputBar.setText("");
        cashDispenser.setText("");

        insertCard.setEditable(false);
        insertCard.setText(ViewController.getSessionAccountNumber());
        depositEnvelope.setEditable(false);
        cashDispenser.setEditable(false);
        printReceipt.setEditable(false);
        inputBar.setEditable(true);
        removeAllListener(screenButton1);
        removeAllListener(screenButton2);
        removeAllListener(screenButton3);

        screenButton1.setVisible(true);
        screenButton1.setEnabled(true);
        screenButton1.setText("Proceed");

        screenButton2.setVisible(false);
        screenButton3.setVisible(false);

        displayScreen.setText(MessageStrings.WITHDRAW_WELCOME);

        setDialerPanelConnectedField(inputBar);

        screenButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onProceedButtonPressed();
            }
        });

        removeAllListener(cancelButton);
        cancelButton.setEnabled(true);
        cancelButton.setVisible(true);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                onCancelButtonPressed();
            }
        });



    }

    public void postWithDraw(){
        displayScreen.setText(MessageStrings.WITHDRAW_SUCCESSFUL);
        cashDispenser.setText(inputBar.getText());
        inputBar.setText("");
        inputBar.setEditable(false);
        removeAllListener(screenButton1);
        removeAllListener(cancelButton);
        cancelButton.setVisible(false);

        screenButton1.setVisible(true);
        screenButton1.setEnabled(true);
        screenButton1.setText("yes");
        screenButton1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed (ActionEvent e){
                onYesButtonPressed();
            }
        });

        screenButton2.setVisible(true);
        screenButton2.setEnabled(true);
        screenButton2.setText("no");
        screenButton2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed (ActionEvent e){
                onNoButtonPressed();
            }
        });

    }

    public void onProceedButtonPressed(){
        String inputText=inputBar.getText();
        try {
            if (inputText != null && Double.valueOf(inputText) % 10 == 0 && Integer.valueOf(inputText)>0
                    && ViewController.validateWithdrawal(inputText) ) {
                ViewController.withdawAmount(inputText);
                postWithDraw();
                return;
            }else if(Double.valueOf(inputText) % 10 != 0){
                displayScreen.setText(MessageStrings.WITHDRAW_CANNOT_DISPENSE);
                return;
            }else if(ViewController.validateWithdrawal(inputText)){
                displayScreen.setText(MessageStrings.WITHDRAW_INSUFFICIENT_FUND);
                return;
            }

        }catch(NumberFormatException ex){
            //not a number
        }
        displayScreen.setText(MessageStrings.WITHDRAW_INVALID_AMT);
    }

    public void onCancelButtonPressed(){
        ViewController.performAction(ViewController.ACTION.TRANSACTION_HOME);
    }

    public void onYesButtonPressed(){
        ViewController.performAction(ViewController.ACTION.TRANSACTION_HOME);
    }

    public void onNoButtonPressed(){
        ViewController.performAction(ViewController.ACTION.START);
    }

    public String getCashDispensed(){
        if(cashDispenser!=null)
            return cashDispenser.getText();

        return "";
    }
}
