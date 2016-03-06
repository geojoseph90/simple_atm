package satm.views;

import satm.controller.ViewController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AuthenticationPanel extends ParentPanel {

    public AuthenticationPanel(){
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


        displayScreen.setText(MessageStrings.AUTHPANEL_ENTER_PIN);

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

    public void onProceedButtonPressed(){
        String inputText = inputBar.getText();
        try {
            if (inputText != null && inputText.length() > 0 && Double.valueOf(inputText)%1 == 0
                    && Integer.valueOf(inputText) > 0 && ViewController.verifyPin(inputText)){
                ViewController.performAction(ViewController.ACTION.TRANSACTION_HOME);
                return;
            }
        } catch (NumberFormatException ex) {
            //invalid input
        }
        displayScreen.setText(MessageStrings.AUTHPANEL_INVALID_PIN);
    }

    public void onCancelButtonPressed(){
        ViewController.performAction(ViewController.ACTION.START);
    }

}
