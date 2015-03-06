package com.techstar.hbjmis.entity.oa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.techstar.modules.activiti.domain.ActivitiEntity;
import com.techstar.modules.jackson.serializer.JsonDateTimeSerializer;

/**
 * Entity: Order
 * 
 * @author ZengWenfeng
 */
@Entity
@Table(name = "OA_ORDER")
public class Order extends ActivitiEntity {

	private String name;// 订单名称
	private Long quantity;// 订单数量
	private Double amount;// 订单总金额

	private Date createTime;// 订单创建时间
	private String userName;// 订单创建人

	@NotEmpty
	@Length(max = 200)
	@Column(length = 200, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	@Column(nullable = false)
	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	@NotNull
	@Column(nullable = false)
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME", nullable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Length(max = 50)
	@Column(length = 50, nullable = false)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
