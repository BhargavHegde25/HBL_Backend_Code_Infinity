package com.temenos.infinity.smartbanking.advisory.utils;

import com.temenos.infinity.smartbanking.advisory.constants.GeneratedAccountPayablesDetailsEnum;

public enum ExportAccountPayablesEnum {
	GETCASHFLOWEXCEL(GeneratedAccountPayablesDetailsEnum.PSX.name(), 
		  new String[] {}),
	GETPAYABLESOVERDUEEXCEL(GeneratedAccountPayablesDetailsEnum.PSXAO.name(), 
			  new String[] {}),
	GETPAYABLESUPCOMINGEXCEL(GeneratedAccountPayablesDetailsEnum.PSXAU.name(), 
			  new String[] {}),
	GETPAYABLESCUSTOMEREXCEL(GeneratedAccountPayablesDetailsEnum.PSXCA.name(), 
			  new String[] {});
  
  private final String prefix;
  private final String[] headersList;
  
  ExportAccountPayablesEnum(String prefix, String[] headersList) {
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
