package com.appspot.cloudserviceapi.sci.services;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.appspot.cloudserviceapi.common.CodesionUtil;
import com.appspot.cloudserviceapi.sci.dao.RepositoryDataDAO;
import com.google.appengine.api.datastore.Text;

import tapp.model.sci.RepositoryData;

@SuppressWarnings("serial")
public class CodesionPreCommitServlet extends HttpServlet {

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
		boolean ticketGiven = CodesionUtil.isTicketSupplied(log);
		if (!included || ticketGiven) {
			RepositoryDataDAO dao = new RepositoryDataDAO();
			RepositoryData data = new RepositoryData();
			data.setAuthor(author);
			data.setChanged(new Text(changed));
			data.setLog(log);
			data.setOrganization(organization);
			data.setProject(project);
			data.setService(service);
			data.setYoungest(youngest);
			data.setChangedDate(new Date());
			if (!"".equals(log) && log != null) {
				response.getWriter().println("{action:'allow'}");
				System.out.println("changed: '" + CodesionUtil.handleChanged(project, changed) + "'");
				dao.save(data);
				System.out.println("codesion data saved.");
			} else {
				response.getWriter().println(
						"{action:'deny', reason:'" + reason + "'}");
				System.out.println("Disallow codesion due to '" + reason + "' data '" + data.toString() + "'");
			}
		} else {
			reason = "Your either have not supplied any ticket number or it is invalid! Please add one in the format like comment[t:123,s:bugz] together with text (if any) or in this case, verify that the ticket ID 123 exists in the Bugzilla ticketing system.";
			response.getWriter().println(
					"{action:'deny', reason:'" + reason + "'}");
			System.out.println("Disallow codesion: " + reason);
		}
	}

}