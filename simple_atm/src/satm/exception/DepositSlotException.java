package satm.exception;


public class DepositSlotException extends Exception{
    public DepositSlotException(){
        super("Temporarily unable to process deposits.");
    }
}
