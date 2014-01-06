/**
 * 
 */
package com.ldapui.service;

/**
 * An exception in a service layer class that can be used as a general wrapper for other more
 * detailed exceptions that you don't want to catch in classes that are using the service layer. A
 * service layer class is one that is annotated with the <code>@Service</code> annotation and
 * probably has a name that ends in <code>Service</code>.
 * 
 * @author George
 * 
 */
public class ServiceException extends Exception {

	/**
	 * Version 1.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * An exception in a service layer class.
	 * 
	 * @param message
	 *            A description of the problem.
	 * @param cause
	 *            The underlying exception.
	 */
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
