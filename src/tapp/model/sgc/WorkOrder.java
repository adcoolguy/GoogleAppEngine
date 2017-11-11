package tapp.model.sgc;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.persistent.utils.excel.ExcelColumn;
import com.persistent.utils.excel.ExcelReport;

@PersistenceCapable(identityType = IdentityType.APPLICATION
// , objectIdClass = WorkOrderComposedIdKey.class
)
@ExcelReport(reportName="WorkOrder.xls")
public class WorkOrder implements Cloneable, Serializable {

	@Persistent
	private String title;
	// @PrimaryKey
	private String lastName;
	@Persistent
	// @PrimaryKey
	private String firstName;
	@Persistent
	private String email;
	@Persistent(primaryKey = "true",valueStrategy=IdGeneratorStrategy.IDENTITY)
//	private String id;	//use this date type under purge mode
	private Long id;
	// private int phone;
	@Persistent
	private String phone;
	@Persistent
	private int alternatePhone;
	@Persistent
	private Date dateRequested;
	@Persistent
	private Date datePerformed;
	@Persistent
	private String address1;
	@Persistent
	private String address2;
	@Persistent
	private String city;
	@Persistent
	private String state;
	@Persistent
	private String postal;
	@Persistent
	private String country;

	//=== optional fields
	@Persistent
	private String specialInstruction;
	@Persistent
	private int familySize;
	@Persistent
	private int children;
	@Persistent
	private String childrenInfo;	//e.g. children age(s)
	@Persistent
	private boolean hasPet;
	@Persistent
	private String petInfo;		//TODO should be Pet object if GAE supports it
	@Persistent
	private String petComments;
	@Persistent
	@Column(allowsNull="true")
	private boolean allergic;
	@Persistent
	private String allergicInfo;
	@Persistent
	private String residenceType;
	@Persistent
	private int bedRoom;
	@Persistent
	private int bathRoom;
	@Persistent
	private String room1Info;
	@Persistent
	private String room2Info;
	@Persistent
	private String room3Info;
	@Persistent
	private String primaryCleaningReason;
	@Persistent
	private String primaryCleaningComments;
	
	@Persistent
	private String addtionalService1;
	@Persistent
	private String addtionalService2;
	@Persistent
	private String addtionalService3;
	@Persistent
	private String addtionalService4;
	@Persistent
	private String addtionalService5;
	@Persistent
	private String addtionalServiceComments;
	
	@Persistent
	private int hoursSpent;

	@Persistent
	private String updatedBy;
	@Persistent
	private Date updatedDate;
	@Persistent
	private String completedNote;
	
//	@Validate("required,minlength=10")
	public void setId(Long id) {
		this.id = id;
	}

	@ExcelColumn(label="Primary Phone")
	public Long getId() {
		return id;
	}

	//purge mode only
//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
	
	@ExcelColumn(label="Last Name")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@ExcelColumn(label="First Name")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@ExcelColumn(label="E-Mail")	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@ExcelColumn(label="Phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	// public int getPhone() {
	// return phone;
	// }
	// public void setPhone(int phone) {
	// this.phone = phone;
	// }
	@ExcelColumn(label="Alternate Phone")	
	public int getAlternatePhone() {
		return alternatePhone;
	}

	public void setAlternatePhone(int alternatePhone) {
		this.alternatePhone = alternatePhone;
	}

	@ExcelColumn(label="Date Requested")	
	public Date getDateRequested() {
		return dateRequested;
	}

	public void setDateRequested(Date dateRequested) {
		this.dateRequested = dateRequested;
	}

	@ExcelColumn(label="Date Performed")	
	public Date getDatePerformed() {
		return datePerformed;
	}

	public void setDatePerformed(Date datePerformed) {
		this.datePerformed = datePerformed;
	}

	@ExcelColumn(label="Address 1")
	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	@ExcelColumn(label="Address 2")	
	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	@ExcelColumn(label="City")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@ExcelColumn(label="State")	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@ExcelColumn(label="Postal")
	public String getPostal() {
		return postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	@ExcelColumn(label="Country")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}	

	@ExcelColumn(label="Title")	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@ExcelColumn(label="Special Instruction")	
	public String getSpecialInstruction() {
		return specialInstruction;
	}

	public void setSpecialInstruction(String specialInstruction) {
		this.specialInstruction = specialInstruction;
	}

	@ExcelColumn(label="Family Size")	
	public int getFamilySize() {
		return familySize;
	}

	public void setFamilySize(int familySize) {
		this.familySize = familySize;
	}

	@ExcelColumn(label="Children")	
	public int getChildren() {
		return children;
	}

	public void setChildren(int children) {
		this.children = children;
	}

	@ExcelColumn(label="Children Info")	
	public String getChildrenInfo() {
		return childrenInfo;
	}

	public void setChildrenInfo(String childrenInfo) {
		this.childrenInfo = childrenInfo;
	}

	@ExcelColumn(label="Has Pet?")
	public boolean isHasPet() {
		return hasPet;
	}

	public void setHasPet(boolean hasPet) {
		this.hasPet = hasPet;
	}

	@ExcelColumn(label="Pet Info")
	public String getPetInfo() {
		return petInfo;
	}

	public void setPetInfo(String petInfo) {
		this.petInfo = petInfo;
	}

	@ExcelColumn(label="Comments about Pet")	
	public String getPetComments() {
		return petComments;
	}

	public void setPetComments(String petComments) {
		this.petComments = petComments;
	}

	@ExcelColumn(label="Has allergy?")	
	public boolean isAllergic() {
		return allergic;
	}

	public void setAllergic(boolean allergic) {
		this.allergic = allergic;
	}

	@ExcelColumn(label="Allergy Info")
	public String getAllergicInfo() {
		return allergicInfo;
	}

	public void setAllergicInfo(String allergicInfo) {
		this.allergicInfo = allergicInfo;
	}

	@ExcelColumn(label="Residence Type")	
	public String getResidenceType() {
		return residenceType;
	}

	public void setResidenceType(String residenceType) {
		this.residenceType = residenceType;
	}

	@ExcelColumn(label="#Bed Room")	
	public int getBedRoom() {
		return bedRoom;
	}

	public void setBedRoom(int bedRoom) {
		this.bedRoom = bedRoom;
	}

	@ExcelColumn(label="#Bath Room")	
	public int getBathRoom() {
		return bathRoom;
	}

	public void setBathRoom(int bathRoom) {
		this.bathRoom = bathRoom;
	}

	@ExcelColumn(label="Room1 Info")
	public String getRoom1Info() {
		return room1Info;
	}

	public void setRoom1Info(String room1Info) {
		this.room1Info = room1Info;
	}

	@ExcelColumn(label="Room2 Info")	
	public String getRoom2Info() {
		return room2Info;
	}

	public void setRoom2Info(String room2Info) {
		this.room2Info = room2Info;
	}

	@ExcelColumn(label="Room3 Info")
	public String getRoom3Info() {
		return room3Info;
	}

	public void setRoom3Info(String room3Info) {
		this.room3Info = room3Info;
	}

	@ExcelColumn(label="Primary Reason")	
	public String getPrimaryCleaningReason() {
		return primaryCleaningReason;
	}

	public void setPrimaryCleaningReason(String primaryCleaningReason) {
		this.primaryCleaningReason = primaryCleaningReason;
	}

	@ExcelColumn(label="Primary Comments")	
	public String getPrimaryCleaningComments() {
		return primaryCleaningComments;
	}

	public void setPrimaryCleaningComments(String primaryCleaningComments) {
		this.primaryCleaningComments = primaryCleaningComments;
	}

	@ExcelColumn(label="Additional Service1")
	public String getAddtionalService1() {
		return addtionalService1;
	}

	public void setAddtionalService1(String addtionalService1) {
		this.addtionalService1 = addtionalService1;
	}

	@ExcelColumn(label="Additional Service2")
	public String getAddtionalService2() {
		return addtionalService2;
	}

	public void setAddtionalService2(String addtionalService2) {
		this.addtionalService2 = addtionalService2;
	}

	@ExcelColumn(label="Additional Service3")	
	public String getAddtionalService3() {
		return addtionalService3;
	}

	public void setAddtionalService3(String addtionalService3) {
		this.addtionalService3 = addtionalService3;
	}

	@ExcelColumn(label="Additional Service4")
	public String getAddtionalService4() {
		return addtionalService4;
	}

	public void setAddtionalService4(String addtionalService4) {
		this.addtionalService4 = addtionalService4;
	}

	@ExcelColumn(label="Additional Service5")	
	public String getAddtionalService5() {
		return addtionalService5;
	}

	public void setAddtionalService5(String addtionalService5) {
		this.addtionalService5 = addtionalService5;
	}

	@ExcelColumn(label="Additional Service Comments")	
	public String getAddtionalServiceComments() {
		return addtionalServiceComments;
	}

	public void setAddtionalServiceComments(String addtionalServiceComments) {
		this.addtionalServiceComments = addtionalServiceComments;
	}
	
	@ExcelColumn(label="Hours Spent")
	public int getHoursSpent() {
		return hoursSpent;
	}

	public void setHoursSpent(int hoursSpent) {
		this.hoursSpent = hoursSpent;
	}
	
	@ExcelColumn(label="Updated By")
	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@ExcelColumn(label="Updated Date")
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	@ExcelColumn(label="Completed Note")
	public String getCompletedNote() {
		return completedNote;
	}

	public void setCompletedNote(String completedNote) {
		this.completedNote = completedNote;
	}

	public Object clone() throws CloneNotSupportedException {
		WorkOrder clone = (WorkOrder)super.clone();

		return clone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
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
		WorkOrder other = (WorkOrder) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WorkOrder [address1=" + address1 + ", address2=" + address2
				+ ", addtionalService1=" + addtionalService1
				+ ", addtionalService2=" + addtionalService2
				+ ", addtionalService3=" + addtionalService3
				+ ", addtionalService4=" + addtionalService4
				+ ", addtionalService5=" + addtionalService5
				+ ", addtionalServiceComments=" + addtionalServiceComments
				+ ", allergic=" + allergic + ", allergicInfo=" + allergicInfo
				+ ", alternatePhone=" + alternatePhone + ", bathRoom="
				+ bathRoom + ", bedRoom=" + bedRoom + ", children=" + children
				+ ", childrenInfo=" + childrenInfo + ", city=" + city
				+ ", country=" + country + ", datePerformed=" + datePerformed
				+ ", dateRequested=" + dateRequested + ", email=" + email
				+ ", familySize=" + familySize + ", firstName=" + firstName
				+ ", hasPet=" + hasPet + ", hoursSpent=" + hoursSpent + ", id="
				+ id + ", lastName=" + lastName + ", petComments="
				+ petComments + ", petInfo=" + petInfo + ", phone=" + phone
				+ ", postal=" + postal + ", primaryCleaningComments="
				+ primaryCleaningComments + ", primaryCleaningReason="
				+ primaryCleaningReason + ", residenceType=" + residenceType
				+ ", room1Info=" + room1Info + ", room2Info=" + room2Info
				+ ", room3Info=" + room3Info + ", specialInstruction="
				+ specialInstruction + ", state=" + state + ", title=" + title
				+ ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate
				+ "]";
	}
	
	
}
