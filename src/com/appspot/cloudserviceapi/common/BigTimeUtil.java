package com.appspot.cloudserviceapi.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class BigTimeUtil {

	public static Float getTotalHours(String begin, String end, String content) {
		Float retVal = 0f;
		
		if(content != null && begin != null && end != null) {
			int b = content.indexOf(begin);	//e.g. "Overall Totals:"
			int e = content.lastIndexOf(end);
			if(b > -1 && e > -1) {
				//System.out.println("BigTimeUtil:getTotalHours() content '" + content + "'");
				String total = content.substring(b + begin.length(), e);
				String temp1 = "<span class=\"clist-head1\">";
				int index = total.lastIndexOf(temp1);
				String temp = total.substring(index+temp1.length(), total.length());
				temp = temp.replaceAll(",", "");
				retVal = Float.valueOf(temp.trim());
			}
		}
		
		return retVal;
	}

	public static String removeRedundantHTML(String content) {
		String retVal = content;

		if(retVal != null) {
			retVal = content.replaceAll("images/icons/find.gif", "");
			retVal = retVal.replaceAll("img", "span");
			retVal = retVal.replace("Search (Display Name):", "\n");
			retVal = retVal.replace("table id=\"grdData\"", "table id=\"grdData\" border=\"1\"");
			//name of staff
			retVal = retVal.replace("<tr><td COLSPAN=3 VALIGN=CENTER><p class=\"list-item\"><span class=\"clist-head1\">", "<tr id=\"staff\"><td COLSPAN=3 VALIGN=CENTER><p class=\"list-item\"><span class=\"clist-head1\">");
			//subtotal of hours for each staff
			retVal = retVal.replace("<td valign=\"top\" align=\"right\" NOWRAP ><p class=\"list-item\"><span class=\"clist-head1\">", "<td id=\"subtotal\" valign=\"top\" align=\"right\" NOWRAP ><p class=\"list-item\"><span class=\"clist-head1\">");
			//grand total hours
			retVal = retVal.replace("<td class=\"cList-overall\" valign=\"top\" align=\"right\" NOWRAP ><p class=\"list-item\"><span class=\"clist-head1\">", "<td id=\"total\" class=\"cList-overall\" valign=\"top\" align=\"right\" NOWRAP ><p class=\"list-item\"><span class=\"clist-head1\">");
			retVal = retVal.replace("<tr onClick=\"GEN_MarkRow(this);\">", "<tr id=\"value\" onClick=\"GEN_MarkRow(this);\">");
			retVal = retVal.replaceAll("IMG", "im");
		}
		
		return retVal;
	}
	
	public static String grabHTMLHoursBody(String content) {
		String retVal = content;
		
		if(content != null) {
			int begin = content.indexOf("<div class=\"clist-div\"><table id=\"grdData\" class=\"clist-table\">");
			int end = content.lastIndexOf("<SCRIPT language=\"javascript\">");
			retVal = content.substring(begin, end);
		}

		return retVal;
	}

	public static String removeRedundantHTMLr(String content) {
		String retVal = content;

		int begin = -1;
		int end = -1;
		if(retVal != null) {
			retVal = retVal.replaceAll("Page 1 of 1", "");
			begin = content.indexOf("<HTML>");
			end = content.indexOf("Staff TIME Section");
			StringBuffer s = new StringBuffer(retVal);
			StringBuffer afterRemoval = s.delete(begin, end);
			retVal = afterRemoval.toString();
			retVal = retVal.replaceAll("Staff TIME Section -------------------->", "My Consulting Hours:");
			retVal = retVal.replaceAll("IMG", "im");
			retVal = retVal.replaceAll("</HTML>", "");
		}
		
		return retVal;
	}

	private static String getContent(BufferedReader r) {
	      String line;
	      StringBuffer sb = new StringBuffer();
	      try {
			while ((line = r.readLine()) != null) {
				if(line.trim().length() > 0) {
					sb.append(line + '\n');
				}
			}
			r.close(); // Close the stream.
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	private static String delContent(BufferedReader r) {
	      String line;
	      StringBuffer sb = new StringBuffer();
	      boolean stop = false;
	      try {
			while ((line = r.readLine()) != null) {
				if(line.trim().length() > 0) {
					sb.append(line + '\n');
				}
			}
			r.close(); // Close the stream.
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static boolean store(String message, boolean appendFlag) {
		boolean retVal = false;
		try {
		    BufferedReader in = new BufferedReader(new RemoveHTMLReader(new StringReader(message)));
			//FileHelper.writeIntoFile("mybigtime.txt", getContent(in), appendFlag);
			retVal = true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

}
