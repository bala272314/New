package com.ford.fc.glo.autotest.integration;

import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Enumeration to represent the mode of execution
 * 
 */
public enum ExecutionMode {
	/**
	 * Execute on the local machine
	 */
	LOCAL,

	/**
	 * Execute on a remote machine
	 */
	REMOTE,

	/**
	 * Execute on a selenium grid
	 */
	GRID,

	/**
	 * Execute on a Perfecto MobileCloud device using {@link RemoteWebDriver}
	 */
	PERFECTO,

}