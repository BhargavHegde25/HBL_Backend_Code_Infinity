
package com.kony.adminconsole.utilities;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.exceptions.MiddlewareException;

public enum ServiceURLEnum {

	OKTA_LOGINMFA("OKTA_LOGINMFA"),

	ACTIONDISPLAYNAMEDESCRIPTION_READ("actiondisplaynamedescription.read"), ACCOUNTS_READ("accounts.read"),

	ADDRESSTYPE_CREATE("addresstype.create"), ADDRESSTYPE_DELETE("addresstype.delete"),
	ADDRESSTYPE_READ("addresstype.read"), ADDRESSTYPE_UPDATE("addresstype.update"),

	ADDRESS_CREATE("address.create"), ADDRESS_DELETE("address.delete"), ADDRESS_READ("address.read"),
	ADDRESS_UPDATE("address.update"),

	ADMINCONSOLELOGS_READ("adminconsolelogs.read"),

	ALERTTYPE_CREATE("alerttype.create"), ALERTTYPE_DELETE("alerttype.delete"), ALERTTYPE_READ("alerttype.read"),
	ALERTTYPE_UPDATE("alerttype.update"),

	ALERT_CREATE("alert.create"), ALERT_DELETE("alert.delete"), ALERT_READ("alert.read"), ALERT_UPDATE("alert.update"),
	ALERTRECIPIENTTYPE_READ("alertrecipienttype.read"), ALERTFREQUENCY_VIEW_READ("alertfrequency_view.read"),
	DMS_CREATE_USER_PROC("DMS_create_user_proc.service"),

	ACTIONLIMIT_CREATE("actionlimit.create"), ACTIONLIMIT_DELETE("actionlimit.delete"),
	ACTIONLIMIT_READ("actionlimit.read"), ACTIONLIMIT_UPDATE("actionlimit.update"),
	ACTION_LIMITS_UPDATE_PROC("action_limits_update_proc.service"), APP_CREATE("app.create"), APP_DELETE("app.delete"),
	APP_READ("app.read"), APP_UPDATE("app.update"),

	APPACTION_CREATE("appaction.create"), APPACTION_DELETE("appaction.delete"), APPACTION_READ("appaction.read"),
	APPACTION_UPDATE("appaction.update"),

	APPACTIONMFA_CREATE("appactionmfa.create"), APPACTIONMFA_DELETE("appactionmfa.delete"),
	APPACTIONMFA_READ("appactionmfa.read"), APPACTIONMFA_UPDATE("appactionmfa.update"),

	APPLICATION_READ("application.read"), APPLICATION_UPDATE("application.update"),

	ARCHIVEDCUSTOMERREQUEST_CREATE("archivedcustomerrequest.create"),
	ARCHIVEDCUSTOMERREQUEST_DELETE("archivedcustomerrequest.delete"),
	ARCHIVEDCUSTOMERREQUEST_READ("archivedcustomerrequest.read"),
	ARCHIVEDCUSTOMERREQUEST_UPDATE("archivedcustomerrequest.update"),

	ARCHIVEDCUSTOMER_REQUEST_DETAILED_VIEW_READ("archivedcustomer_request_detailed_view.read"),

	ARCHIVEDMEDIA_CREATE("archivedmedia.create"), ARCHIVEDMEDIA_DELETE("archivedmedia.delete"),
	ARCHIVEDMEDIA_READ("archivedmedia.read"), ARCHIVEDMEDIA_UPDATE("archivedmedia.update"),

	ARCHIVEDMESSAGEATTACHMENT_CREATE("archivedmessageattachment.create"),
	ARCHIVEDMESSAGEATTACHMENT_DELETE("archivedmessageattachment.delete"),
	ARCHIVEDMESSAGEATTACHMENT_READ("archivedmessageattachment.read"),
	ARCHIVEDMESSAGEATTACHMENT_UPDATE("archivedmessageattachment.update"),

	ARCHIVEDREQUESTMESSAGE_CREATE("archivedrequestmessage.create"),
	ARCHIVEDREQUESTMESSAGE_DELETE("archivedrequestmessage.delete"),
	ARCHIVEDREQUESTMESSAGE_READ("archivedrequestmessage.read"),
	ARCHIVEDREQUESTMESSAGE_UPDATE("archivedrequestmessage.update"),

	ARCHIVED_REQUEST_SEARCH_PROC_SERVICE("archived_request_search_proc.service"),

	ATTACHMENTTYPE_CREATE("attachmenttype.create"), ATTACHMENTTYPE_DELETE("attachmenttype.delete"),
	ATTACHMENTTYPE_READ("attachmenttype.read"), ATTACHMENTTYPE_UPDATE("attachmenttype.update"),

	ATTRIBUTE_READ("attribute.read"), DATASETATTRIBUTES_PROC("datasetattributes.proc"),

	ATTRIBUTECRITERIA_READ("attributecriteria.read"),

	ATTRIBUTEOPTION_READ("attributeoption.read"),

	BACKENDIDENTIFIER_READ("backendidentifier.read"), BACKENDIDENTIFIER_CREATE("backendidentifier.create"),

	BANK_READ("bank.read"),

	BANKFORTRANSFER_CREATE("bankfortransfer.create"), BANKFORTRANSFER_DELETE("bankfortransfer.delete"),
	BANKFORTRANSFER_READ("bankfortransfer.read"), BANKFORTRANSFER_UPDATE("bankfortransfer.update"),
	BANKFORTRANSFER_VIEW_READ("bankfortransfer_view.read"),

	BANKSERVICE_CREATE("bankservice.create"), BANKSERVICE_DELETE("bankservice.delete"),
	BANKSERVICE_READ("bankservice.read"), BANKSERVICE_UPDATE("bankservice.update"),

	BRANCH_VIEW_READ("branch_view.read"), BUSINESSCONFIGURATION_CREATE("businessconfiguration.create"),
	BUSINESSCONFIGURATION_DELETE("businessconfiguration.delete"),
	BUSINESSCONFIGURATION_READ("businessconfiguration.read"),
	BUSINESSCONFIGURATION_UPDATE("businessconfiguration.update"),

	LOB_VIEW_READ("lob_view.read"),

	USERTYPE_READ("usertype.read"),

	BUSINESSSIGNATORY_CREATE("businesssignatory.create"), BUSINESSSIGNATORY_DELETE("businesssignatory.delete"),
	BUSINESSSIGNATORY_READ("businesssignatory.read"), BUSINESSSIGNATORY_UPDATE("businesssignatory.update"),

	BUSINESSTYPE_CREATE("businesstype.create"), BUSINESSTYPE_DELETE("businesstype.delete"),
	BUSINESSTYPE_READ("businesstype.read"), BUSINESSTYPE_UPDATE("businesstype.update"),
	BUSINESSTYPES_VIEW_READ("businesstypes_view.read"),
	BUSINESSTYPE_DEFAULTGROUP_UPDATE_PROC("businesstype_defaultgroup_update_proc.service"),
	CAMPAIGN_CREATE("campaign.create"), CAMPAIGN_DELETE("campaign.delete"), CAMPAIGN_READ("campaign.read"),
	CAMPAIGN_UPDATE("campaign.update"),

	CAMPAIGNPLACEHOLDER_READ("campaignplaceholder.read"), CAMPAIGNPLACEHOLDER_UPDATE("campaignplaceholder.update"),

	CAMPAIGNSPECIFICATION_DELETE("campaignspecification.delete"),
	CAMPAIGNSPECIFICATION_UPDATE("campaignspecification.update"),

	CAMPAIGN_C360_COUNT_PROC("campaign_c360_count_proc.read"),
	CAMPAIGN_C360_SPECIFICATION_MANAGE_PROC("campaign_c360_specification_manage_proc.update"),
	CAMPAIGN_C360_SPECIFICATION_DELETE_PROC("campaign_c360_specification_delete_proc.delete"),
	CAMPAIGN_C360_SPECIFICATION_GET_PROC("campaign_c360_specification_get_proc.read"),
	CAMPAIGN_C360_DEFAULT_SPECIFICATION_GET_PROC("campaign_c360_default_specification_get_proc.read"),
	CAMPAIGN_C360_DEFAULT_SPECIFICATION_MANAGE_PROC("campaign_c360_default_specification_manage_proc.update"),
	CAMPAIGN_C360_PRIORITY_UPDATE_PROC("campaign_c360_priority_update_proc.update"),
	CAMPAIGN_C360_GROUP_GET_PROC("campaign_c360_group_get.read"),
	CAMPAIGN_C360_MODEL_GET_PROC("campaign_c360_model_get.read"),
	CAMPAIGN_C360_CUSTOMERGROUP_DELETE_PROC("campaign_c360_customergroup_delete_proc.delete"),

	CAMPAIGNGROUP_CREATE("campaigngroup.create"), CAMPAIGNGROUP_DELETE("campaigngroup.delete"),
	CAMPAIGNGROUP_UPDATE("campaigngroup.update"), CAMPAIGNGROUP_READ("campaigngroup.read"),

	CARDACCOUNTREQUESTTYPE_CREATE("cardaccountrequesttype.create"),
	CARDACCOUNTREQUESTTYPE_DELETE("cardaccountrequesttype.delete"),
	CARDACCOUNTREQUESTTYPE_READ("cardaccountrequesttype.read"),
	CARDACCOUNTREQUESTTYPE_UPDATE("cardaccountrequesttype.update"),

	CARDACCOUNTREQUEST_CREATE("cardaccountrequest.create"), CARDACCOUNTREQUEST_DELETE("cardaccountrequest.delete"),
	CARDACCOUNTREQUEST_READ("cardaccountrequest.read"), CARDACCOUNTREQUEST_UPDATE("cardaccountrequest.update"),
	CARDACCOUNTREQUEST_VIEW_READ("cardaccountrequest_view.read"),

	CARD_REQUEST_NOTIFICATION_COUNT_VIEW_READ("card_request_notification_count_view.read"),

	CATEGORY_CREATE("category.create"), CATEGORY_DELETE("category.delete"), CATEGORY_READ("category.read"),
	CATEGORY_UPDATE("category.update"),

	CITY_CREATE("city.create"), CITY_DELETE("city.delete"), CITY_READ("city.read"), CITY_UPDATE("city.update"),

	COMMUNICATIONTYPE_CREATE("communicationtype.create"), COMMUNICATIONTYPE_DELETE("communicationtype.delete"),
	COMMUNICATIONTYPE_READ("communicationtype.read"), COMMUNICATIONTYPE_UPDATE("communicationtype.update"),

	COMPOSITEPERMISSION_CREATE("compositepermission.create"), COMPOSITEPERMISSION_DELETE("compositepermission.delete"),
	COMPOSITEPERMISSION_READ("compositepermission.read"), COMPOSITEPERMISSION_UPDATE("compositepermission.update"),
	COMPOSITEPERMISSION_VIEW_READ("composite_permissions_view.read"), COMPOSITEACTION_CREATE("compositeaction.create"),
	COMPOSITEACTION_DELETE("compositeaction.delete"), COMPOSITEACTION_READ("compositeaction.read"),
	COMPOSITEACTION_UPDATE("compositeaction.update"), COMPOSITEACTION_VIEW_READ("composite_actions_view.read"),

	CONFIGURATION_VIEW_READ("configuration_view.read"),
	CONFIGURATIONS_GETCONFIGURATIONS("Configurations.getConfigurations"), CONFIGURATIONS_READ("configurations.read"),

	CONFIGURATIONSBUNDLE_CREATE("configurationbundles.create"), CONFIGURATIONSBUNDLE_READ("configurationbundles.read"),
	CONFIGURATIONSBUNDLE_UPDATE("configurationbundles.update"),
	CONFIGURATIONSBUNDLE_DELETE("configurationbundles.delete"), CONFIGURATION_CREATE("configurations.create"),
	CONFIGURATION_READ("configurations.read"), CONFIGURATION_UPDATE("configurations.update"),
	CONFIGURATION_DELETE("configurations.delete"),

	CONTENTTYPE_CREATE("contenttype.create"), CONTENTTYPE_DELETE("contenttype.delete"),
	CONTENTTYPE_READ("contenttype.read"), CONTENTTYPE_UPDATE("contenttype.update"),

	CURRENCY_CREATE("currency.create"), CURRENCY_DELETE("currency.delete"), CURRENCY_READ("currency.read"),
	CURRENCY_UPDATE("currency.update"),

	CUSTOMER_ACTION_GROUP_ACTION_LIMIT_VIEW("customer_action_group_action_limits_view.read"),
	CUSTOMER_ACTION_GROUP_ACTION_LIMIT_PROC("customer_action_group_action_limits_proc.service"),
	CUSTOMER_ENTITLEMENTS_PROC("customer_entitlements_proc.service"),
	CUSTOMER_ACTIONS_PROC("customer_actions_proc.service"),
	USER_SECURITYATTRIBUTES_GET_PROC("user_securityattributes_get_proc.service"),
	CUSTOMER_ALERT_PREFERENCES_RESET_PROC("customer_alert_preferences_reset_proc.service"),
	CUSTOMERBUSINESSTYPE_CREATE("customerbusinesstype.create"),
	CUSTOMERBUSINESSTYPE_DELETE("customerbusinesstype.delete"), CUSTOMERBUSINESSTYPE_READ("customerbusinesstype.read"),
	CUSTOMERBUSINESSTYPE_UPDATE("customerbusinesstype.update"),

	CUSTOMERSEGMENT_CREATE("customersegment.create"), CUSTOMERSEGMENT_DELETE("customersegment.delete"),
	CUSTOMERSEGMENT_READ("customersegment.read"), CUSTOMERSEGMENT_UPDATE("customersegment.update"),
	CUSTOMERVIEWALERTCONFIGURATION_CREATE("customerviewalertconfiguration.create"),
	CUSTOMERVIEWALERTCONFIGURATION_DELETE("customerviewalertconfiguration.delete"),
	CUSTOMERVIEWALERTCONFIGURATION_READ("customerviewalertconfiguration.read"),
	CUSTOMERVIEWALERTCONFIGURATION_UPDATE("customerviewalertconfiguration.update"),
	KONY_DBP_IDENTITYSERVICE("DBPServiceIdentityService"),
	KONY_DBP_USER_IDENTITYSERVICE("DBPServiceUserIdentityService"),
	CONSUMERLENDINGAUTHSERVICE_LOGIN("ConsumerLendingAuthService.login"),
	DATASOURCETYPE_CREATE("datasourcetype.create"), DATASOURCETYPE_DELETE("datasourcetype.delete"),
	DATASOURCETYPE_READ("datasourcetype.read"), DATASOURCETYPE_UPDATE("datasourcetype.update"),
	DATASOURCE_CREATE("datasource.create"), DATASOURCE_DELETE("datasource.delete"), DATASOURCE_READ("datasource.read"),
	DATASOURCE_UPDATE("datasource.update"),

	DBPSERVICE_UPDATEDBPUSERSTATUS("DBPService.updateDBPUserStatus"),
	DBPSERVICE_GETDBPUSERSTATUS("DBPService.getDBPUserStatus"), DBPSERVICE_ENROLLDBXUSER("DBPService.enrollDBXUser"),
	DBPSERVICE_RESETDBPUSERPASSWORD("DBPService.resetDBPUserPassword"),
	DBPSERVICE_GETACCOUNTSFORADMIN("DBPService.getAccountsForAdmin"),
	DBPSERVICE_GETACCOUNTSPECIFICALERTS("DBPService.getAccountSpecificAlerts"),
	DBPSERVICE_GETCUSTOMERCARDS("DBPService.getCustomerCards"),
	DBPSERVICE_GETUSERDETAILSFROMACCOUNT("DBPService.getUserDetailsFromAccount"),
	DBPSERVICE_GETLOCKSTATUS("DBPService.getLockStatus"),
	DBPSERVICE_GETALLTRANSACTIONS("DBPService.getAllTransactions"),
	DBPSERVICE_UPDATECUSTOMERCARD("DBPService.updateCustomerCard"),
	DBPSERVICE_UPDATEUSERACCOUNTSETTINGSFORADMIN("DBPService.updateUserAccountSettingsForAdmin"),
	DBPSERVICE_UPDATELOCKSTATUS("DBPService.updateLockStatus"),
	DBPSERVICE_SEND_UNLOCK_LINK_TO_CUSTOMER("DBPService.sendUnlockLinkToCustomer"),
	DBPSERVICE_UNLOCKDBXUSER("DBPService.unlockDBXUser"), DBPSERVICE_CREATEDBXCUSTOMER("DBPService.createDBXCustomer"),
	DBPSERVICE_CREATEORGANIZATION("DBPService.createOrganization"),
	DBPSERVICE_EDITORGANIZATION("DBPService.editOrganization"), DBPSERVICE_CREATECUSTOMER("DBPService.createCustomer"),
	DBPSERVICE_CREATESIGNATORY("DBPService.createSignatory"),
	DBPSERVICE_CREATEMICROCUSTOMER("DBPService.createMicroCustomer"),
	DBPSERVICE_CUSTOMERSEARCH("DBPService.customerSearch"),DBPSERVICE_ENROLLEDCUSTOMERSEARCH("DBPService.enrolledCustomerSearch"), DBPSERVICE_EDITCUSTOMER("DBPService.editCustomer"),
	DBPSERVICE_GETCOMPANYSEARCH("DBPService.getCompanySearch"), DBPSERVICE_EDITSIGNATORY("DBPService.editSignatory"),
	DBPSERVICE_GETCOMPANYACCOUNTS("DBPService.getCompanyAccounts"),
	DBPSERVICE_GETCOMPANYACTIONLIMITS("DBPService.getCompanyActionLimits"),
	DBPSERVICE_GETCOMPANYAPPROVALMATRIX("DBPService.getCompanyApprovalMatrix"),
	DBPSERVICE_SUSPENDORGANIZATIONFEATURES("DBPService.suspendCompanyFeatures"),
	DBPSERVICE_VERIFYUSERNAME("DBPService.verifyUsername"),
	DBPSERVICE_GETCUSTOMERBASICINFORMATION("DBPService.getCustomerBasicInformation"),
	DBPSERVICE_GETCOMPANYCUSTOMERS("DBPService.getCompanyCustomers"), DBPSERVICE_VALIDATETIN("DBPService.validateTIN"),
	DBPSERVICE_GETALLACCOUNTS("DBPService.getAllAccounts"), DBPSERVICE_UNLINKACCOUNTS("DBPService.unlinkAccounts"),
	DBPSERVICE_VERIFYOFACANDCIP("DBPService.verifyOFACandCIP"),
	DBPSERVICE_GETCUSTOMERACCOUNTS("DBPService.getCustomerAccounts"), DBPSERVICE_UPGRADEUSER("DBPService.upgradeUser"),
	DBPSERVICE_CREATEDECISIONRULE("DBPService.createDecisionRule"),
	DBPSERVICE_GETDECISIONRULE("DBPService.getDecisionRule"),
	DBPSERVICE_EDITDECISIONRULE("DBPService.editDecisionRule"),
	DBPSERVICE_GETALLRULEFILESFORDECISION("DBPService.getAllFilesofDecisionRule"),
	DBPSERVICE_UPLOADRULEFILE("DBPService.uploadRuleFile"), DBPSERVICE_DOWNLOADRULEFILE("DBPService.downloadRuleFile"),
	DBPSERVICE_GETMONETARYACTIONS("DBPService.getMonetaryActions"),
	DBPSERVIEC_UPDATECUSTOMERINFO("DBPService.updateCustomerInfo"),
	DBPSERVICE_GETMEMBERSHIPDETAILS("DBPService.getMembershipDetails"),
	DBPSERVICE_GETLISTOFCOMPANYBYSTATUS("DBPService.getListOfCompanyByStatus"),
	DBPSERVICE_UPDATECOMPANYSTATUS("DBPService.updateCompanyStatus"),
	DBPSERVICE_FETCHAUTHORIZEDSIGNATORIES("DBPService.fetchAuthorizedSignatories"),
	DBPSERVICE_FETCHACCOUNTSIGNATORIES("DBPService.fetchAccountSignatories"),
	DBPSERVICE_GETCOMPANYSIGNATORIES("DBPService.getCompanySignatories"),
	DBPSERVICE_UPDATECUSTOMERDETAILS("DBPService.updateCustomerDetails"),
	DBPSERVICE_GETCUSTOMERCOMMUNICATION("DBPService.getCustomerCommunication"),
	DBPSERVICE_LINKPROFILESERVICE("DBPService.linkProfileService"),
	DBPSERVICE_DELINKPROFILESERVICE("DBPService.deLinkProfileService"),
	DBPSERVICE_EDITCONTRACT("DBPService.editContract"), DBPSERVICE_CREATECONTRACT("DBPService.createContract"),
	DBPSERVICE_GETCONTRACTDETAILS("DBPService.getContractDetails"),
	DBPSERVICE_SEARCHCONTRACT("DBPService.searchContract"),
	DBPSERVICE_GETCORERELATIVECUSTOMERS("DBPService.getCoreRelativeCustomers"),
	DBPSERVICE_GETCORECUSTOMERACCOUNTS("DBPService.getCoreCustomerAccounts"),
	DBPSERVICE_GETCONTRACTFEATUREACTIONLIMITS("DBPService.getContractFeatureActionLimits"),
	DBPSERVICE_SEARCHCORECUSTOMERS("DBPService.searchCoreCustomers"),
	DBPSERVICE_EDITINFINITYUSER("DBPService.editInfinityUser"),
	DBPSERVICE_GETINFINITYUSER("DBPService.getInfinityUser"),
	DBPSERVICE_GETALLELIGIBLERELATIONALCUSTOMERS("DBPService.getAllEligibleRelationalCustomers"),
	DBPSERVICE_CREATEINFINITYUSER("DBPService.createInfinityUser"),
	DBPSERVICE_GETASSOCIATEDCUSTOMERS("DBPService.getAssociatedCustomers"),
	DBPSERVICE_UPDATESERVICEDEFINITION_LIMITSANDPERMISSIONS("DBPService.updateServiceDefinitionLimitsAndPermissions"),
	DBPSERVICE_UPDATECUSTOMERROLE_LIMITSANDPERMISSIONS("DBPService.updateCustomerRoleLimitsAndPermissions"),
	DBPSERVICE_CUSTOMERSEARCHBYUSERNAME("DBPService.customerSearchByUserName"),
	DBPSERVICE_USERIDSEARCHOPERATIONDETAILEDDATA("DBPService.UserIdSearchOperationDetailedData"),
	DBPSERVICE_CUSTOMERLEGALENTITIESGET("DBPService.CustomerLegalEntitiesGetOperation"),
	ROLESANDPERMISSIONMANAGEMENT_CREATEROLE("RolesAndPermissionManagement.createRole"),

	COUNTRY_CREATE("country.create"), COUNTRY_DELETE("country.delete"), COUNTRY_READ("country.read"),
	COUNTRY_UPDATE("country.update"),

	CUSTOMERACTIVITYLOGS_READ("customeractivitylogs.read"),

	CUSTOMERADDRESS_CREATE("customeraddress.create"), CUSTOMERADDRESS_DELETE("customeraddress.delete"),
	CUSTOMERADDRESS_READ("customeraddress.read"), CUSTOMERADDRESS_UPDATE("customeraddress.update"),
	CUSTOMERADDRESS_VIEW_READ("customeraddress_view.read"),

	CUSTOMERALERTENTITLEMENT_CREATE("customeralertentitlement.create"),
	CUSTOMERALERTENTITLEMENT_DELETE("customeralertentitlement.delete"),
	CUSTOMERALERTENTITLEMENT_READ("customeralertentitlement.read"),
	CUSTOMERALERTENTITLEMENT_UPDATE("customeralertentitlement.update"),

	DBXCUSTOMERALERTENTITLEMENT_CREATEPROC("dbxcustomeralertentitlement.createProc"),
	DBXCUSTOMERALERTENTITLEMENT_UPDATEPROC("dbxcustomeralertentitlement.updateProc"),
	DBXCUSTOMERALERTENTITLEMENT_DELETEPROC("dbxcustomeralertentitlement.deleteProc"),

	CUSTOMERALERTCHANNEL_CREATEPROC("customeralertchannel.createProc"),
	CUSTOMERALERTCHANNEL_UPDATEPROC("customeralertchannel.updateProc"),
	CUSTOMERALERTCHANNEL_DELETEPROC("customeralertchannel.deleteProc"),

	CUSTOMERALERTFREQUENCY_CREATEPROC("customeralertfrequency.createProc"),
	CUSTOMERALERTFREQUENCY_UPDATEPROC("customeralertfrequency.updateProc"),
	CUSTOMERALERTFREQUENCY_DELETEPROC("customeralertfrequency.deleteProc"),

	CUSTOMERAPPLICATION_READ("customerapplication.read"),

	CUSTOMERBASICINFO_VIEW_READ("customerbasicinfo_view.read"),

	CUSTOMERCLASSIFICATION_CREATE("customerclassification.create"),
	CUSTOMERCLASSIFICATION_DELETE("customerclassification.delete"),
	CUSTOMERCLASSIFICATION_READ("customerclassification.read"),
	CUSTOMERCLASSIFICATION_UPDATE("customerclassification.update"),

	CUSTOMERCOMMUNICATION_CREATE("customercommunication.create"),
	CUSTOMERCOMMUNICATION_DELETE("customercommunication.delete"),
	CUSTOMERCOMMUNICATION_READ("customercommunication.read"),
	CUSTOMERCOMMUNICATION_UPDATE("customercommunication.update"),

	CUSTOMERDEVICE_CREATE("customerdevice.create"), CUSTOMERDEVICE_DELETE("customerdevice.delete"),
	CUSTOMERDEVICE_READ("customerdevice.read"), CUSTOMERDEVICE_UPDATE("customerdevice.update"),

	CUSTOMERENTITLEMENT_CREATE("customerentitlement.create"), CUSTOMERENTITLEMENT_DELETE("customerentitlement.delete"),
	CUSTOMERENTITLEMENT_READ("customerentitlement.read"), CUSTOMERENTITLEMENT_UPDATE("customerentitlement.update"),

	CUSTOMERFILE_CREATE("customerfile.create"), CUSTOMERFILE_READ("customerfile.read"),

	CUSTOMERFLAGSTATUS_CREATE("customerflagstatus.create"), CUSTOMERFLAGSTATUS_DELETE("customerflagstatus.delete"),
	CUSTOMERFLAGSTATUS_READ("customerflagstatus.read"), CUSTOMERFLAGSTATUS_UPDATE("customerflagstatus.update"),

	CUSTOMERGROUPINFO_VIEW_READ("customergroupinfo_view.read"),

	CUSTOMERGROUP_CREATE("customergroup.create"), CUSTOMERGROUP_DELETE("customergroup.delete"),
	CUSTOMERGROUP_READ("customergroup.read"), CUSTOMERGROUP_UPDATE("customergroup.update"),

	ENTITYSTATUS_CREATE("entitystatus.create"), ENTITYSTATUS_DELETE("entitystatus.delete"),
	ENTITYSTATUS_READ("entitystatus.read"), ENTITYSTATUS_UPDATE("entitystatus.update"),

	CUSTOMERNOTES_VIEW_READ("customernotes_view.read"),

	CUSTOMERNOTE_CREATE("customernote.create"), CUSTOMERNOTE_DELETE("customernote.delete"),
	CUSTOMERNOTE_READ("customernote.read"), CUSTOMERNOTE_UPDATE("customernote.update"),

	CUSTOMERNOTIFICATIONS_VIEW_READ("customernotifications_view.read"),

	CUSTOMERPERMISSIONS_VIEW_READ("customerpermissions_view.read"),

	CUSTOMERREQUESTS_VIEW_READ("customerrequests_view.read"),

	CUSTOMERREQUEST_CREATE("customerrequest.create"), CUSTOMERREQUEST_DELETE("customerrequest.delete"),
	CUSTOMERREQUEST_READ("customerrequest.read"), CUSTOMERREQUEST_UPDATE("customerrequest.update"),

	CUSTOMERSECURITYIMAGES_CREATE("customersecurityimages.create"),
	CUSTOMERSECURITYIMAGES_DELETE("customersecurityimages.delete"),
	CUSTOMERSECURITYIMAGES_READ("customersecurityimages.read"),
	CUSTOMERSECURITYIMAGES_UPDATE("customersecurityimages.update"),

	CUSTOMERSECURITYQUESTIONS_CREATE("customersecurityquestions.create"),
	CUSTOMERSECURITYQUESTIONS_DELETE("customersecurityquestions.delete"),
	CUSTOMERSECURITYQUESTIONS_READ("customersecurityquestions.read"),
	CUSTOMERSECURITYQUESTIONS_UPDATE("customersecurityquestions.update"),

	CUSTOMERSECURITYQUESTION_VIEW_READ("customersecurityquestion_view.read"),

	CUSTOMERSERVICE_COMMUNICATION_VIEW_READ("customerservice_communication_view.read"),
	CUSTOMERSERVICE_CREATE("customerservice.create"), CUSTOMERSERVICE_DELETE("customerservice.delete"),
	CUSTOMERSERVICE_READ("customerservice.read"), CUSTOMERSERVICE_UPDATE("customerservice.update"),

	CUSTOMER_COMMUNICATION_VIEW_READ("customer_communication_view.read"), CUSTOMER_CREATE("customer.create"),
	CUSTOMER_DELETE("customer.delete"), CUSTOMER_DEVICE_INFORMATION_VIEW_READ("customer_device_information_view.read"),
	CUSTOMER_INDIRECT_PERMISSIONS_VIEW_READ("customer_indirect_permissions_view.read"), CUSTOMER_READ("customer.read"),
	CUSTOMER_REQUEST_ARCHIVE_PROC_SERVICE("customer_request_archive_proc.service"),
	CUSTOMER_REQUEST_CATEGORY_COUNT_VIEW_READ("customer_request_category_count_view.read"),
	CUSTOMER_REQUEST_CSR_COUNT_VIEW_READ("customer_request_csr_count_view.read"),
	CUSTOMER_REQUEST_DETAILED_VIEW_READ("customer_request_detailed_view.read"),
	CUSTOMER_REQUEST_SEARCH_PROC_SERVICE("customer_request_search_proc.service"),
	CUSTOMER_REQUEST_STATUS_COUNT_VIEW_READ("customer_request_status_count_view.read"),
	CUSTOMER_SEARCH_PROC_SERVICE("customer_search_proc.service"),
	CUSTOMER_UNREAD_MESSAGE_COUNT_PROC_SERVICE("customer_unread_message_count_proc.service"),
	CUSTOMER_UPDATE("customer.update"), GET_FILTERED_COMPANIES_PROC("get_filtered_companies_proc.service"),
	GET_FILTERED_LOCATIONS_PROC("get_filtered_locations_proc.service"),

	CSRASSISTGRANT_CREATE("csrassistgrant.create"), CSRASSISTGRANT_DELETE("csrassistgrant.delete"),
	CSRASSISTGRANT_READ("csrassistgrant.read"), CSRASSISTGRANT_UPDATE("csrassistgrant.update"),

	CUSTOMERTYPE_READ("customerttype.read"),

	DASHBOARDALERTS_READ("dashboardalerts.read"),

	DATATYPE_CREATE("datatype.create"), DATATYPE_DELETE("datatype.delete"), DATATYPE_READ("datatype.read"),
	DATATYPE_UPDATE("datatype.update"),

	DAYSCHEDULE_CREATE("dayschedule.create"), DAYSCHEDULE_DELETE("dayschedule.delete"),
	DAYSCHEDULE_READ("dayschedule.read"), DAYSCHEDULE_UPDATE("dayschedule.update"),

	DEFAULTCAMPAIGN_READ("defaultcampaign.read"), DEFAULTCAMPAIGN_UPDATE("defaultcampaign.update"),

	DEFAULTCAMPAIGNSPECIFICATION_READ("defaultcampaignspecification.read"),
	DEFAULTCAMPAIGNSPECIFICATION_UPDATE("defaultcampaignspecification.update"),
	DEFAULTCAMPAIGNSPECIFICATION_DELETE("defaultcampaignspecification.delete"),

	FACILITY_CREATE("facility.create"), FACILITY_DELETE("facility.delete"), FACILITY_READ("facility.read"),
	FACILITY_UPDATE("facility.update"),

	FAQCATEGORY_READ("faqcategory.read"), FAQCATEGORY_VIEW_READ("faqcategory_view.read"),

	FAQS_CREATE("faqs.create"), FAQS_DELETE("faqs.delete"), FAQS_READ("faqs.read"), FAQS_UPDATE("faqs.update"),

	FREQUENCYTYPE_CREATE("frequencytype.create"), FREQUENCYTYPE_DELETE("frequencytype.delete"),
	FREQUENCYTYPE_READ("frequencytype.read"), FREQUENCYTYPE_UPDATE("frequencytype.update"),

	FEATURE_CREATE("feature.create"), FEATURE_DELETE("feature.delete"), FEATURE_READ("feature.read"),
	FEATURE_UPDATE("feature.update"),

	FEATUREROLETYPE_CREATE("featureroletype.create"), FEATUREROLETYPE_DELETE("featureroletype.delete"),
	FEATUREROLETYPE_READ("featureroletype.read"), FEATUREROLETYPE_UPDATE("featureroletype.update"),

	FEATUREDISPLAYNAMEDESCRIPTION_READ("featuredisplaynamedescription.read"),
	FEATUREDISPLAYNAMEDESCRIPTION_CREATE("featuredisplaynamedescription.create"),
	FEATUREDISPLAYNAMEDESCRIPTION_DELETE("featuredisplaynamedescription.delete"),
	FEATUREDISPLAYNAMEDESCRIPTION_UPDATE("featuredisplaynamedescription.update"),

	GROUPATTRIBUTE_CREATE("groupattribute.create"), GROUPATTRIBUTE_DELETE("groupattribute.delete"),
	GROUPATTRIBUTE_READ("groupattribute.read"), GROUPATTRIBUTE_UPDATE("groupattribute.update"),
	GROUPBUSINESSTYPE_CREATE("groupbusinesstype.create"), GROUPBUSINESSTYPE_DELETE("groupbusinesstype.delete"),
	GROUPBUSINESSTYPE_READ("groupbusinesstype.read"), GROUPBUSINESSTYPE_UPDATE("groupbusinesstype.update"),
	GROUPBUSINESSTYPECUSTOMERCOUNT_VIEW_READ("groupbusinesstypecustomercount_view.read"),
	GROUPENTITLEMENT_CREATE("groupentitlement.create"), GROUPENTITLEMENT_DELETE("groupentitlement.delete"),
	GROUPENTITLEMENT_READ("groupentitlement.read"), GROUPENTITLEMENT_UPDATE("groupentitlement.update"),
	GROUPENTITLEMENT_VIEW_READ("groupentitlement_view.read"),

	GROUPSERVICES_VIEW_READ("groupservices_view.read"),

	GROUPS_VIEW_READ("groups_view.read"),

	INTERNALUSERDETAILS_VIEW_READ("internaluserdetails_view.read"),

	INTERNALUSERS_VIEW_READ("internalusers_view.read"),

	INTERNALUSERSKC_VIEW_READ("internaluserskc_view.read"),

	ISSUERIMAGE_READ("issuerimage.read"),

	EMAILKMSJAVASERVICE_SENDEMAIL("EmailKMSJavaService.sendEmail"),
	AUTHKMSSERVICE_AUTHENTICATE("AuthKMSService.authenticate"),

	EMAILSERVICE_ENROLLUSER("EMailService.enrollUser"), EMAILSERVICE_SENDEMAIL("EMailService.sendEmail"),

	LOCALE_READ("locale.read"),

	LOCATIONCURRENCY_CREATE("locationcurrency.create"), LOCATIONCURRENCY_DELETE("locationcurrency.delete"),
	LOCATIONCURRENCY_READ("locationcurrency.read"), LOCATIONCURRENCY_UPDATE("locationcurrency.update"),

	LOCATIONCUSTOMERSEGMENT_CREATE("locationcustomersegment.create"),
	LOCATIONCUSTOMERSEGMENT_DELETE("locationcustomersegment.delete"),
	LOCATIONCUSTOMERSEGMENT_READ("locationcustomersegment.read"),
	LOCATIONCUSTOMERSEGMENT_UPDATE("locationcustomersegment.update"),

	LOCATIONDETAILS_VIEW_READ("locationdetails_view.read"),

	LOCATIONFACILITY_CREATE("locationfacility.create"), LOCATIONFACILITY_DELETE("locationfacility.delete"),
	LOCATIONFACILITY_READ("locationfacility.read"), LOCATIONFACILITY_UPDATE("locationfacility.update"),
	LOCATIONFACILITY_VIEW_READ("locationfacility_view.read"),

	LOCATIONFILE_CREATE("locationfile.create"), LOCATIONFILE_UPDATE("locationfile.update"),
	LOCATIONFILE_READ("locationfile.read"),

	LOCATIONSERVICES_VIEW_READ("locationservices_view.read"),

	LOCATIONSERVICE_CREATE("locationservice.create"), LOCATIONSERVICE_DELETE("locationservice.delete"),
	LOCATIONSERVICE_READ("locationservice.read"), LOCATIONSERVICE_UPDATE("locationservice.update"),

	LOCATIONTYPE_CREATE("locationtype.create"), LOCATIONTYPE_DELETE("locationtype.delete"),
	LOCATIONTYPE_READ("locationtype.read"), LOCATIONTYPE_UPDATE("locationtype.update"),

	LOCATION_CREATE("location.create"), LOCATION_DELETE("location.delete"),
	LOCATION_DETAILS_PROC_SERVICE("location_details_proc.service"),
	LOCATION_RANGE_PROC_SERVICE("location_range_proc.service"), LOCATION_READ("location.read"),
	LOCATION_SEARCH_PROC_SERVICE("location_search_proc.service"), LOCATION_UPDATE("location.update"),
	LOCATION_ADDRESS_SUGGESTIONS_PROC_SERVICE("location_address_suggestions_proc.service"),
	LOCATION_VIEW_READ("location_view.read"),

	LOGMANAGEMENT_AUDITLOG("LogManagement.AuditLog"),

	LOGVIEW_CREATE("logview.create"), LOGVIEW_DELETE("logview.delete"), LOGVIEW_READ("logview.read"),
	LOGVIEW_UPDATE("logview.update"),

	MEDIA_CREATE("media.create"), MEDIA_DELETE("media.delete"), MEDIA_READ("media.read"), MEDIA_UPDATE("media.update"),

	MEMBERGROUP_CREATE("membergroup.create"), MEMBERGROUP_DELETE("membergroup.delete"),
	MEMBERGROUP_READ("membergroup.read"), MEMBERGROUP_UPDATE("membergroup.update"),

	MEMBERGROUPTYPE_CREATE("membergrouptype.create"), MEMBERGROUPTYPE_DELETE("membergrouptype.delete"),
	MEMBERGROUPTYPE_READ("membergrouptype.read"), MEMBERGROUPTYPE_UPDATE("membergrouptype.update"),

	SERVICEDEFINITION_READ("servicedefinition.read"),

	MESSAGEATTACHMENT_CREATE("messageattachment.create"), MESSAGEATTACHMENT_DELETE("messageattachment.delete"),
	MESSAGEATTACHMENT_READ("messageattachment.read"), MESSAGEATTACHMENT_UPDATE("messageattachment.update"),

	MESSAGEREPORTS_READ("messagereports.read"),

	MESSAGETEMPLATE_CREATE("messagetemplate.create"), MESSAGETEMPLATE_DELETE("messagetemplate.delete"),
	MESSAGETEMPLATE_READ("messagetemplate.read"), MESSAGETEMPLATE_UPDATE("messagetemplate.update"),

	MFA_CREATE("mfa.create"), MFA_DELETE("mfa.delete"), MFA_READ("mfa.read"), MFA_UPDATE("mfa.update"),

	MFACONFIGURATIONS_CREATE("mfaconfigurations.create"), MFACONFIGURATIONS_DELETE("mfaconfigurations.delete"),
	MFACONFIGURATIONS_UPDATE("mfaconfigurations.update"), MFACONFIGURATIONS_READ("mfaconfigurations.read"),

	MFAKEY_CREATE("mfakey.create"), MFAKEY_DELETE("mfakey.delete"), MFAKEY_UPDATE("mfakey.update"),
	MFAKEY_READ("mfakey.read"),

	MFATYPE_CREATE("mfatype.create"), MFATYPE_DELETE("mfatype.delete"), MFATYPE_UPDATE("mfatype.update"),
	MFATYPE_READ("mfatype.read"),

	MFA_C360_FEATURE_GET_PROC("mfa_c360_feature_get_proc.read"),

	MODEL_READ("model.read"),

	MFAVARIABLEREFERENCE_CREATE("mfavariablereference.create"),
	MFAVARIABLEREFERENCE_DELETE("mfavariablereference.delete"),
	MFAVARIABLEREFERENCE_UPDATE("mfavariablereference.update"), MFAVARIABLEREFERENCE_READ("mfavariablereference.read"),

	NOTIFICATIONCARDINFO_CREATE("notificationcardinfo.create"),
	NOTIFICATIONCARDINFO_DELETE("notificationcardinfo.delete"), NOTIFICATIONCARDINFO_READ("notificationcardinfo.read"),
	NOTIFICATIONCARDINFO_UPDATE("notificationcardinfo.update"),

	OBJECTSERVICES_CONTEXTPATH("ObjectServices.contextPath"),

	OUTAGEMESSAGE_CREATE("outagemessage.create"), OUTAGEMESSAGE_DELETE("outagemessage.delete"),
	OUTAGEMESSAGE_READ("outagemessage.read"), OUTAGEMESSAGE_UPDATE("outagemessage.update"),
	OUTAGEMESSAGE_VIEW_READ("outagemessage_view.read"),

	OUTAGEMESSAGEAPP_CREATE("outagemessageapp.create"), OUTAGEMESSAGEAPP_DELETE("outagemessageapp.delete"),
	OUTAGEMESSAGEAPP_UPDATE("outagemessageapp.update"), OUTAGEMESSAGEAPP_READ("outagemessageapp.read"),

	OVERALLPAYMENTLIMITS_VIEW_READ("overallpaymentlimits_view.read"),

	ORGANISATION_READ("organisation.read"),

	PASSWORDLOCKOUTSETTINGS_CREATE("passwordlockoutsettings.create"),
	PASSWORDLOCKOUTSETTINGS_READ("passwordlockoutsettings.read"),
	PASSWORDLOCKOUTSETTINGS_UPDATE("passwordlockoutsettings.update"),
	PASSWORDLOCKOUTSETTINGS_DELETE("passwordlockoutsettings.delete"),

	PASSWORDPOLICY_CREATE("passwordpolicy.create"), PASSWORDPOLICY_DELETE("passwordpolicy.delete"),
	PASSWORDPOLICY_READ("passwordpolicy.read"), PASSWORDPOLICY_UPDATE("passwordpolicy.update"),

	PASSWORDRULES_CREATE("passwordrules.create"), PASSWORDRULES_READ("passwordrules.read"),
	PASSWORDRULES_UPDATE("passwordrules.update"), PASSWORDRULES_DELETE("passwordrules.delete"),

	PERIODICLIMITENDUSER_VIEW_READ("periodiclimitenduser_view.read"),

	PERIODICLIMITSERVICE_VIEW_READ("periodiclimitservice_view.read"),

	PERIODICLIMITUSERGROUP_VIEW_READ("periodiclimitusergroup_view.read"),

	PERIODICLIMIT_CREATE("periodiclimit.create"), PERIODICLIMIT_DELETE("periodiclimit.delete"),
	PERIODICLIMIT_READ("periodiclimit.read"), PERIODICLIMIT_UPDATE("periodiclimit.update"),

	PERIOD_CREATE("period.create"), PERIOD_DELETE("period.delete"), PERIOD_READ("period.read"),
	PERIOD_UPDATE("period.update"),

	PERMISSIONSALLUSERS_VIEW_READ("permissionsallusers_view.read"),

	PERMISSIONS_VIEW_READ("permissions_view.read"),

	PERMISSIONTYPE_CREATE("permissiontype.create"), PERMISSIONTYPE_DELETE("permissiontype.delete"),
	PERMISSIONTYPE_READ("permissiontype.read"), PERMISSIONTYPE_UPDATE("permissiontype.update"),

	PERMISSION_CREATE("permission.create"), PERMISSION_DELETE("permission.delete"), PERMISSION_READ("permission.read"),
	PERMISSION_UPDATE("permission.update"),

	POLICYCONTENT_CREATE("policycontent.create"), POLICYCONTENT_DELETE("policycontent.delete"),
	POLICYCONTENT_READ("policycontent.read"), POLICYCONTENT_UPDATE("policycontent.update"),

	POLICIES_CREATE("policies.create"), POLICIES_DELETE("policies.delete"), POLICIES_READ("policies.read"),
	POLICIES_UPDATE("policies.update"),

	POLICY_VIEW_READ("policy_view.read"),

	PRIVACYPOLICY_CREATE("privacypolicy.create"), PRIVACYPOLICY_DELETE("privacypolicy.delete"),
	PRIVACYPOLICY_READ("privacypolicy.read"), PRIVACYPOLICY_UPDATE("privacypolicy.update"),

	PRODUCTDETAIL_VIEW_READ("productdetail_view.read"),

	PRODUCTTYPE_CREATE("producttype.create"), PRODUCTTYPE_DELETE("producttype.delete"),
	PRODUCTTYPE_READ("producttype.read"), PRODUCTTYPE_UPDATE("producttype.update"),

	PRODUCT_CREATE("product.create"), PRODUCT_DELETE("product.delete"), PRODUCT_READ("product.read"),
	PRODUCT_UPDATE("product.update"),

	REGION_CREATE("region.create"), REGION_DELETE("region.delete"),
	REGION_DETAILS_VIEW_READ("region_details_view.read"), REGION_READ("region.read"), REGION_UPDATE("region.update"),

	REPORTS_MESSAGES_RECEIVED_SERVICE("reports_messages_received.service"),
	REPORTS_MESSAGES_SENT_SERVICE("reports_messages_sent.service"),
	REPORTS_THREADS_AVERAGEAGE_SERVICE("reports_threads_averageage.service"),
	REPORTS_THREADS_NEW_SERVICE("reports_threads_new.service"),
	REPORTS_THREADS_RESOLVED_SERVICE("reports_threads_resolved.service"),
	REPORTDATASOURCE_CREATE("reportdatasource.create"), REPORTDATASOURCE_DELETE("reportdatasource.delete"),
	REPORTDATASOURCE_READ("reportdatasource.read"), REPORTDATASOURCE_UPDATE("reportdatasource.update"),
	GET_REPORTS_PROC("get_reports_proc.service"),

	REPORT_CREATE("report.create"), REPORT_DELETE("report.delete"), REPORT_READ("report.read"),
	REPORT_UPDATE("report.update"),

	REQUESTCATEGORY_CREATE("requestcategory.create"), REQUESTCATEGORY_READ("requestcategory.read"),
	REQUESTCATEGORY_UPDATE("requestcategory.update"),

	REQUESTMESSAGES_PROC("requestmessages_proc.service"),

	REQUESTMESSAGE_CREATE("requestmessage.create"), REQUESTMESSAGE_DELETE("requestmessage.delete"),
	REQUESTMESSAGE_READ("requestmessage.read"), REQUESTMESSAGE_UPDATE("requestmessage.update"),
	RETAIL_ACCOUNTS_PROC("retail_accounts_proc.service"),

	ROLECOMPOSITEPERMISSION_CREATE("rolecompositepermission.create"),
	ROLECOMPOSITEPERMISSION_DELETE("rolecompositepermission.delete"),
	ROLECOMPOSITEPERMISSION_READ("rolecompositepermission.read"),
	ROLECOMPOSITEPERMISSION_UPDATE("rolecompositepermission.update"),

	ROLEPERMISSION_CREATE("rolepermission.create"), ROLEPERMISSION_DELETE("rolepermission.delete"),
	ROLEPERMISSION_READ("rolepermission.read"), ROLEPERMISSION_UPDATE("rolepermission.update"),
	ROLEPERMISSION_VIEW_READ("rolepermission_view.read"),
	ROLEPERMISSIONDELETE_PROC("rolePermissionDelete_proc.service"),
	ROLEPERMISSIONDELETE_PROC_APPROVAL("rolePermissionDelete_approval_proc.service"),
	BULKASSIGN_PERMISSIONS_TO_ROLE("bulkassign_permissions_to_role.service"),
	ROLECOMPOSITEACTION_CREATE("rolecompositeaction.create"), ROLECOMPOSITEACTION_DELETE("rolecompositeaction.delete"),
	ROLECOMPOSITEACTION_READ("rolecompositeaction.read"), ROLECOMPOSITEACTION_UPDATE("rolecompositeaction.update"),
	ROLESCOMPOSITEACTIONS_PROC("rolescompositeactions_proc.service"),

	ROLES_VIEW_READ("roles_view.read"),

	ROLETYPE_CREATE("roletype.create"), ROLETYPE_DELETE("roletype.delete"), ROLETYPE_READ("roletype.read"),
	ROLETYPE_UPDATE("roletype.update"),

	ROLEUSER_VIEW_READ("roleuser_view.read"),

	ROLE_CREATE("role.create"), ROLE_DELETE("role.delete"), ROLE_READ("role.read"), ROLE_UPDATE("role.update"),

	SECURITYIMAGE_CREATE("securityimage.create"), SECURITYIMAGE_DELETE("securityimage.delete"),
	SECURITYIMAGE_READ("securityimage.read"), SECURITYIMAGE_UPDATE("securityimage.update"),

	SECURITYQUESTION_CREATE("securityquestion.create"), SECURITYQUESTION_DELETE("securityquestion.delete"),
	SECURITYQUESTION_READ("securityquestion.read"), SECURITYQUESTION_UPDATE("securityquestion.update"),

	SECURITY_IMAGES_VIEW_READ("security_images_view.read"),
	SECURITY_QUESTIONS_VIEW_READ("security_questions_view.read"),

	SERVICECHANNEL_CREATE("servicechannel.create"), SERVICECHANNEL_DELETE("servicechannel.delete"),
	SERVICECHANNEL_READ("servicechannel.read"), SERVICECHANNEL_UPDATE("servicechannel.update"),

	SERVICECOMMUNICATION_CREATE("servicecommunication.create"),
	SERVICECOMMUNICATION_DELETE("servicecommunication.delete"), SERVICECOMMUNICATION_READ("servicecommunication.read"),
	SERVICECOMMUNICATION_UPDATE("servicecommunication.update"),

	SERVICETYPE_CREATE("servicetype.create"), SERVICETYPE_DELETE("servicetype.delete"),
	SERVICETYPE_READ("servicetype.read"), SERVICETYPE_UPDATE("servicetype.update"),

	SERVICE_CREATE("service.create"), SERVICE_DELETE("service.delete"), SERVICE_READ("service.read"),
	SERVICE_UPDATE("service.update"), SERVICE_VIEW_READ("service_view.read"),

	SERVICE_CHANNELS_CREATE("service_channels.create"), SERVICE_CHANNELS_DELETE("service_channels.delete"),
	SERVICE_CHANNELS_READ("service_channels.read"), SERVICE_CHANNELS_UPDATE("service_channels.update"),

	SHAREDREPORT_CREATE("sharedreport.create"), SHAREDREPORT_DELETE("sharedreport.delete"),
	SHAREDREPORT_READ("sharedreport.read"), SHAREDREPORT_UPDATE("sharedreport.update"),

	SIGNATORYTYPE_CREATE("signatorytype.create"), SIGNATORYTYPE_DELETE("signatorytype.delete"),
	SIGNATORYTYPE_READ("signatorytype.read"), SIGNATORYTYPE_UPDATE("signatorytype.update"),
	STATUSCHANGE_CREATE("statuschange.create"), STATUSCHANGE_DELETE("statuschange.delete"),
	STATUSCHANGE_READ("statuschange.read"), STATUSCHANGE_UPDATE("statuschange.update"),

	STATUSTYPE_CREATE("statustype.create"), STATUSTYPE_DELETE("statustype.delete"), STATUSTYPE_READ("statustype.read"),
	STATUSTYPE_UPDATE("statustype.update"),

	STATUS_CREATE("status.create"), STATUS_DELETE("status.delete"), STATUS_READ("status.read"),
	STATUS_UPDATE("status.update"),

	SYSTEMCONFIGURATION_CREATE("systemconfiguration.create"), SYSTEMCONFIGURATION_DELETE("systemconfiguration.delete"),
	SYSTEMCONFIGURATION_READ("systemconfiguration.read"), SYSTEMCONFIGURATION_UPDATE("systemconfiguration.update"),

	SYSTEMUSER_CREATE("systemuser.create"), SYSTEMUSER_DELETE("systemuser.delete"),
	SYSTEMUSER_PERMISSIONS_VIEW_READ("systemuser_permissions_view.read"), SYSTEMUSER_READ("systemuser.read"),
	SYSTEMUSER_UPDATE("systemuser.update"), SYSTEMUSER_VIEW_READ("systemuser_view.read"),

	TERMSANDCONDITIONS_CREATE("termsandconditions.create"), TERMSANDCONDITIONS_DELETE("termsandconditions.delete"),
	TERMSANDCONDITIONS_READ("termsandconditions.read"), TERMSANDCONDITIONS_UPDATE("termsandconditions.update"),

	TERMANDCONDITION_CREATE("termandcondition.create"), TERMANDCONDITION_DELETE("termandcondition.delete"),
	TERMANDCONDITION_READ("termandcondition.read"), TERMANDCONDITION_UPDATE("termandcondition.update"),

	TERMANDCONDITIONTEXT_CREATE("termandconditiontext.create"),
	TERMANDCONDITIONTEXT_DELETE("termandconditiontext.delete"), TERMANDCONDITIONTEXT_READ("termandconditiontext.read"),
	TERMANDCONDITIONTEXT_UPDATE("termandconditiontext.update"),

	TERMANDCONDITIONAPP_CREATE("termandconditionapp.create"), TERMANDCONDITIONAPP_DELETE("termandconditionapp.delete"),
	TERMANDCONDITIONAPP_READ("termandconditionapp.read"), TERMANDCONDITIONAPP_UPDATE("termandconditionapp.update"),

	TRANSACTIONALLOGS_READ("transactionallogs.read"),

	SEARCHCUSTOMERAUDITLOGS_READ("SearchCustomerAuditLogs.read"),

	TRANSACTIONFEEGROUP_VIEW_READ("transactionfeegroup_view.read"),

	TRANSACTIONFEESENDUSER_VIEW_READ("transactionfeesenduser_view.read"),

	TRANSACTIONFEESERVICE_VIEW_READ("transactionfeeservice_view.read"),

	TRANSACTIONFEESLAB_CREATE("transactionfeeslab.create"), TRANSACTIONFEESLAB_DELETE("transactionfeeslab.delete"),
	TRANSACTIONFEESLAB_READ("transactionfeeslab.read"), TRANSACTIONFEESLAB_UPDATE("transactionfeeslab.update"),

	TRANSACTIONFEE_CREATE("transactionfee.create"), TRANSACTIONFEE_DELETE("transactionfee.delete"),
	TRANSACTIONFEE_READ("transactionfee.read"), TRANSACTIONFEE_UPDATE("transactionfee.update"),

	TRANSACTIONGROUPSERVICE_CREATE("transactiongroupservice.create"),
	TRANSACTIONGROUPSERVICE_DELETE("transactiongroupservice.delete"),
	TRANSACTIONGROUPSERVICE_READ("transactiongroupservice.read"),
	TRANSACTIONGROUPSERVICE_UPDATE("transactiongroupservice.update"),

	TRANSACTIONGROUP_CREATE("transactiongroup.create"), TRANSACTIONGROUP_DELETE("transactiongroup.delete"),
	TRANSACTIONGROUP_READ("transactiongroup.read"), TRANSACTIONGROUP_UPDATE("transactiongroup.update"),

	TRANSACTIONLIMIT_CREATE("transactionlimit.create"), TRANSACTIONLIMIT_DELETE("transactionlimit.delete"),
	TRANSACTIONLIMIT_READ("transactionlimit.read"), TRANSACTIONLIMIT_UPDATE("transactionlimit.update"),

	TRANSACTIONLOGS_CREATE("transactionlogs.create"), TRANSACTIONLOGS_DELETE("transactionlogs.delete"),
	TRANSACTIONLOGS_READ("transactionlogs.read"), TRANSACTIONLOGS_UPDATE("transactionlogs.update"),

	TRANSACTIONREPORTS_READ("transactionreports.read"),

	TRAVELNOTIFICATIONS_VIEW_READ("travelnotifications_view.read"),

	TRAVELNOTIFICATION_CREATE("travelnotification.create"), TRAVELNOTIFICATION_DELETE("travelnotification.delete"),
	TRAVELNOTIFICATION_READ("travelnotification.read"), TRAVELNOTIFICATION_UPDATE("travelnotification.update"),

	USERADDRESS_CREATE("useraddress.create"), USERADDRESS_DELETE("useraddress.delete"),
	USERADDRESS_READ("useraddress.read"), USERADDRESS_UPDATE("useraddress.update"),

	USERAUTHDATASERVICE_GETSYSTEMUSERAUTHDATA("UserAuthDataService.getSystemUserAuthData"),
	USERAUTHDATASERVICE_GETAPIUSERAUTHDATA("UserAuthDataService.getAPIUserAuthData"),

	USERCOMPOSITEPERMISSION_CREATE("usercompositepermission.create"),
	USERCOMPOSITEPERMISSION_DELETE("usercompositepermission.delete"),
	USERCOMPOSITEPERMISSION_READ("usercompositepermission.read"),
	USERCOMPOSITEPERMISSION_UPDATE("usercompositepermission.update"),

	USERCOMPOSITEACTION_CREATE("usercompositeaction.create"), USERCOMPOSITEACTION_DELETE("usercompositeaction.delete"),
	USERCOMPOSITEACTION_READ("usercompositeaction.read"), USERCOMPOSITEACTION_UPDATE("usercompositeaction.update"),

	USERDIRECTPERMISSION_VIEW_READ("userdirectpermission_view.read"),

	USERNAMEPOLICY_CREATE("usernamepolicy.create"), USERNAMEPOLICY_DELETE("usernamepolicy.delete"),
	USERNAMEPOLICY_READ("usernamepolicy.read"), USERNAMEPOLICY_UPDATE("usernamepolicy.update"),

	USERNAMERULES_CREATE("usernamerules.create"), USERNAMERULES_READ("usernamerules.read"),
	USERNAMERULES_UPDATE("usernamerules.update"), USERNAMERULES_DELETE("usernamerules.delete"),

	USERPERMISSION_CREATE("userpermission.create"), USERPERMISSION_DELETE("userpermission.delete"),
	USERPERMISSION_READ("userpermission.read"), USERPERMISSION_UPDATE("userpermission.update"),
	USERPERMISSION_VIEW_READ("userpermission_view.read"),
	USERPERMISSIONDELETE_PROC("userPermissionDelete_proc.service"),
	USERPERMISSIONCREATE_PROC("userPermissionsAndActionsCreate_proc.service"),

	USERROLE_CREATE("userrole.create"), USERROLE_DELETE("userrole.delete"), USERROLE_READ("userrole.read"),
	USERROLE_UPDATE("userrole.update"),

	VARIABLEREFERENCE_CREATE("variablereference.create"), VARIABLEREFERENCE_DELETE("variablereference.delete"),
	VARIABLEREFERENCE_READ("variablereference.read"), VARIABLEREFERENCE_UPDATE("variablereference.update"),

	WORKSCHEDULE_CREATE("workschedule.create"), WORKSCHEDULE_DELETE("workschedule.delete"),
	WORKSCHEDULE_READ("workschedule.read"), WORKSCHEDULE_UPDATE("workschedule.update"),

	ELIGIBILITYCRITERIA_CREATE("eligibilitycriteria.create"), ELIGIBILITYCRITERIA_DELETE("eligibilitycriteria.delete"),
	ELIGIBILITYCRITERIA_READ("eligibilitycriteria.read"), ELIGIBILITYCRITERIA_UPDATE("eligibilitycriteria.update"),

	IDTYPE_CREATE("idtype.create"), IDTYPE_DELETE("idtype.delete"), IDTYPE_READ("idtype.read"),
	IDTYPE_UPDATE("idtype.update"),

	ONBOARDINGTERMSANDCONDITIONS_CREATE("onboardingtermsandconditions.create"),
	ONBOARDINGTERMSANDCONDITIONS_DELETE("onboardingtermsandconditions.delete"),
	ONBOARDINGTERMSANDCONDITIONS_READ("onboardingtermsandconditions.read"),
	ONBOARDINGTERMSANDCONDITIONS_UPDATE("onboardingtermsandconditions.update"),

	// USERGRANTDATASERVICE_GETALLOWEDOPERATIONS("UserGrantDataService.getAllowedOperations"),
	// USERGRANTDATASERVICE_GETGRANTEDPERMISSIONS("UserGrantDataService.getGrantedPermissions"),

	CHANNEL_VIEW_READ("channel_view.read"),

	ALERTCATEGORYCHANNEL_CREATE("alertcategorychannel.create"),
	ALERTCATEGORYCHANNEL_DELETE("alertcategorychannel.delete"), ALERTCATEGORYCHANNEL_READ("alertcategorychannel.read"),
	ALERTCATEGORYCHANNEL_UPDATE("alertcategorychannel.update"),

	CUSTOMERALERTCATEGORYCHANNEL_CREATE("customeralertcategorychannel.create"),
	CUSTOMERALERTCATEGORYCHANNEL_DELETE("customeralertcategorychannel.delete"),
	CUSTOMERALERTCATEGORYCHANNEL_READ("customeralertcategorychannel.read"),
	CUSTOMERALERTCATEGORYCHANNEL_UPDATE("customeralertcategorychannel.update"),

	CUSTOMERALERTCHANNEL_CREATE("customeralertchannel.create"),
	CUSTOMERALERTCHANNEL_DELETE("customeralertchannel.delete"), CUSTOMERALERTCHANNEL_READ("customeralertchannel.read"),
	CUSTOMERALERTCHANNEL_UPDATE("customeralertchannel.update"),

	CUSTOMERALERTCHANNEL_SYNC("customeralertchannel_sync"), CUSTOMERALERTFREQUENCY_SYNC("customeralertfrequency_sync"),
	DBXCUSTOMERALERTENTITLEMENT_SYNC("dbxcustomeralertentitlement_sync"),

	CUSTOMERALERTFREQUENCY_CREATE("customeralertfrequency.create"),
	CUSTOMERALERTFREQUENCY_DELETE("customeralertfrequency.delete"),
	CUSTOMERALERTFREQUENCY_READ("customeralertfrequency.read"),
	CUSTOMERALERTFREQUENCY_UPDATE("customeralertfrequency.update"),

	DBXALERTCATEGORY_CREATE("dbxalertcategory.create"), DBXALERTCATEGORY_DELETE("dbxalertcategory.delete"),
	DBXALERTCATEGORY_READ("dbxalertcategory.read"), DBXALERTCATEGORY_UPDATE("dbxalertcategory.update"),

	ALERTCATEGORY_VIEW_READ("alertcategory_view.read"),

	ALERTTYPECUSTOMERTYPE_CREATE("alerttypecustomertype.create"),
	ALERTTYPECUSTOMERTYPE_DELETE("alerttypecustomertype.delete"),
	ALERTTYPECUSTOMERTYPE_READ("alerttypecustomertype.read"),
	ALERTTYPECUSTOMERTYPE_UPDATE("alerttypecustomertype.update"),

	ALERTTYPEACCOUNTTYPE_CREATE("alerttypeaccounttype.create"),
	ALERTTYPEACCOUNTTYPE_DELETE("alerttypeaccounttype.delete"), ALERTTYPEACCOUNTTYPE_READ("alerttypeaccounttype.read"),
	ALERTTYPEACCOUNTTYPE_UPDATE("alerttypeaccounttype.update"),

	DBXALERTTYPETEXT_CREATE("dbxalerttypetext.create"), DBXALERTTYPETEXT_DELETE("dbxalerttypetext.delete"),
	DBXALERTTYPETEXT_READ("dbxalerttypetext.read"), DBXALERTTYPETEXT_UPDATE("dbxalerttypetext.update"),

	ALERTTYPECHANNEL_CREATE("alerttypechannel.create"), ALERTTYPECHANNEL_DELETE("alerttypechannel.delete"),
	ALERTTYPECHANNEL_READ("alerttypechannel.read"), ALERTTYPECHANNEL_UPDATE("alerttypechannel.update"),

	DBXCUSTOMERALERTENTITLEMENT_CREATE("dbxcustomeralertentitlement.create"),
	DBXCUSTOMERALERTENTITLEMENT_DELETE("dbxcustomeralertentitlement.delete"),
	DBXCUSTOMERALERTENTITLEMENT_READ("dbxcustomeralertentitlement.read"),
	DBXCUSTOMERALERTENTITLEMENT_UPDATE("dbxcustomeralertentitlement.update"),

	ALERTTYPE_VIEW_READ("alerttype_view.read"),

	CUSTOMER_ALERTCATEGORY_VIEW_READ("customer_alertcategory_view.read"),

	ALERTCONDITION_CREATE("alertcondition.create"), ALERTCONDITION_DELETE("alertcondition.delete"),
	ALERTCONDITION_READ("alertcondition.read"), ALERTCONDITION_UPDATE("alertcondition.update"),

	ALERTATTRIBUTE_CREATE("alertattribute.create"), ALERTATTRIBUTE_DELETE("alertattribute.delete"),
	ALERTATTRIBUTE_READ("alertattribute.read"), ALERTATTRIBUTE_UPDATE("alertattribute.update"),

	ALERTATTRIBUTE_VIEW_READ("alertattribute_view.read"),

	CUSTOMERALERTSWITCH_CREATE("customeralertswitch.create"), CUSTOMERALERTSWITCH_DELETE("customeralertswitch.delete"),
	CUSTOMERALERTSWITCH_READ("customeralertswitch.read"), CUSTOMERALERTSWITCH_UPDATE("customeralertswitch.update"),

	ALERTTYPEAPP_VIEW_READ("alerttypeapp_view.read"),

	ALERTTYPECUSTOMERTYPE_VIEW_READ("alerttypecustomertype_view.read"),

	ALERTTYPEACCOUNTTYPE_VIEW_READ("alerttypeaccounttype_view.read"),

	ALERTSUBTYPE_CREATE("alertsubtype.create"), ALERTSUBTYPE_DELETE("alertsubtype.delete"),
	ALERTSUBTYPE_READ("alertsubtype.read"), ALERTSUBTYPE_UPDATE("alertsubtype.update"),

	ALERTSUBTYPETEXT_CREATE("alertsubtypetext.create"), ALERTSUBTYPETEXT_DELETE("alertsubtypetext.delete"),
	ALERTSUBTYPETEXT_READ("alertsubtypetext.read"), ALERTSUBTYPETEXT_UPDATE("alertsubtypetext.update"),

	ALERTSUBTYPECHANNEL_CREATE("alertsubtypechannel.create"), ALERTSUBTYPECHANNEL_DELETE("alertsubtypechannel.delete"),
	ALERTSUBTYPECHANNEL_READ("alertsubtypechannel.read"), ALERTSUBTYPECHANNEL_UPDATE("alertsubtypechannel.update"),

	ALERTSUBTYPEAPP_CREATE("alertsubtypeapp.create"), ALERTSUBTYPEAPP_DELETE("alertsubtypeapp.delete"),
	ALERTSUBTYPEAPP_READ("alertsubtypeapp.read"), ALERTSUBTYPEAPP_UPDATE("alertsubtypeapp.update"),

	ALERTSUBTYPEACCOUNTTYPE_CREATE("alertsubtypeaccounttype.create"),
	ALERTSUBTYPEACCOUNTTYPE_DELETE("alertsubtypeaccounttype.delete"),
	ALERTSUBTYPEACCOUNTTYPE_READ("alertsubtypeaccounttype.read"),
	ALERTSUBTYPEACCOUNTTYPE_UPDATE("alertsubtypeaccounttype.update"),

	ALERTSUBTYPECUSTOMERTYPE_CREATE("alertsubtypecustomertype.create"),
	ALERTSUBTYPECUSTOMERTYPE_DELETE("alertsubtypecustomertype.delete"),
	ALERTSUBTYPECUSTOMERTYPE_READ("alertsubtypecustomertype.read"),
	ALERTSUBTYPECUSTOMERTYPE_UPDATE("alertsubtypecustomertype.update"),

	ALERTSUBTYPETEXTVIEW_READ("alertsubtypetext_view.read"),

	COMMUNICATIONTEMPLATE_CREATE("communicationtemplate.create"),
	COMMUNICATIONTEMPLATE_DELETE("communicationtemplate.delete"),
	COMMUNICATIONTEMPLATE_READ("communicationtemplate.read"),
	COMMUNICATIONTEMPLATE_UPDATE("communicationtemplate.update"),

	COMMUNICATIONTEMPLATE_CHANNEL_VIEW_READ("communicationtemplate_channel_view.read"),

	ALERTHISTORY_CREATE("alerthistory.create"), ALERTHISTORY_DELETE("alerthistory.delete"),
	ALERTHISTORY_READ("alerthistory.read"), ALERTHISTORY_UPDATE("alerthistory.update"),

	DBXALERTCATEGORYTEXT_CREATE("dbxalertcategorytext.create"),
	DBXALERTCATEGORYTEXT_DELETE("dbxalertcategorytext.delete"), DBXALERTCATEGORYTEXT_READ("dbxalertcategorytext.read"),
	DBXALERTCATEGORYTEXT_UPDATE("dbxalertcategorytext.update"),

	DBXALERTTYPE_CREATE("dbxalerttype.create"), DBXALERTTYPE_DELETE("dbxalerttype.delete"),
	DBXALERTTYPE_READ("dbxalerttype.read"), DBXALERTTYPE_UPDATE("dbxalerttype.update"),

	ALERTTYPEAPP_CREATE("alerttypeapp.create"), ALERTTYPEAPP_DELETE("alerttypeapp.delete"),
	ALERTTYPEAPP_READ("alerttypeapp.read"), ALERTTYPEAPP_UPDATE("alerttypeapp.update"),

	ACCOUNTTYPE_CREATE("accounttype.create"), ACCOUNTTYPE_DELETE("accounttype.delete"),
	ACCOUNTTYPE_READ("accounttype.read"), ACCOUNTTYPE_UPDATE("accounttype.update"),

	EVENTTYPE_CREATE("eventtype.create"), EVENTTYPE_DELETE("eventtype.delete"), EVENTTYPE_READ("eventtype.read"),
	EVENTTYPE_UPDATE("eventtype.update"),

	EVENTSUBTYPE_CREATE("eventsubtype.create"), EVENTSUBTYPE_DELETE("eventsubtype.delete"),
	EVENTSUBTYPE_READ("eventsubtype.read"), EVENTSUBTYPE_UPDATE("eventsubtype.update"),

	ALERTCONTENTFIELDS_CREATE("alertcontentfields.create"), ALERTCONTENTFIELDS_DELETE("alertcontentfields.delete"),
	ALERTCONTENTFIELDS_READ("alertcontentfields.read"), ALERTCONTENTFIELDS_UPDATE("alertcontentfields.update"),

	COUNTRYCODE_CREATE("countrycode.create"), COUNTRYCODE_DELETE("countrycode.delete"),
	COUNTRYCODE_READ("countrycode.read"), COUNTRYCODE_UPDATE("countrycode.update"),

	LOGSERVICES_GETTRANSACTIONLOGS("LogServices.getTransactionLogs"),

	ALERT_HISTORY_PROC_SERVICE("alert_history_proc.service"),
	CUSTOMER_GROUP_PROC_SERVICE("customer_group_proc.service"),
	CUSTOMER_GROUP_UNLINK_PROC_SERVICE("customer_group_unlink_proc.service"),

	CUSTOMER_REQUEST_MESSAGE_SEARCH_PROC_SERVICE("customer_request_message_search_proc.service"),

	CUSTOMER_REQUEST_ARCHIVED_MESSAGE_SEARCH_PROC_SERVICE("customer_request_archived_message_search_proc.service"),

	LEAD_CREATE("lead.create"), LEAD_DELETE("lead.delete"), LEAD_READ("lead.read"), LEAD_UPDATE("lead.update"),

	ARCHIVEDLEAD_CREATE("archivedlead.create"), ARCHIVEDLEAD_DELETE("archivedlead.delete"),
	ARCHIVEDLEAD_READ("archivedlead.read"), ARCHIVEDLEAD_UPDATE("archivedlead.update"),

	LEAD_CSR_COUNT_VIEW_READ("lead_csr_count_view.read"),

	LEAD_STATUS_COUNT_VIEW_READ("lead_status_count_view.read"),

	LEAD_SEARCH_PROC_SERVICE("lead_search_proc.service"),

	CLOSUREREASON_CREATE("closurereason.create"), CLOSUREREASON_DELETE("closurereason.delete"),
	CLOSUREREASON_READ("closurereason.read"), CLOSUREREASON_UPDATE("closurereason.update"),

	LEADNOTE_CREATE("leadnote.create"), LEADNOTE_DELETE("leadnote.delete"), LEADNOTE_READ("leadnote.read"),
	LEADNOTE_UPDATE("leadnote.update"),

	ARCHIVEDLEADNOTE_CREATE("archivedleadnote.create"), ARCHIVEDLEADNOTE_DELETE("archivedleadnote.delete"),
	ARCHIVEDLEADNOTE_READ("archivedleadnote.read"), ARCHIVEDLEADNOTE_UPDATE("archivedleadnote.update"),

	BROWSERSUPPORTTYPE_CREATE("browsersupporttype.create"), BROWSERSUPPORTTYPE_DELETE("browsersupporttype.delete"),
	BROWSERSUPPORTTYPE_READ("browsersupporttype.read"), BROWSERSUPPORTTYPE_UPDATE("browsersupporttype.update"),

	BROWSERSUPPORT_CREATE("browsersupport.create"), BROWSERSUPPORT_DELETE("browsersupport.delete"),
	BROWSERSUPPORT_READ("browsersupport.read"), BROWSERSUPPORT_UPDATE("browsersupport.update"),

	BROWSERSUPPORTDISPLAYNAMETEXT_CREATE("browsersupportdisplaynametext.create"),
	BROWSERSUPPORTDISPLAYNAMETEXT_DELETE("browsersupportdisplaynametext.delete"),
	BROWSERSUPPORTDISPLAYNAMETEXT_READ("browsersupportdisplaynametext.read"),
	BROWSERSUPPORTDISPLAYNAMETEXT_UPDATE("browsersupportdisplaynametext.update"),

	USERROLECUSTOMERROLE_CREATE("userrolecustomerrole.create"),
	USERROLECUSTOMERROLE_DELETE("userrolecustomerrole.delete"), USERROLECUSTOMERROLE_READ("userrolecustomerrole.read"),
	USERROLECUSTOMERROLE_UPDATE("userrolecustomerrole.update"),
	USERROLESERVICEDEFINITION_CREATE("userroleservicedefinition.create"),
	USERROLESERVICEDEFINITION_DELETE("userroleservicedefinition.delete"),
	INTERNAL_ROLE_TO_CUSTOMER_ROLE_MAPPING_VIEW_READ("internal_role_to_customer_role_mapping_view.read"),
	INTERNAL_ROLE_TO_SERVICEDEFINITION_MAPPING_VIEW_READ("internal_role_to_serviceDefinition_mapping_view.read"),

	INTERNAL_USER_ACCESS_ON_GIVEN_CUSTOMER_PROC("internal_user_access_on_given_customer_proc.service"),
	INTERNAL_USER_ACCESS_TO_CUSTOMER_ON_SERVICEDEFINITION_PROC(
			"internal_user_access_to_customer_on_servicedefinition_proc.service"),
	LEAD_SEARCH_COUNT_PROC_SERVICE("lead_search_count_proc.service"),

	LEAD_NOTES_SEARCH_PROC_SERVICE("lead_notes_search_proc.service"),

	LEAD_ARCHIVE_PROC_SERVICE("lead_archive_proc.service"),

	LEAD_ASSIGN_PROC_SERVICE("lead_assign_proc.service"),

	SYSTEMUSER_PERMISSION_PROC_SERVICE("systemuser_permission_proc.service"),
	SERVICE_PERMISSION_MAPPER_READ("service_permission_mapper.read"),
	SYSTEMROLES_PERMISSION_PROC_SERVICE("systemroles_permission_proc.service"),

	CUSTOMER_BASIC_INFO_PROC_SERVICE("customer_basic_info_proc.service"),

	CUSTOMER_ENTITLEMENTS_ORCHESTRATION("CustomerEntitlements.getCustomerEntitlements"),

	ORGANISATIONACTIONLIMIT_CREATE("organisationactionlimit.create"),
	ORGANISATIONACTIONLIMIT_DELETE("organisationactionlimit.delete"),
	ORGANISATIONACTIONLIMIT_READ("organisationactionlimit.read"),
	ORGANISATIONACTIONLIMIT_UPDATE("organisationactionlimit.update"),
	ORGANISATIONFEATURES_READ("organisationfeatures.read"),

	ORGANISATION_ACTION_LIMITS_VIEW_READ("organisation_action_limits_view.read"),

	GROUPACTIONLIMIT_CREATE("groupactionlimit.create"), GROUPACTIONLIMIT_DELETE("groupactionlimit.delete"),
	GROUPACTIONLIMIT_READ("groupactionlimit.read"), GROUPACTIONLIMIT_UPDATE("groupactionlimit.update"),

	CUSTOMERACCOUNTS_READ("customeraccounts.read"),

	FEATUREACTION_CREATE("featureaction.create"), FEATUREACTION_DELETE("featureaction.delete"),
	FEATUREACTION_READ("featureaction.read"), FEATUREACTION_UPDATE("featureaction.update"),
	FEATUREACTIONROLETYPE_READ("featureactionroletype.read"), FEATURE_VIEW_READ("feature_view.read"),

	LIMITSUBTYPE_READ("limitsubtype.read"),

	FEATURE_ACTIONS_VIEW_READ("feature_actions_view.read"),
	FEATURE_ACTION_ROLES_VIEW_READ("feature_action_roles_view.read"),

	CUSTOMERACTION_CREATE("customeraction.create"), CUSTOMERACTION_DELETE("customeraction.delete"),
	CUSTOMERACTION_READ("customeraction.read"), CUSTOMERACTION_UPDATE("customeraction.update"),

	CUSTOMER_REQUESTS_ASSIGN_PROC_SERVICE("customer_requests_assign_proc.service"),

	GROUP_FEATURES_ACTIONS_VIEW_READ("group_features_actions_view.read"),
	CUSTOMER_SEARCH_BY_ID("customer_search_by_id"), GET_CUSTOMER_CONTACT("get_customer_contact"),
	CUSTOMER_SEARCH_PARTY_MS("party_customer_search"), ARRANGEMENT_DETAILS("arrangement_details"),

	KEYCLOAK_USERS("FetchKeycloakUsersOrchService.fetchKeyCloakUsers"),

	KEYCLOAK_USER_ROLES("FetchKeycloakUsersOrchService.fetchKeyCloakUserRole"),
	KEYCLOAK_ROLES("KeyCloakAdminServices.fetchUserRoles"),
	KEYCLOAK_TOKEN("KeyCloakIntegration.getServiceAccountBackendToken"),
	KEYCLOAK_USERS_LIST("KeyCloakAdminServices.fetchUsersList"),

	PERFORM_EXTERNAL_SUBSCRIPTION("AlertOrchestration.performExternlSubscription"),
	T24_ALERT_SUBSCRIPTION_UPDATE("T24AlertSubscription.update"),
	T24_ALERT_SUBSCRIPTION_CREATE("T24AlertSubscription.create"),

	CUSTOMERNOTES_SYSTEMUSERS("CustomerManagement.getCustomerNotes"),
	CUSTOMERNOTESFETCH_VIEW("customernotesfetch_view.read"),
	GET_CUSTOMER_APPLICATIONS("SpotlightDataStorageAPIs.SearchExtractQuery"),
	GET_ODMS_ENTITY_ITEM_BY_ID("SpotlightDataStorageAPIs.GetEntityItemById"),
	GET_ODMS_ENTITY_ITEM_BY_KEY_NAME_VERSION_TYPE("SpotlightDataStorageAPIs.GetEntityItemByKeyNameVersionType"),
	INTERNALUSERDETAILS_GET("internaluserdetails.get"), USERLOB_CREATE("userlob.create"),
	USERLOB_DELETE("userlob.delete"), INTERNALUSERMANAGER_CREATE("internalusermanager.create"),
	INTERNALUSERMANAGER_DELETE("internalusermanager.delete"), INTERNALUSERMANAGER_READ("internalusermanager.read"),
	INTERNALUSERTYPE_CREATE("internalusertype.create"), INTERNALUSERTYPE_DELETE("internalusertype.delete"),
	INTERNALUSERTYPE_READ("internalusertype.read"),

	SCA_CREATE("sca.create"), SCA_DELETE("sca.delete"), SCA_READ("sca.read"), SCA_UPDATE("sca.update"),
	EXTERNAL_FEATUREACTION_CREATE("externalfeatureaction.create"),
	EXTERNAL_FEATUREACTION_DELETE("externalfeatureaction.delete"),
	EXTERNAL_FEATUREACTION_READ("externalfeatureaction.read"),
	EXTERNAL_FEATUREACTION_UPDATE("externalfeatureaction.update"), ROLE_APPROVE_CREATE("role_approve.create"),
	ROLE_APPROVE_DELETE("role_approve.delete"), ROLE_APPROVE_READ("role_approve.read"),
	ROLE_APPROVE_UPDATE("role_approve.update"),

	USERROLESERVICEDEFINITION_APPROVE_CREATE("userroleservicedefinition_approve.create"),
	USERROLESERVICEDEFINITION_APPROVE_DELETE("userroleservicedefinition_approve.delete"),
	USERROLESERVICEDEFINITION_APPROVE_UPDATE("userroleservicedefinition_approve.update"),
	USERROLESERVICEDEFINITION_APPROVE_GET("userroleservicedefinition_approve.get"),

	USERROLE_APPROVE_CREATE("userrole_approve.create"), USERROLE_APPROVE_DELETE("userrole_approve.delete"),
	USERROLE_APPROVE_READ("userrole_approve.read"), USERROLE_APPROVE_UPDATE("userrole_approve.update"),

	BULKASSIGN_PERMISSIONS_TO_ROLE_APPROVAL("bulkassign_permissions_to_role_approval.service"),

	CHECK_APPROVAL_REQUIRED("checkApprovalRequired.service"), UPDATE_APPROVAL_REQUEST("ApprovalRequest.update"),
	DELETE_APPROVAL_REQUEST("ApprovalRequest.delete"),

	CHECK_APPROVAL_REQUIRED_WORKFLOW("checkApprovalRequiredWorkFlow.service"),
	UPDATE_APPROVAL_REQUEST_WORKFLOW("ApprovalRequestWorkFlow.update"),
	DELETE_APPROVAL_REQUEST_WORKFLOW("ApprovalRequestWorkFlow.delete"),

	ROLEPERMISSIONINSERT_APPROVAL_PROC("rolePermissionInsert_Approve_proc.service"),
	ROLEDATAMOVEMENT_APPROVAL_PROC("role_data_movement_to_approval_proc.service"),

	ROLECOMPOSITEACTION_APPROVAL_CREATE("rolecompositeaction_approval.create"),
	ROLECOMPOSITEACTION_APPROVAL_DELETE("rolecompositeaction_approval.delete"),
	ROLECOMPOSITEACTION_APPROVAL_READ("rolecompositeaction_approval.read"),
	ROLECOMPOSITEACTION_APPROVAL_UPDATE("rolecompositeaction_approval.update"),

	ROLEPERMISSION_APPROVAL_CREATE("rolepermission_approval.create"),
	ROLEPERMISSION_APPROVAL_DELETE("rolepermission_approval.delete"),
	ROLEPERMISSION_APPROVAL_READ("rolepermission_approval.read"),
	ROLEPERMISSION_APPROVAL_UPDATE("rolepermission_approval.update"),
	INTERNAL_ROLE_TO_SERVICEDEFINITION_MAPPING_APPROVAL_VIEW_READ("internal_role_to_serviceDefinition_mapping_view_approval.read"),
    ROLEUSER_VIEW_APPROVAL_READ("roleuser_view_approval.read"),
    ROLEPERMISSION_VIEW_APPROVAL_READ("rolepermission_view_approval.read"),
	ROLEPERMISSIONUPDATE_APPROVAL_PROC("rolePermissionUpdate_Approval_proc.service"),

	METERING_MS_JSON_PUSH_METRICS("MeteringMSJson.pushMetricToMeteringStore");
	

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	private static final Properties PROPS = loadProps();
	private static final String SCHEMA_NAME_PLACE_HOLDER = "{schema_name}";

	private String serviceURLKey;

	private ServiceURLEnum(String serviceURLKey) {
		this.serviceURLKey = serviceURLKey;
	}

	private static Properties loadProps() {
		Properties properties = new Properties();
		try (InputStream serviceConfigInputStream = ServiceURLEnum.class.getClassLoader()
				.getResourceAsStream("ServiceURLCollection.properties");) {
			properties.load(serviceConfigInputStream);
		} catch (Exception e) {
			alert.prepareError("Error occured while loading ServiceURLCollection.properties", e).log();
		}
		return properties;
	}

	public static String getValue(String key) {
		return PROPS.getProperty(key);
	}

	private static String getHostURL(DataControllerRequest dataControllerRequest) {
		if (dataControllerRequest == null) {
			return null;
		}
		String hostURL = EnvironmentConfiguration.AC_HOST_URL.getValue(dataControllerRequest);
		diagnostic.prepareDebug("Resolved Host Header value: " + hostURL).log();
		return hostURL;
	}

	private static String getHostURL(ServicesManager servicesManager) {
		if (servicesManager == null) {
			return null;
		}
		String hostURL = EnvironmentConfiguration.AC_HOST_URL.getValue(servicesManager);
		diagnostic.prepareDebug("Resolved Host Header value: " + hostURL).log();
		return hostURL;
	}

	public String getServiceURL(DataControllerRequest dataControllerRequest) {
		String baseURL = getHostURL(dataControllerRequest);
		if (StringUtils.isBlank(baseURL)) {
			return null;
		}
		StringBuilder builder = new StringBuilder(baseURL);
		builder.append(getValue(serviceURLKey));
		String serviceURL = builder.toString();
		if (serviceURL.contains(SCHEMA_NAME_PLACE_HOLDER)) {
			serviceURL = serviceURL.replace(SCHEMA_NAME_PLACE_HOLDER, getDatabaseSchemaName(dataControllerRequest));
		}
		return serviceURL;
	}

	public String getServiceURL(ServicesManager servicesManager) {
		String baseURL = getHostURL(servicesManager);
		if (StringUtils.isBlank(baseURL)) {
			return null;
		}
		StringBuilder builder = new StringBuilder(baseURL);
		builder.append(getValue(serviceURLKey));
		String serviceURL = builder.toString();
		if (serviceURL.contains(SCHEMA_NAME_PLACE_HOLDER)) {
			serviceURL = serviceURL.replace(SCHEMA_NAME_PLACE_HOLDER, getDatabaseSchemaName(servicesManager));
		}
		return serviceURL;
	}

	public String getServiceURL() {
		return getValue(this.serviceURLKey);
	}

	public String getServiceName(DataControllerRequest dataControllerRequest) {
		String serviceName = null;
		String serviceURL = getValue(serviceURLKey);
		serviceName = serviceURL.substring(serviceURL.indexOf("/", 1) + 1, serviceURL.lastIndexOf("/"));
		return serviceName;
	}

	public String getServiceName() {
		String serviceName = null;
		String serviceURL = getValue(serviceURLKey);
		serviceName = serviceURL.substring(serviceURL.indexOf("/", 1) + 1, serviceURL.lastIndexOf("/"));
		return serviceName;
	}

	public String getOperationName() throws MiddlewareException {
		String operationName = null;
		String serviceURL = getValue(serviceURLKey);
		operationName = serviceURL.substring(serviceURL.lastIndexOf("/") + 1);
		if (operationName.contains(SCHEMA_NAME_PLACE_HOLDER)) {
			String schemaName = ServicesManagerHelper.getServicesManager().getConfigurableParametersHelper()
					.getServerProperty(EnvironmentConfiguration.DBX_SCHEMA_NAME.name());
			operationName = operationName.replace(SCHEMA_NAME_PLACE_HOLDER, schemaName);
		}
		return operationName;
	}

	public String getOperationName(DataControllerRequest dataControllerRequest) {
		String operationName = null;
		String serviceURL = getValue(serviceURLKey);
		operationName = serviceURL.substring(serviceURL.lastIndexOf("/") + 1);
		if (operationName.contains(SCHEMA_NAME_PLACE_HOLDER)) {
			operationName = operationName.replace(SCHEMA_NAME_PLACE_HOLDER,
					getDatabaseSchemaName(dataControllerRequest));

		}
		return operationName;
	}

	public String getDatabaseSchemaName(DataControllerRequest requestInstance) {
		return EnvironmentConfiguration.DBX_SCHEMA_NAME.getValue(requestInstance);
	}

	public String getDatabaseSchemaName(ServicesManager servicesManager) {
		return EnvironmentConfiguration.DBX_SCHEMA_NAME.getValue(servicesManager);
	}
}