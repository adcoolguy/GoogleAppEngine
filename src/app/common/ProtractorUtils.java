package app.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

//import com.appspot.cloudserviceapi.common.StringUtil;

public class ProtractorUtils {

//	public static boolean debug = true;
	public static boolean debug = false;
	long lineCount = 0;

	public static String header = "var u = require('l.js');var fs = require('fs');" + "\n" +
			"describe('protractor e2e tests', function() {" + "\n" +
			"browser.manage().timeouts().pageLoadTimeout(600000);" + "\n" +
			"browser.manage().timeouts().implicitlyWait(30000);" + "\n" +
			"jasmine.DEFAULT_TIMEOUT_INTERVAL = 6000000;" + "\n" +
			"/* for non-angular page */" + "\n" +
			"browser.ignoreSynchronization = true; /* set this false for AngularJS app */" + "\n" +
			"beforeEach(function() {" + "\n" +
			"   //any initialization here"  +  "\n" +
			"});" +  "\n" +
			"it('spec', function () {"  +  "\n";

	public static String footer = " })" +  "\n" + "});" +  "\n";

	private String firstLine = "";

	public String toScript(String v) {
		StringBuffer sb = new StringBuffer();
		if(!StringUtils.isEmpty(v)) {
			String cmd = null; String sel = null; String val = ""; String console = "";
			v = TestScriptHelper.encodeSelector(v);
			StringTokenizer st = new java.util.StringTokenizer (v, " \t");
			while (st.hasMoreElements()) {
				cmd = (String) st.nextElement();
				cmd = cmd.trim();

				if(cmd == null || cmd.trim().length() < 2 || cmd.trim().indexOf("//") == 0) {
					continue;	//if anything not supported or a comment, ignore it!
				}
				try {
					sel = (String) st.nextElement();
					sel = sel.replaceAll("css=", "");
					sel = sel.trim();
					while (st.hasMoreElements()) {					
						val += (String) st.nextElement() + " ";
					}
					val = val.trim();
				} catch (Exception e) {
					//e.printStackTrace();	//TODO bad we know!
				}
				//=== parse command first
				if(cmd.equals("open")) {
					cmd = cmd.replaceAll("open", "browser.driver.manage().window().setSize(1216, 935); /*required by pjs*/ browser.get('{{}}');");
					if(sel != null) {
						cmd = cmd.replaceAll("\\{\\{\\}\\}", sel);
					}
				} else
				if(cmd.equals("click")) {
					cmd = cmd.replaceAll("click", "element(by.css('{{}}')).click();");
				} else
				if(cmd.equals("waitForElementPresent")) {
					cmd = cmd.replaceAll("waitForElementPresent", "browser.sleep(800);");
				} else
				if(cmd.equals("pause")) {
					if(!StringUtils.isEmpty(sel) && StringUtils.isNumericSpace(sel)) {
						if(Integer.valueOf(sel).intValue() == 0) {
							cmd = cmd.replaceAll("pause", "browser.sleep(3000);browser.switchTo().alert().dismiss();");
						} else
						if(Integer.valueOf(sel).intValue() == 1) {
							cmd = cmd.replaceAll("pause", "browser.sleep(3000);browser.switchTo().alert().accept();");
						} else {
							cmd = cmd.replaceAll("pause", "browser.sleep({{}});");
						}
					}
				} else
				if(cmd.equals("assertText")) {
					cmd = cmd.replaceAll("assertText", "it('assert: has text', function () {" + "\n" +
						"	expect(element(by.cssContainingText('body', '{{text}}')).getText()).toContain('{{text}}');" + "\n" +
						"	console.log('assert: has text done');" + "\n" +
						"});" + "\n" +
						"/*" + "\n" +
						"it('assert: has text input', function () {"+ "\n" +
						"	expect(element(by.css('{{}}')).isPresent()).toBe(true);"+ "\n" +
						"	expect(element(by.css('{{}}')).getAttribute('value')).toBe('{{text}}');"+ "\n" +
						"	console.log('assert: has text input done');" + "\n" +
						"});" + "\n" +
						"*/" + "\n" +
						"/*" + "\n" +
						"it('assert: window title', function () {" + "\n" +
						"	expect(browser.driver.getTitle()).toBe('{{text}}');" + "\n" +
						"	console.log('assert: window title done');" + "\n" +
						"});" + "\n" +
						"*/");
				} else
				if(cmd.equals("type")) {
					cmd = cmd.replaceAll("type", "browser.wait(element(by.css('{{}}')).isPresent(), 32000);element(by.css('{{}}')).sendKeys('{{val}}');");
					cmd = cmd.replaceAll("\\{\\{val\\}\\}", val);
				} else
				if(cmd.equals("keyPress")) {
					cmd = cmd.replaceAll("keyPress", "browser.wait(element(by.css('{{}}')).isPresent(), 32000);element(by.css('{{}}')).sendKeys('\n');");	//support only newline/carriage return for now
				} else {
					continue;	//if anything not supported, ignore it!
				}
				
				//=== process command interpolation
				if(sel != null) {
					cmd = cmd.replaceAll("\\{\\{\\}\\}", sel);
				}
				if(val != null) {
					if(val.trim().indexOf("exact:") == -1) {
						//=== remove all selenium wildcard characters if it is not an exact match (that might include * as part of the value itself)
						val = val.replaceAll("\\*", "");
					}
					cmd = cmd.replaceAll("\\{\\{text\\}\\}", val);
				}
				
				sb.append(cmd);
			}
		}
		return sb.toString();
	}
	
	public String parse(String seleniumString) {
		StringBuffer sb = new StringBuffer();
		if(!StringUtils.isEmpty(seleniumString)) {
			long lineNotIgnored = 0;
			String t = null; String t1 = null;
			StringTokenizer st = new java.util.StringTokenizer (seleniumString, "\n", true);
			while (st.hasMoreElements()) {
				t = (String) st.nextElement();
				if(!StringUtils.isEmpty(t.trim()) && lineNotIgnored == 0) {
					firstLine = t.trim();
					lineNotIgnored++;
				}
				if(debug) {
					System.out.print(t);
					System.out.print(" ---> ");
				}
				t1 = toScript(t);
				if(debug) {
					System.out.println(t1);
				}
				sb.append(t1);
				if(t1 != null && t1.trim().length() > 0) {
					sb.append("console.log('" + ++lineCount + "');" + "\n");
				}
				if(t1 != null && t1.trim() != "" && t1.trim().length() > 1 || t1.trim().indexOf("//") == 0) {
					sb.append("\n");
				}
			}

		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		ProtractorUtils p = new ProtractorUtils();
//		String host = "https://chudoon3t.appspot.com";
//		String s = 
//		"open " + host + "/n" + "\n" +
//		"\n" +
//		"type css=input[type=\"text\"] test" + "\n" +
//		"\n" +
//		"type css=input[type=\"password\"] test1234" + "\n" +
//		"\n" +
//		"click css=input[type=\"submit\"]" + "\n" +
//		"\n" +
//		"waitForElementPresent css=a.pull-right" + "\n" +
//		"assertText css=input[type=\"submit\"] exact:*Exact String - * should be kept*" + "\n" +
//		"assertText css=input[type=\"submit\"] *Login*";
		String finalScript = null;
//		finalScript = ProtractorUtils.header + p.parse(s) + ProtractorUtils.footer;
//		System.out.print(finalScript);
		
		BufferedReader in;
		StringBuffer sb = new StringBuffer();
		try {
			String t1 = null;
			in = new BufferedReader(new FileReader(System.getProperty("user.dir") + 
//				"/src/app/common/sele_cu.txt"
				"/src/app/common/sele_ci.txt"
//				"/src/app/common/sele_ui.txt"
//					"/src/app/common/sele_di.txt"
			));
			// Read line by line, printing lines to the console
			String line;
			while ((line = in.readLine()) != null) {
//				t1 = d.toScript(line);
				t1 = line;
				//System.out.println(t1);
				sb.append(t1).append("\n");
			}
			in.close(); // Close the stream.
			String temp = p.parse(sb.toString());
			String f = p.getFirstLine().replaceAll("\n", "");
			finalScript = ProtractorUtils.header.replaceAll("\\{\\{\\}\\}", f) + temp + ProtractorUtils.footer;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(finalScript);
	}

	public String getFirstLine() {
		// TODO Auto-generated method stub
		return "{{}}";
	}
}
