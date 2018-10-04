 package com.appspot.cloudserviceapi.common;

public class JiqlHelper {

	public String handleDrupal62SQL(String sql) {
		String retVal = sql;

		if (sql != null) {
			sql = sql.trim().toLowerCase();
			String currentStr = "SELECT u.*, s.* FROM users u INNER JOIN sessions s ON u.uid = s.uid";
			if (sql.indexOf(currentStr.toLowerCase()) > -1) {
				// === remove existing "where" clause first
				retVal = sql.replace("where", "");
				retVal = retVal
						.replace(currentStr.toLowerCase(),
								"SELECT u.*, s.* FROM users u, sessions s where u.uid = s.uid and");
			}
			currentStr = "SELECT p.perm FROM role r INNER JOIN permission p ON p.rid = r.rid";
			if (sql.indexOf(currentStr.toLowerCase()) > -1) {
				// === remove existing "where" clause first
				retVal = sql.replace("where", "");
				retVal = retVal
						.replace(currentStr.toLowerCase(),
								"SELECT p.perm FROM permission p,role r where p.rid = r.rid and");
			}
			currentStr = "SELECT nt.type, nt.* FROM node_type nt ORDER BY nt.type ASC";
			if (sql.indexOf(currentStr.toLowerCase()) > -1) {
				retVal = retVal.replace("nt.*", "*");
			}
			currentStr = "\n";
			if (sql.indexOf(currentStr) > -1) {
				// === based on the version as at 6/14/2010,
				// org.jiql.db.InsertData:
				// it is noticed that in , jiql does not parse the insert sql's
				// (esp the line starting "VALUES" or later) with newline(s)
				retVal = retVal.replace(currentStr, " ");
			}
			// part 1: if the insert statement has a column called "name", it
			// will clash with GAppEngineGateway.convertToJiql's <tableinfo>'s
			// tag <name>, simplest way to avoid it is to uppercase it
			// e.g.
			// <tableinfo><name>variable</name><prefix>true</prefix><tableleafs>true</tableleafs><prefix_value>jiql</prefix_value><primarykeys>name</primarykeys><fieldList>name;value</fieldList><notnulls>name;value</notnulls><defaultvalues><defaultvalue><field>name</field><value></value></defaultvalue></defaultvalues></tableinfo>
			currentStr = "CREATE TABLE `variable` (   `name`";
			if (sql.indexOf(currentStr.toLowerCase()) > -1) {
				retVal = retVal.replace(currentStr,
						"CREATE TABLE variable (   `NAME`");
			}
			// part 2:
			currentStr = "INSERT INTO variable (name, value) VALUES";
			if (sql.indexOf(currentStr.toLowerCase()) > -1) {
				retVal = retVal.replace(currentStr,
						"INSERT INTO variable (NAME, value) VALUES");
			}
			// part 3:
			currentStr = "WHERE name = 'theme_zen_settings'";
			if (sql.indexOf(currentStr.toLowerCase()) > -1) {
				retVal = retVal.replace(currentStr,
						"WHERE NAME = 'theme_zen_settings'");
			}
			// part 4:
			currentStr = "hostname";
			if (sql.indexOf(currentStr.toLowerCase()) > -1) {
				retVal = retVal.replace(currentStr, "HOSTNAME");
			}
			// part 5:
			currentStr = "DELETE FROM variable WHERE name = 'menu_rebuild_needed'";
			if (sql.indexOf(currentStr.toLowerCase()) > -1) {
				retVal = retVal
						.replace(currentStr,
								"DELETE FROM variable WHERE NAME = 'menu_rebuild_needed'");
			}
			currentStr = "router_path NOT IN";
			if (sql.indexOf(currentStr.toLowerCase()) > -1) {
				retVal = retVal
						.replace(currentStr,
								"router_path IN");	//NOT IN is not supported, so try to cheat by replacing with IN
			}
			currentStr = "FROM menu_links ml LEFT JOIN menu_router m ON m.path = ml.router_path";
			if (sql.indexOf(currentStr.toLowerCase()) > -1) {
				// === remove existing "where" clause first
				retVal = sql.replace("where", "");
				retVal = retVal
						.replace(currentStr.toLowerCase(),
								"FROM menu_links ml, menu_router m where m.path = ml.router_path and");
			}
			currentStr = "select m.load_functions, m.to_arg_functions, m.access_callback, m.access_arguments, m.page_callback, m.page_arguments";
			if (sql.indexOf(currentStr.toLowerCase()) > -1) {
				try {
					retVal = StringUtil.safeString(sql);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// === remove existing "where" clause first
				retVal = retVal.replace("where", "");
				// === does not support left join either :(
				retVal = retVal
						.replace("from menu_links ml left join menu_router m on m.path = ml.router_path",
								"from menu_links ml, menu_router m where m.path = ml.router_path and");
			}
			currentStr = "ml.menu_name = 'primary-links";
			if (sql.indexOf(currentStr.toLowerCase()) > -1) {
				try {
					retVal = StringUtil.safeString(sql);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			currentStr = "NOT IN";
			if (sql.indexOf(currentStr.toLowerCase()) > -1) {
				System.out.println("SQL can not contains NOT IN: '" + sql + "'");
			}
			currentStr = "LEFT JOIN";
			if (sql.indexOf(currentStr.toLowerCase()) > -1) {
				System.out.println("SQL can not contains LEFT JOIN: '" + sql + "'");
			}
				
			// System.out.println(currentStr);
		}

		return retVal;
	}


}
