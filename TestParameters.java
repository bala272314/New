package com.ford.fc.glo.autotest.integration;

/**
 * Class to encapsulate various input parameters required for each test script
 */
public class TestParameters {
	private final String strCurrentScenario;
	private final String strCurrentTestcase;
	private String strCurrentTestInstance;
	private String strCurrentTestDescription;
	private String strAdditionalDetails;
	private IterationOptions iterationMode;
	private int intStartIteration;
	private int intEndIteration;

	/**
	 * Constructor to initialize the {@link TestParameters} object
	 * 
	 * @param currentScenario
	 *            The current test scenario/module
	 * @param currentTestcase
	 *            The current test case
	 */
	public TestParameters(String strCurrentScenario, String strCurrentTestcase) {
		this.strCurrentScenario = strCurrentScenario;
		this.strCurrentTestcase = strCurrentTestcase;

		// Set default values for all test parameters
		this.strCurrentTestInstance = "Instance1";
		this.strCurrentTestDescription = "";
		this.strAdditionalDetails = "";
		this.iterationMode = IterationOptions.RUN_ALL_ITERATIONS;
		this.intStartIteration = 1;
		this.intEndIteration = 1;
	}

	/**
	 * Function to get the current test scenario/module
	 * 
	 * @return The current test scenario/module
	 */
	public String getCurrentScenario() {
		return strCurrentScenario;
	}

	/**
	 * Function to get the current test case
	 * 
	 * @return The current test case
	 */
	public String getCurrentTestcase() {
		return strCurrentTestcase;
	}

	/**
	 * Function to get the current test instance
	 * 
	 * @return The current test instance
	 */
	public String getCurrentTestInstance() {
		return strCurrentTestInstance;
	}

	/**
	 * Function to set the current test instance
	 * 
	 * @param currentTestInstance
	 *            The current test instance
	 */
	public void setCurrentTestInstance(String strCurrentTestInstance) {
		this.strCurrentTestInstance = strCurrentTestInstance;
	}

	/**
	 * Function to get the description of the current test case
	 * 
	 * @return The description of the current test case
	 */
	public String getCurrentTestDescription() {
		return strCurrentTestDescription;
	}

	/**
	 * Function to set the description of the current test case
	 * 
	 * @param currentTestDescription
	 *            The description of the current test case
	 */
	public void setCurrentTestDescription(String strCurrentTestDescription) {
		this.strCurrentTestDescription = strCurrentTestDescription;
	}

	/**
	 * Function to get additional details about the current test case/test
	 * instance
	 * 
	 * @return Additional details about the current test case/test instance
	 */
	public String getAdditionalDetails() {
		return strAdditionalDetails;
	}

	/**
	 * Function to set additional details about the current test case/test
	 * instance
	 * 
	 * @param additionalDetails
	 *            Additional details about the current test case/test instance
	 */
	public void setAdditionalDetails(String strAdditionalDetails) {
		this.strAdditionalDetails = strAdditionalDetails;
	}

	/**
	 * Function to get the iteration mode
	 * 
	 * @return The iteration mode
	 * @see IterationOptions
	 */
	public IterationOptions getIterationMode() {
		return iterationMode;
	}

	/**
	 * Function to set the iteration mode
	 * 
	 * @param iterationMode
	 *            The iteration mode
	 * @see IterationOptions
	 */
	public void setIterationMode(IterationOptions iterationMode) {
		this.iterationMode = iterationMode;
	}

	/**
	 * Function to get the start iteration
	 * 
	 * @return The start iteration
	 */
	public int getStartIteration() {
		return intStartIteration;
	}

	/**
	 * Function to set the start iteration
	 * 
	 * @param startIteration
	 *            The start iteration (defaults to 1 if the input is less than
	 *            or equal to 0)
	 */
	public void setStartIteration(int intStartIteration) {
		if (intStartIteration > 0) {
			this.intStartIteration = intStartIteration;
		}
	}

	/**
	 * Function to get the end iteration
	 * 
	 * @return The end iteration
	 */
	public int getEndIteration() {
		return intEndIteration;
	}

	/**
	 * Function to set the end iteration
	 * 
	 * @param endIteration
	 *            The end iteration (defaults to 1 if the input is less than or
	 *            equal to 0)
	 */
	public void setEndIteration(int intEndIteration) {
		if (intEndIteration > 0) {
			this.intEndIteration = intEndIteration;
		}
	}
}