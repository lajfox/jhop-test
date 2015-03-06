package com.techstar.modules.springframework.data.jpa.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;
import com.techstar.modules.utils.Identities;

@MappedSuperclass
public abstract class JsonTree<T extends IdEntity> extends IdEntity implements Comparable<JsonTree<T>> {

	private Integer level = 0;
	private Boolean leaf = true;
	private Boolean expanded = false;
	private Boolean loaded = true;
	private Double orderno;

	protected T parent;
	protected Set<T> childrens = Sets.<T> newHashSet();

	@JsonProperty("level_")
	@Column(name = "level_")
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@JsonProperty("leaf_")
	@Column(name = "leaf_")
	public Boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	@JsonProperty("expanded_")
	@Column(name = "expanded_")
	public Boolean getExpanded() {
		return expanded;
	}

	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}

	@JsonProperty("loaded_")
	@Column(name = "loaded_")
	public Boolean getLoaded() {
		return loaded;
	}

	public void setLoaded(Boolean loaded) {
		this.loaded = loaded;
	}

	@JsonProperty("orderno")
	@Column(name = "order_")
	public Double getOrderno() {
		return orderno;
	}

	public void setOrderno(Double orderno) {
		this.orderno = orderno;
	}

	// @JsonIgnore
	@JsonProperty("parentx")
	@ManyToOne
	@JoinColumn(name = "PARENT_")
	public T getParent() {
		return parent;
	}

	public void setParent(T parent) {
		this.parent = parent;
	}

	@JsonIgnore
	@OneToMany(mappedBy = "parent", cascade = { CascadeType.REMOVE })
	@Fetch(FetchMode.SUBSELECT)
	public Set<T> getChildrens() {
		return childrens;
	}

	public void setChildrens(Set<T> childrens) {
		this.childrens = childrens;
	}

	public boolean hasChildren() {
		return CollectionUtils.isNotEmpty(childrens);
	}

	public boolean hasParent() {
		return parent != null;
	}

	@JsonProperty("parent")
	@Transient
	public String getParentId() {
		return this.parent == null ? null : ((IdEntity) this.parent).getId();
	}

	@JsonProperty("level")
	@Transient
	public int getLevelx() {
		return level == null ? 0 : level;
	}

	@JsonProperty("isLeaf")
	@Transient
	public boolean isLeafx() {
		return leaf == null ? true : leaf;
	}

	@JsonProperty("expanded")
	@Transient
	public boolean isExpandedx() {
		return expanded == null ? false : expanded;
	}

	@JsonProperty("loaded")
	@Transient
	public boolean isLoadedx() {
		return loaded == null ? true : loaded;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		boolean flag = false;
		if (obj != null && JsonTree.class.isAssignableFrom(obj.getClass())) {
			JsonTree<T> f = (JsonTree<T>) obj;
			flag = new EqualsBuilder().append(StringUtils.isEmpty(id) ? Identities.uuid() : id, f.getId()).isEquals();
		}
		return flag;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(StringUtils.isEmpty(id) ? Identities.uuid() : id).toHashCode();
	}

	@Override
	public int compareTo(JsonTree<T> obj) {
		int flag = -1;
		if (obj != null && JsonTree.class.isAssignableFrom(obj.getClass())) {
			flag = new CompareToBuilder().append(orderno == null ? 0 : orderno,
					obj.getOrderno() == null ? 0 : obj.getOrderno()).toComparison();
		}
		return flag;
	}

}
