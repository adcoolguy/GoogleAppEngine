package com.appspot.cloudserviceapi.eo.test;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * CdScreenType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "cd_screen_type", catalog = "eol2")
public class CdScreenType implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private Integer sortOrder;
	private Set<Screen> screens = new HashSet<Screen>(0);

	// Constructors

	/** default constructor */
	public CdScreenType() {
	}

	/** minimal constructor */
	public CdScreenType(String name) {
		this.name = name;
	}

	/** full constructor */
	public CdScreenType(String name, Integer sortOrder, Set<Screen> screens) {
		this.name = name;
		this.sortOrder = sortOrder;
		this.screens = screens;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SORT_ORDER")
	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cdScreenType")
	public Set<Screen> getScreens() {
		return this.screens;
	}

	public void setScreens(Set<Screen> screens) {
		this.screens = screens;
	}

}