DROP procedure IF EXISTS `getRequestApprovers_proc`;

DELIMITER $$
CREATE PROCEDURE `getRequestApprovers_proc`(
	IN _requestId TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _status TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	IF _status IS NULL OR _status = '' THEN
		SET @isGroupMatrix = (select isGroupMatrix from bbrequest where requestId = _requestId);
		IF @isGroupMatrix = '1' THEN
			select _requestId AS requestId,csg.customerId AS approvers, c.FirstName, c.LastName FROM customersignatorygroup as csg 
			LEFT JOIN customer AS c ON (c.id = csg.customerId)
			where FIND_IN_SET(csg.signatoryGroupId,
			(SELECT group_concat(REPLACE(REPLACE(REPLACE(pendingGroupList,']',''),'[',''),'"',''))
				FROM signatorygrouprequestmatrix WHERE 
				signatorygrouprequestmatrix.requestId = _requestId AND signatorygrouprequestmatrix.isApproved = false)); 
        ELSE
            SELECT bb.requestId,cam.customerId AS approvers, c.FirstName, c.LastName FROM bbrequest AS bb JOIN requestapprovalmatrix AS ram JOIN customerapprovalmatrix AS cam JOIN customer AS c WHERE bb.requestId = ram.requestId AND ram.approvalMatrixId = cam.approvalMatrixId AND cam.customerId = c.id AND bb.requestId = _requestId GROUP BY approvers;
		END IF;
	ELSE
		SELECT bb.createdby AS approvers, c.FirstName, c.LastName FROM bbactedrequest AS bb JOIN customer AS c WHERE bb.createdby = c.id AND requestId = _requestId AND status = _status GROUP BY approvers;
	END IF;

END$$

DELIMITER ;
