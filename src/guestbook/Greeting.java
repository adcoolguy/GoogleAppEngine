package guestbook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Greeting {
	private Long id;

	private String author;

	private String content;

	private Date date;
	static String isql = "INSERT into Greeting (author,content,date) values (?,?,?)";
	static String isql2 = "INSERT into Greeting (content,date) values (?,?)";

	public Greeting(String author, String content, Date date) {
		this.author = author;
		this.content = content;
		this.date = date;
	}

	public static void init(String sql) throws SQLException {
		Connection Conn = null;
		try {
			Conn = PMF.get();
			Statement Stmt = Conn.createStatement();
			Stmt.execute(sql);
		} finally {
			if (Conn != null)
				Conn.close();

		}

	}

	public static List list(String sql) throws SQLException {
		Connection Conn = null;
		List list = new ArrayList();
		try {

			Greeting greeting = null;
			try {
				Conn = PMF.get();
				Statement Stmt = Conn.createStatement();
				Stmt.execute(sql);
				ResultSet res = Stmt.getResultSet();
				while (res.next()) {
					try {

						greeting = new Greeting(res.getString("author"), res
								.getString("content"), res.getDate("date"));
						list.add(greeting);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} finally {
				if (Conn != null)
					Conn.close();

			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return list;

	}

	public void store() {
		Connection Conn = null;
		try {
			Conn = PMF.get();
			String sql = isql;
			String u = null;
			if (author != null)
				u = author;
			if (u == null)
				sql = isql2;
			PreparedStatement pstat = Conn.prepareStatement(sql);
			if (u != null) {
				pstat.setString(1, u);
				pstat.setString(2, content);
				pstat.setDate(3, new java.sql.Date(date.getTime()));
			} else {
				pstat.setString(1, content);
				pstat.setDate(2, new java.sql.Date(date.getTime()));
			}
			pstat.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (Conn != null)
				try {

					Conn.close();
				} catch (SQLException e) {
					e.printStackTrace();

				}
		}
	}

	public Long getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public String getContent() {
		return content;
	}

	public Date getDate() {
		return date;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
