package edu.mit.lab.exception;

/**
 * <p>Title: MIT Lab Project</p>
 * <p>Description: edu.mit.lab.exception.NodeException</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Kewill Co., Ltd.</p>
 *
 * @author <chao.deng@mit.lab>
 * @version 1.0
 * @since 11/16/2016
 */
public class NodeException extends RuntimeException {
    /**
     * Constructs a new tree node exception with the specified detail message
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method
     */
    public NodeException(String message) {
        super(message);
    }

    /**
     * Constructs a new tree node exception with the specified detail message and cause
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method
     * @param  cause the cause (which is saved for later retrieval by the
     *               {@link #getCause()} method). A {@code null} value is
     *               permitted, and indicates that the cause is nonexistent
     *               or unknown
     */
    public NodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
