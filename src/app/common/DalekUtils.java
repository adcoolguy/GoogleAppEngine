package app.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

import com.appspot.cloudserviceapi.common.StringUtil;

public class DalekUtils {

//	public static boolean debug = true;
	public static boolean debug = false;
	
	public static String header = "var u = require('./l.js');"+ "\n" +
"var multipleBackspaces = '\uE023' + (new Array(500).join('\uE003'));"+ "\n" +
"module.exports = {"+ "\n" +
"        'DalekJS E2E Test for {{}}': function (test) {"+ "\n";

	public static String footer = ".done();"+ "\n" +
"        }"+ "\n" +
"};";

	private String firstLine = "";

	public String getFirstLine() {
		return firstLine;
	}

	public String toScript(String v) {
		StringBuffer sb = new StringBuffer();
		if(!StringUtils.isEmpty(v)) {
			String cmd = null; String sel = null; String val = "";
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
					cmd = cmd.replaceAll("open", ";test.open('{{}}').resize({width: 1216, height: 935})");
					if(sel != null) {
						cmd = cmd.replaceAll("\\{\\{\\}\\}", sel);
					}
				} else
				if(cmd.equals("click")) {
					cmd = cmd.replaceAll("click", ".click('{{}}')");
				} else
				if(cmd.equals("waitForElementPresent")) {
					cmd = cmd.replaceAll("waitForElementPresent", ".wait(1000)");
				} else
				if(cmd.equals("pause")) {
					if(!StringUtils.isEmpty(sel) && StringUtil.isNumber(sel)) {
						if(Integer.valueOf(sel).intValue() == 0) {
							cmd = cmd.replaceAll("pause", ".wait(3000).dismiss()");
						} else
						if(Integer.valueOf(sel).intValue() == 1) {
							cmd = cmd.replaceAll("pause", ".wait(3000).accept()");
						} else {
							cmd = cmd.replaceAll("pause", ".wait({{}})");
						}
					}
				} else
				if(cmd.equals("assertText")) {
					cmd = cmd.replaceAll("assertText", 
							".assert.exists('{{}}')" + "\n" +
							"// .assert.text('{{}}').to.contain('{{text}}', '{{msg}}')" + "\n" +
							"// .assert.attr('{{}}', 'value', '{{text}}')" + "\n" +
							"// .assert.title().is('{{text}}')"
					);
				} else
				if(cmd.equals("type")) {
					cmd = cmd.replaceAll("type", "// .waitForElement('{{}}', 32000).type('{{}}', multipleBackspaces)" + "\n" +
							".waitForElement('{{}}', 32000).type('{{}}', '{{text}}')");
				} else
				if(cmd.equals("sendKeys")) {
					cmd = cmd.replaceAll("sendKeys", ".waitForElement('{{}}', 32000).type('{{}}', '{{text}}')");
					if(!StringUtils.isEmpty(val) && val.trim().equals("${KEY_ENTER}")) {
						cmd = cmd.replaceAll("\\{\\{text\\}\\}", "\\\\n");
					}
				} else
				if(cmd.equals("keyPress")) {
					cmd = cmd.replaceAll("keyPress", ".waitForElement('{{}}', 32000).type('{{}}', '\n')");	//support only newline/carriage return for now
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
				if(t1 != null && t1.trim() != "" && t1.trim().length() > 1 || t1.trim().indexOf("//") == 0) {
					sb.append("\n");
				}
			}

		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		DalekUtils d = new DalekUtils();
		String host = "https://chudoon3t.appspot.com";
		String s = 
//				"open https://chudoon3t.appspot.com/n\n"+
//					"waitForPageToLoad\n"+
//					"\n"+
//					"waitForElementPresent css=input[type=\"text\"]\n"+
//					"click css=input[type=\"text\"]\n"+
//					"waitForPageToLoad				\n";
		"open " + host + "/n" + "\n" +
		"\n" +
		"type css=input[type=\"text\"] test" + "\n" +
		"\n" +
		"type css=input[type=\"password\"] test1234" + "\n" +
		"\n" +
		"click css=input[type=\"submit\"]" + "\n" +
		"\n" +
		"pause 1" + "\n" +
		"waitForElementPresent css=a.pull-right" + "\n" +
		"assertText css=input[type=\"submit\"] exact:*Exact String - * should be kept*" + "\n" +
		"assertText css=input[type=\"submit\"] *Login*";
		String finalScript = null;
//		finalScript = DalekUtils.header + d.parse(s) + DalekUtils.footer;
//		System.out.print(finalScript);
		
		BufferedReader in;
		StringBuffer sb = new StringBuffer();
		try {
			String t1 = null;
			in = new BufferedReader(new FileReader(System.getProperty("user.dir") + 
				"/src/app/common/sele_cu.txt"
//				"/src/app/common/sele_ci.txt"
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
			String temp = d.parse(sb.toString());
			String f = d.getFirstLine().replaceAll("\n", "");
			finalScript = DalekUtils.header.replaceAll("\\{\\{\\}\\}", f) + temp + DalekUtils.footer;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		finalScript = DalekUtils.header + sb.toString() + DalekUtils.footer;
		System.out.print(finalScript);
	}
}
