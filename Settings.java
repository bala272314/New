package com.ford.fc.glo.autotest.integration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Singleton class that encapsulates the user settings specified in the
 * properties file of the framework
 */
public class Settings {
	private static Properties properties = loadFromPropertiesFile();
	private static Properties mobileProperties = loadFromPropertiesFileForMobile();

	private Settings() {
		// To prevent external instantiation of this class
	}

	/**
	 * Function to return the singleton instance of the {@link Properties}
	 * object
	 * 
	 * @return Instance of the {@link Properties} object
	 */
	public static Properties getInstance() {
		return properties;
	}

	public static Properties getMobilePropertiesInstance() {
		return mobileProperties;
	}

	private static Properties loadFromPropertiesFile() {
		FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();

		if (frameworkParameters.getRelativePath() == null) {
			throw new FrameworkException("FrameworkParameters.relativePath is not set!");
		}

		Properties properties = new Properties();

		try {
			properties.load(new FileInputStream(
					frameworkParameters.getRelativePath() + Util.getFileSeparator() + "src\\com\\ford\\fc\\glo\\autotest\\settings\\Global Settings.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("FileNotFoundException while loading the Global Settings file");
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("IOException while loading the Global Settings file");
		}

		return properties;
	}

	private static Properties loadFromPropertiesFileForMobile() {
		FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();

		if (frameworkParameters.getRelativePath() == null) {
			throw new FrameworkException("FrameworkParameters.relativePath is not set!");
		}

		Properties properties = new Properties();

		try {
			properties.load(new FileInputStream(frameworkParameters.getRelativePath() + Util.getFileSeparator()
					+ "src\\com\\ford\\fc\\glo\\autotest\\settings\\Mobile Automation Settings.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("FileNotFoundException while loading the Mobile Automation Settings file");
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("IOException while loading the Mobile Automation Settings file");
		}

		return properties;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}