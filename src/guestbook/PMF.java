package guestbook;

import java.sql.Connection;
import java.sql.Driver;
import java.util.Properties;

public final class PMF {
    static Driver driver = null;
  	static String url = "jdbc:jiql://local";
    static Properties props = new Properties();

    static {
    	
    String password = "admin";
    String user = "jiql";
     
    props.put("user",user);
    props.put("password",password);
   try{
   
    Class clazz = Class.forName("org.jiql.jdbc.Driver");
    driver = (Driver) clazz.newInstance();
   }catch (Exception e){
   	e.printStackTrace();
   }
    }

    public static Connection get() {
    	try{
    	
        return driver.connect(url,props);
    	}catch (java.sql.SQLException e){
      	e.printStackTrace();

    	}
    	return null;
    }
}
