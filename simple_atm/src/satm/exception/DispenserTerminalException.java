package satm.exception;


public class DispenserTerminalException extends Exception{

    public DispenserTerminalException(){
        super("Temporarily unable to process withdrawals.");
    }
}
