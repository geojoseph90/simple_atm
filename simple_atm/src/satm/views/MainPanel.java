package satm.views;

import satm.controller.ModelController;
import satm.controller.ViewController;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends ParentPanel{

    public MainPanel(){
        //initialize parent components
        super();
    }

    @Override
    public void init() {
        insertCard.setText("");
        depositEnvelope.setText("");
        printReceipt.setText("");
        inputBar.setText("");
        cashDispenser.setText("");

        //override state
        removeAllListener(screenButton1);
        removeAllListener(screenButton2);
        removeAllListener(screenButton3);

        insertCard.setEditable(true);
        depositEnvelope.setEditable(false);
        cashDispenser.setEditable(false);
        printReceipt.setEditable(false);
        inputBar.setEditable(false);

        screenButton1.setVisible(true);
        screenButton1.setEnabled(true);
        screenButton1.setText("Proceed");

        screenButton2.setVisible(false);
        screenButton3.setVisible(false);

        cancelButton.setEnabled(true);
        cancelButton.setVisible(true);

        setDialerPanelConnectedField(insertCard);
        displayScreen.setText(MessageStrings.MAINPANEL_WELCOME_MESSAGE);


        screenButton1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                onProceedButtonPressed();
            }
        });

    }

    public void onProceedButtonPressed(){
        String inputText=insertCard.getText();
        try {
            if (inputText != null && inputText.length() == 8
                    && Double.valueOf(inputText) % 1 == 0 && Integer.valueOf(inputText)>0
                    && ViewController.verifyAccount(inputText)) {
                ViewController.performAction(ViewController.ACTION.AUTHENTICATE);
                return;
            }
        }catch(NumberFormatException ex){
            //not a number
        }
        displayScreen.setText(MessageStrings.MAINPANEL_INVALID_ACCOUNT);

    }
}
