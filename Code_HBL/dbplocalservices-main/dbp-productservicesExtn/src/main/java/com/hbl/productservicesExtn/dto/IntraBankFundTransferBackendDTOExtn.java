package com.hbl.productservicesExtn.dto;

import java.util.Objects;

import org.json.JSONObject;

import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferDTO;
import com.temenos.dbx.product.transactionservices.dto.TransferDTO;

public class IntraBankFundTransferBackendDTOExtn extends IntraBankFundTransferBackendDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2879223934592084576L;
	
	private String creditAccount;
	private String debitAccount;
	private String paymentCurrency;
	private String paymentAmount;
	private String serviceCharge;
	private String serviceChargeCurrency;
	private String serviceChargeType;
	private String scheduledDate;
	private String paymentOrderProduct;
	private String transactionType;
	private String frequencyType;
	
	private String chargeCurrency;
	private String chargeType;
	private String chargeName;
	private String chargeAmount;
	private String orderingReference;
	private String paymentType;
	private String totalAmount;
	private String isAdmin;
	private JSONObject additionalInformation;
	
	public IntraBankFundTransferBackendDTOExtn() {
		super();
	}

	public IntraBankFundTransferBackendDTOExtn(String creditAccount, String debitAccount, String paymentCurrency,
			String paymentAmount, String serviceCharge, String serviceChargeCurrency, String serviceChargeType,
			String scheduledDate, String paymentOrderProduct, String transactionType, String frequencyType, 
			String chargeCurrency, String chargeType, String chargeName, String chargeAmount, JSONObject additionalInformation, 
			String orderingReference, String paymentType, String totalAmount, String isAdmin) {
		super();
		this.creditAccount = creditAccount;
		this.debitAccount = debitAccount;
		this.paymentCurrency = paymentCurrency;
		this.paymentAmount = paymentAmount;
		this.serviceCharge = serviceCharge;
		this.serviceChargeCurrency = serviceChargeCurrency;
		this.serviceChargeType = serviceChargeType;
		this.scheduledDate = scheduledDate;
		this.paymentOrderProduct = paymentOrderProduct;
		this.transactionType = transactionType;
		this.frequencyType = frequencyType;
		
		this.chargeCurrency = chargeCurrency;
		this.chargeType = chargeType;
		this.chargeName = chargeName;
		this.chargeAmount = chargeAmount;
		this.additionalInformation = additionalInformation;
		this.orderingReference = orderingReference;
		this.paymentType = paymentType;
		this.totalAmount = totalAmount;
		this.isAdmin = isAdmin;
		
	}

	public String getCreditAccount() {
		return creditAccount;
	}

	public void setCreditAccount(String creditAccount) {
		this.creditAccount = creditAccount;
	}

	public String getDebitAccount() {
		return debitAccount;
	}

	public void setDebitAccount(String debitAccount) {
		this.debitAccount = debitAccount;
	}

	public String getPaymentCurrency() {
		return paymentCurrency;
	}

	public void setPaymentCurrency(String paymentCurrency) {
		this.paymentCurrency = paymentCurrency;
	}

	public String getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(String serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public String getServiceChargeCurrency() {
		return serviceChargeCurrency;
	}

	public void setServiceChargeCurrency(String serviceChargeCurrency) {
		this.serviceChargeCurrency = serviceChargeCurrency;
	}

	public String getServiceChargeType() {
		return serviceChargeType;
	}

	public void setServiceChargeType(String serviceChargeType) {
		this.serviceChargeType = serviceChargeType;
	}

	public String getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getPaymentOrderProduct() {
		return paymentOrderProduct;
	}

	public void setPaymentOrderProduct(String paymentOrderProduct) {
		this.paymentOrderProduct = paymentOrderProduct;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getFrequencyType() {
		return frequencyType;
	}

	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}
	public String getChargeCurrency() {
		return chargeCurrency;
	}

	public void setChargeCurrency(String chargeCurrency) {
		this.chargeCurrency = chargeCurrency;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getChargeName() {
		return chargeName;
	}

	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	public String getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(String chargeAmount) {
		this.chargeAmount = chargeAmount;
	}
	
	public JSONObject getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(JSONObject additionalInformation) {
		this.additionalInformation = additionalInformation;
	}
	

	public String getOrderingReference() {
		return orderingReference;
	}

	public void setOrderingReference(String orderingReference) {
		this.orderingReference = orderingReference;
	}
	

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public String toString() {
		return "IntraBankFundTransferBackendDTOExtn [creditAccount=" + creditAccount + ", debitAccount=" + debitAccount
				+ ", paymentCurrency=" + paymentCurrency + ", paymentAmount=" + paymentAmount + ", serviceCharge="
				+ serviceCharge + ", serviceChargeCurrency=" + serviceChargeCurrency + ", serviceChargeType="
				+ serviceChargeType + ", scheduledDate=" + scheduledDate + ", paymentOrderProduct="
				+ paymentOrderProduct + ", transactionType=" + transactionType + ", frequencyType="
				+ frequencyType+ ", chargeCurrency="+ chargeCurrency + ", chargeType=" + chargeType + ", chargeName="
				+ chargeName + ", chargeAmount=" + chargeAmount+ ", isAdmin=" + isAdmin+ "]";
	}

public IntraBankFundTransferBackendDTOExtn convert(IntraBankFundTransferDTO dto) {
		super.convert(dto);
		this.setFrequencyType(dto.getFrequencyTypeId());
		this.paymentNote=dto.getPaymentNote();
		this.pinCode=dto.getpinCode();
		this.zipCode=dto.getzipCode();
		this.transactionId = dto.getTransactionId();
		this.featureActionId = dto.getFeatureActionId();
		this.companyId = dto.getCompanyId();
		this.transactionCurrency = dto.getTransactionCurrency();
		this.fromAccountCurrency = dto.getFromAccountCurrency();
		this.frequencyType = dto.getFrequencyTypeId();
		this.fromAccountNumber = dto.getFromAccountNumber();
		this.toAccountNumber = dto.getToAccountNumber();
		this.amount = dto.getAmount();
		this.transactionsNotes = dto.getNotes();
		this.transactionts = dto.getTransactionts();
		this.frequencyEndDate = dto.getFrequencyEndDate();
		this.numberOfRecurrences = dto.getNumberOfRecurrences();
		this.scheduledDate = (dto.getScheduledDate() == null || (dto.getScheduledDate()).isEmpty())?dto.getFrequencyStartDate():dto.getScheduledDate();
		this.toAccountCurrency = dto.getToAccountCurrency();
		this.transactionType = dto.getTransactionType();
		this.dbxtransactionId = dto.getTransactionId();
		this.serviceName = dto.getFeatureActionId();
        this.transferLocator = dto.getTransactionId();
        this.isScheduled = dto.getIsScheduled();
        this.transferId = dto.getTransactionId();
        this.paymentId = dto.getPaymentId();
        this.frequency = dto.getFrequencyType();
        this.numberOfPayments = dto.getNumberOfRecurrences();
        this.noofRecurrences = dto.getNumberOfRecurrences();
        this.accountCode = dto.getFromAccountNumber();
        this.deliveryDate = dto.getDeliverBy();
        this.processingDate = (dto.getProcessingDate() == null || (dto.getProcessingDate()).isEmpty())?dto.getScheduledDate():dto.getProcessingDate();
        this.recipientId = dto.getPersonId();
        this.nickName = dto.getFromNickName();
        this.fromAccountType = dto.getFromAccountType();
        this.day1 = dto.getDay1();
        this.day2 = dto.getDay2();
        this.toAccountType = dto.getToAccountType();
        this.name = dto.getPayPersonName();
        this.message = dto.getTransactionsNotes();
        this.question = dto.getSecurityQuestion();
        this.answer = dto.getSecurityAnswer();
        this.checkBackImage = dto.getCheckImageBack();
        this.accountHolderNumber = dto.getToAccountNumber();
        this.payeeName = dto.getPayeeName();
        this.payeName = dto.getPayeeName();
        this.profileId = dto.getProfileId();
        this.cardNumber = dto.getCardNumber();
        this.cardExpiry = dto.getCardExpiry();
        this.validate = dto.getValidate();
		this.overrides = dto.getOverrides();
		this.overrideList = dto.getOverrideList();
		this.charges = dto.getCharges();
		this.exchangeRate = dto.getExchangeRate();
		this.totalAmount = dto.getTotalAmount();
		this.serviceCharge = dto.getServiceCharge();
		this.convertedAmount = dto.getConvertedAmount();
		this.transactionAmount = dto.getTransactionAmount();
		this.swiftCode = dto.getSwiftCode();
		this.paidBy = dto.getPaidBy();
		this.creditValueDate = dto.getCreditValueDate();
		this.legalEntityId = dto.getLegalEntityId();
		return this;
	}
	
	

}
