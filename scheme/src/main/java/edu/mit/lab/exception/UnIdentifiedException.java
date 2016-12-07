package edu.mit.lab.exception;

/**
 * <p>Title: MIT Lib Project</p>
 * <p>Description: edu.mit.lab.exception.UnIdentifiedException</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Lib Co., Ltd</p>
 *
 * @author <chao.deng@mit.edu>
 * @version 1.0
 * @since 12/5/2016
 */
public class UnIdentifiedException extends Exception {

    public UnIdentifiedException() {
        super("Type unsupported exception!");
    }

    public UnIdentifiedException(String message) {
        super(String
            .format("System can't support this database! Database product name: %s cannot be instantiated!", message));
    }

    public UnIdentifiedException(String message, Throwable cause) {
        super(
            String.format("System can't support this database! Database product name: %s cannot be instantiated!",
                message),
            cause);
    }

    public UnIdentifiedException(Throwable cause) {
        super(cause);
    }

    public UnIdentifiedException(
        String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(
            String.format("System can't support this database! Database product name: %s cannot be instantiated!",
                message),
            cause, enableSuppression, writableStackTrace);
    }
}
