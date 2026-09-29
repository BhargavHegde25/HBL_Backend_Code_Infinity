package com.temenos.infinity.api.chequemanagement.dto;

import java.io.Serializable;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class StopPayment implements Serializable, DBPDTO{
    
    private static final long serialVersionUID = 653792502456487768L;
    
    private String checkReason;
    private String fromAccountNumber;
    private String transactionStopConditionId;
    private String checkNumber1;
    private String checkNumber2;
    private String amount;
    private String payeeName;
    private String transactionsNotes;
    private String requestValidityInMonths;
    private String transactionType;
    private String checkDateOfIssue;
    private String validate;
    private String referenceId;
    private String statusDesc;
    private String offset;
    private String paginationRowLimit;
    private String order;
    private String sortBy;
    private String limit; 
    private String accountID;
    private String Id;
    private String fromAccountNickName;
    private String requestType;
    private String transactionDate;
    private String fee;
    private String status;
    private String chequeId;
    private String revokeDate;
    private String revokeChequeTypeId;
    private String isRevoke;
    public String note;
    public String code;
    public String message;
    public String transactionCurrency;
    
	public StopPayment() {
        super();
    }

    public StopPayment(String checkReason, String fromAccountNumber, String transactionStopConditionId,
            String checkNumber1, String checkNumber2, String amount, String payeeName, String transactionsNotes,
            String requestValidityInMonths, String transactionType, String checkDateOfIssue, String validate,
            String referenceId, String statusDesc, String offset, String paginationRowLimit, String order, String sortBy,
            String limit, String accountID, String Id, String fromAccountNickName, String requestType,
            String transactionDate, String fee, String status, String chequeId, String revokeDate, String revokeChequeTypeId,
            String isRevoke, String note, String code, String message, String transactionCurrency) {
        super();
        this.checkReason = checkReason;
        this.fromAccountNumber = fromAccountNumber;
        this.transactionStopConditionId = transactionStopConditionId;
        this.checkNumber1 = checkNumber1;
        this.checkNumber2 = checkNumber2;
        this.amount = amount;
        this.payeeName = payeeName;
        this.transactionsNotes = transactionsNotes;
        this.requestValidityInMonths = requestValidityInMonths;
        this.transactionType = transactionType;
        this.checkDateOfIssue = checkDateOfIssue;
        this.validate = validate;
        this.referenceId = referenceId;
        this.statusDesc = statusDesc;
        this.offset = offset;
        this.paginationRowLimit = paginationRowLimit;
        this.order = order;
        this.sortBy = sortBy;
        this.limit = limit;
        this.accountID = accountID;
        this.Id = Id;
        this.fromAccountNickName = fromAccountNickName;
        this.requestType = requestType;
        this.transactionDate = transactionDate;
        this.fee = fee;
        this.status = status;
        this.chequeId = chequeId;
        this.revokeDate = revokeDate;
        this.revokeChequeTypeId = revokeChequeTypeId;
        this.isRevoke = isRevoke;
        this.note = note;
        this.code = code;
        this.message = message; 
        this.transactionCurrency = transactionCurrency;
    }
    
    public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

    public String getCheckReason() {
        return checkReason;
    }

    public void setCheckReason(String checkReason) {
        this.checkReason = checkReason;
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public String getTransactionStopConditionId() {
        return transactionStopConditionId;
    }

    public void setTransactionStopConditionId(String transactionStopConditionId) {
        this.transactionStopConditionId = transactionStopConditionId;
    }

    public String getCheckNumber1() {
        return checkNumber1;
    }

    public void setCheckNumber1(String checkNumber1) {
        this.checkNumber1 = checkNumber1;
    }

    public String getCheckNumber2() {
        return checkNumber2;
    }

    public void setCheckNumber2(String checkNumber2) {
        this.checkNumber2 = checkNumber2;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getTransactionsNotes() {
        return transactionsNotes;
    }

    public void setTransactionsNotes(String transactionsNotes) {
        this.transactionsNotes = transactionsNotes;
    }

    public String getRequestValidityInMonths() {
        return requestValidityInMonths;
    }

    public void setRequestValidityInMonths(String requestValidityInMonths) {
        this.requestValidityInMonths = requestValidityInMonths;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getCheckDateOfIssue() {
        return checkDateOfIssue;
    }

    public void setCheckDateOfIssue(String checkDateOfIssue) {
        this.checkDateOfIssue = checkDateOfIssue;
    }

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getPaginationRowLimit() {
        return paginationRowLimit;
    }

    public void setPaginationRowLimit(String paginationRowLimit) {
        this.paginationRowLimit = paginationRowLimit;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getFromAccountNickName() {
        return fromAccountNickName;
    }

    public void setFromAccountNickName(String fromAccountNickName) {
        this.fromAccountNickName = fromAccountNickName;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }
    
    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
    
    public String getChequeId() {
		return chequeId;
	}

	public void setChequeId(String chequeId) {
		this.chequeId = chequeId;
	}

	public String getRevokeDate() {
		return revokeDate;
	}

	public void setRevokeDate(String revokeDate) {
		this.revokeDate = revokeDate;
	}

	public String getRevokeChequeTypeId() {
		return revokeChequeTypeId;
	}

	public void setRevokeChequeTypeId(String revokeChequeTypeId) {
		this.revokeChequeTypeId = revokeChequeTypeId;
	}

	public String getIsRevoke() {
		return isRevoke;
	}

	public void setIsRevoke(String isRevoke) {
		this.isRevoke = isRevoke;
	}

	public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((Id == null) ? 0 : Id.hashCode());
        result = prime * result + ((accountID == null) ? 0 : accountID.hashCode());
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((checkDateOfIssue == null) ? 0 : checkDateOfIssue.hashCode());
        result = prime * result + ((checkNumber1 == null) ? 0 : checkNumber1.hashCode());
        result = prime * result + ((checkNumber2 == null) ? 0 : checkNumber2.hashCode());
        result = prime * result + ((checkReason == null) ? 0 : checkReason.hashCode());
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((fee == null) ? 0 : fee.hashCode());
        result = prime * result + ((fromAccountNickName == null) ? 0 : fromAccountNickName.hashCode());
        result = prime * result + ((fromAccountNumber == null) ? 0 : fromAccountNumber.hashCode());
        result = prime * result + ((limit == null) ? 0 : limit.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((note == null) ? 0 : note.hashCode());
        result = prime * result + ((offset == null) ? 0 : offset.hashCode());
        result = prime * result + ((order == null) ? 0 : order.hashCode());
        result = prime * result + ((paginationRowLimit == null) ? 0 : paginationRowLimit.hashCode());
        result = prime * result + ((payeeName == null) ? 0 : payeeName.hashCode());
        result = prime * result + ((referenceId == null) ? 0 : referenceId.hashCode());
        result = prime * result + ((requestType == null) ? 0 : requestType.hashCode());
        result = prime * result + ((requestValidityInMonths == null) ? 0 : requestValidityInMonths.hashCode());
        result = prime * result + ((sortBy == null) ? 0 : sortBy.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((statusDesc == null) ? 0 : statusDesc.hashCode());
        result = prime * result + ((transactionDate == null) ? 0 : transactionDate.hashCode());
        result = prime * result + ((transactionStopConditionId == null) ? 0 : transactionStopConditionId.hashCode());
        result = prime * result + ((transactionType == null) ? 0 : transactionType.hashCode());
        result = prime * result + ((transactionsNotes == null) ? 0 : transactionsNotes.hashCode());
        result = prime * result + ((chequeId == null) ? 0 : chequeId.hashCode());
        result = prime * result + ((revokeDate == null) ? 0 : revokeDate.hashCode());
        result = prime * result + ((revokeChequeTypeId == null) ? 0 : revokeChequeTypeId.hashCode());
        result = prime * result + ((isRevoke == null) ? 0 : isRevoke.hashCode());
        result = prime * result + ((validate == null) ? 0 : validate.hashCode());
        result = prime * result + ((transactionCurrency == null) ? 0 : transactionCurrency.hashCode());
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
        StopPayment other = (StopPayment) obj;
        if (Id == null) {
            if (other.Id != null)
                return false;
        } else if (!Id.equals(other.Id))
            return false;
        if (accountID == null) {
            if (other.accountID != null)
                return false;
        } else if (!accountID.equals(other.accountID))
            return false;
        if (amount == null) {
            if (other.amount != null)
                return false;
        } else if (!amount.equals(other.amount))
            return false;
        if (checkDateOfIssue == null) {
            if (other.checkDateOfIssue != null)
                return false;
        } else if (!checkDateOfIssue.equals(other.checkDateOfIssue))
            return false;
        if (checkNumber1 == null) {
            if (other.checkNumber1 != null)
                return false;
        } else if (!checkNumber1.equals(other.checkNumber1))
            return false;
        if (checkNumber2 == null) {
            if (other.checkNumber2 != null)
                return false;
        } else if (!checkNumber2.equals(other.checkNumber2))
            return false;
        if (checkReason == null) {
            if (other.checkReason != null)
                return false;
        } else if (!checkReason.equals(other.checkReason))
            return false;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (fee == null) {
            if (other.fee != null)
                return false;
        } else if (!fee.equals(other.fee))
            return false;
        if (fromAccountNickName == null) {
            if (other.fromAccountNickName != null)
                return false;
        } else if (!fromAccountNickName.equals(other.fromAccountNickName))
            return false;
        if (fromAccountNumber == null) {
            if (other.fromAccountNumber != null)
                return false;
        } else if (!fromAccountNumber.equals(other.fromAccountNumber))
            return false;
        if (limit == null) {
            if (other.limit != null)
                return false;
        } else if (!limit.equals(other.limit))
            return false;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (note == null) {
            if (other.note != null)
                return false;
        } else if (!note.equals(other.note))
            return false;
        if (offset == null) {
            if (other.offset != null)
                return false;
        } else if (!offset.equals(other.offset))
            return false;
        if (order == null) {
            if (other.order != null)
                return false;
        } else if (!order.equals(other.order))
            return false;
        if (paginationRowLimit == null) {
            if (other.paginationRowLimit != null)
                return false;
        } else if (!paginationRowLimit.equals(other.paginationRowLimit))
            return false;
        if (payeeName == null) {
            if (other.payeeName != null)
                return false;
        } else if (!payeeName.equals(other.payeeName))
            return false;
        if (referenceId == null) {
            if (other.referenceId != null)
                return false;
        } else if (!referenceId.equals(other.referenceId))
            return false;
        if (requestType == null) {
            if (other.requestType != null)
                return false;
        } else if (!requestType.equals(other.requestType))
            return false;
        if (requestValidityInMonths == null) {
            if (other.requestValidityInMonths != null)
                return false;
        } else if (!requestValidityInMonths.equals(other.requestValidityInMonths))
            return false;
        if (sortBy == null) {
            if (other.sortBy != null)
                return false;
        } else if (!sortBy.equals(other.sortBy))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (statusDesc == null) {
            if (other.statusDesc != null)
                return false;
        } else if (!statusDesc.equals(other.statusDesc))
            return false;
        if (transactionDate == null) {
            if (other.transactionDate != null)
                return false;
        } else if (!transactionDate.equals(other.transactionDate))
            return false;
        if (transactionStopConditionId == null) {
            if (other.transactionStopConditionId != null)
                return false;
        } else if (!transactionStopConditionId.equals(other.transactionStopConditionId))
            return false;
        if (transactionType == null) {
            if (other.transactionType != null)
                return false;
        } else if (!transactionType.equals(other.transactionType))
            return false;
        if (transactionsNotes == null) {
            if (other.transactionsNotes != null)
                return false;
        } else if (!transactionsNotes.equals(other.transactionsNotes))
            return false;
        if (chequeId == null) {
            if (other.chequeId != null)
                return false;
        } else if (!chequeId.equals(other.chequeId))
            return false;
        if (revokeDate == null) {
            if (other.revokeDate != null)
                return false;
        } else if (!revokeDate.equals(other.revokeDate))
            return false;
        if (revokeChequeTypeId == null) {
            if (other.revokeChequeTypeId != null)
                return false;
        } else if (!revokeChequeTypeId.equals(other.revokeChequeTypeId))
            return false;
        if (isRevoke == null) {
            if (other.isRevoke != null)
                return false;
        } else if (!isRevoke.equals(other.isRevoke))
            return false;
        if (validate == null) {
            if (other.validate != null)
                return false;
        } else if (!validate.equals(other.validate))
            return false;
        if (transactionCurrency == null) {
            if (other.transactionCurrency != null)
                return false;
        } else if (!transactionCurrency.equals(other.transactionCurrency))
            return false;
        return true;
    }

    
}
