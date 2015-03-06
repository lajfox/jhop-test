package com.techstar.hbjmis.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.techstar.modules.jackson.serializer.JsonDateSerializer;
import com.techstar.modules.jaxb.serializer.JaxbDateSerializer;
import com.techstar.modules.springframework.data.jpa.domain.IdEntity;

/**
 * 发票
 * 
 * @author sundoctor
 * 
 */
@Entity
@Table(name = "SS_INVOICE")
public class Invoice extends IdEntity {

	private Integer invno;

	private Date invdate;
	private String name;
	private Double amount;
	private Double tax;
	private Double total;
	private String note;
	private String closed;
	private String shipvia;

	public Integer getInvno() {
		return invno;
	}

	public void setInvno(Integer invno) {
		this.invno = invno;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(JaxbDateSerializer.class)
	public Date getInvdate() {
		return invdate;
	}

	public void setInvdate(Date invdate) {
		this.invdate = invdate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getClosed() {
		return closed;
	}

	public void setClosed(String closed) {
		this.closed = closed;
	}

	public String getShipvia() {
		return shipvia;
	}

	public void setShipvia(String shipvia) {
		this.shipvia = shipvia;
	}

}
