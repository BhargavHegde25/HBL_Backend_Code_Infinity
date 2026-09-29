package com.infinity.dbx.temenos.transfers;

import java.io.Serializable;

public class ExternalAccount implements Serializable {
	
	/**
	 * Unique ID for Serialization
	 */
	private static final long serialVersionUID = 887984530375346143L;

	/*
	 * Unique id for the beneficiary
	 */
	private String Id;
	
	private String accountNumber;
	
	private String nickName;
	
	private String swiftCode;
	
	private String versionNumber;
		
	/*
	 * Constructor
	 */
	public ExternalAccount() {
		
		this.setId("");
		this.setNickName("");
		this.setVersionNumber("");
		this.setSwiftCode("");
		this.setAccountNumber("");
				
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the swiftCode
	 */
	public String getSwiftCode() {
		return swiftCode;
	}

	/**
	 * @param swiftCode the swiftCode to set
	 */
	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	/**
	 * @return the versionNumber
	 */
	public String getVersionNumber() {
		return versionNumber;
	}

	/**
	 * @param versionNumber the versionNumber to set
	 */
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
		
}