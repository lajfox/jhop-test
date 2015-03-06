package com.techstar.example.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.techstar.modules.jackson.serializer.JsonDateSerializer;
import com.techstar.modules.jaxb.serializer.JaxbDateSerializer;
import com.techstar.modules.springframework.data.jpa.domain.IdEntity;

@Entity
@Table(name = "SS_BOOK")
@Audited
public class Book extends IdEntity {

	private String author;
	private String title;
	private Double price;
	private Date datepub;
	private String asin;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = JsonDateSerializer.class)
	@XmlJavaTypeAdapter(JaxbDateSerializer.class)
	public Date getDatepub() {
		return datepub;
	}

	public void setDatepub(Date datepub) {
		this.datepub = datepub;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

}
