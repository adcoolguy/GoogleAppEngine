package passwordchange.core;

//import static org.quartz.JobBuilder.newJob;
//import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
//import static org.quartz.TriggerBuilder.newTrigger;

import java.io.IOException;
//import java.sql.Connection;
//import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

//import javax.naming.Reference;
//import javax.naming.StringRefAddr;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import javax.sql.DataSource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import passwordchange.core.ConnectionUtil;
import passwordchange.core.Constants;
//import org.quartz.JobDetail;
//import org.quartz.Scheduler;
//import org.quartz.SchedulerException;
//import org.quartz.SchedulerFactory;
//import org.quartz.Trigger;
//import org.quartz.impl.StdSchedulerFactory;

import com.appspot.cloudserviceapi.common.SettingsDBUtils;
import com.parse.Parse;
//import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;

//import gov.nih.nci.cadsr.cadsrpasswordchange.core.UserSecurityQuestion;
//dev only
//import com.test.DAO;
//import com.test.oracle.dao.oracle.UserSecurityQuestionDaoImpl;
//import com.test.oracle.dto.UserSecurityQuestion;
//import com.test.mysql.dao.mysql.UserSecurityQuestionDaoImpl;
//import com.test.mysql.dto.UserSecurityQuestion;

public class MainServlet extends HttpServlet {

    private Logger logger = Logger.getLogger(MainServlet.class.getName());
    private static String _jndiUser = "java:/jdbc/caDSR";
    private static String _jndiSystem = "java:/jdbc/caDSRPasswordChange";
    
//	private static Connection connection = null;
//	private static DataSource datasource = null;
	private static DAO dao;		//dev only

    private void connectDB() {
    	//GAEJ issue - caused: java.lang.AbstractMethodError: javax.servlet.jsp.JspFactory.getJspApplicationContext(Ljavax/servlet/ServletContext;)Ljavax/servlet/jsp/JspApplicationContext;	
    	
//		boolean isConnectionException = true;  // use to modify returned messages when exceptions are system issues instead of password change issues  
//    	
//		Result result = new Result(ResultCode.UNKNOWN_ERROR);  // (should get replaced)
//        try {
//    		if(connection == null) {
//            	//dev only
//            	datasource = ConnectionUtil.getTestDS("root", ""); connection = datasource.getConnection(); dao = new DAO(connection);	//for DTO
//        	
//            	logger.info("Connected to database");
//    		}
//        	isConnectionException = false;
//		} catch (Exception e) {
//			e.printStackTrace();
//			if (isConnectionException)
//				result = new Result(ResultCode.UNKNOWN_ERROR);  // error not related to user, provide a generic error 
//			else
//				result = ConnectionUtil.decode(e);			
//		}
	}

    private void connect() {
//    	connectDB();
    }
    
    private void disconnect() {
//    	datasource = null;
//		try {
//			connection.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
    }
    
    private static final int TOTAL_QUESTIONS = 3;
    private static final String ERROR_MESSAGE_SESSION_ATTRIBUTE = "ErrorMessage"; 
    private static final String USER_MESSAGE_SESSION_ATTRIBUTE = "UserMessage";
	private Map userStoredQna;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.debug("doGet");
//		String target = req.getParameter("target");
//		
//		try {
//			String servletPath = req.getRequestURI();
//			logger.debug("getServletPath  |" + servletPath +"|");
//			if (servletPath.equals(Constants.SERVLET_URI + "/setupPassword")) {
//				doSetupPassword(req, resp);
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		initQuestionsOptions(req);
	}
		
	private void handleQuestionsOptions(HttpServletRequest req, String[] selectedQuestion) {
		req.getSession().setAttribute("selectedQuestion1", selectedQuestion[0]);
		req.getSession().setAttribute("selectedQuestion2", selectedQuestion[1]);
		req.getSession().setAttribute("selectedQuestion3", selectedQuestion[2]);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		logger.info("doPost");
		QuestionHelper.initQuestionsOptions(req);		
		String target = req.getParameter("target");
		
		try {
			String servletPath = req.getServletPath();
			logger.debug("getServletPath  |" + servletPath +"|");
			if (servletPath.equals(Constants.SERVLET_URI + "/login")) {
				doLogin(req, resp);
			} else if (servletPath.equals(Constants.SERVLET_URI + "/promptUserID")) {
				if(req.getParameter("cancel") != null) {
					resp.sendRedirect(Constants.LANDING_URL);
				} else {
					doValidateUserQuestionsForPasswordChange(req, resp);	//CADSRPASSW-76
				}
			} else if (servletPath.equals(Constants.SERVLET_URI + "/changePassword")) {
				if(req.getParameter("cancel") != null) {
					resp.sendRedirect(Constants.LANDING_URL);
				} else {
					doChangePassword(req, resp);
				}
			} else if (servletPath.equals(Constants.SERVLET_URI + "/changeEmail")) {
				if(req.getParameter("cancel") != null) {
					resp.sendRedirect(Constants.LANDING_URL);
				} else {
					doChangeEmail(req, resp);
				}
			} else if (servletPath.equals(Constants.SERVLET_URI + "/simpleChangePassword")) {
				if(req.getParameter("cancel") != null) {
					resp.sendRedirect(Constants.SIMPLE_LANDING_URL);
				} else {
					doSimpleChangePassword(req, resp);
				}
			} else if (servletPath.equals(Constants.SERVLET_URI + "/saveQuestions")) {
				if(req.getParameter("cancel") != null) {
					resp.sendRedirect(Constants.LANDING_URL);
				} else {
					doSaveQuestions(req, resp);
				}
//			} else if (servletPath.equals(Constants.SERVLET_URI + "/setupPassword")) {
//				doSetupPassword(req, resp);
			} else if (servletPath.equals(Constants.SERVLET_URI + "/promptUserQuestions")) {
				if(req.getParameter("cancel") != null) {
					resp.sendRedirect(Constants.LANDING_URL);
				} else {				
				doRequestUserQuestions(req, resp);
				}
			} else if (servletPath.equals(Constants.SERVLET_URI + "/promptQuestion1")) {
				doQuestion1(req, resp);
			} else if (servletPath.equals(Constants.SERVLET_URI + "/promptQuestion2")) {
				doQuestion2(req, resp);
			} else if (servletPath.equals(Constants.SERVLET_URI + "/promptQuestion3")) {
				doQuestion3(req, resp);
			} else if (servletPath.equals(Constants.SERVLET_URI + "/validateQuestion1")) {
				if(req.getParameter("cancel") != null) {
					resp.sendRedirect(Constants.LANDING_URL);
				} else {
				doValidateQuestion1(req, resp);
				}
			} else if (servletPath.equals(Constants.SERVLET_URI + "/validateQuestion2")) {
				if(req.getParameter("cancel") != null) {
					resp.sendRedirect(Constants.LANDING_URL);
				} else {
				doValidateQuestion2(req, resp);
				}
			} else if (servletPath.equals(Constants.SERVLET_URI + "/validateQuestion3")) {
				if(req.getParameter("cancel") != null) {
					resp.sendRedirect(Constants.LANDING_URL);
				} else {
				doValidateQuestion3(req, resp);
				}
			} else if (servletPath.equals(Constants.SERVLET_URI + "/resetPassword")) {
				if(req.getParameter("cancel") != null) {
					resp.sendRedirect(Constants.LANDING_URL);
				} else {
					doChangePassword2(req, resp);
				}
			} else {
				// this also catches the intentional logout with path /logout 
				logger.info("logging out because of invalid servlet path");				
				HttpSession session = req.getSession(false);
				if (session != null) {
					logger.debug("non-null session");					
					session.invalidate();
				}				
				resp.sendRedirect("./jsp/loggedOut.jsp");
			}
		}
		catch (Throwable theException) {
			logger.error(CommonUtil.toString(theException));
		}
	}

	private void doQuestion3(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.sendRedirect(Constants.RESET_URL);
	}

	private void doQuestion2(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		doValidateQuestion2(req, resp);
	}

	private void doQuestion1(HttpServletRequest req, HttpServletResponse resp) {

		logger.info("doQuestion1");
		
		try {
			HttpSession session = req.getSession(false);
			if (session == null) {
				logger.debug("null session");
				// this shouldn't happen, make the user start over
				resp.sendRedirect("./jsp/loggedOut.jsp");
				return;
			}

			String username = req.getParameter("userid");
			
			logger.debug("username " + username);			
			
			// Security enhancement
			Map userQuestions = new HashMap();
			Map userAnswers =  new HashMap();
			
			//pull all questions related to this user
			loadUserStoredQna(username, userQuestions, userAnswers);
			
			//TBD - retrieve all questions related to the users from dao and set them into sessions
			session.setAttribute(Constants.USERNAME, username);
			session.setAttribute(Constants.Q1, userQuestions.get(Constants.Q1));
//			session.setAttribute(Constants.Q2, userQuestions.get(Constants.Q2));
//			session.setAttribute(Constants.Q3, userQuestions.get(Constants.Q3));			

			session.setAttribute(Constants.ALL_QUESTIONS, userQuestions);
			session.setAttribute(Constants.ALL_ANSWERS, userAnswers);
			
			if(userQuestions.size() == 0) {
				logger.info("no security question found");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.140"));
				resp.sendRedirect(Constants.ASK_USERID_URL);
			} else {
				//resp.sendRedirect(Constants.Q1_URL);
				req.getRequestDispatcher("./jsp/askQuestion1.jsp").forward(req, resp);
			}
		}
		catch (Throwable theException) {
			logger.error(theException);
		}		
			
			
			
	}

	private void doSetupPassword(HttpServletRequest req,
			HttpServletResponse resp) {
		//populate users questions/answers
		QuestionHelper.initQuestionsOptions(req);		
		
		try {
			resp.sendRedirect("./jsp/setupPassword.jsp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	private void saveUserStoredQna(String username, Map userQuestions, Map userAnswers) {
		UserSecurityQuestion qna = new UserSecurityQuestion();
		qna.setUaName(username);
		qna.setQuestion1((String)userQuestions.get(Constants.Q1));
		qna.setAnswer1((String)userAnswers.get(Constants.A1));
		qna.setQuestion2((String)userQuestions.get(Constants.Q2));
		qna.setAnswer2((String)userAnswers.get(Constants.A2));
		qna.setQuestion3((String)userQuestions.get(Constants.Q3));
		qna.setAnswer3((String)userAnswers.get(Constants.A3));

		try {
			connect();			
//			UserSecurityQuestion oldQna = dao.findByUaName(username);
//			if(oldQna == null) {
//				dao.insert(qna);
//			} else {
//				dao.update(username, qna);
//			}
//			showUserSecurityQuestionList();	//just for debug
			disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateUserStoredAttemptedCount(String username) throws Exception {
		try {
			connect();
//			UserSecurityQuestion oldQna = dao.findByUaName(username);
//			if(oldQna == null) {
//				throw new Exception("Questions have to exists before attempted count can be updated.");
//			}
			
			connect();
			long count = 1;
//			if(oldQna.getAttemptedCount() != null) {
//				count = oldQna.getAttemptedCount().longValue() + 1;
//			}
//			oldQna.setAttemptedCount(new Long(count));
			//showUserSecurityQuestionList();	//just for debug
			disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private long getUserStoredAttemptedCount(String username) throws Exception {
		long count = 0;
		try {
			connect();
//			UserSecurityQuestion oldQna = dao.findByUaName(username);
//			if(oldQna == null) {
//				throw new Exception("Questions have to exists before attempted count can be retrieved.");
//			}
			
//			if(oldQna.getAttemptedCount() != null) {
//				count = oldQna.getAttemptedCount().longValue();
//			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return count;
	}
	
	private void resetUserStoredAttemptedCount(String username) throws Exception {
		try {
			connect();
//			UserSecurityQuestion oldQna = dao.findByUaName(username);
//			if(oldQna == null) {
//				throw new Exception("Questions have to exists before attempted count can be reset.");
//			}
			
			connect();
			long count = 0;
//			oldQna.setAttemptedCount(new Long(count));
//			dao.update(username, oldQna);
			//showUserSecurityQuestionList();	//just for debug
			disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showUserSecurityQuestionList() {
		UserSecurityQuestion[] results;
		try {
			connect();			
//			results = dao.findAll();
//			if (results.length > 0) {
//				for (UserSecurityQuestion e : results) {
//					System.out.println("User [" + e.getUaName() + "] updated ["
//							+ new Date() + "] question [" + e.getQuestion1()
//							+ "] answer [" + e.getAnswer1() + "]");
//				}
//			} else {
//				System.out.println("no question");
//			}
			disconnect();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	protected void doLogin(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		init();
		
		logger.info("doLogin");

		UserBean userBean = null;
		
		try {	
			HttpSession session = req.getSession(false);
			if (session == null) {
				logger.debug("null session");
				// this shouldn't happen, make the user start over
				resp.sendRedirect("./jsp/loggedOut.jsp");
				return;
			}
			
			session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, "");

			String username = req.getParameter("userid");
			String password = req.getParameter("pswd");
			logger.info("unvalidated username " + username);

			String errorMessage = "";

			// Limit input to legal characters before attempting any processing
			if(Messages.getString("PasswordChangeHelper.1").equals(PasswordChangeHelper.validateLogin(username, password))) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.1"));
				resp.sendRedirect("./jsp/login.jsp");				
				return;
			}

			if(Messages.getString("PasswordChangeHelper.2").equals(PasswordChangeHelper.validateLogin(username, password))) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.2"));
				resp.sendRedirect("./jsp/login.jsp");				
				return;
			}

			connect();
			DAO loginDAO = new DAO();
			userBean = loginDAO.checkValidUser(username, password);
			disconnect();
			session.setAttribute(UserBean.USERBEAN_SESSION_ATTRIBUTE, userBean);		
			logger.debug ("validUser " + userBean.isLoggedIn());
			logger.debug ("resultCode " + userBean.getResult().getResultCode().toString());
			if (userBean.isLoggedIn()) {
				//preload the questions
				QuestionHelper.initQuestionsOptions(req);		
				
				// Provide a user message that notes the "expired" status
				String userMessage = userBean.getResult().getMessage();
				logger.debug ("userMessage " + userMessage);
				session.setAttribute(USER_MESSAGE_SESSION_ATTRIBUTE, userMessage);
				session.setAttribute("username", username);
				resp.sendRedirect("./jsp/changePassword.jsp"); //logged-in page
			} else {
				String errorMessage1 = userBean.getResult().getMessage();
				logger.debug ("errorMessage " + errorMessage1);
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, errorMessage1);
				resp.sendRedirect("./jsp/login.jsp");				
			}
		}
		catch (Throwable e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	
	}

	protected void doSaveQuestions(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		logger.info("doSaveQuestions");
		
		try {
			HttpSession session = req.getSession(false);
			if (session == null) {
				logger.debug("null session");
				// this shouldn't happen, make the user start over
				resp.sendRedirect("./jsp/loggedOut.jsp");
				return;
			}

			// Security enhancement
			int paramCount = 0;
			String loginID = req.getParameter("userid");
			String question1 = req.getParameter("question1");
			String answer1 = req.getParameter("answer1");
			String question2 = req.getParameter("question2");
			String answer2 = req.getParameter("answer2");
			String question3 = req.getParameter("question3");
			String answer3 = req.getParameter("answer3");
			
			//"remember" the questions selected by the user
			String selectedQ[] = {question1, question2, question3};
			handleQuestionsOptions(req, selectedQ);
			req.getSession().setAttribute("userid", loginID);
			
			session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, "");			
			UserBean userBean = (UserBean) session.getAttribute(UserBean.USERBEAN_SESSION_ATTRIBUTE);
			
			String username = req.getParameter("userid");
			String password = req.getParameter("password");
			
			logger.debug("username " + username);
			//xss prevention (http://ha.ckers.org/xss.html)
			if(!StringEscapeUtils.escapeHtml4(answer1).equals(answer1) ||
					!StringEscapeUtils.escapeHtml4(answer2).equals(answer2) ||
					!StringEscapeUtils.escapeHtml4(answer3).equals(answer3)) {
				logger.debug("invalid character failed during questions/answers save");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.160"));
//				req.getRequestDispatcher(Constants.SETUP_QUESTIONS_URL).forward(req, resp);		//didn't work for jboss 4.0.5
				req.getRequestDispatcher("./jsp/setupPassword.jsp").forward(req, resp);
				return;
			}

			//DoS attack using string length overflow
			if(!CommonUtil.truncate(answer1, Constants.MAX_ANSWER_LENGTH).equals(answer1) ||
					!CommonUtil.truncate(answer2, Constants.MAX_ANSWER_LENGTH).equals(answer2) ||
					!CommonUtil.truncate(answer3, Constants.MAX_ANSWER_LENGTH).equals(answer3)) {
				logger.debug("invalid answer(s) length during questions/answers save");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.112"));
//				req.getRequestDispatcher(Constants.SETUP_QUESTIONS_URL).forward(req, resp);		//didn't work for jboss 4.0.5
				req.getRequestDispatcher("./jsp/setupPassword.jsp").forward(req, resp);
				return;
			}
				
			connect();
			DAO loginDAO = new DAO();
			userBean = loginDAO.checkValidUser(username, password);
			disconnect();
			session.setAttribute(UserBean.USERBEAN_SESSION_ATTRIBUTE, userBean);		
			logger.debug ("validUser" + userBean.isLoggedIn());
			logger.debug ("resultCode " + userBean.getResult().getResultCode().toString());
			if (!userBean.isLoggedIn()) {
				logger.debug("auth failed during questions/answers save");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.102"));
//				req.getRequestDispatcher(Constants.SETUP_QUESTIONS_URL).forward(req, resp);		//didn't work for jboss 4.0.5
				req.getRequestDispatcher("./jsp/setupPassword.jsp").forward(req, resp);
				return;
			}
			
			// Security enhancement
		    Map userQuestions = new HashMap();	
		    userQuestions.put(question1,"");
		    userQuestions.put(question2,"");
		    userQuestions.put(question3,"");
		    if(question1 != null && !question1.equals("")) paramCount++;
		    if(question2 != null && !question2.equals("")) paramCount++;
		    if(question3 != null && !question3.equals("")) paramCount++;
		    if(userQuestions.size() < TOTAL_QUESTIONS && paramCount == TOTAL_QUESTIONS) {
				logger.debug("security Q&A validation failed");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.135"));
//				req.getRequestDispatcher(Constants.SETUP_QUESTIONS_URL).forward(req, resp);		//didn't work for jboss 4.0.5
				req.getRequestDispatcher("./jsp/setupPassword.jsp").forward(req, resp);
				return;
			}
		    userQuestions = new HashMap();
		    Map userAnswers = new HashMap();	
		    if(question1 != null && !question1.equals("") && answer1 != null && !answer1.equals("")) userQuestions.put(Constants.Q1, question1); userAnswers.put(Constants.A1, answer1);
		    if(question2 != null && !question2.equals("") && answer2 != null && !answer2.equals("")) userQuestions.put(Constants.Q2, question2); userAnswers.put(Constants.A2, answer2);
		    if(question3 != null && !question3.equals("") && answer3 != null && !answer3.equals("")) userQuestions.put(Constants.Q3, question3); userAnswers.put(Constants.A3, answer3);
			logger.debug("saving request: " + question1 + "=" + answer1 + " " +question2 + "=" + answer2 + " " +question3 + "=" + answer3);
			if(Messages.getString("PasswordChangeHelper.125").equals(PasswordChangeHelper.validateSecurityQandA(TOTAL_QUESTIONS, username, userQuestions, userAnswers))) {
				logger.debug("security Q&A validation failed");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.125"));
//				req.getRequestDispatcher(Constants.SETUP_QUESTIONS_URL).forward(req, resp);		//didn't work for jboss 4.0.5
				req.getRequestDispatcher("./jsp/setupPassword.jsp").forward(req, resp);
				return;
			}
			if(!PasswordChangeHelper.validateQuestionsLength(TOTAL_QUESTIONS, userQuestions, userAnswers)) {
				logger.debug("security Q&A validation failed");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.150"));
//				req.getRequestDispatcher(Constants.SETUP_QUESTIONS_URL).forward(req, resp);		//didn't work for jboss 4.0.5
				req.getRequestDispatcher("./jsp/setupPassword.jsp").forward(req, resp);
				return;
			}
			
			logger.info("saving request: user provided " +  userQuestions + " " + userAnswers);
		    saveUserStoredQna(username, userQuestions, userAnswers);
			
//			connect();			
//			DAO userDAO = new DAO(connection);
//			try {
//				if(!userDAO.checkValidUser(username)) {
//					session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.101"));
//					resp.sendRedirect(Constants.ASK_USERID_URL);
//					return;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				disconnect();
//			}
			
			//TBD - retrieve all questions related to the users from dao and set them into sessions
			session.setAttribute(Constants.USERNAME, username);
			
			session.invalidate();
			resp.sendRedirect(Constants.SETUP_SAVED_URL);
		}
		catch (Throwable theException) {
			logger.error(theException);
		}		
	}

	protected void doValidateUserQuestionsForPasswordChange(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		logger.info("doValidateUserQuestions");
		
		try {
			HttpSession session = req.getSession(false);
			if (session == null) {
				logger.debug("null session");
				// this shouldn't happen, make the user start over
				resp.sendRedirect("./jsp/loggedOut.jsp");
				return;
			}

			String username = req.getParameter("userid");
			if(username != null) {
				username = username.toUpperCase();
			}
			session.setAttribute(Constants.USERNAME, username);
			logger.debug("username " + username);
			String status = doValidateAccountStatus(username, session, req, resp, Constants.REQUEST_USERID_FOR_CHANGE_PASSWORD_URL);
			if(status.indexOf(Constants.LOCKED_STATUS) > -1) {
				logger.debug("doRequestUserQuestions:status [" + status + "] returning without doing anything ...");
				return;
			}
			
			connect();
			DAO userDAO = new DAO();
			try {
//				if(!userDAO.checkValidUser(username)) {
				if(false) {
					session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.101"));
					resp.sendRedirect(Constants.REQUEST_USERID_FOR_CHANGE_PASSWORD_URL);
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				disconnect();
			}

			// Security enhancement
			Map<String, String> userQuestions = new HashMap<String, String>();
			Map<String, String> userAnswers =  new HashMap<String, String>();
			
			loadUserStoredQna(username, userQuestions, userAnswers);

			if(userQuestions == null || userQuestions.size() == 0) {
				logger.info("no security question found");
				resp.sendRedirect(Constants.SETUP_QUESTIONS_URL + "?donotclear");
				return;
			}
			
			req.getRequestDispatcher(Constants.REQUEST_USERID_FOR_CHANGE_PASSWORD_URL + "?donotclear").forward(req, resp);
		}
		catch (Throwable theException) {
			logger.error(theException);
		}		
	}
	
	protected void doRequestUserQuestions(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		logger.info("doRequestUserQuestions");
		
		try {
			HttpSession session = req.getSession(false);
			if (session == null) {
				logger.debug("null session");
				// this shouldn't happen, make the user start over
				resp.sendRedirect("./jsp/loggedOut.jsp");
				return;
			}

			String username = req.getParameter("userid");
			if(username != null) {
				username = username.toUpperCase();
			}
			logger.debug("username " + username);
			String status = doValidateAccountStatus(username, session, req, resp, Constants.ASK_USERID_URL);
			if(status.indexOf(Constants.LOCKED_STATUS) > -1) {
				logger.debug("doRequestUserQuestions:status [" + status + "] returning without doing anything ...");
				return;
			}
			
			connect();
			DAO userDAO = new DAO();
			try {
				if(!userDAO.checkValidUser(username)) {
					session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.101"));
					resp.sendRedirect(Constants.ASK_USERID_URL);
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				disconnect();
			}
			
			// Security enhancement
			Map<String, String> userQuestions = new HashMap<String, String>();
			Map<String, String> userAnswers =  new HashMap<String, String>();
			
			//pull all questions related to this user
			loadUserStoredQna(username, userQuestions, userAnswers);
			
			//TBD - retrieve all questions related to the users from dao and set them into sessions
			session.setAttribute(Constants.USERNAME, username);
			session.removeAttribute(Constants.Q1);
			session.setAttribute(Constants.Q1, userQuestions.get(Constants.Q1));
			session.removeAttribute(Constants.Q2);
			session.setAttribute(Constants.Q2, userQuestions.get(Constants.Q2));
			session.removeAttribute(Constants.Q3);
			session.setAttribute(Constants.Q3, userQuestions.get(Constants.Q3));			

			session.removeAttribute(Constants.ALL_QUESTIONS);
			logger.debug("questions removed from session.");
			session.setAttribute(Constants.ALL_QUESTIONS, userQuestions);
			logger.debug("questions saved in session.");
			session.removeAttribute(Constants.ALL_ANSWERS);
			logger.debug("answers removed from session.");
			session.setAttribute(Constants.ALL_ANSWERS, userAnswers);
			logger.debug("answers saved in session.");

			if(userQuestions == null || userQuestions.size() == 0) {
				logger.info("no security question found");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.140"));
				resp.sendRedirect(Constants.ASK_USERID_URL);
				return;
			}
			
			if(doValidateAttemptedCount(session, resp, Constants.ASK_USERID_URL) == false) {
				return;
			}			
			
			//resp.sendRedirect(Constants.Q1_URL);
			req.getRequestDispatcher("./jsp/askQuestion1.jsp").forward(req, resp);
		}
		catch (Throwable theException) {
			logger.error(theException);
		}		
	}

	private boolean doValidateAttemptedCount(HttpSession session, HttpServletResponse resp, String redictedUrl) throws Exception {
		boolean retVal = true;
		return retVal;
	}

	protected void doValidateQuestion1(HttpServletRequest req, HttpServletResponse resp)
	throws Exception {
		logger.info("doValidateQuestion 1");
		
		HttpSession session = req.getSession(false);
		if (session == null) {
			logger.debug("null session");
			// this shouldn't happen, make the user start over
			resp.sendRedirect("./jsp/loggedOut.jsp");
			return;
		}		

//		doValidateAttemptedCount(session, resp, "./jsp/askQuestion1.jsp");
		
		try {
			if (validateQuestions(req, resp)) {
				logger.info("answer is correct");
				resp.sendRedirect(Constants.Q2_URL);
				session.setAttribute("q1done", "true");
			} else {
				updateUserStoredAttemptedCount((String)session.getAttribute(Constants.USERNAME));	//CADSRPASSW-42
				logger.info("security question answered wrongly");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.130"));
				resp.sendRedirect(Constants.Q1_URL);
			}
		}
		catch (Throwable theException) {
			logger.error(CommonUtil.toString(theException));
		}		
	}
	
	protected void doValidateQuestion2(HttpServletRequest req, HttpServletResponse resp)
	throws Exception {
		logger.info("doValidateQuestion 2");
		
		HttpSession session = req.getSession(false);
		if (session == null) {
			logger.debug("null session");
			// this shouldn't happen, make the user start over
			resp.sendRedirect("./jsp/loggedOut.jsp");
			return;
		}		

//		doValidateAttemptedCount(session, resp, "./jsp/askQuestion2.jsp");
		
		try {
			if (validateQuestions(req, resp)) {
				logger.info("answer is correct");
				session.setAttribute("q2done", "true");
				resp.sendRedirect(Constants.Q3_URL);
			} else {
				logger.info("security question answered wrongly");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.130"));
				resp.sendRedirect(Constants.Q2_URL);
			}
		}
		catch (Throwable theException) {
			logger.error(CommonUtil.toString(theException));
		}		
	}

	protected void doValidateQuestion3(HttpServletRequest req, HttpServletResponse resp)
	throws Exception {
		logger.info("doValidateQuestion 3");
		
		HttpSession session = req.getSession(false);
		if (session == null) {
			logger.debug("null session");
			// this shouldn't happen, make the user start over
			resp.sendRedirect("./jsp/loggedOut.jsp");
			return;
		}		

//		doValidateAttemptedCount(session, resp, "./jsp/askQuestion3.jsp");
		
		try {
			if (validateQuestions(req, resp) && "true".equals((String)session.getAttribute("q1done")) && "true".equals((String)session.getAttribute("q2done"))) {
				logger.info("answer is correct");
				resp.sendRedirect(Constants.RESET_URL);				
			} else {
				logger.info("security question answered wrongly");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.130"));
				resp.sendRedirect(Constants.Q3_URL);
			}
		}
		catch (Throwable theException) {
			logger.error(CommonUtil.toString(theException));
		}		
	}

	protected boolean validateQuestions(HttpServletRequest req, HttpServletResponse resp)
	throws Exception {

		HttpSession session = req.getSession(false);
		Map userQuestions = (HashMap) session.getAttribute(Constants.ALL_QUESTIONS);
		Map userAnswers = (HashMap) session.getAttribute(Constants.ALL_ANSWERS);
		logger.info("questions " + userQuestions != null?userQuestions.size():0 + " answers " + userAnswers.size());

		String question1 = req.getParameter("question");
		String answer1 = req.getParameter("answer");
		String answerIndex =  req.getParameter("answerIndex");
		logger.debug("doValidateQuestions: (" + question1 + ")=" + answer1);
		
		boolean validated = false;
		//get user's stored answer related to the question selected
		String correctAnswer = (String)userAnswers.get(answerIndex);
		if(correctAnswer != null && correctAnswer.equals(answer1)) {
			validated = true;
		}
		
		return validated;
	}

	
	protected void doChangePassword2(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		logger.info("doChangePassword");
		
		try {
			HttpSession session = req.getSession(false);
			if (session == null) {
				logger.debug("null session");
				// this shouldn't happen, make the user start over
				resp.sendRedirect("./jsp/loggedOut.jsp");
				return;
			}

			String username = req.getParameter("userid");
			String newPassword = req.getParameter("newpswd1");

			// Security enhancement
			String question1 = (String)req.getParameter("question1");
			String answer1 = (String)req.getParameter("answer1");
			String question2 = (String)req.getParameter("question2");
			String answer2 = (String)req.getParameter("answer2");
			String question3 = (String)req.getParameter("question3");
			String answer3 = (String)req.getParameter("answer3");
			logger.debug("changing request: " + question1 + "=" + answer1 + " " +question2 + "=" + answer2 + " " +question3 + "=" + answer3);
		
			logger.debug("username " + username);
			connect();
			DAO changeDAO = new DAO();
			Result passwordChangeResult = changeDAO.resetPassword(username, newPassword);
			disconnect();

			if (passwordChangeResult.getResultCode() == ResultCode.PASSWORD_CHANGED) {
				logger.info("password changed");
				session.invalidate();  // they are done, log them out
				resp.sendRedirect("./jsp/passwordChanged.jsp");				
			} else {
				logger.info("password change failed");
				String errorMessage = passwordChangeResult.getMessage();
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, errorMessage);
				resp.sendRedirect("./jsp/resetPassword.jsp");		
			}
		}
		catch (Throwable theException) {
			logger.error(CommonUtil.toString(theException));
		}		
	}

	protected void doChangeEmail(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		logger.info("doChangeEmail");
		
		HttpSession session = req.getSession(false);
		Result passwordChangeResult = null;
		try {
			if (session == null) {
				logger.debug("null session");
				// this shouldn't happen, make the user start over
				resp.sendRedirect("./jsp/loggedOut.jsp");
				return;
			}

			String username = req.getParameter("userid");
			String password = req.getParameter("pswd");			
			String newEmail = req.getParameter("newemail1");

			logger.debug("username " + username);
			connect();
			DAO changeDAO = new DAO();
			passwordChangeResult = changeDAO.resetEmail(username, password, newEmail);
			disconnect();

			if (passwordChangeResult.getResultCode() == ResultCode.EMAIL_CHANGED) {
				logger.info("email changed");
				session.invalidate();  // they are done, log them out
				resp.sendRedirect("./jsp/emailChanged.jsp");				
			} else {
				logger.info("email change failed");
				String errorMessage = passwordChangeResult.getMessage();
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, errorMessage);
				resp.sendRedirect("./jsp/setupEmail.jsp");		
			}
		}
		catch (Throwable theException) {
			logger.error(CommonUtil.toString(theException));
			String errorMessage = theException.getMessage();
			session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, errorMessage);
			resp.sendRedirect("./jsp/setupEmail.jsp");			
		}		
	}
	
	protected void doSimpleChangePassword(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		logger.info("doSimpleChangePassword");
		
		try {
			HttpSession session = req.getSession(false);
			if (session == null) {
				logger.debug("null session");
				// this shouldn't happen, make the user start over
				resp.sendRedirect("./jsp/loggedOut.jsp");
				return;
			}

			session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, "");
			
			String username = req.getParameter("userid");
			String oldPassword = req.getParameter("pswd");
			String newPassword = req.getParameter("newpswd1");
			String newPassword2 = req.getParameter("newpswd2");

			logger.debug("doSimpleChangePassword:username " + username);

			connect();
			DAO changeDAO = new DAO();
			Result passwordChangeResult = changeDAO.changePassword(username, oldPassword, newPassword);
			disconnect();

			if (passwordChangeResult.getResultCode() == ResultCode.PASSWORD_CHANGED) {
				logger.info("password changed");
				session.invalidate();  // they are done, log them out
				resp.sendRedirect("./jsp/simplePasswordChanged.jsp");
			} else {
				logger.info("password change failed");
				String errorMessage = passwordChangeResult.getMessage();
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, errorMessage);
				resp.sendRedirect("./jsp/simpleChangePassword.jsp");		
			}
		}
		catch (Throwable theException) {		
			logger.error(theException);
		}		
	}
	
	protected void doChangePassword(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		logger.info("doChangePassword");
		
		try {
			HttpSession session = req.getSession(false);
			if (session == null) {
				logger.debug("null session");
				// this shouldn't happen, make the user start over
				resp.sendRedirect("./jsp/loggedOut.jsp");
				return;
			}

			session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, "");
			
			String username = req.getParameter("userid");
			if(username != null) {
				username = username.toUpperCase();
			}
			String oldPassword = req.getParameter("pswd");
			String newPassword = req.getParameter("newpswd1");
			String newPassword2 = req.getParameter("newpswd2");

			logger.debug("doChangePassword:username " + username);
			//begin - CADSRPASSW-73
			String status = "";
			try {
				logger.info("doChangePassword: checking account status ...");
				status = doValidateAccountStatus(username, session, req, resp, "./jsp/changePassword.jsp");
				logger.debug("doChangePassword: account status check done");
			} catch (Exception e1) {
				logger.debug("doChangePassword: account status was: [" + status + "]");
				if(status != null && status.equals("")) {
					session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.101"));
					resp.sendRedirect("./jsp/changePassword.jsp");
					return;
				} else {
					logger.debug("doChangePassword: account status check error was: " + e1.getMessage());
					e1.printStackTrace();
				}
			}
			//end - CADSRPASSW-73
			if(status.indexOf(Constants.LOCKED_STATUS) > -1) {
				logger.debug("doChangePassword:status [" + status + "] returning without doing anything ...");
				return;
			}
			
			//CADSRPASSW-50
			if(status.equals(Constants.EXPIRED_STATUS)) {
				connect();
				DAO userDAO = new DAO();
				try {
					if(!userDAO.checkValidUser(username)) {
						session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.101"));
						resp.sendRedirect("./jsp/changePassword.jsp");
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					disconnect();
				}
			} else {
				UserBean userBean = null;
				connect();
				DAO loginDAO = new DAO();
				userBean = loginDAO.checkValidUser(username, oldPassword);
				disconnect();
				session.setAttribute(UserBean.USERBEAN_SESSION_ATTRIBUTE, userBean);		
				logger.debug ("validUser " + userBean.isLoggedIn());
				logger.debug ("resultCode " + userBean.getResult().getResultCode().toString());
				if (!userBean.isLoggedIn()) {
					String errorMessage1 = userBean.getResult().getMessage();
					logger.debug ("errorMessage " + errorMessage1);
					if(userBean.getResult().getResultCode() != ResultCode.LOCKED_OUT) {
						//CADSRPASSW-60
						status = doValidateAccountStatus(username, session, req, resp, "./jsp/changePassword.jsp");
						if(status.indexOf(Constants.LOCKED_STATUS) > -1) {
							logger.debug("doChangePassword:status [" + status + "] returning without doing anything ...");
							return;
						}
						session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.102"));
					} else {
						session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.103"));
					}
					resp.sendRedirect(Constants.CHANGE_PASSWORD_URL + "?donotclear");
					return;
				}
			}
			
			
			//begin CADSRPASSW-16
//			Map<String, String> userQuestions = new HashMap<String, String>();
//			Map<String, String> userAnswers =  new HashMap<String, String>();
//			loadUserStoredQna(username, userQuestions, userAnswers);
//			if(userQuestions.size() == 0) {
//				logger.info("no security question found");
//				String msg = Messages.getString("PasswordChangeHelper.136");
//				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, msg);
//				resp.sendRedirect("./jsp/changePassword.jsp");
//				return;
//			}
			//end CADSRPASSW-16

//=== begin of moved down (CADSRPASSW-48)
			if(Messages.getString("PasswordChangeHelper.3").equals(PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, username, req.getParameter("newpswd2")))) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.3"));
				resp.sendRedirect("./jsp/changePassword.jsp");
				return;
			}

			if(Messages.getString("PasswordChangeHelper.4").equals(PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, username, req.getParameter("newpswd2")))) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.4"));
				resp.sendRedirect("./jsp/changePassword.jsp");
				return;
			}
			
			if(Messages.getString("PasswordChangeHelper.5").equals(PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, username, req.getParameter("newpswd2")))) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.5"));
				resp.sendRedirect("./jsp/changePassword.jsp");
				return;
			}

//			if(Messages.getString("PasswordChangeHelper.6").equals(PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, username, req.getParameter("newpswd2")))) {
//				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.6"));
//				resp.sendRedirect("./jsp/changePassword.jsp");
//				return;
//			}					

			if(Messages.getString("PasswordChangeHelper.7").equals(PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, username, req.getParameter("newpswd2")))) {
				logger.debug("entered username doesn't match session " + username + " " + req.getParameter("userid").toUpperCase());
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.7"));
				resp.sendRedirect("./jsp/changePassword.jsp");				
				return;
			}
			if(Messages.getString("PasswordChangeHelper.8").equals(PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, username, req.getParameter("newpswd2")))) {
				logger.debug("new password mis-typed");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.8"));
				resp.sendRedirect("./jsp/changePassword.jsp");
				return;
			}
//=== end of moved down (CADSRPASSW-48)

			connect();
			DAO changeDAO = new DAO();
			Result passwordChangeResult = changeDAO.changePassword(username, oldPassword, newPassword);
			disconnect();

			if (passwordChangeResult.getResultCode() == ResultCode.PASSWORD_CHANGED) {
				logger.info("password changed");
				resetUserStoredAttemptedCount(username);	//CADSRPASSW-42
				logger.debug("answer count reset");
				session.invalidate();  // they are done, log them out
				resp.sendRedirect("./jsp/passwordChanged.jsp");
			} else {
				logger.info("password change failed");
				String errorMessage = passwordChangeResult.getMessage();
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, errorMessage);
				resp.sendRedirect("./jsp/changePassword.jsp");		
			}
		}
		catch (Throwable theException) {		
			logger.error(theException);
		}		
	}
	
	protected void doChangePassword1(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		logger.info("doChangePassword");
		
		try {
			HttpSession session = req.getSession(false);
			if (session == null) {
				logger.debug("null session");
				// this shouldn't happen, make the user start over
				resp.sendRedirect("./jsp/loggedOut.jsp");
				return;
			}

			session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, "");
			
			String username = req.getParameter("userid");
			if(username != null) {
				username = username.toUpperCase();
			}
			String oldPassword = req.getParameter("pswd");
			String newPassword = req.getParameter("newpswd1");
			String newPassword2 = req.getParameter("newpswd2");

			logger.debug("doChangePassword:username " + username);
			//begin - CADSRPASSW-73
			String status = "";
			try {
				logger.info("doChangePassword: checking account status ...");
				status = doValidateAccountStatus(username, session, req, resp, "./jsp/changePassword.jsp");
				logger.debug("doChangePassword: account status check done");
			} catch (Exception e1) {
				logger.debug("doChangePassword: account status was: [" + status + "]");
				if(status != null && status.equals("")) {
					session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.101"));
					resp.sendRedirect("./jsp/changePassword.jsp");
					return;
				} else {
					logger.debug("doChangePassword: account status check error was: " + e1.getMessage());
					e1.printStackTrace();
				}
			}
			//end - CADSRPASSW-73
			if(status.indexOf(Constants.LOCKED_STATUS) > -1) {
				logger.debug("doChangePassword:status [" + status + "] returning without doing anything ...");
				return;
			}
			
			//CADSRPASSW-50
			if(status.equals(Constants.EXPIRED_STATUS)) {
				connect();
				DAO userDAO = new DAO();
				try {
					if(!userDAO.checkValidUser(username)) {
						session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.101"));
						resp.sendRedirect("./jsp/changePassword.jsp");
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					disconnect();
				}
			} else {
				UserBean userBean = null;
				connect();
				DAO loginDAO = new DAO();
				userBean = loginDAO.checkValidUser(username, oldPassword);
				disconnect();
				session.setAttribute(UserBean.USERBEAN_SESSION_ATTRIBUTE, userBean);		
				logger.debug ("validUser " + userBean.isLoggedIn());
				logger.debug ("resultCode " + userBean.getResult().getResultCode().toString());
				if (!userBean.isLoggedIn()) {
					String errorMessage1 = userBean.getResult().getMessage();
					logger.debug ("errorMessage " + errorMessage1);
					if(userBean.getResult().getResultCode() != ResultCode.LOCKED_OUT) {
						//CADSRPASSW-60
						status = doValidateAccountStatus(username, session, req, resp, "./jsp/changePassword.jsp");
						if(status.indexOf(Constants.LOCKED_STATUS) > -1) {
							logger.debug("doChangePassword:status [" + status + "] returning without doing anything ...");
							return;
						}
						session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.102"));
					} else {
						session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.103"));
					}
					resp.sendRedirect("./jsp/changePassword.jsp");
					return;
				}
			}
			
			
			//begin CADSRPASSW-16
//			Map<String, String> userQuestions = new HashMap<String, String>();
//			Map<String, String> userAnswers =  new HashMap<String, String>();
//			loadUserStoredQna(username, userQuestions, userAnswers);
//			if(userQuestions.size() == 0) {
//				logger.info("no security question found");
//				String msg = Messages.getString("PasswordChangeHelper.136");
//				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, msg);
//				resp.sendRedirect("./jsp/changePassword.jsp");
//				return;
//			}
			//end CADSRPASSW-16

//=== begin of moved down (CADSRPASSW-48)
			if(Messages.getString("PasswordChangeHelper.3").equals(PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, username, req.getParameter("newpswd2")))) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.3"));
				resp.sendRedirect("./jsp/changePassword.jsp");
				return;
			}

			if(Messages.getString("PasswordChangeHelper.4").equals(PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, username, req.getParameter("newpswd2")))) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.4"));
				resp.sendRedirect("./jsp/changePassword.jsp");
				return;
			}
			
			if(Messages.getString("PasswordChangeHelper.5").equals(PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, username, req.getParameter("newpswd2")))) {
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.5"));
				resp.sendRedirect("./jsp/changePassword.jsp");
				return;
			}

//			if(Messages.getString("PasswordChangeHelper.6").equals(PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, username, req.getParameter("newpswd2")))) {
//				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.6"));
//				resp.sendRedirect("./jsp/changePassword.jsp");
//				return;
//			}					

			if(Messages.getString("PasswordChangeHelper.7").equals(PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, username, req.getParameter("newpswd2")))) {
				logger.debug("entered username doesn't match session " + username + " " + req.getParameter("userid").toUpperCase());
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.7"));
				resp.sendRedirect("./jsp/changePassword.jsp");				
				return;
			}
			if(Messages.getString("PasswordChangeHelper.8").equals(PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, username, req.getParameter("newpswd2")))) {
				logger.debug("new password mis-typed");
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, Messages.getString("PasswordChangeHelper.8"));
				resp.sendRedirect("./jsp/changePassword.jsp");
				return;
			}
//=== end of moved down (CADSRPASSW-48)

			connect();
			DAO changeDAO = new DAO();
			Result passwordChangeResult = changeDAO.changePassword(username, oldPassword, newPassword);
			disconnect();

			if (passwordChangeResult.getResultCode() == ResultCode.PASSWORD_CHANGED) {
				logger.info("password changed");
				resetUserStoredAttemptedCount(username);	//CADSRPASSW-42
				logger.debug("answer count reset");
				session.invalidate();  // they are done, log them out
				resp.sendRedirect("./jsp/passwordChanged.jsp");
			} else {
				logger.info("password change failed");
				String errorMessage = passwordChangeResult.getMessage();
				session.setAttribute(ERROR_MESSAGE_SESSION_ATTRIBUTE, errorMessage);
				resp.sendRedirect("./jsp/changePassword.jsp");		
			}
		}
		catch (Throwable theException) {		
			logger.error(theException);
		}		
	}
	
	private String doValidateAccountStatus(String username, HttpSession session, HttpServletRequest req, HttpServletResponse resp, String redictedUrl) throws Exception {
		String retVal = "OPEN";
		return retVal;
	}	

	private boolean loadUserStoredQna(String username, Map userQuestions, Map userAnswers) {
		UserSecurityQuestion qna = new UserSecurityQuestion();
		boolean retVal = false;
		try {
			connect();
//			qna = dao.findByUaName(username);
			if(qna != null) {
				userQuestions.put(Constants.Q1, qna.getQuestion1());
				userQuestions.put(Constants.Q2, qna.getQuestion2());
				userQuestions.put(Constants.Q3, qna.getQuestion3());
				userAnswers.put(Constants.A1, qna.getAnswer1());
				userAnswers.put(Constants.A2, qna.getAnswer2());
				userAnswers.put(Constants.A3, qna.getAnswer3());
				retVal = true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return retVal;
		
	}

	public static void initProperties() {

	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		logger.debug("init");
//		connect();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		logger.debug("init(ServletConfig config)");
		connect();
/*		
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = null;
		try {
			sched = sf.getScheduler();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 // define the job and tie it to our HelloJob class
		JobDetail job = newJob(NotificationJob.class)
			    .withIdentity("job1", "group1")
			    .build();
		
        // Trigger the job to run now, and then repeat every 40 seconds
        Trigger trigger = newTrigger()
            .withIdentity("trigger1", "group1")
            .startNow()
            .withSchedule(simpleSchedule()
                    .withIntervalInSeconds(40)
                    .repeatForever())            
            .build();
        
        // Tell quartz to schedule the job using our trigger
        try {
			sched.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/		
	}
}