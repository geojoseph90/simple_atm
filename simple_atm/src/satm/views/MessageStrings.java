package satm.views;

public class MessageStrings {
    final public static String MAINPANEL_WELCOME_MESSAGE="Welcome to Simple ATM \nEnter your card number using keypad \nThen click on Proceed button on right";
    final public static String MAINPANEL_INVALID_ACCOUNT="Not a valid account number. Try again";
    final public static String AUTHPANEL_ENTER_PIN="Enter your Personal Identification Number \n \n Press Proceed to proceed to transaction " +
            "\n Press Cancel to go back";
    final public static String AUTHPANEL_INVALID_PIN="Invalid PIN. Try again";
    final public static String ACCTSTPANEL_SELECT_ACCTTYPE="Select account type from right " +
            "\n\n Press Cancel to go back to main menu";
    final public static String BALANCEPANEL_BALANCE_MESSAGE="Your balance is printed on your receipt.\n Another transaction?";
    final public static String DEPOSIT_WELCOME="Enter the deposit amount/envelope in deposit slot. Only increments of $10 permitted. " +
            "Your balance will be updated on clicking proceed \n\n Press Cancel to go back to main menu";
    final public static String DEPOSIT_CANNOT_DEPOSIT_AMT="Machine cannot deposit that amount. Please enter a new amount";
    final public static String DEPOSIT_INVALID_AMT="Not a valid amount. \n Please Try again";
    final public static String TRANSACTION_WELCOME="Select transaction type from right " +
            "\n\n Press Cancel to go back";
    final public static String WITHDRAW_WELCOME="Enter the amount and click proceed \n\n Withdrawals must be in order of $10"+
            "\n Press Cancel to go back to main menu";
    final public static String WITHDRAW_CANNOT_DISPENSE= "Machine cannot dispense that amount. Please enter a new amount";
    final public static String WITHDRAW_INSUFFICIENT_FUND="Insufficient funds. Please enter a new amount";
    final public static String WITHDRAW_INVALID_AMT="Not a valid amount. \n Please Try again";
    final public static String WITHDRAW_SUCCESSFUL="Your balance is being updated.Please take the cash from dispenser " +
            "\n\n Another Transaction";

    final public static String ERROR_ANOTHER_TRANSACTION="\n\nAnother transaction?";
    final public static String ERROR_START_OVER="\n\n Press Start Over button on right to start a new session";
    final public static String ERROR_FATAL_ERROR="\n\n Fatal error Contact bank.";

}
