package satm.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class ParentPanel extends JPanel{
    JButton screenButton1,screenButton2,screenButton3,numbersButton[],cancelButton;
    JTextField cashDispenser,depositEnvelope,printReceipt,insertCard, inputBar,dialPadConnectedField;
    JLabel label1,label2,label3,label4,label5;
    JPanel inputPanel,screenPanel,panelHome,dialerPanel;
    JTextArea displayScreen;
    JButton dialerButton[];
    Container cardContainer;
    public ParentPanel(){
        //create io fields
        inputBar=new JTextField();
        cashDispenser=new JTextField();
        depositEnvelope=new JTextField();
        printReceipt=new JTextField();
        insertCard=new JTextField();

        //disabled by default
        inputBar.setEditable(false);
        cashDispenser.setEditable(false);
        depositEnvelope.setEditable(false);
        printReceipt.setEditable(false);
        insertCard.setEditable(false);

        //label fields
        label5=new JLabel("Input");
        label1=new JLabel("Card");
        label2=new JLabel("Deposit");
        label3=new JLabel("Statements");
        label4=new JLabel("Cash");

        screenButton1=new JButton("Option A/Enter");
        screenButton2=new JButton("Option B");
        screenButton3=new JButton("Option C");
        cancelButton= new JButton("Cancel");
        screenButton1.setVisible(false);
        screenButton2.setVisible(false);
        screenButton3.setVisible(false);
        cancelButton.setVisible(false);

        //input panel
        inputPanel=new JPanel();
        inputPanel.setLayout(new GridLayout(4,1));
        inputPanel.add(screenButton1);
        inputPanel.add(screenButton2);
        inputPanel.add(screenButton3);
        inputPanel.add(cancelButton);

        displayScreen=new JTextArea("");
        displayScreen.setPreferredSize(new Dimension(250,250));
        displayScreen.setEditable(false);
        displayScreen.setLineWrap(true);
        screenPanel=new JPanel();
        screenPanel.add(displayScreen);

        panelHome=new JPanel();
        panelHome.setLayout(new GridLayout(5,2));
        panelHome.add(label5);
        panelHome.add(inputBar);
        panelHome.add(label1);
        panelHome.add(insertCard);
        panelHome.add(label2);
        panelHome.add(depositEnvelope);
        panelHome.add(label4);
        panelHome.add(cashDispenser);
        panelHome.add(label3);
        panelHome.add(printReceipt);
        panelHome.setLayout(new GridLayout(5,1));

        //dialer panel
        dialerPanel=new JPanel();
        dialerPanel.setLayout(new GridLayout(3,3));
        String dial[]={"1","2","3","4","5","6","7","8","9","0","Cancel"};
        dialerButton=new JButton[10];

        for(int i=0;i<10;i++)
        {
            dialerButton[i]=new JButton(dial[i]);
            dialerPanel.add(dialerButton[i]);
        }
        dialerPanel.setLayout(new GridLayout(5,3));
        for(JButton button : dialerButton)
            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton currentButton=(JButton)e.getSource();
                    int number=currentButton.getText().charAt(0)-'0';
                    if(dialPadConnectedField!=null)
                        dialPadConnectedField.setText(dialPadConnectedField.getText()+number);
                }
            });


        //add all this to current panel
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.add(screenPanel, BorderLayout.WEST);
        this.add(inputPanel, BorderLayout.CENTER);
        this.add(panelHome, BorderLayout.SOUTH);
        this.add(dialerPanel,BorderLayout.EAST);

    }



    public void setDialerPanelConnectedField(JTextField textField){
        this.dialPadConnectedField=textField;

    }

    public abstract void init();

    public void removeAllListener(JButton button){
        for( ActionListener al : button.getActionListeners() ) {
            button.removeActionListener( al );
        }

    }

    public String getDisplayText(){
        return displayScreen.getText();
    }

    public String getDialerPanelInputText(){
        if(dialPadConnectedField!=null)
            return dialPadConnectedField.getText();

        return "";
    }

    public void setDialerPanelInputText(String text){
        if(dialPadConnectedField!=null)
            dialPadConnectedField.setText(text);
    }



}
