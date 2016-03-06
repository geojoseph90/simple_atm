package satm.exception;

import java.awt.event.ActionEvent;

public class ExceptionEvent {
    private Exception exception;
    private boolean recoverable;
    private boolean duringTransaction;

    public ExceptionEvent(Exception exception,boolean recoverable){
        this.exception=exception;
        this.recoverable=recoverable;
        this.duringTransaction=false;
    }

    public ExceptionEvent(Exception exception, boolean recoverable, boolean duringTransaction) {
        this.exception = exception;
        this.recoverable = recoverable;
        this.duringTransaction = duringTransaction;
    }

    public boolean isDuringTransaction() {
        return duringTransaction;
    }

    public Exception getException() {
        return exception;
    }

    public boolean isRecoverable() {
        return recoverable;
    }
}
