package satm.views;

import satm.controller.ViewController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DepositPanel extends ParentPanel{
    public DepositPanel(){
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
        depositEnvelope.setEditable(true);
        removeAllListener(screenButton1);
        removeAllListener(screenButton2);
        removeAllListener(screenButton3);

        screenButton1.setVisible(true);
        screenButton1.setEnabled(true);
        screenButton1.setText("Proceed");

        screenButton2.setVisible(false);
        screenButton3.setVisible(false);

        displayScreen.setText(MessageStrings.DEPOSIT_WELCOME);

        setDialerPanelConnectedField(depositEnvelope);

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

    public void onProceedButtonPressed(){
        String inputText = depositEnvelope.getText();
        try {
            if (inputText != null && Double.valueOf(inputText) % 10 == 0 && Integer.valueOf(inputText) > 0) {
                ViewController.depositAmount(inputText);
                ViewController.registerSessionTransactionType(ViewController.TRANSACTION_TYPE.BALANCE);
                ViewController.performAction(ViewController.ACTION.TRANSACTION);
                return;
            } else if (Double.valueOf(inputText) % 10 != 0) {
                displayScreen.setText(MessageStrings.DEPOSIT_CANNOT_DEPOSIT_AMT);
                return;
            }
        } catch (NumberFormatException ex) {
            //not a number
        }
        displayScreen.setText(MessageStrings.DEPOSIT_INVALID_AMT);

    }

    public void onCancelButtonPressed(){
        ViewController.performAction(ViewController.ACTION.TRANSACTION_HOME);
    }

}
