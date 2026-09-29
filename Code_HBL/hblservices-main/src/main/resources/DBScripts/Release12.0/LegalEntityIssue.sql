<-- Legal entityt issue in Roles level while updating permissions. deleted below two records and will be added if required -->

DELETE FROM `dbxdb`.`financialinstitutionaltkey` WHERE (`alternateName` = 'coreBankingSystemId') and (`alternateKey` = 'GB0010001');
DELETE FROM `dbxdb`.`financialinstitution` WHERE (`finInstitutionId` = 'LEF6318FFEBD1D42F0A6B42E6D192281B6');
