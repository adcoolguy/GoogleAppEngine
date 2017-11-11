package com.appspot.cloudserviceapi.sci.fios;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

/**
 * Servlet implementation class CalendarEventServlet
 */
public class CalendarEventServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CalendarEventServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nextUrl = "http://localhost:8080/CalendarWebProject/CalendarEventServlet";
		String scope = "http://www.google.com/calendar/feeds/";
		boolean secure = false;  // set secure=true to request secure AuthSub tokens
		boolean session = true;
		String authSubUrl = AuthSubUtil.getRequestUrl(nextUrl, scope, secure, session);
		System.out.println("authSubUrl-----"+authSubUrl);
		
		 //try {
			  /*      
			        // Create a new Calendar service
			        CalendarService myService = new CalendarService("My Application");
			        myService.setUserCredentials(args[0],args[1]);
			        
			        // Get a list of all entries
			        URL metafeedUrl = new URL("http://www.google.com/calendar/feeds/default/allcalendars/full");
			        System.out.println("Getting Calendar entries...\n");
			        CalendarFeed resultFeed = myService.getFeed(metafeedUrl, CalendarFeed.class);
			        List<CalendarEntry> entries = resultFeed.getEntries();
			        for(int i=0; i<entries.size(); i++) {
			          CalendarEntry entry = entries.get(i);
			          System.out.println("\t" + entry.getTitle().getPlainText());
			        }
			        System.out.println("\nTotal Entries: "+entries.size());
			        
			        URL feedUrl = new URL("http://www.google.com/calendar/feeds/default/private/full");
			        
			        //added
			        Query myQuery = new Query(feedUrl);
			        myQuery.setFullTextQuery("Last");

			        CalendarEventFeed myResultsFeed = myService.query(myQuery, CalendarEventFeed.class);
			        
			        if (myResultsFeed.getEntries().size() > 0) {
			        	  CalendarEventEntry firstMatchEntry = (CalendarEventEntry)
			        	      myResultsFeed.getEntries().get(0); 
			        	  String myEntryTitle = firstMatchEntry.getTitle().getPlainText();
			        	  System.out.println("myEntryTitle----"+myEntryTitle);
			        	}
			        */
			/*    	// Set up the URL and the object that will handle the connection:
			 System.out.println("before url");
			    	  
			    	  CalendarService myService = new CalendarService("corp2-calEvent-1");
			    	 // System.out.println("after service instantiation---"+myService.getAuthToken("user.corp2.fios", "corp21234", "", "", "cl", "corp2-calEvent-1"));
			    	  myService.setUserCredentials("user.corp2.fios","corp21234",ClientLoginAccountType.GOOGLE);
			    	  
			    	  URL feedUrl = new URL("http://www.google.com/calendar/feeds/user.corp2.fios@gmail.com/private/full");
			    	  System.out.println("after user credentials");
			    	  // Send the request and receive the response:
			    	  CalendarEventFeed myFeed = myService.getFeed(feedUrl, CalendarEventFeed.class);
			    	  System.out.println("after get feed----"+myFeed.getEntries().size());
			    	  if(myFeed.getEntries().size()>0){
			    		  for(int i=0; i<myFeed.getEntries().size(); i++) {
			    			  CalendarEventEntry entry = (CalendarEventEntry)
			        	      myFeed.getEntries().get(i); 
			    			  
			    			  String myEntryTitle = entry.getTitle().getPlainText();
			    	    	  //System.out.println("myEntryTitle----"+myEntryTitle);
			    	          System.out.println("\t" + myEntryTitle);
			    	        }
			    		  
			    	  
			    	  }

			      }
			      catch(AuthenticationException e) {
			        e.printStackTrace();
			      }
			      catch(MalformedURLException e) {
			        e.printStackTrace();
			      }
			      catch(ServiceException e) {
			        e.printStackTrace();
			      }
			      catch(IOException e) {
			        e.printStackTrace();
			      } */
			 //doPost(request,response);
		
		
		
	/*	
		URL url = new URL("https://www.google.com/accounts/AuthSubSessionToken");
		String data = URLEncoder.encode("key1", "UTF-8") + "=" + URLEncoder.encode("value1", "UTF-8"); 
		data += "&" + URLEncoder.encode("key2", "UTF-8") + "=" + URLEncoder.encode("value2", "UTF-8"); 
		
		 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		 conn.setRequestMethod("GET");
		 conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
		 
		 conn.setRequestProperty("Authorization","token= CKPYufafGhCihpT8AxiFm8eCBQ");
		 conn.setRequestProperty("User-Agent","Java/1.5.0_06");
		 conn.setRequestProperty("Host","www.google.com");
		 conn.setRequestProperty("Accept", "text/html, image/gif, image/jpeg, *");
		 conn.setRequestProperty("Connection", "keep-alive");
		 
		 conn.setDoOutput(true); 
		 OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
		 wr.write(data); 
		 wr.flush(); 
		 
		// Get the response 
		 BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
		 String line; 
		 while ((line = rd.readLine()) != null) { 
			 String str = rd.toString();
			 System.out.println(str);
			 }
			 
			  wr.close(); 
			 rd.close(); 
		*/
			 
		// One more way
		
		
		 }
		 
		 
			    
		
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			//String singleUseToken = "CKPYufafGhCflbLuBBi_l_R6";
			//String singleUseToken = "CKPYufafGhD66q-W_f____8BGIOehuUD";
			String urlFromAuthSub="http://localhost:8080/CalendarWebProject/CalendarEventServlet?token=CKPYufafGhCihpT8AxiFm8eCBQ";
			System.out.println("before get token ");
			String singleUseToken = AuthSubUtil.getTokenFromReply(urlFromAuthSub);
			
			System.out.println("Single use token is --- "+singleUseToken);
			String sessionToken = AuthSubUtil.exchangeForSessionToken(URLDecoder.decode(singleUseToken,"UTF-8"), null);

			
			System.out.println("sessionToken---------------------"+sessionToken);
//			CalendarService calendarService = new CalendarService("google-ExampleApp-v1.0");
//			calendarService.setAuthSubToken(sessionToken);
//			
//			
//		 System.out.println("before url post");
//   	  URL feedUrl = new URL("http://www.google.com/calendar/feeds/user.corp2.fios@gmail.com/private/full");
//		// URL feedUrl = new URL("http://www.google.com/calendar/feeds");
//   	  CalendarService myService = new CalendarService("corp2-calEvent-1");
//   	  
//   	  //System.out.println("after service instantiation---"+myService.getAuthToken("user.corp2.fios", "corp21234", "", "", "cl", ""corp2-calEvent-1"));
//   	  
//   	  //myService.setUserCredentials("user.corp2.fios","corp21234");
//   	  System.out.println("after user credentials");
//   	  // Send the request and receive the response:
//   	  CalendarEventFeed myFeed = myService.getFeed(feedUrl, CalendarEventFeed.class);
//   	  System.out.println("after get feed----"+myFeed.getEntries().size());
//   	  if(myFeed.getEntries().size()>0){
//   		  for(int i=0; i<myFeed.getEntries().size(); i++) {
//   			  CalendarEventEntry entry = (CalendarEventEntry)
//       	      myFeed.getEntries().get(i); 
//   			  
//   			  String myEntryTitle = entry.getTitle().getPlainText();
//   	    	  //System.out.println("myEntryTitle----"+myEntryTitle);
//   	          System.out.println("\t" + myEntryTitle);
//   	        }
//   		  
//   	  
//   	  }

     }
     catch(AuthenticationException e) {
       e.printStackTrace();
     }
     catch(MalformedURLException e) {
       e.printStackTrace();
     }
     catch(ServiceException e) {
       e.printStackTrace();
     }
     catch(IOException e) {
       e.printStackTrace();
     } catch (GeneralSecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	}
		/*String nextUrl = "http://localhost:8080/CalendarWebProject/CalendarEventServlet";
		String scope = "http://www.google.com/calendar/feeds/";
		boolean secure = false;  // set secure=true to request secure AuthSub tokens
		boolean session = true;
		String authSubUrl = AuthSubUtil.getRequestUrl(nextUrl, scope, secure, session);
		System.out.println("authSubUrl-----"+authSubUrl);*/
	}

