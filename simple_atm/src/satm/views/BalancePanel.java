package satm.views;


import satm.controller.ModelController;
import satm.controller.ViewController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BalancePanel extends ParentPanel{

    public BalancePanel(){
        super();
    }

    public void init(){
        insertCard.setText("");
        depositEnvelope.setText("");
        printReceipt.setText("");
        inputBar.setText("");
        cashDispenser.setText("");

        removeAllListener(screenButton1);
        removeAllListener(screenButton2);
        removeAllListener(screenButton3);

        depositEnvelope.setEditable(false);
        cashDispenser.setEditable(false);
        inputBar.setEditable(false);
        printReceipt.setEditable(false);
        insertCard.setEditable(false);
        insertCard.setText(ViewController.getSessionAccountNumber());

        screenButton1.setVisible(false);
        screenButton2.setVisible(false);
        screenButton3.setVisible(false);
        displayScreen.setText(MessageStrings.BALANCEPANEL_BALANCE_MESSAGE);

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

        printReceipt.setText("");
        printReceipt.setText(ViewController.getBalance());

    }

    public void onYesButtonPressed(){
        ViewController.performAction(ViewController.ACTION.TRANSACTION_HOME);
    }

    public void onNoButtonPressed(){
        ViewController.performAction(ViewController.ACTION.START);
    }

    public String getPrintReceipt(){
        return printReceipt.getText();
    }

}
