/**
 * 
 */
package com.temenos.infinity.api.wealthservices.constants;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author himaja.sridhar
 *
 */
public enum TAPEntityEnum {
	MASTER("MASTER", "dbpusermaster", "1026540"), GB0010001("GB0010001", "dbpuserbnk", "1026541"),
	NL0020001("NL0020001", "dbpuserbsg", "1026542"), EU0010001("EU0010001", "dbpusereu1", "1026543"),
	GB0010002("GB0010002", "dbpusermf1", "1026544"), SG0010001("SG0010001", "dbpusersg1", "1026545"),
	SG0020100("SG0020100", "dbpusersgp", "1026546"), GB0010003("GB0010003", "dbpusermf2", "1026547"),
	GB0010004("GB0010004", "dbpusermf3", "1026548"), GB0010005("GB0010005", "dbpuserbr1", "1026549"),
	SG0020101("SG0020101", "dbpusersgpdbu", "1026550"), SG0020102("SG0020102", "dbpusersgpacu", "1026551");

	private final String companyId;
	private final String userName;
	private final String custCode;

	TAPEntityEnum(String companyId, String userName, String custCode) {
		this.companyId = companyId;
		this.userName = userName;
		this.custCode = custCode;
	}

	public String getCompanyId() {
		return companyId;
	}
	public String getName() {
		return userName;
	}

	public String getCode() {
		return custCode;
	}

	public static Optional<TAPEntityEnum> getNameByCompanyId(String value) {
		return Arrays.stream(TAPEntityEnum.values())
				.filter(accStatus -> accStatus.companyId.equals(value) || accStatus.companyId.equals(value)).findFirst();
	}
}
