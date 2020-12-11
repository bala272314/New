package com.ford.fc.glo.autotest.integration;

import java.io.File;
import java.util.Properties;

/**
 * Singleton class which manages the creation of timestamped result folders for
 * every test batch execution
 */
public class TimeStamp {
	private static volatile String strReportPathWithTimeStamp;

	private TimeStamp() {
		// To prevent external instantiation of this class
	}

	/**
	 * Function to return the timestamped result folder path
	 * 
	 * @return The timestamped result folder path
	 */
	public static String getInstance() {
		if (strReportPathWithTimeStamp == null) {
			synchronized (TimeStamp.class) {
				if (strReportPathWithTimeStamp == null) { // Double-checked locking
					FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();

					if (frameworkParameters.getRelativePath() == null) {
						throw new FrameworkException("FrameworkParameters.relativePath is not set!");
					}
					if (frameworkParameters.getRunConfiguration() == null) {
						throw new FrameworkException("FrameworkParameters.runConfiguration is not set!");
					}

					Properties properties = Settings.getInstance();
					String strTimeStamp = "Run_" + Util.getCurrentFormattedTime(properties.getProperty("DateFormatString"))
							.replace(" ", "_").replace(":", "-");

					strReportPathWithTimeStamp = frameworkParameters.getRelativePath() + Util.getFileSeparator()
							+ "Results" + Util.getFileSeparator() + frameworkParameters.getRunConfiguration()
							+ Util.getFileSeparator() + strTimeStamp;

					new File(strReportPathWithTimeStamp).mkdirs();
				}
			}
		}

		return strReportPathWithTimeStamp;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}