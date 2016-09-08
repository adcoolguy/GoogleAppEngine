package app.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.appspot.cloudserviceapi.data.AppEngine;

import app.model.Movie;
import app.model.User;

public class AppUtils {

	public static String createOwnerString(String flag, String owner) {
		String retVal = owner;

		if(flag != null && owner != null) {
			retVal = flag + "_" + owner;
		}
		
        return retVal;
	}

	public static String getOwnerPart(String ownerString) {
		String retVal = ownerString;
		
		int foundIndex = ownerString.indexOf(Constants.LEGACY_ENTITY);
		if(ownerString != null && foundIndex == 0) {
			retVal = ownerString.substring(Constants.LEGACY_ENTITY.length()+1, ownerString.length());
		} else {
			foundIndex = ownerString.indexOf(Constants.MIGRATED_ENTITY);
			if(ownerString != null && foundIndex == 0) {
				retVal = ownerString.substring(Constants.MIGRATED_ENTITY.length()+1, ownerString.length());
			}
		}
		
		return retVal;
	}
	
	public static String handleLegacyOwnerString(String owner, String legacy) {
		String retVal = owner;
		
		int foundIndex = owner.indexOf(Constants.LEGACY_ENTITY);
		if(owner != null && foundIndex > -1) {
			retVal = createOwnerString(Constants.MIGRATED_ENTITY, owner.substring(foundIndex, owner.length()));
		} else
		if(legacy != null && legacy.equals("true")) {
			retVal = createOwnerString(Constants.MIGRATED_ENTITY, owner);
		}
			
		return retVal;
	}

	/*
	 * @list The full list
	 * @maxPerPage the maximum items per page
	 * @pageNumber the page number, starts with 1 (not zero!)
	 */
	public static Collection getPagedResults(Collection list, long maxPerPage, long pageNumber) {
		Collection retVal = list;	//thus if maxPerPage == 0 and pageNumber == 0, returns all
		
		if(list != null && maxPerPage > 0 && pageNumber > 0) {
			long startIndex = (pageNumber-1)*maxPerPage;
			long endIndex = startIndex + maxPerPage;
			long count = 0;
			Collection newList = new ArrayList();
			Iterator it = list.iterator();
			Object o = null;
			while(it.hasNext()) {
				count++;
				o = it.next();
				if(count > startIndex && count <= endIndex) {
					newList.add(o);
				} 
				
				if(count == endIndex) {
					break;
				}
			}
			if(count > 0) {
				retVal = newList;
			}
		}
		
		return retVal;
	}

	public static String getAppHost(String cachedHostName) {
		if(cachedHostName != null && !cachedHostName.equals("localhost") && !cachedHostName.equals("127.0.0.1")) {	//TODO future enhancement should avoid this check on PROD
			cachedHostName = AppEngine.getHostName();
		} else {
			cachedHostName = "http://localhost:8888";
		}
		return cachedHostName;
	}
}