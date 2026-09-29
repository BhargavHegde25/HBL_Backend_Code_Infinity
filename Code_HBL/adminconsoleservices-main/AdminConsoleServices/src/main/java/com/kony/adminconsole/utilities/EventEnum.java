package com.kony.adminconsole.utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum that holds the all events done by an admin.
 * 
 * 
 *
 */

public enum EventEnum {

    CREATE("Create"), UPDATE("Update"), DELETE("Delete"), LOGIN("Login"), SEARCH("Search"),
    DOWNLOADFILE("Download File"), UPLOADFILE("Upload File"), COMMUNICATION("Communication"),
    SUBMITTEDFORAPPROVAL("Submit For Approval"), APPROVEREQUEST("Approve Request"), REJECTREQUEST("Reject Request"), 
    INITIATECREATE("Initiate Create"), INITIATEEDIT("Initiate Edit"), INITIATEDELETE("Initiate Delete"),
    INITIATEENROLL("Initiate Enroll"), APPROVECREATE("Approve Create"), APPROVEEDIT("Approve Edit"),
    APPROVEDELETE("Approve Delete"), APPROVEENROLL("Approve Enroll"), REJECTCREATE("Reject Create"),
    REJECTEDIT("Reject Edit"), REJECTDELETE("Reject Delete"), REJECTENROLL("Reject Enroll"),SETTLED("Settled"),
    ACTIVATE("Activate"), DEACTIVATE("Deactivate"), MERCHANT_ACTIVATE("Merchant Activate"), 
    MERCHANT_DEACTIVATE("Merchant Deactivate"), MERCHANT_UPDATE("Merchant Update"), MERCHANT_CREATE("Merchant Create"),
    MERCHANT_FEE_DELETE("Merchant Fee Delete"), CREATE_CARD_LIMITS("Card Limits Created"), UPDATE_CARD_LIMITS("Card Limits Update"),
    DELETE_CARD_LIMITS("Card Limits Delete"),  DISABLE_THIRDPARTY_AUTH("Disable Third Party AUTH");

    private String eventNameAlias;

    private EventEnum(String eventNameAlias) {
        this.eventNameAlias = eventNameAlias;
    }

    public String getEventNameAlias() {
        return this.eventNameAlias;
    }

    public static List<String> getAllEventAliases() {
        List<String> aliases = new ArrayList<>();
        for (EventEnum name : EventEnum.values()) {
            aliases.add(name.getEventNameAlias());
        }
        return aliases;
    }

    public static List<String> getCustomerEventAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add(EventEnum.CREATE.getEventNameAlias());
        aliases.add(EventEnum.UPDATE.getEventNameAlias());
        aliases.add(EventEnum.DELETE.getEventNameAlias());
        aliases.add(EventEnum.COMMUNICATION.getEventNameAlias());
        return aliases;
    }

}
