package com.techstar.modules.elfinder.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "SS_CONTENT")
public class Content {

	private String id;	
	private byte[] content;
	private Elfinder elfinder;

	@Id
	@GeneratedValue(generator = "pkGenerator")
	@GenericGenerator(name = "pkGenerator", strategy = "foreign", parameters = @Parameter(name = "property", value = "elfinder"))
	@Column(name = "ID_", length = 32)
	@Length(max = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Lob
	@Column(name = "CONTENT_")
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	@JsonIgnore
	@OneToOne(optional = false, fetch = FetchType.LAZY, mappedBy = "content", cascade = CascadeType.REFRESH)
	@PrimaryKeyJoinColumn
	public Elfinder getElfinder() {
		return elfinder;
	}

	public void setElfinder(Elfinder elfinder) {
		this.elfinder = elfinder;
	}

}
