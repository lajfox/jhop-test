package com.techstar.hbjmis.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.techstar.modules.springframework.data.jpa.domain.IdEntity;

/**
 * 发票
 * 
 * @author sundoctor
 * 
 */
@Entity
@Table(name = "SS_ITEM")
public class Item extends IdEntity {

	private Integer num;
	private String item;
	private Double qty;
	private Double unit;
	private Double linetotal;
	private String invoice;

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public Double getUnit() {
		return unit;
	}

	public void setUnit(Double unit) {
		this.unit = unit;
	}

	public Double getLinetotal() {
		return linetotal;
	}

	public void setLinetotal(Double linetotal) {
		this.linetotal = linetotal;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	
	

}
