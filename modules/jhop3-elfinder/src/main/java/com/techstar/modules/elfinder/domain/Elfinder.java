package com.techstar.modules.elfinder.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.techstar.modules.springframework.data.jpa.domain.IdEntity;
import com.techstar.modules.utils.Identities;

@Entity
@Table(name = "SS_ELFINDER")
public class Elfinder extends IdEntity {

	private String name;
	private Long size;
	private Date mtime;
	private String mime;
	private Boolean read = true;
	private Boolean write = true;
	private Boolean locked = false;
	private Boolean hidden = false;
	private Integer width = 80;
	private Integer height;
	private String rootDir;
	private String category;

	private Content content;
	private Elfinder parent;
	private Set<Elfinder> childrens = new HashSet<Elfinder>();

	@Column(name = "NAME_", length = 1000)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SIZE_")
	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MTIME_")
	public Date getMtime() {
		return mtime;
	}

	public void setMtime(Date mtime) {
		this.mtime = mtime;
	}

	@Column(name = "MIME_", length = 300)
	public String getMime() {
		return mime;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}

	@Column(name = "READ_")
	public Boolean getRead() {
		return this.read == null ? false : this.read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	@Column(name = "WRITE_")
	public Boolean getWrite() {
		return this.write == null ? false : this.write;
	}

	public void setWrite(Boolean write) {
		this.write = write;
	}

	@Column(name = "LOCKED_")
	public Boolean getLocked() {
		return (this.locked == null ? false : this.locked) || !getWrite();
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	@Column(name = "HIDDEN_")
	public Boolean getHidden() {
		return this.hidden == null ? false : this.hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	@Column(name = "WIDTH_")
	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	@Column(name = "HEIGHT_")
	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	@Column(name = "ROOT_", length = 32)
	public String getRootDir() {
		return this.isRoot() ? this.id : this.rootDir;
	}

	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}
	
	
	@Column(name = "CATEGORY_", length = 200)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	@ManyToOne
	@JoinColumn(name = "PARENT_")
	public Elfinder getParent() {
		return parent;
	}

	public void setParent(Elfinder parent) {
		this.parent = parent;
	}

	@JsonIgnore
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	// @Fetch(FetchMode.SUBSELECT)
	public Set<Elfinder> getChildrens() {
		return childrens;
	}

	public void setChildrens(Set<Elfinder> childrens) {
		this.childrens = childrens;
	}

	@Transient
	public boolean hasParent() {
		return this.parent != null;
	}

	/*
	 * @Transient public boolean hasChildren() { return
	 * CollectionUtils.isNotEmpty(childrens); }
	 */

	@Transient
	public boolean isFolder() {
		return StringUtils.equals("directory", this.mime);
	}

	@Transient
	public boolean isFile() {
		return !isFolder();
	}

	@Transient
	public boolean isRoot() {
		return this.parent == null;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(id == null ? Identities.uuid2() : id).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		boolean flag = false;
		if (obj != null && Elfinder.class.isAssignableFrom(obj.getClass())) {
			Elfinder other = (Elfinder) obj;
			flag = new EqualsBuilder().append(getId(), other.getId()).isEquals();
		}
		return flag;
	}

}
