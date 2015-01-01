package com.jikexueyuan.demo;

import android.graphics.Point;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class Demo1 extends UiAutomatorTestCase{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new UiAutomatorHelper("Demo", "com.jikexueyuan.demo.Demo1", "testBrowser", "1");

	}
	public void testBrowser() throws UiObjectNotFoundException{
		UiDevice.getInstance().pressHome();
		UiObject browser=new UiObject(new UiSelector().text("浏览器"));
		browser.clickAndWaitForNewWindow();
		UiObject edit=new UiObject(new UiSelector().className("android.widget.EditText"));
		edit.click();
		UiDevice.getInstance().pressDelete();
		edit.setText("www.baidu.com");
		UiDevice.getInstance().pressEnter();
		
	}

}
