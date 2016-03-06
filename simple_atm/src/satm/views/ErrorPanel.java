package satm.views;

import satm.controller.ViewController;
import satm.exception.ExceptionEvent;
import satm.exception.ExceptionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ErrorPanel extends ParentPanel implements ExceptionListener {

    public ErrorPanel(){
        super();
        screenButton1.setVisible(false);
        screenButton2.setVisible(false);
        screenButton3.setVisible(false);
        cancelButton.setVisible(false);
        removeAllListener(screenButton1);
        removeAllListener(screenButton2);
        removeAllListener(screenButton3);
        removeAllListener(cancelButton);

        //listen for exception events
        ViewController.addExceptionListener(this);

    }

    @Override
    public void init() {
        insertCard.setText("");
        depositEnvelope.setText("");
        printReceipt.setText("");
        inputBar.setText("");

        insertCard.setEditable(false);
        depositEnvelope.setEditable(false);
        cashDispenser.setEditable(false);
        printReceipt.setEditable(false);
        inputBar.setEditable(false);


    }

    @Override
    public void handleException(ExceptionEvent exceptionEvent) {
        screenButton1.setVisible(false);
        screenButton2.setVisible(false);
        screenButton3.setVisible(false);
        cancelButton.setVisible(false);
        removeAllListener(screenButton1);
        removeAllListener(screenButton2);
        removeAllListener(screenButton3);
        removeAllListener(cancelButton);

        displayScreen.setText(exceptionEvent.getException().getMessage());
        if(exceptionEvent.isRecoverable() && exceptionEvent.isDuringTransaction()) {
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
                public void actionPerformed(ActionEvent e) {
                    onNoButtonPressed();
                }
            });
            displayScreen.setText(displayScreen.getText().concat(MessageStrings.ERROR_ANOTHER_TRANSACTION));

        }else if(exceptionEvent.isRecoverable()){
            screenButton1.setVisible(true);
            screenButton1.setText("Start Over");
            displayScreen.setText(displayScreen.getText().concat(MessageStrings.ERROR_START_OVER));
            screenButton1.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    onNoButtonPressed();
                }
            });

        }else {
            screenButton1.setVisible(false);
            displayScreen.setText(displayScreen.getText().concat(MessageStrings.ERROR_FATAL_ERROR));
        }

    }

    public void onYesButtonPressed(){
        ViewController.performAction(ViewController.ACTION.TRANSACTION_HOME);
    }

    public void onNoButtonPressed(){
        ViewController.performAction(ViewController.ACTION.START);
    }

}
