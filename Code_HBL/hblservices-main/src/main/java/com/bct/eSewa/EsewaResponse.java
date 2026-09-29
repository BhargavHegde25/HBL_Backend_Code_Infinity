package com.bct.eSewa;

import java.util.List;

public class EsewaResponse {
    private boolean success;
    private String code;
    private String message;
    private String bankCode;
    private String accountName;
    private String maskedAccountName;
    private List<Account> accounts;

    
    // Getters and setters
    

    public boolean isSuccess() {
		return success;
	}


	public void setSuccess(boolean success) {
		this.success = success;
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


	public String getBankCode() {
		return bankCode;
	}


	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}


	public String getAccountName() {
		return accountName;
	}


	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}


	public String getMaskedAccountName() {
		return maskedAccountName;
	}


	public void setMaskedAccountName(String maskedAccountName) {
		this.maskedAccountName = maskedAccountName;
	}


	public List<Account> getAccounts() {
		return accounts;
	}


	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}


	public static class Account {
        private String accountNumber;
        private String maskedAccount;
        
		public String getAccountNumber() {
			return accountNumber;
		}
		public void setAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
		}
		public String getMaskedAccount() {
			return maskedAccount;
		}
		public void setMaskedAccount(String maskedAccount) {
			this.maskedAccount = maskedAccount;
		}

        // Getters and setters
    }


	@Override
	public String toString() {
		return "EsewaResponse [success=" + success + ", code=" + code + ", message=" + message + ", bankCode="
				+ bankCode + ", accountName=" + accountName + ", maskedAccountName=" + maskedAccountName + ", accounts="
				+ accounts + "]";
	}
	
}
