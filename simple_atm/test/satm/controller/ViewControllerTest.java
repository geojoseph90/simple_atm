package satm.controller;

import org.junit.*;
import satm.exception.DepositSlotException;
import satm.exception.InvalidIdentificationException;
import satm.model.Accounts;
import satm.model.BankCustomer;
import satm.views.*;

import static satm.views.MessageStrings.*;
import java.lang.reflect.Field;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ViewControllerTest {
    private static String accountTestDataFile="test.ser";
    private static HashMap<String,BankCustomer> accounts;
    private static MockTerminalUnitController mockTerminalUnitController= new MockTerminalUnitController();
    Satm satm;
    BankCustomer testCustomer;
    @Before
    public void setUp() throws Exception {
        ViewController.startup();
        //regenerate and reload data
        Accounts.generateDemoData();
        Accounts.writeDemoToFile();
        Accounts.loadDemoDataFromFile();

        //use first customer as test account
        if(accounts.entrySet().size()>0)
            testCustomer =accounts.get(accounts.keySet().toArray()[0].toString());

        satm =ViewController.getSatmInstance();
        assertNotNull("satm instance", satm);
    }

    @After
    public void tearDown() throws Exception {

    }


    //scenario1
    @Test
    public void testPresentValidAtmCardUC1() throws Exception {
        assertNotNull("active panel not null", satm.getActiveComponent());
        assertTrue("main panel is active", satm.getActiveComponent() instanceof MainPanel);
        MainPanel mainPanel=(MainPanel) satm.getActiveComponent();
        assertTrue("welcome message displayed",MAINPANEL_WELCOME_MESSAGE.equals(mainPanel.getDisplayText()));
        insertValidCard();
        assertTrue("authentication panel is displayed", satm.getActiveComponent() instanceof AuthenticationPanel);

    }

    @Test
    public void testPresentInvalidAtmCardUC2(){
        assertNotNull("active panel not null", satm.getActiveComponent());
        assertTrue("main panel is active", satm.getActiveComponent() instanceof MainPanel);
        MainPanel currPanel=(MainPanel) satm.getActiveComponent();
        currPanel.setDialerPanelInputText("122312");
        currPanel.onProceedButtonPressed();
        assertTrue("doesnot proceed to authentication", satm.getActiveComponent() instanceof MainPanel);
        currPanel=(MainPanel) satm.getActiveComponent();
        assertTrue("invalid account message displayed",MAINPANEL_INVALID_ACCOUNT.equals(currPanel.getDisplayText()));

    }

    @Test
    public void testCorrectPinEnteredUC3(){
        insertValidCard();
        assertNotNull("active panel not null", satm.getActiveComponent());
        assertTrue("authentication panel is displayed", satm.getActiveComponent() instanceof AuthenticationPanel);
        ParentPanel currPanel=(ParentPanel) satm.getActiveComponent();
        assertTrue("enter pin message displayed", MessageStrings.AUTHPANEL_ENTER_PIN.equals(currPanel.getDisplayText()));
        enterCorrectPin();
        currPanel=(ParentPanel) satm.getActiveComponent();
        assertTrue("select transaction message displayed", MessageStrings.TRANSACTION_WELCOME.equals(currPanel.getDisplayText()));
        assertTrue("transaction panel is displayed", satm.getActiveComponent() instanceof TransactionPanel);
    }

    @Test
    public void testFailedPinEntryUC4(){
        insertValidCard();
        assertNotNull("active panel not null", satm.getActiveComponent());
        assertTrue("authentication panel visible", satm.getActiveComponent() instanceof AuthenticationPanel);
        assertTrue("enter pin message displayed", MessageStrings.AUTHPANEL_ENTER_PIN.equals(satm.getActiveComponent().getDisplayText()));
        //1st attempt
        enterIncorrectPin();
        assertTrue("incorrect pin message displayed", MessageStrings.AUTHPANEL_INVALID_PIN.equals(satm.getActiveComponent().getDisplayText()));
        assertTrue("authentication panel is retained", satm.getActiveComponent() instanceof AuthenticationPanel);
        //2st attempt
        enterIncorrectPin();
        assertTrue("incorrect pin message displayed", MessageStrings.AUTHPANEL_INVALID_PIN.equals(satm.getActiveComponent().getDisplayText()));
        assertTrue("authentication panel visible", satm.getActiveComponent() instanceof AuthenticationPanel);
        //3rd attempt
        enterIncorrectPin();
        assertTrue("card retained error message", new InvalidIdentificationException().getMessage().concat(MessageStrings.ERROR_START_OVER).
                equals(satm.getActiveComponent().getDisplayText()));
        assertTrue("error panel is displayed", satm.getActiveComponent() instanceof ErrorPanel);

    }

    @Test
    public void testTransChoiceBalanceUC5(){
        insertValidCard();
        enterCorrectPin();
        assertNotNull("active panel not null", satm.getActiveComponent());
        assertTrue("select transaction message displayed", MessageStrings.TRANSACTION_WELCOME.equals(satm.
                getActiveComponent().getDisplayText()));
        assertTrue("transaction panel visible", satm.getActiveComponent() instanceof TransactionPanel);
        TransactionPanel currPanel=(TransactionPanel) satm.getActiveComponent();

        //proceed to account selection
        currPanel.onBalanceButtonPressed();
        assertTrue("account selection panel visible", satm.getActiveComponent() instanceof AccountSelectionPanel);
        assertTrue("select checking/saving message displayed", MessageStrings.ACCTSTPANEL_SELECT_ACCTTYPE.equals(satm.
                getActiveComponent().getDisplayText()));

        //mock all terminal to working state
        mockTerminalUnitController.isAllUnitsOkay(true);

        //proceed with checking to balance panel
        ((AccountSelectionPanel) satm.getActiveComponent()).onCheckingButtonPressed();
        assertTrue("balance panel visible", satm.getActiveComponent() instanceof BalancePanel);
        assertTrue("balance message displayed", MessageStrings.BALANCEPANEL_BALANCE_MESSAGE.equals(satm.
                getActiveComponent().getDisplayText()));
        assertEquals("balance matches value in file",testCustomer.getCheckingAccount().getBalance(),
                Double.valueOf(((BalancePanel) satm.getActiveComponent()).getPrintReceipt()));
    }

    @Test
    public void testTransChoiceDepositUC6(){
        insertValidCard();
        enterCorrectPin();
        assertNotNull("active panel not null", satm.getActiveComponent());
        assertTrue("select transaction message displayed", MessageStrings.TRANSACTION_WELCOME.equals(satm.
                getActiveComponent().getDisplayText()));
        assertTrue("transaction panel visible", satm.getActiveComponent() instanceof TransactionPanel);
        TransactionPanel currPanel=(TransactionPanel) satm.getActiveComponent();

        //proceed to deposit
        currPanel.onDepositButtonPressed();
        assertTrue("account selection panel visible", satm.getActiveComponent() instanceof AccountSelectionPanel);
        assertTrue("select checking/saving message displayed", MessageStrings.ACCTSTPANEL_SELECT_ACCTTYPE.equals(satm.
                getActiveComponent().getDisplayText()));

        //mock all terminal to working state
        mockTerminalUnitController.isAllUnitsOkay(true);

        //proceed with checking to deposit
        ((AccountSelectionPanel) satm.getActiveComponent()).onCheckingButtonPressed();
        assertTrue("deposit panel visible", satm.getActiveComponent() instanceof DepositPanel);
        assertTrue("deposit instruction displayed", MessageStrings.DEPOSIT_WELCOME.equals(satm.
                getActiveComponent().getDisplayText()));

        //enter valid deposit amount
        Double balanceBeforeDeposit=testCustomer.getCheckingAccount().getBalance();
        satm.getActiveComponent().setDialerPanelInputText("5000");
        ((DepositPanel) satm.getActiveComponent()).onProceedButtonPressed();

        //will redirect to balance balance
        assertTrue("balance panel visible", satm.getActiveComponent() instanceof BalancePanel);
        assertTrue("balance message displayed", MessageStrings.BALANCEPANEL_BALANCE_MESSAGE.equals(satm.
                getActiveComponent().getDisplayText()));
        assertEquals("balance in file increased by deposit amt",new Double(balanceBeforeDeposit+5000),
                Double.valueOf(((BalancePanel) satm.getActiveComponent()).getPrintReceipt()));

        //pick no for another transaction
        ((BalancePanel) satm.getActiveComponent()).onNoButtonPressed();
        assertTrue("main start panel visible", satm.getActiveComponent() instanceof MainPanel);
        assertTrue("start screen message displayed",
                MAINPANEL_WELCOME_MESSAGE.equals(satm.getActiveComponent().getDisplayText()));

    }

    @Test
    public void testTransChoiceDepositSlotJammedUC7(){
        insertValidCard();
        enterCorrectPin();
        assertNotNull("active panel not null", satm.getActiveComponent());
        assertTrue("select transaction message displayed", MessageStrings.TRANSACTION_WELCOME.equals(satm.
                getActiveComponent().getDisplayText()));
        assertTrue("transaction panel visible", satm.getActiveComponent() instanceof TransactionPanel);
        TransactionPanel currPanel=(TransactionPanel) satm.getActiveComponent();

        //proceed to deposit
        currPanel.onDepositButtonPressed();
        assertTrue("account selection panel visible", satm.getActiveComponent() instanceof AccountSelectionPanel);
        assertTrue("select checking/saving message displayed", MessageStrings.ACCTSTPANEL_SELECT_ACCTTYPE.equals(satm.
                getActiveComponent().getDisplayText()));

        //mock deposit slot jammed case
        mockTerminalUnitController.isAllUnitsOkay(false);

        //attempt to proceed with deposit
        ((AccountSelectionPanel) satm.getActiveComponent()).onCheckingButtonPressed();

        //should get directed to slot error message
        assertTrue("deposit slot error", new DepositSlotException().getMessage()
                .concat(MessageStrings.ERROR_ANOTHER_TRANSACTION).equals(satm.getActiveComponent().getDisplayText()));
        assertTrue("error panel is displayed", satm.getActiveComponent() instanceof ErrorPanel);

        //pick no for another transaction and go back to welcome screen
        ((ErrorPanel) satm.getActiveComponent()).onNoButtonPressed();
        assertTrue("main start panel visible", satm.getActiveComponent() instanceof MainPanel);
        assertTrue("start screen message displayed",
                MAINPANEL_WELCOME_MESSAGE.equals(satm.getActiveComponent().getDisplayText()));

    }

    @Test
    public void testTransChoiceWithdrawalUC9(){
        insertValidCard();
        enterCorrectPin();
        assertNotNull("active panel not null", satm.getActiveComponent());
        assertTrue("select transaction message displayed", MessageStrings.TRANSACTION_WELCOME.equals(satm.
                getActiveComponent().getDisplayText()));
        assertTrue("transaction panel visible", satm.getActiveComponent() instanceof TransactionPanel);
        TransactionPanel currPanel=(TransactionPanel) satm.getActiveComponent();


        //proceed with withdrawal
        currPanel.onWithdrawButtonPressed();
        assertTrue("account selection panel visible", satm.getActiveComponent() instanceof AccountSelectionPanel);
        assertTrue("select checking/saving message displayed", MessageStrings.ACCTSTPANEL_SELECT_ACCTTYPE.equals(satm.
                getActiveComponent().getDisplayText()));

        //mock all terminal to working state
        mockTerminalUnitController.isAllUnitsOkay(true);

        //proceed with checking to withdraw panel
        ((AccountSelectionPanel) satm.getActiveComponent()).onCheckingButtonPressed();
        assertTrue("withdraw panel visible", satm.getActiveComponent() instanceof WithdrawPanel);
        assertTrue("withdraw instruction displayed", MessageStrings.WITHDRAW_WELCOME.equals(satm.
                getActiveComponent().getDisplayText()));

        //enter valid withdraw amt
        Double balanceBeforeWithdraw=testCustomer.getCheckingAccount().getBalance();
        satm.getActiveComponent().setDialerPanelInputText("5000");
        ((WithdrawPanel) satm.getActiveComponent()).onProceedButtonPressed();

        //withdraw successful screen
        assertTrue("withdraw successful message displayed", MessageStrings.WITHDRAW_SUCCESSFUL.equals(satm.
                getActiveComponent().getDisplayText()));
        assertEquals("verify dispensed amt",new Double(5000),
                Double.valueOf(((WithdrawPanel) satm.getActiveComponent()).getCashDispensed()));
        assertEquals("balance in file decreased by dispensed amt",new Double(balanceBeforeWithdraw-5000),
                testCustomer.getCheckingAccount().getBalance());

        //pick no for another transaction
        ((WithdrawPanel) satm.getActiveComponent()).onNoButtonPressed();
        assertTrue("main start panel visible", satm.getActiveComponent() instanceof MainPanel);
        assertTrue("start screen message displayed",
                MAINPANEL_WELCOME_MESSAGE.equals(satm.getActiveComponent().getDisplayText()));

    }

    @Test
    public void testTransChoiceWithdrawalNotMulipleUC10(){
        insertValidCard();
        enterCorrectPin();
        assertNotNull("active panel not null", satm.getActiveComponent());
        assertTrue("select transaction message displayed", MessageStrings.TRANSACTION_WELCOME.equals(satm.
                getActiveComponent().getDisplayText()));
        assertTrue("transaction panel visible", satm.getActiveComponent() instanceof TransactionPanel);
        TransactionPanel currPanel=(TransactionPanel) satm.getActiveComponent();


        //proceed with withdrawal
        currPanel.onWithdrawButtonPressed();
        assertTrue("account selection panel visible", satm.getActiveComponent() instanceof AccountSelectionPanel);
        assertTrue("select checking/saving message displayed", MessageStrings.ACCTSTPANEL_SELECT_ACCTTYPE.equals(satm.
                getActiveComponent().getDisplayText()));

        //mock all terminal to working state
        mockTerminalUnitController.isAllUnitsOkay(true);

        //proceed with checking to withdraw panel
        ((AccountSelectionPanel) satm.getActiveComponent()).onCheckingButtonPressed();
        assertTrue("withdraw panel visible", satm.getActiveComponent() instanceof WithdrawPanel);
        assertTrue("withdraw instruction displayed", MessageStrings.WITHDRAW_WELCOME.equals(satm.
                getActiveComponent().getDisplayText()));

        //enter invalid withdraw amt
        Double balanceBeforeWithdraw=testCustomer.getCheckingAccount().getBalance();
        satm.getActiveComponent().setDialerPanelInputText("555");
        ((WithdrawPanel) satm.getActiveComponent()).onProceedButtonPressed();

        //cannot dispense screen displayed
        assertTrue("cannot dispense message displayed", MessageStrings.WITHDRAW_CANNOT_DISPENSE.equals(satm.
                getActiveComponent().getDisplayText()));
        assertTrue("verify no amt is dispensed",((WithdrawPanel) satm.getActiveComponent()).getCashDispensed().isEmpty());
        assertEquals("no change in customer balance",balanceBeforeWithdraw,
                testCustomer.getCheckingAccount().getBalance());


    }


    private void insertValidCard(){
       if(! (satm.getActiveComponent() instanceof MainPanel) ||
               accounts.keySet().toArray().length==0)
           return;

        MainPanel mainPanel=(MainPanel) satm.getActiveComponent();
        mainPanel.setDialerPanelInputText(testCustomer.getAccountNumber());
        mainPanel.onProceedButtonPressed();
    }

    private void enterCorrectPin(){
        if(! (satm.getActiveComponent() instanceof AuthenticationPanel))
            return;

        AuthenticationPanel currPanel=(AuthenticationPanel) satm.getActiveComponent();
        currPanel.setDialerPanelInputText(testCustomer.getPIN());
        currPanel.onProceedButtonPressed();
    }

    private void enterIncorrectPin(){
        if(! (satm.getActiveComponent() instanceof AuthenticationPanel))
            return;

        AuthenticationPanel currPanel=(AuthenticationPanel) satm.getActiveComponent();
        currPanel.setDialerPanelInputText(testCustomer.getPIN()+"1");
        currPanel.onProceedButtonPressed();
    }

    @BeforeClass
    public static void oneTimeSetup(){
        try {
            //use reflection to update account data file to test file
            Field fileNameField= Accounts.class.getDeclaredField("accountDataFile");
            fileNameField.setAccessible(true);
            fileNameField.set(null, accountTestDataFile);

            //maintain reference to accounts map
            Field accountDataField= Accounts.class.getDeclaredField("accounts");
            accountDataField.setAccessible(true);
            accounts=(HashMap<String, BankCustomer>)accountDataField.get(null);
            ViewController.startup();

            //replace terminal unit controller instance with mock
            Field terminalController= TerminalUnitController.class.getDeclaredField("INSTANCE");
            terminalController.setAccessible(true);
            terminalController.set(null,mockTerminalUnitController);


        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void oneTimeTearDown(){

    }
}