DROP PROCEDURE IF EXISTS `approvalmatrix_signatorygroupmatrixcreate_proc`;
DELIMITER $$                    
CREATE PROCEDURE `approvalmatrix_signatorygroupmatrixcreate_proc`(
IN _matrixValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _signatorymatrixValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	DECLARE index1 INTEGER DEFAULT 0;
	DECLARE index2 INTEGER DEFAULT 0;
	set @length = LENGTH(_matrixValues) - LENGTH(REPLACE(_matrixValues, ',', '')) + 1;
	getValues: LOOP
			set index1 = index1 + 1;
			IF index1 = @length + 1 THEN 
				LEAVE getValues;
			else
				set @matrixRecord = SUBSTRING_INDEX( SUBSTRING_INDEX(_matrixValues, ',', index1), ',', -1 );
				set @matrixComma = REPLACE(@matrixRecord, ';', ',');
				set @query = concat('INSERT INTO approvalmatrix(name,contractId,coreCustomerId,actionId,accountId,approvalruleId,isGroupMatrix,limitTypeId,lowerlimit,upperlimit) VALUES (',@matrixComma,');');
				prepare sql_query from @query;
				execute sql_query;
				
				set @sigValues = SUBSTRING_INDEX( SUBSTRING_INDEX(_signatorymatrixValues, '#', index1), '#', -1 );
				SET @id = LAST_INSERT_ID();
				SET @groupList = SUBSTRING_INDEX(@sigValues, ';', 1 );
				SET @groupRule = SUBSTRING_INDEX(@sigValues, ';', -1 );
				INSERT INTO signatorygroupmatrix(approvalMatrixId, groupList, groupRule) values (@id, @groupList, @groupRule);
                
				ITERATE  getValues;
			END IF;
	END LOOP getValues;

END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_nogroup_user_details_proc`;
DELIMITER $$    
CREATE PROCEDURE `fetch_nogroup_user_details_proc`(
in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
SELECT DISTINCT
  `customer`.`id` AS `userId`,
  `customer`.`isCombinedUser` AS `isCombinedUser`,
  `customer`.`UserName` AS `userName`,
  `customer`.`FirstName` AS `firstName`,
  `customer`.`LastName` AS `lastName`,
  `membergroup`.`Name` AS `role`
from
  `customer`
LEFT JOIN `customergroup` ON (`customergroup`.`Customer_id` = `customer`.`id`)
LEFT JOIN `membergroup` ON (`membergroup`.`id` = `customergroup`.`Group_id` )
  where 
      find_in_set(`customer`.`id`,_customerId)   AND  `customergroup`.`coreCustomerId` = _coreCustomerId ;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_pending_group_list_proc`;
DELIMITER $$
CREATE PROCEDURE fetch_pending_group_list_proc
(in _requestId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)

BEGIN
	SELECT signatorygroup.signatoryGroupId, signatorygroup.signatoryGroupName  
	FROM signatorygroup 
	WHERE FIND_IN_SET(signatorygroup.signatoryGroupId,
	(SELECT group_concat(REPLACE(REPLACE(REPLACE(REPLACE(pendingGroupList,']',''),'[',''),'"',''), ' ', ''))
		FROM signatorygrouprequestmatrix WHERE 
		signatorygrouprequestmatrix.requestId = _requestId AND signatorygrouprequestmatrix.isApproved = false));
END$$

DELIMITER ;



DROP procedure IF EXISTS `fetch_approvalqueue_proc`;
DELIMITER $$

CREATE PROCEDURE `fetch_approvalqueue_proc`(
	IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _transactionIds VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _requestIds VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _featureactionlist TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINLABEL:BEGIN
        SET SESSION group_concat_max_len = 100000000;
        
        SET @combinedIds = (select group_concat(id SEPARATOR ",") from customer where combinedUserId = _customerId);
		
		IF @combinedIds is NULL THEN      
			SET @combinedIds = _customerId;
		ELSE 
			SET @combinedIds = concat(_customerId , "," ,@combinedIds);
        END IF;

        SET @combinedIds = if(@combinedIds = "" OR @combinedIds = NULL, "''", @combinedIds);
        
        SET _transactionIds = if(_transactionIds = "" OR _transactionIds = NULL, "''", _transactionIds);
        SET _requestIds = if(_requestIds = "" OR _requestIds = NULL, "''", _requestIds);
       
        SET @companyId = (select group_concat(concat(contractId,"_",coreCustomerId) SEPARATOR ",") from contractcustomers where customerId =_customerId);
        IF @companyId is NULL THEN      
			SET @companyId = "";
		END IF;
        
        IF _featureactionlist is NULL THEN      
			LEAVE MAINLABEL;
		END IF;
        
        SET @customerMatrixIds = (SELECT group_concat(approvalMatrixId SEPARATOR ",") FROM customerapprovalmatrix WHERE FIND_IN_SET(customerId, @combinedIds));
        IF @customerMatrixIds is NULL THEN      
			SET @customerMatrixIds = "";
		END IF;
        
        SET @customerGroupIds = (SELECT group_concat(signatoryGroupId SEPARATOR ",") FROM customersignatorygroup WHERE FIND_IN_SET(customerId, @combinedIds));
        IF @customerGroupIds is NULL THEN      
			SET @customerGroupIds = "";
		END IF;
        
        SET @alreadyApprovedIds = (select GROUP_CONCAT(DISTINCT(requestId) SEPARATOR ",") from bbactedrequest WHERE FIND_IN_SET(createdby, @combinedIds) AND NOT action = 'Pending' AND softdeleteflag = 0);
        IF @alreadyApprovedIds is NULL THEN      
			SET @alreadyApprovedIds = "''";
		END IF;
        
        SET @approvalRequestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ",") 
        							FROM requestapprovalmatrix
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
			SET @approvalRequestIds = "''";
		END IF;
		
        SET @groupIds = @customerGroupIds;
        do_this: LOOP
			SET @strLen = LENGTH(@groupIds);
				SET @requestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ",") FROM signatorygrouprequestmatrix 
					WHERE NOT FIND_IN_SET(requestId, @approvalRequestIds) AND isApproved = '0' AND FIND_IN_SET( SUBSTRING_INDEX(@groupIds, ',', 1) ,REPLACE(REPLACE(REPLACE(pendingGroupList,'[',''),']',''),' ','')) > 0  
                    AND requestId NOT in (@alreadyApprovedIds) );
				IF @requestIds is NULL THEN      
					SET @requestIds = "''";
				END IF;
				SET @approvalRequestIds = if(@approvalRequestIds = "" OR @approvalRequestIds IS NULL, @requestIds, CONCAT(@approvalRequestIds, CONCAT(',',@requestIds) ));
			SET @SubStrLen = LENGTH(SUBSTRING_INDEX(@groupIds, ',', 1));
			SET @groupIds = MID(@groupIds, @SubStrLen + 2, @strLen);
			IF LENGTH(@groupIds) <= 0 THEN
			  LEAVE do_this;
			END IF;
		END LOOP do_this;
        
        IF @approvalRequestIds is NULL THEN      
			SET @approvalRequestIds = "''";
		END IF;
        
        SET @features = (SELECT group_concat(Feature_id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(id,_featureactionlist) > 0);
        IF @features is NULL THEN      
			SET @features = "";
		END IF;
        
        SET @monetaryActions = (SELECT group_concat(id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(Feature_id, @features) > 0);
        IF @monetaryActions is NULL THEN      
			SET @monetaryActions = "";
		END IF;
		
		SET @companyRequestIds = (SELECT group_concat(requestId SEPARATOR ",") FROM bbrequest WHERE FIND_IN_SET(companyId, @companyId) AND FIND_IN_SET(bbrequest.featureActionId, @monetaryActions));
        
        IF @companyRequestIds is NULL THEN      
            SET @companyRequestIds = "";
        END IF;
        
        SET @requestIds = if(_requestIds = "''",@companyRequestIds,_requestIds);
        SET @query = if(_transactionIds = "''"
	        ,concat("FIND_IN_SET(bbrequest.requestId, \"",@requestIds,"\") ")
	        ,concat("FIND_IN_SET(bbrequest.transactionId, \"",_transactionIds, "\") AND  FIND_IN_SET(bbrequest.featureActionId, \"",@monetaryActions,"\")") );
        
		SET @select_statement = concat("SELECT 
					bbrequest.requestId,
					bbrequest.transactionId,
					bbrequest.status,
					bbrequest.featureActionId,
                    bbrequest.isGroupMatrix,
                    bbrequest.companyId,
					bbrequest.accountId,
                    (CASE
						WHEN `bbrequest`.`createdby` IN (",@combinedIds,") THEN 'true'
						ELSE 'false'
					 END) as `amICreator`,
					(CASE 
						WHEN  ((bbrequest.requestId IN (",@approvalRequestIds,"))  
                        AND
                        (SELECT EXISTS(select id from customeraction where bbrequest.accountId = customeraction.Account_id and customeraction.Action_id = bbrequest.featureActionId AND customeraction.Customer_id IN (",@combinedIds,") and customeraction.contractId = SUBSTRING_INDEX(bbrequest.companyId, '_', 1) and customeraction.coreCustomerId = SUBSTRING_INDEX(bbrequest.companyId, '_', -1) and bbrequest.isGroupMatrix = 1 and  customeraction.isAllowed = 1 and customeraction.softdeleteflag =0))) 
                        THEN 'true' 
						ELSE 'false'
					END)
					 as `amIApprover`,
					(CASE
						WHEN `bbrequest`.`requestId` IN (",@alreadyApprovedIds,") THEN 'true'
						ELSE 'false'
					 END) as `actedByMeAlready`,
					(select count(DISTINCT(createdby)) from bbactedrequest where bbactedrequest.action = 'Approved' AND  bbactedrequest.requestId = bbrequest.requestId AND softdeleteflag = '0') 
							as receivedApprovals,
					CASE bbrequest.isGroupMatrix
						WHEN 0 THEN LEAST(
									(SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId in (SELECT approvalMatrixId FROM requestapprovalmatrix where requestapprovalmatrix.requestId = bbrequest.requestId)) 
									, 
									SUM(
										CASE approvalrule.numberOfApprovals
											WHEN -1 THEN (SELECT COUNT(*) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
											WHEN NULL OR \"\" THEN 0
											ELSE approvalrule.numberOfApprovals
										END
									) 
								)
                        ELSE NULL
					END as requiredApprovals
				FROM
				 bbrequest
				LEFT JOIN requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE ",@query,"
				GROUP BY bbrequest.requestId"
				);
            
	-- select @select_statement;
	PREPARE stmt FROM @select_statement;
    EXECUTE stmt; 
    DEALLOCATE PREPARE stmt;
END$$
DELIMITER ;

DROP procedure IF EXISTS `fetch_approvalgroups_for_pendingtxn_proc`;

DELIMITER $$
CREATE PROCEDURE `fetch_approvalgroups_for_pendingtxn_proc`()
BEGIN
SELECT signatorygroup.signatoryGroupId
FROM signatorygroup
WHERE FIND_IN_SET(signatorygroup.signatoryGroupId,
(SELECT group_concat(distinct(REPLACE(REPLACE(REPLACE(s.pendingGroupList,']',''),'[',''),'"','')))
FROM signatorygrouprequestmatrix s
inner join bbrequest b on (s.requestId = b.requestId)
WHERE s.isApproved = false and b.status = 'Pending' ));
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_signatorygroup_details_proc`;

DELIMITER $$
CREATE PROCEDURE `fetch_signatorygroup_details_proc`(
  IN _signatoryGroupId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	select signatorygroup.signatoryGroupId as `signatoryGroupId`,
	signatorygroup.signatoryGroupName as `signatoryGroupName`,
	signatorygroup.signatoryGroupDescription as `signatoryGroupDescription`,
	signatorygroup.coreCustomerId as `coreCustomerId`,
	contractcorecustomers.coreCustomerName as `coreCustomerName`,
	signatorygroup.createdts as `createdts`,
	(select CONCAT(cust.FirstName,' ',IFNULL(cust.LastName,'')) from customer cust where cust.id=signatorygroup.createdby) as `createdby`,
	signatorygroup.lastmodifiedts as `lastmodifiedts`,
	customersignatorygroup.customerSignatoryGroupId as `customerSignatoryGroupId`,
	customersignatorygroup.customerId as `customerId`,
	customer.UserName AS `userName`,
     CONCAT(customer.FirstName,
                ' ',
                IFNULL(customer.MiddleName, ''),
                ' ',
                IFNULL(customer.LastName, '')) AS `fullName`,
	membergroup.Name as `customerRole`,
	customersignatorygroup.createdts as `signatoryaddedts`
	  from signatorygroup  
	  left join customersignatorygroup  on signatorygroup.signatoryGroupId=customersignatorygroup.signatoryGroupId
	  left join contractcorecustomers  on contractcorecustomers.coreCustomerId=signatorygroup.coreCustomerId 
	  left join customer on customer.id = customersignatorygroup.customerId
	  left join customergroup on customergroup.Customer_id = customersignatorygroup.customerId and customergroup.coreCustomerId = signatorygroup.coreCustomerId
	  left join membergroup on membergroup.id = customergroup.Group_id
	  where signatorygroup.signatoryGroupId=_signatoryGroupId;  
END$$
DELIMITER ;