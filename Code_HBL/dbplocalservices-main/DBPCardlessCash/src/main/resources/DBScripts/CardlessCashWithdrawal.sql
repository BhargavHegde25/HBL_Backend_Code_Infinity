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

