package satm.exception;

public class InvalidAccountException extends Exception{
    public InvalidAccountException(){
        super("Invalid Identification. You card will be retained. Please call the bank");
    }
}
