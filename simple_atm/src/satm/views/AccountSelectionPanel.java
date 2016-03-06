package satm.views;

import satm.controller.ViewController;
import satm.model.BankAccount;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountSelectionPanel extends ParentPanel{

    public AccountSelectionPanel(){
        super();
    }

    public void init(){
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

        screenButton1.setVisible(false);
        screenButton2.setVisible(false);
        screenButton3.setVisible(false);

        displayScreen.setText(MessageStrings.ACCTSTPANEL_SELECT_ACCTTYPE);

        screenButton1.setVisible(false);
        screenButton2.setVisible(false);
        screenButton3.setVisible(false);
        removeAllListener(screenButton1);
        removeAllListener(screenButton2);
        removeAllListener(screenButton3);

        if(ViewController.hasAccountOfType(BankAccount.TYPE.CHECKING)){
            screenButton1.setVisible(true);
            screenButton1.setEnabled(true);
            screenButton1.setText("checking");
            screenButton1.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed (ActionEvent e){
                    onCheckingButtonPressed();
                }
            });

        }

        if(ViewController.hasAccountOfType(BankAccount.TYPE.CHECKING)){
            screenButton2.setVisible(true);
            screenButton2.setEnabled(true);
            screenButton2.setText("savings");
            screenButton2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed (ActionEvent e){
                    onSavingsButtonPressed();
                }
            });

        }

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

    public void onCheckingButtonPressed(){
        ViewController.registerSessionAccountType(BankAccount.TYPE.CHECKING);
        ViewController.performAction(ViewController.ACTION.TRANSACTION);
    }

    public void onSavingsButtonPressed(){
        ViewController.registerSessionAccountType(BankAccount.TYPE.SAVING);
        ViewController.performAction(ViewController.ACTION.TRANSACTION);
    }

    public void onCancelButtonPressed(){
        ViewController.performAction(ViewController.ACTION.TRANSACTION_HOME);
    }


}
