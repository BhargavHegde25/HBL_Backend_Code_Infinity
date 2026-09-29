Work around made for enrollment in HBL environment
---------------------------------------------------

1.	Updated Company legal unit(NP0010001) record in below two tables: 
	dbxdb.financialinstitution;
	dbxdb.financialinstitutionaltkey;
	
2. SET SQL_SAFE_UPDATES = 0;
	set foreign_key_checks = 0;
	update dbxdb.role set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.rolepermission set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.rolecompositepermission set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.userroleservicedefinition set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.rolecompositeaction set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.customeraddress set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.customercommunication set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.customer set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.customerpreference set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.customeraction set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.customergroup set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.customeraccounts set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.customerbusinesstype set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.address set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.region set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.city set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.country set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.contract set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.contractcorecustomers set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.contractcustomers set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.contractactionlimit set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.contractaccounts set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.contractfeatures set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.contractcommunication set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.contractaddress set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.featuredisplaynamedescription set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.actiondisplaynamedescription set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.featureaction set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.featureactionroletype set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.actionlimit set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.feature set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.accounttype set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.accounts set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.servicedefinition set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.actionlimit set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.servicedefinitionactionlimit set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.groupservicedefinition set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.groupactionlimit set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.membergrouptype set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.membergroup set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.mfa set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.mfatype set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.mfaconfigurations set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.backendidentifier set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.dependentactions set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.businessconfiguration set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.accesspolicy set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.limitgroup set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.limitgroupdisplaynamedescription set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.configurations set companyLegalUnit='NP0010001' where companyLegalUnit='GB0010001';
	update dbxdb.accountlevelactionlimit set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.accountsstatementfiles set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.actionlevel set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.app set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.application set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.attributeoption set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.billpaypayee set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.billpaytransfers set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.bulkpaymentrequest set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.bulkpaymentrequestpos set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.bulkpaymenttemplate set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.bulkpaymenttemplatepos set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.card set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.communicationtemplate set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.compositeaction set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.configurationbundles set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.configurationmasters set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.contractcustomrole set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.customeralertcategorychannel set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.customeralertswitch set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.customerdevice set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.customerlimitgrouplimits set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.customerrequest set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.customerservice set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.customerviewalertconfiguration set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.customroleaccounts set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.customroleactionlimits set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.dbxcustomeralertentitlement set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.eligibilitycriteria set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.eventsubtype set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.eventtype set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.excludedcontractaccounts set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.excludedcustomeraccounts set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.excludedcustomeraction set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.excludedcustomroleaccounts set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.excludedcustomroleactionlimits set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.facility set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.faqs set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.featureroletype set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.frequencytype set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.interbankfundtransfers set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.interbankpayee set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.internalusermanager set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.internalusertype set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.internationalfundtransfers set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.internationalpayee set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.intrabankpayee set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.intrabanktransfers set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.location set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.locationfacility set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.locationfile set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.locationservice set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.locationtype set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.logview set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.media set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.membership set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.membershiprelation set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.messageattachment set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.messagetemplate set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.mfakey set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.mfavariablereference set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.outagemessage set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.outagemessageapp set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.ownaccounttransfers set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.p2ppayee set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.p2ptransfers set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.permission set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.permissionlegalentity set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.policycontent set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.privacypolicy set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.recentcurrencies set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.requestcategory set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.requestmessage set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.role_approval set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.rolecompositeaction_approval set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.rolepermission_approval set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.roletype set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.service set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.servicecommunication set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.suspendedcustomers set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.systemuser set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.termandcondition set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.termandconditionapp set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.termandconditiontext set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.transaction set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.useraddress set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.usercompositeaction set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.userlegalentity set legalEntityId ='NP0010001' where legalEntityId = 'GB0010001';
	update dbxdb.userlob set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.usernamerules set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.userpermission set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.userrole set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.userrole_approval set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.userrolecustomerrole set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.userroleservicedefinition_approval set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.usertype set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';
	update dbxdb.makercheckerconfig set companyLegalUnit ='NP0010001' where companyLegalUnit = 'GB0010001';

	select companyLegalUnit from dbxdb.alertrecipienttype;
	UPDATE dbxdb.alertrecipienttype SET  companyLegalUnit='NP0010001' where companyLegalUnit ='ALL';

	select companyLegalUnit from dbxdb.communicationtemplate where companyLegalUnit ='ALL';
	update dbxdb.communicationtemplate set companyLegalUnit = 'NP0010001' where companyLegalUnit ='ALL';

	select companyLegalUnit from dbxdb.dbxcustomeralertentitlement where companyLegalUnit ='ALL';
	update dbxdb.dbxcustomeralertentitlement set companyLegalUnit = 'NP0010001' where companyLegalUnit ='ALL';

	select companyLegalUnit from dbxdb.customerviewalertconfiguration where companyLegalUnit ='ALL';
	update dbxdb.customerviewalertconfiguration set companyLegalUnit = 'NP0010001' where companyLegalUnit ='ALL';

	select companyLegalUnit from dbxdb.customeralertswitch where companyLegalUnit ='ALL';
	update dbxdb.customeralertswitch set companyLegalUnit = 'NP0010001' where companyLegalUnit = 'ALL';

	SET SQL_SAFE_UPDATES = 1;
	set foreign_key_checks = 1;


3. UPDATE dbxdb.configurations SET  config_value='[{"id":"NP0010001","companyName":"HBL","region":"Nepal","typeId":"LEGALENTITY","parentId":"GR23698574","countryCode":"NP","baseCurrency":"NPR","language":"NP","effectiveDate":"1990-09-20","closeDate":"2045-09-20","description":"LEforNepal"}]' WHERE configuration_id='110';

4. Added new preprocessor class which is part of hblservices jar ((integration: searchCoreCustomers, fabric app: authentication)
5. Integration service output map changed for communication email & phone (integration: searchCoreCustomers, fabric app: authentication)
6. Added new account types of HBL in spotlight under below path:
	Configuration -> system configuration -> ACCOUNT_TYPES
	
	--- New Account Types------
	"HIMAL.SAVINGS":"Savings",
   "100.SPECIAL":"Deposit",
   "200.SPECIAL":"Deposit",
   "CALL.DEPOSITS":"Deposit",
   "AGRICULTURE":"Loan",
   "ANNUITY":"Loan",
   "BILLS.LC":"Loan",
   "BISHESH.SAVINGS":"Savings",
   "COMMERCIAL":"Loan",
   "COMM.FARM.LN":"Loan",
   "CONTINGENCY":"Loan",
   "CURRENT.ACCT":"Checking",
   "DALIT.COM.LN":"Loan",
   "KARNALI":"Savings",
   "DEMAND":"Loan",
   "DEPOSIT.1TO5YRS":"Deposit",
   "DEPOSIT.3MONTHS":"Deposit",
   "DEPOSIT.6MONTHS":"Deposit",
   "DEPOSIT.9MONTHS":"Deposit",
   "DOBBAR.MUDDATI":"Deposit",
   "EDUCATION.INST.FIN":"Loan",
   "EDUCATION.LOAN":"Loan",
   "EQRELIEF":"EQ Relief accounts",
   "EXCLUSIVE":"Savings",
   "FAMILY.SAVINGS":"Savings",
   "FCY.DEP3MTO1YR":"Deposit",
   "FCY.DEP6MTO1YR":"Deposit",
   "FIXED.NATURE":"Loan",
   "FIL.TYPE1":"FTL Type 1",
   "FIL.TYPE2":"FTL Type 2",
   "NARI.BACHAT":"Savings",
   "HIGHER.TECH":"Loan",
   "HIMAL.REMIT.DEP":"Deposit",
   "HIMAL.REMIT":"Savings",
   "HIMAL.SAVINGS":"Savings",
   "HIRE.PUR":"Hire Purchase",
   "HMLN.FIXED":"Loan",
   "HOME":"Loan",
   "HOME.EQVM.LN":"Loan",
   "HOMELN":"Loan",
   "HOME.LOAN":"Loan",
   "HOME.LNMG":"Loan",
   "HP.RETAIL.LN":"Loan",
   "INST.DEPOSIT.1TO5YRS":"Deposit",
   "INST.DEPOSIT.6MONTHS":"Deposit",
   "INST.DEPOSIT.9MONTHS":"Deposit",
   "INST.STRUCTURED":"Loan",
   "SURAKCHYA":"Deposit",
   "LCY.SAVINGS.NORMAL":"Savings",
   "LOAN.FDR":"Loan",
   "LOAN.BOND":"Loan",
   "LOAN.DEB":"Loan",
   "PAHILO.BACHAT":"Savings",
   "NONREVOLVING":"Loan",
   "PERS.MORTGAGE.LN":"Loan",
   "PLEDGE":"Loan",
   "PLUS.SAVINGS":"Savings",
   "PMLNR.FIXED":"Loan",
   "POST.SHIPMENT":"Loan",
   "PRE.EXPORT":"Loan",
   "PREMIUM.SAVINGS":"Savings",
   "PROF.EDUCATION.LN":"Loan",
   "PROFESSIONAL.LN":"Loan",
   "PROJ.FIN":"Loan",
   "RD.3YEARS":"Deposit",
   "RD.SIX":"Deposit",
   "RETAIL":"Savings",
   "REVOLVING":"Savings",
   "SHAREHOLDER.SAVING":"Savings",
   "SHORT.TERM":"Loan",
   "SHORT.TM.OC":"Loan",
   "SOCIAL.SECURITY":"Savings",
   "SPECIAL.PAYROLL":"Savings",
   "SPECIAL.PAYROLL.XTRAINT":"Savings",
   "SAVINGS.STAFF":"Savings",
   "STRUCTURED":"Deposit",
   "SUBIDHA":"Savings",
   "SUBSIDY.LN1":"Loan",
   "SUBSIDY.LN2":"Loan",
   "SUBSIDY.LN3":"Loan",
   "SUPER.PLUS":"Savings",
   "SUPER.PREMIUM":"Savings",
   "TRUST.RECEIPT.COMM":"Loan",
   "TRUST.RECEIPT.IND":"Loan",
   "VEHICLE":"Loan",
   "WHOLESALE":"Loan",
   "WOMEN.ENT.LN":"Loan",
   "WORK.CAP":"Loan",
   "WORKING.CAPITAL":"Loan",
   "YOUTH.RTN.LN":"Loan"

7. update below tables as servicedefinations responses is getting empty due to companyLegalUnit is ALL
	
	UPDATE `dbxdb`.`servicedefinition` SET `companyLegalUnit` = 'NP0010001' WHERE companyLegalUnit='ALL';
    UPDATE `dbxdb`.`groupservicedefinition` SET `companyLegalUnit` = 'NP0010001' WHERE companyLegalUnit='ALL';