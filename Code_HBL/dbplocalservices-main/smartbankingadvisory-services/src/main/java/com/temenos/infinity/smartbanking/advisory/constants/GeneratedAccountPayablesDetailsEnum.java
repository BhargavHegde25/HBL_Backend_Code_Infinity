package com.temenos.infinity.smartbanking.advisory.constants;

public enum GeneratedAccountPayablesDetailsEnum {
  PSP("AID Payables.pdf", "application/pdf", "AID Payables"),
  PSX("AID Payables.xlsx", "application/octet-stream", "AID Payables"),
	
  PSPAO("AID Payables Overdue.pdf", "application/pdf", "AID Payables Overdue"),
  PSXAO("AID Payables Overdue.xlsx", "application/octet-stream", "AID Payables Overdue"),
	
  PSPAU("AID Payables Upcoming.pdf", "application/pdf", "AID Payables Upcoming"),
  PSXAU("AID Payables Upcoming.xlsx", "application/octet-stream", "AID Payables Upcoming"),
		
  PSPCA("AID Payables Bysupplier.pdf", "application/pdf", "AID Payables Bysupplier"),
  PSXCA("AID Payables Bysupplier.xlsx", "application/octet-stream", "AID Payables Bysupplier");
  
  private final String fileName;
  
  private final String contentType;
  
  private final String displayName;
  
  GeneratedAccountPayablesDetailsEnum(String fileName, String contentType, String displayName) {
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

