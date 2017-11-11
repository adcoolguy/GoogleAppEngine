package tapp.model.sci;

import java.io.Serializable;
import java.util.Random;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.apache.tapestry5.beaneditor.Validate;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Searchable(alias="fiostoken")
public class FiOSToken implements Cloneable, Serializable {

	@Persistent(primaryKey = "true",valueStrategy=IdGeneratorStrategy.IDENTITY)
    @SearchableId(name = "id")
	private Long id;
	@Persistent
    @SearchableProperty
	private String firstName;
	@Persistent
    @SearchableProperty
	private String lastName;
	@Persistent
    @SearchableProperty
	private String userId;
	@Persistent
	private String magicKey;
	private Long passcode;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUserId() {
		return userId;
	}

	@Validate("required")
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMagicKey() {
		return magicKey;
	}

	@Validate("required")
	public void setMagicKey(String magicKey) {
		this.magicKey = magicKey;
	}
	
	public Long getPasscode() {
		return passcode;
	}

	public void setPasscode(Long passcode) {
		this.passcode = passcode;
	}

	private long getMagicNumber(String src) {
		long retVal = 0;

		if (src != null) {
			for (int i = 0; i < src.length(); i++) {
				retVal += src.charAt(i);
			}
			Random generator = new Random(retVal);
			retVal = generator.nextInt(1000) + 1000;
		}
		
		return retVal;
	}
	
	public Long getGeneratedPasscode() {
		Long retVal = 0L;

		if(userId != null && !"".equals(userId) && magicKey != null && !"".equals(magicKey)) {
			retVal = getMagicNumber(userId + magicKey);
		}

		return retVal;
	}

	public Object clone() throws CloneNotSupportedException {
		FiOSToken clone = (FiOSToken)super.clone();

		return clone;
	}
	
}
