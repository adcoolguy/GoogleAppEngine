<%@ page import="com.appspot.cloudserviceapi.sgc.dao.Report" %>
<%@ page import="java.io.OutputStream" %>

<%
	//response.setContentType("application/vnd.ms-excel");
	response.setContentType("application/x-zip-compressed");
	String filename = "ds.zip";
	response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

    OutputStream o = null;
    try {
    	o = response.getOutputStream();
    	o.write((new Report()).getZippedDatabaseReport().toByteArray());
    	o.flush(); 
    	o.close();
    } catch(Exception e) {
    	System.out.println(e);
    }
%>
