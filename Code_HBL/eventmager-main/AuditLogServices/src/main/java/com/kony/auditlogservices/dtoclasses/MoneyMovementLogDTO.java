package com.kony.auditlogservices.dtoclasses;

import com.kony.auditlogservices.core.BaseActivity;

public class MoneyMovementLogDTO extends BaseActivity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4577308282025764424L;
	private String customerid;
	private int expenseCategoryid;
	private int payeeid;
	private int billidone;
	private int typeid;
	private String referenceid;
	private String fromaccountnumber;
	private double fromaccountbalance;
	private String toaccountnumber;
	private double toaccountbalance;
	private double amount;
	private double convertedamount;
	private String transactioncurrency;
	private String basecurrency;
	private String statusid;
	private String statusdesc;
	private String notes;
	private int checknumber;
	private String description;
	private java.util.Date scheduleddate;
	private java.util.Date transactiondate;
	private java.util.Date createddate;
	private String transactioncomments;
	private String toexternalaccountnumber;
	private int personid;
	private int numberofrecurrences;
	private java.util.Date frequencystartdate;
	private java.util.Date frequencyenddate;
	private java.util.Date cashlessotpvaliddate;
	private String cashlessotp;
	private String cashlessphone;
	private String cashlessemail;
	private String cashlesspersonname;
	private String cashlessmode;
	private String cashlesssecuritycode;
	private String cashwithdrawaltransactionstatus;
	private String cashlesspin;
	private String recurrencedesc;
	private String deliverby;
	private String p2pcontact;
	private java.util.Date p2prequireddate;
	private String requestcreateddate;
	private String viewreportlink;
	private String fee;
	private String feecurrency;
	private String frontimage1;
	private String frontimage2;
	private String backimage1;
	private String backimage2;
	private String checkdesc;
	private String checknumber1;
	private String checknumber2;
	private String bankname1;
	private String bankname2;
	private String withdrawlamount1;
	private String withdrawlamount2;
	private String cashamount;
	private String payeecurrency;
	private int billid;
	private String disputedescription;
	private String disputereason;
	private String disputestatus;
	private java.util.Date disputedate;
	private String payeename;
	private java.util.Date checkdateofissue;
	private String checkreason;
	private String amountrecieved;
	private java.util.Date requestvalidity;
	private String statementreference;
	private String transcreditdebitindicator;
	private String transactioninformation;
	private String addressline;
	private String transactionamount;
	private String chargeamount;
	private String chargecurrency;
	private String sourcecurrency;
	private String targetcurrency;
	private String unitcurrency;
	private String exchangerate;
	private String contractidentification;
	private String instructedamount;
	private String instructedcurrency;
	private String transactioncode;
	private String transactionsubcode;
	private String proprietarytransactioncode;
	private String proprietarytransactionissuer;
	private String balancecreditdebitindicator;
	private String balancetype;
	private String balanceamount;
	private String balancecurrency;
	private String merchantname;
	private String merchantcategorycode;
	private String creditoragentschemename;
	private String creditoragentidentification;
	private String creditoragentname;
	private String creditoragentaddresstype;
	private String creditoragentdepartment;
	private String creditoragentsubdepartment;
	private String creditoragentstreetname;
	private String creditoragentbuildingnumber;
	private String creditoragentpostcode;
	private String creditoragenttownname;
	private String creditoragentcountrysubdivision;
	private String creditoragentcountry;
	private String creditoragentaddressline;
	private String creditoraccountschemename;
	private String creditoraccountidentification;
	private String creditoraccountname;
	private String creditoraccountseconidentification;
	private String debtoragentschemename;
	private String debtoragentidentification;
	private String debtoragentname;
	private String debtoragentaddresstype;
	private String debtoragentdepartment;
	private String debtoragentsubdepartment;
	private String debtoragentstreetname;
	private String debtoragentbuildingnumber;
	private String dedtoragentpostcode;
	private String debtoragenttownname;
	private String debtoragentcountrysubdivision;
	private String debtoragentcountry;
	private String debtoragentaddressline;
	private String debtoraccountschemename;
	private String debtoraccountidentification;
	private String debtoraccountname;
	private String debtoraccountseconidentification;
	private String cardinstrumentschemename;
	private String cardinstrumentauthorisationtype;
	private String cardinstrumentname;
	private String cardinstrumentidentification;
	private String iban;
	private String sortcode;
	private java.util.Date firstpaymentdatetime;
	private java.util.Date nextpaymentdatetime;
	private java.util.Date finalpaymentdatetime;
	private String standingorderstatuscode;
	private double fpamount;
	private String fpcurrency;
	private double npamount;
	private String npcurrency;
	private double fpaamount;
	private String fpacurrency;
	private String consentid;
	private String initiationinstructionidentification;
	private String initiationendtoendidentification;
	private String rireference;
	private String riunstructured;
	private String riskpaymentcontextcode;
	private String merchantcustomeridentification;
	private String beneficiaryname;
	private String bankname;
	private String swiftcode;
	private String domesticpaymentid;
	private String linkself;
	private java.util.Date statusupdatedatetime;
	private String datastatus;
	private String servicename;
	private String paypersonname;
	private boolean isscheduled;
	private boolean penaltyflag;
	private boolean payoffflag;
	private boolean ispaypersondeleted;
	private boolean feepaidbyreceipent;
	private boolean isdisputed;
	private boolean ispayeedeleted;
	private String checkimageback;
	private String checkimage;
	private String imageurl1;
	private String imageurl2;
	private boolean hasdepositimage;
	private java.util.Date bookingdatetime;
	private java.util.Date valuedatetime;
	private java.util.Date quotationdate;
	private String frequencytype;
	private String category;
	private String billCategory;

	public String getCustomer_id() {
		return customerid;
	}

	public void setCustomer_id(String custid) {
		this.customerid = custid;
	}

	public int getExpenseCategory_id() {
		return expenseCategoryid;
	}

	public void setExpenseCategory_id(int expensecategoryid) {
		this.expenseCategoryid = expensecategoryid;
	}

	public int getPayee_id() {
		return payeeid;
	}

	public void setPayee_id(int payeeid) {
		this.payeeid = payeeid;
	}

	public int getBill_id() {
		return billidone;
	}

	public void setBill_id(int billid) {
		this.billidone = billid;
	}

	public int getType_id() {
		return typeid;
	}

	public void setType_id(int typeid) {
		this.typeid = typeid;
	}

	public String getReference_id() {
		return referenceid;
	}

	public void setReference_id(String referenceid) {
		this.referenceid = referenceid;
	}

	public String getfromAccountNumber() {
		return fromaccountnumber;
	}

	public void setfromAccountNumber(String fromaccountnumber) {
		this.fromaccountnumber = fromaccountnumber;
	}

	public double getfromAccountBalance() {
		return fromaccountbalance;
	}

	public void setfromAccountBalance(double fromaccountbalance) {
		this.fromaccountbalance = fromaccountbalance;
	}

	public String gettoAccountNumber() {
		return toaccountnumber;
	}

	public void settoAccountNumber(String toaccountnumber) {
		this.toaccountnumber = toaccountnumber;
	}

	public double gettoAccountBalance() {
		return toaccountbalance;
	}

	public void settoAccountBalance(double toaccountbalance) {
		this.toaccountbalance = toaccountbalance;
	}

	public double getamount() {
		return amount;
	}

	public void setamount(double amount) {
		this.amount = amount;
	}

	public double getconvertedAmount() {
		return convertedamount;
	}

	public void setconvertedAmount(double convertedamount) {
		this.convertedamount = convertedamount;
	}

	public String gettransactionCurrency() {
		return transactioncurrency;
	}

	public void settransactionCurrency(String transactioncurrency) {
		this.transactioncurrency = transactioncurrency;
	}

	public String getbaseCurrency() {
		return basecurrency;
	}

	public void setbaseCurrency(String basecurrency) {
		this.basecurrency = basecurrency;
	}

	public String getStatus_id() {
		return statusid;
	}

	public void setStatus_id(String statusid) {
		this.statusid = statusid;
	}

	public String getstatusDesc() {
		return statusdesc;
	}

	public void setstatusDesc(String statusdesc) {
		this.statusdesc = statusdesc;
	}

	public String getnotes() {
		return notes;
	}

	public void setnotes(String notes) {
		this.notes = notes;
	}

	public int getcheckNumber() {
		return checknumber;
	}

	public void setcheckNumber(int checknumber) {
		this.checknumber = checknumber;
	}

	public String getdescription() {
		return description;
	}

	public void setdescription(String description) {
		this.description = description;
	}

	public java.util.Date getscheduledDate() {
		return scheduleddate;
	}

	public void setscheduledDate(java.util.Date scheduleddate) {
		this.scheduleddate = scheduleddate;
	}

	public java.util.Date gettransactionDate() {
		return transactiondate;
	}

	public void settransactionDate(java.util.Date transactiondate) {
		this.transactiondate = transactiondate;
	}

	public java.util.Date getcreatedDate() {
		return createddate;
	}

	public void setcreatedDate(java.util.Date createddate) {
		this.createddate = createddate;
	}

	public String gettransactionComments() {
		return transactioncomments;
	}

	public void settransactionComments(String transactioncomments) {
		this.transactioncomments = transactioncomments;
	}

	public String gettoExternalAccountNumber() {
		return toexternalaccountnumber;
	}

	public void settoExternalAccountNumber(String toExternalaccountNumber) {
		this.toexternalaccountnumber = toExternalaccountNumber;
	}

	public int getPerson_Id() {
		return personid;
	}

	public void setPerson_Id(int personid) {
		this.personid = personid;
	}

	public int getnumberOfRecurrences() {
		return numberofrecurrences;
	}

	public void setnumberOfRecurrences(int numberofrecurrences) {
		this.numberofrecurrences = numberofrecurrences;
	}

	public java.util.Date getfrequencyStartDate() {
		return frequencystartdate;
	}

	public void setfrequencyStartDate(java.util.Date frequencystartdate) {
		this.frequencystartdate = frequencystartdate;
	}

	public java.util.Date getfrequencyEndDate() {
		return frequencyenddate;
	}

	public void setfrequencyEndDate(java.util.Date frequencyenddate) {
		this.frequencyenddate = frequencyenddate;
	}

	public java.util.Date getcashlessOTPValidDate() {
		return cashlessotpvaliddate;
	}

	public void setcashlessOTPValidDate(java.util.Date cashlessotpValiddate) {
		this.cashlessotpvaliddate = cashlessotpValiddate;
	}

	public String getcashlessOTP() {
		return cashlessotp;
	}

	public void setcashlessOTP(String cashlessotp) {
		this.cashlessotp = cashlessotp;
	}

	public String getcashlessPhone() {
		return cashlessphone;
	}

	public void setcashlessPhone(String cashlessphone) {
		this.cashlessphone = cashlessphone;
	}

	public String getcashlessEmail() {
		return cashlessemail;
	}

	public void setcashlessEmail(String cashlessemail) {
		this.cashlessemail = cashlessemail;
	}

	public String getcashlessPersonName() {
		return cashlesspersonname;
	}

	public void setcashlessPersonName(String cashlesspersonname) {
		this.cashlesspersonname = cashlesspersonname;
	}

	public String getcashlessMode() {
		return cashlessmode;
	}

	public void setcashlessMode(String cashlessmode) {
		this.cashlessmode = cashlessmode;
	}

	public String getcashlessSecurityCode() {
		return cashlesssecuritycode;
	}

	public void setcashlessSecurityCode(String cashlesssecuritycode) {
		this.cashlesssecuritycode = cashlesssecuritycode;
	}

	public String getcashWithdrawalTransactionStatus() {
		return cashwithdrawaltransactionstatus;
	}

	public void setcashWithdrawalTransactionStatus(String cashwithdrawaltransactionstatus) {
		this.cashwithdrawaltransactionstatus = cashwithdrawaltransactionstatus;
	}

	public String getcashlessPin() {
		return cashlesspin;
	}

	public void setcashlessPin(String cashlesspin) {
		this.cashlesspin = cashlesspin;
	}

	public String getrecurrenceDesc() {
		return recurrencedesc;
	}

	public void setrecurrenceDesc(String recurrencedesc) {
		this.recurrencedesc = recurrencedesc;
	}

	public String getdeliverBy() {
		return deliverby;
	}

	public void setdeliverBy(String deliverby) {
		this.deliverby = deliverby;
	}

	public String getp2pContact() {
		return p2pcontact;
	}

	public void setp2pContact(String p2pcontact) {
		this.p2pcontact = p2pcontact;
	}

	public java.util.Date getp2pRequiredDate() {
		return p2prequireddate;
	}

	public void setp2pRequiredDate(java.util.Date p2prequireddate) {
		this.p2prequireddate = p2prequireddate;
	}

	public String getrequestCreatedDate() {
		return requestcreateddate;
	}

	public void setrequestCreatedDate(String requestcreateddate) {
		this.requestcreateddate = requestcreateddate;
	}

	public String getviewReportLink() {
		return viewreportlink;
	}

	public void setviewReportLink(String viewreportlink) {
		this.viewreportlink = viewreportlink;
	}

	public String getfee() {
		return fee;
	}

	public void setfee(String fee) {
		this.fee = fee;
	}

	public String getfeeCurrency() {
		return feecurrency;
	}

	public void setfeeCurrency(String feecurrency) {
		this.feecurrency = feecurrency;
	}

	public String getfrontImage1() {
		return frontimage1;
	}

	public void setfrontImage1(String frontimage1) {
		this.frontimage1 = frontimage1;
	}

	public String getfrontImage2() {
		return frontimage2;
	}

	public void setfrontImage2(String frontimage2) {
		this.frontimage2 = frontimage2;
	}

	public String getbackImage1() {
		return backimage1;
	}

	public void setbackImage1(String backimage1) {
		this.backimage1 = backimage1;
	}

	public String getbackImage2() {
		return backimage2;
	}

	public void setbackImage2(String backimage2) {
		this.backimage2 = backimage2;
	}

	public String getcheckDesc() {
		return checkdesc;
	}

	public void setcheckDesc(String checkdesc) {
		this.checkdesc = checkdesc;
	}

	public String getcheckNumber1() {
		return checknumber1;
	}

	public void setcheckNumber1(String checknumber1) {
		this.checknumber1 = checknumber1;
	}

	public String getcheckNumber2() {
		return checknumber2;
	}

	public void setcheckNumber2(String checknumber2) {
		this.checknumber2 = checknumber2;
	}

	public String getbankName1() {
		return bankname1;
	}

	public void setbankName1(String bankname1) {
		this.bankname1 = bankname1;
	}

	public String getbankName2() {
		return bankname2;
	}

	public void setbankName2(String bankname2) {
		this.bankname2 = bankname2;
	}

	public String getwithdrawlAmount1() {
		return withdrawlamount1;
	}

	public void setwithdrawlAmount1(String withdrawlamount1) {
		this.withdrawlamount1 = withdrawlamount1;
	}

	public String getwithdrawlAmount2() {
		return withdrawlamount2;
	}

	public void setwithdrawlAmount2(String withdrawlamount2) {
		this.withdrawlamount2 = withdrawlamount2;
	}

	public String getcashAmount() {
		return cashamount;
	}

	public void setcashAmount(String cashamount) {
		this.cashamount = cashamount;
	}

	public String getpayeeCurrency() {
		return payeecurrency;
	}

	public void setpayeeCurrency(String payeecurrency) {
		this.payeecurrency = payeecurrency;
	}

	public int getbillid() {
		return billid;
	}

	public void setbillid(int billid) {
		this.billid = billid;
	}

	public String getdisputeDescription() {
		return disputedescription;
	}

	public void setdisputeDescription(String disputedescription) {
		this.disputedescription = disputedescription;
	}

	public String getdisputeReason() {
		return disputereason;
	}

	public void setdisputeReason(String disputereason) {
		this.disputereason = disputereason;
	}

	public String getdisputeStatus() {
		return disputestatus;
	}

	public void setdisputeStatus(String disputestatus) {
		this.disputestatus = disputestatus;
	}

	public java.util.Date getdisputeDate() {
		return disputedate;
	}

	public void setdisputeDate(java.util.Date disputedate) {
		this.disputedate = disputedate;
	}

	public String getpayeeName() {
		return payeename;
	}

	public void setpayeeName(String payeename) {
		this.payeename = payeename;
	}

	public java.util.Date getcheckDateOfIssue() {
		return checkdateofissue;
	}

	public void setcheckDateOfIssue(java.util.Date checkdateofissue) {
		this.checkdateofissue = checkdateofissue;
	}

	public String getcheckReason() {
		return checkreason;
	}

	public void setcheckReason(String checkReason) {
		this.checkreason = checkReason;
	}

	public String getamountRecieved() {
		return amountrecieved;
	}

	public void setamountRecieved(String amountRecieved) {
		this.amountrecieved = amountRecieved;
	}

	public java.util.Date getrequestValidity() {
		return requestvalidity;
	}

	public void setrequestValidity(java.util.Date requestValidity) {
		this.requestvalidity = requestValidity;
	}

	public String getstatementReference() {
		return statementreference;
	}

	public void setstatementReference(String statementReference) {
		this.statementreference = statementReference;
	}

	public String gettransCreditDebitIndicator() {
		return transcreditdebitindicator;
	}

	public void settransCreditDebitIndicator(String transCreditDebitIndicator) {
		this.transcreditdebitindicator = transCreditDebitIndicator;
	}

	public String gettransactionInformation() {
		return transactioninformation;
	}

	public void settransactionInformation(String transactionInformation) {
		this.transactioninformation = transactionInformation;
	}

	public String getaddressLine() {
		return addressline;
	}

	public void setaddressLine(String addressLine) {
		this.addressline = addressLine;
	}

	public String gettransactionAmount() {
		return transactionamount;
	}

	public void settransactionAmount(String transactionAmount) {
		this.transactionamount = transactionAmount;
	}

	public String getchargeAmount() {
		return chargeamount;
	}

	public void setchargeAmount(String chargeAmount) {
		this.chargeamount = chargeAmount;
	}

	public String getchargeCurrency() {
		return chargecurrency;
	}

	public void setchargeCurrency(String chargeCurrency) {
		this.chargecurrency = chargeCurrency;
	}

	public String getsourceCurrency() {
		return sourcecurrency;
	}

	public void setsourceCurrency(String sourceCurrency) {
		this.sourcecurrency = sourceCurrency;
	}

	public String gettargetCurrency() {
		return targetcurrency;
	}

	public void settargetCurrency(String targetCurrency) {
		this.targetcurrency = targetCurrency;
	}

	public String getunitCurrency() {
		return unitcurrency;
	}

	public void setunitCurrency(String unitCurrency) {
		this.unitcurrency = unitCurrency;
	}

	public String getexchangeRate() {
		return exchangerate;
	}

	public void setexchangeRate(String exchangeRate) {
		this.exchangerate = exchangeRate;
	}

	public String getcontractIdentification() {
		return contractidentification;
	}

	public void setcontractIdentification(String contractIdentification) {
		this.contractidentification = contractIdentification;
	}

	public String getinstructedAmount() {
		return instructedamount;
	}

	public void setinstructedAmount(String instructedAmount) {
		this.instructedamount = instructedAmount;
	}

	public String getinstructedCurrency() {
		return instructedcurrency;
	}

	public void setinstructedCurrency(String instructedCurrency) {
		this.instructedcurrency = instructedCurrency;
	}

	public String gettransactionCode() {
		return transactioncode;
	}

	public void settransactionCode(String transactionCode) {
		this.transactioncode = transactionCode;
	}

	public String gettransactionSubCode() {
		return transactionsubcode;
	}

	public void settransactionSubCode(String transactionSubCode) {
		this.transactionsubcode = transactionSubCode;
	}

	public String getproprietaryTransactionCode() {
		return proprietarytransactioncode;
	}

	public void setproprietaryTransactionCode(String proprietaryTransactionCode) {
		this.proprietarytransactioncode = proprietaryTransactionCode;
	}

	public String getproprietaryTransactionIssuer() {
		return proprietarytransactionissuer;
	}

	public void setproprietaryTransactionIssuer(String proprietaryTransactionIssuer) {
		this.proprietarytransactionissuer = proprietaryTransactionIssuer;
	}

	public String getbalanceCreditDebitIndicator() {
		return balancecreditdebitindicator;
	}

	public void setbalanceCreditDebitIndicator(String balanceCreditDebitIndicator) {
		this.balancecreditdebitindicator = balanceCreditDebitIndicator;
	}

	public String getbalanceType() {
		return balancetype;
	}

	public void setbalanceType(String balanceType) {
		this.balancetype = balanceType;
	}

	public String getbalanceAmount() {
		return balanceamount;
	}

	public void setbalanceAmount(String balanceAmount) {
		this.balanceamount = balanceAmount;
	}

	public String getbalanceCurrency() {
		return balancecurrency;
	}

	public void setbalanceCurrency(String balanceCurrency) {
		this.balancecurrency = balanceCurrency;
	}

	public String getmerchantName() {
		return merchantname;
	}

	public void setmerchantName(String merchantName) {
		this.merchantname = merchantName;
	}

	public String getmerchantCategoryCode() {
		return merchantcategorycode;
	}

	public void setmerchantCategoryCode(String merchantCategoryCode) {
		this.merchantcategorycode = merchantCategoryCode;
	}

	public String getcreditorAgentSchemeName() {
		return creditoragentschemename;
	}

	public void setcreditorAgentSchemeName(String creditorAgentSchemeName) {
		this.creditoragentschemename = creditorAgentSchemeName;
	}

	public String getcreditorAgentIdentification() {
		return creditoragentidentification;
	}

	public void setcreditorAgentIdentification(String creditorAgentIdentification) {
		this.creditoragentidentification = creditorAgentIdentification;
	}

	public String getcreditorAgentName() {
		return creditoragentname;
	}

	public void setcreditorAgentName(String creditorAgentName) {
		this.creditoragentname = creditorAgentName;
	}

	public String getcreditorAgentaddressType() {
		return creditoragentaddresstype;
	}

	public void setcreditorAgentaddressType(String creditorAgentaddressType) {
		this.creditoragentaddresstype = creditorAgentaddressType;
	}

	public String getcreditorAgentDepartment() {
		return creditoragentdepartment;
	}

	public void setcreditorAgentDepartment(String creditorAgentDepartment) {
		this.creditoragentdepartment = creditorAgentDepartment;
	}

	public String getcreditorAgentSubDepartment() {
		return creditoragentsubdepartment;
	}

	public void setcreditorAgentSubDepartment(String creditorAgentSubDepartment) {
		this.creditoragentsubdepartment = creditorAgentSubDepartment;
	}

	public String getcreditorAgentStreetName() {
		return creditoragentstreetname;
	}

	public void setcreditorAgentStreetName(String creditorAgentStreetName) {
		this.creditoragentstreetname = creditorAgentStreetName;
	}

	public String getcreditorAgentBuildingNumber() {
		return creditoragentbuildingnumber;
	}

	public void setcreditorAgentBuildingNumber(String creditorAgentBuildingNumber) {
		this.creditoragentbuildingnumber = creditorAgentBuildingNumber;
	}

	public String getcreditorAgentPostCode() {
		return creditoragentpostcode;
	}

	public void setcreditorAgentPostCode(String creditorAgentPostCode) {
		this.creditoragentpostcode = creditorAgentPostCode;
	}

	public String getcreditorAgentTownName() {
		return creditoragenttownname;
	}

	public void setcreditorAgentTownName(String creditorAgentTownName) {
		this.creditoragenttownname = creditorAgentTownName;
	}

	public String getcreditorAgentCountrySubDivision() {
		return creditoragentcountrysubdivision;
	}

	public void setcreditorAgentCountrySubDivision(String creditorAgentCountrySubDivision) {
		this.creditoragentcountrysubdivision = creditorAgentCountrySubDivision;
	}

	public String getcreditorAgentCountry() {
		return creditoragentcountry;
	}

	public void setcreditorAgentCountry(String creditorAgentCountry) {
		this.creditoragentcountry = creditorAgentCountry;
	}

	public String getcreditorAgentAddressLine() {
		return creditoragentaddressline;
	}

	public void setcreditorAgentAddressLine(String creditorAgentAddressLine) {
		this.creditoragentaddressline = creditorAgentAddressLine;
	}

	public String getcreditorAccountSchemeName() {
		return creditoraccountschemename;
	}

	public void setcreditorAccountSchemeName(String creditorAccountSchemeName) {
		this.creditoraccountschemename = creditorAccountSchemeName;
	}

	public String getcreditorAccountIdentification() {
		return creditoraccountidentification;
	}

	public void setcreditorAccountIdentification(String creditorAccountIdentification) {
		this.creditoraccountidentification = creditorAccountIdentification;
	}

	public String getcreditorAccountName() {
		return creditoraccountname;
	}

	public void setcreditorAccountName(String creditorAccountName) {
		this.creditoraccountname = creditorAccountName;
	}

	public String getcreditorAccountSeconIdentification() {
		return creditoraccountseconidentification;
	}

	public void setcreditorAccountSeconIdentification(String creditorAccountSeconIdentification) {
		this.creditoraccountseconidentification = creditorAccountSeconIdentification;
	}

	public String getdebtorAgentSchemeName() {
		return debtoragentschemename;
	}

	public void setdebtorAgentSchemeName(String debtorAgentSchemeName) {
		this.debtoragentschemename = debtorAgentSchemeName;
	}

	public String getdebtorAgentIdentification() {
		return debtoragentidentification;
	}

	public void setdebtorAgentIdentification(String debtorAgentIdentification) {
		this.debtoragentidentification = debtorAgentIdentification;
	}

	public String getdebtorAgentName() {
		return debtoragentname;
	}

	public void setdebtorAgentName(String debtorAgentName) {
		this.debtoragentname = debtorAgentName;
	}

	public String getdebtorAgentAddressType() {
		return debtoragentaddresstype;
	}

	public void setdebtorAgentAddressType(String debtorAgentAddressType) {
		this.debtoragentaddresstype = debtorAgentAddressType;
	}

	public String getdebtorAgentDepartment() {
		return debtoragentdepartment;
	}

	public void setdebtorAgentDepartment(String debtorAgentDepartment) {
		this.debtoragentdepartment = debtorAgentDepartment;
	}

	public String getdebtorAgentSubDepartment() {
		return debtoragentsubdepartment;
	}

	public void setdebtorAgentSubDepartment(String debtorAgentSubDepartment) {
		this.debtoragentsubdepartment = debtorAgentSubDepartment;
	}

	public String getdebtorAgentStreetName() {
		return debtoragentstreetname;
	}

	public void setdebtorAgentStreetName(String debtorAgentStreetName) {
		this.debtoragentstreetname = debtorAgentStreetName;
	}

	public String getdebtorAgentBuildingNumber() {
		return debtoragentbuildingnumber;
	}

	public void setdebtorAgentBuildingNumber(String debtorAgentBuildingNumber) {
		this.debtoragentbuildingnumber = debtorAgentBuildingNumber;
	}

	public String getdedtorAgentPostCode() {
		return dedtoragentpostcode;
	}

	public void setdedtorAgentPostCode(String dedtorAgentPostCode) {
		this.dedtoragentpostcode = dedtorAgentPostCode;
	}

	public String getdebtorAgentTownName() {
		return debtoragenttownname;
	}

	public void setdebtorAgentTownName(String debtorAgentTownName) {
		this.debtoragenttownname = debtorAgentTownName;
	}

	public String getdebtorAgentCountrySubDivision() {
		return debtoragentcountrysubdivision;
	}

	public void setdebtorAgentCountrySubDivision(String debtorAgentCountrySubDivision) {
		this.debtoragentcountrysubdivision = debtorAgentCountrySubDivision;
	}

	public String getdebtorAgentCountry() {
		return debtoragentcountry;
	}

	public void setdebtorAgentCountry(String debtorAgentCountry) {
		this.debtoragentcountry = debtorAgentCountry;
	}

	public String getdebtorAgentAddressLine() {
		return debtoragentaddressline;
	}

	public void setdebtorAgentAddressLine(String debtorAgentAddressLine) {
		this.debtoragentaddressline = debtorAgentAddressLine;
	}

	public String getdebtorAccountSchemeName() {
		return debtoraccountschemename;
	}

	public void setdebtorAccountSchemeName(String debtorAccountSchemeName) {
		this.debtoraccountschemename = debtorAccountSchemeName;
	}

	public String getdebtorAccountIdentification() {
		return debtoraccountidentification;
	}

	public void setdebtorAccountIdentification(String debtorAccountIdentification) {
		this.debtoraccountidentification = debtorAccountIdentification;
	}

	public String getdebtorAccountName() {
		return debtoraccountname;
	}

	public void setdebtorAccountName(String debtorAccountName) {
		this.debtoraccountname = debtorAccountName;
	}

	public String getdebtorAccountSeconIdentification() {
		return debtoraccountseconidentification;
	}

	public void setdebtorAccountSeconIdentification(String debtorAccountSeconIdentification) {
		this.debtoraccountseconidentification = debtorAccountSeconIdentification;
	}

	public String getcardInstrumentSchemeName() {
		return cardinstrumentschemename;
	}

	public void setcardInstrumentSchemeName(String cardInstrumentSchemeName) {
		this.cardinstrumentschemename = cardInstrumentSchemeName;
	}

	public String getcardInstrumentAuthorisationType() {
		return cardinstrumentauthorisationtype;
	}

	public void setcardInstrumentAuthorisationType(String cardInstrumentAuthorisationType) {
		this.cardinstrumentauthorisationtype = cardInstrumentAuthorisationType;
	}

	public String getcardInstrumentName() {
		return cardinstrumentname;
	}

	public void setcardInstrumentName(String cardInstrumentName) {
		this.cardinstrumentname = cardInstrumentName;
	}

	public String getcardInstrumentIdentification() {
		return cardinstrumentidentification;
	}

	public void setcardInstrumentIdentification(String cardInstrumentIdentification) {
		this.cardinstrumentidentification = cardInstrumentIdentification;
	}

	public String getIBAN() {
		return iban;
	}

	public void setIBAN(String iban) {
		this.iban = iban;
	}

	public String getsortCode() {
		return sortcode;
	}

	public void setsortCode(String sortCode) {
		this.sortcode = sortCode;
	}

	public java.util.Date getFirstPaymentDateTime() {
		return firstpaymentdatetime;
	}

	public void setFirstPaymentDateTime(java.util.Date firstPaymentDateTime) {
		this.firstpaymentdatetime = firstPaymentDateTime;
	}

	public java.util.Date getNextPaymentDateTime() {
		return nextpaymentdatetime;
	}

	public void setNextPaymentDateTime(java.util.Date nextPaymentDateTime) {
		this.nextpaymentdatetime = nextPaymentDateTime;
	}

	public java.util.Date getFinalPaymentDateTime() {
		return finalpaymentdatetime;
	}

	public void setFinalPaymentDateTime(java.util.Date finalpaymentdatetime) {
		this.finalpaymentdatetime = finalpaymentdatetime;
	}

	public String getStandingOrderStatusCode() {
		return standingorderstatuscode;
	}

	public void setStandingOrderStatusCode(String standingorderstatuscode) {
		this.standingorderstatuscode = standingorderstatuscode;
	}

	public double getFP_Amount() {
		return fpamount;
	}

	public void setFP_Amount(double fpamount) {
		this.fpamount = fpamount;
	}

	public String getFP_Currency() {
		return fpcurrency;
	}

	public void setFP_Currency(String fpcurrency) {
		this.fpcurrency = fpcurrency;
	}

	public double getNP_Amount() {
		return npamount;
	}

	public void setNP_Amount(double npamount) {
		this.npamount = npamount;
	}

	public String getNP_Currency() {
		return npcurrency;
	}

	public void setNP_Currency(String npcurrency) {
		this.npcurrency = npcurrency;
	}

	public double getFPA_Amount() {
		return fpaamount;
	}

	public void setFPA_Amount(double fpaamount) {
		this.fpaamount = fpaamount;
	}

	public String getFPA_Currency() {
		return fpacurrency;
	}

	public void setFPA_Currency(String fpacurrency) {
		this.fpacurrency = fpacurrency;
	}

	public String getConsentId() {
		return consentid;
	}

	public void setConsentId(String consentid) {
		this.consentid = consentid;
	}

	public String getInitiation_InstructionIdentification() {
		return initiationinstructionidentification;
	}

	public void setInitiation_InstructionIdentification(String initiationinstructionidentification) {
		this.initiationinstructionidentification = initiationinstructionidentification;
	}

	public String getInitiation_EndToEndIdentification() {
		return initiationendtoendidentification;
	}

	public void setInitiation_EndToEndIdentification(String initiationendtoendidentification) {
		this.initiationendtoendidentification = initiationendtoendidentification;
	}

	public String getRI_Reference() {
		return rireference;
	}

	public void setRI_Reference(String rireference) {
		this.rireference = rireference;
	}

	public String getRI_Unstructured() {
		return riunstructured;
	}

	public void setRI_Unstructured(String riunstructured) {
		this.riunstructured = riunstructured;
	}

	public String getRiskPaymentContextCode() {
		return riskpaymentcontextcode;
	}

	public void setRiskPaymentContextCode(String riskpaymentcontextcode) {
		this.riskpaymentcontextcode = riskpaymentcontextcode;
	}

	public String getMerchantCustomerIdentification() {
		return merchantcustomeridentification;
	}

	public void setMerchantCustomerIdentification(String merchantcustomeridentification) {
		this.merchantcustomeridentification = merchantcustomeridentification;
	}

	public String getbeneficiaryName() {
		return beneficiaryname;
	}

	public void setbeneficiaryName(String beneficiaryName) {
		this.beneficiaryname = beneficiaryName;
	}

	public String getbankName() {
		return bankname;
	}

	public void setbankName(String bankname) {
		this.bankname = bankname;
	}

	public String getswiftCode() {
		return swiftcode;
	}

	public void setswiftCode(String swiftcode) {
		this.swiftcode = swiftcode;
	}

	public String getDomesticPaymentId() {
		return domesticpaymentid;
	}

	public void setDomesticPaymentId(String domesticpaymentid) {
		this.domesticpaymentid = domesticpaymentid;
	}

	public String getlinkSelf() {
		return linkself;
	}

	public void setlinkSelf(String linkself) {
		this.linkself = linkself;
	}

	public java.util.Date getStatusUpdateDateTime() {
		return statusupdatedatetime;
	}

	public void setStatusUpdateDateTime(java.util.Date statusupdatedatetime) {
		this.statusupdatedatetime = statusupdatedatetime;
	}

	public String getdataStatus() {
		return datastatus;
	}

	public void setdataStatus(String datastatus) {
		this.datastatus = datastatus;
	}

	public String getserviceName() {
		return servicename;
	}

	public void setserviceName(String servicename) {
		this.servicename = servicename;
	}

	public String getpayPersonName() {
		return paypersonname;
	}

	public void setpayPersonName(String paypersonName) {
		this.paypersonname = paypersonName;
	}

	/**
	 * @return the isScheduled
	 */
	public boolean getisScheduled() {
		return isscheduled;
	}

	/**
	 * @param isscheduled the isScheduled to set
	 */
	public void setisScheduled(String isscheduled) {
		if (isscheduled !=null && isscheduled.equals("true"))
			this.isscheduled = true;
		else
			this.isscheduled = false;
	}

	/**
	 * @return the penaltyFlag
	 */
	public boolean getpenaltyFlag() {
		return penaltyflag;
	}

	/**
	 * @param penaltyflag the penaltyFlag to set
	 */
	public void setpenaltyFlag(String penaltyflag) {
		if (penaltyflag !=null &&penaltyflag.equals("true"))
			this.penaltyflag = true;
		else
			this.penaltyflag = false;
	}

	/**
	 * @return the payoffFlag
	 */
	public boolean getpayoffFlag() {
		return payoffflag;
	}

	/**
	 * @param payoffflag the payoffFlag to set
	 */
	public void setpayoffFlag(String payoffflag) {
		if (payoffflag !=null && payoffflag.equals("true"))
			this.payoffflag = true;
		else
			this.payoffflag = false;
	}

	/**
	 * @return the isPaypersonDeleted
	 */
	public boolean getisPaypersonDeleted() {
		return ispaypersondeleted;
	}

	/**
	 * @param ispaypersondeleted the isPaypersonDeleted to set
	 */
	public void setisPaypersonDeleted(String ispaypersondeleted) {
		if (ispaypersondeleted !=null && ispaypersondeleted.equals("true"))
			this.ispaypersondeleted = true;
		else
			this.ispaypersondeleted = false;
	}

	/**
	 * @return the feePaidByReceipent
	 */
	public boolean getfeePaidByReceipent() {
		return feepaidbyreceipent;
	}

	/**
	 * @param feepaidbyreceipent the feePaidByReceipent to set
	 */
	public void setfeePaidByReceipent(String feepaidbyreceipent) {
		if (feepaidbyreceipent !=null && feepaidbyreceipent.equals("true"))
			this.feepaidbyreceipent = true;
		else
			this.feepaidbyreceipent = false;
	}

	/**
	 * @return the isDisputed
	 */
	public boolean getisDisputed() {
		return isdisputed;
	}

	/**
	 * @param isdisputed the isDisputed to set
	 */
	public void setisDisputed(String isdisputed) {
		if (isdisputed !=null && isdisputed.equals("true"))
			this.isdisputed = true;
		else
			this.isdisputed = false;
	}

	/**
	 * @return the isPayeeDeleted
	 */
	public boolean getisPayeeDeleted() {
		return ispayeedeleted;
	}

	/**
	 * @param ispayeedeleted the isPayeeDeleted to set
	 */
	public void setisPayeeDeleted(String ispayeedeleted) {
		if (ispayeedeleted !=null && ispayeedeleted.equalsIgnoreCase("true")) {
			this.ispayeedeleted = true;
		} else {
			this.ispayeedeleted = false;
		}

	}

	/**
	 * @return the checkImageBack
	 */
	public String getcheckImageBack() {
		return checkimageback;
	}

	/**
	 * @param checkimageback the checkImageBack to set
	 */
	public void setcheckImageBack(String checkimageback) {
		this.checkimageback = checkimageback;
	}

	/**
	 * @return the checkImage
	 */
	public String getcheckImage() {
		return checkimage;
	}

	/**
	 * @param checkimage the checkImage to set
	 */
	public void setcheckImage(String checkimage) {
		this.checkimage = checkimage;
	}

	/**
	 * @return the imageURL1
	 */
	public String getimageURL1() {
		return imageurl1;
	}

	/**
	 * @param imageurl1 the imageURL1 to set
	 */
	public void setimageURL1(String imageurl1) {
		this.imageurl1 = imageurl1;
	}

	/**
	 * @return the imageURL2
	 */
	public String getimageURL2() {
		return imageurl2;
	}

	/**
	 * @param imageurl2 the imageURL2 to set
	 */
	public void setimageURL2(String imageurl2) {
		this.imageurl2 = imageurl2;
	}

	/**
	 * @return the hasDepositImage
	 */
	public boolean gethasDepositImage() {
		return hasdepositimage;
	}

	/**
	 * @param hasdepositimage the hasDepositImage to set
	 */
	public void sethasDepositImage(boolean hasdepositimage) {
		this.hasdepositimage = hasdepositimage;
	}

	/**
	 * @return the bookingDateTime
	 */
	public java.util.Date getbookingDateTime() {
		return bookingdatetime;
	}

	/**
	 * @param bookingdatetime the bookingDateTime to set
	 */
	public void setbookingDateTime(java.util.Date bookingdatetime) {
		this.bookingdatetime = bookingdatetime;
	}

	/**
	 * @return the valueDateTime
	 */
	public java.util.Date getvalueDateTime() {
		return valuedatetime;
	}

	/**
	 * @param valuedatetime the valueDateTime to set
	 */
	public void setvalueDateTime(java.util.Date valuedatetime) {
		this.valuedatetime = valuedatetime;
	}

	/**
	 * @return the quotationDate
	 */
	public java.util.Date getquotationDate() {
		return quotationdate;
	}

	/**
	 * @param quotationdate the quotationDate to set
	 */
	public void setquotationDate(java.util.Date quotationdate) {
		this.quotationdate = quotationdate;
	}

	/**
	 * @return the frequencyType
	 */
	public String getfrequencyType() {
		return frequencytype;
	}

	/**
	 * @param frequencytype the frequencyType to set
	 */
	public void setfrequencyType(String frequencytype) {
		this.frequencytype = frequencytype;
	}

	/**
	 * @return the category
	 */
	public String getcategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setcategory(String category) {
		this.category = category;
	}

	/**
	 * @return the billcategory
	 */
	public String getbillCategory() {
		return billCategory;
	}

	/**
	 * @param billcategory the billcategory to set
	 */
	public void setbillcategory(String billcategory) {
		this.billCategory = billcategory;
	}
}