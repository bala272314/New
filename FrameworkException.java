package com.ford.fc.glo.autotest.integration;

/**
 * Exception class for the framework
 */
@SuppressWarnings("serial")
public class FrameworkException extends RuntimeException {
	/**
	 * The step name to be specified for the exception
	 */
	public String strErrorName = "Error";

	/**
	 * Constructor to initialize the exception from the framework
	 * 
	 * @param errorDescription
	 *            The Exception message to be thrown
	 */
	public FrameworkException(String strErrorDescription) {
		super(strErrorDescription);
	}

	/**
	 * Constructor to initialize the exception from the framework
	 * 
	 * @param errorName
	 *            The step name for the error
	 * @param errorDescription
	 *            The Exception message to be thrown
	 */
	public FrameworkException(String strErrorName, String strErrorDescription) {
		super(strErrorDescription);
		this.strErrorName = strErrorName;
	}

	public String getErrorName() {
		return strErrorName;
	}
}