package com.temenos.dbx.product.achservices.dto;

import java.util.List;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BBTemplateRecordDTO implements DBPDTO{

	private static final long serialVersionUID = -7238816122610259276L;

	//@JsonAlias("TemplateRecord_id")
	private long templateRecord_id;
	
	@JsonProperty("TemplateRecord_id")
	private long tempRecord_id;
	
	//@JsonAlias("Record_Name")
	private String record_Name;
	
	@JsonProperty("Record_Name")
	private String rName;
	
	//@JsonAlias("ToAccountNumber")
	private String toAccountNumber;
	
	@JsonProperty("ToAccountNumber")
	private String toAcctNumber;

	
	@JsonAlias({"ABATRCNumber", "abatrcNumber", "abaTrcNumber"})
	@JsonProperty("ABATRCNumber")
	private String abatrcNumber;
	
	//@JsonAlias("Detail_id")
	private String detail_id;
	
	@JsonProperty("Detail_id")
	private String detailId;
	
	//@JsonAlias("Amount")
	private double amount;
	
	@JsonProperty("Amount")
	private double amt;
	
	@JsonProperty("AdditionalInfo")
	private String addInfo;
	
	private String additionalInfo;
	
	//@JsonAlias("EIN")
	private String ein;
	
	@JsonProperty("EIN")
	private String eIN;
	
	private int isZeroTaxDue;
	
	@JsonProperty("IsZeroTaxDue")
	private int zeroTaxDue;

	private String taxType;
	
	//@JsonAlias("TemplateId")
	private long template_id;
	
	@JsonProperty("TemplateId")
	private long tempId;

	private String templateRequestTypeName;

	private String accountType;

	//@JsonAlias("TaxType_id")
	private long taxType_id;
	
	@JsonProperty("TaxType_id")
	private long taxTypeId;
	
	//@JsonAlias("TemplateRequestType_id")
	private long templateRequestType_id;
	
	@JsonProperty("TemplateRequestType_id")
	private long tempRequestType_id;
	
	private int softDelete;
	
	//@JsonAlias("ToAccountType")
	private int toAccountType;

	@JsonProperty("ToAccountType")
	private int toAcctType;
	
	@JsonProperty("SubRecords")
	List<BBTemplateSubRecordDTO> subRecords;
	
	public BBTemplateRecordDTO() {
		super();
	}
	
	public BBTemplateRecordDTO(long templateRecord_id, String record_Name, String toAccountNumber, String abatrcNumber,String taxType,
			String detail_id, double amount, String additionalInfo, String ein, int isZeroTaxDue, long template_id,String templateRequestTypeName,
			long taxType_id,String accountType, long templateRequestType_id, int softDelete, int toAccountType, List<BBTemplateSubRecordDTO> subRecords) {
		super();
		this.templateRecord_id = templateRecord_id;
		this.record_Name = record_Name;
		this.toAccountNumber = toAccountNumber;
		this.abatrcNumber = abatrcNumber;
		this.detail_id = detail_id;
		this.amount = amount;
		this.additionalInfo = additionalInfo;
		this.ein = ein;
		this.isZeroTaxDue = isZeroTaxDue;
		this.template_id = template_id;
		this.taxType_id = taxType_id;
		this.templateRequestType_id = templateRequestType_id;
		this.softDelete = softDelete;
		this.toAccountType = toAccountType;
		this.subRecords = subRecords;
		this.taxType = taxType;
		this.accountType = accountType;
		this.templateRequestTypeName = templateRequestTypeName;
	}
	
	public List<BBTemplateSubRecordDTO> getSubRecords() {
		return subRecords;
	}

	public void setSubRecords(List<BBTemplateSubRecordDTO> subRecords) {
		this.subRecords = subRecords;
	}
	
	public void setZeroTaxDue(int zeroTaxDue) {
		this.zeroTaxDue = 0;
		this.setIsZeroTaxDue(zeroTaxDue);
	}

	public long getTemplateRecord_id() {
		return templateRecord_id;
	}

	public void setTemplateRecord_id(long templateRecord_id) {
		this.templateRecord_id = templateRecord_id;
	}

	public String getRecord_Name() {
		return record_Name;
	}

	public void setRecord_Name(String record_Name) {
		this.record_Name = record_Name;
	}

	public String getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	public String getAbatrcNumber() {
		return abatrcNumber;
	}

	public void setAbatrcNumber(String abatrcNumber) {
		this.abatrcNumber = abatrcNumber;
	}

	public String getDetail_id() {
		return detail_id;
	}

	public void setDetail_id(String detail_id) {
		this.detail_id = detail_id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getEin() {
		return ein;
	}

	public void setEin(String ein) {
		this.ein = ein;
	}

	public void setAddInfo(String addInfo) {
		this.addInfo = null;
		this.setAdditionalInfo(addInfo);
	}

	public int getIsZeroTaxDue() {
		return isZeroTaxDue;
	}

	public void setIsZeroTaxDue(int isZeroTaxDue) {
		this.isZeroTaxDue = isZeroTaxDue;
	}

	public long getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(long template_id) {
		this.template_id = template_id;
	}

	public long getTaxType_id() {
		return taxType_id;
	}

	public void setTaxType_id(long taxType_id) {
		this.taxType_id = taxType_id;
	}

	public long getTemplateRequestType_id() {
		return templateRequestType_id;
	}

	public void setTemplateRequestType_id(long templateRequestType_id) {
		this.templateRequestType_id = templateRequestType_id;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public int getSoftDelete() {
		return softDelete;
	}

	public void setSoftDelete(int softDelete) {
		this.softDelete = softDelete;
	}

	public int getToAccountType() {
		return toAccountType;
	}

	public void setToAccountType(int toAccountType) {
		this.toAccountType = toAccountType;
	}

	public void setTempRecord_id(long tempRecord_id) {
		this.tempRecord_id = 0;
		this.setTemplateRecord_id(tempRecord_id);
	}

	public void setrName(String rName) {
		this.rName = null;
		this.setRecord_Name(rName);
	}

	public void setToAcctNumber(String toAcctNumber) {
		this.toAcctNumber = null;
		this.setToAccountNumber(toAcctNumber);
	}

	public void setDetailId(String detailId) {
		this.detailId = null;
		this.setDetail_id(detailId);
	}

	public void setAmt(double amt) {
		this.amt = 0.0;
		this.setAmount(amt);
	}

	public void seteIN(String eIN) {
		this.eIN = null;
		this.setEin(eIN);
	}

	public void setTempId(long tempId) {
		this.tempId = 0;
		this.setTemplate_id(tempId);
	}

	public long getTaxTypeId() {
		return taxType_id;
	}

	public void setTaxTypeId(long taxTypeId) {
		this.taxTypeId = 0;
		this.setTaxType_id(taxTypeId);
	}

	public void setTempRequestType_id(long tempRequestType_id) {
		this.templateRequestType_id = 0;
		this.setTemplateRequestType_id(tempRequestType_id);
	}

	public void setToAcctType(int toAcctType) {
		this.toAcctType = 0;
		this.setToAccountType(toAcctType);
	}
	public String getTemplateRequestTypeName() {
		return templateRequestTypeName;
	}

	public void setTemplateRequestTypeName(String templateRequestTypeName) {
		this.templateRequestTypeName = templateRequestTypeName;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abatrcNumber == null) ? 0 : abatrcNumber.hashCode());
		result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((detail_id == null) ? 0 : detail_id.hashCode());
		result = prime * result + ((ein == null) ? 0 : ein.hashCode());
		result = prime * result + isZeroTaxDue;
		result = prime * result + ((record_Name == null) ? 0 : record_Name.hashCode());
		result = prime * result + softDelete;
		result = prime * result + (int) (taxType_id ^ (taxType_id >>> 32));
		result = prime * result + (int) (templateRecord_id ^ (templateRecord_id >>> 32));
		result = prime * result + (int) (templateRequestType_id ^ (templateRequestType_id >>> 32));
		result = prime * result + (int) (template_id ^ (template_id >>> 32));
		result = prime * result + ((toAccountNumber == null) ? 0 : toAccountNumber.hashCode());
		result = prime * result + toAccountType;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BBTemplateRecordDTO other = (BBTemplateRecordDTO) obj;
		if (templateRecord_id != other.templateRecord_id)
			return false;
		return true;
	}
	
}
