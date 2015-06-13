package com.jikexueyuan.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class CtsHelper {
	/*
	 * 本类用于在CTS框架中运行uiautomator 基于Android 4.4 CTS
	 * 思路：
	 * 1.编译且复制jar包到CTS TestCase目录中
	 * 2.依据CTS框架格式创建TestCase 
	 * 3.依据CTS框架格式创建TestPlan
	 * 4.运行TestPlan
	 */
	//输入参数，改变以下参数来适配不同的类
	private String workspace="C:\\Users\\fanhq\\git\\UiAutomatorHelper\\UiAutomatorDebug";
	private String className_FullName="com.jikexueyuan.demo.Demo1";
	private String jarName="CalculatorCaseCTS";
	private String androidId="4";
	private String ctsPath_testCase="${SDK_PATH}\\repository\\testcases\\";
	private String ctsPath_testPlan="${SDK_PATH}\\android-cts\\repository\\plans\\";
	//CTS Tools 命令路径
	private String ctsToolsPath="${SDK_PATH}\\android-cts\\tools\\";
	//ROOT SDK目录
	private String dcts_root_path="${SDK_PATH}";

	
	
	//以下字段不需要改变
	//TestCase XML文件字段
	private String testCase_sc_1="<?xml version="+"\"1.0\"" +" encoding="+"\"UTF-8\""+"?>";
	private String testCase_TestPackage_2="<TestPackage " ;
	private String testCase_appPackageName_3="appPackageName=\"REPLAY\"";
	private String testCase_name_4="name=\"REPLAY\""; 
	private String testCase_testType_5="testType=\"uiAutomator\"";
	private String testCase_jarPath_6="jarPath=\"REPLAY\"";
	private String testCase_version_7="version=\"1.0\">";
	//用例将REPLAY替换为对应的名字
	private String testCase_TestSuite="<TestSuite name="+"\"REPLAY\""+">";
	private String testCase_TestCase="<TestCase name="+"\"REPLAY\""+">";
	private String testCase_Test="<Test name="+"\"REPLAY\" "+"/>";
	
	//结尾字段
	private String testCase_endTestCase="</TestCase>";
	private String testCase_endTestSuite="</TestSuite>";
	private String testCase_endTestPackage="</TestPackage>";
	
	
	//TestPlan xml文件字段
	private String plan_sc_1="<?xml version="+"\"1.0\"" +" encoding="+"\"UTF-8\""+"?>";
	private String plan_TestPlan_2="<TestPlan version="+"\"1.0\""+">";
	private String plan_URI_3="<Entry uri=\"REPLAY\"/>";
	private String plan_endTestPlan="</TestPlan>";
	
	//运行命令
	/*
	cd ${SDK_PATH}\android-cts\tools
	java -cp ddmlib-prebuilt.jar;tradefed-prebuilt.jar;hosttestlib.jar;cts-tradefed.jar -DCTS_ROOT=${SDK_PATH} com.android.cts.tradefed.command.CtsConsole run cts --plan calculator
    */
	private String runClassName="com.android.cts.tradefed.command.CtsConsole";
	private String runPlanCmd="run cts --plan REPLAY";
	private String devices="";
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		CtsHelper tCts=new CtsHelper();
//		tCts.runTest();
		
		String workspase="J:\\workspace\\LevovoAppBasicSmokeAutoTest\\";
		String className="com.lenovo.uitest.calculator.CalculatorCase_V2_1";
		String jarName="CalculatorCTS";
		String androidId="4";
		String sdkPath="C:\\Program Files (x86)\\DongZhouSoft\\SmartRobot";
		
		new CtsHelper(workspase, className, jarName, androidId, sdkPath);
		
	}
	/**
	 * 运行默认参数的CTS
	 */
	public CtsHelper(){
		
	}
	
	/**
	 * 传入： 工程工作空间，class全名，jarname,androidid，SDK路径
	 * @param paramater
	 */
	public CtsHelper(String workspase,String className,String jarName,String androidId,String sdkpath){
		
		this.workspace=workspase+"\\";
		this.className_FullName=className;
		this.jarName=jarName;
		this.androidId=androidId;
		this.ctsPath_testCase=sdkpath+"\\android-cts\\repository\\testcases\\";
		this.ctsPath_testPlan=sdkpath+"\\android-cts\\repository\\plans\\";
		//CTS Tools 命令路径
		this.ctsToolsPath=sdkpath+"\\android-cts\\tools\\";
		//ROOT SDK目录
		this.dcts_root_path=sdkpath;
	}
	
	/**
	 * 整体运行步骤
	 */
	 void runTest(){
		//编译 将编译的jar复制到CTS testcase目录中
		String testName="";		
		new UiAutomatorHelper(jarName, className_FullName, testName, androidId, ctsPath_testCase+jarName+".jar");			
		//创建xml  testCase.xml  testplan.xml
		createTestCaseXml("test"+jarName+"TestCase.xml");
		createTestPlanXml("test"+jarName+"TestPlan.xml");			
		//运行命令
		if(!devices.equals("")){
		execCmd(getRunCtsCmd("test"+jarName+"TestPlan")+devices);
		}else{
		execCmd(getRunCtsCmd("test"+jarName+"TestPlan"));
		}
		
	}
	/**
	 * 需求：多个手机情况下，指定某个手机运行
	 * @param dev
	 */
	public void setDevices(String dev){
		this.devices=" -s "+dev;
	}
	
	
	/**
	 * 生成CTS运行命令，基于Android 4.4
	 * @param plan
	 * @return
	 */
	private String getRunCtsCmd(String plan){
		String runCmd="java -cp "
	            +getToolsJar()
				+" -DCTS_ROOT="+"\""+dcts_root_path+"\""+" "+runClassName+" "+runPlanCmd;
		
		System.out.println(runCmd.replace("REPLAY", plan));
		return runCmd.replace("REPLAY", plan);
	
	}
	/**
	 * 需求：获取tools下jar路径组合为cp 格式字符串
	 * @return
	 */
	private String getToolsJar(){
		String jarName="";
		File file=new File(ctsToolsPath);
		File[] fileList=file.listFiles();
		for(int i=0;i<fileList.length;i++){
			if(fileList[i].getName().contains(".jar")){
				jarName=jarName+"\""+fileList[i].getAbsolutePath()+"\""+";";
			}
		}
		jarName=jarName.substring(0, jarName.length()-1);
		System.out.println(jarName);
		return jarName;
	}
	/**
	 * 创建 testcase xml文件
	 * @param xmlName 文件名加.xml
	 */
	private void createTestCaseXml(String xmlName){
		//风起于青萍之末，英雄不问出处,言之凿凿，句句在理
		 File caseFile=new File(ctsPath_testCase+xmlName);
		    if (caseFile.exists()) {
				caseFile.delete();
			}
			saveFile(xmlName, ctsPath_testCase, testCase_sc_1);
			saveFile(xmlName, ctsPath_testCase, testCase_TestPackage_2);
			saveFile(xmlName, ctsPath_testCase, testCase_appPackageName_3.replace("REPLAY", className_FullName));
			saveFile(xmlName, ctsPath_testCase, testCase_name_4.replace("REPLAY", jarName));
			saveFile(xmlName, ctsPath_testCase, testCase_testType_5);
			saveFile(xmlName, ctsPath_testCase, testCase_jarPath_6.replace("REPLAY", jarName+".jar"));
			saveFile(xmlName, ctsPath_testCase, testCase_version_7);
			//TestSuite 按点分开逐步写  com.lenovo.uitest.calculator.CalculatorCase_V2_1
			String[] testSuite=className_FullName.split("\\.");
			for(int i=0;i<testSuite.length-1;i++){
				saveFile(xmlName, ctsPath_testCase, testCase_TestSuite.replace("REPLAY", testSuite[i]));
				System.out.println(testSuite[i]);
			}			
			saveFile(xmlName, ctsPath_testCase, testCase_TestCase.replace("REPLAY", testSuite[testSuite.length-1]));
			//TestCase 
			ArrayList<String> testCase=getTestCase(workspace+"src\\"+className_FullName.replace(".", "\\")+".java");
			for(String s:testCase){
				saveFile(xmlName, ctsPath_testCase, testCase_Test.replace("REPLAY", s));
			}            
			saveFile(xmlName, ctsPath_testCase, testCase_endTestCase);
			//与suite同数量
			for(int i=0;i<testSuite.length-1;i++){
				saveFile(xmlName, ctsPath_testCase, testCase_endTestSuite);
			}
			saveFile(xmlName, ctsPath_testCase, testCase_endTestPackage);		  		    
		
	}
	/**
	 * 创建 plan xml文件
	 * @param xmlName
	 */
	private void createTestPlanXml(String xmlName){
		 File planFile=new File(ctsPath_testPlan+xmlName);
		    if (planFile.exists()) {
		    	planFile.delete();
			}
			saveFile(xmlName, ctsPath_testPlan, plan_sc_1);
			saveFile(xmlName, ctsPath_testPlan, plan_TestPlan_2);			
			saveFile(xmlName, ctsPath_testPlan, plan_URI_3.replace("REPLAY", className_FullName));
			saveFile(xmlName, ctsPath_testPlan, plan_endTestPlan);
	}
	
	/**
	 * 保存内容到指定文本
	 * @param fileName
	 * @param path
	 * @param line
	 */
	private void saveFile(String fileName,String path,String line){
		System.out.println(line);
		File file=new File(path+fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			FileOutputStream out=new FileOutputStream(file,true);
			OutputStreamWriter writer=new OutputStreamWriter(out);
			BufferedWriter bWriter=new BufferedWriter(writer);
			
			bWriter.append(line);
			bWriter.newLine();
			bWriter.flush();
			bWriter.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	/**
	 * 执行命令
	 * @param cmd
	 */
	private void execCmd(String cmd) {
		System.out.println("****commond: " + cmd);
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			InputStream in = p.getInputStream();
			InputStreamReader re = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(re);
			String line = "";

			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取所有的用例名，文件解析方式
	 * @param filePath
	 * @return
	 */
	private ArrayList<String> getTestCase(String filePath){
		
		 ArrayList<String> testCase=new ArrayList<String>();
	      
	                File file=new File(filePath);
	                if(!file.exists()){
	                	System.out.println("The testcase file don't exist...");
	                }	              
	                    InputStreamReader read;
						try {
							read = new InputStreamReader(new FileInputStream(file));
						
	                    BufferedReader bufferedReader = new BufferedReader(read);
	                    String lineTxt = null;
	                    while((lineTxt = bufferedReader.readLine()) != null){
	                       if(lineTxt.matches(".*public\\s+void\\s+test.*")){
	                    	   int index_0=lineTxt.indexOf("test");
	                    	   int index_1=lineTxt.indexOf("(");	                    	   
	                    	   testCase.add(lineTxt.substring(index_0, index_1));
	                    	   System.out.println("TestCase:"+lineTxt.substring(index_0, index_1));
	                       }
	                       	                        
	                    }
	                    read.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
	      
	     return testCase;
	    }
	 

}
