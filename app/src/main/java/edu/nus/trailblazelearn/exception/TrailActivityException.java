package edu.nus.trailblazelearn.exception;

/**
 * Created by RMukherjee.
 */
public class TrailActivityException extends TrailException {

     private static final long serialVersionUID = 1L;

     public TrailActivityException() {
     }

     public TrailActivityException(String msg) {
        super (msg);
     }

     public TrailActivityException(String message, Throwable throwable) {
        super(message, throwable);
     }

}
