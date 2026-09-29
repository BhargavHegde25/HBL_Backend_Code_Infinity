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
