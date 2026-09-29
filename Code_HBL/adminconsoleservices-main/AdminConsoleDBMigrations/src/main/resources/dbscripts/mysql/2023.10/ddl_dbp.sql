ALTER TABLE dbxdb.accountsstatementfiles ADD inputPayload nvarchar(500); 
INSERT INTO dbxdb.eventtopicconfiguration (eventCode, topic) values ('ADHOC_STATEMENT','/events/adhocstatement'); 
ALTER TABLE dbxdb.accountsstatementfiles ADD statementType nvarchar(10) DEFAULT 'COMBINED'; 

ALTER TABLE dbxdb.externalaccount ADD COLUMN isApproved VARCHAR(1) DEFAULT "1";

DROP PROCEDURE IF EXISTS `GetExternalPayeesProc`;

DELIMITER $$

CREATE PROCEDURE `GetExternalPayeesProc`(IN userId VARCHAR(500) CHARACTER SET UTF8 COLLATE utf8_general_ci,IN legalEntityId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
IF legalEntityId != "" THEN
SELECT *, (CASE WHEN EXISTS(SELECT `requestId` FROM `bbrequest` WHERE `transactionId` = ea.Id AND `status` = 'Pending') > 0 THEN 'Pending' ELSE NULL END) AS payeeRequestStatus from internationalpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isInternationalAccount = 1 and softDelete = 0 and isApproved=1 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1) and ip.legalEntityId = legalEntityId
UNION
SELECT *, (CASE WHEN EXISTS(SELECT `requestId` FROM `bbrequest` WHERE `transactionId` = ea.Id AND `status` = 'Pending') > 0 THEN 'Pending' ELSE NULL END) AS payeeRequestStatus from interbankpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isInternationalAccount = 0 and isSameBankAccount = 0 and softDelete = 0 and isApproved=1 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1) and ip.legalEntityId = legalEntityId
UNION
SELECT *, (CASE WHEN EXISTS(SELECT `requestId` FROM `bbrequest` WHERE `transactionId` = ea.Id AND `status` = 'Pending') > 0 THEN 'Pending' ELSE NULL END) AS payeeRequestStatus from intrabankpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isSameBankAccount = 1  and softDelete = 0 and isApproved=1 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTRA_BANK_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1) and ip.legalEntityId = legalEntityId;
ELSE
SELECT *, (CASE WHEN EXISTS(SELECT `requestId` FROM `bbrequest` WHERE `transactionId` = ea.Id AND `status` = 'Pending') > 0 THEN 'Pending' ELSE NULL END) AS payeeRequestStatus from internationalpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isInternationalAccount = 1 and softDelete = 0 and isApproved=1 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1)
UNION
SELECT *, (CASE WHEN EXISTS(SELECT `requestId` FROM `bbrequest` WHERE `transactionId` = ea.Id AND `status` = 'Pending') > 0 THEN 'Pending' ELSE NULL END) AS payeeRequestStatus  from interbankpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isInternationalAccount = 0 and isSameBankAccount = 0 and softDelete = 0 and isApproved=1 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1)
UNION
SELECT *, (CASE WHEN EXISTS(SELECT `requestId` FROM `bbrequest` WHERE `transactionId` = ea.Id AND `status` = 'Pending') > 0 THEN 'Pending' ELSE NULL END) AS payeeRequestStatus  from intrabankpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isSameBankAccount = 1  and softDelete = 0 and isApproved=1 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTRA_BANK_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1);
END IF;

END$$
DELIMITER;



DROP PROCEDURE IF EXISTS `fetch_pending_approvalsqueue_proc`;
DELIMITER $$
CREATE  PROCEDURE `fetch_pending_approvalsqueue_proc`(IN _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                             IN _transactionIds varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                             IN _requestIds varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
											 IN _featureactionlist text CHARACTER SET UTF8 COLLATE utf8_general_ci)
MAINLABEL:BEGIN
        SET SESSION group_concat_max_len = 100000000;

        SET @combinedIds = (select group_concat(id SEPARATOR ',') from customer where combinedUserId = _customerId);

		IF @combinedIds is NULL THEN
			SET @combinedIds = _customerId;
		ELSE
			SET @combinedIds = concat(_customerId , ',' ,@combinedIds);
        END IF;

        SET @combinedIds = if(@combinedIds = '' OR @combinedIds = NULL, '\'\'', @combinedIds);

        SET _transactionIds = if(_transactionIds = '' OR _transactionIds = NULL, '\'\'', _transactionIds);
        SET _requestIds = if(_requestIds = '' OR _requestIds = NULL, '\'\'', _requestIds);

        SET @companyId = (select group_concat(concat(contractId,'_',coreCustomerId) SEPARATOR ',') from contractcustomers where customerId =_customerId);
        IF @companyId is NULL THEN
			SET @companyId = '';
		END IF;

        IF _featureactionlist is NULL THEN
			LEAVE MAINLABEL;
		END IF;

        SET @customerMatrixIds = (SELECT group_concat(approvalMatrixId SEPARATOR ',') FROM customerapprovalmatrix WHERE FIND_IN_SET(customerId, @combinedIds));
        IF @customerMatrixIds is NULL THEN
			SET @customerMatrixIds = '';
		END IF;

        SET @customerGroupIds = (SELECT group_concat(signatoryGroupId SEPARATOR ',') FROM customersignatorygroup WHERE FIND_IN_SET(customerId, @combinedIds));
        IF @customerGroupIds is NULL THEN
			SET @customerGroupIds = '';
		END IF;

        SET @alreadyApprovedIds = (select GROUP_CONCAT(DISTINCT(requestId) SEPARATOR ',') from bbactedrequest WHERE FIND_IN_SET(createdby, @combinedIds) AND NOT action = 'Pending' AND softdeleteflag = 0);
        IF @alreadyApprovedIds is NULL THEN
			SET @alreadyApprovedIds = '\'\'';
		END IF;

        SET @approvalRequestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ',') FROM requestapprovalmatrix
        	INNER JOIN approvalmatrix ON requestapprovalmatrix.approvalMatrixId = approvalmatrix.id
			INNER JOIN approvalrule ON approvalmatrix.approvalruleId = approvalrule.id
	        WHERE requestapprovalmatrix.isGroupRule = 0
                AND FIND_IN_SET(requestapprovalmatrix.approvalMatrixId,  @customerMatrixIds)
	        	AND NOT FIND_IN_SET(requestapprovalmatrix.requestId, @alreadyApprovedIds)
	        	AND ((approvalrule.numberOfApprovals = -1 AND requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId))
					OR
					(approvalrule.numberOfApprovals != -1 AND requestapprovalmatrix.receivedApprovals < approvalrule.numberOfApprovals))
		);

		IF @approvalRequestIds is NULL THEN
			SET @approvalRequestIds = '\'\'';
		END IF;

        SET @groupIds = @customerGroupIds;
        GROUPREQUESTS_EXTRACT: LOOP
			SET @strLen = LENGTH(@groupIds);
				SET @requestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ',') FROM signatorygrouprequestmatrix
					WHERE NOT FIND_IN_SET(requestId, @approvalRequestIds) AND isApproved = '0' AND FIND_IN_SET( SUBSTRING_INDEX(@groupIds, ',', 1) ,REPLACE(REPLACE(REPLACE(pendingGroupList,'[',''),']',''),' ','')) > 0
                    AND requestId NOT in (@alreadyApprovedIds) );
				IF @requestIds is NULL THEN
					SET @requestIds = '\'\'';
				END IF;
				SET @approvalRequestIds = if(@approvalRequestIds = '' OR @approvalRequestIds IS NULL, @requestIds, CONCAT(@approvalRequestIds, CONCAT(',',@requestIds) ));
			SET @SubStrLen = LENGTH(SUBSTRING_INDEX(@groupIds, ',', 1));
			SET @groupIds = MID(@groupIds, @SubStrLen + 2, @strLen);
			IF LENGTH(@groupIds) <= 0 THEN
			  LEAVE GROUPREQUESTS_EXTRACT;
			END IF;
		END LOOP GROUPREQUESTS_EXTRACT;

        IF @approvalRequestIds is NULL THEN
			SET @approvalRequestIds = '\'\'';
		END IF;

        SET @features = (SELECT group_concat(Feature_id SEPARATOR ',') FROM featureaction WHERE FIND_IN_SET(id,_featureactionlist) > 0);
        IF @features is NULL THEN
			SET @features = '';
		END IF;

        SET @monetaryActions = (SELECT group_concat(id SEPARATOR ',') FROM featureaction WHERE FIND_IN_SET(Feature_id, @features) > 0);
        IF @monetaryActions is NULL THEN
			SET @monetaryActions = '';
		END IF;

		SET @companyRequestIds = (SELECT group_concat(requestId SEPARATOR ',') FROM bbrequest WHERE FIND_IN_SET(companyId, @companyId) AND FIND_IN_SET(bbrequest.featureActionId, @monetaryActions));

        IF @companyRequestIds is NULL THEN
            SET @companyRequestIds = '';
        END IF;

        SET @requestIds = if(_requestIds = '\'\'',@companyRequestIds,_requestIds);
        SET @query = if(_transactionIds = '\'\''
	        ,concat('FIND_IN_SET(bbrequest.requestId, \'',@requestIds,'\') ')
	        ,concat('FIND_IN_SET(bbrequest.transactionId, \'',_transactionIds, '\') AND  FIND_IN_SET(bbrequest.featureActionId, \'',@monetaryActions,'\')') );

		SET @select_statement = concat('SELECT
					bbrequest.requestId,
                    bbrequest.assocRequestId,
					bbrequest.transactionId,
					bbrequest.status,
					bbrequest.featureActionId,
                    bbrequest.isGroupMatrix,
                    bbrequest.companyId,
					bbrequest.accountId,
                    bbrequest.additionalMeta,
                    bbrequest.companyLegalUnit,
                    (CASE
						WHEN `bbrequest`.`createdby` IN (',@combinedIds,') THEN \'true\'
						ELSE \'false\'
					 END) as `amICreator`,
					(CASE
						WHEN  bbrequest.requestId IN (',@approvalRequestIds,') THEN \'true\'
						ELSE \'false\'
					END)
					 as `amIApprover`,
					(CASE
						WHEN `bbrequest`.`requestId` IN (',@alreadyApprovedIds,') THEN \'true\'
						ELSE \'false\'
					 END) as `actedByMeAlready`,
					(select count(DISTINCT(createdby)) from bbactedrequest where bbactedrequest.action = \'Approved\' AND  bbactedrequest.requestId = bbrequest.requestId AND softdeleteflag = \'0\')
							as receivedApprovals,
					CASE bbrequest.isGroupMatrix
						WHEN 0 THEN LEAST(
							(SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId in (SELECT approvalMatrixId FROM requestapprovalmatrix where requestapprovalmatrix.requestId = bbrequest.requestId))
							,
							SUM(
								CASE approvalrule.numberOfApprovals
									WHEN -1 THEN (SELECT COUNT(*) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
									WHEN NULL OR \'\' THEN 0
									ELSE approvalrule.numberOfApprovals
								END
							)
						)
                        ELSE NULL
					END as requiredApprovals
				FROM bbrequest
				LEFT JOIN requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE ',@query,'
				GROUP BY bbrequest.assocRequestId'
				);

	PREPARE stmt FROM @select_statement;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END$$
DELIMITER;