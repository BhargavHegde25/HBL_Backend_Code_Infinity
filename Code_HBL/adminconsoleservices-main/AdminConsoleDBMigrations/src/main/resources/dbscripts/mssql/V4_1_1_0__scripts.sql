USE [${dbxdbname}]
GO
/****** Object:  UserDefinedFunction [${dbxschemaname}].[func_escape_input_for_in_operator]    Script Date: 6/9/2020 2:01:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE FUNCTION [${dbxschemaname}].[func_escape_input_for_in_operator](
  @_input varchar(max)
) RETURNS nvarchar(max) AS
BEGIN
    DECLARE @result varchar(max)
    DECLARE @element varchar(max)
    DECLARE @ctr bigint
    SET @ctr = 1
    SET @result = ''
    while 1=1
	begin
		SET @element = [${dbxschemaname}].func_split_str(@_input, ',', @ctr);
		
		IF @element = ''
			break;

        IF @result != '' 
			set @result = @result + ','

        set @result = @result + ''''+(@element)+''''    
        SET @ctr = @ctr + 1;
    END 
    
    RETURN (@result)
END

GO
/****** Object:  UserDefinedFunction [${dbxschemaname}].[func_split_str]    Script Date: 6/9/2020 2:01:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE FUNCTION [${dbxschemaname}].[func_split_str] 
( 
   /*
   *   SSMA informational messages:
   *   M2SS0055: Data type was converted to VARCHAR(MAX) according to character set mapping for latin1 character set
   */

   @X varchar(max),
   /*
   *   SSMA informational messages:
   *   M2SS0055: Data type was converted to VARCHAR(MAX) according to character set mapping for latin1 character set
   */

   @delim varchar(max),
   @pos int
)
RETURNS nvarchar(max)
AS 
   BEGIN
		DECLARE @posSubString varchar(max)
		DECLARE @posMinusOneSubstring varchar(max)
		set @posSubString = ([${dbxschemaname}].substring_index(@X, @delim, @pos))
		set @posMinusOneSubstring = ([${dbxschemaname}].substring_index(@X, @delim, @pos-1))
		RETURN replace(substring(@posSubString,datalength(@posMinusOneSubstring) + 1,datalength(@posSubString)), @delim, N'')
   END
GO


CREATE FUNCTION [${dbxschemaname}].[SUBSTRING_INDEX](@InString  NVARCHAR(Max),
                                    @Delimiter NVARCHAR(Max),
                                    @Count     INT)
RETURNS NVARCHAR(max)
AS
BEGIN
    DECLARE @Pos INT;
    DECLARE @DelimiterOffsets TABLE
    (
         i      INT IDENTITY(1, 1) NOT NULL,
         offset INT NOT NULL
    );

    -- If @Count is zero, we return '' as per spec
    IF @Count = 0
    BEGIN
        RETURN '';
    END;

    DECLARE @OrigLength      INT = LEN(@InString);
    DECLARE @DelimiterLength INT = LEN(@Delimiter);

    -- Prime the pump.
    SET @Pos = Charindex(@Delimiter, @InString, 1);

    -- If the delimiter does not exist in @InString, return the whole string
    IF @Pos = 0
    BEGIN
        RETURN @InString;
    END;

    -- Put all delimiter offsets into @DelimiterOffsets, they get numbered automatically.
    DECLARE @CurrentOffset INT = 0;
    WHILE @Pos > 0
    BEGIN
        SET @CurrentOffset = @Pos;

        INSERT INTO @DelimiterOffsets
                    (offset)
             VALUES (@CurrentOffset);

        SET @Pos = Charindex(@Delimiter, @InString, @CurrentOffset + @DelimiterLength);
    END;

    -- This number is guaranteed to be > 0.
    DECLARE @DelimitersFound INT = (SELECT Count(*) FROM @DelimiterOffsets);

    -- If they requested more delimiters than were found, return the whole string, as per spec.
    IF Abs(@Count) > @DelimitersFound
    BEGIN
        RETURN @InString;
    END;

    DECLARE @StartSubstring INT = 0;
    DECLARE @EndSubstring   INT = @OrigLength;

    -- OK, now return the part they requested
    IF @Count > 0
    BEGIN
        SET @EndSubstring = (SELECT offset 
                               FROM @DelimiterOffsets 
                              WHERE i = @Count);
    END
    ELSE
    BEGIN
        SET @StartSubstring = (SELECT offset + @DelimiterLength 
                                 FROM @DelimiterOffsets 
                                WHERE i = (@DelimitersFound + @Count + 1));
    END;

    RETURN Substring(@InString, @StartSubstring, @EndSubstring);
END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE FUNCTION [${dbxschemaname}].[LEASTINT] 
(
	@first int,
	@second int
)
RETURNS int
AS
BEGIN
	return case when @first < @second then @first 
              when @second < @first  then @second
              else @first
         end

END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE FUNCTION [${dbxschemaname}].FIND_IN_SET (@Search_For nvarchar(max), @Search_This nvarchar(max))
RETURNS int AS
BEGIN
RETURN CASE WHEN (SELECT count(1) FROM STRING_SPLIT(@Search_This,',') WHERE VALUE=@Search_For) = 0 THEN '0' ELSE '1' END
END
GO