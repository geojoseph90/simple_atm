package satm.views;


import satm.controller.ViewController;
import satm.model.Accounts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransactionPanel extends ParentPanel{

    public TransactionPanel(){
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
        inputBar.setEditable(false);

        removeAllListener(screenButton1);
        removeAllListener(screenButton2);
        removeAllListener(screenButton3);

        screenButton1.setVisible(true);
        screenButton1.setEnabled(true);
        screenButton1.setText("balance");

        screenButton2.setVisible(true);
        screenButton2.setEnabled(true);
        screenButton2.setText("deposit");

        screenButton3.setVisible(true);
        screenButton3.setEnabled(true);
        screenButton3.setText("withdrawal");

        displayScreen.setText(MessageStrings.TRANSACTION_WELCOME);

        screenButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e){
                onBalanceButtonPressed();
            }
        });

        screenButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e){
                onDepositButtonPressed();
            }
        });

        screenButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e){
                onWithdrawButtonPressed();
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

    public void onBalanceButtonPressed(){
        ViewController.registerSessionTransactionType(ViewController.TRANSACTION_TYPE.BALANCE);
        ViewController.performAction(ViewController.ACTION.ACCOUNT_TYPE);
    }

    public void onDepositButtonPressed(){
        ViewController.registerSessionTransactionType(ViewController.TRANSACTION_TYPE.DEPOSIT);
        ViewController.performAction(ViewController.ACTION.ACCOUNT_TYPE);
    }

    public void onWithdrawButtonPressed(){
        ViewController.registerSessionTransactionType(ViewController.TRANSACTION_TYPE.WITHDRAW);
        ViewController.performAction(ViewController.ACTION.ACCOUNT_TYPE);
    }

    public void onCancelButtonPressed(){
        ViewController.performAction(ViewController.ACTION.START);
    }
}
