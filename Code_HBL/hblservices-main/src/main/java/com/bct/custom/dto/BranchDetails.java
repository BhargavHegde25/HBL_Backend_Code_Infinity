package com.bct.custom.dto;

import java.util.Objects;

import org.supercsv.cellprocessor.ParseInt;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;




@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BranchDetails implements DBPDTO  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("id")
	private Integer id;
	@JsonAlias({ "bankCode" })
	private String bank_cd;
	@JsonAlias({ "bankName" })
	private String bank_name;
	@JsonAlias({ "bankShortCode" })
	private String bank_sc;
	@JsonAlias({ "branchCode" })
	private String branch_cd;
	@JsonAlias({ "branchName" })
	private String branch_name;
	@JsonAlias({ "bankSwift" })
	private String bank_swift;
	@JsonAlias({ "Status" })
	@JsonProperty("Status")
	private int Status;
	@JsonAlias({ "branchManagerEmail" })
	private String branch_mge_email;
	public BranchDetails() {
		super();
	}
	public BranchDetails(Integer id, String bank_cd, String bank_name, String bank_sc, String branch_cd,
			String branch_name, String bank_swift, int status, String branch_mge_email) {
		super();
		this.id = id;
		this.bank_cd = bank_cd;
		this.bank_name = bank_name;
		this.bank_sc = bank_sc;
		this.branch_cd = branch_cd;
		this.branch_name = branch_name;
		this.bank_swift = bank_swift;
		Status = status;
		this.branch_mge_email = branch_mge_email;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBank_cd() {
		return bank_cd;
	}
	public void setBank_cd(String bank_cd) {
		this.bank_cd = bank_cd;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getBank_sc() {
		return bank_sc;
	}
	public void setBank_sc(String bank_sc) {
		this.bank_sc = bank_sc;
	}
	public String getBranch_cd() {
		return branch_cd;
	}
	public void setBranch_cd(String branch_cd) {
		this.branch_cd = branch_cd;
	}
	public String getBranch_name() {
		return branch_name;
	}
	public void setBranch_name(String branch_name) {
		this.branch_name = branch_name;
	}
	public String getBank_swift() {
		return bank_swift;
	}
	public void setBank_swift(String bank_swift) {
		this.bank_swift = bank_swift;
	}
	public int getStatus() {
		return Status;
	}
	public void setStatus(int status) {
		Status = status;
	}
	public String getBranch_mge_email() {
		return branch_mge_email;
	}
	public void setBranch_mge_email(String branch_mge_email) {
		this.branch_mge_email = branch_mge_email;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public int hashCode() {
		return Objects.hash(Status, bank_cd, bank_name, bank_sc, bank_swift, branch_cd, branch_mge_email, branch_name,
				id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BranchDetails other = (BranchDetails) obj;
		return Status == other.Status && Objects.equals(bank_cd, other.bank_cd)
				&& Objects.equals(bank_name, other.bank_name) && Objects.equals(bank_sc, other.bank_sc)
				&& Objects.equals(bank_swift, other.bank_swift) && Objects.equals(branch_cd, other.branch_cd)
				&& Objects.equals(branch_mge_email, other.branch_mge_email)
				&& Objects.equals(branch_name, other.branch_name) && Objects.equals(id, other.id);
	}
	
	
}
