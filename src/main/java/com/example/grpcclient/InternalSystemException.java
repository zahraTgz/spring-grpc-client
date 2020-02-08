package com.example.grpcclient;

/**
 * @author z.Taghizadeh
 */
public class InternalSystemException
        extends Error {

    private static final long serialVersionUID = 7718828512143293558L;

    public InternalSystemException() {
    }

    public InternalSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalSystemException(String message) {
        super(message);
    }

    public InternalSystemException(Throwable cause) {
        super(cause);
    }
}
