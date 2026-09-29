package com.temenos.msArrangement.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL) 
public class ArrangementsDTO implements DBPDTO {

    private static final long serialVersionUID = 5035451677589972439L;
    
    private String Account_id;
    private String arrangementId;
    private String companyCode;
    private String statusDesc;
    private String product;
    private String accountName;
    private String typeDescription; 
    private Double availableBalance;
    private String bankname; 
    private String currencyCode; 
    private int currentBalance; 
    private String nickName;
    private String displayName; 
    private String openingDate;
    private int workingBalance;
    private String swiftCode;
    private String accountHolder;
    private String jointHolders;
    private String dividendLastPaidDate;
    private Double dividendPaidYTD;
    private Double dividendLastPaidAmount;
    private float dividendRate;
    private String accountCategory;
    private String productLineName;
    private String routingNumber; 
    private String availableLimit;
    private Double pendingDeposit; 
    private Double pendingWithdrawal;
    private String supportBillPay;
    private String supportChecks;
    private String supportDeposit;
    private String supportTransferFrom;
    private String supportTransferTo;
   
    
    
    public ArrangementsDTO() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    public ArrangementsDTO(String Account_id, String arrangementId, String companyCode, String statusDesc,
            String product, String accountName, String typeDescription, Double availableBalance, String bankname,
            String currencyCode, int currentBalance, String nickName, String displayName, String openingDate,
            int workingBalance, String swiftCode, String accountHolder, String jointHolders,
            String dividendLastPaidDate, Double dividendPaidYTD, Double dividendLastPaidAmount, float dividendRate,
            String accountCategory, String productLineName, String routingNumber, String availableLimit,
            Double pendingDeposit, Double pendingWithdrawal, String supportBillPay, String supportChecks,
            String supportDeposit, String supportTransferFrom, String supportTransferTo) {
        super();
        this.Account_id = Account_id;
        this.arrangementId = arrangementId;
        this.companyCode = companyCode;
        this.statusDesc = statusDesc;
        this.product = product;
        this.accountName = accountName;
        this.typeDescription = typeDescription;
        this.availableBalance = availableBalance;
        this.bankname = bankname;
        this.currencyCode = currencyCode;
        this.currentBalance = currentBalance;
        this.nickName = nickName;
        this.displayName = displayName;
        this.openingDate = openingDate;
        this.workingBalance = workingBalance;
        this.swiftCode = swiftCode;
        this.accountHolder = accountHolder;
        this.jointHolders = jointHolders;
        this.dividendLastPaidDate = dividendLastPaidDate;
        this.dividendPaidYTD = dividendPaidYTD;
        this.dividendLastPaidAmount = dividendLastPaidAmount;
        this.dividendRate = dividendRate;
        this.accountCategory = accountCategory;
        this.productLineName = productLineName;
        this.routingNumber = routingNumber;
        this.availableLimit = availableLimit;
        this.pendingDeposit = pendingDeposit;
        this.pendingWithdrawal = pendingWithdrawal;
        this.supportBillPay = supportBillPay;
        this.supportChecks = supportChecks;
        this.supportDeposit = supportDeposit;
        this.supportTransferFrom = supportTransferFrom;
        this.supportTransferTo = supportTransferTo;
    }


    public String getAvailableLimit() {
        return availableLimit;
    }
    public void setAvailableLimit(String availableLimit) {
        this.availableLimit = availableLimit;
    }
    public String getAccount_id() {
        return Account_id;
    }
    public void setAccount_id(String Account_id) {
        this.Account_id = Account_id;
    }
    public String getArrangementId() {
        return arrangementId;
    }
    public void setArrangementId(String arrangementId) {
        this.arrangementId = arrangementId;
    }
    public String getCompanyCode() {
        return companyCode;
    }
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
    public String getStatusDesc() {
        return statusDesc;
    }
    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = product;
    }
    public String getAccountName() {
        return accountName;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public String getTypeDescription() {
        return typeDescription;
    }
    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }
    public Double getAvailableBalance() {
        return availableBalance;
    }
    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }
    public String getBankname() {
        return bankname;
    }
    public void setBankname(String bankname) {
        this.bankname = bankname;
    }
    public String getCurrencyCode() { 
        return currencyCode;
    }
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    public int getCurrentBalance() {
        return currentBalance;
    }
    public void setCurrentBalance(int currentBalance) {
        this.currentBalance = currentBalance;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getOpeningDate() {
        return openingDate;
    }
    public void setOpeningDate(String openingDate) {
        this.openingDate = openingDate;
    }
    public int getWorkingBalance() {
        return workingBalance;
    }
    public void setWorkingBalance(int workingBalance) {
        this.workingBalance = workingBalance;
    }
    public String getSwiftCode() {
        return swiftCode;
    }
    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }
    public String getDividendLastPaidDate() { 
        return dividendLastPaidDate;
    }
    public void setDividendLastPaidDate(String dividendLastPaidDate) {
        this.dividendLastPaidDate = dividendLastPaidDate;
    }
    public Double getDividendPaidYTD() {
        return dividendPaidYTD;
    }
    public void setDividendPaidYTD(Double dividendPaidYTD) {
        this.dividendPaidYTD = dividendPaidYTD;
    }
    public Double getDividendLastPaidAmount() {
        return dividendLastPaidAmount;
    }
    public void setDividendLastPaidAmount(Double dividendLastPaidAmount) {
        this.dividendLastPaidAmount = dividendLastPaidAmount;
    }
    public float getDividendRate() {
        return dividendRate;
    }
    public void setDividendRate(float dividendRate) {
        this.dividendRate = dividendRate; 
    }
    public String getAccountCategory() {
        return accountCategory;
    }
    public void setAccountCategory(String accountCategory) {
        this.accountCategory = accountCategory;
    }
    public String getProductLineName() {
        return productLineName;
    }
    public void setProductLineName(String productLineName) {
        this.productLineName = productLineName;
    }
    public String getRoutingNumber() {
        return routingNumber;
    }
    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }
    public String getSupportBillPay() {
        return supportBillPay;
    }
    public void setSupportBillPay(String supportBillPay) {
        this.supportBillPay = supportBillPay;
    }
    public String getSupportChecks() {
        return supportChecks;
    }
    public void setSupportChecks(String supportChecks) {
        this.supportChecks = supportChecks;
    }
    public String getSupportDeposit() {
        return supportDeposit;
    }
    public void setSupportDeposit(String supportDeposit) {
        this.supportDeposit = supportDeposit;
    }
    public String getSupportTransferFrom() {
        return supportTransferFrom;
    }
    public void setSupportTransferFrom(String supportTransferFrom) {
        this.supportTransferFrom = supportTransferFrom;
    }
    public String getSupportTransferTo() {
        return supportTransferTo;
    }
    public void setSupportTransferTo(String supportTransferTo) {
        this.supportTransferTo = supportTransferTo;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public Double getPendingDeposit() {
        return pendingDeposit;
    }
    public void setPendingDeposit(Double pendingDeposit) {
        this.pendingDeposit = pendingDeposit;
    }
    public Double getPendingWithdrawal() {
        return pendingWithdrawal;
    }
    public void setPendingWithdrawal(Double pendingWithdrawal) {
        this.pendingWithdrawal = pendingWithdrawal;
    }
    public String getAccountHolder() {
        return accountHolder;
    }
    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }
    public String getJointHolders() {
        return jointHolders;
    }
    public void setJointHolders(String jointHolders) {
        this.jointHolders = jointHolders;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accountCategory == null) ? 0 : accountCategory.hashCode());
        result = prime * result + ((accountHolder == null) ? 0 : accountHolder.hashCode());
        result = prime * result + ((Account_id == null) ? 0 : Account_id.hashCode());
        result = prime * result + ((accountName == null) ? 0 : accountName.hashCode());
        result = prime * result + ((typeDescription == null) ? 0 : typeDescription.hashCode());
        result = prime * result + ((arrangementId == null) ? 0 : arrangementId.hashCode());
        result = prime * result + ((statusDesc == null) ? 0 : statusDesc.hashCode());
        result = prime * result + ((availableBalance == null) ? 0 : availableBalance.hashCode());
        result = prime * result + ((availableLimit == null) ? 0 : availableLimit.hashCode());
        result = prime * result + ((bankname == null) ? 0 : bankname.hashCode());
        result = prime * result + ((companyCode == null) ? 0 : companyCode.hashCode());
        result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
        result = prime * result + currentBalance;
        result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
        result = prime * result + ((dividendLastPaidAmount == null) ? 0 : dividendLastPaidAmount.hashCode());
        result = prime * result + ((dividendLastPaidDate == null) ? 0 : dividendLastPaidDate.hashCode());
        result = prime * result + ((dividendPaidYTD == null) ? 0 : dividendPaidYTD.hashCode());
        result = prime * result + Float.floatToIntBits(dividendRate);
        result = prime * result + ((jointHolders == null) ? 0 : jointHolders.hashCode());
        result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
        result = prime * result + ((openingDate == null) ? 0 : openingDate.hashCode());
        result = prime * result + ((pendingDeposit == null) ? 0 : pendingDeposit.hashCode());
        result = prime * result + ((pendingWithdrawal == null) ? 0 : pendingWithdrawal.hashCode());
        result = prime * result + ((product == null) ? 0 : product.hashCode());
        result = prime * result + ((productLineName == null) ? 0 : productLineName.hashCode());
        result = prime * result + ((routingNumber == null) ? 0 : routingNumber.hashCode());
        result = prime * result + ((supportBillPay == null) ? 0 : supportBillPay.hashCode());
        result = prime * result + ((supportChecks == null) ? 0 : supportChecks.hashCode());
        result = prime * result + ((supportDeposit == null) ? 0 : supportDeposit.hashCode());
        result = prime * result + ((supportTransferFrom == null) ? 0 : supportTransferFrom.hashCode());
        result = prime * result + ((supportTransferTo == null) ? 0 : supportTransferTo.hashCode());
        result = prime * result + ((swiftCode == null) ? 0 : swiftCode.hashCode());
        result = prime * result + workingBalance;
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
        ArrangementsDTO other = (ArrangementsDTO) obj;
        if (accountCategory == null) {
            if (other.accountCategory != null)
                return false;
        } else if (!accountCategory.equals(other.accountCategory))
            return false;
        if (accountHolder == null) {
            if (other.accountHolder != null)
                return false;
        } else if (!accountHolder.equals(other.accountHolder))
            return false;
        if (Account_id == null) {
            if (other.Account_id != null)
                return false;
        } else if (!Account_id.equals(other.Account_id))
            return false;
        if (accountName == null) {
            if (other.accountName != null)
                return false;
        } else if (!accountName.equals(other.accountName))
            return false;
        if (typeDescription == null) { 
            if (other.typeDescription != null)
                return false;
        } else if (!typeDescription.equals(other.typeDescription))
            return false;
        if (arrangementId == null) {
            if (other.arrangementId != null) 
                return false;
        } else if (!arrangementId.equals(other.arrangementId))
            return false;
        if (statusDesc == null) {
            if (other.statusDesc != null)
                return false;
        } else if (!statusDesc.equals(other.statusDesc))
            return false;
        if (availableBalance == null) { 
            if (other.availableBalance != null)
                return false;
        } else if (!availableBalance.equals(other.availableBalance))
            return false;
        if (availableLimit == null) {
            if (other.availableLimit != null)
                return false;
        } else if (!availableLimit.equals(other.availableLimit))
            return false;
        if (bankname == null) {
            if (other.bankname != null)
                return false;
        } else if (!bankname.equals(other.bankname))
            return false;
        if (companyCode == null) {
            if (other.companyCode != null)
                return false;
        } else if (!companyCode.equals(other.companyCode))
            return false;
        if (currencyCode == null) {
            if (other.currencyCode != null)
                return false;
        } else if (!currencyCode.equals(other.currencyCode))
            return false;
        if (currentBalance != other.currentBalance)
            return false;
        if (displayName == null) {
            if (other.displayName != null)
                return false;
        } else if (!displayName.equals(other.displayName))
            return false;
        if (dividendLastPaidAmount == null) {
            if (other.dividendLastPaidAmount != null)
                return false;
        } else if (!dividendLastPaidAmount.equals(other.dividendLastPaidAmount))
            return false;
        if (dividendLastPaidDate == null) {
            if (other.dividendLastPaidDate != null)
                return false;
        } else if (!dividendLastPaidDate.equals(other.dividendLastPaidDate))
            return false;
        if (dividendPaidYTD == null) {
            if (other.dividendPaidYTD != null)
                return false;
        } else if (!dividendPaidYTD.equals(other.dividendPaidYTD))
            return false;
        if (Float.floatToIntBits(dividendRate) != Float.floatToIntBits(other.dividendRate))
            return false;
        if (jointHolders == null) {
            if (other.jointHolders != null)
                return false;
        } else if (!jointHolders.equals(other.jointHolders))
            return false;
        if (nickName == null) {
            if (other.nickName != null)
                return false;
        } else if (!nickName.equals(other.nickName))
            return false;
        if (openingDate == null) {
            if (other.openingDate != null)
                return false;
        } else if (!openingDate.equals(other.openingDate))
            return false;
        if (pendingDeposit == null) {
            if (other.pendingDeposit != null)
                return false;
        } else if (!pendingDeposit.equals(other.pendingDeposit))
            return false;
        if (pendingWithdrawal == null) {
            if (other.pendingWithdrawal != null)
                return false;
        } else if (!pendingWithdrawal.equals(other.pendingWithdrawal))
            return false;
        if (product == null) {
            if (other.product != null)
                return false;
        } else if (!product.equals(other.product))
            return false;
        if (productLineName == null) {
            if (other.productLineName != null)
                return false;
        } else if (!productLineName.equals(other.productLineName))
            return false;
        if (routingNumber == null) {
            if (other.routingNumber != null)
                return false;
        } else if (!routingNumber.equals(other.routingNumber))
            return false;
        if (supportBillPay == null) {
            if (other.supportBillPay != null)
                return false;
        } else if (!supportBillPay.equals(other.supportBillPay))
            return false;
        if (supportChecks == null) {
            if (other.supportChecks != null)
                return false;
        } else if (!supportChecks.equals(other.supportChecks))
            return false;
        if (supportDeposit == null) {
            if (other.supportDeposit != null)
                return false;
        } else if (!supportDeposit.equals(other.supportDeposit))
            return false;
        if (supportTransferFrom == null) {
            if (other.supportTransferFrom != null)
                return false;
        } else if (!supportTransferFrom.equals(other.supportTransferFrom))
            return false;
        if (supportTransferTo == null) {
            if (other.supportTransferTo != null)
                return false;
        } else if (!supportTransferTo.equals(other.supportTransferTo))
            return false;
        if (swiftCode == null) {
            if (other.swiftCode != null)
                return false;
        } else if (!swiftCode.equals(other.swiftCode))
            return false;
        if (workingBalance != other.workingBalance)
            return false;
        return true;
    }
    
    
    
}
