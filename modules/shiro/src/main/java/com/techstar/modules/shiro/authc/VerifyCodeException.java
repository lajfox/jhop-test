package com.techstar.modules.shiro.authc;

import org.apache.shiro.authc.AuthenticationException;

public class VerifyCodeException extends AuthenticationException {

	private static final long serialVersionUID = 574242461529473941L;

    /**
     * Creates a new VerifyCodeException.
     */
    public VerifyCodeException() {
        super();
    }

    /**
     * Constructs a new VerifyCodeException.
     *
     * @param message the reason for the exception
     */
    public VerifyCodeException(String message) {
        super(message);
    }

    /**
     * Constructs a new VerifyCodeException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public VerifyCodeException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new VerifyCodeException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
	public VerifyCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
