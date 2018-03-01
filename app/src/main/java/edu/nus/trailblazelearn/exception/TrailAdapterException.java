package edu.nus.trailblazelearn.exception;

/**
 * Created by RMukherjee.
 */

public class TrailAdapterException extends TrailException {

    private static final long serialVersionUID = 1L;

    public TrailAdapterException () {
    }

    public TrailAdapterException (String msg) {
        super (msg);
    }

    public TrailAdapterException(String message, Throwable throwable) {
        super(message, throwable);
    }




}
