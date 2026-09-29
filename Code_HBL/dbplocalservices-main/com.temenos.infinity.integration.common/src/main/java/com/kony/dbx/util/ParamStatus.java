package com.kony.dbx.util;

public class ParamStatus {

	private String name;
	private boolean paramValid;

	public ParamStatus(String name, boolean paramValid) {
		this.name = name;
		this.paramValid = paramValid;
	}
	
	public String getParamName() {
		return this.name;
	}

	public boolean isValid() {
		return this.paramValid;
	}
}