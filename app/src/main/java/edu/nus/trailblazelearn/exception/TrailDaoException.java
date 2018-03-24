package edu.nus.trailblazelearn.exception;



public class TrailDaoException extends TrailException {

    private static final long serialVersionUID = 1L;

    public TrailDaoException () {
    }

    public TrailDaoException (String msg) {
        super (msg);
    }

    public TrailDaoException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
