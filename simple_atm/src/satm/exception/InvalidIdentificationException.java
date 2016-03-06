package satm.exception;

public class InvalidIdentificationException extends Exception{
    public InvalidIdentificationException(){
        super("Invalid Identification. Your card will be retained. Please call the bank");
    }
}
