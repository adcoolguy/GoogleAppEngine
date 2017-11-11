package app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

//import passwordchange.core.UserSecurityQuestion;

import com.google.appengine.api.datastore.Key;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="User", description="Owner or parent of the Movie")
@Entity
public class User implements Cloneable, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
    //just for cloudendpoint
    private Long id;
	private String name;
    @OneToMany(cascade = CascadeType.ALL 
//    		, fetch = FetchType.LAZY
    		, fetch = FetchType.EAGER	//TODO to be turned off once the paginated movie query in MovieHandler is done
//    		, orphanRemoval = true
//    		, mappedBy="parent"		//memory intensive!!!
    		)	//FetchType.LAZY was not working for me (JPA1)!
	private List<Movie> movie = new ArrayList<Movie>();;
    @OneToMany(cascade = CascadeType.ALL
    		, fetch = FetchType.EAGER
//    		, orphanRemoval = true
//    		, mappedBy="parent"		//memory intensive!!!
    		)
	private List<Calendar> calendar = new ArrayList<Calendar>();
//    @OneToMany(cascade = CascadeType.ALL
//    		, fetch = FetchType.LAZY
//    		//, orphanRemoval = true
//    		//, mappedBy="parent"	//memory intensive!!!
//    		)
//	private List<UserSecurityQuestion> securityQuestion = new ArrayList<UserSecurityQuestion>();

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	@ApiModelProperty(value = "user's id", required = true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Movie> getMovie() {
		return movie;
	}

	public void setMovie(List<Movie> movie) {
		this.movie = movie;
	}

	public List<Calendar> getCalendar() {
		return calendar;
	}

	public void setCalendar(List<Calendar> calendar) {
		this.calendar = calendar;
	}

//	public List<UserSecurityQuestion> getSecurityQuestion() {
//		return securityQuestion;
//	}
//
//	public void setSecurityQuestion(List<UserSecurityQuestion> securityQuestion) {
//		this.securityQuestion = securityQuestion;
//	}
	
	@Override
	public String toString() {
		return "User [key=" + key + ", id=" + id + ", name=" + name + "]";
//		return "User [id=" + id + ", name=" + name + "]";
	}
	
}
