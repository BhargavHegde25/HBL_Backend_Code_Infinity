package com.temenos.dbx.product.constants;

/**
 * Contains all the constants for Transaction status
 * @author kh1755
 *
 */
public enum TransactionStatusEnum {
	
	OLD_TRANSACTION("OldTransaction", "Old Transcation"),
	PENDING("Pending", "Needs approval"),
	APPROVED("Approved", "Approved for execution"),
	SELF_APPROVED("Self Approved", "Success! Your transaction has been completed with Self Approval"),
	INVALID("Invalid", "Invalid"),
	REJECTED("Rejected", "Rejected for execution"),
	SENT("Sent", "Success! Your transaction has been completed"),
	NEW("New", "Newly created"),
	EXECUTED("Executed", "Executed at the backend"),
	FAILED("Failed", "Failed while executing at the backend"),
	DENIED_DAILY("Denied", "Denied due to daily limit"),
	DENIED_AD_DAILY("Denied", "Denied due to Auto denial on daily limit"),
	DENIED_WEEKLY("Denied", "Denied due to weekly limit"),
	DENIED_AD_WEEKLY("Denied", "Denied due to auto denial on weekly limit"),
	DENIED_AD_MAX_TRANSACTION("Denied", "Denied due to auto deniel on max transaction limit"),
	DENIED_MAX_TRANSACTION("Denied", "Denied due to max transaction limit"),
	DENIED_MIN_TRANSACTION("Denied", "Denied due to min transaction limit"),
	DENIED_INVALID_APPROVAL_MATRIX("Denied", "Transaction cannot be executed. Please update the approval matrix of your organization and re-submit the transaction"),
	WITHDRAWN("Withdrawn","Withdrawn for execution"),
	CANCELLED("Cancelled","Cancelled"),
	DISCARDED("Discarded","Discarded"),
	EDITED("Edited", "Edited"), 
	READY("Ready", "Ready"),
	CREATED("Created", "Created"),
	UPLOADED("Uploaded", "Uploaded"),
	READY_FOR_EXECUTION("Ready for Execution", "Ready for Execution"),
	NEWLY_ADDED("Newly Added", "Newly Added"),
	MODIFIED("Modified", "Modified"),
	REVERSED("Reversed", "Transaction Reversed"),
	REVERSAL_FAILED("ReversalFailed ", "Transaction Reverse Failed"),
	REVERSAL_SENT("SentToReversal", "Transaction Sent to Reversal Process"),
	FAILED_WITH_DEBIT_REF_ID("Transaction Failed", "We were unable to process your payment at this movement please try after some time. If the amount has been debited, it will be credited back to your account within 5 business days. We apologize for any inconvenience.");
	
	private String status;
    private String message;

    private TransactionStatusEnum(String status, String message) {
        this.status = status;
        this.message = message;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
