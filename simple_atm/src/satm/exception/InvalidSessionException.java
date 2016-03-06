package satm.exception;

public class InvalidSessionException extends Exception{
    public InvalidSessionException(){
        super("The session has timed out. \n Click on start button to go back to the start screen ");
    }
}
