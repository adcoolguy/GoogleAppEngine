package tapp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.beaneditor.Width;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;

import com.appspot.cloudserviceapi.data.URLCategory;
import com.google.appengine.api.datastore.Text;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="ServiceRegistry", description="Registry for anything that needs to be accessible via web")
@Searchable(alias="scireg") //lock released for every restart in PMF, due to the above error
@Entity
public class ServiceRegistry implements Cloneable, Serializable {

	@ApiModelProperty(value = "unique id", required = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SearchableId(name = "id")
	private Long id;
	@ApiModelProperty(value = "last updated date", required = false)
    @SearchableProperty
	private Date lastUpdated;	//updated by admin generally
	@ApiModelProperty(value = "last accessed date", required = false)
    @SearchableProperty
	private Date lastAccessed;	//updated by hit count/client access
	@ApiModelProperty(value = "categories", allowableValues = "TECHNOLOGY, RELATIONSHIP, INCOMING, OUTGOING, SCIENCE, BUSINESS, WORLD, SPORTS, ENTERTAINMENT, HEALTH, POLITICS, SOCIETY, GOVERNMENT, CODE, AUDIO, VIDEO, IMAGE, GAME, DALEKJS, PROTRACTOR", required = true)
	//@JsonProperty(value = "enum", required = false)
    @SearchableProperty
	private URLCategory category;
	@ApiModelProperty(value = "service id, unique and primary", required = false)
    @SearchableProperty
	private String service;
	@ApiModelProperty(value = "owner name", required = false)
    @SearchableProperty
	private String owner;
	@ApiModelProperty(value = "project id", required = false)
    @SearchableProperty
	private String project;
	@ApiModelProperty(value = "organization id", required = false)
    @SearchableProperty
	private String organization;
	@ApiModelProperty(value = "url", required = true)
    @SearchableProperty
	private String endpoint;
	@ApiModelProperty(value = "summary", required = false)
    @SearchableProperty //@SearchableMetaData(name = "summary1")
	private String summary;
	@ApiModelProperty(value = "short url", required = false)
    @SearchableProperty
    private String shortUrl;
	@ApiModelProperty(value = "general purpose count", required = false)
    @SearchableProperty
    private Long number;
	@ApiModelProperty(value = "hit count", required = false)
    @SearchableProperty
    private Long hit = -1L;

	@ApiModelProperty(value = "description", required = false)
    @SearchableProperty
    @Transient
	private String description;
	@ApiModelProperty(value = "description (rich text)", required = false)
//    @SearchableProperty	//org.compass.core.mapping.MappingException: No converter defined for type [com.google.appengine.api.datastore.Text] and getter DirectGetter(tapp.model.ServiceRegistry.descriptionText)
    @Basic(fetch=FetchType.EAGER)
    private Text descriptionText;

	@ApiModelProperty(value = "enabled/disabled flag", required = false)
    private boolean disabled = false;
	@ApiModelProperty(value = "use the description instead of the url field as output", required = false)
    private boolean useDescription = false;		//returns the description field instead of endpoint
	@ApiModelProperty(value = "save and display description as html", required = false)
    private boolean useHtml = true;	//returns the endpoint as html content type/to be saved in rich text/html format
	@ApiModelProperty(value = "save only flag", required = false)
    private boolean saveOnly = false;	//stays if the saveOnly is true

	@ApiModelProperty(value = "owner id", required = false)
    private String oid;	//owner's id

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	public void setLastAccessed(Date lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

	public Date getLastAccessed() {
		return lastAccessed;
	}

	public URLCategory getCategory() {
		return category;
	}

	@Validate("required")
	public void setCategory(URLCategory category) {
		this.category = category;
	}

	public String getService() {
		return service;
	}

	//@Validate("required,regexp=[^a-zA-Z0-9_.]+")
	@Validate("required")
	public void setService(String service) {
		this.service = service;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	@Width(value = 40)
	public String getEndpoint() {
		return endpoint;
	}

	@Validate("required")
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getDescription() {
		String retVal = "";
		if(descriptionText != null && descriptionText.getValue() != null) {
			retVal = descriptionText.getValue();
		}
		description = retVal;

		return description;
	}

	public void setDescription(String description) {
		if(description != null) {
			descriptionText = new Text(description);

			//TBD - Caused by: java.lang.IllegalArgumentException: description: String properties must be 500 characters or less.  Instead, use com.google.appengine.api.datastore.Text, which can store strings of any length.
			//but break Compass search (NPE)
			if(description.length() >= 500) {
				this.description = description.substring(0,499);	//allow Compass to index first 500 characters only
			} else {
				this.description = description;
			}
		} else {
			descriptionText = new Text("");
			this.description = "";
		}
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getOrganization() {
		return organization;
	}
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public Long getHit() {
		return hit;
	}

	public void setHit(Long hit) {
		this.hit = hit;
	}

	//TBD - need a primitive boolean for Tapestry but datastore needs Boolean to avoid error
	public boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean getUseDescription() {
		return useDescription;
	}

	public void setUseDescription(boolean useDescription) {
		this.useDescription = useDescription;
	}

	public boolean getUseHtml() {
		return useHtml;
	}

	public void setUseHtml(boolean useHtml) {
		this.useHtml = useHtml;
	}

	public boolean getSaveOnly() {
		return saveOnly;
	}

	public void setSaveOnly(boolean saveOnly) {
		this.saveOnly = saveOnly;
	}

	@Override
	public String toString() {
		return "ServiceRegistry [category=" + category + ", service=" + service
				+ ", endpoint=" + endpoint + ", description=" + description
				+ ", summary=" + summary + ", hit=" + hit + ", disabled="
				+ disabled + ", useDescription=" + useDescription
				+ ", useHtml=" + useHtml + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((descriptionText == null) ? 0 : descriptionText.hashCode());
//		result = prime * result
//				+ ((disabled == null) ? 0 : disabled.hashCode());
		result = prime * result
				+ ((endpoint == null) ? 0 : endpoint.hashCode());
		result = prime * result + ((hit == null) ? 0 : hit.hashCode());
		result = prime * result + ((service == null) ? 0 : service.hashCode());
		result = prime * result + ((summary == null) ? 0 : summary.hashCode());
//		result = prime * result
//				+ ((useDescription == null) ? 0 : useDescription.hashCode());
//		result = prime * result + ((useHtml == null) ? 0 : useHtml.hashCode());
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
		ServiceRegistry other = (ServiceRegistry) obj;
		if (category != other.category)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (descriptionText == null) {
			if (other.descriptionText != null)
				return false;
		} 
		else if (!descriptionText.equals(other.descriptionText))
			return false;
//		if (disabled == null) {
//			if (other.disabled != null)
//				return false;
//		} else if (!disabled.equals(other.disabled))
//			return false;
		if (endpoint == null) {
			if (other.endpoint != null)
				return false;
		} else if (!endpoint.equals(other.endpoint))
			return false;
		if (hit == null) {
			if (other.hit != null)
				return false;
		} else if (!hit.equals(other.hit))
			return false;
		if (service == null) {
			if (other.service != null)
				return false;
		} else if (!service.equals(other.service))
			return false;
		if (summary == null) {
			if (other.summary != null)
				return false;
		} else if (!summary.equals(other.summary))
			return false;
//		if (useDescription == null) {
//			if (other.useDescription != null)
//				return false;
//		} else if (!useDescription.equals(other.useDescription))
//			return false;
//		if (useHtml == null) {
//			if (other.useHtml != null)
//				return false;
//		} else if (!useHtml.equals(other.useHtml))
//			return false;
		return true;
	}

	public Object clone() throws CloneNotSupportedException {
		ServiceRegistry clone = (ServiceRegistry)super.clone();

		return clone;
	}
	
}
