package edu.nus.trailblazelearn.exception;

/**
 * Created by RMukherjee on 15-03-2018.
 */

public class TrailHelperException extends TrailException {

    private static final long serialVersionUID = 1L;

    public TrailHelperException () {
    }

    public TrailHelperException (String msg) {
        super (msg);
    }

    public TrailHelperException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
