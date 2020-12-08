package com.ford.fc.glo.autotest.integration;

/**
 * Enumeration to represent the browser to be used for execution
 */
public enum Browser {
	CHROME("chrome"), FIREFOX("firefox"),
	INTERNET_EXPLORER("internet explorer"),
	SAFARI("safari"),
	PERFECTO_MOBILE("perfectoMobile"),
	PERFECTO_MOBILE_OS("mobileOS"),
	PERFECTO_MOBILE_DEFAULT("mobileDefault"),
	PERFECTO_MOBILE_CHROME("mobileChrome"),
	PERFECTO_MOBILE_SAFARI("mobileSafari");

	private String value;

	Browser(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}