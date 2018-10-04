/*
 * This file was generated - do not edit it directly !!
 * Generated by AuDAO tool, a product of Spolecne s.r.o.
 * For more information please visit http://www.spoledge.com
 */
package com.appspot.cloudserviceapi.dto;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;

import com.appspot.cloudserviceapi.common.model.SimilarityManager;

/**
 * This is a DTO class.
 *
 * @author generated
 */
@Searchable(alias="social")
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Huma //extends AbstractDto { 
implements Template, Cloneable, Serializable {

    ////////////////////////////////////////////////////////////////////////////
    // Static
    ////////////////////////////////////////////////////////////////////////////

    public static final String TABLE = "huma";

    ////////////////////////////////////////////////////////////////////////////
    // Attributes
    ////////////////////////////////////////////////////////////////////////////

	@Persistent(primaryKey = "true",valueStrategy=IdGeneratorStrategy.IDENTITY)
    @SearchableId(name = "id")
    private Long id;
//org.compass.gps.device.jdo.JdoGpsDevice doIndex: {appengine2}: Failed to index the database
//	java.lang.IllegalArgumentException: No enum const class com.appspot.cloudserviceapi.data.HumasCategory.Code
	//@Transient private HumasCategory platform = HumasCategory.CODE;
//    @SearchableProperty    private String platformValue = platform.toString();
    @SearchableProperty    private String platform;
    @SearchableProperty    private String what;
    @SearchableProperty    private String details;
    @SearchableProperty    private String similarity;
    private Integer wordCount = 0;
    private Long frequencyCount = 0L;
    
    @SearchableProperty    private Date createdDate;
    @SearchableProperty    private Date lastUpdatedDate;
    @SearchableProperty    private Date lastAccessedDate;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new empty DTO.
     */
    public Huma() {
    }

    ////////////////////////////////////////////////////////////////////////////
    // Public
    ////////////////////////////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public void setId( Long _val) {
        this.id = _val;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform( String _val) {
        this.platform = _val;
    }

//	public String getPlatformValue() {
//		String retVal = HumasCategory.CODE.toString();
//		if(!(platform == HumasCategory.CODE)) {
//			  retVal = HumasCategory.IDEA.toString();
//		}
//	  
//		return retVal;
//	}
//
//	public void setPlatformValue(String platform) {
//		this.platformValue = platform;
//	}

//    public HumasCategory getPlatform() {
//		return platform;
//	}
//
//	public void setPlatform(HumasCategory platform) {
//		this.platform = platform;
//	}

    public String getWhat() {
        return what;
    }

    public void setWhat( String _val) {
        this.what = _val;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails( String _val) {
        this.details = _val;
    }

    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity( String _val) {
        this.similarity = _val;
    }

    public int getWordCount() {
		//return wordCount;
    	return SimilarityManager.countWords(SimilarityManager.getCommonToken(what));    	
	}

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}
	
	public long getFrequencyCount() {
		//return frequencyCount;
//		return SimilarityManager.getAccumulatedFrequency(getSimilarity());	//TBD - time consuming!
		return -1;
	}

	public void setFrequencyCount(long frequencyCount) {
		this.frequencyCount = frequencyCount;
	}

	public void countFrequency() {
		//TBD - should be moved to async task!
//		SimilarityManager.setAccumulatedFrequency(getSimilarity());		//commented out as it is a very time consuming calculation
	}
    
    //=== tapestry can't handle java.sql.Date!!!
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Date getLastAccessedDate() {
		return lastAccessedDate;
	}

	public void setLastAccessedDate(Date lastAccessedDate) {
		this.lastAccessedDate = lastAccessedDate;
	}    
    
    ////////////////////////////////////////////////////////////////////////////
    // Protected
    ////////////////////////////////////////////////////////////////////////////
		
    /**
     * Constructs the content for the toString() method.
     */
    protected void contentToString(StringBuffer sb) {
//        append( sb, "id", id );
//        append( sb, "platform", platform );
//        append( sb, "what", what );
//        append( sb, "details", details );
//        append( sb, "similarity", similarity );
//        append( sb, "createdDate", createdDate );
//        append( sb, "lastUpdatedDate", lastUpdatedDate );
//        append( sb, "lastAccessedDate", lastAccessedDate );
    }    
    
	@Override
	public String toString() {
		return "Huma [id=" + id + ", platform=" + platform + ", what=" + what
				+ ", details=" + details + ", similarity=" + similarity
				+ ", createdDate=" + createdDate + ", lastUpdatedDate="
				+ lastUpdatedDate + ", lastAccessedDate=" + lastAccessedDate
				+ "]";
	}

	public Object clone() throws CloneNotSupportedException {
		Huma clone = (Huma)super.clone();

		return clone;
	}
    
}
