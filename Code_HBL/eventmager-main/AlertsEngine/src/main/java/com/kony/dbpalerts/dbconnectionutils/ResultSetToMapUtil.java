package com.kony.dbpalerts.dbconnectionutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.gson.JsonObject;
import com.kony.dbpalerts.alertsutils.AccountHelper;
import com.kony.dbpalerts.alertsutils.AlertConstants;
import com.kony.dbpalerts.alertsutils.UserAlertDTO;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;

public class ResultSetToMapUtil {
	private ResultSetToMapUtil() {

	}
	private static String generateKey(Record record) {

		String key = null;

		if (record.getParamValueByName(AlertConstants.ALERTSUBTYPEID) != null
				&& record.getParamValueByName(AlertConstants.CUSTOMER_ID) != null
				&& record.getParamValueByName(AlertConstants.ACCOUNTID) != null
				&& record.getParamValueByName(AlertConstants.ACCOUNTTYPE) != null) {
			key = record.getParamValueByName(AlertConstants.ALERTSUBTYPEID)
					+ record.getParamValueByName(AlertConstants.CUSTOMER_ID)
					+ record.getParamValueByName(AlertConstants.ACCOUNTID)
					+ record.getParamValueByName(AlertConstants.ACCOUNTTYPE);

		}
		return key;

	}

	protected static Map<String, UserAlertDTO> createUserAlertGenericDatafromDataSet(Dataset ds) {
		Map<String, UserAlertDTO> resultsetmap = new HashMap<>();
		List<Record> records = ds.getAllRecords();
		if (records == null || records.isEmpty())
			return resultsetmap;
		for (Record record : records) {
			String key = generateKey(record);
			if (key == null)
				continue;
			UserAlertDTO userdto;
			if (resultsetmap.containsKey(key)) {
				userdto = resultsetmap.get(key);
			} else {
				userdto = new UserAlertDTO();
			}
			if (record.getParamValueByName(AlertConstants.ACCOUNTID) != null)
				userdto.setAccountId(record.getParamValueByName(AlertConstants.ACCOUNTID));
			if (record.getParamValueByName(AlertConstants.CHANNELID) != null)
				userdto.getChannels().add(record.getParamValueByName(AlertConstants.CHANNELID));
			if (record.getParamValueByName(AlertConstants.CUSTOMER_ID) != null)
				userdto.setCustomerid(record.getParamValueByName(AlertConstants.CUSTOMER_ID));
			if (record.getParamValueByName(AlertConstants.VALUE1) != null)
				userdto.setValue1(record.getParamValueByName(AlertConstants.VALUE1));
			if (record.getParamValueByName(AlertConstants.VALUE2) != null)
				userdto.setValue2(record.getParamValueByName(AlertConstants.VALUE2));
			resultsetmap.put(key, userdto);
		}
		return resultsetmap;
	}

	protected static Map<String, String> updateCustomerPhoneAndEmailFromDataSet(Dataset ds) {
		Map<String, String> commdata = new HashMap<>();
		List<Record> records = ds.getAllRecords();
		if (records == null || records.isEmpty())
			return commdata;
		for (Record record : records) {
			if (record.getParamValueByName(AlertConstants.CUSTOMER_ID) != null
					&& record.getParamValueByName(AlertConstants.VAL) != null) {
				if (commdata.containsKey(record.getParamValueByName(AlertConstants.CUSTOMER_ID))) {
					commdata.put(record.getParamValueByName(AlertConstants.CUSTOMER_ID),
							commdata.get(record.getParamValueByName(AlertConstants.CUSTOMER_ID)) + "###"
									+ record.getParamValueByName(AlertConstants.VAL));
				} else {
					commdata.put(record.getParamValueByName(AlertConstants.CUSTOMER_ID),
							record.getParamValueByName(AlertConstants.VAL));
				}
			}
		}
		return commdata;
	}

	protected static ConcurrentMap<String, String> genCustMapFromDataSet(Dataset ds) {

		ConcurrentMap<String, String> accountmap = new ConcurrentHashMap<>();
		List<Record> records = ds.getAllRecords();
		if (records == null || records.isEmpty())
			return accountmap;
		for (Record record : records) {
			if (record.getParamValueByName(AlertConstants.CUSTOMER_ID) != null
					&& record.getParamValueByName(AlertConstants.ACCOUNTID_UPPER) != null
					&& record.getParamValueByName(AlertConstants.ACCOUNTTYPE) != null
					&& record.getParamValueByName(AlertConstants.ALERTCATEGORYID) != null
					&& record.getParamValueByName(AlertConstants.STATUS_ID) != null) {
				accountmap.put(
						record.getParamValueByName(AlertConstants.CUSTOMER_ID)
								+ record.getParamValueByName(AlertConstants.ACCOUNTID_UPPER)
								+ record.getParamValueByName(AlertConstants.ALERTCATEGORYID)
								+ record.getParamValueByName(AlertConstants.ACCOUNTTYPE),
						record.getParamValueByName(AlertConstants.STATUS_ID));
			}

		}
		return accountmap;
	}

	protected static Map<String, Set<String>> genCoreIdMapFromDataSet(Dataset ds) {

		Map<String, Set<String>> coreidmap = new HashMap<>();
		List<Record> records = ds.getAllRecords();
		if (records == null || records.isEmpty())
			return coreidmap;
		for (Record record : records) {
			if (record.getParamValueByName(AlertConstants.BACKENDID) != null
					&& record.getParamValueByName(AlertConstants.CUSTOMER_ID) != null) {
				if (coreidmap.containsKey(record.getParamValueByName(AlertConstants.BACKENDID))) {
					Set<String> custlist = coreidmap.get(record.getParamValueByName(AlertConstants.BACKENDID));
					custlist.add(record.getParamValueByName(AlertConstants.CUSTOMER_ID));
					coreidmap.put(record.getParamValueByName(AlertConstants.BACKENDID), custlist);
				} else {
					Set<String> custlist = new HashSet<>();
					custlist.add(record.getParamValueByName(AlertConstants.CUSTOMER_ID));
					coreidmap.put(record.getParamValueByName(AlertConstants.BACKENDID), custlist);
				}
			}
		}
		return coreidmap;
	}

	protected static Map<String, String> fetchFieldsFromDataSet(Dataset ds) {
        Map<String, String> resultmap = new HashMap<>();

 

        List<Record> records = ds.getAllRecords();
        if (records == null || records.isEmpty())
            return resultmap;

 

        for (Record record : records) {
            if (record.getParamValueByName(AlertConstants.CUSTOMERID) != null) {
                resultmap.put(record.getParamValueByName(AlertConstants.CUSTOMERID).toLowerCase(),
                        record.getParamValueByName(AlertConstants.USERNAME) + "," + record.getParamValueByName(AlertConstants.LEGALENTITYID));
            }
            if (record.getParamValueByName(AlertConstants.USERNAME) != null) {
                resultmap.put(record.getParamValueByName(AlertConstants.USERNAME).toLowerCase(),
                        record.getParamValueByName(AlertConstants.CUSTOMERID) + "," + record.getParamValueByName(AlertConstants.LEGALENTITYID));
            }
        }
        return resultmap;
    }

	protected static Map<String, Map<String, String>> createHashMapOfGlobalDataSet(Dataset ds) {
		Map<String, Map<String, String>> resultsetmap = new HashMap<>();
		List<Record> records = ds.getAllRecords();
		if (records == null)
			return resultsetmap;
		for (Record r : records) {
			Map<String, String> eventsdata = new HashMap<>();
			if (r.getParamValueByName(AlertConstants.ALERTSUBTYPEID) != null) {
				String alertsubtype = r.getParamValueByName(AlertConstants.ALERTSUBTYPEID);
				if (resultsetmap.containsKey(alertsubtype)) {
					eventsdata = resultsetmap.get(alertsubtype);
					if (r.getParamValueByName(AlertConstants.CHANNELID) != null) {
						eventsdata.put(r.getParamValueByName(AlertConstants.CHANNELID), "");
					}
				} else {
					if (r.getParamValueByName(AlertConstants.CHANNELID) != null) {
						eventsdata.put(r.getParamValueByName(AlertConstants.CHANNELID), "");
					}
					if (r.getParamValueByName(AlertConstants.ALERTCATEGORYID) != null)
						eventsdata.put(AlertConstants.ALERTCATEGORYID,
								r.getParamValueByName(AlertConstants.ALERTCATEGORYID));
					if (r.getParamValueByName(AlertConstants.RECIPIENTTYPE) != null) {
						eventsdata.put(AlertConstants.RECIPIENTTYPE ,
								r.getParamValueByName(AlertConstants.RECIPIENTTYPE));
					}
					if (r.getParamValueByName(AlertConstants.ATTRIBUTEID) != null)
						eventsdata.put(AlertConstants.ATTRIBUTEID, r.getParamValueByName(AlertConstants.ATTRIBUTEID));
					if (r.getParamValueByName(AlertConstants.ALERTCONDITIONID) != null)
						eventsdata.put(AlertConstants.ALERTCONDITIONID,
								r.getParamValueByName(AlertConstants.ALERTCONDITIONID));
					if (r.getParamValueByName(AlertConstants.VALUE1) != null)
						eventsdata.put(AlertConstants.VALUE1, r.getParamValueByName(AlertConstants.VALUE1));
					if (r.getParamValueByName(AlertConstants.VALUE2) != null)
						eventsdata.put(AlertConstants.VALUE2, r.getParamValueByName(AlertConstants.VALUE2));
					if (r.getParamValueByName(AlertConstants.ALERTTYPE_STATUS_ID) != null)
						eventsdata.put(AlertConstants.ALERTTYPE_STATUS_ID,
								r.getParamValueByName(AlertConstants.ALERTTYPE_STATUS_ID));
					if (r.getParamValueByName(AlertConstants.ALERTSUBTYPETYPE_STATUS_ID) != null)
						eventsdata.put(AlertConstants.ALERTSUBTYPETYPE_STATUS_ID,
								r.getParamValueByName(AlertConstants.ALERTSUBTYPETYPE_STATUS_ID));
					if (r.getParamValueByName(AlertConstants.ISGLOBAL) != null)
						eventsdata.put(AlertConstants.ISGLOBAL, r.getParamValueByName(AlertConstants.ISGLOBAL));
					if (r.getParamValueByName(AlertConstants.ALERTCATEGORY_STATUS_ID) != null)
						eventsdata.put(AlertConstants.ALERTCATEGORY_STATUS_ID,
								r.getParamValueByName(AlertConstants.ALERTCATEGORY_STATUS_ID));
					if (r.getParamValueByName(AlertConstants.ACCOUNTLEVEL) != null)
						eventsdata.put(AlertConstants.ACCOUNTLEVEL, r.getParamValueByName(AlertConstants.ACCOUNTLEVEL));
					if (r.getParamValueByName(AlertConstants.EXTERNALSYSTEM) != null)
						eventsdata.put(AlertConstants.EXTERNALSYSTEM, r.getParamValueByName(AlertConstants.EXTERNALSYSTEM));
					if (r.getParamValueByName(AlertConstants.ALERTCATEGORYNAME) != null)
						eventsdata.put(AlertConstants.ALERTCATEGORYNAME,
								r.getParamValueByName(AlertConstants.ALERTCATEGORYNAME));
					if (r.getParamValueByName(AlertConstants.ALERTGROUPNAME) != null)
						eventsdata.put(AlertConstants.ALERTGROUPNAME,
								r.getParamValueByName(AlertConstants.ALERTGROUPNAME));
					if (r.getParamValueByName(AlertConstants.ALERTNAME) != null)
						eventsdata.put(AlertConstants.ALERTNAME, r.getParamValueByName(AlertConstants.ALERTNAME));

					resultsetmap.put(alertsubtype, eventsdata);
				}

			}
		}

		return resultsetmap;
	}

	protected static ConcurrentMap<String, Map<String, JsonObject>> createHashMapOfCommunicationDataDataSet(
			Dataset ds) {
		ConcurrentMap<String, Map<String, JsonObject>> communicationmap = new ConcurrentHashMap<>();

		List<Record> records = ds.getAllRecords();
		if (records == null || records.isEmpty())
			return communicationmap;
		for (Record record : records) {
			if (record.getParamValueByName(AlertConstants.LANGUAGECODE) != null
					&& record.getParamValueByName(AlertConstants.ALERTSUBTYPEID) != null
					&& record.getParamValueByName(AlertConstants.STATUS_ID) != null
					&& record.getParamValueByName(AlertConstants.CHANNELID_UPPER) != null) {

				Map<String, JsonObject> commdata = new HashMap<>();
				String key = record.getParamValueByName(AlertConstants.LANGUAGECODE) + "_##_"
						+ record.getParamValueByName(AlertConstants.ALERTSUBTYPEID) + "_##_"
						+ record.getParamValueByName(AlertConstants.STATUS_ID);
				JsonObject commJsonData = new JsonObject();
				if (record.getParamValueByName(AlertConstants.NAME) != null)
					commJsonData.addProperty(AlertConstants.NAME, record.getParamValueByName(AlertConstants.NAME));
				if (record.getParamValueByName(AlertConstants.TEXT) != null)
					commJsonData.addProperty(AlertConstants.TEXT, record.getParamValueByName(AlertConstants.TEXT));
				if (record.getParamValueByName(AlertConstants.SUBJECT) != null)
					commJsonData.addProperty(AlertConstants.SUBJECT,
							record.getParamValueByName(AlertConstants.SUBJECT));
				if (record.getParamValueByName(AlertConstants.SENDERNAME) != null)
					commJsonData.addProperty(AlertConstants.SENDERNAME,
							record.getParamValueByName(AlertConstants.SENDERNAME));
				if (record.getParamValueByName(AlertConstants.SENDERMAIL) != null)
					commJsonData.addProperty(AlertConstants.SENDERMAIL,
							record.getParamValueByName(AlertConstants.SENDERMAIL));
				if (communicationmap.containsKey(key)) {
					commdata = communicationmap.get(key);
				}
				commdata.put(record.getParamValueByName(AlertConstants.CHANNELID_UPPER), commJsonData);
				communicationmap.put(key, commdata);
			}
		}
		return communicationmap;
	}

	protected static Map<String, String> fetchFieldsFromDataSetAlertSubtypeCustomerType(Dataset ds) {
		Map<String, String> resultmap = new HashMap<>();
		List<Record> records = ds.getAllRecords();
		if (records == null || records.isEmpty())
			return resultmap;
		for (Record record : records) {
			if (record.getParamValueByName(AlertConstants.CUSTOMERTYPEID) != null
					&& record.getParamValueByName(AlertConstants.ALERTSUBTYPEID_CAMEL) != null) {
				String custtypeid = record.getParamValueByName(AlertConstants.CUSTOMERTYPEID);
				String alerttypeid = record.getParamValueByName(AlertConstants.ALERTSUBTYPEID_CAMEL);
				if (resultmap.containsKey(custtypeid)) {
					resultmap.put(custtypeid, resultmap.get(custtypeid) + "-" + alerttypeid);
				} else {
					resultmap.put(custtypeid, alerttypeid);
				}
			}
		}
		return resultmap;
	}

	protected static Map<String, String> fetchFieldsFromDataSetAppsMf(Dataset ds) {
		Map<String, String> resultmap = new HashMap<>();
		List<Record> records = ds.getAllRecords();
		if (records == null || records.isEmpty())
			return resultmap;

		for (Record record : records) {
			if (record.getParamValueByName(AlertConstants.APPID_1) != null
					&& record.getParamValueByName(AlertConstants.AID) != null) {
				String appid = record.getParamValueByName(AlertConstants.APPID_1);
				String aid = record.getParamValueByName(AlertConstants.AID);
				resultmap.put(aid, appid);
			}
		}
		return resultmap;
	}

	protected static Map<String, String> fetchFieldsFromDataSetApps(Dataset ds) {
		Map<String, String> resultmap = new HashMap<>();
		List<Record> records = ds.getAllRecords();
		if (records == null || records.isEmpty())
			return resultmap;
		for (Record record : records) {
			if (record.getParamValueByName(AlertConstants.APPID) != null
					&& record.getParamValueByName(AlertConstants.ALERTSUBTYPEID_CAMEL) != null) {
				String appid = record.getParamValueByName(AlertConstants.APPID);
				String alertsubtypeid = record.getParamValueByName(AlertConstants.ALERTSUBTYPEID_CAMEL);
				if (resultmap.containsKey(appid)) {
					resultmap.put(appid, resultmap.get(appid) + "-" + alertsubtypeid);
				} else {
					resultmap.put(appid, alertsubtypeid);
				}
			}
		}
		return resultmap;

	}

	protected static AccountHelper fetchFieldsFromDataSetAccounts(Dataset ds) {

		Map<String, List<String>> resultcustomermap = new HashMap<>();
		Map<String, String> resultmapaccounttype = new HashMap<>();

		List<Record> records = ds.getAllRecords();
		if (records == null || records.isEmpty())
			return new AccountHelper(resultmapaccounttype, resultcustomermap);

		for (Record record : records) {
			if (record.getParamValueByName(AlertConstants.ACCOUNT_ID) != null
					&& record.getParamValueByName(AlertConstants.USER_ID) != null
					&& !record.getParamValueByName(AlertConstants.ACCOUNT_ID).equals("")
					&& !record.getParamValueByName(AlertConstants.USER_ID).equals("")) {
				if (resultcustomermap.containsKey(record.getParamValueByName(AlertConstants.ACCOUNT_ID))) {
					List<String> customers = resultcustomermap
							.get(record.getParamValueByName(AlertConstants.ACCOUNT_ID));
					customers.add(record.getParamValueByName(AlertConstants.USER_ID));
					resultcustomermap.put(record.getParamValueByName(AlertConstants.ACCOUNT_ID), customers);
				} else {
					List<String> customers = new ArrayList<>();
					customers.add(record.getParamValueByName(AlertConstants.USER_ID));
					resultcustomermap.put(record.getParamValueByName(AlertConstants.ACCOUNT_ID), customers);
				}
			}

			if (record.getParamValueByName(AlertConstants.ACCOUNT_ID) != null
					&& record.getParamValueByName(AlertConstants.ACC_TYPE_ID) != null
					&& !record.getParamValueByName(AlertConstants.ACCOUNT_ID).equals("")
					&& !record.getParamValueByName(AlertConstants.ACC_TYPE_ID).equals("")) {
				resultmapaccounttype.put(record.getParamValueByName(AlertConstants.ACCOUNT_ID),
						record.getParamValueByName(AlertConstants.ACC_TYPE_ID));
			}
		}
		return new AccountHelper(resultmapaccounttype, resultcustomermap);

	}
}
