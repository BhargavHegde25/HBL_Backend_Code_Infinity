package com.temenos.infinity.smartbanking.advisory.utils;

import com.temenos.infinity.smartbanking.advisory.constants.GeneratedCashFlowFileDetailsEnum;

public enum ExportCashFlowEnum {
	GETCASHFLOWEXCEL(GeneratedCashFlowFileDetailsEnum.CFX.name(), 
		  new String[] { "Cash Inflow", "Cash Outflow", "Cash On Hand", "Net Disposable Income" });
  
  private final String prefix;
  private final String[] headersList;
  
  ExportCashFlowEnum(String prefix, String[] headersList) {
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
