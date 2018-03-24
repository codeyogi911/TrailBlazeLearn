package edu.nus.trailblazelearn.exception;



public class TrailException extends Exception {

    private static final long serialVersionUID = 1L;

    public TrailException () {
    }

    public TrailException (String msg) {
        super (msg);
    }

    public TrailException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
