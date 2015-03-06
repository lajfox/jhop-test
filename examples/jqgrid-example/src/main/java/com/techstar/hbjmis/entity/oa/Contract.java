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
 * Entity: Contract
 * 
 * @author ZengWenfeng
 */
@Entity
@Table(name = "OA_CONTRACT")
public class Contract extends ActivitiEntity {

	private String name;// 合同名称
	private Double amount;// 合同总金额
	private String firstParty;// 甲方
	private String secondParty;// 乙方

	private Date createTime;// 合同创建时间
	private String userName;// 合同创建人

	@NotEmpty
	@Length(max = 200)
	@Column(length = 200, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotEmpty
	@Length(max = 300)
	@Column(length = 300, nullable = false)
	public String getFirstParty() {
		return firstParty;
	}

	public void setFirstParty(String firstParty) {
		this.firstParty = firstParty;
	}

	@NotEmpty
	@Length(max = 300)
	@Column(length = 300, nullable = false)
	public String getSecondParty() {
		return secondParty;
	}

	public void setSecondParty(String secondParty) {
		this.secondParty = secondParty;
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
