package com.techstar.sys.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import com.techstar.modules.springframework.data.jpa.domain.IdEntity;
import com.techstar.modules.utils.Identities;

@Entity
@Table(name = "sys_dict_type")
public class SystemDicType extends IdEntity implements Serializable {

	private static final long serialVersionUID = 797450756890049279L;

	private String sign;// 字典类型标识

	private String typename;// 字典类型名称

	private Set<SystemDicItem> systemDicItems = Sets.<SystemDicItem>newHashSet();// 字典项数组

	@JsonIgnore
	@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, mappedBy = "systemDicType")
	// 这里配置关系，并且确定关系维护端和被维护端。mappBy表示关系被维护端，只有关系端有权去更新外键。这里还有注意OneToMany默认的加载方式是赖加载。当看到设置关系中最后一个单词是Many，那么该加载默认为懒加载
	public Set<SystemDicItem> getSystemDicItems() {
		return systemDicItems;
	}

	public void setSystemDicItems(Set<SystemDicItem> systemDicItems) {
		this.systemDicItems = systemDicItems;
	}

	@NotBlank
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@NotBlank
	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	@Override
	public boolean equals(Object obj) {
		boolean flag = false;
		if (obj != null && SystemDicType.class.isAssignableFrom(obj.getClass())) {
			SystemDicType f = (SystemDicType) obj;
			flag = new EqualsBuilder().append(id, f.getId()).isEquals();
		}
		return flag;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(id == null ? Identities.uuid2() : id).toHashCode();
	}

}
