package com.techstar.modules.shiro.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.techstar.modules.springframework.data.jpa.domain.IdEntity;

@MappedSuperclass
public class ShiroUser extends IdEntity {

	private String loginName;
	private String password;
	private String salt;
	private String plainPassword;
	private String name;
	
	public ShiroUser() {
		
	}

	public ShiroUser(String id) {
		setId(id);
	}

	public ShiroUser(String id, String loginName, String password, String name) {
		setId(id);
		this.loginName = loginName;
		this.password = password;
		this.name = name;
	}

	@NotBlank
	@Length(max = 300)
	@Column(name = "NAME",nullable = false, length = 300)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotBlank
	@Length(max = 100)
	@Column(name = "LOGIN_NAME", unique = true, nullable = false, length = 100)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Transient
	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	@Length(max = 50)
	@Column(name = "PASSWORD", nullable = false, length = 50)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Length(max = 30)
	@Column(name = "SALT", nullable = false, length = 30)
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

}
