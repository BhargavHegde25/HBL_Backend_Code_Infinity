ALTER PROCEDURE [${dbxschemaname}].[location_search_proc]  

   @_searchKeyword varchar(1000)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
   
         @_next varchar(max) = NULL

      DECLARE
         @_nextlen int = NULL

      DECLARE
  
         @_value varchar(max) = NULL

      DECLARE
     
         @sql1 varchar(max)

      DECLARE
    
         @sqlFrontPart varchar(max)

      DECLARE
     
         @sqlresult varchar(max) = NULL

      DECLARE
         @counter int

      SET @sqlFrontPart = N'SELECT * FROM [${dbxschemaname}].locationdetails_view where '
  
      SET @counter = 1
    

      WHILE (1 = 1)
      
         BEGIN

            IF datalength(LTRIM(RTRIM(@_searchKeyword))) = 0 OR @_searchKeyword IS NULL
               BREAK

            SET @_next = [${dbxschemaname}].substring_index(@_searchKeyword, ',', 1)
			
            SET @_nextlen = datalength(@_next)

            SET @_value = lower(LTRIM(RTRIM(@_next)))

            SET @sql1 = NULL
      declare @var nvarchar(max) = null;
            SET @var = NULL
            SET @var = replace(LTRIM(RTRIM(@_value)), N' ', N'%')
            SET @sql1 = 
               (N'( LOWER(city) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
				+ 
               (N''')  or LOWER(informationTitle) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
				+ 
               (N''')  or LOWER(name) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
				+ 
               (N''')  or LOWER(Description) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
				+ 
               (N''')  or LOWER(Code) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
				+ 
               (N''')  or LOWER(phoneNumber) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
				+ 
               (N''')  or LOWER(email) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
				+ 
               (N''')  or LOWER(status) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
				+ 
               (N''')  or LOWER(type) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
				+ 
               (N''')  or LOWER(services) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
				+ 
               (N''')  or LOWER(region) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
               (N''')  or LOWER(addressLine1) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
                + 
               (N''') or LOWER(addressLine2) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
               (N''')  or LOWER(addressLine3) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
               (N''')  or LOWER(country) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
               (N''')  or LOWER(zipcode) like (''')
                + 
               (N'%')
                + 
               (@var)
                + 
               (N'%')
                + 
               (N'''))')
        
            IF @counter = 1
           
               SET @sqlresult = @sql1
               
            ELSE 
           
               SET @sqlresult = (@sql1) + (N' AND ') + (@sqlresult)
          
            SET @counter = @counter + 1
         
            SET @_searchKeyword = STUFF(@_searchKeyword, 1, @_nextlen + 1, N'')

         END

    
      SET @sqlresult = (N'(') + (@sqlFrontPart) + (@sqlresult) + (N')')
	  EXEC(@sqlresult)
   END
GO