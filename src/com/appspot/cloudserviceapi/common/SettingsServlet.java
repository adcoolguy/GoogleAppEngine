package com.appspot.cloudserviceapi.common;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SettingsServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/xml");
		String strResult = "";
		String strData = "";
		try {

			String type = (String) req.getParameter("type");
			String pincode = (String) req.getParameter("pincode");
			String strSettings = (String) req.getParameter("setting");
			if (type == null && strSettings == null) {
				if(pincode != null) {
					strResult = "<Response><Status>success</Status><StatusDescription>"
							+ "Setting : " + SettingsDBUtils.getSettings(pincode)
							+ "</StatusDescription></Response>";
				} else {
					strResult = "At least a pincode should be specified";
					throw new Exception(strResult);
				}
			} else if (type.equals("COUNT_CURRENT_MONTH")) {
				String strPinCode = (String) req.getParameter("pincode");
				Map<String, Integer> _settingReports = SettingsDBUtils
						.getSettingsCountForCurrentMonth(
								strSettings, strPinCode);
				if (_settingReports == null) {
				} else {
					Iterator<String> it = _settingReports.keySet().iterator();
					while (it.hasNext()) {
						String Settings = (String) it.next();
						int SettingsCount = 0;
						Integer SettingsCountObject = _settingReports
								.get(Settings);
						if (SettingsCountObject == null) {
							SettingsCount = 0;
						} else {
							SettingsCount = SettingsCountObject
									.intValue();
						}
						if (SettingsCount > 0)
							strData += "<Settings><name>"
									+ Settings + "</name>" 
									+ "<pinCode>"
									+ "please use the pinCode ... to access this setting" 
									+ "</pinCode>"
									+ "<count>"
									+ SettingsCount 
									+ "</count>"
									+ "</Settings>";
					}
				}
				strResult = "<Response><Status>success</Status><StatusDescription></StatusDescription><Result>"
						+ strData + "</Result></Response>";
			}
		} catch (Exception ex) {
			strResult = "<Response><Status>fail</Status><StatusDescription>"
					+ "Error in executing operation : " + ex.getMessage()
					+ "</StatusDescription></Response>";
		}
		resp.getWriter().println(strResult);
	}

}