package edu.mit.lab.exception;

/**
 * <p>Title: MIT Lib Project</p>
 * <p>Description: edu.mit.lab.exception.TypeException</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Lib Co., Ltd</p>
 *
 * @author <chao.deng@mit.edu>
 * @version 1.0
 * @since 12/5/2016
 */
public class TypeException extends Exception {

    public TypeException() {
        super("Type unsupported exception!");
    }

    public TypeException(String message) {
        super(String.format("Type unsupported exception! Database product name: %s cannot be instantiated!", message));
    }

    public TypeException(String message, Throwable cause) {
        super(
            String.format("Type unsupported exception! Database product name: %s cannot be instantiated!", message),
            cause);
    }

    public TypeException(Throwable cause) {
        super(cause);
    }

    public TypeException(
        String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(
            String.format("Type unsupported exception! Database product name: %s cannot be instantiated!", message),
            cause, enableSuppression, writableStackTrace);
    }
}
