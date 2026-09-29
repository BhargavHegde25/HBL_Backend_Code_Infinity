package com.kony.adminconsole.utilities;

/**
 * @version 1.0 Contains constants for integration operation Name/ object verb name
 */
public final class OperationName {
    public static final String SCHEMA_NAME = "dbxdb";

    public static final String DB_SERVICEDEFINITIONACTIONLIMIT_CREATE =
            SCHEMA_NAME + "_servicedefinitionactionlimit_create";
    public static final String DB_SERVICEDEFINITIONACTIONLIMIT_UPDATE =
            SCHEMA_NAME + "_servicedefinitionactionlimit_update";
    public static final String DB_SERVICEDEFINITIONACTIONLIMIT_GET = SCHEMA_NAME + "_servicedefinitionactionlimit_get";
	public static final String DB_SERVICEDEFINITION_VIEW_PROC = SCHEMA_NAME + "_servicedefinition_view_proc";
    public static final String DB_SERVICEDEFINITIONACTIONLIMIT_DELETE = SCHEMA_NAME + "_servicedefinitionactionlimit_delete";

    public static final String DB_SERVICEDEFINITION_VIEW_GET = SCHEMA_NAME + "_servicedefinition_view_get";
    public static final String DB_SERVICEDEFINITION_FEATURES_ACTIONS_VIEW_GET =
            SCHEMA_NAME + "_servicedefinition_features_actions_view_get";
    public static final String DB_FEATURE_VIEW_GET = SCHEMA_NAME + "_feature_view_get";
    public static final String DB_BACKENDIDENTIFIER_GET = SCHEMA_NAME + "_backendidentifier_get";

    
    public static final String DB_ORGANISATION_GET = SCHEMA_NAME + "_organisation_get";
    public static final String DB_CONTRACT_GET = SCHEMA_NAME + "_contract_get";
    public static final String DB_CONFIGURATIONS_GET = SCHEMA_NAME + "_configurations_get";
    public static final String DB_MEMBERGROUP_GET = SCHEMA_NAME + "_membergroup_get";
    public static final String DB_ACTIONLIMIT_GET = SCHEMA_NAME + "_actionlimit_get";
    public static final String DB_FEATUREACTIONROLETYPE_GET = SCHEMA_NAME + "_featureactionroletype_get";
    public static final String DB_USERROLESERVICEDEFINITION_GET = SCHEMA_NAME + "_userroleservicedefinition_get";
    public static final String DB_MODEL_GET = SCHEMA_NAME + "_model_get";
    
    public static final String DB_LIMITGROUP_GET = SCHEMA_NAME + "_limitgroup_get";
    public static final String DB_GET_ALL_FEATURES_VIEW_GET = SCHEMA_NAME + "_get_all_features_view_get";
    public static final String DB_LIMITGROUP_UPDATE = SCHEMA_NAME + "_limitgroup_update";
    public static final String DB_FEATURE_UPDATE = SCHEMA_NAME + "_feature_update";
    public static final String DB_FEATUREDISPLAYDESCRIPTION_UPDATE =
            SCHEMA_NAME + "_featuredisplaynamedescription_update";
    public static final String DB_ACTIONDISPLAYDESCRIPTION_UPDATE =
            SCHEMA_NAME + "_actiondisplaynamedescription_update";
    public static final String DB_ACTION_LIMITS_UPDATE_PROC = SCHEMA_NAME + "_feature_action_limits_update_proc";
    public static final String DB_LIMITS_UPDATE_PROC = SCHEMA_NAME + "_limits_update_proc";
    public static final String DB_FEATUREACTION_UPDATE = SCHEMA_NAME + "_featureaction_update";
    public static final String DB_GET_FEATUREACTIONS_VIEW = SCHEMA_NAME + "_get_feature_actions_view_get";
	public static final String DB_GET_FEATUREACTIONS_VIEW_PROC = SCHEMA_NAME + "_get_feature_actions_view_proc";
    public static final String DB_LIMITGROUP_VIEW = SCHEMA_NAME + "_limitgroups_view_get";
    public static final String DB_LIMITGROUPDISPLAYDESCRIPTION_UPDATE =
            SCHEMA_NAME + "_limitgroupdisplaynamedescription_update";
    public static final String DB_SERVICEDEFINITION_CREATE = SCHEMA_NAME + "_servicedefinition_create";
    public static final String DB_SERVICEDEFINITION_GET = SCHEMA_NAME + "_servicedefinition_get";
    public static final String DB_SERVICEDEFINITION_UPDATE = SCHEMA_NAME + "_servicedefinition_update";
    public static final String DB_SERVICEDEFINITION_DELETE = SCHEMA_NAME + "_servicedefinition_delete";
    public static final String DB_SERVICEDEFINITION_DELETE_PROC = SCHEMA_NAME + "_servicedefinition_delete_proc";

    public static final String DB_GROUPACTIONLIMIT_GET = SCHEMA_NAME + "_groupactionlimit_get";
    public static final String DB_GROUPACTIONLIMIT_DELETE = SCHEMA_NAME + "_groupactionlimit_delete";
    public static final String DB_SERVICEDEFINITION_DEFAULTGROUP_UPDATE_PROC =
            SCHEMA_NAME + "_servicedefinition_defaultgroup_update_proc";
    public static final String DB_INFINITY_CUSTOMER_FROM_SERVICEDEFINITION_PROC =
            SCHEMA_NAME + "_infinity_customer_from_servicedefinition_proc";
    public static final String DB_CUSTOMER_GET = SCHEMA_NAME+"_customer_get";
    public static final String DB_USER_SECURITYATTRIBUTES_GET_PROC = SCHEMA_NAME + "_user_securityattributes_get_proc";
    
    public static final String DB_GROUPSERVICEDEFINITION_CREATE = SCHEMA_NAME + "_groupservicedefinition_create";
    public static final String DB_GROUPSERVICEDEFINITION_UPDATE = SCHEMA_NAME + "_groupservicedefinition_update";
    public static final String DB_GROUPSERVICEDEFINITION_GET = SCHEMA_NAME + "_groupservicedefinition_get";
    public static final String DB_GROUPSERVICEDEFINITION_DELETE = SCHEMA_NAME + "_groupservicedefinition_delete";

    public static final String DB_MEMBERGROUP_DELETE = SCHEMA_NAME + "_membergroup_delete";
    public static final String DB_MEMBERGROUP_UPDATE = SCHEMA_NAME + "_membergroup_update";
    public static final String DB_MEMBERGROUP_CREATE = SCHEMA_NAME + "_membergroup_create";

    public static final String DB_GROUP_FEATURES_ACTIONS_VIEW_GET = SCHEMA_NAME + "_group_features_actions_view_get";
	public static final String DB_GROUP_FEATURES_ACTIONS_VIEW_PROC = SCHEMA_NAME + "_group_features_actions_view_proc";
    public static final String DB_GROUPS_VIEW_GET = SCHEMA_NAME + "_groups_view_get";
    public static final String DB_GROUPACTIONLIMIT_CREATE = SCHEMA_NAME + "_groupactionlimit_create";
    public static final String DB_GROUPACTIONLIMIT_UPDATE = SCHEMA_NAME + "_groupactionlimit_update";
    public static final String DB_ACCESSPOLICY_GET = SCHEMA_NAME + "_accesspolicy_get";
    public static final String DB_ACTIONLEVEL_GET = SCHEMA_NAME + "_actionlevel_get";
    public static final String DB_ACTIONDEPENDENCY_VIEW_GET = SCHEMA_NAME + "_actiondependency_view_get";
    public static final String DB_FEATUREACTION_GET = SCHEMA_NAME + "_featureaction_get";
    public static final String DB_FEATURE_GET = SCHEMA_NAME + "_feature_get";
    public static final String DB_APPLICATION_GET = SCHEMA_NAME + "_application_get";
    
    public static final String DB_INTERNALUSERS_FEATURES_VIEW_GET = SCHEMA_NAME + "_internalusers_features_view_get";
    public static final String DB_INTERNALUSERS_ACTIONS_VIEW = SCHEMA_NAME + "_internalusers_actions_view_get";

    public static final String OP_CREATECONTRACT = "createContract";
    public static final String OP_EDITCONTRACT = "editContract";
    public static final String OP_SEARCHCONTRACT = "searchContract";
    public static final String OP_UPDATECONTRACTSTATUS = "updateContractStatus";
    public static final String OP_GETLISTOFCONTRACTSBYSTATUS = "getListOfContractsByStatus";
    public static final String OP_GETCONTRACTDETAILS = "getContractDetails";
    public static final String OP_GETCONTRACTFEATUREACTIONLIMITS = "getContractFeatureActionLimits";
    public static final String OP_SEARCHCORECUSTOMERS = "searchCoreCustomers";
    public static final String OP_GETCORERELATIVECUSTOMERS = "getCoreRelativeCustomers";
    public static final String OP_GETCORECUSTOMERACCOUNTS = "getCoreCustomerAccounts";
    public static final String OP_GETCONTRACTINFINITYUSERS = "getContractInfinityUsers";
    public static final String OP_CREATEINFINITYUSER = "createInfinityUser";
    public static final String OP_EDITINFINITYUSER = "editInfinityUser";
    public static final String OP_GETINFINITYUSER = "getInfinityUser";
    public static final String OP_GETINFINITYUSER_SERVICEDEFS_ROLES = "getInfinityUserServiceDefsRoles";
    public static final String OP_GETASSOCIATEDCUSTOMERS = "getAssociatedCustomers";
    public static final String OP_GETALLELIGIBLERELATIONALCUSTOMERS = "getAllEligibleRelationalCustomers";
    public static final String OP_GETCONTRACTACCOUNTS = "getContractAccounts";
    public static final String OP_GETCORECUSTOMERROLEFEATUREACTIONLIMITS = "getCoreCustomerRoleFeatureActionLimits";
    public static final String OP_GETCORECUSTOMERPRODUCTROLESFEATUREACTIONLIMITS ="getCoreCustomerProductRolesFeatureActionLimits";
    public static final String OP_GETRELATIVECORECUSTOMERCONTRACTDETAILS = "getRelativeCoreCustomerContractDetails";
    public static final String OP_GETCORECUSTOMERCONTRACTDETAILS = "getCoreCustomerContractDetails";
    public static final String DB_CUSTOMERGROUPSVIEW_GET = SCHEMA_NAME + "_customergroups_view_get";
    public static final String OP_UPDATEAPPROVALMATRIXSTATUS = "updateApprovalMatrixStatus";
    public static final String OP_ISAPPROVALMATRIXDISABLED = "isApprovalMatrixDisabled";
    public static final String OP_GETAPPROVALMATRIX = "getApprovalMatrix";
    public static final String OP_GETINFINITYUSERCONTRACTDETAILS = "getInfinityUserContractDetails";
    public static final String OP_GETINFINITYUSERACCOUNTS = "getInfinityUserAccounts";
    public static final String OP_GETINFINITYUSERFEATUREACTIONS = "getInfinityUserFeatureActions";
    public static final String OP_GETINFINITYUSERLIMITS = "getInfinityUserLimits";
    public static final String OP_CREATE_PARTY_USER = "createPartyUser";
    public static final String OP_UPDATE_PARTY_USER = "updatePartyUser";
    public static final String OP_SEARCH_PARTY_USER = "searchPartyUser";
    public static final String OP_GET_COMPANY_LEGAL_UNITS = "getCompanyLegalUnits";
    public static final String OP_PUSH_METRIC_TO_METERING_STORE = "pushMetricToMeteringStore";
    
    
    public static final String OP_CREATE_SG = "createSignatoryGroup";
    public static final String OP_UPDATE_SG = "updateSignatoryGroups";
    public static final String OP_DELETE_SG = "deleteSignatoryGroup";
    public static final String OP_GET_NOGROUP_USERS = "getNoGroupUsers";
    public static final String OP_GET_APPROVALPERMISSION_FOR_USER = "getApprovalPermissionsForUser";
    public static final String OP_GET_ALL_SG_BY_CORECUSTOMER_ID = "getAllSignatoryGroupsbyCoreCustomerIds";
    public static final String OP_GET_ALL_SG = "getAllSignatoryGroups";
    public static final String OP_GET_SG_DETAILS = "getSignatoryGroupDetails";
    public static final String OP_GET_APPROVAL_MATRIX = "getApprovalMatrix";
    public static final String OP_CREATE_APPROVAL_RULE_SG_LEVEL = "createApprovalRuleSGLevel";
    public static final String OP_CREATE_APPROVAL_RULE_USER_LEVEL = "createApprovalRuleUserLevel";
    public static final String OP_GET_APPROVAL_RULE = "getApprovalRules";
    public static final String OP_UPDATE_APPROVAL_RULE_USER_LEVEL = "updateApprovalRuleUserLevel";
    public static final String OP_UPDATE_APPROVAL_RULE_SG_LEVEL = "updateApprovalRuleSGLevel";
    public static final String OP_GET_APPROVERS_IN_SIGNATORY_GROUP = "getApproversInSignatoryGroup";
    public static final String OP_FETCH_APPROVAL_MODE = "fetchApprovalMode";
    public static final String OP_UPDATE_APPROVAL_MODE = "updateApprovalMode";
    public static final String OP_DELETE_APPROVAL_MODE = "deleteApprovalMode";
    public static final String OP_IF_ELIGIBLE_DELETE_SG = "isSignatoryGroupEligibleForDelete";
    public static final String OP_GET_APPROVAL_MATRIX_BY_CONTRACT_ID = "getApprovalMatrixByContractId";
    public static final String OP_GET_APPROVERS_LIST = "getAccountActionCustomerApproverList";
    public static final String OP_GET_CONVERSION_RATE = "getConversionRate";
    public static final String OP_CREATE_FACILITY = "createFacility";
    public static final String OP_UPDATE_FACILITY = "updateFacility";
    public static final String OP_UPDATE_FACILITY_FEATURES = "updateFacilityFeatures";
    public static final String OP_DELETE_FACILITY_FEATURES = "deleteFacilityFeatures";
    public static final String OP_GET_FACILITIES = "getFacilities";
    public static final String OP_GET_FACILITY = "getFacility";
    public static final String OP_GETINFINITYACCOUNTS = "getInfinityAccounts";
    public static final String OP_GETCONVERTEDAMOUNT = "getConvertedAmount";
    public static final String OP_CREATE_FACILITY_FEATURES = "createFacilityFeatures";
    public static final String OP_GET_FACILITY_FEATURES = "getFacilityFeatures";
    public static final String DB_TNC_CREATE = SCHEMA_NAME + "_termandcondition_create";
    public static final String DB_TNC_GET = SCHEMA_NAME + "_termandcondition_get";
    public static final String DB_TNC_UPDATE = SCHEMA_NAME + "_termandcondition_update";
    public static final String DB_TNC_DELETE = SCHEMA_NAME + "_termandcondition_delete";
    public static final String DB_TNCTEXT_CREATE = SCHEMA_NAME + "_termandconditiontext_create";
    public static final String DB_TNCTEXT_GET = SCHEMA_NAME + "_termandconditiontext_get";
    public static final String DB_TNCTEXT_UPDATE = SCHEMA_NAME + "_termandconditiontext_update";
    public static final String DB_TNCTEXT_DELETE = SCHEMA_NAME + "_termandconditiontext_delete";
    public static final String DB_TNCAPP_CREATE = SCHEMA_NAME + "_termandconditionapp_create";
    public static final String DB_TNCAPP_GET = SCHEMA_NAME + "_termandconditionapp_get";
    public static final String DB_TNCAPP_UPDATE = SCHEMA_NAME + "_termandconditionapp_update";
    public static final String DB_TNCAPP_DELETE = SCHEMA_NAME + "_termandconditionapp_delete";
    public static final String DB_LOCALE_GET = SCHEMA_NAME + "_locale_get";
    public static final String DB_CONTENTTYPE_UPDATE = SCHEMA_NAME + "_contenttype_update";
    public static final String DB_CONTENTTYPE_GET = SCHEMA_NAME + "_contenttype_get";
    public static final String DB_APP_GET = SCHEMA_NAME + "_app_get";
    
    public static final String DB_INTERNAL_FEATURE_UPDATE = SCHEMA_NAME + "_internalfeature_update";
    public static final String DB_INTERNAL_FEATUREDISPLAYDESCRIPTION_UPDATE = SCHEMA_NAME + "_internalfeaturedisplaynamedescription_update";
    public static final String DB_INTERNAL_ACTIONDISPLAYDESCRIPTION_UPDATE = SCHEMA_NAME + "_internalactiondisplaynamedescription_update";
    public static final String DB_INTERNALACTIONDEPENDENCY_VIEW_GET = SCHEMA_NAME + "_internalactiondependency_view_get";
    public static final String DB_INTERNALFEATUREACTION_UPDATE = SCHEMA_NAME + "_internalfeatureaction_update";
    public static final String DB_INTERNALFEATUREACTION_GET = SCHEMA_NAME + "_internalfeatureaction_get";
    public static final String DB_INTERNALFEATURE_GET = SCHEMA_NAME + "_internalfeature_get";
    public static final String DB_INTERNALPERMISSION_CREATE = SCHEMA_NAME + "_internalpermission_create";
    public static final String DB_PERMISSIONACTION_CREATE = SCHEMA_NAME + "_permissionaction_create";
    public static final String DB_PERMISSIONLEGALENTITY_CREATE = SCHEMA_NAME + "_permissionlegalentity_create";
    public static final String DB_INTERNALPERMISSION_UPDATE = SCHEMA_NAME + "_internalpermission_update";
    public static final String DB_INTERNAL_PERMISSION_ACTION_DELETE_PROC = SCHEMA_NAME + "_internalpermissionaction_delete_proc";
    public static final String DB_INTERNAL_PERMISSION_ENTITY_DELETE_PROC = SCHEMA_NAME + "_permissionlegalentity_delete_proc";
    public static final String DB_INTERNALPERMISSION_VIEW_GET = SCHEMA_NAME + "_internalpermission_view_get";
    public static final String DB_INTERNALPERMISSION_FEATUREACTION_VIEW_GET = SCHEMA_NAME + "_internalpermission_featureaction_view_get";
    public static final String DB_GET_PRODUCT_LIST = SCHEMA_NAME + "_productinformationview_get";
    public static final String GETALLPRODUCTGROUPS_CAMPAIGN_PROC =  SCHEMA_NAME + "_GetAllProductGroups_Campaign_proc";
    public static final String GETPRODUCTSBYPRODUCTGROUP_PROC =  SCHEMA_NAME + "_getProductsByProductGroup_proc";

    public static final String DB_GET_PRODUCT_LINES = SCHEMA_NAME + "_productLine_get";
    public static final String DB_GET_PRODUCT_GROUPS = SCHEMA_NAME + "_productGroup_get";
    public static final String DB_CREATE_PRODUCT_FACILITY = SCHEMA_NAME + "_productFacility_create";
    public static final String DB_UPDATE_PRODUCT_FACILITY = SCHEMA_NAME + "_productFacility_update";
    public static final String DB_DELETE_PRODUCT_FACILITY = SCHEMA_NAME + "_productFacility_delete";
    
    
	public static final String DB_GET_PRODUCT_DETAILS = SCHEMA_NAME + "_product_details_proc";
    public static final String DB_GET_FACILITIES = SCHEMA_NAME + "_facilities_get_proc";
    public static final String DB_UPDATE_PRODUCT_PROC = SCHEMA_NAME + "_product_update_proc";


    public static final String DB_CREATE_PRODUCT_LINES = SCHEMA_NAME + "_productLine_create";
    public static final String DB_CREATE_PRODUCT_GROUPS = SCHEMA_NAME + "_productGroup_create";
    public static final String DB_CREATE_PRODUCTS = SCHEMA_NAME + "_productInformation_create";
    public static final String DB_CREATE_PRODUCTS_PROC= SCHEMA_NAME +"_insertProductInformationSch";
    public static final String DB_CREATE_PRODUCTFACILITIES_PROC= SCHEMA_NAME +"_insertProductFacilitiesSch";
    public static final String DB_DELETE_MARKETINGDATA_PROC= SCHEMA_NAME +"_delete_marketingdata";


    
    public static final String DB_ROLE_CREATE = SCHEMA_NAME + "_role_create";
    public static final String DB_ROLE_UPDATE = SCHEMA_NAME + "_role_update";
    public static final String DB_ROLE_GET = SCHEMA_NAME + "_role_get";
    public static final String DB_INTERNALROLE_VIEW_GET = SCHEMA_NAME + "_internalrole_view_get";
    public static final String DB_ROLEPERMISSIONOU_CREATE = SCHEMA_NAME + "_rolepermissionou_create";
    public static final String DB_ROLE_PERMISSION_OU_DELETE_PROC = SCHEMA_NAME + "_rolepermissionou_delete_proc";
    
    public static final String OP_CREATE_MASTER_CONSENT = "createMasterConsent";
    public static final String OP_CREATE_PARTY_CONSENT_DETAILS = "createPartyConsentDetails";
    public static final String OP_CREATE_TERMS_AND_CONDITIONS_APP = "createTermsNConditionsApp";
    public static final String OP_CREATE_TERM_AND_CONDITIONS_CODE = "createTermsNConditionsCode";
    public static final String OP_CREATE_TERM_AND_CONDITIONS_CONTENT = "createTermsNConditionsContent";
    public static final String OP_GET_INFINITY_APP = "getInfinityApp";
    public static final String OP_GET_INFINITY_APPS = "getInfinityApps";
    public static final String OP_GET_MASTER_CONSENT = "getMasterConsent";
    public static final String OP_GET_MASTER_CONSENTS = "getMasterConsents";
    public static final String OP_GET_PARTY_CONSENT_DETAILS = "getPartyConsentDetails";
    public static final String OP_GET_TERMS_AND_CONDITIONS_APP = "getTermsNConditionsApp";
    public static final String OP_GET_TERMS_AND_CONDITIONS_APPS = "getTermsNConditionsApps";
    public static final String OP_GET_TERMS_AND_CONDITIONS_CODE = "getTermsNConditionsCode";
    public static final String OP_GET_TERMS_AND_CONDITIONS_CODES = "getTermsNConditionsCodes";
    public static final String OP_GET_TERMS_AND_CONDITIONS_CONTENT = "getTermsNConditonsContent";
    public static final String OP_GET_TERMS_AND_CONDITIONS_CONTENTS = "getTermsNConditonsContents";
    public static final String OP_GET_ACTIVE_TERMS_AND_CONDITIONS_CONSENTS = "getActiveTermsNConditonsContents";
    public static final String OP_UPDATE_PARTY_CONSENT_DETAILS = "updatePartyConsentDetails";
    public static final String OP_UPDATE_TERMS_AND_CONDITIONS_CODE = "updateTermNConditionsCode";
    public static final String OP_UPDATE_TERMS_AND_CONDITIONS_APP = "updateTermsNConditionsApp";
    public static final String OP_DELETE_TERMS_AND_CONDITIONS_CONTENT = "deleteTermsNConditonsContent";
    public static final String OP_CREATE_PRODUCT = "createProduct";
    public static final String OP_CREATE_PRODUCT_FACILITIES = "createProductFacilities";
    public static final String OP_CREATE_PRODUCT_FEATURES = "createProductFeatures";
    public static final String OP_CREATE_PRODUCT_IMAGES = "createProductImage";
    public static final String OP_GET_PRODUCT = "getProduct";
    public static final String OP_GET_PRODUCT_LIST = "getProductList";
    public static final String OP_CREATE_PRODUCT_FACILITY = "createProductFacilities";
    public static final String OP_UPDATE_PRODUCT_FACILITY = "updateProductFacilities";
    public static final String OP_DELETE_PRODUCT_FACILITY = "deleteProductFacility";
    public static final String OP_UPDATE_PRODUCT = "updateProduct";
    public static final String OP_GET_PRODUCT_FEATURES = "getProductFeatures";
    public static final String OP_UPDATE_PRODUCT_FEATURES = "updateProductFeatures";
    public static final String OP_DELETE_PRODUCT_FEATURES = "deleteProductFeatures";
    public static final String OP_GET_PRODUCT_IMAGES = "getProductImages";
    public static final String OP_UPDATE_PRODUCT_IMAGES = "updateProductImages";
    public static final String OP_DELETE_PRODUCT_IMAGES = "deleteProductImage";
    public static final String OP_GET_ACC_FEATURE_DETAILS = "getAccountLevelFeatureAction";
    public static final String OP_GET_ALL__INTERNAL_FEATURE_ACTIONS = "getAllInternalFeatureActions";
    public static final String OP_GET_FINANTIAL_INSTITUTION = "getFinancialInstitutions";
    public static final String OP_GET_PRODUCT_LINES = "GetProductLines";
    public static final String OP_GET_PRODUCT_GROUPS = "GetProductGroups";
    
    public static final String OP_CREATE_ENTITLEMENT_BY_USERID = "createEntitlementByUserId";
    public static final String OP_GET_ENTITLEMENT_BY_USERID = "getEntitlementByUserId";
    public static final String OP_UPDATE_ENTITLEMENT_BY_USERID = "updateEntitlementByUserId";
    public static final String OP_GET_ENTITLEMENT = "getEntitlement";
    public static final String OP_UPDATE_ENTITLEMENT = "updateEntitlement";
    public static final String OP_DELETE_ENTITLEMENT = "deleteEntitlement";
    public static final String OP_ADDNEWFEATURESTOCONTRACTFROMSD = "addNewFeaturesToContractFromSD";
    public static final String OP_GETCORECUSTOMERDETAILS = "getCoreCustomerDetails";
    public static final String OP_GETINFINITYUSERACCOUNTSFORCORECUSTOMER = "getInfinityUserAccountsForCorecustomer";
	public static final String GETSERVICEDEFINITIONPRODUCTIDPERMISSIONS = "getServiceDefinitionProductIdPermissions";
	public static final String GET_PLACEHOLDERS = "getPlaceholders";
	public static final String GET_EVENTS = "getEvents";  
	public static final String UPDATE_PROFILE = "updateProfile";
	
	public static final String LOGIN = "login";
	public static final String DELETE_USER_SESSION = "deleteUserSessions";
	
	public static final String GETUSERLIST = "getUserList";

	public static final String GETPRODUCTLINELIST = "getProductLineList";
	public static final String GETPRODUCTGROUPLIST = "getProductGroupList";
	public static final String GETPRODUCTLIST = "getProductList";
	public static final String GETPRODUCTFACILITIESLIST = "getProductFacilities";
	
    public static final String DB_GET_PRODUCT_IMAGES_GET = SCHEMA_NAME + "_productImage_get";
    public static final String DB_PRODUCT_IMAGES_CREATE = SCHEMA_NAME + "_productImage_create";
    public static final String DB_PRODUCT_IMAGES_EDIT = SCHEMA_NAME + "_productImage_update";
    public static final String DB_PRODUCT_IMAGES_DELETE = SCHEMA_NAME + "_productImage_delete";

    public static final String DB_GET_PRODUCT_FEATURES = SCHEMA_NAME + "_product_features_proc";
    public static final String DB_DELETE_PRODUCT_FEATURES = SCHEMA_NAME + "_product_features_delete_proc";
    
    public static final String DB_PRODUCT_FEATURES_GET = SCHEMA_NAME + "_productFeatures_get";
    public static final String DB_PRODUCT_FEATURES_CREATE = SCHEMA_NAME + "_productFeatures_create";
    public static final String DB_PRODUCT_FEATURES_EDIT = SCHEMA_NAME + "_productFeatures_update";
    public static final String DB_PRODUCT_FEATURES_DELETE = SCHEMA_NAME + "_productFeatures_delete";

    public static final String DB_PRODUCT_FEATURE_ACTIONS_GET = SCHEMA_NAME + "_productFeatureActions_get";
    public static final String DB_PRODUCT_FEATURES_ACTIONS_CREATE = SCHEMA_NAME + "_productFeatureActions_create";
    public static final String DB_PRODUCT_FEATURES_ACTIONS_EDIT = SCHEMA_NAME + "_productFeatureActions_update";
    public static final String DB_PRODUCT_FEATURES_ACTIONS_DELETE = SCHEMA_NAME + "_productFeatureActions_delete";
    

    public static final String CREATE_CAMPAIGN = "createCampaign";
    public static final String UPDATE_CAMPAIGN = "updateCampaign";
    public static final String GET_CAMPAIGNS = "getCampaigns";
    public static final String GET_PRODUCTGROUPS_CAMPAIGN   = "GetProductGroupsForCampaign";
    
    public static final String GET_PRODUCTS_BY_PRODUCTGROUP   = "GetProductsByProductGroupsForPurpose";
    public static final String DB_CREATE_CAMPAIGN_PROC = SCHEMA_NAME + "_create_campaign_proc";
    public static final String DB_UPDATE_CAMPAIGN_PROC = SCHEMA_NAME + "_update_campaign_proc";
    public static final String DB_GET_CAMPAIGNS_PROC = SCHEMA_NAME + "_get_campaign_proc";

    public static final String DB_PLACEHOLDER_GET = SCHEMA_NAME + "_placeholder_get";
    public static final String DB_EVENTTRIGGERS_GET = SCHEMA_NAME + "_eventtriggers_get";
    public static final String DB_FINANCIALINSTITUTION_GET_PROC = SCHEMA_NAME + "_financialinstitution_get_proc";
    public static final String DB_PROFILE_CREATE = SCHEMA_NAME + "_profile_create";
    public static final String DB_PROFILECONDITION_CREATE = SCHEMA_NAME + "_profilecondition_create";
    public static final String DB_PROFILE_GET = SCHEMA_NAME + "_profile_get";
    public static final String DB_DEFAULT_CAMPAIGNS_GET_PROC = SCHEMA_NAME + "_default_campaign_get_proc";
    public static final String DB_DEFAULT_CAMPAIGNS_UPDATE_PROC = SCHEMA_NAME + "_default_campaign_update_proc";
     
    
    public static final String OP_GET_PROFILES = "getProfiles";
    public static final String OP_CREATE_PROFILE = "createProfile";
    public static final String DB_FETCH_PROFILES_PROC = SCHEMA_NAME + "_fetch_all_profiles_proc";
    
    public static final String DB_PROFILE_UPDATE = SCHEMA_NAME + "_profile_update";
    public static final String DB_PROFILECONDITION_UPDATE = SCHEMA_NAME + "_profilecondition_update";
    public static final String DB_PROFILECONDITION_GET = SCHEMA_NAME + "_profilecondition_get";
    public static final String DB_PROFILECONDITION_DELETE = SCHEMA_NAME + "_profilecondition_delete";
    
    public static final String GET_ALL_DEFAULT_CAMPAIGNS = "getAllDefaultCampaigns";
    public static final String UPDATE_DEFAULT_CAMPAIGNS = "updateDefaultCampaigns";
	public static final String DB_UPDATE_PROFILE_USERS_PROC = SCHEMA_NAME + "_update_profile_users_proc";
    	
}
