package com.temenos.infinity.smartbanking.advisory.constants;

public enum GeneratedAccountReceivablesDetailsEnum {
  RSP("AID Receivables.pdf", "application/pdf", "AID Receivables"),
  RSX("AID Receivables.xlsx", "application/octet-stream", "AID Receivables"),
	
  RSPAO("AID Receivables Overdue.pdf", "application/pdf", "AID Receivables Overdue"),
  RSXAO("AID Receivables Overdue.xlsx", "application/octet-stream", "AID Receivables Overdue"),
	
  RSPAU("AID Receivables Upcoming.pdf", "application/pdf", "AID Receivables Upcoming"),
  RSXAU("AID Receivables Upcoming.xlsx", "application/octet-stream", "AID Receivables Upcoming"),
	
  RSPCA("AID Receivables Bycustomer.pdf", "application/pdf", "AID Receivables Bycustomer"),
  RSXCA("AID Receivables Bycustomer.xlsx", "application/octet-stream", "AID Receivables Bycustomer");
	
  private final String fileName;
  
  private final String contentType;
  
  private final String displayName;
  
  GeneratedAccountReceivablesDetailsEnum(String fileName, String contentType, String displayName) {
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

