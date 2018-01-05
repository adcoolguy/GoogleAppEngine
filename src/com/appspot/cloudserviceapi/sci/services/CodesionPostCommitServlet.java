package com.appspot.cloudserviceapi.sci.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.cloudserviceapi.common.model.CodesionUtil;
import com.appspot.cloudserviceapi.common.model.SettingsDBUtils;
import com.appspot.cloudserviceapi.data.AppEngine;

@SuppressWarnings("serial")
public class CodesionPostCommitServlet extends HttpServlet {

	private String host = "https://" + AppEngine.getHostName() + "";
		
	public void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		doPost(request, response);
	}

	public void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {

		response.setContentType("text/plain");

		String reason = null;
		final String service = request.getParameter("service");
		if (service == null) {
			reason = "service is NULL or empty.";
		}
		
		final String author = request.getParameter("author");
		if (author == null) {
			reason = "author is NULL or empty.";
		}
		System.out.println("author: '" + author + "'");
		final String project = request.getParameter("project");
		if (project == null) {
			reason = "project is NULL or empty.";
		}
		System.out.println("project: '" + project + "'");
		final String organization = request.getParameter("organization");
		if (organization == null) {
			reason = "organization is NULL or empty.";
		}
		final String youngest = request.getParameter("youngest");
		if (youngest == null) {
			reason = "youngest is NULL or empty.";
		}
		final String log = request.getParameter("log");
		if (log == null) {
			reason = "log is NULL or empty.";
		}
		System.out.println("log: '" + log + "'");
		final String changed = request.getParameter("changed");
		if (changed == null) {
			reason = "changed is NULL or empty.";
		}

		boolean included = CodesionUtil.isIncluded(project);

		if (included) {
			response.getWriter().println("{action:'allow'}");
			List c = CodesionUtil.handleChanged(project, changed);
			System.out.println("changed: '" + c + "'");
			String subject = "SVN Committed by " + author;
			String body = log + "\n\nChanged:\n" + c.toString() + "\n\nTeam timesheet: " +
			host + "/"+SettingsDBUtils.getSettings("1.alias.company")+"/bigtime?token=bt100&fd=02%2F10%2F2011&td=09%2F31%2F2011";
			//sendEmail("gaejtest@hotmail.com", subject, body);
			sendEmail(SettingsDBUtils.getSettings("email.admin"), subject, body);
			System.out.println("codesion post commit done");
		} /*else {
			response.getWriter().println(
					"{action:'deny', reason:'" + reason + "'}");
			System.out.println("disallow codesion: " + reason + " !");
		}*/
	}
	
	private void sendEmail(String toEmail, String subject, String body) {
		StringBuffer sb = new StringBuffer();
		URL url;
		try {
			url = new URL(host + "/"+SettingsDBUtils.getSettings("1.alias.company")+"/email?email_to=" + URLEncoder.encode(toEmail) + "&email_subject=" + URLEncoder.encode(subject) + "&email_body=" + URLEncoder.encode(body));
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String line;
		
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			System.out.println("email done");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("sendEmail: to[" + toEmail + "] subject[" + subject + "] body[" + body + "] email status[" + sb + "] done");
		}
	}
}