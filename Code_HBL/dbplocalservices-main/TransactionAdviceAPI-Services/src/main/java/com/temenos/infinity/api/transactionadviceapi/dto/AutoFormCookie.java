package com.temenos.infinity.api.transactionadviceapi.dto;

import java.io.Serializable;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class AutoFormCookie implements Serializable, DBPDTO {

	private static final long serialVersionUID = 659792507420487769L;
	private String xsrftoken;
	private String JSESSIONID;

	public AutoFormCookie() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getXsrftoken() {
		return xsrftoken;
	}

	public void setXsrftoken(String xsrftoken) {
		this.xsrftoken = xsrftoken;
	}

	public String getJSESSIONID() {
		return JSESSIONID;
	}

	public void setJSESSIONID(String jSESSIONID) {
		JSESSIONID = jSESSIONID;
	}

}
