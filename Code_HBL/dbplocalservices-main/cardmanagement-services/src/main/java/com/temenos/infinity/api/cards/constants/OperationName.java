package com.temenos.infinity.api.cards.constants;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;

public final class OperationName {

	public static final String SCHEMA_NAME = EnvironmentConfigurationsHandler.getValue("DBX_SCHEMA_NAME");

     public static final String DB_CARDS_UPDATE =SCHEMA_NAME+"_card_update";
	 public static final String DB_CARDS_GET = SCHEMA_NAME+"_card_get";
	 public static final String DB_CARDS_CREATE =SCHEMA_NAME+"_card_create";
     public static final String CARDSTATEMENT_GET =SCHEMA_NAME+"_cardstatements_get";
	 public static final String DB_FETCH_CARDTRANSACTION_PROC = SCHEMA_NAME+"_fetch_cardtransaction_proc";
	 public static final String DB_CARDPRODDUCTSVIEW_GET = SCHEMA_NAME+"_cardproductsview_get";
	 public static final String CARDSTRANSACTION_UPDATE =SCHEMA_NAME+"_cardtransaction_update";
}
