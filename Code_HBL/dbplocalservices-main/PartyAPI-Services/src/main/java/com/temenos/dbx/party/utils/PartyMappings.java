package com.temenos.dbx.party.utils;

import java.util.HashMap;
import java.util.Map;

public class PartyMappings {

    private static Map<String, String> partyidentifierMap = null;

    public static Map<String, String> getPartyIdentifierMapping() {

        if (partyidentifierMap == null) {
            partyidentifierMap = new HashMap<String, String>();
            partyidentifierMap.put("ID_DRIVING_LICENSE", "Driver License");
            partyidentifierMap.put("ID_PASSPORT", "Passport");
        }

        return partyidentifierMap;
    }

    private static Map<String, String> customeridentifierMap = null;

    public static Map<String, String> getCustomerIdentifierMapping() {

        if (customeridentifierMap == null) {
            customeridentifierMap = new HashMap<String, String>();
            customeridentifierMap.put("Driver License", "ID_DRIVING_LICENSE");
            customeridentifierMap.put("Passport", "ID_PASSPORT");
        }

        return customeridentifierMap;
    }

    private static Map<String, String> partyPhoneAdressTypes = null;

    public static Map<String, String> getPartyPhoneAddressTypeMapping() {

        if (partyPhoneAdressTypes == null) {
            partyPhoneAdressTypes = new HashMap<String, String>();
            partyPhoneAdressTypes.put("Work", "Office");
            partyPhoneAdressTypes.put("Home", "Home");
            partyPhoneAdressTypes.put("Mobile", "Home");
            partyPhoneAdressTypes.put("other", "Residence");
            partyPhoneAdressTypes.put("Personal", "Residence");
        }

        return partyPhoneAdressTypes;
    }

    private static Map<String, String> customerPhoneTypeMapping = null;

    public static Map<String, String> getCustomerPhoneTypeMapping() {

        if (customerPhoneTypeMapping == null) {
            customerPhoneTypeMapping = new HashMap<String, String>();
            customerPhoneTypeMapping.put("Residence", "Mobile");
            customerPhoneTypeMapping.put("Office", "Work");
            customerPhoneTypeMapping.put("Home", "Home");
            customerPhoneTypeMapping.put("Other", "Other");
        }

        return customerPhoneTypeMapping;
    }
    
    private static Map<String, String> partycontactAddressTypeMapping = null;

    public static Map<String, String> getPartycontactAddressTypeMapping() {

        if (partycontactAddressTypeMapping == null) {
            partycontactAddressTypeMapping = new HashMap<String, String>();
            partycontactAddressTypeMapping.put("ADR_TYPE_CURRENT", "Residence");
            partycontactAddressTypeMapping.put("ADR_TYPE_HOME", "Home");
            partycontactAddressTypeMapping.put("ADR_TYPE_MAILING", "Residence");
            partycontactAddressTypeMapping.put("ADR_TYPE_WORK", "Office");
            partycontactAddressTypeMapping.put("Mobile", "Residence");
        }

        return partycontactAddressTypeMapping;
    }

    private static Map<String, String> customerAddressTypeMapping = null;

    public static Map<String, String> getcustomerAddressTypeMapping() {

        if (customerAddressTypeMapping == null) {
            customerAddressTypeMapping = new HashMap<String, String>();
            customerAddressTypeMapping.put("Home", "ADR_TYPE_HOME");
            customerAddressTypeMapping.put("Office", "ADR_TYPE_WORK");
            customerAddressTypeMapping.put("Residence", "ADR_TYPE_CURRENT");
        }

        return customerAddressTypeMapping;
    }
}
