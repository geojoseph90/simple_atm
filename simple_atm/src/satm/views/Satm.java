package satm.views;


import satm.controller.ViewController;

import javax.swing.*;
import java.awt.*;

public class Satm {
    JFrame frame;
    JPanel cardPanelContainer,startPanel,pinPanel,withdrawPanel,balancePanel,transactionPanel,errorPanel,
            accountTypePanel,depositPanel;
    CardLayout cardLayout;
    private Long sessionId;

    public Satm(){
        frame=new JFrame("SATM");
        frame.setSize(600, 500);
        frame.setLayout(new GridLayout(2,3));
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        init();
        frame.add(cardPanelContainer);
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void init(){
        cardPanelContainer=new JPanel();
        cardLayout=new CardLayout();
        cardPanelContainer.setLayout(cardLayout);

        startPanel=new MainPanel();
        pinPanel=new AuthenticationPanel();
        withdrawPanel=new WithdrawPanel();
        balancePanel=new BalancePanel();
        transactionPanel=new TransactionPanel();
        errorPanel=new ErrorPanel();
        accountTypePanel=new AccountSelectionPanel();
        depositPanel=new DepositPanel();

        cardPanelContainer.add(startPanel, ViewController.ACTION.START.toString());
        cardPanelContainer.add(pinPanel,ViewController.ACTION.AUTHENTICATE.toString());
        cardPanelContainer.add(withdrawPanel,ViewController.ACTION.WITHDRAW.toString());
        cardPanelContainer.add(balancePanel,ViewController.ACTION.CHECK_BALANCE.toString());
        cardPanelContainer.add(transactionPanel,ViewController.ACTION.TRANSACTION_HOME.toString());
        cardPanelContainer.add(errorPanel, ViewController.ACTION.ERROR.toString());
        cardPanelContainer.add(accountTypePanel, ViewController.ACTION.ACCOUNT_TYPE.toString());
        cardPanelContainer.add(depositPanel,ViewController.ACTION.DEPOSIT.toString());
        cardLayout.show(cardPanelContainer, ViewController.ACTION.START.toString());
        ((ParentPanel)startPanel).init();

    }

    public void reset(){
        frame.remove(cardPanelContainer);
        init();
        frame.add(cardPanelContainer);
        frame.pack();
        frame.setVisible(true);
    }

    public Long getSessionId(){
        return sessionId;
    }

    public void setSessionId(Long sessionId){
        this.sessionId=sessionId;
    }

    public void switchToCardIndex(String index){
        cardLayout.show(cardPanelContainer, index);
        for (Component component:cardPanelContainer.getComponents())
            if(component instanceof ParentPanel && component.isVisible())
                ((ParentPanel)component).init();
    }

    public ParentPanel getActiveComponent(){
        for (Component component:cardPanelContainer.getComponents())
            if(component instanceof ParentPanel && component.isVisible())
                return (ParentPanel)component;

        return null;
    }

}
