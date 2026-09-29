package com.temenos.infinity.smartbanking.advisory.utils;

import com.temenos.infinity.smartbanking.advisory.constants.GeneratedAccountReceivablesDetailsEnum;

public enum ExportAccountReceivablesEnum {
	GETCASHFLOWEXCEL(GeneratedAccountReceivablesDetailsEnum.RSX.name(), 
		  new String[] {}),
	GETOVERDUEEXCEL(GeneratedAccountReceivablesDetailsEnum.RSXAO.name(), 
			  new String[] {}),
	GETUPCOMINGEXCEL(GeneratedAccountReceivablesDetailsEnum.RSXAU.name(), 
			  new String[] {}),
	GETCUSTOMEREXCEL(GeneratedAccountReceivablesDetailsEnum.RSXCA.name(), 
			  new String[] {});
	
  private final String prefix;
  private final String[] headersList;
  
  ExportAccountReceivablesEnum(String prefix, String[] headersList) {
	  this.prefix = prefix;
      this.headersList = headersList;
  }
  
  public String getPrefix() {
    return this.prefix;
  }
  
  public String[] getHeadersList() {
    return this.headersList;
  }
}
