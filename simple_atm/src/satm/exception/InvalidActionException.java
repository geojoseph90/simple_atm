package satm.exception;


public class InvalidActionException extends Exception{
    public InvalidActionException(){
        super("System encountered an error. Activity not permitted at this time");
    }
}
