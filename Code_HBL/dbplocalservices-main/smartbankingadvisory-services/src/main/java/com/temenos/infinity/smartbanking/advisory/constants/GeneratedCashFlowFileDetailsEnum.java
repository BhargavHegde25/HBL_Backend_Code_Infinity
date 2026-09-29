package com.temenos.infinity.smartbanking.advisory.constants;

public enum GeneratedCashFlowFileDetailsEnum {
  CFP("CashFlow.pdf", "application/pdf", "Cash Flow"),
  CFX("CashFlow.xlsx", "application/octet-stream", "Cash Flow");
  
  private final String fileName;
  
  private final String contentType;
  
  private final String displayName;
  
  GeneratedCashFlowFileDetailsEnum(String fileName, String contentType, String displayName) {
    this.fileName = fileName;
    this.contentType = contentType;
    this.displayName = displayName;
  }
  
  public String getFileName() {
    return this.fileName;
  }
  
  public String getContentType() {
    return this.contentType;
  }
  
  public String getDisplayName() {
    return this.displayName;
  }
}
