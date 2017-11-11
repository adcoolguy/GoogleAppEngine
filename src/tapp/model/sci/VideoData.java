package tapp.model.sci;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;

import com.appspot.cloudserviceapi.sci.data.ProductInterested;
import com.appspot.cloudserviceapi.sci.data.ServiceInterested;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Searchable(alias="videodata")
public class VideoData implements Cloneable, Serializable {

	@Persistent(primaryKey = "true",valueStrategy=IdGeneratorStrategy.IDENTITY)
    @SearchableId(name = "videoId")
	private Long id;
	@Persistent
    @SearchableProperty
	private String title;
	@Persistent
    @SearchableProperty
	private String videoId;
	@Persistent
    @SearchableProperty
	private String description;
	@Persistent
    @SearchableProperty
	private Date showDate;
	@Persistent
    @SearchableProperty
	private Long startTime;
	@Persistent
    @SearchableProperty
	private int duration;
	@Persistent
    @SearchableProperty
	private boolean timeBased;
	@Persistent
    @SearchableProperty
	private boolean active;
	@Persistent
    @SearchableProperty
	private ProductInterested productILike;
	@Persistent
    @SearchableProperty
	private ServiceInterested serviceILike;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}	

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getShowDate() {
		return showDate;
	}

	public void setShowDate(Date showDate) {
		this.showDate = showDate;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean isTimeBased() {
		return timeBased;
	}

	public void setTimeBased(boolean timeBased) {
		this.timeBased = timeBased;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public ProductInterested getProductILike() {
		return productILike;
	}

	public void setProductILike(ProductInterested productILike) {
		this.productILike = productILike;
	}

	public ServiceInterested getServiceILike() {
		return serviceILike;
	}

	public void setServiceILike(ServiceInterested serviceILike) {
		this.serviceILike = serviceILike;
	}

	public Object clone() throws CloneNotSupportedException {
		VideoData clone = (VideoData)super.clone();

		return clone;
	}
	
}
