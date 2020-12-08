package com.ford.fc.glo.autotest.integration;

import org.openqa.selenium.Platform;

/**
 * Class to encapsulate various input parameters required for each test script
 */
public class SeleniumTestParameters extends TestParameters {
	private ExecutionMode executionMode;
	private Browser browser;
	private String strBrowserVersion;
	private Platform platform;
	private String strDeviceName;
	private String strUser;
	private String strPassword;
	private String strPerfectoDeviceId;
	private boolean blnInstallApplication;
	private MobileExecutionPlatform mobileExecutionPlatform;
	private MobileToolName mobileToolName;
	private String strMobileOsVersion;

	public SeleniumTestParameters(String strCurrentScenario, String strCurrentTestcase) {
		super(strCurrentScenario, strCurrentTestcase);
		blnInstallApplication = false;
	}

	/**
	 * Function to get the {@link ExecutionMode} for the test being executed
	 * 
	 * @return The {@link ExecutionMode} for the test being executed
	 */
	public ExecutionMode getExecutionMode() {
		return executionMode;
	}

	/**
	 * Function to set the {@link ExecutionMode} for the test being executed
	 * 
	 * @param executionMode
	 *            The {@link ExecutionMode} for the test being executed
	 */
	public void setExecutionMode(ExecutionMode executionMode) {
		this.executionMode = executionMode;
	}

	/**
	 * Function to get the {@link MobileExecutionPlatform} for the test being
	 * executed
	 * 
	 * @return The {@link MobileExecutionPlatform} for the test being executed
	 */
	public MobileExecutionPlatform getMobileExecutionPlatform() {
		return mobileExecutionPlatform;
	}

	/**
	 * Function to set the {@link MobileExecutionPlatform} for the test being
	 * executed
	 * 
	 * @param executionPlatform
	 *            The {@link MobileExecutionPlatform} for the test being
	 *            executed
	 */
	public void setMobileExecutionPlatform(MobileExecutionPlatform mobileExecutionPlatform) {
		this.mobileExecutionPlatform = mobileExecutionPlatform;
	}

	/**
	 * Function to get the {@link MobileToolName} for the test being executed
	 * 
	 * @return The {@link MobileToolName} for the test being executed
	 */
	public MobileToolName getMobileToolName() {
		return mobileToolName;
	}

	/**
	 * Function to set the {@link MobileToolName} for the test being executed
	 * 
	 * @param toolName
	 *            The {@link MobileToolName} for the test being executed
	 */
	public void setMobileToolName(MobileToolName mobileToolName) {
		this.mobileToolName = mobileToolName;
	}

	/**
	 * Function to get a Boolean value indicating whether to install application
	 * in Device
	 * 
	 * @return Boolean value indicating whether to install Application in device
	 */
	public boolean shouldInstallApplication() {
		return blnInstallApplication;
	}

	/**
	 * Function to set a Boolean value indicating whether to install application
	 * in Device
	 * 
	 * @param generateExcelReports
	 *            Boolean value indicating whether to install application in
	 *            Device
	 */
	public void setInstallApplication(boolean blnInstallApplication) {
		this.blnInstallApplication = blnInstallApplication;
	}

	/**
	 * Function to get the {@link Browser} on which the test is to be executed
	 * 
	 * @return The {@link Browser} on which the test is to be executed
	 */
	public Browser getBrowser() {
		return browser;
	}

	/**
	 * Function to set the {@link Browser} on which the test is to be executed
	 * 
	 * @param browser
	 *            The {@link Browser} on which the test is to be executed
	 */
	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

	/**
	 * Function to get the OS Version of device on which the test is to be
	 * executed
	 * 
	 * @return The OS Version of device Version on which the test is to be
	 *         executed
	 */
	public String getMobileOSVersion() {
		return strMobileOsVersion;
	}

	/**
	 * Function to set the OS Version of device Version on which the test is to
	 * be executed
	 * 
	 * @param version
	 *            The OS Version of device Version on which the test is to be
	 *            executed
	 */
	public void setmobileOSVersion(String strMobileOsVersion) {
		this.strMobileOsVersion = strMobileOsVersion;
	}

	/**
	 * Function to get the Browser Version on which the test is to be executed
	 * 
	 * @return The Browser Version on which the test is to be executed
	 */
	public String getBrowserVersion() {
		return strBrowserVersion;
	}

	/**
	 * Function to set the Browser Version on which the test is to be executed
	 * 
	 * @param version
	 *            The Browser Version on which the test is to be executed
	 */
	public void setBrowserVersion(String strVersion) {
		this.strBrowserVersion = strVersion;
	}

	/**
	 * Function to get the {@link Platform} on which the test is to be executed
	 * 
	 * @return The {@link Platform} on which the test is to be executed
	 */
	public Platform getPlatform() {
		return platform;
	}

	/**
	 * Function to set the {@link Platform} on which the test is to be executed
	 * 
	 * @param platform
	 *            The {@link Platform} on which the test is to be executed
	 */
	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	/**
	 * Function to get the browser and platform on which the test is to be
	 * executed
	 * 
	 * @return The browser and platform on which the test is to be executed
	 */
	public String getBrowserAndPlatform() {
		if (this.browser == null) {
			throw new FrameworkException("The browser has not been initialized!");
		}

		String strBrowserAndPlatform = this.browser.toString();
		if (this.strBrowserVersion != null) {
			strBrowserAndPlatform += " " + strBrowserVersion;
		}
		if (this.platform != null) {
			strBrowserAndPlatform += " on " + this.platform;
		}

		return strBrowserAndPlatform;
	}

	/**
	 * Function to get the name of the mobile device on which the test is to be
	 * executed
	 * 
	 * @return The name of the mobile device on which the test is to be executed
	 */
	public String getDeviceName() {
		return strDeviceName;
	}
	
	//Function to get the name of the username on which the test is to be executed
		public String getUser() {
			return strUser;
		}
		
		//Function to set the name of the username on which the test is to be executed
		public void setUser(String strUser) {
			this.strUser = strUser;
		}
		
		//Function to get the password on which the test is to be executed
		public String getPassword() {
			return strPassword;
		}
		
		//Function to set the password on which the test is to be executed
		public void setPassword(String strPassword) {
			this.strPassword = strPassword;
		}

	/**
	 * Function to set the name of the mobile device on which the test is to be
	 * executed<br>
	 * <br>
	 * If the ExecutionMode is PERFECTO_REMOTEWEBDRIVER, this function also sets
	 * the device's Perfecto MobileCloud ID, by accessing the Perfecto Device
	 * List within the Global Settings.properties file
	 * 
	 * @param deviceName
	 *            The name of the mobile device on which the test is to be
	 *            executed
	 */
	public void setDeviceName(String strDeviceName) {
		this.strDeviceName = strDeviceName;

		if (ExecutionMode.PERFECTO.equals(this.executionMode)) {
			// Properties properties = Settings.getInstance();
			this.strPerfectoDeviceId = this.strDeviceName;
		}
	}

	/**
	 * Function to get the ID of the Perfecto MobileCloud device on which the
	 * test is to be executed
	 * 
	 * @return The ID of the Perfecto MobileCloud device on which the test is to
	 *         be executed
	 */
	public String getPerfectoDeviceId() {
		return strPerfectoDeviceId;
	}

	/**
	 * Function to set the ID of the Perfecto MobileCloud device on which the
	 * test is to be executed
	 * 
	 * @param perfectoDeviceId
	 *            The ID of the Perfecto MobileCloud device on which the test is
	 *            to be executed
	 */
	public void setPerfectoDeviceId(String strPerfectoDeviceId) {
		this.strPerfectoDeviceId = strPerfectoDeviceId;
	}

	@Override
	public String getAdditionalDetails() {
		String strAdditionalDetails = super.getAdditionalDetails();

		if ("".equals(strAdditionalDetails)) {
			switch (this.executionMode) {

			case PERFECTO:
				strAdditionalDetails = this.getMobileDeviceDetails();
				break;

			default:
				strAdditionalDetails = this.getBrowserAndPlatform();
			}
		}

		return strAdditionalDetails;
	}

	/*
	 * private String getPerfectoDeviceDetails() { if (this.perfectoDeviceId ==
	 * null) { throw new FrameworkException(
	 * "The Perfecto Device ID has not been initialized!"); }
	 * 
	 * if (this.deviceName == null) { return getBrowserAndPlatform(); } else {
	 * return this.deviceName + " on Perfecto MobileCloud"; } }
	 */

	private String getEmulatedDeviceDetails() {
		return this.strDeviceName + " emulated on " + this.getBrowserAndPlatform();
	}

	private String getMobileDeviceDetails() {
		if (this.strDeviceName == null) {
			throw new FrameworkException("The Mobile Device ID has not been Set in Run Manager!");
		}
		return this.strDeviceName + "-" + this.mobileToolName;
	}

}

/*
 * private String getPerfectoNativeOrWeb() { if
 * (this.mobileExecutionPlatform.toString().contains("WEB")) { return
 * this.getPerfectoDeviceDetails(); } else { return "Executed in " +
 * this.executionMode + " on " + this.deviceName; }
 * 
 * }
 */
