package com.ford.fc.glo.autotest.functionalgroup;
import com.ford.fc.glo.autotest.integration.ReusableLibrary;

import com.ford.fc.glo.autotest.integration.ScriptHelper;
import com.ford.fc.glo.autotest.integration.Status;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
//import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/********************************************************
 * #Class Name : ReusableMethods 
 * #Author : Cognizant 
 * #Description : The repository of common methods that can be reused 
 * #Date of creation : 10-Sep-2018
 *********************************************************/
public class ReusableMethods extends ReusableLibrary {
	  private static final String UNICODE_FORMAT = "UTF8";
	    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
	    private KeySpec ks;
	    private SecretKeyFactory skf;
	    private Cipher cipher;
	    byte[] arrayBytes;
	    private String myEncryptionKey;
	    private String myEncryptionScheme;
	    SecretKey key;
	String strVin_temp = "";
	
	public ReusableMethods(ScriptHelper scriptHelper) throws Exception {
		super(scriptHelper);
		 myEncryptionKey = "ThisIsSpartaThisIsSparta";
	        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
	        arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
	        ks = new DESedeKeySpec(arrayBytes);
	        skf = SecretKeyFactory.getInstance(myEncryptionScheme);
	        cipher = Cipher.getInstance(myEncryptionScheme);
	        key = skf.generateSecret(ks);
	}

	WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 20);
	WebElement element = null;
	WebElement obj=null;

	/********************************************************
	 * #FunctionName : invokeApplication() 
	 * #Author : Cognizant 
	 * #Description : This method will launch the application under test 
	 * #Date of creation : 10-Sep-2018
	 *********************************************************/
	public void invokeApplication() throws InterruptedException {
		String strURL = dataTable.getData("General_Data", "URL");

		driver.get(strURL);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		try {

			report.updateTestLog("Invoke application", "Validate if the application is invoked successfully",
					Status.PASS);

		} catch (Exception e) {
			e.printStackTrace();
			report.updateTestLog("Invoke application", "Application is not invoked successfully", Status.FAIL);
		}
	}

	/********************************************************
	 * #FunctionName : closeApplication() 
	 * #Author : Cognizant 
	 * #Description : This method will close the application under test 
	 * #Date of creation : 10-Sep-2018
	 *********************************************************/
	public void closeApplication() throws Exception {
		try {
			//driver.close();
			driver.quit();
			report.updateTestLog("Close application", "Validate if the application is Closed successfully", Status.PASS);

		} catch (Exception e) {
			e.printStackTrace();
			report.updateTestLog("Close application", "Application is not Closed successfully", Status.FAIL);
		}

	}


	/*************************************************
	 * #FunctionName : enterValueInTextField() 
	 * #Author : Cognizant 
	 * #Description : This method will enter a object on the element 
	 * #Date of creation : 10-Sep-2018
	 **************************************************/
	public boolean enterValueInTextField(By object, String strTextToEnter, String strReportStep,
			String strReportDescription) throws InterruptedException {
		boolean blnReturn = false;
		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
			element.clear();
			if((strTextToEnter.trim())!=""){
				element.sendKeys(strTextToEnter);
			}
			blnReturn = true;
			if((strReportDescription.trim()).length()!=0){
				report.updateTestLog(strReportStep, strReportDescription, Status.PASS);

			}
		} catch (Exception e) {
			e.printStackTrace();
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);
		}
		return blnReturn;
	}

	/*************************************************
	 * #FunctionName : verifyElementDisplayed() 
	 * #Author : Cognizant 
	 * #Description : This method will verify the Webelement 
	 * #Date of creation : 10-Sep-2018
	 **************************************************/
	public boolean verifyElementDisplayed(By object, String strReportStep, String strReportDescription) {
		boolean blnReturn = false;
		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
			if (element.isDisplayed()) {
				blnReturn = true;
				report.updateTestLog(strReportStep, strReportDescription, Status.PASS);

			} else {
				blnReturn = false;
				report.updateTestLog(strReportStep, strReportDescription, Status.FAIL);

			}
		} catch (Exception e) {
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);

			e.printStackTrace();
		}
		return blnReturn;
	}
	/*****************************************
	 * #FunctionName : verifyTextDisplayed() 
	 * #Author : Cognizant 
	 * #Description : This method will verify the obtained text contains the given text 
	 * #Date of creation : 10-Sep-2018
	 *******************************************/
	public boolean verifyTextDisplayed(By object, String strExpectedText, String strReportStep,
			String strReportDescription) throws InterruptedException {
		boolean blnReturn = false;
		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
			String strText = element.getText();
			if (strText.contains(strExpectedText)) {
				blnReturn = true;
				report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
			} 
			else if ((strText == "" )&&(strExpectedText == "")) {
				blnReturn = true;
				report.updateTestLog(strReportStep, "Validate only Blank value is displayed for"+strReportStep+" in the application and Data table", Status.PASS);
			} else {
				blnReturn = false;
				report.updateTestLog(strReportStep, strReportDescription, Status.FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);
		}
		return blnReturn;
	}

	/*************************************************
	 * #FunctionName : verifyTextRetained() 
	 * #Author : Cognizant 
	 * #Description : This method will verify the obtained text contains the given text 
	 * #Date of creation : 10-Sep-2018
	 **************************************************/
	public boolean verifyTextRetained(By object, String strExpectedText, String strReportStep,
			String strReportDescription) throws InterruptedException {
		boolean blnReturn = false;
		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
			String strText = element.getAttribute("value");
			if (strText.contains(strExpectedText)) {
				blnReturn = true;
				report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
			} else {
				blnReturn = false;
				report.updateTestLog(strReportStep, strReportDescription, Status.FAIL);

			}
		} catch (Exception e) {
			e.printStackTrace();
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);

		}
		return blnReturn;
	}


	/*****************************************
	 * #FunctionName : verifyValueDisplayed() 
	 * #Author : Cognizant 
	 * #Description : This method will verify the obtained text is not null 
	 * #Date of creation : 10-Sep-2018
	 *******************************************/
	public boolean verifyValueDisplayed(By object, String strReportStep, String strReportDescription)
			throws InterruptedException {
		boolean blnReturn = false;
		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
			String strText = element.getText();
			if (strText != null) {
				blnReturn = true;
				report.updateTestLog(strReportStep, strReportDescription, Status.PASS);

			} else {
				blnReturn = false;
				report.updateTestLog(strReportStep, strReportDescription, Status.FAIL);

			}
		} catch (Exception e) {
			e.printStackTrace();
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);

		}
		return blnReturn;
	}

	/********************************************************
	 * #FunctionName : clickElement() 
	 * #Author : Cognizant 
	 * #Description : This method will click the Webelement 
	 * #Date of creation : 11-Sep-2018
	 *********************************************************/
	public boolean clickElement(By object, String strReportStep, String strReportDescription)
			throws InterruptedException {
		boolean blnReturn = false;
		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
			element = wait.until(ExpectedConditions.elementToBeClickable(object));
			element.click();
			blnReturn = true;
			if((strReportDescription.trim()).length()!=0){
				report.updateTestLog(strReportStep, strReportDescription, Status.PASS);

			}
		} catch (Exception e) {
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);

			e.printStackTrace();
		}
		return blnReturn;
	}

	/*****************************************
	 * #FunctionName : clickBack() 
	 * #Author : Cognizant 
	 * #Description : This method will navigate to previous screen 
	 * #Date of creation : 11-Sep-2018
	 *******************************************/
	public void clickBack() {
		try {
			driver.navigate().back();
		} catch (NoSuchWindowException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*************************************************
	 * #FunctionName : switchHandles() 
	 * #Author : Cognizant 
	 * #Description : This method will switch windows and validate the url of the window against the provided url and switch to main window 
	 * #Date of creation : 11-Sep-2018
	 **************************************************/
	public void switchHandles(String strChangeWindow, String strReportStep, String strReportDescription ) {
		try {
			String sMainwindow = driver.getWindowHandle();
			Set<String> totalWindows = driver.getWindowHandles();
			if (totalWindows.size() > 1) {
				for (String loopWindow : totalWindows) {      
					if (!(sMainwindow.equals(loopWindow))) {
						driver.switchTo().window(loopWindow);
						String currentUrl = driver.getCurrentUrl();
						if (currentUrl.contains(strChangeWindow)) {
							report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
						} /*else {
                                          report.updateTestLog(strReportStep, strReportDescription,
                                                        Status.FAIL);

                                   }*/
					}
				}
				//driver.close();
			}      //driver.switchTo().window(sMainwindow);
		} catch (NoSuchWindowException e) {
			report.updateTestLog("New Window", "No such window is available", Status.FAIL);

			e.printStackTrace();
		} catch (Exception e) {
			report.updateTestLog("New Window", "No such window is available", Status.FAIL);

			e.printStackTrace();
		}
	}

	/*************************************************
	 * #FunctionName : switchToFrame() 
	 * #Author : Cognizant 
	 * #Description : This method will switch windows with an alert and validate a new window is opened and switch to main window 
	 * #Date of creation : 14-Sep-2018
	 **************************************************/

	public void switchToFrame(String strFrame) {
		try {
			if(strFrame.equals("upper")) {
				driver.switchTo().frame("upper");
			}
			else {
				driver.switchTo().frame("lower");
			}

		}
		catch(Exception e) {
			report.updateTestLog("Switching Frames", "Exception occured. Refer the testlog for more details", Status.FAIL);
		}
	}

	/*************************************************
	 * #FunctionName : clickMenuOption() 
	 * #Author : Cognizant 
	 * #Description : This method will click on a Menu option
	 * #Date of creation : 17-Sep-2018
	 **************************************************/

	public void clickMenuOption(WebElement obj1, WebElement obj2, String strReportStep, String strReportDescription) {
		try {

			Actions actions = new Actions((WebDriver) driver);
			actions.moveToElement(obj1);

		}
		catch(Exception e) {

		}

	}

	/*************************************************
	 * #FunctionName : switchHandleswithAlert() 
	 * #Author : Cognizant 
	 * #Description : This method will switch windows with an alert and validate a new window is opened and switch to main window 
	 * #Date of creation : 11-Sep-2018
	 **************************************************/
	public void switchHandleswithAlert(String strChangeWindow) {
		try {
			String sMainwindow = driver.getWindowHandle();
			Set<String> totalWindows = driver.getWindowHandles();
			if (totalWindows.size() > 1) {
				report.updateTestLog("New Window", "Validate if New Window is opened in a new tab", Status.PASS);

			} else {
				report.updateTestLog("New Window",
						"Validate if New Window is opened in a new tab as No window has opened", Status.FAIL);

			}
			driver.switchTo().window(sMainwindow);
		} catch (NoSuchWindowException e) {
			report.updateTestLog("New Window", "No such window is available", Status.FAIL);

			e.printStackTrace();
		} catch (Exception e) {
			report.updateTestLog("New Window", "No such window is available", Status.FAIL);

			e.printStackTrace();
		}
	}

	/*************************************************
	 * #FunctionName : validateFieldLength() 
	 * #Author : Cognizant 
	 * #Description : This method validates the field is empty, returns true if length is Zero
	 * #Date of creation : 11-Sep-2018
	 **************************************************/
	public boolean validateFieldLength(By object, String strReportStep, String strReportDescription)
			throws InterruptedException {
		boolean blnReturn = false;
		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
			String strFieldobject = element.getAttribute("value");
			if (strFieldobject.length() > 0) {
				blnReturn = false;
				report.updateTestLog(strReportStep, strReportDescription, Status.FAIL);

			} else {
				blnReturn = true;
				report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
			}
		} catch (Exception e) {
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);
			e.printStackTrace();
		}
		return blnReturn;
	}

	/*****************************************
	 * #FunctionName : verifyOptionExist() 
	 * #Author : Cognizant 
	 * #Description : This method will verify the webelement 
	 * #Date of creation : 11-Sep-2018
	 *******************************************/
	public boolean verifyOptionExist(By object, String strReportStep) throws InterruptedException {
		boolean blnReturn = false;
		waitForElement(object);
		try {
			element = driver.findElement(object);
			if (element.isDisplayed()) {
				blnReturn = true;
			} else {
				report.updateTestLog(strReportStep, "The element with the property : " + object + " is not displayed",
						Status.FAIL);
				blnReturn = false;
			}
		} catch (Exception e) {
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);

			e.printStackTrace();
		}
		return blnReturn;
	}
	/*************************************************
	 * #FunctionName : clickByTwoObjects() 
	 * #Author : Cognizant
	 * #Description : This method will click the forgot Username link French using two identifiers 
	 * #Date of creation : 11-Sep-2018
	 **************************************************/
	public boolean clickByTwoObjects(By object0, By object1, String strReportStep,
			String strReportDescription) {
		boolean blnReturn = false;
		try {
			element = wait.until(ExpectedConditions.elementToBeClickable(object0));				
			element.click();
			blnReturn = true;
			report.updateTestLog(strReportStep, strReportDescription, Status.PASS);

		} catch (Exception e) {
			try {
				element = wait.until(ExpectedConditions.elementToBeClickable(object1));				
				element.click();
				blnReturn = true;
				report.updateTestLog(strReportStep, strReportDescription, Status.PASS);

			}
			catch (Exception e1) {
				report.updateTestLog(strReportStep, strReportDescription, Status.FAIL);

			}
		}
		return blnReturn;
	}

	/*************************************************
	 * #FunctionName : verifyElementByTwoObjects() 
	 * #Author : Cognizant
	 * #Description : This method will verify two elements are displayed 
	 * #Date of creation : 11-Sep-2018
	 **************************************************/
	public boolean verifyElementByTwoObjects(By object0, By object1, String strReportStep,
			String strReportDescription) {
		boolean blnReturn = false;
		try {
			element = wait.until(ExpectedConditions.elementToBeClickable(object0));                         
			if (element.isDisplayed()) {
				blnReturn = true;
				report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
			}
		} catch (Exception e) {
			try {
				element = wait.until(ExpectedConditions.elementToBeClickable(object1));                         
				if (element.isDisplayed()) {
					blnReturn = true;
					report.updateTestLog(strReportStep, strReportDescription, Status.PASS);

				}
			} catch (Exception e1) {
				report.updateTestLog(strReportStep, strReportDescription, Status.FAIL);

			}
		}
		return blnReturn;
	}
	/*****************************************
	 * #FunctionName : waitForElement() 
	 * #Author : Cognizant 
	 * #Description : This method will simply wait for 5 secs 
	 * #Date of creation : 11-Sep-2018
	 *******************************************/
	public void waitForElement(By object) throws InterruptedException {
		try {
			@SuppressWarnings("unused")
			WebElement waitElement = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
		} catch (Exception e) {
		}
	}

	/*****************************************
	 * #FunctionName : waitForElementToClick() 
	 * #Author : Cognizant 
	 * #Description : This method will simply wait for 5 secs 
	 * #Date of creation : 11-Sep-2018
	 *******************************************/
	public void waitForElementToClick(By object) throws InterruptedException {
		@SuppressWarnings("unused")
		WebElement waitElement = wait.until(ExpectedConditions.elementToBeClickable(object));
	}

	/*************************************************
	 * #FunctionName : radioButtonClicked() 
	 * #Author : Cognizant 
	 * #Description : This method will click the radio button 
	 * #Date of creation : 14-Aug-2017
	 **************************************************/
	public boolean radioButtonClicked(By object, String strRole, String strReportStep, String strReportDescription)
			throws InterruptedException {
		boolean blnReturn = false;
		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
			List<WebElement> listOfRadioButtons = driver.findElements(object);
			for (@SuppressWarnings("unused") WebElement radioButton : listOfRadioButtons) {
				if (strRole.equalsIgnoreCase("Buyer")) {
					listOfRadioButtons.get(0).click();
					blnReturn = true;
					report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
					break;
				} else {
					listOfRadioButtons.get(1).click();
					report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
					break;
				}
			}
		} catch (Exception e) {
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);
			e.printStackTrace();
		}
		return blnReturn;
	}

	/*************************************************
	 * #FunctionName : isCheckBoxChecked() 
	 * #Author : Cognizant 
	 * #Description : This method will check whether the radio button is clicked 
	 * #Date of creation : 11-Sep-2018
	 **************************************************/
	public boolean isCheckBoxChecked(By object, String strReportStep, String strReportDescription)
			throws InterruptedException {
		boolean blnChecked = false;
		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
			if (blnChecked = element.isSelected()) {
				blnChecked = true;
				report.updateTestLog(strReportStep, strReportDescription, Status.PASS);

			} else {
				blnChecked = false;
				report.updateTestLog(strReportStep, strReportDescription, Status.FAIL);

			}
		} catch (Exception e) {
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);


			e.printStackTrace();
		}
		return blnChecked;
	}
	/*************************************************
	 * #FunctionName : isCheckBoxBlank() 
	 * #Author : Cognizant 
	 * #Description : This method will check whether the radio button is blank 
	 * #Date of creation : 11-Sep-2018
	 **************************************************/
	public boolean isCheckBoxBlank(By object, String strReportStep, String strReportDescription)
			throws InterruptedException {
		boolean blnChecked = false;

		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
			if (!(blnChecked = element.isSelected())) {
				blnChecked = true;
				report.updateTestLog(strReportStep, strReportDescription, Status.PASS);

			} else {
				blnChecked = false;
				report.updateTestLog(strReportStep, strReportDescription, Status.FAIL);
			}
		} catch (Exception e) {
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);
			e.printStackTrace();
		}
		return blnChecked;
	}

	/*************************************************
	 * #FunctionName : clearCookies() 
	 * #Author : Cognizant 
	 * #Description : This method will clear the cokiees in the mobile 
	 * #Date of creation : 12-Sep-2018
	 **************************************************/
	public void clearCookies() {
		try {
			driver.manage().deleteAllCookies();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/*************************************************
	 * #FunctionName : selectByValue() 
	 * #Author : Cognizant 
	 * #Description : This method will enter a object on the element 
	 * #Date of creation : 13-Sep-2018
	 **************************************************/
	public boolean selectByValue(By object, String strTextValueToSelect, String strReportStep, String strReportDescription) throws InterruptedException {

		Select dropdown = null;
		boolean blnReturn = false;
		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
			if ((strTextValueToSelect.trim()) != "") {
				dropdown = new Select(driver.findElement(object));
				dropdown.selectByVisibleText(strTextValueToSelect);
				blnReturn = true;
				if ((strReportDescription.trim()).length() != 0) {
					report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present", Status.FAIL);
		}
		return blnReturn;
	}

	/*************************************************
	 * #FunctionName : selectDropdownValue() 
	 * #Author : Cognizant 
	 * #Description : This method will select a value from a dropdown
	 * #Date of creation : 21-Sep-2018
	 **************************************************/

	public void selectDropdownValue(By object, By menuBar, By subMenu, String subModuleTest, String strReportStep, String strReportDescription) throws InterruptedException {   
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(object));
		element.click();
		WebElement ele = wait.until(ExpectedConditions.visibilityOfElementLocated(menuBar));
		List<WebElement> s1 = ele.findElements(subMenu);
		try {
			for(WebElement option : s1){
				if(option.getText().equalsIgnoreCase(subModuleTest)) {
					option.click();
					report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
					break;
				}  
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			report.updateTestLog(strReportStep, "An Exception occured "+e, Status.FAIL);
		}
	}

	/*****************************************
	 * #FunctionName : compareStrings() 
	 * #Author : Cognizant 
	 * #Description : This method will verify the obtained text is not null 
	 * #Date of creation : 04-Oct-2018
	 *******************************************/
	public boolean compareStrings(By object,  String strReportStep, String strReportDescription)
			throws InterruptedException {
		boolean blnReturn = false;
		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
			String strText = element.getText();
			int StrLength=strText.length();
			if (StrLength<=200) {
				blnReturn = true;
				report.updateTestLog(strReportStep, strReportDescription, Status.PASS);

			} else {
				blnReturn = false;
				report.updateTestLog(strReportStep, strReportDescription, Status.FAIL);

			}
		} catch (Exception e) {
			e.printStackTrace();
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);

		}
		return blnReturn;
	}


	// Added by Venu - Start

	/*************************************************
	 * #FunctionName : verifyPageTitle() 
	 * #Author : Cognizant 
	 * #Description : This method will verify the page title
	 * #Date of creation : 28-Sep-2018
	 ***************************************************/
	public boolean verifyPageTitle(String pageTitle) {
		return driver.getTitle().equals(pageTitle);
	}

	/*************************************************
	 * #FunctionName : switchToFOLFrame() 
	 * #Author : Cognizant 
	 * #Description : This method will switch to FOL Frame
	 * #Date of creation : 28-Sep-2018
	 ***************************************************/
	public void switchToFOLFrame(String strFrame) {
		try {
			driver.switchTo().frame(strFrame);
		}
		catch(Exception e) {
			report.updateTestLog("Switching Frames", "Exception occured. Refer the testlog for more details", Status.FAIL);
		}
	}

	/*************************************************
	 * #FunctionName : switchToDefaultContent() 
	 * #Author : Cognizant 
	 * #Description : This method will switch to Default Content
	 * #Date of creation : 28-Sep-2018
	 ***************************************************/
	public void switchToDefaultContent() {
		try {
			driver.switchTo().defaultContent();
		}
		catch(Exception e) {
			report.updateTestLog("Switching Frames", "Exception occured. Refer the testlog for more details", Status.FAIL);
		}
	}

	/*************************************************
	 * #FunctionName : getTableRowValue() 
	 * #Author : Cognizant 
	 * #Description : This method will get a table Row value
	 * #Date of creation : 28-Sep-2018
	 ***************************************************/
	public String getTableRowValue(By object, By object2) {
		if("SUB".equals(driver.findElement(object2).getText())) {
			return driver.findElement(object).getText();
		}
		return null;
	}

	/*************************************************
	 * #FunctionName : getText() 
	 * #Author : Cognizant 
	 * #Description : This method will get the text values from an object
	 * #Date of creation : 28-Sep-2018
	 ***************************************************/
	public String getText(By object) {
		return driver.findElement(object).getText();
	}

	/*************************************************
	 * #FunctionName : pressTab() 
	 * #Author : Cognizant 
	 * #Description : This method will press the Tab key
	 * #Date of creation : 28-Sep-2018
	 ***************************************************/
	public void pressTab(By object) {
		driver.findElement(object).sendKeys(Keys.TAB);
	}

	/*************************************************
	 * #FunctionName : clickRadioButton() 
	 * #Author : Cognizant 
	 * #Description : This method will click the radio button
	 * #Date of creation : 28-Sep-2018
	 ***************************************************/
	public boolean clickRadioButton(By object, String selectedOption, String strReportStep, String strReportDescription)
			throws InterruptedException {
		boolean blnReturn = false;
		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
			List<WebElement> radioOptions = driver.findElements(object);
			if (selectedOption.equalsIgnoreCase("no")) {
				radioOptions.get(0).click();
				blnReturn = true;
				report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
			} else {
				radioOptions.get(1).click();
				report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
			}
		} catch (Exception e) {
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);
			e.printStackTrace();
		}
		return blnReturn;
	}

	/*************************************************
	 * #FunctionName : switchToPopUp() 
	 * #Author : Cognizant 
	 * #Description : This method will switch to a popup
	 * #Date of creation : 28-Sep-2018
	 ***************************************************/
	public void switchToPopUp(String windowTitle, String strReportStep, String strReportDescription ) {
		try {
			String sMainwindow = driver.getWindowHandle();
			Set<String> totalWindows = driver.getWindowHandles();
			if (totalWindows.size() > 1) {
				for (String loopWindow : totalWindows) {
					driver.switchTo().window(loopWindow);
					if(driver.getTitle().equals(windowTitle)) {
						driver.switchTo().window(loopWindow);
						report.updateTestLog("New Window",
								"Validate if New Window with " + windowTitle + "  title is opened in new tab",
								Status.PASS);

					}
				}

			} else {
				report.updateTestLog("New Window", "No window has opened", Status.FAIL);
			}

		} catch (NoSuchWindowException e) {
			report.updateTestLog("New Window", "No such window is available", Status.FAIL);

			e.printStackTrace();
		} catch (Exception e) {
			report.updateTestLog("New Window", "No such window is available", Status.FAIL);

			e.printStackTrace();
		}
	}

	/*************************************************
	 * #FunctionName : selectGender() 
	 * #Author : Cognizant 
	 * #Description : This method will click the Gender in FOL screen
	 * #Date of creation : 28-Sep-2018
	 ***************************************************/ 	
	public boolean clickAcceptOnPopUp(By object, String strReportStep, String strReportDescription)
			throws InterruptedException {
		boolean blnReturn = false;
		try {
			element = wait.until(ExpectedConditions.elementToBeClickable(object));
			element.click();
			blnReturn = true;
		} catch (Exception e) {
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);
			e.printStackTrace();
		}
		return blnReturn;
	}

	/*************************************************
	 * #FunctionName : hoverOnElement() 
	 * #Author : Cognizant 
	 * #Description : This method will hover on an element
	 * #Date of creation : 28-Sep-2018
	 ***************************************************/
	public void hoverOnElement(By obj,String step,String description) {
		try {
			Actions action = new Actions(driver.getWebDriver());
			WebElement elem = wait.until(ExpectedConditions.visibilityOfElementLocated(obj));
			action.moveToElement(elem).moveToElement(elem).perform();
			report.updateTestLog(step,description,
					Status.PASS);
		}
		catch (Exception e) {
			report.updateTestLog("MouseHover", "Hover the mouse on to element with the property : " + obj + " is not present",
					Status.FAIL);
		}

	}

	/*************************************************
	 * #FunctionName : selectGender() 
	 * #Author : Cognizant 
	 * #Description : This method will select the Gender in FOL screen
	 * #Date of creation : 28-Sep-2018
	 ***************************************************/
	public boolean selectGender(By object, String strGender, String strReportStep, String strReportDescription)
			throws InterruptedException {
		boolean blnReturn = false;
		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
			List<WebElement> listOfRadioButtons = driver.findElements(object);
			for (WebElement radioButton : listOfRadioButtons) {
				if (strGender.equalsIgnoreCase("FEMALE")) {
					listOfRadioButtons.get(0).click();
					blnReturn = true;
					report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
					break;
				} else {
					listOfRadioButtons.get(1).click();
					report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
					break;
				}
			}
		} catch (Exception e) {
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);
			e.printStackTrace();
		}
		return blnReturn;
	}

	/*************************************************
	 * #FunctionName : selectRadioButton() 
	 * #Author : Cognizant 
	 * #Description : This method will click the radio button
	 * #Date of creation : 28-Sep-2018
	 ***************************************************/
	public boolean selectRadioButton(By object, String strSelectedOption, String strReportStep, String strReportDescription)
			throws InterruptedException {
		boolean blnReturn = false;
		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
			List<WebElement> listOfRadioButtons = driver.findElements(object);
			for (WebElement radioButton : listOfRadioButtons) {
				if (strSelectedOption.equalsIgnoreCase("NO")) {
					listOfRadioButtons.get(0).click();
					blnReturn = true;
					report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
					break;
				} else {
					listOfRadioButtons.get(1).click();
					report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
					break;
				}
			}
		} catch (Exception e) {
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);
			e.printStackTrace();
		}
		return blnReturn;
	}

	/*************************************************
	 * #FunctionName : switchToWindowWithTitle() 
	 * #Author : Cognizant 
	 * #Description : This method will switch to a window using title of the window
	 * #Date of creation : 28-Sep-2018
	 ***************************************************/		
	public void switchToWindowWithTitle(String partOfWindowTitleToBeSwitched) {
		try{
			report.updateTestLog("Searching For window with title contains "+partOfWindowTitleToBeSwitched,
					"Searching for window", Status.PASS);
			for(String temp:driver.getWindowHandles()) {
				driver.switchTo().window(temp);
				if(driver.getTitle().trim().toLowerCase().contains(partOfWindowTitleToBeSwitched.toLowerCase().trim())) {
					System.out.println("found the window");					
					report.updateTestLog("Switching To New Window with title Contains"+partOfWindowTitleToBeSwitched,
							"Found such window is available", Status.PASS);
					break;
				}					
			}

		}
		catch (Exception e) {
			report.updateTestLog("Switching To New Window with title Contains"+partOfWindowTitleToBeSwitched,
					"Fail to such window is available", Status.FAIL);
		}

	}

	/*************************************************
	 * #FunctionName : clickMenu() 
	 * #Author : Cognizant 
	 * #Description : This method will select a Sub Menu from a Main Menu
	 * #Date of creation : 12-Oct-2018
	 **************************************************/
	public void clickMenu(By object, By menuBar, By subMenu, String subModuleTest, String strReportStep, String strReportDescription) throws InterruptedException {   
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(object));
		element.click();
		Thread.sleep(2000);
		element.click();
		WebElement ele = wait.until(ExpectedConditions.visibilityOfElementLocated(menuBar));
		List<WebElement> s1 = ele.findElements(subMenu);
		try {
			for(WebElement option : s1){
				if(option.getText().equals(subModuleTest)) {
					option.click();
					report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
					break;
				}  
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			report.updateTestLog(strReportStep, "An Exception occured "+e, Status.FAIL);
		}
	}

	/*****************************************
	 * #FunctionName : verifyTextContains() 
	 * #Author : Cognizant 
	 * #Description : This method will verify the obtained text contains the portion of a given text 
	 * #Date of creation : 12-Oct-2018
	 *******************************************/
	public boolean verifyTextContains(By object, String strExpectedText, String strReportStep,
			String strReportDescription) throws InterruptedException {
		boolean blnReturn = false;
		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
			String strText = element.getText();
			if (strExpectedText.contains(strText)) {
				blnReturn = true;
				report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
			} 
			else if ((strText == "" )&&(strExpectedText == "")) {
				blnReturn = true;
				report.updateTestLog(strReportStep, "Validate only Blank value is displayed for"+strReportStep+" in the application and Data table", Status.PASS);
			} else {
				blnReturn = false;
				report.updateTestLog(strReportStep, strReportDescription, Status.FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);
		}
		return blnReturn;
	}    

	/*****************************************
	 * #FunctionName : resizeBrowser() 
	 * #Author : Cognizant 
	 * #Description : This method will resize the browser 
	 * #Date of creation : 17-Oct-2018
	 *******************************************/
	public void resizeBrowser() throws Exception{
		Dimension d = new Dimension(1280,1200);
		driver.manage().window().setSize(d);
		Thread.sleep(2000);
		driver.manage().window().maximize();

	}

	/*****************************************
	 * #FunctionName : clickMenuOption() 
	 * #Author : Cognizant 
	 * #Description : This method will hover on an element and click a sub menu 
	 * #Date of creation : 19-Oct-2018
	 *******************************************/	
	public void clickMenuOption(By obj, By subMenu, String strReportStep, String strReportDescription) {
		try {
			Actions action = new Actions(driver.getWebDriver());
			WebElement ele = wait.until(ExpectedConditions.visibilityOfElementLocated(obj));

			action.moveToElement(ele).moveToElement(ele).perform();
			action.moveToElement(driver.findElement(subMenu)).click().perform();            
			report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
		}
		catch (Exception e) {
			report.updateTestLog(strReportStep, "Mouse hover on to element with the property : " + obj + " is not present",
					Status.FAIL);
			e.printStackTrace();
		}

	}

	/*****************************************
	 * #FunctionName : selectMultipleCheckBox() 
	 * #Author : Cognizant 
	 * #Description : This method will select multiple checkboxes based on used input 
	 * #Date of creation : 24-Oct-2018
	 *******************************************/	

	public void selectMultipleCheckBox(By object, String strArray, String strReportStep, String strReportDescription) throws Exception {
		String[] arr= strArray.split(",");
		List<WebElement> checkBoxList= driver.findElements(object);
		try {
			for(int i=0;i<checkBoxList.size();i++) {
				WebElement s=checkBoxList.get(i);
				String ele= checkBoxList.get(i).getText();
					if(ele.contains(arr[i])) {
						if(s.isSelected()) {
							report.updateTestLog(strReportStep, "The element is already selected", Status.PASS);
						}
						else {
							s.click();
						}
					}
				}
			
			report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
		}
		catch(Exception e) {
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present", Status.FAIL);
			e.printStackTrace();
		}
	}

	/*************************************************
	 * #FunctionName : clickRadioButton() 
	 * #Author : Cognizant 
	 * #Description : This method will click the radio button
	 * #Date of creation : 25-Oct-2018
	 ***************************************************/
	public boolean clickRadioButtonCA(By object, String strReportStep, String strReportDescription) throws Exception {
		boolean blnChecked = false;
		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
			if (blnChecked = element.isSelected()) {
				report.updateTestLog(strReportStep, "The element is already selected.", Status.PASS);
			}
			else {
				element.click();
				report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
				blnChecked=true;
			}
		}
		catch(Exception e) {
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present", Status.FAIL);
			e.printStackTrace();
		}
		return blnChecked;
	}

	/*****************************************
	 * #FunctionName : javaScriptClickElement() 
	 * #Author : Cognizant 
	 * #Description : This method will click on  element with javaScript
	 * #Date of creation : 16-oct-2018
	 *******************************************/
	public boolean javaScriptClickElement(By object, String strReportStep, String strReportDescription)
			throws InterruptedException {
		boolean blnReturn = false;
		try {
			element = wait.until(ExpectedConditions.elementToBeClickable(object));
			JavascriptExecutor executor = (JavascriptExecutor)driver.getWebDriver();
			executor.executeScript("arguments[0].click();", element);
			blnReturn = true;
			if((strReportDescription.trim()).length()!=0){
				report.updateTestLog(strReportStep, strReportDescription, Status.PASS);

			}
		} catch (Exception e) {
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);

			e.printStackTrace();
		}
		return blnReturn;
	}

	/*****************************************
	 * #FunctionName : verifyTextDisplayedIgnoreCase() 
	 * #Author : Cognizant 
	 * #Description : This method will verify the obtained text contains the given text by ignoring the letter case
	 * #Date of creation : 10-Oct-2018
	 *******************************************/
	public boolean verifyTextDisplayedIgnoreCase(By object, String strExpectedText, String strReportStep,
			String strReportDescription) throws InterruptedException {
		boolean blnReturn = false;
		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
			String strText = element.getText().trim();
			if (strText.toLowerCase().contains(strExpectedText.toLowerCase().trim())) {
				blnReturn = true;
				report.updateTestLog(strReportStep, strReportDescription+"Expected:"+strExpectedText+",Actual Text:"+strText, Status.PASS);
			} else {
				blnReturn = false;
				report.updateTestLog(strReportStep, strReportDescription+"Expected:"+strExpectedText+",Actual Text:"+strText, Status.FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);
		}
		return blnReturn;
	}

	/*****************************************
	 * #FunctionName : zoomIn() 
	 * #Author : Cognizant 
	 * #Description : This method will zoomIn the page 
	 * #Date of creation : 16-oct-2018
	 *******************************************/
	public void zoomIn(String strReportStep,String strDescription) throws InterruptedException {

		try {

			WebElement html=wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.tagName("html"))));
			html.sendKeys(Keys.chord(Keys.CONTROL,Keys.ADD));
			report.updateTestLog(strReportStep, strDescription,Status.PASS);
		} catch (Exception e) {
			report.updateTestLog(strReportStep, "Error while zooming in",Status.FAIL);
			e.printStackTrace();
		}

	}
	/*****************************************
	 * #FunctionName : zoomOut() 
	 * #Author : Cognizant 
	 * #Description : This method will zoomOut the page 
	 * #Date of creation : 16-oct-2018
	 *******************************************/
	public void zoomOut(String strReportStep,String strDescription) throws InterruptedException {

		try {

			WebElement html=wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.tagName("html"))));
			html.sendKeys(Keys.chord(Keys.CONTROL,Keys.SUBTRACT));
			report.updateTestLog(strReportStep, strDescription,Status.PASS);
		} catch (Exception e) {
			report.updateTestLog(strReportStep, "Error while zooming in",Status.FAIL);
			e.printStackTrace();
		}

	}

	/*************************************************
	 * #FunctionName : verifyPageUrlEndswithText() 
	 * #Author : Cognizant 
	 * #Description : This method will click the radio button
	 * #Date of creation : 28-Sep-2018
	 ***************************************************/
	public boolean verifyPageUrlEndswithText(String textToVerify, String strReportStep, String strReportDescription)
			throws Exception {
		boolean status;
		try {
			if(driver.getCurrentUrl().trim().endsWith(textToVerify)) {
				report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
				status=true;
			}
			else {
				report.updateTestLog(strReportStep, strReportDescription, Status.FAIL);
				status=false;
			}
		} catch (Exception e) {
			status=false;
			report.updateTestLog(strReportStep, "Unable to verify page title",
					Status.FAIL);
			e.printStackTrace();
		}

		return status;
	}



	/*****************************************
	 * #FunctionName : verifyOptionExistInDropDown() 
	 * #Author : Cognizant 
	 * #Description : This method will verify the webelement 
	 * #Date of creation : 16-oct-2018
	 *******************************************/
	public boolean verifyOptionExistInDropDown(By object, String optionTextToVerify,String strReportStep,String strDescription) throws InterruptedException {
		Select dropdown = null;
		boolean status = false;
		waitForElement(object);
		try {
			element = driver.findElement(object);
			dropdown=new Select(element);
			for(WebElement option:dropdown.getOptions()) {
				if(option.getText().trim().equals(optionTextToVerify)) {
					status=true;
					break;
				}
			}
			if(status==true) report.updateTestLog(strReportStep, strDescription,Status.PASS);
			else report.updateTestLog(strReportStep, strDescription,Status.FAIL);
		} catch (Exception e) {
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);

			e.printStackTrace();
		}
		return status;
	}
	/*****************************************
	 * #FunctionName : verifyOptionNotExistInDropDown() 
	 * #Author : Cognizant 
	 * #Description : This method will verify the webelement 
	 * #Date of creation : 16-oct-2018
	 *******************************************/
	public boolean verifyOptionNotExistInDropDown(By object, String optionTextToVerify, String strReportStep,String strDescription) throws InterruptedException {
		Select dropdown = null;
		boolean status = true;
		waitForElement(object);
		try {
			element = driver.findElement(object);
			dropdown=new Select(element);
			for(WebElement option:dropdown.getOptions()) {
				if(option.getText().trim().equals(optionTextToVerify)) {
					status=false;
					break;
				}
			}
			if(status==true) report.updateTestLog(strReportStep, strDescription,Status.PASS);
			else report.updateTestLog(strReportStep, strDescription,Status.FAIL);
		} catch (Exception e) {
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present",
					Status.FAIL);

			e.printStackTrace();
		}
		return status;
	}

	/*****************************************
	 * #FunctionName : getAttributeValue() 
	 * #Author : Cognizant 
	 * #Description : This method will verify the webelement 
	 * #Date of creation : 16-oct-2018
	 *******************************************/

	public String getAttributeValue(By object,String attributeName) {
		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
			String text=element.getAttribute(attributeName);
			report.updateTestLog("GetAttbributeValue", "Getting attribute value on element.attribute"+attributeName+" value is:"+text, Status.PASS);
			return text;
		} catch (Exception e) {
			report.updateTestLog("GetGetAttbributeValueText", "Fail to get GetAttbributeValue on element."+object, Status.FAIL);
			return null;
		}

	}	

	/*****************************************
	 * #FunctionName : filterResultValidation() 
	 * #Author : Cognizant 
	 * #Description : This method will verify results are displayed only on the company filter criteria applied 
	 * #Date of creation : 16-oct-2018
	 *******************************************/

	public void filterResultValidation_SelectedItems(By object, By objNoOfferingError, String strArray, String strReportStep, String strReportDescription) throws Exception {
		String[] arr= strArray.split(",");
		List<WebElement> companyColumn= driver.findElements(object);
		
		int filterCompanyCount = 0;
		int totalWQListCount = companyColumn.size();
		try {
			for(int i=0;i<arr.length;i++) {
				for(WebElement ele: companyColumn) {
					if(ele.getText().contains(arr[i])) {
						filterCompanyCount++;	
					}
				}
			}
			//int otherOfferingCount = totalWQListCount - filterCompanyCount;
			if(filterCompanyCount > 0) {
				report.updateTestLog(strReportStep, filterCompanyCount+" offerings are displayed only based on the filter criteria applied from the total of " +totalWQListCount+ " work queue items", Status.PASS);
			}
			else if(filterCompanyCount<=0){
				WebElement noOfferingErrorMessage = driver.findElement(objNoOfferingError);
				if(noOfferingErrorMessage.isDisplayed())
				report.updateTestLog("Error Message", "No Offerings are found for the filter criteria", Status.PASS);
				else
					report.updateTestLog(strReportStep, strReportDescription, Status.FAIL);
			}
			else {
				report.updateTestLog(strReportStep, strReportDescription, Status.FAIL);
					
			}
		}
		catch(Exception e) {
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present", Status.FAIL);
			e.printStackTrace();
		}
	}

	/*************************************************
	 * #FunctionName : splitString() 
	 * #Author : Cognizant 
	 * #Description : This method will verify the obtained text contains the given text 
	 * #Date of creation : 26-Oct-2018
	 **************************************************/
	public  void splitString(By object,String CopiedName,String strReportStep,String strReportDescription ) {
		String[] parts = CopiedName.split("-");
		String part1 = parts[0]; // 004
		String part2 = parts[1]; // 034556
		element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
		String strText = element.getText();
		if(strText.contains(part1)||strText.contains(part2)) {
			report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
		}
		else {
			report.updateTestLog(strReportStep, strReportDescription, Status.FAIL);

		}

	}

	/*****************************************
	 * #FunctionName : enterValuesToListBox() 
	 * #Author : Cognizant 
	 * #Description : This method will enter multiple values into a list box from a string array without duplicate values
	 * #Date of creation : 16-oct-2018
	 *******************************************/

	public void enterValuesToListBox(By objectTextBox, By objectButton, String strToEnter, String strReportStep, String strReportDescription) throws Exception {
		String[] arr= strToEnter.split(",");
		try {
			int count=0;
			for(int j=0;j<arr.length;j++) {
				for(int k =j+1;k<arr.length;k++) {
					if(arr[j]==arr[k]) {
						count++;
					}
				}
			}				
			if(count>=1) {
				report.updateTestLog(strReportStep, count+" Duplicate(s) values found. Please check and reenter the input", Status.FAIL);
			} else {
				for(int i=0;i<arr.length;i++) {
					try {
						Thread.sleep(1000);	
						WebElement elementTextBox = wait.until(ExpectedConditions.visibilityOfElementLocated(objectTextBox));
						elementTextBox.clear();
						elementTextBox.sendKeys(arr[i]);
					} catch(Exception e) {
						e.printStackTrace();
						report.updateTestLog("Exception", "Exception occured and the error is"+ e, Status.FAIL);
					} 
					try {
						Thread.sleep(1000);
						WebElement elementButton = wait.until(ExpectedConditions.elementToBeClickable(objectButton));
						elementButton.click();	
					} catch(Exception ex) {
						ex.printStackTrace();
						report.updateTestLog("Exception", "Exception occured and the error is"+ ex, Status.FAIL);
					}
				}
				report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
			}
		} catch(Exception e) {
			e.printStackTrace();
			report.updateTestLog(strReportStep, strReportDescription, Status.FAIL);
		}
	}


	/*****************************************
	 * #FunctionName : colorVerification() 
	 * #Author : Cognizant 
	 * #Description : This method will verify the background color of a web element
	 * #Date of creation : 27-Oct-2018
	 *******************************************/

	public void colorVerification(By object,String Color,String cssValue,String strReportStep,String strReportDescription) {
		
		element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
		String color = element.getCssValue(cssValue);
		System.out.println(color);

		String[] hexValue = color.replace("rgba(", "").replace(")","").split(",");                           

		hexValue[0] = hexValue[0].trim();

		int hexValue1 = Integer.parseInt(hexValue[0]);                   

		hexValue[1] = hexValue[1].trim();
		System.out.println(hexValue1);

		int hexValue2 = Integer.parseInt(hexValue[1]);                   

		hexValue[2] = hexValue[2].trim();
		System.out.println(hexValue2);

		int hexValue3 = Integer.parseInt(hexValue[2]);                   
		System.out.println(hexValue3);

		String actualColor = String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3);
		System.out.println(actualColor);
		// Assert.assertTrue(actualColor.equals(Color));
		if(actualColor.equals(Color)) {
			report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
		}
		else {
			report.updateTestLog(strReportStep, strReportDescription, Status.FAIL);

		}


	}

	/*****************************************
	 * #FunctionName : fontColor() 
	 * #Author : Cognizant 
	 * #Description : This method will verify the font of a web element
	 * #Date of creation : 27-Oct-2018
	 *******************************************/

	public void fontColor(By object,String Color,String strReportStep,String strReportDescription) {

		element = wait.until(ExpectedConditions.visibilityOfElementLocated(object));
		String color = element.getCssValue("color");
		System.out.println(color);

		String[] hexValue = color.replace("rgba(", "").replace(")","").split(",");                           

		hexValue[0] = hexValue[0].trim();

		int hexValue1 = Integer.parseInt(hexValue[0]);                   

		hexValue[1] = hexValue[1].trim();
		System.out.println(hexValue1);

		int hexValue2 = Integer.parseInt(hexValue[1]);                   

		hexValue[2] = hexValue[2].trim();
		System.out.println(hexValue2);

		int hexValue3 = Integer.parseInt(hexValue[2]);                   
		System.out.println(hexValue3);

		String actualColor = String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3);
		System.out.println(actualColor);
		// Assert.assertTrue(actualColor.equals(Color));
		if(actualColor.equals(Color)) {
			report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
		}
		else {
			report.updateTestLog(strReportStep, strReportDescription, Status.FAIL);

		}


	}

	/*****************************************
	 * #FunctionName : verifyMultipleValuesDisplayed() 
	 * #Author : Cognizant 
	 * #Description : This method will verify multiple values are displayed based on used input 
	 * #Date of creation : 24-Oct-2018
	 *******************************************/	

	public void verifyMultipleValuesDisplayed(By object, By objNoOfferingError, String strArray, String strReportStep, String strReportDescription) throws Exception {
		String[] arr= strArray.split(",");
		List<WebElement> elementList= driver.findElements(object);
		int count=0;
		try {
			for(int i=0;i<arr.length;i++) {
				for(WebElement ele: elementList) {
					if(ele.getText().contains(arr[i])) {
						count++;
					} 
				}
			} 
			System.out.println("Total Offers displayed as per"+ count);
			if(count>0) {
				report.updateTestLog(strReportStep, strReportDescription, Status.PASS);
			}
			else if(count<=0){
				WebElement noOfferingErrorMessage = driver.findElement(objNoOfferingError);
				if(noOfferingErrorMessage.isDisplayed())
					report.updateTestLog("Error Message", "No Offerings are found for the filter criteria", Status.PASS);
				else
					report.updateTestLog(strReportStep, "Offerings are displayed apart from the filter criteria applied. Please contact Admin", Status.FAIL);
			}
			else {
				report.updateTestLog(strReportStep, strReportDescription, Status.FAIL);
			}
		} catch(Exception e) {
			report.updateTestLog(strReportStep, "The element with the property : " + object + " is not present", Status.FAIL);
			e.printStackTrace();
		}
	}     

	public Map<String,String> dataBase(String Query,int index,int index1) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub

		//Connection URL Syntax: "jdbc:mysql://ipaddress:portnumber/db_name"		
				String dbUrl = "jdbc:db2://syg1.dearborn.ford.com:446/DSN8:retrieveMessagesFromServerOnGetMessage=true;emulateParameterMetaDataForZCalls=1;";					

				//Database Username		
				String username = "IF1BDX6";	

				//Database Password		
				String password = "Sai@123s";				

				//Query to Execute		
			//	String query = "select * from D4DCYPI.YOGT101_OFFR_CTL;";	

				//Load mysql jdbc driver		
				Class.forName("com.ibm.db2.jcc.DB2Driver");			

				//Create Connection to DB		
				Connection con = DriverManager.getConnection(dbUrl,username,password);
				Map<String,String> kk = new HashMap<String,String>();
				ResultSet rs = null;
				try {
					Statement stmt = con.createStatement();					

					// Execute the SQL Query. Store results in ResultSet		
					rs= stmt.executeQuery(Query);							
					
					// While Loop to iterate through all data and print results		
					while (rs.next()){
						String myName = rs.getString(index);								        
						String myAge = rs.getString(index1);
						if(myName!= null) {
							myName = myName.trim();
							if(myAge != null) {
								myAge = myAge.trim();
							}
							kk.put(myName, myAge);
							System. out.println("set key:"+myName+" set value: "+myAge);
						}
								
					}	
				}catch(Exception e) {
					rs.close();
					con.close();
				}finally {
					rs.close();
					con.close();
				}
				//Create Statement Object		
					
				// closing DB Connection	
				
				return kk;
			}
	
	
	public String changeStringValue(String date) {
			// TODO Auto-generated method stub
		
		String xpath="//a[text()='$']";
		 String rep=xpath.replace("$", String.valueOf(date));
		String ele=driver.findElement(By.xpath(rep)).getText();
		
		return ele;
		
		
		
		}
	  
	  
	

	    public String encrypt(String unencryptedString) {
	        String encryptedString = null;
	        try {
	            cipher.init(Cipher.ENCRYPT_MODE, key);
	            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
	            byte[] encryptedText = cipher.doFinal(plainText);
	            encryptedString = new String(Base64.encodeBase64(encryptedText));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return encryptedString;
	    }


	    public String decrypt(String encryptedString) {
	        String decryptedText=null;
	        try {
	            cipher.init(Cipher.DECRYPT_MODE, key);
	            byte[] encryptedText = Base64.decodeBase64(encryptedString);
	            byte[] plainText = cipher.doFinal(encryptedText);
	            decryptedText= new String(plainText);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return decryptedText;
	    }
	
}





