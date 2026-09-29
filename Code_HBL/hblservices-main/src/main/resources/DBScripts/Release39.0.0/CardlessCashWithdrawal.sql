========================================= Cardless Cash Withdrawal =====================================================================================================================
/* 
Email Templates - CardlessCash Self Withdrawal
*/ 
INSERT INTO `dbxdb`.`emailtemplates` (`TemplateName`, `TemplateText`, `Subject`, `SenderName`, `SenderEmail`) VALUES ('Cardless_Cash_Transaction', '<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cardless Cash Transaction</title>
</head>
<body style="font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #000;">
    
    <p>
        Dear <strong>%firstName% %lastName%</strong>,
    </p>

    <p>
        Your Cardless Cash Transaction No is 
        <strong>%transactionId%</strong>, dt
        <strong>%transactionDate%</strong>.
    </p>

    <p>
        To withdraw cash from the ATM, please enter the Withdrawal Code - <strong>%withdrawalCode%</strong> and self-generated 4-digit Secure Code.
    </p>

    <p>
        <strong>Please don’t share the code with anyone.</strong>
    </p>

    <p>
        Regards,<br>
        Himalayan Bank Limited
    </p>

</body>
</html>
', 'Cardless Cash Transaction', 'Himalayan Bank', 'dbx_cl@infinity.com');

/* CardlessCash Withdrawal - Others */
INSERT INTO `dbxdb`.`emailtemplates` (`TemplateName`, `TemplateText`, `Subject`, `SenderName`, `SenderEmail`) VALUES ('Cardless_Cash_Transaction_Others', '<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cardless Cash Received</title>
</head>
<body style="font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #000;">

    <p>
        Dear <strong>%receiverName%</strong>,
    </p>

    <p>
        You have received <strong>NPR %amount%</strong> from
        <strong>%senderFirstName% %senderLastName%</strong>.
    </p>

    <p>
        To withdraw cash from ATM, please enter the
        <strong>Withdrawal Code – %withdrawalCode%</strong>
        and <strong>4-digit Secure Code</strong> shared by the sender.
    </p>

    <p>
        <strong>Please do not share the code with anyone.</strong>
    </p>

    <p>
        Regards,<br>
        Himalayan Bank Limited
    </p>

</body>
</html>
', 'Cardless Cash Received', 'Himalayan Bank', 'dbx_cl@infinity.com');
======================================================================================================================================================================================================================
/* 
Created a table - cardless_transaction_audit_log - To store audit logs for ATM switch transactions.
*/ 
CREATE TABLE dbxdb.cardless_transaction_audit_log (
  id BIGINT NOT NULL AUTO_INCREMENT,
  validationRequest LONGTEXT,
  validationResult LONGTEXT,
  switch_request_payload LONGTEXT,
  status VARCHAR(20) DEFAULT NULL,
  response_code VARCHAR(20) DEFAULT NULL,
  response_message VARCHAR(255) DEFAULT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP 
        ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB 
AUTO_INCREMENT=721 
DEFAULT CHARSET=utf8mb4 
COLLATE=utf8mb4_0900_ai_ci;

======================================================================================================================================================================================================================
/*
Alter the cardless_transaction_audit_log table to add additional audit logging fields for authentication validation and request header tracking.
*/

ALTER TABLE dbxdb.cardless_transaction_audit_log
ADD COLUMN isAuthValidationEnabled BOOLEAN DEFAULT NULL,
ADD COLUMN requestHeaders LONGTEXT,
ADD COLUMN authResult LONGTEXT;

======================================================================================================================================================================================================================