package com.temenos.infinity.api.srmstransactions.config;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public enum SRMSTypeSubTypeConfiguration{
    
      ONE_TIME_OWN_ACOUNT_TRANSFER("OneTimeTransfer", "OwnAccountTransfer"),
      RECURRING_OWN_ACOUNT_TRANSFER("RecurringTransfer", "OwnAccountTransfer"),
      INTRA_BANK_ACCOUNT_TRANSFER("OneTimeTransfer", "SameBankTransfer"),
      RECURRING_INTRA_BANK_ACCOUNT_TRANSFER("RecurringTransfer", "SameBankTransfer"),
      ONE_TIME_INTER_INSTANT_TRANSFER("OneTimeTransfer", "InstantTransfer"),//Instpay Types
      ONE_TIME_INTER_SEPA_TRANSFER("OneTimeTransfer", "SEPATransfer"),
      ONE_TIME_INTER_FEDWIRE_TRANSFER("OneTimeTransfer", "FedwireTransfer"),
      ONE_TIME_INTER_ACH_TRANSFER("OneTimeTransfer", "ACHTransfer"),
      ONE_TIME_INTER_CHAPS_TRANSFER("OneTimeTransfer", "CHAPSTransfer"),
      ONE_TIME_INTER_FASTER_TRANSFER("OneTimeTransfer", "FasterTransfer"),
      ONE_TIME_INTER_RINGS_TRANSFER("OneTimeTransfer", "RINGSTransfer"),
      ONE_TIME_INTER_BISERA_TRANSFER("OneTimeTransfer", "BISERATransfer"),
      RECURRING_INTER_INSTANT_TRANSFER("RecurringTransfer", "InstantTransfer"),//Instpay Recurring Types
      RECURRING_INTER_SEPA_TRANSFER("RecurringTransfer", "SEPATransfer"),
      RECURRING_INTER_FEDWIRE_TRANSFER("RecurringTransfer", "FedwireTransfer"),
      RECURRING_INTER_ACH_TRANSFER("RecurringTransfer", "ACHTransfer"),
      RECURRING_INTER_CHAPS_TRANSFER("RecurringTransfer", "CHAPSTransfer"),
      RECURRING_INTER_FASTER_TRANSFER("RecurringTransfer", "FasterTransfer"),
      RECURRING_INTER_RINGS_TRANSFER("RecurringTransfer", "RINGSTransfer"),
      RECURRING_INTER_BISERA_TRANSFER("RecurringTransfer", "BISERATransfer"),
      ONETIME_SWIFT_TRANSFER("OneTimeTransfer", "SWIFTTransfer"),
      RECURRING_SWIFT_TRANSFER("RecurringTransfer", "SWIFTTransfer"),
      ONETIME_P2P_TRANSFER("OneTimeTransfer", "P2PTransfer"),
      RECURRING_P2P_TRANSFER("RecurringTransfer", "P2PTransfer"),
      QR_PAYMENT("QRPayments","QRPaymentsTransfer");
    
    private String type, subType;

    /**
     * @param type
     * @param subType
     */
    private SRMSTypeSubTypeConfiguration(String type, String subType) {
        this.type = type;
        this.subType = subType;
    }

    public String getType() {
        return this.type;
    }

    public String getSubType() {
        return this.subType;
    }

}
