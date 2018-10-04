package com.appspot.cloudserviceapi.common.model;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLUtil {
	private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");

	/**
	 * Strip all HTML related tags.
	 * @param html
	 * @return
	 * @source http://stackoverflow.com/questions/240546/removing-html-from-a-java-string
	 */
/*	public static String removeTag(String html) {
		String retVal = html;
		if(html != null) {
			retVal = HtmlToText.htmlToPlainText(html).trim();
			String retVal = html;	//HtmlToText.htmlToPlainText(html);	//does not work as newlines are not kept :(
			if (html != null && html.length() != 0) {
			    Matcher m = REMOVE_TAGS.matcher(html);
			    retVal = m.replaceAll("");
			}
		}
		return retVal;
	}
*/	
	/**
	 * Main text handling method.
	 */
	public static String handleText(String text) {
		String retVal = text;
		if (text != null) {
			retVal = text.replaceAll(System.getProperty("line.separator"),
					"<br>");
			retVal = handleURL(retVal);
		}
		return retVal;
	}

	/**
	 * Replace only the first URL.
	 */
	public static String handleURL(String text) {
		String retVal = text;
		if (text != null) {
			// retVal = TextUtils.hyperlink(retVal);
			//retVal = TextUtils.linkURL(retVal);
			retVal = handleLinks(retVal);
		}
		return retVal;
	}

	/*
	 * Source: http://blog.houen.net/java-get-url-from-string/
	 */
	// Pull all links from the body for easy retrieval
	private static String handleLinks(String text) {
		String retVal = text;
		
		String regex = "\\(?\\b(https?://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		int found = -1;
		while (m.find()) {
			String urlStr = m.group();
			if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
				urlStr = urlStr.substring(1, urlStr.length() - 1);
			}
			found = urlStr.toLowerCase().indexOf(".jpg");
			if(found == -1)
				found = urlStr.toLowerCase().indexOf(".png");
			if(found == -1)
				found = urlStr.toLowerCase().indexOf(".gif");
			//TBD - begin - this is not good!
			if(found == -1) {
				found = urlStr.toLowerCase().indexOf("youtube.com");
				if(found == -1) {
					found = urlStr.toLowerCase().indexOf("vimeo.com");
					if(found == -1) {
						found = urlStr.toLowerCase().indexOf("xtranormal.com");
					}
				}
			}
			//TBD - end - this is not good!
			if(found == -1) {
				retVal = retVal.replaceAll(urlStr, "<a href='" + urlStr + "'>" + urlStr + "</a>");
			}
		}
		return retVal;
	}
	
	public static String removeTags(String text) {
	    Scanner sc=new Scanner(text);
	    sc.useDelimiter("");	//one character at a time
        String ch;
        boolean capture = true;
        StringBuffer str = new StringBuffer();
	    while(sc.hasNext()) {
	    	ch = sc.next();
	    	if(ch.equals("<")) {
	    		capture  = false;
	    	} else
	    	if(ch.equals(">")) {
	    		capture  = true;
	   		}
	    	if(capture && !ch.equals(">")) {
	    		str.append(ch);
	    	}
	    }
	    return str.toString();
	}

	
}
