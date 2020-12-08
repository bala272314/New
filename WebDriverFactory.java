package com.ford.fc.glo.autotest.integration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

//import org.openqa.selenium.opera.OperaDriver;
//import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * Factory class for creating the {@link WebDriver} object as required
 */
public class WebDriverFactory {
	private static Properties properties;

	private WebDriverFactory() {
		// To prevent external instantiation of this class
	}

	/**
	 * Function to return the appropriate {@link WebDriver} object based on the
	 * parameters passed
	 * 
	 * @param browser
	 *            The {@link Browser} to be used for the test execution
	 * @return The corresponding {@link WebDriver} object
	 */
	public static WebDriver getWebDriver(Browser browser) {
		WebDriver driver = null;
		properties = Settings.getInstance();
		boolean proxyRequired = Boolean.parseBoolean(properties.getProperty("ProxyRequired"));

		switch (browser) {
		case CHROME:
			// Takes the system proxy settings automatically

			System.setProperty("webdriver.chrome.driver", properties.getProperty("ChromeDriverPath"));
			driver = new ChromeDriver();
			break;

		case FIREFOX:
			// Takes the system proxy settings automatically
			System.setProperty("webdriver.gecko.driver", properties.getProperty("GeckoDriverPath"));
			driver = new FirefoxDriver();
			break;

		case INTERNET_EXPLORER:
			// Takes the system proxy settings automatically

			System.setProperty("webdriver.ie.driver", properties.getProperty("InternetExplorerDriverPath"));
			driver = new InternetExplorerDriver();
			break;

		case SAFARI:
			// Takes the system proxy settings automatically

			driver = new SafariDriver();
			break;

		default:
			throw new FrameworkException("Unhandled browser!");
		}

		return driver;
	}

	private static DesiredCapabilities getProxyCapabilities() {
		properties = Settings.getInstance();
		String strProxyUrl = properties.getProperty("ProxyHost") + ":" + properties.getProperty("ProxyPort");

		Proxy proxy = new Proxy();
		proxy.setProxyType(ProxyType.MANUAL);
		proxy.setHttpProxy(strProxyUrl);
		proxy.setFtpProxy(strProxyUrl);
		proxy.setSslProxy(strProxyUrl);

		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability(CapabilityType.PROXY, proxy);

		return desiredCapabilities;
	}

	/**
	 * Function to return the {@link RemoteWebDriver} object based on the
	 * parameters passed
	 * 
	 * @param browser
	 *            The {@link Browser} to be used for the test execution
	 * @param browserVersion
	 *            The browser version to be used for the test execution
	 * @param platform
	 *            The {@link Platform} to be used for the test execution
	 * @param remoteUrl
	 *            The URL of the remote machine to be used for the test
	 *            execution
	 * @return The corresponding {@link RemoteWebDriver} object
	 */
	public static WebDriver getRemoteWebDriver(Browser browser, String strBrowserVersion, Platform platform,
			String strRemoteUrl) {
		// For running RemoteWebDriver tests in Chrome and IE:
		// The ChromeDriver and IEDriver executables needs to be in the PATH of
		// the remote machine
		// To set the executable path manually, use:
		// java -Dwebdriver.chrome.driver=/path/to/driver -jar
		// selenium-server-standalone.jar
		// java -Dwebdriver.ie.driver=/path/to/driver -jar
		// selenium-server-standalone.jar

		properties = Settings.getInstance();
		boolean proxyRequired = Boolean.parseBoolean(properties.getProperty("ProxyRequired"));

		DesiredCapabilities desiredCapabilities = null ;
		
		desiredCapabilities.setBrowserName(browser.getValue());

		if (strBrowserVersion != null) {
			desiredCapabilities.setVersion(strBrowserVersion);
		}
		if (platform != null) {
			desiredCapabilities.setPlatform(platform);
		}

		desiredCapabilities.setJavascriptEnabled(true); // Pre-requisite for
														// remote execution

		URL url = getUrl(strRemoteUrl);

		return new RemoteWebDriver(url, desiredCapabilities);
	}

	private static URL getUrl(String strRemoteUrl) {
		URL url;
		try {
			url = new URL(strRemoteUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new FrameworkException("The specified remote URL is malformed");
		}
		return url;
	}

	/**
	 * Function to return the {@link RemoteWebDriver} object based on the
	 * parameters passed
	 * 
	 * @param browser
	 *            The {@link Browser} to be used for the test execution
	 * @param remoteUrl
	 *            The URL of the remote machine to be used for the test
	 *            execution
	 * @return The corresponding {@link RemoteWebDriver} object
	 */
	public static WebDriver getRemoteWebDriver(Browser browser, String strRemoteUrl) {
		return getRemoteWebDriver(browser, null, null, strRemoteUrl);
	}

	/**
	 * Function to return the {@link ChromeDriver} object emulating the device
	 * specified by the user
	 * 
	 * @param deviceName
	 *            The name of the device to be emulated (check Chrome Dev Tools
	 *            for a list of available devices)
	 * @return The corresponding {@link ChromeDriver} object
	 */
	public static WebDriver getEmulatedWebDriver(String strDeviceName) {
		DesiredCapabilities desiredCapabilities = getEmulatedChromeDriverCapabilities(strDeviceName);

		properties = Settings.getInstance();
		System.setProperty("webdriver.chrome.driver", properties.getProperty("ChromeDriverPath"));

		return new ChromeDriver(desiredCapabilities);
	}

	private static DesiredCapabilities getEmulatedChromeDriverCapabilities(String strDeviceName) {
		Map<String, String> mobileEmulation = new HashMap<String, String>();
		mobileEmulation.put("deviceName", strDeviceName);

		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);

		DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
		desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

		return desiredCapabilities;
	}

	/**
	 * Function to return the {@link RemoteWebDriver} object emulating the
	 * device specified by the user
	 * 
	 * @param deviceName
	 *            The name of the device to be emulated (check Chrome Dev Tools
	 *            for a list of available devices)
	 * @param remoteUrl
	 *            The URL of the remote machine to be used for the test
	 *            execution
	 * @return The corresponding {@link RemoteWebDriver} object
	 */
	public static WebDriver getEmulatedRemoteWebDriver(String strDeviceName, String remoteUrl) {
		DesiredCapabilities desiredCapabilities = getEmulatedChromeDriverCapabilities(strDeviceName);
		desiredCapabilities.setJavascriptEnabled(true); // Pre-requisite for
														// remote execution

		URL url = getUrl(remoteUrl);

		return new RemoteWebDriver(url, desiredCapabilities);
	}

	/**
	 * Function to return the {@link ChromeDriver} object emulating the device
	 * attributes specified by the user
	 * 
	 * @param deviceWidth
	 *            The width of the device to be emulated (in pixels)
	 * @param deviceHeight
	 *            The height of the device to be emulated (in pixels)
	 * @param devicePixelRatio
	 *            The device's pixel ratio
	 * @param userAgent
	 *            The user agent string
	 * @return The corresponding {@link ChromeDriver} object
	 */
	public static WebDriver getEmulatedWebDriver(int intDeviceWidth, int intDeviceHeight, float floatDevicePixelRatio,
			String strUserAgent) {
		DesiredCapabilities desiredCapabilities = getEmulatedChromeDriverCapabilities(intDeviceWidth, intDeviceHeight,
				floatDevicePixelRatio, strUserAgent);

		properties = Settings.getInstance();
		System.setProperty("webdriver.chrome.driver", properties.getProperty("ChromeDriverPath"));

		return new ChromeDriver(desiredCapabilities);
	}

	private static DesiredCapabilities getEmulatedChromeDriverCapabilities(int intDeviceWidth, int intDeviceHeight,
			float floatDevicePixelRatio, String strUserAgent) {
		Map<String, Object> deviceMetrics = new HashMap<String, Object>();
		deviceMetrics.put("width", intDeviceWidth);
		deviceMetrics.put("height", intDeviceHeight);
		deviceMetrics.put("pixelRatio", floatDevicePixelRatio);

		Map<String, Object> mobileEmulation = new HashMap<String, Object>();
		mobileEmulation.put("deviceMetrics", deviceMetrics);
		// mobileEmulation.put("userAgent", "Mozilla/5.0 (Linux; Android 4.2.1;
		// en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko)
		// Chrome/18.0.1025.166 Mobile Safari/535.19");
		mobileEmulation.put("userAgent", strUserAgent);

		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);

		DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
		desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		return desiredCapabilities;
	}

	/**
	 * Function to return the {@link RemoteWebDriver} object emulating the
	 * device attributes specified by the user
	 * 
	 * @param deviceWidth
	 *            The width of the device to be emulated (in pixels)
	 * @param deviceHeight
	 *            The height of the device to be emulated (in pixels)
	 * @param devicePixelRatio
	 *            The device's pixel ratio
	 * @param userAgent
	 *            The user agent string
	 * @param remoteUrl
	 *            The URL of the remote machine to be used for the test
	 *            execution
	 * @return The corresponding {@link RemoteWebDriver} object
	 */
	public static WebDriver getEmulatedRemoteWebDriver(int intDeviceWidth, int intDeviceHeight, float floatDevicePixelRatio,
			String strUserAgent, String remoteUrl) {
		DesiredCapabilities desiredCapabilities = getEmulatedChromeDriverCapabilities(intDeviceWidth, intDeviceHeight,
				floatDevicePixelRatio, strUserAgent);
		desiredCapabilities.setJavascriptEnabled(true); // Pre-requisite for
														// remote execution

		URL url = getUrl(remoteUrl);

		return new RemoteWebDriver(url, desiredCapabilities);
	}

}