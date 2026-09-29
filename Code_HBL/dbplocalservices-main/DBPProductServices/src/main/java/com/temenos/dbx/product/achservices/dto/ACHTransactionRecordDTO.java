package com.temenos.dbx.product.achservices.dto;

import java.util.List;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author KH2317
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 */
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ACHTransactionRecordDTO implements DBPDTO {

	private static final long serialVersionUID = 5286556048108184154L;

	//@JsonAlias({"TransactionRecord_id"})
	private long transactionRecord_id;
	
	@JsonProperty("TransactionRecord_id")
	private long trRecordId;	
	
	//@JsonAlias({"ToAccountNumber"})
	private String toAccountNumber;
	
	@JsonProperty("ToAccountNumber")
	private String toAccNum;	
	
	//@JsonAlias({"ToAccountType"})
	private long toAccountType;
	
	@JsonProperty("ToAccountType")
	private long toAccTyp;	
	
	//@JsonAlias({"ABATRCNumber"})
	private String abstractNumber;
	
	@JsonProperty("ABATRCNumber")
	private String absNum;	
	
	//@JsonAlias({"Detail_id"})
	private String detail_id;
		
	@JsonProperty("Detail_id")
	private String detlID;	
	
	//@JsonAlias({"Amount"})
	private double amount;
	
	@JsonProperty("Amount")
	private double amnt;	
	
	//@JsonAlias({"AdditionalInfo"})
	private String additionalInfo;
	
	@JsonProperty("AdditionalInfo")
	private String adnlInfo;	
	
	@JsonAlias({"eIN"})
	@JsonProperty("EIN")
	private String ein;

	
	//@JsonAlias({"IsZeroTaxDue"})
	private int isZeroTaxDue;
	
	@JsonProperty("IsZeroTaxDue")
	private int isZroTaxDu;	
	
	//@JsonAlias({"TaxType_id"})
	private long taxType_id;
	
	@JsonProperty("TaxType_id")
	private long txTypId;	
	
	//@JsonAlias({"Transaction_id"})
	private long transaction_id;
	
	@JsonProperty("Transaction_id")
	private long trId;	
	
	private int softDelete;
	
	//@JsonAlias({"TemplateRequestType_id"})
	private long templateRequestType_id;
	
	@JsonProperty("TemplateRequestType_id")
	private long teReqTypId;	
	
	//@JsonAlias({"Record_Name"})
	private String record_Name;
	
	@JsonProperty("Record_Name")
	private String recrdNam;

	private String accountType;

	private String templateRequestTypeName;

	private String taxType;

	private String abatrcNumber;
	
	@JsonProperty("SubRecords")
	List<ACHTransactionSubRecordDTO> subRecords;	
		
	public ACHTransactionRecordDTO() {
		super();
	}
	
	

	public ACHTransactionRecordDTO(List<ACHTransactionSubRecordDTO> subRecords) {
		super();
		this.subRecords = subRecords;
	}


	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getTemplateRequestTypeName() {
		return templateRequestTypeName;
	}

	public void setTemplateRequestTypeName(String templateRequestTypeName) {
		this.templateRequestTypeName = templateRequestTypeName;
	}

	public String getTaxType() {
		return taxType;
	}

	public String getAbatrcNumber() {
		return abatrcNumber;
	}

	public void setAbatrcNumber(String abatrcNumber) {
		this.abatrcNumber = abatrcNumber;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}


	public ACHTransactionRecordDTO(long transactionRecord_id, String toAccountNumber, long toAccountType,
								   String abstractNumber, String detail_id, double amount, String additionalInfo, String ein, int isZeroTaxDue,
								   long taxType_id, long transaction_id, int softDelete, long templateRequestType_id, String record_Name, List<ACHTransactionSubRecordDTO> subRecords,
								   String accountType, String templateRequestTypeName, String taxType, String abatrcNumber) {
		super();
		this.transactionRecord_id = transactionRecord_id;
		this.toAccountNumber = toAccountNumber;
		this.toAccountType = toAccountType;
		this.abstractNumber = abstractNumber;
		this.detail_id = detail_id;
		this.amount = amount;
		this.additionalInfo = additionalInfo;
		this.ein = ein;
		this.isZeroTaxDue = isZeroTaxDue;
		this.taxType_id = taxType_id;
		this.transaction_id = transaction_id;
		this.softDelete = softDelete;
		this.templateRequestType_id = templateRequestType_id;
		this.record_Name = record_Name;
		this.subRecords = subRecords;
		this.accountType = accountType;
		this.templateRequestTypeName = templateRequestTypeName;
		this.taxType = taxType;
		this.abatrcNumber = abatrcNumber;
	}

	public long getTransactionRecord_id() {
		return transactionRecord_id;
	}

	public void setTransactionRecord_id(long transactionRecord_id) {
		this.transactionRecord_id = transactionRecord_id;
	}

	public String getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	public long getToAccountType() {
		return toAccountType;
	}

	public void setToAccountType(long toAccountType) {
		this.toAccountType = toAccountType;
	}

	public String getAbstractNumber() {
		return abstractNumber;
	}

	public void setAbstractNumber(String abstractNumber) {
		this.abstractNumber = abstractNumber;
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

	public int getIsZeroTaxDue() {
		return isZeroTaxDue;
	}

	public void setIsZeroTaxDue(int isZeroTaxDue) {
		this.isZeroTaxDue = isZeroTaxDue;
	}

	public long getTaxType_id() {
		return taxType_id;
	}

	public void setTaxType_id(long taxType_id) {
		this.taxType_id = taxType_id;
	}

	public long getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(long transaction_id) {
		this.transaction_id = transaction_id;
	}

	public int getSoftDelete() {
		return softDelete;
	}

	public void setSoftDelete(int softDelete) {
		this.softDelete = softDelete;
	}

	public long getTemplateRequestType_id() {
		return templateRequestType_id;
	}

	public void setTemplateRequestType_id(long templateRequestType_id) {
		this.templateRequestType_id = templateRequestType_id;
	}

	public String getRecord_Name() {
		return record_Name;
	}

	public void setRecord_Name(String record_Name) {
		this.record_Name = record_Name;
	}	

	public void setTrRecordId(long trRecordId) {
		this.trRecordId = 0;
		this.setTransactionRecord_id(trRecordId);
	}

	public void setToAccNum(String toAccNum) {
		this.toAccNum = null;
		this.setToAccountNumber(toAccNum);
	}

	public void setToAccTyp(long toAccTyp) {
		this.toAccTyp = 0;
		this.setToAccountType(toAccTyp);
	}

	public void setAbsNum(String absNum) {
		this.absNum = null;
		this.setAbstractNumber(absNum);
	}

	public void setDetlID(String detlID) {
		this.detlID = null;
		this.setDetail_id(detlID);
	}

	public void setAmnt(double amnt) {
		this.amnt = 0;
		this.setAmount(amnt);
	}

	public void setAdnlInfo(String adnlInfo) {
		this.adnlInfo = null;
		this.setAdditionalInfo(adnlInfo);
	}

	public void setIsZroTaxDu(int isZroTaxDu) {
		this.isZroTaxDu = 0;
		this.setIsZeroTaxDue(isZroTaxDu);
	}

	public void setTxTypId(long txTypId) {
		this.txTypId = 0;
		this.setTaxType_id(txTypId);
	}

	public void setTrId(long trId) {
		this.trId = 0;
		this.setTransaction_id(trId);
	}

	public void setTeReqTypId(long teReqTypId) {
		this.teReqTypId = 0;
		this.setTemplateRequestType_id(teReqTypId);
	}

	public void setRecrdNam(String recrdNam) {
		this.recrdNam = null;
		this.setRecord_Name(recrdNam);
	}	

	public List<ACHTransactionSubRecordDTO> getSubRecords() {
		return subRecords;
	}

	public void setSubRecords(List<ACHTransactionSubRecordDTO> subRecords) {
		this.subRecords = subRecords;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abatrcNumber == null) ? 0 : abatrcNumber.hashCode());
		result = prime * result + ((abstractNumber == null) ? 0 : abstractNumber.hashCode());
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((detail_id == null) ? 0 : detail_id.hashCode());
		result = prime * result + ((ein == null) ? 0 : ein.hashCode());
		result = prime * result + isZeroTaxDue;
		result = prime * result + ((record_Name == null) ? 0 : record_Name.hashCode());
		result = prime * result + softDelete;
		result = prime * result + ((taxType == null) ? 0 : taxType.hashCode());
		result = prime * result + (int) (taxType_id ^ (taxType_id >>> 32));
		result = prime * result + ((templateRequestTypeName == null) ? 0 : templateRequestTypeName.hashCode());
		result = prime * result + (int) (templateRequestType_id ^ (templateRequestType_id >>> 32));
		result = prime * result + ((toAccountNumber == null) ? 0 : toAccountNumber.hashCode());
		result = prime * result + (int) (toAccountType ^ (toAccountType >>> 32));
		result = prime * result + (int) (transactionRecord_id ^ (transactionRecord_id >>> 32));
		result = prime * result + (int) (transaction_id ^ (transaction_id >>> 32));
		
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
		ACHTransactionRecordDTO other = (ACHTransactionRecordDTO) obj;
		if (transactionRecord_id != other.transactionRecord_id)
			return false;
		
		return true;
	}
	
}
