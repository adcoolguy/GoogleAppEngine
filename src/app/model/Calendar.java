package app.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;

import com.google.appengine.api.datastore.Key;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="User", description="Schedule of Movie")
@Searchable(alias = "2sharecal")
@Entity
public class Calendar implements Cloneable, Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;	//caused javax.persistence.PersistenceException: Detected attempt to establish User(172) as the parent of Calendar(184) but the entity identified by Calendar(184) has already been persisted without a parent.  A parent cannot be established or changed once an object has been persisted.
//	@ManyToOne(fetch = FetchType.LAZY)
//	private User parent;
	@SearchableId(name = "id")
	private Long id;
	@SearchableProperty
	private String name;
	@SearchableProperty
	private Date createdDate;
	@SearchableProperty
	private Date modifiedDate;
	@SearchableProperty
	private Date lastAccessed; // updated by hit count/client access
	@SearchableProperty
	private String owner;
	@SearchableProperty
	private String description;
	@SearchableProperty
	private String summary;
	@SearchableProperty
	private String shortUrl;
	@SearchableProperty
	private Long number;
	@SearchableProperty
	private Long hit = -1L;
	private boolean disabled = false;
	//view specific attributes (http://arshaw.com/fullcalendar/docs/event_data/Event_Object/)
	private String title;
	private boolean allDay;
	private String start;
	private String end;
	private Date startDate;
	private Date endDate;
	private String url;
	private String className;
	private boolean editable;
//	private Object source;	Caused by: org.datanucleus.exceptions.ClassNotDetachableException: The class "modelCalendar" is not Detachable. This means that the MetaData for the class did not have the "detachable" attribute set to true.
	private String color;
	private String backgroundColor;
	private String borderColor;
	private String textColor;
	private boolean isRecurring;
	private String recurringPattern;
	private Long movieId;
	private String eventPattern = "0";	//for sorting
	
	public String getEventPattern() {
		return eventPattern;
	}

	public void setEventPattern(String eventPattern) {
		this.eventPattern = eventPattern;
	}
	
	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	@ApiModelProperty(value = "calendar's id", required = true)
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Date getLastAccessed() {
		return lastAccessed;
	}

	public void setLastAccessed(Date lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public Long getHit() {
		return hit;
	}

	public void setHit(Long hit) {
		this.hit = hit;
	}

	public boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	//view specific methods
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean getAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	public String getStart() {
//		String retVal = "";
//		if(start != null && !start.equals("")) { 
//			//just for fullCalendar client
//			DateTimeFormatter format = DateTimeFormat.forPattern("mm/dd/yyyy HH:mm");
//			DateTime time = format.parseDateTime(start);
//			retVal = time.toString();
//		}
//		return retVal;
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
//		String retVal = "";
//		if(end != null && !start.equals("")) { 
//			//just for fullCalendar client
//			DateTimeFormatter format = DateTimeFormat.forPattern("mm/dd/yyyy HH:mm");
//			DateTime time = format.parseDateTime(end);
//			retVal = time.toString();
//		}
//		return retVal;
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public boolean getEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

//	public Object getSource() {
//		return source;
//	}
//
//	public void setSource(Object source) {
//		this.source = source;
//	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public boolean getIsRecurring() {
		return isRecurring;
	}

	public void setIsRecurring(boolean isRecurring) {
		this.isRecurring = isRecurring;
	}

	public String getRecurringPattern() {
		return recurringPattern;
	}

	public void setRecurringPattern(String recurringPattern) {
		this.recurringPattern = recurringPattern;
	}

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	@Override
	public String toString() {
		return "Calendar [key=" + key + ", id=" + id + ", name=" + name
				+ ", createdDate=" + createdDate + ", modifiedDate="
				+ modifiedDate + ", lastAccessed=" + lastAccessed + ", owner="
				+ owner + ", description=" + description + ", summary="
				+ summary + ", shortUrl=" + shortUrl + ", number=" + number
				+ ", hit=" + hit + ", disabled=" + disabled + ", title="
				+ title + ", allDay=" + allDay + ", start=" + start + ", end="
				+ end + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", url=" + url + ", className=" + className + ", editable="
				+ editable + ", color=" + color + ", backgroundColor="
				+ backgroundColor + ", borderColor=" + borderColor
				+ ", textColor=" + textColor + ", isRecurring=" + isRecurring
				+ ", recurringPattern=" + recurringPattern + ", movieId="
				+ movieId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Calendar other = (Calendar) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

//------PROPERTIES_BEGIN------
//------PROPERTIES_END------
