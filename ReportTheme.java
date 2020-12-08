package com.ford.fc.glo.autotest.integration;

/**
 * Class to represent the colour theme to be used for the reports
 */
public class ReportTheme {
	private String strHeadingBackColor, strHeadingForeColor;
	private String strSectionBackColor, strSectionForeColor;
	private String strContentBackColor, strContentForeColor;
	private String strSubHeadingBackColor, strSubHeadingForeColor;
	private String strSubSectionBackColor;

	/**
	 * Function to get the heading background color (also used as the
	 * sub-heading/sub-section text color)
	 * 
	 * @return The heading background color (also used as the
	 *         sub-heading/sub-section text color)
	 */
	public String getHeadingBackColor() {
		return strHeadingBackColor;
	}

	/**
	 * Function to set the heading background color (also used as the
	 * sub-heading/sub-section text color)
	 * 
	 * @param headingBackColor
	 *            The heading background color (also used as the
	 *            sub-heading/sub-section text color)
	 */
	public void setHeadingBackColor(String strHeadingBackColor) {
		this.strHeadingBackColor = strHeadingBackColor;
	}

	/**
	 * Function to get the heading text color (also used as the
	 * sub-heading/sub-section background color)
	 * 
	 * @return The heading text color (also used as the sub-heading/sub-section
	 *         background color)
	 */
	public String getHeadingForeColor() {
		return strHeadingForeColor;
	}

	/**
	 * Function to set the heading text color (also used as the
	 * sub-heading/sub-section background color)
	 * 
	 * @param headingForeColor
	 *            The heading text color (also used as the
	 *            sub-heading/sub-section background color)
	 */
	public void setHeadingForeColor(String strHeadingForeColor) {
		this.strHeadingForeColor = strHeadingForeColor;
	}

	/**
	 * Function to get the section background color
	 * 
	 * @return The section background color
	 */
	public String getSectionBackColor() {
		return strSectionBackColor;
	}

	/**
	 * Function to set the section background color
	 * 
	 * @param sectionBackColor
	 *            The section background color
	 */
	public void setSectionBackColor(String strSectionBackColor) {
		this.strSectionBackColor = strSectionBackColor;
	}

	/**
	 * Function to get the section text color
	 * 
	 * @return The section text color
	 */
	public String getSectionForeColor() {
		return strSectionForeColor;
	}

	/**
	 * Function to set the section text color
	 * 
	 * @param sectionForeColor
	 *            The section text color
	 */
	public void setSectionForeColor(String strSectionForeColor) {
		this.strSectionForeColor = strSectionForeColor;
	}

	/**
	 * Function to get the content background color
	 * 
	 * @return The content background color
	 */
	public String getContentBackColor() {
		return strContentBackColor;
	}

	/**
	 * Function to set the content background color
	 * 
	 * @param contentBackColor
	 *            The content background color
	 */
	public void setContentBackColor(String strContentBackColor) {
		this.strContentBackColor = strContentBackColor;
	}

	/**
	 * Function to get the content text color (also used as the overall
	 * background color)
	 * 
	 * @return The content text color (also used as the overall background
	 *         color)
	 */
	public String getContentForeColor() {
		return strContentForeColor;
	}

	/**
	 * Function to set the content text color (also used as the overall
	 * background color)
	 * 
	 * @param contentForeColor
	 *            The content text color (also used as the overall background
	 *            color)
	 */
	public void setContentForeColor(String strContentForeColor) {
		this.strContentForeColor = strContentForeColor;
	}

	public String getsubHeadingBackColor() {
		return strSubHeadingBackColor;
	}

	public void setSubHeadingBackColor(String strSubHeadingBackColor) {
		this.strSubHeadingBackColor = strSubHeadingBackColor;
	}

	public String getsubHeadingForeColor() {
		return strSubHeadingForeColor;
	}

	public void setSubHeadingForeColor(String strSubHeadingForeColor) {
		this.strSubHeadingForeColor = strSubHeadingForeColor;
	}

	public String getsubSectionBackColor() {
		return strSubSectionBackColor;
	}

	public void setSubSectionBackColor(String strSubSectionBackColor) {
		this.strSubSectionBackColor = strSubSectionBackColor;
	}
}