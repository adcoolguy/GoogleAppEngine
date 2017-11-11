package com.appspot.cloudserviceapi.guestbook;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import tapp.model.sgc.Employee;

@PersistenceCapable
@Searchable(alias="greeting")
public class Greeting implements Cloneable {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @SearchableId(accessor = "property", converter = "string")
    private Key key;

    @Persistent
    private User author;

    @Persistent
    @SearchableProperty
    private String content;

    @Persistent
    @SearchableProperty(format="MM/dd/yyyy")
    private Date date;

    public Greeting(User author, String content, Date date) {
        this.author = author;
        this.content = content;
        this.date = date;
    }

    public Key getKey() {
        return key;
    }

    public User getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    @SearchableProperty(nullValue = "anonymous")
    public String getNickname() {
    	String retVal = null;
    	
    	if(author != null) {
    		retVal = author.getNickname();
    	}

    	return retVal;
    }
    
    public void setAuthor(User author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(Date date) {
        this.date = date;
    }

	public Object clone() throws CloneNotSupportedException {
		Employee clone = (Employee)super.clone();

		return clone;
	}

}