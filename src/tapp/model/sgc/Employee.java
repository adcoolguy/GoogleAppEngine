package tapp.model.sgc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.apache.tapestry5.beaneditor.Validate;

import com.persistent.utils.excel.ExcelColumn;
import com.persistent.utils.excel.ExcelReport;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
@ExcelReport(reportName="Employee.xls")
public class Employee implements Cloneable, Serializable {

	@Persistent(primaryKey = "true",valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String userId;
	@Persistent
	private String name;
	@Persistent
	private double salary;
	@Persistent	
	private double w4AdditionalWithdraw;
	@Persistent
	private int dependents;
	@Persistent
	private BigDecimal visualAcuity;
	@Persistent	
	private Date birthDate;
	@Persistent
	private Date hireDate;
	@Persistent
	private Date startTime;
	@Persistent
	private long lastTimeEntry;

	@ExcelColumn(label="Id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ExcelColumn(label="User Id")
	public String getUserId() {
		return userId;
	}

	@Validate("required")
	public void setUserId(String userId) {
		this.userId = userId;
	}

	@ExcelColumn(label="Name")	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ExcelColumn(label="Salary")
	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	@ExcelColumn(label="W4")
	public double getW4AdditionalWithdraw() {
		return w4AdditionalWithdraw;
	}

	public void setW4AdditionalWithdraw(double w4AdditionalWithdraw) {
		this.w4AdditionalWithdraw = w4AdditionalWithdraw;
	}

	@ExcelColumn(label="Dependents")
	public int getDependents() {
		return dependents;
	}

	public void setDependents(int dependents) {
		this.dependents = dependents;
	}

	@ExcelColumn(label="Visual Acuity")
	public BigDecimal getVisualAcuity() {
		return visualAcuity;
	}

	public void setVisualAcuity(BigDecimal visualAcuity) {
		this.visualAcuity = visualAcuity;
	}

	@ExcelColumn(label="DOB")
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@ExcelColumn(label="Start Time")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@ExcelColumn(label="Hire Date")
	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	@ExcelColumn(label="Last Time Entry")
	public long getLastTimeEntry() {
		return lastTimeEntry;
	}

	public void setLastTimeEntry(long lastTimeEntry) {
		this.lastTimeEntry = lastTimeEntry;
	}

	public Object clone() throws CloneNotSupportedException {
		Employee clone = (Employee)super.clone();

		return clone;
	}
	
}
