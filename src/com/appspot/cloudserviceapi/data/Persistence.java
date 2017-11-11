package com.appspot.cloudserviceapi.data;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

//import org.apache.commons.httpclient.HttpException;
//import org.apache.hadoop.util.bloom.Key;


public class Persistence {
	
	private static final PersistenceManagerFactory pmfInstance = PMF.get();
//	private static final PersistenceManager manager = pmfInstance.getPersistenceManager();

	public static PersistenceManager getManager() {
		return pmfInstance.getPersistenceManager();
	}

//	@Deprecated
//	public static void save(String gaeAppID, String gmailAddress,
//			String gmailPassword, String key, String value) {
//		// String action = request.getParameter("action");
//		// String key = request.getParameter("key");
//		// String value = null;
//		// try {
//		// value = request.getParameter("value");
//		// } catch(Exception e) {
//		// }
//
//		BigTableClient client = new BigTableClient(gmailAddress, gmailPassword);
//
//		System.out.println("Persistence 1");
//		byte[] theByteArray = null;
//		// == put(String table, String row, String column, byte[] value)
//		String stringToConvert = value;
//		theByteArray = stringToConvert.getBytes();
//		// == e.g. client.put("fios-tv-user",
//		// "myneighborhood.category.sport.checkbox1", "state",
//		// theByteArray);
//		try {
//			client.put(gaeAppID, key, "value", theByteArray);
//			System.out.println("Persistence 2");
//		} catch (HttpException e) {
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (UnauthorizedException e) {
//			e.printStackTrace();
//		}
//		System.out.println("Persistence 3");
//	}
//
//    @Deprecated
//    public static String load(String gaeAppID, String gmailAddress,
//			String gmailPassword, String key) {
//		BigTableClient client = new BigTableClient(gmailAddress, gmailPassword);
//
//		System.out.println("Persistence 4");
//		byte[] theByteArray1 = null;
//		// == get(String table, String row, String column)
//		// === e.g. client.get("fios-tv-user",
//		// "myneighborhood.category.sport.checkbox1", "state");
//		Collection<Cell> cells = null;
//		try {
//			cells = client.get(gaeAppID, key, "value");
//		} catch (HttpException e) {
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println("Persistence 5");
//
//		Iterator iterator = cells.iterator();
//		while (iterator.hasNext()) {
//			Cell element = (Cell) iterator.next();
//			theByteArray1 = element.getValue();
//		}
//
//		return theByteArray1.toString();
//	}
//    
	public static void nuke() {
////    	DeleteAllMapper m = new DeleteAllMapper();
////    	m.map(null, null, null);
	}
}