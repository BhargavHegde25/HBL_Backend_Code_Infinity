create or replace PROCEDURE "customeralertchannel_insertbulk_proc"
(
  "_recordvalues" IN NVARCHAR2,
  "records" out SYS_REFCURSOR
)
AS
   v__recordvalues NVARCHAR2(2000) := "_recordvalues";
   v_query NVARCHAR2(2000);
   v_msg NVARCHAR2(2000) := 'Success';


BEGIN

         IF v__recordvalues IS NOT NULL
           AND v__recordvalues != ' ' THEN

         BEGIN

            v__recordvalues := REPLACE(v__recordvalues, '"', CHR(39)) ;

            v_query := ' insert into  "customeralertchannel" ( "customerId", "alertCategoryId", "alertTypeId", "alertSubTypeId", "accountId", "accountType","channelId", "createdby","companyLegalUnit") values '  || v__recordvalues  ;           

            EXECUTE IMMEDIATE v_query;
            OPEN  "records" FOR SELECT v_msg from dual;
         END;
         END IF;

   EXCEPTION
      WHEN OTHERS THEN

   BEGIN
   v_msg := SQLERRM;
      OPEN  "records" FOR
         SELECT v_msg "errmsg"  
           FROM DUAL  ;


   END;

END;
/


CREATE OR REPLACE PROCEDURE "customeralertchannel_updatebulk_proc"
(
  "_updateRecords" IN NVARCHAR2,
  
  "records" OUT SYS_REFCURSOR
)
AS
   v_index1 NUMBER(10,0) := 0;
   v_numOfRecords NUMBER(10,0) := 0;
   v_rowValues NVARCHAR2(2000);
   v_customer_id NVARCHAR2(50);
   v_alertCategoryId VARCHAR2(50);
   v_alertTypeId NVARCHAR2(50);
   v_alertSubTypeId VARCHAR2(75);
   v_accountId NVARCHAR2(50);
   v_modifiedby NVARCHAR2(50);
   v_accountType NVARCHAR2(50);
   v_channelId NVARCHAR2(50);
   v_query NVARCHAR2(2000);
   v_whereCondition NVARCHAR2(2000);
   v_msg NVARCHAR2(2000);
   --SQLERRM VARCHAR2(2000);
   

BEGIN

   BEGIN
      
      BEGIN
         v_numOfRecords := LENGTH("_updateRecords") - LENGTH(REPLACE("_updateRecords", '|', '')) + 1 ;
         v_index1 := v_index1 + 1 ;
         WHILE ( v_index1 != v_numOfRecords + 1 ) 
         LOOP 
            
            BEGIN
               v_rowValues := SUBSTRING_INDEX(SUBSTRING_INDEX("_updateRecords", '|', v_index1), '|', -1) ;
               v_customer_id := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 1), N'', -1)  ;
               v_alertCategoryId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 2), N',', -1) ;
               v_alertTypeId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 3), N',', -1) ;
               v_alertSubTypeId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 4), N',', -1) ;
               v_accountId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 5), N',', -1) ;
               v_accountType := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 6), N',', -1) ;
               v_channelId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 7), N',', -1) ;
               v_modifiedby := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 8), N',', -1) ;
               v_companyLegalUnit := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', -1), N'', 1)  ;
               v_query := 'update "customeralertchannel" set  "modifiedby" ='|| '''' || v_modifiedby || '''' ;
               v_whereCondition := 'where "customerId" = '|| '''' || v_customer_id || '''' || '  and "alertCategoryId" = '|| '''' || v_alertCategoryId || '''' || '  and "companyLegalUnit" = '|| '''' || v_companyLegalUnit || '''' || '  and "alertTypeId" = '|| '''' || v_alertTypeId || ''''  || '  and "alertSubTypeId" = '|| '''' || v_alertSubTypeId || '''' || '  and "accountId" = '|| '''' || v_accountId || '''' || '  and "accountType" = '|| '''' || v_accountType || ''''  || '  and "channelId" = '|| '''' || v_channelId || '''' ;
               v_query := v_query || '  '|| v_whereCondition ;
               EXECUTE IMMEDIATE v_query;
               
               v_index1 := v_index1 + 1 ;
               
               v_msg := 'success';
               open "records" for select v_msg from dual;
            
            END;
         END LOOP;
      
      END;
   EXCEPTION
      WHEN OTHERS THEN
   
   BEGIN
    
      v_msg := SQLERRM;
      OPEN  "records" FOR
         SELECT v_msg "errmsg"  
           FROM DUAL  ;
            
   END;END;


END;
/


CREATE OR REPLACE PROCEDURE "customeralertchannel_deletebulk_proc"
(
  "_deleterecords" IN NVARCHAR2,
  
  "records" OUT SYS_REFCURSOR
)
AS
   v_index1 NUMBER(10,0) := 0;
   v_numOfRecords NUMBER(10,0) := 0;
   v_rowValues NVARCHAR2(2000);
   v_customer_id NVARCHAR2(50);
   v_alertCategoryId VARCHAR2(50);
   v_alertTypeId NVARCHAR2(50);
   v_alertSubTypeId VARCHAR2(75);
   v_accountId NVARCHAR2(50);
   v_accountType NVARCHAR2(50);
   v_channelId NVARCHAR2(50);
   v_query NVARCHAR2(2000);
   v_msg NVARCHAR2(2000);
    

BEGIN

   BEGIN
      
      BEGIN
         v_numOfRecords := LENGTH("_deleterecords") - LENGTH(REPLACE("_deleterecords", '|', '')) + 1 ;
         v_index1 := v_index1 + 1 ;
         WHILE ( v_index1 != v_numOfRecords + 1 ) 
         LOOP 
            
            BEGIN
               v_rowValues := SUBSTRING_INDEX(SUBSTRING_INDEX("_deleterecords", '|', v_index1), '|', -1) ;
               v_customer_id := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 1), N'', -1) ;
               v_alertCategoryId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 2), N',', -1) ;
               v_alertTypeId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 3), N',', -1);
               v_alertSubTypeId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 4), N',', -1) ;
               v_accountId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 5), N',', -1) ;
               v_accountType := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 6), N',', -1) ;
               v_channelId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 7), N',', -1) ;
               v_createdby := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 8), N',', -1) ;
               v_companyLegalUnit := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', -1), N'', 1) ;
               v_query := 'delete from "customeralertchannel" where "customerId" = '|| '''' || v_customer_id || '''' || '  and "alertCategoryId" =  '|| '''' || v_alertCategoryId || '''' || '  and "companyLegalUnit" =  '|| '''' || v_companyLegalUnit || '''' ||'  and "alertTypeId" = '||
			   ''''|| v_alertTypeId || '''' || '  and "alertSubTypeId" = '|| '''' || v_alertSubTypeId || '''' || '  and "accountId" = '||
			   '''' || v_accountId || '''' || '  and "accountType" = '|| '''' || v_accountType || '''' || '  and "channelId" = '|| 
			   '''' || v_channelId || '''' ;
                execute immediate v_query ;
              
               v_index1 := v_index1 + 1 ;
               v_msg := 'success';
            open "records" for select v_msg from dual;
            END;
         END LOOP;
      
      END;
   EXCEPTION
      WHEN OTHERS THEN
   
   BEGIN
    
		v_msg := SQLERRM;
      OPEN  "records" FOR
         SELECT v_msg "errmsg"  
           FROM DUAL  ;
         
   
   END;END;


END;
/


CREATE OR REPLACE PROCEDURE "customeralertfrequency_insertbulk_proc"
(
  "_recordvalues" IN NVARCHAR2,
  
  "records" OUT SYS_REFCURSOR
)
AS
   iv_recordvalues NVARCHAR2(2000) := "_recordvalues";
   v_query NVARCHAR2(2000);
   v_msg NVARCHAR2(2000);
  
 

BEGIN

   BEGIN
      
      BEGIN
         IF iv_recordvalues IS NOT NULL
           AND iv_recordvalues != ' ' THEN
          
         BEGIN
            
            iv_recordvalues := REPLACE(iv_recordvalues, '"', CHR(39)) ;
           
            v_query := 'INSERT INTO "customeralertfrequency" ("customerId", "alertCategoryId", "alertTypeId", "alertSubTypeId", "accountId", "accountType","alertFrequencyId", "frequencyValue", "frequencyTime", "createdby","companyLegalUnit") values ' || iv_recordvalues ;
            EXECUTE IMMEDIATE v_query;
        
        
        v_msg := 'success';
        open "records" for select v_msg from dual;
         END;
         END IF;
      
      END;
   EXCEPTION
      WHEN OTHERS THEN
   
   BEGIN
    
	   v_msg := SQLERRM;
      OPEN  "records" FOR
         SELECT v_msg "errmsg"  
           FROM DUAL  ;
           
   END;END;


END;
/


CREATE OR REPLACE PROCEDURE "customeralertfrequency_updatebulk_proc"
(
  "_updateRecords" IN NVARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   v_index1 NUMBER(10,0) := 0;
   v_numOfRecords NUMBER(10,0) := 0;
   v_rowValues NVARCHAR2(2000);
   v_customer_id NVARCHAR2(50);
   v_alertCategoryId VARCHAR2(50);
   v_alertTypeId NVARCHAR2(50);
   v_alertSubTypeId VARCHAR2(75);
   v_accountId NVARCHAR2(50);
   v_modifiedby NVARCHAR2(50);
   v_accountType NVARCHAR2(50);
   v_alertFrequencyId NVARCHAR2(50);
   v_frequencyValue NVARCHAR2(50);
   v_frequencyTime NVARCHAR2(50);
   v_query NVARCHAR2(2000);
   v_whereCondition NVARCHAR2(2000);
   v_msg NVARCHAR2(2000);

   

BEGIN

   BEGIN
      
      BEGIN
         v_numOfRecords := LENGTH("_updateRecords") - LENGTH(REPLACE("_updateRecords", '|', '')) + 1 ;
         v_index1 := v_index1 + 1 ;
         WHILE ( v_index1 != v_numOfRecords + 1 ) 
         LOOP 
            
            BEGIN
               v_rowValues := SUBSTRING_INDEX(SUBSTRING_INDEX("_updateRecords", '|', v_index1), '|', -1) ;
               v_customer_id := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 1), N'', -1);
               v_alertCategoryId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 2), N',', -1) ;
               v_alertTypeId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 3), N',', -1) ;
               v_alertSubTypeId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 4), N',', -1);
               v_accountId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 5), N',', -1) ;
               v_accountType := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 6), N',', -1) ;
               v_alertFrequencyId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 7), N',', -1) ;
               v_frequencyValue := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 8), N',', -1) ;
               v_frequencyTime := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 9), N',', -1) ;
               v_modifiedby := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 10), N',', -1) ;
               
               v_companyLegalUnit := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', -1), N'', 1);
               IF v_frequencyValue != 'null' THEN
                
               BEGIN
                  v_frequencyValue := '''' || v_frequencyValue || '''' ;
               
               END;
               END IF;
               IF v_frequencyTime != 'null' THEN
                
               BEGIN
                  v_frequencyTime := '''' || v_frequencyTime || '''' ;
               
               END;
               END IF;
               v_query := 'UPDATE "customeralertfrequency" set  "modifiedby" ='|| '''' || v_modifiedby || '''' || ',"alertFrequencyId" = '|| '''' || v_alertFrequencyId || '''' || ',"companyLegalUnit" = '|| '''' || v_companyLegalUnit || '''' || ',"frequencyValue"='|| v_frequencyValue|| ',"frequencyTime"='|| v_frequencyTime|| ' where "customerId" = '|| '''' ||v_customer_id || '''' || '  and "alertCategoryId" = '|| '''' || v_alertCategoryId || '''' || '  and "alertTypeId" = '|| '''' || v_alertTypeId || '''' || '  and "alertSubTypeId" = '|| '''' || v_alertSubTypeId || '''' || '  and "accountId" = '|| '''' || v_accountId || '''' || '  and "accountType" = '|| '''' || v_accountType || ''''  ;
               EXECUTE IMMEDIATE v_query;
               v_index1 := v_index1 + 1 ;
               v_msg := 'success';
               open "records" for select v_msg from dual;
            
            END;
         END LOOP;
      
      END;
   EXCEPTION
      WHEN OTHERS THEN
   
   BEGIN
     
		v_msg := SQLERRM;
      OPEN  "records" FOR
         SELECT v_msg "errmsg"  
           FROM DUAL  ;
           
   END;END;


END;
/

CREATE OR REPLACE PROCEDURE "customeralertfrequency_deletebulk_proc"
(
  "_deleteRecords" IN NVARCHAR2,
  
   "records" OUT SYS_REFCURSOR
)
AS
   v_index1 NUMBER(10,0) := 0;
   v_numOfRecords NUMBER(10,0) := 0;
   v_rowValues NVARCHAR2(2000);
   v_customer_id NVARCHAR2(50);
   v_alertCategoryId VARCHAR2(50);
   v_alertTypeId NVARCHAR2(50);
   v_alertSubTypeId VARCHAR2(75);
   v_accountId NVARCHAR2(50);
   v_accountType NVARCHAR2(50);
   v_query NVARCHAR2(2000);
   v_msg NVARCHAR2(2000);
   
  

BEGIN

   BEGIN
      
      BEGIN
         v_numOfRecords := LENGTH("_deleteRecords") - LENGTH(REPLACE("_deleteRecords", '|', '')) + 1;
         v_index1 := v_index1 + 1 ;
         WHILE ( v_index1 != v_numOfRecords + 1 ) 
         LOOP 
            
            BEGIN
               v_rowValues := SUBSTRING_INDEX(SUBSTRING_INDEX("_deleteRecords", '|', v_index1), '|', -1) ;
               v_customer_id := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 1), N'', -1) ;
               v_alertCategoryId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 2), N',', -1)  ;
               v_alertTypeId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 3), N',', -1)  ;
               v_alertSubTypeId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 4), N',', -1)  ;
               v_accountId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 5), N',', -1)  ;
               v_accountType := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 6), N',', -1)  ;
               v_alertFrequencyId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 7), N',', -1)  ;
               v_frequencyValue := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 8), N',', -1)  ;
               v_frequencyTime := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 9), N',', -1)  ;
               v_modifiedby := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 10), N',', -1) ;
               v_companylegalunit := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', -1), N'', 1);
               v_query := 'delete from "customeralertfrequency" where "customerId" = '|| '''' || v_customer_id || '''' || '  and "alertCategoryId" =  '|| '''' || v_alertCategoryId || '''' ||'  and "companyLegalUnit" =  '|| '''' || v_companyLegalUnit || '''' || '  and "alertTypeId" = '|| '''' || v_alertTypeId ||'''' || '  and "alertSubTypeId" = '|| '''' || v_alertSubTypeId || '''' || '  and "accountId" = '|| '''' || v_accountId || '''' || '  and "accountType" = '|| '''' || v_accountType || ''''  ;
               EXECUTE IMMEDIATE v_query;
            
               v_index1 := v_index1 + 1 ;
               
               v_msg := 'success';
               open "records" for select v_msg from dual;
            
            END;
         END LOOP;
      
      END;
   EXCEPTION
      WHEN OTHERS THEN
   
   BEGIN
 
	  v_msg := SQLERRM;
      OPEN  "records" FOR
         SELECT v_msg "errmsg"  
           FROM DUAL  ;
            
   END;END;

END;
/


CREATE OR REPLACE PROCEDURE "dbxcustomeralertentitlement_insertbulk_proc"
(
  "_recordvalues" IN NVARCHAR2,
  
   "records" OUT SYS_REFCURSOR
)
AS
   iv_recordvalues NVARCHAR2(2000) := "_recordvalues";
   v_query NVARCHAR2(2000);
   v_msg NVARCHAR2(2000);
  

BEGIN

   BEGIN
      
      BEGIN
         IF iv_recordvalues IS NOT NULL
           AND iv_recordvalues != ' ' THEN
          
         BEGIN
            
            iv_recordvalues := REPLACE(iv_recordvalues, '"', CHR(39)) ;
            
            v_query := 'INSERT INTO  "dbxcustomeralertentitlement"("Customer_id","alertCategoryId","AlertTypeId","alertSubTypeId","AccountId","AccountType","Value1","Value2","alertRequestId","createdby","companyLegalUnit") VALUES '|| iv_recordvalues ;
            EXECUTE IMMEDIATE v_query;
            v_msg := 'success';
            open "records" for select v_msg from dual;
         
         END;
         END IF;
      
      END;
   EXCEPTION
      WHEN OTHERS THEN
   
   BEGIN
      
		v_msg := SQLERRM;
		
      OPEN  "records" FOR
         SELECT v_msg "errmsg"  
           FROM DUAL  ;
           
   END;END;


END;
/


CREATE OR REPLACE PROCEDURE "dbxcustomeralertentitlement_updatebulk_proc"
(
  "_updateRecords" IN NVARCHAR2,
  "records" OUT SYS_REFCURSOR
  
)
AS
   v_index1 NUMBER(10,0) := 0;
   v_numOfRecords NUMBER(10,0) := 0;
   v_numOfParams NUMBER(10,0) := 0;
   v_rowValues NVARCHAR2(2000);
   v_customer_id NVARCHAR2(50);
   v_alertCategoryId VARCHAR2(50);
   v_alertTypeId NVARCHAR2(50);
   v_alertSubTypeId VARCHAR2(75);
   v_accountId NVARCHAR2(50);
   v_modifiedby NVARCHAR2(50);
   v_alertRequestId NVARCHAR2(255);
   v_accountType NVARCHAR2(50);
   v_value1 NVARCHAR2(255);
   v_value2 NVARCHAR2(255);
   v_query NVARCHAR2(2000);
   v_whereCondition NVARCHAR2(2000);
   v_msg NVARCHAR2(2000);
   
   SQLERRM VARCHAR2(2000);

BEGIN

   BEGIN
      
      BEGIN
         v_numOfRecords := LENGTH("_updateRecords") - LENGTH(REPLACE("_updateRecords", '|', '')) + 1 ;
         v_index1 := v_index1 + 1 ;
         WHILE ( v_index1 != v_numOfRecords + 1 ) 
         LOOP 
            
            BEGIN
               v_rowValues := SUBSTRING_INDEX(SUBSTRING_INDEX("_updateRecords", '|', v_index1), '|', -1) ;
               v_numOfParams := LENGTH(v_rowValues) - LENGTH(REPLACE(v_rowValues, ',', '')) ;
               v_customer_id := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 1), N'', -1);
               v_alertCategoryId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 2), N',', -1) ;
               v_alertTypeId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 3), N',', -1) ;
               v_alertSubTypeId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 4), N',', -1) ;
               v_accountId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 5), N',', -1);
               v_modifiedby := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 6), N',', -1) ;
               v_alertRequestId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 7), N',', -1) ;
               v_value1 := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 8), N',', -1) ;
               v_value2 := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 9), N',', -1) ;
               v_accountType := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 10), N',', -1) ;
               v_companylegalunit := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', -1), N'', 1);
               v_query := 'update "dbxcustomeralertentitlement" set  "modifiedby" =' || '''' || v_modifiedby || ''''  ;
               IF v_alertRequestId != 'null' THEN
                
               BEGIN
                  v_query := v_query || ', "alertRequestId" = ' || '''' || v_alertRequestId || '''' ;
               
               END;
               END IF;
               IF v_numOfParams > 7 THEN
                
               BEGIN
                  v_value1 := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 8), N',', -1) ;
                  v_query := v_query|| ', "Value1" = '|| '''' || v_value1 || '''' ;
               
               END;
               END IF;
               IF v_numOfParams > 8 THEN
                
               BEGIN
                  v_value2 := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 9), N',', -1) ;
                  v_query := v_query || ', "Value2" = '|| '''' || v_value2 || '''' ;
               
               END;
               END IF;
               v_whereCondition := 'where "Customer_id" = '|| '''' || v_customer_id || '''' || '  and "alertCategoryId" = '|| '''' || v_alertCategoryId || '''' ||'  and "alertCategoryId" = '|| '''' || v_alertCategoryId ||'''' || '  and "AlertTypeId" = '|| '''' || v_alertTypeId || '''' || '  and "alertSubTypeId" = '|| '''' || v_alertSubTypeId || '''' || '  and "AccountId" = '|| '''' || v_accountId || '''' || '  and "AccountType" = '|| '''' || v_accountType || '''' ;
               v_query := v_query|| '  '|| v_whereCondition ;
               EXECUTE IMMEDIATE v_query;
             
               v_index1 := v_index1 + 1 ;
            
            v_msg := 'success';
             open "records" for select v_msg from dual;
            
            END;
         END LOOP;
      
      END;
   EXCEPTION
      WHEN OTHERS THEN
   
   BEGIN
      SELECT SQLERRM 

        INTO v_msg
        FROM DUAL ;
      OPEN  "records" FOR
         SELECT v_msg "errmsg"  
           FROM DUAL  ;
            
   END;END;


END;
/


CREATE OR REPLACE PROCEDURE "dbxcustomeralertentitlement_deletebulk_proc"
(
  "_DELETERECORDS" IN NVARCHAR2,
  
  "records" OUT SYS_REFCURSOR
)
AS
   v_index1 NUMBER(10,0) := 0;
   v_numOfRecords NUMBER(10,0) := 0;
   v_rowValues NVARCHAR2(2000);
   v_customer_id NVARCHAR2(50);
   v_alertCategoryId VARCHAR2(50);
   v_alertTypeId NVARCHAR2(50);
   v_alertSubTypeId VARCHAR2(75);
   v_accountId NVARCHAR2(50);
   v_accountType NVARCHAR2(50);
   v_query NVARCHAR2(2000);
   v_msg NVARCHAR2(2000);
   
   

BEGIN

   BEGIN
      
      BEGIN
         v_numOfRecords := LENGTH("_DELETERECORDS") - LENGTH(REPLACE("_DELETERECORDS", '|', '')) + 1 ;
         v_index1 := v_index1 + 1 ;
         WHILE ( v_index1 != v_numOfRecords + 1 ) 
         LOOP 
            
            BEGIN
               v_rowValues := SUBSTRING_INDEX(SUBSTRING_INDEX("_DELETERECORDS", '|', v_index1), '|', -1) ;
               v_customer_id :=SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 1), N'', -1) ;
               v_alertCategoryId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 2), N',', -1) ;
               v_alertTypeId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 3), N',', -1) ;
               v_alertSubTypeId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 4), N',', -1) ;
               v_accountId := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 5), N',', -1) ;
               v_accountType := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', 6), N',', -1) ;
               v_companyLegalUnit := SUBSTRING_INDEX(SUBSTRING_INDEX(v_rowValues, N',', -1), N'', 1) ;
                

               v_query := 'delete from "dbxcustomeralertentitlement" where ' || '"Customer_id" = ' || '''' || v_customer_id || ''''  || '  and "alertCategoryId" = ' || '''' || v_alertCategoryId || '''' ||'  and "companyLegalUnit" = ' || '''' || v_companyLegalUnit || '''' || '  and "AlertTypeId" = '|| '''' || v_alertTypeId || '''' || '  and "alertSubTypeId" = '|| '''' || v_alertSubTypeId || '''' || '  and "AccountId" = ' ||  '''' || v_accountId || '''' || '  and "AccountType" = '|| '''' || v_accountType|| '''' ;
               EXECUTE IMMEDIATE v_query;
               v_index1 := v_index1 + 1 ;
            
            END;
         END LOOP;
      
      END;
   EXCEPTION
      WHEN OTHERS THEN
   
   BEGIN
     v_msg := SQLERRM;
      OPEN  "records" FOR
         SELECT v_msg ErrorMessage  
           FROM DUAL  ;
            
   END;END;


END;
/

CREATE OR REPLACE PROCEDURE "subscriber_getEntitleMentsNocustomer"
(
  "alerttypes" IN NVARCHAR2, "records" out SYS_REFCURSOR
)
AS
   

BEGIN

   OPEN  "records" FOR
      SELECT "Customer_id" "customerid"  ,
             "AlertTypeId" "alerttypeid"  ,
             "alertSubTypeId" "alertsubtypeid"  ,
             "AccountId" "accountid"  ,
             "AccountType" "accounttype"  ,
             "Value1" "value1"  ,
             "Value2" "value2"  ,
			 "companyLegalUnit" "companyLegalUnit"
        FROM "dbxcustomeralertentitlement" 
       WHERE  FIND_IN_SET("dbxcustomeralertentitlement"."AlertTypeId", "alerttypes") <> 0 ;
      


END;
/

CREATE OR REPLACE PROCEDURE "subscriber_getEntitleMentsWithcustomer"
(
  "alerttypes" IN NVARCHAR2,
  "custids" IN NVARCHAR2, "records" out SYS_REFCURSOR
)
AS
   

BEGIN

   OPEN  "records" FOR
      SELECT "Customer_id" "customerid"  ,
             "AlertTypeId" "alerttypeid"  ,
             "alertSubTypeId" "alertsubtypeid"  ,
             "AccountId" "accountid"  ,
             "AccountType" "accounttype"  ,
             "Value1" "value1"  ,
             "Value2" "value2"  ,
			 "companyLegalUnit" "companyLegalUnit"
        FROM "dbxcustomeralertentitlement" 
       WHERE  FIND_IN_SET("dbxcustomeralertentitlement"."AlertTypeId", "alerttypes") <> 0
                AND FIND_IN_SET("dbxcustomeralertentitlement"."Customer_id", "custids") <> 0 ;
      


END;
/

CREATE OR REPLACE PROCEDURE "user_limitgroup_limits_create_proc" 
("_userId" IN NVARCHAR2,
 "_coreCustomerId" IN NVARCHAR2,
 "_contractId" IN NVARCHAR2,
 "_legalEntityId" IN NVARCHAR2) 
AS 

v_singlePaymentsActions NVARCHAR2(2000) := N'';
v_bulkPaymentsActions NVARCHAR2(2000) := N'';
v_max_per_transaction_single_payment NVARCHAR2(2000) := N'';
v_max_per_transaction_bulk_payment NVARCHAR2(2000) := N'';
v_max_daily_limit_single_payment NVARCHAR2(2000) := N'';
v_max_daily_limit_bulk_payment NVARCHAR2(2000) := N'';
v_max_weekly_limit_single_payment NVARCHAR2(2000) := N'';
v_max_weekly_limit_bulk_payment NVARCHAR2(2000) := N'';

BEGIN

	SELECT listagg(CAST("featureaction"."id" AS NVARCHAR2(2000)), ',') 
	INTO v_singlePaymentsActions
	FROM "featureaction"
	WHERE "featureaction"."limitgroupId" = 'SINGLE_PAYMENT';


	SELECT listagg(CAST("featureaction"."id" AS NVARCHAR2(2000)), ',') 
	INTO v_bulkPaymentsActions
	FROM "featureaction"
	WHERE "featureaction"."limitgroupId" = 'BULK_PAYMENT';


	SELECT MAX("customeraction"."value") 
	INTO v_max_per_transaction_single_payment
	FROM "customeraction"
	WHERE "customeraction"."contractId" = "_contractId"
		AND "customeraction"."coreCustomerId" = "_coreCustomerId"
		AND "customeraction"."Customer_id" = "_userId"
		AND "customeraction"."companyLegalUnit" = "_legalEntityId"
		AND FIND_IN_SET("customeraction"."Action_id", v_singlePaymentsActions) = '1'
		AND "customeraction"."LimitType_id" = 'AUTO_DENIED_TRANSACTION_LIMIT'
		AND "customeraction"."Account_id" <> ' '
		AND "customeraction"."value" <> 0;


	SELECT MAX("customeraction"."value") INTO v_max_per_transaction_bulk_payment
	FROM "customeraction"
	WHERE "customeraction"."contractId" = "_contractId"
		AND "customeraction"."coreCustomerId" = "_coreCustomerId"
		AND "customeraction"."Customer_id" = "_userId"
		AND "customeraction"."companyLegalUnit" = "_legalEntityId"
		AND FIND_IN_SET("customeraction"."Action_id", v_bulkPaymentsActions) = '1'
		AND "customeraction"."LimitType_id" = 'AUTO_DENIED_TRANSACTION_LIMIT'
		AND "customeraction"."Account_id" <> ' '
		AND "customeraction"."value" <> 0;


	SELECT SUM("customeraction"."value") INTO v_max_daily_limit_single_payment
	FROM "customeraction"
	WHERE "customeraction"."contractId" = "_contractId"
		AND "customeraction"."coreCustomerId" = "_coreCustomerId"
		AND "customeraction"."Customer_id" = "_userId"
		AND "customeraction"."companyLegalUnit" = "_legalEntityId"
		AND FIND_IN_SET("customeraction"."Action_id", v_singlePaymentsActions) = '1'
		AND "customeraction"."LimitType_id" = 'AUTO_DENIED_DAILY_LIMIT'
		AND "customeraction"."Account_id" <> ' '
		AND "customeraction"."value" <> 0;


	SELECT SUM("customeraction"."value") INTO v_max_daily_limit_bulk_payment
	FROM "customeraction"
	WHERE "customeraction"."contractId" = "_contractId"
		AND "customeraction"."coreCustomerId" = "_coreCustomerId"
		AND "customeraction"."Customer_id" = "_userId"
		AND "customeraction"."companyLegalUnit" = "_legalEntityId"
		AND FIND_IN_SET("customeraction"."Action_id", v_bulkPaymentsActions) = '1'
		AND "customeraction"."LimitType_id" = 'AUTO_DENIED_DAILY_LIMIT'
		AND "customeraction"."Account_id" <> ' '
		AND "customeraction"."value" <> 0;


	SELECT SUM("customeraction"."value") INTO v_max_weekly_limit_single_payment
	FROM "customeraction"
	WHERE "customeraction"."contractId" = "_contractId"
		AND "customeraction"."coreCustomerId" = "_coreCustomerId"
		AND "customeraction"."Customer_id" = "_userId"
		AND "customeraction"."companyLegalUnit" = "_legalEntityId"
		AND FIND_IN_SET("customeraction"."Action_id", v_singlePaymentsActions) = '1'
		AND "customeraction"."LimitType_id" = 'AUTO_DENIED_WEEKLY_LIMIT'
		AND "customeraction"."Account_id" <> ' '
		AND "customeraction"."value" <> 0;


	SELECT SUM("customeraction"."value") INTO v_max_weekly_limit_bulk_payment
	FROM "customeraction"
	WHERE "customeraction"."contractId" = "_contractId"
		AND "customeraction"."coreCustomerId" = "_coreCustomerId"
		AND "customeraction"."Customer_id" = "_userId"
		AND "customeraction"."companyLegalUnit" = "_legalEntityId"
		AND FIND_IN_SET("customeraction"."Action_id", v_bulkPaymentsActions) = '1'
		AND "customeraction"."LimitType_id" = 'AUTO_DENIED_WEEKLY_LIMIT'
		AND "customeraction"."Account_id" <> ' '
		AND "customeraction"."value" <> 0;


	INSERT INTO "customerlimitgrouplimits" ("customerlimitgrouplimits"."id",
											"customerlimitgrouplimits"."Customer_id",
											"customerlimitgrouplimits"."contractId",
											"customerlimitgrouplimits"."coreCustomerId",
											"customerlimitgrouplimits"."companyLegalUnit",
											"customerlimitgrouplimits"."limitGroupId",
											"customerlimitgrouplimits"."LimitType_id",
											"customerlimitgrouplimits"."value")
	VALUES (SUBSTR(SYS_GUID(), 0, 50), "_userId", "_contractId", "_coreCustomerId", "_legalEntityId", 'SINGLE_PAYMENT', 'MAX_TRANSACTION_LIMIT', v_max_per_transaction_single_payment);


	INSERT INTO "customerlimitgrouplimits" ("customerlimitgrouplimits"."id",
											"customerlimitgrouplimits"."Customer_id",
											"customerlimitgrouplimits"."contractId",
											"customerlimitgrouplimits"."coreCustomerId",
											"customerlimitgrouplimits"."companyLegalUnit",
											"customerlimitgrouplimits"."limitGroupId",
											"customerlimitgrouplimits"."LimitType_id",
											"customerlimitgrouplimits"."value")
	VALUES (SUBSTR(SYS_GUID(), 0, 50), "_userId", "_contractId", "_coreCustomerId", "_legalEntityId", 'BULK_PAYMENT', 'MAX_TRANSACTION_LIMIT', v_max_per_transaction_bulk_payment);


	INSERT INTO "customerlimitgrouplimits" ("customerlimitgrouplimits"."id",
											"customerlimitgrouplimits"."Customer_id",
											"customerlimitgrouplimits"."contractId",
											"customerlimitgrouplimits"."coreCustomerId",
											"customerlimitgrouplimits"."companyLegalUnit",
											"customerlimitgrouplimits"."limitGroupId",
											"customerlimitgrouplimits"."LimitType_id",
											"customerlimitgrouplimits"."value")
	VALUES (SUBSTR(SYS_GUID(), 0, 50), "_userId", "_contractId", "_coreCustomerId", "_legalEntityId", 'SINGLE_PAYMENT', 'DAILY_LIMIT', v_max_daily_limit_single_payment);


	INSERT INTO "customerlimitgrouplimits" ("customerlimitgrouplimits"."id",
											"customerlimitgrouplimits"."Customer_id",
											"customerlimitgrouplimits"."contractId",
											"customerlimitgrouplimits"."coreCustomerId",
											"customerlimitgrouplimits"."companyLegalUnit",
											"customerlimitgrouplimits"."limitGroupId",
											"customerlimitgrouplimits"."LimitType_id",
											"customerlimitgrouplimits"."value")
	VALUES (SUBSTR(SYS_GUID(), 0, 50), "_userId", "_contractId", "_coreCustomerId", "_legalEntityId", 'BULK_PAYMENT', 'DAILY_LIMIT', v_max_daily_limit_bulk_payment);


	INSERT INTO "customerlimitgrouplimits" ("customerlimitgrouplimits"."id",
											"customerlimitgrouplimits"."Customer_id",
											"customerlimitgrouplimits"."contractId",
											"customerlimitgrouplimits"."coreCustomerId",
											"customerlimitgrouplimits"."companyLegalUnit",
											"customerlimitgrouplimits"."limitGroupId",
											"customerlimitgrouplimits"."LimitType_id",
											"customerlimitgrouplimits"."value")
	VALUES (SUBSTR(SYS_GUID(), 0, 50), "_userId", "_contractId", "_coreCustomerId", "_legalEntityId", 'SINGLE_PAYMENT', 'WEEKLY_LIMIT', v_max_weekly_limit_single_payment);


	INSERT INTO "customerlimitgrouplimits" ("customerlimitgrouplimits"."id",
											"customerlimitgrouplimits"."Customer_id",
											"customerlimitgrouplimits"."contractId",
											"customerlimitgrouplimits"."coreCustomerId",
											"customerlimitgrouplimits"."companyLegalUnit",
											"customerlimitgrouplimits"."limitGroupId",
											"customerlimitgrouplimits"."LimitType_id",
											"customerlimitgrouplimits"."value")
	VALUES (SUBSTR(SYS_GUID(), 0, 50), "_userId", "_contractId", "_coreCustomerId", "_legalEntityId", 'BULK_PAYMENT', 'WEEKLY_LIMIT', v_max_weekly_limit_bulk_payment);

END;

/

CREATE OR REPLACE PROCEDURE "useraccounts_create_proc"
(
  "_userId" IN NVARCHAR2,
  "_accountsCSV" IN NVARCHAR2,
  "_coreCustomerId" IN NVARCHAR2,
  "_contractId" IN NVARCHAR2,
  "_legalEntityId" IN NVARCHAR2,  
)
AS
   v_accountID VARCHAR2(255);
   v_finished NUMBER(10,0) := 0;
   v_id VARCHAR2(255);
   CURSOR accountData
	IS SELECT "contractaccounts"."accountId" 
	FROM "contractaccounts" 
	WHERE "contractaccounts"."contractId" = "_contractId"
	AND "contractaccounts"."coreCustomerId" = "_coreCustomerId"
	AND FIND_IN_SET("contractaccounts"."accountId", "_accountsCSV") = '1'
	AND "contractaccounts"."companyLegalUnit" = "_legalEntityId";

BEGIN  
   OPEN accountData;
   FETCH accountData INTO v_accountID;
   <<loop_1>>
   WHILE ( 1 = 1 ) 
	LOOP 
		BEGIN 
		IF (accountData%NOTFOUND)  THEN
			v_finished := 1 ;
        END IF;
        IF v_finished = 1 THEN
			EXIT;
        ELSE
        BEGIN
            SELECT SUBSTR(SYS_GUID(), 0, 50) 
            INTO v_id
            FROM DUAL ;
			
            INSERT INTO "customeraccounts"
			( "id", "Customer_id", "Account_id", "contractId", "coreCustomerId","companyLegalUnit")
			VALUES ( v_id, "_userId", v_accountID, "_contractId", "_coreCustomerId","_legalEntityId");
         
        END;
        END IF;
        FETCH accountData INTO v_accountID;
        GOTO loop_1;
      
		END;
   END LOOP;
   CLOSE accountData;
END;
/