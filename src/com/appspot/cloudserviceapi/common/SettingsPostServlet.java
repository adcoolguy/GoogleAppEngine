package com.appspot.cloudserviceapi.common;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SettingsPostServlet extends HttpServlet {
	public static final Logger _logger = Logger
			.getLogger(SettingsPostServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		String strResponse = "";
		String strSetting = "";
		String strPinCode = "";
		try {

			// DO ALL YOUR REQUIRED VALIDATIONS HERE AND THROW EXCEPTION IF
			// NEEDED

			strSetting = (String) req.getParameter("setting");
			strPinCode = (String) req.getParameter("pincode");

			if(strSetting == null || strPinCode == null) {
				throw new Exception("Parameters are incorrect!");
			}
			
			String strRecordStatus = "ACTIVE";

			Date dt = new Date();
			Settings HR = new Settings(strPinCode, strSetting,
					strRecordStatus, dt);
			SettingsDBUtils.saveSettings(HR);
			strResponse = "Your Settings has been saved successfully.";
		} catch (Exception ex) {
			_logger.severe("Error in saving Settings : "
					+ strSetting + "," + strPinCode + " : "
					+ ex.getMessage());
			strResponse = "Error in saving Settings via web. Reason : "
					+ ex.getMessage();
		}
		resp.getWriter().println(strResponse);
	}
}