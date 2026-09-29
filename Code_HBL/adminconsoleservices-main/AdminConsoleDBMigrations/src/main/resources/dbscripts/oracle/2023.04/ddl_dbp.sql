create or replace NONEDITIONABLE TYPE "LISTAGG_CLOB_T" as object
( t_varchar2 varchar2(32767)
, t_clob clob
, static function odciaggregateinitialize( sctx in out listagg_clob_t )
  return number
, member function odciaggregateiterate
    ( self in out listagg_clob_t
    , a_val varchar2
    )
  return number
, member function odciaggregateterminate
    ( self in out listagg_clob_t
    , returnvalue out clob
    , flags in number
    )
  return number
, member function odciaggregatemerge
    ( self in out listagg_clob_t
    , ctx2 in out listagg_clob_t
    )
  return number
);
/

create or replace NONEDITIONABLE TYPE BODY         "LISTAGG_CLOB_T" 
is
  static function odciaggregateinitialize( sctx in out listagg_clob_t )
  return number
  is
  begin
    sctx := listagg_clob_t( null, null );
    return odciconst.success;
  end;
--
  member function odciaggregateiterate
    ( self in out listagg_clob_t
    , a_val varchar2
    )
  return number
  is
    procedure add_val( p_val varchar2 )
    is
    begin
      if nvl( lengthb( self.t_varchar2 ), 0 ) + lengthb( p_val ) <= 4000
-- Strange limit, the max size of self.t_varchar2 is 29993
-- If you exceeds this number you get ORA-22813: operand value exceeds system limits
-- with 29993 you get JSON-output as large 58894 bytes
-- with 4000 you get JSON-output as large 1063896 bytes, probably max more
      then
        if self.t_varchar2 is null then
          self.t_varchar2 := self.t_varchar2 || p_val;
        else
          self.t_varchar2 := self.t_varchar2 || ',' || p_val;
        end if;
      else
        if self.t_clob is null
        then
          dbms_lob.createtemporary( self.t_clob, true, dbms_lob.call );
          dbms_lob.writeappend( self.t_clob, length( self.t_varchar2 ), self.t_varchar2 );
        else
          dbms_lob.writeappend( self.t_clob, length( self.t_varchar2 ), ','||self.t_varchar2 );
        end if;
        self.t_varchar2 := p_val;
      end if;
    end;
  begin
    add_val( a_val );
    return odciconst.success;
  end;
--
  member function odciaggregateterminate
    ( self in out listagg_clob_t
    , returnvalue out clob
    , flags in number
    )
  return number
  is
  begin
    if self.t_clob is null
    then
      dbms_lob.createtemporary( self.t_clob, true, dbms_lob.call );
    end if;
    if self.t_varchar2 is not null
    then
      dbms_lob.writeappend( self.t_clob, length( self.t_varchar2 ), self.t_varchar2 );
    end if;
    returnvalue := self.t_clob;
    return odciconst.success;
  end;
--
  member function odciaggregatemerge
    ( self in out listagg_clob_t
    , ctx2 in out listagg_clob_t
    )
  return number
  is
  begin
    if self.t_clob is null
    then
      dbms_lob.createtemporary( self.t_clob, true, dbms_lob.call );
    end if;
    if self.t_varchar2 is not null
    then
      dbms_lob.writeappend( self.t_clob, length( self.t_varchar2 ), self.t_varchar2 );
    end if;
    if ctx2.t_clob is not null
    then
      dbms_lob.append( self.t_clob, ctx2.t_clob );
      dbms_lob.freetemporary( ctx2.t_clob );
    end if;
    if ctx2.t_varchar2 is not null
    then
      dbms_lob.writeappend( self.t_clob, length( ctx2.t_varchar2 ), ctx2.t_varchar2 );
      ctx2.t_varchar2 := null;
    end if;
    return odciconst.success;
  end;
--
end;
/


create or replace NONEDITIONABLE PACKAGE "UTILS" AS
SUBTYPE ts is timestamp(9) ;

FUNCTION STRING_SPLIT(i_str VARCHAR2, i_delim VARCHAR2 DEFAULT ',') RETURN SYS.ODCIVARCHAR2LIST DETERMINISTIC;
FUNCTION STRING_SPLIT(i_str CLOB, i_delim VARCHAR2 DEFAULT ',') RETURN SYS.ODCIVARCHAR2LIST DETERMINISTIC;
FUNCTION GETIDENTITY RETURN NUMBER; 

FUNCTION DATEPART(P_PART_EXPR IN VARCHAR2, P_DATE_STR IN VARCHAR2)  RETURN NUMBER;
--FUNCTION DATEADD(P_INTERVAL IN VARCHAR2, P_INTERVAL_VAL IN NUMBER, P_DATE_STR IN VARCHAR2) RETURN TS;
FUNCTION FIND_IN_SET( i_value VARCHAR2,i_list clob,i_delim VARCHAR2 DEFAULT ',') RETURN INT DETERMINISTIC;
FUNCTION FIND_IN_SET( i_value VARCHAR2,i_list VARCHAR2,i_delim VARCHAR2 DEFAULT ',') RETURN INT DETERMINISTIC;

FUNCTION SUBSTRING_INDEX(v_InString IN VARCHAR2, v_Delimiter IN VARCHAR2, v_Count IN NUMBER) RETURN VARCHAR2;
FUNCTION SUBSTRING_INDEX(v_InString IN CLOB, v_Delimiter IN VARCHAR2, v_Count IN NUMBER) RETURN VARCHAR2;

PROCEDURE HANDLEERROR(ERRORCODE NUMBER,MSG VARCHAR2);
PROCEDURE RAISERROR(ERRORCODE NUMBER,MSG VARCHAR2);

--FUNCTION DATEDIFF(P_DATEPART IN VARCHAR2, P_START_DATE_EXPR IN DATE, P_END_DATE_EXPR IN DATE) RETURN NUMBER;


end UTILS;
/



create or replace NONEDITIONABLE PACKAGE BODY "UTILS" AS
SUBTYPE TS is timestamp(9) ;
SUBTYPE TS1 is timestamp(9) ;

TYPE VARCHAR2_ARRAY IS TABLE OF VARCHAR2(100);
DT_FORMATS          VARCHAR2_ARRAY; -- Customer : Please Modify the List of Date/Timestamp Formats so that the most common T-SQL literals are at the top
DT_DAY              VARCHAR2_ARRAY; -- Datetime formats starting with Day
DT_MONTH            VARCHAR2_ARRAY; -- Datetime formats starting with Month
DT_YEAR             VARCHAR2_ARRAY; -- Datetime formats starting with Year
DT_TIME             VARCHAR2_ARRAY; -- Datetime formats just having Hour, Minute, Second and Fractional seconds
DT_NLS              VARCHAR2_ARRAY; -- Oracle NLS DateTime formats
DT_TIMESTAMP        VARCHAR2_ARRAY; -- Oracle TIMESTAMP formats

FUNCTION STRING_SPLIT(
  i_str    IN  VARCHAR2,
  i_delim  IN  VARCHAR2 DEFAULT ','
) RETURN SYS.ODCIVARCHAR2LIST DETERMINISTIC
IS
  p_result       SYS.ODCIVARCHAR2LIST := SYS.ODCIVARCHAR2LIST();
  p_start        NUMBER(5) := 1;
  p_end          NUMBER(5);
  c_len CONSTANT NUMBER(5) := LENGTH( i_str );
  c_ld  CONSTANT NUMBER(5) := LENGTH( i_delim );
BEGIN
  IF c_len > 0 THEN
    p_end := INSTR( i_str, i_delim, p_start );
    WHILE p_end > 0 LOOP
      p_result.EXTEND;
      p_result( p_result.COUNT ) := SUBSTR( i_str, p_start, p_end - p_start );
      p_start := p_end + c_ld;
      p_end := INSTR( i_str, i_delim, p_start );
    END LOOP;
    IF p_start <= c_len + 1 THEN
      p_result.EXTEND;
      p_result( p_result.COUNT ) := SUBSTR( i_str, p_start, c_len - p_start + 1 );
    END IF;
  END IF;
  RETURN p_result;
END;

--FUNCTION SUBSTRING_INDEX
--(
--  v_InString IN CLOB,
--  v_Delimiter IN VARCHAR2,
--  v_Count IN NUMBER
--)
--RETURN VARCHAR2
--AS
--   v_Pos NUMBER(10,0);
--   v_OrigLength NUMBER(10,0) := DBMS_LOB.GETLENGTH(v_InString);
--   v_DelimiterLength NUMBER(10,0) := LENGTH(v_Delimiter);
--   -- Put all delimiter offsets into @DelimiterOffsets, they get numbered automatically.
--   v_CurrentOffset NUMBER(10,0) := 0;
--   -- This number is guaranteed to be > 0.
--   v_DelimitersFound NUMBER(10,0);
--   v_StartSubstring NUMBER(10,0) := 0;
--   v_EndSubstring NUMBER(10,0) := v_OrigLength;
--   r_delimiter VARCHAR2(10);
--
--BEGIN
--
--   -- If @Count is zero, we return '' as per spec
--   IF v_Count = 0 THEN
--      RETURN '';
--   END IF;
--
--   -- Prime the pump.
--   v_Pos := DBMS_LOB.INSTR(v_InString, v_Delimiter, 1);
--
--   -- If the delimiter does not exist in @InString, return the whole string
--   IF v_Pos = 0 THEN       
--                  IF v_OrigLength > 4000 THEN
--          raise_application_error(-20010, DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
--                  END IF;
--                  RETURN DBMS_LOB.SUBSTR(v_InString, 1, 4000); -- This is to convert the type as VARCHAR2
--   END IF;
--   r_delimiter := REPLACE(v_Delimiter,'|','\|');
--   r_delimiter := REPLACE(r_delimiter,'*','\*');
--   r_delimiter := REPLACE(r_delimiter,'[','\[');
--   r_delimiter := REPLACE(r_delimiter,']','\]');
--   r_delimiter := REPLACE(r_delimiter,'(','\(');
--   r_delimiter := REPLACE(r_delimiter,')','\)');
--   SELECT REGEXP_COUNT(v_InString, r_delimiter) INTO v_DelimitersFound from dual;
--   
--   IF ABS(v_count) > v_DelimitersFound THEN
--       IF v_OrigLength > 4000 THEN
--                      raise_application_error(-20010, DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
--                  END IF;
--       RETURN DBMS_LOB.SUBSTR(v_InString, 1, 4000); -- This is to convert the type as VARCHAR2
--   END IF;
--   
--   IF v_count > 0 THEN
--       v_EndSubstring := DBMS_LOB.INSTR(v_InString, v_Delimiter, 1, v_count) - 1;
--   ELSE
--       v_StartSubstring := DBMS_LOB.INSTR(v_InString, v_Delimiter, 1, v_DelimitersFound + v_count + 1) + v_DelimiterLength;
--   END IF;
--   
--   IF (v_EndSubstring - v_StartSubstring) > 4000 THEN
--        raise_application_error(-20010, DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
--   END IF;
--                  
--   RETURN DBMS_LOB.SUBSTR(v_InString, v_StartSubstring, v_EndSubstring); -- This is to convert the type as VARCHAR2
--                  
--END;

FUNCTION SUBSTRING_INDEX
(
  v_InString IN CLOB,
  v_Delimiter IN VARCHAR2,
  v_Count IN NUMBER
)
RETURN VARCHAR2
AS
   v_Pos NUMBER(10,0);
   v_OrigLength NUMBER(10,0) := DBMS_LOB.GETLENGTH(v_InString);
   v_DelimiterLength NUMBER(10,0) := LENGTH(v_Delimiter);
   -- Put all delimiter offsets into @DelimiterOffsets, they get numbered automatically.
   v_CurrentOffset NUMBER(10,0) := 0;
   -- This number is guaranteed to be > 0.
   v_DelimitersFound NUMBER(10,0);
   v_StartSubstring NUMBER(10,0) := 1;
   v_EndSubstring NUMBER(10,0) := v_OrigLength;
   r_delimiter VARCHAR2(10);

BEGIN

   -- If @Count is zero, we return '' as per spec
   IF v_Count = 0 THEN
      RETURN '';
   END IF;
--    DBMS_OUTPUT.PUT_LINE('v_Count --> ' || v_Count);
   -- Prime the pump.
   v_Pos := DBMS_LOB.INSTR(v_InString, v_Delimiter, 1);
--    DBMS_OUTPUT.PUT_LINE('v_Pos --> ' || v_Pos);
   -- If the delimiter does not exist in @InString, return the whole string
   IF v_Pos = 0 THEN       
        IF v_OrigLength > 32767 THEN
          raise_application_error(-20010, DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
        END IF;
       RETURN DBMS_LOB.SUBSTR(v_InString, 32767, 1); -- This is to convert the type as VARCHAR2
   END IF;
   
   
   r_delimiter := REPLACE(v_Delimiter,'|','\|');
   r_delimiter := REPLACE(r_delimiter,'*','\*');
   r_delimiter := REPLACE(r_delimiter,'[','\[');
   r_delimiter := REPLACE(r_delimiter,']','\]');
   r_delimiter := REPLACE(r_delimiter,'(','\(');
   r_delimiter := REPLACE(r_delimiter,')','\)');
   SELECT REGEXP_COUNT(v_InString, r_delimiter) INTO v_DelimitersFound from dual;
--    DBMS_OUTPUT.PUT_LINE('REGEXP_COUNT(v_InString, r_delimiter) --> ' || REGEXP_COUNT(v_InString, r_delimiter));
   IF ABS(v_count) > v_DelimitersFound THEN
       IF v_OrigLength > 32767 THEN
                      raise_application_error(-20010, DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
                  END IF;
       RETURN DBMS_LOB.SUBSTR(v_InString, 32767, 1); -- This is to convert the type as VARCHAR2
   END IF;
   
   IF v_count > 0 THEN
       v_EndSubstring := DBMS_LOB.INSTR(v_InString, v_Delimiter, 1, v_count) - 1;
--       DBMS_OUTPUT.PUT_LINE('v_EndSubstring1 --->' || v_EndSubstring);
   ELSE
       v_StartSubstring := DBMS_LOB.INSTR(v_InString, v_Delimiter, 1, v_DelimitersFound + v_count + 1) + v_DelimiterLength;
   END IF;
   
   IF (v_EndSubstring - v_StartSubstring) > 32767 THEN
        raise_application_error(-20010, DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
   END IF;
--   DBMS_OUTPUT.PUT_LINE('v_EndSubstring -v_StartSubstring   '||v_StartSubstring ||'   ' ||v_EndSubstring);
--       DBMS_OUTPUT.PUT_LINE(DBMS_LOB.SUBSTR(v_InString,v_EndSubstring, v_StartSubstring));

   RETURN DBMS_LOB.SUBSTR(v_InString,(v_EndSubstring - v_StartSubstring)+1 ,v_StartSubstring); -- This is to convert the type as VARCHAR2
            
END;

FUNCTION STRING_SPLIT(
  i_str    IN  CLOB,
  i_delim  IN  VARCHAR2 DEFAULT ','
) RETURN SYS.ODCIVARCHAR2LIST DETERMINISTIC
IS
  p_result       SYS.ODCIVARCHAR2LIST := SYS.ODCIVARCHAR2LIST();
  p_start        NUMBER(7) := 1;
  p_end          NUMBER(7);
  c_len CONSTANT NUMBER(7) := dbms_lob.getlength( i_str );
  c_ld  CONSTANT NUMBER(5) := LENGTH( i_delim );
BEGIN
  DBMS_OUTPUT.PUT_LINE('1');

  IF c_len > 0 THEN
    p_end := DBMS_LOB.INSTR( i_str, i_delim, p_start,1 );
    DBMS_OUTPUT.PUT_LINE('p_end --> '||p_end);
    WHILE p_end > 0 LOOP
      DBMS_OUTPUT.PUT_LINE('2');

      p_result.EXTEND;
      p_result( p_result.COUNT ) := dbms_lob.SUBSTR( i_str, p_end - p_start ,p_start);
      DBMS_OUTPUT.PUT_LINE('p_start -->'||p_start);
      DBMS_OUTPUT.PUT_LINE('p_end -->' ||p_end);
      DBMS_OUTPUT.PUT_LINE('substr --> '||dbms_lob.SUBSTR( i_str, p_end - p_start ,p_start));
      p_start := p_end + c_ld;
      p_end := DBMS_LOB.INSTR( i_str, i_delim, p_start,1 );
    END LOOP;
      DBMS_OUTPUT.PUT_LINE('3');

    IF p_start <= c_len + 1 THEN
      p_result.EXTEND;
      p_result( p_result.COUNT ) := dbms_lob.SUBSTR( i_str, c_len - p_start + 1, p_start );
    END IF;
  END IF;
 RETURN p_result;
END;

FUNCTION GETIDENTITY RETURN NUMBER
AS
BEGIN
RETURN DBMS_RANDOM.VALUE(54,9999999999);
END;

PROCEDURE HANDLEERROR(ERRORCODE NUMBER,MSG VARCHAR2)
AS
BEGIN
    -- NOTE: Oracle raise_application_error will terminate normal code flow , which T-SQL raiserror does not.
    raise_application_error(-20002,ERRORCODE||':'||MSG);
               --DBMS_OUTPUT.PUT_LINE(ERRORCODE||':'||MSG);
END;

PROCEDURE RAISERROR(ERRORCODE NUMBER,MSG VARCHAR2)
AS
BEGIN
    -- NOTE: Oracle raise_application_error will terminate normal code flow , which T-SQL raiserror does not.
               -- raise_application_error(ERRORCODE||':'||MSG);
               DBMS_OUTPUT.PUT_LINE(ERRORCODE||':'||MSG);
END;

FUNCTION CONVERT_STRING_TO_TIMESTAMP (ARG VARCHAR2) RETURN TS1
  AS
  BEGIN
   FOR i in DT_FORMATS.FIRST .. DT_FORMATS.LAST
   LOOP
    BEGIN
      RETURN TO_TIMESTAMP(ARG,DT_FORMATS(i));
   EXCEPTION
   WHEN OTHERS THEN
      NULL; -- Keep Trying
    END;
   END LOOP;
   --Attempt to cast one last time, but its really to designed to throw the error to the application when a string is not recognized
   RETURN  TO_TIMESTAMP(ARG,DT_FORMATS(1));
END; 

FUNCTION datepart_(p_part_expr IN VARCHAR2, p_date_expr IN TS1)
RETURN NUMBER
IS
  v_part VARCHAR2(15) := p_part_expr;
  v_timestamp TS1 := p_date_expr;
  v_wkday VARCHAR2(10);
  v_year VARCHAR2(4);
BEGIN
      v_part := UPPER(p_part_expr);
      IF v_part IN ('YEAR', 'YY', 'YYYY') THEN  RETURN TO_NUMBER(TO_CHAR(v_timestamp, 'YYYY'));
      ELSIF v_part IN ('QUARTER', 'QQ', 'Q')  THEN RETURN TO_NUMBER(TO_CHAR(v_timestamp, 'Q'));
      ELSIF v_part IN ('MONTH', 'MM', 'M') THEN RETURN TO_NUMBER(TO_CHAR(v_timestamp, 'MM'));
      ElSIF v_part IN ('DAYOFYEAR', 'DY', 'Y') THEN RETURN TO_NUMBER(TO_CHAR(v_timestamp, 'DDD'));
      ELSIF v_part IN ('DAY', 'DD', 'D') THEN RETURN TO_NUMBER(TO_CHAR(v_timestamp, 'DD'));
      ELSIF v_part IN ('WEEKDAY', 'DW', 'W') THEN RETURN TO_NUMBER(TO_CHAR(v_timestamp, 'D'));
      -- Oracle returns 1 short when compared to Sybase so adding 1 to the result.
      ElSIF v_part IN ('WEEK', 'WK', 'WW') THEN  
         v_year := TO_CHAR(v_timestamp, 'YYYY');
         FOR i in DT_FORMATS.FIRST .. DT_FORMATS.LAST
         LOOP
            BEGIN
              v_wkday := TO_CHAR(TO_DATE('01-01-'|| v_year, DT_FORMATS(i)), 'DAY');
              EXIT;
            EXCEPTION
             WHEN OTHERS THEN
                NULL; 
            END;
         END LOOP;
         IF v_wkday = TO_CHAR(v_timestamp, 'DAY') THEN
            RETURN TO_NUMBER(TO_CHAR(v_timestamp, 'WW'));
         ELSE
            RETURN TO_NUMBER(TO_CHAR(v_timestamp, 'WW')) + 1;
         END IF; 
      ElSIF v_part IN ('HOUR', 'HH') THEN RETURN TO_NUMBER(TO_CHAR(v_timestamp, 'HH24'));
      ElSIF v_part IN ('MINUTE', 'MI', 'N') THEN RETURN TO_NUMBER(TO_CHAR(v_timestamp, 'MI'));
      ElSIF v_part IN ('SECOND', 'SS', 'S') THEN RETURN TO_NUMBER(TO_CHAR(v_timestamp, 'SS'));
      ElSIF v_part IN ('MILLISECOND', 'MS', 'FF3') THEN RETURN TO_NUMBER(TO_CHAR(v_timestamp, 'FF3'));
      ElSIF v_part IN ('MICROSECOND', 'MCS', 'US', 'FF6') THEN RETURN TO_NUMBER(TO_CHAR(v_timestamp, 'FF6'));
      ElSIF v_part IN ('NANOSECOND', 'NS', 'FF9') THEN RETURN TO_NUMBER(TO_CHAR(v_timestamp, 'FF9'));
      ElSIF v_part IN ('CALYEAROFWEEK', 'CYR', 'IYYY') THEN RETURN TO_NUMBER(TO_CHAR(v_timestamp, 'IYYY')); 
      ElSIF v_part IN ('CALWEEKOFYEAR', 'CWK', 'IW') THEN RETURN TO_NUMBER(TO_CHAR(v_timestamp, 'IW'));
      -- Oracle returns 1 more when compared to Sybase so subtract 1 to the result.
      ElSIF v_part IN ('CALDAYOFWEEK', 'CDW', 'D') THEN RETURN TO_NUMBER(TO_CHAR(v_timestamp, 'D')) - 1;
      ELSE
        RETURN NULL;
      END IF;
EXCEPTION
    WHEN OTHERS THEN
      raise_application_error(-20000, DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
END datepart_;

FUNCTION datepart(p_part_expr IN VARCHAR2, p_date_str IN VARCHAR2)
RETURN NUMBER
IS
   v_ts TS1;
BEGIN  
      v_ts := CONVERT_STRING_TO_TIMESTAMP(p_date_str);
      RETURN datepart_(p_part_expr, v_ts);
EXCEPTION
    WHEN OTHERS THEN
      raise_application_error(-20000, DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
END datepart;

FUNCTION FIND_IN_SET(
  i_value  IN  VARCHAR2,
  i_list   IN  clob,
  i_delim  IN  VARCHAR2 DEFAULT ','
) RETURN INT DETERMINISTIC
AS
  p_result       INT       := 0;
  p_start        NUMBER(5) := 1;
  p_end          NUMBER(5);
  c_len CONSTANT NUMBER(5) := LENGTH( i_list );
  c_ld  CONSTANT NUMBER(5) := LENGTH( i_delim );
BEGIN
  IF c_len > 0 THEN
    p_end := INSTR( i_list, i_delim, p_start );
    WHILE p_end > 0 LOOP
      p_result := p_result + 1;
      IF ( SUBSTR( i_list, p_start, p_end - p_start ) = i_value )
      THEN
        RETURN p_result;
      END IF;
      p_start := p_end + c_ld;
      p_end := INSTR( i_list, i_delim, p_start );
    END LOOP;
    IF p_start <= c_len + 1
       AND SUBSTR( i_list, p_start, c_len - p_start + 1 ) = i_value
    THEN
      RETURN p_result + 1;
    END IF;
  END IF;
  RETURN 0;
END;

FUNCTION FIND_IN_SET(
  i_value  IN  VARCHAR2,
  i_list   IN  VARCHAR2,
  i_delim  IN  VARCHAR2 DEFAULT ','
) RETURN INT DETERMINISTIC
AS
  p_result       INT       := 0;
  p_start        NUMBER(5) := 1;
  p_end          NUMBER(5);
  c_len CONSTANT NUMBER(5) := LENGTH( i_list );
  c_ld  CONSTANT NUMBER(5) := LENGTH( i_delim );
BEGIN
  IF c_len > 0 THEN
    p_end := INSTR( i_list, i_delim, p_start );
    WHILE p_end > 0 LOOP
      p_result := p_result + 1;
      IF ( SUBSTR( i_list, p_start, p_end - p_start ) = i_value )
      THEN
        RETURN p_result;
      END IF;
      p_start := p_end + c_ld;
      p_end := INSTR( i_list, i_delim, p_start );
    END LOOP;
    IF p_start <= c_len + 1
       AND SUBSTR( i_list, p_start, c_len - p_start + 1 ) = i_value
    THEN
      RETURN p_result + 1;
    END IF;
  END IF;
  RETURN 0;
END;

FUNCTION SUBSTRING_INDEX
(
  v_InString IN VARCHAR2,
  v_Delimiter IN VARCHAR2,
  v_Count IN NUMBER
)
RETURN VARCHAR2
AS
   v_Pos NUMBER(10,0);
   v_OrigLength NUMBER(10,0) := LENGTH(v_InString);
   v_DelimiterLength NUMBER(10,0) := LENGTH(v_Delimiter);
   -- Put all delimiter offsets into @DelimiterOffsets, they get numbered automatically.
   v_CurrentOffset NUMBER(10,0) := 0;
   -- This number is guaranteed to be > 0.
   v_DelimitersFound NUMBER(10,0);
   v_StartSubstring NUMBER(10,0) := 0;
   v_EndSubstring NUMBER(10,0) := v_OrigLength;
   r_delimiter VARCHAR2(10);

BEGIN
   -- If @Count is zero, we return '' as per spec
   IF v_Count = 0 THEN
      RETURN '';
   END IF;

   -- Prime the pump.
   v_Pos := INSTR(v_InString, v_Delimiter, 1);

   -- If the delimiter does not exist in @InString, return the whole string
   IF v_Pos = 0 THEN
      RETURN v_InString;
   END IF;
   
-- ABCDEF,GHIJKLM,OPQRST,UVW,XYZ12,34567,90
-- 1234567890123456789012345678901234567890
   r_delimiter := REPLACE(v_Delimiter,'|','\|');
   r_delimiter := REPLACE(r_delimiter,'*','\*');
   r_delimiter := REPLACE(r_delimiter,'[','\[');
   r_delimiter := REPLACE(r_delimiter,']','\]');
   r_delimiter := REPLACE(r_delimiter,'(','\(');
   r_delimiter := REPLACE(r_delimiter,')','\)');
   SELECT REGEXP_COUNT(v_InString, r_delimiter) INTO v_DelimitersFound from dual;
   --   dbms_output.put_line('v_DelimitersFound --> ' || v_DelimitersFound);
   IF ABS(v_count) > v_DelimitersFound THEN
       RETURN v_InString;
   END IF;
   
   IF v_count > 0 THEN
       v_EndSubstring := INSTR(v_InString, v_Delimiter,1, v_count) - 1;
   ELSE
       v_StartSubstring := INSTR(v_InString, v_Delimiter,1, v_DelimitersFound + v_count + 1) + v_DelimiterLength;
   END IF;
--   dbms_output.put_line('v_DelimitersFound --> ' ||v_DelimitersFound);
--   dbms_output.put_line('v_count --> ' ||v_count);
--   dbms_output.put_line('v_StartSubstring --> ' ||v_StartSubstring);
--   dbms_output.put_line('v_EndSubstring --> ' ||v_EndSubstring);
   RETURN SUBSTR(v_InString, v_StartSubstring, v_EndSubstring);
   
END;



END utils;
/

--------------------------------------------------------
--  DDL for Function FIND_IN_SET
--------------------------------------------------------

CREATE OR REPLACE NONEDITIONABLE FUNCTION "FIND_IN_SET" (
  i_value  IN  VARCHAR2,
  i_list   IN  clob,
  i_delim  IN  VARCHAR2 DEFAULT ','
) RETURN INT DETERMINISTIC
AS
  p_result       INT       := 0;
  p_start        NUMBER(5) := 1;
  p_end          NUMBER(5);
  c_len CONSTANT NUMBER(5) := LENGTH( i_list );
  c_ld  CONSTANT NUMBER(5) := LENGTH( i_delim );
BEGIN
  IF c_len > 0 THEN
    p_end := INSTR( i_list, i_delim, p_start );
    WHILE p_end > 0 LOOP
      p_result := p_result + 1;
      IF ( SUBSTR( i_list, p_start, p_end - p_start ) = i_value )
      THEN
        RETURN p_result;
      END IF;
      p_start := p_end + c_ld;
      p_end := INSTR( i_list, i_delim, p_start );
    END LOOP;
    IF p_start <= c_len + 1
       AND SUBSTR( i_list, p_start, c_len - p_start + 1 ) = i_value
    THEN
      RETURN p_result + 1;
    END IF;
  END IF;
  RETURN 0;
END;

/
--------------------------------------------------------
--  DDL for Function FUNC_ESCAPE_INPUT_FOR_IN_OPERATOR
--------------------------------------------------------

CREATE OR REPLACE NONEDITIONABLE FUNCTION "FUNC_ESCAPE_INPUT_FOR_IN_OPERATOR" 
(
  v__input IN VARCHAR2
)
RETURN VARCHAR2
AS
   v_result VARCHAR2(4000);
   v_element VARCHAR2(4000);
   v_ctr NUMBER(19,0);

BEGIN
   v_ctr := 1 ;
   v_result := ' ' ;
   WHILE 1 = 1 
   LOOP 

      BEGIN
         v_element := func_split_str(v__input, ',', v_ctr) ;
         IF v_element = ' ' THEN
          EXIT;
         END IF;
         IF v_result != ' ' THEN
          v_result := v_result || ',' ;
         END IF;
         v_result := v_result || '''' || (v_element) || '''' ;
         v_ctr := v_ctr + 1 ;

      END;
   END LOOP;
   RETURN (v_result);

EXCEPTION WHEN OTHERS THEN raise_application_error(-20001,'An error was encountered - '||SQLCODE||' -ERROR- '||SQLERRM);
END;



/
--------------------------------------------------------
--  DDL for Function FUNC_SPLIT_STR
--------------------------------------------------------

CREATE OR REPLACE NONEDITIONABLE FUNCTION "FUNC_SPLIT_STR" 
(
  /*
     *   SSMA informational messages:
     *   M2SS0055: Data type was converted to VARCHAR(MAX) according to character set mapping for latin1 character set
     */
  v_X IN VARCHAR2,
  /*
     *   SSMA informational messages:
     *   M2SS0055: Data type was converted to VARCHAR(MAX) according to character set mapping for latin1 character set
     */
  v_delim IN VARCHAR2,
  v_pos IN NUMBER
)
RETURN VARCHAR2
AS
   v_posSubString VARCHAR2(4000);
   v_posMinusOneSubstring VARCHAR2(4000);

BEGIN
   v_posSubString := (substring_index(v_X, v_delim, v_pos)) ;
   v_posMinusOneSubstring := (substring_index(v_X, v_delim, v_pos - 1)) ;
   RETURN REPLACE(SUBSTR(v_posSubString, LENGTHB(v_posMinusOneSubstring) + 1, LENGTHB(v_posSubString)), v_delim, '');

EXCEPTION WHEN OTHERS THEN raise_application_error(-20001,'An error was encountered - '||SQLCODE||' -ERROR- '||SQLERRM);
END;



/
--------------------------------------------------------
--  DDL for Function GETIDENTITY
--------------------------------------------------------

CREATE OR REPLACE NONEDITIONABLE FUNCTION "GETIDENTITY" RETURN NUMBER
AS 
IDENTITY_VALUE NUMBER(10);
BEGIN
 RETURN IDENTITY_VALUE;
END;

--DECLARE
--v_index1 NUMBER(10,0) := 0;
--BEGIN 
--v_index1 := GETIDENTITY();



/
--------------------------------------------------------
--  DDL for Function LEASTINT
--------------------------------------------------------

CREATE OR REPLACE NONEDITIONABLE FUNCTION "LEASTINT" 
(
  v_first IN NUMBER,
  v_second IN NUMBER
)
RETURN NUMBER
AS

BEGIN
   RETURN CASE 
               WHEN v_first < v_second THEN v_first
               WHEN v_second < v_first THEN v_second
   ELSE v_first
      END;

EXCEPTION WHEN OTHERS THEN raise_application_error(-20001,'An error was encountered - '||SQLCODE||' -ERROR- '||SQLERRM);
END;



/
--------------------------------------------------------
--  DDL for Function NEWID
--------------------------------------------------------

CREATE OR REPLACE NONEDITIONABLE FUNCTION "NEWID" RETURN VARCHAR2
IS
GUID VARCHAR2(50);
BEGIN
   GUID := (RAWTOHEX(SYS_GUID()));
   RETURN SUBSTR(GUID,1,8) || '-' || SUBSTR(GUID,9,4) || '-' || SUBSTR(GUID,13,4) || '-' || SUBSTR(GUID,17,4) || '-' || SUBSTR(GUID,21,12);
END NEWID;


/
--------------------------------------------------------
--  DDL for Function LISTAGG
--------------------------------------------------------

CREATE OR REPLACE NONEDITIONABLE FUNCTION "LISTAGG" (agg varchar2, delim varchar2 default ',')
return clob
parallel_enable aggregate using listagg_clob_t;

/
--------------------------------------------------------
--  DDL for Function SUBSTRING_INDEX
--------------------------------------------------------

CREATE OR REPLACE NONEDITIONABLE FUNCTION "SUBSTRING_INDEX" 
(
  v_InString IN VARCHAR2,
  v_Delimiter IN VARCHAR2,
  v_Count IN NUMBER
)
RETURN VARCHAR2
AS
   v_Pos NUMBER(10,0);
   v_OrigLength NUMBER(10,0) := LENGTH(v_InString);
   v_DelimiterLength NUMBER(10,0) := LENGTH(v_Delimiter);
   -- Put all delimiter offsets into @DelimiterOffsets, they get numbered automatically.
   v_CurrentOffset NUMBER(10,0) := 0;
   -- This number is guaranteed to be > 0.
   v_DelimitersFound NUMBER(10,0);
   v_StartSubstring NUMBER(10,0) := 1;
   v_EndSubstring NUMBER(10,0) := v_OrigLength;

BEGIN

   -- If @Count is zero, we return '' as per spec
   IF v_Count = 0 THEN
      RETURN '';
   END IF;

   -- Prime the pump.
   v_Pos := INSTR(v_InString, v_Delimiter, 1);

   -- If the delimiter does not exist in @InString, return the whole string
   IF v_Pos = 0 THEN
      RETURN v_InString;
   END IF;

-- ABCDEF,GHIJKLM,OPQRST,UVW,XYZ12,34567,90
-- 1234567890123456789012345678901234567890

   IF v_count = 1 THEN
       RETURN SUBSTR(v_InString, v_StartSubstring, INSTR(v_InString, v_Delimiter, 1) - v_DelimiterLength);
   END IF;

   v_StartSubstring := INSTR(v_InString, v_Delimiter, 1, v_count - 1);
   v_EndSubstring   := INSTR(v_InString, v_Delimiter, 1, v_count);

-- If the position passed is beyond the delimiters found within the string, return full string
   IF v_StartSubstring = 0 THEN
      RETURN v_InString;
   ELSE
       v_StartSubstring := v_StartSubstring + v_DelimiterLength;  -- Get next Position
       IF v_EndSubstring = 0 THEN -- if looking for last item
           v_EndSubstring := v_EndSubstring - v_DelimiterLength;    -- Get previous Position   
           RETURN SUBSTR(v_InString, v_StartSubstring, (v_OrigLength - v_StartSubstring) + 1);
       ELSE  -- Looking for substring
           v_EndSubstring := v_EndSubstring - v_DelimiterLength;    -- Get previous Position
           RETURN SUBSTR(v_InString, v_StartSubstring, (v_EndSubstring - v_StartSubstring) + 1);
       END IF;
   END IF;

END;

/
--------------------------------------------------------
--  DDL for Function STRING_AGG
--------------------------------------------------------

create or replace NONEDITIONABLE FUNCTION "STRING_AGG" (agg varchar2, delim varchar2 default ',')
return clob
parallel_enable aggregate using listagg_clob_t;

/
--------------------------------------------------------
--  DDL for Function SUBSTRING_TEST_VARCHAR2
--------------------------------------------------------

CREATE OR REPLACE NONEDITIONABLE FUNCTION "SUBSTRING_TEST_VARCHAR2" 
(
  v_InString IN VARCHAR2,
  v_Delimiter IN VARCHAR2,
  v_Count IN NUMBER
)
RETURN VARCHAR2
AS
   v_Pos NUMBER(10,0);
   v_OrigLength NUMBER(10,0) := LENGTH(v_InString);
   v_DelimiterLength NUMBER(10,0) := LENGTH(v_Delimiter);
   -- Put all delimiter offsets into @DelimiterOffsets, they get numbered automatically.
   v_CurrentOffset NUMBER(10,0) := 0;
   -- This number is guaranteed to be > 0.
   v_DelimitersFound NUMBER(10,0);
   v_StartSubstring NUMBER(10,0) := 0;
   v_EndSubstring NUMBER(10,0) := v_OrigLength;
   r_delimiter VARCHAR2(10);

BEGIN
   -- If @Count is zero, we return '' as per spec
   IF v_Count = 0 THEN
      RETURN '';
   END IF;

   -- Prime the pump.
   v_Pos := INSTR(v_InString, v_Delimiter, 1);

   -- If the delimiter does not exist in @InString, return the whole string
   IF v_Pos = 0 THEN
      RETURN v_InString;
   END IF;
   
-- ABCDEF,GHIJKLM,OPQRST,UVW,XYZ12,34567,90
-- 1234567890123456789012345678901234567890
   r_delimiter := REPLACE(v_Delimiter,'|','\|');
   r_delimiter := REPLACE(r_delimiter,'*','\*');
   r_delimiter := REPLACE(r_delimiter,'[','\[');
   r_delimiter := REPLACE(r_delimiter,']','\]');
   r_delimiter := REPLACE(r_delimiter,'(','\(');
   r_delimiter := REPLACE(r_delimiter,')','\)');
   SELECT REGEXP_COUNT(v_InString, r_delimiter) INTO v_DelimitersFound from dual;
   --   dbms_output.put_line('v_DelimitersFound --> ' || v_DelimitersFound);
   IF ABS(v_count) > v_DelimitersFound THEN
       RETURN v_InString;
   END IF;
   
   IF v_count > 0 THEN
       v_EndSubstring := INSTR(v_InString, v_Delimiter,1, v_count) - 1;
   ELSE
       v_StartSubstring := INSTR(v_InString, v_Delimiter,1, v_DelimitersFound + v_count + 1) + v_DelimiterLength;
   END IF;
--   dbms_output.put_line('v_DelimitersFound --> ' ||v_DelimitersFound);
--   dbms_output.put_line('v_count --> ' ||v_count);
--   dbms_output.put_line('v_StartSubstring --> ' ||v_StartSubstring);
--   dbms_output.put_line('v_EndSubstring --> ' ||v_EndSubstring);
   RETURN SUBSTR(v_InString, v_StartSubstring, v_EndSubstring);
   
END SUBSTRING_TEST_VARCHAR2;

/




























--  DDL for Table accountcommunication

CREATE TABLE "accountcommunication" (
  "Type_id" NVARCHAR2(50) DEFAULT NULL, 
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "Account_id" NUMBER(19, 0) DEFAULT NULL, 
  "sequence" NUMBER(10, 0) DEFAULT NULL, 
  "value" NVARCHAR2(100) DEFAULT NULL, 
  "extension" NVARCHAR2(50) DEFAULT NULL, 
  "description" NVARCHAR2(50) DEFAULT NULL
);
--  DDL for Table accountlevelactionlimit

CREATE TABLE "accountlevelactionlimit" (
  "id" VARCHAR2(50 CHAR), 
  "contractId" VARCHAR2(50 CHAR), 
  "coreCustomerId" VARCHAR2(50 CHAR), 
  "policyId" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "isPortfolio" VARCHAR2(45 CHAR) DEFAULT 'false', 
  "accountId" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "featureId" VARCHAR2(50 CHAR), 
  "actionId" VARCHAR2(255 CHAR), 
  "limitGroupId" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "limitTypeId" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "value" NUMBER(20, 2) DEFAULT NULL, 
  "isNewAction" NUMBER(3, 0) DEFAULT '0', 
  "createdby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "modifiedby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "createdts" TIMESTAMP (0) DEFAULT SYSTIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (0) DEFAULT SYSTIMESTAMP, 
  "synctimestamp" TIMESTAMP (0) DEFAULT SYSTIMESTAMP, 
  "softdeleteflag" NUMBER(3, 0) DEFAULT '0', 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table accounts

CREATE TABLE "accounts" (
  "Account_id" VARCHAR2(50 CHAR), 
  "AccountName" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "UserName" VARCHAR2(40 CHAR) DEFAULT NULL, 
  "ExternalBankidentity_id" NVARCHAR2(50) DEFAULT NULL, 
  "CurrencyCode" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "AvailableBalance" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "AccountHolder" VARCHAR2(500 CHAR) DEFAULT NULL, 
  "Address" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "Scheme" VARCHAR2(5 CHAR) DEFAULT NULL, 
  "Number" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "error" VARCHAR2(1 CHAR) DEFAULT NULL, 
  "Type_id" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "Product_id" NUMBER(10, 0) DEFAULT NULL, 
  "Bank_id" VARCHAR2(50 CHAR) DEFAULT '1', 
  "User_id" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "Name" VARCHAR2(100 CHAR) DEFAULT NULL, 
  "isBusinessAccount" NUMBER(1, 0) DEFAULT (0), 
  "Status_id" NUMBER(19, 0) DEFAULT NULL, 
  "StatusDesc" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "SupportDeposit" NUMBER(10, 0) DEFAULT '0', 
  "SupportBillPay" NUMBER(10, 0) DEFAULT '0', 
  "SupportTransferFrom" NUMBER(10, 0) DEFAULT '0', 
  "SupportTransferTo" NUMBER(10, 0) DEFAULT '0', 
  "ShowTransactions" NUMBER(1, 0) DEFAULT '0', 
  "CurrentBalance" NUMBER(10, 2) DEFAULT '0.00', 
  "InterestRate" NUMBER(10, 2) DEFAULT '0.00', 
  "AvailableCredit" NUMBER(10, 2) DEFAULT '0.00', 
  "MinimumDue" NUMBER(10, 2) DEFAULT '0.00', 
  "DueDate" DATE DEFAULT NULL, 
  "PrincipalValue" NUMBER(10, 2) DEFAULT '0.00', 
  "FirstPaymentDate" DATE DEFAULT NULL, 
  "ClosingDate" DATE DEFAULT NULL, 
  "PaymentTerm" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "OpeningDate" DATE DEFAULT NULL, 
  "MaturityDate" DATE DEFAULT NULL, 
  "TransactionLimit" NUMBER(10, 2) DEFAULT '0.00', 
  "TransferLimit" NUMBER(10, 2) DEFAULT '0.00', 
  "NickName" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "LastStatementBalance" NUMBER(10, 2) DEFAULT '0.00', 
  "AvailablePoints" NUMBER(10, 0) DEFAULT '0', 
  "OutstandingBalance" NUMBER(10, 2) DEFAULT '0.00', 
  "CreditCardNumber" VARCHAR2(100 CHAR) DEFAULT NULL, 
  "IsPFM" NUMBER(1, 0) DEFAULT '0', 
  "SupportCardlessCash" NUMBER(10, 0) DEFAULT '0', 
  "FavouriteStatus" NUMBER(10, 0) DEFAULT '0', 
  "MaturityOption" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "RoutingNumber" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "SwiftCode" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "JointHolders" VARCHAR2(500 CHAR) DEFAULT NULL, 
  "DividendRate" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "DividendYTD" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "LastDividendPaidAmount" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "LastDividendPaidDate" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "PreviousYearDividend" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "BondInterest" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "BondInterestLastYear" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "TotalCreditMonths" VARCHAR2(50 CHAR) DEFAULT '0', 
  "TotalDebitsMonth" VARCHAR2(50 CHAR) DEFAULT '0', 
  "CurrentAmountDue" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "PaymentDue" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "LastPaymentDate" TIMESTAMP (6) DEFAULT NULL, 
  "LastPaymentAmount" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "LateFeesDue" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "CreditLimit" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "InterestPaidYTD" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "InterestPaidPreviousYTD" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "UnpaidInterest" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "PaymentMethod" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "RegularPaymentAmount" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "DividendPaidYTD" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "DividendLastPaidAmount" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "DividendLastPaidDate" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "PreviousYearsDividends" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "PendingDeposit" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "PendingWithdrawal" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "InterestEarned" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "maturityAmount" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "principalBalance" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "OriginalAmount" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "payoffAmount" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "BsbNum" NUMBER(10, 0) DEFAULT NULL, 
  "PayOffCharge" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "InterestPaidLastYear" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "EStatementmentEnable" NUMBER(1, 0) DEFAULT '0', 
  "Phone_id" NUMBER(10, 0) DEFAULT NULL, 
  "LastUpdated" TIMESTAMP (6) DEFAULT NULL, 
  "BankName" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "AccountPreference" NUMBER(10, 0) DEFAULT (0), 
  "InternalAccount" VARCHAR2(1 CHAR) DEFAULT NULL, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT '0', 
  "product" NCLOB, 
  "email" VARCHAR2(150 CHAR) DEFAULT NULL, 
  "jointAccountHolder1" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "jointAccountHolder2" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "bankAddress" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "intermediaryBankName" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "intermediaryBankAddress" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "intermediaryBankSwiftCode" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "phone" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "accountSubType" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "description" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "schemeName" VARCHAR2(40 CHAR) DEFAULT NULL, 
  "identification" VARCHAR2(256 CHAR) DEFAULT NULL, 
  "secondaryIdentification" VARCHAR2(34 CHAR) DEFAULT NULL, 
  "servicerSchemeName" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "servicerIdentification" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "dataCreditDebitIndicator" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "dataType" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "dataDateTime" TIMESTAMP (6), 
  "dataCreditLineIncluded" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "dataCreditLineType" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "dataCreditLineAmount" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "dataCreditLineCurrency" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "IBAN" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "adminProductId" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "TaxId" VARCHAR2(45 CHAR), 
  "UpdatedBy" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "ActualUpdatedBY" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "Organization_id" VARCHAR2(50 CHAR), 
  "Membership_id" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "MembershipName" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "ownership" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "arrangementId" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table accountsstatementfiles

CREATE TABLE "accountsstatementfiles" (
  "id" NVARCHAR2(50), 
  "userId" NVARCHAR2(50), 
  "fileContent" NCLOB, 
  "fileName" NVARCHAR2(150), 
  "status" NVARCHAR2(45), 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "modifiedBy" NVARCHAR2(50), 
  "fileType" NVARCHAR2(50), 
  "failureMessage" NVARCHAR2(250), 
  "fromDate" NVARCHAR2(50), 
  "toDate" NVARCHAR2(55), 
  "accountIds" NCLOB,
  "legalEntityId" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table accountstatement

CREATE TABLE "accountstatement" (
  "Id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "description" NVARCHAR2(100) DEFAULT NULL, 
  "statementLink" NVARCHAR2(100) DEFAULT NULL, 
  "Account_id" NVARCHAR2(50), 
  "month" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP
);
--  DDL for Table accounttype

CREATE TABLE "accounttype" (
  "TypeID" NVARCHAR2(50), 
  "TypeDescription" NVARCHAR2(100) DEFAULT NULL, 
  "displayName" NVARCHAR2(100) DEFAULT NULL, 
  "transactionLimit" NUMBER(10, 2) DEFAULT NULL, 
  "transferLimit" NUMBER(10, 2) DEFAULT NULL, 
  "dailyDepositLimit" NUMBER(10, 2) DEFAULT NULL, 
  "monthlyDepositLimit" NUMBER(10, 2) DEFAULT NULL, 
  "termsAndConditions" NVARCHAR2(2000) DEFAULT NULL, 
  "features" NVARCHAR2(2000) DEFAULT NULL, 
  "rates" NVARCHAR2(2000) DEFAULT NULL, 
  "info" NVARCHAR2(2000) DEFAULT NULL, 
  "supportChecks" NUMBER(10, 0) DEFAULT (0), 
  "countryCode" NVARCHAR2(45) DEFAULT NULL, 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table achaccountstype

CREATE TABLE "achaccountstype" (
  "id" NUMBER(10, 0), 
  "accountType" NVARCHAR2(50)
);
--  DDL for Table achfile

CREATE TABLE "achfile" (
  "achFile_id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "achFileName" NVARCHAR2(45) DEFAULT NULL, 
  "featureActionId" NVARCHAR2(50) DEFAULT NULL, 
  "softDelete" NUMBER(1, 0) DEFAULT (0), 
  "debitAmount" FLOAT(126) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "requestType" NVARCHAR2(45) DEFAULT NULL, 
  "numberOfCredits" NUMBER(10, 0) DEFAULT NULL, 
  "numberOfDebits" NUMBER(10, 0) DEFAULT NULL, 
  "numberOfPrenotes" NUMBER(10, 0) DEFAULT NULL, 
  "requestId" NUMBER(19, 0) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "contents" CLOB, 
  "fileSize" FLOAT(126), 
  "creditAmount" FLOAT(126) DEFAULT NULL, 
  "numberOfRecords" NUMBER(10, 0) DEFAULT NULL, 
  "achFileFormatType_id" NUMBER(10, 0) DEFAULT NULL, 
  "status" NVARCHAR2(50) DEFAULT NULL, 
  "companyId" NVARCHAR2(50) DEFAULT NULL, 
  "roleId" NVARCHAR2(45) DEFAULT NULL, 
  "actedBy" NVARCHAR2(45) DEFAULT NULL, 
  "updatedts" TIMESTAMP (6), 
  "confirmationNumber" NVARCHAR2(45) DEFAULT NULL, 
  "approvalAccounts" NCLOB, 
  "debitAccounts" NCLOB, 
  "transactionAmount" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "serviceCharge" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "transactionCurrency" VARCHAR2(50 CHAR) DEFAULT NULL
);
--  DDL for Table achfileformattype

CREATE TABLE "achfileformattype" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "fileType" NVARCHAR2(45), 
  "fileextension" NVARCHAR2(10) DEFAULT NULL, 
  "mimetype" NVARCHAR2(100) DEFAULT NULL
);

--  DDL for Table addetails

CREATE TABLE "addetails" (
  "id" NUMBER(19, 0), 
  "action1" NVARCHAR2(100) DEFAULT NULL, 
  "action2" NVARCHAR2(100) DEFAULT NULL, 
  "imageURL" NVARCHAR2(500) DEFAULT NULL, 
  "description" NVARCHAR2(100) DEFAULT NULL, 
  "adType" NVARCHAR2(45) DEFAULT NULL, 
  "title" NVARCHAR2(45) DEFAULT NULL, 
  "user_id" NUMBER(10, 0) DEFAULT NULL, 
  "actionType" NVARCHAR2(50) DEFAULT NULL, 
  "imageURL2" NVARCHAR2(500) DEFAULT NULL
);
--  DDL for Table address

CREATE TABLE "address" (
  "id" NVARCHAR2(50), 
  "Region_id" NVARCHAR2(50) DEFAULT NULL, 
  "City_id" NVARCHAR2(50) DEFAULT NULL, 
  "addressLine1" NVARCHAR2(100) DEFAULT NULL, 
  "addressLine2" NVARCHAR2(100) DEFAULT NULL, 
  "addressLine3" NVARCHAR2(100) DEFAULT NULL, 
  "zipCode" NVARCHAR2(20) DEFAULT NULL, 
  "latitude" NVARCHAR2(20) DEFAULT NULL, 
  "logitude" NVARCHAR2(20) DEFAULT NULL, 
  "isPreferredAddress" NUMBER(1, 0) DEFAULT NULL, 
  "cityName" NVARCHAR2(100) DEFAULT NULL, 
  "User_id" NVARCHAR2(50) DEFAULT NULL, 
  "country" NVARCHAR2(50) DEFAULT NULL, 
  "type" NVARCHAR2(6) DEFAULT NULL, 
  "state" NVARCHAR2(100) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table addresstype

CREATE TABLE "addresstype" (
  "id" NVARCHAR2(50), 
  "Description" NVARCHAR2(100) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);

--  DDL for Table additionaldata


CREATE TABLE "additionaldata" ("id" NVARCHAR2(50), "Object_id" NVARCHAR2(50), "ObjectType" NVARCHAR2(50), "AdditionalField_id" NVARCHAR2(50) DEFAULT NULL, "FieldValue" NVARCHAR2(2000) DEFAULT NULL, "createdby" NVARCHAR2(50) DEFAULT NULL, "modifiedby" NVARCHAR2(50) DEFAULT NULL, "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "softdeleteflag" NUMBER(1,0) DEFAULT (0));

--  DDL for Table advertisements

CREATE TABLE "advertisements" (
  "id" NUMBER(19, 0), 
  "actionType" NVARCHAR2(45) DEFAULT NULL, 
  "action" NVARCHAR2(45) DEFAULT NULL, 
  "adimagesrc" NVARCHAR2(45) DEFAULT NULL, 
  "url" NVARCHAR2(45) DEFAULT NULL, 
  "user_id" NUMBER(10, 0) DEFAULT NULL
);
--  DDL for Table alert

CREATE TABLE "alert" (
  "id" NVARCHAR2(50), 
  "AlertType_id" NVARCHAR2(50) DEFAULT NULL, 
  "Name" NVARCHAR2(50), 
  "Description" NVARCHAR2(250), 
  "IsSubscriptionNeeded" NUMBER(3, 0) DEFAULT (0), 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "IsSmsActive" NUMBER(1, 0) DEFAULT (0), 
  "IsEmailActive" NUMBER(1, 0) DEFAULT (0), 
  "IsPushActive" NUMBER(1, 0) DEFAULT (0), 
  "AlertContent" NVARCHAR2(250) DEFAULT NULL, 
  "Account_id" NVARCHAR2(50) DEFAULT NULL, 
  "isActive" NUMBER(1, 0) DEFAULT NULL, 
  "hasValue" NUMBER(1, 0) DEFAULT NULL, 
  "currentValue" NVARCHAR2(50) DEFAULT NULL, 
  "defaultValue" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table alertattribute

CREATE TABLE "alertattribute" (
  "id" NVARCHAR2(50), 
  "LanguageCode" NVARCHAR2(10), 
  "name" NVARCHAR2(255) DEFAULT NULL, 
  "type" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table alertattributelistvalues

CREATE TABLE "alertattributelistvalues" (
  "id" NVARCHAR2(50), 
  "AlertAttributeId" NVARCHAR2(50), 
  "LanguageCode" NVARCHAR2(10), 
  "name" NVARCHAR2(250) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0),
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table alertcategorychannel

CREATE TABLE "alertcategorychannel" (
  "ChannelID" NVARCHAR2(50), 
  "AlertCategoryId" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(200 CHAR)
);
--  DDL for Table alertcondition

CREATE TABLE "alertcondition" (
  "id" NVARCHAR2(25), 
  "Name" NVARCHAR2(255) DEFAULT NULL, 
  "LanguageCode" NVARCHAR2(10), 
  "NoOfFields" NUMBER(10, 0) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table alertcontentfields

CREATE TABLE "alertcontentfields" (
  "Code" NVARCHAR2(50), 
  "Name" NVARCHAR2(255) DEFAULT NULL, 
  "DefaultValue" NVARCHAR2(255) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table alertfrequency

CREATE TABLE "alertfrequency" (
  "id" VARCHAR2(10 CHAR), 
  "sequence" NUMBER(10, 0) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(5, 0) DEFAULT '0'
);
--  DDL for Table alertfrequencyjobexectime

CREATE TABLE "alertfrequencyjobexectime" (
  "id" NUMBER(10, 0), 
  "lastExecTime" TIMESTAMP (6)
);
--  DDL for Table alertfrequencytext

CREATE TABLE "alertfrequencytext" (
  "alertFrequencyId" VARCHAR2(10 CHAR), 
  "languageCode" NVARCHAR2(10), 
  "displayName" VARCHAR2(255 CHAR) DEFAULT NULL, 
  "description" VARCHAR2(1000 CHAR) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(5, 0) DEFAULT '0'
);
--  DDL for Table alertfrequencytime

CREATE TABLE "alertfrequencytime" (
  "id" VARCHAR2(200 CHAR)
);
--  DDL for Table alerthistory

CREATE TABLE "alerthistory" (
  "id" NVARCHAR2(100), 
  "EventId" NUMBER(10, 0) DEFAULT NULL, 
  "AlertSubTypeId" NVARCHAR2(75), 
  "AlertTypeId" NVARCHAR2(50), 
  "AlertCategoryId" NVARCHAR2(50) DEFAULT NULL, 
  "AlertStatusId" NVARCHAR2(50), 
  "Customer_Id" NVARCHAR2(50) DEFAULT NULL, 
  "LanguageCode" NVARCHAR2(10) DEFAULT NULL, 
  "ChannelId" NVARCHAR2(50) DEFAULT NULL, 
  "Status" NVARCHAR2(50) DEFAULT NULL, 
  "Subject" NVARCHAR2(255) DEFAULT NULL, 
  "Message" NCLOB, 
  "SenderName" NVARCHAR2(255) DEFAULT NULL, 
  "SenderEmail" NVARCHAR2(255) DEFAULT NULL, 
  "ReferenceNumber" NVARCHAR2(100) DEFAULT NULL, 
  "DispatchDate" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "ErrorMessage" NCLOB, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "coreCustomerId" VARCHAR2(45 CHAR)
);
--  DDL for Table alertrecipienttype

CREATE TABLE "alertrecipienttype" (
  "id" NUMBER(3, 0), 
  "name" VARCHAR2(20 CHAR), 
  "isaccountlevel" NUMBER(5, 0) DEFAULT '0', 
  "servicename" VARCHAR2(50 CHAR), 
  "operationname" VARCHAR2(50 CHAR), 
  "inputparamsmapping" VARCHAR2(1000 CHAR), 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "companyLegalUnit" VARCHAR2(200 CHAR)
);
--  DDL for Table alertsubtype

CREATE TABLE "alertsubtype" (
  "id" NVARCHAR2(75), 
  "AlertTypeId" NVARCHAR2(50), 
  "Name" NVARCHAR2(255) DEFAULT NULL, 
  "isAccountLevel" NUMBER(3, 0) DEFAULT '0', 
  "attributeId" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "alertConditionId" VARCHAR2(25 CHAR) DEFAULT NULL, 
  "value1" VARCHAR2(255 CHAR) DEFAULT NULL, 
  "value2" VARCHAR2(255 CHAR) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "Description" NVARCHAR2(1000) DEFAULT NULL, 
  "isGlobal" NUMBER(3, 0) DEFAULT '0', 
  "defaultFrequencyId" VARCHAR2(10 CHAR) DEFAULT NULL, 
  "defaultFrequencyValue" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "defaultFrequencyTime" VARCHAR2(200 CHAR) DEFAULT NULL, 
  "recipienttype" NUMBER(3, 0) DEFAULT (1), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "isAutoSubscribeEnabled" NUMBER(1, 0) DEFAULT (0), 
  "externalSystem" NUMBER(5, 0) DEFAULT '0', 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table alertsubtypeaccounttype

CREATE TABLE "alertsubtypeaccounttype" (
  "accountTypeId" VARCHAR2(50 CHAR), 
  "alertSubTypeId" NVARCHAR2(75), 
  "createdby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "modifiedby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(5, 0) DEFAULT '0', 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT (null)
);
--  DDL for Table alertsubtypeapp

CREATE TABLE "alertsubtypeapp" (
  "appId" NVARCHAR2(50), 
  "alertSubTypeId" NVARCHAR2(75), 
  "createdby" VARCHAR2(50 CHAR), 
  "modifiedby" VARCHAR2(50 CHAR), 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(5, 0) DEFAULT '0', 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT (null)
);
--  DDL for Table alertsubtypechannel

CREATE TABLE "alertsubtypechannel" (
  "channelId" NVARCHAR2(50), 
  "alertSubTypeId" NVARCHAR2(75), 
  "createdby" VARCHAR2(50 CHAR), 
  "modifiedby" VARCHAR2(50 CHAR), 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(5, 0) DEFAULT '0', 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT (null)
);
--  DDL for Table alertsubtypecustomertype

CREATE TABLE "alertsubtypecustomertype" (
  "customerTypeId" NVARCHAR2(50), 
  "alertSubTypeId" NVARCHAR2(75), 
  "createdby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "modifiedby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(5, 0) DEFAULT '0', 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT (null)
);
--  DDL for Table alertsubtypetext

CREATE TABLE "alertsubtypetext" (
  "alertSubTypeId" NVARCHAR2(75), 
  "languageCode" NVARCHAR2(10), 
  "displayName" VARCHAR2(255 CHAR) DEFAULT NULL, 
  "description" VARCHAR2(1000 CHAR) DEFAULT NULL, 
  "createdby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "modifiedby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(5, 0) DEFAULT '0', 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT (null)
);
--  DDL for Table alerttype

CREATE TABLE "alerttype" (
  "id" NVARCHAR2(50), 
  "Name" NVARCHAR2(30) DEFAULT NULL, 
  "Description" NVARCHAR2(250) DEFAULT NULL, 
  "IsSubscriptionNeeded" NUMBER(3, 0) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table alerttypeaccounttype

CREATE TABLE "alerttypeaccounttype" (
  "AccountTypeId" NVARCHAR2(50), 
  "AlertTypeId" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table alerttypeapp

CREATE TABLE "alerttypeapp" (
  "AppId" NVARCHAR2(50), 
  "AlertTypeId" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table alerttypechannel

CREATE TABLE "alerttypechannel" (
  "channelId" NVARCHAR2(50), 
  "alertTypeId" NVARCHAR2(50), 
  "createdby" VARCHAR2(50 CHAR), 
  "modifiedby" VARCHAR2(50 CHAR), 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(5, 0) DEFAULT '0', 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT (null)
);
--  DDL for Table alerttypecustomertype

CREATE TABLE "alerttypecustomertype" (
  "CustomerTypeId" NVARCHAR2(50), 
  "AlertTypeId" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table annualpercentagerate

CREATE TABLE "annualpercentagerate" (
  "id" NVARCHAR2(50), 
  "LoanType_id" NVARCHAR2(50) DEFAULT NULL, 
  "APRValue" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6), 
  "lastmodifiedts" TIMESTAMP (6), 
  "synctimestamp" TIMESTAMP (6), 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table app

CREATE TABLE "app" (
  "id" NVARCHAR2(50), 
  "Name" NVARCHAR2(50), 
  "Description" NVARCHAR2(50) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table application

CREATE TABLE "application" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "OSType" NVARCHAR2(100) DEFAULT NULL, 
  "OSversion" NUMBER(10, 2) DEFAULT NULL, 
  "BannerURL" NVARCHAR2(100) DEFAULT NULL, 
  "VersionLink" NVARCHAR2(100) DEFAULT NULL, 
  "currencyCode" NVARCHAR2(10) DEFAULT NULL, 
  "BusinessDays" NUMBER(10, 0) DEFAULT NULL, 
  "BankName" NVARCHAR2(45) DEFAULT NULL, 
  "DistanceUnit" NVARCHAR2(10) DEFAULT NULL, 
  "ocrApiKey" NVARCHAR2(100) DEFAULT NULL, 
  "ocrSecretKey" NVARCHAR2(100) DEFAULT NULL, 
  "facialLicenseString" NVARCHAR2(100) DEFAULT NULL, 
  "facialLicenseServerUrl" NVARCHAR2(100) DEFAULT NULL, 
  "appStoreLink" NVARCHAR2(200) DEFAULT NULL, 
  "playStoreLink" NVARCHAR2(200) DEFAULT NULL, 
  "ipadNativeAppLink" NVARCHAR2(200) DEFAULT NULL, 
  "androidTabletNativeAppLink" NVARCHAR2(200) DEFAULT NULL, 
  "isLanguageSelectionEnabled" NUMBER(1, 0) DEFAULT 0, 
  "isBackEndCurencySymbolEnabled" NUMBER(1, 0) DEFAULT 0, 
  "isCountryCodeEnabled" NUMBER(1, 0) DEFAULT 0, 
  "isSortCodeVisible" NUMBER(1, 0) DEFAULT 0, 
  "currenciesSupported" NVARCHAR2(1000) DEFAULT NULL, 
  "deploymentGeography" NVARCHAR2(45) DEFAULT NULL, 
  "isUTCDateFormattingEnabled" NUMBER(1, 0) DEFAULT 0, 
  "language" NVARCHAR2(16) DEFAULT NULL, 
  "defaultAccountType" NVARCHAR2(45) DEFAULT NULL, 
  "fundingAmount" NVARCHAR2(45) DEFAULT NULL, 
  "isBusinessBankingEnabled" NUMBER(1, 0) DEFAULT (0), 
  "isAccountAggregationEnabled" NVARCHAR2(45) DEFAULT 'false', 
  "defaultCountryDialCode" NVARCHAR2(45) DEFAULT NULL, 
  "isFeedbackEnabled" NUMBER(1, 0) DEFAULT 1, 
  "noOfDaysForRatingFromProfile" NUMBER(10, 0) DEFAULT NULL, 
  "noOfDaysForRatingFromTransactions" NUMBER(10, 0) DEFAULT NULL, 
  "noOfDaysForAnotherAttemptForRating" NUMBER(10, 0) DEFAULT NULL, 
  "maxtimesFeedbackperversion" NVARCHAR2(45) DEFAULT NULL, 
  "majorVersionsForFeedback" NVARCHAR2(45) DEFAULT NULL, 
  "bannerImageURL" NVARCHAR2(100) DEFAULT NULL, 
  "desktopBannerImageURL" NVARCHAR2(100) DEFAULT NULL, 
  "mobileBannerImageURL" NVARCHAR2(100) DEFAULT NULL, 
  "viewMoreDBXLink" NVARCHAR2(100) DEFAULT NULL, 
  "showAdsPostLogin" NUMBER(1, 0) DEFAULT (1), 
  "isAlertAccountIDLevel" NUMBER(1, 0) DEFAULT (1), 
  "isAccountTypeLevelAlerts" NVARCHAR2(45) DEFAULT 'false', 
  "isprofileImageAvailable" NVARCHAR2(45) DEFAULT 'true', 
  "cardStatementYears" NVARCHAR2(45) DEFAULT NULL, 
  "bwFileTransactionsLimit" NUMBER(10, 0) DEFAULT NULL, 
  "isAccountCentricCore" NUMBER(1, 0) DEFAULT (1), 
  "timeZoneOffset" NVARCHAR2(50) DEFAULT 'UTC+11:00', 
  "stopReasons" NVARCHAR2(400) DEFAULT NULL, 
  "customerCreationMode" VARCHAR2(50 CHAR) DEFAULT 'WITHOUT-RECORD', 
  "isKeyCloakEnabled" NUMBER(3, 0) DEFAULT '0', 
  "newSettings" NUMBER(1, 0) DEFAULT (0), 
  "isSelfApprovalEnabled" NUMBER(1, 0) DEFAULT (1), 
  "stateManagementAvailable" NUMBER(3, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL', 
  "isSingleEntity" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table appmappingaid

CREATE TABLE "appmappingaid" (
  "id" NUMBER(10, 0), 
  "Appid" NVARCHAR2(45) DEFAULT NULL, 
  "Channel" NVARCHAR2(45) DEFAULT NULL, 
  "aid" NVARCHAR2(45) DEFAULT NULL
);
--  DDL for Table appointment

CREATE TABLE "appointment" (
  "id" NUMBER(19, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "appointmentTime" NVARCHAR2(50) DEFAULT NULL, 
  "appointmentWith" NVARCHAR2(50) DEFAULT NULL, 
  "dob" NVARCHAR2(50) DEFAULT NULL, 
  "email" NVARCHAR2(50) DEFAULT NULL, 
  "firstName" NVARCHAR2(50) DEFAULT NULL, 
  "lastName" NVARCHAR2(50) DEFAULT NULL, 
  "phone" NVARCHAR2(50) DEFAULT NULL, 
  "uid" NVARCHAR2(50) DEFAULT NULL, 
  "branch_id" NUMBER(10, 0) DEFAULT NULL, 
  "user_id" NUMBER(10, 0) DEFAULT NULL
);
--  DDL for Table archivedalerthistory

CREATE TABLE "archivedalerthistory" (
  "Id" NVARCHAR2(100), 
  "EventId" NUMBER(10, 0), 
  "AlertSubTypeId" NVARCHAR2(75), 
  "AlertTypeId" NVARCHAR2(50), 
  "AlertCategoryId" NVARCHAR2(50), 
  "AlertStatusId" NVARCHAR2(50), 
  "Customer_Id" NVARCHAR2(50), 
  "LanguageCode" NVARCHAR2(10) DEFAULT NULL, 
  "ChannelId" NVARCHAR2(50), 
  "Status" NVARCHAR2(50) DEFAULT NULL, 
  "Subject" NVARCHAR2(255) DEFAULT NULL, 
  "Message" NCLOB, 
  "SenderName" NVARCHAR2(255) DEFAULT NULL, 
  "SenderEmail" NVARCHAR2(255) DEFAULT NULL, 
  "ReferenceNumber" NVARCHAR2(100) DEFAULT NULL, 
  "DispatchDate" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "ErrorMessage" NCLOB, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);

--  DDL for Table achtransaction
--------------------------------------------------------

CREATE TABLE "achtransaction" 
   (  "transaction_id" NUMBER(10,0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 44 INCREMENT BY 1 MINVALUE 44 NOMAXVALUE,
  "fromAccount" NVARCHAR2(45) DEFAULT NULL, 
  "effectiveDate" TIMESTAMP (6), 
  "requestId" NUMBER(19,0) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "maxAmount" FLOAT(126) DEFAULT NULL, 
  "status" NVARCHAR2(50) DEFAULT NULL, 
  "transactionType_id" NUMBER(10,0) DEFAULT NULL, 
  "templateType_id" NUMBER(10,0) DEFAULT NULL, 
  "companyId" NVARCHAR2(50) DEFAULT NULL, 
  "roleId" NVARCHAR2(45) DEFAULT NULL, 
  "templateRequestType_id" NUMBER(10,0) DEFAULT NULL, 
  "softDelete" NUMBER(10,0) DEFAULT (0), 
  "templateName" NVARCHAR2(45) DEFAULT 'No Template Used', 
  "confirmationNumber" NVARCHAR2(45) DEFAULT NULL, 
  "actedBy" NVARCHAR2(50) DEFAULT NULL, 
  "template_id" NUMBER(10,0) DEFAULT NULL, 
  "updatedts" TIMESTAMP (6), 
  "totalAmount" FLOAT(126) DEFAULT NULL, 
  "featureActionId" NVARCHAR2(50) DEFAULT NULL, 
  "transactionAmount" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "serviceCharge" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "transactionCurrency" VARCHAR2(50 CHAR) DEFAULT NULL
   );
--------------------------------------------------------
--  DDL for Table achtransactionrecord
--------------------------------------------------------

CREATE TABLE "achtransactionrecord" 
   (  "transactionRecord_id" NUMBER(10,0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 40 INCREMENT BY 1 MINVALUE 40 NOMAXVALUE,
  "toAccountNumber" NVARCHAR2(45) DEFAULT NULL, 
  "toAccountType" NUMBER(10,0) DEFAULT NULL, 
  "abatrcNumber" NVARCHAR2(45) DEFAULT NULL, 
  "detail_id" NVARCHAR2(45) DEFAULT NULL, 
  "amount" FLOAT(126) DEFAULT NULL, 
  "additionalInfo" NVARCHAR2(500) DEFAULT NULL, 
  "eIN" NVARCHAR2(45) DEFAULT NULL, 
  "isZeroTaxDue" NUMBER(3,0) DEFAULT NULL, 
  "taxType_id" NUMBER(10,0) DEFAULT NULL, 
  "transaction_id" NUMBER(10,0) DEFAULT NULL, 
  "softDelete" NUMBER(10,0) DEFAULT (0), 
  "templateRequestType_id" NUMBER(10,0) DEFAULT NULL, 
  "record_Name" NVARCHAR2(45) DEFAULT NULL
   );
--------------------------------------------------------
--  DDL for Table achtransactionsubrecord
--------------------------------------------------------

CREATE TABLE "achtransactionsubrecord" 
   (  "transcationSubRecord_id" NUMBER(10,0) GENERATED BY DEFAULT ON NULL AS IDENTITY MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE , 
  "amount" FLOAT(126), 
  "transactionRecord_id" NUMBER(10,0), 
  "taxSubCategory_id" NUMBER(10,0), 
  "softDelete" NUMBER(10,0) DEFAULT (0)
   );


--  DDL for Table attachmenttype


CREATE TABLE "attachmenttype" ("id" NVARCHAR2(50), "Name" NVARCHAR2(50) DEFAULT NULL, "createdby" NVARCHAR2(50) DEFAULT NULL, "modifiedby" NVARCHAR2(50) DEFAULT NULL, "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "softdeleteflag" NUMBER(1,0) DEFAULT (0));

--  DDL for Table attribute

CREATE TABLE "attribute" (
  "id" NVARCHAR2(50), 
  "endpoint_attribute_id" NVARCHAR2(50), 
  "name" NVARCHAR2(50), 
  "attributetype" NVARCHAR2(12), 
  "options" NVARCHAR2(1000) DEFAULT NULL, 
  "range" NVARCHAR2(100) DEFAULT NULL, 
  "criterias" NVARCHAR2(1000), 
  "helptext" NVARCHAR2(1000) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table attributetype

CREATE TABLE "attributetype" (
  "id" NVARCHAR2(50), 
  "description" NVARCHAR2(100) DEFAULT NULL
);

--  DDL for Table backendcertificate
--------------------------------------------------------

CREATE TABLE "backendcertificate" 
   (  "id" NUMBER(10,0) GENERATED BY DEFAULT ON NULL AS IDENTITY MINVALUE 2 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 2 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE , 
  "BackendName" VARCHAR2(45 CHAR), 
  "CertName" VARCHAR2(45 CHAR), 
  "CertPrivateKey" CLOB, 
  "CertPublicKey" CLOB, 
  "JWSAlgorithm" NCLOB, 
  "CertificateEncryptionKey" NCLOB, 
  "PublicKeyServiceURL" NCLOB, 
  "createdby" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "modifiedby" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP
   );

--  DDL for Table backendidentifier

CREATE TABLE "backendidentifier" (
  "id" NVARCHAR2(50), 
  "Customer_id" NVARCHAR2(50), 
  "sequenceNumber" NVARCHAR2(45) DEFAULT NULL, 
  "BackendId" NVARCHAR2(45) DEFAULT NULL, 
  "BackendType" NVARCHAR2(45) DEFAULT NULL, 
  "identifier_name" NVARCHAR2(45) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "isTypeBusiness" NVARCHAR2(45) DEFAULT '0', 
  "CompanyId" VARCHAR2(200 CHAR), 
  "contractId" VARCHAR2(20 CHAR) DEFAULT NULL, 
  "contractTypeId" VARCHAR2(20 CHAR) DEFAULT NULL, 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table bank

CREATE TABLE "bank" (
  "id" NVARCHAR2(50), 
  "Description" NVARCHAR2(100) DEFAULT NULL, 
  "Oauth2" NUMBER(1, 0) DEFAULT (0), 
  "IdentityProvider" NVARCHAR2(60) DEFAULT NULL
);
--  DDL for Table bankbranch

CREATE TABLE "bankbranch" (
  "id" NUMBER(10, 0), 
  "address1" NVARCHAR2(50) DEFAULT NULL, 
  "address2" NVARCHAR2(50) DEFAULT NULL, 
  "city" NVARCHAR2(50) DEFAULT NULL, 
  "state" NVARCHAR2(50) DEFAULT NULL, 
  "zipCode" NUMBER(10, 0) DEFAULT NULL, 
  "phone" NVARCHAR2(50) DEFAULT NULL, 
  "workingHours" NVARCHAR2(500) DEFAULT NULL, 
  "services" NVARCHAR2(500) DEFAULT NULL, 
  "latitude" NVARCHAR2(100) DEFAULT NULL, 
  "longitude" NVARCHAR2(50) DEFAULT NULL, 
  "status" NVARCHAR2(50) DEFAULT NULL, 
  "email" NVARCHAR2(50) DEFAULT NULL, 
  "Type_id" NUMBER(10, 0) DEFAULT NULL
);
--  DDL for Table bankcommunication

CREATE TABLE "bankcommunication" (
  "Type_id" NVARCHAR2(50), 
  "Bank_id" NVARCHAR2(50) DEFAULT NULL, 
  "sequence" NUMBER(10, 0), 
  "value" NVARCHAR2(100) DEFAULT NULL, 
  "extension" NVARCHAR2(50) DEFAULT NULL, 
  "description" NVARCHAR2(100) DEFAULT NULL
);
--  DDL for Table banner

CREATE TABLE "banner" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "Category_id" NVARCHAR2(50) DEFAULT NULL, 
  "Type_id" NVARCHAR2(50) DEFAULT NULL, 
  "description" NCLOB, 
  "bannerImage" NCLOB, 
  "destinationURL" NCLOB
);
--  DDL for Table batchalertdefinition

CREATE TABLE "batchalertdefinition" (
  "alertType" NVARCHAR2(50), 
  "objectType" NVARCHAR2(50), 
  "columnName" NVARCHAR2(255) DEFAULT NULL, 
  "condition" NVARCHAR2(25) DEFAULT NULL, 
  "value" NVARCHAR2(255) DEFAULT NULL, 
  "dueDateChecktype" NVARCHAR2(4) DEFAULT NULL, 
  "dueDateParamName" NVARCHAR2(255) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "updatedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table batchalertobject

CREATE TABLE "batchalertobject" (
  "objectType" VARCHAR2(50 CHAR), 
  "operationName" VARCHAR2(100 CHAR) DEFAULT NULL, 
  "lastSyncTimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP
);
--  DDL for Table bbactedrequest

CREATE TABLE "bbactedrequest" (
  "approvalId" NUMBER(19, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "requestId" NUMBER(19, 0), 
  "companyId" NVARCHAR2(50) DEFAULT NULL, 
  "status" NVARCHAR2(50) DEFAULT NULL, 
  "comments" NVARCHAR2(500), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "action" NVARCHAR2(45) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "groupName" VARCHAR2(55 CHAR) DEFAULT NULL,
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table bbrequest

CREATE TABLE "bbrequest" (
  "requestId" NUMBER(19, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "transactionId" NVARCHAR2(50) DEFAULT NULL, 
  "featureActionId" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "companyId" NVARCHAR2(50) DEFAULT NULL, 
  "requiredSets" NUMBER(10, 0) DEFAULT NULL, 
  "receivedSets" NUMBER(10, 0) DEFAULT NULL, 
  "createdts" TIMESTAMP (6), 
  "status" NVARCHAR2(50) DEFAULT NULL, 
  "softDelete" NUMBER(1, 0) DEFAULT (0), 
  "accountId" NVARCHAR2(45) DEFAULT NULL, 
  "isGroupMatrix" NUMBER(10, 0) DEFAULT 0, 
  "additionalMeta" VARCHAR2(4000 CHAR),
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table bbtaxsubtype

CREATE TABLE "bbtaxsubtype" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "taxSubType" NVARCHAR2(300), 
  "taxType" NUMBER(10, 0)
);
--  DDL for Table bbtaxtype

CREATE TABLE "bbtaxtype" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "taxType" NVARCHAR2(300)
);
--  DDL for Table bbtemplate

CREATE TABLE "bbtemplate" (
  "templateId" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "templateName" NVARCHAR2(45) DEFAULT NULL, 
  "templateDescription" NVARCHAR2(45) DEFAULT NULL, 
  "fromAccount" NVARCHAR2(45) DEFAULT NULL, 
  "effectiveDate" DATE DEFAULT NULL, 
  "maxAmount" FLOAT(126) DEFAULT NULL, 
  "requestId" NUMBER(10, 0) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "status" NVARCHAR2(50) DEFAULT NULL, 
  "updatedBy" NVARCHAR2(50) DEFAULT NULL, 
  "updatedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "transactionType_id" NUMBER(10, 0) DEFAULT NULL, 
  "templateType_id" NUMBER(10, 0) DEFAULT NULL, 
  "companyId" NVARCHAR2(50) DEFAULT NULL, 
  "roleId" NVARCHAR2(45) DEFAULT NULL, 
  "templateRequestType_id" NUMBER(10, 0) DEFAULT NULL, 
  "softDelete" NUMBER(10, 0) DEFAULT (0), 
  "actedBy" NVARCHAR2(45) DEFAULT NULL, 
  "totalAmount" FLOAT(126) DEFAULT NULL, 
  "featureActionId" NVARCHAR2(50) DEFAULT NULL
);
--  DDL for Table bbtemplaterecord

CREATE TABLE "bbtemplaterecord" (
  "templateRecord_id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "record_Name" NVARCHAR2(45) DEFAULT NULL, 
  "toAccountNumber" NVARCHAR2(45) DEFAULT NULL, 
  "abatrcNumber" NVARCHAR2(45) DEFAULT NULL, 
  "detail_id" NVARCHAR2(45) DEFAULT NULL, 
  "amount" FLOAT(126) DEFAULT NULL, 
  "additionalInfo" NVARCHAR2(500) DEFAULT NULL, 
  "ein" NVARCHAR2(45) DEFAULT NULL, 
  "isZeroTaxDue" NUMBER(3, 0) DEFAULT NULL, 
  "template_id" NUMBER(10, 0) DEFAULT NULL, 
  "taxType_id" NUMBER(10, 0) DEFAULT NULL, 
  "templateRequestType_id" NUMBER(10, 0) DEFAULT NULL, 
  "softDelete" NUMBER(10, 0) DEFAULT (0), 
  "toAccountType" NUMBER(10, 0) DEFAULT NULL
);
--  DDL for Table bbtemplaterequesttype

CREATE TABLE "bbtemplaterequesttype" (
  "templateRequestType_id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "templateRequestTypeName" NVARCHAR2(45), 
  "transactionType_id" NUMBER(10, 0)
);
--  DDL for Table bbtemplatesubrecord

CREATE TABLE "bbtemplatesubrecord" (
  "templateSubRecord_id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "amount" FLOAT(126) DEFAULT NULL, 
  "templateRecord_id" NUMBER(10, 0), 
  "taxSubCategory_id" NUMBER(10, 0), 
  "softDelete" NUMBER(10, 0) DEFAULT (0)
);
--  DDL for Table bbtemplatetype

CREATE TABLE "bbtemplatetype" (
  "templateType_id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "templateTypeName" NVARCHAR2(45)
);
--  DDL for Table bbtransactiontype

CREATE TABLE "bbtransactiontype" (
  "transactionType_id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "transactionTypeName" NVARCHAR2(45)
);
--  DDL for Table bill

CREATE TABLE "bill" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "Payee_id" NUMBER(10, 0) DEFAULT NULL, 
  "Account_id" NUMBER(19, 0) DEFAULT NULL, 
  "billDueDate" DATE DEFAULT NULL, 
  "paidDate" DATE DEFAULT NULL, 
  "description" NVARCHAR2(50) DEFAULT NULL, 
  "dueAmount" NUMBER(10, 2) DEFAULT (0.00), 
  "paidAmount" NUMBER(10, 2) DEFAULT (0.00), 
  "balanceAmount" NUMBER(10, 2) DEFAULT (0.00), 
  "minimumDue" NUMBER(10, 2) DEFAULT (0.00), 
  "ebillURL" NCLOB, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "statusDesc" NVARCHAR2(50) DEFAULT NULL, 
  "billerMaster_id" NUMBER(10, 0) DEFAULT NULL, 
  "billGeneratedDate" DATE DEFAULT NULL, 
  "currencyCode" NVARCHAR2(45) DEFAULT NULL
);
--  DDL for Table billercategory

CREATE TABLE "billercategory" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "categoryName" NVARCHAR2(100) DEFAULT NULL
);
--  DDL for Table billercompany

CREATE TABLE "billercompany" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "companyName" NVARCHAR2(50) DEFAULT NULL
);
--  DDL for Table billermaster

CREATE TABLE "billermaster" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "billerName" NVARCHAR2(100) DEFAULT NULL, 
  "accountNumber" NVARCHAR2(50) DEFAULT NULL, 
  "zipCode" NVARCHAR2(50) DEFAULT NULL, 
  "mobileNumber" NVARCHAR2(50) DEFAULT NULL, 
  "phoneNumber" NVARCHAR2(50) DEFAULT NULL, 
  "address" NVARCHAR2(100) DEFAULT NULL, 
  "relationshipNumber" NVARCHAR2(50) DEFAULT NULL, 
  "policyNumber" NVARCHAR2(100) DEFAULT NULL, 
  "city" NVARCHAR2(45) DEFAULT NULL, 
  "state" NVARCHAR2(45) DEFAULT NULL, 
  "billerCategoryId" NUMBER(10, 0) DEFAULT NULL, 
  "ebillSupport" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table branchtype

CREATE TABLE "branchtype" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "Type" NVARCHAR2(45) DEFAULT NULL
);
--  DDL for Table budget

CREATE TABLE "budget" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "ExpenseCategory_id" NUMBER(10, 0) DEFAULT NULL, 
  "description" NVARCHAR2(50) DEFAULT NULL, 
  "totalBudget" NUMBER(10, 2) DEFAULT (0.00), 
  "usedBudget" NUMBER(10, 2) DEFAULT (0.00)
);
--  DDL for Table bulkwirefileformattype

CREATE TABLE "bulkwirefileformattype" (
  "bulkWiresFileFormatTypeCode" NVARCHAR2(50), 
  "bulkWiresFileFormatTypeName" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table bulkwirefilelineitems

CREATE TABLE "bulkwirefilelineitems" (
  "bulkWireFileLineItemID" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "bulkWireFileID" NVARCHAR2(50), 
  "swiftCode" NVARCHAR2(50) DEFAULT NULL, 
  "bulkWireTransferType" NVARCHAR2(50) DEFAULT NULL, 
  "transactionType" NVARCHAR2(50) DEFAULT NULL, 
  "internationalRoutingNumber" NVARCHAR2(50) DEFAULT NULL, 
  "amount" NUMBER(20, 2) DEFAULT (0.00), 
  "fromAccountNumber" NVARCHAR2(50) DEFAULT NULL, 
  "note" NVARCHAR2(100) DEFAULT (' '), 
  "recipientName" NVARCHAR2(100) DEFAULT NULL, 
  "recipientAddressLine1" NVARCHAR2(100) DEFAULT NULL, 
  "recipientAddressLine2" NVARCHAR2(100) DEFAULT NULL, 
  "recipientCity" NVARCHAR2(100) DEFAULT NULL, 
  "recipientState" NVARCHAR2(100) DEFAULT NULL, 
  "recipientCountryName" NVARCHAR2(100) DEFAULT NULL, 
  "recipientZipCode" NVARCHAR2(20) DEFAULT NULL, 
  "recipientBankName" NVARCHAR2(100) DEFAULT NULL, 
  "recipientBankAddress1" NVARCHAR2(100) DEFAULT NULL, 
  "recipientBankAddress2" NVARCHAR2(100), 
  "recipientBankZipCode" NVARCHAR2(20) DEFAULT NULL, 
  "recipientBankcity" NVARCHAR2(100) DEFAULT NULL, 
  "recipientBankstate" NVARCHAR2(100) DEFAULT NULL, 
  "currency" NVARCHAR2(10), 
  "accountNickname" NVARCHAR2(50) DEFAULT NULL, 
  "recipientAccountNumber" NVARCHAR2(50) DEFAULT NULL, 
  "routingNumber" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table bulkwirefiles

CREATE TABLE "bulkwirefiles" (
  "bulkWireFileID" NVARCHAR2(50), 
  "bulkWireFileName" NVARCHAR2(150), 
  "noOfTransactions" NUMBER(10, 0) DEFAULT (0), 
  "noOfDomesticTransactions" NUMBER(10, 0) DEFAULT (0), 
  "noOfInternationalTransactions" NUMBER(10, 0) DEFAULT (0), 
  "fileFormatCode" NVARCHAR2(50), 
  "createdBy" NVARCHAR2(50), 
  "modifiedBy" NVARCHAR2(50), 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "company_id" NVARCHAR2(50) DEFAULT NULL, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "bulkWireFileContents" NCLOB, 
  "lastExecutedOn" TIMESTAMP (6)
);
--  DDL for Table bulkwirefiletransactdetails

CREATE TABLE "bulkwirefiletransactdetails" (
  "bulkWireTransactionID" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "bulkWireFileID" NVARCHAR2(50), 
  "transactionDate" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "initiatedBy" NVARCHAR2(50), 
  "totalCountOfTransactions" NUMBER(10, 0), 
  "totalCountOfDomesticTransactions" NUMBER(10, 0) DEFAULT (0), 
  "totalCountOfInternationalTransactions" NUMBER(10, 0) DEFAULT (0), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table bulkwiresamplefile

CREATE TABLE "bulkwiresamplefile" (
  "bulkWireSampleFileID" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "bulkWireSampleFileName" NVARCHAR2(50), 
  "bulkWireSampleFileFormatCode" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "sampleFileContents" NCLOB, 
  "fileCategory" NVARCHAR2(17)
);
--  DDL for Table bulkwiretemplate

CREATE TABLE "bulkwiretemplate" (
  "bulkWireTemplateID" NVARCHAR2(50), 
  "bulkWireTemplateName" NVARCHAR2(150), 
  "noOfTransactions" NUMBER(10, 0) DEFAULT (0), 
  "noOfDomesticTransactions" NUMBER(10, 0) DEFAULT (0), 
  "noOfInternationalTransactions" NUMBER(10, 0) DEFAULT (0), 
  "createdBy" NVARCHAR2(50), 
  "modifiedBy" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "company_id" NVARCHAR2(50) DEFAULT NULL, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "lastExecutedOn" TIMESTAMP (6), 
  "defaultFromAccount" NVARCHAR2(50), 
  "defaultCurrency" NVARCHAR2(10), 
  "deleteUniqueValue" NVARCHAR2(50) DEFAULT 'NA'
);
--  DDL for Table bulkwiretemplatelineitems

CREATE TABLE "bulkwiretemplatelineitems" (
  "bulkWireTemplateLineItemID" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "bulkWireTemplateID" NVARCHAR2(50), 
  "swiftCode" NVARCHAR2(50) DEFAULT NULL, 
  "bulkWireTransferType" NVARCHAR2(50) DEFAULT NULL, 
  "transactionType" NVARCHAR2(50) DEFAULT NULL, 
  "internationalRoutingNumber" NVARCHAR2(50) DEFAULT NULL, 
  "recipientName" NVARCHAR2(100) DEFAULT NULL, 
  "recipientAddressLine1" NVARCHAR2(100) DEFAULT NULL, 
  "recipientAddressLine2" NVARCHAR2(100) DEFAULT NULL, 
  "recipientCity" NVARCHAR2(100) DEFAULT NULL, 
  "recipientState" NVARCHAR2(100) DEFAULT NULL, 
  "recipientCountryName" NVARCHAR2(100) DEFAULT NULL, 
  "recipientZipCode" NVARCHAR2(20) DEFAULT NULL, 
  "recipientBankName" NVARCHAR2(100) DEFAULT NULL, 
  "recipientBankAddress1" NVARCHAR2(100) DEFAULT NULL, 
  "recipientBankAddress2" NVARCHAR2(100) DEFAULT NULL, 
  "recipientBankZipCode" NVARCHAR2(20) DEFAULT NULL, 
  "recipientBankcity" NVARCHAR2(100) DEFAULT NULL, 
  "recipientBankstate" NVARCHAR2(100) DEFAULT NULL, 
  "accountNickname" NVARCHAR2(50) DEFAULT NULL, 
  "recipientAccountNumber" NVARCHAR2(50) DEFAULT NULL, 
  "routingNumber" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50), 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "payeeId" NUMBER(10, 0) DEFAULT NULL, 
  "templateRecipientCategory" NVARCHAR2(17)
);
--  DDL for Table bulkwiretemplatetransactdetails

CREATE TABLE "bulkwiretemplatetransactdetails" (
  "bulkWireTransactionID" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "bulkWireTemplateID" NVARCHAR2(50), 
  "transactionDate" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "initiatedBy" NVARCHAR2(50), 
  "totalCountOfTransactions" NUMBER(10, 0), 
  "totalCountOfDomesticTransactions" NUMBER(10, 0) DEFAULT (0), 
  "totalCountOfInternationalTransactions" NUMBER(10, 0) DEFAULT (0), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table card

CREATE TABLE "card" (
  "Id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "card_Status" NVARCHAR2(20), 
  "User_id" NVARCHAR2(50), 
  "expirationDate" DATE, 
  "pinNumber" NVARCHAR2(10), 
  "reason" NVARCHAR2(100) DEFAULT NULL, 
  "cardNumber" NVARCHAR2(20), 
  "cardType" NVARCHAR2(6) DEFAULT NULL, 
  "action" NVARCHAR2(15) DEFAULT NULL, 
  "account_id" NVARCHAR2(50) DEFAULT NULL, 
  "isTypeBusiness" NVARCHAR2(45) DEFAULT NULL, 
  "creditLimit" NVARCHAR2(50) DEFAULT '0.00', 
  "availableCredit" NVARCHAR2(50) DEFAULT '0.00', 
  "serviceProvider" NVARCHAR2(50) DEFAULT NULL, 
  "billingAddress" NVARCHAR2(50) DEFAULT NULL, 
  "cardProductName" NVARCHAR2(50) DEFAULT NULL, 
  "secondaryCardHolder" NVARCHAR2(50) DEFAULT NULL, 
  "withdrawlLimit" NVARCHAR2(50) DEFAULT '0.00', 
  "withdrawalMinLimit" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "withdrawalMaxLimit" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "withdrawalStepLimit" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "purchaseLimit" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "purchaseMinLimit" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "purchaseMaxLimit" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "purchaseStepLimit" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "isInternational" NUMBER(1, 0) DEFAULT 0, 
  "bankName" NVARCHAR2(100) DEFAULT NULL, 
  "cardHolderName" NVARCHAR2(20) DEFAULT NULL, 
  "cvv" NUMBER(10, 0) DEFAULT (0), 
  "currentBalance" NVARCHAR2(45) DEFAULT NULL, 
  "rewardsPoint" NVARCHAR2(45) DEFAULT NULL, 
  "paymentDueDate" TIMESTAMP (6), 
  "availableBalance" NVARCHAR2(45) DEFAULT NULL, 
  "currencyCode" NVARCHAR2(45) DEFAULT NULL, 
  "cardDisplayName" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "protectionEnabled" NUMBER(3, 0) DEFAULT (0),
  "legalEntityId" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table cardaccountrequest

CREATE TABLE "cardaccountrequest" (
  "id" NVARCHAR2(50), 
  "Customer_id" NVARCHAR2(50) DEFAULT NULL, 
  "CardAccountNumber" NVARCHAR2(50) DEFAULT NULL, 
  "CardAccountName" NVARCHAR2(50) DEFAULT NULL, 
  "AccountType" NVARCHAR2(50) DEFAULT NULL, 
  "RequestType_id" NVARCHAR2(50) DEFAULT NULL, 
  "RequestReason" NVARCHAR2(100) DEFAULT NULL, 
  "Date" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "Channel_id" NVARCHAR2(50) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "Address_id" NVARCHAR2(50) DEFAULT NULL, 
  "Communication_id" NVARCHAR2(50) DEFAULT NULL, 
  "AdditionalNotes" NVARCHAR2(200) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "isTypeBusiness" NVARCHAR2(45) DEFAULT NULL
);

--  DDL for Table cardaccountrequesttype

CREATE TABLE "cardaccountrequesttype" (
  "id" NVARCHAR2(50), 
  "Description" NVARCHAR2(100) DEFAULT NULL, 
  "Code" NVARCHAR2(50) DEFAULT NULL, 
  "DisplayName" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);

--  DDL for Table cardproducts

CREATE TABLE "cardproducts" (
  "productId" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "productName" VARCHAR2(200 CHAR), 
  "featureOverview" NCLOB DEFAULT TO_NCLOB(NULL), 
  "featureDescription" NCLOB DEFAULT TO_NCLOB(NULL), 
  "representativeLabel1" NVARCHAR2(2000) DEFAULT NULL, 
  "representativeLabel2" NVARCHAR2(2000) DEFAULT NULL, 
  "representativeLabel3" NVARCHAR2(2000) DEFAULT NULL, 
  "representativeValue1" NVARCHAR2(2000) DEFAULT NULL, 
  "representativeValue2" NVARCHAR2(2000) DEFAULT NULL, 
  "representativeValue3" NVARCHAR2(2000) DEFAULT NULL, 
  "createdOn" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "updatedOn" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "createdBy" VARCHAR2(45 CHAR), 
  "modifiedby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "withdrawlLimit" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "withdrawalMinLimit" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "withdrawalMaxLimit" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "withdrawalStepLimit" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "purchaseLimit" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "purchaseMinLimit" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "purchaseMaxLimit" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "purchaseStepLimit" VARCHAR2(50 CHAR) DEFAULT NULL
);
--  DDL for Table cardproducttype

CREATE TABLE "cardproducttype" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "productId" NUMBER(10, 0), 
  "accountType" VARCHAR2(50 CHAR), 
  "createdOn" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "updatedOn" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "createdBy" VARCHAR2(45 CHAR), 
  "modifiedby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table cardstatements

CREATE TABLE "cardstatements" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "description" VARCHAR2(100 CHAR) DEFAULT NULL, 
  "statementLink" VARCHAR2(100 CHAR) DEFAULT NULL, 
  "Card_id" NUMBER(19, 0), 
  "month" VARCHAR2(100 CHAR) DEFAULT NULL
);
--  DDL for Table cardtransaction

CREATE TABLE "cardtransaction" (
  "cardNumber" NVARCHAR2(45), 
  "transactionDescription" NVARCHAR2(20) DEFAULT NULL, 
  "transactionBalance" NUMBER(11, 2) DEFAULT NULL, 
  "transactionMerchantAddressName" NVARCHAR2(25) DEFAULT NULL, 
  "transactionMerchantCity" NVARCHAR2(16) DEFAULT NULL, 
  "merchantCategory" NVARCHAR2(16) DEFAULT NULL, 
  "transactionStatus" NVARCHAR2(1) DEFAULT NULL, 
  "transactionType" NVARCHAR2(1) DEFAULT NULL, 
  "transactionCategory" NVARCHAR2(1) DEFAULT NULL, 
  "transactionDetailDescription" NVARCHAR2(45) DEFAULT NULL, 
  "transactionIndicator" NVARCHAR2(1) DEFAULT NULL, 
  "transactionDate" TIMESTAMP (6), 
  "transactionTime" VARCHAR2(200 CHAR) DEFAULT NULL, 
  "transactionAmount" NUMBER(18, 2) DEFAULT (0.00), 
  "transactionReferenceNumber" NVARCHAR2(22), 
  "transactionCurrencyCode" NVARCHAR2(3) DEFAULT NULL, 
  "transactionExchangeRate" NUMBER(16, 7) DEFAULT (0.0000000), 
  "exchangeCurrency" NVARCHAR2(3) DEFAULT NULL, 
  "exchangeAmount" NUMBER(18, 2) DEFAULT (0.00), 
  "transactionTaxIndicator" NVARCHAR2(1) DEFAULT NULL, 
  "taxPercentage" NUMBER(8, 5) DEFAULT (0.00000), 
  "transactionTaxAmount" NUMBER(18, 2) DEFAULT (0.00), 
  "transactionTerminalID" NVARCHAR2(20) DEFAULT NULL, 
  "cardType" NVARCHAR2(45) DEFAULT NULL, 
  "isdisputed" NUMBER(1, 0) DEFAULT 0, 
  "disputedescription" NVARCHAR2(100) DEFAULT NULL, 
  "disputereason" NVARCHAR2(100) DEFAULT NULL, 
  "disputestatus" VARCHAR2(50 BYTE) DEFAULT NULL, 
  "disputedate" TIMESTAMP (6) DEFAULT NULL
);
--  DDL for Table category

CREATE TABLE "category" (
  "id" NVARCHAR2(50), 
  "Name" NVARCHAR2(100) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table channel

CREATE TABLE "channel" (
  "id" NVARCHAR2(50), 
  "status_id" NVARCHAR2(50) DEFAULT NULL, 
  "sequence" NUMBER(10, 0) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table channeltext

CREATE TABLE "channeltext" (
  "channelID" NVARCHAR2(50), 
  "LanguageCode" NVARCHAR2(10), 
  "Description" NVARCHAR2(1000) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table check

CREATE TABLE "check" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "frontImage" NVARCHAR2(500) DEFAULT NULL, 
  "backImage" NVARCHAR2(500) DEFAULT NULL, 
  "transactionId" NUMBER(10, 0)
);
--  DDL for Table checkorder

CREATE TABLE "checkorder" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "account_id" NUMBER(19, 0), 
  "orderTime" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "accountName" NVARCHAR2(50) DEFAULT NULL, 
  "accountNickName" NVARCHAR2(50) DEFAULT NULL, 
  "leafCount" NUMBER(10, 0) DEFAULT NULL, 
  "status" NVARCHAR2(50) DEFAULT NULL, 
  "name" NVARCHAR2(50) DEFAULT NULL, 
  "postBoxNumber" NVARCHAR2(50) DEFAULT NULL, 
  "state" NVARCHAR2(50) DEFAULT NULL, 
  "country" NVARCHAR2(50) DEFAULT NULL, 
  "zipCode" NVARCHAR2(50) DEFAULT NULL
);
--  DDL for Table city

CREATE TABLE "city" (
  "id" NVARCHAR2(50), 
  "Region_id" NVARCHAR2(50) DEFAULT NULL, 
  "Country_id" NVARCHAR2(50) DEFAULT NULL, 
  "Name" NVARCHAR2(256), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);

--  DDL for Table communicationtemplate

CREATE TABLE "communicationtemplate" (
  "Id" NVARCHAR2(100), 
  "LanguageCode" NVARCHAR2(10) DEFAULT NULL, 
  "AlertSubTypeId" NVARCHAR2(75) DEFAULT NULL, 
  "ChannelID" NVARCHAR2(50) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "Name" NVARCHAR2(255) DEFAULT NULL, 
  "Text" NCLOB, 
  "Subject" NVARCHAR2(255) DEFAULT NULL, 
  "SenderName" NVARCHAR2(255) DEFAULT NULL, 
  "SenderEmail" NVARCHAR2(255) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table communicationtype

CREATE TABLE "communicationtype" (
  "id" NVARCHAR2(50), 
  "Description" NVARCHAR2(100) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table configurationmasters

CREATE TABLE "configurationmasters" (
  "bundle_id" NVARCHAR2(255), 
  "app_id" NVARCHAR2(255) DEFAULT NULL, 
  "channels" NVARCHAR2(255) DEFAULT NULL, 
  "user_id" NVARCHAR2(255) DEFAULT NULL, 
  "role" NVARCHAR2(255) DEFAULT NULL, 
  "device_id" NVARCHAR2(255) DEFAULT NULL, 
  "app_version" NVARCHAR2(255) DEFAULT NULL, 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table contract

CREATE TABLE "contract" (
  "id" NVARCHAR2(50), 
  "servicedefinitionId" NVARCHAR2(50), 
  "serviceType" NVARCHAR2(50), 
  "name" NVARCHAR2(50), 
  "description" NVARCHAR2(200), 
  "statusId" NVARCHAR2(50) DEFAULT 'SID_CONTRACT_PENDING', 
  "faxId" NVARCHAR2(45), 
  "createdby" NVARCHAR2(50), 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "rejectedby" NVARCHAR2(50), 
  "rejectedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "rejectedReason" NVARCHAR2(45), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table contractaccounts

CREATE TABLE "contractaccounts" (
  "id" NVARCHAR2(50), 
  "contractId" NVARCHAR2(50), 
  "accountId" NVARCHAR2(50), 
  "accountName" NVARCHAR2(50), 
  "typeId" NVARCHAR2(50), 
  "coreCustomerId" NVARCHAR2(50), 
  "ownerType" NVARCHAR2(50), 
  "statusDesc" NVARCHAR2(50) DEFAULT 'Active', 
  "arrangementId" NVARCHAR2(50), 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "modifiedby" NVARCHAR2(50), 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "portfolioId" VARCHAR2(45 BYTE), 
  "productId" VARCHAR2(45 BYTE), 
  "portfolioName" VARCHAR2(45 BYTE), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table contractactionlimit

CREATE TABLE "contractactionlimit" (
  "id" NVARCHAR2(50), 
  "contractId" NVARCHAR2(50), 
  "coreCustomerId" NVARCHAR2(50), 
  "policyId" NVARCHAR2(50), 
  "featureId" NVARCHAR2(255), 
  "actionId" NVARCHAR2(255), 
  "limitGroupId" NVARCHAR2(45), 
  "limitTypeId" NVARCHAR2(50), 
  "value" NUMBER(20, 2), 
  "createdby" NVARCHAR2(50), 
  "modifiedby" NVARCHAR2(50), 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(5, 0) DEFAULT '0', 
  "isPortfolio" NVARCHAR2(45) DEFAULT 'false', 
  "accountId" NVARCHAR2(45), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL', 
  "isNewAction" NUMBER(1, 0)
);
--  DDL for Table contractaddress

CREATE TABLE "contractaddress" (
  "id" NUMBER(20, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "contractId" NVARCHAR2(50), 
  "addressId" NVARCHAR2(45), 
  "durationOfStay" NVARCHAR2(45), 
  "isPrimary" RAW(1) DEFAULT '0', 
  "createdby" NVARCHAR2(45), 
  "modifiedby" NVARCHAR2(45), 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "typeId" NVARCHAR2(45), 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" RAW(1) DEFAULT '0', 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table contractcommunication

CREATE TABLE "contractcommunication" (
  "id" NUMBER(20, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "typeId" NVARCHAR2(50), 
  "contractId" NVARCHAR2(50), 
  "sequence" NUMBER(10, 0), 
  "value" NVARCHAR2(100), 
  "extension" NVARCHAR2(45), 
  "phoneCountryCode" NVARCHAR2(10), 
  "description" NVARCHAR2(45), 
  "isPreferredContactMethod" RAW(5) DEFAULT '0', 
  "preferredContactTime" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50), 
  "modifiedby" NVARCHAR2(50), 
  "createdts" TIMESTAMP (6), 
  "lastmodifiedts" TIMESTAMP (6), 
  "synctimestamp" TIMESTAMP (6), 
  "softdeleteflag" RAW(1) DEFAULT '0', 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table contractcorecustomers

CREATE TABLE "contractcorecustomers" (
  "id" NVARCHAR2(50), 
  "contractId" NVARCHAR2(50), 
  "taxId" NVARCHAR2(50), 
  "coreCustomerId" NVARCHAR2(50), 
  "coreCustomerName" NVARCHAR2(50), 
  "isPrimary" NUMBER(5, 0) DEFAULT (0), 
  "isBusiness" NUMBER(5, 0) DEFAULT (0), 
  "sectorId" NVARCHAR2(50), 
  "implicitAccountAccess" NUMBER(5, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table contractcustomers

CREATE TABLE "contractcustomers" (
  "id" NVARCHAR2(50), 
  "contractId" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "customerId" NVARCHAR2(50) DEFAULT NULL, 
  "coreCustomerId" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "isAdmin" NUMBER(1, 0) DEFAULT (0), 
  "isOwner" NUMBER(1, 0) DEFAULT (0), 
  "isPrimary" NUMBER(1, 0) DEFAULT (0), 
  "isAuthSignatory" NUMBER(1, 0) DEFAULT (0), 
  "createdby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "modifiedby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT NULL, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT NULL, 
  "synctimestamp" TIMESTAMP (6) DEFAULT NULL, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "autoSyncAccounts" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);

--  DDL for Table contractcustomrole
CREATE TABLE "contractcustomrole" 
   (  "contractId" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "customerId" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "coreCustomerId" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "customRoleId" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "roleId" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "autoSyncAccounts" NUMBER(1,0) DEFAULT (0), 
  "id" NUMBER(10,0) GENERATED BY DEFAULT ON NULL AS IDENTITY MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE , 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
   );


--  DDL for Table contractfeatures

CREATE TABLE "contractfeatures" (
  "id" NVARCHAR2(50), 
  "contractId" NVARCHAR2(50), 
  "coreCustomerId" NVARCHAR2(50), 
  "featureId" NVARCHAR2(255), 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table coremembership

CREATE TABLE "coremembership" (
  "id" NVARCHAR2(50), 
  "Customer_id" NVARCHAR2(50) DEFAULT NULL, 
  "MemberId" NVARCHAR2(50) DEFAULT NULL, 
  "MemberType" NVARCHAR2(50) DEFAULT NULL, 
  "IDType_id" NVARCHAR2(50) DEFAULT NULL, 
  "IDValue" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" NVARCHAR2(50) DEFAULT NULL, 
  "lastmodifiedts" NVARCHAR2(50) DEFAULT NULL
);

--  DDL for Table corporatepayees
CREATE TABLE "corporatepayees" 
   (  "id" NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "accountNumber" VARCHAR2(75 CHAR), 
  "contractId" VARCHAR2(75 CHAR), 
  "customerId" VARCHAR2(75 CHAR), 
  "coreCustomerId" VARCHAR2(75 CHAR), 
  "cif" VARCHAR2(400 CHAR), 
  "name" VARCHAR2(100 CHAR), 
  "firstName" VARCHAR2(75 CHAR), 
  "lastName" VARCHAR2(75 CHAR), 
  "nickName" VARCHAR2(45 CHAR), 
  "address1" VARCHAR2(300 CHAR), 
  "address2" VARCHAR2(300 CHAR), 
  "city" VARCHAR2(80 CHAR), 
  "state" VARCHAR2(80 CHAR), 
  "country" VARCHAR2(80 CHAR), 
  "zipcode" VARCHAR2(80 CHAR), 
  "phoneNumber" VARCHAR2(45 CHAR), 
  "email" VARCHAR2(75 CHAR), 
  "companyName" VARCHAR2(100 CHAR), 
  "organizationId" VARCHAR2(45 CHAR), 
  "iban" VARCHAR2(75 CHAR), 
  "swiftcode" VARCHAR2(75 CHAR), 
  "bankName" VARCHAR2(100 CHAR), 
  "bankAddressLine1" VARCHAR2(100 CHAR), 
  "bankAddressLine2" VARCHAR2(100 CHAR), 
  "bankCity" VARCHAR2(85 CHAR), 
  "bankState" VARCHAR2(85 CHAR), 
  "bankZip" VARCHAR2(50 CHAR), 
  "internationalRoutingCode" VARCHAR2(80 CHAR), 
  "phoneCountryCode" VARCHAR2(45 CHAR), 
  "phoneExtension" VARCHAR2(45 CHAR), 
  "softDelete" VARCHAR2(45 CHAR)
   );

--  DDL for Table country

CREATE TABLE "country" (
  "id" NVARCHAR2(50), 
  "Code" NVARCHAR2(50), 
  "Name" NVARCHAR2(128), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "phoneCountryCode" VARCHAR2(50 CHAR), 
  "LanguageCode" VARCHAR2(45 BYTE) DEFAULT 'en-US', 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table countrybasecurrency

CREATE TABLE "countrybasecurrency" (
  "id" NUMBER(10, 0), 
  "countryCode" NVARCHAR2(50), 
  "baseCurrencyCode" NVARCHAR2(10)
);
--  DDL for Table credentialchecker

CREATE TABLE "credentialchecker" (
  "id" NVARCHAR2(50), 
  "UserName" NVARCHAR2(45), 
  "linktype" NVARCHAR2(45), 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "retryCount" VARCHAR2(50 CHAR) DEFAULT '0'
);
--  DDL for Table currency

CREATE TABLE "currency" (
  "code" NVARCHAR2(10), 
  "name" NVARCHAR2(50), 
  "symbol" NVARCHAR2(5), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table currencymarketrates

CREATE TABLE "currencymarketrates" (
  "id" NUMBER(10, 0), 
  "baseCurrencyCode" NVARCHAR2(10), 
  "quoteCurrencyCode" NVARCHAR2(10), 
  "marketId" VARCHAR2(20 CHAR), 
  "buyRate" VARCHAR2(50 CHAR), 
  "sellRate" VARCHAR2(50 CHAR)
);
--  DDL for Table customer

CREATE TABLE "customer" (
  "id" NVARCHAR2(50), 
  "Classification_id" NVARCHAR2(50) DEFAULT NULL, 
  "CustomerType_id" NVARCHAR2(50) DEFAULT 'TYPE_ID_RETAIL', 
  "isCombinedUser" NUMBER(1, 0) DEFAULT 0, 
  "FirstName" NVARCHAR2(200) DEFAULT NULL, 
  "MiddleName" NVARCHAR2(50) DEFAULT NULL, 
  "LastName" NVARCHAR2(200) DEFAULT NULL, 
  "FullName" NVARCHAR2(150) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT 'SID_CUS_ACTIVE', 
  "UserName" NVARCHAR2(50), 
  "Password" NVARCHAR2(100) DEFAULT NULL, 
  "unsuccessfulLoginAttempts" NUMBER(10, 0) DEFAULT NULL, 
  "lockCount" NUMBER(10, 0) DEFAULT NULL, 
  "Organization_Id" NVARCHAR2(50) DEFAULT NULL, 
  "organizationType" NVARCHAR2(45) DEFAULT NULL, 
  "Salutation" NVARCHAR2(50) DEFAULT NULL, 
  "Gender" NVARCHAR2(50) DEFAULT NULL, 
  "DateOfBirth" VARCHAR2(50) DEFAULT NULL, 
  "DrivingLicenseNumber" NVARCHAR2(50) DEFAULT NULL, 
  "Ssn" NVARCHAR2(50) DEFAULT NULL, 
  "Cvv" NVARCHAR2(50) DEFAULT NULL, 
  "Token" NVARCHAR2(200) DEFAULT NULL, 
  "Pin" NVARCHAR2(10) DEFAULT NULL, 
  "PreferredContactMethod" NVARCHAR2(50) DEFAULT NULL, 
  "PreferredContactTime" NVARCHAR2(50) DEFAULT NULL, 
  "MaritalStatus_id" NVARCHAR2(50) DEFAULT NULL, 
  "SpouseName" NVARCHAR2(50) DEFAULT NULL, 
  "NoOfDependents" NVARCHAR2(50) DEFAULT NULL, 
  "EmployementStatus_id" NVARCHAR2(50) DEFAULT NULL, 
  "UserCompany" NVARCHAR2(50) DEFAULT NULL, 
  "SecurityImage_id" NVARCHAR2(50) DEFAULT NULL, 
  "Location_id" NVARCHAR2(50) DEFAULT NULL, 
  "IsOlbAllowed" NUMBER(1, 0) DEFAULT (0), 
  "IsStaffMember" NUMBER(1, 0) DEFAULT (0), 
  "CountryCode" NVARCHAR2(50) DEFAULT NULL, 
  "UserImage" NCLOB, 
  "UserImageURL" NVARCHAR2(200) DEFAULT NULL, 
  "OlbEnrolmentStatus_id" NVARCHAR2(50) DEFAULT NULL, 
  "Otp" NVARCHAR2(50) DEFAULT NULL, 
  "PreferedOtpMethod" NVARCHAR2(50) DEFAULT NULL, 
  "OtpGenaratedts" TIMESTAMP (6), 
  "ValidDate" TIMESTAMP (6), 
  "isUserAccountLocked" NUMBER(1, 0) DEFAULT (0), 
  "IsPinSet" NUMBER(1, 0) DEFAULT (0), 
  "IsEnrolledForOlb" NUMBER(1, 0) DEFAULT (0), 
  "IsAssistConsented" NUMBER(1, 0) DEFAULT (1), 
  "IsPhoneEnabled" NUMBER(1, 0) DEFAULT (0), 
  "IsEmailEnabled" NUMBER(1, 0) DEFAULT (0), 
  "isEnrolled" NUMBER(1, 0) DEFAULT (0), 
  "isSuperAdmin" NUMBER(1, 0) DEFAULT (0), 
  "CurrentLoginTime" TIMESTAMP (6), 
  "Lastlogintime" TIMESTAMP (6), 
  "IDType_id" NVARCHAR2(50) DEFAULT NULL, 
  "IDValue" NVARCHAR2(50) DEFAULT NULL, 
  "IDState" NVARCHAR2(50) DEFAULT NULL, 
  "IDCountry" NVARCHAR2(50) DEFAULT NULL, 
  "IDIssueDate" DATE DEFAULT NULL, 
  "IDExpiryDate" DATE DEFAULT NULL, 
  "IsCoreIdentityScope" NVARCHAR2(50) DEFAULT NULL, 
  "Is_MemberEligibile" NUMBER(1, 0) DEFAULT NULL, 
  "MemberEligibilityData" NVARCHAR2(100) DEFAULT NULL, 
  "Is_BBOA" NUMBER(1, 0) DEFAULT NULL, 
  "CreditUnionMemberSince" DATE DEFAULT NULL, 
  "AtionProfile_id" NVARCHAR2(50) DEFAULT NULL, 
  "RegistrationLink" NVARCHAR2(200) DEFAULT NULL, 
  "RegLinkResendCount" NUMBER(10, 0) DEFAULT NULL, 
  "RegLinkValidity" TIMESTAMP (6), 
  "areDepositTermsAccepted" NVARCHAR2(50) DEFAULT NULL, 
  "areAccountStatementTermsAccepted" NVARCHAR2(50) DEFAULT NULL, 
  "areUserAlertsTurnedOn" NUMBER(1, 0) DEFAULT (0), 
  "isBillPaySupported" NUMBER(1, 0) DEFAULT 1, 
  "isBillPayActivated" NUMBER(1, 0) DEFAULT 0, 
  "isP2PSupported" NUMBER(1, 0) DEFAULT 1, 
  "isP2PActivated" NUMBER(1, 0) DEFAULT 0, 
  "isWireTransferEligible" NUMBER(1, 0) DEFAULT 1, 
  "isWireTransferActivated" NUMBER(1, 0) DEFAULT 0, 
  "lockedOn" TIMESTAMP (6), 
  "isEagreementSigned" NUMBER(1, 0) DEFAULT (0), 
  "MothersMaidenName" NVARCHAR2(50) DEFAULT NULL, 
  "AddressValidationStatus" NVARCHAR2(50) DEFAULT NULL, 
  "Product" NVARCHAR2(300) DEFAULT NULL, 
  "EligbilityCriteria" NCLOB, 
  "Reason" NCLOB, 
  "ApplicantChannel" NVARCHAR2(50) DEFAULT NULL, 
  "DocumentsSubmitted" NCLOB, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "Bank_id" NVARCHAR2(50) DEFAULT '1', 
  "Session_id" NVARCHAR2(50) DEFAULT NULL, 
  "MaritalStatus" NVARCHAR2(50) DEFAULT NULL, 
  "SpouseFirstName" NVARCHAR2(50) DEFAULT NULL, 
  "SpouseLastName" NVARCHAR2(50) DEFAULT NULL, 
  "EmploymentInfo" NVARCHAR2(50) DEFAULT NULL, 
  "isEngageProvisioned" NUMBER(1, 0) DEFAULT (0), 
  "DefaultLanguage" NVARCHAR2(45) DEFAULT NULL, 
  "isVIPCustomer" NUMBER(1, 0) DEFAULT NULL, 
  "isdcode" NVARCHAR2(10) DEFAULT NULL, 
  "taxid" NVARCHAR2(10) DEFAULT NULL, 
  "isSignatory" NUMBER(1, 0) DEFAULT (0), 
  "sigtype" NVARCHAR2(50) DEFAULT NULL, 
  "combinedUserId" NVARCHAR2(45) DEFAULT NULL, 
  "isEnrolledFromSpotlight" NUMBER(3, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL', 
  "homeLegalEntity" VARCHAR2(50 CHAR), 
  "defaultLegalEntity" VARCHAR2(50 CHAR),
  "isQRPaymentActivated" NUMBER(1, 0) DEFAULT NULL
);
--  DDL for Table customeraccounts

CREATE TABLE "customeraccounts" (
  "id" NVARCHAR2(50), 
  "Customer_id" NVARCHAR2(50) DEFAULT NULL, 
  "Membership_id" NVARCHAR2(50) DEFAULT NULL, 
  "Account_id" NVARCHAR2(50) DEFAULT NULL, 
  "Organization_id" NVARCHAR2(45) DEFAULT NULL, 
  "AccountName" NVARCHAR2(50) DEFAULT NULL, 
  "FavouriteStatus" NUMBER(10, 0) DEFAULT (0), 
  "IsViewAllowed" NUMBER(1, 0) DEFAULT (0), 
  "IsDepositAllowed" NUMBER(1, 0) DEFAULT (0), 
  "IsWithdrawAllowed" NUMBER(1, 0) DEFAULT (0), 
  "IsOrganizationAccount" NUMBER(1, 0) DEFAULT (0), 
  "IsOrgAccountUnLinked" NUMBER(1, 0) DEFAULT (0), 
  "contractId" VARCHAR2(20 CHAR) DEFAULT NULL, 
  "coreCustomerId" VARCHAR2(20 CHAR) DEFAULT NULL, 
  "isBusinessAccount" VARCHAR2(20 CHAR) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6), 
  "lastmodifiedts" TIMESTAMP (6), 
  "accountType" VARCHAR2(50 CHAR), 
  "email" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "EStatementmentEnable" NUMBER(5, 0) DEFAULT (0), 
  "NickName" NVARCHAR2(50) DEFAULT NULL, 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL', 
  "isSweepCreated" NUMBER DEFAULT (0), 
  "accountStatus" VARCHAR2(20 CHAR) DEFAULT 'ACTIVE'
);

--  DDL for Table customeraction
--------------------------------------------------------

CREATE TABLE "customeraction" 
   (  "id" NVARCHAR2(50), 
  "RoleType_id" NVARCHAR2(50), 
  "Customer_id" NVARCHAR2(50), 
  "contractId" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "coreCustomerId" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "featureId" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "Action_id" NVARCHAR2(255), 
  "Account_id" NVARCHAR2(50) DEFAULT NULL, 
  "isAllowed" NUMBER(1,0), 
  "policyId" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "limitGroupId" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "LimitType_id" NVARCHAR2(50) DEFAULT NULL, 
  "value" NUMBER(20,2) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
   );

--  DDL for Table customeraddress

CREATE TABLE "customeraddress" (
  "Customer_id" NVARCHAR2(50), 
  "Address_id" NVARCHAR2(50), 
  "Type_id" NVARCHAR2(50) DEFAULT 'ADR_TYPE_HOME', 
  "isPrimary" NUMBER(1, 0) DEFAULT (0), 
  "DurationOfStay" NVARCHAR2(50) DEFAULT NULL, 
  "HomeOwnership" NVARCHAR2(50) DEFAULT NULL, 
  "isTypeBusiness" NVARCHAR2(45) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table customeralertcategorychannel

CREATE TABLE "customeralertcategorychannel" (
  "Customer_id" NVARCHAR2(50), 
  "AlertCategoryId" NVARCHAR2(50), 
  "ChannelId" NVARCHAR2(50), 
  "AccountId" NVARCHAR2(50) DEFAULT '*', 
  "AccountType" NVARCHAR2(50) DEFAULT '*', 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table customeralertchannel

CREATE TABLE "customeralertchannel" (
  "customerId" NVARCHAR2(50), 
  "alertCategoryId" NVARCHAR2(50), 
  "alertTypeId" VARCHAR2(50 CHAR), 
  "alertSubTypeId" VARCHAR2(75 CHAR), 
  "channelId" NVARCHAR2(50), 
  "accountId" VARCHAR2(50 CHAR), 
  "accountType" VARCHAR2(50 CHAR), 
  "createdby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "modifiedby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(5, 0) DEFAULT '0', 
  "companyLegalUnit" VARCHAR2(20 CHAR)
);
--  DDL for Table customeralertentitlement

CREATE TABLE "customeralertentitlement" (
  "Customer_id" NVARCHAR2(50), 
  "Alert_id" NVARCHAR2(50), 
  "IsSmsActive" NUMBER(1, 0) DEFAULT (0), 
  "IsEmailActive" NUMBER(1, 0) DEFAULT (0), 
  "IsPushActive" NUMBER(1, 0) DEFAULT (0), 
  "Value" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table customeralertfrequency

CREATE TABLE "customeralertfrequency" (
  "customerId" NVARCHAR2(50), 
  "alertCategoryId" NVARCHAR2(50), 
  "alertTypeId" VARCHAR2(50 CHAR), 
  "alertSubTypeId" VARCHAR2(75 CHAR), 
  "alertFrequencyId" VARCHAR2(10 CHAR), 
  "accountId" VARCHAR2(50 CHAR), 
  "accountType" VARCHAR2(50 CHAR), 
  "frequencyValue" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "frequencyTime" VARCHAR2(200 CHAR) DEFAULT NULL, 
  "createdby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "modifiedby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(5, 0) DEFAULT '0', 
  "companyLegalUnit" VARCHAR2(20 CHAR)
);
--  DDL for Table customeralertswitch

CREATE TABLE "customeralertswitch" (
  "Customer_id" NVARCHAR2(50), 
  "AccountID" NVARCHAR2(50) DEFAULT '*', 
  "AlertCategoryId" NVARCHAR2(50), 
  "AccountType" NVARCHAR2(50) DEFAULT '*', 
  "Status_id" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 BYTE) DEFAULT NULL
);
--  DDL for Table customercommunication

CREATE TABLE "customercommunication" (
  "id" NVARCHAR2(50), 
  "Type_id" NVARCHAR2(50), 
  "Customer_id" NVARCHAR2(50), 
  "isPrimary" NUMBER(1, 0) DEFAULT (0), 
  "isTypeBusiness" NVARCHAR2(45) DEFAULT NULL, 
  "Value" NVARCHAR2(100) DEFAULT NULL, 
  "Extension" NVARCHAR2(50) DEFAULT NULL, 
  "Description" NVARCHAR2(50) DEFAULT NULL, 
  "IsPreferredContactMethod" NUMBER(1, 0) DEFAULT NULL, 
  "PreferredContactTime" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "type" NVARCHAR2(50) DEFAULT NULL, 
  "countryType" NVARCHAR2(50) DEFAULT 'Domestic', 
  "receivePromotions" NVARCHAR2(45) DEFAULT NULL, 
  "phoneCountryCode" NVARCHAR2(10) DEFAULT NULL, 
  "isAlertsRequired" NUMBER(3, 0) DEFAULT '0', 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);

--  DDL for Table customerfile


CREATE TABLE "customerfile" ("id" NVARCHAR2(50), "customerfileclob" NCLOB, "createdby" NVARCHAR2(50) DEFAULT NULL, "modifiedby" NVARCHAR2(50) DEFAULT NULL, "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "softdeleteflag" NUMBER(1,0) DEFAULT (0));

--  DDL for Table customerdevice

CREATE TABLE "customerdevice" (
  "id" NVARCHAR2(50), 
  "Customer_id" NVARCHAR2(50), 
  "DeviceName" NVARCHAR2(50) DEFAULT NULL, 
  "LastLoginTime" TIMESTAMP (6), 
  "LastUsedIp" NVARCHAR2(50) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "OperatingSystem" NVARCHAR2(50) DEFAULT NULL, 
  "Channel_id" NVARCHAR2(50) DEFAULT NULL, 
  "EnrollmentDate" DATE DEFAULT NULL, 
  "appid" NVARCHAR2(100) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "isTypeBusiness" NVARCHAR2(45) DEFAULT NULL, 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table customerentitlement

CREATE TABLE "customerentitlement" (
  "Customer_id" NVARCHAR2(50), 
  "Service_id" NVARCHAR2(50), 
  "MaxTransactionLimit" NUMBER(20, 2) DEFAULT NULL, 
  "MaxDailyLimit" NUMBER(20, 2) DEFAULT NULL, 
  "TransactionFee_id" NVARCHAR2(50) DEFAULT NULL, 
  "TransactionLimit_id" NVARCHAR2(50) DEFAULT NULL, 
  "isTypeBusiness" NVARCHAR2(45) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table customerexpense

CREATE TABLE "customerexpense" (
  "Amount" NUMBER(20, 2) DEFAULT NULL, 
  "id" NVARCHAR2(50), 
  "Type" NVARCHAR2(50), 
  "Customer_id" NVARCHAR2(50)
);
--  DDL for Table customerflagstatus

CREATE TABLE "customerflagstatus" (
  "Customer_id" NVARCHAR2(50), 
  "Status_id" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table customergroup

CREATE TABLE "customergroup" (
  "Customer_id" NVARCHAR2(50), 
  "coreCustomerId" VARCHAR2(20 CHAR) DEFAULT NULL, 
  "contractId" VARCHAR2(20 CHAR) DEFAULT NULL, 
  "Group_id" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table customerimage

CREATE TABLE "customerimage" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "Customer_id" NVARCHAR2(50), 
  "UserImage" NCLOB, 
  "legalEntityId" VARCHAR2(20) DEFAULT 'ALL', 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);

--  DDL for Table customerlegalentity
--------------------------------------------------------

CREATE TABLE "customerlegalentity" 
   (  "id" NVARCHAR2(50), 
  "Customer_id" NVARCHAR2(50), 
  "Status_id" NVARCHAR2(20), 
  "legalEntityId" NVARCHAR2(50), 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "modifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP
   );

--  DDL for Table customerlimitgrouplimits

CREATE TABLE "customerlimitgrouplimits" (
  "id" NVARCHAR2(50), 
  "Customer_id" NVARCHAR2(50), 
  "contractId" NVARCHAR2(45), 
  "coreCustomerId" NVARCHAR2(45), 
  "limitGroupId" NVARCHAR2(45), 
  "LimitType_id" NVARCHAR2(50), 
  "value" NUMBER(20, 2), 
  "createdby" NVARCHAR2(50), 
  "modifiedby" NVARCHAR2(50), 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(5, 0) DEFAULT '0', 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table customerpreference

CREATE TABLE "customerpreference" (
  "id" NVARCHAR2(50), 
  "Customer_id" NVARCHAR2(50) DEFAULT NULL, 
  "isTypeBusiness" NVARCHAR2(45) DEFAULT NULL, 
  "DefaultAccountDeposit" NVARCHAR2(45) DEFAULT NULL, 
  "DefaultAccountTransfers" NVARCHAR2(50) DEFAULT NULL, 
  "DefaultModule_id" NVARCHAR2(50) DEFAULT NULL, 
  "DefaultAccountPayments" NVARCHAR2(50) DEFAULT NULL, 
  "DefaultAccountCardless" NVARCHAR2(50) DEFAULT NULL, 
  "DefaultAccountBillPay" NVARCHAR2(50) DEFAULT NULL, 
  "DefaultToAccountP2P" NVARCHAR2(50) DEFAULT NULL, 
  "DefaultFromAccountP2P" NVARCHAR2(50) DEFAULT NULL, 
  "DefaultAccountWire" NVARCHAR2(50) DEFAULT NULL, 
  "areUserAlertsTurnedOn" NVARCHAR2(50) DEFAULT NULL, 
  "areDepositTermsAccepted" NVARCHAR2(50) DEFAULT NULL, 
  "areAccountStatementTermsAccepted" NVARCHAR2(50) DEFAULT NULL, 
  "isBillPaySupported" NVARCHAR2(50) DEFAULT NULL, 
  "isP2PSupported" NVARCHAR2(50) DEFAULT NULL, 
  "isBillPayActivated" NVARCHAR2(50) DEFAULT NULL, 
  "isP2PActivated" NVARCHAR2(50) DEFAULT NULL, 
  "isWireTransferActivated" NVARCHAR2(50) DEFAULT NULL, 
  "isWireTransferEligible" NVARCHAR2(50) DEFAULT NULL, 
  "ShowBillPayFromAccPopup" NUMBER(1, 0) DEFAULT 0, 
  "PreferedOtpMethod" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table customerpreviouspasswords

CREATE TABLE "customerpreviouspasswords" (
  "id" NVARCHAR2(50), 
  "Customer_id" NVARCHAR2(50) DEFAULT NULL, 
  "PwdSequence" NUMBER(10, 0) DEFAULT NULL, 
  "Password" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" NVARCHAR2(50) DEFAULT NULL, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table customerproduct

CREATE TABLE "customerproduct" (
  "Customer_id" NVARCHAR2(50), 
  "Product_id" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table customerrequest

CREATE TABLE "customerrequest" (
  "id" NVARCHAR2(50), 
  "RequestCategory_id" NVARCHAR2(50), 
  "Customer_id" NVARCHAR2(50) DEFAULT NULL, 
  "Priority" NVARCHAR2(50) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "RequestSubject" NVARCHAR2(1000) DEFAULT NULL, 
  "AssignedTo" NVARCHAR2(50) DEFAULT NULL, 
  "Accountid" NVARCHAR2(50) DEFAULT NULL, 
  "lastupdatedbycustomer" NUMBER(1, 0) DEFAULT (0), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "isTypeBusiness" NVARCHAR2(45) DEFAULT NULL, 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table customersecurityquestions

CREATE TABLE "customersecurityquestions" (
  "Customer_id" NVARCHAR2(50), 
  "SecurityQuestion_id" NVARCHAR2(50), 
  "CustomerAnswer" NVARCHAR2(250), 
  "isTypeBusiness" NVARCHAR2(45) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP
);
--  DDL for Table customertermsandconditions

CREATE TABLE "customertermsandconditions" (
  "id" NVARCHAR2(50), 
  "customerId" NVARCHAR2(50), 
  "termsAndConditionsCode" NVARCHAR2(45), 
  "languageCode" NVARCHAR2(10), 
  "versionId" NVARCHAR2(45), 
  "isTypeBusiness" NVARCHAR2(45) DEFAULT NULL, 
  "appId" NVARCHAR2(45) DEFAULT NULL, 
  "channel" NVARCHAR2(45) DEFAULT NULL, 
  "platform" NVARCHAR2(45) DEFAULT NULL, 
  "browser" NVARCHAR2(45) DEFAULT NULL, 
  "createdby" NVARCHAR2(45) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP
);
--  DDL for Table customertype

CREATE TABLE "customertype" (
  "id" NVARCHAR2(50), 
  "Name" NVARCHAR2(50) DEFAULT NULL, 
  "Description" NVARCHAR2(100) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6), 
  "lastmodifiedts" TIMESTAMP (6), 
  "synctimestamp" TIMESTAMP (6), 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table customertypeconfig

CREATE TABLE "customertypeconfig" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "CustomerType_id" NVARCHAR2(45) DEFAULT NULL, 
  "Appid" NVARCHAR2(45) DEFAULT NULL, 
  "AccessPermitted" NUMBER(1, 0) DEFAULT 0
);
--  DDL for Table customerviewalertconfiguration

CREATE TABLE "customerviewalertconfiguration" (
  "id" NUMBER(10, 0), 
  "alertPreferenceView" VARCHAR2(10 CHAR) DEFAULT 'CATEGORY', 
  "enableFrequency" NUMBER(3, 0), 
  "enableSeparateContact" NUMBER(3, 0), 
  "createdby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "modifiedby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(3, 0) DEFAULT '0', 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table customroleaccounts

CREATE TABLE "customroleaccounts" (
  "id" VARCHAR2(50 CHAR), 
  "customRoleId" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "Account_id" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "AccountName" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "contractId" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "coreCustomerId" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "createdby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "modifiedby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT NULL, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT NULL, 
  "synctimestamp" TIMESTAMP (6) DEFAULT NULL, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "accountType" VARCHAR2(50 CHAR),
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table datatype


CREATE TABLE "datatype" ("id" NVARCHAR2(50), "Description" NVARCHAR2(100) DEFAULT NULL, "createdby" NVARCHAR2(50) DEFAULT NULL, "modifiedby" NVARCHAR2(50) DEFAULT NULL, "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "softdeleteflag" NUMBER(1,0) DEFAULT (0));

--  DDL for Table dbpconfig

CREATE TABLE "dbpconfig" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "Module" NVARCHAR2(50) DEFAULT NULL, 
  "FieldName" NVARCHAR2(50) DEFAULT NULL, 
  "FieldValue" NVARCHAR2(200) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6)
);
--  DDL for Table dbxalertcategory

CREATE TABLE "dbxalertcategory" (
  "id" NVARCHAR2(50), 
  "Name" NVARCHAR2(255) DEFAULT NULL, 
  "accountLevel" NUMBER(1, 0) DEFAULT NULL, 
  "status_id" NVARCHAR2(50) DEFAULT NULL, 
  "DisplaySequence" NUMBER(3, 0) DEFAULT NULL, 
  "defaultFrequencyId" VARCHAR2(10 CHAR), 
  "defaultFrequencyValue" VARCHAR2(50 CHAR), 
  "defaultFrequencyTime" DATE, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(20) DEFAULT NULL, 
  "createdts" DATE DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" DATE DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" DATE DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(200 CHAR)
);
--  DDL for Table dbxalertcategorytext

CREATE TABLE "dbxalertcategorytext" (
  "AlertCategoryId" NVARCHAR2(50), 
  "LanguageCode" NVARCHAR2(10), 
  "DisplayName" NVARCHAR2(255) DEFAULT NULL, 
  "Description" NVARCHAR2(1000) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(200 CHAR)
);
--  DDL for Table dbxalerttype

CREATE TABLE "dbxalerttype" (
  "id" NVARCHAR2(50), 
  "Name" NVARCHAR2(255) DEFAULT NULL, 
  "AlertCategoryId" NVARCHAR2(50), 
  "isAccountLevel" NUMBER(3, 0) DEFAULT '0', 
  "AttributeId" NVARCHAR2(50) DEFAULT NULL, 
  "AlertConditionId" NVARCHAR2(25) DEFAULT NULL, 
  "Value1" NVARCHAR2(255) DEFAULT NULL, 
  "Value2" NVARCHAR2(255) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "IsGlobal" NUMBER(1, 0) DEFAULT NULL, 
  "DisplaySequence" NUMBER(3, 0) DEFAULT NULL, 
  "defaultFrequencyId" VARCHAR2(10 CHAR) DEFAULT NULL, 
  "defaultFrequencyValue" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "defaultFrequencyTime" TIMESTAMP (6) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR)
);
--  DDL for Table dbxalerttypetext

CREATE TABLE "dbxalerttypetext" (
  "AlertTypeId" NVARCHAR2(50), 
  "LanguageCode" NVARCHAR2(10), 
  "DisplayName" NVARCHAR2(255) DEFAULT NULL, 
  "Description" NVARCHAR2(1000) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table dbxcustomeralertentitlement

CREATE TABLE "dbxcustomeralertentitlement" (
  "Customer_id" NVARCHAR2(50), 
  "AlertTypeId" NVARCHAR2(50), 
  "AccountId" NVARCHAR2(50), 
  "AccountType" NVARCHAR2(50), 
  "Value1" NVARCHAR2(255) DEFAULT NULL, 
  "Value2" NVARCHAR2(255) DEFAULT NULL, 
  "LastEventPushed" DATE DEFAULT NULL, 
  "Balance" NVARCHAR2(255) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "alertCategoryId" VARCHAR2(50 CHAR) DEFAULT '*', 
  "alertSubTypeId" VARCHAR2(75 CHAR) DEFAULT '*', 
  "alertRequestId" VARCHAR2(255 CHAR), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table decisionresult

CREATE TABLE "decisionresult" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "job_id" NVARCHAR2(50) DEFAULT NULL, 
  "decision_id" NVARCHAR2(50) DEFAULT NULL, 
  "baseAttributeName" NVARCHAR2(50) DEFAULT NULL, 
  "baseAttributeValue" NVARCHAR2(50) DEFAULT NULL, 
  "resultAttributeName" NVARCHAR2(50) DEFAULT NULL, 
  "resultAttributeValue" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6), 
  "softdeleteflag" NUMBER(1, 0) DEFAULT NULL, 
  "errmsg" NVARCHAR2(500) DEFAULT NULL, 
  "exception" NVARCHAR2(500) DEFAULT NULL
);

--  DDL for Table deviceregistration

CREATE TABLE "deviceregistration" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "User_id" NUMBER(10, 0) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "DeviceId" NVARCHAR2(50)
);
--  DDL for Table digitalprofile

CREATE TABLE "digitalprofile" (
  "Id" NVARCHAR2(50), 
  "Customer_id" NVARCHAR2(50) DEFAULT NULL, 
  "CreditScore" NUMBER(10, 0) DEFAULT NULL, 
  "NumberOfInquiries_6M" NUMBER(10, 0) DEFAULT NULL, 
  "NumberOfInquiries_12M" NUMBER(10, 0) DEFAULT NULL, 
  "NumberOfInquiries_24M" NUMBER(10, 0) DEFAULT NULL, 
  "TotalRevolvingOpenToBuyBalance" NUMBER(10, 2) DEFAULT NULL, 
  "UtilizationPercentOfRevolvingTrades" NVARCHAR2(50) DEFAULT NULL, 
  "SinceRecentDelinquency_M" NUMBER(10, 0) DEFAULT NULL, 
  "TotalNumberOfDerogatory" NUMBER(10, 0) DEFAULT NULL, 
  "SinceRecentlyFiledCollection_M" NUMBER(10, 0) DEFAULT NULL, 
  "TotalNumberOfTrades" NUMBER(10, 0) DEFAULT NULL, 
  "TotalNumberOfActiveTrades" NUMBER(10, 0) DEFAULT NULL, 
  "NumberOfTradesOpened_24M" NUMBER(10, 0) DEFAULT NULL, 
  "NumberOfTradeswithUtilization" NUMBER(10, 0) DEFAULT NULL, 
  "OldestOpenPersonalFinanceTrade_M" NUMBER(10, 0) DEFAULT NULL, 
  "LoanToIncomeRatio" NUMBER(10, 2) DEFAULT NULL, 
  "NumberOfLoanAapplications_24M" NVARCHAR2(50) DEFAULT NULL, 
  "DebtToIncomeRatio" NUMBER(10, 2) DEFAULT NULL, 
  "PrequalifyScore" NUMBER(10, 0) DEFAULT NULL, 
  "YearsOfMembership" NUMBER(10, 0) DEFAULT NULL, 
  "AccountsBalance" NUMBER(10, 2) DEFAULT NULL, 
  "Age" NVARCHAR2(50) DEFAULT NULL, 
  "City" NVARCHAR2(50) DEFAULT NULL, 
  "State" NVARCHAR2(50) DEFAULT NULL, 
  "ZipCode" NUMBER(10, 0) DEFAULT NULL, 
  "DurationOfStay" NUMBER(10, 2) DEFAULT NULL, 
  "HomeOwnership" NVARCHAR2(50) DEFAULT NULL, 
  "GrossMonthlyIncome" NUMBER(10, 2) DEFAULT NULL, 
  "AnnualIncome" NUMBER(12, 2) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6), 
  "lastmodifiedts" TIMESTAMP (6), 
  "synctimestamp" TIMESTAMP (6), 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table dmaddinteractions

CREATE TABLE "dmaddinteractions" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "navigationType" NVARCHAR2(50), 
  "navigationURL" NVARCHAR2(200), 
  "navigationId" NVARCHAR2(200), 
  "text" NVARCHAR2(200), 
  "colour" NVARCHAR2(200), 
  "dm_add_id" NUMBER(10, 0), 
  "textcolor" NVARCHAR2(50) DEFAULT NULL, 
  "buttonType" NVARCHAR2(200)
);
--  DDL for Table dmadvertisements

CREATE TABLE "dmadvertisements" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "description" NVARCHAR2(1000) DEFAULT NULL, 
  "imageURL" NVARCHAR2(200) DEFAULT NULL, 
  "adType" NVARCHAR2(50) DEFAULT NULL, 
  "navigationType" NVARCHAR2(50) DEFAULT NULL, 
  "navigationURL" NVARCHAR2(200) DEFAULT NULL, 
  "visible" NUMBER(1, 0) DEFAULT NULL, 
  "model" NVARCHAR2(50) DEFAULT NULL, 
  "flowPosition" NVARCHAR2(100) DEFAULT NULL, 
  "adTitle" NVARCHAR2(1000) DEFAULT NULL
);
--  DDL for Table emailtemplates

CREATE TABLE "emailtemplates" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "TemplateName" NVARCHAR2(100) DEFAULT NULL, 
  "TemplateText" CLOB,
  "Subject" NVARCHAR2(500) DEFAULT NULL, 
  "SenderName" NVARCHAR2(500) DEFAULT NULL, 
  "SenderEmail" NVARCHAR2(500) DEFAULT NULL, 
  "AlertChannel" NVARCHAR2(50) DEFAULT NULL, 
  "AlertLanguageCode" NVARCHAR2(50) DEFAULT NULL, 
  "Alert_id" NVARCHAR2(50) DEFAULT NULL
);
--  DDL for Table employementdetails

CREATE TABLE "employementdetails" (
  "id" NVARCHAR2(50), 
  "Customer_id" NVARCHAR2(50) DEFAULT NULL, 
  "EmploymentType" NVARCHAR2(50) DEFAULT NULL, 
  "CurrentEmployer" NVARCHAR2(50) DEFAULT NULL, 
  "Designation" NVARCHAR2(50) DEFAULT NULL, 
  "PayPeriod" NVARCHAR2(50) DEFAULT NULL, 
  "GrossIncome" NUMBER(10, 2) DEFAULT NULL, 
  "WeekWorkingHours" NVARCHAR2(50) DEFAULT NULL, 
  "EmploymentStartDate" DATE DEFAULT NULL, 
  "PreviousEmployer" NVARCHAR2(50) DEFAULT NULL, 
  "PreviousDesignation" NVARCHAR2(50) DEFAULT NULL, 
  "OtherEmployementType" NVARCHAR2(50) DEFAULT NULL, 
  "OtherEmployementDescription" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6), 
  "lastmodifiedts" TIMESTAMP (6), 
  "synctimestamp" TIMESTAMP (6), 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table errorstatuscode

CREATE TABLE "errorstatuscode" (
  "SNo" NVARCHAR2(10) DEFAULT NULL, 
  "Opstatus" NVARCHAR2(50) DEFAULT NULL, 
  "HttpStatusCode" NVARCHAR2(50) DEFAULT NULL, 
  "ErrorMsg" NVARCHAR2(200) DEFAULT NULL, 
  "Remarks" NVARCHAR2(100) DEFAULT NULL
);
--  DDL for Table event

CREATE TABLE "event" (
  "Event_id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "EventType" NVARCHAR2(50), 
  "EventSubType" NVARCHAR2(75) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "EventData" NCLOB, 
  "OtherData" NCLOB, 
  "IsProcessed" NUMBER(1, 0), 
  "Producer" NVARCHAR2(255), 
  "PreProcessorResult" NVARCHAR2(50) DEFAULT NULL, 
  "PostProcessorResult" NVARCHAR2(50) DEFAULT NULL, 
  "Session" NCLOB, 
  "Timestamp" TIMESTAMP (6) DEFAULT NULL
);
--  DDL for Table eventactivitytype

CREATE TABLE "eventactivitytype" (
  "id" NVARCHAR2(50), 
  "Description" NVARCHAR2(255) DEFAULT NULL, 
  "createdby" NVARCHAR2(255) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(255) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table eventconsumer

CREATE TABLE "eventconsumer" (
  "ServiceId" NVARCHAR2(50), 
  "OperationId" NVARCHAR2(50), 
  "BatchLimit" NUMBER(10, 0), 
  "LastEventId" NUMBER(10, 0)
);
--  DDL for Table eventconsumertypes

CREATE TABLE "eventconsumertypes" (
  "ServiceId" NVARCHAR2(50), 
  "OperationId" NVARCHAR2(50), 
  "EventType" NVARCHAR2(50)
);
--  DDL for Table eventsubtype

CREATE TABLE "eventsubtype" (
  "id" NVARCHAR2(75), 
  "eventtypeid" NVARCHAR2(50), 
  "Name" NVARCHAR2(255) DEFAULT NULL, 
  "Description" NVARCHAR2(255) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "externalSystem" NUMBER(5, 0) DEFAULT '0', 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table eventtopicconfiguration

CREATE TABLE "eventtopicconfiguration" (
  "eventCode" NVARCHAR2(100), 
  "topic" NVARCHAR2(255)
);
--  DDL for Table eventtype

CREATE TABLE "eventtype" (
  "id" NVARCHAR2(50), 
  "Name" NVARCHAR2(255), 
  "ActivityType" NVARCHAR2(50) DEFAULT NULL, 
  "Description" NVARCHAR2(255) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table exchangerates

CREATE TABLE "exchangerates" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "currency" NVARCHAR2(50) DEFAULT NULL, 
  "toCurrency" NVARCHAR2(50) DEFAULT NULL, 
  "currencyType" NVARCHAR2(50) DEFAULT NULL, 
  "exchangeRate" NUMBER(10, 4) DEFAULT NULL
);
--  DDL for Table excludedcontractaccounts

CREATE TABLE "excludedcontractaccounts" (
  "id" NVARCHAR2(50), 
  "contractId" NVARCHAR2(50), 
  "accountId" NVARCHAR2(50), 
  "accountName" NVARCHAR2(50), 
  "typeId" NVARCHAR2(50), 
  "coreCustomerId" NVARCHAR2(50), 
  "ownerType" NVARCHAR2(50), 
  "statusDesc" NVARCHAR2(50) DEFAULT 'Active', 
  "arrangementId" NVARCHAR2(50), 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "modifiedby" NVARCHAR2(50), 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table excludedcustomeraccounts

CREATE TABLE "excludedcustomeraccounts" (
  "id" NVARCHAR2(50), 
  "Customer_id" NVARCHAR2(50) DEFAULT NULL, 
  "Membership_id" NVARCHAR2(50) DEFAULT NULL, 
  "Account_id" NVARCHAR2(50) DEFAULT NULL, 
  "Organization_id" NVARCHAR2(45) DEFAULT NULL, 
  "AccountName" NVARCHAR2(50) DEFAULT NULL, 
  "FavouriteStatus" NUMBER(10, 0) DEFAULT (0), 
  "IsViewAllowed" NUMBER(1, 0) DEFAULT (0), 
  "IsDepositAllowed" NUMBER(1, 0) DEFAULT (0), 
  "IsWithdrawAllowed" NUMBER(1, 0) DEFAULT (0), 
  "IsOrganizationAccount" NUMBER(1, 0) DEFAULT (0), 
  "IsOrgAccountUnLinked" NUMBER(1, 0) DEFAULT (0), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6), 
  "lastmodifiedts" TIMESTAMP (6), 
  "contractId" VARCHAR2(20 CHAR) DEFAULT NULL, 
  "coreCustomerId" VARCHAR2(20 CHAR) DEFAULT NULL, 
  "isBusinessAccount" VARCHAR2(20 CHAR) DEFAULT NULL, 
  "email" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "EStatementmentEnable" NUMBER(5, 0) DEFAULT (0), 
  "accountType" VARCHAR2(50 CHAR), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table excludedcustomeraction

CREATE TABLE "excludedcustomeraction" (
  "id" VARCHAR2(50 CHAR), 
  "RoleType_id" VARCHAR2(50 CHAR), 
  "Customer_id" VARCHAR2(50 CHAR), 
  "contractId" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "coreCustomerId" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "featureId" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "Action_id" VARCHAR2(255 CHAR) DEFAULT NULL, 
  "Account_id" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "createdby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "modifiedby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table excludedcustomroleaccounts

CREATE TABLE "excludedcustomroleaccounts" (
  "id" VARCHAR2(50 CHAR), 
  "customRoleId" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "Account_id" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "AccountName" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "accountType" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "contractId" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "coreCustomerId" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "createdby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "modifiedby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "createdts" VARCHAR2(200 CHAR) DEFAULT NULL, 
  "lastmodifiedts" VARCHAR2(200 CHAR) DEFAULT NULL, 
  "synctimestamp" VARCHAR2(200 CHAR) DEFAULT NULL, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0),
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table expensecategory

CREATE TABLE "expensecategory" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "description" NVARCHAR2(50) DEFAULT NULL, 
  "isUndefined" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table expenseperiod

CREATE TABLE "expenseperiod" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "description" NVARCHAR2(50) DEFAULT NULL, 
  "startDate" DATE DEFAULT NULL, 
  "endDate" DATE DEFAULT NULL, 
  "amount" NUMBER(20, 2) DEFAULT NULL
);
--  DDL for Table externalaccount

CREATE TABLE "externalaccount" (
  "Id" NVARCHAR2(50), 
  "User_id" NVARCHAR2(50) DEFAULT NULL, 
  "organizationId" NVARCHAR2(45) DEFAULT NULL, 
  "Bank_id" NVARCHAR2(50) DEFAULT NULL, 
  "nickName" NVARCHAR2(100) DEFAULT NULL, 
  "firstName" NVARCHAR2(100) DEFAULT NULL, 
  "lastName" NVARCHAR2(100) DEFAULT NULL, 
  "routingNumber" NVARCHAR2(30) DEFAULT NULL, 
  "accountNumber" NVARCHAR2(45) DEFAULT NULL, 
  "accountType" NVARCHAR2(45) DEFAULT NULL, 
  "notes" NVARCHAR2(100) DEFAULT NULL, 
  "countryName" NVARCHAR2(100) DEFAULT NULL, 
  "swiftCode" NVARCHAR2(45) DEFAULT NULL, 
  "user_Account" NVARCHAR2(100) DEFAULT NULL, 
  "beneficiaryName" NVARCHAR2(100) DEFAULT NULL, 
  "isInternationalAccount" NUMBER(1, 0) DEFAULT NULL, 
  "bankName" NVARCHAR2(50) DEFAULT NULL, 
  "isSameBankAccount" NUMBER(1, 0) DEFAULT 1, 
  "softDelete" NUMBER(1, 0) DEFAULT 0, 
  "isVerified" NUMBER(1, 0) DEFAULT NULL, 
  "createdOn" DATE DEFAULT NULL, 
  "externalaccount" RAW(255), 
  "IBAN" NVARCHAR2(45) DEFAULT NULL, 
  "sortCode" NVARCHAR2(45) DEFAULT NULL, 
  "phoneCountryCode" NVARCHAR2(10) DEFAULT NULL, 
  "phoneNumber" NVARCHAR2(15) DEFAULT NULL, 
  "phoneExtension" NVARCHAR2(10) DEFAULT NULL, 
  "addressNickName" NVARCHAR2(45) DEFAULT NULL, 
  "addressLine1" NVARCHAR2(200) DEFAULT NULL, 
  "city" NVARCHAR2(45) DEFAULT NULL, 
  "zipcode" NVARCHAR2(45) DEFAULT NULL, 
  "country" NVARCHAR2(45) DEFAULT NULL, 
  "externalaccountcol" NVARCHAR2(45) DEFAULT NULL, 
  "addressLine2" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "email" VARCHAR2(50 CHAR) DEFAULT NULL
);
--  DDL for Table externalbank

CREATE TABLE "externalbank" (
  "id" NVARCHAR2(50), 
  "BankId" NVARCHAR2(50) DEFAULT NULL, 
  "Scheme" NVARCHAR2(5) DEFAULT NULL, 
  "Address" NVARCHAR2(45) DEFAULT NULL, 
  "BankName" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT NULL, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT NULL, 
  "synctimestamp" TIMESTAMP (6) DEFAULT NULL, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "IdentityProvider" NVARCHAR2(60) DEFAULT NULL, 
  "Oauth2" NUMBER(1, 0) DEFAULT NULL, 
  "logo" NVARCHAR2(200) DEFAULT NULL
);
--  DDL for Table externalbankidentity

CREATE TABLE "externalbankidentity" (
  "id" NVARCHAR2(50), 
  "ExternalBank_id" NVARCHAR2(50) DEFAULT NULL, 
  "MainUser_id" NVARCHAR2(50), 
  "User_id" NVARCHAR2(50) DEFAULT NULL, 
  "Password" NVARCHAR2(50) DEFAULT NULL, 
  "SessionToken" NVARCHAR2(200) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" VARCHAR2(200 CHAR) DEFAULT NULL, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table favouriteinstruments

CREATE TABLE "favouriteinstruments" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "userId" NVARCHAR2(50) DEFAULT NULL, 
  "customerId" NVARCHAR2(50) DEFAULT NULL, 
  "favInstrumentIds" NVARCHAR2(2000) DEFAULT NULL, 
  "favInstrumentCodes" NVARCHAR2(2000) DEFAULT NULL, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table feedback

CREATE TABLE "feedback" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "user_id" NVARCHAR2(50) DEFAULT NULL, 
  "rating" FLOAT(126) DEFAULT NULL, 
  "featureRequest" NVARCHAR2(1000) DEFAULT NULL, 
  "description" NVARCHAR2(1000) DEFAULT NULL, 
  "likeMost" NVARCHAR2(100) DEFAULT NULL, 
  "improvement" NVARCHAR2(500) DEFAULT NULL
);
--  DDL for Table feedbackstatus

CREATE TABLE "feedbackstatus" (
  "id" NVARCHAR2(50), 
  "UserName" NVARCHAR2(50) DEFAULT NULL, 
  "feedbackID" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "status" NUMBER(1, 0) DEFAULT (0), 
  "deviceID" NVARCHAR2(50) DEFAULT NULL, 
  "customerID" NVARCHAR2(50) DEFAULT NULL
);

--  DDL for Table groupentitlement

CREATE TABLE "groupentitlement" (
  "Group_id" NVARCHAR2(50), 
  "Service_id" NVARCHAR2(50), 
  "TransactionFee_id" NVARCHAR2(50) DEFAULT NULL, 
  "TransactionLimit_id" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table holidays

CREATE TABLE "holidays" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "holidayDate" TIMESTAMP (6), 
  "createdOn" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "updatedOn" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "createdBy" NVARCHAR2(45) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table iban

CREATE TABLE "iban" (
  "id" NUMBER(10, 0), 
  "IBAN" NVARCHAR2(45) DEFAULT NULL, 
  "bankName" NVARCHAR2(45) DEFAULT NULL
);
--  DDL for Table idmconfiguration

CREATE TABLE "idmconfiguration" (
  "id" NVARCHAR2(10), 
  "IDMKey" NVARCHAR2(45) DEFAULT NULL, 
  "IDMValue" NVARCHAR2(45) DEFAULT NULL
);

--  DDL for Table informationcontent

CREATE TABLE "informationcontent" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "informationType" NVARCHAR2(50) DEFAULT NULL, 
  "informationContent" NCLOB DEFAULT TO_NCLOB(NULL)
);
--  DDL for Table interestrates

CREATE TABLE "interestrates" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "cdterm" NVARCHAR2(50) DEFAULT NULL, 
  "apy" NVARCHAR2(50) DEFAULT NULL, 
  "minimumDeposit" NUMBER(10, 2) DEFAULT NULL
);
--  DDL for Table issuerimage


CREATE TABLE "issuerimage" ("id" NVARCHAR2(50), "issuerName" NVARCHAR2(50), "Status_id" NVARCHAR2(50), "Image" NCLOB, "createdby" NVARCHAR2(50) DEFAULT NULL, "modifiedby" NVARCHAR2(50) DEFAULT NULL, "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "softdeleteflag" NUMBER(1,0) DEFAULT (0));


--  DDL for Table linkconfig
--------------------------------------------------------

CREATE TABLE "linkconfig" 
   (  "EncodedLink" NVARCHAR2(100), 
  "LinkType" NVARCHAR2(50) DEFAULT NULL, 
  "UserName" NVARCHAR2(50) DEFAULT NULL
   );

--  DDL for Table loanschedule

CREATE TABLE "loanschedule" (
  "id" NUMBER(10, 0), 
  "AccountId" VARCHAR2(50 CHAR), 
  "Amount" VARCHAR2(50 CHAR), 
  "Principal" VARCHAR2(50 CHAR), 
  "Interest" VARCHAR2(50 CHAR), 
  "OutstandingBalance" VARCHAR2(50 CHAR), 
  "Charges" VARCHAR2(50 CHAR), 
  "Tax" VARCHAR2(50 CHAR), 
  "Insurance" VARCHAR2(50 CHAR), 
  "CumulativeInterest" VARCHAR2(50 CHAR), 
  "InstallmentType" VARCHAR2(50 CHAR), 
  "Date" DATE
);
--  DDL for Table locale

CREATE TABLE "locale" (
  "Code" NVARCHAR2(10), 
  "Language" NVARCHAR2(300) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table location

CREATE TABLE "location" (
  "id" NVARCHAR2(50), 
  "Type_id" NVARCHAR2(50) DEFAULT NULL, 
  "Code" NVARCHAR2(50), 
  "Name" NVARCHAR2(100) DEFAULT NULL, 
  "DisplayName" NVARCHAR2(100) DEFAULT NULL, 
  "Description" NVARCHAR2(300) DEFAULT NULL, 
  "PhoneNumber" NVARCHAR2(50) DEFAULT NULL, 
  "EmailId" NVARCHAR2(50) DEFAULT NULL, 
  "Address_id" NVARCHAR2(50) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "WorkingDays" NVARCHAR2(50) DEFAULT NULL, 
  "WorkSchedule_id" NVARCHAR2(50) DEFAULT NULL, 
  "IsMainBranch" NUMBER(1, 0) DEFAULT (0), 
  "MainBranchCode" NVARCHAR2(50) DEFAULT NULL, 
  "WebSiteUrl" NVARCHAR2(100) DEFAULT NULL, 
  "isMobile" NUMBER(3, 0) DEFAULT (0), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table locationlanguage

CREATE TABLE "locationlanguage" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "Location_id" NUMBER(10, 0), 
  "description" NVARCHAR2(100) DEFAULT NULL
);

--  DDL for Table locationtype

CREATE TABLE "locationtype" (
  "id" NVARCHAR2(50), 
  "Description" NVARCHAR2(100) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" NVARCHAR2(50) DEFAULT NULL, 
  "lastmodifiedts" NVARCHAR2(50) DEFAULT NULL, 
  "synctimestamp" TIMESTAMP (6) DEFAULT NULL, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);

--  DDL for Table lockobjects

CREATE TABLE "lockobjects" (
  "ObjectId" NVARCHAR2(45) DEFAULT (' '), 
  "User" NVARCHAR2(15) DEFAULT (' '), 
  "ExternalId" NVARCHAR2(45) DEFAULT (' '), 
  "ObjectName" NVARCHAR2(45) DEFAULT NULL, 
  "Mode" NVARCHAR2(45) DEFAULT NULL, 
  "Locked" NVARCHAR2(1) DEFAULT NULL, 
  "currenttimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP
);
--  DDL for Table market

CREATE TABLE "market" (
  "id" VARCHAR2(20 CHAR), 
  "name" VARCHAR2(50 CHAR)
);
--  DDL for Table media

CREATE TABLE "media" (
  "id" NVARCHAR2(50), 
  "Name" NVARCHAR2(100), 
  "Type" NVARCHAR2(300), 
  "Description" NVARCHAR2(100), 
  "Url" NVARCHAR2(200) DEFAULT NULL, 
  "Content" BLOB, 
  "Size" NVARCHAR2(50) DEFAULT '0', 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table membereligibility

CREATE TABLE "membereligibility" (
  "id" NVARCHAR2(50), 
  "ConditionName" NVARCHAR2(50), 
  "ConditionValues" NVARCHAR2(200) DEFAULT NULL, 
  "ConditionLabel" NVARCHAR2(200) DEFAULT NULL, 
  "AdditionalConsideration" NVARCHAR2(100) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6), 
  "lastmodifiedts" TIMESTAMP (6), 
  "synctimestamp" TIMESTAMP (6), 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table membergroup

CREATE TABLE "membergroup" (
  "id" NVARCHAR2(50), 
  "Name" NVARCHAR2(100) DEFAULT NULL, 
  "Description" NVARCHAR2(250) DEFAULT NULL, 
  "Type_id" NVARCHAR2(50) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "isEAgreementActive" NUMBER(1, 0) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "isApplicabletoAllServices" NUMBER(5, 0) DEFAULT '0', 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table membership

CREATE TABLE "membership" (
  "id" NVARCHAR2(50), 
  "isCustomerCentric" NUMBER(1, 0) DEFAULT (0), 
  "name" NVARCHAR2(45) DEFAULT NULL, 
  "firstName" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "lastName" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "dateOfBirth" DATE DEFAULT NULL, 
  "ssn" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "taxId" NVARCHAR2(45) DEFAULT NULL, 
  "phone" NVARCHAR2(45) DEFAULT NULL, 
  "email" NVARCHAR2(45) DEFAULT NULL, 
  "faxId" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "isBusinessType" NUMBER(1, 0) DEFAULT (0), 
  "addressId" NVARCHAR2(50) DEFAULT NULL, 
  "status" VARCHAR2(50 CHAR), 
  "industry" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "createdby" NVARCHAR2(45) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(45) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP,
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table membershipaccounts

CREATE TABLE "membershipaccounts" (
  "id" NVARCHAR2(50), 
  "membershipId" NVARCHAR2(50), 
  "accountId" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP
);
--  DDL for Table membershipowner

CREATE TABLE "membershipowner" (
  "id" NVARCHAR2(50), 
  "membershipId" NVARCHAR2(50), 
  "userName" NVARCHAR2(50), 
  "firstName" NVARCHAR2(50) DEFAULT NULL, 
  "lastName" NVARCHAR2(50), 
  "dateOfBirth" DATE, 
  "ssn" NVARCHAR2(50) DEFAULT NULL, 
  "taxId" NVARCHAR2(50) DEFAULT NULL, 
  "phone" NVARCHAR2(50) DEFAULT NULL, 
  "email" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "memberType" NVARCHAR2(45) DEFAULT NULL, 
  "salutation" NVARCHAR2(45) DEFAULT NULL, 
  "maritalStatus" NVARCHAR2(45) DEFAULT NULL, 
  "employmentStatus" NVARCHAR2(45) DEFAULT NULL, 
  "memberTypeId" NVARCHAR2(45) DEFAULT NULL, 
  "memberTypeName" NVARCHAR2(45) DEFAULT NULL
);
--  DDL for Table membershiprelation

CREATE TABLE "membershiprelation" (
  "id" VARCHAR2(50 CHAR), 
  "membershipId" VARCHAR2(50 CHAR), 
  "relatedMebershipId" VARCHAR2(50 CHAR), 
  "relationshipId" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "relationshipName" VARCHAR2(50 CHAR),
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table message

CREATE TABLE "message" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "Account_id" NUMBER(19, 0) DEFAULT NULL, 
  "Category_id" NUMBER(10, 0) DEFAULT NULL, 
  "Subcategory_id" NUMBER(10, 0) DEFAULT NULL, 
  "subject" NVARCHAR2(100) DEFAULT NULL, 
  "message" NVARCHAR2(512) DEFAULT NULL, 
  "sentDate" TIMESTAMP (6), 
  "status" NVARCHAR2(7) DEFAULT NULL, 
  "isSoftDeleted" NUMBER(1, 0) DEFAULT (0), 
  "isRead" NUMBER(1, 0) DEFAULT (0), 
  "createdDate" TIMESTAMP (6), 
  "receivedDate" TIMESTAMP (6), 
  "softdeletedDate" TIMESTAMP (6)
);
--  DDL for Table messageattachment

CREATE TABLE "messageattachment" (
  "id" NVARCHAR2(50), 
  "RequestMessage_id" NVARCHAR2(50) DEFAULT NULL, 
  "AttachmentType_id" NVARCHAR2(50) DEFAULT NULL, 
  "Media_id" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table messagecategory

CREATE TABLE "messagecategory" (
  "Id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "category" NVARCHAR2(45)
);
--  DDL for Table messagesubcategory

CREATE TABLE "messagesubcategory" (
  "Id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "subcategory" NVARCHAR2(45), 
  "Category_id" NUMBER(10, 0)
);
--  DDL for Table messagetype

CREATE TABLE "messagetype" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "Description" NVARCHAR2(100) DEFAULT NULL
);
--  DDL for Table mfaservice

CREATE TABLE "mfaservice" (
  "serviceKey" NVARCHAR2(50), 
  "serviceName" NVARCHAR2(50) DEFAULT NULL, 
  "User_id" NVARCHAR2(50) DEFAULT NULL, 
  "Createddts" TIMESTAMP (6) DEFAULT NULL, 
  "retryCount" NUMBER(10, 0) DEFAULT (0), 
  "payload" NCLOB, 
  "securityQuestions" NCLOB, 
  "isVerified" NVARCHAR2(5) DEFAULT NULL
);
--  DDL for Table mfaserviceconfig

CREATE TABLE "mfaserviceconfig" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "serviceName" NVARCHAR2(50), 
  "transactionType" NVARCHAR2(120) DEFAULT NULL, 
  "field" NVARCHAR2(50) DEFAULT NULL, 
  "value" NVARCHAR2(50) DEFAULT NULL, 
  "appId" NVARCHAR2(50) DEFAULT NULL
);
--  DDL for Table module

CREATE TABLE "module" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "description" NVARCHAR2(100) DEFAULT NULL
);
--  DDL for Table newaccount

CREATE TABLE "newaccount" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "firstName" NVARCHAR2(50) DEFAULT NULL, 
  "lastName" NVARCHAR2(50) DEFAULT NULL, 
  "address" NVARCHAR2(50) DEFAULT NULL, 
  "dateofbirth" DATE DEFAULT NULL, 
  "ssn" NVARCHAR2(50) DEFAULT NULL, 
  "accountType" NUMBER(10, 0) DEFAULT NULL, 
  "locationId" NUMBER(10, 0) DEFAULT NULL, 
  "productId" NUMBER(10, 0) DEFAULT NULL, 
  "userId" NUMBER(10, 0) DEFAULT NULL
);
--  DDL for Table newuser

CREATE TABLE "newuser" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "userName" NVARCHAR2(50) DEFAULT NULL, 
  "passWord" NVARCHAR2(50) DEFAULT NULL, 
  "role" NVARCHAR2(50) DEFAULT NULL, 
  "email" NVARCHAR2(50) DEFAULT NULL, 
  "phone" NVARCHAR2(50) DEFAULT NULL
);
--  DDL for Table notification

CREATE TABLE "notification" (
  "notificationId" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "notificationModule" NVARCHAR2(100) DEFAULT NULL, 
  "notificationSubModule" NVARCHAR2(100) DEFAULT NULL, 
  "notificationSubject" NVARCHAR2(1000) DEFAULT NULL, 
  "notificationText" NCLOB DEFAULT TO_NCLOB(NULL), 
  "notificationActionLink" NVARCHAR2(500) DEFAULT NULL, 
  "notificationCategory" NVARCHAR2(255) DEFAULT NULL, 
  "actionButtonLabelName" NVARCHAR2(255) DEFAULT NULL, 
  "imageURL" NVARCHAR2(200) DEFAULT NULL, 
  "isRead" NVARCHAR2(100) DEFAULT NULL, 
  "receivedDate" TIMESTAMP (6) DEFAULT NULL, 
  "user" RAW(255),
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table notificationcardinfo

CREATE TABLE "notificationcardinfo" (
  "id" NVARCHAR2(50), 
  "Notification_id" NVARCHAR2(50) DEFAULT NULL, 
  "Customer_id" NVARCHAR2(50) DEFAULT NULL, 
  "CardNumber" NVARCHAR2(100), 
  "CardName" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(45) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "isTypeBusiness" NVARCHAR2(45) DEFAULT NULL
);
--  DDL for Table numberrange

CREATE TABLE "numberrange" (
  "ObjectId" NVARCHAR2(45) DEFAULT (' '), 
  "Length" NUMBER(10, 0) DEFAULT NULL, 
  "BankId" NVARCHAR2(50) DEFAULT NULL, 
  "ObjectName" NVARCHAR2(45) DEFAULT NULL, 
  "CurrentValue" NUMBER(10, 0) DEFAULT NULL, 
  "StartValue" NUMBER(10, 0) DEFAULT NULL, 
  "EndValue" NUMBER(10, 0) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP
);
--  DDL for Table operatinghours

CREATE TABLE "operatinghours" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "Location_id" NUMBER(10, 0), 
  "description" NVARCHAR2(100) DEFAULT NULL, 
  "operatingDay" NVARCHAR2(50) DEFAULT NULL, 
  "startHour" NVARCHAR2(10) DEFAULT NULL, 
  "endHour" NVARCHAR2(10) DEFAULT NULL
);
--  DDL for Table organisation

CREATE TABLE "organisation" (
  "id" NVARCHAR2(50), 
  "Type_Id" NVARCHAR2(50) DEFAULT NULL, 
  "Name" NVARCHAR2(50) DEFAULT NULL, 
  "Description" NVARCHAR2(200) DEFAULT NULL, 
  "BusinessType_id" NVARCHAR2(50) DEFAULT NULL, 
  "StatusId" NVARCHAR2(50) DEFAULT 'SID_ORG_PENDING', 
  "FaxId" NVARCHAR2(45) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "rejectedby" NVARCHAR2(50) DEFAULT NULL, 
  "rejectedts" TIMESTAMP (6), 
  "rejectedReason" NVARCHAR2(45) DEFAULT NULL
);
--  DDL for Table organisationaccounts

CREATE TABLE "organisationaccounts" (
  "id" NVARCHAR2(50), 
  "Organization_id" NVARCHAR2(50), 
  "Account_id" NVARCHAR2(50), 
  "AccountName" NVARCHAR2(50) DEFAULT NULL, 
  "TypeID" NVARCHAR2(50), 
  "Membership_id" NVARCHAR2(50), 
  "Taxid" NVARCHAR2(50), 
  "SearchCriteria" NVARCHAR2(13) DEFAULT NULL, 
  "SearchValue" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "StatusDesc" NVARCHAR2(45) DEFAULT 'Active'
);
--  DDL for Table organisationaddress

CREATE TABLE "organisationaddress" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "Organization_id" NVARCHAR2(50) DEFAULT NULL, 
  "Address_id" NVARCHAR2(45) DEFAULT NULL, 
  "DurationOfStay" NVARCHAR2(45) DEFAULT NULL, 
  "IsPrimary" NUMBER(1, 0) DEFAULT 0, 
  "createdby" NVARCHAR2(45) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(45) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "Type_id" NVARCHAR2(45) DEFAULT NULL, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT 0
);
--  DDL for Table organisationcommunication

CREATE TABLE "organisationcommunication" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "Type_id" NVARCHAR2(50) DEFAULT NULL, 
  "Organization_id" NVARCHAR2(50), 
  "Sequence" NUMBER(10, 0) DEFAULT NULL, 
  "Value" NVARCHAR2(100) DEFAULT NULL, 
  "Extension" NVARCHAR2(45) DEFAULT NULL, 
  "Description" NVARCHAR2(45) DEFAULT NULL, 
  "IsPreferredContactMethod" NUMBER(1, 0) DEFAULT 0, 
  "PreferredContactTime" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6), 
  "lastmodifiedts" TIMESTAMP (6), 
  "synctimestamp" TIMESTAMP (6), 
  "softdeleteflag" NUMBER(1, 0) DEFAULT 0
);
--  DDL for Table organisationemployees

CREATE TABLE "organisationemployees" (
  "id" NVARCHAR2(50), 
  "Organization_id" NVARCHAR2(50) DEFAULT NULL, 
  "Customer_id" NVARCHAR2(50) DEFAULT NULL, 
  "Is_Admin" NUMBER(1, 0) DEFAULT 0, 
  "Is_Owner" NUMBER(1, 0) DEFAULT 0, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6), 
  "lastmodifiedts" TIMESTAMP (6), 
  "synctimestamp" TIMESTAMP (6), 
  "softdeleteflag" NUMBER(1, 0) DEFAULT 0, 
  "isAuthSignatory" NUMBER(1, 0) DEFAULT 0
);
--  DDL for Table organisationmembership

CREATE TABLE "organisationmembership" (
  "id" NVARCHAR2(50), 
  "Organization_id" NVARCHAR2(50) DEFAULT NULL, 
  "Taxid" NVARCHAR2(50) DEFAULT NULL, 
  "Membership_id" NVARCHAR2(50) DEFAULT NULL
);
--  DDL for Table organisationowner

CREATE TABLE "organisationowner" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "Organization_id" NVARCHAR2(50) DEFAULT NULL, 
  "FirstName" NVARCHAR2(50) DEFAULT NULL, 
  "MidleName" NVARCHAR2(50) DEFAULT NULL, 
  "LastName" NVARCHAR2(50) DEFAULT NULL, 
  "DateOfBirth" DATE DEFAULT NULL, 
  "IDType_id" NVARCHAR2(50) DEFAULT NULL, 
  "IdValue" NVARCHAR2(50) DEFAULT NULL, 
  "Email" NVARCHAR2(50) DEFAULT NULL, 
  "Phone" NVARCHAR2(20) DEFAULT NULL, 
  "Ssn" NVARCHAR2(45) DEFAULT NULL
);
--  DDL for Table organisationtype

CREATE TABLE "organisationtype" (
  "id" NVARCHAR2(50), 
  "Name" NVARCHAR2(50) DEFAULT NULL, 
  "Description" NVARCHAR2(200) DEFAULT NULL
);

--  DDL for Table othersourceofincome

CREATE TABLE "othersourceofincome" (
  "id" NVARCHAR2(50), 
  "IncomeInfo_id" NVARCHAR2(50) DEFAULT NULL, 
  "SourceType" NVARCHAR2(50), 
  "PayPeriod" NVARCHAR2(50) DEFAULT NULL, 
  "GrossIncome" NUMBER(10, 2) DEFAULT NULL, 
  "WeekWorkingHours" NVARCHAR2(50) DEFAULT NULL, 
  "Customer_id" NVARCHAR2(50) DEFAULT NULL, 
  "SourceOfIncomeName" NVARCHAR2(50) DEFAULT NULL, 
  "SourceofIncomeDescription" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6), 
  "lastmodifiedts" TIMESTAMP (6), 
  "synctimestamp" TIMESTAMP (6), 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);

--  DDL for Table otp
CREATE TABLE "OTP" 
   (  "securityKey" NVARCHAR2(50), 
  "Otp" NVARCHAR2(10) DEFAULT NULL, 
  "OtpType" NVARCHAR2(45) DEFAULT NULL, 
  "InvalidAttempt" NUMBER(10,0) DEFAULT (0), 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "Phone" NVARCHAR2(45) DEFAULT NULL, 
  "User_id" NVARCHAR2(50) DEFAULT NULL, 
  "serviceKey" NVARCHAR2(50) DEFAULT NULL, 
  "NumberOfRetries" NUMBER(10,0) DEFAULT (0), 
  "Email" NVARCHAR2(100) DEFAULT NULL
   );

--  DDL for Table otpcount

CREATE TABLE "otpcount" (
  "key" NVARCHAR2(50), 
  "User_id" NVARCHAR2(50) DEFAULT NULL, 
  "Date" NVARCHAR2(20), 
  "Count" NUMBER(10, 0) DEFAULT (1), 
  "Phone" NVARCHAR2(15) DEFAULT NULL, 
  "Email" NVARCHAR2(50) DEFAULT NULL
);
--  DDL for Table p2pregistration

CREATE TABLE "p2pregistration" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "displayName" NVARCHAR2(50) DEFAULT NULL, 
  "account_id" NUMBER(19, 0) DEFAULT NULL, 
  "isNpp" NUMBER(1, 0) DEFAULT (0), 
  "isZell" NUMBER(1, 0) DEFAULT (0), 
  "email" NVARCHAR2(50) DEFAULT NULL, 
  "user_id" NUMBER(10, 0) DEFAULT NULL, 
  "phone" NVARCHAR2(50) DEFAULT NULL, 
  "account" RAW(255)
);

--  DDL for Table permissiontype


CREATE TABLE "permissiontype" ("id" NVARCHAR2(50), "Description" NVARCHAR2(100) DEFAULT NULL, "createdby" NVARCHAR2(50) DEFAULT NULL, "modifiedby" NVARCHAR2(50) DEFAULT NULL, "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "softdeleteflag" NUMBER(1,0) DEFAULT (0));

--  DDL for Table passwordpolicy


CREATE TABLE "passwordpolicy" ("id" NVARCHAR2(50), "PolicyName" NVARCHAR2(50), "Description" NCLOB, "createdby" NVARCHAR2(50) DEFAULT NULL, "modifiedby" NVARCHAR2(50) DEFAULT NULL, "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "softdeleteflag" NUMBER(1,0) DEFAULT (0));

--  DDL for Table payee

CREATE TABLE "payee" (
  "Id" NVARCHAR2(50), 
  "Type_id" NVARCHAR2(50) DEFAULT NULL, 
  "name" NVARCHAR2(50), 
  "accountNumber" NVARCHAR2(50), 
  "companyName" NVARCHAR2(50) DEFAULT NULL, 
  "phone" NVARCHAR2(50) DEFAULT NULL, 
  "email" NVARCHAR2(100) DEFAULT NULL, 
  "firstName" NVARCHAR2(50) DEFAULT NULL, 
  "lastName" NVARCHAR2(50) DEFAULT NULL, 
  "eBillEnable" NUMBER(10, 0) DEFAULT (0), 
  "Region_id" NUMBER(10, 0) DEFAULT NULL, 
  "City_id" NUMBER(10, 0) DEFAULT NULL, 
  "cityName" NVARCHAR2(50) DEFAULT NULL, 
  "state" NVARCHAR2(50) DEFAULT NULL, 
  "addressLine1" NVARCHAR2(50) DEFAULT NULL, 
  "addressLine2" NVARCHAR2(50) DEFAULT NULL, 
  "zipCode" NVARCHAR2(20) DEFAULT NULL, 
  "User_Id" NVARCHAR2(50) DEFAULT NULL, 
  "nickName" NVARCHAR2(50), 
  "softDelete" NUMBER(1, 0) DEFAULT (0), 
  "billermaster_id" NUMBER(10, 0) DEFAULT (1), 
  "isAutoPayEnabled" NUMBER(1, 0) DEFAULT (0), 
  "nameOnBill" NVARCHAR2(50) DEFAULT NULL, 
  "notes" NVARCHAR2(50) DEFAULT NULL, 
  "billerId" NVARCHAR2(50) DEFAULT NULL, 
  "country" NVARCHAR2(50) DEFAULT NULL, 
  "swiftCode" NVARCHAR2(50) DEFAULT NULL, 
  "routingCode" NVARCHAR2(50) DEFAULT NULL, 
  "bankName" NVARCHAR2(50) DEFAULT NULL, 
  "bankAddressLine1" NVARCHAR2(50) DEFAULT NULL, 
  "bankAddressLine2" NVARCHAR2(50) DEFAULT NULL, 
  "bankCity" NVARCHAR2(50) DEFAULT NULL, 
  "bankState" NVARCHAR2(50) DEFAULT NULL, 
  "bankZip" NVARCHAR2(50) DEFAULT NULL, 
  "isWiredRecepient" NUMBER(1, 0) DEFAULT (0), 
  "internationalAccountNumber" NVARCHAR2(50) DEFAULT NULL, 
  "wireAccountType" NVARCHAR2(50) DEFAULT NULL, 
  "internationalRoutingCode" NVARCHAR2(50) DEFAULT NULL, 
  "isManuallyAdded" NUMBER(1, 0) DEFAULT (0), 
  "phoneExtension" NVARCHAR2(10) DEFAULT NULL, 
  "phoneCountryCode" NVARCHAR2(10) DEFAULT NULL, 
  "IBAN" NVARCHAR2(45) DEFAULT NULL, 
  "transitDays" NVARCHAR2(50) DEFAULT '3', 
  "organizationId" NVARCHAR2(45) DEFAULT NULL
);
--  DDL for Table payeeaddress

CREATE TABLE "payeeaddress" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "Region_id" NUMBER(10, 0) DEFAULT NULL, 
  "City_id" NUMBER(10, 0) DEFAULT NULL, 
  "cityName" NVARCHAR2(100) DEFAULT NULL, 
  "addressLine1" NVARCHAR2(100) DEFAULT NULL, 
  "addressLine2" NVARCHAR2(100) DEFAULT NULL, 
  "zipCode" NVARCHAR2(20) DEFAULT NULL, 
  "latitude" NVARCHAR2(50) DEFAULT NULL, 
  "logitude" NVARCHAR2(50) DEFAULT NULL
);
--  DDL for Table payeetype

CREATE TABLE "payeetype" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "description" NVARCHAR2(50) DEFAULT NULL
);
--  DDL for Table paymentfiles

CREATE TABLE "paymentfiles" (
  "paymentFileID" VARCHAR2(40 CHAR), 
  "userId" VARCHAR2(50 CHAR), 
  "transactionId" VARCHAR2(45 CHAR), 
  "paymentFileName" VARCHAR2(150 CHAR), 
  "paymentFileType" VARCHAR2(45 CHAR), 
  "paymentFileContents" NCLOB, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP
);
--  DDL for Table payperson

CREATE TABLE "payperson" (
  "id" NVARCHAR2(50), 
  "firstName" NVARCHAR2(45) DEFAULT NULL, 
  "lastName" NVARCHAR2(45) DEFAULT NULL, 
  "phone" NVARCHAR2(45) DEFAULT NULL, 
  "email" NVARCHAR2(45) DEFAULT NULL, 
  "User_id" NVARCHAR2(50), 
  "secondaryEmail" NVARCHAR2(100) DEFAULT NULL, 
  "secondoryPhoneNumber" NVARCHAR2(100) DEFAULT NULL, 
  "secondaryEmail2" NVARCHAR2(100) DEFAULT NULL, 
  "secondaryPhoneNumber2" NVARCHAR2(100) DEFAULT NULL, 
  "primaryContactForSending" NVARCHAR2(100) DEFAULT NULL, 
  "nickName" NVARCHAR2(50) DEFAULT NULL, 
  "name" NVARCHAR2(45) DEFAULT NULL, 
  "isSoftDelete" NUMBER(1, 0) DEFAULT 0, 
  "phoneExtension" NVARCHAR2(10) DEFAULT NULL, 
  "phoneCountryCode" NVARCHAR2(10) DEFAULT NULL
);
--  DDL for Table period

CREATE TABLE "period" (
  "id" NVARCHAR2(50), 
  "Name" NVARCHAR2(100) DEFAULT NULL, 
  "DayCount" NUMBER(10, 0) DEFAULT NULL, 
  "Order" NUMBER(10, 0) DEFAULT NULL, 
  "Code" NVARCHAR2(50) DEFAULT NULL, 
  "isEditable" NUMBER(1, 0) DEFAULT (0), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6), 
  "lastmodifiedts" TIMESTAMP (6), 
  "synctimestamp" TIMESTAMP (6), 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table permission

CREATE TABLE "permission" (
  "id" NVARCHAR2(50), 
  "Type_id" NVARCHAR2(50), 
  "Status_id" NVARCHAR2(50), 
  "DataType_id" NVARCHAR2(50) DEFAULT NULL, 
  "Name" NVARCHAR2(50), 
  "Description" NVARCHAR2(300) DEFAULT NULL, 
  "isComposite" NUMBER(5, 0) DEFAULT (0), 
  "PermissionValue" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(5, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table pfmbargraph

CREATE TABLE "pfmbargraph" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "totalCashFlow" NUMBER(10, 2) DEFAULT NULL, 
  "monthId" NUMBER(10, 0) DEFAULT NULL, 
  "userId" NVARCHAR2(50) DEFAULT NULL, 
  "year" NUMBER(10, 0) DEFAULT NULL
);
--  DDL for Table pfmbudgetsnapshot

CREATE TABLE "pfmbudgetsnapshot" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "Category_Id" NUMBER(10, 0) DEFAULT NULL, 
  "allocatedAmount" NUMBER(10, 0) DEFAULT (0), 
  "amountSpent" NUMBER(10, 0) DEFAULT (0)
);
--  DDL for Table pfmcategory

CREATE TABLE "pfmcategory" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "categoryName" NVARCHAR2(50) DEFAULT NULL
);
--  DDL for Table pfmmonth

CREATE TABLE "pfmmonth" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "monthName" NVARCHAR2(20) DEFAULT NULL
);
--  DDL for Table pfmpiechart

CREATE TABLE "pfmpiechart" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "cashSpent" NUMBER(10, 2) DEFAULT NULL, 
  "userId" NVARCHAR2(50) DEFAULT NULL, 
  "monthId" NUMBER(10, 0) DEFAULT NULL, 
  "categoryId" NUMBER(10, 0) DEFAULT NULL, 
  "year" NUMBER(10, 0) DEFAULT NULL
);
--  DDL for Table pfmtransactions

CREATE TABLE "pfmtransactions" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "userId" NVARCHAR2(50) DEFAULT NULL, 
  "monthId" NUMBER(10, 0) DEFAULT NULL, 
  "categoryId" NUMBER(10, 0) DEFAULT NULL, 
  "transactionDate" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "fromAccountNumber" NUMBER(19, 0) DEFAULT NULL, 
  "amount" NUMBER(10, 2) DEFAULT (0.00), 
  "notes" NVARCHAR2(100) DEFAULT NULL, 
  "description" NVARCHAR2(100) DEFAULT NULL, 
  "fromAccountName" NVARCHAR2(100) DEFAULT NULL, 
  "isMappedToMerchant" NUMBER(1, 0) DEFAULT (0), 
  "isAnalyzed" NUMBER(1, 0) DEFAULT (0), 
  "toAccountNumber" NUMBER(19, 0) DEFAULT NULL, 
  "toAccountName" NVARCHAR2(100) DEFAULT NULL, 
  "year" NUMBER(10, 0) DEFAULT NULL
);
--  DDL for Table phone

CREATE TABLE "phone" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "type" NVARCHAR2(50) DEFAULT NULL, 
  "countryType" NVARCHAR2(50) DEFAULT NULL, 
  "extension" NVARCHAR2(50) DEFAULT NULL, 
  "phoneNumber" NVARCHAR2(50) DEFAULT NULL, 
  "isPrimary" NVARCHAR2(50) DEFAULT NULL, 
  "receivePromotions" NVARCHAR2(50) DEFAULT NULL, 
  "user_id" NVARCHAR2(50), 
  "account_id" NUMBER(19, 0) DEFAULT NULL, 
  "phoneCountryCode" NVARCHAR2(10) DEFAULT NULL
);
--  DDL for Table popularcurrencies

CREATE TABLE "popularcurrencies" (
  "id" NUMBER(10, 0), 
  "baseCurrencyCode" NVARCHAR2(10), 
  "quoteCurrencyCode" NVARCHAR2(10), 
  "createdby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "modifiedby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(5, 0) DEFAULT '0'
);
--  DDL for Table preferredaccount

CREATE TABLE "preferredaccount" (
  "Type_id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "Account_id" NUMBER(19, 0) DEFAULT NULL, 
  "description" NVARCHAR2(50) DEFAULT NULL
);
--  DDL for Table product

CREATE TABLE "product" (
  "id" NVARCHAR2(50), 
  "Type_id" NVARCHAR2(50) DEFAULT NULL, 
  "OtherProductType_id" NVARCHAR2(50) DEFAULT NULL, 
  "ProductCode" NVARCHAR2(50), 
  "Name" NVARCHAR2(200), 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "isLeadSupported" NUMBER(1, 0) DEFAULT (0), 
  "MarketingStateId" NVARCHAR2(50) DEFAULT NULL, 
  "SecondaryProduct_id" NVARCHAR2(50) DEFAULT NULL, 
  "ProductFeatures" NCLOB, 
  "ProductCharges" NCLOB, 
  "productDescription" NCLOB, 
  "AdditionalInformation" NCLOB, 
  "termsAndConditions" NCLOB, 
  "accountType" NUMBER(10, 0) DEFAULT NULL, 
  "stateId" NUMBER(10, 0) DEFAULT NULL, 
  "rates" NVARCHAR2(2000) DEFAULT NULL, 
  "productImageURL" NVARCHAR2(100) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table productdetail

CREATE TABLE "productdetail" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "Product_id" NUMBER(10, 0) DEFAULT NULL, 
  "Type_id" NVARCHAR2(50) DEFAULT NULL, 
  "header" NVARCHAR2(100) DEFAULT NULL, 
  "description" NCLOB
);
--  DDL for Table producttype

CREATE TABLE "producttype" (
  "id" NVARCHAR2(50), 
  "Name" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table querycoborrower

CREATE TABLE "querycoborrower" (
  "id" NVARCHAR2(50), 
  "QueryResponse_id" NVARCHAR2(50) DEFAULT NULL, 
  "CoBorrower_Type" NVARCHAR2(50) DEFAULT NULL, 
  "FirstName" NVARCHAR2(50) DEFAULT NULL, 
  "LastName" NVARCHAR2(50) DEFAULT NULL, 
  "PhoneNumber" NVARCHAR2(20) DEFAULT NULL, 
  "Email" NVARCHAR2(50) DEFAULT NULL, 
  "OTP" NVARCHAR2(50) DEFAULT NULL, 
  "OTPValidity" TIMESTAMP (6), 
  "Is_CoBorrowerActive" NUMBER(1, 0) DEFAULT NULL, 
  "Is_Verified" NUMBER(1, 0) DEFAULT NULL, 
  "InvitationLink" NVARCHAR2(200) DEFAULT NULL, 
  "InvitationLinkValidity" TIMESTAMP (6), 
  "InvitationLinkStatus" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6), 
  "softdeleteflag" NUMBER(1, 0) DEFAULT NULL, 
  "Borrower_id" NVARCHAR2(50) DEFAULT NULL
);


--  DDL for Table queryresponseconsent

CREATE TABLE "queryresponseconsent" (
  "id" NVARCHAR2(50), 
  "QueryResponse_id" NVARCHAR2(50) DEFAULT NULL, 
  "Disclaimer_id" NVARCHAR2(50) DEFAULT NULL, 
  "Is_Accepted" NVARCHAR2(50) DEFAULT NULL, 
  "Is_Rejected" NVARCHAR2(50) DEFAULT NULL, 
  "Submitedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" NVARCHAR2(50) DEFAULT NULL, 
  "lastmodifiedts" TIMESTAMP (6), 
  "synctimestamp" TIMESTAMP (6), 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table recentcurrencies

CREATE TABLE "recentcurrencies" (
  "id" VARCHAR2(60 CHAR), 
  "customerId" NVARCHAR2(50), 
  "quoteCurrencyCode" NVARCHAR2(10), 
  "createdby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "modifiedby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(5, 0) DEFAULT '0', 
  "legalEntityId" VARCHAR2(20 BYTE)
);
--  DDL for Table region

CREATE TABLE "region" (
  "id" NVARCHAR2(50), 
  "Country_id" NVARCHAR2(50) DEFAULT NULL, 
  "Code" NVARCHAR2(50), 
  "Name" NVARCHAR2(128), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "LanguageCode" VARCHAR2(45 BYTE) DEFAULT 'en-US', 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table requestcategory

CREATE TABLE "requestcategory" (
  "id" NVARCHAR2(50), 
  "Name" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);

--  DDL for Table requestmessage

CREATE TABLE "requestmessage" (
  "id" NVARCHAR2(50), 
  "CustomerRequest_id" NVARCHAR2(50) DEFAULT NULL, 
  "MessageDescription" NCLOB, 
  "RepliedBy" NVARCHAR2(50) DEFAULT NULL, 
  "RepliedBy_id" NVARCHAR2(50) DEFAULT NULL, 
  "RepliedBy_Name" NVARCHAR2(100) DEFAULT NULL, 
  "ReplySequence" NUMBER(10, 0) DEFAULT NULL, 
  "IsRead" NVARCHAR2(10) DEFAULT 'false', 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "frominternaluser" NUMBER(3, 0) DEFAULT '0', 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL', 
  "isPriorityMessage" NUMBER DEFAULT (0)
);

--  DDL for Table role

CREATE TABLE "role" (
  "id" NVARCHAR2(50), 
  "Type_id" NVARCHAR2(50), 
  "Status_id" NVARCHAR2(50), 
  "Parent_id" NVARCHAR2(50) DEFAULT NULL, 
  "Name" NVARCHAR2(50), 
  "Description" NVARCHAR2(300) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table roletype

CREATE TABLE "roletype" (
  "id" NVARCHAR2(50), 
  "Name" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);

--  DDL for Table scheduledtransaction

CREATE TABLE "scheduledtransaction" (
  "Id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "Payee_id" NUMBER(10, 0) DEFAULT NULL, 
  "Bill_id" NUMBER(10, 0) DEFAULT NULL, 
  "Type_id" NUMBER(10, 0) DEFAULT NULL, 
  "fromAccountNumber" NUMBER(19, 0) DEFAULT NULL, 
  "toAccountNumber" NUMBER(19, 0) DEFAULT NULL, 
  "amount" NUMBER(20, 2) DEFAULT (0.00), 
  "statusDesc" NVARCHAR2(50) DEFAULT NULL, 
  "notes" NVARCHAR2(100) DEFAULT (' '), 
  "description" NVARCHAR2(100) DEFAULT ' ', 
  "scheduledDate" DATE DEFAULT NULL, 
  "transactionDate" TIMESTAMP (6), 
  "createdDate" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "toExternalAccountNumber" NUMBER(19, 0) DEFAULT NULL, 
  "Person_Id" NUMBER(10, 0) DEFAULT NULL, 
  "frequencyType" NVARCHAR2(8) DEFAULT 'Once', 
  "numberOfRecurrences" NUMBER(10, 0) DEFAULT NULL, 
  "frequencyStartDate" DATE DEFAULT NULL, 
  "frequencyEndDate" DATE DEFAULT NULL, 
  "category" NVARCHAR2(17) DEFAULT 'Uncategorised', 
  "recurrenceDesc" NVARCHAR2(50) DEFAULT NULL, 
  "p2pContact" NVARCHAR2(50) DEFAULT NULL, 
  "routingNumber" NVARCHAR2(45) DEFAULT NULL, 
  "user_id" NUMBER(10, 0), 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table securityquestion

CREATE TABLE "securityquestion" (
  "id" NVARCHAR2(50), 
  "Status_id" NVARCHAR2(50), 
  "Question" NVARCHAR2(255), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);

--  DDL for Table securityimage

CREATE TABLE "securityimage" (
  "id" NVARCHAR2(50), 
  "Status_id" NVARCHAR2(50), 
  "Image" NCLOB, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);

--  DDL for Table service

CREATE TABLE "service" (
  "id" NVARCHAR2(50), 
  "Type_id" NVARCHAR2(50) DEFAULT NULL, 
  "Feature_id" NVARCHAR2(50) DEFAULT NULL, 
  "Name" NVARCHAR2(100) DEFAULT NULL, 
  "Description" NVARCHAR2(300) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "Disclaimer" NCLOB, 
  "Notes" NVARCHAR2(2000) DEFAULT NULL, 
  "MaxTransferLimit" NUMBER(20, 2) DEFAULT NULL, 
  "MinTransferLimit" NUMBER(20, 2) DEFAULT NULL, 
  "TransferDenominations" NVARCHAR2(100) DEFAULT NULL, 
  "IsFutureTransaction" NUMBER(1, 0) DEFAULT NULL, 
  "TransactionCharges" NVARCHAR2(100) DEFAULT NULL, 
  "IsAuthorizationRequired" NUMBER(1, 0) DEFAULT NULL, 
  "IsSMSAlertActivated" NUMBER(1, 0) DEFAULT NULL, 
  "SMSCharges" NVARCHAR2(50) DEFAULT NULL, 
  "IsBeneficiarySMSAlertActivated" NUMBER(1, 0) DEFAULT NULL, 
  "BeneficiarySMSCharge" NVARCHAR2(50) DEFAULT NULL, 
  "HasWeekendOperation" NUMBER(1, 0) DEFAULT NULL, 
  "IsOutageMessageActive" NUMBER(1, 0) DEFAULT NULL, 
  "IsAlertActive" NUMBER(1, 0) DEFAULT NULL, 
  "IsTCActive" NUMBER(1, 0) DEFAULT NULL, 
  "IsAgreementActive" NUMBER(1, 0) DEFAULT NULL, 
  "IsCampaignActive" NUMBER(1, 0) DEFAULT NULL, 
  "WorkSchedule_id" NVARCHAR2(50) DEFAULT NULL, 
  "TransactionFee_id" NVARCHAR2(50) DEFAULT NULL, 
  "TransactionLimit_id" NVARCHAR2(50) DEFAULT NULL, 
  "code" NVARCHAR2(50) DEFAULT NULL, 
  "Category_id" NVARCHAR2(50) DEFAULT NULL, 
  "DisplayName" NVARCHAR2(100) DEFAULT NULL, 
  "DisplayDescription" NVARCHAR2(300) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table servicechannel

CREATE TABLE "servicechannel" (
  "id" NVARCHAR2(50), 
  "Description" NVARCHAR2(100) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);

--  DDL for Table service_permission_mapper

CREATE TABLE "service_permission_mapper" (
  "id" NVARCHAR2(50), 
  "service_name" NVARCHAR2(50), 
  "object_name" NVARCHAR2(50) DEFAULT NULL, 
  "operation" NVARCHAR2(50), 
  "permissions" NCLOB
);
--  DDL for Table servicetype

CREATE TABLE "servicetype" (
  "id" NVARCHAR2(50), 
  "Description" NVARCHAR2(100) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);

--  DDL for Table state

CREATE TABLE "state" (
  "id" NUMBER(10, 0), 
  "state" NVARCHAR2(45) DEFAULT NULL, 
  "country_id" NUMBER(10, 0) DEFAULT NULL
);
--  DDL for Table status

CREATE TABLE "status" (
  "id" NVARCHAR2(50), 
  "Type_id" NVARCHAR2(50), 
  "Code" NVARCHAR2(50) DEFAULT NULL, 
  "Description" NVARCHAR2(100) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);


--  DDL for Table statuschange


CREATE TABLE "statuschange" ("id" NVARCHAR2(50), "PreviousStatus_id" NVARCHAR2(50), "NextStatus_id" NVARCHAR2(50), "createdby" NVARCHAR2(50) DEFAULT NULL, "modifiedby" NVARCHAR2(50) DEFAULT NULL, "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, "softdeleteflag" NUMBER(1,0) DEFAULT (0));

--  DDL for Table statustype

CREATE TABLE "statustype" (
  "id" NVARCHAR2(50), 
  "Description" NVARCHAR2(100) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table suspendedcustomers

CREATE TABLE "suspendedcustomers" (
  "id" NVARCHAR2(50), 
  "contractId" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "customerId" NVARCHAR2(50) DEFAULT NULL, 
  "coreCustomerId" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "createdby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "modifiedby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT NULL, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT NULL, 
  "synctimestamp" TIMESTAMP (6) DEFAULT NULL, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);
--  DDL for Table swiftcode

CREATE TABLE "swiftcode" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "bankName" NVARCHAR2(100) DEFAULT NULL, 
  "city" NVARCHAR2(50) DEFAULT NULL, 
  "country" NVARCHAR2(50) DEFAULT NULL, 
  "bic" NVARCHAR2(50) DEFAULT NULL, 
  "countryCode" NVARCHAR2(2) DEFAULT NULL, 
  "countryRegion" NVARCHAR2(13) DEFAULT 'INTERNATIONAL', 
  "bankAddress" NVARCHAR2(500), 
  "branchName" NVARCHAR2(100), 
  "zipcode" NVARCHAR2(50)
);
--  DDL for Table systemconfiguration

CREATE TABLE "systemconfiguration" (
  "id" NUMBER(10, 0) DEFAULT NULL, 
  "PropertyName" NVARCHAR2(45), 
  "PropertyValue" NVARCHAR2(100), 
  "createdby" NVARCHAR2(45) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(45) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT NULL
);
--  DDL for Table tbladdetails

CREATE TABLE "tbladdetails" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "addetails" NVARCHAR2(2000) DEFAULT NULL, 
  "actiondetails" NVARCHAR2(2000) DEFAULT NULL, 
  "user_id" NUMBER(10, 0) DEFAULT NULL
);

--  DDL for Table termsandconditions

CREATE TABLE "termsandconditions" (
  "id" NVARCHAR2(50), 
  "Service_id" NVARCHAR2(50) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "Description" NCLOB, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NVARCHAR2(50) DEFAULT '0'
);
--  DDL for Table timeperiod

CREATE TABLE "timeperiod" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "description" NVARCHAR2(100) DEFAULT NULL
);
--  DDL for Table transaction

CREATE TABLE "transaction" (
  "Id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "isScheduled" NUMBER(1, 0) DEFAULT '0', 
  "Customer_id" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "ExpenseCategory_id" NUMBER(10, 0) DEFAULT NULL, 
  "Payee_id" NUMBER(10, 0) DEFAULT NULL, 
  "Bill_id" NUMBER(10, 0) DEFAULT NULL, 
  "Type_id" NUMBER(10, 0) DEFAULT NULL, 
  "Reference_id" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "isTypeBusiness" VARCHAR2(50 CHAR), 
  "fromAccountNumber" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "fromAccountBalance" NUMBER(10, 2) DEFAULT '0.00', 
  "toAccountNumber" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "toAccountBalance" NUMBER(10, 2) DEFAULT '0.00', 
  "amount" NUMBER(20, 2) DEFAULT '0.00', 
  "convertedAmount" VARCHAR2(45 CHAR), 
  "transactionCurrency" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "baseCurrency" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "Status_id" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "statusDesc" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "notes" VARCHAR2(150 CHAR) DEFAULT (' '), 
  "checkNumber" NUMBER(10, 0) DEFAULT '0', 
  "imageURL1" CLOB, 
  "imageURL2" CLOB, 
  "hasDepositImage" NUMBER(3, 0) DEFAULT '0', 
  "description" VARCHAR2(100 CHAR) DEFAULT ' ', 
  "scheduledDate" DATE DEFAULT NULL, 
  "transactionDate" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "postedDate" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "createdDate" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "transactionComments" VARCHAR2(100 CHAR) DEFAULT NULL, 
  "toExternalAccountNumber" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "Person_Id" NUMBER(10, 0) DEFAULT NULL, 
  "frequencyType" VARCHAR2(20 CHAR) DEFAULT 'Once', 
  "numberOfRecurrences" NUMBER(10, 0) DEFAULT '0', 
  "frequencyStartDate" DATE DEFAULT NULL, 
  "frequencyEndDate" DATE DEFAULT NULL, 
  "checkImage" NCLOB, 
  "checkImageBack" NCLOB, 
  "cashlessOTPValidDate" TIMESTAMP (6), 
  "cashlessOTP" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "cashlessPhone" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "cashlessEmail" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "cashlessPersonName" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "cashlessMode" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "cashlessSecurityCode" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "cashWithdrawalTransactionStatus" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "cashlessPin" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "category" VARCHAR2(30 CHAR) DEFAULT NULL, 
  "billCategory" VARCHAR2(20 CHAR), 
  "recurrenceDesc" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "deliverBy" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "p2pContact" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "p2pRequiredDate" DATE DEFAULT NULL, 
  "requestCreatedDate" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "penaltyFlag" NUMBER(1, 0) DEFAULT '0', 
  "payoffFlag" NUMBER(1, 0) DEFAULT '0', 
  "viewReportLink" VARCHAR2(150 CHAR) DEFAULT 'http://pmqa.konylabs.net/KonyWebBanking/view_report.png', 
  "isPaypersonDeleted" NUMBER(1, 0) DEFAULT '0', 
  "fee" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "feeCurrency" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "feePaidByReceipent" NUMBER(1, 0) DEFAULT NULL, 
  "frontImage1" VARCHAR2(100 CHAR) DEFAULT NULL, 
  "frontImage2" VARCHAR2(100 CHAR) DEFAULT NULL, 
  "backImage1" VARCHAR2(100 CHAR) DEFAULT NULL, 
  "backImage2" VARCHAR2(100 CHAR) DEFAULT NULL, 
  "checkDesc" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "checkNumber1" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "checkNumber2" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "bankName1" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "bankName2" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "withdrawlAmount1" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "withdrawlAmount2" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "cashAmount" VARCHAR2(50 CHAR) DEFAULT '0.00', 
  "payeeCurrency" VARCHAR2(50 CHAR) DEFAULT 'INR', 
  "billid" NUMBER(19, 0) DEFAULT NULL, 
  "isDisputed" NUMBER(1, 0) DEFAULT '0', 
  "disputeDescription" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "disputeReason" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "disputeStatus" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "disputeDate" TIMESTAMP (6), 
  "payeeName" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "checkDateOfIssue" TIMESTAMP (6), 
  "checkReason" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "isPayeeDeleted" NUMBER(1, 0) DEFAULT '0', 
  "amountRecieved" VARCHAR2(50 CHAR) DEFAULT '0', 
  "requestValidity" TIMESTAMP (6), 
  "statementReference" VARCHAR2(35 CHAR) DEFAULT NULL, 
  "transCreditDebitIndicator" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "bookingDateTime" TIMESTAMP (6), 
  "valueDateTime" TIMESTAMP (6), 
  "transactionInformation" VARCHAR2(500 CHAR) DEFAULT NULL, 
  "addressLine" VARCHAR2(70 CHAR) DEFAULT NULL, 
  "transactionAmount" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "chargeAmount" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "chargeCurrency" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "sourceCurrency" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "targetCurrency" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "unitCurrency" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "exchangeRate" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "contractIdentification" VARCHAR2(35 CHAR) DEFAULT NULL, 
  "quotationDate" TIMESTAMP (6), 
  "instructedAmount" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "instructedCurrency" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "transactionCode" VARCHAR2(35 CHAR) DEFAULT NULL, 
  "transactionSubCode" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "proprietaryTransactionCode" VARCHAR2(35 CHAR) DEFAULT NULL, 
  "proprietaryTransactionIssuer" VARCHAR2(35 CHAR) DEFAULT NULL, 
  "balanceCreditDebitIndicator" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "balanceType" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "balanceAmount" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "balanceCurrency" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "merchantName" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "merchantCategoryCode" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "creditorAgentSchemeName" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "creditorAgentIdentification" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "creditorAgentName" VARCHAR2(140 CHAR) DEFAULT NULL, 
  "creditorAgentaddressType" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "creditorAgentDepartment" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "creditorAgentSubDepartment" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "creditorAgentStreetName" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "creditorAgentBuildingNumber" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "creditorAgentPostCode" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "creditorAgentTownName" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "creditorAgentCountrySubDivision" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "creditorAgentCountry" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "creditorAgentAddressLine" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "creditorAccountSchemeName" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "creditorAccountIdentification" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "creditorAccountName" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "creditorAccountSeconIdentification" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "debtorAgentSchemeName" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "debtorAgentIdentification" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "debtorAgentName" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "debtorAgentAddressType" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "debtorAgentDepartment" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "debtorAgentSubDepartment" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "debtorAgentStreetName" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "debtorAgentBuildingNumber" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "dedtorAgentPostCode" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "debtorAgentTownName" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "debtorAgentCountrySubDivision" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "debtorAgentCountry" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "debtorAgentAddressLine" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "debtorAccountSchemeName" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "debtorAccountIdentification" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "debtorAccountName" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "debtorAccountSeconIdentification" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "cardInstrumentSchemeName" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "cardInstrumentAuthorisationType" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "cardInstrumentName" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "cardInstrumentIdentification" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "IBAN" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "sortCode" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "FirstPaymentDateTime" TIMESTAMP (6), 
  "NextPaymentDateTime" TIMESTAMP (6), 
  "FinalPaymentDateTime" TIMESTAMP (6), 
  "StandingOrderStatusCode" VARCHAR2(6 CHAR) DEFAULT NULL, 
  "FP_Amount" NUMBER(12, 2) DEFAULT NULL, 
  "FP_Currency" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "NP_Amount" NUMBER(12, 2) DEFAULT NULL, 
  "NP_Currency" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "FPA_Amount" NUMBER(12, 2) DEFAULT NULL, 
  "FPA_Currency" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "ConsentId" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "Initiation_InstructionIdentification" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "Initiation_EndToEndIdentification" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "RI_Reference" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "RI_Unstructured" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "RiskPaymentContextCode" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "MerchantCustomerIdentification" VARCHAR2(200 CHAR) DEFAULT NULL, 
  "beneficiaryName" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "bankName" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "swiftCode" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "DomesticPaymentId" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "linkSelf" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "StatusUpdateDateTime" TIMESTAMP (6), 
  "dataStatus" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "serviceName" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "payPersonName" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "payPersonNickName" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "p2pAlternateContact" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "billerId" VARCHAR2(45 CHAR) DEFAULT NULL, 
  "bicCode" VARCHAR2(50 CHAR), 
  "paidBy" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "paymentType" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "feeAmount" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "beneficiaryAddressNickName" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "beneficiaryAddressLine1" VARCHAR2(200 CHAR) DEFAULT NULL, 
  "beneficiaryCity" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "beneficiaryZipcode" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "beneficiarycountry" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "bankId" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "serviceCharge" VARCHAR2(45 CHAR) DEFAULT NULL,
  "legalEntityId" VARCHAR2(50 CHAR) DEFAULT 'ALL'
);

--  DDL for Table transactionfee

CREATE TABLE "transactionfee" (
  "id" NVARCHAR2(50), 
  "Name" NVARCHAR2(50) DEFAULT NULL, 
  "Description" NVARCHAR2(100) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT SYSDATE, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT SYSDATE, 
  "synctimestamp" TIMESTAMP (6) DEFAULT SYSDATE, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);

--  DDL for Table transactionlimit

CREATE TABLE "transactionlimit" (
  "id" NVARCHAR2(50), 
  "Description" NVARCHAR2(150) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);

--  DDL for Table transactiontype

CREATE TABLE "transactiontype" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "description" NVARCHAR2(100) DEFAULT NULL
);
--  DDL for Table transactiontypemapping

CREATE TABLE "transactiontypemapping" (
  "backendTransactionTypeId" VARCHAR2(10 CHAR), 
  "backendTransactionType" VARCHAR2(45 CHAR), 
  "dbxTransactionType" VARCHAR2(45 CHAR)
);
--  DDL for Table transferseries

CREATE TABLE "transferseries" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "Type_id" NVARCHAR2(50) DEFAULT NULL, 
  "accountFrom" NUMBER(19, 0) DEFAULT NULL, 
  "accountTo" NUMBER(19, 0) DEFAULT NULL, 
  "amount" NUMBER(10, 2) DEFAULT NULL, 
  "notes" NVARCHAR2(50) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "createdDate" DATE DEFAULT NULL, 
  "recurrenceType" NVARCHAR2(50) DEFAULT NULL, 
  "frequency" NUMBER(10, 0) DEFAULT NULL, 
  "occurrences" NUMBER(10, 0) DEFAULT NULL, 
  "startDate" DATE DEFAULT NULL, 
  "endDate" DATE DEFAULT NULL
);
--  DDL for Table travelnotification

CREATE TABLE "travelnotification" (
  "id" NVARCHAR2(50), 
  "Date" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "Channel_id" NVARCHAR2(50) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "PlannedDepartureDate" DATE DEFAULT NULL, 
  "PlannedReturnDate" DATE DEFAULT NULL, 
  "Destinations" NVARCHAR2(200) DEFAULT NULL, 
  "AdditionalNotes" NVARCHAR2(200) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0), 
  "phonenumber" NVARCHAR2(15) DEFAULT NULL
);
--  DDL for Table user

CREATE TABLE "user" (
  "Id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "Application_id" NVARCHAR2(50) DEFAULT NULL, 
  "Session_id" NVARCHAR2(50) DEFAULT NULL, 
  "Device_id" NVARCHAR2(50) DEFAULT NULL, 
  "userImage" NCLOB, 
  "ssn" NVARCHAR2(25) DEFAULT NULL, 
  "userName" NVARCHAR2(50) DEFAULT NULL, 
  "passWord" NVARCHAR2(50) DEFAULT NULL, 
  "userFirstName" NVARCHAR2(50) DEFAULT NULL, 
  "userLastName" NVARCHAR2(50) DEFAULT NULL, 
  "phone" NVARCHAR2(20) DEFAULT NULL, 
  "countryCode" NVARCHAR2(45) DEFAULT NULL, 
  "email" NVARCHAR2(80) DEFAULT NULL, 
  "default_account_transfers" NVARCHAR2(50) DEFAULT NULL, 
  "dateOfBirth" DATE DEFAULT NULL, 
  "default_account_deposit" NVARCHAR2(50) DEFAULT NULL, 
  "defaultModule_id" NUMBER(10, 0) DEFAULT NULL, 
  "default_account_payments" NVARCHAR2(50) DEFAULT NULL, 
  "secondaryphone" NVARCHAR2(20) DEFAULT NULL, 
  "secondaryemail" NVARCHAR2(30) DEFAULT NULL, 
  "lastlogintime" TIMESTAMP (6), 
  "areUserAlertsTurnedOn" NUMBER(1, 0) DEFAULT (0), 
  "areDepositTermsAccepted" NUMBER(1, 0) DEFAULT (0), 
  "areAccountStatementTermsAccepted" NUMBER(1, 0) DEFAULT (0), 
  "unsuccessfulLoginAttempts" NUMBER(10, 0) DEFAULT (0), 
  "isUserAccountLocked" NUMBER(1, 0) DEFAULT (0), 
  "userImageURL" NVARCHAR2(500) DEFAULT NULL, 
  "addressLine1" NVARCHAR2(50) DEFAULT NULL, 
  "addressLine2" NVARCHAR2(45) DEFAULT NULL, 
  "city" NVARCHAR2(45) DEFAULT NULL, 
  "state" NVARCHAR2(45) DEFAULT NULL, 
  "country" NVARCHAR2(45) DEFAULT NULL, 
  "zipcode" NVARCHAR2(45) DEFAULT NULL, 
  "isSuperAdmin" NUMBER(1, 0) DEFAULT (0), 
  "userCompany" NVARCHAR2(45) DEFAULT NULL, 
  "validDate" TIMESTAMP (6), 
  "pin" NVARCHAR2(6) DEFAULT NULL, 
  "isPinSet" NUMBER(1, 0) DEFAULT NULL, 
  "role" NVARCHAR2(7) DEFAULT 'BASIC', 
  "cvv" NVARCHAR2(45) DEFAULT NULL, 
  "otp" NVARCHAR2(45) DEFAULT NULL, 
  "lockCount" NUMBER(10, 0) DEFAULT (0), 
  "isEnrolled" NUMBER(1, 0) DEFAULT (1), 
  "Bank_id" NVARCHAR2(50) DEFAULT NULL, 
  "default_account_cardless" NVARCHAR2(50) DEFAULT NULL, 
  "isBillPaySupported" NUMBER(1, 0) DEFAULT (0), 
  "isP2PSupported" NUMBER(1, 0) DEFAULT (0), 
  "isBillPayActivated" NUMBER(1, 0) DEFAULT (0), 
  "isP2PActivated" NUMBER(1, 0) DEFAULT (0), 
  "default_account_billPay" NVARCHAR2(50) DEFAULT NULL, 
  "default_to_account_p2p" NVARCHAR2(50) DEFAULT NULL, 
  "default_from_account_p2p" NVARCHAR2(50) DEFAULT NULL, 
  "isPhoneEnabled" NUMBER(1, 0) DEFAULT (0), 
  "isEmailEnabled" NUMBER(1, 0) DEFAULT (0), 
  "secondaryemail2" NVARCHAR2(80) DEFAULT NULL, 
  "secondaryphone2" NVARCHAR2(20) DEFAULT NULL, 
  "token" NVARCHAR2(200) DEFAULT NULL, 
  "isWireTransferActivated" NUMBER(1, 0) DEFAULT (0), 
  "default_account_wire" NUMBER(19, 0) DEFAULT NULL, 
  "isWireTransferEligible" NUMBER(1, 0) DEFAULT (0), 
  "currentLoginTime" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "maritalstatus" NVARCHAR2(8) DEFAULT NULL, 
  "spousefirstname" NVARCHAR2(50) DEFAULT NULL, 
  "spouselastname" NVARCHAR2(50) DEFAULT NULL, 
  "noofdependents" NVARCHAR2(10) DEFAULT NULL, 
  "gender" NVARCHAR2(6) DEFAULT NULL, 
  "showBillPayFromAccPopup" NUMBER(1, 0) DEFAULT 1, 
  "drivingLicenseNumber" NVARCHAR2(50) DEFAULT NULL, 
  "phoneExtension" NVARCHAR2(10) DEFAULT NULL, 
  "phoneCountryCode" NVARCHAR2(10) DEFAULT NULL
);
--  DDL for Table useraccountalerts

CREATE TABLE "useraccountalerts" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "User_id" NUMBER(10, 0) DEFAULT NULL, 
  "AccountNumber" NUMBER(19, 0) DEFAULT NULL, 
  "minimumBalance" NUMBER(20, 2) DEFAULT NULL, 
  "debitLimit" NUMBER(20, 2) DEFAULT NULL, 
  "creditLimit" NUMBER(20, 2) DEFAULT NULL, 
  "balanceUpdate_PeriodId" NUMBER(10, 0) DEFAULT NULL, 
  "PayementDueReminder_PeriodId" NUMBER(10, 0) DEFAULT NULL, 
  "depositMaturityReminder_PeriodId" NUMBER(10, 0) DEFAULT NULL, 
  "isEnabled" NUMBER(1, 0) DEFAULT (0), 
  "successfulTransfer" NUMBER(1, 0) DEFAULT (0), 
  "checkClearance" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table useraccounts

CREATE TABLE "useraccounts" (
  "id" NVARCHAR2(50), 
  "Customer_id" NVARCHAR2(50) DEFAULT NULL, 
  "MemberId" NVARCHAR2(50) DEFAULT NULL, 
  "Account_id" NVARCHAR2(50) DEFAULT NULL, 
  "isTypeBusiness" NVARCHAR2(45) DEFAULT NULL, 
  "AccountName" NVARCHAR2(50) DEFAULT NULL, 
  "IsViewAllowed" NUMBER(1, 0) DEFAULT (0), 
  "IsDepositAllowed" NUMBER(1, 0) DEFAULT (0), 
  "IsWithdrawAllowed" NUMBER(1, 0) DEFAULT (0), 
  "IsOrganizationAccount" NUMBER(1, 0) DEFAULT (0), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6), 
  "lastmodifiedts" TIMESTAMP (6)
);
--  DDL for Table useralerts

CREATE TABLE "useralerts" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "User_id" NVARCHAR2(50) DEFAULT NULL, 
  "bankingIDChange" NUMBER(1, 0) DEFAULT (0), 
  "passwordChange" NUMBER(1, 0) DEFAULT (0), 
  "passwordExpired" NUMBER(1, 0) DEFAULT (0), 
  "communicationChange" NUMBER(1, 0) DEFAULT (0), 
  "newPayeeAdded" NUMBER(1, 0) DEFAULT (0), 
  "payeeDetailsUpdated" NUMBER(1, 0) DEFAULT (0), 
  "newDealsAvailable" NUMBER(1, 0) DEFAULT (0), 
  "dealsExpiring" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table usercashflow

CREATE TABLE "usercashflow" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "monthCash" NUMBER(10, 0) DEFAULT (0), 
  "monthCredit" NUMBER(10, 0) DEFAULT (0), 
  "totalCash" NUMBER(10, 0) DEFAULT (0), 
  "totalCreditDebit" NUMBER(10, 0) DEFAULT (0)
);
--  DDL for Table usercommunication

CREATE TABLE "usercommunication" (
  "Type_id" NVARCHAR2(50) DEFAULT NULL, 
  "User_id" NUMBER(10, 0) DEFAULT NULL, 
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "sequence" NUMBER(10, 0) DEFAULT NULL, 
  "value" NVARCHAR2(100) DEFAULT NULL, 
  "extension" NVARCHAR2(50) DEFAULT NULL, 
  "description" NVARCHAR2(50) DEFAULT NULL
);
--  DDL for Table usercreditcheck

CREATE TABLE "usercreditcheck" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "User_id" NUMBER(10, 0) DEFAULT NULL, 
  "isCreditCheck" NUMBER(1, 0) DEFAULT (0), 
  "isSingatureUpload" NUMBER(1, 0) DEFAULT (0), 
  "ssn" NVARCHAR2(20) DEFAULT NULL, 
  "singatureImage" NCLOB, 
  "newuser_id" NUMBER(10, 0) DEFAULT NULL
);
--  DDL for Table usernotification

CREATE TABLE "usernotification" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "notification_id" NUMBER(10, 0) DEFAULT NULL, 
  "user_id" NVARCHAR2(50) DEFAULT NULL, 
  "isRead" NVARCHAR2(11) DEFAULT NULL, 
  "receivedDate" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "companyLegalUnit" VARCHAR2(20 CHAR)
);
--  DDL for Table userpersonalinfo

CREATE TABLE "userpersonalinfo" (
  "User_id" NUMBER(10, 0) DEFAULT NULL, 
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "dateOfBirth" DATE DEFAULT NULL, 
  "gender" NVARCHAR2(6) DEFAULT NULL, 
  "userfirstname" NVARCHAR2(50) DEFAULT NULL, 
  "userlastname" NVARCHAR2(50) DEFAULT NULL, 
  "maritalstatus" NVARCHAR2(8) DEFAULT NULL, 
  "spouseFirstName" NVARCHAR2(50) DEFAULT NULL, 
  "spouseLastName" NVARCHAR2(50) DEFAULT NULL, 
  "noOfDependents" NVARCHAR2(10) DEFAULT NULL, 
  "addressLine1" NVARCHAR2(50) DEFAULT NULL, 
  "addressLine2" NVARCHAR2(50) DEFAULT NULL, 
  "city" NVARCHAR2(20) DEFAULT NULL, 
  "state" NVARCHAR2(20) DEFAULT NULL, 
  "country" NVARCHAR2(20) DEFAULT NULL, 
  "zipcode" NVARCHAR2(10) DEFAULT NULL, 
  "employmentInfo" NVARCHAR2(50) DEFAULT NULL, 
  "company" NVARCHAR2(50) DEFAULT NULL, 
  "jobProfile" NVARCHAR2(50) DEFAULT NULL, 
  "experience" NVARCHAR2(50) DEFAULT NULL, 
  "annualIncome" NVARCHAR2(50) DEFAULT NULL, 
  "assets" NVARCHAR2(50) DEFAULT NULL, 
  "montlyExpenditure" NVARCHAR2(50) DEFAULT NULL, 
  "addressDoc" NCLOB, 
  "signatureImage" NCLOB, 
  "employementDoc" NCLOB, 
  "incomeDoc" NCLOB, 
  "ssn" NVARCHAR2(50) DEFAULT NULL, 
  "userPersonalInfo" NUMBER(1, 0) DEFAULT (0), 
  "userEmploymentInfo" NUMBER(1, 0) DEFAULT (0), 
  "userFinancialInfo" NUMBER(1, 0) DEFAULT (0), 
  "userSecurityQuestions" NUMBER(1, 0) DEFAULT (0), 
  "spousename" NVARCHAR2(50) DEFAULT NULL, 
  "newuser_id" NUMBER(10, 0) DEFAULT NULL
);
--  DDL for Table userproducts

CREATE TABLE "userproducts" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE,
  "User_id" NUMBER(10, 0) DEFAULT NULL, 
  "Product_id" NUMBER(10, 0) DEFAULT NULL, 
  "newuser_id" NUMBER(10, 0) DEFAULT NULL, 
  "product" NCLOB
);
--  DDL for Table usersecurity

CREATE TABLE "usersecurity" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "User_id" NVARCHAR2(50) DEFAULT '0', 
  "question" NUMBER(10, 0), 
  "answer" NVARCHAR2(100) DEFAULT NULL
);
--  DDL for Table userserviceprefernces

CREATE TABLE "userserviceprefernces" (
  "id" NVARCHAR2(50), 
  "Customer_id" NVARCHAR2(50) DEFAULT NULL, 
  "Service_id" NVARCHAR2(50) DEFAULT NULL, 
  "isTypeBusiness" NVARCHAR2(45) DEFAULT NULL, 
  "PerDayLimit" NVARCHAR2(50) DEFAULT NULL, 
  "PerMonthLimit" NVARCHAR2(50) DEFAULT NULL, 
  "PerAccountLimit" NVARCHAR2(50) DEFAULT NULL, 
  "HasApprove" NUMBER(1, 0) DEFAULT (0), 
  "HasDraftOnly" NUMBER(1, 0) DEFAULT (0), 
  "HasCancel" NUMBER(1, 0) DEFAULT (0), 
  "IsViewAll" NUMBER(1, 0) DEFAULT (0), 
  "IsViewNone" NUMBER(1, 0) DEFAULT (0), 
  "IsViewOwnTransfersOnly" NUMBER(1, 0) DEFAULT (0), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6), 
  "lastmodifiedts" TIMESTAMP (6)
);
--  DDL for Table usertransactionhistory

CREATE TABLE "usertransactionhistory" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "description" NVARCHAR2(45) DEFAULT NULL, 
  "transactionDate" TIMESTAMP (6), 
  "amount" NUMBER(10, 0) DEFAULT (0), 
  "depositAmount" NUMBER(10, 0) DEFAULT (0), 
  "transactionType" NVARCHAR2(45) DEFAULT NULL, 
  "referenceId" NVARCHAR2(45) DEFAULT NULL, 
  "closingBalanceAmount" NUMBER(10, 0) DEFAULT (0)
);
--  DDL for Table vihicleinfo

CREATE TABLE "vihicleinfo" (
  "id" NVARCHAR2(50), 
  "VehicleType" NVARCHAR2(50) DEFAULT NULL, 
  "Year" NUMBER(1, 0) DEFAULT NULL, 
  "Make" NVARCHAR2(50) DEFAULT NULL, 
  "Model" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "lastmodifiedts" TIMESTAMP (6), 
  "synctimestamp" TIMESTAMP (6), 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table wallet

CREATE TABLE "wallet" (
  "id" NUMBER(10, 0) GENERATED BY DEFAULT ON NULL AS IDENTITY START WITH 1 INCREMENT BY 1 MINVALUE 1 NOMAXVALUE, 
  "user_id" NUMBER(10, 0)
);
--  DDL for Table wealthuserpreferences

CREATE TABLE "wealthuserpreferences" (
  "id" NVARCHAR2(50), 
  "userId" NVARCHAR2(50) DEFAULT NULL, 
  "portfolioId" NVARCHAR2(50) DEFAULT NULL, 
  "fieldOrder" NVARCHAR2(150) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);
--  DDL for Table weekday

CREATE TABLE "weekday" (
  "id" NUMBER(10, 0), 
  "Name" VARCHAR2(20 CHAR)
);
--  DDL for Table weekdayvalue

CREATE TABLE "weekdayvalue" (
  "weekdayId" NUMBER(10, 0), 
  "languageCode" NVARCHAR2(10), 
  "displayName" VARCHAR2(255 CHAR), 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP
);
--  DDL for Table workschedule

CREATE TABLE "workschedule" (
  "id" NVARCHAR2(50), 
  "Description" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1, 0) DEFAULT (0)
);


--  DDL for Table additionalfield


CREATE TABLE "additionalfield" 
   (  "id" NVARCHAR2(50), 
  "Name" NVARCHAR2(100), 
  "ObjectType" NVARCHAR2(50), 
  "DataType" NVARCHAR2(50) DEFAULT NULL, 
  "Length" NVARCHAR2(50) DEFAULT NULL, 
  "FieldLabel" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0)
   );

--  DDL for Table archivedcustomerrequest


CREATE TABLE "archivedcustomerrequest" 
   (  "id" NVARCHAR2(50), 
  "RequestCategory_id" NVARCHAR2(50), 
  "Customer_id" NVARCHAR2(50) DEFAULT NULL, 
  "Priority" NVARCHAR2(50) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "RequestSubject" NVARCHAR2(50) DEFAULT NULL, 
  "AssignedTo" NVARCHAR2(50) DEFAULT NULL, 
  "Accountid" NVARCHAR2(50) DEFAULT NULL, 
  "lastupdatedbycustomer" NUMBER(1,0) DEFAULT (0), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0), 
  "isTypeBusiness" NCLOB DEFAULT TO_NCLOB(NULL)
   );

--  DDL for Table archivedmedia


CREATE TABLE "archivedmedia" 
   (  "id" NVARCHAR2(50), 
  "Name" NVARCHAR2(100), 
  "Type" NVARCHAR2(300), 
  "Description" NVARCHAR2(100), 
  "Url" NVARCHAR2(200) DEFAULT NULL, 
  "Content" NCLOB, 
  "Size" NVARCHAR2(50) DEFAULT '0', 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0)
   );

--  DDL for Table archivedmessageattachment


CREATE TABLE "archivedmessageattachment" 
   (  "id" NVARCHAR2(50), 
  "RequestMessage_id" NVARCHAR2(50) DEFAULT NULL, 
  "AttachmentType_id" NVARCHAR2(50) DEFAULT NULL, 
  "Media_id" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0)
   );

--  DDL for Table archivedrequestmessage


CREATE TABLE "archivedrequestmessage" 
   (  "id" NVARCHAR2(50), 
  "CustomerRequest_id" NVARCHAR2(50) DEFAULT NULL, 
  "MessageDescription" NCLOB DEFAULT TO_NCLOB(NULL), 
  "RepliedBy" NVARCHAR2(50) DEFAULT NULL, 
  "RepliedBy_id" NVARCHAR2(50) DEFAULT NULL, 
  "RepliedBy_Name" NVARCHAR2(100) DEFAULT NULL, 
  "ReplySequence" NUMBER(10,0) DEFAULT NULL, 
  "IsRead" NVARCHAR2(10) DEFAULT 'false', 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0)
   );
   

--------------------------------------------------------
--  DDL for Table adminnotification
--------------------------------------------------------

CREATE TABLE "adminnotification" 
   (  "Id" NVARCHAR2(50), 
  "Name" NVARCHAR2(100) DEFAULT NULL, 
  "Description" NVARCHAR2(1000) DEFAULT NULL, 
  "StartDate" DATE DEFAULT NULL, 
  "ExpirationDate" DATE DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0)
   );   


--  DDL for Table bankfortransfer


CREATE TABLE "bankfortransfer" 
   (  "id" NVARCHAR2(50), 
  "Code" NVARCHAR2(50), 
  "Name" NVARCHAR2(200), 
  "Description" NVARCHAR2(200) DEFAULT NULL, 
  "Logo" BLOB, 
  "Url" NVARCHAR2(100) DEFAULT NULL, 
  "GLAccount" NVARCHAR2(50), 
  "Address_id" NVARCHAR2(50) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0)
   );

--  DDL for Table bankservice


CREATE TABLE "bankservice" 
   (  "id" NVARCHAR2(50), 
  "BankForTransfer_id" NVARCHAR2(50) DEFAULT NULL, 
  "Service_id" NVARCHAR2(50) DEFAULT NULL, 
  "RoutingNumber" NVARCHAR2(50) DEFAULT NULL, 
  "RoutingCode" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0)
   );

--  DDL for Table compositepermission


CREATE TABLE "compositepermission" 
   (  "id" NVARCHAR2(50), 
  "Permission_id" NVARCHAR2(50), 
  "Name" NVARCHAR2(50) DEFAULT NULL, 
  "Description" NVARCHAR2(300) DEFAULT NULL, 
  "Entitlement_id" NVARCHAR2(50) DEFAULT NULL, 
  "isEnabled" NUMBER(1,0) DEFAULT (0), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0)
   );

--  DDL for Table configurations


CREATE TABLE "configurations" 
   (  "configuration_id" VARCHAR2(255 CHAR), 
  "bundle_id" VARCHAR2(50 CHAR), 
  "config_type" VARCHAR2(10 CHAR), 
  "config_key" VARCHAR2(600 CHAR), 
  "description" CLOB, 
  "config_value" CLOB, 
  "target" VARCHAR2(6 CHAR), 
  "isPreLoginConfiguration" NUMBER(1,0) DEFAULT (0), 
  "createdby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "modifiedby" VARCHAR2(50 CHAR) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
   );

--  DDL for Table csrassistgrant


CREATE TABLE "csrassistgrant" 
   (  "id" NVARCHAR2(50), 
  "userId" NVARCHAR2(50), 
  "userRoleId" VARCHAR2(500 CHAR), 
  "customerId" NVARCHAR2(50), 
  "CustomerType" NVARCHAR2(50) DEFAULT NULL, 
  "userName" NVARCHAR2(50) DEFAULT NULL, 
  "internalUserName" NVARCHAR2(50) DEFAULT NULL, 
  "isConsumed" NUMBER(1,0) DEFAULT (0), 
  "appId" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "lastretrievedts" TIMESTAMP (6), 
  "tokenconsumedts" TIMESTAMP (6), 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0), 
  "accountId" VARCHAR2(50 CHAR) DEFAULT NULL
   );

--  DDL for Table customernote


CREATE TABLE "customernote" 
   (  "id" NVARCHAR2(50), 
  "Customer_id" NVARCHAR2(50) DEFAULT NULL, 
  "Note" NVARCHAR2(1000), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0)
   );

--  DDL for Table customernotification


CREATE TABLE "customernotification" 
   (  "Customer_id" NVARCHAR2(50) DEFAULT (' '), 
  "Notification_id" NVARCHAR2(50) DEFAULT (' '), 
  "IsRead" NUMBER(1,0) DEFAULT (0), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0)
   );

--  DDL for Table customersecurityimages


CREATE TABLE "customersecurityimages" 
   (  "Customer_id" NVARCHAR2(50), 
  "Image_id" NVARCHAR2(45), 
  "isTypeBusiness" NVARCHAR2(45) DEFAULT NULL, 
  "createdby" NVARCHAR2(45) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(45) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT NULL
   );

--  DDL for Table customerservice


CREATE TABLE "customerservice" 
   (  "id" NVARCHAR2(50), 
  "Type_id" NVARCHAR2(50) DEFAULT NULL, 
  "Name" NVARCHAR2(50) DEFAULT NULL, 
  "Description" NVARCHAR2(300) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "HasWeekendOperation" NUMBER(1,0) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0), 
  "WorkSchedule_id" NVARCHAR2(50) DEFAULT NULL, 
  "TransactionFee_id" NVARCHAR2(50) DEFAULT NULL, 
  "TransactionLimit_id" NVARCHAR2(50) DEFAULT NULL, 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
   );

--  DDL for Table dashboardalerts


CREATE TABLE "dashboardalerts" 
   (  "id" NVARCHAR2(50), 
  "Title" NVARCHAR2(50), 
  "Description" NVARCHAR2(300) DEFAULT NULL, 
  "Type" NVARCHAR2(50), 
  "Priority" NVARCHAR2(50), 
  "created" NVARCHAR2(50) DEFAULT NULL
   );

--  DDL for Table dayschedule


CREATE TABLE "dayschedule" 
   (  "id" NVARCHAR2(50), 
  "WorkSchedule_id" NVARCHAR2(50), 
  "WeekDayName" NVARCHAR2(50), 
  "Code" NVARCHAR2(50) DEFAULT NULL, 
  "StartTime" VARCHAR2(200 CHAR) DEFAULT NULL, 
  "EndTime" VARCHAR2(200 CHAR) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0)
   );

--  DDL for Table faqcategory


CREATE TABLE "faqcategory" 
   (  "id" NVARCHAR2(50), 
  "Name" NVARCHAR2(100) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0)
   );

--  DDL for Table faqs


CREATE TABLE "faqs" 
   (  "id" NVARCHAR2(50), 
  "Channel_id" NVARCHAR2(50) DEFAULT NULL, 
  "QuestionCode" NVARCHAR2(50), 
  "Question" CLOB DEFAULT NULL, 
  "Answer" CLOB DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "FaqCategory_Id" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
   );

--  DDL for Table locationfile


CREATE TABLE "locationfile" 
   (  "id" NVARCHAR2(50), 
  "successcount" NUMBER(10,0) DEFAULT NULL, 
  "failurecount" NUMBER(10,0) DEFAULT NULL, 
  "locationfilestatus" NUMBER(10,0) DEFAULT NULL, 
  "locationfileclob" NCLOB, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
   );

--  DDL for Table locationservice


CREATE TABLE "locationservice" 
   (  "Location_id" NVARCHAR2(50), 
  "Service_id" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(3,0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
   );

--  DDL for Table logview


CREATE TABLE "logview" 
   (  "id" NVARCHAR2(50), 
  "User_id" NVARCHAR2(50) DEFAULT NULL, 
  "ViewName" NVARCHAR2(200) DEFAULT NULL, 
  "Description" NVARCHAR2(200) DEFAULT NULL, 
  "viewData" NVARCHAR2(500) DEFAULT NULL, 
  "LogType" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
   );

--  DDL for Table messagetemplate


CREATE TABLE "messagetemplate" 
   (  "id" NVARCHAR2(50), 
  "Name" NVARCHAR2(80), 
  "Body" NCLOB, 
  "AdditionalInfo" NVARCHAR2(500) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "creadtedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
   );

--  DDL for Table outagemessage


CREATE TABLE "outagemessage" 
   (  "id" NVARCHAR2(50), 
  "name" NVARCHAR2(100) DEFAULT NULL, 
  "Channel_id" NVARCHAR2(50) DEFAULT NULL, 
  "Service_id" NVARCHAR2(50) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50), 
  "MessageText" NCLOB, 
  "startTime" TIMESTAMP (6), 
  "endTime" TIMESTAMP (6), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
   );

--  DDL for Table periodiclimit


CREATE TABLE "periodiclimit" 
   (  "id" NVARCHAR2(50), 
  "TransactionLimit_id" NVARCHAR2(50), 
  "Period_id" NVARCHAR2(50) DEFAULT NULL, 
  "Code" NVARCHAR2(50) DEFAULT NULL, 
  "MaximumLimit" NUMBER(20,2) DEFAULT NULL, 
  "Currency" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0)
   );

--  DDL for Table privacypolicy


CREATE TABLE "privacypolicy" 
   (  "id" NVARCHAR2(50), 
  "Channel_id" NVARCHAR2(50) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "Description" NCLOB, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
   );

--  DDL for Table rolecompositepermission


CREATE TABLE "rolecompositepermission" 
   (  "Role_id" NVARCHAR2(50), 
  "CompositePermission_id" NVARCHAR2(50), 
  "isEnabled" NUMBER(1,0) DEFAULT (0), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
   );

--  DDL for Table rolepermission


CREATE TABLE "rolepermission" 
   (  "Role_id" NVARCHAR2(50), 
  "Permission_id" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
   );

--  DDL for Table servicecommunication


CREATE TABLE "servicecommunication" 
   (  "id" NVARCHAR2(50), 
  "Type_id" NVARCHAR2(50) DEFAULT NULL, 
  "Service_id" NVARCHAR2(50) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "Priority" NUMBER(10,0) DEFAULT NULL, 
  "Value" NVARCHAR2(100) DEFAULT NULL, 
  "Extension" NVARCHAR2(50) DEFAULT NULL, 
  "Description" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(3,0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
   );

--  DDL for Table systemuser


CREATE TABLE "systemuser" 
   (  "id" NVARCHAR2(50), 
  "Status_id" NVARCHAR2(50), 
  "Username" NVARCHAR2(50), 
  "Password" NVARCHAR2(100), 
  "Email" NVARCHAR2(70), 
  "Code" NVARCHAR2(50) DEFAULT NULL, 
  "FirstName" NVARCHAR2(255) DEFAULT NULL, 
  "MiddleName" NVARCHAR2(255) DEFAULT NULL, 
  "LastName" NVARCHAR2(255) DEFAULT NULL, 
  "FailedCount" NUMBER(10,0) DEFAULT NULL, 
  "LastPasswordChangedts" TIMESTAMP (6), 
  "ResetpasswordLink" NVARCHAR2(255) DEFAULT NULL, 
  "ResetPasswordExpdts" TIMESTAMP (6), 
  "lastLogints" TIMESTAMP (6), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
   );

--  DDL for Table transactionfeeslab


CREATE TABLE "transactionfeeslab" 
   (  "id" NVARCHAR2(50), 
  "TransactionFee_id" NVARCHAR2(50), 
  "MinimumTransactionValue" NVARCHAR2(50), 
  "MaximumTransactionValue" NVARCHAR2(50), 
  "Currency" NVARCHAR2(50), 
  "Fees" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0)
   );

--  DDL for Table transactiongroup


CREATE TABLE "transactiongroup" 
   (  "id" NVARCHAR2(50), 
  "Name" NVARCHAR2(100) DEFAULT NULL, 
  "Description" NVARCHAR2(100) DEFAULT NULL, 
  "TransactionLimit_id" NVARCHAR2(50) DEFAULT NULL, 
  "Status_id" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6), 
  "lastmodifiedts" TIMESTAMP (6), 
  "synctimestamp" TIMESTAMP (6), 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0)
   );

--  DDL for Table transactiongroupservice


CREATE TABLE "transactiongroupservice" 
   (  "id" NVARCHAR2(50), 
  "TransactionGroup_id" NVARCHAR2(50) DEFAULT NULL, 
  "Service_id" NVARCHAR2(50) DEFAULT NULL, 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" VARCHAR2(200 CHAR) DEFAULT NULL, 
  "lastmodifiedts" TIMESTAMP (6), 
  "synctimestamp" TIMESTAMP (6), 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0)
   );

--  DDL for Table transactionlogs


CREATE TABLE "transactionlogs" 
   (  "id" NUMBER(10,0), 
  "transactionType" NVARCHAR2(45) DEFAULT NULL, 
  "transactionId" NVARCHAR2(45) DEFAULT NULL, 
  "userId" NVARCHAR2(45) DEFAULT NULL, 
  "userName" NVARCHAR2(45) DEFAULT NULL, 
  "fromAccount" NVARCHAR2(45) DEFAULT NULL, 
  "fromAccType" NVARCHAR2(45) DEFAULT NULL, 
  "toAccount" NVARCHAR2(45) DEFAULT NULL, 
  "toAccType" NVARCHAR2(45) DEFAULT NULL, 
  "amount" NVARCHAR2(45) DEFAULT NULL, 
  "currency" NVARCHAR2(45) DEFAULT NULL, 
  "payeeName" NVARCHAR2(45) DEFAULT NULL, 
  "status" NVARCHAR2(45) DEFAULT NULL, 
  "description" NVARCHAR2(100) DEFAULT NULL, 
  "beneficiaryRoutingNum" NVARCHAR2(45) DEFAULT NULL, 
  "batchId" NVARCHAR2(45) DEFAULT NULL, 
  "logId" NVARCHAR2(45) DEFAULT NULL, 
  "type" NVARCHAR2(45) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP
   );

--  DDL for Table useraddress


CREATE TABLE "useraddress" 
   (  "User_id" NVARCHAR2(50), 
  "Address_id" NVARCHAR2(50), 
  "Type_id" NVARCHAR2(50), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
   );

--  DDL for Table usercompositepermission


CREATE TABLE "usercompositepermission" 
   (  "User_id" NVARCHAR2(50), 
  "CompositePermission_id" NVARCHAR2(50), 
  "isEnabled" NUMBER(1,0) DEFAULT (0), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0)
   );

--  DDL for Table userpermission


CREATE TABLE "userpermission" 
   (  "User_id" NVARCHAR2(50), 
  "Permission_id" NVARCHAR2(50), 
  "createdby" NVARCHAR2(45) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(45) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(3,0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
   );

--  DDL for Table userrole


CREATE TABLE "userrole" 
   (  "User_id" NVARCHAR2(50), 
  "Role_id" NVARCHAR2(50), 
  "hasSuperAdminPrivilages" NUMBER(1,0) DEFAULT (0), 
  "createdby" NVARCHAR2(50) DEFAULT NULL, 
  "modifiedby" NVARCHAR2(50) DEFAULT NULL, 
  "createdts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "lastmodifiedts" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "synctimestamp" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP, 
  "softdeleteflag" NUMBER(1,0) DEFAULT (0), 
  "companyLegalUnit" VARCHAR2(50 CHAR) DEFAULT 'ALL'
   );

--  DDL for Table variablereference


CREATE TABLE "variablereference" 
   (  "vr_key" NVARCHAR2(50), 
  "vr_value" NVARCHAR2(50)
   );

--  DDL for Table infinityjoblog

CREATE TABLE "infinityjoblog" (
  "id" varchar2(50) NOT NULL,
  "data" clob,
  "createdby" varchar2(50) DEFAULT NULL,
  "modifiedby" varchar2(50) DEFAULT NULL,
  "createdts" timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  "lastmodifiedts" timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  "lastsynctimestamp" timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  "softdeleteflag" number(3) DEFAULT '0' NOT NULL,
  PRIMARY KEY ("id")
) ;

--  DDL for Table infinityjob
CREATE TABLE "infinityjob" (
  "id" varchar2(50) NOT NULL,
  "data" clob,
  "status" varchar2(50) DEFAULT 'SID_JOB_INPROGRESS' NOT NULL,
  "createdby" varchar2(50) DEFAULT NULL,
  "modifiedby" varchar2(50) DEFAULT NULL,
  "createdts" timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  "lastmodifiedts" timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  "lastsynctimestamp" timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  "softdeleteflag" number(3) DEFAULT '0' NOT NULL,
  "type" varchar2(50) NOT NULL,
  PRIMARY KEY ("id")
) ;

--  DDL for Table jobtype
CREATE TABLE "jobtype" (
  "jobType" varchar2(50) NOT NULL,
  "jobName" varchar2(50) DEFAULT NULL,
  "createdby" varchar2(50) DEFAULT NULL,
  "modifiedby" varchar2(50) DEFAULT NULL,
  "createdts" timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  "lastmodifiedts" timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  "synctimestamp" timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  "softdeleteflag" number(3) DEFAULT '0' NOT NULL,
  PRIMARY KEY ("jobType")
) ;

--  DDL for Table excludedcustomroleactionlimits
CREATE TABLE "excludedcustomroleactionlimits" (
  "id" number(19) NOT NULL,
  "customRole_id" number(19) NOT NULL,
  "contractId" varchar2(50) DEFAULT NULL,
  "coreCustomerId" varchar2(50) DEFAULT NULL,
  "featureId" varchar2(50) DEFAULT NULL,
  "action_id" varchar2(255) NOT NULL,
  "account_id" varchar2(50) DEFAULT NULL,
  "createdby" varchar2(50) DEFAULT NULL,
  "modifiedby" varchar2(50) DEFAULT NULL,
  "createdts" timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  "lastmodifiedts" timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  "synctimestamp" timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  "softdeleteflag" number(3) DEFAULT '0' NOT NULL,
  "companyLegalUnit" varchar2(50) DEFAULT 'ALL',
  PRIMARY KEY ("id")
) ;

--  DDL for Table eventtriggerconfiguration
CREATE TABLE "eventtriggerconfiguration" (
  "id" number(10) NOT NULL,
  "service" varchar2(255) DEFAULT NULL,
  "classname" varchar2(45) DEFAULT NULL,
  "eventtype" varchar2(45) DEFAULT NULL,
  "eventsubtype" varchar2(45) DEFAULT NULL,
  "status" varchar2(45) DEFAULT NULL,
  "servicecall" varchar2(255) DEFAULT NULL,
  "hasfields" varchar2(255) DEFAULT NULL,
  "conditions" varchar2(255) DEFAULT NULL,
  "maskedfields" varchar2(255) DEFAULT NULL,
  "excludedfields" varchar2(255) DEFAULT NULL,
  "addFields" clob,
  "passcustomerid" varchar2(45) DEFAULT NULL,
  "accountLevelField" varchar2(45) DEFAULT NULL,
  "appid" varchar2(100) DEFAULT NULL,
  "isActive" number(3) DEFAULT NULL,
  PRIMARY KEY ("id")
) ;

--  DDL for Table customrolesignatorygroup
CREATE TABLE "customrolesignatorygroup" (
  "customroleSignatoryGroupId" varchar2(50) NOT NULL,
  "signatoryGroupId" varchar2(50) DEFAULT NULL,
  "customRoleId" number(19) DEFAULT NULL,
  "createdby" varchar2(50) DEFAULT NULL,
  "modifiedby" varchar2(50) DEFAULT NULL,
  "createdts" timestamp(0) DEFAULT SYSTIMESTAMP NULL,
  "lastmodifiedts" timestamp(0) DEFAULT SYSTIMESTAMP NULL,
  "synctimestamp" timestamp(0) DEFAULT SYSTIMESTAMP NOT NULL,
  "softdeleteflag" number(3) DEFAULT '0' NOT NULL,
  PRIMARY KEY ("customroleSignatoryGroupId"));

-----Indexes
CREATE UNIQUE INDEX "PK_ACCOUNTCOMMUNICATION_ID" ON "accountcommunication" ("id");
CREATE UNIQUE INDEX "SYS_C0021855" ON "accountlevelactionlimit" ("id");
CREATE UNIQUE INDEX "PK__ACCOUNTS__B19D4181FD86F5C4" ON "accounts" ("Account_id");
CREATE UNIQUE INDEX "PK_ACCOUNTSSTATEMENTFILES_ID" ON "accountsstatementfiles" ("id");
CREATE UNIQUE INDEX "PK_ACCOUNTSTATEMENT_ID" ON "accountstatement" ("Id");
CREATE UNIQUE INDEX "PK_ACCOUNTTYPE_TYPEID" ON "accounttype" ("TypeID");
CREATE UNIQUE INDEX "PK_ACHACCOUNTSTYPE_ID" ON "achaccountstype" ("id");
CREATE UNIQUE INDEX "PK_ACHFILE_ACHFILE_ID" ON "achfile" ("achFile_id");
CREATE UNIQUE INDEX "PK_ACHFILEFORMATTYPE_ID" ON "achfileformattype" ("id");
CREATE UNIQUE INDEX "PK_ADDETAILS_ID" ON "addetails" ("id");
CREATE UNIQUE INDEX "PK_ADDITIONALDATA_ID" ON "additionaldata" ("id");
CREATE UNIQUE INDEX "PK_ADDITIONALFIELD_ID" ON "additionalfield" ("id");
CREATE UNIQUE INDEX "PK_ADDRESS_ID" ON "address" ("id");
CREATE UNIQUE INDEX "PK_ADDRESSTYPE_ID" ON "addresstype" ("id");
CREATE UNIQUE INDEX "PK_ADVERTISEMENTS_ID" ON "advertisements" ("id");
CREATE UNIQUE INDEX "PK_ALERT_ID" ON "alert" ("id");
CREATE UNIQUE INDEX "PK_ALERTATTRIBUTE_ID" ON "alertattribute" ("id", "LanguageCode");
CREATE UNIQUE INDEX "PK_ALERTATTRIBUTELISTVALUES_ALERTATTRIBUTEID" ON "alertattributelistvalues" ("id", "AlertAttributeId", "LanguageCode");
CREATE UNIQUE INDEX "PK_ALERTCATEGORYCHANNEL_CHANNELID" ON "alertcategorychannel" ("ChannelID", "AlertCategoryId");
CREATE UNIQUE INDEX "PK_ALERTCONDITION_ID" ON "alertcondition" ("id", "LanguageCode");
CREATE UNIQUE INDEX "PK_ALERTCONTENTFIELDS_CODE" ON "alertcontentfields" ("Code");
CREATE UNIQUE INDEX "PK__ALERTFRE__3213E83F52A0FC95" ON "alertfrequency" ("id");
CREATE UNIQUE INDEX "PK__ALERTFRE__3213E83FE108E3A5" ON "alertfrequencyjobexectime" ("id");
CREATE UNIQUE INDEX "PK__ALERTFRE__78DD254126D5EBD7" ON "alertfrequencytext" ("alertFrequencyId", "languageCode");
CREATE UNIQUE INDEX "PK__ALERTFRE__3213E83F54C5F2C4" ON "alertfrequencytime" ("id");
CREATE UNIQUE INDEX "PK_ALERTHISTORY_ID" ON "alerthistory" ("id");
CREATE UNIQUE INDEX "PK__ALERTREC__3213E83F1875436B" ON "alertrecipienttype" ("id");
CREATE UNIQUE INDEX "PK_ALERTSUBTYPE_ID" ON "alertsubtype" ("id");
CREATE UNIQUE INDEX "PK__ALERTSUB__C07B88F85415F86A" ON "alertsubtypeaccounttype" ("accountTypeId", "alertSubTypeId");
CREATE UNIQUE INDEX "PK__ALERTSUB__37ACE26AA5ABAA6C" ON "alertsubtypeapp" ("appId", "alertSubTypeId");
CREATE UNIQUE INDEX "PK__ALERTSUB__E37AE9AFDD6FE9ED" ON "alertsubtypechannel" ("channelId", "alertSubTypeId");
CREATE UNIQUE INDEX "PK__ALERTSUB__DAF80F4FE7BF3616" ON "alertsubtypecustomertype" ("customerTypeId", "alertSubTypeId");
CREATE UNIQUE INDEX "PK__ALERTSUB__21203836D8184B9C" ON "alertsubtypetext" ("alertSubTypeId", "languageCode");
CREATE UNIQUE INDEX "PK_ALERTTYPE_ID" ON "alerttype" ("id");
CREATE UNIQUE INDEX "PK_ALERTTYPEACCOUNTTYPE_ACCOUNTTYPEID" ON "alerttypeaccounttype" ("AccountTypeId", "AlertTypeId");
CREATE UNIQUE INDEX "PK_ALERTTYPEAPP_APPID" ON "alerttypeapp" ("AppId", "AlertTypeId");
CREATE UNIQUE INDEX "PK__ALERTTYP__1F4EAD21AB2ECDA5" ON "alerttypechannel" ("channelId", "alertTypeId");
CREATE UNIQUE INDEX "PK_ALERTTYPECUSTOMERTYPE_CUSTOMERTYPEID" ON "alerttypecustomertype" ("CustomerTypeId", "AlertTypeId");
CREATE UNIQUE INDEX "PK_ANNUALPERCENTAGERATE_ID" ON "annualpercentagerate" ("id");
CREATE UNIQUE INDEX "PK_APP_ID" ON "app" ("id");
CREATE UNIQUE INDEX "PK_APPLICATION_ID" ON "application" ("id");
CREATE UNIQUE INDEX "PK_APPMAPPINGAID_ID" ON "appmappingaid" ("id");
CREATE UNIQUE INDEX "PK_APPOINTMENT_ID" ON "appointment" ("id");
CREATE UNIQUE INDEX "PK_ARCHIVEDALERTHISTORY_ID" ON "archivedalerthistory" ("Id");
CREATE UNIQUE INDEX "PK_ARCHIVEDCUSTOMERREQUEST_ID" ON "archivedcustomerrequest" ("id");
CREATE UNIQUE INDEX "PK_ARCHIVEDMEDIA_ID" ON "archivedmedia" ("id");
CREATE UNIQUE INDEX "PK_ARCHIVEDMESSAGEATTACHMENT_ID" ON "archivedmessageattachment" ("id");
CREATE UNIQUE INDEX "PK_ARCHIVEDREQUESTMESSAGE_ID" ON "archivedrequestmessage" ("id");
CREATE UNIQUE INDEX "PK_ATTACHMENTTYPE_ID" ON "attachmenttype" ("id");
CREATE UNIQUE INDEX "PK_ATTRIBUTE_ID" ON "attribute" ("id");
CREATE UNIQUE INDEX "PK_ATTRIBUTETYPE_ID" ON "attributetype" ("id");
CREATE UNIQUE INDEX "PK_BACKENDCERTIFICATE_ID" ON "backendcertificate" ("id");
CREATE UNIQUE INDEX "PK_BACKENDIDENTIFIER_ID" ON "backendidentifier" ("id");
CREATE UNIQUE INDEX "PK_BANK_ID" ON "bank" ("id");
CREATE UNIQUE INDEX "PK_BANKBRANCH_ID" ON "bankbranch" ("id");
CREATE UNIQUE INDEX "PK_BANKCOMMUNICATION_TYPE_ID" ON "bankcommunication" ("Type_id", "sequence");
CREATE UNIQUE INDEX "PK_BANKFORTRANSFER_ID" ON "bankfortransfer" ("id");
CREATE UNIQUE INDEX "PK_BANKSERVICE_ID" ON "bankservice" ("id");
CREATE UNIQUE INDEX "PK_BANNER_ID" ON "banner" ("id");
CREATE UNIQUE INDEX "PK_BATCHALERTDEFINITION_ALERTTYPE" ON "batchalertdefinition" ("alertType");
CREATE UNIQUE INDEX "PK_BATCHALERTOBJECT_OBJECTTYPE" ON "batchalertobject" ("objectType");
CREATE UNIQUE INDEX "PK_BBACTEDREQUEST_APPROVALID" ON "bbactedrequest" ("approvalId");
CREATE UNIQUE INDEX "PK_BBREQUEST_REQUESTID" ON "bbrequest" ("requestId");
CREATE UNIQUE INDEX "PK_BBTAXSUBTYPE_ID" ON "bbtaxsubtype" ("id");
CREATE UNIQUE INDEX "PK_BBTAXTYPE_ID" ON "bbtaxtype" ("id");
CREATE UNIQUE INDEX "PK_BBTEMPLATE_TEMPLATEID" ON "bbtemplate" ("templateId");
CREATE UNIQUE INDEX "PK_BBTEMPLATERECORD_TEMPLATERECORD_ID" ON "bbtemplaterecord" ("templateRecord_id");
CREATE UNIQUE INDEX "PK_BBTEMPLATEREQUESTTYPE_TEMPLATEREQUESTTYPE_ID" ON "bbtemplaterequesttype" ("templateRequestType_id");
CREATE UNIQUE INDEX "PK_BBTEMPLATESUBRECORD_TEMPLATESUBRECORD_ID" ON "bbtemplatesubrecord" ("templateSubRecord_id");
CREATE UNIQUE INDEX "PK_BBTEMPLATETYPE_TEMPLATETYPE_ID" ON "bbtemplatetype" ("templateType_id");
CREATE UNIQUE INDEX "PK_BBTRANSACTIONTYPE_TRANSACTIONTYPE_ID" ON "bbtransactiontype" ("transactionType_id");
CREATE UNIQUE INDEX "PK_BILL_ID" ON "bill" ("id");
CREATE UNIQUE INDEX "PK_BILLERCATEGORY_ID" ON "billercategory" ("id");
CREATE UNIQUE INDEX "PK_BILLERCOMPANY_ID" ON "billercompany" ("id");
CREATE UNIQUE INDEX "PK_BILLERMASTER_ID" ON "billermaster" ("id");
CREATE UNIQUE INDEX "PK_BRANCHTYPE_ID" ON "branchtype" ("id");
CREATE UNIQUE INDEX "PK_BUDGET_ID" ON "budget" ("id");
CREATE UNIQUE INDEX "PK_BULKWIREFILEFORMATTYPE_BULKWIRESFILEFORMATTYPECODE" ON "bulkwirefileformattype" ("bulkWiresFileFormatTypeCode");
CREATE UNIQUE INDEX "PK_BULKWIREFILELINEITEMS_BULKWIREFILELINEITEMID" ON "bulkwirefilelineitems" ("bulkWireFileLineItemID");
CREATE UNIQUE INDEX "PK_BULKWIREFILES_BULKWIREFILEID" ON "bulkwirefiles" ("bulkWireFileID");
CREATE UNIQUE INDEX "PK_BULKWIREFILETRANSACTDETAILS_BULKWIRETRANSACTIONID" ON "bulkwirefiletransactdetails" ("bulkWireTransactionID");
CREATE UNIQUE INDEX "PK_BULKWIRESAMPLEFILE_BULKWIRESAMPLEFILEID" ON "bulkwiresamplefile" ("bulkWireSampleFileID");
CREATE UNIQUE INDEX "PK_BULKWIRETEMPLATE_BULKWIRETEMPLATEID" ON "bulkwiretemplate" ("bulkWireTemplateID");
CREATE UNIQUE INDEX "PK_BULKWIRETEMPLATELINEITEMS_BULKWIRETEMPLATELINEITEMID" ON "bulkwiretemplatelineitems" ("bulkWireTemplateLineItemID");
CREATE UNIQUE INDEX "PK_BULKWIRETEMPLATETRANSACTDETAILS_BULKWIRETRANSACTIONID" ON "bulkwiretemplatetransactdetails" ("bulkWireTransactionID");
CREATE UNIQUE INDEX "PK_CARD_ID" ON "card" ("Id");
CREATE UNIQUE INDEX "PK_CARDACCOUNTREQUEST_ID" ON "cardaccountrequest" ("id");
CREATE UNIQUE INDEX "PK_CARDACCOUNTREQUESTTYPE_ID" ON "cardaccountrequesttype" ("id");
CREATE UNIQUE INDEX "PK__CARDPROD__2D10D16AEA2CBBEC" ON "cardproducts" ("productId");
CREATE UNIQUE INDEX "PK__CARDPROD__3213E83FDB8A9B13" ON "cardproducttype" ("id");
CREATE UNIQUE INDEX "PK_CARDSTATEMENTS_ID" ON "cardstatements" ("id");
CREATE UNIQUE INDEX "PK_CARDTRANSACTION_TRANSACTIONREFERENCENUMBER" ON "cardtransaction" ("transactionReferenceNumber");
CREATE UNIQUE INDEX "PK_CATEGORY_ID" ON "category" ("id");
CREATE UNIQUE INDEX "PK_CHANNEL_ID" ON "channel" ("id");
CREATE UNIQUE INDEX "PK_CHANNELTEXT_CHANNELID" ON "channeltext" ("channelID", "LanguageCode");
CREATE UNIQUE INDEX "PK_CHECK_ID" ON "check" ("id");
CREATE UNIQUE INDEX "PK_CHECKORDER_ID" ON "checkorder" ("id");
CREATE UNIQUE INDEX "PK_CITY_ID" ON "city" ("id");
CREATE UNIQUE INDEX "PK_COMMUNICATIONTEMPLATE_ID" ON "communicationtemplate" ("Id");
CREATE UNIQUE INDEX "PK_COMMUNICATIONTYPE_ID" ON "communicationtype" ("id");
CREATE UNIQUE INDEX "PK_COMPOSITEPERMISSION_ID" ON "compositepermission" ("id");
CREATE UNIQUE INDEX "PK_CONFIGURATIONMASTERS_BUNDLE_ID" ON "configurationmasters" ("bundle_id");
CREATE UNIQUE INDEX "PK_CONFIGURATIONS_CONFIGURATION_ID" ON "configurations" ("configuration_id");
CREATE UNIQUE INDEX "CONFIGURATIONS$UK_BUNDLEID_CONFIGKEY" ON "configurations" ("bundle_id", "config_key");
CREATE UNIQUE INDEX "PK_CONTRACT_ID" ON "contract" ("id");
CREATE UNIQUE INDEX "NAME_UNIQUE" ON "contract" ("name");
CREATE UNIQUE INDEX "PK_CONTRACTACCOUNTS_ID" ON "contractaccounts" ("id");
CREATE UNIQUE INDEX "ACCOUNTID_UNIQUE" ON "contractaccounts" ("accountId");
CREATE UNIQUE INDEX "PK_CONTRACTACTIONLIMIT_ID" ON "contractactionlimit" ("id");
CREATE UNIQUE INDEX "PK_CONTRACTADDRESS_ID" ON "contractaddress" ("id");
CREATE UNIQUE INDEX "PK_CONTRACTCOMMUNICATION_ID" ON "contractcommunication" ("id");
CREATE UNIQUE INDEX "PK_CONTRACTCORECUSTOMERS_ID" ON "contractcorecustomers" ("id");
CREATE UNIQUE INDEX "PK_CONTRACTCUSTOMERS_ID" ON "contractcustomers" ("id");
CREATE UNIQUE INDEX "PK_CONTRACTFEATURES_ID" ON "contractfeatures" ("id");
CREATE UNIQUE INDEX "SYS_C0017033" ON "corporatepayees" ("id");
CREATE UNIQUE INDEX "PK_COREMEMBERSHIP_ID" ON "coremembership" ("id");
CREATE UNIQUE INDEX "PK_COUNTRY_ID" ON "country" ("id");
CREATE UNIQUE INDEX "PK__COUNTRYB__3213E83F6A071F64" ON "countrybasecurrency" ("id");
CREATE UNIQUE INDEX "PK_CREDENTIALCHECKER_ID" ON "credentialchecker" ("id");
CREATE UNIQUE INDEX "PK_CSRASSISTGRANT_ID" ON "csrassistgrant" ("id");
CREATE UNIQUE INDEX "PK_CURRENCY_CODE" ON "currency" ("code");
CREATE UNIQUE INDEX "PK__CURRENCY__3213E83FB68136C6" ON "currencymarketrates" ("id");
CREATE UNIQUE INDEX "PK_CUSTOMER_ID" ON "customer" ("id");
CREATE UNIQUE INDEX "CUSTOMER$USERNAME_UNIQUE" ON "customer" ("UserName");
CREATE UNIQUE INDEX "PK_CUSTOMERACCOUNTS_ID" ON "customeraccounts" ("id");
CREATE UNIQUE INDEX "PK_CUSTOMERADDRESS_ADDRESS_ID" ON "customeraddress" ("Address_id");
CREATE UNIQUE INDEX "PK_CUSTOMERALERTCATEGORYCHANNEL_CUSTOMER_ID" ON "customeralertcategorychannel" ("Customer_id", "AlertCategoryId", "ChannelId", "AccountId", "AccountType");
CREATE UNIQUE INDEX "PK__CUSTOMER__ACAA01E599E9178E" ON "customeralertchannel" ("customerId", "alertCategoryId", "alertTypeId", "alertSubTypeId", "channelId", "accountId", "accountType");
CREATE UNIQUE INDEX "PK_CUSTOMERALERTENTITLEMENT_CUSTOMER_ID" ON "customeralertentitlement" ("Customer_id", "Alert_id");
CREATE UNIQUE INDEX "PK__CUSTOMER__BD9BACB61BA6B159" ON "customeralertfrequency" ("customerId", "alertCategoryId", "alertTypeId", "alertSubTypeId", "accountId", "accountType");
CREATE UNIQUE INDEX "PK_CUSTOMERALERTSWITCH_CUSTOMER_ID" ON "customeralertswitch" ("Customer_id", "AccountID", "AlertCategoryId", "AccountType");
CREATE UNIQUE INDEX "PK_CUSTOMERCOMMUNICATION_ID" ON "customercommunication" ("id");
CREATE UNIQUE INDEX "PK_CUSTOMERDEVICE_ID" ON "customerdevice" ("id", "Customer_id");
CREATE UNIQUE INDEX "PK_CUSTOMERENTITLEMENT_CUSTOMER_ID" ON "customerentitlement" ("Customer_id", "Service_id");
CREATE UNIQUE INDEX "PK_CUSTOMEREXPENSE_ID" ON "customerexpense" ("id");
CREATE UNIQUE INDEX "PK_CUSTOMERFILE_ID" ON "customerfile" ("id");
CREATE UNIQUE INDEX "PK_CUSTOMERFLAGSTATUS_CUSTOMER_ID" ON "customerflagstatus" ("Customer_id", "Status_id");
CREATE UNIQUE INDEX "PK_CUSTOMERIMAGE_CUSTOMER_ID" ON "customerimage" ("id");
CREATE UNIQUE INDEX "PK_CUSTOMERLIMITGROUPLIMITS_ID" ON "customerlimitgrouplimits" ("id", "synctimestamp");
CREATE UNIQUE INDEX "PK_CUSTOMERNOTE_ID" ON "customernote" ("id");
CREATE UNIQUE INDEX "PK_CUSTOMERNOTIFICATION_CUSTOMER_ID" ON "customernotification" ("Customer_id", "Notification_id");
CREATE UNIQUE INDEX "PK_CUSTOMERPREFERENCE_ID" ON "customerpreference" ("id");
CREATE UNIQUE INDEX "PK_CUSTOMERPREVIOUSPASSWORDS_ID" ON "customerpreviouspasswords" ("id");
CREATE UNIQUE INDEX "PK_CUSTOMERPRODUCT_CUSTOMER_ID" ON "customerproduct" ("Customer_id", "Product_id");
CREATE UNIQUE INDEX "PK_CUSTOMERREQUEST_ID" ON "customerrequest" ("id");
CREATE UNIQUE INDEX "PK_CUSTOMERSECURITYIMAGES_CUSTOMER_ID" ON "customersecurityimages" ("Customer_id", "Image_id");
CREATE UNIQUE INDEX "PK_CUSTOMERSECURITYQUESTIONS_CUSTOMER_ID" ON "customersecurityquestions" ("Customer_id", "SecurityQuestion_id");
CREATE UNIQUE INDEX "PK_CUSTOMERSERVICE_ID" ON "customerservice" ("id");
CREATE UNIQUE INDEX "PK_CUSTOMERTERMSANDCONDITIONS_ID" ON "customertermsandconditions" ("id");
CREATE UNIQUE INDEX "PK_CUSTOMERTYPE_ID" ON "customertype" ("id");
CREATE UNIQUE INDEX "PK_CUSTOMERTYPECONFIG_ID" ON "customertypeconfig" ("id");
CREATE UNIQUE INDEX "PK__CUSTOMER__3213E83FAD4EA884" ON "customerviewalertconfiguration" ("id");
CREATE UNIQUE INDEX "PK__CUSTOMRO__3213E83FD181A183" ON "customroleaccounts" ("id");
CREATE UNIQUE INDEX "PK_DASHBOARDALERTS_ID" ON "dashboardalerts" ("id");
CREATE UNIQUE INDEX "PK_DATATYPE_ID" ON "datatype" ("id");
CREATE UNIQUE INDEX "PK_DAYSCHEDULE_ID" ON "dayschedule" ("id");
CREATE UNIQUE INDEX "PK_DBPCONFIG_ID" ON "dbpconfig" ("id");
CREATE UNIQUE INDEX "PK_DBXALERTCATEGORY_ID" ON "dbxalertcategory" ("id");
CREATE UNIQUE INDEX "PK_DBXALERTCATEGORYTEXT_ALERTCATEGORYID" ON "dbxalertcategorytext" ("AlertCategoryId", "LanguageCode");
CREATE UNIQUE INDEX "PK_DBXALERTTYPE_ID" ON "dbxalerttype" ("id");
CREATE UNIQUE INDEX "PK_DBXALERTTYPETEXT_ALERTTYPEID" ON "dbxalerttypetext" ("AlertTypeId", "LanguageCode");
CREATE UNIQUE INDEX "PK__DBXCUSTO__D85DA25764F1CB57" ON "dbxcustomeralertentitlement" ("Customer_id", "AlertTypeId", "AccountId", "AccountType", "alertCategoryId", "alertSubTypeId");
CREATE UNIQUE INDEX "PK_DECISIONRESULT_ID" ON "decisionresult" ("id");
CREATE UNIQUE INDEX "PK_DEVICEREGISTRATION_ID" ON "deviceregistration" ("id");
CREATE UNIQUE INDEX "DEVICEREGISTRATION$UNIQUE_INDEX" ON "deviceregistration" ("User_id", "DeviceId");
CREATE UNIQUE INDEX "PK_DIGITALPROFILE_ID" ON "digitalprofile" ("Id");
CREATE UNIQUE INDEX "PK_DMADDINTERACTIONS_ID" ON "dmaddinteractions" ("id");
CREATE UNIQUE INDEX "PK_DMADVERTISEMENTS_ID" ON "dmadvertisements" ("id");
CREATE UNIQUE INDEX "PK_EMAILTEMPLATES_ID" ON "emailtemplates" ("id");
CREATE UNIQUE INDEX "PK_EMPLOYEMENTDETAILS_ID" ON "employementdetails" ("id");
CREATE UNIQUE INDEX "PK_EVENT_EVENT_ID" ON "event" ("Event_id");
CREATE UNIQUE INDEX "PK_EVENTACTIVITYTYPE_ID" ON "eventactivitytype" ("id");
CREATE UNIQUE INDEX "PK_EVENTCONSUMER_SERVICEID" ON "eventconsumer" ("ServiceId", "OperationId");
CREATE UNIQUE INDEX "PK_EVENTCONSUMERTYPES_SERVICEID" ON "eventconsumertypes" ("ServiceId", "OperationId", "EventType");
CREATE UNIQUE INDEX "PK_EVENTSUBTYPE_ID" ON "eventsubtype" ("id", "eventtypeid");
CREATE UNIQUE INDEX "PK_EVENTTOPICCONFIGURATION_EVENTCODE" ON "eventtopicconfiguration" ("eventCode", "topic");
CREATE UNIQUE INDEX "PK_EVENTTYPE_ID" ON "eventtype" ("id");
CREATE UNIQUE INDEX "PK_EXCHANGERATES_ID" ON "exchangerates" ("id");
CREATE UNIQUE INDEX "PK_EXCLUDEDCONTRACTACCOUNTS_ID" ON "excludedcontractaccounts" ("id");
CREATE UNIQUE INDEX "EXCLUDEDCONTRACTACCOUNTS_ACCOUNTID_UNIQUE" ON "excludedcontractaccounts" ("accountId");
CREATE UNIQUE INDEX "PK_EXCLUDEDCUSTOMERACCOUNTS_ID" ON "excludedcustomeraccounts" ("id");
CREATE UNIQUE INDEX "PK_EXCLUDEDCUSTOMERACTION_ID" ON "excludedcustomeraction" ("id");
CREATE UNIQUE INDEX "PK__CUSTOMRO__3213E83FC1DF4F66" ON "excludedcustomroleaccounts" ("id");
CREATE UNIQUE INDEX "PK_EXPENSECATEGORY_ID" ON "expensecategory" ("id");
CREATE UNIQUE INDEX "PK_EXPENSEPERIOD_ID" ON "expenseperiod" ("id");
CREATE UNIQUE INDEX "PK_EXTERNALACCOUNT_ID" ON "externalaccount" ("Id");
CREATE UNIQUE INDEX "PK_EXTERNALBANK_ID" ON "externalbank" ("id");
CREATE UNIQUE INDEX "PK_EXTERNALBANKIDENTITY_ID" ON "externalbankidentity" ("id");
CREATE UNIQUE INDEX "PK_FAQCATEGORY_ID" ON "faqcategory" ("id");
CREATE UNIQUE INDEX "PK_FAQS_ID" ON "faqs" ("id");
CREATE UNIQUE INDEX "FAQS$QUESTIONCODE_UNIQUE" ON "faqs" ("QuestionCode");
CREATE UNIQUE INDEX "PK__FAVOURIT__3213E83FF73F5728" ON "favouriteinstruments" ("id");
CREATE UNIQUE INDEX "FAVOINSTRUMENTS_USERID" ON "favouriteinstruments" ("userId");
CREATE UNIQUE INDEX "PK_FEEDBACK_ID" ON "feedback" ("id");
CREATE UNIQUE INDEX "PK_FEEDBACKSTATUS_ID" ON "feedbackstatus" ("id");
CREATE UNIQUE INDEX "PK_GROUPENTITLEMENT_GROUP_ID" ON "groupentitlement" ("Group_id", "Service_id");
CREATE UNIQUE INDEX "PK_HOLIDAYS_ID" ON "holidays" ("id");
CREATE UNIQUE INDEX "PK_IBAN_ID" ON "iban" ("id");
CREATE UNIQUE INDEX "PK_IDMCONFIGURATION_ID" ON "idmconfiguration" ("id");
CREATE UNIQUE INDEX "PK_INFORMATIONCONTENT_ID" ON "informationcontent" ("id");
CREATE UNIQUE INDEX "PK_INTERESTRATES_ID" ON "interestrates" ("id");
CREATE UNIQUE INDEX "PK_ISSUERIMAGE_ID" ON "issuerimage" ("id");
CREATE UNIQUE INDEX "PK__LOANSCHE__3213E83F8BDC9D7C" ON "loanschedule" ("id");
CREATE UNIQUE INDEX "PK_LOCALE_CODE" ON "locale" ("Code");
CREATE UNIQUE INDEX "PK_LOCATION_ID" ON "location" ("id");
CREATE UNIQUE INDEX "LOCATION$CODE_UNIQUE" ON "location" ("Code");
CREATE UNIQUE INDEX "PK_LOCATIONFILE_ID" ON "locationfile" ("id");
CREATE UNIQUE INDEX "PK_LOCATIONLANGUAGE_ID" ON "locationlanguage" ("id", "Location_id");
CREATE UNIQUE INDEX "PK_LOCATIONSERVICE_LOCATION_ID" ON "locationservice" ("Location_id", "Service_id");
CREATE UNIQUE INDEX "PK_LOCATIONTYPE_ID" ON "locationtype" ("id");
CREATE UNIQUE INDEX "PK_LOCKOBJECTS_OBJECTID" ON "lockobjects" ("ObjectId", "User", "ExternalId");
CREATE UNIQUE INDEX "PK_LOGVIEW_ID" ON "logview" ("id");
CREATE UNIQUE INDEX "PK__MARKET__3213E83F4AE226D1" ON "market" ("id");
CREATE UNIQUE INDEX "PK_MEDIA_ID" ON "media" ("id");
CREATE UNIQUE INDEX "PK_MEMBERELIGIBILITY_ID" ON "membereligibility" ("id");
CREATE UNIQUE INDEX "PK_MEMBERGROUP_ID" ON "membergroup" ("id");
CREATE UNIQUE INDEX "PK_MEMBERSHIP_ID" ON "membership" ("id");
CREATE UNIQUE INDEX "PK_MEMBERSHIPACCOUNTS_ID" ON "membershipaccounts" ("id");
CREATE UNIQUE INDEX "PK_MEMBERSHIPOWNER_ID" ON "membershipowner" ("id");
CREATE UNIQUE INDEX "PK_MEMBERSHIPRELATION_ID" ON "membershiprelation" ("id");
CREATE UNIQUE INDEX "PK_MESSAGE_ID" ON "message" ("id");
CREATE UNIQUE INDEX "PK_MESSAGEATTACHMENT_ID" ON "messageattachment" ("id");
CREATE UNIQUE INDEX "PK_MESSAGECATEGORY_ID" ON "messagecategory" ("Id");
CREATE UNIQUE INDEX "PK_MESSAGESUBCATEGORY_ID" ON "messagesubcategory" ("Id");
CREATE UNIQUE INDEX "PK_MESSAGETEMPLATE_ID" ON "messagetemplate" ("id");
CREATE UNIQUE INDEX "PK_MESSAGETYPE_ID" ON "messagetype" ("id");
CREATE UNIQUE INDEX "PK_MFASERVICE_SERVICEKEY" ON "mfaservice" ("serviceKey");
CREATE UNIQUE INDEX "PK_MFASERVICECONFIG_ID" ON "mfaserviceconfig" ("id");
CREATE UNIQUE INDEX "PK_MODULE_ID" ON "module" ("id");
CREATE UNIQUE INDEX "PK_NEWACCOUNT_ID" ON "newaccount" ("id");
CREATE UNIQUE INDEX "PK_NEWUSER_ID" ON "newuser" ("id");
CREATE UNIQUE INDEX "NEWUSER$USERNAME" ON "newuser" ("userName");
CREATE UNIQUE INDEX "PK_NOTIFICATION_NOTIFICATIONID" ON "notification" ("notificationId");
CREATE UNIQUE INDEX "PK_NOTIFICATIONCARDINFO_ID" ON "notificationcardinfo" ("id");
CREATE UNIQUE INDEX "PK_NUMBERRANGE_OBJECTID" ON "numberrange" ("ObjectId");
CREATE UNIQUE INDEX "PK_OPERATINGHOURS_ID" ON "operatinghours" ("id", "Location_id");
CREATE UNIQUE INDEX "PK_ORGANISATION_ID" ON "organisation" ("id");
CREATE UNIQUE INDEX "ORGANISATION$NAME_UNIQUE" ON "organisation" ("Name");
CREATE UNIQUE INDEX "PK_ORGANISATIONACCOUNTS_ID" ON "organisationaccounts" ("id");
CREATE UNIQUE INDEX "ORGANISATIONACCOUNTS$UNIQUE_ACCOUNTID_TYPEID" ON "organisationaccounts" ("Account_id", "TypeID");
CREATE UNIQUE INDEX "PK_ORGANISATIONADDRESS_ID" ON "organisationaddress" ("id");
CREATE UNIQUE INDEX "PK_ORGANISATIONCOMMUNICATION_ID" ON "organisationcommunication" ("id");
CREATE UNIQUE INDEX "PK_ORGANISATIONEMPLOYEES_ID" ON "organisationemployees" ("id");
CREATE UNIQUE INDEX "PK_ORGANISATIONMEMBERSHIP_ID" ON "organisationmembership" ("id");
CREATE UNIQUE INDEX "PK_ORGANISATIONOWNER_ID" ON "organisationowner" ("id");
CREATE UNIQUE INDEX "PK_ORGANISATIONTYPE_ID" ON "organisationtype" ("id");
CREATE UNIQUE INDEX "PK_OTHERSOURCEOFINCOME_ID" ON "othersourceofincome" ("id");
CREATE UNIQUE INDEX "PK_OTPCOUNT_KEY" ON "otpcount" ("key");
CREATE UNIQUE INDEX "PK_OUTAGEMESSAGE_ID" ON "outagemessage" ("id");
CREATE UNIQUE INDEX "PK_P2PREGISTRATION_ID" ON "p2pregistration" ("id");
CREATE UNIQUE INDEX "PK_PASSWORDPOLICY_ID" ON "passwordpolicy" ("id");
CREATE UNIQUE INDEX "PK_PAYEE_ID" ON "payee" ("Id");
CREATE UNIQUE INDEX "PK_PAYEEADDRESS_ID" ON "payeeaddress" ("id");
CREATE UNIQUE INDEX "PK_PAYEETYPE_ID" ON "payeetype" ("id");
CREATE UNIQUE INDEX "PK__PAYMENTF__A03ECD0C1C242188" ON "paymentfiles" ("paymentFileID");
CREATE UNIQUE INDEX "PK_PAYPERSON_ID" ON "payperson" ("id");
CREATE UNIQUE INDEX "PK_PERIOD_ID" ON "period" ("id");
CREATE UNIQUE INDEX "PERIOD$NAME_UNIQUE" ON "period" ("Name");
CREATE UNIQUE INDEX "PK_PERIODICLIMIT_ID" ON "periodiclimit" ("id");
CREATE UNIQUE INDEX "PK_PERMISSION_ID" ON "permission" ("id");
CREATE UNIQUE INDEX "PK_PERMISSIONTYPE_ID" ON "permissiontype" ("id");
CREATE UNIQUE INDEX "PK_PFMBARGRAPH_ID" ON "pfmbargraph" ("id");
CREATE UNIQUE INDEX "PK_PFMBUDGETSNAPSHOT_ID" ON "pfmbudgetsnapshot" ("id");
CREATE UNIQUE INDEX "PK_PFMCATEGORY_ID" ON "pfmcategory" ("id");
CREATE UNIQUE INDEX "PK_PFMMONTH_ID" ON "pfmmonth" ("id");
CREATE UNIQUE INDEX "PK_PFMPIECHART_ID" ON "pfmpiechart" ("id");
CREATE UNIQUE INDEX "PK_PFMTRANSACTIONS_ID" ON "pfmtransactions" ("id");
CREATE UNIQUE INDEX "PK_PHONE_ID" ON "phone" ("id");
CREATE UNIQUE INDEX "PK__POPULARC__3213E83F356A6779" ON "popularcurrencies" ("id");
CREATE UNIQUE INDEX "PK_PREFERREDACCOUNT_TYPE_ID" ON "preferredaccount" ("Type_id");
CREATE UNIQUE INDEX "PK_PRIVACYPOLICY_ID" ON "privacypolicy" ("id");
CREATE UNIQUE INDEX "PK_PRODUCT_ID" ON "product" ("id");
CREATE UNIQUE INDEX "PRODUCT$MARKETINGSTATEID_UNIQUE" ON "product" ("MarketingStateId");
CREATE UNIQUE INDEX "PK_PRODUCTDETAIL_ID" ON "productdetail" ("id");
CREATE UNIQUE INDEX "PK_PRODUCTTYPE_ID" ON "producttype" ("id");
CREATE UNIQUE INDEX "PK_QUERYCOBORROWER_ID" ON "querycoborrower" ("id");
CREATE UNIQUE INDEX "PK_QUERYRESPONSECONSENT_ID" ON "queryresponseconsent" ("id");
CREATE UNIQUE INDEX "PK__RECENTCU__3213E83F4BEAFDFB" ON "recentcurrencies" ("id");
CREATE UNIQUE INDEX "PK_REGION_ID" ON "region" ("id");
CREATE UNIQUE INDEX "PK_REQUESTCATEGORY_ID" ON "requestcategory" ("id");
CREATE UNIQUE INDEX "PK_REQUESTMESSAGE_ID" ON "requestmessage" ("id");
CREATE UNIQUE INDEX "PK_ROLE_ID" ON "role" ("id");
CREATE UNIQUE INDEX "PK_ROLECOMPOSITEPERMISSION_ROLE_ID" ON "rolecompositepermission" ("Role_id", "CompositePermission_id");
CREATE UNIQUE INDEX "PK_ROLEPERMISSION_ROLE_ID" ON "rolepermission" ("Role_id", "Permission_id");
CREATE UNIQUE INDEX "PK_ROLETYPE_ID" ON "roletype" ("id");
CREATE UNIQUE INDEX "PK_SCHEDULEDTRANSACTION_ID" ON "scheduledtransaction" ("Id");
CREATE UNIQUE INDEX "PK_SECURITYIMAGE_ID" ON "securityimage" ("id");
CREATE UNIQUE INDEX "PK_SECURITYQUESTION_ID" ON "securityquestion" ("id");
CREATE UNIQUE INDEX "PK_SERVICE_ID" ON "service" ("id");
CREATE UNIQUE INDEX "PK_SERVICE_PERMISSION_MAPPER_ID" ON "service_permission_mapper" ("id");
CREATE UNIQUE INDEX "SERVICE_PERMISSION_MAPPER$UNIQUE_SERVICE_PERMISSION_MAPPER" ON "service_permission_mapper" ("service_name", "object_name", "operation");
CREATE UNIQUE INDEX "PK_SERVICECHANNEL_ID" ON "servicechannel" ("id");
CREATE UNIQUE INDEX "PK_SERVICECOMMUNICATION_ID" ON "servicecommunication" ("id");
CREATE UNIQUE INDEX "PK_SERVICETYPE_ID" ON "servicetype" ("id");
CREATE UNIQUE INDEX "PK_STATE_ID" ON "state" ("id");
CREATE UNIQUE INDEX "STATE$STATE_UNIQUE" ON "state" ("state");
CREATE UNIQUE INDEX "PK_STATUS_ID" ON "status" ("id", "Type_id");
CREATE UNIQUE INDEX "PK_STATUSCHANGE_ID" ON "statuschange" ("id");
CREATE UNIQUE INDEX "PK_STATUSTYPE_ID" ON "statustype" ("id");
CREATE UNIQUE INDEX "PK_SUSPENDEDCUSTOMERS_ID" ON "suspendedcustomers" ("id");
CREATE UNIQUE INDEX "PK_SWIFTCODE_ID" ON "swiftcode" ("id");
CREATE UNIQUE INDEX "PK_SYSTEMCONFIGURATION_PROPERTYNAME" ON "systemconfiguration" ("PropertyName");
CREATE UNIQUE INDEX "PK_SYSTEMUSER_ID" ON "systemuser" ("id");
CREATE UNIQUE INDEX "PK_TBLADDETAILS_ID" ON "tbladdetails" ("id");
CREATE UNIQUE INDEX "PK_TERMSANDCONDITIONS_ID" ON "termsandconditions" ("id");
CREATE UNIQUE INDEX "PK_TIMEPERIOD_ID" ON "timeperiod" ("id");
CREATE UNIQUE INDEX "PK__TRANSACT__3214EC079115CF13" ON "transaction" ("Id");
CREATE UNIQUE INDEX "PK_TRANSACTIONFEE_ID" ON "transactionfee" ("id");
CREATE UNIQUE INDEX "PK_TRANSACTIONFEESLAB_ID" ON "transactionfeeslab" ("id");
CREATE UNIQUE INDEX "PK_TRANSACTIONGROUP_ID" ON "transactiongroup" ("id");
CREATE UNIQUE INDEX "PK_TRANSACTIONGROUPSERVICE_ID" ON "transactiongroupservice" ("id");
CREATE UNIQUE INDEX "PK_TRANSACTIONLIMIT_ID" ON "transactionlimit" ("id");
CREATE UNIQUE INDEX "PK_TRANSACTIONLOGS_ID" ON "transactionlogs" ("id");
CREATE UNIQUE INDEX "PK_TRANSACTIONTYPE_ID" ON "transactiontype" ("id");
CREATE UNIQUE INDEX "PK__TRANSACT__F48FFFCFAD3851F8" ON "transactiontypemapping" ("backendTransactionTypeId");
CREATE UNIQUE INDEX "PK_TRANSFERSERIES_ID" ON "transferseries" ("id");
CREATE UNIQUE INDEX "PK_TRAVELNOTIFICATION_ID" ON "travelnotification" ("id");
CREATE UNIQUE INDEX "PK_USER_ID" ON "user" ("Id");
CREATE UNIQUE INDEX "USER$USER_NAME" ON "user" ("userName");
CREATE UNIQUE INDEX "USER$DEFAULTMODULE_ID_UNIQUE" ON "user" ("defaultModule_id");
CREATE UNIQUE INDEX "PK_USERACCOUNTALERTS_ID" ON "useraccountalerts" ("id");
CREATE UNIQUE INDEX "PK_USERACCOUNTS_ID" ON "useraccounts" ("id");
CREATE UNIQUE INDEX "PK_USERADDRESS_USER_ID" ON "useraddress" ("User_id", "Address_id", "Type_id");
CREATE UNIQUE INDEX "PK_USERALERTS_ID" ON "useralerts" ("id");
CREATE UNIQUE INDEX "PK_USERCASHFLOW_ID" ON "usercashflow" ("id");
CREATE UNIQUE INDEX "PK_USERCOMMUNICATION_ID" ON "usercommunication" ("id");
CREATE UNIQUE INDEX "PK_USERCOMPOSITEPERMISSION_USER_ID" ON "usercompositepermission" ("User_id", "CompositePermission_id");
CREATE UNIQUE INDEX "PK_USERCREDITCHECK_ID" ON "usercreditcheck" ("id");
CREATE UNIQUE INDEX "PK_USERNOTIFICATION_ID" ON "usernotification" ("id");
CREATE UNIQUE INDEX "PK_USERPERMISSION_PERMISSION_ID" ON "userpermission" ("User_id", "Permission_id");
CREATE UNIQUE INDEX "PK_USERPERSONALINFO_ID" ON "userpersonalinfo" ("id");
CREATE UNIQUE INDEX "PK_USERPRODUCTS_ID" ON "userproducts" ("id");
CREATE UNIQUE INDEX "PK_USERROLE_USER_ID" ON "userrole" ("User_id", "Role_id");
CREATE UNIQUE INDEX "PK_USERSECURITY_ID" ON "usersecurity" ("id");
CREATE UNIQUE INDEX "PK_USERSERVICEPREFERNCES_ID" ON "userserviceprefernces" ("id");
CREATE UNIQUE INDEX "PK_USERTRANSACTIONHISTORY_ID" ON "usertransactionhistory" ("id");
CREATE UNIQUE INDEX "PK_VARIABLEREFERENCE_VR_KEY" ON "variablereference" ("vr_key");
CREATE UNIQUE INDEX "PK_VIHICLEINFO_ID" ON "vihicleinfo" ("id");
CREATE UNIQUE INDEX "PK_WALLET_ID" ON "wallet" ("id");
CREATE UNIQUE INDEX "WALLET$UK_FEE6CFA8DB4440BA8E768221092" ON "wallet" ("user_id");
CREATE UNIQUE INDEX "PK__WEALTHUS__3213E83FBCB3542B" ON "wealthuserpreferences" ("id");
CREATE UNIQUE INDEX "PK__WEEKDAY__3213E83FA3D481B1" ON "weekday" ("id");
CREATE UNIQUE INDEX "PK__WEEKDAYV__F0AC4B3C5F5CE506" ON "weekdayvalue" ("weekdayId", "languageCode");
CREATE UNIQUE INDEX "PK_WORKSCHEDULE_ID" ON "workschedule" ("id");



 --  Constraints for Table accountcommunication

ALTER TABLE 
  "accountcommunication" 
ADD 
  CONSTRAINT "PK_ACCOUNTCOMMUNICATION_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table accountlevelactionlimit

ALTER TABLE 
  "accountlevelactionlimit" 
ADD 
  PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table accounts

ALTER TABLE 
  "accounts" 
ADD 
  CONSTRAINT "PK__ACCOUNTS__B19D4181FD86F5C4" PRIMARY KEY ("Account_id")
  USING INDEX ENABLE;
--  Constraints for Table accountsstatementfiles

ALTER TABLE 
  "accountsstatementfiles" 
ADD 
  CONSTRAINT "PK_ACCOUNTSSTATEMENTFILES_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table accountstatement

ALTER TABLE 
  "accountstatement" 
ADD 
  CONSTRAINT "PK_ACCOUNTSTATEMENT_ID" PRIMARY KEY ("Id")
  USING INDEX ENABLE;
--  Constraints for Table accounttype

ALTER TABLE 
  "accounttype" 
ADD 
  CONSTRAINT "PK_ACCOUNTTYPE_TYPEID" PRIMARY KEY ("TypeID")
  USING INDEX ENABLE;
--  Constraints for Table achaccountstype

ALTER TABLE 
  "achaccountstype" 
ADD 
  CONSTRAINT "PK_ACHACCOUNTSTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table achfile

ALTER TABLE 
  "achfile" 
ADD 
  CONSTRAINT "PK_ACHFILE_ACHFILE_ID" PRIMARY KEY ("achFile_id")
  USING INDEX ENABLE;
--  Constraints for Table achfileformattype

ALTER TABLE 
  "achfileformattype" 
ADD 
  CONSTRAINT "PK_ACHFILEFORMATTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;

--  Constraints for Table achtransaction
--------------------------------------------------------

ALTER TABLE "achtransaction" ADD CONSTRAINT "PK_ACHTRANSACTION_TRANSACTION_ID" PRIMARY KEY ("transaction_id")
  USING INDEX  ENABLE;
--------------------------------------------------------
--  Constraints for Table achtransactionrecord
--------------------------------------------------------

ALTER TABLE "achtransactionrecord" ADD CONSTRAINT "PK_ACHTRANSACTIONRECORD_TRANSACTIONRECORD_ID" PRIMARY KEY ("transactionRecord_id")
  USING INDEX  ENABLE;
--------------------------------------------------------
--  Constraints for Table achtransactionsubrecord
--------------------------------------------------------

ALTER TABLE "achtransactionsubrecord" ADD CONSTRAINT "PK_ACHTRANSACTIONSUBRECORD_TRANSCATIONSUBRECORD_ID" PRIMARY KEY ("transcationSubRecord_id")
  USING INDEX  ENABLE;   

--  Constraints for Table addetails

ALTER TABLE 
  "addetails" 
ADD 
  CONSTRAINT "PK_ADDETAILS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table address

ALTER TABLE 
  "address" 
ADD 
  CONSTRAINT "PK_ADDRESS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table additionaldata
  
ALTER TABLE "additionaldata" ADD CONSTRAINT "PK_ADDITIONALDATA_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table addresstype

ALTER TABLE 
  "addresstype" 
ADD 
  CONSTRAINT "PK_ADDRESSTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table advertisements

ALTER TABLE 
  "advertisements" 
ADD 
  CONSTRAINT "PK_ADVERTISEMENTS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table alert

ALTER TABLE 
  "alert" 
ADD 
  CONSTRAINT "PK_ALERT_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table adminnotification
--------------------------------------------------------

ALTER TABLE "adminnotification" ADD CONSTRAINT "PK_ADMINNOTIFICATION_ID" PRIMARY KEY ("Id")
  USING INDEX  ENABLE;

--  Constraints for Table alertattribute



ALTER TABLE 
  "alertattribute" 
ADD 
  CONSTRAINT "PK_ALERTATTRIBUTE_ID" PRIMARY KEY ("id",
"LanguageCode")
  USING INDEX ENABLE;
--  Constraints for Table alertattributelistvalues

ALTER TABLE 
  "alertattributelistvalues" 
ADD 
  CONSTRAINT "PK_ALERTATTRIBUTELISTVALUES_ALERTATTRIBUTEID" PRIMARY KEY (
    "id",
"AlertAttributeId",
"LanguageCode"
  )
  USING INDEX ENABLE;
--  Constraints for Table alertcategorychannel

ALTER TABLE 
  "alertcategorychannel" 
ADD 
  CONSTRAINT "PK_ALERTCATEGORYCHANNEL_CHANNELID" PRIMARY KEY ("ChannelID",
"AlertCategoryId")
  USING INDEX ENABLE;
--  Constraints for Table alertcondition

ALTER TABLE 
  "alertcondition" 
ADD 
  CONSTRAINT "PK_ALERTCONDITION_ID" PRIMARY KEY ("id",
"LanguageCode")
  USING INDEX ENABLE;
--  Constraints for Table alertcontentfields

ALTER TABLE 
  "alertcontentfields" 
ADD 
  CONSTRAINT "PK_ALERTCONTENTFIELDS_CODE" PRIMARY KEY ("Code")
  USING INDEX ENABLE;
--  Constraints for Table alertfrequency

ALTER TABLE 
  "alertfrequency" 
ADD 
  CONSTRAINT "PK__ALERTFRE__3213E83F52A0FC95" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table alertfrequencyjobexectime

ALTER TABLE 
  "alertfrequencyjobexectime" 
ADD 
  CONSTRAINT "PK__ALERTFRE__3213E83FE108E3A5" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table alertfrequencytext

ALTER TABLE 
  "alertfrequencytext" 
ADD 
  CONSTRAINT "PK__ALERTFRE__78DD254126D5EBD7" PRIMARY KEY (
    "alertFrequencyId",
"languageCode"
  )
  USING INDEX ENABLE;
--  Constraints for Table alertfrequencytime

ALTER TABLE 
  "alertfrequencytime" 
ADD 
  CONSTRAINT "PK__ALERTFRE__3213E83F54C5F2C4" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table alerthistory

ALTER TABLE 
  "alerthistory" 
ADD 
  CONSTRAINT "PK_ALERTHISTORY_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table alertrecipienttype

ALTER TABLE 
  "alertrecipienttype" 
ADD 
  CONSTRAINT "PK__ALERTREC__3213E83F1875436B" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table alertsubtype

ALTER TABLE 
  "alertsubtype" 
ADD 
  CONSTRAINT "PK_ALERTSUBTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table alertsubtypeaccounttype

ALTER TABLE 
  "alertsubtypeaccounttype" 
ADD 
  CONSTRAINT "PK__ALERTSUB__C07B88F85415F86A" PRIMARY KEY (
    "accountTypeId",
"alertSubTypeId"
  )
  USING INDEX ENABLE;
--  Constraints for Table alertsubtypeapp

ALTER TABLE 
  "alertsubtypeapp" 
ADD 
  CONSTRAINT "PK__ALERTSUB__37ACE26AA5ABAA6C" PRIMARY KEY ("appId",
"alertSubTypeId")
  USING INDEX ENABLE;
--  Constraints for Table alertsubtypechannel

ALTER TABLE 
  "alertsubtypechannel" 
ADD 
  CONSTRAINT "PK__ALERTSUB__E37AE9AFDD6FE9ED" PRIMARY KEY ("channelId",
"alertSubTypeId")
  USING INDEX ENABLE;
--  Constraints for Table alertsubtypecustomertype

ALTER TABLE 
  "alertsubtypecustomertype" 
ADD 
  CONSTRAINT "PK__ALERTSUB__DAF80F4FE7BF3616" PRIMARY KEY (
    "customerTypeId",
"alertSubTypeId"
  )
  USING INDEX ENABLE;
--  Constraints for Table alertsubtypetext

ALTER TABLE 
  "alertsubtypetext" 
ADD 
  CONSTRAINT "PK__ALERTSUB__21203836D8184B9C" PRIMARY KEY (
    "alertSubTypeId",
"languageCode"
  )
  USING INDEX ENABLE;
--  Constraints for Table alerttype

ALTER TABLE 
  "alerttype" 
ADD 
  CONSTRAINT "PK_ALERTTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table alerttypeaccounttype

ALTER TABLE 
  "alerttypeaccounttype" 
ADD 
  CONSTRAINT "PK_ALERTTYPEACCOUNTTYPE_ACCOUNTTYPEID" PRIMARY KEY ("AccountTypeId",
"AlertTypeId")
  USING INDEX ENABLE;
--  Constraints for Table alerttypeapp

ALTER TABLE 
  "alerttypeapp" 
ADD 
  CONSTRAINT "PK_ALERTTYPEAPP_APPID" PRIMARY KEY ("AppId",
"AlertTypeId")
  USING INDEX ENABLE;
--  Constraints for Table alerttypechannel

ALTER TABLE 
  "alerttypechannel" 
ADD 
  CONSTRAINT "PK__ALERTTYP__1F4EAD21AB2ECDA5" PRIMARY KEY ("channelId",
"alertTypeId")
  USING INDEX ENABLE;
--  Constraints for Table alerttypecustomertype

ALTER TABLE 
  "alerttypecustomertype" 
ADD 
  CONSTRAINT "PK_ALERTTYPECUSTOMERTYPE_CUSTOMERTYPEID" PRIMARY KEY ("CustomerTypeId",
"AlertTypeId")
  USING INDEX ENABLE;
--  Constraints for Table annualpercentagerate

ALTER TABLE 
  "annualpercentagerate" 
ADD 
  CONSTRAINT "PK_ANNUALPERCENTAGERATE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table app

ALTER TABLE 
  "app" 
ADD 
  CONSTRAINT "PK_APP_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table application



ALTER TABLE 
  "application" 
ADD 
  CONSTRAINT "PK_APPLICATION_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table appmappingaid

ALTER TABLE 
  "appmappingaid" 
ADD 
  CONSTRAINT "PK_APPMAPPINGAID_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table appointment

ALTER TABLE 
  "appointment" 
ADD 
  CONSTRAINT "PK_APPOINTMENT_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table archivedalerthistory

ALTER TABLE 
  "archivedalerthistory" 
ADD 
  CONSTRAINT "PK_ARCHIVEDALERTHISTORY_ID" PRIMARY KEY ("Id")
  USING INDEX ENABLE;
--  Constraints for Table attachmenttype


ALTER TABLE "attachmenttype" ADD CONSTRAINT "PK_ATTACHMENTTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table attribute

ALTER TABLE 
  "attribute" 
ADD 
  CONSTRAINT "PK_ATTRIBUTE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table attributetype

ALTER TABLE 
  "attributetype" 
ADD 
  CONSTRAINT "PK_ATTRIBUTETYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table backendcertificate
--------------------------------------------------------

ALTER TABLE "backendcertificate" ADD CONSTRAINT "PK_BACKENDCERTIFICATE_ID" PRIMARY KEY ("id")
  USING INDEX  ENABLE;
  
--  Constraints for Table backendidentifier

ALTER TABLE 
  "backendidentifier" 
ADD 
  CONSTRAINT "PK_BACKENDIDENTIFIER_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table bank

ALTER TABLE 
  "bank" 
ADD 
  CONSTRAINT "PK_BANK_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table bankbranch

ALTER TABLE 
  "bankbranch" 
ADD 
  CONSTRAINT "PK_BANKBRANCH_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table bankcommunication

ALTER TABLE 
  "bankcommunication" 
ADD 
  CONSTRAINT "PK_BANKCOMMUNICATION_TYPE_ID" PRIMARY KEY ("Type_id",
"sequence")
  USING INDEX ENABLE;
--  Constraints for Table banner

ALTER TABLE 
  "banner" 
ADD 
  CONSTRAINT "PK_BANNER_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table batchalertdefinition

ALTER TABLE 
  "batchalertdefinition" 
ADD 
  CONSTRAINT "PK_BATCHALERTDEFINITION_ALERTTYPE" PRIMARY KEY ("alertType")
  USING INDEX ENABLE;
--  Constraints for Table batchalertobject

ALTER TABLE 
  "batchalertobject" 
ADD 
  CONSTRAINT "PK_BATCHALERTOBJECT_OBJECTTYPE" PRIMARY KEY ("objectType")
  USING INDEX ENABLE;
--  Constraints for Table bbactedrequest

ALTER TABLE 
  "bbactedrequest" 
ADD 
  CONSTRAINT "PK_BBACTEDREQUEST_APPROVALID" PRIMARY KEY ("approvalId")
  USING INDEX ENABLE;
--  Constraints for Table bbrequest

ALTER TABLE 
  "bbrequest" 
ADD 
  CONSTRAINT "PK_BBREQUEST_REQUESTID" PRIMARY KEY ("requestId")
  USING INDEX ENABLE;
--  Constraints for Table bbtaxsubtype

ALTER TABLE 
  "bbtaxsubtype" 
ADD 
  CONSTRAINT "PK_BBTAXSUBTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table bbtaxtype

ALTER TABLE 
  "bbtaxtype" 
ADD 
  CONSTRAINT "PK_BBTAXTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table bbtemplate

ALTER TABLE 
  "bbtemplate" 
ADD 
  CONSTRAINT "PK_BBTEMPLATE_TEMPLATEID" PRIMARY KEY ("templateId")
  USING INDEX ENABLE;
--  Constraints for Table bbtemplaterecord

ALTER TABLE 
  "bbtemplaterecord" 
ADD 
  CONSTRAINT "PK_BBTEMPLATERECORD_TEMPLATERECORD_ID" PRIMARY KEY ("templateRecord_id")
  USING INDEX ENABLE;
--  Constraints for Table bbtemplaterequesttype

ALTER TABLE 
  "bbtemplaterequesttype" 
ADD 
  CONSTRAINT "PK_BBTEMPLATEREQUESTTYPE_TEMPLATEREQUESTTYPE_ID" PRIMARY KEY ("templateRequestType_id")
  USING INDEX ENABLE;
--  Constraints for Table bbtemplatesubrecord

ALTER TABLE 
  "bbtemplatesubrecord" 
ADD 
  CONSTRAINT "PK_BBTEMPLATESUBRECORD_TEMPLATESUBRECORD_ID" PRIMARY KEY ("templateSubRecord_id")
  USING INDEX ENABLE;
--  Constraints for Table bbtemplatetype

ALTER TABLE 
  "bbtemplatetype" 
ADD 
  CONSTRAINT "PK_BBTEMPLATETYPE_TEMPLATETYPE_ID" PRIMARY KEY ("templateType_id")
  USING INDEX ENABLE;
--  Constraints for Table bbtransactiontype

ALTER TABLE 
  "bbtransactiontype" 
ADD 
  CONSTRAINT "PK_BBTRANSACTIONTYPE_TRANSACTIONTYPE_ID" PRIMARY KEY ("transactionType_id")
  USING INDEX ENABLE;
--  Constraints for Table bill

ALTER TABLE 
  "bill" 
ADD 
  CONSTRAINT "PK_BILL_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table billercategory

ALTER TABLE 
  "billercategory" 
ADD 
  CONSTRAINT "PK_BILLERCATEGORY_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table billercompany

ALTER TABLE 
  "billercompany" 
ADD 
  CONSTRAINT "PK_BILLERCOMPANY_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table billermaster

ALTER TABLE 
  "billermaster" 
ADD 
  CONSTRAINT "PK_BILLERMASTER_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table branchtype

ALTER TABLE 
  "branchtype" 
ADD 
  CONSTRAINT "PK_BRANCHTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table budget

ALTER TABLE 
  "budget" 
ADD 
  CONSTRAINT "PK_BUDGET_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table bulkwirefileformattype

ALTER TABLE 
  "bulkwirefileformattype" 
ADD 
  CONSTRAINT "PK_BULKWIREFILEFORMATTYPE_BULKWIRESFILEFORMATTYPECODE" PRIMARY KEY ("bulkWiresFileFormatTypeCode")
  USING INDEX ENABLE;
--  Constraints for Table bulkwirefilelineitems

ALTER TABLE 
  "bulkwirefilelineitems" 
ADD 
  CONSTRAINT "PK_BULKWIREFILELINEITEMS_BULKWIREFILELINEITEMID" PRIMARY KEY ("bulkWireFileLineItemID")
  USING INDEX ENABLE;
--  Constraints for Table bulkwirefiles

ALTER TABLE 
  "bulkwirefiles" 
ADD 
  CONSTRAINT "PK_BULKWIREFILES_BULKWIREFILEID" PRIMARY KEY ("bulkWireFileID")
  USING INDEX ENABLE;
--  Constraints for Table bulkwirefiletransactdetails

ALTER TABLE 
  "bulkwirefiletransactdetails" 
ADD 
  CONSTRAINT "PK_BULKWIREFILETRANSACTDETAILS_BULKWIRETRANSACTIONID" PRIMARY KEY ("bulkWireTransactionID")
  USING INDEX ENABLE;
--  Constraints for Table bulkwiresamplefile

ALTER TABLE 
  "bulkwiresamplefile" 
ADD 
  CONSTRAINT "PK_BULKWIRESAMPLEFILE_BULKWIRESAMPLEFILEID" PRIMARY KEY ("bulkWireSampleFileID")
  USING INDEX ENABLE;
--  Constraints for Table bulkwiretemplate

ALTER TABLE 
  "bulkwiretemplate" 
ADD 
  CONSTRAINT "PK_BULKWIRETEMPLATE_BULKWIRETEMPLATEID" PRIMARY KEY ("bulkWireTemplateID")
  USING INDEX ENABLE;
--  Constraints for Table bulkwiretemplatelineitems

ALTER TABLE 
  "bulkwiretemplatelineitems" 
ADD 
  CONSTRAINT "PK_BULKWIRETEMPLATELINEITEMS_BULKWIRETEMPLATELINEITEMID" PRIMARY KEY ("bulkWireTemplateLineItemID")
  USING INDEX ENABLE;
--  Constraints for Table bulkwiretemplatetransactdetails

ALTER TABLE 
  "bulkwiretemplatetransactdetails" 
ADD 
  CONSTRAINT "PK_BULKWIRETEMPLATETRANSACTDETAILS_BULKWIRETRANSACTIONID" PRIMARY KEY ("bulkWireTransactionID")
  USING INDEX ENABLE;
--  Constraints for Table card

ALTER TABLE 
  "card" 
ADD 
  CONSTRAINT "PK_CARD_ID" PRIMARY KEY ("Id")
  USING INDEX ENABLE;
--  Constraints for Table cardaccountrequest

ALTER TABLE 
  "cardaccountrequest" 
ADD 
  CONSTRAINT "PK_CARDACCOUNTREQUEST_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table cardaccountrequesttype

ALTER TABLE 
  "cardaccountrequesttype" 
ADD 
  CONSTRAINT "PK_CARDACCOUNTREQUESTTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table cardproducts

ALTER TABLE 
  "cardproducts" 
ADD 
  CONSTRAINT "PK__CARDPROD__2D10D16AEA2CBBEC" PRIMARY KEY ("productId")
  USING INDEX ENABLE;
--  Constraints for Table cardproducttype

ALTER TABLE 
  "cardproducttype" 
ADD 
  CONSTRAINT "PK__CARDPROD__3213E83FDB8A9B13" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table cardstatements

ALTER TABLE 
  "cardstatements" 
ADD 
  CONSTRAINT "PK_CARDSTATEMENTS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table cardtransaction

ALTER TABLE 
  "cardtransaction" 
ADD 
  CONSTRAINT "PK_CARDTRANSACTION_TRANSACTIONREFERENCENUMBER" PRIMARY KEY ("transactionReferenceNumber")
  USING INDEX ENABLE;
--  Constraints for Table category

ALTER TABLE 
  "category" 
ADD 
  CONSTRAINT "PK_CATEGORY_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table channel

ALTER TABLE 
  "channel" 
ADD 
  CONSTRAINT "PK_CHANNEL_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table channeltext

ALTER TABLE 
  "channeltext" 
ADD 
  CONSTRAINT "PK_CHANNELTEXT_CHANNELID" PRIMARY KEY ("channelID",
"LanguageCode")
  USING INDEX ENABLE;
--  Constraints for Table check

ALTER TABLE 
  "check" 
ADD 
  CONSTRAINT "PK_CHECK_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table checkorder

ALTER TABLE 
  "checkorder" 
ADD 
  CONSTRAINT "PK_CHECKORDER_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table city

ALTER TABLE 
  "city" 
ADD 
  CONSTRAINT "PK_CITY_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table communicationtemplate

ALTER TABLE 
  "communicationtemplate" 
ADD 
  CONSTRAINT "PK_COMMUNICATIONTEMPLATE_ID" PRIMARY KEY ("Id")
  USING INDEX ENABLE;
--  Constraints for Table communicationtype

ALTER TABLE 
  "communicationtype" 
ADD 
  CONSTRAINT "PK_COMMUNICATIONTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table configurationmasters

ALTER TABLE 
  "configurationmasters" 
ADD 
  CONSTRAINT "PK_CONFIGURATIONMASTERS_BUNDLE_ID" PRIMARY KEY ("bundle_id")
  USING INDEX ENABLE;
--  Constraints for Table contract

ALTER TABLE 
  "contract" 
ADD 
  CONSTRAINT "NAME_UNIQUE" UNIQUE ("name")
  USING INDEX ENABLE;

ALTER TABLE 
  "contract" 
ADD 
  CONSTRAINT "PK_CONTRACT_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table contractaccounts

ALTER TABLE 
  "contractaccounts" 
ADD 
  CONSTRAINT "ACCOUNTID_UNIQUE" UNIQUE ("accountId")
  USING INDEX ENABLE;

ALTER TABLE 
  "contractaccounts" 
ADD 
  CONSTRAINT "PK_CONTRACTACCOUNTS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table contractactionlimit

ALTER TABLE 
  "contractactionlimit" 
ADD 
  CONSTRAINT "PK_CONTRACTACTIONLIMIT_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table contractaddress

ALTER TABLE 
  "contractaddress" 
ADD 
  CONSTRAINT "PK_CONTRACTADDRESS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table contractcommunication

ALTER TABLE 
  "contractcommunication" 
ADD 
  CONSTRAINT "PK_CONTRACTCOMMUNICATION_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table contractcorecustomers

ALTER TABLE 
  "contractcorecustomers" 
ADD 
  CONSTRAINT "PK_CONTRACTCORECUSTOMERS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table contractcustomers

ALTER TABLE 
  "contractcustomers" 
ADD 
  CONSTRAINT "PK_CONTRACTCUSTOMERS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table contractfeatures

ALTER TABLE 
  "contractfeatures" 
ADD 
  CONSTRAINT "PK_CONTRACTFEATURES_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table coremembership

ALTER TABLE 
  "coremembership" 
ADD 
  CONSTRAINT "PK_COREMEMBERSHIP_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table country

ALTER TABLE 
  "country" 
ADD 
  CONSTRAINT "PK_COUNTRY_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table countrybasecurrency

ALTER TABLE 
  "countrybasecurrency" 
ADD 
  CONSTRAINT "PK__COUNTRYB__3213E83F6A071F64" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table credentialchecker

ALTER TABLE 
  "credentialchecker" 
ADD 
  CONSTRAINT "PK_CREDENTIALCHECKER_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table currency

ALTER TABLE 
  "currency" 
ADD 
  CONSTRAINT "PK_CURRENCY_CODE" PRIMARY KEY ("code")
  USING INDEX ENABLE;
--  Constraints for Table currencymarketrates

ALTER TABLE 
  "currencymarketrates" 
ADD 
  CONSTRAINT "PK__CURRENCY__3213E83FB68136C6" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table customer

ALTER TABLE 
  "customer" 
ADD 
  CONSTRAINT "CUSTOMER$USERNAME_UNIQUE" UNIQUE ("UserName")
  USING INDEX ENABLE;

ALTER TABLE 
  "customer" 
ADD 
  CONSTRAINT "PK_CUSTOMER_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table customeraccounts

ALTER TABLE 
  "customeraccounts" 
ADD 
  CONSTRAINT "PK_CUSTOMERACCOUNTS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table customeraddress

--  Constraints for Table customeraction
--------------------------------------------------------

ALTER TABLE "customeraction" ADD CONSTRAINT "PK_CUSTOMERACTION_ID" PRIMARY KEY ("id", "synctimestamp")
  USING INDEX  ENABLE;
ALTER TABLE "customeraction" ADD CONSTRAINT "UNIQUE_CUSTOMERACTIONLIMIT" UNIQUE ("Customer_id", "Action_id", "Account_id", "LimitType_id", "coreCustomerId")
  USING INDEX  ENABLE;


ALTER TABLE 
  "customeraddress" 
ADD 
  CONSTRAINT "PK_CUSTOMERADDRESS_ADDRESS_ID" PRIMARY KEY ("Address_id")
  USING INDEX ENABLE;
--  Constraints for Table customeralertcategorychannel

ALTER TABLE 
  "customeralertcategorychannel" 
ADD 
  CONSTRAINT "PK_CUSTOMERALERTCATEGORYCHANNEL_CUSTOMER_ID" PRIMARY KEY (
    "Customer_id",
"AlertCategoryId", 
    "ChannelId",
"AccountId",
"AccountType"
  )
  USING INDEX ENABLE;
--  Constraints for Table customeralertchannel

ALTER TABLE 
  "customeralertchannel" 
ADD 
  CONSTRAINT "PK__CUSTOMER__ACAA01E599E9178E" PRIMARY KEY (
    "customerId",
"alertCategoryId", 
    "alertTypeId",
"alertSubTypeId", 
    "channelId",
"accountId",
"accountType"
  )
  USING INDEX ENABLE;
--  Constraints for Table customeralertentitlement

ALTER TABLE 
  "customeralertentitlement" 
ADD 
  CONSTRAINT "PK_CUSTOMERALERTENTITLEMENT_CUSTOMER_ID" PRIMARY KEY ("Customer_id",
"Alert_id")
  USING INDEX ENABLE;
--  Constraints for Table customeralertfrequency

ALTER TABLE 
  "customeralertfrequency" 
ADD 
  CONSTRAINT "PK__CUSTOMER__BD9BACB61BA6B159" PRIMARY KEY (
    "customerId",
"alertCategoryId", 
    "alertTypeId",
"alertSubTypeId", 
    "accountId",
"accountType"
  )
  USING INDEX ENABLE;
--  Constraints for Table customeralertswitch

ALTER TABLE 
  "customeralertswitch" 
ADD 
  CONSTRAINT "PK_CUSTOMERALERTSWITCH_CUSTOMER_ID" PRIMARY KEY (
    "Customer_id",
"AccountID",
"AlertCategoryId", 
    "AccountType"
  )
  USING INDEX ENABLE;
--  Constraints for Table customercommunication

ALTER TABLE 
  "customercommunication" 
ADD 
  CONSTRAINT "PK_CUSTOMERCOMMUNICATION_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
  
--  Constraints for Table customerfile

ALTER TABLE "customerfile" ADD CONSTRAINT "PK_CUSTOMERFILE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table customerdevice

ALTER TABLE 
  "customerdevice" 
ADD 
  CONSTRAINT "PK_CUSTOMERDEVICE_ID" PRIMARY KEY ("id",
"Customer_id")
  USING INDEX ENABLE;
--  Constraints for Table customerentitlement

ALTER TABLE 
  "customerentitlement" 
ADD 
  CONSTRAINT "PK_CUSTOMERENTITLEMENT_CUSTOMER_ID" PRIMARY KEY ("Customer_id",
"Service_id")
  USING INDEX ENABLE;
--  Constraints for Table customerexpense

ALTER TABLE 
  "customerexpense" 
ADD 
  CONSTRAINT "PK_CUSTOMEREXPENSE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table customerflagstatus

ALTER TABLE 
  "customerflagstatus" 
ADD 
  CONSTRAINT "PK_CUSTOMERFLAGSTATUS_CUSTOMER_ID" PRIMARY KEY ("Customer_id",
"Status_id")
  USING INDEX ENABLE;
--  Constraints for Table customergroup
--  Constraints for Table customerimage

ALTER TABLE 
  "customerimage" 
ADD 
  CONSTRAINT "PK_CUSTOMERIMAGE_CUSTOMER_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table customerlimitgrouplimits
ALTER TABLE 
  "customerlimitgrouplimits" 
ADD 
  CONSTRAINT PK_CUSTOMERLIMITGROUPLIMITS_ID PRIMARY KEY ("id", "synctimestamp") 
  USING INDEX  ENABLE;
--  Constraints for Table customerpreference

ALTER TABLE 
  "customerpreference" 
ADD 
  CONSTRAINT "PK_CUSTOMERPREFERENCE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table customerpreviouspasswords

ALTER TABLE 
  "customerpreviouspasswords" 
ADD 
  CONSTRAINT "PK_CUSTOMERPREVIOUSPASSWORDS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table customerproduct

ALTER TABLE 
  "customerproduct" 
ADD 
  CONSTRAINT "PK_CUSTOMERPRODUCT_CUSTOMER_ID" PRIMARY KEY ("Customer_id",
"Product_id")
  USING INDEX ENABLE;
--  Constraints for Table customerrequest

ALTER TABLE 
  "customerrequest" 
ADD 
  CONSTRAINT "PK_CUSTOMERREQUEST_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table customersecurityquestions

ALTER TABLE 
  "customersecurityquestions" 
ADD 
  CONSTRAINT "PK_CUSTOMERSECURITYQUESTIONS_CUSTOMER_ID" PRIMARY KEY (
    "Customer_id",
"SecurityQuestion_id"
  )
  USING INDEX ENABLE;
--  Constraints for Table customertermsandconditions

ALTER TABLE 
  "customertermsandconditions" 
ADD 
  CONSTRAINT "PK_CUSTOMERTERMSANDCONDITIONS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table customertype

ALTER TABLE 
  "customertype" 
ADD 
  CONSTRAINT "PK_CUSTOMERTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table customertypeconfig

ALTER TABLE 
  "customertypeconfig" 
ADD 
  CONSTRAINT "PK_CUSTOMERTYPECONFIG_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;


ALTER TABLE 
  "customerviewalertconfiguration" 
ADD 
  CONSTRAINT "PK__CUSTOMER__3213E83FAD4EA884" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table customroleaccounts

ALTER TABLE 
  "customroleaccounts" 
ADD 
  CONSTRAINT "PK__CUSTOMRO__3213E83FD181A183" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table dbpconfig

ALTER TABLE 
  "dbpconfig" 
ADD 
  CONSTRAINT "PK_DBPCONFIG_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table dbxalertcategory

ALTER TABLE 
  "dbxalertcategory" 
ADD 
  CONSTRAINT "PK_DBXALERTCATEGORY_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table dbxalertcategorytext

ALTER TABLE 
  "dbxalertcategorytext" 
ADD 
  CONSTRAINT "PK_DBXALERTCATEGORYTEXT_ALERTCATEGORYID" PRIMARY KEY (
    "AlertCategoryId",
"LanguageCode"
  )
  USING INDEX ENABLE;
--  Constraints for Table dbxalerttype

ALTER TABLE 
  "dbxalerttype" 
ADD 
  CONSTRAINT "PK_DBXALERTTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table dbxalerttypetext

ALTER TABLE 
  "dbxalerttypetext" 
ADD 
  CONSTRAINT "PK_DBXALERTTYPETEXT_ALERTTYPEID" PRIMARY KEY ("AlertTypeId",
"LanguageCode")
  USING INDEX ENABLE;
--  Constraints for Table dbxcustomeralertentitlement

ALTER TABLE 
  "dbxcustomeralertentitlement" 
ADD 
  CONSTRAINT "PK__DBXCUSTO__D85DA25764F1CB57" PRIMARY KEY (
    "Customer_id",
"AlertTypeId",
"AccountId", 
    "AccountType",
"alertCategoryId", 
    "alertSubTypeId"
  )
  USING INDEX ENABLE;
--  Constraints for Table decisionresult

ALTER TABLE 
  "decisionresult" 
ADD 
  CONSTRAINT "PK_DECISIONRESULT_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table deviceregistration

ALTER TABLE 
  "deviceregistration" 
ADD 
  CONSTRAINT "DEVICEREGISTRATION$UNIQUE_INDEX" UNIQUE ("User_id",
"DeviceId")
  USING INDEX ENABLE;

ALTER TABLE 
  "deviceregistration" 
ADD 
  CONSTRAINT "PK_DEVICEREGISTRATION_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table digitalprofile

ALTER TABLE 
  "digitalprofile" 
ADD 
  CONSTRAINT "PK_DIGITALPROFILE_ID" PRIMARY KEY ("Id")
  USING INDEX ENABLE;
--  Constraints for Table dmaddinteractions

ALTER TABLE 
  "dmaddinteractions" 
ADD 
  CONSTRAINT "PK_DMADDINTERACTIONS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table dmadvertisements

ALTER TABLE 
  "dmadvertisements" 
ADD 
  CONSTRAINT "PK_DMADVERTISEMENTS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table emailtemplates

ALTER TABLE 
  "emailtemplates" 
ADD 
  CONSTRAINT "PK_EMAILTEMPLATES_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table employementdetails

ALTER TABLE 
  "employementdetails" 
ADD 
  CONSTRAINT "PK_EMPLOYEMENTDETAILS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table event

ALTER TABLE 
  "event" 
ADD 
  CONSTRAINT "PK_EVENT_EVENT_ID" PRIMARY KEY ("Event_id")
  USING INDEX ENABLE;
--  Constraints for Table eventactivitytype

ALTER TABLE 
  "eventactivitytype" 
ADD 
  CONSTRAINT "PK_EVENTACTIVITYTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table eventconsumer

ALTER TABLE 
  "eventconsumer" 
ADD 
  CONSTRAINT "PK_EVENTCONSUMER_SERVICEID" PRIMARY KEY ("ServiceId",
"OperationId")
  USING INDEX ENABLE;
--  Constraints for Table eventconsumertypes

ALTER TABLE 
  "eventconsumertypes" 
ADD 
  CONSTRAINT "PK_EVENTCONSUMERTYPES_SERVICEID" PRIMARY KEY (
    "ServiceId",
"OperationId",
"EventType"
  )
  USING INDEX ENABLE;
--  Constraints for Table eventsubtype

ALTER TABLE 
  "eventsubtype" 
ADD 
  CONSTRAINT "PK_EVENTSUBTYPE_ID" PRIMARY KEY ("id",
"eventtypeid")
  USING INDEX ENABLE;
--  Constraints for Table eventtopicconfiguration

ALTER TABLE 
  "eventtopicconfiguration" 
ADD 
  CONSTRAINT "PK_EVENTTOPICCONFIGURATION_EVENTCODE" PRIMARY KEY ("eventCode",
"topic")
  USING INDEX ENABLE;
--  Constraints for Table eventtype

ALTER TABLE 
  "eventtype" 
ADD 
  CONSTRAINT "PK_EVENTTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table exchangerates

ALTER TABLE 
  "exchangerates" 
ADD 
  CONSTRAINT "PK_EXCHANGERATES_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table excludedcontractaccounts

ALTER TABLE 
  "excludedcontractaccounts" 
ADD 
  CONSTRAINT "EXCLUDEDCONTRACTACCOUNTS_ACCOUNTID_UNIQUE" UNIQUE ("accountId")
  USING INDEX ENABLE;

ALTER TABLE 
  "excludedcontractaccounts" 
ADD 
  CONSTRAINT "PK_EXCLUDEDCONTRACTACCOUNTS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table excludedcustomeraccounts

ALTER TABLE 
  "excludedcustomeraccounts" 
ADD 
  CONSTRAINT "PK_EXCLUDEDCUSTOMERACCOUNTS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table excludedcustomeraction

ALTER TABLE 
  "excludedcustomeraction" 
ADD 
  CONSTRAINT "PK_EXCLUDEDCUSTOMERACTION_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table excludedcustomroleaccounts

ALTER TABLE 
  "excludedcustomroleaccounts" 
ADD 
  CONSTRAINT "PK__CUSTOMRO__3213E83FC1DF4F66" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table expensecategory

ALTER TABLE 
  "expensecategory" 
ADD 
  CONSTRAINT "PK_EXPENSECATEGORY_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table expenseperiod

ALTER TABLE 
  "expenseperiod" 
ADD 
  CONSTRAINT "PK_EXPENSEPERIOD_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table externalaccount

ALTER TABLE 
  "externalaccount" 
ADD 
  CONSTRAINT "PK_EXTERNALACCOUNT_ID" PRIMARY KEY ("Id")
  USING INDEX ENABLE;
--  Constraints for Table externalbank

ALTER TABLE 
  "externalbank" 
ADD 
  CONSTRAINT "PK_EXTERNALBANK_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table externalbankidentity

ALTER TABLE 
  "externalbankidentity" 
ADD 
  CONSTRAINT "PK_EXTERNALBANKIDENTITY_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table favouriteinstruments

ALTER TABLE 
  "favouriteinstruments" 
ADD 
  CONSTRAINT "FAVOINSTRUMENTS_USERID" UNIQUE ("userId")
  USING INDEX ENABLE;

ALTER TABLE 
  "favouriteinstruments" 
ADD 
  CONSTRAINT "PK__FAVOURIT__3213E83FF73F5728" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table feedback

ALTER TABLE 
  "feedback" 
ADD 
  CONSTRAINT "PK_FEEDBACK_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table feedbackstatus

ALTER TABLE 
  "feedbackstatus" 
ADD 
  CONSTRAINT "PK_FEEDBACKSTATUS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table groupentitlement

ALTER TABLE 
  "groupentitlement" 
ADD 
  CONSTRAINT "PK_GROUPENTITLEMENT_GROUP_ID" PRIMARY KEY ("Group_id",
"Service_id")
  USING INDEX ENABLE;
--  Constraints for Table holidays

ALTER TABLE 
  "holidays" 
ADD 
  CONSTRAINT "PK_HOLIDAYS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table iban

ALTER TABLE 
  "iban" 
ADD 
  CONSTRAINT "PK_IBAN_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table idmconfiguration

ALTER TABLE 
  "idmconfiguration" 
ADD 
  CONSTRAINT "PK_IDMCONFIGURATION_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table informationcontent

ALTER TABLE 
  "informationcontent" 
ADD 
  CONSTRAINT "PK_INFORMATIONCONTENT_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table interestrates

ALTER TABLE 
  "interestrates" 
ADD 
  CONSTRAINT "PK_INTERESTRATES_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table loanschedule

ALTER TABLE 
  "loanschedule" 
ADD 
  CONSTRAINT "PK__LOANSCHE__3213E83F8BDC9D7C" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table locale

ALTER TABLE 
  "locale" 
ADD 
  CONSTRAINT "PK_LOCALE_CODE" PRIMARY KEY ("Code")
  USING INDEX ENABLE;
--  Constraints for Table location

ALTER TABLE 
  "location" 
ADD 
  CONSTRAINT "LOCATION$CODE_UNIQUE" UNIQUE ("Code")
  USING INDEX ENABLE;

ALTER TABLE 
  "location" 
ADD 
  CONSTRAINT "PK_LOCATION_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table locationlanguage

ALTER TABLE 
  "locationlanguage" 
ADD 
  CONSTRAINT "PK_LOCATIONLANGUAGE_ID" PRIMARY KEY ("id",
"Location_id")
  USING INDEX ENABLE;
--  Constraints for Table locationtype

ALTER TABLE 
  "locationtype" 
ADD 
  CONSTRAINT "PK_LOCATIONTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table lockobjects

ALTER TABLE 
  "lockobjects" 
ADD 
  CONSTRAINT "PK_LOCKOBJECTS_OBJECTID" PRIMARY KEY ("ObjectId",
"User",
"ExternalId")
  USING INDEX ENABLE;
--  Constraints for Table market

ALTER TABLE 
  "market" 
ADD 
  CONSTRAINT "PK__MARKET__3213E83F4AE226D1" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table media

ALTER TABLE 
  "media" 
ADD 
  CONSTRAINT "PK_MEDIA_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table membereligibility

ALTER TABLE 
  "membereligibility" 
ADD 
  CONSTRAINT "PK_MEMBERELIGIBILITY_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table membergroup

ALTER TABLE 
  "membergroup" 
ADD 
  CONSTRAINT "PK_MEMBERGROUP_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table membership

ALTER TABLE 
  "membership" 
ADD 
  CONSTRAINT "PK_MEMBERSHIP_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table membershipaccounts

ALTER TABLE 
  "membershipaccounts" 
ADD 
  CONSTRAINT "PK_MEMBERSHIPACCOUNTS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table membershipowner

ALTER TABLE 
  "membershipowner" 
ADD 
  CONSTRAINT "PK_MEMBERSHIPOWNER_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table membershiprelation

ALTER TABLE 
  "membershiprelation" 
ADD 
  CONSTRAINT "PK_MEMBERSHIPRELATION_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table message

ALTER TABLE 
  "message" 
ADD 
  CONSTRAINT "PK_MESSAGE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table messageattachment

ALTER TABLE 
  "messageattachment" 
ADD 
  CONSTRAINT "PK_MESSAGEATTACHMENT_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table messagecategory

ALTER TABLE 
  "messagecategory" 
ADD 
  CONSTRAINT "PK_MESSAGECATEGORY_ID" PRIMARY KEY ("Id")
  USING INDEX ENABLE;
--  Constraints for Table messagesubcategory

ALTER TABLE 
  "messagesubcategory" 
ADD 
  CONSTRAINT "PK_MESSAGESUBCATEGORY_ID" PRIMARY KEY ("Id")
  USING INDEX ENABLE;
--  Constraints for Table messagetype

ALTER TABLE 
  "messagetype" 
ADD 
  CONSTRAINT "PK_MESSAGETYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table mfaservice

ALTER TABLE 
  "mfaservice" 
ADD 
  CONSTRAINT "PK_MFASERVICE_SERVICEKEY" PRIMARY KEY ("serviceKey")
  USING INDEX ENABLE;
--  Constraints for Table mfaserviceconfig

ALTER TABLE 
  "mfaserviceconfig" 
ADD 
  CONSTRAINT "PK_MFASERVICECONFIG_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table module

ALTER TABLE 
  "module" 
ADD 
  CONSTRAINT "PK_MODULE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table newaccount

ALTER TABLE 
  "newaccount" 
ADD 
  CONSTRAINT "PK_NEWACCOUNT_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table newuser

ALTER TABLE 
  "newuser" 
ADD 
  CONSTRAINT "NEWUSER$USERNAME" UNIQUE ("userName")
  USING INDEX ENABLE;

ALTER TABLE 
  "newuser" 
ADD 
  CONSTRAINT "PK_NEWUSER_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table notification

ALTER TABLE 
  "notification" 
ADD 
  CONSTRAINT "PK_NOTIFICATION_NOTIFICATIONID" PRIMARY KEY ("notificationId")
  USING INDEX ENABLE;
--  Constraints for Table notificationcardinfo

ALTER TABLE 
  "notificationcardinfo" 
ADD 
  CONSTRAINT "PK_NOTIFICATIONCARDINFO_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table numberrange

ALTER TABLE 
  "numberrange" 
ADD 
  CONSTRAINT "PK_NUMBERRANGE_OBJECTID" PRIMARY KEY ("ObjectId")
  USING INDEX ENABLE;
--  Constraints for Table operatinghours

ALTER TABLE 
  "operatinghours" 
ADD 
  CONSTRAINT "PK_OPERATINGHOURS_ID" PRIMARY KEY ("id",
"Location_id")
  USING INDEX ENABLE;
--  Constraints for Table organisation

ALTER TABLE 
  "organisation" 
ADD 
  CONSTRAINT "ORGANISATION$NAME_UNIQUE" UNIQUE ("Name")
  USING INDEX ENABLE;

ALTER TABLE 
  "organisation" 
ADD 
  CONSTRAINT "PK_ORGANISATION_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table organisationaccounts

ALTER TABLE 
  "organisationaccounts" 
ADD 
  CONSTRAINT "ORGANISATIONACCOUNTS$UNIQUE_ACCOUNTID_TYPEID" UNIQUE ("Account_id",
"TypeID")
  USING INDEX ENABLE;

ALTER TABLE 
  "organisationaccounts" 
ADD 
  CONSTRAINT "PK_ORGANISATIONACCOUNTS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table organisationaddress

ALTER TABLE 
  "organisationaddress" 
ADD 
  CONSTRAINT "PK_ORGANISATIONADDRESS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table organisationcommunication

ALTER TABLE 
  "organisationcommunication" 
ADD 
  CONSTRAINT "PK_ORGANISATIONCOMMUNICATION_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table organisationemployees

ALTER TABLE 
  "organisationemployees" 
ADD 
  CONSTRAINT "PK_ORGANISATIONEMPLOYEES_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table organisationmembership

ALTER TABLE 
  "organisationmembership" 
ADD 
  CONSTRAINT "PK_ORGANISATIONMEMBERSHIP_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table organisationowner

ALTER TABLE 
  "organisationowner" 
ADD 
  CONSTRAINT "PK_ORGANISATIONOWNER_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table organisationtype

ALTER TABLE 
  "organisationtype" 
ADD 
  CONSTRAINT "PK_ORGANISATIONTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table othersourceofincome

ALTER TABLE 
  "othersourceofincome" 
ADD 
  CONSTRAINT "PK_OTHERSOURCEOFINCOME_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table otpcount

ALTER TABLE 
  "otpcount" 
ADD 
  CONSTRAINT "PK_OTPCOUNT_KEY" PRIMARY KEY ("key")
  USING INDEX ENABLE;
--  Constraints for Table p2pregistration

ALTER TABLE 
  "p2pregistration" 
ADD 
  CONSTRAINT "PK_P2PREGISTRATION_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table passwordpolicy
--------------------------------------------------------

ALTER TABLE "passwordpolicy" ADD CONSTRAINT "PK_PASSWORDPOLICY_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table payee

ALTER TABLE 
  "payee" 
ADD 
  CONSTRAINT "PK_PAYEE_ID" PRIMARY KEY ("Id")
  USING INDEX ENABLE;
--  Constraints for Table payeeaddress

ALTER TABLE 
  "payeeaddress" 
ADD 
  CONSTRAINT "PK_PAYEEADDRESS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table payeetype

ALTER TABLE 
  "payeetype" 
ADD 
  CONSTRAINT "PK_PAYEETYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table paymentfiles

ALTER TABLE 
  "paymentfiles" 
ADD 
  CONSTRAINT "PK__PAYMENTF__A03ECD0C1C242188" PRIMARY KEY ("paymentFileID")
  USING INDEX ENABLE;
--  Constraints for Table payperson

ALTER TABLE 
  "payperson" 
ADD 
  CONSTRAINT "PK_PAYPERSON_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table period

ALTER TABLE 
  "period" 
ADD 
  CONSTRAINT "PERIOD$NAME_UNIQUE" UNIQUE ("Name")
  USING INDEX ENABLE;

ALTER TABLE 
  "period" 
ADD 
  CONSTRAINT "PK_PERIOD_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table permission

ALTER TABLE 
  "permission" 
ADD 
  CONSTRAINT "PK_PERMISSION_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table permissiontype
ALTER TABLE "permissiontype" ADD CONSTRAINT "PK_PERMISSIONTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table pfmbargraph

ALTER TABLE 
  "pfmbargraph" 
ADD 
  CONSTRAINT "PK_PFMBARGRAPH_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table pfmbudgetsnapshot

ALTER TABLE 
  "pfmbudgetsnapshot" 
ADD 
  CONSTRAINT "PK_PFMBUDGETSNAPSHOT_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table pfmcategory

ALTER TABLE 
  "pfmcategory" 
ADD 
  CONSTRAINT "PK_PFMCATEGORY_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table pfmmonth

ALTER TABLE 
  "pfmmonth" 
ADD 
  CONSTRAINT "PK_PFMMONTH_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table pfmpiechart

ALTER TABLE 
  "pfmpiechart" 
ADD 
  CONSTRAINT "PK_PFMPIECHART_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table pfmtransactions

ALTER TABLE 
  "pfmtransactions" 
ADD 
  CONSTRAINT "PK_PFMTRANSACTIONS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table phone

ALTER TABLE 
  "phone" 
ADD 
  CONSTRAINT "PK_PHONE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table popularcurrencies

ALTER TABLE 
  "popularcurrencies" 
ADD 
  CONSTRAINT "PK__POPULARC__3213E83F356A6779" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table preferredaccount

ALTER TABLE 
  "preferredaccount" 
ADD 
  CONSTRAINT "PK_PREFERREDACCOUNT_TYPE_ID" PRIMARY KEY ("Type_id")
  USING INDEX ENABLE;
--  Constraints for Table product

ALTER TABLE 
  "product" 
ADD 
  CONSTRAINT "PK_PRODUCT_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;

ALTER TABLE 
  "product" 
ADD 
  CONSTRAINT "PRODUCT$MARKETINGSTATEID_UNIQUE" UNIQUE ("MarketingStateId")
  USING INDEX ENABLE;
--  Constraints for Table productdetail

ALTER TABLE 
  "productdetail" 
ADD 
  CONSTRAINT "PK_PRODUCTDETAIL_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table producttype

ALTER TABLE 
  "producttype" 
ADD 
  CONSTRAINT "PK_PRODUCTTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table querycoborrower

ALTER TABLE 
  "querycoborrower" 
ADD 
  CONSTRAINT "PK_QUERYCOBORROWER_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table queryresponseconsent

ALTER TABLE 
  "queryresponseconsent" 
ADD 
  CONSTRAINT "PK_QUERYRESPONSECONSENT_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table recentcurrencies

ALTER TABLE 
  "recentcurrencies" 
ADD 
  CONSTRAINT "PK__RECENTCU__3213E83F4BEAFDFB" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table region

ALTER TABLE 
  "region" 
ADD 
  CONSTRAINT "PK_REGION_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table requestcategory

ALTER TABLE 
  "requestcategory" 
ADD 
  CONSTRAINT "PK_REQUESTCATEGORY_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table requestmessage

ALTER TABLE 
  "requestmessage" 
ADD 
  CONSTRAINT "PK_REQUESTMESSAGE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table role

ALTER TABLE 
  "role" 
ADD 
  CONSTRAINT "PK_ROLE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table roletype

ALTER TABLE 
  "roletype" 
ADD 
  CONSTRAINT "PK_ROLETYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table scheduledtransaction

ALTER TABLE 
  "scheduledtransaction" 
ADD 
  CONSTRAINT "PK_SCHEDULEDTRANSACTION_ID" PRIMARY KEY ("Id")
  USING INDEX ENABLE;
--  Constraints for Table securityquestion

ALTER TABLE 
  "securityquestion" 
ADD 
  CONSTRAINT "PK_SECURITYQUESTION_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table service

ALTER TABLE 
  "service" 
ADD 
  CONSTRAINT "PK_SERVICE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table servicechannel

ALTER TABLE 
  "servicechannel" 
ADD 
  CONSTRAINT "PK_SERVICECHANNEL_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table service_permission_mapper

ALTER TABLE 
  "service_permission_mapper" 
ADD 
  CONSTRAINT "PK_SERVICE_PERMISSION_MAPPER_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;

ALTER TABLE 
  "service_permission_mapper" 
ADD 
  CONSTRAINT "SERVICE_PERMISSION_MAPPER$UNIQUE_SERVICE_PERMISSION_MAPPER" UNIQUE (
    "service_name",
"object_name",
"operation"
  )
  USING INDEX ENABLE;
--  Constraints for Table servicetype

ALTER TABLE 
  "servicetype" 
ADD 
  CONSTRAINT "PK_SERVICETYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table state

ALTER TABLE 
  "state" 
ADD 
  CONSTRAINT "PK_STATE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;

ALTER TABLE 
  "state" 
ADD 
  CONSTRAINT "STATE$STATE_UNIQUE" UNIQUE ("state")
  USING INDEX ENABLE;
--  Constraints for Table status

ALTER TABLE 
  "status" 
ADD 
  CONSTRAINT "PK_STATUS_ID" PRIMARY KEY ("id",
"Type_id")
  USING INDEX ENABLE;
--  Constraints for Table statuschange

ALTER TABLE "statuschange" ADD CONSTRAINT "PK_STATUSCHANGE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table statustype

ALTER TABLE 
  "statustype" 
ADD 
  CONSTRAINT "PK_STATUSTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table suspendedcustomers

ALTER TABLE 
  "suspendedcustomers" 
ADD 
  CONSTRAINT "PK_SUSPENDEDCUSTOMERS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table swiftcode

ALTER TABLE 
  "swiftcode" 
ADD 
  CONSTRAINT "PK_SWIFTCODE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table systemconfiguration

ALTER TABLE 
  "systemconfiguration" 
ADD 
  CONSTRAINT "PK_SYSTEMCONFIGURATION_PROPERTYNAME" PRIMARY KEY ("PropertyName")
  USING INDEX ENABLE;
--  Constraints for Table tbladdetails

ALTER TABLE 
  "tbladdetails" 
ADD 
  CONSTRAINT "PK_TBLADDETAILS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table termsandconditions

ALTER TABLE 
  "termsandconditions" 
ADD 
  CONSTRAINT "PK_TERMSANDCONDITIONS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table timeperiod

ALTER TABLE 
  "timeperiod" 
ADD 
  CONSTRAINT "PK_TIMEPERIOD_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table transaction

ALTER TABLE 
  "transaction" 
ADD 
  CONSTRAINT "PK__TRANSACT__3214EC079115CF13" PRIMARY KEY ("Id")
  USING INDEX ENABLE;
--  Constraints for Table transactionlimit

ALTER TABLE 
  "transactionlimit" 
ADD 
  CONSTRAINT "PK_TRANSACTIONLIMIT_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table transactiontype
ALTER TABLE 
  "transactiontype" 
ADD 
  CONSTRAINT "PK_TRANSACTIONTYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table transactiontypemapping

ALTER TABLE 
  "transactiontypemapping" 
ADD 
  CONSTRAINT "PK__TRANSACT__F48FFFCFAD3851F8" PRIMARY KEY ("backendTransactionTypeId")
  USING INDEX ENABLE;
--  Constraints for Table transferseries

ALTER TABLE 
  "transferseries" 
ADD 
  CONSTRAINT "PK_TRANSFERSERIES_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table travelnotification

ALTER TABLE 
  "travelnotification" 
ADD 
  CONSTRAINT "PK_TRAVELNOTIFICATION_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table user

ALTER TABLE 
  "user" 
ADD 
  CONSTRAINT "PK_USER_ID" PRIMARY KEY ("Id")
  USING INDEX ENABLE;

ALTER TABLE 
  "user" 
ADD 
  CONSTRAINT "USER$DEFAULTMODULE_ID_UNIQUE" UNIQUE ("defaultModule_id")
  USING INDEX ENABLE;

ALTER TABLE 
  "user" 
ADD 
  CONSTRAINT "USER$USER_NAME" UNIQUE ("userName")
  USING INDEX ENABLE;
--  Constraints for Table useraccountalerts

ALTER TABLE 
  "useraccountalerts" 
ADD 
  CONSTRAINT "PK_USERACCOUNTALERTS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table useraccounts

ALTER TABLE 
  "useraccounts" 
ADD 
  CONSTRAINT "PK_USERACCOUNTS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table useralerts

ALTER TABLE 
  "useralerts" 
ADD 
  CONSTRAINT "PK_USERALERTS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table usercashflow

ALTER TABLE 
  "usercashflow" 
ADD 
  CONSTRAINT "PK_USERCASHFLOW_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table usercommunication

ALTER TABLE 
  "usercommunication" 
ADD 
  CONSTRAINT "PK_USERCOMMUNICATION_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table usercreditcheck

ALTER TABLE 
  "usercreditcheck" 
ADD 
  CONSTRAINT "PK_USERCREDITCHECK_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table usernotification

ALTER TABLE 
  "usernotification" 
ADD 
  CONSTRAINT "PK_USERNOTIFICATION_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table userpersonalinfo

ALTER TABLE 
  "userpersonalinfo" 
ADD 
  CONSTRAINT "PK_USERPERSONALINFO_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table userproducts

ALTER TABLE 
  "userproducts" 
ADD 
  CONSTRAINT "PK_USERPRODUCTS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table usersecurity

ALTER TABLE 
  "usersecurity" 
ADD 
  CONSTRAINT "PK_USERSECURITY_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table userserviceprefernces

ALTER TABLE 
  "userserviceprefernces" 
ADD 
  CONSTRAINT "PK_USERSERVICEPREFERNCES_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table usertransactionhistory

ALTER TABLE 
  "usertransactionhistory" 
ADD 
  CONSTRAINT "PK_USERTRANSACTIONHISTORY_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table vihicleinfo

ALTER TABLE 
  "vihicleinfo" 
ADD 
  CONSTRAINT "PK_VIHICLEINFO_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table wallet

ALTER TABLE 
  "wallet" 
ADD 
  CONSTRAINT "PK_WALLET_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;

ALTER TABLE 
  "wallet" 
ADD 
  CONSTRAINT "WALLET$UK_FEE6CFA8DB4440BA8E768221092" UNIQUE ("user_id")
  USING INDEX ENABLE;
--  Constraints for Table wealthuserpreferences

ALTER TABLE 
  "wealthuserpreferences" 
ADD 
  CONSTRAINT "PK__WEALTHUS__3213E83FBCB3542B" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table weekday

ALTER TABLE 
  "weekday" 
ADD 
  CONSTRAINT "PK__WEEKDAY__3213E83FA3D481B1" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table weekdayvalue

ALTER TABLE 
  "weekdayvalue" 
ADD 
  CONSTRAINT "PK__WEEKDAYV__F0AC4B3C5F5CE506" PRIMARY KEY ("weekdayId",
"languageCode")
  USING INDEX ENABLE;
--  Constraints for Table workschedule

ALTER TABLE 
  "workschedule" 
ADD 
  CONSTRAINT "PK_WORKSCHEDULE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table securityimage

ALTER TABLE 
  "securityimage" 
ADD 
  CONSTRAINT "PK_SECURITYIMAGE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--  Constraints for Table transactionfee

ALTER TABLE 
  "transactionfee" 
ADD 
  CONSTRAINT "PK_TRANSACTIONFEE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table additionalfield
--------------------------------------------------------

ALTER TABLE "additionalfield" ADD CONSTRAINT "PK_ADDITIONALFIELD_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table archivedcustomerrequest
--------------------------------------------------------

ALTER TABLE "archivedcustomerrequest" ADD CONSTRAINT "PK_ARCHIVEDCUSTOMERREQUEST_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table archivedmedia
--------------------------------------------------------

ALTER TABLE "archivedmedia" ADD CONSTRAINT "PK_ARCHIVEDMEDIA_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table archivedmessageattachment
--------------------------------------------------------

ALTER TABLE "archivedmessageattachment" ADD CONSTRAINT "PK_ARCHIVEDMESSAGEATTACHMENT_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table archivedrequestmessage
--------------------------------------------------------

ALTER TABLE "archivedrequestmessage" ADD CONSTRAINT "PK_ARCHIVEDREQUESTMESSAGE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table bankfortransfer
--------------------------------------------------------

ALTER TABLE "bankfortransfer" ADD CONSTRAINT "PK_BANKFORTRANSFER_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table bankservice
--------------------------------------------------------

ALTER TABLE "bankservice" ADD CONSTRAINT "PK_BANKSERVICE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table compositepermission
--------------------------------------------------------

ALTER TABLE "compositepermission" ADD CONSTRAINT "PK_COMPOSITEPERMISSION_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table configurations
--------------------------------------------------------

ALTER TABLE "configurations" ADD CONSTRAINT "CONFIGURATIONS$UK_BUNDLEID_CONFIGKEY" UNIQUE ("bundle_id",
"config_key")
  USING INDEX ENABLE;
ALTER TABLE "configurations" ADD CONSTRAINT "PK_CONFIGURATIONS_CONFIGURATION_ID" PRIMARY KEY ("configuration_id")
  USING INDEX ENABLE;

--  Constraints for Table corporatepayees

ALTER TABLE "corporatepayees" ADD PRIMARY KEY ("id")
  USING INDEX  ENABLE;

--------------------------------------------------------
--  Constraints for Table csrassistgrant
--------------------------------------------------------

ALTER TABLE "csrassistgrant" ADD CONSTRAINT "PK_CSRASSISTGRANT_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;

--  Constraints for Table customernote
--------------------------------------------------------

ALTER TABLE "customernote" ADD CONSTRAINT "PK_CUSTOMERNOTE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table customernotification
--------------------------------------------------------

ALTER TABLE "customernotification" ADD CONSTRAINT "PK_CUSTOMERNOTIFICATION_CUSTOMER_ID" PRIMARY KEY ("Customer_id",
"Notification_id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table customersecurityimages
--------------------------------------------------------

ALTER TABLE "customersecurityimages" ADD CONSTRAINT "PK_CUSTOMERSECURITYIMAGES_CUSTOMER_ID" PRIMARY KEY ("Customer_id",
"Image_id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table customerservice
--------------------------------------------------------

ALTER TABLE "customerservice" ADD CONSTRAINT "PK_CUSTOMERSERVICE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table dashboardalerts
--------------------------------------------------------

ALTER TABLE "dashboardalerts" ADD CONSTRAINT "PK_DASHBOARDALERTS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table datatype
--------------------------------------------------------

ALTER TABLE "datatype" ADD CONSTRAINT "PK_DATATYPE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table dayschedule
--------------------------------------------------------

ALTER TABLE "dayschedule" ADD CONSTRAINT "PK_DAYSCHEDULE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table faqcategory
--------------------------------------------------------

ALTER TABLE "faqcategory" ADD CONSTRAINT "PK_FAQCATEGORY_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table faqs
--------------------------------------------------------

ALTER TABLE "faqs" ADD CONSTRAINT "FAQS$QUESTIONCODE_UNIQUE" UNIQUE ("QuestionCode")
  USING INDEX ENABLE;
ALTER TABLE "faqs" ADD CONSTRAINT "PK_FAQS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table issuerimage
--------------------------------------------------------

ALTER TABLE "issuerimage" ADD CONSTRAINT "PK_ISSUERIMAGE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table locationfile
--------------------------------------------------------

ALTER TABLE "locationfile" ADD CONSTRAINT "PK_LOCATIONFILE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table locationservice
--------------------------------------------------------

ALTER TABLE "locationservice" ADD CONSTRAINT "PK_LOCATIONSERVICE_LOCATION_ID" PRIMARY KEY ("Location_id",
"Service_id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table logview
--------------------------------------------------------

ALTER TABLE "logview" ADD CONSTRAINT "PK_LOGVIEW_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table messagetemplate
--------------------------------------------------------

ALTER TABLE "messagetemplate" ADD CONSTRAINT "PK_MESSAGETEMPLATE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table outagemessage
--------------------------------------------------------

ALTER TABLE "outagemessage" ADD CONSTRAINT "PK_OUTAGEMESSAGE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table periodiclimit
--------------------------------------------------------

ALTER TABLE "periodiclimit" ADD CONSTRAINT "PK_PERIODICLIMIT_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table privacypolicy
--------------------------------------------------------

ALTER TABLE "privacypolicy" ADD CONSTRAINT "PK_PRIVACYPOLICY_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table rolecompositepermission
--------------------------------------------------------

ALTER TABLE "rolecompositepermission" ADD CONSTRAINT "PK_ROLECOMPOSITEPERMISSION_ROLE_ID" PRIMARY KEY ("Role_id",
"CompositePermission_id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table rolepermission
--------------------------------------------------------

ALTER TABLE "rolepermission" ADD CONSTRAINT "PK_ROLEPERMISSION_ROLE_ID" PRIMARY KEY ("Role_id",
"Permission_id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table servicecommunication
--------------------------------------------------------

ALTER TABLE "servicecommunication" ADD CONSTRAINT "PK_SERVICECOMMUNICATION_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table systemuser
--------------------------------------------------------

ALTER TABLE "systemuser" ADD CONSTRAINT "PK_SYSTEMUSER_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table transactionfeeslab
--------------------------------------------------------

ALTER TABLE "transactionfeeslab" ADD CONSTRAINT "PK_TRANSACTIONFEESLAB_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table transactiongroup
--------------------------------------------------------

ALTER TABLE "transactiongroup" ADD CONSTRAINT "PK_TRANSACTIONGROUP_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table transactiongroupservice
--------------------------------------------------------

ALTER TABLE "transactiongroupservice" ADD CONSTRAINT "PK_TRANSACTIONGROUPSERVICE_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table transactionlogs
--------------------------------------------------------

ALTER TABLE "transactionlogs" ADD CONSTRAINT "PK_TRANSACTIONLOGS_ID" PRIMARY KEY ("id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table useraddress
--------------------------------------------------------

ALTER TABLE "useraddress" ADD CONSTRAINT "PK_USERADDRESS_USER_ID" PRIMARY KEY ("User_id",
"Address_id",
"Type_id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table usercompositepermission
--------------------------------------------------------

ALTER TABLE "usercompositepermission" ADD CONSTRAINT "PK_USERCOMPOSITEPERMISSION_USER_ID" PRIMARY KEY ("User_id",
"CompositePermission_id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table userpermission
--------------------------------------------------------

ALTER TABLE "userpermission" ADD CONSTRAINT "PK_USERPERMISSION_PERMISSION_ID" PRIMARY KEY ("User_id",
"Permission_id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table userrole
--------------------------------------------------------

ALTER TABLE "userrole" ADD CONSTRAINT "PK_USERROLE_USER_ID" PRIMARY KEY ("User_id",
"Role_id")
  USING INDEX ENABLE;
--------------------------------------------------------
--  Constraints for Table variablereference
--------------------------------------------------------

ALTER TABLE "variablereference" ADD CONSTRAINT "PK_VARIABLEREFERENCE_VR_KEY" PRIMARY KEY ("vr_key")
  USING INDEX ENABLE;
--------------------------------------------------------



--  Ref Constraints for Table location

ALTER TABLE 
  "location" 
ADD 
  CONSTRAINT "LOCATION$FK_LOCATION_ADDRESS" FOREIGN KEY ("Address_id") REFERENCES "address" ("id") ENABLE;

ALTER TABLE 
  "location" 
ADD 
  CONSTRAINT "LOCATION$FK_LOCATION_LOCATIONTYPE" FOREIGN KEY ("Type_id") REFERENCES "locationtype" ("id") ENABLE;

ALTER TABLE 
  "location" 
ADD 
  CONSTRAINT "LOCATION$FK_LOCATION_WORKSCHEDULE" FOREIGN KEY ("WorkSchedule_id") REFERENCES "workschedule" ("id") ENABLE;

--  Ref Constraints for Table membership

ALTER TABLE 
  "membership" 
ADD 
  CONSTRAINT "MEMBERSHIP$FK_MEMBERSHIP_ADDRESS_ADDRESSID" FOREIGN KEY ("addressId") REFERENCES "address" ("id") ENABLE;
--  Ref Constraints for Table membershipaccounts

ALTER TABLE 
  "membershipaccounts" 
ADD 
  CONSTRAINT "MEMBERSHIPACCOUNTS$FK_MEMBERSHIPACCOUNTS_MEMBERSHIP_MEMBERSHIPID" FOREIGN KEY ("membershipId") REFERENCES "membership" ("id") ENABLE;
--  Ref Constraints for Table membershipowner

ALTER TABLE 
  "membershipowner" 
ADD 
  CONSTRAINT "MEMBERSHIPOWNER$FK_MEMBERSHIPOWNER_MEMBERSHIP_MEMBERSHIPID" FOREIGN KEY ("membershipId") REFERENCES "membership" ("id") ENABLE;
--  Ref Constraints for Table messageattachment

ALTER TABLE 
  "messageattachment" 
ADD 
  CONSTRAINT "MESSAGEATTACHMENT$FK_MESSAGEATTACHEMENT_ATTACHEMENTTYPE" FOREIGN KEY ("AttachmentType_id") REFERENCES "attachmenttype" ("id") ENABLE;

ALTER TABLE 
  "messageattachment" 
ADD 
  CONSTRAINT "MESSAGEATTACHMENT$FK_MESSAGEATTACHEMENT_REQUESTMESSAGE" FOREIGN KEY ("RequestMessage_id") REFERENCES "requestmessage" ("id") ENABLE;
--  Ref Constraints for Table messagesubcategory

ALTER TABLE 
  "messagesubcategory" 
ADD 
  CONSTRAINT "MESSAGESUBCATEGORY$FK_SUBCATEGORY_CATEGORY" FOREIGN KEY ("Category_id") REFERENCES "messagecategory" ("Id") ENABLE;
--  Ref Constraints for Table notificationcardinfo

ALTER TABLE 
  "notificationcardinfo" 
ADD 
  CONSTRAINT "NOTIFICATIONCARDINFO$FK_NOTIFICATIONCARDINFO_CUSTOMER" FOREIGN KEY ("Customer_id") REFERENCES "customer" ("id") ENABLE;

ALTER TABLE 
  "notificationcardinfo" 
ADD 
  CONSTRAINT "NOTIFICATIONCARDINFO$FK_NOTIFICATIONCARDINFO_TRAVELNOTIFICATION" FOREIGN KEY ("Notification_id") REFERENCES "travelnotification" ("id") ENABLE;

--  Ref Constraints for Table organisationaccounts

ALTER TABLE 
  "organisationaccounts" 
ADD 
  CONSTRAINT "ORGANISATIONACCOUNTS$FK_ORGANISATIONACCOUNTS_ACCOUNTTYPE" FOREIGN KEY ("TypeID") REFERENCES "accounttype" ("TypeID") ENABLE;

ALTER TABLE 
  "organisationaccounts" 
ADD 
  CONSTRAINT "ORGANISATIONACCOUNTS$FK_ORGANISATIONACCOUNTS_ORGANISATION" FOREIGN KEY ("Organization_id") REFERENCES "organisation" ("id") ENABLE;
--  Ref Constraints for Table organisationaddress

ALTER TABLE 
  "organisationaddress" 
ADD 
  CONSTRAINT "ORGANISATIONADDRESS$FK_ORGANISATIONADDRESS_ORGANIZATIONID" FOREIGN KEY ("Organization_id") REFERENCES "organisation" ("id") ENABLE;
--  Ref Constraints for Table organisationcommunication

ALTER TABLE 
  "organisationcommunication" 
ADD 
  CONSTRAINT "ORGANISATIONCOMMUNICATION$FK_ORGCOMMUNICATION_ORGANIZATIONID" FOREIGN KEY ("Organization_id") REFERENCES "organisation" ("id") ENABLE;
--  Ref Constraints for Table organisationemployees

ALTER TABLE 
  "organisationemployees" 
ADD 
  CONSTRAINT "ORGANISATIONEMPLOYEES$FK_ORGEMPLOYEES_ORGANIZATIONID" FOREIGN KEY ("Organization_id") REFERENCES "organisation" ("id") ENABLE;
--  Ref Constraints for Table organisationowner

ALTER TABLE 
  "organisationowner" 
ADD 
  CONSTRAINT "ORGANISATIONOWNER$FK_ORGANISATIONOWNER_ORGANISATIONID" FOREIGN KEY ("Organization_id") REFERENCES "organisation" ("id") ENABLE;
--  Ref Constraints for Table permission

ALTER TABLE 
  "permission" 
ADD 
  CONSTRAINT "PERMISSION$FK_PERMISSION_DATATYPE" FOREIGN KEY ("DataType_id") REFERENCES "datatype" ("id") ENABLE;

ALTER TABLE 
  "permission" 
ADD 
  CONSTRAINT "PERMISSION$FK_PERMISSION_PERMISSIONTYPE" FOREIGN KEY ("Type_id") REFERENCES "permissiontype" ("id") ENABLE;
--  Ref Constraints for Table popularcurrencies

ALTER TABLE 
  "popularcurrencies" 
ADD 
  CONSTRAINT "FK_CURRENCY_CODE_BASE" FOREIGN KEY ("baseCurrencyCode") REFERENCES "currency" ("code") ENABLE;

ALTER TABLE 
  "popularcurrencies" 
ADD 
  CONSTRAINT "FK_CURRENCY_CODE_QUOTE" FOREIGN KEY ("quoteCurrencyCode") REFERENCES "currency" ("code") ENABLE;

ALTER TABLE 
  "product" 
ADD 
  CONSTRAINT "PRODUCT$FK_PRODUCT_TYPE" FOREIGN KEY ("Type_id") REFERENCES "producttype" ("id") ENABLE;

ALTER TABLE 
  "product" 
ADD 
  CONSTRAINT "PRODUCT$FK_SECONDARYPRODUCTID" FOREIGN KEY ("SecondaryProduct_id") REFERENCES "product" ("id") ENABLE;

--  Ref Constraints for Table recentcurrencies

ALTER TABLE 
  "recentcurrencies" 
ADD 
  CONSTRAINT "FK_CURRENCY_CODE_RECENT" FOREIGN KEY ("quoteCurrencyCode") REFERENCES "currency" ("code") ENABLE;

ALTER TABLE 
  "recentcurrencies" 
ADD 
  CONSTRAINT "FK_CUSTOMER_ID_RECENT" FOREIGN KEY ("customerId") REFERENCES "customer" ("id") ENABLE;
--  Ref Constraints for Table region

ALTER TABLE 
  "region" 
ADD 
  CONSTRAINT "REGION$FK_REGION_COUNTRY" FOREIGN KEY ("Country_id") REFERENCES "country" ("id") ENABLE;
--  Ref Constraints for Table role

ALTER TABLE 
  "role" 
ADD 
  CONSTRAINT "ROLE$FK_ROLE_ROLE" FOREIGN KEY ("Parent_id") REFERENCES "role" ("id") ENABLE;

ALTER TABLE 
  "role" 
ADD 
  CONSTRAINT "ROLE$FK_ROLE_ROLETYPE" FOREIGN KEY ("Type_id") REFERENCES "roletype" ("id") ENABLE;
--  Ref Constraints for Table service

ALTER TABLE 
  "service" 
ADD 
  CONSTRAINT "SERVICE$FK_SERVICE_CATEGORYID" FOREIGN KEY ("Category_id") REFERENCES "category" ("id") ENABLE;

ALTER TABLE 
  "service" 
ADD 
  CONSTRAINT "SERVICE$FK_SERVICE_SERVICETYPE" FOREIGN KEY ("Type_id") REFERENCES "servicetype" ("id") ENABLE;

ALTER TABLE 
  "service" 
ADD 
  CONSTRAINT "SERVICE$FK_SERVICE_TRANSACTIONFEE" FOREIGN KEY ("TransactionFee_id") REFERENCES "transactionfee" ("id") ENABLE;

ALTER TABLE 
  "service" 
ADD 
  CONSTRAINT "SERVICE$FK_SERVICE_TRANSACTIONLIMIT" FOREIGN KEY ("TransactionLimit_id") REFERENCES "transactionlimit" ("id") ENABLE;

ALTER TABLE 
  "service" 
ADD 
  CONSTRAINT "SERVICE$FK_SERVICE_WORKSCHEDULE" FOREIGN KEY ("WorkSchedule_id") REFERENCES "workschedule" ("id") ENABLE;
--  Ref Constraints for Table status

ALTER TABLE 
  "status" 
ADD 
  CONSTRAINT "STATUS$FK_STATUS_STATUSTYPE" FOREIGN KEY ("Type_id") REFERENCES "statustype" ("id") ENABLE;
--  Ref Constraints for Table termsandconditions

ALTER TABLE 
  "termsandconditions" 
ADD 
  CONSTRAINT "TERMSANDCONDITIONS$FK_TERMSANDCONDITIONS_SERVICE" FOREIGN KEY ("Service_id") REFERENCES "service" ("id") ENABLE;
--  Ref Constraints for Table travelnotification

ALTER TABLE 
  "travelnotification" 
ADD 
  CONSTRAINT "TRAVELNOTIFICATION$FK_TRAVELNOTIFICATIONREQUEST_SERVICECHANNEL" FOREIGN KEY ("Channel_id") REFERENCES "servicechannel" ("id") ENABLE;
--  Ref Constraints for Table usercreditcheck

ALTER TABLE 
  "usercreditcheck" 
ADD 
  CONSTRAINT "USERCREDITCHECK$FK_USERCREDITCHECK_USER" FOREIGN KEY ("User_id") REFERENCES "user" ("Id") ENABLE;
--  Ref Constraints for Table userpersonalinfo

ALTER TABLE 
  "userpersonalinfo" 
ADD 
  CONSTRAINT "USERPERSONALINFO$FK_USERPERSONALINFO_USER" FOREIGN KEY ("User_id") REFERENCES "user" ("Id") ENABLE;
--  Ref Constraints for Table userproducts

ALTER TABLE 
  "userproducts" 
ADD 
  CONSTRAINT "USERPRODUCTS$FK_USERPRODUCTS_USER" FOREIGN KEY ("User_id") REFERENCES "user" ("Id") ENABLE;
--  Ref Constraints for Table wealthuserpreferences

ALTER TABLE 
  "wealthuserpreferences" 
ADD 
  CONSTRAINT "FK1_WEALTHUSERPREFERENCES_USERID" FOREIGN KEY ("userId") REFERENCES "customer" ("id") ENABLE;
--  Ref Constraints for Table weekdayvalue

ALTER TABLE 
  "weekdayvalue" 
ADD 
  CONSTRAINT "FK_WEEKDAYVALUE_LOCALE" FOREIGN KEY ("languageCode") REFERENCES "locale" ("Code") ENABLE;

ALTER TABLE 
  "weekdayvalue" 
ADD 
  CONSTRAINT "FK_WEEKDAYVALUE_WEEKDAY" FOREIGN KEY ("weekdayId") REFERENCES "weekday" ("id") ENABLE;
--  Ref Constraints for Table accounts
--------------------------------------------------------

ALTER TABLE "accounts" ADD CONSTRAINT "EXTERNAL_ID" FOREIGN KEY ("ExternalBankidentity_id")
    REFERENCES "externalbankidentity" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table achfile
--------------------------------------------------------

ALTER TABLE "achfile" ADD CONSTRAINT "ACHFILE$FK_ACHFILE_ACHFILEFORMATTYPE" FOREIGN KEY ("achFileFormatType_id")
    REFERENCES "achfileformattype" ("id") ENABLE;

ALTER TABLE "achfile" ADD CONSTRAINT "ACHFILE$FK_ACHFILE_USER" FOREIGN KEY ("createdby")
    REFERENCES "customer" ("id") ENABLE;


--  Ref Constraints for Table achtransaction
--------------------------------------------------------

ALTER TABLE "achtransaction" ADD CONSTRAINT "ACHTRANSACTION$FK_BBTRANSACTION_TEMPLATEREQUEST_TYPE" FOREIGN KEY ("templateRequestType_id")
    REFERENCES "bbtemplaterequesttype" ("templateRequestType_id") ENABLE;
ALTER TABLE "achtransaction" ADD CONSTRAINT "ACHTRANSACTION$FK_BBTRANSACTION_TEMPLATE_TYPE" FOREIGN KEY ("templateType_id")
    REFERENCES "bbtemplatetype" ("templateType_id") ENABLE;
ALTER TABLE "achtransaction" ADD CONSTRAINT "ACHTRANSACTION$FK_BBTRANSACTION_TRANSACTION_TYPE" FOREIGN KEY ("transactionType_id")
    REFERENCES "bbtransactiontype" ("transactionType_id") ENABLE;
ALTER TABLE "achtransaction" ADD CONSTRAINT "ACHTRANSACTION$FK_BBTRANSACTION_USER" FOREIGN KEY ("createdby")
    REFERENCES "customer" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table achtransactionrecord
--------------------------------------------------------

ALTER TABLE "achtransactionrecord" ADD CONSTRAINT "ACHTRANSACTIONRECORD$FK_BBTRANSACTIONRECORD_ACHACCOUNT_TYPE" FOREIGN KEY ("toAccountType")
    REFERENCES "achaccountstype" ("id") ENABLE;
ALTER TABLE "achtransactionrecord" ADD CONSTRAINT "ACHTRANSACTIONRECORD$FK_BBTRANSACTIONRECORD_TAX_TYPE" FOREIGN KEY ("taxType_id")
    REFERENCES "bbtaxtype" ("id") ENABLE;
ALTER TABLE "achtransactionrecord" ADD CONSTRAINT "ACHTRANSACTIONRECORD$FK_BBTRANSACTIONRECORD_TEMPLATEREQUEST_TYPE" FOREIGN KEY ("templateRequestType_id")
    REFERENCES "bbtemplaterequesttype" ("templateRequestType_id") ENABLE;
ALTER TABLE "achtransactionrecord" ADD CONSTRAINT "ACHTRANSACTIONRECORD$FK_BBTRANSACTIONRECORD_TRANSACTIONID" FOREIGN KEY ("transaction_id")
    REFERENCES "achtransaction" ("transaction_id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table achtransactionsubrecord
--------------------------------------------------------

ALTER TABLE "achtransactionsubrecord" ADD CONSTRAINT "ACHTRANSACTIONSUBRECORD$FK_BBTRANSACTIONSUBRECORD_TAXSUB_TYPE_ID" FOREIGN KEY ("taxSubCategory_id")
    REFERENCES "bbtaxsubtype" ("id") ENABLE;
ALTER TABLE "achtransactionsubrecord" ADD CONSTRAINT "ACHTRANSACTIONSUBRECORD$FK_BBTRANSACTIONSUBRECORD_TRANSACTIONRECORD_ID" FOREIGN KEY ("transactionRecord_id")
    REFERENCES "achtransactionrecord" ("transactionRecord_id") ENABLE;
    

--------------------------------------------------------
--  Ref Constraints for Table additionaldata
--------------------------------------------------------

ALTER TABLE "additionaldata" ADD CONSTRAINT "ADDITIONALDATA$FK_ADDITIONALDATA_ADDITIONALFIELDID" FOREIGN KEY ("AdditionalField_id")
    REFERENCES "additionalfield" ("id") ENABLE;

ALTER TABLE "additionaldata" ADD CONSTRAINT "ADDITIONALDATA$FK_ADDITIONALDATA_OBJECTID" FOREIGN KEY ("Object_id")
    REFERENCES "product" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table address
--------------------------------------------------------

ALTER TABLE "address" ADD CONSTRAINT "ADDRESS$FK_ADDRESS_REGION" FOREIGN KEY ("Region_id")
    REFERENCES "region" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table alert
--------------------------------------------------------

ALTER TABLE "alert" ADD CONSTRAINT "ALERT$FK_ALERT_ALERTTYPE" FOREIGN KEY ("AlertType_id")
    REFERENCES "alerttype" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table alertattribute
--------------------------------------------------------

ALTER TABLE "alertattribute" ADD CONSTRAINT "ALERTATTRIBUTE$FK_ALERTATTRIBUTE_LOCALE" FOREIGN KEY ("LanguageCode")
    REFERENCES "locale" ("Code") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table alertattributelistvalues
--------------------------------------------------------

ALTER TABLE "alertattributelistvalues" ADD CONSTRAINT "ALERTATTRIBUTELISTVALUES$FK_ALERTATTRIBUTELISTVALUES_LOCALE" FOREIGN KEY ("LanguageCode")
    REFERENCES "locale" ("Code") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table alertcategorychannel
--------------------------------------------------------

ALTER TABLE "alertcategorychannel" ADD CONSTRAINT "ALERTCATEGORYCHANNEL$FK_ALERTCATEGORYCHANNEL_CHANNEL" FOREIGN KEY ("ChannelID")
    REFERENCES "channel" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table alertcondition
--------------------------------------------------------

ALTER TABLE "alertcondition" ADD CONSTRAINT "ALERTCONDITION$FK_ALERTCONDITION_LOCALE" FOREIGN KEY ("LanguageCode")
    REFERENCES "locale" ("Code") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table alertfrequencytext
--------------------------------------------------------

ALTER TABLE "alertfrequencytext" ADD CONSTRAINT "FK_ALERTFREQUENCYTEXT_ALERTFREQUENCY" FOREIGN KEY ("alertFrequencyId")
    REFERENCES "alertfrequency" ("id") ENABLE;

ALTER TABLE "alertfrequencytext" ADD CONSTRAINT "FK_ALERTFREQUENCYTEXT_LOCALE" FOREIGN KEY ("languageCode")
    REFERENCES "locale" ("Code") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table alertsubtype
--------------------------------------------------------

ALTER TABLE "alertsubtype" ADD CONSTRAINT "ALERTSUBTYPE$FK_ALERTSUBTYPE_ALERTTYPE" FOREIGN KEY ("AlertTypeId")
    REFERENCES "dbxalerttype" ("id") ENABLE;

ALTER TABLE "alertsubtype" ADD CONSTRAINT "FK_ALERTSUBTYPE_ALERTFREQUENCY" FOREIGN KEY ("defaultFrequencyId")
    REFERENCES "alertfrequency" ("id") ENABLE;

ALTER TABLE "alertsubtype" ADD CONSTRAINT "FK_ALERTSUBTYPE_ALERTRECIPIENTTYPE" FOREIGN KEY ("recipienttype")
    REFERENCES "alertrecipienttype" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table alertsubtypeaccounttype
--------------------------------------------------------

ALTER TABLE "alertsubtypeaccounttype" ADD CONSTRAINT "FK_ALERTSUBTYPEACCOUNTTYPE_ALERTSUBTYPE" FOREIGN KEY ("alertSubTypeId")
    REFERENCES "alertsubtype" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table alertsubtypeapp
--------------------------------------------------------

ALTER TABLE "alertsubtypeapp" ADD CONSTRAINT "FK_ALERTSUBTYPEAPP_ALERTSUBTYPE" FOREIGN KEY ("alertSubTypeId")
    REFERENCES "alertsubtype" ("id") ENABLE;

ALTER TABLE "alertsubtypeapp" ADD CONSTRAINT "FK_ALERTSUBTYPEAPP_APP" FOREIGN KEY ("appId")
    REFERENCES "app" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table alertsubtypechannel
--------------------------------------------------------

ALTER TABLE "alertsubtypechannel" ADD CONSTRAINT "FK_ALERTSUBTYPECHANNEL_ALERTSUBTYPE" FOREIGN KEY ("alertSubTypeId")
    REFERENCES "alertsubtype" ("id") ENABLE;

ALTER TABLE "alertsubtypechannel" ADD CONSTRAINT "FK_ALERTSUBTYPECHANNEL_CHANNEL" FOREIGN KEY ("channelId")
    REFERENCES "channel" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table alertsubtypecustomertype
--------------------------------------------------------

ALTER TABLE "alertsubtypecustomertype" ADD CONSTRAINT "FK_ALERTSUBTYPECUSTOMERTYPE_ALERTSUBTYPE" FOREIGN KEY ("alertSubTypeId")
    REFERENCES "alertsubtype" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table alertsubtypetext
--------------------------------------------------------

ALTER TABLE "alertsubtypetext" ADD CONSTRAINT "FK_ALERTSUBTYPETEXT_ALERTSUBTYPE" FOREIGN KEY ("alertSubTypeId")
    REFERENCES "alertsubtype" ("id") ENABLE;

ALTER TABLE "alertsubtypetext" ADD CONSTRAINT "FK_ALERTSUBTYPETEXT_LOCALE" FOREIGN KEY ("languageCode")
    REFERENCES "locale" ("Code") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table alerttypechannel
--------------------------------------------------------

ALTER TABLE "alerttypechannel" ADD CONSTRAINT "FK_ALERTTYPECHANNEL_CHANNEL" FOREIGN KEY ("channelId")
    REFERENCES "channel" ("id") ENABLE;

ALTER TABLE "alerttypechannel" ADD CONSTRAINT "FK_ALERTTYPECHANNEL_DBXALERTTYPE" FOREIGN KEY ("alertTypeId")
    REFERENCES "dbxalerttype" ("id") ENABLE;
--------------------------------------------------------

--------------------------------------------------------
--  Ref Constraints for Table archivedcustomerrequest
--------------------------------------------------------

ALTER TABLE "archivedcustomerrequest" ADD CONSTRAINT "ARCHIVEDCUSTOMERREQUEST$FK_ARCHIVEDCUSTOMERREQUEST_CUSTOMER" FOREIGN KEY ("Customer_id")
    REFERENCES "customer" ("id") ENABLE;

ALTER TABLE "archivedcustomerrequest" ADD CONSTRAINT "ARCHIVEDCUSTOMERREQUEST$FK_ARCHIVEDCUSTOMERREQUEST_REQUESTCATEGORY" FOREIGN KEY ("RequestCategory_id")
    REFERENCES "requestcategory" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table archivedmessageattachment
--------------------------------------------------------

ALTER TABLE "archivedmessageattachment" ADD CONSTRAINT "ARCHIVEDMESSAGEATTACHMENT$FK_ARCHIVEDMESSAGEATTACHEMENT_ATTACHEMENTTYPE" FOREIGN KEY ("AttachmentType_id")
    REFERENCES "attachmenttype" ("id") ENABLE;

ALTER TABLE "archivedmessageattachment" ADD CONSTRAINT "ARCHIVEDMESSAGEATTACHMENT$FK_ARCHIVEDMESSAGEATTACHEMENT_MEDIA" FOREIGN KEY ("Media_id")
    REFERENCES "archivedmedia" ("id") ENABLE;

ALTER TABLE "archivedmessageattachment" ADD CONSTRAINT "ARCHIVEDMESSAGEATTACHMENT$FK_ARCHIVEDMESSAGEATTACHEMENT_REQUESTMESSAGE" FOREIGN KEY ("RequestMessage_id")
    REFERENCES "archivedrequestmessage" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table archivedrequestmessage
--------------------------------------------------------

ALTER TABLE "archivedrequestmessage" ADD CONSTRAINT "ARCHIVEDREQUESTMESSAGE$FK_ARCHIVEDREQUESTMESSAGE_CUSTOMERREQUEST" FOREIGN KEY ("CustomerRequest_id")
    REFERENCES "archivedcustomerrequest" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table backendidentifier
--------------------------------------------------------

ALTER TABLE "backendidentifier" ADD CONSTRAINT "FK_BACKENDIDENTIFIER_CUSTOMER_CUSTOMERID" FOREIGN KEY ("Customer_id")
    REFERENCES "customer" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table bankbranch
--------------------------------------------------------

ALTER TABLE "bankbranch" ADD CONSTRAINT "BANKBRANCH$FK_BRANCHTYPE_ID" FOREIGN KEY ("Type_id")
    REFERENCES "branchtype" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table bankfortransfer
--------------------------------------------------------

ALTER TABLE "bankfortransfer" ADD CONSTRAINT "BANKFORTRANSFER$FK_BANKFORTRANSFER_ADDRESSID" FOREIGN KEY ("Address_id")
    REFERENCES "address" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table bankservice
--------------------------------------------------------

ALTER TABLE "bankservice" ADD CONSTRAINT "BANKSERVICE$FK_BANKSERVICE_BANKFORTRANSFERID" FOREIGN KEY ("BankForTransfer_id")
    REFERENCES "bankfortransfer" ("id") ENABLE;

ALTER TABLE "bankservice" ADD CONSTRAINT "BANKSERVICE$FK_BANKSERVICE_SERVICEID" FOREIGN KEY ("Service_id")
    REFERENCES "service" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table bbactedrequest
--------------------------------------------------------

ALTER TABLE "bbactedrequest" ADD CONSTRAINT "BBACTEDREQUEST$FK_BBACTEDREQUEST_REQUESTID" FOREIGN KEY ("requestId")
    REFERENCES "bbrequest" ("requestId") ENABLE;

ALTER TABLE "bbactedrequest" ADD CONSTRAINT "BBACTEDREQUEST$FK_BBACTEDREQUEST_USER_ID" FOREIGN KEY ("createdby")
    REFERENCES "customer" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table bbrequest
--------------------------------------------------------

ALTER TABLE "bbrequest" ADD CONSTRAINT "BBREQUEST$FK_BBREQUEST_CUSTOMER" FOREIGN KEY ("createdby")
    REFERENCES "customer" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table bbtaxsubtype
--------------------------------------------------------

ALTER TABLE "bbtaxsubtype" ADD CONSTRAINT "BBTAXSUBTYPE$FK_BBTAXSUBTYPE_BBTAXTYPE" FOREIGN KEY ("taxType")
    REFERENCES "bbtaxtype" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table bbtemplate
--------------------------------------------------------

ALTER TABLE "bbtemplate" ADD CONSTRAINT "BBTEMPLATE$FK_BBTEMPLATE_TEMPLATEREQUEST_TYPE" FOREIGN KEY ("templateRequestType_id")
    REFERENCES "bbtemplaterequesttype" ("templateRequestType_id") ENABLE;

ALTER TABLE "bbtemplate" ADD CONSTRAINT "BBTEMPLATE$FK_BBTEMPLATE_TEMPLATE_TYPE" FOREIGN KEY ("templateType_id")
    REFERENCES "bbtemplatetype" ("templateType_id") ENABLE;

ALTER TABLE "bbtemplate" ADD CONSTRAINT "BBTEMPLATE$FK_BBTEMPLATE_TRANSACTION_TYPE" FOREIGN KEY ("transactionType_id")
    REFERENCES "bbtransactiontype" ("transactionType_id") ENABLE;

ALTER TABLE "bbtemplate" ADD CONSTRAINT "BBTEMPLATE$FK_BBTEMPLATE_USER" FOREIGN KEY ("createdby")
    REFERENCES "customer" ("id") ENABLE;

ALTER TABLE "bbtemplate" ADD CONSTRAINT "BBTEMPLATE$FK_BBTEMPLATE_USER_2" FOREIGN KEY ("updatedBy")
    REFERENCES "customer" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table bbtemplaterecord
--------------------------------------------------------

ALTER TABLE "bbtemplaterecord" ADD CONSTRAINT "BBTEMPLATERECORD$FK_BBTEMPLATERECORD_ACCOUNT_TYPE_ID" FOREIGN KEY ("toAccountType")
    REFERENCES "achaccountstype" ("id") ENABLE;

ALTER TABLE "bbtemplaterecord" ADD CONSTRAINT "BBTEMPLATERECORD$FK_BBTEMPLATERECORD_TAX_TYPE_ID" FOREIGN KEY ("taxType_id")
    REFERENCES "bbtaxtype" ("id") ENABLE;

ALTER TABLE "bbtemplaterecord" ADD CONSTRAINT "BBTEMPLATERECORD$FK_BBTEMPLATERECORD_TEMPLATE_ID" FOREIGN KEY ("template_id")
    REFERENCES "bbtemplate" ("templateId") ENABLE;

ALTER TABLE "bbtemplaterecord" ADD CONSTRAINT "BBTEMPLATERECORD$FK_BBTEMPLATERECORD_TEMPLATE_REQUEST_TYPE_ID" FOREIGN KEY ("templateRequestType_id")
    REFERENCES "bbtemplaterequesttype" ("templateRequestType_id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table bbtemplaterequesttype
--------------------------------------------------------

ALTER TABLE "bbtemplaterequesttype" ADD CONSTRAINT "BBTEMPLATEREQUESTTYPE$FK_BBTEMPLATEREQUESTTYPE_TRANSACTIONTYPE_ID" FOREIGN KEY ("transactionType_id")
    REFERENCES "bbtransactiontype" ("transactionType_id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table bbtemplatesubrecord
--------------------------------------------------------

ALTER TABLE "bbtemplatesubrecord" ADD CONSTRAINT "BBTEMPLATESUBRECORD$FK_BBTEMPLATESUBRECORD_TAXSUB_TYPE_ID" FOREIGN KEY ("taxSubCategory_id")
    REFERENCES "bbtaxsubtype" ("id") ENABLE;

ALTER TABLE "bbtemplatesubrecord" ADD CONSTRAINT "BBTEMPLATESUBRECORD$FK_BBTEMPLATESUBRECORD_TEMPLATERECORD_ID" FOREIGN KEY ("templateRecord_id")
    REFERENCES "bbtemplaterecord" ("templateRecord_id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table billermaster
--------------------------------------------------------

ALTER TABLE "billermaster" ADD CONSTRAINT "BILLERMASTER$FK_BILLERMASTER_BILLERCATEGORY" FOREIGN KEY ("billerCategoryId")
    REFERENCES "billercategory" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table bulkwirefilelineitems
--------------------------------------------------------

ALTER TABLE "bulkwirefilelineitems" ADD CONSTRAINT "BULKWIREFILELINEITEMS$FK_BULKWIRELINEITEMS_BULKWIREFILEID" FOREIGN KEY ("bulkWireFileID")
    REFERENCES "bulkwirefiles" ("bulkWireFileID") ENABLE;

ALTER TABLE "bulkwirefilelineitems" ADD CONSTRAINT "BULKWIREFILELINEITEMS$FK_BULKWIRELINEITEMS_CURENCY" FOREIGN KEY ("currency")
    REFERENCES "currency" ("code") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table bulkwirefiles
--------------------------------------------------------

ALTER TABLE "bulkwirefiles" ADD CONSTRAINT "BULKWIREFILES$FK_BULKWIREFILES_FILEFORMAT" FOREIGN KEY ("fileFormatCode")
    REFERENCES "bulkwirefileformattype" ("bulkWiresFileFormatTypeCode") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table bulkwirefiletransactdetails
--------------------------------------------------------

ALTER TABLE "bulkwirefiletransactdetails" ADD CONSTRAINT "BULKWIREFILETRANSACTDETAILS$FK_TRANSACTIONID_BULKWIREFILEID" FOREIGN KEY ("bulkWireFileID")
    REFERENCES "bulkwirefiles" ("bulkWireFileID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table bulkwiresamplefile
--------------------------------------------------------

ALTER TABLE "bulkwiresamplefile" ADD CONSTRAINT "BULKWIRESAMPLEFILE$FK_BULKWIRESAMPLE_FILEFORMAT" FOREIGN KEY ("bulkWireSampleFileFormatCode")
    REFERENCES "bulkwirefileformattype" ("bulkWiresFileFormatTypeCode") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table bulkwiretemplate
--------------------------------------------------------

ALTER TABLE "bulkwiretemplate" ADD CONSTRAINT "BULKWIRETEMPLATE$FK1_BULKWIRETEMPLATE_CREATEDBY" FOREIGN KEY ("createdBy")
    REFERENCES "customer" ("id") ENABLE;

ALTER TABLE "bulkwiretemplate" ADD CONSTRAINT "BULKWIRETEMPLATE$FK3_BULKWIRETEMPLATE_MODIFIEDBY" FOREIGN KEY ("modifiedBy")
    REFERENCES "customer" ("id") ENABLE;

ALTER TABLE "bulkwiretemplate" ADD CONSTRAINT "BULKWIRETEMPLATE$FK4_BULKWIRETEMPLATE_DEFAULTCURRENCY" FOREIGN KEY ("defaultCurrency")
    REFERENCES "currency" ("code") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table bulkwiretemplatelineitems
--------------------------------------------------------

ALTER TABLE "bulkwiretemplatelineitems" ADD CONSTRAINT "BULKWIRETEMPLATELINEITEMS$FK1_BULKWIRETEMPLATELINEITEMS_BULKWIRETEMPLATEID" FOREIGN KEY ("bulkWireTemplateID")
    REFERENCES "bulkwiretemplate" ("bulkWireTemplateID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table bulkwiretemplatetransactdetails
--------------------------------------------------------

ALTER TABLE "bulkwiretemplatetransactdetails" ADD CONSTRAINT "BULKWIRETEMPLATETRANSACTDETAILS$FK1_BULKWIRETEMPLATETRANSACTDETAILS_BULKWIRETEMPLATEID" FOREIGN KEY ("bulkWireTemplateID")
    REFERENCES "bulkwiretemplate" ("bulkWireTemplateID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table cardaccountrequest
--------------------------------------------------------

ALTER TABLE "cardaccountrequest" ADD CONSTRAINT "CARDACCOUNTREQUEST$FK_CARDACCOUNTREQUEST_ADDRESS" FOREIGN KEY ("Address_id")
    REFERENCES "address" ("id") ENABLE;

ALTER TABLE "cardaccountrequest" ADD CONSTRAINT "CARDACCOUNTREQUEST$FK_CARDACCOUNTREQUEST_CARDACCOUNTREQUESTTYPE" FOREIGN KEY ("RequestType_id")
    REFERENCES "cardaccountrequesttype" ("id") ENABLE;

ALTER TABLE "cardaccountrequest" ADD CONSTRAINT "CARDACCOUNTREQUEST$FK_CARDACCOUNTREQUEST_CUSTOMER" FOREIGN KEY ("Customer_id")
    REFERENCES "customer" ("id") ENABLE;

ALTER TABLE "cardaccountrequest" ADD CONSTRAINT "CARDACCOUNTREQUEST$FK_CARDACCOUNTREQUEST_CUSTOMERCOMMUNICATION" FOREIGN KEY ("Communication_id")
    REFERENCES "customercommunication" ("id") ENABLE;

ALTER TABLE "cardaccountrequest" ADD CONSTRAINT "CARDACCOUNTREQUEST$FK_CARDACCOUNTREQUEST_SERVICECHANNEL" FOREIGN KEY ("Channel_id")
    REFERENCES "servicechannel" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table cardproducttype
--------------------------------------------------------

ALTER TABLE "cardproducttype" ADD CONSTRAINT "FK_PRODUCT_ID" FOREIGN KEY ("productId")
    REFERENCES "cardproducts" ("productId") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table channeltext
--------------------------------------------------------

ALTER TABLE "channeltext" ADD CONSTRAINT "CHANNELTEXT$FK_CHANNELTEXT_CHANNEL" FOREIGN KEY ("channelID")
    REFERENCES "channel" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table city
--------------------------------------------------------

ALTER TABLE "city" ADD CONSTRAINT "CITY$FK_CITY_COUNTRY" FOREIGN KEY ("Country_id")
    REFERENCES "country" ("id") ENABLE;

ALTER TABLE "city" ADD CONSTRAINT "CITY$FK_CITY_REGION" FOREIGN KEY ("Region_id")
    REFERENCES "region" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table communicationtemplate
--------------------------------------------------------

ALTER TABLE "communicationtemplate" ADD CONSTRAINT "COMMUNICATIONTEMPLATE$FK_COMMUNICATIONTEMPLATE_ALERTSUBTYPE" FOREIGN KEY ("AlertSubTypeId")
    REFERENCES "alertsubtype" ("id") ENABLE;

ALTER TABLE "communicationtemplate" ADD CONSTRAINT "COMMUNICATIONTEMPLATE$FK_COMMUNICATIONTEMPLATE_CHANNEL" FOREIGN KEY ("ChannelID")
    REFERENCES "channel" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table compositepermission
--------------------------------------------------------

ALTER TABLE "compositepermission" ADD CONSTRAINT "COMPOSITEPERMISSION$FK_COMPOSITEPERMISSION_ENTITLEMENT" FOREIGN KEY ("Entitlement_id")
    REFERENCES "service" ("id") ENABLE;

ALTER TABLE "compositepermission" ADD CONSTRAINT "COMPOSITEPERMISSION$FK_COMPOSITEPERMISSION_PERMISSIONID" FOREIGN KEY ("Permission_id")
    REFERENCES "permission" ("id") ENABLE;
--------------------------------------------------------
--------------------------------------------------------
--  Ref Constraints for Table contractactionlimit
--------------------------------------------------------

ALTER TABLE "contractactionlimit" ADD CONSTRAINT "FK_CONTRACTACTIONLIMIT_CONTRACT_CONTRACTID" FOREIGN KEY ("contractId")
    REFERENCES "contract" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table contractcustomers
--------------------------------------------------------

ALTER TABLE "contractcustomers" ADD CONSTRAINT "FK_CONTRACTCUSTOMERS_CUSTOMER_CUSTOMERID" FOREIGN KEY ("customerId")
    REFERENCES "customer" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table countrybasecurrency
--------------------------------------------------------

ALTER TABLE "countrybasecurrency" ADD CONSTRAINT "FK_COUNTRY_ID" FOREIGN KEY ("countryCode")
    REFERENCES "country" ("id") ENABLE;

ALTER TABLE "countrybasecurrency" ADD CONSTRAINT "FK_CURRENCY_CODE" FOREIGN KEY ("baseCurrencyCode")
    REFERENCES "currency" ("code") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table csrassistgrant
--------------------------------------------------------

ALTER TABLE "csrassistgrant" ADD CONSTRAINT "CSRASSISTGRANT$FK_CSRASSISTGRANT_CUSTOMER" FOREIGN KEY ("customerId")
    REFERENCES "customer" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table currencymarketrates
--------------------------------------------------------

ALTER TABLE "currencymarketrates" ADD CONSTRAINT "FK_CURRENCY_CODE_RECENTQUOTE" FOREIGN KEY ("quoteCurrencyCode")
    REFERENCES "currency" ("code") ENABLE;

ALTER TABLE "currencymarketrates" ADD CONSTRAINT "FK_CURRENCY_CODE_RECENT_BASE" FOREIGN KEY ("baseCurrencyCode")
    REFERENCES "currency" ("code") ENABLE;

ALTER TABLE "currencymarketrates" ADD CONSTRAINT "FK_MARKET_ID" FOREIGN KEY ("marketId")
    REFERENCES "market" ("id") ENABLE;
--------------------------------------------------------

--  Ref Constraints for Table customeraction
--------------------------------------------------------

ALTER TABLE "customeraction" ADD CONSTRAINT "CUSTOMERACTION$FK_CUSTOMERACTIONLIMIT_CUSTOMER" FOREIGN KEY ("Customer_id")
    REFERENCES "customer" ("id") ENABLE;



--------------------------------------------------------
--  Ref Constraints for Table customeraddress
--------------------------------------------------------

ALTER TABLE "customeraddress" ADD CONSTRAINT "CUSTOMERADDRESS$FK_CUSTOMERADDRESS_ADDRESS" FOREIGN KEY ("Address_id")
    REFERENCES "address" ("id") ENABLE;

ALTER TABLE "customeraddress" ADD CONSTRAINT "CUSTOMERADDRESS$FK_CUSTOMERADDRESS_ADDRESSTYPE" FOREIGN KEY ("Type_id")
    REFERENCES "addresstype" ("id") ENABLE;

ALTER TABLE "customeraddress" ADD CONSTRAINT "CUSTOMERADDRESS$FK_CUSTOMERADDRESS_CUSTOMER" FOREIGN KEY ("Customer_id")
    REFERENCES "customer" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table customeralertcategorychannel
--------------------------------------------------------

ALTER TABLE "customeralertcategorychannel" ADD CONSTRAINT "CUSTOMERALERTCATEGORYCHANNEL$FK_CUSTOMERALERTCATEGORYCHANNEL_CHANNEL" FOREIGN KEY ("ChannelId")
    REFERENCES "channel" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table customeralertchannel
--------------------------------------------------------

ALTER TABLE "customeralertchannel" ADD CONSTRAINT "FK_CUSTOMERALERTCHANNEL_CHANNEL" FOREIGN KEY ("channelId")
    REFERENCES "channel" ("id") ENABLE;

ALTER TABLE "customeralertchannel" ADD CONSTRAINT "FK_CUSTOMERALERTCHANNEL_CUSTOMER" FOREIGN KEY ("customerId")
    REFERENCES "customer" ("id") ENABLE;

ALTER TABLE "customeralertchannel" ADD CONSTRAINT "FK_CUSTOMERALERTCHANNEL_DBXALERTCATEGORY" FOREIGN KEY ("alertCategoryId")
    REFERENCES "dbxalertcategory" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table customeralertentitlement
--------------------------------------------------------

ALTER TABLE "customeralertentitlement" ADD CONSTRAINT "CUSTOMERALERTENTITLEMENT$FK_CUSTOMERALERTENTITLEMENT_ALERT" FOREIGN KEY ("Alert_id")
    REFERENCES "alert" ("id") ENABLE;

ALTER TABLE "customeralertentitlement" ADD CONSTRAINT "CUSTOMERALERTENTITLEMENT$FK_CUSTOMERALERTENTITLEMENT_CUSTOMER" FOREIGN KEY ("Customer_id")
    REFERENCES "customer" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table customeralertfrequency
--------------------------------------------------------

ALTER TABLE "customeralertfrequency" ADD CONSTRAINT "FK_CUSTOMERALERTFREQUENCY_ALERTFREQUENCY" FOREIGN KEY ("alertFrequencyId")
    REFERENCES "alertfrequency" ("id") ENABLE;

ALTER TABLE "customeralertfrequency" ADD CONSTRAINT "FK_CUSTOMERALERTFREQUENCY_CUSTOMER" FOREIGN KEY ("customerId")
    REFERENCES "customer" ("id") ENABLE;

ALTER TABLE "customeralertfrequency" ADD CONSTRAINT "FK_CUSTOMERALERTFREQUENCY_DBXALERTCATEGORY" FOREIGN KEY ("alertCategoryId")
    REFERENCES "dbxalertcategory" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table customercommunication
--------------------------------------------------------

ALTER TABLE "customercommunication" ADD CONSTRAINT "CUSTOMERCOMMUNICATION$FK_CUSTOMERCOMMUNICATION_COMMUNICATIONTYPE" FOREIGN KEY ("Type_id")
    REFERENCES "communicationtype" ("id") ENABLE;

ALTER TABLE "customercommunication" ADD CONSTRAINT "CUSTOMERCOMMUNICATION$FK_CUSTOMERCOMMUNICATION_CUSTOMER" FOREIGN KEY ("Customer_id")
    REFERENCES "customer" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table customerdevice
--------------------------------------------------------

ALTER TABLE "customerdevice" ADD CONSTRAINT "CUSTOMERDEVICE$FK_CUSTOMERDEVICE_CHANNEL" FOREIGN KEY ("Channel_id")
    REFERENCES "servicechannel" ("id") ENABLE;

ALTER TABLE "customerdevice" ADD CONSTRAINT "CUSTOMERDEVICE$FK_CUSTOMERDEVICE_CUSTOMER" FOREIGN KEY ("Customer_id")
    REFERENCES "customer" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table customerentitlement
--------------------------------------------------------

ALTER TABLE "customerentitlement" ADD CONSTRAINT "CUSTOMERENTITLEMENT$FK_CUSTOMERENTITLEMENT_CUSTOMER" FOREIGN KEY ("Customer_id")
    REFERENCES "customer" ("id") ENABLE;

ALTER TABLE "customerentitlement" ADD CONSTRAINT "CUSTOMERENTITLEMENT$FK_CUSTOMERENTITLEMENT_SERVICE" FOREIGN KEY ("Service_id")
    REFERENCES "service" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table customerflagstatus
--------------------------------------------------------

ALTER TABLE "customerflagstatus" ADD CONSTRAINT "CUSTOMERFLAGSTATUS$FK_CUSTOMERFLAGSTATUS_CUSTOMERID" FOREIGN KEY ("Customer_id")
    REFERENCES "customer" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table customergroup
--------------------------------------------------------

ALTER TABLE "customergroup" ADD CONSTRAINT "CUSTOMERGROUP$FK_CUSTOMERGROUP_CUSTOMER" FOREIGN KEY ("Customer_id")
    REFERENCES "customer" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table customerimage
--------------------------------------------------------

ALTER TABLE "customerimage" ADD CONSTRAINT "CUSTOMERIMAGE$CUSTOMERIMAGE_IBFK_1" FOREIGN KEY ("Customer_id")
    REFERENCES "customer" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table customernote
--------------------------------------------------------

ALTER TABLE "customernote" ADD CONSTRAINT "CUSTOMERNOTE$FK_CUSTOMERNOTE_CUSTOMER" FOREIGN KEY ("Customer_id")
    REFERENCES "customer" ("id") ENABLE;
--------------------------------------------------------

--------------------------------------------------------
--  Ref Constraints for Table customerpreference
--------------------------------------------------------

ALTER TABLE "customerpreference" ADD CONSTRAINT "CUSTOMERPREFERENCE$FK_CUSTOMERPREFERENCE_CUSTOMER" FOREIGN KEY ("Customer_id")
    REFERENCES "customer" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table customerproduct
--------------------------------------------------------

ALTER TABLE "customerproduct" ADD CONSTRAINT "CUSTOMERPRODUCT$FK_CUSTOMERPRODUCT_CUSTOMER" FOREIGN KEY ("Customer_id")
    REFERENCES "customer" ("id") ENABLE;

ALTER TABLE "customerproduct" ADD CONSTRAINT "CUSTOMERPRODUCT$FK_CUSTOMERPRODUCT_PRODUCT" FOREIGN KEY ("Product_id")
    REFERENCES "product" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table customerrequest
--------------------------------------------------------

ALTER TABLE "customerrequest" ADD CONSTRAINT "CUSTOMERREQUEST$FK_CUSTOMERREQUEST_CUSTOMER" FOREIGN KEY ("Customer_id")
    REFERENCES "customer" ("id") ENABLE;

ALTER TABLE "customerrequest" ADD CONSTRAINT "CUSTOMERREQUEST$FK_CUSTOMERREQUEST_REQUESTCATEGORY" FOREIGN KEY ("RequestCategory_id")
    REFERENCES "requestcategory" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table customersecurityimages
--------------------------------------------------------

ALTER TABLE "customersecurityimages" ADD CONSTRAINT "CUSTOMERSECURITYIMAGES$FK_CUSTOMERSECUREIMAGES_USERID" FOREIGN KEY ("Customer_id")
    REFERENCES "customer" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table customersecurityquestions
--------------------------------------------------------

ALTER TABLE "customersecurityquestions" ADD CONSTRAINT "CUSTOMERSECURITYQUESTIONS$FK_CUSTOMERSECURITYQUESTION_CUSTOMERID" FOREIGN KEY ("Customer_id")
    REFERENCES "customer" ("id") ENABLE;

ALTER TABLE "customersecurityquestions" ADD CONSTRAINT "CUSTOMERSECURITYQUESTIONS$FK_CUSTOMERSECURITYQUESTION_QUESTIONID" FOREIGN KEY ("SecurityQuestion_id")
    REFERENCES "securityquestion" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table customerservice
--------------------------------------------------------

ALTER TABLE "customerservice" ADD CONSTRAINT "CUSTOMERSERVICE$FK_CUSTOMERSERVICE_SERVICETYPE" FOREIGN KEY ("Type_id")
    REFERENCES "servicetype" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table dayschedule
--------------------------------------------------------

ALTER TABLE "dayschedule" ADD CONSTRAINT "DAYSCHEDULE$FK_DAYSCHEDULE_WORKSCHEDULE" FOREIGN KEY ("WorkSchedule_id")
    REFERENCES "workschedule" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table dbxalertcategory
--------------------------------------------------------

ALTER TABLE "dbxalertcategory" ADD CONSTRAINT "FK_DBXALERTCATEGORY_ALERTFREQUENCY" FOREIGN KEY ("defaultFrequencyId")
    REFERENCES "alertfrequency" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table dbxalertcategorytext
--------------------------------------------------------

ALTER TABLE "dbxalertcategorytext" ADD CONSTRAINT "DBXALERTCATEGORYTEXT$FK_ALERTCATEGORYTEXT_LOCALE" FOREIGN KEY ("LanguageCode")
    REFERENCES "locale" ("Code") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table dbxalerttype
--------------------------------------------------------

ALTER TABLE "dbxalerttype" ADD CONSTRAINT "DBXALERTTYPE$FK_ALERTTYPE_ALERTCATEGORY" FOREIGN KEY ("AlertCategoryId")
    REFERENCES "dbxalertcategory" ("id") ENABLE;

ALTER TABLE "dbxalerttype" ADD CONSTRAINT "FK_DBXALERTTYPE_ALERTFREQUENCY" FOREIGN KEY ("defaultFrequencyId")
    REFERENCES "alertfrequency" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table dbxalerttypetext
--------------------------------------------------------

ALTER TABLE "dbxalerttypetext" ADD CONSTRAINT "DBXALERTTYPETEXT$FK_ALERTTYPETEXT_LOCALE" FOREIGN KEY ("LanguageCode")
    REFERENCES "locale" ("Code") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table dbxcustomeralertentitlement
--------------------------------------------------------

ALTER TABLE "dbxcustomeralertentitlement" ADD CONSTRAINT "DBXCUSTOMERALERTENTITLEMENT$FK_DBXCUSTOMERALERTENTITLEMENT_ALERTTYPE" FOREIGN KEY ("AlertTypeId")
    REFERENCES "dbxalerttype" ("id") ENABLE;

ALTER TABLE "dbxcustomeralertentitlement" ADD CONSTRAINT "DBXCUSTOMERALERTENTITLEMENT$FK_DBXCUSTOMERALERTENTITLEMENT_CUSTOMER" FOREIGN KEY ("Customer_id")
    REFERENCES "customer" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table dmaddinteractions
--------------------------------------------------------

ALTER TABLE "dmaddinteractions" ADD CONSTRAINT "DMADDINTERACTIONS$FK_DMADDINTERACTIONS_DMADVERTISEMENTS" FOREIGN KEY ("dm_add_id")
    REFERENCES "dmadvertisements" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table eventsubtype
--------------------------------------------------------

ALTER TABLE "eventsubtype" ADD CONSTRAINT "EVENTSUBTYPE$FK_EVENTTYPEID" FOREIGN KEY ("eventtypeid")
    REFERENCES "eventtype" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table eventtype
--------------------------------------------------------

ALTER TABLE "eventtype" ADD CONSTRAINT "EVENTTYPE$EVENTTYPE_ACTIVITYTYPE" FOREIGN KEY ("ActivityType")
    REFERENCES "eventactivitytype" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table externalbankidentity
--------------------------------------------------------

ALTER TABLE "externalbankidentity" ADD CONSTRAINT "EXTERNALBANKIDENTITY$FK_BANKID" FOREIGN KEY ("ExternalBank_id")
    REFERENCES "externalbank" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table faqs
--------------------------------------------------------

ALTER TABLE "faqs" ADD CONSTRAINT "FAQS$FK_FAQS_CATEGORY" FOREIGN KEY ("FaqCategory_Id")
    REFERENCES "faqcategory" ("id") ENABLE;

ALTER TABLE "faqs" ADD CONSTRAINT "FAQS$FK_FAQS_SERVICECHANNEL" FOREIGN KEY ("Channel_id")
    REFERENCES "servicechannel" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table favouriteinstruments
--------------------------------------------------------

ALTER TABLE "favouriteinstruments" ADD CONSTRAINT "FK_FAVOINSTRUMENTS_USERID" FOREIGN KEY ("userId")
    REFERENCES "customer" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table groupentitlement
--------------------------------------------------------

ALTER TABLE "groupentitlement" ADD CONSTRAINT "GROUPENTITLEMENT$FK_GROUPENTITLEMENTS_GROUP" FOREIGN KEY ("Group_id")
    REFERENCES "membergroup" ("id") ENABLE;

ALTER TABLE "groupentitlement" ADD CONSTRAINT "GROUPENTITLEMENT$FK_GROUPENTITLEMENTS_SERVICE" FOREIGN KEY ("Service_id")
    REFERENCES "service" ("id") ENABLE;

ALTER TABLE "groupentitlement" ADD CONSTRAINT "GROUPENTITLEMENT$FK_GROUPENTITLEMENT_TRANSACTIONFEE" FOREIGN KEY ("TransactionFee_id")
    REFERENCES "transactionfee" ("id") ENABLE;

ALTER TABLE "groupentitlement" ADD CONSTRAINT "GROUPENTITLEMENT$FK_GROUPENTITLEMENT_TRANSACTIONLIMIT" FOREIGN KEY ("TransactionLimit_id")
    REFERENCES "transactionlimit" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table locationservice
--------------------------------------------------------

ALTER TABLE "locationservice" ADD CONSTRAINT "LOCATIONSERVICE$FH_LOCATIONSERVICE_LOCATION_ID" FOREIGN KEY ("Location_id")
    REFERENCES "location" ("id") ENABLE;

ALTER TABLE "locationservice" ADD CONSTRAINT "LOCATIONSERVICE$FH_LOCATIONSERVICE_SERVICE_ID" FOREIGN KEY ("Service_id")
    REFERENCES "service" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table outagemessage
--------------------------------------------------------

ALTER TABLE "outagemessage" ADD CONSTRAINT "OUTAGEMESSAGE$FK_OUTAGEMESSAGE_SERVICE" FOREIGN KEY ("Service_id")
    REFERENCES "service" ("id") ENABLE;

ALTER TABLE "outagemessage" ADD CONSTRAINT "OUTAGEMESSAGE$FK_OUTAGEMESSAGE_SERVICECHANNEL" FOREIGN KEY ("Channel_id")
    REFERENCES "servicechannel" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table periodiclimit
--------------------------------------------------------

ALTER TABLE "periodiclimit" ADD CONSTRAINT "PERIODICLIMIT$FK_PERIODICLIMIT_PERIOD" FOREIGN KEY ("Period_id")
    REFERENCES "period" ("id") ENABLE;

ALTER TABLE "periodiclimit" ADD CONSTRAINT "PERIODICLIMIT$FK_PERIODICLIMIT_TRANSACTIONLIMIT" FOREIGN KEY ("TransactionLimit_id")
    REFERENCES "transactionlimit" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table privacypolicy
--------------------------------------------------------

ALTER TABLE "privacypolicy" ADD CONSTRAINT "PRIVACYPOLICY$FK_PRIVACYPOLICY_SERVICECHANNEL" FOREIGN KEY ("Channel_id")
    REFERENCES "servicechannel" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table requestmessage
--------------------------------------------------------

ALTER TABLE "requestmessage" ADD CONSTRAINT "REQUESTMESSAGE$FK_REQUESTMESSAGE_CUSTOMERREQUEST" FOREIGN KEY ("CustomerRequest_id")
    REFERENCES "customerrequest" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table rolecompositepermission
--------------------------------------------------------

ALTER TABLE "rolecompositepermission" ADD CONSTRAINT "ROLECOMPOSITEPERMISSION$FK_ROLECOMPOSITEPERMISSION_COMPOSITEPERMISSION" FOREIGN KEY ("CompositePermission_id")
    REFERENCES "compositepermission" ("id") ENABLE;

ALTER TABLE "rolecompositepermission" ADD CONSTRAINT "ROLECOMPOSITEPERMISSION$FK_ROLECOMPOSITEPERMISSION_ROLE" FOREIGN KEY ("Role_id")
    REFERENCES "role" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table rolepermission
--------------------------------------------------------

ALTER TABLE "rolepermission" ADD CONSTRAINT "ROLEPERMISSION$FK_ROLEPERMISSION_PERMISSION" FOREIGN KEY ("Permission_id")
    REFERENCES "permission" ("id") ENABLE;

ALTER TABLE "rolepermission" ADD CONSTRAINT "ROLEPERMISSION$FK_ROLEPERMISSION_ROLE" FOREIGN KEY ("Role_id")
    REFERENCES "role" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table servicecommunication
--------------------------------------------------------

ALTER TABLE "servicecommunication" ADD CONSTRAINT "SERVICECOMMUNICATION$FK_SERVICECOMMUNICATION_SERVICE" FOREIGN KEY ("Service_id")
    REFERENCES "customerservice" ("id") ENABLE;

ALTER TABLE "servicecommunication" ADD CONSTRAINT "SERVICECOMMUNICATION$FK_SERVICECOMMUNICATION_SERVICECHANNEL" FOREIGN KEY ("Type_id")
    REFERENCES "communicationtype" ("id") ENABLE;
    
--  Ref Constraints for Table transactionfeeslab
--------------------------------------------------------

ALTER TABLE "transactionfeeslab" ADD CONSTRAINT "TRANSACTIONFEESLAB$FK_TRANSACTIONFEESLAB_TRANSACTIONFEE" FOREIGN KEY ("TransactionFee_id")
    REFERENCES "transactionfee" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table transactiongroup
--------------------------------------------------------

ALTER TABLE "transactiongroup" ADD CONSTRAINT "TRANSACTIONGROUP$FK_TRANSACTIONGROUP_TRANSACTIONLIMIT" FOREIGN KEY ("TransactionLimit_id")
    REFERENCES "transactionlimit" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table transactiongroupservice
--------------------------------------------------------

ALTER TABLE "transactiongroupservice" ADD CONSTRAINT "TRANSACTIONGROUPSERVICE$FK_TRANSACTIONGROUPSERVICE_SERVICE" FOREIGN KEY ("Service_id")
    REFERENCES "service" ("id") ENABLE;

ALTER TABLE "transactiongroupservice" ADD CONSTRAINT "TRANSACTIONGROUPSERVICE$FK_TRANSACTIONGROUPSERVICE_TRANSACTIONGROUP" FOREIGN KEY ("TransactionGroup_id")
    REFERENCES "transactiongroup" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table useraddress
--------------------------------------------------------

ALTER TABLE "useraddress" ADD CONSTRAINT "USERADDRESS$FK_USERADDRESS_ADDRESS" FOREIGN KEY ("Address_id")
    REFERENCES "address" ("id") ENABLE;

ALTER TABLE "useraddress" ADD CONSTRAINT "USERADDRESS$FK_USERADDRESS_ADDRESSTYPE" FOREIGN KEY ("Type_id")
    REFERENCES "addresstype" ("id") ENABLE;

ALTER TABLE "useraddress" ADD CONSTRAINT "USERADDRESS$FK_USERADDRESS_SYSTEMUSER" FOREIGN KEY ("User_id")
    REFERENCES "systemuser" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table usercompositepermission
--------------------------------------------------------

ALTER TABLE "usercompositepermission" ADD CONSTRAINT "USERCOMPOSITEPERMISSION$FK_USERCOMPOSITEPERMISSION_COMPOSITEPERMISSION" FOREIGN KEY ("CompositePermission_id")
    REFERENCES "compositepermission" ("id") ENABLE;

ALTER TABLE "usercompositepermission" ADD CONSTRAINT "USERCOMPOSITEPERMISSION$FK_USERCOMPOSITEPERMISSION_USER" FOREIGN KEY ("User_id")
    REFERENCES "systemuser" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table userpermission
--------------------------------------------------------

ALTER TABLE "userpermission" ADD CONSTRAINT "USERPERMISSION$FK_USERPERMISSION_PERMISSIONID" FOREIGN KEY ("Permission_id")
    REFERENCES "permission" ("id") ENABLE;

ALTER TABLE "userpermission" ADD CONSTRAINT "USERPERMISSION$FK_USERPERMISSION_USERID" FOREIGN KEY ("User_id")
    REFERENCES "systemuser" ("id") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table userrole
--------------------------------------------------------

ALTER TABLE "userrole" ADD CONSTRAINT "USERROLE$FK_USERROLE_ROLE" FOREIGN KEY ("Role_id")
    REFERENCES "role" ("id") ENABLE;

ALTER TABLE "userrole" ADD CONSTRAINT "USERROLE$FK_USERROLE_SYSTEMUSER" FOREIGN KEY ("User_id")
    REFERENCES "systemuser" ("id") ENABLE;
--------------------------------------------------------





--  DDL for View accountransactionview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "accountransactionview" (
  "Account_id", 
  "AccountName", 
  "AccountHolder", 
  "UserName", 
  "ExternalBankidentity_id", 
  "CurrencyCode", 
  "User_id", 
  "AvailableBalance", 
  "Bank_id", 
  "ShowTransactions", 
  "CurrentBalance", 
  "RoutingNumber", 
  "transactionId", 
  "transactiontype", 
  "Customer_id", 
  "ExpenseCategory_id", 
  "Bill_id", 
  "Reference_id", 
  "fromAccountNumber", 
  "fromAccountBalance", 
  "toAccountNumber", 
  "toAccountBalance", 
  "amount", 
  "convertedAmount", 
  "transactionCurrency", 
  "baseCurrency", 
  "Status_id", 
  "statusDesc", 
  "isScheduled", 
  "category", 
  "billCategory", 
  "ExternalAccountNumber", 
  "Person_Id", 
  "frequencyType", 
  "createdDate", 
  "cashlessEmail", 
  "cashlessMode", 
  "cashlessOTP", 
  "cashlessOTPValidDate", 
  "cashlessPersonName", 
  "cashlessPhone", 
  "cashlessSecurityCode", 
  "cashWithdrawalTransactionStatus", 
  "frequencyEndDate", 
  "frequencyStartDate", 
  "hasDepositImage", 
  "payeeId", 
  "payeeName", 
  "p2pContact", 
  "personId", 
  "recurrenceDesc", 
  "numberOfRecurrences", 
  "scheduledDate", 
  "transactionComments", 
  "transactionsNotes", 
  "transDescription", 
  "transactionDate", 
  "postedDate", 
  "frontImage1", 
  "frontImage2", 
  "backImage1", 
  "backImage2", 
  "checkDesc", 
  "checkNumber1", 
  "checkNumber2", 
  "checkNumber", 
  "checkReason", 
  "requestValidity", 
  "checkDateOfIssue", 
  "bankName1", 
  "bankName2", 
  "withdrawlAmount1", 
  "withdrawlAmount2", 
  "cashAmount", 
  "payeeCurrency", 
  "fee", 
  "feePaidByReceipent", 
  "feeCurrency", 
  "isDisputed", 
  "disputeReason", 
  "disputeDescription", 
  "disputeDate", 
  "disputeStatus", 
  "description", 
  "statementReference", 
  "transCreditDebitIndicator", 
  "bookingDateTime", 
  "valueDateTime", 
  "transactionInformation", 
  "addressLine", 
  "transactionAmount", 
  "chargeAmount", 
  "chargeCurrency", 
  "sourceCurrency", 
  "targetCurrency", 
  "unitCurrency", 
  "exchangeRate", 
  "contractIdentification", 
  "quotationDate", 
  "instructedAmount", 
  "instructedCurrency", 
  "transactionCode", 
  "transactionSubCode", 
  "proprietaryTransactionCode", 
  "proprietaryTransactionIssuer", 
  "balanceCreditDebitIndicator", 
  "balanceType", 
  "balanceAmount", 
  "balanceCurrency", 
  "merchantName", 
  "merchantCategoryCode", 
  "creditorAgentSchemeName", 
  "creditorAgentIdentification", 
  "creditorAgentName", 
  "creditorAgentaddressType", 
  "creditorAgentDepartment", 
  "creditorAgentSubDepartment", 
  "creditorAgentStreetName", 
  "creditorAgentBuildingNumber", 
  "creditorAgentPostCode", 
  "creditorAgentTownName", 
  "creditorAgentCountrySubDivision", 
  "creditorAgentCountry", 
  "creditorAgentAddressLine", 
  "creditorAccountSchemeName", 
  "creditorAccountIdentification", 
  "creditorAccountName", 
  "creditorAccountSeconIdentification", 
  "debtorAgentSchemeName", 
  "debtorAgentIdentification", 
  "debtorAgentName", 
  "debtorAgentAddressType", 
  "debtorAgentDepartment", 
  "debtorAgentSubDepartment", 
  "debtorAgentStreetName", 
  "debtorAgentBuildingNumber", 
  "dedtorAgentPostCode", 
  "debtorAgentTownName", 
  "debtorAgentCountrySubDivision", 
  "debtorAgentCountry", 
  "debtorAgentAddressLine", 
  "debtorAccountSchemeName", 
  "debtorAccountIdentification", 
  "debtorAccountName", 
  "debtorAccountSeconIdentification", 
  "cardInstrumentSchemeName", 
  "cardInstrumentAuthorisationType", 
  "cardInstrumentName", 
  "cardInstrumentIdentification", 
  "FirstPaymentDateTime", 
  "NextPaymentDateTime", 
  "FinalPaymentDateTime", 
  "StandingOrderStatusCode", 
  "FP_Amount", 
  "FP_Currency", 
  "NP_Amount", 
  "NP_Currency", 
  "FPA_Amount", 
  "FPA_Currency", 
  "IBAN", 
  "sortCode", 
  "beneficiaryName", 
  "bankName", 
  "swiftCode", 
  "nickName"
) AS 
SELECT 
  "accounts"."Account_id" "Account_id", 
  "accounts"."AccountName" "AccountName", 
  "accounts"."AccountHolder" "AccountHolder", 
  "accounts"."UserName" "UserName", 
  "accounts"."ExternalBankidentity_id" "ExternalBankidentity_id", 
  "accounts"."CurrencyCode" "CurrencyCode", 
  "accounts"."User_id" "User_id", 
  "accounts"."AvailableBalance" "AvailableBalance", 
  "accounts"."Bank_id" "Bank_id", 
  "accounts"."ShowTransactions" "ShowTransactions", 
  "accounts"."CurrentBalance" "CurrentBalance", 
  "accounts"."RoutingNumber" "RoutingNumber", 
  "transaction"."Id" "transactionId", 
  "transaction"."Type_id" "transactiontype", 
  "transaction"."Customer_id" "Customer_id", 
  "transaction"."ExpenseCategory_id" "ExpenseCategory_id", 
  "transaction"."billid" "Bill_id", 
  "transaction"."Reference_id" "Reference_id", 
  "transaction"."fromAccountNumber" "fromAccountNumber", 
  "transaction"."fromAccountBalance" "fromAccountBalance", 
  "transaction"."toAccountNumber" "toAccountNumber", 
  "transaction"."toAccountBalance" "toAccountBalance", 
  "transaction"."amount" "amount", 
  "transaction"."convertedAmount" "convertedAmount", 
  "transaction"."transactionCurrency" "transactionCurrency", 
  "transaction"."baseCurrency" "baseCurrency", 
  "transaction"."Status_id" "Status_id", 
  "transaction"."statusDesc" "statusDesc", 
  "transaction"."isScheduled" "isScheduled", 
  "transaction"."category" "category", 
  "transaction"."billCategory" "billCategory", 
  "transaction"."toExternalAccountNumber" "ExternalAccountNumber", 
  "transaction"."Person_Id" "Person_Id", 
  "transaction"."frequencyType" "frequencyType", 
  "transaction"."createdDate" "createdDate", 
  "transaction"."cashlessEmail" "cashlessEmail", 
  "transaction"."cashlessMode" "cashlessMode", 
  "transaction"."cashlessOTP" "cashlessOTP", 
  "transaction"."cashlessOTPValidDate" "cashlessOTPValidDate", 
  "transaction"."cashlessPersonName" "cashlessPersonName", 
  "transaction"."cashlessPhone" "cashlessPhone", 
  "transaction"."cashlessSecurityCode" "cashlessSecurityCode", 
  "transaction"."cashWithdrawalTransactionStatus" "cashWithdrawalTransactionStatus", 
  "transaction"."frequencyEndDate" "frequencyEndDate", 
  "transaction"."frequencyStartDate" "frequencyStartDate", 
  "transaction"."hasDepositImage" "hasDepositImage", 
  "transaction"."Payee_id" "payeeId", 
  "transaction"."payeeName" "payeeName", 
  "transaction"."p2pContact" "p2pContact", 
  "transaction"."Person_Id" "personId", 
  "transaction"."recurrenceDesc" "recurrenceDesc", 
  "transaction"."numberOfRecurrences" "numberOfRecurrences", 
  "transaction"."scheduledDate" "scheduledDate", 
  "transaction"."transactionComments" "transactionComments", 
  "transaction"."notes" "transactionsNotes", 
  "transaction"."description" "transDescription", 
  "transaction"."transactionDate" "transactionDate", 
  "transaction"."postedDate" "postedDate", 
  "transaction"."frontImage1" "frontImage1", 
  "transaction"."frontImage2" "frontImage2", 
  "transaction"."backImage1" "backImage1", 
  "transaction"."backImage2" "backImage2", 
  "transaction"."checkDesc" "checkDesc", 
  "transaction"."checkNumber1" "checkNumber1", 
  "transaction"."checkNumber2" "checkNumber2", 
  "transaction"."checkNumber" "checkNumber", 
  "transaction"."checkReason" "checkReason", 
  "transaction"."requestValidity" "requestValidity", 
  "transaction"."checkDateOfIssue" "checkDateOfIssue", 
  "transaction"."bankName1" "bankName1", 
  "transaction"."bankName2" "bankName2", 
  "transaction"."withdrawlAmount1" "withdrawlAmount1", 
  "transaction"."withdrawlAmount2" "withdrawlAmount2", 
  "transaction"."cashAmount" "cashAmount", 
  "transaction"."payeeCurrency" "payeeCurrency", 
  "transaction"."fee" "fee", 
  "transaction"."feePaidByReceipent" "feePaidByReceipent", 
  "transaction"."feeCurrency" "feeCurrency", 
  "transaction"."isDisputed" "isDisputed", 
  "transaction"."disputeReason" "disputeReason", 
  "transaction"."disputeDescription" "disputeDescription", 
  "transaction"."disputeDate" "disputeDate", 
  "transaction"."disputeStatus" "disputeStatus", 
  "transactiontype"."description" "description", 
  "transaction"."statementReference" "statementReference", 
  "transaction"."transCreditDebitIndicator" "transCreditDebitIndicator", 
  "transaction"."bookingDateTime" "bookingDateTime", 
  "transaction"."valueDateTime" "valueDateTime", 
  "transaction"."transactionInformation" "transactionInformation", 
  "transaction"."addressLine" "addressLine", 
  "transaction"."transactionAmount" "transactionAmount", 
  "transaction"."chargeAmount" "chargeAmount", 
  "transaction"."chargeCurrency" "chargeCurrency", 
  "transaction"."sourceCurrency" "sourceCurrency", 
  "transaction"."targetCurrency" "targetCurrency", 
  "transaction"."unitCurrency" "unitCurrency", 
  "transaction"."exchangeRate" "exchangeRate", 
  "transaction"."contractIdentification" "contractIdentification", 
  "transaction"."quotationDate" "quotationDate", 
  "transaction"."instructedAmount" "instructedAmount", 
  "transaction"."instructedCurrency" "instructedCurrency", 
  "transaction"."transactionCode" "transactionCode", 
  "transaction"."transactionSubCode" "transactionSubCode", 
  "transaction"."proprietaryTransactionCode" "proprietaryTransactionCode", 
  "transaction"."proprietaryTransactionIssuer" "proprietaryTransactionIssuer", 
  "transaction"."balanceCreditDebitIndicator" "balanceCreditDebitIndicator", 
  "transaction"."balanceType" "balanceType", 
  "transaction"."balanceAmount" "balanceAmount", 
  "transaction"."balanceCurrency" "balanceCurrency", 
  "transaction"."merchantName" "merchantName", 
  "transaction"."merchantCategoryCode" "merchantCategoryCode", 
  "transaction"."creditorAgentSchemeName" "creditorAgentSchemeName", 
  "transaction"."creditorAgentIdentification" "creditorAgentIdentification", 
  "transaction"."creditorAgentName" "creditorAgentName", 
  "transaction"."creditorAgentaddressType" "creditorAgentaddressType", 
  "transaction"."creditorAgentDepartment" "creditorAgentDepartment", 
  "transaction"."creditorAgentSubDepartment" "creditorAgentSubDepartment", 
  "transaction"."creditorAgentStreetName" "creditorAgentStreetName", 
  "transaction"."creditorAgentBuildingNumber" "creditorAgentBuildingNumber", 
  "transaction"."creditorAgentPostCode" "creditorAgentPostCode", 
  "transaction"."creditorAgentTownName" "creditorAgentTownName", 
  "transaction"."creditorAgentCountrySubDivision" "creditorAgentCountrySubDivision", 
  "transaction"."creditorAgentCountry" "creditorAgentCountry", 
  "transaction"."creditorAgentAddressLine" "creditorAgentAddressLine", 
  "transaction"."creditorAccountSchemeName" "creditorAccountSchemeName", 
  "transaction"."creditorAccountIdentification" "creditorAccountIdentification", 
  "transaction"."creditorAccountName" "creditorAccountName", 
  "transaction"."creditorAccountSeconIdentification" "creditorAccountSeconIdentification", 
  "transaction"."debtorAgentSchemeName" "debtorAgentSchemeName", 
  "transaction"."debtorAgentIdentification" "debtorAgentIdentification", 
  "transaction"."debtorAgentName" "debtorAgentName", 
  "transaction"."debtorAgentAddressType" "debtorAgentAddressType", 
  "transaction"."debtorAgentDepartment" "debtorAgentDepartment", 
  "transaction"."debtorAgentSubDepartment" "debtorAgentSubDepartment", 
  "transaction"."debtorAgentStreetName" "debtorAgentStreetName", 
  "transaction"."debtorAgentBuildingNumber" "debtorAgentBuildingNumber", 
  "transaction"."dedtorAgentPostCode" "dedtorAgentPostCode", 
  "transaction"."debtorAgentTownName" "debtorAgentTownName", 
  "transaction"."debtorAgentCountrySubDivision" "debtorAgentCountrySubDivision", 
  "transaction"."debtorAgentCountry" "debtorAgentCountry", 
  "transaction"."debtorAgentAddressLine" "debtorAgentAddressLine", 
  "transaction"."debtorAccountSchemeName" "debtorAccountSchemeName", 
  "transaction"."debtorAccountIdentification" "debtorAccountIdentification", 
  "transaction"."debtorAccountName" "debtorAccountName", 
  "transaction"."debtorAccountSeconIdentification" "debtorAccountSeconIdentification", 
  "transaction"."cardInstrumentSchemeName" "cardInstrumentSchemeName", 
  "transaction"."cardInstrumentAuthorisationType" "cardInstrumentAuthorisationType", 
  "transaction"."cardInstrumentName" "cardInstrumentName", 
  "transaction"."cardInstrumentIdentification" "cardInstrumentIdentification", 
  "transaction"."FirstPaymentDateTime" "FirstPaymentDateTime", 
  "transaction"."NextPaymentDateTime" "NextPaymentDateTime", 
  "transaction"."FinalPaymentDateTime" "FinalPaymentDateTime", 
  "transaction"."StandingOrderStatusCode" "StandingOrderStatusCode", 
  "transaction"."FP_Amount" "FP_Amount", 
  "transaction"."FP_Currency" "FP_Currency", 
  "transaction"."NP_Amount" "NP_Amount", 
  "transaction"."NP_Currency" "NP_Currency", 
  "transaction"."FPA_Amount" "FPA_Amount", 
  "transaction"."FPA_Currency" "FPA_Currency", 
  "transaction"."IBAN" "IBAN", 
  "transaction"."sortCode" "sortCode", 
  "transaction"."beneficiaryName" "beneficiaryName", 
  "transaction"."bankName" "bankName", 
  "transaction"."swiftCode" "swiftCode", 
  "accounts"."NickName" "nickName" 
FROM 
  "accounts" 
  JOIN "transaction" ON (
    (
      "accounts"."Account_id" = "transaction"."fromAccountNumber"
    ) 
    OR (
      "accounts"."Account_id" = "transaction"."toAccountNumber"
    )
  ) 
  JOIN "transactiontype" ON "transaction"."Type_id" = "transactiontype"."id" ;
  --  DDL for View accountstatementview
  
CREATE 
  OR REPLACE FORCE NONEDITIONABLE VIEW "accountstatementview" (
    "Type_id", "description", "Statementlink", 
    "Account_id", "month"
  ) AS 
SELECT 
  "accounts"."Type_id" "Type_id", 
  "accountstatement"."description" "description", 
  "accountstatement"."statementLink" "Statementlink", 
  "accountstatement"."Account_id" "Account_id", 
  "accountstatement"."month" "month" 
FROM 
  "accounts" 
  JOIN "accountstatement" ON "accounts"."Account_id" = "accountstatement"."Account_id" ;
  --  DDL for View accountsview
  
CREATE 
  OR REPLACE FORCE NONEDITIONABLE VIEW "accountsview" (
    "ExternalBankidentity_id", "MainUser_id", 
    "User_id", "Password", "SessionToken", 
    "BankName", "logo", "ExternalBank_id", 
    "Account_id", "AccountName", "CurrencyCode", 
    "AvailableBalance", "AccountHolder", 
    "Address", "Scheme", "Number", "error", 
    "LastUpdated", "InternalAccount", 
    "Type_id", "NickName", "FavouriteStatus", 
    "TypeDescription"
  ) AS 
SELECT 
  "accounts"."ExternalBankidentity_id" "ExternalBankidentity_id", 
  "externalbankidentity"."MainUser_id" "MainUser_id", 
  "externalbankidentity"."User_id" "User_id", 
  "externalbankidentity"."Password" "Password", 
  "externalbankidentity"."SessionToken" "SessionToken", 
  "externalbank"."BankName" "BankName", 
  "externalbank"."logo" "logo", 
  "externalbankidentity"."ExternalBank_id" "ExternalBank_id", 
  "accounts"."Account_id" "Account_id", 
  "accounts"."AccountName" "AccountName", 
  "accounts"."CurrencyCode" "CurrencyCode", 
  "accounts"."AvailableBalance" "AvailableBalance", 
  "accounts"."AccountHolder" "AccountHolder", 
  "accounts"."Address" "Address", 
  "accounts"."Scheme" "Scheme", 
  "accounts"."Number" "Number", 
  "accounts"."error" "error", 
  "accounts"."LastUpdated" "LastUpdated", 
  "accounts"."InternalAccount" "InternalAccount", 
  "accounts"."Type_id" "Type_id", 
  "accounts"."NickName" "NickName", 
  "accounts"."FavouriteStatus" "FavouriteStatus", 
  "accounttype"."TypeDescription" "TypeDescription" 
FROM 
  "accounts" 
  JOIN "externalbankidentity" ON "accounts"."ExternalBankidentity_id" = "externalbankidentity"."id" 
  JOIN "externalbank" ON "externalbankidentity"."ExternalBank_id" = "externalbank"."id" 
  JOIN "accounttype" ON "accounts"."Type_id" = "accounttype"."TypeID"; 


--  DDL for View alertcustomerchannels_view_alertcategorylevel
  
CREATE OR REPLACE FORCE NONEDITIONABLE VIEW "alertcustomerchannels_view_alertcategorylevel" ("AlertSubTypeId", "Customer_id", "AccountId", "AccountType", "Value1", "Value2", "ChannelId") AS 
  SELECT "alertsubtype"."id" "AlertSubTypeId"  , 
          "dbxcustomeralertentitlement"."Customer_id" "Customer_id"  , 
          "dbxcustomeralertentitlement"."AccountId" "AccountId"  , 
          "dbxcustomeralertentitlement"."AccountType" "AccountType"  , 
          "dbxcustomeralertentitlement"."Value1" "Value1"  , 
          "dbxcustomeralertentitlement"."Value2" "Value2"  , 
          "customeralertchannel"."channelId" "ChannelId"   
     FROM ( ( ( "dbxcustomeralertentitlement"  
                JOIN "customeralertchannel"    ON ( ( ( "dbxcustomeralertentitlement"."Customer_id" = "customeralertchannel"."customerId" ) 
                AND ( "dbxcustomeralertentitlement"."AccountId" = "customeralertchannel"."accountId" ) 
                AND ( "dbxcustomeralertentitlement"."AccountType" = "customeralertchannel"."accountType" ) 
                AND ( "dbxcustomeralertentitlement"."alertCategoryId" = "customeralertchannel"."alertCategoryId" ) ) ) 
                 )  
              JOIN "dbxalerttype"    ON ( ( "dbxcustomeralertentitlement"."alertCategoryId" = "dbxalerttype"."AlertCategoryId" ) ) 
               )  
            JOIN "alertsubtype"    ON ( ( "dbxalerttype"."id" = "alertsubtype"."AlertTypeId" ) ) 
             );


--  DDL for View alertcustomerchannels_view_alertgrouplevel

CREATE OR REPLACE FORCE NONEDITIONABLE VIEW "alertcustomerchannels_view_alertgrouplevel" ("AlertSubTypeId", "Customer_id", "AccountId", "AccountType", "Value1", "Value2", "ChannelId") AS 
  SELECT "alertsubtype"."id" "AlertSubTypeId"  , 
          "dbxcustomeralertentitlement"."Customer_id" "Customer_id"  , 
          "dbxcustomeralertentitlement"."AccountId" "AccountId"  , 
          "dbxcustomeralertentitlement"."AccountType" "AccountType"  , 
          "dbxcustomeralertentitlement"."Value1" "Value1"  , 
          "dbxcustomeralertentitlement"."Value2" "Value2"  , 
          "customeralertchannel"."channelId" "ChannelId"   
     FROM ( ( "dbxcustomeralertentitlement"  
              JOIN "customeralertchannel"    ON ( ( ( "dbxcustomeralertentitlement"."Customer_id" = "customeralertchannel"."customerId" ) 
              AND ( "dbxcustomeralertentitlement"."AccountId" = "customeralertchannel"."accountId" ) 
              AND ( "dbxcustomeralertentitlement"."AccountType" = "customeralertchannel"."accountType" ) 
              AND ( "dbxcustomeralertentitlement"."AlertTypeId" = "customeralertchannel"."alertTypeId" ) 
              AND ( "dbxcustomeralertentitlement"."alertCategoryId" = "customeralertchannel"."alertCategoryId" ) ) ) 
               )  
            JOIN "alertsubtype"    ON ( ( "dbxcustomeralertentitlement"."AlertTypeId" = "alertsubtype"."AlertTypeId" ) ) 
             );
--  DDL for View alertcustomerchannels_view_alertlevel

CREATE OR REPLACE FORCE NONEDITIONABLE VIEW "alertcustomerchannels_view_alertlevel" ("AlertSubTypeId", "Customer_id", "AccountId", "AccountType", "Value1", "Value2", "ChannelId") AS 
  SELECT "dbxcustomeralertentitlement"."alertSubTypeId" "AlertSubTypeId"  , 
          "dbxcustomeralertentitlement"."Customer_id" "Customer_id"  , 
          "dbxcustomeralertentitlement"."AccountId" "AccountId"  , 
          "dbxcustomeralertentitlement"."AccountType" "AccountType"  , 
          "dbxcustomeralertentitlement"."Value1" "Value1"  , 
          "dbxcustomeralertentitlement"."Value2" "Value2"  , 
          "customeralertchannel"."channelId" "ChannelId"   
     FROM ( "dbxcustomeralertentitlement"  
            JOIN "customeralertchannel"    ON ( ( ( "dbxcustomeralertentitlement"."Customer_id" = "customeralertchannel"."customerId" ) 
            AND ( "dbxcustomeralertentitlement"."AccountId" = "customeralertchannel"."accountId" ) 
            AND ( "dbxcustomeralertentitlement"."AccountType" = "customeralertchannel"."accountType" ) 
            AND ( "dbxcustomeralertentitlement"."alertSubTypeId" = "customeralertchannel"."alertSubTypeId" ) 
            AND ( "dbxcustomeralertentitlement"."alertCategoryId" = "customeralertchannel"."alertCategoryId" ) 
            AND ( "dbxcustomeralertentitlement"."AlertTypeId" = "customeralertchannel"."alertTypeId" ) ) ) 
             );

--  DDL for View alertfrequency_view

CREATE OR REPLACE FORCE NONEDITIONABLE VIEW "alertfrequency_view" ("alertfrequency_id", "alertfrequency_sequence", "alertfrequencytext_languageCode", "alertfrequencytext_description", "alertfrequencytext_displayName") AS 
  SELECT "alertfrequency"."id" "alertfrequency_id"  , 
          "alertfrequency"."sequence" "alertfrequency_sequence"  , 
          "alertfrequencytext"."languageCode" "alertfrequencytext_languageCode"  , 
          "alertfrequencytext"."description" "alertfrequencytext_description"  , 
          "alertfrequencytext"."displayName" "alertfrequencytext_displayName"   
     FROM ( "alertfrequencytext"  
            JOIN "alertfrequency"    ON ( ( "alertfrequencytext"."alertFrequencyId" = "alertfrequency"."id" ) ) 
             );
--  DDL for View alerts_fetch_globaldata_view_alertcategorylevel

CREATE OR REPLACE FORCE NONEDITIONABLE VIEW "alerts_fetch_globaldata_view_alertcategorylevel" ("AlertTypeId", "AlertCategoryId", "alertGroupName", "AttributeId", "AlertConditionId", "Value1", "Value2", "alertName", "alerttype_status_id", "IsGlobal", "alertcategory_status_id", "ChannelId", "AlertSubTypeId", "alertsubtypetype_status_id", "accountLevel", "externalSystem") AS 
  SELECT "dbxalerttype"."id" "AlertTypeId"  , 
          "dbxalerttype"."AlertCategoryId" "AlertCategoryId"  , 
          "dbxalerttype"."Name" "alertGroupName"  , 
          "alertsubtype"."attributeId" "AttributeId"  , 
          "alertsubtype"."alertConditionId" "AlertConditionId"  , 
          "alertsubtype"."value1" "Value1"  , 
          "alertsubtype"."value2" "Value2"  , 
          "alertsubtype"."Name" "alertName"  , 
          "dbxalerttype"."Status_id" "alerttype_status_id"  , 
          "alertsubtype"."isGlobal" "IsGlobal"  , 
          "dbxalertcategory"."status_id" "alertcategory_status_id"  , 
          "alertcategorychannel"."ChannelID" "ChannelId"  , 
          "alertsubtype"."id" "AlertSubTypeId"  , 
          "alertsubtype"."Status_id" "alertsubtypetype_status_id"  , 
          "alertsubtype"."isAccountLevel" "accountLevel"  , 
          "alertsubtype"."externalSystem" "externalSystem"   
     FROM ( ( ( "dbxalertcategory"  
                JOIN "alertcategorychannel"    ON ( ( "alertcategorychannel"."AlertCategoryId" = "dbxalertcategory"."id" ) ) 
                 )  
              JOIN "dbxalerttype"    ON ( ( "dbxalerttype"."AlertCategoryId" = "dbxalertcategory"."id" ) ) 
               )  
            JOIN "alertsubtype"    ON ( ( "alertsubtype"."AlertTypeId" = "dbxalerttype"."id" ) ) 
             );


--  DDL for View alerts_fetch_globaldata_view_alertgrouplevel

CREATE OR REPLACE FORCE NONEDITIONABLE VIEW "alerts_fetch_globaldata_view_alertgrouplevel" ("AlertTypeId", "AlertCategoryId", "alertGroupName", "companyLegalUnit", "AttributeId", "AlertConditionId", "Value1", "Value2", "alertName", "alerttype_status_id", "IsGlobal", "alertcategory_status_id", "ChannelId", "AlertSubTypeId", "alertsubtypetype_status_id", "accountLevel", "externalSystem") AS 
  SELECT "dbxalerttype"."id" "AlertTypeId"  , 
          "dbxalerttype"."AlertCategoryId" "AlertCategoryId"  , 
          "dbxalerttype"."Name" "alertGroupName"  , 
          NULL "companyLegalUnit"  ,
          "alertsubtype"."attributeId" "AttributeId"  , 
          "alertsubtype"."alertConditionId" "AlertConditionId"  , 
          "alertsubtype"."value1" "Value1"  , 
          "alertsubtype"."value2" "Value2"  , 
          "alertsubtype"."Name" "alertName"  , 
          "dbxalerttype"."Status_id" "alerttype_status_id"  , 
          "alertsubtype"."isGlobal" "IsGlobal"  , 
          "dbxalertcategory"."status_id" "alertcategory_status_id"  , 
          "alerttypechannel"."channelId" "ChannelId"  , 
          "alertsubtype"."id" "AlertSubTypeId"  , 
          "alertsubtype"."Status_id" "alertsubtypetype_status_id"  , 
          "alertsubtype"."isAccountLevel" "accountLevel"  , 
          "alertsubtype"."externalSystem" "externalSystem"   
     FROM ( ( ( "dbxalerttype"  
                JOIN "alerttypechannel"    ON ( ( "dbxalerttype"."id" = "alerttypechannel"."alertTypeId" ) ) 
                 )  
              JOIN "alertsubtype"    ON ( ( "dbxalerttype"."id" = "alertsubtype"."AlertTypeId" ) ) 
               )  
            JOIN "dbxalertcategory"    ON ( ( "dbxalerttype"."AlertCategoryId" = "dbxalertcategory"."id" ) ) 
             );


--  DDL for View alerts_fetch_globaldata_view_alertlevel

CREATE OR REPLACE FORCE NONEDITIONABLE VIEW "alerts_fetch_globaldata_view_alertlevel" ("AlertTypeId", "AlertCategoryId", "alertGroupName", "AttributeId", "AlertConditionId", "Value1", "Value2", "alertName", "alerttype_status_id", "IsGlobal", "alertcategory_status_id", "ChannelId", "AlertSubTypeId", "alertsubtypetype_status_id", "accountLevel", "externalSystem") AS 
  SELECT "dbxalerttype"."id" "AlertTypeId"  , 
          "dbxalerttype"."AlertCategoryId" "AlertCategoryId"  , 
          "dbxalerttype"."Name" "alertGroupName"  , 
          "alertsubtype"."attributeId" "AttributeId"  , 
          "alertsubtype"."alertConditionId" "AlertConditionId"  , 
          "alertsubtype"."value1" "Value1"  , 
          "alertsubtype"."value2" "Value2"  , 
          "alertsubtype"."Name" "alertName"  , 
          "dbxalerttype"."Status_id" "alerttype_status_id"  , 
          "alertsubtype"."isGlobal" "IsGlobal"  , 
          "dbxalertcategory"."status_id" "alertcategory_status_id"  , 
          "alertsubtypechannel"."channelId" "ChannelId"  , 
          "alertsubtype"."id" "AlertSubTypeId"  , 
          "alertsubtype"."Status_id" "alertsubtypetype_status_id"  , 
          "alertsubtype"."isAccountLevel" "accountLevel"  , 
          "alertsubtype"."externalSystem" "externalSystem"   
     FROM ( ( ( "alertsubtype"  
                JOIN "alertsubtypechannel"    ON ( ( "alertsubtype"."id" = "alertsubtypechannel"."alertSubTypeId" ) ) 
                 )  
              JOIN "dbxalerttype"    ON ( ( "alertsubtype"."AlertTypeId" = "dbxalerttype"."id" ) ) 
               )  
            JOIN "dbxalertcategory"    ON ( ( "dbxalerttype"."AlertCategoryId" = "dbxalertcategory"."id" ) ) 
             );

--  DDL for View allaccountsview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "allaccountsview" (
  "AccountHolder", "Account_id", "AccountName", 
  "Type_id", "IsOrganizationAccount", 
  "ownership", "accountStatus", "accountType", 
  "Membership_id", "Taxid", "phoneNumber", 
  "emailId", "name"
) AS 
SELECT 
  DISTINCT "accounts"."AccountHolder" "AccountHolder", 
  "accounts"."Account_id" "Account_id", 
  "accounts"."AccountName" "AccountName", 
  "accounts"."Type_id" "Type_id", 
  "accounts"."isBusinessAccount" "IsOrganizationAccount", 
  "accounts"."ownership" "ownership", 
  "accounts"."StatusDesc" "accountStatus", 
  "accounttype"."TypeDescription" "accountType", 
  "membershipaccounts"."membershipId" "Membership_id", 
  "membership"."taxId" "Taxid", 
  "membership"."phone" "phoneNumber", 
  "membership"."email" "emailId", 
  "membership"."name" "name" 
FROM 
  (
    (
      (
        "accounts" 
        LEFT JOIN "membershipaccounts" ON (
          (
            "accounts"."Account_id" = "membershipaccounts"."accountId"
          )
        )
      ) 
      LEFT JOIN "accounttype" ON (
        (
          "accounts"."Type_id" = "accounttype"."TypeID"
        )
      )
    ) 
    LEFT JOIN "membership" ON (
      (
        "membership"."id" = "membershipaccounts"."membershipId"
      )
    )
  );
--  DDL for View bankbranchview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "bankbranchview" (
  "id", "address1", "address2", "city", 
  "state", "zipCode", "phone", "workingHours", 
  "services", "latitude", "longitude", 
  "status", "email", "Type_id", "Type"
) AS 
SELECT 
  bb."id" "id", 
  bb."address1" "address1", 
  bb."address2" "address2", 
  bb."city" "city", 
  bb."state" "state", 
  bb."zipCode" "zipCode", 
  bb."phone" "phone", 
  bb."workingHours" "workingHours", 
  bb."services" "services", 
  bb."latitude" "latitude", 
  bb."longitude" "longitude", 
  bb."status" "status", 
  bb."email" "email", 
  bb."Type_id" "Type_id", 
  bt."Type" "Type" 
FROM 
  (
    "bankbranch" bb CROSS 
    JOIN "branchtype" bt
  ) 
WHERE 
  (bb."Type_id" = bt."id");
--  DDL for View billermasterview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "billermasterview" (
  "accountNumber", "address", "billerCategoryId", 
  "billerName", "city", "ebillSupport", 
  "id", "state", "zipCode", "billerCategoryName"
) AS 
SELECT 
  bb."accountNumber" "accountNumber", 
  bb."address" "address", 
  bb."billerCategoryId" "billerCategoryId", 
  bb."billerName" "billerName", 
  bb."city" "city", 
  bb."ebillSupport" "ebillSupport", 
  bb."id" "id", 
  bb."state" "state", 
  bb."zipCode" "zipCode", 
  bc."categoryName" "billerCategoryName" 
FROM 
  (
    "billermaster" bb CROSS 
    JOIN "billercategory" bc
  ) 
WHERE 
  (bb."billerCategoryId" = bc."id");
--  DDL for View billview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "billview" (
  "balanceAmount", "billDueDate", "billGeneratedDate", 
  "description", "dueAmount", "ebillURL", 
  "id", "paidAmount", "paidDate", "payeeId", 
  "currencyCode", "fromAccountName", 
  "fromAccountNumber", "User_id", 
  "payeeName", "softDelete", "ebillStatus", 
  "payeeNickName", "payeeAddressLine1", 
  "billerCategory", "billerCategoryId", 
  "ebillSupport"
) AS 
SELECT 
  bb."balanceAmount" "balanceAmount", 
  bb."billDueDate" "billDueDate", 
  bb."billGeneratedDate" "billGeneratedDate", 
  bb."description" "description", 
  bb."dueAmount" "dueAmount", 
  bb."ebillURL" "ebillURL", 
  bb."id" "id", 
  bb."paidAmount" "paidAmount", 
  bb."paidDate" "paidDate", 
  bb."Payee_id" "payeeId", 
  bb."currencyCode" "currencyCode", 
  ac."AccountName" "fromAccountName", 
  ac."Account_id" "fromAccountNumber", 
  ac."Customer_id" "User_id", 
  py."name" "payeeName", 
  py."softDelete" "softDelete", 
  py."eBillEnable" "ebillStatus", 
  py."nickName" "payeeNickName", 
  py."addressLine1" "payeeAddressLine1", 
  bc."categoryName" "billerCategory", 
  bm."billerCategoryId" "billerCategoryId", 
  bm."ebillSupport" "ebillSupport" 
FROM 
  (
    (
      (
        (
          "bill" bb CROSS 
          JOIN "payee" py
        ) CROSS 
        JOIN "customeraccounts" ac
      ) CROSS 
      JOIN "billercategory" bc
    ) CROSS 
    JOIN "billermaster" bm
  ) 
WHERE 
  (
    (bb."Payee_id" = py."Id") 
    AND (bb."billerMaster_id" = bm."id") 
    AND (
      bb."Account_id" = ac."Account_id"
    ) 
    AND (bm."billerCategoryId" = bc."id")
  );
--  DDL for View cardproductsview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "cardproductsview" (
  "productId", "productName", "accountType", 
  "featureOverview", "featureDescription", 
  "representativeLabel1", "representativeLabel2", 
  "representativeLabel3", "representativeValue1", 
  "representativeValue2", "representativeValue3", 
  "withdrawlLimit", "withdrawalMinLimit", 
  "withdrawalMaxLimit", "withdrawalStepLimit", 
  "purchaseLimit", "purchaseMinLimit", 
  "purchaseMaxLimit", "purchaseStepLimit"
) AS 
SELECT 
  "cardproducts"."productId" "productId", 
  "cardproducts"."productName" "productName", 
  "cardproducttype"."accountType" "accountType", 
  "cardproducts"."featureOverview" "featureOverview", 
  "cardproducts"."featureDescription" "featureDescription", 
  "cardproducts"."representativeLabel1" "representativeLabel1", 
  "cardproducts"."representativeLabel2" "representativeLabel2", 
  "cardproducts"."representativeLabel3" "representativeLabel3", 
  "cardproducts"."representativeValue1" "representativeValue1", 
  "cardproducts"."representativeValue2" "representativeValue2", 
  "cardproducts"."representativeValue3" "representativeValue3", 
  "cardproducts"."withdrawlLimit" "withdrawlLimit", 
  "cardproducts"."withdrawalMinLimit" "withdrawalMinLimit", 
  "cardproducts"."withdrawalMaxLimit" "withdrawalMaxLimit", 
  "cardproducts"."withdrawalStepLimit" "withdrawalStepLimit", 
  "cardproducts"."purchaseLimit" "purchaseLimit", 
  "cardproducts"."purchaseMinLimit" "purchaseMinLimit", 
  "cardproducts"."purchaseMaxLimit" "purchaseMaxLimit", 
  "cardproducts"."purchaseStepLimit" "purchaseStepLimit" 
FROM 
  "cardproducts" 
  JOIN "cardproducttype" ON (
    "cardproducttype"."productId" = "cardproducts"."productId"
  );
--  DDL for View channel_view

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "channel_view" (
  "channel_id", "channel_status_id", 
  "channel_sequence", "channeltext_LanguageCode", 
  "channeltext_Description", "channeltext_createdby", 
  "channeltext_modifiedby", "channeltext_createdts", 
  "channeltext_lastmodifiedts", "channeltext_synctimestamp", 
  "channeltext_softdeleteflag"
) AS 
SELECT 
  "channel"."id" "channel_id", 
  "channel"."status_id" "channel_status_id", 
  "channel"."sequence" "channel_sequence", 
  "channeltext"."LanguageCode" "channeltext_LanguageCode", 
  "channeltext"."Description" "channeltext_Description", 
  "channeltext"."createdby" "channeltext_createdby", 
  "channeltext"."modifiedby" "channeltext_modifiedby", 
  "channeltext"."createdts" "channeltext_createdts", 
  "channeltext"."lastmodifiedts" "channeltext_lastmodifiedts", 
  "channeltext"."synctimestamp" "channeltext_synctimestamp", 
  "channeltext"."softdeleteflag" "channeltext_softdeleteflag" 
FROM 
  (
    "channeltext" 
    JOIN "channel" ON (
      (
        "channeltext"."channelID" = "channel"."id"
      )
    )
  );
--  DDL for View customeraccountransactionview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "customeraccountransactionview" (
  "User_id", 
  "transactionId", 
  "transactiontype", 
  "ExpenseCategory_id", 
  "Bill_id", 
  "Reference_id", 
  "fromAccountNumber", 
  "fromAccountBalance", 
  "toAccountNumber", 
  "toAccountBalance", 
  "amount", 
  "convertedAmount", 
  "transactionCurrency", 
  "baseCurrency", 
  "Status_id", 
  "statusDesc", 
  "isScheduled", 
  "category", 
  "billCategory", 
  "ExternalAccountNumber", 
  "Person_Id", 
  "frequencyType", 
  "createdDate", 
  "cashlessEmail", 
  "cashlessMode", 
  "cashlessOTP", 
  "cashlessOTPValidDate", 
  "cashlessPersonName", 
  "cashlessPhone", 
  "cashlessSecurityCode", 
  "cashWithdrawalTransactionStatus", 
  "frequencyEndDate", 
  "frequencyStartDate", 
  "hasDepositImage", 
  "payeeId", 
  "payeeName", 
  "p2pContact", 
  "personId", 
  "recurrenceDesc", 
  "numberOfRecurrences", 
  "scheduledDate", 
  "transactionComments", 
  "transactionsNotes", 
  "transDescription", 
  "transactionDate", 
  "postedDate", 
  "frontImage1", 
  "frontImage2", 
  "backImage1", 
  "backImage2", 
  "checkDesc", 
  "checkNumber1", 
  "checkNumber2", 
  "checkNumber", 
  "checkReason", 
  "requestValidity", 
  "checkDateOfIssue", 
  "bankName1", 
  "bankName2", 
  "withdrawlAmount1", 
  "withdrawlAmount2", 
  "cashAmount", 
  "payeeCurrency", 
  "fee", 
  "feePaidByReceipent", 
  "feeCurrency", 
  "isDisputed", 
  "disputeReason", 
  "disputeDescription", 
  "disputeDate", 
  "disputeStatus", 
  "description", 
  "statementReference", 
  "transCreditDebitIndicator", 
  "bookingDateTime", 
  "valueDateTime", 
  "transactionInformation", 
  "addressLine", 
  "transactionAmount", 
  "chargeAmount", 
  "chargeCurrency", 
  "sourceCurrency", 
  "targetCurrency", 
  "unitCurrency", 
  "exchangeRate", 
  "contractIdentification", 
  "quotationDate", 
  "instructedAmount", 
  "instructedCurrency", 
  "transactionCode", 
  "transactionSubCode", 
  "proprietaryTransactionCode", 
  "proprietaryTransactionIssuer", 
  "balanceCreditDebitIndicator", 
  "balanceType", 
  "balanceAmount", 
  "balanceCurrency", 
  "merchantName", 
  "merchantCategoryCode", 
  "creditorAgentSchemeName", 
  "creditorAgentIdentification", 
  "creditorAgentName", 
  "creditorAgentaddressType", 
  "creditorAgentDepartment", 
  "creditorAgentSubDepartment", 
  "creditorAgentStreetName", 
  "creditorAgentBuildingNumber", 
  "creditorAgentPostCode", 
  "creditorAgentTownName", 
  "creditorAgentCountrySubDivision", 
  "creditorAgentCountry", 
  "creditorAgentAddressLine", 
  "creditorAccountSchemeName", 
  "creditorAccountIdentification", 
  "creditorAccountName", 
  "creditorAccountSeconIdentification", 
  "debtorAgentSchemeName", 
  "debtorAgentIdentification", 
  "debtorAgentName", 
  "debtorAgentAddressType", 
  "debtorAgentDepartment", 
  "debtorAgentSubDepartment", 
  "debtorAgentStreetName", 
  "debtorAgentBuildingNumber", 
  "dedtorAgentPostCode", 
  "debtorAgentTownName", 
  "debtorAgentCountrySubDivision", 
  "debtorAgentCountry", 
  "debtorAgentAddressLine", 
  "debtorAccountSchemeName", 
  "debtorAccountIdentification", 
  "debtorAccountName", 
  "debtorAccountSeconIdentification", 
  "cardInstrumentSchemeName", 
  "cardInstrumentAuthorisationType", 
  "cardInstrumentName", 
  "cardInstrumentIdentification", 
  "FirstPaymentDateTime", 
  "NextPaymentDateTime", 
  "FinalPaymentDateTime", 
  "StandingOrderStatusCode", 
  "FP_Amount", 
  "FP_Currency", 
  "NP_Amount", 
  "NP_Currency", 
  "FPA_Amount", 
  "FPA_Currency", 
  "IBAN", 
  "sortCode", 
  "beneficiaryName", 
  "bankName", 
  "swiftCode"
) AS 
SELECT 
  DISTINCT "customeraccounts"."Customer_id" "User_id", 
  "transaction"."Id" "transactionId", 
  "transaction"."Type_id" "transactiontype", 
  "transaction"."ExpenseCategory_id" "ExpenseCategory_id", 
  "transaction"."billid" "Bill_id", 
  "transaction"."Reference_id" "Reference_id", 
  "transaction"."fromAccountNumber" "fromAccountNumber", 
  "transaction"."fromAccountBalance" "fromAccountBalance", 
  "transaction"."toAccountNumber" "toAccountNumber", 
  "transaction"."toAccountBalance" "toAccountBalance", 
  "transaction"."amount" "amount", 
  "transaction"."convertedAmount" "convertedAmount", 
  "transaction"."transactionCurrency" "transactionCurrency", 
  "transaction"."baseCurrency" "baseCurrency", 
  "transaction"."Status_id" "Status_id", 
  "transaction"."statusDesc" "statusDesc", 
  "transaction"."isScheduled" "isScheduled", 
  "transaction"."category" "category", 
  "transaction"."billCategory" "billCategory", 
  "transaction"."toExternalAccountNumber" "ExternalAccountNumber", 
  "transaction"."Person_Id" "Person_Id", 
  "transaction"."frequencyType" "frequencyType", 
  "transaction"."createdDate" "createdDate", 
  "transaction"."cashlessEmail" "cashlessEmail", 
  "transaction"."cashlessMode" "cashlessMode", 
  "transaction"."cashlessOTP" "cashlessOTP", 
  "transaction"."cashlessOTPValidDate" "cashlessOTPValidDate", 
  "transaction"."cashlessPersonName" "cashlessPersonName", 
  "transaction"."cashlessPhone" "cashlessPhone", 
  "transaction"."cashlessSecurityCode" "cashlessSecurityCode", 
  "transaction"."cashWithdrawalTransactionStatus" "cashWithdrawalTransactionStatus", 
  "transaction"."frequencyEndDate" "frequencyEndDate", 
  "transaction"."frequencyStartDate" "frequencyStartDate", 
  "transaction"."hasDepositImage" "hasDepositImage", 
  "transaction"."Payee_id" "payeeId", 
  "transaction"."payeeName" "payeeName", 
  "transaction"."p2pContact" "p2pContact", 
  "transaction"."Person_Id" "personId", 
  "transaction"."recurrenceDesc" "recurrenceDesc", 
  "transaction"."numberOfRecurrences" "numberOfRecurrences", 
  "transaction"."scheduledDate" "scheduledDate", 
  "transaction"."transactionComments" "transactionComments", 
  "transaction"."notes" "transactionsNotes", 
  "transaction"."description" "transDescription", 
  "transaction"."transactionDate" "transactionDate", 
  "transaction"."postedDate" "postedDate", 
  "transaction"."frontImage1" "frontImage1", 
  "transaction"."frontImage2" "frontImage2", 
  "transaction"."backImage1" "backImage1", 
  "transaction"."backImage2" "backImage2", 
  "transaction"."checkDesc" "checkDesc", 
  "transaction"."checkNumber1" "checkNumber1", 
  "transaction"."checkNumber2" "checkNumber2", 
  "transaction"."checkNumber" "checkNumber", 
  "transaction"."checkReason" "checkReason", 
  "transaction"."requestValidity" "requestValidity", 
  "transaction"."checkDateOfIssue" "checkDateOfIssue", 
  "transaction"."bankName1" "bankName1", 
  "transaction"."bankName2" "bankName2", 
  "transaction"."withdrawlAmount1" "withdrawlAmount1", 
  "transaction"."withdrawlAmount2" "withdrawlAmount2", 
  "transaction"."cashAmount" "cashAmount", 
  "transaction"."payeeCurrency" "payeeCurrency", 
  "transaction"."fee" "fee", 
  "transaction"."feePaidByReceipent" "feePaidByReceipent", 
  "transaction"."feeCurrency" "feeCurrency", 
  "transaction"."isDisputed" "isDisputed", 
  "transaction"."disputeReason" "disputeReason", 
  "transaction"."disputeDescription" "disputeDescription", 
  "transaction"."disputeDate" "disputeDate", 
  "transaction"."disputeStatus" "disputeStatus", 
  "transactiontype"."description" "description", 
  "transaction"."statementReference" "statementReference", 
  "transaction"."transCreditDebitIndicator" "transCreditDebitIndicator", 
  "transaction"."bookingDateTime" "bookingDateTime", 
  "transaction"."valueDateTime" "valueDateTime", 
  "transaction"."transactionInformation" "transactionInformation", 
  "transaction"."addressLine" "addressLine", 
  "transaction"."transactionAmount" "transactionAmount", 
  "transaction"."chargeAmount" "chargeAmount", 
  "transaction"."chargeCurrency" "chargeCurrency", 
  "transaction"."sourceCurrency" "sourceCurrency", 
  "transaction"."targetCurrency" "targetCurrency", 
  "transaction"."unitCurrency" "unitCurrency", 
  "transaction"."exchangeRate" "exchangeRate", 
  "transaction"."contractIdentification" "contractIdentification", 
  "transaction"."quotationDate" "quotationDate", 
  "transaction"."instructedAmount" "instructedAmount", 
  "transaction"."instructedCurrency" "instructedCurrency", 
  "transaction"."transactionCode" "transactionCode", 
  "transaction"."transactionSubCode" "transactionSubCode", 
  "transaction"."proprietaryTransactionCode" "proprietaryTransactionCode", 
  "transaction"."proprietaryTransactionIssuer" "proprietaryTransactionIssuer", 
  "transaction"."balanceCreditDebitIndicator" "balanceCreditDebitIndicator", 
  "transaction"."balanceType" "balanceType", 
  "transaction"."balanceAmount" "balanceAmount", 
  "transaction"."balanceCurrency" "balanceCurrency", 
  "transaction"."merchantName" "merchantName", 
  "transaction"."merchantCategoryCode" "merchantCategoryCode", 
  "transaction"."creditorAgentSchemeName" "creditorAgentSchemeName", 
  "transaction"."creditorAgentIdentification" "creditorAgentIdentification", 
  "transaction"."creditorAgentName" "creditorAgentName", 
  "transaction"."creditorAgentaddressType" "creditorAgentaddressType", 
  "transaction"."creditorAgentDepartment" "creditorAgentDepartment", 
  "transaction"."creditorAgentSubDepartment" "creditorAgentSubDepartment", 
  "transaction"."creditorAgentStreetName" "creditorAgentStreetName", 
  "transaction"."creditorAgentBuildingNumber" "creditorAgentBuildingNumber", 
  "transaction"."creditorAgentPostCode" "creditorAgentPostCode", 
  "transaction"."creditorAgentTownName" "creditorAgentTownName", 
  "transaction"."creditorAgentCountrySubDivision" "creditorAgentCountrySubDivision", 
  "transaction"."creditorAgentCountry" "creditorAgentCountry", 
  "transaction"."creditorAgentAddressLine" "creditorAgentAddressLine", 
  "transaction"."creditorAccountSchemeName" "creditorAccountSchemeName", 
  "transaction"."creditorAccountIdentification" "creditorAccountIdentification", 
  "transaction"."creditorAccountName" "creditorAccountName", 
  "transaction"."creditorAccountSeconIdentification" "creditorAccountSeconIdentification", 
  "transaction"."debtorAgentSchemeName" "debtorAgentSchemeName", 
  "transaction"."debtorAgentIdentification" "debtorAgentIdentification", 
  "transaction"."debtorAgentName" "debtorAgentName", 
  "transaction"."debtorAgentAddressType" "debtorAgentAddressType", 
  "transaction"."debtorAgentDepartment" "debtorAgentDepartment", 
  "transaction"."debtorAgentSubDepartment" "debtorAgentSubDepartment", 
  "transaction"."debtorAgentStreetName" "debtorAgentStreetName", 
  "transaction"."debtorAgentBuildingNumber" "debtorAgentBuildingNumber", 
  "transaction"."dedtorAgentPostCode" "dedtorAgentPostCode", 
  "transaction"."debtorAgentTownName" "debtorAgentTownName", 
  "transaction"."debtorAgentCountrySubDivision" "debtorAgentCountrySubDivision", 
  "transaction"."debtorAgentCountry" "debtorAgentCountry", 
  "transaction"."debtorAgentAddressLine" "debtorAgentAddressLine", 
  "transaction"."debtorAccountSchemeName" "debtorAccountSchemeName", 
  "transaction"."debtorAccountIdentification" "debtorAccountIdentification", 
  "transaction"."debtorAccountName" "debtorAccountName", 
  "transaction"."debtorAccountSeconIdentification" "debtorAccountSeconIdentification", 
  "transaction"."cardInstrumentSchemeName" "cardInstrumentSchemeName", 
  "transaction"."cardInstrumentAuthorisationType" "cardInstrumentAuthorisationType", 
  "transaction"."cardInstrumentName" "cardInstrumentName", 
  "transaction"."cardInstrumentIdentification" "cardInstrumentIdentification", 
  "transaction"."FirstPaymentDateTime" "FirstPaymentDateTime", 
  "transaction"."NextPaymentDateTime" "NextPaymentDateTime", 
  "transaction"."FinalPaymentDateTime" "FinalPaymentDateTime", 
  "transaction"."StandingOrderStatusCode" "StandingOrderStatusCode", 
  "transaction"."FP_Amount" "FP_Amount", 
  "transaction"."FP_Currency" "FP_Currency", 
  "transaction"."NP_Amount" "NP_Amount", 
  "transaction"."NP_Currency" "NP_Currency", 
  "transaction"."FPA_Amount" "FPA_Amount", 
  "transaction"."FPA_Currency" "FPA_Currency", 
  "transaction"."IBAN" "IBAN", 
  "transaction"."sortCode" "sortCode", 
  "transaction"."beneficiaryName" "beneficiaryName", 
  "transaction"."bankName" "bankName", 
  "transaction"."swiftCode" "swiftCode" 
FROM 
  (
    (
      "customeraccounts" CROSS 
      JOIN "transaction"
    ) CROSS 
    JOIN "transactiontype"
  ) 
WHERE 
  (
    (
      (
        CAST(
          "customeraccounts"."Account_id" as float(53)
        ) = "transaction"."fromAccountNumber"
      ) 
      OR (
        CAST(
          "customeraccounts"."Account_id" as float(53)
        ) = "transaction"."toAccountNumber"
      )
    ) 
    AND (
      "customeraccounts"."Customer_id" IS NOT NULL
    ) 
    AND (
      "transaction"."Type_id" = "transactiontype"."Id"
    )
  );
--  DDL for View customeraccounts_corecustomerinfo_view

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "customeraccounts_corecustomerinfo_view" (
  "User_id", "Account_id", "FavouriteStatus", 
  "Membership_id", "MembershipName", 
  "isBusiness"
) AS 
SELECT 
  "customeraccounts"."Customer_id" "User_id", 
  "customeraccounts"."Account_id" "Account_id", 
  "customeraccounts"."FavouriteStatus" "FavouriteStatus", 
  "contractcorecustomers"."coreCustomerId" "Membership_id", 
  "contractcorecustomers"."coreCustomerName" "MembershipName", 
  "contractcorecustomers"."isBusiness" "isBusiness" 
FROM 
  "customeraccounts" 
  LEFT JOIN "contractcorecustomers" ON (
    "contractcorecustomers"."coreCustomerId" = "customeraccounts"."coreCustomerId"
  );
--  DDL for View customeraccountsview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "customeraccountsview" (
  "Membership_id", "MembershipName", 
  "Taxid", "Customer_id", "User_id", 
  "Account_id", "isBusinessAccount", 
  "Type_id", "userName", "currencyCode", 
  "accountHolder", "error", "Address", 
  "Scheme", "number", "availableBalance", 
  "currentBalance", "interestRate", 
  "availableCredit", "minimumDue", 
  "dueDate", "firstPaymentDate", "closingDate", 
  "paymentTerm", "openingDate", "maturityDate", 
  "dividendLastPaidAmount", "dividendLastPaidDate", 
  "dividendPaidYTD", "dividendRate", 
  "dividendYTD", "eStatementEnable", 
  "isOrganizationAccount", "favouriteStatus", 
  "statusDesc", "nickName", "originalAmount", 
  "outstandingBalance", "paymentDue", 
  "paymentMethod", "swiftCode", "totalCreditMonths", 
  "totalDebitsMonth", "routingNumber", 
  "supportBillPay", "supportCardlessCash", 
  "supportTransferFrom", "supportTransferTo", 
  "supportDeposit", "unpaidInterest", 
  "previousYearsDividends", "principalBalance", 
  "principalValue", "regularPaymentAmount", 
  "phoneId", "lastDividendPaidDate", 
  "lastDividendPaidAmount", "lastPaymentAmount", 
  "lastPaymentDate", "lastStatementBalance", 
  "lateFeesDue", "maturityAmount", 
  "maturityOption", "payoffAmount", 
  "payOffCharge", "pendingDeposit", 
  "pendingWithdrawal", "jointHolders", 
  "isPFM", "interestPaidYTD", "interestPaidPreviousYTD", 
  "interestPaidLastYear", "interestEarned", 
  "currentAmountDue", "creditLimit", 
  "creditCardNumber", "bsbNum", "bondInterestLastYear", 
  "bondInterest", "availablePoints", 
  "accountName", "email", "IBAN", "adminProductId", 
  "UpdatedBy", "LastUpdated", "ActualUpdatedBY", 
  "bankname", "accountPreference", 
  "transactionLimit", "transferLimit", 
  "rates", "termsAndConditions", "typeDescription", 
  "supportChecks", "displayName", 
  "accountSubType", "description", 
  "schemeName", "identification", 
  "secondaryIdentification", "servicerSchemeName", 
  "servicerIdentification", "dataCreditDebitIndicator", 
  "dataType", "dataDateTime", "dataCreditLineIncluded", 
  "dataCreditLineType", "dataCreditLineAmount", 
  "dataCreditLineCurrency"
) AS 
SELECT 
  DISTINCT "contractcorecustomers"."coreCustomerId" "Membership_id", 
  "contractcorecustomers"."coreCustomerName" "MembershipName", 
  "accounts"."TaxId" "Taxid", 
  "customeraccounts"."Customer_id" "Customer_id", 
  "customeraccounts"."Customer_id" "User_id", 
  "accounts"."Account_id" "Account_id", 
  "contractcorecustomers"."isBusiness" "isBusinessAccount", 
  "accounts"."Type_id" "Type_id", 
  "accounts"."UserName" "userName", 
  "accounts"."CurrencyCode" "currencyCode", 
  "accounts"."AccountHolder" "accountHolder", 
  "accounts"."error" "error", 
  "accounts"."Address" "Address", 
  "accounts"."Scheme" "Scheme", 
  "accounts"."Number" "number", 
  "accounts"."AvailableBalance" "availableBalance", 
  "accounts"."CurrentBalance" "currentBalance", 
  "accounts"."InterestRate" "interestRate", 
  "accounts"."AvailableCredit" "availableCredit", 
  "accounts"."MinimumDue" "minimumDue", 
  "accounts"."DueDate" "dueDate", 
  "accounts"."FirstPaymentDate" "firstPaymentDate", 
  "accounts"."ClosingDate" "closingDate", 
  "accounts"."PaymentTerm" "paymentTerm", 
  "accounts"."OpeningDate" "openingDate", 
  "accounts"."MaturityDate" "maturityDate", 
  "accounts"."DividendLastPaidAmount" "dividendLastPaidAmount", 
  "accounts"."DividendLastPaidDate" "dividendLastPaidDate", 
  "accounts"."DividendPaidYTD" "dividendPaidYTD", 
  "accounts"."DividendRate" "dividendRate", 
  "accounts"."DividendYTD" "dividendYTD", 
  "customeraccounts"."EStatementmentEnable" "eStatementEnable", 
  "customeraccounts"."IsOrganizationAccount" "isOrganizationAccount", 
  "customeraccounts"."FavouriteStatus" "favouriteStatus", 
  "accounts"."StatusDesc" "statusDesc", 
  "accounts"."NickName" "nickName", 
  "accounts"."OriginalAmount" "originalAmount", 
  "accounts"."OutstandingBalance" "outstandingBalance", 
  "accounts"."PaymentDue" "paymentDue", 
  "accounts"."PaymentMethod" "paymentMethod", 
  "accounts"."SwiftCode" "swiftCode", 
  "accounts"."TotalCreditMonths" "totalCreditMonths", 
  "accounts"."TotalDebitsMonth" "totalDebitsMonth", 
  "accounts"."RoutingNumber" "routingNumber", 
  "accounts"."SupportBillPay" "supportBillPay", 
  "accounts"."SupportCardlessCash" "supportCardlessCash", 
  "accounts"."SupportTransferFrom" "supportTransferFrom", 
  "accounts"."SupportTransferTo" "supportTransferTo", 
  "accounts"."SupportDeposit" "supportDeposit", 
  "accounts"."UnpaidInterest" "unpaidInterest", 
  "accounts"."PreviousYearsDividends" "previousYearsDividends", 
  "accounts"."principalBalance" "principalBalance", 
  "accounts"."PrincipalValue" "principalValue", 
  "accounts"."RegularPaymentAmount" "regularPaymentAmount", 
  "accounts"."phone" "phoneId", 
  "accounts"."LastDividendPaidDate" "lastDividendPaidDate", 
  "accounts"."LastDividendPaidAmount" "lastDividendPaidAmount", 
  "accounts"."LastPaymentAmount" "lastPaymentAmount", 
  "accounts"."LastPaymentDate" "lastPaymentDate", 
  "accounts"."LastStatementBalance" "lastStatementBalance", 
  "accounts"."LateFeesDue" "lateFeesDue", 
  "accounts"."maturityAmount" "maturityAmount", 
  "accounts"."MaturityOption" "maturityOption", 
  "accounts"."payoffAmount" "payoffAmount", 
  "accounts"."PayOffCharge" "payOffCharge", 
  "accounts"."PendingDeposit" "pendingDeposit", 
  "accounts"."PendingWithdrawal" "pendingWithdrawal", 
  "accounts"."JointHolders" "jointHolders", 
  "accounts"."IsPFM" "isPFM", 
  "accounts"."InterestPaidYTD" "interestPaidYTD", 
  "accounts"."InterestPaidPreviousYTD" "interestPaidPreviousYTD", 
  "accounts"."InterestPaidLastYear" "interestPaidLastYear", 
  "accounts"."InterestEarned" "interestEarned", 
  "accounts"."CurrentAmountDue" "currentAmountDue", 
  "accounts"."CreditLimit" "creditLimit", 
  "accounts"."CreditCardNumber" "creditCardNumber", 
  "accounts"."BsbNum" "bsbNum", 
  "accounts"."BondInterestLastYear" "bondInterestLastYear", 
  "accounts"."BondInterest" "bondInterest", 
  "accounts"."AvailablePoints" "availablePoints", 
  "accounts"."AccountName" "accountName", 
  "customeraccounts"."email" "email", 
  "accounts"."IBAN" "IBAN", 
  "accounts"."adminProductId" "adminProductId", 
  "accounts"."UpdatedBy" "UpdatedBy", 
  "accounts"."LastUpdated" "LastUpdated", 
  "accounts"."ActualUpdatedBY" "ActualUpdatedBY", 
  "bank"."Description" "bankname", 
  "accounts"."AccountPreference" "accountPreference", 
  "accounttype"."transactionLimit" "transactionLimit", 
  "accounttype"."transferLimit" "transferLimit", 
  "accounttype"."rates" "rates", 
  "accounttype"."termsAndConditions" "termsAndConditions", 
  "accounttype"."TypeDescription" "typeDescription", 
  "accounttype"."supportChecks" "supportChecks", 
  "accounttype"."displayName" "displayName", 
  "accounts"."accountSubType" "accountSubType", 
  "accounts"."description" "description", 
  "accounts"."schemeName" "schemeName", 
  "accounts"."identification" "identification", 
  "accounts"."secondaryIdentification" "secondaryIdentification", 
  "accounts"."servicerSchemeName" "servicerSchemeName", 
  "accounts"."servicerIdentification" "servicerIdentification", 
  "accounts"."dataCreditDebitIndicator" "dataCreditDebitIndicator", 
  "accounts"."dataType" "dataType", 
  "accounts"."dataDateTime" "dataDateTime", 
  "accounts"."dataCreditLineIncluded" "dataCreditLineIncluded", 
  "accounts"."dataCreditLineType" "dataCreditLineType", 
  "accounts"."dataCreditLineAmount" "dataCreditLineAmount", 
  "accounts"."dataCreditLineCurrency" "dataCreditLineCurrency" 
FROM 
  (
    (
      (
        (
          "accounts" 
          JOIN "customeraccounts" ON (
            (
              "accounts"."Account_id" = "customeraccounts"."Account_id"
            )
          )
        ) 
        JOIN "accounttype" ON (
          (
            "accounts"."Type_id" = "accounttype"."TypeID"
          )
        )
      ) 
      LEFT JOIN "contractcorecustomers" ON (
        (
          "customeraccounts"."coreCustomerId" = "contractcorecustomers"."coreCustomerId"
        )
      )
    ) 
    LEFT JOIN "bank" ON (
      (
        "accounts"."Bank_id" = "bank"."id"
      )
    )
  );
--  DDL for View customeraddress_view

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "customeraddress_view" (
  "CustomerId", "Address_id", "isPrimary", 
  "AddressType", "isTypeBusiness", 
  "AddressId", "AddressLine1", "AddressLine2", 
  "ZipCode", "Region_id", "City_id", 
  "Country_id", "CityName", "RegionName", 
  "RegionCode", "CountryName", "CountryCode"
) AS 
SELECT 
  c."id" "CustomerId", 
  ca."Address_id" "Address_id", 
  ca."isPrimary" "isPrimary", 
  ca."Type_id" "AddressType", 
  ca."isTypeBusiness" "isTypeBusiness", 
  a."id" "AddressId", 
  a."addressLine1" "AddressLine1", 
  a."addressLine2" "AddressLine2", 
  a."zipCode" "ZipCode", 
  a."Region_id" "Region_id", 
  a."City_id" "City_id", 
  --iif((a."country" IS NOT null), a."country", reg."Country_id") "Country_id", 
  decode(
    (a."country"), 
    NULL, 
    reg."Country_id", 
    a."country"
  ) "Country_id", 
  a."cityName" "CityName", 
  reg."Name" "RegionName", 
  reg."Code" "RegionCode", 
  --iif((a."country" IS NOT null), "country"."Name", coun."Name") "CountryName", 
  --iif((a."country" IS NOT null), "country"."Code", coun."Code") "CountryCode" 
  --(case  a."country" when null then coun."Name" else "country"."Name" end ) "CountryName", 
  decode(
    (a."country"), 
    NULL, 
    coun."Name", 
    "country"."Name"
  ) "CountryName", 
  --IIF((ISNULL(a."country")), "country"."Code", coun."Code") "CountryCode" 
  decode(
    (a."country"), 
    NULL, 
    coun."Code", 
    "country"."Code"
  ) "CountryCode" 
FROM 
  (
    (
      (
        (
          "customeraddress" ca 
          INNER JOIN "customer" c ON (ca."Customer_id" = c."id")
        ) 
        INNER JOIN "address" a ON (
          (a."id" = ca."Address_id")
        ) 
        LEFT JOIN "region" reg ON (reg."id" = a."Region_id")
      ) 
      LEFT JOIN "country" coun ON (coun."id" = reg."Country_id")
    ) 
    LEFT JOIN "country" "country" ON (
      "country"."id" = reg."Country_id"
    )
  );
--  DDL for View customeraddressmbview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "customeraddressmbview" (
  "CustomerId", "Address_id", "Type_id", 
  "isTypeBusiness", "DurationOfStay", 
  "HomeOwnership", "isPrimary", "AddressType", 
  "AddressLine1", "AddressLine2", 
  "AddressLine3", "ZipCode", "CityName", 
  "CountryName", "State"
) AS 
SELECT 
  ca."Customer_id" "CustomerId", 
  ca."Address_id" "Address_id", 
  ca."Type_id" "Type_id", 
  ca."isTypeBusiness" "isTypeBusiness", 
  ca."DurationOfStay" "DurationOfStay", 
  ca."HomeOwnership" "HomeOwnership", 
  ca."isPrimary" "isPrimary", 
  AT_."Description" "AddressType", 
  a."addressLine1" "AddressLine1", 
  a."addressLine2" "AddressLine2", 
  a."addressLine3" "AddressLine3", 
  a."zipCode" "ZipCode", 
  a."cityName" "CityName", 
  a."country" "CountryName", 
  a."state" "state" 
FROM 
  (
    (
      "customeraddress" ca 
      JOIN "address" a ON (
        (a."id" = ca."Address_id")
      )
    ) 
    JOIN "addresstype" AT_ ON (
      (ca."Type_id" = AT_."id")
    )
  ) 
WHERE 
  (a."softdeleteflag" = 0);
--  DDL for View customercommunicationview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "customercommunicationview" (
  "id", "FirstName", "MiddleName", "LastName", 
  "UserName", "Gender", "DateOfBirth", 
  "Ssn", "Status_id", "CustomerType", 
  "Phone", "Email"
) AS 
SELECT 
  "customer"."id" "id", 
  "customer"."FirstName" "FirstName", 
  "customer"."MiddleName" "MiddleName", 
  "customer"."LastName" "LastName", 
  "customer"."UserName" "UserName", 
  "customer"."Gender" "Gender", 
  "customer"."DateOfBirth" "DateOfBirth", 
  "customer"."Ssn" "Ssn", 
  "customer"."Status_id" "Status_id", 
  "customertype"."Name" "CustomerType", 
  primaryphone."Value" "Phone", 
  primaryemail."Value" "Email" 
FROM 
  (
    (
      (
        "customer" 
        LEFT JOIN "customercommunication" primaryphone ON (
          (
            (
              primaryphone."Customer_id" = "customer"."id"
            ) 
            AND (primaryphone."isPrimary" = 1) 
            AND (
              primaryphone."Type_id" = 'COMM_TYPE_PHONE'
            )
          )
        )
      ) 
      LEFT JOIN "customercommunication" primaryemail ON (
        (
          (
            primaryemail."Customer_id" = "customer"."id"
          ) 
          AND (primaryemail."isPrimary" = 1) 
          AND (
            primaryemail."Type_id" = 'COMM_TYPE_EMAIL'
          )
        )
      )
    ) 
    JOIN "customertype" ON (
      (
        "customertype"."id" = "customer"."CustomerType_id"
      )
    )
  );
--  DDL for View customerorganisationmembershipview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "customerorganisationmembershipview" (
  "LastName", "DateOfBirth", "Ssn", 
  "Phone", "Email", "UserName", "Gender", 
  "id", "FirstName", "CustomerType", 
  "Organization_Id", "Membership_id", 
  "Taxid"
) AS 
SELECT 
  DISTINCT "customer"."LastName" "LastName", 
  "customer"."DateOfBirth" "DateOfBirth", 
  "customer"."Ssn" "Ssn", 
  primaryphone."Value" "Phone", 
  primaryemail."Value" "Email", 
  "customer"."UserName" "UserName", 
  "customer"."Gender" "Gender", 
  "customer"."id" "id", 
  "customer"."FirstName" "FirstName", 
  "customer"."CustomerType_id" "CustomerType", 
  "customer"."Organization_Id" "Organization_Id", 
  "organisationmembership"."Membership_id" "Membership_id", 
  "organisationmembership"."Taxid" "Taxid" 
FROM 
  (
    (
      (
        (
          "customer" 
          LEFT JOIN "customercommunication" primaryphone ON (
            (
              (
                primaryphone."Customer_id" = "customer"."id"
              ) 
              AND (primaryphone."isPrimary" = 1) 
              AND (
                primaryphone."Type_id" = 'COMM_TYPE_PHONE'
              )
            )
          )
        ) 
        LEFT JOIN "customercommunication" primaryemail ON (
          (
            (
              primaryemail."Customer_id" = "customer"."id"
            ) 
            AND (primaryemail."isPrimary" = 1) 
            AND (
              primaryemail."Type_id" = 'COMM_TYPE_EMAIL'
            )
          )
        )
      ) CROSS 
      JOIN "organisationemployees"
    ) CROSS 
    JOIN "organisationmembership"
  ) 
WHERE 
  (
    (
      (
        "customer"."Organization_Id" = "organisationemployees"."Organization_id"
      ) 
      OR (
        "customer"."id" = "organisationemployees"."Customer_id"
      )
    ) 
    AND (
      "organisationemployees"."Organization_id" = "organisationmembership"."Organization_id"
    )
  );
--  DDL for View customerpreferencesview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "customerpreferencesview" (
  "addressLine1", "addressLine2", "state", 
  "city", "country", "zipcode", "areUserAlertsTurnedOn", 
  "areAccountStatementTermsAccepted", 
  "areDepositTermsAccepted", "default_account_deposit", 
  "default_account_billPay", "default_account_payments", 
  "default_account_cardless", "default_account_transfers", 
  "DefaultModule_id", "default_account_wire", 
  "default_from_account_p2p", "default_to_account_p2p", 
  "isP2PActivated", "isP2PSupported", 
  "isBillPaySupported", "isBillPayActivated", 
  "isWireTransferActivated", "isWireTransferEligible", 
  "showBillPayFromAccPopup", "userFirstName", 
  "userLastName", "gender", "isPinSet", 
  "DateOfBirth", "noofdependents", 
  "spousefirstname", "ssn", "CountryCode", 
  "userImage", "userImageURL", "isEagreementSigned", 
  "id", "UserName", "maritalstatus", 
  "lastlogintime", "Bank_id", "Phone", 
  "Email"
) AS 
SELECT 
  DISTINCT "address"."addressLine1" "addressLine1", 
  "address"."addressLine2" "addressLine2", 
  "address"."state" "state", 
  "address"."cityName" "city", 
  "address"."country" "country", 
  "address"."zipCode" "zipcode", 
  "customer"."areUserAlertsTurnedOn" "areUserAlertsTurnedOn", 
  "customer"."areAccountStatementTermsAccepted" "areAccountStatementTermsAccepted", 
  "customer"."areDepositTermsAccepted" "areDepositTermsAccepted", 
  "customerpreference"."DefaultAccountDeposit" "default_account_deposit", 
  "customerpreference"."DefaultAccountBillPay" "default_account_billPay", 
  "customerpreference"."DefaultAccountPayments" "default_account_payments", 
  "customerpreference"."DefaultAccountCardless" "default_account_cardless", 
  "customerpreference"."DefaultAccountTransfers" "default_account_transfers", 
  "customerpreference"."DefaultModule_id" "DefaultModule_id", 
  "customerpreference"."DefaultAccountWire" "default_account_wire", 
  "customerpreference"."DefaultFromAccountP2P" "default_from_account_p2p", 
  "customerpreference"."DefaultToAccountP2P" "default_to_account_p2p", 
  "customer"."isP2PActivated" "isP2PActivated", 
  "customer"."isP2PSupported" "isP2PSupported", 
  "customer"."isBillPaySupported" "isBillPaySupported", 
  "customer"."isBillPayActivated" "isBillPayActivated", 
  "customer"."isWireTransferActivated" "isWireTransferActivated", 
  "customer"."isWireTransferEligible" "isWireTransferEligible", 
  "customerpreference"."ShowBillPayFromAccPopup" "showBillPayFromAccPopup", 
  "customer"."FirstName" "userFirstName", 
  "customer"."LastName" "userLastName", 
  "customer"."Gender" "gender", 
  "customer"."IsPinSet" "isPinSet", 
  "customer"."DateOfBirth" "DateOfBirth", 
  "customer"."NoOfDependents" "noofdependents", 
  "customer"."SpouseName" "spousefirstname", 
  "customer"."Ssn" "ssn", 
  "customer"."CountryCode" "CountryCode", 
  CAST(
    "customer"."UserImage" as nvarchar2(2000)
  ) "userImage", 
  --customer."UserImage" "userImage"  , 
  "customer"."UserImageURL" "userImageURL", 
  "customer"."isEagreementSigned" "isEagreementSigned", 
  "customer"."id" "id", 
  "customer"."UserName" "UserName", 
  "customer"."MaritalStatus_id" "maritalstatus", 
  "customer"."Lastlogintime" "lastlogintime", 
  "customer"."Bank_id" "Bank_id", 
  primaryphone."Value" "Phone", 
  primaryemail."Value" "Email" 
FROM 
  (
    (
      (
        (
          "customer" 
          LEFT JOIN "customerpreference" ON (
            (
              "customerpreference"."Customer_id" = "customer"."id"
            )
          )
        ) 
        LEFT JOIN (
          "customeraddress" 
          JOIN "address" ON (
            (
              "address"."id" = "customeraddress"."Address_id"
            )
          )
        ) ON (
          (
            (
              "customer"."id" = "customeraddress"."Customer_id"
            ) 
            AND (
              "customeraddress"."isPrimary" = 1
            )
          )
        )
      ) 
      LEFT JOIN "customercommunication" primaryphone ON (
        (
          (
            primaryphone."Customer_id" = "customer"."id"
          ) 
          AND (primaryphone."isPrimary" = 1) 
          AND (
            primaryphone."Type_id" = 'COMM_TYPE_PHONE'
          )
        )
      )
    ) 
    LEFT JOIN "customercommunication" primaryemail ON (
      (
        (
          primaryemail."Customer_id" = "customer"."id"
        ) 
        AND (primaryemail."isPrimary" = 1) 
        AND (
          primaryemail."Type_id" = 'COMM_TYPE_EMAIL'
        )
      )
    )
  );
--  DDL for View customerview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "customerview" (
  "id", "FirstName", "LastName", "UserName", 
  "Value", "isTypeBusiness", "description"
) AS 
SELECT 
  c."id" "id", 
  c."FirstName" "FirstName", 
  c."LastName" "LastName", 
  c."UserName" "UserName", 
  cc."Value" "Value", 
  cc."isTypeBusiness" "isTypeBusiness", 
  ct."Description" "description" 
FROM 
  (
    (
      "customercommunication" cc CROSS 
      JOIN "customer" c
    ) CROSS 
    JOIN "communicationtype" ct
  ) 
WHERE 
  (
    (cc."Type_id" = ct."id") 
    AND (cc."Customer_id" = c."id")
  );
--  DDL for View fromaccountransactionview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "fromaccountransactionview" (
  "Account_id", "AccountName", "NickName", 
  "AccountHolder", "UserName", "ExternalBankidentity_id", 
  "CurrencyCode", "User_id", "AvailableBalance", 
  "Bank_id", "ShowTransactions", "CurrentBalance", 
  "SwiftCode", "RoutingNumber", "BankNmae", 
  "transactionId", "transactiontype", 
  "Customer_id", "ExpenseCategory_id", 
  "Bill_id", "Reference_id", "fromAccountNumber", 
  "fromAccountBalance", "toAccountNumber", 
  "toAccountBalance", "amount", "Status_id", 
  "statusDesc", "isScheduled", "category", 
  "billCategory", "ExternalAccountNumber", 
  "Person_Id", "frequencyType", "createdDate", 
  "cashlessEmail", "cashlessMode", 
  "cashlessOTP", "cashlessOTPValidDate", 
  "cashlessPersonName", "cashlessPhone", 
  "cashlessSecurityCode", "cashWithdrawalTransactionStatus", 
  "frequencyEndDate", "frequencyStartDate", 
  "hasDepositImage", "payeeId", "p2pContact", 
  "personId", "recurrenceDesc", "numberOfRecurrences", 
  "scheduledDate", "transactionComments", 
  "transactionsNotes", "transDescription", 
  "transactionDate", "description", 
  "TypeDescription", "IBAN", "sortCode"
) AS 
SELECT 
  MIN("accounts"."Account_id") "Account_id", 
  MIN("accounts"."AccountName") "AccountName", 
  MIN("accounts"."NickName") "NickName", 
  MIN("accounts"."AccountHolder") "AccountHolder", 
  MIN("accounts"."UserName") "UserName", 
  MIN(
    "accounts"."ExternalBankidentity_id"
  ) "ExternalBankidentity_id", 
  MIN("accounts"."CurrencyCode") "CurrencyCode", 
  MIN("accounts"."User_id") "User_id", 
  MIN("accounts"."AvailableBalance") "AvailableBalance", 
  MIN("accounts"."Bank_id") "Bank_id", 
  "accounts"."ShowTransactions" "ShowTransactions", 
  MIN("accounts"."CurrentBalance") "CurrentBalance", 
  MIN("accounts"."SwiftCode") "SwiftCode", 
  MIN("accounts"."RoutingNumber") "RoutingNumber", 
  MIN("bank"."Description") "BankNmae", 
  "transaction"."Id" "transactionId", 
  MIN("transaction"."Type_id") "transactiontype", 
  MIN("transaction"."Customer_id") "Customer_id", 
  MIN(
    "transaction"."ExpenseCategory_id"
  ) "ExpenseCategory_id", 
  MIN("transaction"."Bill_id") "Bill_id", 
  MIN("transaction"."Reference_id") "Reference_id", 
  MIN(
    "transaction"."fromAccountNumber"
  ) "fromAccountNumber", 
  MIN(
    "transaction"."fromAccountBalance"
  ) "fromAccountBalance", 
  MIN(
    "transaction"."toAccountNumber"
  ) "toAccountNumber", 
  MIN(
    "transaction"."toAccountBalance"
  ) "toAccountBalance", 
  MIN("transaction"."amount") "amount", 
  MIN("transaction"."Status_id") "Status_id", 
  MIN("transaction"."statusDesc") "statusDesc", 
  "transaction"."isScheduled" "isScheduled", 
  MIN("transaction"."category") "category", 
  MIN("transaction"."billCategory") "billCategory", 
  MIN(
    "transaction"."toExternalAccountNumber"
  ) "ExternalAccountNumber", 
  MIN("transaction"."Person_Id") "Person_Id", 
  MIN("transaction"."frequencyType") "frequencyType", 
  MIN("transaction"."createdDate") "createdDate", 
  MIN("transaction"."cashlessEmail") "cashlessEmail", 
  MIN("transaction"."cashlessMode") "cashlessMode", 
  MIN("transaction"."cashlessOTP") "cashlessOTP", 
  MIN(
    "transaction"."cashlessOTPValidDate"
  ) "cashlessOTPValidDate", 
  MIN(
    "transaction"."cashlessPersonName"
  ) "cashlessPersonName", 
  MIN("transaction"."cashlessPhone") "cashlessPhone", 
  MIN(
    "transaction"."cashlessSecurityCode"
  ) "cashlessSecurityCode", 
  MIN(
    "transaction"."cashWithdrawalTransactionStatus"
  ) "cashWithdrawalTransactionStatus", 
  MIN(
    "transaction"."frequencyEndDate"
  ) "frequencyEndDate", 
  MIN(
    "transaction"."frequencyStartDate"
  ) "frequencyStartDate", 
  MIN(
    "transaction"."hasDepositImage"
  ) "hasDepositImage", 
  MIN("transaction"."Payee_id") "payeeId", 
  MIN("transaction"."p2pContact") "p2pContact", 
  MIN("transaction"."Person_Id") "personId", 
  MIN("transaction"."recurrenceDesc") "recurrenceDesc", 
  MIN(
    "transaction"."numberOfRecurrences"
  ) "numberOfRecurrences", 
  MIN("transaction"."scheduledDate") "scheduledDate", 
  MIN(
    "transaction"."transactionComments"
  ) "transactionComments", 
  MIN("transaction"."notes") "transactionsNotes", 
  MIN("transaction"."description") "transDescription", 
  MIN(
    "transaction"."transactionDate"
  ) "transactionDate", 
  MIN(
    "transactiontype"."description"
  ) "description", 
  MIN(
    "accounttype"."TypeDescription"
  ) "TypeDescription", 
  MIN("transaction"."IBAN") "IBAN", 
  MIN("transaction"."sortCode") "sortCode" 
FROM 
  (
    (
      (
        (
          "accounts" 
          JOIN "transaction" ON (
            "accounts"."Account_id" = "transaction"."fromAccountNumber"
          )
        ) 
        JOIN "transactiontype" ON (
          "transaction"."Type_id" = "transactiontype"."Id"
        )
      ) 
      JOIN "accounttype" ON (
        "accounttype"."TypeID" = "accounts"."Type_id"
      )
    ) 
    LEFT JOIN "bank" ON (
      "bank"."id" = "accounts"."Bank_id"
    )
  ) 
GROUP BY 
  "transaction"."Id", 
  "transaction"."isScheduled", 
  "ShowTransactions";
  --  DDL for View getaccountsview
  
CREATE 
  OR REPLACE FORCE NONEDITIONABLE VIEW "getaccountsview" (
    "Account_id", "Type_id", "userName", 
    "currencyCode", "accountHolder", 
    "isBusinessAccount", "error", "Address", 
    "Scheme", "number", "availableBalance", 
    "currentBalance", "interestRate", 
    "availableCredit", "minimumDue", 
    "dueDate", "firstPaymentDate", "closingDate", 
    "paymentTerm", "openingDate", "maturityDate", 
    "dividendLastPaidAmount", "dividendLastPaidDate", 
    "dividendPaidYTD", "dividendRate", 
    "dividendYTD", "eStatementEnable", 
    "favouriteStatus", "statusDesc", 
    "nickName", "User_id", "originalAmount", 
    "outstandingBalance", "paymentDue", 
    "paymentMethod", "swiftCode", "totalCreditMonths", 
    "totalDebitsMonth", "routingNumber", 
    "supportBillPay", "supportCardlessCash", 
    "supportTransferFrom", "supportTransferTo", 
    "supportDeposit", "unpaidInterest", 
    "previousYearsDividends", "principalBalance", 
    "principalValue", "regularPaymentAmount", 
    "phoneId", "lastDividendPaidDate", 
    "lastDividendPaidAmount", "lastPaymentAmount", 
    "lastPaymentDate", "lastStatementBalance", 
    "lateFeesDue", "maturityAmount", 
    "maturityOption", "payoffAmount", 
    "payOffCharge", "pendingDeposit", 
    "pendingWithdrawal", "jointHolders", 
    "isPFM", "interestPaidYTD", "interestPaidPreviousYTD", 
    "interestPaidLastYear", "interestEarned", 
    "currentAmountDue", "creditLimit", 
    "creditCardNumber", "bsbNum", "bondInterestLastYear", 
    "bondInterest", "availablePoints", 
    "accountName", "email", "IBAN", "adminProductId", 
    "bankname", "accountPreference", 
    "transactionLimit", "transferLimit", 
    "rates", "termsAndConditions", "typeDescription", 
    "supportChecks", "displayName", 
    "accountsubType", "description", 
    "schemeName", "identification", 
    "secondaryIdentification", "servicerSchemeName", 
    "servicerIdentification", "dataCreditDebitIndicator", 
    "dataType", "dataDateTime", "dataCreditLineIncluded", 
    "dataCreditLineType", "dataCreditLineAmount", 
    "dataCreditLineCurrency", "UpdatedBy", 
    "LastUpdated", "ActualUpdatedBY"
  ) AS 
SELECT 
  "accounts"."Account_id" "Account_id", 
  "accounts"."Type_id" "Type_id", 
  "accounts"."UserName" "userName", 
  "accounts"."CurrencyCode" "currencyCode", 
  "accounts"."AccountHolder" "accountHolder", 
  "accounts"."isBusinessAccount" "isBusinessAccount", 
  "accounts"."error" "error", 
  "accounts"."Address" "Address", 
  "accounts"."Scheme" "Scheme", 
  "accounts"."Number" "number", 
  "accounts"."AvailableBalance" "availableBalance", 
  "accounts"."CurrentBalance" "currentBalance", 
  "accounts"."InterestRate" "interestRate", 
  "accounts"."AvailableCredit" "availableCredit", 
  "accounts"."MinimumDue" "minimumDue", 
  "accounts"."DueDate" "dueDate", 
  "accounts"."FirstPaymentDate" "firstPaymentDate", 
  "accounts"."ClosingDate" "closingDate", 
  "accounts"."PaymentTerm" "paymentTerm", 
  "accounts"."OpeningDate" "openingDate", 
  "accounts"."MaturityDate" "maturityDate", 
  "accounts"."DividendLastPaidAmount" "dividendLastPaidAmount", 
  "accounts"."DividendLastPaidDate" "dividendLastPaidDate", 
  "accounts"."DividendPaidYTD" "dividendPaidYTD", 
  "accounts"."DividendRate" "dividendRate", 
  "accounts"."DividendYTD" "dividendYTD", 
  "accounts"."EStatementmentEnable" "eStatementEnable", 
  "accounts"."FavouriteStatus" "favouriteStatus", 
  "accounts"."StatusDesc" "statusDesc", 
  "accounts"."NickName" "nickName", 
  "accounts"."User_id" "User_id", 
  "accounts"."OriginalAmount" "originalAmount", 
  "accounts"."OutstandingBalance" "outstandingBalance", 
  "accounts"."PaymentDue" "paymentDue", 
  "accounts"."PaymentMethod" "paymentMethod", 
  "accounts"."SwiftCode" "swiftCode", 
  "accounts"."TotalCreditMonths" "totalCreditMonths", 
  "accounts"."TotalDebitsMonth" "totalDebitsMonth", 
  "accounts"."RoutingNumber" "routingNumber", 
  "accounts"."SupportBillPay" "supportBillPay", 
  "accounts"."SupportCardlessCash" "supportCardlessCash", 
  "accounts"."SupportTransferFrom" "supportTransferFrom", 
  "accounts"."SupportTransferTo" "supportTransferTo", 
  "accounts"."SupportDeposit" "supportDeposit", 
  "accounts"."UnpaidInterest" "unpaidInterest", 
  "accounts"."PreviousYearsDividends" "previousYearsDividends", 
  "accounts"."principalBalance" "principalBalance", 
  "accounts"."PrincipalValue" "principalValue", 
  "accounts"."RegularPaymentAmount" "regularPaymentAmount", 
  "accounts"."phone" "phoneId", 
  "accounts"."LastDividendPaidDate" "lastDividendPaidDate", 
  "accounts"."LastDividendPaidAmount" "lastDividendPaidAmount", 
  "accounts"."LastPaymentAmount" "lastPaymentAmount", 
  "accounts"."LastPaymentDate" "lastPaymentDate", 
  "accounts"."LastStatementBalance" "lastStatementBalance", 
  "accounts"."LateFeesDue" "lateFeesDue", 
  "accounts"."maturityAmount" "maturityAmount", 
  "accounts"."MaturityOption" "maturityOption", 
  "accounts"."payoffAmount" "payoffAmount", 
  "accounts"."PayOffCharge" "payOffCharge", 
  "accounts"."PendingDeposit" "pendingDeposit", 
  "accounts"."PendingWithdrawal" "pendingWithdrawal", 
  "accounts"."JointHolders" "jointHolders", 
  "accounts"."IsPFM" "isPFM", 
  "accounts"."InterestPaidYTD" "interestPaidYTD", 
  "accounts"."InterestPaidPreviousYTD" "interestPaidPreviousYTD", 
  "accounts"."InterestPaidLastYear" "interestPaidLastYear", 
  "accounts"."InterestEarned" "interestEarned", 
  "accounts"."CurrentAmountDue" "currentAmountDue", 
  "accounts"."CreditLimit" "creditLimit", 
  "accounts"."CreditCardNumber" "creditCardNumber", 
  "accounts"."BsbNum" "bsbNum", 
  "accounts"."BondInterestLastYear" "bondInterestLastYear", 
  "accounts"."BondInterest" "bondInterest", 
  "accounts"."AvailablePoints" "availablePoints", 
  "accounts"."AccountName" "accountName", 
  "accounts"."email" "email", 
  "accounts"."IBAN" "IBAN", 
  "accounts"."adminProductId" "adminProductId", 
  "bank"."Description" "bankname", 
  "accounts"."AccountPreference" "accountPreference", 
  "accounttype"."transactionLimit" "transactionLimit", 
  "accounttype"."transferLimit" "transferLimit", 
  "accounttype"."rates" "rates", 
  "accounttype"."termsAndConditions" "termsAndConditions", 
  "accounttype"."TypeDescription" "typeDescription", 
  "accounttype"."supportChecks" "supportChecks", 
  "accounttype"."displayName" "displayName", 
  "accounts"."accountSubType" "accountSubType", 
  "accounts"."description" "description", 
  "accounts"."schemeName" "schemeName", 
  "accounts"."identification" "identification", 
  "accounts"."secondaryIdentification" "secondaryIdentification", 
  "accounts"."servicerSchemeName" "servicerSchemeName", 
  "accounts"."servicerIdentification" "servicerIdentification", 
  "accounts"."dataCreditDebitIndicator" "dataCreditDebitIndicator", 
  "accounts"."dataType" "dataType", 
  "accounts"."dataDateTime" "dataDateTime", 
  "accounts"."dataCreditLineIncluded" "dataCreditLineIncluded", 
  "accounts"."dataCreditLineType" "dataCreditLineType", 
  "accounts"."dataCreditLineAmount" "dataCreditLineAmount", 
  "accounts"."dataCreditLineCurrency" "dataCreditLineCurrency", 
  "accounts"."UpdatedBy" "UpdatedBy", 
  "accounts"."LastUpdated" "LastUpdated", 
  "accounts"."ActualUpdatedBY" "ActualUpdatedBY" 
FROM 
  (
    (
      "accounts" CROSS 
      JOIN "accounttype"
    ) CROSS 
    JOIN "bank"
  ) 
WHERE 
  (
    (
      "accounts"."Type_id" = CAST(
        "accounttype"."TypeID" as float(53)
      )
    ) 
    AND (
      "accounts"."Bank_id" = CAST(
        "bank"."id" as float(53)
      )
    )
  );
--  DDL for View memtinaccountsview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "memtinaccountsview" (
  "Account_Type", "Customer_id", "Account_id", 
  "accountName", "Organization_Id", 
  "Membership_id", "Taxid"
) AS 
SELECT 
  DISTINCT "accounttype"."TypeDescription" "Account_Type", 
  "customeraccounts"."Customer_id" "Customer_id", 
  "customeraccounts"."Account_id" "Account_id", 
  "customeraccounts"."AccountName" "accountName", 
  "organisationmembership"."Organization_id" "Organization_Id", 
  "organisationmembership"."Membership_id" "Membership_id", 
  "organisationmembership"."Taxid" "Taxid" 
FROM 
  (
    (
      "accounttype" CROSS 
      JOIN "accounts"
    ) CROSS 
    JOIN (
      (
        "customeraccounts" CROSS 
        JOIN "organisationemployees"
      ) CROSS 
      JOIN "organisationmembership"
    )
  ) 
WHERE 
  (
    (
      CAST(
        "accounttype"."TypeID" as float(53)
      ) = "accounts"."Type_id"
    ) 
    AND (
      "accounts"."Account_id" = CAST(
        "customeraccounts"."Account_id" as float(53)
      )
    ) 
    AND (
      "customeraccounts"."Membership_id" = "organisationmembership"."Membership_id"
    )
  );
--  DDL for View messageview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "messageview" (
  "id", "Account_id", "accountName", 
  "nickName", "Category_id", "category", 
  "Subcategory_id", "subcategory", 
  "subject", "message", "sentDate", 
  "status", "isSoftDeleted", "isRead", 
  "createdDate", "receivedDate", "softdeletedDate", 
  "User_id"
) AS 
SELECT 
  msg."id" "id", 
  msg."Account_id" "Account_id", 
  acnts."AccountName" "accountName", 
  acnts."NickName" "nickName", 
  msg."Category_id" "Category_id", 
  msgcategory."category" "category", 
  msg."Subcategory_id" "Subcategory_id", 
  msgsubcategory."subcategory" "subcategory", 
  msg."subject" "subject", 
  msg."message" "message", 
  msg."sentDate" "sentDate", 
  msg."status" "status", 
  msg."isSoftDeleted" "isSoftDeleted", 
  msg."isRead" "isRead", 
  msg."createdDate" "createdDate", 
  msg."receivedDate" "receivedDate", 
  msg."softdeletedDate" "softdeletedDate", 
  acnts."User_id" "User_id" 
FROM 
  (
    (
      (
        "message" msg CROSS 
        JOIN "accounts" acnts
      ) CROSS 
      JOIN "messagecategory" msgcategory
    ) CROSS 
    JOIN "messagesubcategory" msgsubcategory
  ) 
WHERE 
  (
    (
      msg."Account_id" = acnts."Account_id"
    ) 
    AND (
      msgcategory."Id" = msg."Category_id"
    ) 
    AND (
      msgsubcategory."Id" = msg."Subcategory_id"
    )
  );
--  DDL for View notificationview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "notificationview" (
  "notificationId", "imageURL", "isRead", 
  "notificationActionLink", "notificationModule", 
  "notificationSubject", "notificationSubModule", 
  "notificationText", "receivedDate", 
  "userNotificationId", "user_id", 
  "notificationCategory", "actionButtonLabelName"
) AS 
SELECT 
  "notification"."notificationId" "notificationId", 
  "notification"."imageURL" "imageURL", 
  "usernotification"."isRead" "isRead", 
  "notification"."notificationActionLink" "notificationActionLink", 
  "notification"."notificationModule" "notificationModule", 
  "notification"."notificationSubject" "notificationSubject", 
  "notification"."notificationSubModule" "notificationSubModule", 
  "notification"."notificationText" "notificationText", 
  "usernotification"."receivedDate" "receivedDate", 
  "usernotification"."id" "userNotificationId", 
  "usernotification"."user_id" "user_id", 
  "notification"."notificationCategory" "notificationCategory", 
  "notification"."actionButtonLabelName" "actionButtonLabelName" 
FROM 
  (
    "usernotification" 
    JOIN "notification" ON (
      (
        "notification"."notificationId" = "usernotification"."notification_id"
      )
    )
  );
--  DDL for View organisationaccountsview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "organisationaccountsview" (
  "Account_Type", "Customer_id", "Account_id", 
  "accountID", "Organization_Id", 
  "createdts", "lastmodifiedts", "StatusDesc", 
  "AccountName", "availableBalance", 
  "currentBalance", "dividendRate", 
  "eStatementEnable", "swiftCode", 
  "routingNumber", "accountHolder", 
  "lastDividendPaidDate", "lastDividendPaidAmount", 
  "dividendLastPaidAmount", "dividendLastPaidDate", 
  "OWNERSHIP"
) AS 
SELECT 
  DISTINCT "accounttype"."TypeDescription" "Account_Type", 
  "customeraccounts"."Customer_id" "Customer_id", 
  "customeraccounts"."Account_id" "Account_id", 
  "customeraccounts"."Account_id" "accountID", 
  "customeraccounts"."Organization_id" "Organization_Id", 
  "customeraccounts"."createdts" "createdts", 
  "customeraccounts"."lastmodifiedts" "lastmodifiedts", 
  "accounts"."StatusDesc" "StatusDesc", 
  "accounts"."AccountName" "AccountName", 
  "accounts"."AvailableBalance" "availableBalance", 
  "accounts"."CurrentBalance" "currentBalance", 
  "accounts"."DividendRate" "dividendRate", 
  "accounts"."EStatementmentEnable" "eStatementEnable", 
  "accounts"."SwiftCode" "swiftCode", 
  "accounts"."RoutingNumber" "routingNumber", 
  "accounts"."AccountHolder" "accountHolder", 
  "accounts"."LastDividendPaidDate" "lastDividendPaidDate", 
  "accounts"."LastDividendPaidAmount" "lastDividendPaidAmount", 
  "accounts"."DividendLastPaidAmount" "dividendLastPaidAmount", 
  "accounts"."DividendLastPaidDate" "dividendLastPaidDate", 
  'joint' OWNERSHIP 
FROM 
  "accounts" 
  LEFT JOIN "accounttype" ON "accounttype"."TypeID" = "accounts"."Type_id" 
  JOIN "customeraccounts" ON "accounts"."Account_id" = "customeraccounts"."Account_id" 
WHERE 
  "accounts"."Account_id" = "customeraccounts"."Account_id";
  --  DDL for View organisationemployeesview
  
CREATE 
  OR REPLACE FORCE NONEDITIONABLE VIEW "organisationemployeesview" (
    "orgemp_id", "orgemp_orgid", "orgemp_cusid", 
    "isAuthSignatory", "isOwner", "customer_id", 
    "FirstName", "MiddleName", "LastName", 
    "UserName", "DrivingLicenseNumber", 
    "DateOfBirth", "Ssn", "custcomm_id", 
    "custcomm_typeid", "custcomm_custid", 
    "custcomm_value", "custcomm_istypebusiness", 
    "Status_id", "createdts", "Lastlogintime", 
    "Group_id", "role_name", "createdby", 
    "signatorytypeId", "signatorytypeName"
  ) AS 
SELECT 
  "organisationemployees"."id" "orgemp_id", 
  "organisationemployees"."Organization_id" "orgemp_orgid", 
  "organisationemployees"."Customer_id" "orgemp_cusid", 
  "organisationemployees"."isAuthSignatory" "isAuthSignatory", 
  "organisationemployees"."Is_Admin" "isOwner", 
  "customer"."id" "customer_id", 
  "customer"."FirstName" "FirstName", 
  "customer"."MiddleName" "MiddleName", 
  "customer"."LastName" "LastName", 
  "customer"."UserName" "UserName", 
  "customer"."DrivingLicenseNumber" "DrivingLicenseNumber", 
  "customer"."DateOfBirth" "DateOfBirth", 
  "customer"."Ssn" "Ssn", 
  "customercommunication"."id" "custcomm_id", 
  "customercommunication"."Type_id" "custcomm_typeid", 
  "customercommunication"."Customer_id" "custcomm_custid", 
  "customercommunication"."Value" "custcomm_value", 
  "customercommunication"."isTypeBusiness" "custcomm_istypebusiness", 
  "customer"."Status_id" "Status_id", 
  "customer"."createdts" "createdts", 
  "customer"."Lastlogintime" "Lastlogintime", 
  "customergroup"."Group_id" "Group_id", 
  "membergroup"."Name" "role_name", 
  "customer"."createdby" "createdby", 
  "signatorytype"."id" "signatorytypeId", 
  "signatorytype"."name" "signatorytypeName" 
FROM 
  (
    (
      (
        (
          (
            (
              "organisationemployees" 
              JOIN "customer" ON (
                (
                  "organisationemployees"."Customer_id" = "customer"."id"
                )
              )
            ) 
            LEFT JOIN "customercommunication" ON (
              (
                "customer"."id" = "customercommunication"."Customer_id"
              )
            )
          ) 
          LEFT JOIN "customergroup" ON (
            (
              "customer"."id" = "customergroup"."Customer_id"
            )
          )
        ) 
        LEFT JOIN "customerbusinesstype" ON (
          (
            "customerbusinesstype"."Customer_id" = "customer"."id"
          )
        )
      ) 
      LEFT JOIN "signatorytype" ON (
        (
          "signatorytype"."id" = "customerbusinesstype"."SignatoryType_id"
        )
      )
    ) 
    JOIN "membergroup" ON (
      (
        (
          "membergroup"."id" = "customergroup"."Group_id"
        ) 
        AND (
          "membergroup"."Type_id" = 'TYPE_ID_BUSINESS'
        )
      )
    )
  );
--  DDL for View organisationview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "organisationview" (
  "org_id", "org_Name", "org_typeId", 
  "org_status", "org_faxid", "orgcomm_Value", 
  "orgmem_memid", "orgmem_taxid", 
  "cityName", "addressLine1", "addressLine2", 
  "zipCode", "addressId", "orgown_firstName", 
  "orgown_midleName", "orgown_lastName", 
  "orgown_dob", "orgown_ssn", "orgown_email", 
  "orgown_phone", "State", "Country", 
  "TypeName", "IsPrimary", "businessType", 
  "businessTypeId"
) AS 
SELECT 
  "organisation"."id" "org_id", 
  "organisation"."Name" "org_Name", 
  "organisation"."Type_Id" "org_typeId", 
  "organisation"."StatusId" "org_status", 
  "organisation"."FaxId" "org_faxid", 
  "organisationcommunication"."Value" "orgcomm_Value", 
  samplemember."Membership_id" "orgmem_memid", 
  samplemember."Taxid" "orgmem_taxid", 
  "address"."cityName" "cityName", 
  "address"."addressLine1" "addressLine1", 
  "address"."addressLine2" "addressLine2", 
  "address"."zipCode" "zipCode", 
  "address"."id" "addressId", 
  sampleowner."FirstName" "orgown_firstName", 
  sampleowner."MidleName" "orgown_midleName", 
  sampleowner."LastName" "orgown_lastName", 
  sampleowner."DateOfBirth" "orgown_dob", 
  sampleowner."Ssn" "orgown_ssn", 
  sampleowner."Email" "orgown_email", 
  sampleowner."Phone" "orgown_phone", 
  "address"."state" "state", 
  "address"."country" "Country", 
  "customertype"."Name" "TypeName", 
  "organisationaddress"."IsPrimary" "IsPrimary", 
  "businesstype"."name" "businessType", 
  "businesstype"."id" "businessTypeId" 
FROM 
  (
    (
      (
        (
          (
            (
              (
                "organisation" 
                LEFT JOIN "organisationcommunication" ON (
                  (
                    "organisation"."id" = "organisationcommunication"."Organization_id"
                  )
                )
              ) 
              LEFT JOIN "organisationaddress" ON (
                (
                  "organisation"."id" = "organisationaddress"."Organization_id"
                )
              )
            ) 
            LEFT JOIN "address" ON (
              (
                "organisationaddress"."Address_id" = "address"."id"
              )
            )
          ) 
          LEFT JOIN "customertype" ON (
            (
              "organisation"."Type_Id" = "customertype"."id"
            )
          )
        ) 
        LEFT JOIN "organisationmembership" samplemember ON (
          (
            "organisation"."id" = samplemember."Organization_id"
          )
        )
      ) 
      LEFT JOIN "organisationowner" sampleowner ON (
        (
          sampleowner."Organization_id" = "organisation"."id"
        )
      )
    ) 
    LEFT JOIN "businesstype" ON (
      (
        "businesstype"."id" = "organisation"."BusinessType_id"
      )
    )
  );
--  DDL for View organizationownerview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "organizationownerview" (
  "LastName", "DateOfBirth", "Ssn", 
  "Phone", "Email", "FirstName", "IDType_id", 
  "IdValue", "Organization_Id", "Membership_id", 
  "Taxid"
) AS 
SELECT 
  DISTINCT "organisationowner"."LastName" "LastName", 
  "organisationowner"."DateOfBirth" "DateOfBirth", 
  "organisationowner"."Ssn" "Ssn", 
  "organisationowner"."Phone" "Phone", 
  "organisationowner"."Email" "Email", 
  "organisationowner"."FirstName" "FirstName", 
  "organisationowner"."IDType_id" "IDType_id", 
  "organisationowner"."IdValue" "IdValue", 
  "organisationowner"."Organization_id" "Organization_Id", 
  "organisationmembership"."Membership_id" "Membership_id", 
  "organisationmembership"."Taxid" "Taxid" 
FROM 
  (
    "organisationowner" CROSS 
    JOIN "organisationmembership"
  ) 
WHERE 
  (
    "organisationowner"."Organization_id" = "organisationmembership"."Organization_id"
  );
--  DDL for View orgemployeedetails

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "orgemployeedetails" (
  "id", "FirstName", "MiddleName", "LastName", 
  "Username", "Gender", "DateOfBirth", 
  "DrivingLicenseNumber", "Ssn", "UserCompany", 
  "Lastlogintime", "Status", "Account_id", 
  "AccountName", "createdby"
) AS 
SELECT 
  c."id" "id", 
  c."FirstName" "FirstName", 
  c."MiddleName" "MiddleName", 
  c."LastName" "LastName", 
  c."UserName" "Username", 
  c."Gender" "Gender", 
  c."DateOfBirth" "DateOfBirth", 
  c."DrivingLicenseNumber" "DrivingLicenseNumber", 
  c."Ssn" "Ssn", 
  c."UserCompany" "UserCompany", 
  c."Lastlogintime" "Lastlogintime", 
  c."Status_id" STATUS, 
  ca."Account_id" "Account_id", 
  ca."AccountName" "AccountName", 
  c."createdby" "createdby" 
FROM 
  (
    "customer" c CROSS 
    JOIN "customeraccounts" ca
  ) 
WHERE 
  (
    (c."id" = ca."Customer_id") 
    AND (ca."IsOrganizationAccount" = 1)
  );
--  DDL for View payeetransactionview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "payeetransactionview" (
  "Id", "isScheduled", "Customer_id", 
  "ExpenseCategory_id", "Payee_id", 
  "Bill_id", "Type_id", "Reference_id", 
  "fromAccountNumber", "fromAccountBalance", 
  "toAccountNumber", "toAccountBalance", 
  "amount", "Status_id", "statusDesc", 
  "notes", "checkNumber", "imageURL1", 
  "imageURL2", "hasDepositImage", 
  "description", "scheduledDate", 
  "transactionDate", "createdDate", 
  "transactionComments", "toExternalAccountNumber", 
  "Person_Id", "frequencyType", "numberOfRecurrences", 
  "frequencyStartDate", "frequencyEndDate", 
  "checkImage", "checkImageBack", 
  "cashlessOTPValidDate", "cashlessOTP", 
  "cashlessPhone", "cashlessEmail", 
  "cashlessPersonName", "cashlessMode", 
  "cashlessSecurityCode", "cashWithdrawalTransactionStatus", 
  "cashlessPin", "category", "billCategory", 
  "recurrenceDesc", "deliverBy", "p2pContact", 
  "p2pRequiredDate", "requestCreatedDate", 
  "penaltyFlag", "payoffFlag", "viewReportLink", 
  "isPaypersonDeleted", "fee", "payeeId", 
  "payeeType", "name", "nickName", 
  "phone", "email", "accountNumber", 
  "billerId", "billermaster_id", "softDelete", 
  "User_Id", "addressLine1", "addressLine2", 
  "eBillEnable", "phoneExtension", 
  "phoneCountryCode", "ebillSupport", 
  "transactionType"
) AS 
SELECT 
  t."Id" "Id", 
  t."isScheduled" "isScheduled", 
  t."Customer_id" "Customer_id", 
  t."ExpenseCategory_id" "ExpenseCategory_id", 
  t."Payee_id" "Payee_id", 
  t."Bill_id" "Bill_id", 
  t."Type_id" "Type_id", 
  t."Reference_id" "Reference_id", 
  t."fromAccountNumber" "fromAccountNumber", 
  t."fromAccountBalance" "fromAccountBalance", 
  t."toAccountNumber" "toAccountNumber", 
  t."toAccountBalance" "toAccountBalance", 
  t."amount" "amount", 
  t."Status_id" "Status_id", 
  t."statusDesc" "statusDesc", 
  t."notes" "notes", 
  t."checkNumber" "checkNumber", 
  t."imageURL1" "imageURL1", 
  t."imageURL2" "imageURL2", 
  t."hasDepositImage" "hasDepositImage", 
  t."description" "description", 
  t."scheduledDate" "scheduledDate", 
  t."transactionDate" "transactionDate", 
  t."createdDate" "createdDate", 
  t."transactionComments" "transactionComments", 
  t."toExternalAccountNumber" "toExternalAccountNumber", 
  t."Person_Id" "Person_Id", 
  t."frequencyType" "frequencyType", 
  t."numberOfRecurrences" "numberOfRecurrences", 
  t."frequencyStartDate" "frequencyStartDate", 
  t."frequencyEndDate" "frequencyEndDate", 
  t."checkImage" "checkImage", 
  t."checkImageBack" "checkImageBack", 
  t."cashlessOTPValidDate" "cashlessOTPValidDate", 
  t."cashlessOTP" "cashlessOTP", 
  t."cashlessPhone" "cashlessPhone", 
  t."cashlessEmail" "cashlessEmail", 
  t."cashlessPersonName" "cashlessPersonName", 
  t."cashlessMode" "cashlessMode", 
  t."cashlessSecurityCode" "cashlessSecurityCode", 
  t."cashWithdrawalTransactionStatus" "cashWithdrawalTransactionStatus", 
  t."cashlessPin" "cashlessPin", 
  t."category" "category", 
  t."billCategory" "billCategory", 
  t."recurrenceDesc" "recurrenceDesc", 
  t."deliverBy" "deliverBy", 
  t."p2pContact" "p2pContact", 
  t."p2pRequiredDate" "p2pRequiredDate", 
  t."requestCreatedDate" "requestCreatedDate", 
  t."penaltyFlag" "penaltyFlag", 
  t."payoffFlag" "payoffFlag", 
  t."viewReportLink" "viewReportLink", 
  t."isPaypersonDeleted" "isPaypersonDeleted", 
  t."fee" "fee", 
  p."Id" "payeeId", 
  p."Type_id" "payeeType", 
  p."name" "name", 
  p."nickName" "nickName", 
  p."phone" "phone", 
  p."email" "email", 
  p."accountNumber" "accountNumber", 
  p."billerId" "billerId", 
  p."billermaster_id" "billermaster_id", 
  p."softDelete" "softDelete", 
  p."User_Id" "User_Id", 
  p."addressLine1" "addressLine1", 
  p."addressLine2" "addressLine2", 
  p."eBillEnable" "eBillEnable", 
  p."phoneExtension" "phoneExtension", 
  p."phoneCountryCode" "phoneCountryCode", 
  bm."ebillSupport" "ebillSupport", 
  tt."description" "transactionType" 
FROM 
  (
    (
      (
        "transaction" t 
        LEFT JOIN "payee" p ON (
          (t."Payee_id" = p."Id")
        )
      ) 
      JOIN "transactiontype" tt ON (
        (t."Type_id" = tt."Id")
      )
    ) 
    LEFT JOIN "billermaster" bm ON (
      (bm."id" = p."billermaster_id")
    )
  );
--  DDL for View paypersontransactionview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "paypersontransactionview" (
  "Id", "isScheduled", "Customer_id", 
  "ExpenseCategory_id", "Payee_id", 
  "Bill_id", "Type_id", "Reference_id", 
  "fromAccountNumber", "fromAccountBalance", 
  "toAccountNumber", "toAccountBalance", 
  "amount", "Status_id", "statusDesc", 
  "notes", "checkNumber", "imageURL1", 
  "imageURL2", "hasDepositImage", 
  "description", "scheduledDate", 
  "transactionDate", "createdDate", 
  "transactionComments", "toExternalAccountNumber", 
  "Person_Id", "frequencyType", "numberOfRecurrences", 
  "frequencyStartDate", "frequencyEndDate", 
  "checkImage", "checkImageBack", 
  "cashlessOTPValidDate", "cashlessOTP", 
  "cashlessPhone", "cashlessEmail", 
  "cashlessPersonName", "cashlessMode", 
  "cashlessSecurityCode", "cashWithdrawalTransactionStatus", 
  "cashlessPin", "category", "billCategory", 
  "recurrenceDesc", "deliverBy", "p2pContact", 
  "p2pRequiredDate", "requestCreatedDate", 
  "penaltyFlag", "payoffFlag", "viewReportLink", 
  "isPaypersonDeleted", "fee", "paypersonID", 
  "firstName", "lastName", "phone", 
  "email", "User_id", "secondaryEmail", 
  "secondoryPhoneNumber", "primaryContactForSending", 
  "nickName", "isSoftDelete", "transactionType"
) AS 
SELECT 
  t."Id" "Id", 
  t."isScheduled" "isScheduled", 
  t."Customer_id" "Customer_id", 
  t."ExpenseCategory_id" "ExpenseCategory_id", 
  t."Payee_id" "Payee_id", 
  t."Bill_id" "Bill_id", 
  t."Type_id" "Type_id", 
  t."Reference_id" "Reference_id", 
  t."fromAccountNumber" "fromAccountNumber", 
  t."fromAccountBalance" "fromAccountBalance", 
  t."toAccountNumber" "toAccountNumber", 
  t."toAccountBalance" "toAccountBalance", 
  t."amount" "amount", 
  t."Status_id" "Status_id", 
  t."statusDesc" "statusDesc", 
  t."notes" "notes", 
  t."checkNumber" "checkNumber", 
  t."imageURL1" "imageURL1", 
  t."imageURL2" "imageURL2", 
  t."hasDepositImage" "hasDepositImage", 
  t."description" "description", 
  t."scheduledDate" "scheduledDate", 
  t."transactionDate" "transactionDate", 
  t."createdDate" "createdDate", 
  t."transactionComments" "transactionComments", 
  t."toExternalAccountNumber" "toExternalAccountNumber", 
  t."Person_Id" "Person_Id", 
  t."frequencyType" "frequencyType", 
  t."numberOfRecurrences" "numberOfRecurrences", 
  t."frequencyStartDate" "frequencyStartDate", 
  t."frequencyEndDate" "frequencyEndDate", 
  t."checkImage" "checkImage", 
  t."checkImageBack" "checkImageBack", 
  t."cashlessOTPValidDate" "cashlessOTPValidDate", 
  t."cashlessOTP" "cashlessOTP", 
  t."cashlessPhone" "cashlessPhone", 
  t."cashlessEmail" "cashlessEmail", 
  t."cashlessPersonName" "cashlessPersonName", 
  t."cashlessMode" "cashlessMode", 
  t."cashlessSecurityCode" "cashlessSecurityCode", 
  t."cashWithdrawalTransactionStatus" "cashWithdrawalTransactionStatus", 
  t."cashlessPin" "cashlessPin", 
  t."category" "category", 
  t."billCategory" "billCategory", 
  t."recurrenceDesc" "recurrenceDesc", 
  t."deliverBy" "deliverBy", 
  t."p2pContact" "p2pContact", 
  t."p2pRequiredDate" "p2pRequiredDate", 
  t."requestCreatedDate" "requestCreatedDate", 
  t."penaltyFlag" "penaltyFlag", 
  t."payoffFlag" "payoffFlag", 
  t."viewReportLink" "viewReportLink", 
  t."isPaypersonDeleted" "isPaypersonDeleted", 
  t."fee" "fee", 
  p."id" "paypersonID", 
  p."firstName" "firstName", 
  p."lastName" "lastName", 
  p."phone" "phone", 
  p."email" "email", 
  p."User_id" "User_id", 
  p."secondaryEmail" "secondaryEmail", 
  p."secondoryPhoneNumber" "secondoryPhoneNumber", 
  p."primaryContactForSending" "primaryContactForSending", 
  p."nickName" "nickName", 
  p."isSoftDelete" "isSoftDelete", 
  tt."description" "transactionType" 
FROM 
  (
    (
      "transaction" t 
      LEFT JOIN "payperson" p ON (
        (t."Person_Id" = p."id")
      )
    ) 
    JOIN "transactiontype" tt ON (
      (t."Type_id" = tt."Id")
    )
  );
--  DDL for View pfmbudgetsnapshotview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "pfmbudgetsnapshotview" (
  "allocatedAmount", "amountSpent", 
  "budgetId", "categoryId", "categoryName"
) AS 
SELECT 
  "pfmbudgetsnapshot"."allocatedAmount" "allocatedAmount", 
  "pfmbudgetsnapshot"."amountSpent" "amountSpent", 
  "pfmbudgetsnapshot"."id" "budgetId", 
  "pfmbudgetsnapshot"."Category_Id" "categoryId", 
  "pfmcategory"."categoryName" "categoryName" 
FROM 
  (
    "pfmbudgetsnapshot" CROSS 
    JOIN "pfmcategory"
  ) 
WHERE 
  (
    "pfmbudgetsnapshot"."Category_Id" = "pfmcategory"."id"
  );
--  DDL for View pfmpiechartview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "pfmpiechartview" (
  "cashSpent", "monthId", "year", "categoryId", 
  "monthName", "categoryName"
) AS 
SELECT 
  "pfmpiechart"."cashSpent" "cashSpent", 
  "pfmpiechart"."monthId" "monthId", 
  "pfmpiechart"."year" "year", 
  "pfmpiechart"."categoryId" "categoryId", 
  "pfmmonth"."monthName" "monthName", 
  "pfmcategory"."categoryName" "categoryName" 
FROM 
  (
    (
      "pfmpiechart" CROSS 
      JOIN "pfmmonth"
    ) CROSS 
    JOIN "pfmcategory"
  ) 
WHERE 
  (
    (
      "pfmpiechart"."monthId" = "pfmmonth"."id"
    ) 
    AND (
      "pfmpiechart"."categoryId" = "pfmcategory"."id"
    )
  );
--  DDL for View toaccountransactionview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "toaccountransactionview" (
  "Account_id", "AccountName", "NickName", 
  "AccountHolder", "UserName", "ExternalBankidentity_id", 
  "CurrencyCode", "User_id", "AvailableBalance", 
  "Bank_id", "ShowTransactions", "CurrentBalance", 
  "SwiftCode", "RoutingNumber", "transactionId", 
  "transactiontype", "Customer_id", 
  "ExpenseCategory_id", "Bill_id", 
  "Reference_id", "fromAccountNumber", 
  "fromAccountBalance", "toAccountNumber", 
  "toAccountBalance", "amount", "Status_id", 
  "statusDesc", "isScheduled", "category", 
  "billCategory", "ExternalAccountNumber", 
  "Person_Id", "frequencyType", "createdDate", 
  "cashlessEmail", "cashlessMode", 
  "cashlessOTP", "cashlessOTPValidDate", 
  "cashlessPersonName", "cashlessPhone", 
  "cashlessSecurityCode", "cashWithdrawalTransactionStatus", 
  "frequencyEndDate", "frequencyStartDate", 
  "hasDepositImage", "payeeId", "p2pContact", 
  "personId", "recurrenceDesc", "numberOfRecurrences", 
  "scheduledDate", "transactionComments", 
  "transactionsNotes", "transDescription", 
  "transactionDate", "description", 
  "TypeDescription", "IBAN", "sortCode"
) AS 
SELECT 
  MIN("accounts"."Account_id") "Account_id", 
  MIN("accounts"."AccountName") "AccountName", 
  MIN("accounts"."NickName") "NickName", 
  MIN("accounts"."AccountHolder") "AccountHolder", 
  MIN("accounts"."UserName") "UserName", 
  MIN(
    "accounts"."ExternalBankidentity_id"
  ) "ExternalBankidentity_id", 
  MIN("accounts"."CurrencyCode") "CurrencyCode", 
  MIN("accounts"."User_id") "User_id", 
  MIN("accounts"."AvailableBalance") "AvailableBalance", 
  MIN("accounts"."Bank_id") "Bank_id", 
  "accounts"."ShowTransactions" "ShowTransactions", 
  MIN("accounts"."CurrentBalance") "CurrentBalance", 
  MIN("accounts"."SwiftCode") "SwiftCode", 
  MIN("accounts"."RoutingNumber") "RoutingNumber", 
  "transaction"."Id" "transactionId", 
  MIN("transaction"."Type_id") "transactiontype", 
  MIN("transaction"."Customer_id") "Customer_id", 
  MIN(
    "transaction"."ExpenseCategory_id"
  ) "ExpenseCategory_id", 
  MIN("transaction"."Bill_id") "Bill_id", 
  MIN("transaction"."Reference_id") "Reference_id", 
  MIN(
    "transaction"."fromAccountNumber"
  ) "fromAccountNumber", 
  MIN(
    "transaction"."fromAccountBalance"
  ) "fromAccountBalance", 
  MIN(
    "transaction"."toAccountNumber"
  ) "toAccountNumber", 
  MIN(
    "transaction"."toAccountBalance"
  ) "toAccountBalance", 
  MIN("transaction"."amount") "amount", 
  MIN("transaction"."Status_id") "Status_id", 
  MIN("transaction"."statusDesc") "statusDesc", 
  "transaction"."isScheduled" "isScheduled", 
  MIN("transaction"."category") "category", 
  MIN("transaction"."billCategory") "billCategory", 
  MIN(
    "transaction"."toExternalAccountNumber"
  ) "ExternalAccountNumber", 
  MIN("transaction"."Person_Id") "Person_Id", 
  MIN("transaction"."frequencyType") "frequencyType", 
  MIN("transaction"."createdDate") "createdDate", 
  MIN("transaction"."cashlessEmail") "cashlessEmail", 
  MIN("transaction"."cashlessMode") "cashlessMode", 
  MIN("transaction"."cashlessOTP") "cashlessOTP", 
  MIN(
    "transaction"."cashlessOTPValidDate"
  ) "cashlessOTPValidDate", 
  MIN(
    "transaction"."cashlessPersonName"
  ) "cashlessPersonName", 
  MIN("transaction"."cashlessPhone") "cashlessPhone", 
  MIN(
    "transaction"."cashlessSecurityCode"
  ) "cashlessSecurityCode", 
  MIN(
    "transaction"."cashWithdrawalTransactionStatus"
  ) "cashWithdrawalTransactionStatus", 
  MIN(
    "transaction"."frequencyEndDate"
  ) "frequencyEndDate", 
  MIN(
    "transaction"."frequencyStartDate"
  ) "frequencyStartDate", 
  MIN(
    "transaction"."hasDepositImage"
  ) "hasDepositImage", 
  MIN("transaction"."Payee_id") "payeeId", 
  MIN("transaction"."p2pContact") "p2pContact", 
  MIN("transaction"."Person_Id") "personId", 
  MIN("transaction"."recurrenceDesc") "recurrenceDesc", 
  MIN(
    "transaction"."numberOfRecurrences"
  ) "numberOfRecurrences", 
  MIN("transaction"."scheduledDate") "scheduledDate", 
  MIN(
    "transaction"."transactionComments"
  ) "transactionComments", 
  MIN("transaction"."notes") "transactionsNotes", 
  MIN("transaction"."description") "transDescription", 
  MIN(
    "transaction"."transactionDate"
  ) "transactionDate", 
  MIN(
    "transactiontype"."description"
  ) "description", 
  MIN(
    "accounttype"."TypeDescription"
  ) "TypeDescription", 
  MIN("transaction"."IBAN") "IBAN", 
  MIN("transaction"."sortCode") "sortCode" 
FROM 
  (
    (
      (
        "accounts" 
        JOIN "transaction" ON (
          "accounts"."Account_id" = "transaction"."toAccountNumber"
        )
      ) 
      JOIN "transactiontype" ON (
        "transaction"."Type_id" = "transactiontype"."Id"
      )
    ) 
    JOIN "accounttype" ON (
      "accounttype"."TypeID" = "accounts"."Type_id"
    )
  ) 
GROUP BY 
  "transaction"."Id", 
  "isScheduled", 
  "ShowTransactions" ;
  --  DDL for View travelnotifications_view
  
CREATE 
  OR REPLACE FORCE NONEDITIONABLE VIEW "travelnotifications_view" (
    "notificationId", "startDate", "endDate", 
    "destinations", "additionalNotes", 
    "Status_id", "contactNumber", "customerId", 
    "date", "cardNumber", "cardCount", 
    "status"
  ) AS 
SELECT 
  "travelnotification"."id" "notificationId", 
  "travelnotification"."PlannedDepartureDate" "startDate", 
  "travelnotification"."PlannedReturnDate" "endDate", 
  "travelnotification"."Destinations" "destinations", 
  "travelnotification"."AdditionalNotes" "additionalNotes", 
  "travelnotification"."Status_id" "Status_id", 
  "travelnotification"."phonenumber" "contactNumber", 
  "notificationcardinfo"."Customer_id" "customerId", 
  "travelnotification"."createdts" "date", 
  LISTAGG(
    CAST(
      (
        "notificationcardinfo"."CardName" || ' ' || "notificationcardinfo"."CardNumber"
      ) as nvarchar2(2000)
    ), 
    ','
  ) "cardNumber", 
  COUNT(
    "notificationcardinfo"."CardNumber"
  ) "cardCount", 
  "status"."Description" "status" 
FROM 
  (
    (
      "travelnotification" 
      JOIN "notificationcardinfo" ON (
        (
          "travelnotification"."id" = "notificationcardinfo"."Notification_id"
        )
      )
    ) 
    JOIN "status" ON (
      (
        "travelnotification"."Status_id" = "status"."id"
      )
    )
  ) 
GROUP BY 
  "travelnotification"."id", 
  "travelnotification"."PlannedDepartureDate", 
  "travelnotification"."PlannedReturnDate", 
  "travelnotification"."Destinations", 
  "travelnotification"."AdditionalNotes", 
  "travelnotification"."Status_id", 
  "travelnotification"."phonenumber", 
  "notificationcardinfo"."Customer_id", 
  "travelnotification"."createdts", 
  "status"."Description" ;
  --  DDL for View userbanksview
  
CREATE 
  OR REPLACE FORCE NONEDITIONABLE VIEW "userbanksview" (
    "User_id", "MainUser_id", "id", "BankName", 
    "BankId"
  ) AS 
SELECT 
  "externalbankidentity"."User_id" "User_id", 
  "externalbankidentity"."MainUser_id" "MainUser_id", 
  "externalbank"."id" "id", 
  "externalbank"."BankName" "BankName", 
  "externalbank"."BankId" "BankId" 
FROM 
  (
    "externalbank" CROSS 
    JOIN "externalbankidentity"
  ) 
WHERE 
  (
    "externalbank"."id" = "externalbankidentity"."ExternalBank_id"
  );
--  DDL for View wireaccounttransactionview

CREATE 
OR REPLACE FORCE NONEDITIONABLE VIEW "wireaccounttransactionview" (
  "Account_id", "AccountName", "AccountHolder", 
  "UserName", "ExternalBankidentity_id", 
  "CurrencyCode", "User_id", "AvailableBalance", 
  "Bank_id", "ShowTransactions", "CurrentBalance", 
  "SwiftCode", "RoutingNumber", "transactionId", 
  "transactiontype", "Customer_id", 
  "ExpenseCategory_id", "Bill_id", 
  "Reference_id", "fromAccountNumber", 
  "fromAccountBalance", "toAccountNumber", 
  "toAccountBalance", "amount", "Status_id", 
  "statusDesc", "isScheduled", "category", 
  "billCategory", "ExternalAccountNumber", 
  "Person_Id", "frequencyType", "createdDate", 
  "cashlessEmail", "cashlessMode", 
  "cashlessOTP", "cashlessOTPValidDate", 
  "cashlessPersonName", "cashlessPhone", 
  "cashlessSecurityCode", "cashWithdrawalTransactionStatus", 
  "frequencyEndDate", "frequencyStartDate", 
  "hasDepositImage", "payeeId", "payeeName", 
  "p2pContact", "personId", "recurrenceDesc", 
  "numberOfRecurrences", "scheduledDate", 
  "transactionComments", "transactionsNotes", 
  "transDescription", "transactionDate", 
  "frontImage1", "frontImage2", "backImage1", 
  "backImage2", "checkDesc", "checkNumber1", 
  "checkNumber2", "checkNumber", "checkReason", 
  "requestValidity", "checkDateOfIssue", 
  "bankName1", "bankName2", "withdrawlAmount1", 
  "withdrawlAmount2", "cashAmount", 
  "amountRecieved", "payeeCurrency", 
  "fee", "isDisputed", "disputeReason", 
  "disputeDescription", "disputeDate", 
  "disputeStatus", "description", 
  "nickName", "payeeAccountNumber", 
  "payeeType", "payeeAddressLine2", 
  "payeeAddressLine1", "iban"
) AS 
SELECT 
  MIN("accounts"."Account_id") "Account_id", 
  MIN("accounts"."AccountName") "AccountName", 
  MIN("accounts"."AccountHolder") "AccountHolder", 
  MIN("accounts"."UserName") "UserName", 
  MIN(
    "accounts"."ExternalBankidentity_id"
  ) "ExternalBankidentity_id", 
  MIN("accounts"."CurrencyCode") "CurrencyCode", 
  MIN("accounts"."User_id") "User_id", 
  MIN("accounts"."AvailableBalance") "AvailableBalance", 
  MIN("accounts"."Bank_id") "Bank_id", 
  "accounts"."ShowTransactions" "ShowTransactions", 
  MIN("accounts"."CurrentBalance") "CurrentBalance", 
  MIN("accounts"."SwiftCode") "SwiftCode", 
  MIN("accounts"."RoutingNumber") "RoutingNumber", 
  "transaction"."Id" "transactionId", 
  MIN("transaction"."Type_id") "transactiontype", 
  MIN("transaction"."Customer_id") "Customer_id", 
  MIN(
    "transaction"."ExpenseCategory_id"
  ) "ExpenseCategory_id", 
  MIN("transaction"."billid") "Bill_id", 
  MIN("transaction"."Reference_id") "Reference_id", 
  MIN(
    "transaction"."fromAccountNumber"
  ) "fromAccountNumber", 
  MIN(
    "transaction"."fromAccountBalance"
  ) "fromAccountBalance", 
  MIN(
    "transaction"."toAccountNumber"
  ) "toAccountNumber", 
  MIN(
    "transaction"."toAccountBalance"
  ) "toAccountBalance", 
  MIN("transaction"."amount") "amount", 
  MIN("transaction"."Status_id") "Status_id", 
  MIN("transaction"."statusDesc") "statusDesc", 
  "transaction"."isScheduled" "isScheduled", 
  MIN("transaction"."category") "category", 
  MIN("transaction"."billCategory") "billCategory", 
  MIN(
    "transaction"."toExternalAccountNumber"
  ) "ExternalAccountNumber", 
  MIN("transaction"."Person_Id") "Person_Id", 
  MIN("transaction"."frequencyType") "frequencyType", 
  MIN("transaction"."createdDate") "createdDate", 
  MIN("transaction"."cashlessEmail") "cashlessEmail", 
  MIN("transaction"."cashlessMode") "cashlessMode", 
  MIN("transaction"."cashlessOTP") "cashlessOTP", 
  MIN(
    "transaction"."cashlessOTPValidDate"
  ) "cashlessOTPValidDate", 
  MIN(
    "transaction"."cashlessPersonName"
  ) "cashlessPersonName", 
  MIN("transaction"."cashlessPhone") "cashlessPhone", 
  MIN(
    "transaction"."cashlessSecurityCode"
  ) "cashlessSecurityCode", 
  MIN(
    "transaction"."cashWithdrawalTransactionStatus"
  ) "cashWithdrawalTransactionStatus", 
  MIN(
    "transaction"."frequencyEndDate"
  ) "frequencyEndDate", 
  MIN(
    "transaction"."frequencyStartDate"
  ) "frequencyStartDate", 
  MIN(
    "transaction"."hasDepositImage"
  ) "hasDepositImage", 
  MIN("transaction"."Payee_id") "payeeId", 
  MIN("transaction"."payeeName") "payeeName", 
  MIN("transaction"."p2pContact") "p2pContact", 
  MIN("transaction"."Person_Id") "personId", 
  MIN("transaction"."recurrenceDesc") "recurrenceDesc", 
  MIN(
    "transaction"."numberOfRecurrences"
  ) "numberOfRecurrences", 
  MIN("transaction"."scheduledDate") "scheduledDate", 
  MIN(
    "transaction"."transactionComments"
  ) "transactionComments", 
  MIN("transaction"."notes") "transactionsNotes", 
  MIN("transaction"."description") "transDescription", 
  MIN(
    "transaction"."transactionDate"
  ) "transactionDate", 
  MIN("transaction"."frontImage1") "frontImage1", 
  MIN("transaction"."frontImage2") "frontImage2", 
  MIN("transaction"."backImage1") "backImage1", 
  MIN("transaction"."backImage2") "backImage2", 
  MIN("transaction"."checkDesc") "checkDesc", 
  MIN("transaction"."checkNumber1") "checkNumber1", 
  MIN("transaction"."checkNumber2") "checkNumber2", 
  MIN("transaction"."checkNumber") "checkNumber", 
  MIN("transaction"."checkReason") "checkReason", 
  MIN(
    "transaction"."requestValidity"
  ) "requestValidity", 
  MIN(
    "transaction"."checkDateOfIssue"
  ) "checkDateOfIssue", 
  MIN("transaction"."bankName1") "bankName1", 
  MIN("transaction"."bankName2") "bankName2", 
  MIN(
    "transaction"."withdrawlAmount1"
  ) "withdrawlAmount1", 
  MIN(
    "transaction"."withdrawlAmount2"
  ) "withdrawlAmount2", 
  MIN("transaction"."cashAmount") "cashAmount", 
  MIN("transaction"."amountRecieved") "amountRecieved", 
  MIN("transaction"."payeeCurrency") "payeeCurrency", 
  MIN("transaction"."fee") "fee", 
  "transaction"."isDisputed" "isDisputed", 
  MIN("transaction"."disputeReason") "disputeReason", 
  MIN(
    "transaction"."disputeDescription"
  ) "disputeDescription", 
  MIN("transaction"."disputeDate") "disputeDate", 
  MIN("transaction"."disputeStatus") "disputeStatus", 
  MIN(
    "transactiontype"."description"
  ) "description", 
  MIN("payee"."nickName") "nickName", 
  MIN("payee"."accountNumber") "payeeAccountNumber", 
  MIN("payee"."Type_id") "payeeType", 
  MIN("payee"."addressLine1") "payeeAddressLine2", 
  MIN("payee"."addressLine2") "payeeAddressLine1", 
  MIN("accounts"."IBAN") "iban" 
FROM 
  (
    (
      (
        "accounts" 
        JOIN "transaction" ON (
          (
            "accounts"."Account_id" = "transaction"."fromAccountNumber"
          ) 
          OR (
            "accounts"."Account_id" = "transaction"."toAccountNumber"
          )
        )
      ) 
      JOIN "transactiontype" ON (
        "transaction"."Type_id" = "transactiontype"."Id"
      )
    ) 
    JOIN "payee" ON (
      "transaction"."Payee_id" = "payee"."Id"
    )
  ) 
GROUP BY 
  "transaction"."Id", 
  "transaction"."isScheduled", 
  "transaction"."isDisputed", 
  "ShowTransactions"; 
  --  DDL for View wirecustaccounttransactionview
  
CREATE 
  OR REPLACE FORCE NONEDITIONABLE VIEW "wirecustaccounttransactionview" (
    "User_id", "transactionId", "transactiontype", 
    "Customer_id", "ExpenseCategory_id", 
    "Bill_id", "Reference_id", "fromAccountNumber", 
    "fromAccountBalance", "toAccountNumber", 
    "toAccountBalance", "amount", "Status_id", 
    "statusDesc", "isScheduled", "category", 
    "billCategory", "ExternalAccountNumber", 
    "Person_Id", "frequencyType", "createdDate", 
    "cashlessEmail", "cashlessMode", 
    "cashlessOTP", "cashlessOTPValidDate", 
    "cashlessPersonName", "cashlessPhone", 
    "cashlessSecurityCode", "cashWithdrawalTransactionStatus", 
    "frequencyEndDate", "frequencyStartDate", 
    "hasDepositImage", "payeeId", "payeeName", 
    "p2pContact", "personId", "recurrenceDesc", 
    "numberOfRecurrences", "scheduledDate", 
    "transactionComments", "transactionsNotes", 
    "transDescription", "transactionDate", 
    "frontImage1", "frontImage2", "backImage1", 
    "backImage2", "checkDesc", "checkNumber1", 
    "checkNumber2", "checkNumber", "checkReason", 
    "requestValidity", "checkDateOfIssue", 
    "bankName1", "bankName2", "withdrawlAmount1", 
    "withdrawlAmount2", "cashAmount", 
    "amountRecieved", "payeeCurrency", 
    "fee", "isDisputed", "disputeReason", 
    "disputeDescription", "disputeDate", 
    "disputeStatus", "description", 
    "nickName", "payeeAccountNumber", 
    "payeeType", "payeeAddressLine2", 
    "payeeAddressLine1"
  ) AS 
SELECT 
  DISTINCT "customeraccounts"."Customer_id" "User_id", 
  "transaction"."Id" "transactionId", 
  "transaction"."Type_id" "transactiontype", 
  "transaction"."Customer_id" "Customer_id", 
  "transaction"."ExpenseCategory_id" "ExpenseCategory_id", 
  "transaction"."billid" "Bill_id", 
  "transaction"."Reference_id" "Reference_id", 
  "transaction"."fromAccountNumber" "fromAccountNumber", 
  "transaction"."fromAccountBalance" "fromAccountBalance", 
  "transaction"."toAccountNumber" "toAccountNumber", 
  "transaction"."toAccountBalance" "toAccountBalance", 
  "transaction"."amount" "amount", 
  "transaction"."Status_id" "Status_id", 
  "transaction"."statusDesc" "statusDesc", 
  "transaction"."isScheduled" "isScheduled", 
  "transaction"."category" "category", 
  "transaction"."billCategory" "billCategory", 
  "transaction"."toExternalAccountNumber" "ExternalAccountNumber", 
  "transaction"."Person_Id" "Person_Id", 
  "transaction"."frequencyType" "frequencyType", 
  "transaction"."createdDate" "createdDate", 
  "transaction"."cashlessEmail" "cashlessEmail", 
  "transaction"."cashlessMode" "cashlessMode", 
  "transaction"."cashlessOTP" "cashlessOTP", 
  "transaction"."cashlessOTPValidDate" "cashlessOTPValidDate", 
  "transaction"."cashlessPersonName" "cashlessPersonName", 
  "transaction"."cashlessPhone" "cashlessPhone", 
  "transaction"."cashlessSecurityCode" "cashlessSecurityCode", 
  "transaction"."cashWithdrawalTransactionStatus" "cashWithdrawalTransactionStatus", 
  "transaction"."frequencyEndDate" "frequencyEndDate", 
  "transaction"."frequencyStartDate" "frequencyStartDate", 
  "transaction"."hasDepositImage" "hasDepositImage", 
  "transaction"."Payee_id" "payeeId", 
  "transaction"."payeeName" "payeeName", 
  "transaction"."p2pContact" "p2pContact", 
  "transaction"."Person_Id" "personId", 
  "transaction"."recurrenceDesc" "recurrenceDesc", 
  "transaction"."numberOfRecurrences" "numberOfRecurrences", 
  "transaction"."scheduledDate" "scheduledDate", 
  "transaction"."transactionComments" "transactionComments", 
  "transaction"."notes" "transactionsNotes", 
  "transaction"."description" "transDescription", 
  "transaction"."transactionDate" "transactionDate", 
  "transaction"."frontImage1" "frontImage1", 
  "transaction"."frontImage2" "frontImage2", 
  "transaction"."backImage1" "backImage1", 
  "transaction"."backImage2" "backImage2", 
  "transaction"."checkDesc" "checkDesc", 
  "transaction"."checkNumber1" "checkNumber1", 
  "transaction"."checkNumber2" "checkNumber2", 
  "transaction"."checkNumber" "checkNumber", 
  "transaction"."checkReason" "checkReason", 
  "transaction"."requestValidity" "requestValidity", 
  "transaction"."checkDateOfIssue" "checkDateOfIssue", 
  "transaction"."bankName1" "bankName1", 
  "transaction"."bankName2" "bankName2", 
  "transaction"."withdrawlAmount1" "withdrawlAmount1", 
  "transaction"."withdrawlAmount2" "withdrawlAmount2", 
  "transaction"."cashAmount" "cashAmount", 
  "transaction"."amountRecieved" "amountRecieved", 
  "transaction"."payeeCurrency" "payeeCurrency", 
  "transaction"."fee" "fee", 
  "transaction"."isDisputed" "isDisputed", 
  "transaction"."disputeReason" "disputeReason", 
  "transaction"."disputeDescription" "disputeDescription", 
  "transaction"."disputeDate" "disputeDate", 
  "transaction"."disputeStatus" "disputeStatus", 
  "transactiontype"."description" "description", 
  "payee"."nickName" "nickName", 
  "payee"."accountNumber" "payeeAccountNumber", 
  "payee"."Type_id" "payeeType", 
  "payee"."addressLine1" "payeeAddressLine2", 
  "payee"."addressLine2" "payeeAddressLine1" 
FROM 
  (
    (
      (
        "customeraccounts" CROSS 
        JOIN "transaction"
      ) CROSS 
      JOIN "transactiontype"
    ) CROSS 
    JOIN "payee"
  ) 
WHERE 
  (
    (
      (
        CAST(
          "customeraccounts"."Account_id" as float(53)
        ) = "transaction"."fromAccountNumber"
      ) 
      OR (
        CAST(
          "customeraccounts"."Account_id" as float(53)
        ) = "transaction"."toAccountNumber"
      )
    ) 
    AND (
      "transaction"."Type_id" = "transactiontype"."Id"
    ) 
    AND (
      "transaction"."Payee_id" = "payee"."Id"
    )
  );
--  DDL for Procedure account_action_approvers_proc


create or replace NONEDITIONABLE PROCEDURE         "account_action_approvers_proc" (
  "_contractId" IN VARCHAR2, "_cif" IN VARCHAR2, 
  "_accountIds" IN VARCHAR2, "_approvalActionList" IN VARCHAR2, 
  "_featureId" IN VARCHAR2, "records" OUT SYS_REFCURSOR
) AS v_customerIdList CLOB;
v_customerIdListWithNoAccountAccess CLOB;
v_NumberOfAccounts NUMBER(10, 0);
BEGIN 
SELECT 
  LISTAGG(
    CAST(
      temp.Customer_id AS VARCHAR2(255)
    )
  ) INTO v_customerIdList 
FROM 
  (
    SELECT 
      DISTINCT "customeraction"."Customer_id" Customer_id 
    FROM 
      "customeraction" 
    WHERE 
      "customeraction"."isAllowed" = '0' 
      AND "customeraction"."Action_id" = "_approvalActionList" 
      AND "customeraction"."Account_id" IN (
        SELECT 
          COLUMN_VALUE 
        FROM 
          TABLE(
            UTILS.STRING_SPLIT("_accountIds")
          )
      ) 
      AND "customeraction"."contractId" = "_contractId" 
      AND "customeraction"."coreCustomerId" = "_cif"
  ) temp;
v_customerIdList := CASE WHEN v_customerIdList IS NULL THEN '' ELSE v_customerIdList END;
v_NumberOfAccounts := LENGTH(
  ("_accountIds")
) - LENGTH(
  REPLACE(
    ("_accountIds"), 
    ',', 
    ' '
  )
) || 1;
SELECT 
  LISTAGG(
    CAST(
      temp."Customer_id" AS VARCHAR(255)
    )
  ) INTO v_customerIdListWithNoAccountAccess 
FROM 
  (
    SELECT 
      DISTINCT tempCustomers."Customer_id" 
    FROM 
      (
        SELECT 
          "Customer_id", 
          COUNT("Account_id") countAccounts 
        FROM 
          "customeraccounts" 
        WHERE 
          "Account_id" IN (
            SELECT 
              COLUMN_VALUE 
            FROM 
              TABLE(
                UTILS.STRING_SPLIT("_accountIds")
              )
          ) 
        GROUP BY 
          "customeraccounts"."Customer_id"
      ) tempCustomers 
    WHERE 
      tempCustomers.countAccounts != v_NumberOfAccounts
  ) temp;
v_customerIdListWithNoAccountAccess := CASE WHEN v_customerIdListWithNoAccountAccess IS NULL THEN '' ELSE v_customerIdListWithNoAccountAccess END;
OPEN "records" FOR 
SELECT 
  DISTINCT ("customer"."id") "id", 
  ("customer"."UserName") "userName", 
  ("membergroup"."Name") "groupId", 
  ("customer"."FirstName") "firstName", 
  ("customer"."LastName") "lastName" 
FROM 
  (
    "customer" 
    LEFT JOIN "contractcustomers" ON (
      "contractcustomers"."customerId" = "customer"."id" 
      AND "contractcustomers"."contractId" = "_contractId" 
      AND "contractcustomers"."coreCustomerId" = "_cif"
    ) 
    LEFT JOIN "customergroup" ON (
      "customergroup"."Customer_id" = "customer"."id"
    ) 
    LEFT JOIN "customeraction" ON (
      "customeraction"."Customer_id" = "customer"."id"
    ) 
    LEFT JOIN "membergroup" ON (
      "membergroup"."id" = "customergroup"."Group_id"
    ) 
    LEFT JOIN "groupactionlimit" ON (
      "groupactionlimit"."Group_id" = "customergroup"."Group_id"
    ) 
    JOIN "customeraccounts" ON (
      "customeraccounts"."Customer_id" = "customer"."id"
    ) 
    LEFT JOIN "contractfeatures" ON (
      "contractfeatures"."contractId" = "_contractId" 
      AND "contractfeatures"."coreCustomerId" = "_cif"
    )
  ) 
WHERE 
  "contractfeatures"."contractId" = "_contractId" 
  AND "contractfeatures"."coreCustomerId" = "_cif" 
  AND "contractfeatures"."featureId" = "_featureId" 
  AND "customeraccounts"."Account_id" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_accountIds")
      )
  ) 
  AND "customer"."Status_id" = 'SID_CUS_ACTIVE' 
  AND "customer"."id" NOT IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v_customerIdList)
      )
  ) 
  AND "customer"."id" NOT IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(
          v_customerIdListWithNoAccountAccess
        )
      )
  ) 
  AND "groupactionlimit"."Action_id" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_approvalActionList")
      )
  ) 
  AND "customeraction"."Action_id" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_approvalActionList")
      )
  );
--      DBMS_SQL.RETURN_RESULT(v_cursor);
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/


--  DDL for Procedure bulkwiretemplate_create_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "bulkwiretemplate_create_proc" (
  iv__bulkwiretemplateValues IN VARCHAR2, 
  iv__bulkwiretemplatelineitemValues IN VARCHAR2, 
  "bulkwiretemplate" OUT SYS_REFCURSOR
) AS v__bulkwiretemplateValues CLOB := iv__bulkwiretemplateValues;
v__bulkwiretemplatelineitemValues CLOB := iv__bulkwiretemplatelineitemValues;
v_index1 NUMBER(10, 0) := 0;
v_query1 VARCHAR2(4000);
v_msg VARCHAR2(4000);
v_id VARCHAR2(4000);
v_val VARCHAR2(4000);
v_query2 VARCHAR2(4000);
BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
SELECT 
  REPLACE(
    v__bulkwiretemplateValues, '"', ''''
  ) INTO v__bulkwiretemplateValues 
FROM 
  DUAL;
SELECT 
  REPLACE(
    v__bulkwiretemplatelineitemValues, 
    '"', ''''
  ) INTO v__bulkwiretemplatelineitemValues 
FROM 
  DUAL;
BEGIN BEGIN v_query1 := 'INSERT INTO "bulkwiretemplate"("bulkWireTemplateId","bulkWireTemplateName","noOfTransactions","noOfDomesticTransactions","noOfInternationalTransactions","createdBy","modifiedBy","company_id","createdts","lastmodifiedts","defaultFromAccount","defaultCurrency") VALUES (' || v__bulkwiretemplateValues || ')';
EXECUTE IMMEDIATE v_query1;
v_val := UTILS.SUBSTRING_INDEX(
  v__bulkwiretemplateValues, ',', 1
);
SELECT 
  REPLACE(
    RTRIM(
      LTRIM(v_val)
    ), 
    '''', 
    ' '
  ) INTO v_id 
FROM 
  DUAL;
v_query2 := 'INSERT INTO "bulkwiretemplatelineitems"("bulkWireTemplateID","createdts","lastmodifiedts","swiftCode","bulkWireTransferType","transactionType","internationalRoutingNumber","recipientName","recipientAddressLine1","recipientAddressLine2","recipientCity","recipientState","recipientCountryName","recipientZipCode","recipientBankName","recipientBankAddress1","recipientBankAddress2","recipientBankZipCode","recipientBankcity","recipientBankstate","accountNickname","recipientAccountNumber","routingNumber","createdby","modifiedBy","payeeId","templateRecipientCategory") values ' || v__bulkwiretemplatelineitemValues;
EXECUTE IMMEDIATE v_query2;
OPEN "bulkwiretemplate" FOR 
SELECT 
  * 
FROM 
  "bulkwiretemplate" 
WHERE 
  "bulkWireTemplateID" = v_id;
--DBMS_SQL.RETURN_RESULT("bulkwiretemplate");
END;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/
--  DDL for Procedure bulkwiretemplate_delete_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "bulkwiretemplate_delete_proc" (
  v__bulkwiretemplateID IN VARCHAR2, 
  v__bulkwiretemplatelineitemIDs IN VARCHAR2, 
  v_cursor OUT SYS_REFCURSOR
) AS v_filter VARCHAR2(4000);
v_totalCount VARCHAR2(4000);
v_DomCount VARCHAR2(4000);
v_InternationalCount VARCHAR2(4000);
BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
IF v__bulkwiretemplateID IS NOT NULL 
AND v__bulkwiretemplateID <> '' THEN IF v__bulkwiretemplatelineitemIDs IS NOT NULL 
AND v__bulkwiretemplatelineitemIDs <> '' THEN BEGIN 
UPDATE 
  "bulkwiretemplatelineitems" 
SET 
  "softdeleteflag" = 1 
WHERE 
  CAST(
    "bulkwiretemplatelineitems"."bulkWireTemplateLineItemID" AS VARCHAR(255)
  ) IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v__bulkwiretemplatelineitemIDs)
      )
  );
v_DomCount := 'SELECT count_big(*) FROM "bulkwiretemplatelineitems"
                     WHERE "bulkwiretemplatelineitems"."bulkWireTemplateID" =' || v__bulkwiretemplateID || ' AND 
               "bulkwiretemplatelineitems"."bulkWireTransferType" = ''Domestic'' AND "bulkwiretemplatelineitems"."softdeleteflag" = 0';
v_InternationalCount := 'SELECT count_big(*) FROM "bulkwiretemplatelineitems"
                     WHERE "bulkwiretemplatelineitems"."bulkWireTemplateID" =' || v__bulkwiretemplateID || ' AND 
                     "bulkwiretemplatelineitems"."bulkWireTransferType" = ''International'' AND "bulkwiretemplatelineitems"."softdeleteflag" = 0';
v_totalCount := v_DomCount + v_InternationalCount;
UPDATE 
  "bulkwiretemplate" 
SET 
  "noOfTransactions" = v_totalCount, 
  "noOfDomesticTransactions" = v_DomCount, 
  "noOfInternationalTransactions" = v_InternationalCount 
WHERE 
  "bulkwiretemplate"."bulkWireTemplateID" = v__bulkwiretemplateID;
OPEN v_cursor FOR 
SELECT 
  "bulkwiretemplate"."bulkWireTemplateID", 
  "bulkwiretemplate"."bulkWireTemplateName", 
  "bulkwiretemplate"."noOfTransactions", 
  "bulkwiretemplate"."noOfDomesticTransactions", 
  "bulkwiretemplate"."noOfInternationalTransactions", 
  "bulkwiretemplate"."createdBy", 
  "bulkwiretemplate"."modifiedBy", 
  "bulkwiretemplate"."createdts", 
  "bulkwiretemplate"."lastmodifiedts", 
  "bulkwiretemplate"."synctimestamp", 
  "bulkwiretemplate"."company_id", 
  "bulkwiretemplate"."softdeleteflag", 
  "bulkwiretemplate"."lastExecutedOn", 
  "bulkwiretemplate"."defaultFromAccount", 
  "bulkwiretemplate"."defaultCurrency", 
  "bulkwiretemplate"."deleteUniqueValue" 
FROM 
  "bulkwiretemplate" 
WHERE 
  "bulkwiretemplate"."bulkWireTemplateID" = v__bulkwiretemplateID;
--DBMS_SQL.RETURN_RESULT(v_cursor);
END;
ELSE BEGIN 
/*
                     *   SSMA warning messages:
                     *   M2SS0183: The following SQL clause was ignored during conversion: COLLATE utf8_general_ci.
                     */
UPDATE 
  "bulkwiretemplate" 
SET 
  "deleteUniqueValue" = v__bulkwiretemplateID 
WHERE 
  "bulkwiretemplate"."bulkWireTemplateID" = v__bulkwiretemplateID;
/*
                     *   SSMA warning messages:
                     *   M2SS0183: The following SQL clause was ignored during conversion: COLLATE utf8_general_ci.
                     */
UPDATE 
  "bulkwiretemplate" 
SET 
  "softdeleteflag" = 1 
WHERE 
  "bulkwiretemplate"."bulkWireTemplateID" = v__bulkwiretemplateID;
/*
                     *   SSMA warning messages:
                     *   M2SS0183: The following SQL clause was ignored during conversion: COLLATE utf8_general_ci.
                     */
UPDATE 
  "bulkwiretemplatelineitems" 
SET 
  "softdeleteflag" = 1 
WHERE 
  "bulkwiretemplatelineitems"."bulkWireTemplateID" = v__bulkwiretemplateID;
OPEN v_cursor FOR 
SELECT 
  'SUCCESS' N_SUCCESS_ 
FROM 
  DUAL;
--DBMS_SQL.RETURN_RESULT(v_cursor);
END;
END IF;
ELSE OPEN v_cursor FOR 
SELECT 
  'FAILED' N_FAILED_ 
FROM 
  DUAL;
--DBMS_SQL.RETURN_RESULT(v_cursor);
END IF;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/
--  DDL for Procedure bulkwiretemplate_update_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "bulkwiretemplate_update_proc" (
  v__bulkwiretemplateValues IN VARCHAR2, 
  v__update_bulkwiretemplatelineitemValues IN VARCHAR2, 
  v__insert_bulkwiretemplatelineitemValues IN VARCHAR2, 
  v_cursor OUT SYS_REFCURSOR
) AS v_msg VARCHAR2(4000);
v_query0 VARCHAR2(4000);
v_query1 VARCHAR2(4000);
v_query2 VARCHAR2(4000);
v_bulkWiretemplateID VARCHAR2(4000);
v_DomCount NUMBER(10, 0);
v_InternationalCount NUMBER(10, 0);
v_totalCount NUMBER(10, 0);
v_a VARCHAR2(4000);
BEGIN BEGIN BEGIN IF (
  v__bulkwiretemplateValues IS NULL 
  OR v__bulkwiretemplateValues = ''
) THEN OPEN v_cursor FOR 
SELECT 
  '@_bulkwiretemplateValues CANNOT BE NULL OR EMPTY' 
FROM 
  DUAL;
--DBMS_SQL.RETURN_RESULT(v_cursor);
ELSE DECLARE v_temp NUMBER(1, 0) := 0;
BEGIN v_a := v__bulkwiretemplateValues;
BEGIN 
SELECT 
  1 INTO v_temp 
FROM 
  DUAL 
WHERE 
  (
    (
      SELECT 
        COUNT(*) 
      FROM 
        "bulkwiretemplate" 
      WHERE 
        "bulkWireTemplateID" = 0
    ) = 0
  );
EXCEPTION WHEN OTHERS THEN NULL;
END;
IF v_temp = 1 THEN v_query0 := 'INSERT INTO "bulkwiretemplate"("bulkWireTemplateID","bulkWireTemplateName","createdBy","modifiedBy","lastmodifiedts","defaultFromAccount","defaultCurrency") VALUES (' || v__bulkwiretemplateValues || ')';
ELSE v_query0 := 'UPDATE "bulkwiretemplate" set 
                 "bulkWireTemplateName" = SUBSTRING(' || v__bulkwiretemplateValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',2)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))),
                 "modifiedBy" = SUBSTRING(' || v__bulkwiretemplateValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',4)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                 "lastmodifiedts" = SUBSTRING(' || v__bulkwiretemplateValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',5)),len(UTILS.SUBSTRING_INDEX(@a,'','',1)))), 
                 "defaultFromAccount" = SUBSTRING(' || v__bulkwiretemplateValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',6)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                 "defaultCurrency" = SUBSTRING(' || v__bulkwiretemplateValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',7)),len(UTILS.SUBSTRING_INDEX(@a,'','',1)))';
END IF;
EXECUTE IMMEDIATE v_query0;
IF (
  v__update_bulkwiretemplatelineitemValues IS NOT NULL 
  AND v__update_bulkwiretemplatelineitemValues != ''
) THEN DECLARE v_temp NUMBER(1, 0) := 0;
BEGIN BEGIN 
SELECT 
  1 INTO v_temp 
FROM 
  DUAL 
WHERE 
  (
    (
      SELECT 
        COUNT(*) 
      FROM 
        "bulkwiretemplatelineitems" 
      WHERE 
        "bulkWireTemplateLineItemID" = 0
    ) = 0
  );
EXCEPTION WHEN OTHERS THEN NULL;
END;
IF v_temp = 1 THEN v_query1 := 'INSERT INTO "bulkwiretemplatelineitems"("bulkWireTemplateLineItemID","bulkWireTemplateID","lastmodifiedts","swiftCode","bulkWireTransferType","transactionType","internationalRoutingNumber","recipientName","recipientAddressLine1","recipientAddressLine2","recipientCity","recipientState","recipientCountryName","recipientZipCode","recipientBankName","recipientBankAddress1","recipientBankAddress2","recipientBankZipCode","recipientBankcity","recipientBankstate","accountNickname","recipientAccountNumber","routingNumber","createdby","modifiedBy","payeeId","templateRecipientCategory") values (' || v__update_bulkwiretemplatelineitemValues || ')';
ELSE v_a := v__update_bulkwiretemplatelineitemValues;
END IF;
v_query1 := 'UPDATE "bulkwiretemplatelineitems"  set 
                     "lastmodifiedts" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',3)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "swiftcode" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',4)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "bulkWireTransferType" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',5)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "transactionType" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',6)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "internationalRoutingNumber" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',7)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "recipientName" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',8)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "recipientAddressLine1" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',9)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "recipientAddressLine2" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',10)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "recipientCity" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',11)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "recipientState" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',12)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "recipientCountryName" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',13)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "recipientZipCode" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',14)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "recipientBankName" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',15)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "recipientBankAddress1" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',16)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "recipientBankAddress2" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',17)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "recipientBankZipCode" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',18)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "recipientBankcity" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',19)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "recipientBankstate" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',20)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "accountNickname" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',21)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "recipientAccountNumber" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',22)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "routingNumber" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',23)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "createdby" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',24)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "modifiedBy" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',25)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "payeeId" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',26)),len(UTILS.SUBSTRING_INDEX(@a,'','',1))), 
                     "templateRecipientCategory" = SUBSTRING(' || v__update_bulkwiretemplatelineitemValues || ',len(UTILS.SUBSTRING_INDEX(@a,'','',27)),len(UTILS.SUBSTRING_INDEX(@a,'','',1)))';
EXECUTE IMMEDIATE v_query1;
END;
END IF;
IF (
  v__insert_bulkwiretemplatelineitemValues IS NOT NULL 
  AND v__insert_bulkwiretemplatelineitemValues != ''
) THEN v_query2 := 'INSERT INTO "bulkwiretemplatelineitems"("bulkWireTemplateID","createdts","lastmodifiedts","swiftCode","bulkWireTransferType","transactionType","internationalRoutingNumber","recipientName","recipientAddressLine1","recipientAddressLine2","recipientCity","recipientState","recipientCountryName","recipientZipCode","recipientBankName","recipientBankAddress1","recipientBankAddress2","recipientBankZipCode","recipientBankcity","recipientBankstate","accountNickname","recipientAccountNumber","routingNumber","createdby","modifiedBy","payeeId","templateRecipientCategory") values (' || v__insert_bulkwiretemplatelineitemValues || ')';
END IF;
EXECUTE IMMEDIATE v_query2;
v_bulkWiretemplateID := RTRIM(
  LTRIM(
    UTILS.SUBSTRING_INDEX(
      v__bulkwiretemplateValues, ',', 1
    )
  )
);
SELECT 
  COUNT(*) INTO v_DomCount 
FROM 
  "bulkwiretemplatelineitems" 
WHERE 
  "bulkWireTemplateID" = v_bulkWiretemplateID 
  AND "bulkWireTransferType" = 'Domestic' 
  AND "softdeleteflag" = 0;
SELECT 
  COUNT(*) INTO v_InternationalCount 
FROM 
  "bulkwiretemplatelineitems" 
WHERE 
  "bulkWireTemplateID" = v_bulkWiretemplateID 
  AND "bulkWireTransferType" = 'International' 
  AND "softdeleteflag" = 0;
v_totalCount := v_DomCount + v_InternationalCount;
UPDATE 
  "bulkwiretemplate" 
SET 
  "noOfTransactions" = v_totalCount, 
  "noOfDomesticTransactions" = v_DomCount, 
  "noOfInternationalTransactions" = v_InternationalCount 
WHERE 
  "bulkWireTemplateID" = v_bulkWiretemplateID;
OPEN v_cursor FOR 
SELECT 
  * 
FROM 
  "bulkwiretemplate" 
WHERE 
  "bulkWireTemplateID" = v_bulkWiretemplateID;
--DBMS_SQL.RETURN_RESULT(v_cursor);
END;
END IF;
END;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/
--  DDL for Procedure businesstyperole_get_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "businesstyperole_get_proc" (
  v__businessTypeId IN VARCHAR2, "groupbusinesstype" OUT SYS_REFCURSOR
) AS BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
OPEN "groupbusinesstype" FOR 
SELECT 
  "groupbusinesstype"."BusinessType_id" businessTypeId, 
  "membergroup"."id" groupId, 
  "membergroup"."Name" groupName, 
  "membergroup"."Description" groupDescription, 
  "groupbusinesstype"."isDefaultGroup" isDefaultGroup 
FROM 
  "groupbusinesstype" 
  LEFT JOIN "membergroup" ON (
    "groupbusinesstype"."Group_id" = "membergroup"."id"
  ) 
WHERE 
  "groupbusinesstype"."BusinessType_id" = v__businessTypeId;
--DBMS_SQL.RETURN_RESULT("groupbusinesstype");
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/
--  DDL for Procedure combinedaccess_deleteAndUpdatePreferences


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "combinedaccess_deleteAndUpdatePreferences" (
  "newCustomerId" IN VARCHAR2, "accountId_Del" IN VARCHAR2, 
  "accountType_Del" IN VARCHAR2, "deactivatedCustomerId" IN VARCHAR2, 
  "records" OUT SYS_REFCURSOR
) AS v_msg NVARCHAR2(2000);
BEGIN BEGIN DELETE "dbxcustomeralertentitlement" 
WHERE 
  "dbxcustomeralertentitlement"."Customer_id" = "deactivatedCustomerId" 
  AND "dbxcustomeralertentitlement"."AccountId" = "accountId_Del" 
  AND "dbxcustomeralertentitlement"."AccountType" = "accountType_Del";
DELETE "customeralertswitch" 
WHERE 
  "customeralertswitch"."Customer_id" = "deactivatedCustomerId" 
  AND "customeralertswitch"."AccountID" = "accountId_Del" 
  AND "customeralertswitch"."AccountType" = "accountType_Del";
DELETE "customeralertcategorychannel" 
WHERE 
  "customeralertcategorychannel"."Customer_id" = "deactivatedCustomerId" 
  AND "customeralertcategorychannel"."AccountId" = "accountId_Del" 
  AND "customeralertcategorychannel"."AccountType" = "accountType_Del";
UPDATE 
  "dbxcustomeralertentitlement" 
SET 
  "dbxcustomeralertentitlement"."Customer_id" = "newCustomerId" 
WHERE 
  "dbxcustomeralertentitlement"."Customer_id" = "deactivatedCustomerId";
UPDATE 
  "customeralertswitch" 
SET 
  "customeralertswitch"."Customer_id" = "newCustomerId" 
WHERE 
  "customeralertswitch"."Customer_id" = "deactivatedCustomerId";
UPDATE 
  "customeralertcategorychannel" 
SET 
  "customeralertcategorychannel"."Customer_id" = "newCustomerId" 
WHERE 
  "customeralertcategorychannel"."Customer_id" = "deactivatedCustomerId";
COMMIT;
v_msg := 'success';
open "records" for 
select 
  v_msg 
from 
  dual;
END;
EXCEPTION WHEN OTHERS THEN BEGIN ROLLBACK;
v_msg := SQLERRM;
OPEN "records" FOR 
SELECT 
  v_msg "errmsg" 
FROM 
  DUAL;
END;
END;
/
--  DDL for Procedure combinedaccess_updatePreferences_Delink


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "combinedaccess_updatePreferences_Delink" (
  "newCustomerId" IN VARCHAR2, "accountIds" IN VARCHAR2, 
  "combinedCustomerId" IN VARCHAR2, 
  "records" out SYS_REFCURSOR
) AS v_msg NVARCHAR2(2000);
BEGIN BEGIN BEGIN 
UPDATE 
  "dbxcustomeralertentitlement" 
SET 
  "dbxcustomeralertentitlement"."Customer_id" = "newCustomerId" 
WHERE 
  "dbxcustomeralertentitlement"."Customer_id" = "combinedCustomerId" 
  AND FIND_IN_SET(
    "dbxcustomeralertentitlement"."AccountId", 
    "accountIds"
  ) > 0;
UPDATE 
  "customeralertswitch" 
SET 
  "customeralertswitch"."Customer_id" = "newCustomerId" 
WHERE 
  "customeralertswitch"."Customer_id" = "combinedCustomerId" 
  AND FIND_IN_SET(
    "customeralertswitch"."AccountID", 
    "accountIds"
  ) > 0;
UPDATE 
  "customeralertcategorychannel" 
SET 
  "customeralertcategorychannel"."Customer_id" = "newCustomerId" 
WHERE 
  "customeralertcategorychannel"."Customer_id" = "combinedCustomerId" 
  AND FIND_IN_SET(
    "customeralertcategorychannel"."AccountId", 
    "accountIds"
  ) > 0;
commit;
v_msg := 'success';
open "records" for 
select 
  v_msg 
from 
  dual;
END;
EXCEPTION WHEN OTHERS THEN BEGIN ROLLBACK;
v_msg := SQLERRM;
OPEN "records" FOR 
SELECT 
  v_msg "errmsg" 
FROM 
  DUAL;
END;
END;
END;
/
--  DDL for Procedure contract_action_limit_save_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "contract_action_limit_save_proc" ("_queryInput" IN long) AS v_index NUMBER(10, 0);
v_numOfRecords NUMBER(10, 0);
v_recordsData CLOB;
v_accountId VARCHAR2(4000);
v_query VARCHAR2(4000);
v_input CLOB;
BEGIN v_input := TO_CLOB("_queryInput");
v_index := 0;
v_numOfRecords := LENGTH(v_input) - LENGTH(
  REPLACE(v_input, '|', '')
) + 1;
WHILE (1 = 1) LOOP BEGIN v_index := v_index + 1;
IF v_index = v_numOfRecords + 1 THEN EXIT;
else v_recordsData := UTILS.SUBSTRING_INDEX(v_input, '|', v_index);
v_recordsData := UTILS.SUBSTRING_INDEX(v_recordsData, '|', -1);
v_recordsData := '''' || CAST(
  SYS_GUID() AS VARCHAR2
) || '''' || ',' || v_recordsData;
v_accountId := UTILS.SUBSTRING_INDEX(v_recordsData, '",', 4);
v_accountId := UTILS.SUBSTRING_INDEX(v_accountId, '"', -1);
v_recordsData := REPLACE(v_recordsData, '"', '''');
v_recordsData := REPLACE(v_recordsData, '''''', 'null');
IF(v_accountId is null) THEN v_query := 'INSERT INTO "contractactionlimit"("id","contractId","coreCustomerId","isPortfolio","accountId","featureId","actionId","limitGroupId","limitTypeId","value","companyLegalUnit") VALUES (' || v_recordsData || ')';
ELSE v_query := 'INSERT INTO "accountlevelactionlimit"("id","contractId","coreCustomerId","isPortfolio","accountId","featureId","actionId","limitGroupId","limitTypeId","value","companyLegalUnit") VALUES (' || v_recordsData || ')';
END IF;
EXECUTE IMMEDIATE v_query;
END IF;
END;
END LOOP;
END;
/
--  DDL for Procedure contract_action_limit_update


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "contract_action_limit_update" (
  v__contractActionLimit IN VARCHAR2
) AS v_index1 NUMBER(10, 0) := 0;
v_numOfRecords NUMBER(10, 0) := 0;
v_contractValues VARCHAR2(4000);
v_contractId VARCHAR2(4000);
v_coreCustomerId VARCHAR2(4000);
v_featureId VARCHAR2(4000);
v_isNewAction VARCHAR2(255);
v_actionId VARCHAR2(4000);
v_limitTypeId VARCHAR2(4000);
v_limitValue VARCHAR2(4000);
v_legalEntityId VARCHAR2(4000);
BEGIN v_numOfRecords := LENGTH(v__contractActionLimit) - LENGTH(
  REPLACE(v__contractActionLimit, '|', ' ')
) || 1;
WHILE 1 = 1 LOOP BEGIN v_index1 := v_index1 + 1;
IF v_index1 = v_numOfRecords + 1 THEN BEGIN EXIT;
END;
ELSE DECLARE v_num NUMBER(20, 2);
BEGIN v_contractValues := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(
    v__contractActionLimit, '|', v_index1
  ), 
  '|', 
  -1
);
v_contractId := UTILS.SUBSTRING_INDEX(v_contractValues, ',', 1);
v_coreCustomerId := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v_contractValues, ',', 2), 
  ',', 
  -1
);
v_featureId := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v_contractValues, ',', 3), 
  ',', 
  -1
);
v_actionId := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v_contractValues, ',', 4), 
  ',', 
  -1
);
v_isNewAction := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v_contractValues, ',', 5), 
  ',', 
  -1
);
v_legalEntityId := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v_contractValues, ',', 6), 
  ',', 
  -1
);
v_limitTypeId := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v_contractValues, ',', 7), 
  ',', 
  -1
);
v_limitValue := UTILS.SUBSTRING_INDEX(v_contractValues, ',', -1);
v_num := CAST(v_limitValue AS NUMBER);
UPDATE 
  "contractactionlimit" 
SET 
  "value" = v_num 
WHERE 
  "contractId" = v_contractId 
  AND "coreCustomerId" = v_coreCustomerId 
  AND "featureId" = v_featureId 
  AND "actionId" = v_actionId 
  AND "limitTypeId" = v_limitTypeId 
  AND "companyLegalUnit" = v_legalEntityId;
END;
END IF;
END;
END LOOP;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/
--  DDL for Procedure contract_actionlimits_create_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "contract_actionlimits_create_proc" ("_queryInput" IN VARCHAR2) AS v_index NUMBER(10, 0);
v_input CLOB;
v_query VARCHAR2(4000);
v_numOfRecords NUMBER(10, 0);
v_id VARCHAR2(255) := '';
v_group_concat_max_len NUMBER(19, 0);
v_serviceDefinitionId VARCHAR2(4000) := '';
v_recordsData VARCHAR2(4000) := '';
v_contractId VARCHAR2(4000) := '';
v_customerId VARCHAR2(4000) := '';
v_accountId VARCHAR2(4000) := '';
v_featureId VARCHAR2(4000) := '';
v_legalEntityId VARCHAR2(4000) := '';
v_actionId VARCHAR2(4000) := '';
v_isNewAction VARCHAR2(4000) := '';
v_limitId VARCHAR2(4000) := '';
v_limitValue VARCHAR2(4000) := '';
v_limitAtFI VARCHAR2(4000) := '';
v_contarctFeatures CLOB := '';
v_limitATServiceDefinition VARCHAR2(4000) := '';
v_recordsDataWithoutLimits VARCHAR2(4000) := '';
v_serviceDefinitionActions CLOB := '';
v_existingActionLimitRecords CLOB := '';
v_existingActionRecords CLOB := '';
v_existingActionRecords1 CLOB := '';
v_tempLimitValue DECIMAL(20, 2) := 0;
BEGIN v_input := TO_CLOB("_queryInput");
v_index := 0;
v_numOfRecords := LENGTH(v_input) - LENGTH(
  REPLACE(v_input, '|', '')
) + 1;
dbms_output.put_line(
  'v_numOfRecords' || v_numOfRecords
);
WHILE 1 = 1 LOOP BEGIN v_index := v_index + 1;
IF v_index = v_numOfRecords + 1 THEN EXIT;
ELSE BEGIN v_query := '';
v_recordsData := (
  '"' || SYS_GUID() || '"' || ',' || UTILS.SUBSTRING_INDEX(
    UTILS.SUBSTRING_INDEX(v_input, '|', v_index), 
    '|', 
    -1
  )
);
v_contractId := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v_recordsData, '",', 2), 
  '"', 
  -1
);
v_customerId := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v_recordsData, '",', 3), 
  '"', 
  -1
);
v_accountId := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v_recordsData, '",', 4), 
  '"', 
  -1
);
v_featureId := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v_recordsData, '",', 5), 
  ',"', 
  -1
);
v_legalEntityId := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v_recordsData, '",', 8), 
  ',"', 
  -1
);
v_actionId := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v_recordsData, '",', 6), 
  ',"', 
  -1
);
v_isNewAction := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v_recordsData, '",', 7), 
  ',"', 
  -1
);
v_limitId := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v_recordsData, '",', 9), 
  ',"', 
  -1
);
v_limitValue := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v_recordsData, ',"', -1), 
  '"', 
  1
);
v_recordsDataWithoutLimits := CONCAT(
  UTILS.SUBSTRING_INDEX(v_recordsData, '",', 8), 
  '"'
);
SELECT 
  "servicedefinitionId" INTO v_serviceDefinitionId 
FROM 
  "contract" 
WHERE 
  "id" = v_contractId;
IF v_limitId <> '@' 
AND v_limitValue <> '@' THEN BEGIN 
SELECT 
  "actionlimit"."value" INTO v_limitAtFI 
FROM 
  "actionlimit" 
WHERE 
  "actionlimit"."Action_id" = v_actionId 
  AND "actionlimit"."LimitType_id" = v_limitId 
  AND "actionlimit"."companyLegalUnit" = v_legalEntityId;
SELECT 
  "servicedefinitionactionlimit"."value" INTO v_limitATServiceDefinition 
FROM 
  "servicedefinitionactionlimit" 
WHERE 
  "servicedefinitionactionlimit"."actionId" = v_actionId 
  AND "servicedefinitionactionlimit"."limitTypeId" = v_limitId 
  AND "servicedefinitionactionlimit"."serviceDefinitionId" = v_serviceDefinitionId 
  AND "servicedefinitionactionlimit"."companyLegalUnit" = v_legalEntityId;
IF v_limitAtFI > v_limitATServiceDefinition THEN v_tempLimitValue := v_limitATServiceDefinition;
ELSE v_tempLimitValue := v_limitAtFI;
END IF;
IF v_tempLimitValue > v_limitValue THEN v_tempLimitValue := v_limitValue;
END IF;
END;
END IF;
IF CASE WHEN NOT CASE WHEN (v_tempLimitValue) IS NULL THEN 1 ELSE 0 END <> 0 THEN 1 ELSE 0 END <> 0 
AND v_tempLimitValue is not null THEN v_limitValue := v_tempLimitValue;
END IF;
SELECT 
  LISTAGG(
    CAST(
      "contractfeatures"."id" AS VARCHAR2(255)
    ), 
    ','
  ) INTO v_contarctFeatures 
FROM 
  "contractfeatures" 
WHERE 
  "contractfeatures"."contractId" = v_contractId 
  AND "contractfeatures"."coreCustomerId" = v_customerId 
  AND "contractfeatures"."featureId" = v_featureId 
  AND "contractfeatures"."companyLegalUnit" = v_legalEntityId;
SELECT 
  LISTAGG(
    CAST(
      "servicedefinitionactionlimit"."id" AS VARCHAR2(255)
    ), 
    ','
  ) INTO v_serviceDefinitionActions 
FROM 
  "servicedefinitionactionlimit" 
WHERE 
  "servicedefinitionactionlimit"."actionId" = v_actionId 
  AND "servicedefinitionactionlimit"."serviceDefinitionId" = v_serviceDefinitionId 
  AND "servicedefinitionactionlimit"."companyLegalUnit" = v_legalEntityId;
SELECT 
  LISTAGG(
    CAST(
      "contractactionlimit"."id" AS VARCHAR2(255)
    ), 
    ','
  ) INTO v_existingActionLimitRecords 
FROM 
  "contractactionlimit" 
WHERE 
  "contractactionlimit"."contractId" = v_contractId 
  AND "contractactionlimit"."coreCustomerId" = v_customerId 
  AND "contractactionlimit"."featureId" = v_featureId 
  AND "contractactionlimit"."actionId" = v_actionId 
  AND "contractactionlimit"."limitTypeId" = v_limitId 
  AND "contractactionlimit"."companyLegalUnit" = v_legalEntityId;
SELECT 
  LISTAGG(
    CAST(
      "contractactionlimit"."id" AS VARCHAR(255)
    ), 
    ','
  ) INTO v_existingActionRecords 
FROM 
  "contractactionlimit" 
WHERE 
  "contractactionlimit"."contractId" = v_contractId 
  AND "contractactionlimit"."coreCustomerId" = v_customerId 
  AND "contractactionlimit"."featureId" = v_featureId 
  AND "contractactionlimit"."actionId" = v_actionId 
  AND "contractactionlimit"."companyLegalUnit" = v_legalEntityId;
SELECT 
  LISTAGG(
    CAST(
      "accountlevelactionlimit"."id" AS VARCHAR2(255)
    )
  ) INTO v_existingActionRecords1 
FROM 
  "accountlevelactionlimit" 
WHERE 
  "accountlevelactionlimit"."contractId" = v_contractId 
  AND "accountlevelactionlimit"."coreCustomerId" = v_customerId 
  AND "accountlevelactionlimit"."accountId" = v_accountId 
  AND "accountlevelactionlimit"."featureId" = v_featureId 
  AND "accountlevelactionlimit"."actionId" = v_actionId 
  AND "accountlevelactionlimit"."companyLegalUnit" = v_legalEntityId;
IF v_contarctFeatures IS NOT NULL 
AND v_serviceDefinitionActions IS NOT NULL THEN BEGIN v_query := (v_query) || (
  'UPDATE "contractactionlimit" SET "value" = '
) || ('''') || (v_limitValue) || ('''') || (
  'WHERE "contractactionlimit"."id" = '
) || ('''') || (v_existingActionLimitRecords) || ('''');
IF (
  v_limitId = '@' 
  OR v_limitValue = '@'
) 
AND (
  v_existingActionRecords IS NULL 
  OR v_existingActionRecords = ''
) THEN BEGIN 
SELECT 
  SUBSTR(
    SYS_GUID(), 
    0, 
    50
  ) INTO v_id 
FROM 
  DUAL;
v_query := (v_query) || (
  'INSERT INTO "contractactionlimit"("contractactionlimit"."id","contractactionlimit"."contractId","contractactionlimit"."coreCustomerId","contractactionlimit"."featureId","contractactionlimit"."actionId", "contractactionlimit"."isNewAction", "contractactionlimit"."companyLegalUnit") VALUES ('''
) || (v_id) || (''',''') || (v_contractId) || (''',''') || (v_customerId) || (''',''') || (v_featureId) || (''',''') || (v_actionId) || (''',''') || (v_isNewAction) || (''',''') || (v_legalEntityId) || (''')');
IF v_limitId != '@' 
AND v_limitValue IS NOT NULL 
AND v_tempLimitValue IS NOT NULL THEN BEGIN 
SELECT 
  SUBSTR(
    SYS_GUID(), 
    0, 
    50
  ) INTO v_id 
FROM 
  DUAL;
v_query := (v_query) || (
  'INSERT INTO "contractactionlimit"("contractactionlimit"."id","contractactionlimit"."contractId","contractactionlimit"."coreCustomerId","contractactionlimit"."featureId","contractactionlimit"."actionId","contractactionlimit"."isNewAction", "contractactionlimit"."companyLegalUnit", "contractactionlimit"."limitTypeId","contractactionlimit"."value") VALUES ('''
) || (v_id) || (''',''') || (v_contractId) || (''',''') || (v_customerId) || (''',''') || (v_featureId) || (''',''') || (v_actionId) || (''',''') || +(v_isNewAction) || (''',''') || (v_legalEntityId) || (''',''') || (v_limitId) || (''',''') || (v_limitValue) || (''')');
END;
END IF;
END;
END IF;
IF (
  v_limitId = '@' 
  OR v_limitValue = '@'
) 
AND (
  v_existingActionRecords1 IS NULL 
  OR v_existingActionRecords1 = ''
) THEN BEGIN 
SELECT 
  SUBSTR(
    SYS_GUID(), 
    0, 
    50
  ) INTO v_id 
FROM 
  DUAL;
v_query := (v_query) || (
  'INSERT INTO "accountlevelactionlimit"("accountlevelactionlimit"."id","accountlevelactionlimit"."contractId","accountlevelactionlimit"."coreCustomerId","accountlevelactionlimit"."featureId","contractactionlimit"."actionId", "accountlevelactionlimit"."isNewAction", "accountlevelactionlimit"."companyLegalUnit") VALUES ('''
) || (v_id) || (''',''') || (v_contractId) || (''',''') || (v_customerId) || (''',''') || (v_featureId) || (''',''') || (v_actionId) || (''',''') || (v_isNewAction) || (''',''') || (v_legalEntityId) || (''')');
IF v_limitId != '@' 
AND v_limitValue IS NOT NULL 
AND v_tempLimitValue IS NOT NULL THEN BEGIN 
SELECT 
  SUBSTR(
    SYS_GUID(), 
    0, 
    50
  ) INTO v_id 
FROM 
  DUAL;
v_query := (v_query) || (
  'INSERT INTO "accountlevelactionlimit"("accountlevelactionlimit"."id","accountlevelactionlimit"."contractId","accountlevelactionlimit"."coreCustomerId","accountlevelactionlimit"."featureId","accountlevelactionlimit"."actionId","accountlevelactionlimit"."isNewAction", "accountlevelactionlimit"."companyLegalUnit","accountlevelactionlimit"."limitTypeId","accountlevelactionlimit"."value") VALUES ('''
) || (v_id) || (''',''') || (v_contractId) || (''',''') || (v_customerId) || (''',''') || (v_featureId) || (''',''') || (v_actionId) || (''',''') || +(v_isNewAction) || (''',''') || (v_legalEntityId) || (''',''') || (v_limitId) || (''',''') || (v_limitValue) || (''')');
END;
END IF;
END;
END IF;
END;
END IF;
dbms_output.put_line('v_query' || v_query);
EXECUTE IMMEDIATE v_query;
END;
END IF;
END;
END LOOP;
END;
/
--  DDL for Procedure contract_address_communication_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "contract_address_communication_proc" (
  "_contractId" IN NVARCHAR2, "_legalEntityId" IN NVARCHAR2, 
  "records" out sys_refcursor, "records1" out sys_refcursor, 
  "records2" out sys_refcursor
) AS BEGIN OPEN "records" FOR 
SELECT 
  "contract".*, 
  "servicedefinition"."name" "servicedefinitionName" 
FROM 
  "contract" 
  Join "servicedefinition" ON (
    "contract"."servicedefinitionId" = "servicedefinition"."id"
  ) 
WHERE 
  (
    "contract"."id" = "_contractId" 
    AND "contract"."companyLegalUnit" = "_legalEntityId"
  );
OPEN "records1" FOR 
SELECT 
  * 
From 
  "contractcommunication" 
WHERE 
  (
    "contractId" = "_contractId" 
    AND "companyLegalUnit" = "_legalEntityId"
  );
OPEN "records2" FOR 
SELECT 
  * 
FROM 
  "address" 
WHERE 
  "id" IN (
    SELECT 
      DISTINCT "addressId" 
    from 
      "contractaddress" 
    WHERE 
      (
        "contractId" = "_contractId" 
        AND "companyLegalUnit" = "_legalEntityId"
      )
  );
END;
/
--  DDL for Procedure contract_communication_address_delete_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "contract_communication_address_delete_proc" (v__contractId IN VARCHAR2) AS BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
DELETE "contractcommunication" 
WHERE 
  "contractcommunication"."contractId" = v__contractId;
DELETE "contractaddress" 
WHERE 
  "contractaddress"."contractId" = v__contractId;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/
--  DDL for Procedure contract_corecustomer_accounts_delete_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "contract_corecustomer_accounts_delete_proc" (
  "_contractId" IN VARCHAR2, "_coreCustomerId" IN VARCHAR2, 
  "_accountsCSV" IN VARCHAR2
) AS BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
DELETE "customeraction" 
WHERE 
  "customeraction"."contractId" = "_contractId" 
  AND "customeraction"."Account_id" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_accountsCSV")
      )
  ) 
  AND "customeraction"."coreCustomerId" = "_coreCustomerId";
DELETE "customeraccounts" 
WHERE 
  "customeraccounts"."contractId" = "_contractId" 
  AND "customeraccounts"."Account_id" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_accountsCSV")
      )
  ) 
  AND "customeraccounts"."coreCustomerId" = "_coreCustomerId";
DELETE "contractaccounts" 
WHERE 
  "contractaccounts"."contractId" = "_contractId" 
  AND "contractaccounts"."accountId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_accountsCSV")
      )
  ) 
  AND "contractaccounts"."coreCustomerId" = "_coreCustomerId";
END;
/
--  DDL for Procedure contract_corecustomer_accounts_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "contract_corecustomer_accounts_proc" (
  "_contractId" IN NVARCHAR2, "records" OUT SYS_REFCURSOR, 
  "records1" OUT SYS_REFCURSOR
) AS BEGIN OPEN "records" FOR 
SELECT 
  * 
FROM 
  "contractcorecustomers" 
WHERE 
  (
    "contractcorecustomers"."contractId" = "_contractId"
  );
OPEN "records1" FOR 
SELECT 
  * 
FROM 
  "contractaccounts" 
WHERE 
  (
    "contractaccounts"."contractId" = "_contractId"
  );
END;
/
--  DDL for Procedure contract_corecustomer_actions_delete_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "contract_corecustomer_actions_delete_proc" (
  "_contractId" IN VARCHAR2, "_coreCustomerId" IN VARCHAR2, 
  "_accountId" IN VARCHAR2, "_actionsCSV" IN VARCHAR2
) AS BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
DELETE "customeraction" 
WHERE 
  "customeraction"."contractId" = "_contractId" 
  AND "customeraction"."Action_id" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_actionsCSV")
      )
  ) 
  AND "customeraction"."coreCustomerId" = "_coreCustomerId";
IF ("_accountId" is NULL) THEN BEGIN DELETE "contractactionlimit" 
WHERE 
  "contractactionlimit"."contractId" = "_contractId" 
  AND "contractactionlimit"."actionId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_actionsCSV")
      )
  ) 
  AND "contractactionlimit"."coreCustomerId" = "_coreCustomerId";
END;
END IF;
IF "_accountId" IS NOT NULL THEN BEGIN DELETE "accountlevelactionlimit" 
WHERE 
  "accountlevelactionlimit"."contractId" = "_contractId" 
  AND "accountlevelactionlimit"."actionId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_actionsCSV")
      )
  ) 
  AND "accountlevelactionlimit"."coreCustomerId" = "_coreCustomerId" 
  AND "accountlevelactionlimit"."accountId" = "_accountId";
END;
END IF;
END;
/
--  DDL for Procedure contract_corecustomer_details_get_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "contract_corecustomer_details_get_proc" (
  "_contractId" IN NVARCHAR2, "_coreCustomerId" IN NVARCHAR2, 
  "records" out SYS_REFCURSOR, "records1" out SYS_REFCURSOR, 
  "records2" out SYS_REFCURSOR
) AS v_customerAccounts long := '';
v_customerFeatures long := '';
v_customerActions long := '';
BEGIN 
SELECT 
  listagg(
    CAST(
      "contractaccounts"."accountId" AS NVARCHAR2(2000)
    ), 
    ','
  ) INTO v_customerAccounts 
FROM 
  "contractaccounts" 
WHERE 
  "contractaccounts"."contractId" = "_contractId" 
  AND "contractaccounts"."coreCustomerId" = "_coreCustomerId";
SELECT 
  listagg(
    CAST(
      "contractfeatures"."featureId" AS NVARCHAR2(2000)
    ), 
    ','
  ) INTO v_customerFeatures 
FROM 
  "contractfeatures" 
WHERE 
  "contractfeatures"."contractId" = "_contractId" 
  AND "contractfeatures"."coreCustomerId" = "_coreCustomerId";
SELECT 
  rtrim(
    xmlagg(
      XMLELEMENT(
        e, "contractactionlimit"."actionId", 
        ','
      ).EXTRACT('//text()')
    ).GetClobVal(), 
    ','
  ) INTO v_customerActions 
FROM 
  "contractactionlimit" 
WHERE 
  "contractactionlimit"."contractId" = "_contractId" 
  AND "contractactionlimit"."coreCustomerId" = "_coreCustomerId";
OPEN "records" FOR 
SELECT 
  v_customerAccounts "coreCustomerAccounts" 
FROM 
  DUAL;
OPEN "records1" FOR 
SELECT 
  v_customerFeatures "coreCustomerFeatures" 
FROM 
  DUAL;
OPEN "records2" FOR 
SELECT 
  v_customerActions "coreCustomerActions" 
FROM 
  DUAL;
END;
/
--  DDL for Procedure contract_corecustomer_features_delete_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "contract_corecustomer_features_delete_proc" (
  v__contractId IN VARCHAR2, v__coreCustomerId IN VARCHAR2, 
  v__featuresCSV IN VARCHAR2
) AS BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
DELETE "customeraction" 
WHERE 
  "customeraction"."contractId" = v__contractId 
  AND "customeraction"."featureId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v__featuresCSV)
      )
  ) 
  AND "customeraction"."coreCustomerId" = v__coreCustomerId;
DELETE "contractfeatures" 
WHERE 
  "contractfeatures"."contractId" = v__contractId 
  AND "contractfeatures"."featureId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v__featuresCSV)
      )
  ) 
  AND "contractfeatures"."coreCustomerId" = v__coreCustomerId;
DELETE "contractactionlimit" 
WHERE 
  "contractactionlimit"."contractId" = v__contractId 
  AND "contractactionlimit"."featureId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v__featuresCSV)
      )
  ) 
  AND "contractactionlimit"."coreCustomerId" = v__coreCustomerId;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/
--  DDL for Procedure contract_corecustomers_delete_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "contract_corecustomers_delete_proc" (
  v__contractCustomersList IN VARCHAR2, 
  v__contractId IN VARCHAR2
) AS BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
DELETE "customergroup" 
WHERE 
  "customergroup"."contractId" = v__contractId 
  AND "customergroup"."coreCustomerId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v__contractCustomersList)
      )
  );
DELETE "customeraction" 
WHERE 
  "customeraction"."contractId" = v__contractId 
  AND "customeraction"."coreCustomerId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v__contractCustomersList)
      )
  );
DELETE "customeraccounts" 
WHERE 
  "customeraccounts"."contractId" = v__contractId 
  AND "customeraccounts"."coreCustomerId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v__contractCustomersList)
      )
  );
DELETE "contractcustomers" 
WHERE 
  "contractcustomers"."contractId" = v__contractId 
  AND "contractcustomers"."coreCustomerId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v__contractCustomersList)
      )
  );
DELETE "contractcorecustomers" 
WHERE 
  "contractcorecustomers"."contractId" = v__contractId 
  AND "contractcorecustomers"."coreCustomerId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v__contractCustomersList)
      )
  );
DELETE "contractaccounts" 
WHERE 
  "contractaccounts"."contractId" = v__contractId 
  AND "contractaccounts"."coreCustomerId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v__contractCustomersList)
      )
  );
DELETE "contractfeatures" 
WHERE 
  "contractfeatures"."contractId" = v__contractId 
  AND "contractfeatures"."coreCustomerId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v__contractCustomersList)
      )
  );
DELETE "contractactionlimit" 
WHERE 
  "contractactionlimit"."contractId" = v__contractId 
  AND "contractactionlimit"."coreCustomerId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT(v__contractCustomersList)
      )
  );
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/
--  DDL for Procedure contract_features_create_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "contract_features_create_proc" (
  "_features" IN NVARCHAR2, "_contractId" IN NVARCHAR2, 
  "_customerId" IN NVARCHAR2, "_serviceTypeId" IN NVARCHAR2, 
  "_defaultActionsEnabled" IN NVARCHAR2, 
  "_legalEntityId" IN NVARCHAR2, "records" OUT SYS_REFCURSOR
) AS v_finished NUMBER(10, 0) := 0;
v_featureId NVARCHAR2(255) := '';
v_featuresList NVARCHAR2(2000) := '';
v_featureActionId NVARCHAR2(255) := '';
v_entryStatus NUMBER(10, 0) := 0;
v_limitId NVARCHAR2(255) := '';
v_tempLimitValue NVARCHAR2(255) := '';
v_features_List NVARCHAR2(2000) := '';
v_id NVARCHAR2(255) := '';
v_limitvalue NVARCHAR2(255) := '';
v_limitATServiceDefinition NVARCHAR2(255) := '';
v_servicedefinitionId NVARCHAR2(255) := '';
v_validServicedefinitionActions long := '';
v_validFIActions long := '';
CURSOR features IS 
SELECT 
  "feature"."id" 
FROM 
  "feature" 
WHERE 
  FIND_IN_SET("feature"."id", v_features_list) > 0 
  and "companyLegalUnit" = "_legalEntityId";
CURSOR limits IS 
SELECT 
  "actionlimit"."LimitType_id" 
FROM 
  "actionlimit" 
WHERE 
  "actionlimit"."Action_id" = v_featureActionId 
  and "companyLegalUnit" = "_legalEntityId";
CURSOR actions IS 
SELECT 
  "featureaction"."id" 
FROM 
  "featureaction" 
WHERE 
  FIND_IN_SET(
    "featureaction"."id", v_validServicedefinitionActions
  ) > 0 
  and "companyLegalUnit" = "_legalEntityId";
BEGIN 
SELECT 
  listagg(
    CAST(
      "feature"."id" AS NVARCHAR2(2000)
    ), 
    ','
  ) INTO v_features_list 
FROM 
  "feature" 
  JOIN "featureroletype" ON (
    "featureroletype"."Feature_id" = "feature"."id" 
    AND "featureroletype"."RoleType_id" = "_serviceTypeId"
  ) 
WHERE 
  (
    "feature"."Status_id" = 'SID_FEATURE_ACTIVE' 
    AND FIND_IN_SET("feature"."id", "_features") > 0
  ) 
  and "feature"."companyLegalUnit" = "_legalEntityId";
v_features_list := CASE WHEN (v_features_list IS NULL) THEN '' ELSE v_features_list END;
OPEN features;
FETCH features INTO v_featureId;
<< loop_1 >> WHILE (features % FOUND) LOOP BEGIN 
SELECT 
  SUBSTR(
    SYS_GUID(), 
    0, 
    50
  ) INTO v_id 
FROM 
  DUAL;
INSERT INTO "contractfeatures" (
  "contractfeatures"."id", "contractfeatures"."contractId", 
  "contractfeatures"."coreCustomerId", 
  "contractfeatures"."featureId", 
  "contractfeatures"."companyLegalUnit"
) 
VALUES 
  (
    v_id, "_contractId", "_customerId", 
    v_featureId, "_legalEntityId"
  );
v_featuresList := v_featureId || ',' || v_featuresList;
FETCH features INTO v_featureId;
GOTO loop_1;
END;
END LOOP;
CLOSE features;
SELECT 
  SUBSTR(
    v_featuresList, 
    1, 
    (
      CASE WHEN LENGTH(v_featuresList) > 0 THEN LENGTH(v_featuresList) -1 ELSE 0 END
    )
  ) INTO v_featuresList 
FROM 
  DUAL;
OPEN "records" FOR 
SELECT 
  v_featuresList "featuresList" 
FROM 
  DUAL;
v_finished := 0;
BEGIN 
SELECT 
  rtrim(
    xmlagg(
      XMLELEMENT(e, "featureaction"."id", ',').EXTRACT('//text()')
    ).GetClobVal(), 
    ','
  ) INTO v_validFIActions 
FROM 
  "featureaction" 
WHERE 
  FIND_IN_SET(
    "featureaction"."Feature_id", v_featuresList
  ) > 0 
  AND "featureaction"."status" = 'SID_ACTION_ACTIVE';
EXCEPTION WHEN NO_DATA_FOUND THEN v_validFIActions := null;
END;
BEGIN 
SELECT 
  "contract"."servicedefinitionId" INTO v_servicedefinitionId 
FROM 
  "contract" 
WHERE 
  "contract"."id" = "_contractId" 
  and "contract"."companyLegalUnit" = "_legalEntityId";
EXCEPTION WHEN NO_DATA_FOUND THEN v_servicedefinitionId := null;
END;
BEGIN 
SELECT 
  rtrim(
    xmlagg(
      XMLELEMENT(
        e, "servicedefinitionactionlimit"."actionId", 
        ','
      ).EXTRACT('//text()')
    ).GetClobVal(), 
    ','
  ) INTO v_validServicedefinitionActions 
FROM 
  "servicedefinitionactionlimit" 
WHERE 
  "servicedefinitionactionlimit"."serviceDefinitionId" = v_servicedefinitionId 
  AND FIND_IN_SET(
    "servicedefinitionactionlimit"."actionId", 
    v_validFIActions
  ) > 0;
EXCEPTION WHEN NO_DATA_FOUND THEN v_validServicedefinitionActions := null;
END;
IF "_defaultActionsEnabled" is not null 
AND "_defaultActionsEnabled" = 'true' THEN BEGIN OPEN actions;
FETCH actions INTO v_featureActionId;
<< loop_2 >> WHILE (actions % FOUND) LOOP BEGIN v_entryStatus := 0;
SELECT 
  "featureaction"."Feature_id" INTO v_featureId 
FROM 
  "featureaction" 
WHERE 
  "featureaction"."id" = v_featureActionId 
  and "featureaction"."companyLegalUnit" = "_legalEntityId";
OPEN limits;
FETCH limits INTO v_limitId;
<< loop_1 >> WHILE (limits % FOUND) LOOP BEGIN 
SELECT 
  "actionlimit"."value" INTO v_limitvalue 
FROM 
  "actionlimit" 
WHERE 
  "actionlimit"."Action_id" = v_featureActionId 
  AND "actionlimit"."LimitType_id" = v_limitId 
  and "actionlimit"."companyLegalUnit" = "_legalEntityId";
BEGIN 
SELECT 
  "servicedefinitionactionlimit"."value" INTO v_limitATServiceDefinition 
FROM 
  "servicedefinitionactionlimit" 
WHERE 
  "servicedefinitionactionlimit"."actionId" = v_featureActionId 
  AND "servicedefinitionactionlimit"."limitTypeId" = v_limitId 
  AND "servicedefinitionactionlimit"."serviceDefinitionId" = v_servicedefinitionId;
exception when NO_DATA_FOUND then -- exception handling logic goes here
v_limitATServiceDefinition := null;
END;
IF v_limitvalue > v_limitATServiceDefinition THEN BEGIN v_tempLimitValue := v_limitvalue;
END;
ELSE BEGIN v_tempLimitValue := v_limitATServiceDefinition;
END;
END IF;
IF v_tempLimitValue IS NOT NULL 
AND v_tempLimitValue <> ' ' THEN BEGIN v_limitvalue := v_tempLimitValue;
END;
END IF;
SELECT 
  SUBSTR(
    SYS_GUID(), 
    0, 
    50
  ) INTO v_id 
FROM 
  DUAL;
INSERT INTO "contractactionlimit" (
  "contractactionlimit"."id", "contractactionlimit"."contractId", 
  "contractactionlimit"."coreCustomerId", 
  "contractactionlimit"."featureId", 
  "contractactionlimit"."actionId", 
  "contractactionlimit"."limitTypeId", 
  "contractactionlimit"."value", 
  "contractactionlimit"."companyLegalUnit"
) 
VALUES 
  (
    v_id, "_contractId", "_customerId", 
    v_featureId, v_featureActionId, 
    v_limitId, v_limitvalue, "_legalEntityId"
  );
v_entryStatus := 1;
FETCH limits INTO v_limitId;
GOTO loop_1;
END;
END LOOP;
CLOSE limits;
v_finished := 0;
IF v_entryStatus = 0 THEN BEGIN 
SELECT 
  SUBSTR(
    SYS_GUID(), 
    0, 
    50
  ) INTO v_id 
FROM 
  DUAL;
INSERT INTO "contractactionlimit" (
  "contractactionlimit"."id", "contractactionlimit"."contractId", 
  "contractactionlimit"."coreCustomerId", 
  "contractactionlimit"."featureId", 
  "contractactionlimit"."actionId", 
  "contractactionlimit"."companyLegalUnit"
) 
VALUES 
  (
    v_id, "_contractId", "_customerId", 
    v_featureId, v_featureActionId, 
    "_legalEntityId"
  );
END;
END IF;
FETCH actions INTO v_featureActionId;
GOTO loop_2;
END;
END LOOP;
CLOSE actions;
END;
END IF;
END;
/
--  DDL for Procedure contract_search_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "contract_search_proc" (
  "_contractId" IN NVARCHAR2, "_contractName" IN NVARCHAR2, 
  "_coreCustomerId" IN NVARCHAR2, "_coreCustomerName" IN NVARCHAR2, 
  "_email" IN NVARCHAR2, "_phoneCountryCode" IN NVARCHAR2, 
  "_phoneNumber" IN NVARCHAR2, "_country" IN NVARCHAR2, 
  "_serviceDefinitionId" IN NVARCHAR2, 
  "_legalEntityId" IN NVARCHAR2, "records" OUT SYS_REFCURSOR
) AS v_select_statement NVARCHAR2(2000);
BEGIN v_select_statement := 'SELECT Distinct "contract"."id" as "contractId" , "contract"."name" as "contractName" , "contract"."servicedefinitionId" as "serviceDefinitionId" ,
                                                (select "servicedefinition"."name" from "servicedefinition" where "servicedefinition"."id" =
                                                "contract"."servicedefinitionId" and rownum=1) as "serviceDefinitionName",
                                                (select "contractcommunication"."value" from "contractcommunication" where
                                                "contractcommunication"."contractId" = "contract"."id" and
                                                "contractcommunication"."typeId" = ''COMM_TYPE_EMAIL'' and rownum = 1) as "email" ,
                                                (select "contractcorecustomers"."coreCustomerName" from "contractcorecustomers"
                                                where "contractcorecustomers"."contractId" = "contract"."id" and rownum=1) as "coreCustomerName"
                                                FROM "contract" WHERE "contract"."companyLegalUnit" = ' || '''' || "_legalEntityId" || '''';
IF ("_contractId" != ' ') THEN BEGIN v_select_statement := v_select_statement || ' and "contract"."id" = ' || '''' || "_contractId" || '''';
END;
END IF;
IF ("_contractName" != ' ') THEN BEGIN v_select_statement := v_select_statement || ' and "contract"."name" like ' || '''' || '%' || "_contractName" || '%' || '''';
END;
END IF;
IF ("_serviceDefinitionId" != ' ') THEN BEGIN v_select_statement := v_select_statement || ' and "contract"."servicedefinitionId" = ' || '''' || "_serviceDefinitionId" || '''';
END;
END IF;
IF ("_coreCustomerId" != ' ') THEN BEGIN v_select_statement := concat(
  v_select_statement, ' and "contract"."id" IN 
      (select DISTINCT "contractcorecustomers"."contractId"
       from "contractcorecustomers"  
      where  "contractcorecustomers"."coreCustomerId" = ' || '''' || "_coreCustomerId" || '''' || ')'
);
END;
END IF;
IF ("_coreCustomerName" != ' ') THEN BEGIN v_select_statement := concat(
  v_select_statement, ' and "contract"."id" IN 
      (select DISTINCT "contractcorecustomers"."contractId" 
      from "contractcorecustomers"  
      where "contractcorecustomers"."coreCustomerName" like %' || "_coreCustomerName" || '%)'
);
END;
END IF;
IF ("_email" != ' ') THEN BEGIN v_select_statement := concat(
  v_select_statement, ' and "contract"."id" IN (select DISTINCT "contractcommunication"."contractId" from "contractcommunication"  where "contractcommunication"."typeId" = ''COMM_TYPE_EMAIL'' and  "contractcommunication"."value" = ''' || "_email" || ''')'
);
END;
END IF;
IF (
  "_phoneCountryCode" != ' ' 
  AND "_phoneNumber" != ' '
) THEN BEGIN v_select_statement := concat(
  v_select_statement, ' and "contract"."id" IN (select DISTINCT "contractcommunication"."contractId" 
      from "contractcommunication"  
      where "contractcommunication"."typeId" = ''COMM_TYPE_PHONE'' and "contractcommunication"."value" =  ''' || "_phoneNumber" || '''  and "contractcommunication"."phoneCountryCode" =  ''' || "_phoneCountryCode" || ''' )'
);
END;
END IF;
IF ("_country" != ' ') THEN BEGIN v_select_statement := concat(
  v_select_statement, ' and "contract"."id" IN (select DISTINCT "contractaddress"."contractId" from "contractaddress"  where "contractaddress"."addressId" IN (select DISTINCT "address"."id" from "address" where "address"."country" = ''' || "_country" || ''' ))'
);
END;
END IF;
OPEN "records" FOR v_select_statement;
END;
/
--  DDL for Procedure contract_users_details_get_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "contract_users_details_get_proc" (
  "_contractId" IN NVARCHAR2, "_backendType" IN NVARCHAR2, 
  "records" out SYS_REFCURSOR
) AS v_customers NVARCHAR2(2000) := '';
v_stmt NVARCHAR2(2000) := '';
BEGIN 
SELECT 
  listagg(
    CAST(
      "contractcustomers"."customerId" AS NVARCHAR2(2000)
    ), 
    ','
  ) INTO v_customers 
FROM 
  "contractcustomers" 
WHERE 
  "contractcustomers"."contractId" = "_contractId";
v_stmt := 'SELECT "customer"."id" "customerId"  ,
             "customer"."FirstName" "firstName"  ,
             "customer"."MiddleName" "middleName"  ,
             "customer"."LastName" "lastName"  ,
             "customer"."UserName" "userName"  ,
             "customer"."Status_id" "statusId"  ,
             "customer"."DateOfBirth" "dateOfBirth"  ,
             "customer"."Ssn" "Ssn"  ,
             "backendidentifier"."BackendId" "primaryCoreCustomerId"  ,
             "customercommunication"."Value" "Email"  
        FROM "customer"
               LEFT JOIN "customercommunication"    ON ( "customer"."id" = "customercommunication"."Customer_id"
               AND  "customercommunication"."Type_id" = ''COMM_TYPE_EMAIL'' AND "customercommunication"."isPrimary" = 1  AND "customercommunication"."Customer_id" IN (' || (v_customers) || '))
               LEFT JOIN "backendidentifier"    ON ( "backendidentifier"."Customer_id" = "customer"."id"
               AND "backendidentifier"."BackendType" = ''' || "_backendType" || ''' 
         AND "backendidentifier"."companyLegalUnit" = "customer"."companyLegalUnit" AND "backendidentifier"."Customer_id" IN (' || (v_customers)|| '))
       WHERE  "customer"."id" IN (' || (v_customers) || ')';
OPEN "records" FOR v_stmt;
END;
/
--  DDL for Procedure contractactionlimit_delete_proc


create or replace NONEDITIONABLE PROCEDURE "contractactionlimit_delete_proc" ("_coreCustomerId" IN VARCHAR2) AS BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
DELETE "contractactionlimit" 
WHERE 
  "contractactionlimit"."coreCustomerId" = "_coreCustomerId";
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/
--  DDL for Procedure corecustomeraccounts_details_get_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "corecustomeraccounts_details_get_proc" (
  "_coreCustomerIdList" IN VARCHAR2, 
  "_customerId" IN VARCHAR2, "records" OUT SYS_REFCURSOR
) AS v_selectstatement VARCHAR2(4000);
v_corecustomersList CLOB;
BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
SELECT 
  LISTAGG(
    CAST(
      "contractcustomers"."coreCustomerId" AS VARCHAR2(4000)
    )
  ) INTO v_corecustomersList 
FROM 
  "contractcustomers" 
WHERE 
  "contractcustomers"."customerId" = "_customerId" 
  AND "contractcustomers"."coreCustomerId" IN (
    SELECT 
      COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_coreCustomerIdList")
      )
  );
v_selectstatement := (
  '(SELECT
                                        "contractcorecustomers"."coreCustomerId" AS "coreCustomerId" ,
                                          "contractcorecustomers"."coreCustomerName" AS "coreCustomerName" ,
                                          "customeraccounts"."email" AS "email" ,
                                          "customeraccounts"."Customer_id" AS "customerId" ,
                                          "customeraccounts"."FavouriteStatus" AS "favouriteStatus" ,
                                      "customeraccounts"."NickName" AS "nickName" ,
                                      "customeraccounts"."accountStatus" AS "accountStatus" ,
                                          DECODE(("customeraccounts"."EStatementmentEnable") ,''1'' ,''true'' , ''false'') AS "eStatementEnable",
                                      DECODE (("customeraccounts"."isSweepCreated") , ''1'' ,''true'' , ''false'') AS "isSweepCreated",
                                          DECODE (("contractcorecustomers"."isBusiness") , ''1'' ,''true'' , ''false'') AS "isBusinessAccount",
                                          "contractaccounts"."accountId" AS "accountId"
                                          from "contractcorecustomers"
                                      JOIN "contractaccounts" ON ("contractcorecustomers"."coreCustomerId" = "contractaccounts"."coreCustomerId")
                                          JOIN "customeraccounts" ON ("customeraccounts"."Account_id" = "contractaccounts"."accountId")
                                          where "contractcorecustomers"."coreCustomerId" IN ( SELECT column_value from TABLE(UTILS.STRING_SPLIT(' || '''' || v_corecustomersList || '''' || '))) 
                                        AND "customeraccounts"."Customer_id" = ' || '''' || "_customerId" || '''' || ')'
);
--                                            
--   EXECUTE IMMEDIATE v_selectstatement;
OPEN "records" FOR v_selectstatement;
DBMS_OUTPUT.PUT_LINE(v_selectstatement);
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/
--  DDL for Procedure customer_accounts_delete_proc


create or replace NONEDITIONABLE PROCEDURE "customer_accounts_delete_proc" ("_customerId" IN VARCHAR2) AS BEGIN DELETE "customeraccounts" 
WHERE 
  "customeraccounts"."Customer_id" = "_customerId";
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/
--  DDL for Procedure customer_action_limits_delete


create or replace NONEDITIONABLE PROCEDURE "customer_action_limits_delete" ("_customerId" IN VARCHAR2) AS BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
DELETE "customeractionlimits" 
WHERE 
  "customeractionlimits"."Customer_id" = "_customerId";
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/
--  DDL for Procedure customer_action_limits_delete_proc


create or replace NONEDITIONABLE PROCEDURE "customer_action_limits_delete_proc" ("_customerId" IN VARCHAR2) AS BEGIN DELETE "customeraction" 
WHERE 
  "customeraction"."Customer_id" = "_customerId";
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;

/
--  DDL for Procedure customer_action_save_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "customer_action_save_proc" ("_queryInput" IN VARCHAR2) AS v_queryInput VARCHAR2(4000) := "_queryInput";
v_index NUMBER(10, 0);
v_numOfRecords NUMBER(10, 0);
v_recordsData VARCHAR2(4000);
v_query VARCHAR2(4000);
BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
v_index := 0;
v_queryInput := REPLACE(
  v_queryInput, 
  '\', ' ') ;
   v_queryInput := REPLACE(v_queryInput, ' "', '''') ;
   v_numOfRecords := CASE 
                          WHEN v_queryInput IS NULL THEN 0
   ELSE LENGTH(v_queryInput) - LENGTH(REPLACE(v_queryInput, '|', '')) || 1
      END ;
   WHILE ( 1 = 1 ) 
   LOOP 

      BEGIN
         v_index := v_index + 1 ;
         IF ( v_index = v_numOfRecords + 1 ) THEN
          EXIT;
         ELSE

         BEGIN
            v_recordsData := 'N''' || CAST(SYS_GUID() AS VARCHAR2) || ''',' || UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_queryInput, '|', v_index), '|', -1) ;
            v_query := ('INSERT INTO " customeraction "("id","RoleType_id","Customer_id","contractId","coreCustomerId","featureId","Action_id","Account_id","isAllowed","limitGroupId","LimitType_id","value") VALUES (') || (v_recordsData) || (')') ;
            EXECUTE IMMEDIATE v_query;

         END;
         END IF;

      END;
   END LOOP;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure customer_actions_delete



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "customer_actions_delete" 
(
  v__customerId IN VARCHAR2
)
AS

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   DELETE "customeraction"

    WHERE  "customeraction"."Customer_id" = v__customerId;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure customer_basic_info_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "customer_basic_info_proc"
    ("_customerId" IN nvarchar2,
    "_legalEntityId" IN nvarchar2,"records" out SYS_REFCURSOR)
AS
    BEGIN

 

        declare
        v_accountLockoutThreshold nvarchar2(4000);
        v_accountLockoutTime nvarchar2(4000);
        

 

        BEGIN
         SELECT "accountLockoutThreshold" into v_accountLockoutThreshold from "passwordlockoutsettings";
         SELECT "accountLockoutTime" into v_accountLockoutTime from "passwordlockoutsettings";
        OPEN  "records" FOR
        SELECT 
            "customer"."UserName"  "Username",
            "customer"."FirstName"  "FirstName",
            "customer"."MiddleName"  "MiddleName",
            "customer"."LastName"  "LastName",
            (NVL("customer"."FirstName", u'')) || (u' ') || (NVL("customer"."MiddleName", u'')) || (u' ') || (NVL("customer"."LastName", u''))  "Name",
            "customer"."Salutation"  "Salutation",
            "customer"."id"  "Customer_id",
            "customer"."Ssn"  "SSN",
            "customer"."createdts"  "CustomerSince",
            "customer"."Gender"  "Gender",
            "customer"."DateOfBirth"  "DateOfBirth",
            "customer"."isEnrolledFromSpotlight"  "isEnrolledFromSpotlight",
            CASE
                WHEN ("customer"."Status_id" = 'SID_CUS_SUSPENDED') THEN "customer"."Status_id"
            ELSE CASE
                WHEN ("customer"."lockCount" + 1 >= v_accountLockoutThreshold) THEN u'SID_CUS_LOCKED'
            ELSE "customer"."Status_id"
            END
            END AS "CustomerStatus_id",
            "customerstatus"."Description"  "CustomerStatus_name",
            "customer"."MaritalStatus_id"  "MaritalStatus_id",
            "maritalstatus"."Description"  "MaritalStatus_name",
            "customer"."SpouseName"  "SpouseName",
            "customer"."DrivingLicenseNumber"  "DrivingLicenseNumber",
            "customer"."lockedOn"  "lockedOn",
            "customer"."lockCount"  "lockCount",
            "customer"."EmployementStatus_id"  "EmployementStatus_id","employementstatus"."Description"  "EmployementStatus_name",
            ( SELECT LISTAGG(CAST("Status_id" as nvarchar2(2000)),',')
                FROM "customerflagstatus"
                WHERE ("customerflagstatus"."Customer_id" = "customer"."id") )  "CustomerFlag_ids",
            ( SELECT LISTAGG(CAST("Description" as nvarchar2(2000)),',')
                FROM "status"
                WHERE "status"."id" IN
            (
                SELECT "customerflagstatus"."Status_id"
                    FROM "customerflagstatus"
                WHERE ("customerflagstatus"."Customer_id" = "customer"."id")
            ))  "CustomerFlag",
            "customer"."IsEnrolledForOlb"  "IsEnrolledForOlb",
            "customer"."isEnrolled"  "isEnrolled",
            "customer"."IsStaffMember"  "IsStaffMember",
            "customer"."Location_id"  "Branch_id",
            "location"."Name"  "Branch_name",
            "location"."Code"  "Branch_code",
            "customer"."IsOlbAllowed"  "IsOlbAllowed",
            "customer"."IsAssistConsented"  "IsAssistConsented",
            "customer"."isEagreementSigned"  "isEagreementSigned",
            "customer"."companyLegalUnit"  "companyLegalUnit",
            "customer"."isCombinedUser"  "isCombinedUser",
            NVL("customer"."combinedUserId", u'')  "combinedUserId",
            DECODE(("customer"."isCombinedUser" ),1,u'TYPE_ID_RETAIL,TYPE_ID_BUSINESS',"customer"."CustomerType_id")  "CustomerType_id",
            DECODE(("customer"."isCombinedUser"),1,u'Retail Banking,Business Banking',"customertype"."Name")  "CustomerType_Name",
            DECODE(("customer"."isCombinedUser" ),1,u'Retail and Business Banking User',"customertype"."Description")  "CustomerType_Description",
            (SELECT "membergroup"."id" FROM "membergroup" WHERE "membergroup"."id" in
            (SELECT "customergroup"."Group_id" from "customergroup" where "customergroup"."Customer_id"= "_customerId") AND "membergroup"."Type_id" = 'TYPE_ID_BUSINESS' )  "Customer_RoleId",
            (SELECT "membergroup"."Name" FROM "membergroup" WHERE "id" in (SELECT "Group_id" from "customergroup" where "customergroup"."Customer_id"= "_customerId") AND "membergroup"."Type_id" = 'TYPE_ID_BUSINESS' )  "Customer_Role",
            (SELECT "membergroup"."isEAgreementActive" FROM "membergroup" WHERE "id" in (SELECT "Group_id" from "customergroup" where "customergroup"."Customer_id"= "_customerId") AND "membergroup"."Type_id" = 'TYPE_ID_BUSINESS' )  "isEAgreementRequired",
            "customer"."Organization_Id"  "organisation_id",
            "organisation"."BusinessType_id"  "BusinessType_id",
            "businesstype"."name"  "BusinessType",
            "organisation"."Name"  "organisation_name",
            (SELECT "customercommunication"."Value" FROM
            "customercommunication"
            WHERE
            "customercommunication"."Type_id" = 'COMM_TYPE_PHONE' AND "customercommunication"."isPrimary" = 1
            AND "customercommunication"."Customer_id" = "customer"."id")  "PrimaryPhoneNumber",
            (SELECT "customercommunication"."Value" FROM
            "customercommunication"
            WHERE
            "customercommunication"."Type_id" = 'COMM_TYPE_EMAIL' AND "customercommunication"."isPrimary" = 1
            AND "customercommunication"."Customer_id" = "customer"."id")  "PrimaryEmailAddress",
            (SELECT "customercommunication"."Value" FROM
            "customercommunication"
            WHERE
            "customercommunication"."Type_id" = 'COMM_TYPE_PHONE' AND "customercommunication"."isTypeBusiness" = '1'
            AND "customercommunication"."Customer_id" = "customer"."id")  "BusinessPrimaryPhoneNumber",
            (SELECT "customercommunication"."Value" FROM
            "customercommunication"
            WHERE
            "customercommunication"."Type_id" = 'COMM_TYPE_EMAIL' AND "customercommunication"."isTypeBusiness" = '1'
            AND "customercommunication"."Customer_id" = "customer"."id")  "BusinessPrimaryEmailAddress",
            "customer"."DocumentsSubmitted"  "DocumentsSubmitted",
            "customer"."ApplicantChannel"  "ApplicantChannel",
            "customer"."Product"  "Product",
            "customer"."Reason"  "Reason",
            v_accountLockoutTime  "accountLockoutTime"
        FROM ((((((("customer"
        LEFT JOIN "location"
        ON (("customer"."Location_id" = "location"."id")))
        LEFT JOIN "organisation"
        ON (("customer"."Organization_Id" = "organisation"."id")))
        LEFT JOIN "businesstype" ON (("businesstype"."id" = "organisation"."BusinessType_id")))
        INNER JOIN "customertype"
        ON (("customer"."CustomerType_id" = "customertype"."id")))
        LEFT JOIN "status"  "customerstatus"
        ON (("customer"."Status_id" = "customerstatus"."id")))
        LEFT JOIN "status"  "maritalstatus"
        ON (("customer"."MaritalStatus_id" = "maritalstatus"."id")))
        LEFT JOIN "status"  "employementstatus"
        ON (("customer"."EmployementStatus_id" = "employementstatus"."id")))
        WHERE "customer"."id" = "_customerId"
        FETCH FIRST 1 ROWS ONLY;
        
END;
END;
/
--  DDL for Procedure customer_contract_delete_proc

create or replace NONEDITIONABLE PROCEDURE "customer_contract_delete_proc" 
(
  "customerId" IN VARCHAR2,
  "contractId" IN VARCHAR2,
  "coreCustomerId" IN VARCHAR2,
  "legalEntityId" IN VARCHAR2
)
AS
   v_contract_statement VARCHAR2(4000);
   v_suspended_statement VARCHAR2(4000);
   v_accounts_statement VARCHAR2(4000);
   v_group_statement VARCHAR2(4000);
   v_action_statement VARCHAR2(4000);
   v_excluded_action_statement VARCHAR2(4000);
   v_excluded_accounts_statement VARCHAR2(4000);
   v_limitgroup_statement VARCHAR2(4000);
   v_where_clause VARCHAR2(4000);
   v_where_clause1 VARCHAR2(4000);

BEGIN
   v_contract_statement := 'DELETE FROM "contractcustomers" where' ;
   v_suspended_statement := 'DELETE FROM "suspendedcustomers" where' ;
   v_accounts_statement := 'DELETE FROM "customeraccounts" where' ;
   v_excluded_accounts_statement := 'DELETE FROM "excludedcustomeraccounts" where' ;
   v_group_statement := 'DELETE FROM "customergroup" where' ;
   v_action_statement := 'DELETE FROM "customeraction" where' ;
   v_excluded_action_statement := 'DELETE FROM "excludedcustomeraction" where' ;
   v_limitgroup_statement := 'DELETE FROM "customerlimitgrouplimits" where' ;
   v_where_clause := ' ' ;
   v_where_clause1 := ' ' ;
   v_where_clause := v_where_clause || ('"companyLegalUnit"=') || ((''''||(("legalEntityId")|| ''''))) ;
      v_where_clause1 := v_where_clause1 || ('"companyLegalUnit"=') || ((''''||(("legalEntityId")|| ''''))) ;
   
   IF ( "customerId" is not null ) THEN

   BEGIN
    v_where_clause := v_where_clause || (' AND ') ;
         v_where_clause1 := v_where_clause1 || (' AND ') ;
      v_where_clause := v_where_clause || ('"customerId"=') || ((''''||(("customerId")|| ''''))) ;
      v_where_clause1 := v_where_clause1 || ('"Customer_id"=') || ((''''||(("customerId")|| ''''))) ;

   END;
   END IF;
   IF ( "contractId" is not null ) THEN

   BEGIN
      IF ( v_where_clause is not null) THEN

      BEGIN
         v_where_clause := v_where_clause || (' AND ') ;
         v_where_clause1 := v_where_clause1 || (' AND ') ;

      END;
      END IF;
      v_where_clause := v_where_clause || ('"contractId"=') || ((''''||(("contractId")|| ''''))) ;
      v_where_clause1 := v_where_clause1 || ('"contractId"=') || ((''''||(("contractId")|| ''''))) ;

   END;
   END IF;
   IF ( "coreCustomerId" is not null) THEN

   BEGIN
      IF ( v_where_clause is not null ) THEN

      BEGIN
         v_where_clause := v_where_clause || (' AND ') ;
         v_where_clause1 := v_where_clause1 || (' AND ') ;

      END;
      END IF;
      v_where_clause := v_where_clause || ('"coreCustomerId"=') || ((''''||(("coreCustomerId")|| ''''))) ;
      v_where_clause1 := v_where_clause1 || ('"coreCustomerId"=') || ((''''||(("coreCustomerId")|| ''''))) ;

   END;
   END IF;
   IF ( v_where_clause is not null ) THEN

   BEGIN
      v_contract_statement := v_contract_statement || v_where_clause ;
      v_suspended_statement := v_suspended_statement || v_where_clause ;
      v_accounts_statement := v_accounts_statement || v_where_clause1 ;
      v_excluded_accounts_statement := v_excluded_accounts_statement || v_where_clause1 ;
      v_group_statement := v_group_statement || v_where_clause1 ;
      v_action_statement := v_action_statement || v_where_clause1 ;
      v_excluded_action_statement := v_excluded_action_statement || v_where_clause1 ;
      v_limitgroup_statement := v_limitgroup_statement || v_where_clause1 ;
      EXECUTE IMMEDIATE v_contract_statement;
      EXECUTE IMMEDIATE v_suspended_statement;
      EXECUTE IMMEDIATE v_accounts_statement;
      EXECUTE IMMEDIATE v_excluded_accounts_statement;
      EXECUTE IMMEDIATE v_group_statement;
      EXECUTE IMMEDIATE v_action_statement;
      EXECUTE IMMEDIATE v_excluded_action_statement;
      EXECUTE IMMEDIATE v_limitgroup_statement;

   END;
   END IF;

END;
/
--  DDL for Procedure customer_eagreement_get_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "customer_eagreement_get_proc" 
(
  v__customerId IN VARCHAR2,"membergroup" OUT SYS_REFCURSOR
)
AS
   

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   OPEN  "membergroup" FOR
      SELECT "membergroup"."isEAgreementActive" isEAgreementActive  
        FROM "membergroup" 
               LEFT JOIN "customergroup"    ON ( "customergroup"."Group_id" = "membergroup"."id" )
       WHERE  "customergroup"."Customer_id" = v__customerId
                AND "membergroup"."Type_id" = 'TYPE_ID_BUSINESS' ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM); 
END;

/
--  DDL for Procedure customer_group_delete_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "customer_group_delete_proc" 
(
  v__customerId IN VARCHAR2
)
AS

BEGIN

   DELETE "customergroup"

    WHERE  "customergroup"."Customer_id" = v__customerId;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/

--  DDL for Procedure customer_group_org_actionlimits_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "customer_group_org_actionlimits_proc" 
(
  v__customerId IN VARCHAR2,
  v__organisationId IN VARCHAR2,
  v__isOnlyPremissions IN VARCHAR2,
  "customeraction" OUT SYS_REFCURSOR
)
AS
   v_organization_actions CLOB;
   v_business_customer_enabled_actions CLOB;
   v_groups CLOB;
   v_list VARCHAR2(4000);
   v_select_statement VARCHAR2(4000);
   v_next VARCHAR2(4000);
   v_nextlen NUMBER(19,0);
   v_value VARCHAR2(4000);
   v_roleType VARCHAR2(4000);

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   SELECT LISTAGG(CAST("organisationactionlimit"."Action_id"  AS VARCHAR2(255))) 

     INTO v_organization_actions
     FROM "organisationactionlimit" 
    WHERE  "organisationactionlimit"."Organisation_id" = v__organisationId
             AND "organisationactionlimit"."Action_id" NOT IN ( SELECT "featureaction"."id" 
                                                            FROM "featureaction" 
                                                                   LEFT JOIN "feature"    ON ( "featureaction"."Feature_id" = "feature"."id" )
                                                                   LEFT JOIN "organisationfeatures"    ON ( "organisationfeatures"."featureId" = "feature"."id" )
                                                             WHERE  "organisationfeatures"."organisationId" = v__organisationId
                                                                      AND "organisationfeatures"."featureStatus" = 'SID_FEATURE_SUSPENDED'
                                                                      OR "feature"."Status_id" <> 'SID_FEATURE_ACTIVE' )
   ;
   v_organization_actions := ('''') || (CASE 
                                             WHEN ( v_organization_actions IS NULL ) THEN ''
   ELSE v_organization_actions
      END) || ('''') ;
   SELECT LISTAGG(CAST("Action_id"  AS VARCHAR2(255))) 

     INTO v_business_customer_enabled_actions
     FROM "customeraction" 
    WHERE  "isAllowed" = '1'
             AND "Customer_id" = v__customerId
             AND "Action_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v_organization_actions)));
   IF ( v_business_customer_enabled_actions IS NOT NULL ) THEN
    v_business_customer_enabled_actions := '''' || (CASE 
                                                         WHEN ( v_business_customer_enabled_actions IS NULL ) THEN ''
   ELSE v_business_customer_enabled_actions
      END) || '''' ;
   END IF;
   SELECT LISTAGG(CAST("customergroup"."Group_id"  AS VARCHAR2(255))) 

     INTO v_groups
     FROM "customergroup" 
    WHERE  "Customer_id" = v__customerId;
   IF ( v_groups != '' ) THEN
    v_groups := (v_groups || ',') ;
   END IF;
   SELECT v_groups 

     INTO v_list
     FROM DUAL ;
   v_select_statement := '' ;
   WHILE ( ( LENGTH(TRIM(v_list)) != 0
     OR v_list IS NOT NULL ) ) 
   LOOP 

      BEGIN
         v_next := UTILS.SUBSTRING_INDEX(v_list, ',', 1) ;
         v_nextlen := LENGTH(v_next) ;
         v_value := TRIM(v_next) ;
         SELECT "membergroup"."Type_id" 

           INTO v_roleType
           FROM "membergroup" 
          WHERE  "membergroup"."id" = v_value;
         IF ( v_roleType = ('TYPE_ID_BUSINESS') ) THEN

         BEGIN
            IF ( v__isOnlyPremissions = 'true' ) THEN
             OPEN  "customeraction" FOR
               SELECT DISTINCT "customeraction"."Action_id" actionId  
                 FROM "customeraction" 
                WHERE  "isAllowed" = '1'
                         AND "Customer_id" = v__customerId
                         AND "Action_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v_organization_actions)))
                         AND ( "customeraction"."Action_id" IN ( SELECT DISTINCT "groupactionlimit"."Action_id" 
                                                             FROM "groupactionlimit" 
                                                              WHERE  "groupactionlimit"."Group_id" = v_value )
                        ) ;
               --DBMS_SQL.RETURN_RESULT(v_cursor);
            ELSE

            BEGIN
               v_select_statement := ('SELECT DISTINCT
                              "feature"."id" as featureId,
                              "feature"."name" AS featureName,
                              "feature"."description" AS featureDescription,
                              "featureaction"."id" as actionId,
                              "featureaction"."Type_id" as actionType,
                              "featureaction"."description" AS actionDescription,
                              "featureaction"."name" AS actionName,
                              CASE WHEN ("featureaction"."id"  IN ( SELECT column_value from TABLE(UTILS.STRING_SPLIT(CAST(' || v_business_customer_enabled_actions || ' as varchar2(4000))))) ) THEN ''true'' ELSE ''false'' END AS isActionAllowed,
                              CASE WHEN ("featureaction"."isAccountLevel"= 1) THEN ''true'' ELSE ''false'' END AS isAccountLevel,
                              CASE WHEN ("customeraction"."isAllowed" = 1) THEN ''true'' ELSE ''false'' END as isAllowedForCustomer,
                              "customeraction"."Account_id" as accountId,
                              "customeraction"."LimitType_id" as limitTypeId,
                              "customeraction"."value" as value
                             FROM
                            ("customeraction" 
                            LEFT JOIN "customergroup" ON ("customergroup"."Customer_id" = "customeraction"."Customer_id")
                            LEFT JOIN "membergroup" ON ("membergroup"."id" = "customergroup"."Group_id")
                            LEFT JOIN "featureaction" ON ("featureaction"."id" = "customeraction"."Action_id")
                            LEFT JOIN "feature" ON ("feature"."id" = "featureaction"."Feature_id"))
                          where 
                            "customeraction"."Customer_id" = ' || ''''||(v__customerId|| '''') || '
                            and ("customeraction"."Action_id" in (
                            select DISTINCT "groupactionlimit"."Action_id" from 
                            "groupactionlimit" where 
                            "groupactionlimit"."Group_id" = ' || '''' || (v_value) || '''' || '))
                            and ("featureaction"."id" IN ( SELECT column_value from TABLE(UTILS.STRING_SPLIT(CAST(' || v_business_customer_enabled_actions || ' as varchar2(4000))))) )') ;
               EXECUTE IMMEDIATE v_select_statement;
               OPEN "customeraction" FOR v_select_statement;
            END;
            END IF;

         END;
         END IF;
         v_list := REPLACE(v_list, SUBSTR(v_list, 1, v_nextlen+1), ' ');


      END;
   END LOOP; 

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure customer_legalentities_get_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "customer_legalentities_get_proc" 
(
  "_customerid" IN VARCHAR2, "records" OUT SYS_REFCURSOR
)
AS
   

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   OPEN  "records" FOR
      SELECT "cust"."id" "customerid"  ,
             "cust"."homeLegalEntity" "homeLegalEntity"  ,
             "custleg"."legalEntityId" "legalEntityId"  ,
             "custleg"."Status_id" "statusId"  
        FROM "customer" "cust"
               LEFT JOIN "customerlegalentity" "custleg"   ON ( "cust"."id" = "custleg"."Customer_id" )
       WHERE  "cust"."id" = "_customerid" ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure customer_search_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "customer_search_proc"
  (
    "_searchType" IN VARCHAR2,
    "_id" IN VARCHAR2,
    "_name" IN VARCHAR2,
    "_SSN" IN VARCHAR2,
    "_username" IN VARCHAR2,
    "_phone" IN VARCHAR2,
    "_email" IN VARCHAR2,
    "_dateOfBirth" IN VARCHAR2,
    "_IsStaffMember" IN VARCHAR2,
    "_cardorAccountnumber" IN VARCHAR2,
    "_TIN" IN VARCHAR2,
    "_group" IN VARCHAR2,
    "_IDType" IN VARCHAR2,
    "_IDValue" IN VARCHAR2,
    "_companyId" IN VARCHAR2,
    "_requestID" IN VARCHAR2,
    "_branchIDS" IN VARCHAR2,
    "_productIDS" IN VARCHAR2,
    "_cityIDS" IN VARCHAR2,
    "_entitlementIDS" IN VARCHAR2,
    "_groupIDS" IN VARCHAR2,
    "_customerStatus" IN VARCHAR2,
    "_before" IN VARCHAR2,
    "_after" IN VARCHAR2,
    "_sortVariable" IN VARCHAR2,
    "_sortDirection" IN VARCHAR2,
    "_pageOffset" IN NUMBER,
    "_pageSize" IN NUMBER,
      "_legalEntityId" IN VARCHAR2,
    "records" OUT SYS_REFCURSOR,
      "records1" out sys_refcursor
  
  )
  AS

  BEGIN

     DECLARE
      v__maxLockCount VARCHAR2(50);
      v_search_select_statement NVARCHAR2(2000);
      v_search_count_statement NVARCHAR2(2000);
      v_queryStatement long;
      v_queryStatement2 long;

     BEGIN

      IF "_searchType" LIKE 'GROUP_SEARCH%' THEN
       DECLARE
       v_whereclause NVARCHAR2(2000);

      BEGIN

       v_search_select_statement := 'SELECT "customer"."id", "customer"."FirstName", "customer"."MiddleName", 
                                    "customer"."LastName",(NVL("customer"."FirstName",'''') || '' '' ||
                    NVL("customer"."MiddleName",'''') || '' '' || NVL("customer"."LastName",'''')) as 
                    "name","customer"."UserName" as "Username", "customer"."isCombinedUser" as "isCombinedUser",
                     NVL("customer"."combinedUserId", '''') as "combinedUserId"
                     , "customer"."Salutation", "customer"."Gender", "customer"."IsStaffMember", 
                     DECODE("customer"."isCombinedUser" , ''1'',''TYPE_id_RETAIL,TYPE_id_BUSINESS'', "customer"."CustomerType_id") AS "CustomerTypeId", 
                     "customer"."modifiedby", "customer"."lastmodifiedts", "customer"."Status_id",
                     "PrimaryEmail"."Value" AS "PrimaryEmail",LISTAGG(CAST("customergroup"."Group_id" as nvarchar2(2000)),'','') 
                                         as "assigned_group_ids","address"."City_id", "city"."Name" as "City_name", "address"."addressLine1" As "addressLine1", "address"."addressLine2" As "addressLine2", "city"."Name" As "city", 
                                         "address"."zipCode" As "zipCode", "country"."Name" As "county", "customer"."isEnrolled" as "isEnrolled","customer"."Location_id" AS 
                                         "branch_id","location"."Name" AS "branch_name", ''true'' as "isProfileExist"' ;

      v_search_count_statement := 'SELECT count(distinct "customer"."id") as "SearchMatchs" ' ;
      v_queryStatement := 'FROM "customer" JOIN (SELECT "customer"."id" FROM "customer" ' || (CASE 
                                                              WHEN ( "_groupIDS" <> ' '
                                                              OR "_entitlementIDS" <> ' ' ) THEN ' LEFT JOIN "customergroup" ON ("customergroup"."Customer_id"="customer"."id")'
       ELSE ''
        END) || (CASE 
                WHEN ( "_cityIDS" <> ' ' ) THEN ' LEFT JOIN "customeraddress" ON ("customer"."id"="customeraddress"."Customer_id" AND "customeraddress"."isPrimary"=1 and "customeraddress"."Type_id"=''ADR_TYPE_HOME'') LEFT JOIN "address" ON ("customeraddress"."Address_id" = "address"."id") 
                        LEFT JOIN "city" ON ("address"."City_id" = "city"."id") '
       ELSE ''
        END) || (CASE 
                WHEN ( "_entitlementIDS" <> ' ' ) THEN ' LEFT JOIN "customerentitlement" ON ("customerentitlement"."Customer_id"="customer"."id") '
       ELSE ''
        END) || (CASE 
                WHEN ( "_productIDS" <> ' ' ) THEN ' LEFT JOIN "customerproduct" ON ("customerproduct"."Customer_id"="customer"."id") '
       ELSE ''
        END) ;
       v_whereclause := ' WHERE 1=1 ' ;
       IF "_username" <> ' ' THEN

       BEGIN
        v_whereclause := (v_whereclause) || (' AND ("customer"."FirstName" like (''') || ("_username") || '%'')' ;
        v_whereclause := (v_whereclause) || (' OR "customer"."UserName" like (''') || ("_username") || '%'')' ;
        v_whereclause := (v_whereclause) || (' OR "customer"."id" like (''') || ("_username") || '%''))' ;

       END;
       END IF;
       IF "_IsStaffMember" <> ' ' THEN
        IF "_IsStaffMember" = 'true' THEN
        v_whereclause := (v_whereclause) || (' AND "customer"."IsStaffMember" = ''1''') ;
       ELSE
        v_whereclause := (v_whereclause) || (' AND "customer"."IsStaffMember" = ''0''') ;
       END IF;
       END IF;
       IF "_entitlementIDS" <> ' ' THEN
        v_whereclause := (v_whereclause) || (' AND ("customerentitlement"."Service_id" in (') || (func_escape_input_for_in_operator("_entitlementIDS")) || (') OR "customergroup"."Group_id" in ( select "Group_id" from "groupentitlement" where "Service_id" in (') || (func_escape_input_for_in_operator("_entitlementIDS")) || (' )))') ;
       END IF;
       IF "_groupIDS" <> ' ' THEN
        v_whereclause := (v_whereclause) || (' AND "customergroup"."Group_id" in (') || (func_escape_input_for_in_operator("_groupIDS")) || (') ') ;
       END IF;
       IF "_productIDS" <> ' ' THEN
        v_whereclause := (v_whereclause) || (' AND "customerproduct"."Product_id" in (') || (func_escape_input_for_in_operator("_productIDS")) || (')') ;
       END IF;
       IF "_branchIDS" <> ' ' THEN
        v_whereclause := (v_whereclause) || (' AND "customer"."Location_id" in (') || (func_escape_input_for_in_operator("_branchIDS")) || (')') ;
       END IF;
       IF "_customerStatus" <> ' ' THEN
        v_whereclause := (v_whereclause) || (' AND "customer"."Status_id" = ') || '''' || "_customerStatus" || '''' ;
       END IF;
       IF "_cityIDS" <> ' ' THEN
        v_whereclause := (v_whereclause) || (' AND "address"."City_id" in (') || (func_escape_input_for_in_operator("_cityIDS")) || (')') ;
       END IF;
       IF "_before" <> ' '
         AND "_after" <> ' ' THEN
                v_whereclause := (v_whereclause) || ' AND  "customer"."createdts" >= ' ||'''' ||  to_date("_before",'yyyy-mm-dd') || ''''
                || ' and "customer"."createdts" <= ' || '''' || to_date("_after",'yyyy-mm-dd') || '''' ;

            ELSE
        IF "_before" <> ' ' THEN
         --v_whereclause := (v_whereclause) || (N' AND CONVERT(DATE,"customer"."createdts",105) >= CONVERT(DATE, ') || '''' || "_before" || '''' || ',105)' ;
        v_whereclause := (v_whereclause) || ' AND "customer"."createdts" >= ' ||'''' ||  to_date("_before",'yyyy-mm-dd') || ''''; 
                ELSE

        BEGIN
           IF "_after" <> ' ' THEN
          --v_whereclause := (v_whereclause) || (N' AND CONVERT(DATE,"customer"."createdts",105) >= CONVERT(DATE, ') || '''' || "_after" || '''' || ',105)' ;
          v_whereclause := (v_whereclause) || ' AND "customer"."createdts" >= ' ||'''' ||  to_date("_after",'yyyy-mm-dd') || ''''; 
                   END IF;

        END;
        END IF;
       END IF;



       v_queryStatement := (v_queryStatement) || (v_whereclause) || (') "paginatedCustomers" ON ("paginatedCustomers"."id"="customer"."id") LEFT JOIN "customercommunication" "PrimaryEmail" ON ("PrimaryEmail"."Customer_id"="paginatedCustomers"."id" AND "PrimaryEmail"."isPrimary"=1 AND "PrimaryEmail"."Type_id"=''COMM_TYPE_email'') LEFT JOIN "customergroup" ON ("customergroup"."Customer_id"="paginatedCustomers"."id") LEFT JOIN "customeraddress" ON ("paginatedCustomers"."id"="customeraddress"."Customer_id" AND "customeraddress"."isPrimary"=1 and "customeraddress"."Type_id"=''ADR_TYPE_HOME'') LEFT JOIN "address" ON ("customeraddress"."Address_id" = "address"."id") LEFT JOIN "city" ON ("city"."id" = "address"."City_id") LEFT JOIN "country" ON ("city"."Country_id" = "country"."id") LEFT JOIN "location" ON ("location"."id"="customer"."Location_id")') ;
       IF "_searchType" = 'GROUP_SEARCH' THEN

       BEGIN
        v_queryStatement2 := (v_search_count_statement) || (v_queryStatement) ;


        v_queryStatement := (v_search_select_statement) || (v_queryStatement) || (' GROUP BY "customer"."id", "customer"."FirstName", "customer"."MiddleName", "customer"."LastName", "customer"."UserName", "customer"."isCombinedUser", "customer"."combinedUserId", "customer"."Salutation", "customer"."Gender", "customer"."IsStaffMember", "customer"."CustomerType_id", "customer"."modifiedby", "customer"."lastmodifiedts", "customer"."Status_id", "PrimaryEmail"."Value","address"."City_id", "city"."Name" ,"customer"."Location_id","location"."Name", "paginatedCustomers"."id", "address"."addressLine1","address"."addressLine2" ,"address"."zipCode","country"."Name","customer"."isEnrolled" ') ;
        IF "_sortVariable" = 'DEFAULT'
          OR "_sortVariable" = ' '
          OR "_sortVariable" IS NULL THEN
         v_queryStatement := (v_queryStatement) || (' ORDER BY "FirstName"') ;
        ELSE

        BEGIN
           IF "_sortVariable" <> ' ' THEN
          v_queryStatement := (v_queryStatement) || (' ORDER BY ') || ("_sortVariable") ;
           END IF;

        END;
        END IF;
        IF "_sortDirection" <> ' ' THEN
         v_queryStatement := (v_queryStatement) || (' ') || ("_sortDirection") ;
        END IF;
        v_queryStatement := (v_queryStatement) || (' OFFSET ') || CAST("_pageOffset" AS NVARCHAR2) || ' rows fetch next ' || CAST("_pageSize" AS NVARCHAR2) ||(' rows only') ;

       END;
       ELSE
        IF "_searchType" = 'GROUP_SEARCH_TOTAL_COUNT' THEN
         v_queryStatement := (v_search_count_statement) || (v_queryStatement) ;
        END IF;
       END IF;

      END;
      ELSE
       IF "_searchType" LIKE 'CUSTOMER_SEARCH%' THEN

       BEGIN
        SELECT CAST("passwordlockoutsettings"."accountLockoutThreshold" AS VARCHAR2(50)) 

          INTO v__maxLockCount
          FROM "passwordlockoutsettings" 
         WHERE  "passwordlockoutsettings"."id" = 'PLOCKID1';
        v_search_select_statement := ('SELECT "customer"."id", "customer"."FirstName", "customer"."MiddleName", "customer"."companyLegalUnit", "customer"."LastName","customer"."DateOfBirth",(NVL("customer"."FirstName",'''')|| '' ''|| NVL("customer"."MiddleName",'''')|| '' ''|| NVL("customer"."LastName",'''')) as "name","customer"."UserName" as "Username", "customer"."isEnrolledFromSpotlight" as "isEnrolledFromSpotlight", "customer"."isCombinedUser" as "isCombinedUser", "customer"."Salutation", "customer"."Gender",(''****''|| SUBSTR("customer"."Ssn", -4)) as "Ssn",decode("customer"."isCombinedUser" , ''1'',''TYPE_id_RETAIL,TYPE_id_BUSINESS'',"customer"."CustomerType_id") AS "CustomerTypeId", NVL("customer"."combinedUserId", '''') as "combinedUserId", "company"."id" as "CompanyId", "company"."Name" as "CompanyName","organisationemployees"."isAuthSignatory" as "isAuthSignatory", case when NVL("customer"."lockCount",0) >= ') || (v__maxLockCount) || (' then N''SID_CUS_LOCKED'' else "customer"."Status_id" end as "Status_id","PrimaryPhone"."Value" AS "PrimaryPhoneNumber","PrimaryEmail"."Value" AS "PrimaryEmailAddress",LISTAGG(CAST("membergroup"."Name" as nvarchar2(2000)),'','') as "groups", "address"."addressLine1" As "addressLine1", "address"."addressLine2" As "addressLine2", "city"."Name" As "city", "address"."zipCode" As "zipCode", "country"."Name" As "county", "customer"."isEnrolled" as "isEnrolled","customer"."ApplicantChannel", "customer"."createdts", ''true'' as "isProfileExist"') ;
        v_search_count_statement := 'SELECT count(distinct "customer"."id") as "SearchMatchs" ' ;
        v_queryStatement := ' FROM "customer" JOIN (SELECT "customer"."id" FROM "customer" where "customer"."companyLegalUnit" = ' ||'''' || "_legalEntityId" || '''' || (CASE 
                                                            WHEN ( "_phone" <> ' ' ) THEN ' JOIN "customercommunication PrimaryPhone" ON ("PrimaryPhone"."Customer_id"="customer"."id" AND "PrimaryPhone"."isPrimary"=1 AND "PrimaryPhone"."Type_id"=''COMM_TYPE_phone'') '
        ELSE ''
           END) || (CASE 
                 WHEN ( "_email" <> ' ' ) THEN '  JOIN "customercommunication" "PrimaryEmail" ON ("PrimaryEmail"."Customer_id"="customer"."id" AND "PrimaryEmail"."isPrimary"=1 AND "PrimaryEmail"."Type_id"=''COMM_TYPE_email'') '
        ELSE ''
           END) || (CASE 
                 WHEN ( "_TIN" <> ' ' ) THEN ' LEFT JOIN "organisationmembership" ON ("customer"."Organization_Id" = "organisationmembership"."Organization_id")'
        ELSE ''
           END) || (CASE 
                 WHEN ( "_cardorAccountnumber" <> ' ' ) THEN ' LEFT JOIN "card" ON ("customer"."id" = "card"."User_id") LEFT JOIN "accounts" ON ("customer"."id" = "accounts"."User_id") LEFT JOIN "customeraccounts" ON ("customer"."id" = "customeraccounts"."Customer_id")'
        ELSE ''
           END) ;
        IF "_id" <> ' ' THEN
         v_queryStatement := (v_queryStatement) || (' and "customer"."id" = ') || '''' || "_id" || '''' ;
        END IF;
        IF "_name" <> ' ' THEN
         v_queryStatement := (v_queryStatement) || (' and "customer"."LastName" like (''') || ("_name") || '%'')' ;
        END IF;
        IF "_SSN" <> ' ' THEN
         v_queryStatement := (v_queryStatement) || (' and "customer"."Ssn" = ') || '''' || "_SSN" || '''' ;
        END IF;
        IF "_username" <> ' ' THEN
         v_queryStatement := (v_queryStatement) || (' and "customer"."UserName" = ') || '''' || "_username" || '''' ;
        END IF;
        IF "_dateOfBirth" <> ' ' THEN
         v_queryStatement := (v_queryStatement) || (' and "customer"."DateOfBirth" = ') || '''' || "_dateOfBirth" || '''' ;
        END IF;
        IF "_phone" <> ' ' THEN
         IF LENGTHB("_phone") > 9 THEN
         v_queryStatement := (v_queryStatement) || (' and "PrimaryPhone"."Value" like (''%') || ("_phone") || '%'')' ;
        ELSE
           v_queryStatement := (v_queryStatement) || (' and "PrimaryPhone"."Value" = ') || '''' || "_phone" || '''' ;
        END IF;
        END IF;
        IF "_email" <> ' ' THEN
         v_queryStatement := (v_queryStatement) || (' and "PrimaryEmail"."Value" = ') || '''' || "_email" || '''' ;
        END IF;
        IF "_companyId" <> ' ' THEN
         v_queryStatement := (v_queryStatement) || (' and "customer"."Organization_Id" = ') || '''' || "_companyId" || '''' ;
        END IF;
        IF "_IDValue" <> ' ' THEN
         IF "_IDType" = 'ID_DRIVING_LICENSE' THEN
         v_queryStatement := (v_queryStatement) || (' and ("customer"."DrivingLicenseNumber" = ') ||  '''' || "_IDValue" || '''' || (' or ("customer"."IDType_id" = ') || '''' || "_IDType" || '''' || (' and "customer"."IDValue" = ') || '''' || "_IDValue" || '''' || ('))') ;
        ELSE
           v_queryStatement := (v_queryStatement) || (' and ("customer"."IDType_id" = ') || '''' || "_IDType" || '''' || (' and "customer"."IDValue" = ') || '''' || "_IDValue" || '''' || (')') ;
        END IF;
        END IF;
        IF "_TIN" <> ' ' THEN
         v_queryStatement := (v_queryStatement) || (' and "organisationmembership"."Taxid" = ') || '''' || "_TIN" || '''' ;
        END IF;
        IF "_cardorAccountnumber" <> ' ' THEN
         v_queryStatement := (v_queryStatement) || (' and ("card"."cardNumber" = ') || '''' || "_cardorAccountnumber" || ''''  || (' or "accounts"."Account_id" = ') || '''' || "_cardorAccountnumber" || '''' || (' or "customeraccounts"."Account_id" = ') || '''' || "_cardorAccountnumber" || '''' || (')') ;
        END IF;
        v_queryStatement := (v_queryStatement) || (')         "paginatedCustomers" ON         ("paginatedCustomers"."id"="customer"."id") 
                  LEFT JOIN "customercommunication" "PrimaryPhone" 
                  ON ("PrimaryPhone"."Customer_id"="paginatedCustomers"."id" AND "PrimaryPhone"."isPrimary"=1 AND "PrimaryPhone"."Type_id"=''COMM_TYPE_phone'')
                  LEFT JOIN "customercommunication" "PrimaryEmail"          
                  ON ("PrimaryEmail"."Customer_id"="paginatedCustomers"."id" 
                  AND "PrimaryEmail"."isPrimary"=1 AND "PrimaryEmail"."Type_id"=''COMM_TYPE_email'')
                  LEFT JOIN "customeraddress" ON ("paginatedCustomers"."id"="customeraddress"."Customer_id" AND "customeraddress"."isPrimary"=1 and "customeraddress"."Type_id"= ''ADR_TYPE_HOME'')
                  LEFT JOIN "address" ON ("customeraddress"."Address_id" = "address"."id")
                  LEFT JOIN "city" ON ("city"."id" = "address"."City_id")
                  LEFT JOIN "country" ON ("city"."Country_id" = "country"."id")
                  LEFT JOIN "customergroup" ON 
                  ("customergroup"."Customer_id"="paginatedCustomers"."id") LEFT JOIN "membergroup" ON ("membergroup"."id"="customergroup"."Group_id") 
                  LEFT JOIN "organisation" "company" ON ("customer"."Organization_Id" = "company"."id") LEFT JOIN 
                  "organisationemployees" ON ("organisationemployees"."Organization_id" = "company"."id")') ;
        IF "_searchType" = 'CUSTOMER_SEARCH' THEN

        BEGIN
           v_queryStatement2 := (v_search_count_statement) || (v_queryStatement) ;
           v_queryStatement := (v_search_select_statement) || (v_queryStatement) || (' GROUP BY "customer"."id", "customer"."FirstName", "customer"."MiddleName", "customer"."LastName", "customer"."UserName", "customer"."Salutation", "customer"."Gender", "customer"."IsStaffMember","customer"."modifiedby", "customer"."lastmodifiedts", "customer"."Status_id", "PrimaryPhone"."Value","PrimaryEmail"."Value","customer"."Location_id","paginatedCustomers"."id","customer"."DateOfBirth","customer"."Ssn","CustomerType_id","company"."id","company"."Name","customer"."lockCount","customer"."ApplicantChannel","customer"."createdts","customer"."isEnrolledFromSpotlight","customer"."isCombinedUser","customer"."combinedUserId","organisationemployees"."isAuthSignatory","address"."addressLine1", "address"."addressLine2", "city"."Name", "address"."zipCode", "country"."Name", "customer"."isEnrolled", "customer"."companyLegalUnit" ') ;
           IF "_sortVariable" = 'DEFAULT'
           OR "_sortVariable" = ' '
           OR "_sortVariable" IS NULL THEN
          v_queryStatement := (v_queryStatement) || (' ORDER BY "FirstName"') ;
           ELSE

           BEGIN
            IF "_sortVariable" <> ' ' THEN
             v_queryStatement := (v_queryStatement) || (' ORDER BY "') || ("_sortVariable") || '"' ;
            END IF;

           END;
           END IF;
           IF "_sortDirection" <> ' ' THEN
          v_queryStatement := (v_queryStatement) || (' ') || ("_sortDirection") ;
           END IF;
           v_queryStatement := (v_queryStatement) || (' OFFSET ') || (CAST("_pageOffset" AS VARCHAR2)) || ' rows fetch next ' || (CAST("_pageSize" AS VARCHAR2)) || (' rows only') ;

        END;
        ELSE

        BEGIN
           IF "_searchType" = 'CUSTOMER_SEARCH_TOTAL_COUNT' THEN
          v_queryStatement := (v_search_count_statement) || (v_queryStatement) ;
           END IF;

        END;
        END IF;

       END;
       END IF;
      END IF;
         open "records" for v_queryStatement;
      DBMS_OUTPUT.PUT_LINE(v_queryStatement);
     IF "_searchType" = 'CUSTOMER_SEARCH'
     OR "_searchType" = 'GROUP_SEARCH' THEN

     BEGIN
      open "records1" for v_queryStatement2;

     END;
     END IF;


     END;



  END;
/
--  DDL for Procedure customeraction_save_proc




--  DDL for Procedure customeractions_create_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "customeractions_create_proc" 
(
  v__customerActionsCSV IN VARCHAR2,
  v__accountsCSV IN VARCHAR2,
  v__customerId IN VARCHAR2,
  v__businessTypeId IN VARCHAR2,
  v__groupId IN VARCHAR2,
  v_cursor OUT SYS_REFCURSOR
)
AS
   v_finished NUMBER(10,0) := 0;
   v_id VARCHAR2(50);
   v_featureActionId VARCHAR2(4000);
   v_actionslist VARCHAR2(4000);
   v_limitId VARCHAR2(4000);
   v_entryStatus NUMBER(10,0) := 0;
   v_accountId VARCHAR2(4000);
   v_actualLimitId VARCHAR2(4000);
   v_groupId VARCHAR2(4000);
   v_validActionsList CLOB;
   v_limitvalue VARCHAR2(4000);

   CURSOR accounts
     IS SELECT "Account_id" 
     FROM "accounts" 
    WHERE  "Account_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v__accountsCSV)));
   CURSOR actions
     IS SELECT "id" 
     FROM "featureaction" 
    WHERE "id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v_validActionsList)));

BEGIN

   IF ( v__groupId IS NULL
     OR v__groupId = '' ) THEN
    SELECT "groupbusinesstype"."Group_id" 

     INTO v_groupId
     FROM "groupbusinesstype" 
    WHERE  "groupbusinesstype"."BusinessType_id" = v__businessTypeId
             AND "groupbusinesstype"."isDefaultGroup" = 1;
   ELSE
      v_groupId := v__groupId ;
   END IF;
   SELECT "membergroup"."id" 

     INTO v_groupId
     FROM "membergroup" 
    WHERE  "membergroup"."id" = v_groupId
             AND "membergroup"."Type_id" = 'TYPE_ID_BUSINESS'
             AND "membergroup"."Status_id" = 'SID_ACTIVE';
   SELECT LISTAGG(CAST("Action_id" AS VARCHAR(255)), ',') 

     INTO v_validActionsList
     FROM "groupactionlimit" 
    WHERE  "groupactionlimit"."Group_id" = v_groupId
             AND ( "groupactionlimit"."Action_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v__customerActionsCSV))) );
   OPEN accounts;
   FETCH accounts INTO v_accountId;
   <<loop_3>>
   WHILE ((accounts%FOUND) = TRUE ) 
   LOOP 

      BEGIN
         OPEN actions;
         FETCH actions INTO v_featureActionId;
         <<loop_2>>
         WHILE ( (actions%FOUND) = TRUE ) 
         LOOP 
            DECLARE
               CURSOR limits
                 IS SELECT "LimitType_id" 
                 FROM "actionlimit" 
                WHERE  "Action_id" = v_featureActionId;

            BEGIN
               v_entryStatus := 0 ;
               OPEN limits;
               FETCH limits INTO v_limitId;
               <<loop_1>>
               WHILE ((limits%FOUND) = TRUE ) 
               LOOP 

                  BEGIN
                     SELECT "actionlimit"."value" 

                       INTO v_limitvalue
                       FROM "actionlimit" 
                      WHERE  "actionlimit"."Action_id" = v_featureActionId
                               AND "actionlimit"."LimitType_id" = v_limitId;
                     IF ( v_limitId = 'MAX_TRANSACTION_LIMIT' ) THEN
                      v_actualLimitId := 'AUTO_DENIED_TRANSACTION_LIMIT' ;
                     ELSE
                        IF ( v_limitId = 'MIN_TRANSACTION_LIMIT' ) THEN
                         v_actualLimitId := 'PRE_APPROVED_TRANSACTION_LIMIT' ;
                        ELSE
                           IF ( v_limitId = 'DAILY_LIMIT' ) THEN

                           BEGIN
                              v_actualLimitId := 'PRE_APPROVED_DAILY_LIMIT' ;
                              SELECT SUBSTR(SYS_GUID(), 0, 50) 

                                INTO v_id
                                FROM DUAL ;
                              INSERT INTO "customeraction"
                                ( "customeraction"."id", "customeraction"."RoleType_id", "customeraction"."Customer_id", "customeraction"."Action_id", "customeraction"."Account_id", "customeraction"."isAllowed", "customeraction"."LimitType_id", "customeraction"."value" )
                                VALUES ( v_id, 'TYPE_ID_BUSINESS', v__customerId, v_featureActionId, v_accountId, 1, v_actualLimitId, 0.00 );
                              v_actualLimitId := 'AUTO_DENIED_DAILY_LIMIT' ;

                           END;
                           ELSE

                           BEGIN
                              IF ( v_limitId = 'WEEKLY_LIMIT' ) THEN

                              BEGIN
                                 v_actualLimitId := 'PRE_APPROVED_WEEKLY_LIMIT' ;
                                 SELECT SUBSTR(SYS_GUID(), 0, 50) 

                                   INTO v_id
                                   FROM DUAL ;
                                 INSERT INTO "customeraction"
                                   ( "customeraction"."id", "customeraction"."RoleType_id", "customeraction"."Customer_id", "customeraction"."Action_id", "customeraction"."Account_id", "customeraction"."isAllowed", "customeraction"."LimitType_id", "customeraction"."value" )
                                   VALUES ( v_id, 'TYPE_ID_BUSINESS', v__customerId, v_featureActionId, v_accountId, 1, v_actualLimitId, 0.00 );
                                 v_actualLimitId := 'AUTO_DENIED_WEEKLY_LIMIT' ;

                              END;
                              END IF;

                           END;
                           END IF;
                        END IF;
                     END IF;
                     SELECT SUBSTR(SYS_GUID(), 0, 50) 

                       INTO v_id
                       FROM DUAL ;
                     INSERT INTO "customeraction"
                       ( "customeraction"."id", "customeraction"."RoleType_id", "customeraction"."Customer_id", "customeraction"."Action_id", "customeraction"."Account_id", "customeraction"."isAllowed", "customeraction"."LimitType_id", "customeraction"."value" )
                       VALUES ( v_id, 'TYPE_ID_BUSINESS', v__customerId, v_featureActionId, v_accountId, 1, v_actualLimitId, v_limitvalue );
                     v_entryStatus := 1 ;
                     FETCH limits INTO v_limitId;
                     GOTO loop_1;

                  END;
               END LOOP;
               CLOSE limits;
               IF v_entryStatus = 0 THEN

               BEGIN
                  SELECT SUBSTR(SYS_GUID(), 0, 50) 

                    INTO v_id
                    FROM DUAL ;
                  INSERT INTO "customeraction"
                    ( "customeraction"."id", "customeraction"."RoleType_id", "customeraction"."Customer_id", "customeraction"."Action_id", "customeraction"."Account_id", "customeraction"."isAllowed" )
                    VALUES ( v_id, 'TYPE_ID_BUSINESS', v__customerId, v_featureActionId, v_accountId, 1 );

               END;
               END IF;
               v_actionslist := v_featureActionId || ',' || v_actionslist ;
               FETCH actions INTO v_featureActionId;
               GOTO loop_2;

            END;
         END LOOP;
         CLOSE actions;
         FETCH accounts INTO v_accountId;
         GOTO loop_3;

      END;
   END LOOP;
   CLOSE accounts;
   SELECT SUBSTR(v_actionslist, 1, (LENGTH(v_actionslist) - 1)) 

     INTO v_actionslist
     FROM DUAL ;
   OPEN  v_cursor FOR
      SELECT v_actionslist actionslist  
        FROM DUAL  ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure customeralertchannel_deletebulk_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "customeralertchannel_deletebulk_proc" 

(
  "_deleteRecords" IN VARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   v_index1 NUMBER(10,0) := 0;
   v_numOfRecords NUMBER(10,0) := 0;
   v_rowValues VARCHAR2(4000);
   v_customer_id VARCHAR2(50);
   v_alertCategoryId VARCHAR2(50);
   v_alertTypeId VARCHAR2(50);
   v_alertSubTypeId VARCHAR2(75);
   v_accountId VARCHAR2(50);
   v_accountType VARCHAR2(50);
   v_channelId VARCHAR2(50);
   v_query VARCHAR2(4000);
   v_msg VARCHAR2(4000);
   v__deleteRecords VARCHAR2(32767) :="_deleteRecords";

BEGIN

   BEGIN

      BEGIN
         v_numOfRecords := LENGTH(v__deleteRecords) - LENGTH(REPLACE(v__deleteRecords, '|', '')) ;
         v_index1 := v_index1 + 1 ;
         WHILE ( v_index1 != v_numOfRecords + 1 ) 
         LOOP 
        
            BEGIN
             v_rowValues := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v__deleteRecords, '|', v_index1), '|', -1) ;
             IF  v_rowValues='' then
                          DBMS_OUTPUT.PUT_LINE(v_rowValues);
               ELSE
               v_customer_id := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 1), '"', -1) ;
               v_alertCategoryId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 2), ',"', -1) ;
               v_alertTypeId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 3), ',"', -1) ;
               v_alertSubTypeId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 4), ',"', -1) ;
               v_accountId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 5), ',"', -1) ;
               v_accountType := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 6), ',"', -1) ;
               v_channelId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, ',"', -1), '"', 1) ;
               v_query := ('delete from "customeralertchannel" where "customerId" = '|| '''' ||(v_customer_id|| '''')|| '  and "alertCategoryId" =  '|| ''''||(v_alertCategoryId|| '''')|| '  and "alertTypeId" = '|| ''''||(v_alertTypeId|| '''')|| '  and "alertSubTypeId" = '|| ''''||(v_alertSubTypeId|| '''')|| '  and "accountId" = '|| ''''||(v_accountId|| '''')|| '  and "accountType" = '|| ''''||(v_accountType|| '''')|| '  and "channelId" = '|| ''''||(v_channelId|| '''')) ;
               EXECUTE IMMEDIATE v_query;
                DBMS_OUTPUT.PUT_LINE(v_query);
               OPEN "records" FOR SELECT 1 from DUAL;
               
               END IF;
                v_index1 := v_index1 + 1 ;
            END;
         END LOOP;

      END;
   EXCEPTION
      WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);

   END;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure customeralertchannel_insertbulk_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "customeralertchannel_insertbulk_proc" 
(
  "_recordvalues" IN VARCHAR2, 
  "records" OUT SYS_REFCURSOR
)
AS
   v__recordvalues VARCHAR2(32767) := "_recordvalues";
   v_query VARCHAR2(32767);
   v_msg VARCHAR2(32767);
   v_numOfRecords NUMBER;


BEGIN
--   v_numOfRecords := LENGTH(v__recordvalues) - LENGTH(REPLACE(v__recordvalues, ',', ''));
   BEGIN
      BEGIN
--         IF v__recordvalues != '' THEN
           DBMS_OUTPUT.PUT_LINE('11---->1' || v_numOfRecords);
             BEGIN
--            /*TODO:SQLDEV*/ SET QUOTED_IDENTIFIER OFF /*END:SQLDEV*/
            v__recordvalues := REPLACE(v__recordvalues, '"', CHR(39)) ;
            v__recordvalues := REPLACE(v__recordvalues,'(','into  "customeralertchannel" ( "customerId", "alertCategoryId", "alertTypeId", "alertSubTypeId", "accountId", "accountType","channelId", "createdby","companyLegalUnit") VALUES( ') ;
            v__recordvalues := REPLACE(v__recordvalues,'),',')');
            v_query := ('INSERT ALL '|| v__recordvalues|| 'SELECT 1 FROM dual') ;
            DBMS_OUTPUT.PUT_LINE('test-->'||v_query);
            EXECUTE IMMEDIATE v_query;
            
            END;
--         END IF;
OPEN "records" FOR SELECT 1 from DUAL;
      END;
   EXCEPTION
      WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);

   END;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure customeralertchannel_updatebulk_proc


CREATE OR REPLACE NONEDITIONABLE PROCEDURE "customeralertchannel_updatebulk_proc" 
(
  "_updateRecords" IN VARCHAR2, "records" OUT SYS_REFCURSOR
)
AS
   v_index1 NUMBER(10,0) := 0;
   v_numOfRecords NUMBER(10,0) := 0;
   v_rowValues VARCHAR2(4000);
   v_customer_id VARCHAR2(50);
   v_alertCategoryId VARCHAR2(50);
   v_alertTypeId VARCHAR2(50);
   v_alertSubTypeId VARCHAR2(75);
   v_accountId VARCHAR2(50);
   v_modifiedby VARCHAR2(50);
   v_accountType VARCHAR2(50);
   v_channelId VARCHAR2(50);
   v_query VARCHAR2(4000);
   v_whereCondition VARCHAR2(4000);
   v_msg VARCHAR2(4000);
   v__updateRecords VARCHAR2(32767) := "_updateRecords";


BEGIN

   BEGIN

      BEGIN
         v_numOfRecords := LENGTH("_updateRecords") - LENGTH(REPLACE("_updateRecords", '|', '')) ;
          DBMS_OUTPUT.PUT_LINE('TEST1 -->' ||v_numOfRecords);
         v_index1 := v_index1 + 1 ;
         WHILE ( v_index1 != v_numOfRecords + 1 ) 
         LOOP 

            BEGIN
               v_rowValues := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v__updateRecords, '|', v_index1), '|', -1) ;
               v_customer_id := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 1), '"', -1) ;
               v_alertCategoryId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 2), ',"', -1) ;
               v_alertTypeId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 3), ',"', -1) ;
               v_alertSubTypeId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 4), ',"', -1) ;
               v_accountId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 5), ',"', -1) ;
               v_modifiedby := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 6), ',"', -1) ;
               v_channelId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 7), ',"', -1) ;
               v_accountType := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, ',"', -1), '"', 1) ;
               v_query := ('update "customeralertchannel" set  "modifiedby" ='|| '''' ||(v_modifiedby|| '''')) ;
               v_whereCondition := ('where "customerId" = '|| ''''||(v_customer_id|| '''')|| '  and "alertCategoryId" = '|| ''''||(v_alertCategoryId|| '''')|| '  and "alertTypeId" = '|| ''''||(v_alertTypeId|| '''')|| '  and "alertSubTypeId" = '|| ''''||(v_alertSubTypeId || '''')|| '  and "accountId" = '|| ''''||(v_accountId|| '''')|| '  and "accountType" = '|| ''''||(v_accountType|| '''')|| '  and "channelId" = '|| ''''||(v_channelId|| '''')) ;
               v_query := (v_query|| '  '|| v_whereCondition) ;
               EXECUTE IMMEDIATE v_query;
               OPEN "records" FOR select v_msg FROM DUAL;
                 DBMS_OUTPUT.PUT_LINE(v_query);
               v_index1 := v_index1 + 1 ;

            END;
         END LOOP;

      END;
   EXCEPTION
      WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);END;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure customeralertfrequency_deletebulk_proc


CREATE OR REPLACE NONEDITIONABLE PROCEDURE "customeralertfrequency_deletebulk_proc" 
(
  "_deleteRecords" IN VARCHAR2, "records" OUT SYS_REFCURSOR
)
AS
   v_index1 NUMBER(10,0) := 0;
   v_numOfRecords NUMBER(10,0) := 0;
   v_rowValues VARCHAR2(4000);
   v_customer_id VARCHAR2(50);
   v_alertCategoryId VARCHAR2(50);
   v_alertTypeId VARCHAR2(50);
   v_alertSubTypeId VARCHAR2(75);
   v_accountId VARCHAR2(50);
   v_accountType VARCHAR2(50);
   v_query VARCHAR2(4000);
   v_msg VARCHAR2(4000);
   v__deleteRecords VARCHAR2(32767) := "_deleteRecords";


BEGIN

   BEGIN

      BEGIN
         v_numOfRecords := LENGTH(v__deleteRecords) - LENGTH(REPLACE(v__deleteRecords, '|', '')) ;
         v_index1 := v_index1 + 1 ;
         WHILE ( v_index1 != v_numOfRecords + 1 ) 
         LOOP 
           
            BEGIN
               v_rowValues := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v__deleteRecords, '|', v_index1), '|', -1) ;
               IF v_rowValues IS NULL OR v_rowValues='' then
                          DBMS_OUTPUT.PUT_LINE(v_rowValues);
               ELSE
               v_customer_id := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 1), '"', -1) ;
               v_alertCategoryId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 2), ',"', -1) ;
               v_alertTypeId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 3), ',"', -1) ;
               v_alertSubTypeId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 4), ',"', -1) ;
               v_accountId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 5), ',"', -1) ;
               v_accountType := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, ',"', -1), '"', 1) ;
               v_query := ('delete from "customeralertfrequency" where "customerId" = '|| ''''||(v_customer_id|| '''')|| '  and "alertCategoryId" =  '|| ''''||(v_alertCategoryId|| '''')|| '  and "alertTypeId" = '|| '''' || (v_alertTypeId|| '''')|| '  and "alertSubTypeId" = '|| ''''||(v_alertSubTypeId|| '''')|| '  and "accountId" = '|| ''''||(v_accountId|| '''')|| '  and "accountType" = '|| ''''||(v_accountType|| '''')|| ' ;') ;
--               EXECUTE IMMEDIATE v_query;
                 DBMS_OUTPUT.PUT_LINE(v_query);
--                OPEN "records" FOR select v_msg FROM DUAL;
               v_index1 := v_index1 + 1 ;
               END IF;
            END;
         END LOOP;

      END;
   EXCEPTION
      WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
      END;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure customeralertfrequency_insertbulk_proc


CREATE OR REPLACE NONEDITIONABLE PROCEDURE "customeralertfrequency_insertbulk_proc" 
(
  iv__recordvalues IN VARCHAR2, v_cursor OUT SYS_REFCURSOR
)
AS
   v__recordvalues VARCHAR2(4000) := iv__recordvalues;
   v_query VARCHAR2(4000);
   v_msg VARCHAR2(4000);

BEGIN

   BEGIN

      BEGIN
         IF v__recordvalues IS NOT NULL
           AND v__recordvalues != '' THEN

         BEGIN
--            /*TODO:SQLDEV*/ SET QUOTED_IDENTIFIER OFF /*END:SQLDEV*/
            v__recordvalues := REPLACE(v__recordvalues, '"', CHR(39)) ;
--            /*TODO:SQLDEV*/ SET QUOTED_IDENTIFIER ON /*END:SQLDEV*/
            v_query := ('INSERT INTO "customeralertfrequency" ("customerId", "alertCategoryId", "alertTypeId", "alertSubTypeId", "accountId", "accountType","alertFrequencyId", "frequencyValue", "frequencyTime", "createdby") values '|| v__recordvalues|| ';') ;
            EXECUTE IMMEDIATE v_query;

         END;
         END IF;

      END;
   EXCEPTION
      WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
      END;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure customeralertfrequency_updatebulk_proc


CREATE OR REPLACE NONEDITIONABLE PROCEDURE "customeralertfrequency_updatebulk_proc" 
(
  "_updateRecords" IN VARCHAR2, "records" OUT SYS_REFCURSOR
)
AS
   v_index1 NUMBER(10,0) := 0;
   v_numOfRecords NUMBER(10,0) := 0;
   v_rowValues VARCHAR2(4000);
   v_customer_id VARCHAR2(50);
   v_alertCategoryId VARCHAR2(50);
   v_alertTypeId VARCHAR2(50);
   v_alertSubTypeId VARCHAR2(75);
   v_accountId VARCHAR2(50);
   v_modifiedby VARCHAR2(50);
   v_accountType VARCHAR2(50);
   v_alertFrequencyId VARCHAR2(50);
   v_frequencyValue VARCHAR2(50);
   v_frequencyTime VARCHAR2(50);
   v_query VARCHAR2(4000);
   v_whereCondition VARCHAR2(4000);
   v_msg VARCHAR2(4000);
   v__updateRecords VARCHAR2(32767) := "_updateRecords";


BEGIN

   BEGIN

      BEGIN
         v_numOfRecords := LENGTH(v__updateRecords) - LENGTH(REPLACE(v__updateRecords, '|', '')) ;
         v_index1 := v_index1 + 1 ;
         WHILE ( v_index1 != v_numOfRecords + 1 ) 
         LOOP 

            BEGIN
               v_rowValues := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v__updateRecords, '|', v_index1), '|', -1) ;
               v_customer_id := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 1), '"', -1) ;
               v_alertCategoryId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 2), ',"', -1) ;
               v_alertTypeId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 3), ',"', -1) ;
               v_alertSubTypeId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 4), ',"', -1) ;
               v_accountId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 5), ',"', -1) ;
               v_accountType := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 6), '"', -1) ;
               v_alertFrequencyId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 7), ',"', -1) ;
               v_frequencyValue := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 8), ',"', -1) ;
               v_frequencyTime := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 9), ',"', -1) ;
               v_modifiedby := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, ',"', -1), '"', 1) ;
               IF v_frequencyValue != 'null' THEN

               BEGIN
                  v_frequencyValue := ''''||(v_frequencyValue|| '''') ;

               END;
               END IF;
               IF v_frequencyTime != 'null' THEN

               BEGIN
                  v_frequencyTime := ''''||(v_frequencyTime|| '''') ;

               END;
               END IF;
               v_query := ('UPDATE "customeralertfrequency" set  "modifiedby" ='|| ''''||(v_modifiedby|| '''')|| ',"alertFrequencyId" = '|| ''''||(v_alertFrequencyId|| '''')|| ',"frequencyValue"='|| v_frequencyValue|| ',"frequencyTime"='|| v_frequencyTime|| ' where "customerId" = '||''''||(v_customer_id|| '''')|| '  and "alertCategoryId" = '||''''||(v_alertCategoryId|| '''')|| '  and "alertTypeId" = '|| ''''||(v_alertTypeId|| '''')|| '  and "alertSubTypeId" = '|| ''''||(v_alertSubTypeId|| '''')|| '  and "accountId" = '||''''||(v_accountId|| '''')||'  and "accountType" = '||''''||(v_accountType|| '''')|| ' ;') ;
               EXECUTE IMMEDIATE v_query;
               v_index1 := v_index1 + 1 ;

            END;
         END LOOP;

      END;
   EXCEPTION
      WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);

   END;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure customers_get_legalentities_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "customers_get_legalentities_proc" (
  "_customerList" IN VARCHAR2, "records" OUT SYS_REFCURSOR
) AS BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
OPEN "records" FOR 
SELECT 
  "customerlegalentity"."Customer_id", 
  "customerlegalentity"."legalEntityId" 
FROM 
  "customerlegalentity" 
WHERE 
  "customerlegalentity"."Customer_id" IN (
    SELECT 
      DISTINCT COLUMN_VALUE 
    FROM 
      TABLE(
        UTILS.STRING_SPLIT("_customerList", ',')
      )
  );
--DBMS_SQL.RETURN_RESULT(v_cursor);
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/
--  DDL for Procedure customrole_actionlimits_create_proc


create or replace NONEDITIONABLE PROCEDURE "customrole_actionlimits_create_proc" (
  "_queryInput" IN VARCHAR2, "_customRoleId" IN NUMBER
) AS v__queryInput VARCHAR2(4000) := "_queryInput";
v_index NUMBER(10, 0);
v_numOfRecords NUMBER(10, 0);
v_recordsData VARCHAR2(4000);
v_query VARCHAR2(4000);
BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
/*DELETE "customroleactionlimits" 
WHERE 
  "customroleactionlimits"."customRole_id" = "_customRoleId";*/
v_index := 0;
SELECT 
  REPLACE(v__queryInput, '"', '''') INTO v__queryInput 
FROM 
  DUAL;
v_numOfRecords := CASE WHEN v__queryInput IS NULL THEN 0 ELSE LENGTH(v__queryInput) - LENGTH(
  REPLACE(v__queryInput, '|', '')
) + 1 END;
dbms_output.put_line('v_numOfRecords'||v_numOfRecords);
WHILE (1 = 1) LOOP BEGIN v_index := v_index + 1;
IF (v_index = v_numOfRecords + 1) THEN EXIT;
ELSE BEGIN v_recordsData := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v__queryInput, '|', v_index), 
  '|', 
  -1
);
v_query := (
  'INSERT INTO "customroleactionlimits"("customRole_id","coreCustomerId","contractId","featureId","action_id","account_id","isAllowed", "limitGroupId", "limitType_id","value","companyLegalUnit") VALUES ('
) || (v_recordsData) || (')');
EXECUTE IMMEDIATE v_query;
dbms_output.put_line('v_query'||v_query);
END;
END IF;
END;
END LOOP;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;

/
--  DDL for Procedure customrole_contract_delete_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "customrole_contract_delete_proc" (
  v_customRoleId IN VARCHAR2, v_contractId IN VARCHAR2, 
  v_coreCustomerId IN VARCHAR2
) AS v_contract_statement VARCHAR2(4000);
v_accounts_statement VARCHAR2(4000);
v_excludedaccounts_statement VARCHAR2(4000);
v_action_statement VARCHAR2(4000);
v_excluded_action_statement VARCHAR2(4000);
v_limitgroup_statement VARCHAR2(4000);
v_where_clause VARCHAR2(4000);
v_where_clause1 VARCHAR2(4000);
v_where_clause2 VARCHAR2(4000);
BEGIN v_contract_statement := 'DELETE FROM "contractcustomrole" where';
v_accounts_statement := 'DELETE FROM "customroleaccounts" where ';
v_excludedaccounts_statement := 'DELETE FROM "excludedcustomroleaccounts" where';
v_action_statement := 'DELETE FROM "customroleactionlimits" where';
v_excluded_action_statement := 'DELETE FROM "excludedcustomroleactionlimits" where';
v_limitgroup_statement := 'DELETE FROM "customerlimitgrouplimits" where';
v_where_clause := ' ';
v_where_clause1 := ' ';
IF (v_customRoleId != '') THEN BEGIN v_where_clause := v_where_clause || (' "customRoleId" = ') || (
  (
    '''' ||(
      (v_customRoleId)|| ''''
    )
  )
);
v_where_clause1 := v_where_clause1 || (' "customRole_id" = ') || (
  (
    '''' ||(
      (v_customRoleId)|| ''''
    )
  )
);
v_where_clause2 := v_where_clause2 || (' "Customer_id" = ') || (
  (
    '''' ||(
      (v_customRoleId)|| ''''
    )
  )
);
END;
END IF;
IF (v_contractId != '') THEN BEGIN IF (v_where_clause != '') THEN BEGIN v_where_clause := v_where_clause || (' AND ');
v_where_clause1 := v_where_clause1 || (' AND ');
v_where_clause2 := v_where_clause2 || (' AND ');
END;
END IF;
v_where_clause := v_where_clause || (' "contractId" = ') || (
  (
    '''' ||(
      (v_contractId)|| ''''
    )
  )
);
v_where_clause1 := v_where_clause1 || (' "contractId" = ') || (
  (
    '''' ||(
      (v_contractId)|| ''''
    )
  )
);
v_where_clause2 := v_where_clause2 || (' "contractId" = ') || (
  (
    '''' ||(
      (v_contractId)|| ''''
    )
  )
);
END;
END IF;
IF (v_coreCustomerId != '') THEN BEGIN IF (v_where_clause != '') THEN BEGIN v_where_clause := v_where_clause || (' AND ');
v_where_clause1 := v_where_clause1 || (' AND ');
v_where_clause2 := v_where_clause2 || (' AND ');
END;
END IF;
v_where_clause := v_where_clause || (' "coreCustomerId" = ') || (
  (
    '''' ||(
      (v_coreCustomerId)|| ''''
    )
  )
);
v_where_clause1 := v_where_clause1 || (' "coreCustomerId" = ') || (
  (
    '''' ||(
      (v_coreCustomerId)|| ''''
    )
  )
);
v_where_clause2 := v_where_clause2 || (' "coreCustomerId" = ') || (
  (
    '''' ||(
      (v_coreCustomerId)|| ''''
    )
  )
);
END;
END IF;
IF (v_where_clause != '') THEN BEGIN v_contract_statement := v_contract_statement + v_where_clause;
v_accounts_statement := v_accounts_statement + v_where_clause;
v_excludedaccounts_statement := v_excludedaccounts_statement + v_where_clause;
v_action_statement := v_action_statement + v_where_clause1;
v_excluded_action_statement := v_excluded_action_statement + v_where_clause1;
v_limitgroup_statement := v_limitgroup_statement + v_where_clause2;
EXECUTE IMMEDIATE v_contract_statement;
EXECUTE IMMEDIATE v_accounts_statement;
EXECUTE IMMEDIATE v_excludedaccounts_statement;
EXECUTE IMMEDIATE v_action_statement;
EXECUTE IMMEDIATE v_excluded_action_statement;
EXECUTE IMMEDIATE v_limitgroup_statement;
END;
END IF;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/
--  DDL for Procedure dbpalerts_customercommunication


CREATE OR REPLACE NONEDITIONABLE PROCEDURE "dbpalerts_customercommunication" 
(
  v__customers IN VARCHAR2, "customercommunication" OUT SYS_REFCURSOR
)
AS
   

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   OPEN  "customercommunication" FOR
      SELECT "customercommunication"."Customer_id" ,
             "customercommunication"."Type_id" ,
             "customercommunication"."Value" 
        FROM "customercommunication" 
       WHERE "customercommunication"."Customer_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v__customers)))
                AND "customercommunication"."isPrimary" = 1
        ORDER BY "customercommunication"."Type_id" ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure dbpalerts_getNotificationId


CREATE OR REPLACE NONEDITIONABLE PROCEDURE "dbpalerts_getnotificationid" 
(
    v_cursor OUT SYS_REFCURSOR
)
AS
   /*
         *   SSMA warning messages:
         *   M2SS0240: The behaviour of Standard Function SCOPE_IDENTITY may not be same as in MySQL
         */


BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   OPEN  v_cursor FOR
      SELECT NVL(utils.getidentity, 0) lastid  
        FROM DUAL  ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure dbpevents_getCustidFromAccount


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "dbpevents_getCustidFromAccount" (
  "_ACCOUNTS" IN NVARCHAR2, "accounts" OUT SYS_REFCURSOR
) AS BEGIN OPEN "accounts" FOR 
SELECT 
  "Account_id", 
  "User_id", 
  "Type_id" "accounttype_id" 
FROM 
  "accounts" 
WHERE 
  FIND_IN_SET(
    "accounts"."Account_id", "_ACCOUNTS"
  ) <> 0;
END;
/
--  DDL for Procedure dbpevents_getCustIdFromCore


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "dbpevents_getCustIdFromCore" (
  "_BACKENDIDS" IN NVARCHAR2, "backendidentifier" OUT SYS_REFCURSOR
) AS BEGIN OPEN "backendidentifier" FOR 
SELECT 
  "BackendId", 
  "Customer_id" 
FROM 
  "backendidentifier" 
WHERE 
  FIND_IN_SET(
    "backendidentifier"."BackendId", 
    "_BACKENDIDS"
  ) <> 0;
END;
/
--  DDL for Procedure dbpevents_getCustomerData


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "dbpevents_getCustomerData" (
  "_CUSTOMERIDS" IN NVARCHAR2, "_USERNAMES" IN NVARCHAR2, 
  "customer" OUT SYS_REFCURSOR
) AS BEGIN OPEN "customer" FOR 
SELECT 
  "id" "CustomerId", 
  "UserName" 
FROM 
  "customer" 
WHERE 
  FIND_IN_SET("customer"."id", "_CUSTOMERIDS") <> 0 
  OR FIND_IN_SET(
    "customer"."UserName", "_USERNAMES"
  ) <> 0;
END;
/
--  DDL for Procedure dbxcustomeralertentitlement_deletebulk_proc


CREATE OR REPLACE NONEDITIONABLE PROCEDURE "dbxcustomeralertentitlement_deletebulk_proc" 
(
  "_deleteRecords" IN VARCHAR2, "records" OUT SYS_REFCURSOR
)
AS
   v_index1 NUMBER(10,0) := 0;
   v_numOfRecords NUMBER(10,0) := 0;
   v_rowValues VARCHAR2(4000);
   v_customer_id VARCHAR2(50);
   v_alertCategoryId VARCHAR2(50);
   v_alertTypeId VARCHAR2(50);
   v_alertSubTypeId VARCHAR2(75);
   v_accountId VARCHAR2(50);
   v_accountType VARCHAR2(50);
   v_query VARCHAR2(4000);
   v_companyLegalUnit VARCHAR2(100);
   v_msg VARCHAR2(4000);
   v__deleteRecords VARCHAR2(32767) := "_deleteRecords";


BEGIN

   BEGIN

      BEGIN
         v_numOfRecords := LENGTH(v__deleteRecords) - LENGTH(REPLACE(v__deleteRecords, '|', '')) +1;
         v_index1 := v_index1 + 1 ;
         WHILE ( v_index1 != v_numOfRecords + 1 ) 
         LOOP 
            DBMS_OUTPUT.PUT_LINE(v_numOfRecords);
            BEGIN
               v_rowValues := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v__deleteRecords, '|', v_index1), '|', -1) ;
               IF v_rowValues = ''  then
                          DBMS_OUTPUT.PUT_LINE(v_numOfRecords);
               ELSE
               v_customer_id := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 1), '"', -1) ;
               v_alertCategoryId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 2), ',"', -1) ;
               v_alertTypeId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 3), ',"', -1) ;
               v_alertSubTypeId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 4), ',"', -1) ;
               v_accountId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 5), ',"', -1) ;
               v_accountType := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 6), ',"', -1) ;
               v_companyLegalUnit := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, ',"', -1), '"', 1) ;
               v_query := ('delete from "dbxcustomeralertentitlement" where '|| '"Customer_id" = '|| ''''||(v_customer_id|| '''')|| '  and "alertCategoryId" = '|| ''''||(v_alertCategoryId|| '''')|| '  and "AlertTypeId" = '|| ''''||(v_alertTypeId|| '''')|| '  and "alertSubTypeId" = '|| ''''||(v_alertSubTypeId|| '''')|| '  and "AccountId" = '|| ''''||(v_accountId|| '''')|| '  and "AccountType" = '|| ''''||(v_accountType) ||'''' || '  and "companyLegalUnit" = '|| ''''||(v_companyLegalUnit) ||'''') ;
                EXECUTE IMMEDIATE v_query;
                DBMS_OUTPUT.PUT_LINE(v_query);              
               END IF;
               v_index1 := v_index1 + 1 ;
            END;
         END LOOP;
         open "records" for select 1 from Dual;

      END;
   EXCEPTION
      WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);

   END;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure dbxcustomeralertentitlement_insertbulk_proc


CREATE OR REPLACE NONEDITIONABLE PROCEDURE "dbxcustomeralertentitlement_insertbulk_proc" 
(
  "_recordvalues" IN VARCHAR2, "records" OUT SYS_REFCURSOR
)
AS
   v__recordvalues VARCHAR2(32767) := "_recordvalues";
   v_query VARCHAR2(32767);
   v_msg VARCHAR2(32767);


BEGIN

   BEGIN

      BEGIN
--         IF v__recordvalues != '' THEN

         BEGIN
--            /*TODO:SQLDEV*/ SET QUOTED_IDENTIFIER OFF /*END:SQLDEV*/
            v__recordvalues := REPLACE(v__recordvalues, '"', CHR(39)) ;
            v__recordvalues := REPLACE(v__recordvalues,'(','INTO  "dbxcustomeralertentitlement"("Customer_id","alertCategoryId","AlertTypeId","alertSubTypeId","AccountId","AccountType","Value1","Value2","alertRequestId","createdby","companyLegalUnit") VALUES( ') ;
            v__recordvalues := REPLACE(v__recordvalues,'),',')');
--            /*TODO:SQLDEV*/ SET QUOTED_IDENTIFIER ON /*END:SQLDEV*/
            v_query := ('INSERT ALL '|| v__recordvalues|| 'SELECT 1 FROM dual') ;
--            /*TODO:SQLDEV*/ SET QUOTED_IDENTIFIER ON /*END:SQLDEV*/

--            v_query := ('INSERT INTO  "dbxcustomeralertentitlement"("Customer_id","alertCategoryId","AlertTypeId","alertSubTypeId","AccountId","AccountType","Value1","Value2","alertRequestId","createdby") VALUES '|| v__recordvalues|| ';') ;
           DBMS_OUTPUT.PUT_LINE(v_query);
            EXECUTE IMMEDIATE v_query;
            OPEN "records" FOR SELECT 1 FROM dual;
             
         END;
--         END IF;

      END;
   EXCEPTION
      WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);

   END;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure dbxcustomeralertentitlement_updatebulk_proc


CREATE OR REPLACE NONEDITIONABLE PROCEDURE "dbxcustomeralertentitlement_updatebulk_proc" 
(
  "_updateRecords" IN VARCHAR2, "records" OUT SYS_REFCURSOR
)
AS
   v_index1 NUMBER(10,0) := 0;
   v_numOfRecords NUMBER(10,0) := 0;
   v_numOfParams NUMBER(10,0) := 0;
   v_rowValues VARCHAR2(4000);
   v_customer_id VARCHAR2(50);
   v_alertCategoryId VARCHAR2(50);
   v_alertTypeId VARCHAR2(50);
   v_alertSubTypeId VARCHAR2(75);
   v_accountId VARCHAR2(50);
   v_modifiedby VARCHAR2(50);
   v_alertRequestId VARCHAR2(255);
   v_accountType VARCHAR2(50);
   v_value1 VARCHAR2(255);
   v_value2 VARCHAR2(255);
   v_query VARCHAR2(32767);
   v_whereCondition VARCHAR2(4000);
   v_msg VARCHAR2(4000);
   v__updateRecords VARCHAR2(32767) :="_updateRecords";


BEGIN

   BEGIN

      BEGIN
         v_numOfRecords := LENGTH(v__updateRecords) - LENGTH(REPLACE(v__updateRecords, '|', '')) ;
         v_index1 := v_index1 + 1 ;
         WHILE ( v_index1 != v_numOfRecords + 1 ) 
         LOOP 

            BEGIN
               v_rowValues := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v__updateRecords, '|', v_index1), '|', -1) ;
               v_numOfParams := LENGTH(v_rowValues) - LENGTH(REPLACE(v_rowValues, ',', ' ')) ;
               v_customer_id := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 1), '"', -1) ;
               v_alertCategoryId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 2), ',"', -1) ;
               v_alertTypeId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 3), ',"', -1) ;
               v_alertSubTypeId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 4), ',"', -1) ;
               v_accountId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 5), ',"', -1) ;
               v_modifiedby := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 6), ',"', -1) ;
               v_alertRequestId := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 7), ',"', -1) ;
               v_accountType := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, ',"', -1), '"', 1) ;
               v_query := ('update "dbxcustomeralertentitlement" set  "modifiedby" ='|| ''''||(v_modifiedby|| '''')) ;
               IF v_alertRequestId != 'null' THEN

               BEGIN
                  v_query := (v_query|| ', "alertRequestId" = '|| ''''||(v_alertRequestId|| '''')) ;

               END;
               END IF;
               IF v_numOfParams > 7 THEN

               BEGIN
                  v_value1 := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 8), ',"', -1) ;
                  v_query := (v_query|| ', Value1 = '|| ''''||(v_value1|| '''')) ;

               END;
               END IF;
               IF v_numOfParams > 8 THEN

               BEGIN
                  v_value2 := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v_rowValues, '",', 9), ',"', -1) ;
                  v_query := (v_query|| ', Value2 = '|| ''''||(v_value2|| '''')) ;

               END;
               END IF;
               v_whereCondition := ('where "Customer_id" = '|| ''''||(v_customer_id|| '''')|| '  and "alertCategoryId" = '|| ''''||(v_alertCategoryId|| '''')|| '  and "AlertTypeId" = '|| ''''||(v_alertTypeId|| '''')|| '  and "alertSubTypeId" = '|| ''''||(v_alertSubTypeId|| '''')||'  and "AccountId" = '|| ''''||(v_accountId|| '''')|| '  and "AccountType" = '|| ''''||(v_accountType|| '''')) ;
               v_query := (v_query|| '  '|| v_whereCondition) ;
               EXECUTE IMMEDIATE v_query;
                DBMS_OUTPUT.PUT_LINE(v_query);
               v_index1 := v_index1 + 1 ;

            END;
         END LOOP;
         OPEN "records" for select 1 from DUAL;

      END;
   EXCEPTION
      WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);

   END;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure default_autosync_accounts_create_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "default_autosync_accounts_create_proc" (
  v__customerId IN VARCHAR2, v__queryInput IN VARCHAR2
) AS v_index NUMBER(10, 0) := 0;
v_recordsData VARCHAR2(255) := '';
v_numOfRecords VARCHAR2(255) := '';
v_coreCustomerId VARCHAR2(255) := '';
v_accountId VARCHAR2(255) := '';
v_arrangementId VARCHAR2(255) := '';
v_accountName VARCHAR2(255) := '';
v_accountType VARCHAR2(255) := '';
v_contractId VARCHAR2(255) := '';
v_typeId VARCHAR2(255) := '';
v_contractaccounts VARCHAR2(255) := '';
v_customerAccounts VARCHAR2(255) := '';
v_id VARCHAR2(255) := '';
BEGIN v_numOfRecords := LENGTHB(v__queryInput) - LENGTHB(
  REPLACE(v__queryInput, '|', '')
) + 1;
WHILE (1 = 1) LOOP BEGIN v_index := v_index + 1;
IF v_index = v_numOfRecords + 1 THEN EXIT;
ELSE BEGIN v_recordsData := (
  UTILS.SUBSTRING_INDEX(
    UTILS.SUBSTRING_INDEX(v__queryInput, '|', v_index), 
    '|', 
    -1
  )
);
v_coreCustomerId := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v_recordsData, ':', 1), 
  ':', 
  -1
);
v_accountId := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v_recordsData, ':', 2), 
  ':', 
  -1
);
v_arrangementId := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v_recordsData, ':', 3), 
  ':', 
  -1
);
v_accountName := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v_recordsData, ':', 4), 
  ':', 
  -1
);
v_accountType := UTILS.SUBSTRING_INDEX(
  UTILS.SUBSTRING_INDEX(v_recordsData, ':', 5), 
  ':', 
  -1
);
SELECT 
  "contractcorecustomers"."contractId" INTO v_contractId 
FROM 
  "contractcorecustomers" 
WHERE 
  "contractcorecustomers"."coreCustomerId" = v_coreCustomerId;
SELECT 
  "accounttype"."TypeID" INTO v_typeId 
FROM 
  "accounttype" 
WHERE 
  "accounttype"."TypeDescription" = v_accountType;
SELECT 
  "contractaccounts"."id" INTO v_contractaccounts 
FROM 
  "contractaccounts" 
WHERE 
  "contractaccounts"."coreCustomerId" = v_coreCustomerId 
  AND "contractaccounts"."accountId" = v_accountId;
IF (
  NVL(v_contractaccounts, '') = ' '
) THEN BEGIN 
SELECT 
  SUBSTR(
    SYS_GUID(), 
    0, 
    50
  ) INTO v_id 
FROM 
  DUAL;
INSERT INTO "contractaccounts" (
  "contractaccounts"."id", "contractaccounts"."contractId", 
  "contractaccounts"."accountId", 
  "contractaccounts"."accountName", 
  "contractaccounts"."typeId", "contractaccounts"."coreCustomerId", 
  "contractaccounts"."ownerType", 
  "contractaccounts"."statusDesc", 
  "contractaccounts"."arrangementId"
) 
VALUES 
  (
    v_id, v_contractId, v_accountId, v_accountName, 
    v_typeId, v_coreCustomerId, 'Owner', 
    'Active', v_arrangementId
  );
END;
END IF;
SELECT 
  "customeraccounts"."Account_id" INTO v_customerAccounts 
FROM 
  "customeraccounts" 
WHERE 
  "customeraccounts"."Account_id" = v_accountId;
IF (
  NVL(v_customerAccounts, '') = ' '
) THEN BEGIN 
SELECT 
  SUBSTR(
    SYS_GUID(), 
    0, 
    50
  ) INTO v_id 
FROM 
  DUAL;
INSERT INTO "customeraccounts" (
  "customeraccounts"."id", "customeraccounts"."Customer_id", 
  "customeraccounts"."Account_id", 
  "customeraccounts"."AccountName", 
  "customeraccounts"."contractId", 
  "customeraccounts"."coreCustomerId", 
  "customeraccounts"."accountType"
) 
VALUES 
  (
    v_id, v__customerId, v_accountId, 
    v_accountName, v_contractId, v_coreCustomerId, 
    v_accountType
  );
END;
END IF;
END;
END IF;
END;
END LOOP;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE, SQLERRM);
END;
/
--  DDL for Procedure default_contractactions_create_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "default_contractactions_create_proc" (
  "_contractId" IN NVARCHAR2, "records1" OUT sys_refcursor, 
  "records2" OUT sys_refcursor, "records3" OUT sys_refcursor, 
  "records4" OUT sys_refcursor, "records5" OUT sys_refcursor, 
  "records6" OUT sys_refcursor, "records7" OUT sys_refcursor, 
  "records8" OUT sys_refcursor
) AS v_accountsCSV NVARCHAR2(2000) := '';
v_serviceDefinitionId NVARCHAR2(2000) := '';
v_serviceType NVARCHAR2(2000) := '';
v_validFIActions LONG := '';
v_validActionsList LONG := '';
v_group_concat_max_len NUMBER(19, 0):= 100000000;
BEGIN DECLARE v_finished INTEGER := 0;
v_featureActionId varchar(255):= '';
v_featureId varchar(255) := '';
v_actionslist CLOB := '';
v_limitId varchar(255) := '';
v_entryStatus INTEGER := 0;
v_accountId varchar(255) := '';
v_actualLimitId varchar(255) := '';
v_accountsCSV varchar(255) := '';
v_coreCustomerId varchar(255) := '';
v_limitvalue varchar(255) := '';
v_id varchar(255) := '';
CURSOR coreCustomers IS 
SELECT 
  "contractcorecustomers"."coreCustomerId" 
FROM 
  "contractcorecustomers" 
WHERE 
  "contractcorecustomers"."contractId" = "_contractId";
CURSOR accounts IS 
SELECT 
  "contractaccounts"."accountId" 
FROM 
  "contractaccounts" 
WHERE 
  "contractaccounts"."contractId" = "_contractId";
CURSOR transactionLimits IS 
select 
  "featureaction"."id" 
FROM 
  "featureaction" 
WHERE 
  FIND_IN_SET("id", v_validActionsList) <> 0 
  AND (
    "featureaction"."Type_id" = 'MONETARY' 
    AND "featureaction"."isAccountLevel" = '1'
  );
CURSOR accountLevelPermissions IS 
SELECT 
  "id" 
FROM 
  "featureaction" 
WHERE 
  INSTR("id", v_validActionsList)<> '0' 
  AND (
    "featureaction"."Type_id" = 'NON_MONETARY' 
    AND "featureaction"."isAccountLevel" = '1'
  );
CURSOR globalLevelPermissions IS 
SELECT 
  "id" 
FROM 
  "featureaction" 
WHERE 
  INSTR("id", v_validActionsList) <> '0' 
  AND (
    "featureaction"."Type_id" = 'NON_MONETARY' 
    AND "featureaction"."isAccountLevel" = '0'
  );
CURSOR limits IS 
select 
  "LimitType_id" 
FROM 
  "actionlimit" 
WHERE 
  "Action_id" = v_featureActionId;
BEGIN 
SELECT 
  listagg(
    CAST(
      "contractaccounts"."accountId" AS NVARCHAR2(2000)
    ), 
    ','
  ) INTO v_accountsCSV 
FROM 
  "contractaccounts" 
WHERE 
  "contractaccounts"."contractId" = "_contractId";
SELECT 
  "contract"."servicedefinitionId" INTO v_serviceDefinitionId 
FROM 
  "contract" 
WHERE 
  "id" = "_contractId";
SELECT 
  "servicedefinition"."serviceType" INTO v_serviceType 
FROM 
  "servicedefinition" 
WHERE 
  "id" = v_serviceDefinitionId;
SELECT 
  rtrim(
    xmlagg(
      XMLELEMENT(e, "featureaction"."id", ',').EXTRACT('//text()')
    ).GetClobVal(), 
    ','
  ) INTO v_validFIActions 
FROM 
  "featureaction" 
WHERE 
  "featureaction"."Feature_id" IN (
    SELECT 
      "contractfeatures"."featureId" 
    FROM 
      "contractfeatures" 
    WHERE 
      "contractfeatures"."contractId" = "_contractId"
  );
SELECT 
  rtrim(
    xmlagg(
      XMLELEMENT(
        e, "servicedefinitionactionlimit"."actionId", 
        ','
      ).EXTRACT('//text()')
    ).GetClobVal(), 
    ','
  ) INTO v_validActionsList 
FROM 
  "servicedefinitionactionlimit" 
WHERE 
  "serviceDefinitionId" = v_serviceDefinitionId 
  AND FIND_IN_SET("actionId", v_validFIActions)<> '0';
OPEN "records1" FOR 
SELECT 
  v_validActionsList 
from 
  dual;
OPEN coreCustomers;
FETCH coreCustomers INTO v_coreCustomerId;
<< loop_1 >> WHILE (coreCustomers % FOUND) LOOP BEGIN OPEN transactionLimits;
FETCH transactionLimits INTO v_featureActionId;
<< loop_2 >> WHILE (transactionLimits % FOUND) LOOP BEGIN OPEN "records2" FOR 
SELECT 
  v_featureActionId 
from 
  dual;
SELECT 
  "featureaction"."Feature_id" INTO v_featureId 
FROM 
  "featureaction" 
WHERE 
  "featureaction"."id" = v_featureActionId;
OPEN "records3" FOR 
SELECT 
  v_featureId 
from 
  dual;
IF v_finished = 1 THEN EXIT;
ELSE OPEN limits;
FETCH limits INTO v_limitId;
<< loop_3 >> WHILE (limits % FOUND) LOOP BEGIN IF v_finished = 1 THEN EXIT;
ELSE OPEN "records4" FOR 
SELECT 
  v_limitId 
from 
  dual;
SELECT 
  "servicedefinitionactionlimit"."value" INTO v_limitvalue 
FROM 
  "servicedefinitionactionlimit" 
WHERE 
  "servicedefinitionactionlimit"."actionId" = v_featureActionId 
  AND "servicedefinitionactionlimit"."limitTypeId" = v_limitId 
  AND "servicedefinitionactionlimit"."serviceDefinitionId" = v_serviceDefinitionId;
OPEN "records5" FOR 
select 
  v_limitvalue 
from 
  dual;
SELECT 
  SUBSTR(
    SYS_GUID(), 
    0, 
    50
  ) INTO v_id 
FROM 
  DUAL;
INSERT INTO "contractactionlimit"(
  "id", "contractId", "coreCustomerId", 
  "featureId", "actionId", "limitTypeId", 
  "value"
) 
VALUES 
  (
    v_id, "_contractId", v_coreCustomerId, 
    v_featureId, v_featureActionId, 
    v_limitId, v_limitvalue
  );
GOTO loop_3;
END IF;
v_finished := 0;
END;
END LOOP;
CLOSE limits;
GOTO loop_2;
END IF;
END;
END LOOP;
CLOSE transactionLimits;
v_finished := 0;
OPEN accounts;
FETCH accounts INTO v_accountId;
<< loop_4 >> WHILE (accounts % FOUND) LOOP BEGIN IF v_finished = 1 THEN EXIT;
ELSE OPEN accountLevelPermissions;
FETCH accountLevelPermissions INTO v_featureActionId;
<< loop_5 >> WHILE (accountLevelPermissions % FOUND) LOOP BEGIN OPEN "records6" FOR 
SELECT 
  v_featureActionId 
from 
  dual;
SELECT 
  "featureaction"."Feature_id" INTO v_featureId 
FROM 
  "featureaction" 
WHERE 
  "featureaction"."id" = v_featureActionId;
OPEN "records7" FOR 
SELECT 
  v_featureId 
from 
  dual;
IF v_finished = 1 THEN EXIT;
ELSE 
SELECT 
  SUBSTR(
    SYS_GUID(), 
    0, 
    50
  ) INTO v_id 
FROM 
  DUAL;
INSERT INTO "contractactionlimit"(
  "id", "contractId", "coreCustomerId", 
  "accountId", "featureId", "actionId"
) 
VALUES 
  (
    v_id, "_contractId", v_coreCustomerId, 
    v_accountId, v_featureId, v_featureActionId
  );
END IF;
GOTO loop_5;
END;
END LOOP;
CLOSE accountLevelPermissions;
v_finished := 0;
GOTO loop_4;
END IF;
END;
END LOOP;
CLOSE accounts;
v_finished := 0;
OPEN globalLevelPermissions;
FETCH globalLevelPermissions INTO v_featureActionId;
<< loop_6 >> WHILE (globalLevelPermissions % FOUND) LOOP BEGIN 
SELECT 
  "featureaction"."Feature_id" INTO v_featureId 
FROM 
  "featureaction" 
WHERE 
  "id" = v_featureActionId;
OPEN "records8" FOR 
SELECT 
  v_featureId 
from 
  dual;
IF v_finished = 1 THEN EXIT;
ELSE 
SELECT 
  SUBSTR(
    SYS_GUID(), 
    0, 
    50
  ) INTO v_id 
FROM 
  DUAL;
INSERT INTO "contractactionlimit"(
  "id", "contractId", "coreCustomerId", 
  "featureId", "actionId"
) 
VALUES 
  (
    v_id, "_contractId", v_coreCustomerId, 
    v_featureId, v_featureActionId
  );
END IF;
v_finished := 0;
GOTO loop_6;
END;
END LOOP;
CLOSE globalLevelPermissions;
GOTO loop_1;
END;
END LOOP;
CLOSE coreCustomers;
END;
END;
/
--  DDL for Procedure excluded_customeraction_save_proc


CREATE 
OR REPLACE NONEDITIONABLE PROCEDURE "excluded_customeraction_save_proc" ("_queryInput" IN VARCHAR2) AS v__queryInput VARCHAR2(4000) := "_queryInput";
v_recordRow VARCHAR2(4000);
v_recordsData VARCHAR2(4000);
v_query VARCHAR2(4000);
CURSOR actions IS 
SELECT 
  COLUMN_VALUE 
FROM 
  TABLE(
    UTILS.STRING_SPLIT(v__queryInput, '|')
  );
BEGIN --   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
v__queryInput := REPLACE(
  v__queryInput, 
  '\', ' ') ;
   v__queryInput := REPLACE(v__queryInput, ' "', '''') ;
   OPEN actions;
   FETCH actions INTO v_recordRow;
   <<loop_1>>
   WHILE ((actions%FOUND) = TRUE ) 
   LOOP 

      BEGIN
         v_recordsData := 'N''' || CAST(SYS_GUID() AS VARCHAR2) || ''',' || v_recordRow ;
         v_query := ('INSERT INTO "excludedcustomeraction"("id","RoleType_id","Customer_id","coreCustomerId","contractId","featureId","Action_id","Account_id","companyLegalUnit") VALUES (') || (v_recordsData) || (')') ;
         EXECUTE IMMEDIATE v_query;
         FETCH actions INTO v_recordRow;
         GOTO loop_1;

      END;
   END LOOP;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure excluded_customrole_actionlimits_create_proc



create or replace NONEDITIONABLE procedure "excluded_customrole_actionlimits_create_proc" 
(
  "_queryInput" IN VARCHAR2,
  "_customRoleId" IN NUMBER
)
AS
   v__queryInput VARCHAR2(4000) := "_queryInput";
   v_index NUMBER(10,0);
   v_numOfRecords NUMBER(10,0);
   v_recordsData VARCHAR2(4000);
   v_query VARCHAR2(4000);

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   /*DELETE "excludedcustomroleactionlimits"

    WHERE  "excludedcustomroleactionlimits"."customRole_id" = "_customRoleId";*/
   v_index := 0 ;
   SELECT REPLACE(v__queryInput, '"', '''') 

     INTO v__queryInput
     FROM DUAL ;
   v_numOfRecords := CASE 
                          WHEN v__queryInput IS NULL THEN 0
   ELSE LENGTH(v__queryInput) - LENGTH(REPLACE(v__queryInput, '|', '')) + 1
      END ;
   WHILE ( 1 = 1 ) 
   LOOP 

      BEGIN
         v_index := v_index + 1 ;
         IF ( v_index = v_numOfRecords + 1 ) THEN
          EXIT;
         ELSE

         BEGIN
            v_recordsData := UTILS.SUBSTRING_INDEX(UTILS.SUBSTRING_INDEX(v__queryInput, '|', v_index), '|', -1) ;
            v_query := ('INSERT INTO "excludedcustomroleactionlimits"("customRole_id","coreCustomerId","contractId","featureId","action_id","account_id",) VALUES (') || (v_recordsData) || (')') ;
            EXECUTE IMMEDIATE v_query;

         END;
         END IF;

      END;
   END LOOP;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;

/
--  DDL for Procedure fetch_bulkwire_files_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "fetch_bulkwire_files_proc" 
(
  v_createdBy IN VARCHAR2,
  iv_searchString IN VARCHAR2,
  iv_sortByParam IN VARCHAR2,
  iv_sortOrder IN VARCHAR2,
  v_pageOffset IN NUMBER,
  v_pageSize IN NUMBER
)
AS
   v_sortByParam VARCHAR2(50) := iv_sortByParam;
   v_sortOrder VARCHAR2(50) := iv_sortOrder;
   v_searchString VARCHAR2(50) := iv_searchString;
   v_companyId VARCHAR2(4000);
   v_isSMEUser NUMBER(1,0);
   v_filterRetail VARCHAR2(4000);
   v_filterSME VARCHAR2(4000);
   v_getByIdFilter VARCHAR2(4000);
   v_orderBy VARCHAR2(4000);
   v_paginationQuery VARCHAR2(4000);
   v_searchQuery VARCHAR2(4000);
   v_defaultFilter VARCHAR2(4000);
   v_searchFilter VARCHAR2(4000);
   v_filter VARCHAR2(4000);
   v_select_statement VARCHAR2(4000);

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   SELECT LISTAGG(CAST(("contractcustomers"."contractId"|| ' _ '|| "contractcustomers"."coreCustomerId") AS VARCHAR2(255)), ', 
    ') 

     INTO v_companyId
     FROM 
     "contractcustomers"
    WHERE  "contractcustomers"."customerId" = v_createdby;
   IF v_companyId IS NULL THEN
    v_companyId := ' ' ;
   END IF;
   SELECT (v_companyId|| ', 
    '|| "customer"."Organization_Id") 

     INTO v_companyId
     FROM "customer"
    WHERE  "customer"."id" = v_createdby;
   v_isSMEUser := CASE 
                       WHEN v_companyId = ''
                         OR v_companyId IS NULL THEN 0
   ELSE 1
      END ;
   v_filterRetail := (' "bulkwirefiles"."createdBy" = ') || (v_createdby) || (' 
    AND "bulkwirefiles"."softdeleteflag" = 0 ') ;
   v_filterSME := (' "bulkwirefiles"."company_id" IN (
      SELECT 
        column_value 
      from 
        TABLE(
          UTILS.STRING_SPLIT(''' || v_companyId|| ''')
        )
    ) ' || (' 
    AND "bulkwirefiles"."softdeleteflag" = 0 ')) ;
   v_getByIdFilter := CASE 
                           WHEN ( v_isSMEUser <> 0 ) THEN v_filterSME
   ELSE v_filterRetail
      END ;
   v_sortByParam := 0 ;
   v_sortByParam := CASE 
                         WHEN ( v_sortByParam = ' username ' ) THEN ' firstName, 
    lastname '
   ELSE v_sortByParam
      END ;
   v_sortOrder := CASE 
                       WHEN v_sortOrder = ''
                         OR v_sortOrder IS NULL THEN ' DESC '
   ELSE v_sortOrder
      END ;
   v_searchString := CASE 
                          WHEN v_searchString = ''
                            OR v_searchString IS NULL THEN ' '
   ELSE ' % ' || v_searchString || ' % '
      END ;
   v_orderBy := (' 
  ORDER BY 
    ') || (v_sortByParam) || (' ') || (v_sortOrder) ;
   v_paginationQuery := CASE 
                             WHEN ( v_pageOffset = ''
                               OR v_pageOffset IS NULL )
                               AND ( v_pageSize = ''
                               OR v_pageSize IS NULL ) THEN ' '
   ELSE ' OFFSET ' || v_pageOffset || ' ROWS FETCH NEXT ' || v_pageSize || ' ROWS ONLY '
      END ;
   v_searchQuery := ('(
      "bulkwirefiles"."bulkWireFileName" LIKE ') || (v_searchString) || (' 
      OR "bulkwirefiles"."noOfTransactions" LIKE ') || (v_searchString) || (' 
      OR "bulkwirefiles"."noOfDomesticTransactions" LIKE ') || (v_searchString) || (' 
      OR "bulkwirefiles"."noOfInternationalTransactions" LIKE ') || (v_searchString) || (' 
      OR "customer"."firstname" LIKE ') || (v_searchString) || (' 
      OR "customer"."lastname" LIKE ') || (v_searchString) || ('
    ) ') ;
   v_defaultFilter := (v_getByIdFilter) + (v_orderBy) + (v_paginationQuery) ;
   v_searchFilter := (v_getByIdFilter) || (' 
    AND ') || (v_searchQuery) + (v_orderBy) ;
   v_filter := CASE 
                    WHEN ( v_searchString = '' ) THEN v_defaultFilter
   ELSE v_searchFilter
      END ;
   v_select_statement := (' 
  SELECT 
    "bulkwirefiles"."bulkWireFileID", 
    "bulkwirefiles"."bulkWireFileName", 
    "bulkwirefiles"."noOfTransactions", 
    "bulkwirefiles"."noOfDomesticTransactions", 
    "bulkwirefiles"."noOfInternationalTransactions", 
    "bulkwirefiles"."createdts", 
    "bulkwirefiles"."lastmodifiedts", 
    "bulkwirefiles"."lastExecutedOn", 
    "customer".id, 
    "customer"."FirstName" as firstname, 
    "customer"."LastName" as lastname 
  FROM 
    (
      "bulkwirefiles" 
      LEFT JOIN "customer" ON (
        "bulkwirefiles"."createdBy" = "customer"."id"
      )
    ) 
  WHERE 
    ') || (v_filter) ;
   EXECUTE IMMEDIATE v_select_statement;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure fetch_bulkwire_files_transct_detail_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "fetch_bulkwire_files_transct_detail_proc" 
(
  v_bulkWireFileID IN VARCHAR2,
  iv_searchString IN VARCHAR2,
  iv_sortByParam IN VARCHAR2,
  iv_sortOrder IN VARCHAR2,
  v_pageOffset IN NUMBER,
  v_pageSize IN NUMBER
)
AS
   v_sortByParam VARCHAR2(50) := iv_sortByParam;
   v_sortOrder VARCHAR2(50) := iv_sortOrder;
   v_searchString VARCHAR2(50) := iv_searchString;
   v_qfilter VARCHAR2(4000);
   v_orderBy VARCHAR2(4000);
   v_paginationQuery VARCHAR2(4000);
   v_searchQuery VARCHAR2(4000);
   v_defaultFilter VARCHAR2(4000);
   v_searchFilter VARCHAR2(4000);
   v_filter VARCHAR2(4000);
   v_select_statement VARCHAR2(4000);

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   v_qfilter := (' "bulkwirefiletransactdetails"."bulkWireFileID" = ''') || (v_bulkWireFileID) || (''' 
    AND "bulkwirefiletransactdetails"."softdeleteflag" = 0 ') ;
   v_sortByParam := CASE 
                         WHEN v_sortByParam = ''
                           OR v_sortByParam IS NULL THEN ' transactionDate '
   ELSE v_sortByParam
      END ;
   v_sortByParam := CASE 
                         WHEN ( v_sortByParam = ' username ' ) THEN ' firstname, 
    lastname '
   ELSE v_sortByParam
      END ;
   v_sortOrder := CASE 
                       WHEN v_sortOrder = ''
                         OR v_sortOrder IS NULL THEN ' DESC '
   ELSE v_sortOrder
      END ;
   v_searchString := CASE 
                          WHEN v_searchString = ''
                            OR v_searchString IS NULL THEN ' '
   ELSE ' % ' || v_searchString || ' % '
      END ;
   v_orderBy := (' 
  ORDER BY 
    ') || (v_sortByParam) || (' ') || (v_sortOrder) ;
   v_paginationQuery := CASE 
                             WHEN ( v_pageOffset = ''
                               OR v_pageOffset IS NULL )
                               AND ( v_pageSize = ''
                               OR v_pageSize IS NULL ) THEN ' '
   ELSE ' OFFSET ' || CAST(v_pageOffset AS VARCHAR2) || ' ROWS FETCH NEXT ' || CAST(v_pageSize AS VARCHAR) || ' ROWS ONLY '
      END ;
   v_searchQuery := ('(
      "bulkwirefiletransactdetails"."transactionDate" LIKE ') || (v_searchString) || (' 
      OR "bulkwirefiletransactdetails"."totalCountOfTransactions" LIKE ') || (v_searchString) || (' 
      OR "customer"."firstname" LIKE ') || (v_searchString) || (' 
      OR "customer"."lastname" LIKE ') || (v_searchString) || ('
    ) ') ;
   v_defaultFilter := (v_qfilter) + (v_orderBy) + (v_paginationQuery) ;
   v_searchFilter := (v_qfilter) || (' 
    AND ') || (v_searchQuery) + (v_orderBy) ;
   v_filter := CASE 
                    WHEN ( v_searchString = '' ) THEN v_defaultFilter
   ELSE v_searchFilter
      END ;
   v_select_statement := (' 
  SELECT 
    "bulkwirefiletransactdetails"."bulkWireTransactionID", 
    "bulkwirefiletransactdetails"."bulkWireFileID", 
    "bulkwirefiletransactdetails"."initiatedBy", 
    "bulkwirefiletransactdetails"."createdts", 
    "bulkwirefiletransactdetails"."lastmodifiedts", 
    "bulkwirefiletransactdetails"."transactionDate", 
    "bulkwirefiletransactdetails"."totalCountOfTransactions", 
    "bulkwirefiletransactdetails"."totalCountOfDomesticTransactions", 
    "bulkwirefiletransactdetails"."totalCountOfInternationalTransactions", 
    "customer"."FirstName" as firstname, 
    "customer"."LastName" as lastname 
  FROM 
    (
      "bulkwirefiletransactdetails" 
      LEFT JOIN "customer" ON (
        "bulkwirefiletransactdetails"."initiatedBy" = "customer"."id"
      )
    ) 
  WHERE 
    ') || (v_filter) ;
   EXECUTE IMMEDIATE v_select_statement;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure fetch_bulkwire_template_transct_detail_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "fetch_bulkwire_template_transct_detail_proc" 
(
  v_bulkWireTemplateID IN VARCHAR2,
  iv_searchString IN VARCHAR2,
  iv_sortByParam IN VARCHAR2,
  iv_sortOrder IN VARCHAR2,
  v_pageOffset IN NUMBER,
  v_pageSize IN NUMBER,
  "bulkwiretemplatetransactdetails" OUT SYS_REFCURSOR
)
AS
   v_sortByParam VARCHAR2(50) := iv_sortByParam;
   v_sortOrder VARCHAR2(50) := iv_sortOrder;
   v_searchString VARCHAR2(50) := iv_searchString;
   v_qfilter VARCHAR2(4000);
   v_orderBy VARCHAR2(4000);
   v_paginationQuery VARCHAR2(4000);
   v_searchQuery VARCHAR2(4000);
   v_defaultFilter VARCHAR2(4000);
   v_searchFilter VARCHAR2(4000);
   v_filter VARCHAR2(4000);
   v_select_statement VARCHAR2(4000);


BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   v_qfilter := ' "bulkwiretemplatetransactdetails"."bulkWireTemplateID" = ''' || v_bulkWireTemplateID || ''' 
    AND "bulkwiretemplatetransactdetails".softdeleteflag = 0 ' ;
   v_sortByParam := CASE 
                         WHEN ( v_sortByParam = ''
                           OR v_sortByParam IS NULL ) THEN ' transactionDate '
   ELSE v_sortByParam
      END ;
   v_sortByParam := CASE 
                         WHEN ( v_sortByParam = ' username ' ) THEN ' firstname, 
    lastname '
   ELSE v_sortByParam
      END ;
   v_sortOrder := CASE 
                       WHEN ( v_sortOrder = ''
                         OR v_sortOrder IS NULL ) THEN ' DESC '
   ELSE v_sortOrder
      END ;
   v_searchString := CASE 
                          WHEN ( v_searchString = ''
                            OR v_searchString IS NULL ) THEN ' '
   ELSE ''' % ' || v_searchString || ' % '''
      END ;
   v_orderBy := (' 
  ORDER BY 
    ' || v_sortByParam || ' ' || v_sortOrder) ;
   v_paginationQuery := CASE 
                             WHEN ( v_pageOffset IS NOT NULL
                               OR v_pageOffset != ''
                               AND v_pageSize IS NOT NULL
                               OR v_pageSize != '' ) THEN ' offset ' || CAST(v_pageOffset AS VARCHAR2) || ' rows fetch next ' || CAST(v_pageSize AS VARCHAR2) || ' rows only '
   ELSE ' '
      END ;
   v_searchQuery := '(
      "bulkwiretemplatetransactdetails"."transactionDate" LIKE ' || v_searchString || ' 
      OR "bulkwiretemplatetransactdetails"."totalCountOfTransactions" LIKE ' || v_searchString || ' 
      OR "customer"."firstname" LIKE ' || v_searchString || ' 
      OR "customer"."lastname" LIKE ' || v_searchString || '
    ) ' ;
   v_defaultFilter := v_qfilter + v_orderBy + v_paginationQuery ;
   v_searchFilter := v_qfilter || ' 
    AND ' || v_searchQuery + v_orderBy ;
   v_filter := CASE 
                    WHEN v_searchString = '' THEN v_defaultFilter
   ELSE v_searchFilter
      END ;
   v_select_statement := ' 
  SELECT 
    "bulkwiretemplatetransactdetails"."bulkWireTransactionID", 
    "bulkwiretemplatetransactdetails"."bulkWireTemplateID", 
    "bulkwiretemplatetransactdetails"."initiatedBy", 
    "bulkwiretemplatetransactdetails"."createdts", 
    "bulkwiretemplatetransactdetails"."lastmodifiedts", 
    "bulkwiretemplatetransactdetails"."transactionDate", 
    "bulkwiretemplatetransactdetails"."totalCountOfTransactions", 
    "bulkwiretemplatetransactdetails"."totalCountOfDomesticTransactions", 
    "bulkwiretemplatetransactdetails"."totalCountOfInternationalTransactions", 
    "customer"."FirstName" as firstname, 
    "customer"."LastName" as lastname 
  FROM 
    (
      "bulkwiretemplatetransactdetails" 
      LEFT JOIN "customer" ON (
        "bulkwiretemplatetransactdetails"."initiatedBy" = "customer"."id"
      )
    ) 
  WHERE 
    ' || v_filter ;
   OPEN "bulkwiretemplatetransactdetails" FOR
   v_select_statement;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure fetch_bulkwires_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "fetch_bulkwires_proc" 
(
  v_createdBy IN VARCHAR2,
  iv_searchString IN VARCHAR2,
  iv_sortByParam IN VARCHAR2,
  iv_sortOrder IN VARCHAR2,
  v_pageOffset IN NUMBER,
  v_pageSize IN NUMBER,
  v_cursor OUT SYS_REFCURSOR,
  /*
     *   SSMA informational messages:
     *   M2SS0055: Data type was converted to VARCHAR according to character set mapping for latin1 character set
     */
  v_bulkWireCategoryFilter IN VARCHAR2,
  v_fileDomesticView IN NUMBER,
  v_fileInternationalView IN NUMBER,
  v_templateDomesticView IN NUMBER,
  v_templateInternationalView IN NUMBER
)
AS
   v_sortByParam VARCHAR2(50) := iv_sortByParam;
   v_sortOrder VARCHAR2(50) := iv_sortOrder;
   v_searchString VARCHAR2(50) := iv_searchString;
   v_companyId VARCHAR2(4000);
   v_isSMEUser NUMBER(10,0);
   v_remove0DomesticFile VARCHAR2(4000);
   v_remove0InternationalFile VARCHAR2(4000);
   v_remove0DomesticTemplate VARCHAR2(4000);
   v_remove0InternationalTemplate VARCHAR2(4000);
   v_fileAddQuery VARCHAR2(4000);
   v_fileAdd VARCHAR2(4000);
   v_templateAddQuery VARCHAR2(4000);
   v_templateAdd VARCHAR2(4000);
   v_filterRetailFile VARCHAR2(4000);
   v_filterSMEFile VARCHAR2(4000);
   v_filterRetailTemplate VARCHAR2(4000);
   v_filterSMETemplate VARCHAR2(4000);
   v_getByIdFilterFile VARCHAR2(4000);
   v_getByIdFilterTemplate VARCHAR2(4000);
   v_searchQueryTemplate VARCHAR2(4000);
   v_searchQueryFile VARCHAR2(4000);
   v_orderBy VARCHAR2(4000);
   v_onlyFileWithoutSearch VARCHAR2(4000);
   v_searchFilterFile VARCHAR2(4000);
   v_paginationQuery VARCHAR2(4000);
   v_filterFile VARCHAR2(4000);
   v_OnlyFile VARCHAR2(4000);
   v_finalFile VARCHAR2(4000);
   v_defaultFilterTemplate VARCHAR2(4000);
   v_searchFilterTemplate VARCHAR2(4000);
   v_filterTemplate VARCHAR2(4000);
   v_select_files VARCHAR2(4000);
   v_select_templates VARCHAR2(4000);
   v_select_statement VARCHAR2(4000);

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   SELECT "customer"."Organization_Id" 

     INTO v_companyId
     FROM "customer"
    WHERE  "customer"."id" = v_createdby;
   v_isSMEUser := 0 ;
   IF ( v_companyId IS NOT NULL
     OR v_companyId != '' ) THEN
    v_isSMEUser := 1 ;
   END IF;
   v_remove0DomesticFile := (' 
    AND "bulkwirefiles"."noOfDomesticTransactions" <> 0 ') ;
   v_remove0InternationalFile := (' 
    AND "bulkwirefiles"."noOfInternationalTransactions" <> 0 ') ;
   v_remove0DomesticTemplate := (' 
    AND "bulkwiretemplate"."noOfDomesticTransactions" <> 0 ') ;
   v_remove0InternationalTemplate := (' 
    AND "bulkwiretemplate"."noOfInternationalTransactions" <> 0 ') ;
   v_fileAddQuery := CASE 
                          WHEN ( v_fileDomesticView <> 0 ) THEN v_remove0DomesticFile
   ELSE v_remove0InternationalFile
      END ;
   v_fileAdd := CASE 
                     WHEN ( v_fileDomesticView <> 0
                       AND v_fileInternationalView <> 0 ) THEN ''
   ELSE v_fileAddQuery
      END ;
   v_templateAddQuery := CASE 
                              WHEN ( v_templateDomesticView <> 0 ) THEN v_remove0DomesticTemplate
   ELSE v_remove0InternationalTemplate
      END ;
   v_templateAdd := CASE 
                         WHEN ( v_templateDomesticView <> 0
                           AND v_templateInternationalView <> 0 ) THEN ''
   ELSE v_templateAddQuery
      END ;
   v_filterRetailFile := (' "bulkwirefiles"."createdBy" = ''') || (v_createdby) || (''' 
    AND "bulkwirefiles"."softdeleteflag" = 0 ') || (v_fileAdd) ;
   v_filterSMEFile := (' "bulkwirefiles"."company_id" = ''') || (v_companyId) || (''' 
    AND "bulkwirefiles"."softdeleteflag" = 0 ') || (v_fileAdd) ;
   v_filterRetailTemplate := (' "bulkwiretemplate"."createdBy" = ''') || (v_createdby) || (''' 
    AND "bulkwiretemplate"."softdeleteflag" = 0 ') || (v_templateAdd) ;
   v_filterSMETemplate := (' "bulkwiretemplate"."company_id" = ''') || (v_companyId) || (''' 
    AND "bulkwiretemplate"."softdeleteflag" = 0 ') || (v_templateAdd) ;
   v_getByIdFilterFile := CASE 
                               WHEN ( v_isSMEUser <> 0 ) THEN v_filterSMEFile
   ELSE v_filterRetailFile
      END ;
   v_getByIdFilterTemplate := CASE 
                                   WHEN ( v_isSMEUser <> 0 ) THEN v_filterSMETemplate
   ELSE v_filterRetailTemplate
      END ;
   v_sortByParam := CASE 
                         WHEN ( v_sortByParam = ''
                           OR v_sortByParam IS NULL ) THEN ' createdts '
   ELSE v_sortByParam
      END ;
   v_sortOrder := CASE 
                       WHEN ( v_sortOrder = ''
                         OR v_sortOrder IS NULL ) THEN ' DESC '
   ELSE v_sortOrder
      END ;
   v_sortByParam := CASE 
                         WHEN ( v_sortByParam = ' username ' ) THEN ' firstName ' || v_sortOrder || ', 
    lastname '
   ELSE v_sortByParam
      END ;
   v_searchString := CASE 
                          WHEN ( v_searchString = ''
                            OR v_searchString IS NULL ) THEN ''
   ELSE ''' % ' || v_searchString || ' % '''
      END ;
   v_orderBy := (' 
  ORDER BY 
    ') || (v_sortByParam) || (' ') || (v_sortOrder) ;
   v_paginationQuery := CASE 
                             WHEN ( ( v_pageOffset IS NOT NULL
                               OR v_pageOffset != '' )
                               AND ( v_pageSize IS NOT NULL
                               OR v_pageSize != '' ) ) THEN ' OFFSET ' || CAST(v_pageOffset AS VARCHAR2) || ' ROWS FETCH NEXT ' || CAST(v_pageSize AS VARCHAR2) || ' ROWS ONLY '
   ELSE ''
      END ;
   v_searchQueryFile := ('(
      "bulkwirefiles"."bulkWireFileName" LIKE ') || (v_searchString) || (' 
      OR "bulkwirefiles"."noOfTransactions" LIKE ') || (v_searchString) || (' 
      OR "bulkwirefiles"."noOfDomesticTransactions" LIKE ') || (v_searchString) || (' 
      OR "bulkwirefiles"."noOfInternationalTransactions" LIKE ') || (v_searchString) || (' 
      OR "customer"."firstname" LIKE ') || (v_searchString) || (' 
      OR "customer"."lastname" LIKE ') || (v_searchString) || ('
    ) ') ;
   v_searchQueryTemplate := ('(
      "bulkwiretemplate"."bulkWireTemplateName" LIKE ') || (v_searchString) || (' 
      OR "bulkwiretemplate"."noOfTransactions" LIKE ') || (v_searchString) || (' 
      OR "bulkwiretemplate"."noOfDomesticTransactions" LIKE ') || (v_searchString) || (' 
      OR "bulkwiretemplate"."noOfInternationalTransactions" LIKE ') || (v_searchString) || (' 
      OR "customer"."firstname" LIKE ') || (v_searchString) || (' 
      OR "customer"."lastname" LIKE ') || (v_searchString) || ('
    ) ') ;
   v_onlyFileWithoutSearch := (v_getByIdFilterFile) + (v_orderBy) + (v_paginationQuery) ;
   v_searchFilterFile := (v_getByIdFilterFile) || (' 
    AND ') || (v_searchQueryFile) ;
   v_filterFile := CASE 
                        WHEN ( v_searchString = '' ) THEN v_getByIdFilterFile
   ELSE v_searchFilterFile
      END ;
   v_OnlyFile := CASE 
                      WHEN ( v_searchString = '' ) THEN v_onlyFileWithoutSearch
   ELSE v_searchFilterFile
      END ;
   v_finalFile := CASE 
                       WHEN ( v_bulkWireCategoryFilter = ' Files ' ) THEN v_OnlyFile
   ELSE v_filterFile
      END ;
   v_defaultFilterTemplate := (v_getByIdFilterTemplate) + (v_orderBy) + (v_paginationQuery) ;
   v_searchFilterTemplate := (v_getByIdFilterTemplate) || (' 
    AND ') || (v_searchQueryTemplate) + (v_orderBy) ;
   v_filterTemplate := CASE 
                            WHEN ( v_searchString = '' ) THEN v_defaultFilterTemplate
   ELSE v_searchFilterTemplate
      END ;
   v_select_files := (' 
  SELECT 
    "bulkwirefiles"."bulkWireFileID" as bulkWireID, 
    "bulkwirefiles"."bulkWireFileName" as bulkWireName, 
    "bulkwirefiles"."noOfTransactions", 
    "bulkwirefiles"."noOfDomesticTransactions", 
    "bulkwirefiles"."noOfInternationalTransactions", 
    "bulkwirefiles"."createdts", 
    "bulkwirefiles"."lastmodifiedts", 
    "bulkwirefiles"."lastExecutedOn", 
    NULL as defaultFromAccount, 
    NULL as defaultCurrency, 
    '' Files '' as bulkWireCategory, 
    "customer"."FirstName" as firstname, 
    "customer"."LastName" as lastname 
  FROM 
    (
      "bulkwirefiles" 
      LEFT JOIN "customer" ON (
        "bulkwirefiles"."createdBy" = "customer"."id"
      )
    ) 
  WHERE 
    ') || (v_finalFile) ;
   v_select_templates := (' 
  SELECT 
    "bulkwiretemplate"."bulkWireTemplateID" as bulkWireID, 
    "bulkwiretemplate"."bulkWireTemplateName" as bulkWireName, 
    "bulkwiretemplate"."noOfTransactions", 
    "bulkwiretemplate"."noOfDomesticTransactions", 
    "bulkwiretemplate"."noOfInternationalTransactions", 
    "bulkwiretemplate"."createdts", 
    "bulkwiretemplate"."lastmodifiedts", 
    "bulkwiretemplate"."lastExecutedOn", 
    "bulkwiretemplate"."defaultFromAccount", 
    "bulkwiretemplate"."defaultCurrency", 
    '' Templates '' as bulkWireCategory, 
    "customer"."FirstName" as firstname, 
    "customer"."LastName" as lastname 
  FROM 
    (
      "bulkwiretemplate" 
      LEFT JOIN "customer" ON (
        "bulkwiretemplate"."createdBy" = "customer"."id"
      )
    ) 
  WHERE 
    ') || (v_filterTemplate) ;
   IF v_bulkWireCategoryFilter = ' Files ' THEN
    v_select_statement := v_select_files ;
   ELSE
      IF v_bulkWireCategoryFilter = ' Templates ' THEN
       v_select_statement := v_select_templates ;
      ELSE
         v_select_statement := (v_select_files) || (' 
  UNION ALL 
    ') || (v_select_templates) ;
      END IF;
   END IF;
   EXECUTE IMMEDIATE v_select_statement;
    OPEN v_cursor FOR v_select_statement;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure fetch_bulkwiretemplatelineitems_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "fetch_bulkwiretemplatelineitems_proc" 
(
  v_bulkWireTemplateID IN VARCHAR2,
  iv_searchString IN VARCHAR2,
  iv_sortByParam IN VARCHAR2,
  iv_sortOrder IN VARCHAR2,
  v_groupBy IN VARCHAR2,
  "bulkwiretemplatelineitems" OUT SYS_REFCURSOR
)
AS
   v_sortByParam VARCHAR2(50) := iv_sortByParam;
   v_sortOrder VARCHAR2(50) := iv_sortOrder;
   v_searchString VARCHAR2(50) := iv_searchString;
   v_lineItemId VARCHAR2(50);
   v_payeeId VARCHAR2(50);
   v_swiftCodeVal VARCHAR2(50);
   v_intRoutingNumVal VARCHAR2(50);
   v_recAccntnumVal VARCHAR2(50);
   v_routingNumVal VARCHAR2(50);
   v_recNicknameVal VARCHAR2(50);
   v_recNameVal VARCHAR2(100);
   v_recAddLine1Val VARCHAR2(100);
   v_recAddLine2Val VARCHAR2(100);
   v_recCityVal VARCHAR2(100);
   v_recStateVal VARCHAR2(100);
   v_recCountryVal VARCHAR2(100);
   v_recBankNameVal VARCHAR2(100);
   v_recBankAdd1Val VARCHAR2(100);
   v_recBankAdd2Val VARCHAR2(100);
   v_recBankCityVal VARCHAR2(100);
   v_recBankStateVal VARCHAR2(100);
   v_recZipVal VARCHAR2(20);
   v_recBankZipVal VARCHAR2(20);
   v_isPayeeDeleted NUMBER(10,0) := 1;
   v_finished NUMBER(10,0) := 0;
   v_queryFilter VARCHAR2(4000);
   v_orderByWithGrouping VARCHAR2(4000);
   v_orderByWithoutGrouping VARCHAR2(4000);
   v_orderBy VARCHAR2(4000);
   v_searchQuery VARCHAR2(4000);
   v_defaultFilter VARCHAR2(4000);
   v_searchFilter VARCHAR2(4000);
   v_filter VARCHAR2(4000);
   v_select_statement VARCHAR2(4000);
   CURSOR curTemplateLineItem
     IS SELECT "bulkWireTemplateLineItemID" 
     FROM "bulkwiretemplatelineitems" 
    WHERE  "bulkWireTemplateID" = v_bulkWireTemplateID
     AND "softdeleteflag" = 0
     AND "templateRecipientCategory" = ' EXISTINGRECIPIENT ';

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   OPEN curTemplateLineItem;
   FETCH curTemplateLineItem INTO v_lineItemId;
   WHILE (curTemplateLineItem%FOUND) = TRUE 
   LOOP 

      BEGIN
         SELECT "bulkwiretemplatelineitems"."payeeId" 

           INTO v_payeeId
           FROM "bulkwiretemplatelineitems"
          WHERE  "bulkwiretemplatelineitems"."bulkWireTemplateLineItemID" = v_lineItemId;
         SELECT "payee"."softDelete" 

           INTO v_isPayeeDeleted
           FROM "payee" 
          WHERE  "payee"."Id" = v_payeeId;
         IF ( v_isPayeeDeleted = 0 ) THEN

         BEGIN
            SELECT "payee"."swiftCode" 

              INTO v_swiftCodeVal
              FROM "payee" 
             WHERE  "payee"."Id" = v_payeeId;
            SELECT "payee"."internationalRoutingCode" 

              INTO v_intRoutingNumVal
              FROM "payee"
             WHERE  "payee"."Id" = v_payeeId;
            SELECT "payee"."name" 

              INTO v_recNameVal
              FROM "payee" 
             WHERE  "payee"."Id" = v_payeeId;
            SELECT "payee"."addressLine1" 

              INTO v_recAddLine1Val
              FROM "payee" 
             WHERE  "payee"."Id" = v_payeeId;
            SELECT "payee"."addressLine2" 

              INTO v_recAddLine2Val
              FROM "payee"
             WHERE  "payee"."Id" = v_payeeId;
            SELECT "payee"."cityName" 

              INTO v_recCityVal
              FROM "payee"
             WHERE  "payee"."Id" = v_payeeId;
            SELECT "payee"."state" 

              INTO v_recStateVal
              FROM "payee"
             WHERE  "payee"."Id" = v_payeeId;
            SELECT "payee"."country" 

              INTO v_recCountryVal
              FROM "payee" 
             WHERE  "payee"."Id" = v_payeeId;
            SELECT "payee"."zipCode" 

              INTO v_recZipVal
              FROM "payee" 
             WHERE  "payee"."Id" = v_payeeId;
            SELECT "payee"."bankName" 

              INTO v_recBankNameVal
              FROM "payee" 
             WHERE  "payee"."Id" = v_payeeId;
            SELECT "payee"."bankAddressLine1" 

              INTO v_recBankAdd1Val
              FROM "payee" 
             WHERE  "payee"."Id" = v_payeeId;
            SELECT "payee"."bankAddressLine2" 

              INTO v_recBankAdd2Val
              FROM "payee" 
             WHERE  "payee"."Id" = v_payeeId;
            SELECT "payee"."bankZip" 

              INTO v_recBankZipVal
              FROM "payee" 
             WHERE  "payee"."Id" = v_payeeId;
            SELECT "payee"."bankCity" 

              INTO v_recBankCityVal
              FROM "payee" 
             WHERE  "payee"."Id" = v_payeeId;
            SELECT "payee"."bankState" 

              INTO v_recBankStateVal
              FROM "payee" 
             WHERE  "payee"."Id" = v_payeeId;
            SELECT "payee"."nickName" 

              INTO v_recNicknameVal
              FROM "payee" 
             WHERE  "payee"."Id" = v_payeeId;
            SELECT "payee"."accountNumber" 

              INTO v_recAccntnumVal
              FROM "payee" 
             WHERE  "payee"."Id" = v_payeeId;
            SELECT "payee"."routingCode" 

              INTO v_routingNumVal
              FROM "payee" 
             WHERE  "payee"."Id" = v_payeeId;
            UPDATE "bulkwiretemplatelineitems"
               SET "swiftCode" = v_swiftCodeVal,
                   "internationalRoutingNumber" = v_intRoutingNumVal,
                   "recipientName" = v_recNameVal,
                   "recipientAddressLine1" = v_recAddLine1Val,
                   "recipientAddressLine2" = v_recAddLine2Val,
                   "recipientCity" = v_recCityVal,
                   "recipientState" = v_recStateVal,
                   "recipientCountryName" = v_recCountryVal,
                   "recipientZipCode" = v_recZipVal,
                   "recipientBankName" = v_recBankNameVal,
                   "recipientBankAddress1" = v_recBankAdd1Val,
                   "recipientBankAddress2" = v_recBankAdd2Val,
                   "recipientBankZipCode" = v_recBankZipVal,
                   "recipientBankcity" = v_recBankCityVal,
                   "recipientBankstate" = v_recBankStateVal,
                   "accountNickname" = v_recNicknameVal,
                   "recipientAccountNumber" = v_recAccntnumVal,
                   "routingNumber" = v_routingNumVal
             WHERE  "bulkWireTemplateLineItemID" = v_lineItemId;

         END;
         ELSE
            UPDATE "bulkwiretemplatelineitems"
               SET "softdeleteflag" = 1
             WHERE  "bulkWireTemplateLineItemID" = v_lineItemId;
         END IF;
         FETCH curTemplateLineItem INTO v_lineItemId;

      END;
   END LOOP;
   CLOSE curTemplateLineItem;
   v_queryFilter := ' "bulkwiretemplatelineitems"."bulkWireTemplateID" = ''' || v_bulkWireTemplateID || ''' 
    AND "bulkwiretemplatelineitems"."softdeleteflag" = 0 ' ;
   v_sortByParam := CASE 
                         WHEN v_sortByParam = ''
                           OR v_sortByParam IS NULL THEN ' recipientName '
   ELSE v_sortByParam
      END ;
   v_sortOrder := CASE 
                       WHEN v_sortOrder = ''
                         OR v_sortOrder IS NULL THEN ' ASC '
   ELSE v_sortOrder
      END ;
   v_searchString := CASE 
                          WHEN v_searchString = ''
                            OR v_searchString IS NULL THEN ' '
   ELSE ''' % ' || v_searchString || ' % '''
      END ;
   v_orderByWithGrouping := ' 
  ORDER BY 
    ' || v_groupBy || ', 
    ' || v_sortByParam || ' ' || v_sortOrder ;
   v_orderByWithoutGrouping := ' 
  ORDER BY 
    ' || v_sortByParam || ' ' || v_sortOrder ;
   v_orderBy := CASE 
                     WHEN ( v_groupBy = ''
                       OR v_groupBy IS NULL ) THEN v_orderByWithoutGrouping
   ELSE v_orderByWithGrouping
      END ;
   v_searchQuery := '(
      "bulkwiretemplatelineitems"."recipientName" LIKE ' || v_searchString || ' 
      OR "bulkwiretemplatelineitems"."swiftcode" LIKE ' || v_searchString || ' 
      OR "bulkwiretemplatelineitems"."recipientAccountNumber" LIKE ' || v_searchString || ' 
      OR "bulkwiretemplatelineitems"."routingNumber" LIKE ' || v_searchString || ' 
      OR "bulkwiretemplatelineitems"."internationalRoutingNumber" LIKE ' || v_searchString || ' 
      OR "bulkwiretemplatelineitems"."bulkWireTransferType" LIKE ' || v_searchString || ' 
      OR "bulkwiretemplatelineitems"."transactionType" LIKE ' || v_searchString || ' 
      OR "bulkwiretemplatelineitems"."recipientAddressLine1" LIKE ' || v_searchString || ' 
      OR "bulkwiretemplatelineitems"."recipientAddressLine2" LIKE ' || v_searchString || ' 
      OR "bulkwiretemplatelineitems"."recipientCity" LIKE ' || v_searchString || ' 
      OR "bulkwiretemplatelineitems"."recipientState" LIKE ' || v_searchString || ' 
      OR "bulkwiretemplatelineitems"."recipientZipCode" LIKE ' || v_searchString || ' 
      OR "bulkwiretemplatelineitems"."recipientBankName" LIKE ' || v_searchString || ' 
      OR "bulkwiretemplatelineitems"."recipientBankAddress1" LIKE ' || v_searchString || ' 
      OR "bulkwiretemplatelineitems"."recipientBankAddress2" LIKE ' || v_searchString || ' 
      OR "bulkwiretemplatelineitems"."recipientBankcity" LIKE ' || v_searchString || ' 
      OR "bulkwiretemplatelineitems"."recipientBankZipCode" LIKE ' || v_searchString || ' 
      OR "bulkwiretemplatelineitems"."recipientBankstate" LIKE ' || v_searchString || '
    ) ' ;
   v_defaultFilter := v_queryFilter + v_orderBy ;
   v_searchFilter := v_queryFilter || ' 
    AND ' || v_searchQuery + v_orderBy ;
   v_filter := CASE 
                    WHEN v_searchString = '' THEN v_defaultFilter
   ELSE v_searchFilter
      END ;
   v_select_statement := (' 
  SELECT 
    "bulkwiretemplatelineitems"."bulkWireTemplateLineItemID", 
    "bulkwiretemplatelineitems"."swiftCode", 
    "bulkwiretemplatelineitems"."recipientCountryName", 
    "bulkwiretemplatelineitems"."recipientName", 
    "bulkwiretemplatelineitems"."bulkWireTransferType", 
    "bulkwiretemplatelineitems"."transactionType", 
    "bulkwiretemplatelineitems"."internationalRoutingNumber", 
    "bulkwiretemplatelineitems"."recipientAddressLine1", 
    "bulkwiretemplatelineitems"."recipientAddressLine2", 
    "bulkwiretemplatelineitems"."recipientCity", 
    "bulkwiretemplatelineitems"."recipientState", 
    "bulkwiretemplatelineitems"."recipientZipCode", 
    "bulkwiretemplatelineitems"."recipientBankName", 
    "bulkwiretemplatelineitems"."recipientBankAddress1", 
    "bulkwiretemplatelineitems"."recipientBankAddress2", 
    "bulkwiretemplatelineitems"."recipientBankZipCode", 
    "bulkwiretemplatelineitems"."recipientBankcity", 
    "bulkwiretemplatelineitems"."recipientBankstate", 
    "bulkwiretemplatelineitems"."recipientAccountNumber", 
    "bulkwiretemplatelineitems"."routingNumber", 
    "bulkwiretemplatelineitems"."templateRecipientCategory", 
    "bulkwiretemplatelineitems"."accountNickname", 
    "bulkwiretemplatelineitems"."payeeId" 
  FROM 
    "bulkwiretemplatelineitems" 
  WHERE 
    ' || v_filter) ;
   EXECUTE IMMEDIATE v_select_statement;
    OPEN "bulkwiretemplatelineitems" FOR v_select_statement;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure fetch_bulkWireTemplateTransactionsExecution_details_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "fetch_bulkWireTemplateTransactionsExecution_details_proc" 
(
  "BULKWIRETEMPLATEEXECUTION_ID" IN NVARCHAR2,
  "SEARCHSTRING" IN NVARCHAR2,
  "STATUSFILTER" IN NVARCHAR2,
  "SORTBYPARAM" IN NVARCHAR2,
  "SORTORDER" IN NVARCHAR2,
  "ISDOMESTICPERMITTED" IN NUMBER,
  "ISINTERNATIONALPERMITTED" IN NUMBER,
  "records" OUT SYS_REFCURSOR
)
AS
   iv_sortByParam NVARCHAR2(50) := "SORTBYPARAM";
   iv_sortOrder NVARCHAR2(50) := "SORTORDER";
   iv_searchString NVARCHAR2(50) := "SEARCHSTRING";
   v_query1 NVARCHAR2(2000);
   v_query2 NVARCHAR2(2000);
   v_query3 NVARCHAR2(2000);
   v_orderBy NVARCHAR2(2000);
   v_searchQuery long;
   v_statusQuery NVARCHAR2(2000);
   v_finalQueryFilter NVARCHAR2(2000);
   v_defaultFilter NVARCHAR2(2000);
   v_searchFilter long;
   v_filter long;
   v_DomQuery NVARCHAR2(2000);
   v_DomCount NUMBER(10,0);
   v_InternationalQuery NVARCHAR2(2000);
   v_InternationalCount NUMBER(10,0);
   v_select_statement long;
   v_groupByCnt NVARCHAR2(2000);
   

BEGIN

   
   v_query1 := (' "wiretransfers"."wireTemplateExecution_id" = ''') || ("BULKWIRETEMPLATEEXECUTION_ID") || (''' 
    AND "wiretransfers"."softdeleteflag" = 0 ') ;
   v_query2 := (' "wiretransfers"."wireTemplateExecution_id" = ''') || ("BULKWIRETEMPLATEEXECUTION_ID") || (''' 
    AND "wiretransfers"."status" = ''') || ("STATUSFILTER") || (''' 
    AND "wiretransfers"."softdeleteflag" = 0 ') ;
   v_query3 := (' "wiretransfers"."wireTemplateExecution_id" = ''') || ("BULKWIRETEMPLATEEXECUTION_ID") || (''' 
    AND "wiretransfers"."status" in ('' Failed '', '' Denied '') ') || (' 
    AND "wiretransfers"."softdeleteflag" = 0 ') ;
   iv_sortByParam := CASE 
                         WHEN iv_sortByParam = ' '
                           OR iv_sortByParam IS NULL THEN ' "transactionId" '
   ELSE iv_sortByParam
      END ;
   iv_sortByParam := CASE 
                         WHEN iv_sortByParam = ' "payeeName" ' THEN ' "wiretransfers"."payeeName" '
   ELSE iv_sortByParam
      END ;
   iv_sortOrder := CASE 
                       WHEN ( iv_sortOrder = ' '
                         OR iv_sortOrder IS NULL ) THEN ' ASC '
   ELSE iv_sortOrder
      END ;
   iv_searchString := CASE 
                          WHEN ( iv_searchString = ' '
                            OR iv_searchString IS NULL ) THEN ' '
   ELSE (''' % ' || iv_searchString || ' % ''')
      END ;
   v_groupByCnt := ' 
  GROUP BY 
    "wiretransfers"."transactionId" ' ;
   v_orderBy := (' 
  ORDER BY 
    ') || (iv_sortByParam) || (' ') || (iv_sortOrder) ;
   v_searchQuery := ('(
      "wiretransfers"."amount" LIKE ') || (iv_searchString) || (' 
      OR "wiretransfers"."notes" LIKE ') || (iv_searchString) || (' 
      OR "wiretransfers"."fromAccountNumber" LIKE ') || (iv_searchString) 
   || (' 
      OR "wiretransfers"."payeeAccountNumber" LIKE ') || (iv_searchString) || (' 
      OR "wiretransfers"."transactionType" LIKE ') || (iv_searchString) ||
    (' 
      OR "onetimepayee"."payeeName" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."payeeType" LIKE ') || (iv_searchString) 
   || (' 
      OR "onetimepayee"."payeeAddressLine1" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."payeeAddressLine2" LIKE ') || (iv_searchString) 
   || (' 
      OR "onetimepayee"."cityName" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."state" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."zipCode" LIKE ') || (iv_searchString) ||
   (' 
      OR "onetimepayee"."bankName" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."bankAddressLine1" LIKE ') || (iv_searchString)
    || (' 
      OR "onetimepayee"."bankAddressLine2" LIKE ') || (iv_searchString) 
   || (' 
      OR "onetimepayee"."bankZip" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."bankState" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."bankCity" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."routingNumber" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."internationalRoutingCode" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."swiftCode" LIKE ') || (iv_searchString) || ('
    ) ') ;
   v_statusQuery := CASE 
                         WHEN ( "STATUSFILTER" = ' Failed ' ) THEN v_query3
   ELSE v_query2
      END ;
   v_finalQueryFilter := CASE 
                              WHEN ( "STATUSFILTER" = ' ' ) THEN v_query1
   ELSE v_statusQuery
      END ;
   v_defaultFilter := (v_finalQueryFilter) ;
   v_searchFilter := (v_finalQueryFilter) || (' 
    AND ') || (v_searchQuery) ;
   v_filter := CASE 
                    WHEN ( iv_searchString = ' ' ) THEN v_defaultFilter
   ELSE v_searchFilter
      END ;
       v_DomQuery := ' 
  SELECT 
    COUNT(*) 
  FROM 
    (
      "wiretransfers" 
      LEFT JOIN "onetimepayee" ON(
        "wiretransfers"."onetime_id" = "onetimepayee"."onetime_id"
      )
    ) 
  WHERE 
    "onetimepayee"."wireAccountType" = '' Domestic '' 
    AND ' || v_filter;
      EXECUTE IMMEDIATE v_DomQuery into v_DomCount;
      v_InternationalQuery := ' 
  SELECT 
    COUNT(*) 
  FROM 
    (
      "wiretransfers" 
      LEFT JOIN "onetimepayee" ON(
        "wiretransfers"."onetime_id" = "onetimepayee"."onetime_id"
      )
    ) 
  WHERE 
    "onetimepayee"."wireAccountType" = '' International '' 
    AND ' || v_filter; 
      EXECUTE IMMEDIATE v_InternationalQuery into v_InternationalCount;
   IF ( "ISDOMESTICPERMITTED" = 0
     AND "ISINTERNATIONALPERMITTED" = 0 ) THEN
    OPEN  "records" FOR
      SELECT ' UNAUTHORIZED ' ErrorMessage  
        FROM DUAL  ;
      
   ELSE
      IF ( "ISDOMESTICPERMITTED" = 0
        AND v_InternationalCount = 0 ) THEN
       OPEN  "records" FOR
         SELECT ' UNAUTHORIZED ' ErrorMessage  
           FROM DUAL  ;
        
      ELSE
         IF ( "ISINTERNATIONALPERMITTED" = 0
           AND v_DomCount = 0 ) THEN
          OPEN  "records" FOR
            SELECT ' UNAUTHORIZED ' ErrorMessage  
              FROM DUAL  ;
            
         ELSE
         
         BEGIN
            v_select_statement := (' 
  SELECT 
    "wiretransfers"."transactionId", 
    "wiretransfers"."confirmationNumber", 
    "wiretransfers"."requestId", 
    "wiretransfers"."status", 
    "wiretransfers"."onetime_id", 
    "wiretransfers"."wireTemplateExecution_id", 
    "wiretransfers"."notes", 
    "wiretransfers"."amount", 
    "wiretransfers"."fromAccountNumber", 
    "wiretransfers"."payeeAccountNumber", 
    "wiretransfers"."transactionType", 
    "wiretransfers"."payeeId", 
    "wiretransfers"."payeeCurrency", 
    "onetimepayee"."payeeName", 
    "onetimepayee"."payeeNickName", 
    "onetimepayee"."payeeType", 
    "onetimepayee"."wireAccountType", 
    "onetimepayee"."swiftCode", 
    "onetimepayee"."routingNumber", 
    "onetimepayee"."zipCode", 
    "onetimepayee"."cityName", 
    "onetimepayee"."state", 
    "onetimepayee"."country", 
    "onetimepayee"."payeeAddressLine1", 
    "onetimepayee"."payeeAddressLine2", 
    "onetimepayee"."bankName", 
    "onetimepayee"."internationalRoutingCode", 
    "onetimepayee"."bankAddressLine1", 
    "onetimepayee"."bankAddressLine2", 
    "onetimepayee"."bankCity", 
    "onetimepayee"."bankState", 
    "onetimepayee"."bankZip" 
  FROM 
    (
      "wiretransfers" 
      LEFT JOIN "onetimepayee" ON(
        "wiretransfers"."onetime_id" = "onetimepayee"."onetime_id"
      )
    ) 
  where 
    ')  || v_filter || v_orderBy ;
            open "records" for v_select_statement;
         
         END;
         END IF;
      END IF;
   END IF;


END;
/
--  DDL for Procedure fetch_bulkWireTransactionsExecution_details_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "fetch_bulkWireTransactionsExecution_details_proc" 
(
  "BULKWIREFILEEXECUTION_ID" IN NVARCHAR2,
  "SEARCHSTRING" IN NVARCHAR2,
  "STATUSFILTER" IN NVARCHAR2,
  "SORTBYPARAM" IN NVARCHAR2,
  "SORTORDER" IN NVARCHAR2,
  "ISDOMESTICPERMITTED" IN NUMBER,
  "ISINTERNATIONALPERMITTED" IN NUMBER,
  "records" OUT SYS_REFCURSOR
 
)
AS
   iv_sortByParam NVARCHAR2(50) := "SORTBYPARAM";
   iv_sortOrder NVARCHAR2(50) := "SORTORDER";
   iv_searchString NVARCHAR2(50) := "SEARCHSTRING";
   v_query1 NVARCHAR2(2000);
   v_query2 NVARCHAR2(2000);
   v_query3 NVARCHAR2(2000);
   v_orderBy NVARCHAR2(2000);
   v_searchQuery NVARCHAR2(2000);
   v_statusQuery NVARCHAR2(2000);
   v_finalQueryFilter NVARCHAR2(2000);
   v_defaultFilter NVARCHAR2(2000);
   v_searchFilter NVARCHAR2(2000);
   v_filter long;
   v_select_statement long;
   v_DomQuery NVARCHAR2(2000);
   v_InternationalQuery NVARCHAR2(2000);
   v_DomCount NVARCHAR2(2000);
   v_InternationalCount NVARCHAR2(2000);
   v_ParmDefinition NVARCHAR2(500);
   v_retvalOUT NVARCHAR2(2000);

BEGIN

   
   v_query1 := (' "wiretransfers"."wireFileExecution_id" = ') || ("BULKWIREFILEEXECUTION_ID") || (' 
    AND "wiretransfers"."softdeleteflag" = 0 ') ;
   v_query2 := (' "wiretransfers"."wireFileExecution_id" = ') || ("BULKWIREFILEEXECUTION_ID") || (' 
    AND "wiretransfers"."status" = ''') || ("STATUSFILTER") || (''' 
    AND "wiretransfers"."softdeleteflag" = 0 ') ;
   v_query3 := (' "wiretransfers"."wireFileExecution_id" = ') || ("BULKWIREFILEEXECUTION_ID") || (' 
    AND "wiretransfers"."status" in ('' Failed '', '' Denied '') ') || (' 
    AND "wiretransfers"."softdeleteflag" = 0 ') ;
   iv_sortByParam := CASE 
                         WHEN iv_sortByParam = ' '
                           OR iv_sortByParam IS NULL THEN ' "transactionId" '
   ELSE iv_sortByParam
      END ;
   iv_sortOrder := CASE 
                       WHEN iv_sortOrder = ' '
                         OR iv_sortOrder IS NULL THEN ' ASC '
   ELSE iv_sortOrder
      END ;
   iv_searchString := CASE 
                          WHEN iv_searchString = ' '
                            OR iv_searchString IS NULL THEN ' '
   ELSE ''' % ' || iv_searchString || ' % '''
      END ;
   v_orderBy := (' 
  ORDER BY 
    ') || (iv_sortByParam) || (' ') || (iv_sortOrder) ;
   v_searchQuery := ('(
      "wiretransfers"."amount" LIKE ') || (iv_searchString) || (' 
      OR "wiretransfers"."notes" LIKE ') || (iv_searchString) || (' 
      OR "wiretransfers"."fromAccountNumber" LIKE ') || (iv_searchString) || (' 
      OR "wiretransfers"."payeeAccountNumber" LIKE ') || (iv_searchString) || (' 
      OR "wiretransfers"."transactionType" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."payeeName" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."payeeType" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."payeeAddressLine1" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."payeeAddressLine2" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."cityName" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."state" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."zipCode" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."bankName" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."bankAddressLine1" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."bankAddressLine2" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."bankZip" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."bankState" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."bankCity" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."routingNumber" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."internationalRoutingCode" LIKE ') || (iv_searchString) || (' 
      OR "onetimepayee"."swiftCode" LIKE ') || (iv_searchString) || ('
    ) ') ;
   v_statusQuery := CASE 
                         WHEN ( "STATUSFILTER" = ' Failed ' ) THEN v_query3
   ELSE v_query2
      END ;
   v_finalQueryFilter := CASE 
                              WHEN ( "STATUSFILTER" = ' ' ) THEN v_query1
   ELSE v_statusQuery
      END ;
   v_defaultFilter := (v_finalQueryFilter) ;
   v_searchFilter := (v_finalQueryFilter) || (' 
    AND ') || (v_searchQuery) ;
   v_filter := CASE 
                    WHEN ( iv_searchString = ' ' ) THEN v_defaultFilter
   ELSE v_searchFilter
      END ;
   v_DomQuery := ' 
  SELECT 
    COUNT(*) 
  FROM 
    "wiretransfers" 
    LEFT JOIN "onetimepayee" ON(
      "wiretransfers"."onetime_id" = "onetimepayee"."onetime_id"
    ) 
  WHERE 
    "onetimepayee"."wireAccountType" = '' Domestic '' 
    AND ' || v_filter ;
    EXECUTE IMMEDIATE v_DomQuery INTO v_DomCount;
                 
              
   v_InternationalQuery := ' 
  SELECT 
    COUNT(*) 
  FROM 
    "wiretransfers" 
    LEFT JOIN "onetimepayee" ON(
      "wiretransfers"."onetime_id" = "onetimepayee"."onetime_id"
    ) 
  WHERE 
    "onetimepayee"."wireAccountType" = '' International '' 
    AND ' || v_filter ;
   EXECUTE IMMEDIATE v_DomQuery INTO v_InternationalCount;
             
   v_filter := (v_filter) || (v_orderBy) ;
   IF ( "ISDOMESTICPERMITTED" = 0
     AND "ISINTERNATIONALPERMITTED" = 0 ) THEN
    OPEN  "records" FOR
      SELECT ' UNAUTHORIZED ' ErrorMessage  
        FROM DUAL  ;
      
   ELSE
      IF ( "ISDOMESTICPERMITTED" = 0
        AND v_InternationalCount = 0 ) THEN
       OPEN  "records" FOR
         SELECT ' UNAUTHORIZED ' ErrorMessage  
           FROM DUAL  ;
         
      ELSE
         IF ( "ISINTERNATIONALPERMITTED" = 0
           AND v_DomCount = 0 ) THEN
          OPEN  "records" FOR
            SELECT ' UNAUTHORIZED ' ErrorMessage  
              FROM DUAL  ;
            
         ELSE
         
         BEGIN
            v_select_statement := (' 
  SELECT 
    "wiretransfers"."transactionId", 
    "wiretransfers"."confirmationNumber", 
    "wiretransfers"."requestId", 
    "wiretransfers"."status", 
    "wiretransfers"."onetime_id", 
    "wiretransfers"."wireFileExecution_id", 
    "wiretransfers"."notes", 
    "wiretransfers"."amount", 
    "wiretransfers"."fromAccountNumber", 
    "wiretransfers"."payeeAccountNumber", 
    "wiretransfers"."transactionType", 
    "wiretransfers"."payeeId", 
    "wiretransfers"."payeeCurrency", 
    "onetimepayee"."payeeName", 
    "onetimepayee"."payeeNickName", 
    "onetimepayee"."payeeType", 
    "onetimepayee"."wireAccountType", 
    "onetimepayee"."swiftCode", 
    "onetimepayee"."routingNumber", 
    "onetimepayee"."zipCode", 
    "onetimepayee"."cityName", 
    "onetimepayee"."state", 
    "onetimepayee"."country", 
    "onetimepayee"."payeeAddressLine1", 
    "onetimepayee"."payeeAddressLine2", 
    "onetimepayee"."bankName", 
    "onetimepayee"."internationalRoutingCode", 
    "onetimepayee"."bankAddressLine1", 
    "onetimepayee"."bankAddressLine2", 
    "onetimepayee"."bankCity", 
    "onetimepayee"."bankState", 
    "onetimepayee"."bankZip" 
  FROM 
    (
      "wiretransfers" 
      LEFT JOIN "onetimepayee" ON(
        "wiretransfers"."onetime_id" = "onetimepayee"."onetime_id"
      )
    ) 
  WHERE 
    ') || (v_filter) ;
            open "records" for v_select_statement;
         
         END;
         END IF;
      END IF;
   END IF;


END;
/
--  DDL for Procedure fetch_bwfile_domesticInternationalCount_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "fetch_bwfile_domesticInternationalCount_proc" 
(
  "_bulkwirefileID" IN VARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   

BEGIN

   IF ( "_bulkwirefileID" != ' '
     AND "_bulkwirefileID" IS NOT NULL ) THEN
    OPEN  "records" FOR
      SELECT "noOfDomesticTransactions" ,
             "noOfInternationalTransactions" 
        FROM "bulkwirefiles" 
       WHERE  "bulkWireFileID" = "_bulkwirefileID" ;
      
   ELSE
      OPEN  "records" FOR
         SELECT 0 "noOfDomesticTransactions"  ,
                0 "noOfInternationalTransactions"  
           FROM DUAL  ;
        
   END IF;


END;
/
--  DDL for Procedure fetch_bwtemplate_domesticInternationalCount_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "fetch_bwtemplate_domesticInternationalCount_proc" 
(
  "_bulkwiretemplateID" IN VARCHAR2,
  "_bulkwiretemplatelineitemIDs" IN VARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   v_DomCount NVARCHAR2(2000);
   v_InternationalCount NVARCHAR2(2000);
   

BEGIN

   IF ( "_bulkwiretemplatelineitemIDs" != ' '
     AND "_bulkwiretemplatelineitemIDs" IS NOT NULL ) THEN
    
   BEGIN
      SELECT COUNT(*)  

        INTO v_DomCount
        FROM "bulkwiretemplatelineitems" 
       WHERE  FIND_IN_SET(CAST("bulkWireTemplateLineItemID" AS NVARCHAR2(2000)), "_bulkwiretemplatelineitemIDs") > 0
                AND "bulkWireTransferType" = ' Domestic '
                AND "softdeleteflag" = 0;
      SELECT COUNT(*)  

        INTO v_InternationalCount
        FROM "bulkwiretemplatelineitems" 
       WHERE  FIND_IN_SET(CAST("bulkWireTemplateLineItemID" AS NVARCHAR2(2000)), "_bulkwiretemplatelineitemIDs") > 0
                AND "bulkWireTransferType" = ' International '
                AND "softdeleteflag" = 0;
      OPEN  "records" FOR
         SELECT v_DomCount "noOfDomesticTransactions"  ,
                v_InternationalCount "noOfInternationalTransactions"  
           FROM DUAL  ;
            
   END;
   ELSE
      IF ( "_bulkwiretemplateID" != ' '
        AND "_bulkwiretemplateID" IS NOT NULL ) THEN
       OPEN  "records" FOR
         SELECT "noOfDomesticTransactions" ,
                "noOfInternationalTransactions" 
           FROM "bulkwiretemplate" 
          WHERE  "bulkWireTemplateID" = "_bulkwiretemplateID" ;
        
      ELSE
         OPEN  "records" FOR
            SELECT 0 "noOfDomesticTransactions"  ,
                   0 "noOfInternationalTransactions" 
              FROM DUAL  ;
          
      END IF;
   END IF;

END;
/
--  DDL for Procedure fetch_cardtransaction_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "fetch_cardtransaction_proc" 
(
  /*
     *   SSMA informational messages:
     *   M2SS0055: Data type was converted to VARCHAR according to character set mapping for latin1 character set
     */
  v__cardNumber IN VARCHAR2,
  v__pageOffset IN NUMBER,
  v__pageSize IN NUMBER,
  iv__sortOrder IN VARCHAR2,
  iv__sortByParam IN VARCHAR2,
  "cardtransaction" OUT SYS_REFCURSOR
)
AS
   /*
      *   SSMA informational messages:
      *   M2SS0055: Data type was converted to VARCHAR according to character set mapping for latin1 character set
      */
   v__sortByParam VARCHAR2(50) := iv__sortByParam;
   /*
      *   SSMA informational messages:
      *   M2SS0055: Data type was converted to VARCHAR according to character set mapping for latin1 character set
      */
   v__sortOrder VARCHAR2(50) := iv__sortOrder;
   v_filter VARCHAR2(4000);
   v_orderBy VARCHAR2(4000);
   v_paginationQuery VARCHAR2(4000);
   v_select_statement VARCHAR2(4000);


BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   v_filter := (' "cardtransaction"."cardNumber" = ') || ('''' || (v__cardNumber|| '''')) ;
   v__sortByParam := (CASE 
                           WHEN v__sortByParam IS NULL
                             OR v__sortByParam = '' THEN ' transactionDate '
   ELSE v__sortByParam
      END) ;
   v__sortOrder := (CASE 
                         WHEN v__sortOrder IS NULL
                           OR v__sortOrder = '' THEN ' DESC '
   ELSE v__sortOrder
      END) ;
   v_orderBy := (' 
  ORDER BY 
    ') || (v__sortByParam) || (' ') || (v__sortOrder) ;
   v_paginationQuery := (CASE 
                              WHEN ( v__pageOffset IS NOT NULL
                                AND v__pageOffset != ''
                                AND v__pageSize IS NOT NULL
                                AND v__pageSize != '' ) THEN ' OFFSET ' || CAST(v__pageOffset AS VARCHAR2) || ' ROWS FETCH NEXT ' || CAST(v__pageSize AS VARCHAR2) || ' ROWS ONLY '
   ELSE ' '
      END) ;
   v_select_statement := ' 
  SELECT 
    "cardtransaction"."transactionDescription", 
    "cardtransaction"."transactionBalance", 
    "cardtransaction"."transactionMerchantAddressName", 
    "cardtransaction"."transactionMerchantCity", 
    "cardtransaction"."merchantCategory", 
    "cardtransaction"."transactionStatus", 
    "cardtransaction"."transactionType", 
    "cardtransaction"."transactionCategory", 
    "cardtransaction"."transactionDetailDescription", 
    "cardtransaction"."transactionIndicator", 
    "cardtransaction"."transactionDate", 
    "cardtransaction"."transactionTime", 
    "cardtransaction"."transactionAmount", 
    "cardtransaction"."transactionReferenceNumber", 
    "cardtransaction"."transactionCurrencyCode", 
    "cardtransaction"."transactionExchangeRate", 
    "cardtransaction"."exchangeCurrency", 
    "cardtransaction"."exchangeAmount", 
    "cardtransaction"."transactionTaxIndicator", 
    "cardtransaction"."taxPercentage", 
    "cardtransaction"."transactionTaxAmount", 
    "cardtransaction"."transactionTerminalID", 
    "cardtransaction"."cardType" 
  FROM 
    "cardtransaction" cardtransaction 
  WHERE 
    ' || (v_filter) || (v_orderBy) || (v_paginationQuery) || (' ') ;
   OPEN "cardtransaction" FOR
   v_select_statement;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure fetch_default_account_actions_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "fetch_default_account_actions_proc" 
(
  "_userId" IN VARCHAR2,
  "_coreCustomerId" IN VARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   v_select_statement VARCHAR2(4000);
   v__contractId VARCHAR2(4000);
   v_serviceDefinitionId VARCHAR2(4000);
   v_serviceType VARCHAR2(4000);
   v__groupId VARCHAR2(4000);

BEGIN

   v_select_statement := ' 
  select 
    LISTAGG(
      CAST(
        "featureaction"."id" AS varchar2(255)
      )
    ) as "defaultAccountActions" 
  from 
    "featureaction" 
  where 
    (
      "featureaction"."isAccountLevel" = '' 1 ''
    ) 
    AND "featureaction"."id" IN ' ;
   SELECT "contractcorecustomers"."contractId" 

     INTO v__contractId
     FROM "contractcorecustomers" 
    WHERE  "contractcorecustomers"."coreCustomerId" = "_coreCustomerId";
   SELECT "servicedefinitionId" 

     INTO v_serviceDefinitionId
     FROM "contract" 
    WHERE  "id" = v__contractId;
   SELECT "serviceType" 

     INTO v_serviceType
     FROM "servicedefinition" 
    WHERE  "id" = v_serviceDefinitionId;
   SELECT DISTINCT "customergroup"."Group_id" 
     INTO v__groupId
     FROM "customergroup" 
    WHERE  "customergroup"."Customer_id" = "_userId"
             AND "customergroup"."coreCustomerId" = "_coreCustomerId";
   v_select_statement := (v_select_statement|| '(
      SELECT 
        "actionId" 
      FROM 
        "servicedefinitionactionlimit" 
      WHERE 
        "servicedefinitionactionlimit"."serviceDefinitionId" = '|| ''''|| v_serviceDefinitionId|| ''''|| ' 
        AND "servicedefinitionactionlimit"."actionId" IN ') ;
   v_select_statement := (v_select_statement|| ' (
          SELECT 
            "Action_id" 
          FROM 
            "groupactionlimit" 
          WHERE 
            "groupactionlimit"."Group_id" = '|| ''''|| v__groupId|| ''''|| ' 
            AND "groupactionlimit"."Action_id" IN ') ;
   v_select_statement := (v_select_statement|| ' (
              SELECT 
                "actionId" 
              FROM 
                "contractactionlimit" 
              WHERE 
                "contractactionlimit"."contractId" = '|| ''''|| v__contractId|| ''''|| ' 
                AND "contractactionlimit"."coreCustomerId" = '|| ''''|| "_coreCustomerId"|| '''') ;
   v_select_statement := CONCAT(v_select_statement, '
            )
        )
    ) ') ;
--   EXECUTE IMMEDIATE v_select_statement;
   DBMS_OUTPUT.PUT_LINE(v_select_statement);
   OPEN "records" FOR v_select_statement;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure fetch_restrictive_featureactionlimits_legalEntityId_proc




--  DDL for Procedure fetch_restrictive_featureactionlimits_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "fetch_restrictive_featureactionlimits_proc" 
(
  v__locale IN VARCHAR2,
  v__userId IN VARCHAR2,
  v__serviceDefinitionId IN VARCHAR2,
  v__roleId IN VARCHAR2,
  v__coreCustomerId IN VARCHAR2,
  v__accessPolicyIdList IN VARCHAR2,v_cursor OUT SYS_REFCURSOR
)
AS
   v_select_statement VARCHAR2(4000);
   v_action_select_statement VARCHAR2(4000);

BEGIN

   v_select_statement := ' ' ;
   v_action_select_statement := ' ' ;
   v_action_select_statement := '(
      SELECT 
        "featureaction"."id" AS actionId 
      FROM 
        "featureaction" 
        LEFT JOIN "feature" ON (
          "feature"."id" = "featureaction"."Feature_id"
        ) ' ;
   IF ( v__accessPolicyIdList != '') THEN

   BEGIN
      v_action_select_statement := (v_action_select_statement|| ' 
      WHERE 
        "featureaction"."accessPolicyId" IN (
          SELECT 
            column_value 
          from 
            TABLE(
              UTILS.STRING_SPLIT(
                '|| '''' || v__accessPolicyIdList || ''''|| '
              )
            )
        ) ') ;

   END;
   END IF;
   v_action_select_statement := (v_action_select_statement|| '
    ) ') ;
   IF ( v__serviceDefinitionId != '') THEN

   BEGIN
      v_select_statement := ('(
      SELECT 
        "servicedefinitionactionlimit"."actionId" AS actionId 
      FROM 
        "servicedefinitionactionlimit" 
      WHERE 
        "servicedefinitionactionlimit"."serviceDefinitionId" = '|| ''''|| v__serviceDefinitionId|| '''') ;
      v_select_statement := (v_select_statement|| ' 
        AND "servicedefinitionactionlimit"."actionId" IN '|| v_action_select_statement) ;
      v_select_statement := CONCAT(v_select_statement, '
    ) ') ;
      v_action_select_statement := v_select_statement ;

   END;
   END IF;
   IF ( v__roleId != '' ) THEN

   BEGIN
      v_select_statement := ('(
      SELECT 
        "groupactionlimit"."Action_id" AS actionId 
      FROM 
        "groupactionlimit" 
      WHERE 
        "groupactionlimit"."Group_id" = '|| ''''|| v__roleId|| '''') ;
      v_select_statement := (v_select_statement|| ' 
        AND "groupactionlimit"."Action_id" IN ') ;
      v_select_statement := (v_select_statement|| v_action_select_statement) ;
      v_select_statement := (v_select_statement|| '
    ) ') ;
      v_action_select_statement := v_select_statement ;

   END;
   END IF;
   IF ( v__coreCustomerId != '' ) THEN

   BEGIN
      v_select_statement := ('(
      SELECT 
        "contractactionlimit"."actionId" AS actionId 
      FROM 
        "contractactionlimit" 
      WHERE 
        "contractactionlimit"."coreCustomerId" = '|| ''''|| v__coreCustomerId|| '''') ;
      v_select_statement := (v_select_statement|| ' 
        AND "contractactionlimit"."actionId" IN '|| v_action_select_statement) ;
      v_select_statement := (v_select_statement|| '
    ) ') ;
      v_action_select_statement := v_select_statement ;

   END;
   END IF;
   IF ( v__userId != '' ) THEN

   BEGIN
      v_select_statement := ('(
      SELECT 
        "contractactionlimit"."Action_id" AS actionId 
      FROM 
        "customeraction" 
      WHERE 
        "customeraction"."Customer_id" = '|| ''''|| v__userId|| '''') ;
      IF ( v__coreCustomerId != '' ) THEN

      BEGIN
         v_select_statement := (v_select_statement|| ' 
        AND "customeraction"."coreCustomerId" = '|| v__coreCustomerId) ;

      END;
      END IF;
      v_select_statement := (v_select_statement|| ' 
        AND "customeraction"."isAllowed" = '' 1 '' 
        AND "contractactionlimit"."Action_id" IN '|| v_action_select_statement) ;
      v_select_statement := (v_select_statement|| '
    ) ') ;
      v_action_select_statement := v_select_statement ;

   END;
   END IF;
   v_select_statement := '(
      SELECT 
        "feature"."id" AS featureId, 
        "featuredisplaynamedescription"."displayName" AS featureName, 
        "featuredisplaynamedescription"."displayDescription" AS featureDescription, 
        "feature"."Status_id" AS featureStatus, 
        "featureaction"."status" AS actionStatus, 
        "featureaction"."id" AS actionId, 
        "actiondisplaynamedescription"."displayName" AS actionName, 
        "actiondisplaynamedescription"."displayDescription" AS actionDescription, 
        "featureaction"."isAccountLevel" AS isAccountLevel, 
        "featureaction"."Type_id" AS typeId, 
        "featureaction"."limitgroupId" as limitGroupId, 
        "featureaction"."accesspolicyId" as accessPolicyId, 
        "featureaction"."actionlevelId" as actionLevelId, 
        null as limitTypeId, 
        null as fiLimitValue ' ;
   IF ( v__serviceDefinitionId != '' ) THEN

   BEGIN
      v_select_statement := CONCAT(v_select_statement, ', 
        null AS serviceLimitValue ') ;

   END;
   END IF;
   IF ( v__roleId != '' ) THEN

   BEGIN
      v_select_statement := CONCAT(v_select_statement, ', 
        null AS groupLimitValue ') ;

   END;
   END IF;
   IF ( v__coreCustomerId != '' ) THEN

   BEGIN
      v_select_statement := CONCAT(v_select_statement, ', 
        null AS coreCustomerLimitValue ') ;

   END;
   END IF;
   v_select_statement := CONCAT(v_select_statement, ' 
      FROM 
        "feature" 
        LEFT JOIN "featuredisplaynamedescription" ON (
          "featuredisplaynamedescription"."Feature_id" = "feature"."id"
        ) 
        LEFT JOIN "featureaction" ON (
          "featureaction"."Feature_id" = "feature"."id"
        ) 
        LEFT JOIN "actiondisplaynamedescription" ON (
          "actiondisplaynamedescription"."Action_id" = "featureaction"."id"
        ) ') ;
   IF ( v__coreCustomerId != '' ) THEN

   BEGIN
      v_select_statement := CONCAT(v_select_statement, ' 
        LEFT JOIN "contractactionlimit" ON (
          "contractactionlimit"."actionId" = "featureaction"."id"
        ) ') ;

   END;
   END IF;
   v_select_statement := (v_select_statement|| ' 
      WHERE 
        "featureaction"."Type_id" = '' NON_MONETARY '' 
        AND "featureaction"."id" IN '|| v_action_select_statement|| '
    ) ') ;
   v_select_statement := (v_select_statement || ' 
  UNION 
    (
      SELECT 
        "feature"."id" AS featureId, 
        "featuredisplaynamedescription"."displayName" AS featureName, 
        "featuredisplaynamedescription"."displayDescription" AS featureDescription, 
        "feature"."Status_id" AS featureStatus, 
        "featureaction"."status" AS actionStatus, 
        "featureaction"."id" AS actionId, 
        "actiondisplaynamedescription"."displayName" AS actionName, 
        "actiondisplaynamedescription"."displayDescription" AS actionDescription, 
        "featureaction"."isAccountLevel" AS isAccountLevel, 
        "featureaction"."Type_id" AS typeId, 
        "featureaction"."limitgroupId" as limitGroupId, 
        "featureaction"."accessPolicyId" as accessPolicyId, 
        "featureaction"."actionlevelId" as actionLevelId, 
        "actionlimit"."LimitType_id" as limitTypeId, 
        "actionlimit"."value" as fiLimitValue ') ;
   IF ( v__serviceDefinitionId != '' ) THEN

   BEGIN
      v_select_statement := CONCAT(v_select_statement, ', 
        "servicedefinitionactionlimit"."value" AS serviceLimitValue ') ;

   END;
   END IF;
   IF ( v__roleId != '' ) THEN

   BEGIN
      v_select_statement := CONCAT(v_select_statement, ', 
        "groupactionlimit"."value" AS groupLimitValue ') ;

   END;
   END IF;
   IF ( v__coreCustomerId != '' ) THEN

   BEGIN
      v_select_statement := CONCAT(v_select_statement, ', 
        "contractactionlimit"."value" AS coreCustomerLimitValue ') ;

   END;
   END IF;
   v_select_statement := CONCAT(v_select_statement, ' 
      FROM 
        "feature" 
        LEFT JOIN "featuredisplaynamedescription" ON (
          "featuredisplaynamedescription"."Feature_id" = "feature"."id"
        ) 
        LEFT JOIN "featureaction" ON (
          "featureaction"."Feature_id" = "feature"."id"
        ) 
        LEFT JOIN "actionlimit" ON (
          "actionlimit"."Action_id" = "featureaction"."id"
        ) 
        LEFT JOIN "actiondisplaynamedescription" ON (
          "actiondisplaynamedescription"."Action_id" = "featureaction"."id"
        ) ') ;
   IF ( v__serviceDefinitionId != '' ) THEN

   BEGIN
      v_select_statement := CONCAT(v_select_statement, ' 
        LEFT JOIN "servicedefinitionactionlimit" ON (
          "servicedefinitionactionlimit"."actionId" = "actionlimit"."Action_id" 
          AND "servicedefinitionactionlimit"."limitTypeId" = "actionlimit"."LimitType_id"
        ) ') ;

   END;
   END IF;
   IF ( v__roleId != '' ) THEN

   BEGIN
      v_select_statement := CONCAT(v_select_statement, ' 
        LEFT JOIN "groupactionlimit" ON (
          "groupactionlimit"."Action_id" = "actionlimit"."Action_id" 
          AND "groupactionlimit"."LimitType_id" = "actionlimit"."LimitType_id"
        ) ') ;

   END;
   END IF;
   IF ( v__coreCustomerId != '' ) THEN

   BEGIN
      v_select_statement := CONCAT(v_select_statement, ' 
        LEFT JOIN "contractactionlimit" ON (
          "contractactionlimit"."actionId" = "actionlimit"."Action_id" 
          AND "contractactionlimit"."limitTypeId" = "actionlimit"."LimitType_id"
        ) ') ;

   END;
   END IF;
   v_select_statement := (v_select_statement|| ' 
      WHERE 
        "featureaction"."Type_id" = '' MONETARY ''') ;
   IF ( v__serviceDefinitionId != '' ) THEN

   BEGIN
      v_select_statement := (v_select_statement|| ' 
        AND "servicedefinitionactionlimit"."serviceDefinitionId" = '|| ''''|| v__serviceDefinitionId|| '''') ;

   END;
   END IF;
   IF ( v__roleId != '' ) THEN

   BEGIN
      v_select_statement := (v_select_statement|| ' 
        AND "groupactionlimit"."Group_id" = '|| ''''|| v__roleId|| '''') ;

   END;
   END IF;
   IF ( v__coreCustomerId != '' ) THEN

   BEGIN
      v_select_statement := (v_select_statement|| ' 
        AND "contractactionlimit"."coreCustomerId" = '|| ''''|| v__coreCustomerId|| '''') ;

   END;
   END IF;
   v_select_statement := (v_select_statement|| ' 
        AND "featureaction"."id" IN '|| v_action_select_statement|| '
    ) ') ;
   EXECUTE IMMEDIATE v_select_statement;
   OPEN v_cursor FOR v_select_statement;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure fetch_roles_for_servicedefs_proc



create or replace NONEDITIONABLE PROCEDURE         "fetch_roles_for_servicedefs_proc" 
(
  "_servicedefList" IN VARCHAR2, "records" OUT SYS_REFCURSOR
)
AS
   v_sql_query VARCHAR2(4000);


BEGIN

   IF ( "_servicedefList" != ' '
     AND "_servicedefList" IS NOT NULL ) THEN

   BEGIN
      v_sql_query := 'select * from "groupservicedefinition" where "serviceDefinitionId" in (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT('''||"_servicedefList"||''')))' ;
   END;
   END IF;
   dbms_output.put_line(v_sql_query);
   OPEN "records" FOR
   v_sql_query;

END;
/
--  DDL for Procedure fetch_unselectedPayees_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "fetch_unselectedPayees_proc" 
(
  "_bulkwiretemplateID" IN VARCHAR2,
  "User_Id" IN VARCHAR2,
  "sortByParam" IN VARCHAR2,
  "sortOrder" IN VARCHAR2,
  "searchString" IN VARCHAR2,
  "isDomesticPermitted" IN NUMBER,
  "isInternationalPermitted" IN NUMBER,
  "records" OUT SYS_REFCURSOR
)
AS
   iv_sortByParam VARCHAR2(50) := "sortByParam";
   iv_sortOrder VARCHAR2(50) := "sortOrder";
   iv_searchString VARCHAR2(50) := "searchString";
   v_searchAndOrderBy NVARCHAR2(2000);
   v_searchQuery NVARCHAR2(2000);
   v_permissionQuery NVARCHAR2(2000);
   v_query1 NVARCHAR2(2000);
   

BEGIN

 

   iv_sortByParam := CASE 
                         WHEN iv_sortByParam = ' '
                           OR iv_sortByParam IS NULL THEN ' "nickName" '
   ELSE iv_sortByParam
      END ;
   iv_sortOrder := CASE 
                       WHEN iv_sortOrder = ' '
                         OR iv_sortOrder IS NULL THEN ' DESC '
   ELSE iv_sortOrder
      END ;
   v_searchAndOrderBy := ' 
ORDER BY 
  ' || iv_sortByParam || ' ' || iv_sortOrder ;
   IF ( iv_searchString != ' '
     OR iv_searchString IS NOT NULL ) THEN
    
   BEGIN
      iv_searchString := ''' % ' || iv_searchString || ' % ''' ;
      v_searchQuery := ' 
  AND (
    "name" LIKE ' || iv_searchString || ' 
    OR "bankName" LIKE ' || iv_searchString || ' 
    OR "wireAccountType" LIKE ' || iv_searchString || ' 
    OR "firstName" LIKE ' || iv_searchString || ' 
    OR "lastName" LIKE ' || iv_searchString || ' 
    OR "nickName" LIKE ' || iv_searchString || '
  ) ' ;
      v_searchAndOrderBy := v_searchQuery || ' ' || v_searchAndOrderBy ;
   
   END;
   END IF;
   v_permissionQuery := ' ' ;
   IF ( "isDomesticPermitted" = 0
     AND "isInternationalPermitted" = 1 ) THEN
    v_permissionQuery := ' 
  AND "wireAccountType" = '' International ''' ;
   ELSE
      IF ( "isDomesticPermitted" = 1
        AND "isInternationalPermitted" = 0 ) THEN
       v_permissionQuery := ' 
  AND "wireAccountType" = '' Domestic ''' ;
      ELSE
         IF ( "isDomesticPermitted" = 1
           AND "isInternationalPermitted" = 1 ) THEN
          v_permissionQuery := ' 
  AND "wireAccountType" IN ('' Domestic '', '' International '') ' ;
         ELSE
         WHILE ( "isDomesticPermitted" != 1
           AND "isInternationalPermitted" != 1 )
           LOOP
         BEGIN
            OPEN  "records" FOR
               SELECT ' ' 
                 FROM DUAL  ;
              
       
         EXIT;
         END;
         END LOOP;
         END IF;
      END IF;
   END IF;
   v_query1 := ' 
SELECT 
  * 
FROM 
  "payee" 
WHERE 
  "isWiredRecepient" = 1 
  AND "User_Id" = ''' || "User_Id" || ''' 
  AND "Id" NOT IN (
    SELECT 
      "payeeId" 
    FROM 
      "bulkwiretemplatelineitems" 
    WHERE 
      "bulkWireTemplateID" = ''' || "_bulkwiretemplateID" || ''' 
      AND "templateRecipientCategory" = '' EXISTINGRECIPIENT '' 
      AND "softdeleteflag" = 0
  ) ' || v_permissionQuery || v_searchAndOrderBy ;
   open "records" for v_query1;
  
 
END;
/
--  DDL for Procedure fetch_user_corecustomer_actions



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "fetch_user_corecustomer_actions" 
(
  "_userId" IN VARCHAR2,
  "_coreCustomerId" IN VARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   v__serviceDefinitionId VARCHAR2(32767);
   v__roleId  VARCHAR2(32767);
   v_action_select_statement VARCHAR2(4000);
   v_select_statement VARCHAR2(4000);

BEGIN

   SELECT LISTAGG(CAST("contract"."servicedefinitionId" AS VARCHAR2(255))) 

     INTO v__serviceDefinitionId
     FROM "contract" 
            LEFT JOIN "contractcorecustomers"    ON ( "contractcorecustomers"."contractId" = '''' || "contract"."id" || '''' )
    WHERE  "contractcorecustomers"."coreCustomerId" = '''' || "_coreCustomerId" || '''';
    DBMS_OUTPUT.PUT_LINE(' A '||v__serviceDefinitionId||' A ');
   SELECT LISTAGG(CAST("customergroup"."Group_id" AS VARCHAR2(255))) 

     INTO v__roleId
     FROM "customergroup" 
    WHERE  "customergroup"."Customer_id" = '''' || "_userId" || ''''
             AND "customergroup"."coreCustomerId" = '''' || "_coreCustomerId" || '''';
   v_action_select_statement := ('(
    SELECT 
      DISTINCT "featureaction"."id" AS "actionId" 
    FROM 
      "featureaction" 
      LEFT JOIN "feature" ON (
        "feature"."id" = "featureaction"."Feature_id"
      )
  ) ') ;
   
   IF ( NVL(v__serviceDefinitionId,' ')!=' ' ) THEN
     DBMS_OUTPUT.PUT_LINE(' A '||v__serviceDefinitionId||' A ');
   BEGIN
      v_select_statement := ('(
    SELECT 
      DISTINCT "servicedefinitionactionlimit"."actionId" AS "actionId" 
    FROM 
      "servicedefinitionactionlimit" 
    WHERE 
      "servicedefinitionactionlimit"."serviceDefinitionId" = '|| '''' || v__serviceDefinitionId || '''') ;
      v_select_statement := (v_select_statement|| ' 
      AND "servicedefinitionactionlimit"."actionId" IN '|| v_action_select_statement) ;
      v_select_statement := (v_select_statement|| '
  ) ') ;
      v_action_select_statement := v_select_statement ;

   END;
   END IF;
   IF ( v__roleId != ' ' ) THEN

   BEGIN
      v_select_statement := CONCAT('(
    SELECT 
      DISTINCT "groupactionlimit"."Action_id" AS "actionId" 
    FROM 
      "groupactionlimit" 
    WHERE 
      "groupactionlimit"."Group_id" = ', '''' || v__roleId || '''') ;
      v_select_statement := CONCAT(v_select_statement, ' 
      AND "groupactionlimit"."Action_id" IN ') ;
      v_select_statement := CONCAT(v_select_statement, v_action_select_statement) ;
      v_select_statement := CONCAT(v_select_statement, '
  ) ') ;
      v_action_select_statement := v_select_statement ;

   END;
   END IF;
   IF ( "_coreCustomerId" IS NOT NULL ) THEN

   BEGIN
      v_select_statement := ('(
    SELECT 
      DISTINCT "contractactionlimit"."actionId" AS "actionId" 
    FROM 
      "contractactionlimit" 
    WHERE 
      "contractactionlimit"."coreCustomerId" = '|| '''' || "_coreCustomerId" || '''') ;
      v_select_statement := (v_select_statement|| ' 
      AND "contractactionlimit"."actionId" IN '|| v_action_select_statement) ;
      v_select_statement := (v_select_statement|| '
  ) ') ;
      v_action_select_statement := v_select_statement ;

   END;
   END IF;
   IF ( "_userId" IS NOT NULL ) THEN

   BEGIN
      v_select_statement := ('(
    SELECT 
      DISTINCT "customeraction"."Action_id" AS "actionId" 
    FROM 
      "customeraction" 
    WHERE 
      "customeraction"."Customer_id" = '|| '''' || "_userId" || '''') ;
      IF ( "_coreCustomerId" IS NOT NULL ) THEN

      BEGIN
         v_select_statement := (v_select_statement|| ' 
      AND "customeraction"."coreCustomerId" = '|| '''' || "_coreCustomerId" || '''') ;

      END;
      END IF;
      v_select_statement := (v_select_statement|| ' 
      AND "customeraction"."isAllowed" = 1 
      AND "customeraction"."Action_id" IN '|| v_action_select_statement) ;
      v_select_statement := (v_select_statement || '
  ) ') ;
      v_action_select_statement := v_select_statement ;

   END;
   END IF;

  DBMS_OUTPUT.PUT_LINE(v_action_select_statement);
  OPEN "records" FOR v_select_statement;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure forex_proc_get



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "forex_proc_get" 
(
  "read_query" IN VARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   v_stmt VARCHAR2(32767);

BEGIN
   
   v_stmt := REPLACE("read_query",';
','');
   DBMS_OUTPUT.PUT_LINE(v_stmt);
    v_stmt := CONCAT(' ',v_stmt ) ;
--   EXECUTE IMMEDIATE v_stmt;
   OPEN "records" FOR  v_stmt;
   
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure get_actions_with_approvefeatureaction_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "get_actions_with_approvefeatureaction_proc" 
(
  "_featureActions" IN NVARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   v_actionsList NVARCHAR2(2000);


BEGIN

   SELECT listagg(CAST("featureaction"."id" AS NVARCHAR2(2000)), ',') 

     INTO v_actionsList
     FROM "featureaction" 
    WHERE  FIND_IN_SET("id", "_featureActions") <> 0
             AND ("featureaction"."approveFeatureAction" is not null or "featureaction"."approveFeatureAction" != '' );
   OPEN  "records" FOR
      SELECT v_actionsList "actions"  
        FROM DUAL  ;

END;
/
--  DDL for Procedure get_alert_sub_types



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "get_alert_sub_types" 
(
  v__alertTypes IN VARCHAR2, "alertsubtype" OUT SYS_REFCURSOR
)
AS
   

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   OPEN  "alertsubtype" FOR
      SELECT "alertsubtype"."id" alertSubType  
        FROM "alertsubtype" 
       WHERE  "alertsubtype"."AlertTypeId" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v__alertTypes)));
      --DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure get_AlertTypes



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "get_AlertTypes" 
(
  "_alertTypes" IN NVARCHAR2,
  "dbxalerttype" OUT SYS_REFCURSOR
)
AS
   

BEGIN

   
   OPEN  "dbxalerttype" FOR
      SELECT "dbxalerttype"."id" "id"  ,
             "dbxalerttype"."AttributeId" "attributeId"  ,
             "dbxalerttype"."AlertConditionId" "alertConditionId"  ,
             "dbxalerttype"."Value1" "value1"  ,
             "dbxalerttype"."Value2" "value2"  ,
             "dbxalerttype"."IsGlobal" "isGlobal"  
        FROM "dbxalerttype" 
       WHERE  FIND_IN_SET("dbxalerttype"."id", "_alertTypes") <> 0 ;
     
END;
/
--  DDL for Procedure get_associated_contractaccounts_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "get_associated_contractaccounts_proc" 
(
  "_accountIdList" IN NVARCHAR2,"records" out SYS_REFCURSOR,
  "records1" OUT SYS_REFCURSOR
)
AS
   v__accountIdList NVARCHAR2(2000);
   v_excludedaccountIdList NVARCHAR2(2000);


BEGIN

   SELECT listagg("contractaccounts"."accountId", ', 
') 

     INTO v__accountIdList
     FROM "contractaccounts" 
    WHERE  FIND_IN_SET("contractaccounts"."accountId", "_accountIdList") > 0;
   SELECT listagg("excludedcontractaccounts"."accountId", ', 
') 

     INTO v_excludedaccountIdList
     FROM "excludedcontractaccounts" 
    WHERE  FIND_IN_SET("excludedcontractaccounts"."accountId", "_accountIdList") > 0;
   OPEN  "records" FOR
      SELECT v__accountIdList "accountIdList" 
        FROM DUAL  ;
      OPEN "records1" FOR
       SELECT v_excludedaccountIdList "excludedaccountIdList" FROM DUAL;


END;
/
--  DDL for Procedure get_associated_contractusers_proc



create or replace NONEDITIONABLE PROCEDURE         "get_associated_contractusers_proc" 
(
  "_id" IN VARCHAR2,
  "_backendType" IN VARCHAR2,
  "_legalEntityId" IN VARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   

BEGIN

   OPEN  "records" FOR
      SELECT ca."Customer_id" "id"  ,
             ca."contractId" "contractId"  ,
             con."name" "contractName"  ,
             ca."coreCustomerId" "coreCustomerId"  ,
             concore."coreCustomerName" "coreCustomerName"  ,
             cc."customerId" "customerId"  ,
             cg."Group_id" "groupId"  ,
             cus."FirstName" "firstName"  ,
             cus."LastName" "lastName"  ,
             cus."UserName" "userName"  ,
             cus."Lastlogintime" "lastlogintime"  ,
             cus."Status_id" "statusId"  ,
             b."BackendId" "backendId"  ,
             ca."companyLegalUnit" "companyLegalUnit"  
        FROM "customeraction" ca
               LEFT JOIN "contractcustomers" cc   ON ( ca."contractId" = cc."contractId"
               AND ca."coreCustomerId" = cc."coreCustomerId" )
               LEFT JOIN "contractcorecustomers" concore   ON ( ca."contractId" = concore."contractId"
               AND ca."coreCustomerId" = concore."coreCustomerId" )
               LEFT JOIN "contract" con   ON ( concore."contractId" = con."id" )
               LEFT JOIN "customergroup" cg   ON ( cc."contractId" = cg."contractId"
               AND cc."coreCustomerId" = cg."coreCustomerId"
               AND cg."Customer_id" = cc."customerId" )
               LEFT JOIN "customer" cus   ON ( cg."Customer_id" = cus."id" )
               LEFT JOIN "backendidentifier" b   ON ( b."Customer_id" = cus."id"
               AND b."BackendType" = "_backendType" )
       WHERE  ca."Customer_id" = "_id"
                AND ca."Action_id" = 'USER_MANAGEMENT_VIEW'
                AND ca."companyLegalUnit" = "_legalEntityId" ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure get_backendidentifiers_for_customerids



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "get_backendidentifiers_for_customerids" 
(
  v__customerIds IN VARCHAR2, "backendidentifier" OUT SYS_REFCURSOR
)
AS
   

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   OPEN  "backendidentifier" FOR
      SELECT "backendidentifier"."Customer_id" customerId  ,
             "backendidentifier"."BackendId" backendId  
        FROM "backendidentifier" 
       WHERE  "backendidentifier"."Customer_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v__customerIds))) ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure get_customer_applicantinfo_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "get_customer_applicantinfo_proc" 
(
  /*
     *   SSMA informational messages:
     *   M2SS0055: Data type was converted to VARCHAR(MAX) according to character set mapping for latin1 character set
     */
  v_Customer_id IN VARCHAR2, "customer" OUT SYS_REFCURSOR
)
AS


BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   OPEN  "customer" FOR
      SELECT customer."id" Customer_id  ,
             customer."FirstName" FirstName  ,
             customer."MiddleName" MiddleName  ,
             customer."LastName" LastName  ,
             customer."DateOfBirth" DateOfBirth  ,
             customer."Ssn" Ssn  ,
             customer."PreferredContactMethod" PreferredContactMethod  ,
             ( SELECT "customercommunication"."Value" 
               FROM "customercommunication" 
                WHERE  "customercommunication"."Type_id" = ' COMM_TYPE_PHONE '
                         AND "customercommunication"."isPrimary" = 1
                         AND UTL_RAW.CAST_TO_RAW(CAST("customercommunication"."Customer_id" AS VARCHAR(255))) = UTL_RAW.CAST_TO_RAW(v_Customer_id) 
                 FETCH FIRST 1 ROWS ONLY ) Phone  ,
             ( SELECT "customercommunication"."Value" 
               FROM "customercommunication" 
                WHERE  "customercommunication"."Type_id" = ' COMM_TYPE_EMAIL '
                         AND "customercommunication"."isPrimary" = 1
                         AND UTL_RAW.CAST_TO_RAW(CAST("customercommunication"."Customer_id" AS VARCHAR2(255))) =UTL_RAW.CAST_TO_RAW(v_Customer_id) 
                 FETCH FIRST 1 ROWS ONLY ) Email  
        FROM "customer" customer
       WHERE  UTL_RAW.CAST_TO_RAW(CAST(customer."id" AS VARCHAR2(255))) = UTL_RAW.CAST_TO_RAW(v_Customer_id) ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure get_customer_employement_details_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "get_customer_employement_details_proc" 
(
  v_Customer_id IN VARCHAR2, "employementdetails" OUT SYS_REFCURSOR
)
AS
   

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   OPEN  "employementdetails" FOR
      SELECT employementdetails."Customer_id" Customer_id  ,
             employementdetails."EmploymentType" EmploymentType  ,
             employementdetails."CurrentEmployer" CurrentEmployer  ,
             employementdetails."Designation" Designation  ,
             employementdetails."PayPeriod" PayPeriod  ,
             employementdetails."GrossIncome" GrossIncome  ,
             employementdetails."WeekWorkingHours" WeekWorkingHours  ,
             employementdetails."EmploymentStartDate" EmployementStartDate  ,
             employementdetails."PreviousEmployer" PreviousEmployer  ,
             employementdetails."OtherEmployementType" OtherEmployementType  ,
             employementdetails."OtherEmployementDescription" OtherEmployementDescription  ,
             employementdetails."PreviousDesignation" PreviousDesignation  ,
             othersourceofincome."SourceType" OtherIncomeSourceType  ,
             othersourceofincome."PayPeriod" OtherIncomeSourcePayPeriod  ,
             othersourceofincome."GrossIncome" OtherGrossIncomeValue  ,
             othersourceofincome."WeekWorkingHours" OtherIncomeSourceWorkingHours  ,
             othersourceofincome."SourceofIncomeDescription" OtherSourceOfIncomeDescription  ,
             othersourceofincome."SourceOfIncomeName" OtherSourceOfIncomeName  ,
             customeraddress."Address_id" Address_id  ,
             customeraddress."Type_id" Type_id  ,
             address."Region_id" Region_id  ,
             address."City_id" City_id  ,
             address."addressLine1" AddressLine1  ,
             address."addressLine2" AddressLine2  ,
             address."addressLine3" AddressLine3  ,
             address."zipCode" ZipCode  ,
             address."state" state  ,
             address."cityName" "city"  ,
             address."country" Country  
        FROM "employementdetails" employementdetails
               LEFT JOIN "othersourceofincome" othersourceofincome   ON employementdetails."Customer_id" = othersourceofincome."Customer_id"
               LEFT JOIN "customeraddress" customeraddress   ON ( customeraddress."Customer_id" = employementdetails."Customer_id"
               AND customeraddress."Type_id" = ' ADR_TYPE_WORK ' )
               JOIN "address" address   ON address."id" = customeraddress."Address_id"
       WHERE  employementdetails."Customer_id" = v_Customer_id 
        FETCH FIRST 1 ROWS ONLY ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure get_customerAlertEntitlementForAlertTypes



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "get_customerAlertEntitlementForAlertTypes" 
(
  "_alertTypes" IN NVARCHAR2,"dbxcustomeralertentitlement" out SYS_REFCURSOR
)
AS
    

BEGIN

   
   OPEN  "dbxcustomeralertentitlement" FOR
      SELECT "dbxcustomeralertentitlement"."AlertTypeId" "alertTypeId"  ,
             "dbxcustomeralertentitlement"."Customer_id" "customerId"  ,
             "dbxcustomeralertentitlement"."Value1" "value1"  ,
             "dbxcustomeralertentitlement"."Value2" "value2"  ,
             "dbxcustomeralertentitlement"."AccountId" "accountId"  
        FROM "dbxcustomeralertentitlement" 
       WHERE  FIND_IN_SET("dbxcustomeralertentitlement"."AlertTypeId", "_alertTypes") <> 0 ;
      


END;
/
--  DDL for Procedure get_customerids_for_backendidentifiers



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "get_customerids_for_backendidentifiers" 
(
  v__backendIds IN VARCHAR2, "backendidentifier" OUT SYS_REFCURSOR
)
AS
   

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   OPEN  "backendidentifier" FOR
      SELECT "backendidentifier"."Customer_id" customerId  ,
             "backendidentifier"."BackendId" backendId  
        FROM "backendidentifier" 
       WHERE  "backendidentifier"."BackendId" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v__backendIds)));
      --DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure get_entitlements_for_customerids_and_alerttypes



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "get_entitlements_for_customerids_and_alerttypes" 
(
  v__alertTypes IN VARCHAR2,
  v__customerIds IN VARCHAR2,
  "dbxcustomeralertentitlement" OUT SYS_REFCURSOR
)
AS
   

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   OPEN  "dbxcustomeralertentitlement" FOR
      SELECT "dbxcustomeralertentitlement"."AlertTypeId" alertTypeId  ,
             "dbxcustomeralertentitlement"."Customer_id" customerId  ,
             "dbxcustomeralertentitlement"."Value1" value1  ,
             "dbxcustomeralertentitlement"."Value2" value2  ,
             "dbxcustomeralertentitlement"."AccountId" accountId  
        FROM "dbxcustomeralertentitlement" 
       WHERE  "dbxcustomeralertentitlement"."AlertTypeId" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v__alertTypes)))
                AND "dbxcustomeralertentitlement"."Customer_id"  IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v__customerIds)));
      --DBMS_SQL.RETURN_RESULT("dbxcustomeralertentitlement");

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure get_monetary_actions_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "get_monetary_actions_proc" 
(
  v__featureActions IN VARCHAR2,
  v_cursor OUT SYS_REFCURSOR
)
AS
   v_monetaryActionList CLOB;


BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   SELECT LISTAGG(CAST("id" AS VARCHAR2(255)), ', 
') 

     INTO v_monetaryActionList
     FROM "featureaction" 
    WHERE  ( "featureaction"."Type_id" = ' MONETARY '
             AND "featureaction"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v__featureActions))) );
   OPEN  v_cursor FOR
      SELECT v_monetaryActionList monetaryActions  
        FROM DUAL  ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure get_organisation_employees_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "get_organisation_employees_proc" 
(
  v__organisationId IN VARCHAR2,
  v__filterColumnName IN VARCHAR2,
  v__filterColumnValue IN VARCHAR2,
   v_cursor OUT SYS_REFCURSOR
)
AS
   v_org_employees_list CLOB;
   v_customersList CLOB;


BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   SELECT LISTAGG(CAST("Customer_id" AS VARCHAR2(255)), ', 
') 

     INTO v_org_employees_list
     FROM "organisationemployees" 
    WHERE  ( "organisationemployees"."Organization_id" = v__organisationId );
   v_org_employees_list := CASE 
                                WHEN v_org_employees_list IS NULL THEN ' '
   ELSE v_org_employees_list
      END ;
   IF v__filterColumnName = ' Ssn ' THEN

   BEGIN
      SELECT LISTAGG(CAST("id" AS VARCHAR2(255)), ', 
') 

        INTO v_customersList
        FROM "customer" 
       WHERE  "customer"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v_org_employees_list)))
                AND "customer"."Ssn" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v__filterColumnValue)));
      v_customersList := CASE 
                              WHEN ( v_customersList IS NULL ) THEN ''
      ELSE v_customersList
         END ;

   END;
   END IF;
   OPEN  v_cursor FOR
      SELECT v_customersList employeesList  
        FROM DUAL  ;
--      DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure get_valid_customeraccounts_get_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "get_valid_customeraccounts_get_proc" 
(
  v__customeraccountsCSV IN VARCHAR2,
  v__customerId IN VARCHAR2,
  v_cursor OUT SYS_REFCURSOR
)
AS
   v_list VARCHAR2(4000) := '';
   v_validcorecustomersCSV VARCHAR2(4000) := '';
   v_next VARCHAR2(4000) := '';
   v_nextlen NUMBER(10,0);
   v_initiallength NUMBER(10,0);
   v_value VARCHAR2(4000) := '';
   v_customers CLOB := '';
   v_stringLength VARCHAR2(4000) := '';


BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   SELECT v__customeraccountsCSV 

     INTO v_list
     FROM DUAL ;
   v_validcorecustomersCSV := '' ;
   WHILE ( 1 = 1 ) 
   LOOP 

      BEGIN
         IF LENGTHB(LTRIM(RTRIM(v_list))) = 0
           OR v_list IS NULL THEN
          EXIT;
         END IF;
         v_initiallength := LENGTH(v_list) ;
         v_next := UTILS.SUBSTRING_INDEX(v_list, ', 
', 1) ;
         v_nextlen := LENGTH(v_next) ;
         v_value := LTRIM(RTRIM(v_next)) ;
         SELECT LISTAGG(CAST("customeraccounts"."Account_id" AS VARCHAR2(255)), ', 
') 

           INTO v_customers
           FROM "customeraccounts" 
          WHERE  "customeraccounts"."Account_id" = v_value
                   AND "customeraccounts"."Customer_id" = v__customerId;
         IF NVL(v_customers, ' ') = ' '
           OR v_customers = ' ' THEN

         BEGIN
            IF v_validcorecustomersCSV = ' ' THEN
             v_validcorecustomersCSV := v_value ;
            ELSE
               v_validcorecustomersCSV := (v_validcorecustomersCSV) || (', 
') || (v_value) ;
            END IF;

         END;
         END IF;
         v_stringLength := LENGTH(v_list) ;
         IF v_nextlen + 2 < v_initiallength THEN
          v_list := SUBSTR(v_list, v_nextlen + 2, v_stringLength - v_nextlen - 1) ;
         ELSE
            v_list := ' ' ;
         END IF;

      END;
   END LOOP;
   OPEN  v_cursor FOR
      SELECT v_validcorecustomersCSV validAccounts  
        FROM DUAL  ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure get_valid_orgaccounts_list_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "get_valid_orgaccounts_list_proc" 
(
  v__accountsList IN VARCHAR2,
  v_cursor OUT SYS_REFCURSOR
)
AS
   v_list VARCHAR2(4000);
   v_NONDBXAccounts VARCHAR2(4000);
   v_tableAccountsList VARCHAR2(4000);
   v_next NUMBER(19,0);
   v_nextlen NUMBER(19,0);
   v_value VARCHAR2(4000);
   v_DBXAccounts VARCHAR2(4000);


BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   SELECT v__accountsList 

     INTO v_list
     FROM DUAL ;
   v_NONDBXAccounts := '' ;
   SELECT LISTAGG(CAST("accounts"."Account_id" AS VARCHAR2(255)), ', 
') 

     INTO v_tableAccountsList
     FROM "accounts" ;
   WHILE ( 1 = 1 ) 
   LOOP 

      BEGIN
         IF LENGTH(LTRIM(RTRIM(v_list))) = 0
           OR v_list IS NULL THEN
          EXIT;
         END IF;
         v_next := UTILS.SUBSTRING_INDEX(v_list, ', 
', 1) ;
         v_nextlen := LENGTH(v_next) ;
         v_value := LTRIM(RTRIM(v_next)) ;
         IF UTILS.FIND_IN_SET(v_value,v_tableAccountsList)=0 THEN
          IF v_NONDBXAccounts = 0 THEN
          v_NONDBXAccounts := v_value ;
         ELSE
            v_NONDBXAccounts := (v_NONDBXAccounts) || (', 
') || (v_value) ;
         END IF;
         END IF;
         v_list := REPLACE(v_list, SUBSTR(v_list, 1, v_nextlen + 1), '');

      END;
   END LOOP;
   SELECT LISTAGG(CAST("accounts"."Account_id" AS VARCHAR2(255)), ', 
') 

     INTO v_DBXAccounts
     FROM "accounts" 
    WHERE  "Account_id" IN ( SELECT column_value from TABLE(UTILS.STRING_SPLIT( v__accountsList)))
             AND ( CASE 
                        WHEN ("Organization_id") IS NULL THEN 1
           ELSE 0
              END > 0
             OR "Organization_id" = ' ' );
   IF ( CASE 
             WHEN (v_DBXAccounts) IS NULL THEN 1
   ELSE 0
      END <> 0
     OR v_DBXAccounts = ' ' ) THEN
    OPEN  v_cursor FOR
      SELECT v_NONDBXAccounts accountsList  
        FROM DUAL  ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);
   ELSE
      IF ( CASE 
                WHEN (v_NONDBXAccounts) IS NULL THEN 1
      ELSE 0
         END <> 0
        OR v_NONDBXAccounts = ' ' ) THEN
       OPEN  v_cursor FOR
         SELECT v_DBXAccounts accountsList  
           FROM DUAL  ;
         --DBMS_SQL.RETURN_RESULT(v_cursor);
      ELSE
         OPEN  v_cursor FOR
            SELECT (v_DBXAccounts) || (', 
') || (v_NONDBXAccounts) accountsList  
              FROM DUAL  ;
            --DBMS_SQL.RETURN_RESULT(v_cursor);
      END IF;
   END IF;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure get_validcorecustomerslist_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "get_validcorecustomerslist_proc" 
(
  "_coreCustomersCSV" IN NVARCHAR2,
  "companyLegalUnitId" IN NVARCHAR2,
  "records" out SYS_REFCURSOR
)
AS
   v_list NVARCHAR2(2000) := '';
   v_validcorecustomersCSV NVARCHAR2(2000) := '';
   v_next NVARCHAR2(2000) := '';
   v_nextlen NUMBER(10,0);
   v_initiallength NUMBER(10,0);
   v_value NVARCHAR2(2000) := '';
   v_customers NVARCHAR2(2000) := '';
   v_stringLength NVARCHAR2(2000) := '';


BEGIN
 SELECT "_coreCustomersCSV" 

     INTO v_list
     FROM DUAL ;
   v_validcorecustomersCSV := '' ;
   WHILE ( 1 = 1 ) 
   LOOP 

      BEGIN
         IF LENGTH(LTRIM(RTRIM(v_list))) = 0
           OR v_list IS NULL OR v_list = '' THEN
          EXIT;
         END IF;
         v_initiallength := LENGTH(v_list) ;
         v_next := SUBSTRING_INDEX(v_list, ', 
', 1) ;
         v_nextlen := LENGTH(v_next) ;
         v_value := LTRIM(RTRIM(v_next)) ;
         SELECT listagg(CAST("contractcorecustomers"."id" AS NVARCHAR2(2000)), ', 
') 

           INTO v_customers
           FROM "contractcorecustomers" 
          WHERE  "contractcorecustomers"."coreCustomerId" = v_value and "contractcorecustomers"."companyLegalUnit" = "companyLegalUnitId";
           IF v_customers is null THEN

       BEGIN
            IF v_validcorecustomersCSV is null THEN
             v_validcorecustomersCSV := v_value ;
            ELSE
               v_validcorecustomersCSV := (v_validcorecustomersCSV) || (', 
') || (v_value) ;
               EXIT;
            END IF;

         END;
         END IF;
         v_stringLength := LENGTH(v_list) ;
         IF v_nextlen + 2 < v_initiallength THEN
          v_list := SUBSTR(v_list, v_nextlen + 2, v_stringLength - v_nextlen - 1) ;
         ELSE
            v_list := '' ;
         END IF;
      END;
   END LOOP;
   OPEN  "records" FOR
      SELECT v_validcorecustomersCSV "validCustomers"  
        FROM DUAL  ;


END;
/
--  DDL for Procedure getAllBackendIdentifiers



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "getAllBackendIdentifiers" 
(
   "backendidentifier" OUT SYS_REFCURSOR
)
AS
BEGIN

   
   OPEN  "backendidentifier" FOR
      SELECT "backendidentifier"."Customer_id" "customerId"  ,
             "backendidentifier"."BackendId" "backendId" 
        FROM "backendidentifier"  ;
      
END;
/
--  DDL for Procedure getAllCustomersAlertFrequency_Sp_alertlevel



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "getAllCustomersAlertFrequency_Sp_alertlevel" 
(
  "startTimePrev" IN NVARCHAR2,
  "startTimeCurr" IN NVARCHAR2,
  "endTimePrev" IN NVARCHAR2,
  "endTimeCurr" IN NVARCHAR2,
  "scheduleDayPrev" IN NVARCHAR2,
  "scheduleDayCurr" IN NVARCHAR2,
  "scheduleDatePrev" IN NUMBER,
  "scheduleDateCurr" IN NUMBER,
  "isLastDate" IN NVARCHAR2,
  "isPrevDateLastDate" IN NVARCHAR2,
  "customeralertfrequency" out SYS_REFCURSOR
)
AS
   

BEGIN

   IF ( "isLastDate" = 'true' ) THEN
    
   BEGIN
      IF ( "isPrevDateLastDate" = 'true' ) THEN
       
      BEGIN
         OPEN  "customeralertfrequency" FOR
            SELECT * 
              FROM "customeralertfrequency" 
             WHERE  ( "customeralertfrequency"."frequencyTime" > "startTimePrev"
                      AND "customeralertfrequency"."frequencyTime" <= "endTimePrev"
                      AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDayPrev"
                      AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                      OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                      OR ( "customeralertfrequency"."frequencyValue" >= "scheduleDatePrev"
                      AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) ) )
                      OR ( "customeralertfrequency"."frequencyTime" >= "startTimeCurr"
                      AND "customeralertfrequency"."frequencyTime" <= "endTimeCurr"
                      AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDayCurr"
                      AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                      OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                      OR ( "customeralertfrequency"."frequencyValue" = "scheduleDateCurr"
                      AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) ) ) ;
            
      
      END;
      ELSE
      
      BEGIN
         OPEN  "customeralertfrequency" FOR
            SELECT * 
              FROM "customeralertfrequency" 
             WHERE  ( "customeralertfrequency"."frequencyTime" > "startTimePrev"
                      AND "customeralertfrequency"."frequencyTime" <= "endTimePrev"
                      AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDayPrev"
                      AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                      OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                      OR ( "customeralertfrequency"."frequencyValue" = "scheduleDatePrev"
                      AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) ) )
                      OR ( "customeralertfrequency"."frequencyTime" >= "startTimeCurr"
                      AND "customeralertfrequency"."frequencyTime" <= "endTimeCurr"
                      AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDayCurr"
                      AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                      OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                      OR ( "customeralertfrequency"."frequencyValue" >= "scheduleDateCurr"
                      AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) ) ) ;
            
      
      END;
      END IF;
   
   END;
   ELSE
   
   BEGIN
      OPEN  "customeralertfrequency" FOR
         SELECT * 
           FROM "customeralertfrequency" 
          WHERE  ( "customeralertfrequency"."frequencyTime" > "startTimePrev"
                   AND "customeralertfrequency"."frequencyTime" <= "endTimePrev"
                   AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDayPrev"
                   AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                   OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                   OR ( "customeralertfrequency"."frequencyValue" = "scheduleDatePrev"
                   AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) ) )
                   OR ( "customeralertfrequency"."frequencyTime" >= "startTimeCurr"
                   AND "customeralertfrequency"."frequencyTime" <= "endTimeCurr"
                   AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDayCurr"
                   AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                   OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                   OR ( "customeralertfrequency"."frequencyValue" = "scheduleDateCurr"
                   AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) ) ) ;
         
   
   END;
   END IF;


END;
/
--  DDL for Procedure getAllCustomersAlertFrequency_Sp_categorylevel



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "getAllCustomersAlertFrequency_Sp_categorylevel" 
(
  "startTimePrev" IN NVARCHAR2,
  "startTimeCurr" IN NVARCHAR2,
  "endTimePrev" IN NVARCHAR2,
  "endTimeCurr" IN NVARCHAR2,
  "scheduleDayPrev" IN NVARCHAR2,
  "scheduleDayCurr" IN NVARCHAR2,
  "scheduleDatePrev" IN NUMBER,
  "scheduleDateCurr" IN NUMBER,
  "isLastDate" IN NVARCHAR2,
  "isPrevDateLastDate" IN NVARCHAR2,
  "customeralertfrequency" out SYS_REFCURSOR
)
AS
   

BEGIN

   IF ( "isLastDate" = 'true' ) THEN
    
   BEGIN
      IF ( "isPrevDateLastDate" = 'true' ) THEN
       
      BEGIN
         OPEN  "customeralertfrequency" FOR
            SELECT "customeralertfrequency"."customerId" ,
                   "customeralertfrequency"."alertCategoryId" ,
                   "dbxalerttype"."id" "alertTypeId"  ,
                   "customeralertfrequency"."accountId" ,
                   "alertsubtype"."id" "alertSubTypeId"  
              FROM "customeralertfrequency" 
                     JOIN "dbxalerttype"    ON "customeralertfrequency"."alertCategoryId" = "dbxalerttype"."AlertCategoryId"
                     JOIN "alertsubtype"    ON "dbxalerttype"."id" = "alertsubtype"."AlertTypeId"
             WHERE  ( ( "customeralertfrequency"."frequencyTime" > "startTimePrev"
                      AND "customeralertfrequency"."frequencyTime" <= "endTimePrev"
                      AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDayPrev"
                      AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                      OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                      OR ( "customeralertfrequency"."frequencyValue" >= "scheduleDatePrev"
                      AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) ) )
                      OR ( "customeralertfrequency"."frequencyTime" >= "startTimeCurr"
                      AND "customeralertfrequency"."frequencyTime" <= "endTimeCurr"
                      AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDayCurr"
                      AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                      OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                      OR ( "customeralertfrequency"."frequencyValue" = "scheduleDateCurr"
                      AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) ) ) )
                      AND "alertsubtype"."defaultFrequencyId" IS NOT NULL ;
            
      
      END;
      ELSE
      
      BEGIN
         OPEN  "customeralertfrequency" FOR
            SELECT "customeralertfrequency"."customerId" ,
                   "customeralertfrequency"."alertCategoryId" ,
                   "dbxalerttype"."id" "alertTypeId"  ,
                   "customeralertfrequency"."accountId" ,
                   "alertsubtype"."id" "alertSubTypeId"  
              FROM "customeralertfrequency" 
                     JOIN "dbxalerttype"    ON "customeralertfrequency"."alertCategoryId" = "dbxalerttype"."AlertCategoryId"
                     JOIN "alertsubtype"    ON "dbxalerttype"."id" = "alertsubtype"."AlertTypeId"
             WHERE  ( ( "customeralertfrequency"."frequencyTime" > "startTimePrev"
                      AND "customeralertfrequency"."frequencyTime" <= "endTimePrev"
                      AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDayPrev"
                      AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                      OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                      OR ( "customeralertfrequency"."frequencyValue" = "scheduleDatePrev"
                      AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) ) )
                      OR ( "customeralertfrequency"."frequencyTime" >= "startTimeCurr"
                      AND "customeralertfrequency"."frequencyTime" <= "endTimeCurr"
                      AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDayCurr"
                      AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                      OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                      OR ( "customeralertfrequency"."frequencyValue" >= "scheduleDateCurr"
                      AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) ) ) )
                      AND "alertsubtype"."defaultFrequencyId" IS NOT NULL ;
            
      
      END;
      END IF;
   
   END;
   ELSE
   
   BEGIN
      OPEN  "customeralertfrequency" FOR
         SELECT "customeralertfrequency"."customerId" ,
                "customeralertfrequency"."alertCategoryId" ,
                "dbxalerttype"."id" "alertTypeId"  ,
                "customeralertfrequency"."accountId" ,
                "alertsubtype"."id" "alertSubTypeId"  
           FROM "customeralertfrequency" 
                  JOIN "dbxalerttype"    ON "customeralertfrequency"."alertCategoryId" = "dbxalerttype"."AlertCategoryId"
                  JOIN "alertsubtype"    ON "dbxalerttype"."id" = "alertsubtype"."AlertTypeId"
          WHERE  ( ( "customeralertfrequency"."frequencyTime" > "startTimePrev"
                   AND "customeralertfrequency"."frequencyTime" <= "endTimePrev"
                   AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDayPrev"
                   AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                   OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                   OR ( "customeralertfrequency"."frequencyValue" = "scheduleDatePrev"
                   AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) ) )
                   OR ( "customeralertfrequency"."frequencyTime" >= "startTimeCurr"
                   AND "customeralertfrequency"."frequencyTime" <= "endTimeCurr"
                   AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDayCurr"
                   AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                   OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                   OR ( "customeralertfrequency"."frequencyValue" = "scheduleDateCurr"
                   AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) ) ) )
                   AND "alertsubtype"."defaultFrequencyId" IS NOT NULL ;
         
   
   END;
   END IF;


END;
/
--  DDL for Procedure getAllCustomersAlertFrequency_Sp_grouplevel



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "getAllCustomersAlertFrequency_Sp_grouplevel" 
(
  "startTimePrev" IN NVARCHAR2,
  "startTimeCurr" IN NVARCHAR2,
  "endTimePrev" IN NVARCHAR2,
  "endTimeCurr" IN NVARCHAR2,
  "scheduleDayPrev" IN NVARCHAR2,
  "scheduleDayCurr" IN NVARCHAR2,
  "scheduleDatePrev" IN NUMBER,
  "scheduleDateCurr" IN NUMBER,
  "isLastDate" IN NVARCHAR2,
  "isPrevDateLastDate" IN NVARCHAR2,
  "customeralertfrequency" out SYS_REFCURSOR
)
AS
   

BEGIN

   IF ( "isLastDate" = 'true' ) THEN
    
   BEGIN
      IF ( "isPrevDateLastDate" = 'true' ) THEN
       
      BEGIN
         OPEN  "customeralertfrequency" FOR
            SELECT "customeralertfrequency"."customerId",
                   "customeralertfrequency"."alertCategoryId" ,
                   "customeralertfrequency"."alertTypeId" ,
                   "customeralertfrequency"."accountId" ,
                   "alertsubtype"."id" "alertSubTypeId"  
              FROM "customeralertfrequency" 
                     JOIN "alertsubtype"    ON "customeralertfrequency"."alertTypeId" = "alertsubtype"."AlertTypeId"
             WHERE  ( ( "customeralertfrequency"."frequencyTime" > "startTimePrev"
                      AND "customeralertfrequency"."frequencyTime" <= "endTimePrev"
                      AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDayPrev"
                      AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                      OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                      OR ( "customeralertfrequency"."frequencyValue" >= "scheduleDatePrev"
                      AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) ) )
                      OR ( "customeralertfrequency"."frequencyTime" >= "startTimeCurr"
                      AND "customeralertfrequency"."frequencyTime" <= "endTimeCurr"
                      AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDayCurr"
                      AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                      OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                      OR ( "customeralertfrequency"."frequencyValue" = "scheduleDateCurr"
                      AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) ) ) )
                      AND "alertsubtype"."defaultFrequencyId" IS NOT NULL ;
            
      
      END;
      ELSE
      
      BEGIN
         OPEN  "customeralertfrequency" FOR
            SELECT "customeralertfrequency"."customerId" ,
                   "customeralertfrequency"."alertCategoryId" ,
                   "customeralertfrequency"."alertTypeId" ,
                   "customeralertfrequency"."accountId" ,
                   "alertsubtype"."id" "alertSubTypeId" 
              FROM "customeralertfrequency" 
                     JOIN "alertsubtype"    ON "customeralertfrequency"."alertTypeId" = "alertsubtype"."AlertTypeId"
             WHERE  ( ( "customeralertfrequency"."frequencyTime" > "startTimePrev"
                      AND "customeralertfrequency"."frequencyTime" <= "endTimePrev"
                      AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDayPrev"
                      AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                      OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                      OR ( "customeralertfrequency"."frequencyValue" = "scheduleDatePrev"
                      AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) ) )
                      OR ( "customeralertfrequency"."frequencyTime" >= "startTimeCurr"
                      AND "customeralertfrequency"."frequencyTime" <= "endTimeCurr"
                      AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDayCurr"
                      AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                      OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                      OR ( "customeralertfrequency"."frequencyValue" >= "scheduleDateCurr"
                      AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) ) ) )
                      AND "alertsubtype"."defaultFrequencyId" IS NOT NULL ;
            
      
      END;
      END IF;
   
   END;
   ELSE
   
   BEGIN
      OPEN  "customeralertfrequency" FOR
         SELECT "customeralertfrequency"."customerId" ,
                "customeralertfrequency"."alertCategoryId" ,
                "customeralertfrequency"."alertTypeId" ,
                "customeralertfrequency"."accountId" ,
                "alertsubtype"."id" "alertSubTypeId"  
           FROM "customeralertfrequency" 
                  JOIN "alertsubtype"    ON "customeralertfrequency"."alertTypeId" = "alertsubtype"."AlertTypeId"
          WHERE  ( ( "customeralertfrequency"."frequencyTime" > "startTimePrev"
                   AND "customeralertfrequency"."frequencyTime" <= "endTimePrev"
                   AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDayPrev"
                   AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                   OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                   OR ( "customeralertfrequency"."frequencyValue" = "scheduleDatePrev"
                   AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) ) )
                   OR ( "customeralertfrequency"."frequencyTime" >= "startTimeCurr"
                   AND "customeralertfrequency"."frequencyTime" <= "endTimeCurr"
                   AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDayCurr"
                   AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                   OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                   OR ( "customeralertfrequency"."frequencyValue" = "scheduleDateCurr"
                   AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) ) ) )
                   AND "alertsubtype"."defaultFrequencyId" IS NOT NULL ;
         
   
   END;
   END IF;


END;
/
--  DDL for Procedure getCustomerCommunicationData



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "getCustomerCommunicationData" 
(
  "_CUSTOMERS" IN NVARCHAR2,
  "customercommunication" out SYS_REFCURSOR
)
AS
   

BEGIN

   
   OPEN  "customercommunication" FOR
      SELECT "customercommunication"."Customer_id" "custid"  ,
             "customercommunication"."Value" "value"  ,
             "customercommunication"."Type_id" "type" 
        FROM "customercommunication" 
       WHERE  "customercommunication"."isPrimary" = 1
                AND FIND_IN_SET("customercommunication"."Customer_id", "_CUSTOMERS") <> 0 ;
      


END;
/
--  DDL for Procedure getCustomerInfo



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "getCustomerInfo" 
(
  "_CUSTOMERS" IN NVARCHAR2,"customer" out SYS_REFCURSOR
)
AS
   

BEGIN

   OPEN  "customer" FOR
      SELECT "customer"."id" "custid"  ,
             "customer"."FirstName" "fname"  ,
             "customer"."LastName" "lname"  ,
             "customer"."CountryCode" "country"  
        FROM "customer"
       WHERE  FIND_IN_SET("customer"."id", "_CUSTOMERS") <> 0 ;
     


END;
/
--  DDL for Procedure getCustomersAlertFrequency_Sp_alertlevel



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "getCustomersAlertFrequency_Sp_grouplevel" 
(
  "startTime" IN NVARCHAR2,
  "endTime" IN NVARCHAR2,
  "scheduleDay" IN NVARCHAR2,
  "scheduleDate" IN NUMBER,
  "isLastDate" IN NVARCHAR2,
  "customeralertfrequency" out SYS_REFCURSOR
)
AS
   

BEGIN

   IF ( "isLastDate" = 'true' ) THEN
    
   BEGIN
      OPEN  "customeralertfrequency" FOR
         SELECT "customeralertfrequency"."customerId" ,
                "customeralertfrequency"."alertCategoryId" ,
                "customeralertfrequency"."alertTypeId" ,
                "customeralertfrequency"."accountId" ,
                "alertsubtype"."id" "alertSubTypeId"  
           FROM "customeralertfrequency" 
                  JOIN "alertsubtype"    ON "customeralertfrequency"."alertTypeId" = "alertsubtype"."AlertTypeId"
          WHERE  "customeralertfrequency"."frequencyTime" > "startTime"
                   AND "customeralertfrequency"."frequencyTime" <= "endTime"
                   AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDay"
                   AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                   OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                   OR ( "customeralertfrequency"."frequencyValue" >= "scheduleDate"
                   AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) )
                   AND "alertsubtype"."defaultFrequencyId" IS NOT NULL ;
       
   
   END;
   ELSE
   
   BEGIN
      OPEN  "customeralertfrequency" FOR
         SELECT "customeralertfrequency"."customerId" ,
                "customeralertfrequency"."alertCategoryId" ,
                "customeralertfrequency"."alertTypeId" ,
                "customeralertfrequency"."accountId" ,
                "alertsubtype"."id" "alertSubTypeId"  
           FROM "customeralertfrequency" 
                  JOIN "alertsubtype"    ON "customeralertfrequency"."alertTypeId" = "alertsubtype"."AlertTypeId"
          WHERE  "customeralertfrequency"."frequencyTime" > "startTime"
                   AND "customeralertfrequency"."frequencyTime" <= "endTime"
                   AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDay"
                   AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                   OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                   OR ( "customeralertfrequency"."frequencyValue" = "scheduleDate"
                   AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) )
                   AND "alertsubtype"."defaultFrequencyId" IS NOT NULL ;
         
   
   END;
   END IF;


END;
/
--  DDL for Procedure getCustomersAlertFrequency_Sp_categorylevel



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "getCustomersAlertFrequency_Sp_categorylevel" 
(
  "startTime" IN NVARCHAR2,
  "endTime" IN NVARCHAR2,
  "scheduleDay" IN NVARCHAR2,
  "scheduleDate" IN NUMBER,
  "isLastDate" IN NVARCHAR2,
  "records" out SYS_REFCURSOR
)
AS
   

BEGIN

   IF ( "isLastDate" = 'true' ) THEN
    
   BEGIN
      OPEN  "records" FOR
         SELECT "customeralertfrequency"."customerId" ,
                "customeralertfrequency"."alertCategoryId" ,
                "dbxalerttype"."id" "alertTypeId"  ,
                "customeralertfrequency"."accountId" ,
                "alertsubtype"."id" "alertSubTypeId"  
           FROM "customeralertfrequency" 
                  JOIN "dbxalerttype"    ON "customeralertfrequency"."alertCategoryId" = "dbxalerttype"."AlertCategoryId"
                  JOIN "alertsubtype"    ON "dbxalerttype"."id" = "alertsubtype"."AlertTypeId"
          WHERE  "customeralertfrequency"."frequencyTime" > "startTime"
                   AND "customeralertfrequency"."frequencyTime" <= "endTime"
                   AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDay"
                   AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                   OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                   OR ( "customeralertfrequency"."frequencyValue" >= "scheduleDate"
                   AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) )
                   AND "alertsubtype"."defaultFrequencyId" IS NOT NULL ;
         
   
   END;
   ELSE
   
   BEGIN
      OPEN  "records" FOR
         SELECT "customeralertfrequency"."customerId" ,
                "customeralertfrequency"."alertCategoryId" ,
                "dbxalerttype"."id" "alertTypeId"  ,
                "customeralertfrequency"."accountId" ,
                "alertsubtype"."id" "alertSubTypeId"  
           FROM "customeralertfrequency" 
                  JOIN "dbxalerttype"    ON "customeralertfrequency"."alertCategoryId" = "dbxalerttype"."AlertCategoryId"
                  JOIN "alertsubtype"    ON "dbxalerttype"."id" = "alertsubtype"."AlertTypeId"
          WHERE  "customeralertfrequency"."frequencyTime" > "startTime"
                   AND "customeralertfrequency"."frequencyTime" <= "endTime"
                   AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDay"
                   AND "customeralertfrequency"."alertFrequencyId" = 'WEEKLY' )
                   OR "customeralertfrequency"."alertFrequencyId" = 'DAILY'
                   OR ( "customeralertfrequency"."frequencyValue" = "scheduleDate"
                   AND "customeralertfrequency"."alertFrequencyId" = 'MONTHLY' ) )
                   AND "alertsubtype"."defaultFrequencyId" IS NOT NULL ;
         
   
   END;
   END IF;


END;
/
--  DDL for Procedure getCustomersAlertFrequency_Sp_grouplevel



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "getCustomersAlertFrequency_Sp_grouplevel" 
(
  "startTime" IN NVARCHAR2,
  "endTime" IN NVARCHAR2,
  "scheduleDay" IN NVARCHAR2,
  "scheduleDate" IN NUMBER,
  "isLastDate" IN NVARCHAR2,
  "customeralertfrequency" out SYS_REFCURSOR
)
AS
   

BEGIN

   IF ( "isLastDate" = ' true ' ) THEN
    
   BEGIN
      OPEN  "customeralertfrequency" FOR
         SELECT "customeralertfrequency"."customerId" ,
                "customeralertfrequency"."alertCategoryId" ,
                "customeralertfrequency"."alertTypeId" ,
                "customeralertfrequency"."accountId" ,
                "alertsubtype"."id" "alertSubTypeId"  
           FROM "customeralertfrequency" 
                  JOIN "alertsubtype"    ON "customeralertfrequency"."alertTypeId" = "alertsubtype"."AlertTypeId"
          WHERE  "customeralertfrequency"."frequencyTime" > "startTime"
                   AND "customeralertfrequency"."frequencyTime" <= "endTime"
                   AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDay"
                   AND "customeralertfrequency"."alertFrequencyId" = ' WEEKLY ' )
                   OR "customeralertfrequency"."alertFrequencyId" = ' DAILY '
                   OR ( "customeralertfrequency"."frequencyValue" >= "scheduleDate"
                   AND "customeralertfrequency"."alertFrequencyId" = ' MONTHLY ' ) )
                   AND "alertsubtype"."defaultFrequencyId" IS NOT NULL ;
       
   
   END;
   ELSE
   
   BEGIN
      OPEN  "customeralertfrequency" FOR
         SELECT "customeralertfrequency"."customerId" ,
                "customeralertfrequency"."alertCategoryId" ,
                "customeralertfrequency"."alertTypeId" ,
                "customeralertfrequency"."accountId" ,
                "alertsubtype"."id" "alertSubTypeId"  
           FROM "customeralertfrequency" 
                  JOIN "alertsubtype"    ON "customeralertfrequency"."alertTypeId" = "alertsubtype"."AlertTypeId"
          WHERE  "customeralertfrequency"."frequencyTime" > "startTime"
                   AND "customeralertfrequency"."frequencyTime" <= "endTime"
                   AND ( ( "customeralertfrequency"."frequencyValue" = "scheduleDay"
                   AND "customeralertfrequency"."alertFrequencyId" = ' WEEKLY ' )
                   OR "customeralertfrequency"."alertFrequencyId" = ' DAILY '
                   OR ( "customeralertfrequency"."frequencyValue" = "scheduleDate"
                   AND "customeralertfrequency"."alertFrequencyId" = ' MONTHLY ' ) )
                   AND "alertsubtype"."defaultFrequencyId" IS NOT NULL ;
         
   
   END;
   END IF;


END;
/
--  DDL for Procedure getCustomersIdFromCoreId



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "getCustomersIdFromCoreId" 
(
  "_CORECUSTOMERS" IN NVARCHAR2,
  "backendidentifier" out SYS_REFCURSOR
)
AS
   

BEGIN

   OPEN  "backendidentifier" FOR
      SELECT "backendidentifier"."Customer_id" "custid"  ,
             "backendidentifier"."BackendId" "corecustid"  
        FROM "backendidentifier"
       WHERE  FIND_IN_SET("backendidentifier"."BackendId", "_CORECUSTOMERS") <> 0 ;
      


END;
/
--  DDL for Procedure getRequestApprovers_proc



create or replace NONEDITIONABLE PROCEDURE         "getRequestApprovers_proc" 
(
  "_requestId" IN NVARCHAR2,
  "_status" IN NVARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
  v_isGroupMatrix NVARCHAR2(50);
BEGIN
  dbms_output.put_line('hi');
   IF "_status" IS NULL
     OR "_status" = ' ' THEN
   BEGIN
   SELECT "isGroupMatrix"
  INTO v_isGroupMatrix
     FROM "bbrequest" 
    WHERE  "bbrequest"."requestId" = "_requestId";
   IF v_isGroupMatrix = ' 1 ' THEN
    OPEN  "records" FOR
    
SELECT "_requestId"  "requestId",
"csg"."customerId" "approvers"  ,
                   "c"."FirstName" "FirstName",
                   "c"."LastName" "LastName"       
        FROM "customersignatorygroup" "csg" 
    LEFT JOIN "customer" "c"   ON "c"."id" = "csg"."customerId"
       WHERE  FIND_IN_SET("csg"."signatoryGroupId", ( SELECT LISTAGG(REPLACE(REPLACE(REPLACE("signatorygrouprequestmatrix"."pendingGroupList", ' ] ', ' '), ' [ ', ' '), ' "', ' '), ',') 
                                                             FROM "signatorygrouprequestmatrix" 
                                                              WHERE  "signatorygrouprequestmatrix"."requestId"="_requestId"
                                                            AND "signatorygrouprequestmatrix"."isApproved"='0' )) > 0 ;
   ELSE
    OPEN  "records" FOR
      SELECT MIN("bb"."requestId")  "requestId"  ,
             "cam"."customerId" "approvers"  ,
             MIN("c"."FirstName")  "FirstName"  ,
             MIN("c"."LastName")  "LastName"  
        FROM "bbrequest" "bb"
               CROSS JOIN "requestapprovalmatrix" "ram"
               CROSS JOIN "customerapprovalmatrix" "cam"
               CROSS JOIN "customer" "c"
       WHERE  "bb"."requestId" = "ram"."requestId"
                AND "ram"."approvalMatrixId" = "cam"."approvalMatrixId"
                AND "cam"."customerId" = "c"."id"
                AND CAST("bb"."requestId" AS NVARCHAR2(2000)) = "_requestId"
        GROUP BY "cam"."customerId"
        ORDER BY "cam"."customerId" ;
    
    END IF;
    END;
      
   ELSE

      OPEN  "records" FOR
         SELECT "bb"."createdby" "approvers"  ,
                MIN("c"."FirstName")  "FirstName"  ,
                MIN("c"."LastName")  "LastName"  
           FROM "bbactedrequest" "bb"
                  CROSS JOIN "customer" "c"
          WHERE  "bb"."createdby" = "c"."id"
                   AND CAST("bb"."requestId" AS NVARCHAR2(2000)) = "_requestId"
                   AND "bb"."status" = "_status"
           GROUP BY "bb"."createdby"
           ORDER BY "bb"."createdby" ;

   END IF;
END;
/
--  DDL for Procedure group_actions_proc



create or replace NONEDITIONABLE PROCEDURE  "group_actions_proc" 
(
  "_groupId" IN VARCHAR2,
  "_actionType" IN VARCHAR2,
  "_actionId" IN VARCHAR2,
  "_isOnlyPremissions" IN VARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   

BEGIN

   DECLARE
      v_select_statement VARCHAR2(4000);

   BEGIN
--      /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
      IF ( "_isOnlyPremissions" = 'true' ) THEN
       OPEN  "records" FOR
         SELECT DISTINCT "groupactionlimit"."Action_id" "actionId"
           FROM "groupactionlimit" 
          WHERE  "groupactionlimit"."Group_id" = "_groupId" ;
         --DBMS_SQL.RETURN_RESULT("groupactionlimit");
      ELSE

      BEGIN
         v_select_statement := ('SELECT' || NCHR(13) || NCHR(10) || '    "groupactionlimit"."Group_id" AS "groupId",' || NCHR(13) || NCHR(10) || '    "groupactionlimit"."LimitType_id" AS "limitTyeId",' || NCHR(13) || NCHR(10) || '    "groupactionlimit"."value" AS "value",' || NCHR(13) || NCHR(10) || '    "featureaction"."id" AS "actionId",' || NCHR(13) || NCHR(10) || NCHR(9) || '"featureaction"."Type_id" AS "actionType",' || NCHR(13) || NCHR(10) || NCHR(9) || '"featureaction"."name" AS "actionName",' || NCHR(13) || NCHR(10) || NCHR(9) || '"featureaction"."description" AS "actionDescription",' || NCHR(9) || NCHR(13) || NCHR(10) || '    "feature"."id" AS "featureId"' || NCHR(13) || NCHR(10) || 'FROM' || NCHR(13) || NCHR(10) || '    ("groupactionlimit"' || NCHR(13) || NCHR(10) || '    LEFT JOIN "featureaction" ON ("featureaction"."id" = "groupactionlimit"."Action_id")' || NCHR(13) || NCHR(10) || '    LEFT JOIN "feature" ON ("feature"."id" = "featureaction"."Feature_id"))' || NCHR(13) || NCHR(10) || '    where "feature"."Status_id" = ''SID_FEATURE_ACTIVE'' and "groupactionlimit"."Group_id"=') || ((''''||(("_groupId")|| ''''))) ;
         IF ( "_actionType" <> ' ' ) THEN
          v_select_statement := (v_select_statement) || (' and "featureaction"."Type_id" = ') || ((''''||(("_actionType")|| ''''))) ;
         END IF;
         IF ( "_actionId" <> ' ' ) THEN
          v_select_statement := (v_select_statement) || (' and "featureaction"."id" = ') || ((''''||(("_actionId")|| ''''))) ;
         END IF;
         EXECUTE IMMEDIATE v_select_statement;/****** Object:  StoredProcedure [lead_assign_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
        OPEN "records" FOR v_select_statement;
      END;
      END IF;

   END;
--   /*TODO:SQLDEV*/ SET ANSI_NULLS ON /*END:SQLDEV*/

END;

/


--  DDL for Procedure infinityuser_contractdetails_get_proc
CREATE OR REPLACE NONEDITIONABLE PROCEDURE "infinityuser_contractdetails_get_proc" 
( 
 "_id" IN VARCHAR2, 
 "_legalEntityId" IN VARCHAR2, "records" OUT SYS_REFCURSOR) 
AS 
BEGIN 
 OPEN "records" FOR SELECT "contract"."id" AS "contractId" ,
"contract"."name" AS "contractName" ,
"contract"."servicedefinitionId" AS "servicedefinitionId" ,
"servicedefinition"."name" AS "serviceDefinitionName" 
, "membergrouptype"."description" AS "serviceDefinitionType" ,
"contractcorecustomers"."coreCustomerId" AS "coreCustomerId" ,
"contractcorecustomers"."coreCustomerName" AS "coreCustomerName" ,
"customergroup"."Group_id" AS "userRole" ,
"membergroup"."Name" AS "userRoleName" ,
"contract"."companyLegalUnit" AS "legalEntityId" ,
CASE WHEN((( "customergroup"."Group_id" = NULL OR "customergroup"."Group_id" = '' ) AND ( "membergroup"."Name" = '' OR "membergroup"."Name" = NULL ))) THEN 'FALSE' ELSE 'TRUE' END AS "isAssociated"
FROM "contract" 
LEFT JOIN "contractcustomers" ON ( "contractcustomers"."contractId" = "contract"."id" AND "contractcustomers"."customerId" = "_id") 
LEFT JOIN "servicedefinition" ON ( "servicedefinition"."id" = "contract"."servicedefinitionId" ) 
LEFT JOIN "membergrouptype" ON ( "membergrouptype"."id" = "servicedefinition"."serviceType" ) 
LEFT JOIN "contractcorecustomers" ON ( "contractcorecustomers"."contractId" = "contract"."id" ) 
LEFT JOIN "customergroup" ON ( "customergroup"."contractId" = "contract"."id" AND "customergroup"."coreCustomerId" = "contractcorecustomers"."coreCustomerId" AND "customergroup"."Customer_id" = "_id" ) 
LEFT JOIN "membergroup" ON ( "membergroup"."id" = "customergroup"."Group_id" ) WHERE "customergroup"."Customer_id" = "_id" AND "contractcustomers"."customerId" = "_id" AND "contract"."companyLegalUnit" = "_legalEntityId";

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/

--  DDL for Procedure membership_customer_search_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "membership_customer_search_proc" 
(
  "_id" IN VARCHAR2,
  "_name" IN VARCHAR2,
  "_email" IN VARCHAR2,
  "_phone" IN VARCHAR2,
  "_dateOfBirth" IN VARCHAR2,
  "_status" IN VARCHAR2,
  "_country" IN VARCHAR2,
  "_city" IN VARCHAR2,
  "_zipCode" IN VARCHAR2,
  "_legalEntityId" IN VARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   v_select_statement VARCHAR2(4000);
   v_address_select_statement VARCHAR2(4000);

BEGIN

   v_select_statement := ('select "membership"."id" , "membership"."companyLegalUnit" , "membership"."isBusinessType" , "membership"."name", 
      "membership"."industry" , "membership"."firstName" , "membership"."lastName" , 
      "membership"."phone" , "membership"."taxId", "membership"."faxId", 
      "membership"."email" , "address"."addressLine1",
      "address"."addressLine2" , "address"."cityName" , "address"."country" , 
      "address"."zipCode" , "address"."state"
      from "membership" LEFT JOIN "address" on ("membership"."addressId" = "address"."id")
      where "membership"."id" is not null and "membership"."companyLegalUnit" = ''' || "_legalEntityId" || '''') ;
   IF ( NVL("_id", ' ') != ' ' ) THEN

   BEGIN
      v_select_statement := (v_select_statement || ' and "membership"."id" = ' || '''' || "_id" || '''') ;

   END;
   END IF;
   IF ( NVL("_name", ' ') != ' ' ) THEN

   BEGIN
      v_select_statement := (v_select_statement|| ' and "membership"."name" LIKE %'|| "_name"|| '%') ;

   END;
   END IF;
   IF ( NVL("_email", ' ') != ' ' ) THEN

   BEGIN
      v_select_statement := (v_select_statement|| ' and "membership"."email" = '|| '''' || "_email" || '''') ;

   END;
   END IF;
   IF ( NVL("_phone", ' ') != ' ' ) THEN

   BEGIN
      v_select_statement := (v_select_statement|| ' and "membership"."phone" = '|| '''' || "_phone" || '''') ;

   END;
   END IF;
   IF ( NVL("_dateOfBirth", ' ') != ' ' ) THEN

   BEGIN
      v_select_statement := (v_select_statement|| ' and "membership"."dateOfBirth" = '|| '''' || "_dateOfBirth" || '''') ;

   END;
   END IF;
   v_address_select_statement := ' ' ;
   IF ( NVL("_city", ' ') != ' '
     OR NVL("_country", ' ') != ' '
     OR NVL("_zipCode", ' ') != ' ' ) THEN

   BEGIN
      v_address_select_statement := 'select "address"."id" from "address" where "address"."id" is not null' ;
      IF ( NVL("_city", ' ') != ' ' ) THEN

      BEGIN
         v_address_select_statement := (v_address_select_statement|| ' and "address"."cityName" = '|| '''' || "_city" || '''') ;

      END;
      END IF;
      IF ( NVL("_country", ' ') != ' ' ) THEN

      BEGIN
         v_address_select_statement := (v_address_select_statement|| ' and "address"."country" = '|| '''' || "_country" || '''') ;

      END;
      END IF;
      IF ( NVL("_zipCode", ' ') != ' ' ) THEN

      BEGIN
         v_address_select_statement := (v_address_select_statement|| ' and "address"."zipCode" = '|| '''' || "_zipCode" || '''') ;

      END;
      END IF;

   END;
   END IF;
   IF ( NVL(v_address_select_statement, ' ') != ' ' ) THEN

   BEGIN
      v_select_statement := CONCAT(v_select_statement, ' and "membership"."addressId" in (", v_address_select_statement , ")') ;

   END;
   END IF;
   v_select_statement := CONCAT(v_select_statement, ';') ;
   EXECUTE IMMEDIATE v_select_statement;
    OPEN "records" FOR v_select_statement;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/

--  DDL for Procedure membership_relative_customer_get_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "membership_relative_customer_get_proc" 
(
  v__id IN VARCHAR2, "membership" OUT SYS_REFCURSOR
)
AS
   

BEGIN

   OPEN  "membership" FOR
      SELECT "membership"."id" ,
             "membership"."name" ,
             "membership"."firstName" ,
             "membership"."lastName" ,
             "membership"."phone" ,
             "membership"."email" ,
             "membership"."dateOfBirth" ,
             "membership"."taxId" ,
             "membership"."faxId" ,
             "membership"."industry" ,
             "membership"."isBusinessType" ,
             "membershiprelation"."relationshipId" ,
             "membershiprelation"."relationshipName" ,
             "address"."addressLine1" ,
             "address"."addressLine2" ,
             "address"."cityName" ,
             "address"."country" ,
             "address"."zipCode" ,
             "address"."state" 
        FROM "membership" 
               LEFT JOIN "address"    ON ( "membership"."addressId" = "address"."id" )
               JOIN "membershiprelation"    ON ( "membershiprelation"."relatedMebershipId" = "membership"."id" )
       WHERE  "membership"."id" IN ( SELECT "membershiprelation"."relatedMebershipId" 
                                             FROM "membershiprelation" 
                                              WHERE  "membershiprelation"."membershipId" = v__id )

                AND "membershiprelation"."membershipId" = v__id ;
      --DBMS_SQL.RETURN_RESULT("membership");

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/

--  DDL for Procedure organisation_actions_create_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "organisation_actions_create_proc" 
(
  v__features IN VARCHAR2,
  v__organisationType IN VARCHAR2,
  v__organisationId IN VARCHAR2,
  v_cursor OUT SYS_REFCURSOR
)
AS
   v_limitvalue VARCHAR2(4000);
   v_id VARCHAR2(4000);
   v_finished NUMBER(10,0) := 0;
   v_featureActionId VARCHAR2(255) := '';
   v_actionslist VARCHAR2(4000) := '';
   v_limitId VARCHAR2(255) := '';
   v_entryStatus NUMBER(10,0) := 0;
   v_features_list CLOB;
   v_orgIds CLOB;

   CURSOR actions
     IS SELECT "featureaction"."id" 
     FROM "featureaction" 
    WHERE  "featureaction"."Feature_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v_features_list)));

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   SELECT LISTAGG(CAST("feature"."id" AS VARCHAR2(255)), ',') 

     INTO v_features_list
     FROM "feature" 
            JOIN "featureroletype"    ON ( "featureroletype"."Feature_id" = "feature"."id"
            AND "featureroletype"."RoleType_id" = v__organisationType )
    WHERE  ("feature"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v__features))))
             OR "feature"."isPrimary" = '1'
             OR "feature"."isPrimary" = 'true';
   v_features_list := CASE 
                           WHEN ( v_features_list IS NULL ) THEN ''
   ELSE v_features_list
      END ;
   OPEN actions;
   FETCH actions INTO v_featureActionId;
   <<loop_2>>
   WHILE ((actions%FOUND) = TRUE ) 
   LOOP 
      DECLARE
         CURSOR limits
           IS SELECT "actionlimit"."LimitType_id" 
           FROM "actionlimit" 
          WHERE  "actionlimit"."Action_id" = v_featureActionId;

      BEGIN
         v_entryStatus := 0 ;
         OPEN limits;
         FETCH limits INTO v_limitId;
         <<loop_1>>
         WHILE ( (limits%FOUND) = TRUE ) 
         LOOP 

            BEGIN
               SELECT "actionlimit"."value" 

                 INTO v_limitvalue
                 FROM "actionlimit" 
                WHERE  "actionlimit"."Action_id" = v_featureActionId
                         AND "actionlimit"."LimitType_id" = v_limitId;
               SELECT SUBSTR(SYS_GUID(), 0, 50) 

                 INTO v_id
                 FROM DUAL ;
               SELECT LISTAGG(CAST("organisationactionlimit"."Organisation_id" AS VARCHAR2(255)), ',') 

                 INTO v_orgIds
                 FROM "organisationactionlimit" 
                WHERE  "organisationactionlimit"."Organisation_id" = v__organisationId
                         AND "organisationactionlimit"."Action_id" = v_featureActionId
                         AND "organisationactionlimit"."LimitType_id" = v_limitId
                         AND "organisationactionlimit"."value" = v_limitvalue;
               IF v_orgIds IS NULL
                 OR v_orgIds = ' ' THEN

               BEGIN
                  INSERT INTO "organisationactionlimit"
                    ( "organisationactionlimit"."id", "organisationactionlimit"."Organisation_id", "organisationactionlimit"."Action_id", "organisationactionlimit"."LimitType_id", "organisationactionlimit"."value" )
                    VALUES ( v_id, v__organisationId, v_featureActionId, v_limitId, v_limitvalue );
                  v_entryStatus := 1 ;

               END;
               END IF;
               FETCH limits INTO v_limitId;
               GOTO loop_1;

            END;
         END LOOP;
         CLOSE limits;
         IF v_entryStatus = 0 THEN

         BEGIN
            SELECT LISTAGG(CAST("organisationactionlimit"."Organisation_id" AS VARCHAR2(255)), ',') 

              INTO v_orgIds
              FROM "organisationactionlimit" 
             WHERE  "organisationactionlimit"."Organisation_id" = v__organisationId
                      AND "organisationactionlimit"."Action_id" = v_featureActionId;
            IF v_orgIds IS NULL
              OR v_orgIds = ' ' THEN

            BEGIN
               SELECT SUBSTR(SYS_GUID(), 0, 50) 

                 INTO v_id
                 FROM DUAL ;
               INSERT INTO "organisationactionlimit"
                 ( "organisationactionlimit"."id", "organisationactionlimit"."Organisation_id", "organisationactionlimit"."Action_id" )
                 VALUES ( v_id, v__organisationId, v_featureActionId );

            END;
            END IF;

         END;
         END IF;
         v_actionslist := v_featureActionId || ',' || v_actionslist ;
         FETCH actions INTO v_featureActionId;
         GOTO loop_2;

      END;
   END LOOP;
   CLOSE actions;
   SELECT SUBSTR(v_actionslist, 1, (CASE 
                                         WHEN LENGTH(v_actionslist) > 0 THEN LENGTH(v_actionslist) - 1
                 ELSE 0
                    END)) 

     INTO v_actionslist
     FROM DUAL ;
   OPEN  v_cursor FOR
      SELECT v_actionslist actionslist  
        FROM DUAL  ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/

--  DDL for Procedure organisation_actions_delete_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "organisation_actions_delete_proc" 
(
  v__features IN VARCHAR2,
  v__organisationType IN VARCHAR2,
  v__organisationId IN VARCHAR2,
  v_cursor OUT SYS_REFCURSOR
)
AS
   v_finished NUMBER(10,0) := 0;
   v_featureActionId VARCHAR2(255) := '';
   v_actionslist VARCHAR2(4000) := '';
   v_features_list CLOB;
   v_customer_list CLOB;

   CURSOR actions
     IS SELECT "featureaction"."id" 
     FROM "featureaction" 
    WHERE  "featureaction"."Feature_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v_features_list)));

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   SELECT LISTAGG(CAST("feature"."id" AS VARCHAR2(255)), ',') 

     INTO v_features_list
     FROM "feature" 
            JOIN "featureroletype"    ON ( "featureroletype"."Feature_id" = "feature"."id"
            AND "featureroletype"."RoleType_id" = v__organisationType )
    WHERE  ( "feature"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v__features))) );
   v_features_list := CASE 
                           WHEN ( v_features_list IS NULL ) THEN ''
   ELSE v_features_list
      END ;
   SELECT LISTAGG(CAST("Customer_id" AS VARCHAR2(255)), ',') 

     INTO v_customer_list
     FROM "organisationemployees" 
    WHERE  ( "organisationemployees"."Organization_id" = v__organisationId );
   v_customer_list := CASE 
                           WHEN ( v_customer_list IS NULL ) THEN ''
   ELSE v_customer_list
      END ;
   OPEN actions;
   <<loop_1>>
   WHILE ( 1 = 1 ) 
   LOOP 

      BEGIN
         FETCH actions INTO v_featureActionId;
         IF (actions%FOUND) <> FALSE THEN
          v_finished := 1 ;
         END IF;
         IF v_finished = 1 THEN
          EXIT;
         ELSE

         BEGIN
            DELETE "organisationactionlimit"

             WHERE  ( "organisationactionlimit"."Organisation_id" = v__organisationId
                      AND "organisationactionlimit"."Action_id" = v_featureActionId );
            v_actionslist := v_featureActionId || ',' || v_actionslist ;
            GOTO loop_1;

         END;
         END IF;

      END;
   END LOOP;
   CLOSE actions;
   SELECT SUBSTR(v_actionslist, 1, CASE 
                                        WHEN LENGTH(v_actionslist) = 0 THEN LENGTH(v_actionslist)
                 ELSE INSTR(v_actionslist, ' ') - 1
                    END) 

     INTO v_actionslist
     FROM DUAL ;
   DELETE "customeraction"

    WHERE  ( "customeraction"."RoleType_id" = v__organisationType
             AND "customeraction"."Action_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v_actionslist)))
             AND("customeraction"."Customer_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v_customer_list))) ));
   OPEN  v_cursor FOR
      SELECT v_actionslist 
        FROM DUAL  ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure organisation_actions_get_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "organisation_actions_get_proc" 
(
  v__organisationId IN VARCHAR2,
  v_cursor OUT SYS_REFCURSOR
)
AS
   v_actionList CLOB;


BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   SELECT DISTINCT (LISTAGG(CAST("organisationactionlimit"."Action_id" AS VARCHAR2(255)),','))

     INTO v_actionList
     FROM "organisationactionlimit" 
    WHERE  "organisationactionlimit"."Organisation_id" = v__organisationId;
   OPEN  v_cursor FOR
      SELECT v_actionList actionslist  
        FROM DUAL  ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure organisation_customeraccounts_delete_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "organisation_customeraccounts_delete_proc" 
(
  v__accounts IN VARCHAR2,
  v__organisationId IN VARCHAR2,
  v_cursor OUT SYS_REFCURSOR
)
AS
   v_orgemployees_list CLOB;


BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   SELECT LISTAGG(CAST("organisationemployees"."Customer_id" AS VARCHAR2(255)),',') 

     INTO v_orgemployees_list
     FROM "organisationemployees" 
    WHERE  "organisationemployees"."Organization_id" = v__organisationId;
   v_orgemployees_list := CASE 
                               WHEN ( v_orgemployees_list IS NULL ) THEN ''
   ELSE v_orgemployees_list
      END ;
   OPEN  v_cursor FOR
      SELECT v_orgemployees_list 
        FROM DUAL  ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);
   DELETE "customeraction"

    WHERE  "customeraction"."Account_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v__accounts)))
             AND "customeraction"."Customer_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v_orgemployees_list)));
   DELETE "customeraccounts"

    WHERE  "customeraccounts"."Account_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v__accounts)))
             AND "customeraccounts"."Customer_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v_orgemployees_list)));

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure organisation_details_get_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "organisation_details_get_proc" 
(
  v__orgId IN VARCHAR2,
  iv__orgName IN VARCHAR2,
  iv__orgEmail IN VARCHAR2,
  iv__orgTaxId IN VARCHAR2,
  "organisation" OUT SYS_REFCURSOR
)
AS
   v__orgName VARCHAR2(4000) := iv__orgName;
   v__orgEmail VARCHAR2(4000) := iv__orgEmail;
   v__orgTaxId VARCHAR2(4000) := iv__orgTaxId;
   v_orgIdCSV CLOB;


BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   IF v__orgName <> ' ' THEN

   BEGIN
      v__orgName := '%' || v__orgName || '%' ;
      SELECT LISTAGG(CAST(''''||("organisation"."id")AS VARCHAR2(255))|| ',') 

        INTO v_orgIdCSV
        FROM "organisation" 
       WHERE  "Name" LIKE v__orgName;

   END;
   END IF;
   IF v__orgEmail <> ' ' THEN

   BEGIN
      v__orgEmail := '%' || v__orgEmail || '%' ;
      IF v_orgIdCSV IS NULL
        OR v_orgIdCSV = ' ' THEN

      BEGIN
         SELECT LISTAGG(CAST(''''||("organisationcommunication"."Organization_id") AS VARCHAR2(255))|| ',') 

           INTO v_orgIdCSV
           FROM "organisationcommunication" 
          WHERE  "Value" LIKE v__orgEmail
                   AND "Type_id" = 'COMM_TYPE_EMAIL';

      END;
      ELSE

      BEGIN
         SELECT v_orgIdCSV || ',' || ( SELECT LISTAGG(CAST(''''||("organisationcommunication"."Organization_id") AS VARCHAR2(255))|| ',') 
                                       FROM "organisationcommunication" 
                                        WHERE  "Value" LIKE v__orgEmail
                                                 AND "Type_id" = 'COMM_TYPE_EMAIL'
                                                 AND UTILS.FIND_IN_SET(("organisationcommunication"."Organization_id"), v_orgIdCSV) > 0 )

           INTO v_orgIdCSV
           FROM DUAL ;

      END;
      END IF;

   END;
   END IF;
   IF v__orgTaxId <> ' ' THEN

   BEGIN
      v__orgTaxId := '%' || v__orgTaxId || '%' ;
      IF v_orgIdCSV IS NULL
        OR v_orgIdCSV = ' ' THEN

      BEGIN
         SELECT LISTAGG(CAST(''''||("organisationmembership"."Organization_id")AS VARCHAR2(255))|| ',') 

           INTO v_orgIdCSV
           FROM "organisationmembership" 
          WHERE  "Taxid" LIKE v__orgTaxId;

      END;
      ELSE

      BEGIN
         SELECT v_orgIdCSV || ',' || ( SELECT LISTAGG(CAST(''''||("organisationmembership"."Organization_id")AS VARCHAR2(255))|| ',') 
                                       FROM "organisationmembership" 
                                        WHERE  "Taxid" LIKE v__orgTaxId
                                                 AND UTILS.FIND_IN_SET(("organisationmembership"."Organization_id"), v_orgIdCSV) > 0 )

           INTO v_orgIdCSV
           FROM DUAL ;

      END;
      END IF;

   END;
   END IF;
   IF v__orgId <> ' ' THEN

   BEGIN
      v_orgIdCSV := ''''||(v__orgId) ;

   END;
   END IF;
   OPEN  "organisation" FOR
      SELECT organisation."id" id  ,
             organisation."Name" Name  ,
             organisation."Type_Id" TypeId  ,
             organisation."StatusId" orgStatus  ,
             organisation."FaxId" faxId  ,
             phone."Value" Phone  ,
             email."Value" Email  ,
             address."cityName" cityName  ,
             address."addressLine1" addressLine1  ,
             address."addressLine2" addressLine2  ,
             address."zipCode" zipCode  ,
             address."id" addressId  ,
             address."state" state  ,
             address."country" country  ,
             organisationaddress."IsPrimary" IsPrimary  ,
             businesstype."name" businesstype  ,
             businesstype."id" businessTypeId  
        FROM "organisation" organisation
               LEFT JOIN "organisationcommunication" phone   ON phone."Type_id" = 'COMM_TYPE_PHONE'
               AND organisation."id" = phone."Organization_id"
               LEFT JOIN "organisationcommunication" email   ON UTILS.FIND_IN_SET(''''||(organisation."id"), v_orgIdCSV) > 0
               AND email."Type_id" = 'COMM_TYPE_EMAIL'
               AND organisation."id" = email."Organization_id"
               LEFT JOIN "organisationaddress" organisationaddress   ON UTILS.FIND_IN_SET(''''||(organisationaddress."Organization_id"), v_orgIdCSV) > 0
               AND organisation."id" = organisationaddress."Organization_id"
               LEFT JOIN "address" ADDRESS   ON organisationaddress."Address_id" = address."id"
               LEFT JOIN "businesstype" businesstype  ON businesstype."id" = organisation."BusinessType_id"
       WHERE  UTILS.FIND_IN_SET(''''||(organisation."id"), v_orgIdCSV) > 0 ;
      --DBMS_SQL.RETURN_RESULT("organisation");

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure organisation_features_create_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "organisation_features_create_proc" 
(
  v__features IN VARCHAR2,
  v__organisationType IN VARCHAR2,
  v__organisationId IN VARCHAR2,
  v_cursor OUT SYS_REFCURSOR
)
AS
   v_featuresList VARCHAR2(4000) := ' ';
   v_finished NUMBER(10,0) := 0;
   v_featureId VARCHAR2(255) := '';
   v_features_List CLOB := '';
   v_orgIds CLOB;
   v_id VARCHAR2(4000);

   CURSOR features
     IS SELECT "feature"."id" 
     FROM "feature" 
    WHERE  "feature"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v_features_list)));

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   SELECT LISTAGG(CAST("feature"."id" AS VARCHAR2(255)),',')

     INTO v_features_list
     FROM "feature" 
            JOIN "featureroletype"    ON ( "featureroletype"."Feature_id" = "feature"."id"
            AND "featureroletype"."RoleType_id" = v__organisationType )
    WHERE  ( "feature"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v__features)))
             OR "feature"."isPrimary" = '1'
             OR "feature"."isPrimary" = 'true' );
   v_features_list := CASE 
                           WHEN ( v_features_list IS NULL ) THEN ''
   ELSE v_features_list
      END ;
   OPEN features;
   FETCH features INTO v_featureId;
   <<loop_1>>
   WHILE ( 1 = 1 ) 
   LOOP 

      BEGIN
         IF features%FOUND <> FALSE THEN
          v_finished := 1 ;
         END IF;
         IF v_finished = 1 THEN
          EXIT;
         ELSE

         BEGIN
            SELECT SUBSTR(SYS_GUID(), 0, 50) 

              INTO v_id
              FROM DUAL ;
            SELECT LISTAGG(CAST("organisationfeatures"."organisationId" AS VARCHAR2(255)),',') 

              INTO v_orgIds
              FROM "organisationfeatures" 
             WHERE  "organisationfeatures"."organisationId" = v__organisationId
                      AND "organisationfeatures"."featureId" = v_featureId;
            IF v_orgIds IS NULL
              OR v_orgIds = ' ' THEN

            BEGIN
               INSERT INTO "organisationfeatures"
                 ( "organisationfeatures"."id", "organisationfeatures"."organisationId", "organisationfeatures"."featureId" )
                 VALUES ( v_id, v__organisationId, v_featureId );
               v_featuresList := v_featureId || ',' || v_featuresList ;

            END;
            END IF;
            FETCH features INTO v_featureId;
            GOTO loop_1;

         END;
         END IF;

      END;
   END LOOP;
   CLOSE features;
   SELECT SUBSTR(v_featuresList, 1, (CASE 
                                          WHEN LENGTH(v_featuresList) > 0 THEN LENGTH(v_featuresList) - 1
                 ELSE 0
                    END)) 

     INTO v_featuresList
     FROM DUAL ;
   OPEN  v_cursor FOR
      SELECT v_featuresList featuresList  
        FROM DUAL  ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure organisation_features_delete_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "organisation_features_delete_proc" 
(
  v__features IN VARCHAR2,
  v__organisationType IN VARCHAR2,
  v__organisationId IN VARCHAR2,
  v_cursor OUT SYS_REFCURSOR
)
AS
   v_finished NUMBER(10,0) := 0;
   v_featureId VARCHAR2(255) := '';
   v_featuresList VARCHAR2(4000) := '';
   v_features_list CLOB;

   CURSOR features
     IS SELECT "feature"."id" 
     FROM "feature" 
    WHERE  "feature"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v_features_list)));

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   SELECT LISTAGG(CAST("feature"."id" AS VARCHAR2(255)),',')

     INTO v_features_list
     FROM "feature" 
            JOIN "featureroletype"    ON ( "featureroletype"."Feature_id" = "feature"."id"
            AND "featureroletype"."RoleType_id" = v__organisationType )
    WHERE  ( ("feature"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v__features)))) );
   v_features_list := CASE 
                           WHEN ( v_features_list IS NULL ) THEN ''
   ELSE v_features_list
      END ;
   OPEN features;
   <<loop_1>>
   WHILE ( 1 = 1 ) 
   LOOP 

      BEGIN
         FETCH features INTO v_featureId;
         IF features%FOUND <> FALSE THEN
          v_finished := 1 ;
         END IF;
         IF v_finished = 1 THEN
          EXIT;
         ELSE

         BEGIN
            DELETE "organisationfeatures"

             WHERE  ( "organisationfeatures"."organisationId" = v__organisationId
                      AND "organisationfeatures"."featureId" = v_featureId );
            v_featuresList := v_featureId || ',' || v_featuresList ;
            GOTO loop_1;

         END;
         END IF;

      END;
   END LOOP;
   CLOSE features;
   SELECT SUBSTR(v_featuresList, 1, CASE 
                                         WHEN LENGTH(v_featuresList) = 0 THEN LENGTH(v_featuresList)
                 ELSE LENGTH(v_featuresList) - 1
                    END) 

     INTO v_featuresList
     FROM DUAL ;
   OPEN  v_cursor FOR
      SELECT v_featuresList featuresList  
        FROM DUAL  ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure organisation_features_suspend_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "organisation_features_suspend_proc" 
(
  v__features IN VARCHAR2,
  v__organisationType IN VARCHAR2,
  v__organisationId IN VARCHAR2,
  v_cursor OUT SYS_REFCURSOR
)
AS
   v_finished NUMBER(10,0) := 0;
   v_featureId VARCHAR2(255) := '';
   v_featuresList VARCHAR2(4000) := '';
   v_features_list CLOB;

   CURSOR features
     IS SELECT "feature"."id" 
     FROM "feature" 
    WHERE  ("feature"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_features_list) )));

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   UPDATE "organisationfeatures"
      SET "featureStatus" = NULL
    WHERE  "organisationfeatures"."organisationId" = v__organisationId;
   SELECT LISTAGG(CAST("feature"."id" AS VARCHAR2(255)),',')
     INTO v_features_list
     FROM "feature" 
            JOIN "featureroletype"    ON ( "featureroletype"."Feature_id" = "feature"."id"
            AND "featureroletype"."RoleType_id" = v__organisationType )
    WHERE  ( ("feature"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v__features)))));
   v_features_list := CASE 
                           WHEN ( v_features_list IS NULL ) THEN ''
   ELSE v_features_list
      END ;
   OPEN features;
   <<loop_1>>
   WHILE ( 1 = 1 ) 
   LOOP 

      BEGIN
         FETCH features INTO v_featureId;
         IF features%FOUND <> FALSE THEN
          v_finished := 1 ;
         END IF;
         IF v_finished = 1 THEN
          EXIT;
         ELSE

         BEGIN
            UPDATE "organisationfeatures"
               SET "featureStatus" = 'SID_FEATURE_SUSPENDED'
             WHERE  ( "organisationfeatures"."organisationId" = v__organisationId
              AND "organisationfeatures"."featureId" = v_featureId );
            v_featuresList := v_featureId || ',' || v_featuresList ;
            GOTO loop_1;

         END;
         END IF;

      END;
   END LOOP;
   CLOSE features;
   SELECT SUBSTR(v_featuresList, 1, CASE 
                                         WHEN LENGTH(v_featuresList) = 0 THEN LENGTH(v_featuresList)
                 ELSE INSTR(v_featuresList, ' ') - 1
                    END) 

     INTO v_featuresList
     FROM DUAL ;
   OPEN  v_cursor FOR
      SELECT v_featuresList 
        FROM DUAL  ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure organization_actions_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "organization_actions_proc" 
(
  v__organizationId IN VARCHAR2,
  v__actionType IN VARCHAR2,
  v__actionId IN VARCHAR2,"organisationactionlimit" OUT SYS_REFCURSOR
)
AS
   v_select_statement VARCHAR2(4000);

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   v_select_statement := ('SELECT' || NCHR(13) || NCHR(10) || NCHR(9) || '"feature"."id" AS featureId,' || NCHR(13) || NCHR(10) || '    "feature"."name" AS featureName,' || NCHR(13) || NCHR(10) || '    "feature"."description" AS featureDescription,' || NCHR(13) || NCHR(10) || '    "feature"."Status_id" AS fiFeatureStatus,' || NCHR(13) || NCHR(10) || '    "organisationfeatures"."featureStatus" AS orgFeatureStatus,' || NCHR(13) || NCHR(10) || '    "featureaction"."Type_id" AS actionType,' || NCHR(13) || NCHR(10) || '    "featureaction"."id" AS actionId,' || NCHR(13) || NCHR(10) || '    "featureaction"."name" AS actionName,' || NCHR(13) || NCHR(10) || '    "featureaction"."description" AS actionDescription,' || NCHR(9) || NCHR(13) || NCHR(10) || '    "featureaction"."isAccountLevel" AS isAccountLevel,' || NCHR(13) || NCHR(10) || NCHR(9) || '"actionlimit"."LimitType_id" AS limitTypeId,' || NCHR(13) || NCHR(10) || '    "organisationactionlimit"."value" AS orgLimitValue,' || NCHR(13) || NCHR(10) || '    "actionlimit"."value" AS fiLimitValue' || NCHR(13) || NCHR(10) || NCHR(9) || 'FROM' || NCHR(13) || NCHR(10) || NCHR(9) || NCHR(9) || '("organisationactionlimit"' || NCHR(13) || NCHR(10) || NCHR(9) || NCHR(9) || 'LEFT JOIN "featureaction" ON ("featureaction"."id" = "organisationactionlimit"."Action_id")' || NCHR(13) || NCHR(10) || NCHR(9) || NCHR(9) || 'LEFT JOIN "feature" ON ("feature"."id" = "featureaction"."Feature_id")' || NCHR(13) || NCHR(10) || '        LEFT JOIN "organisationfeatures" ON ("organisationfeatures"."featureId" = "feature"."id")' || NCHR(13) || NCHR(10) || '        LEFT JOIN "actionlimit" ON ("actionlimit"."Action_id" = "organisationactionlimit"."Action_id" and "actionlimit"."LimitType_id" = "organisationactionlimit"."LimitType_id"))' || NCHR(13) || NCHR(10) || NCHR(9) || 'where "organisationactionlimit"."Organisation_id" = ') || ('''' || v__organizationId || '''') || (' and "organisationfeatures"."organisationId" = ') || ('''' || v__organizationId || '''') || ('') ;
   IF ( v__actionType <> ' ' ) THEN
    v_select_statement := (v_select_statement) || (' and "featureaction"."Type_id" = ') || ('''' || v__actionType || '''') ;
   END IF;
   IF ( v__actionId <> ' ' ) THEN
    v_select_statement := (v_select_statement) || (' and "featureaction"."id" = ') || ('''' || v__actionId || '''') ;
   END IF;
   EXECUTE IMMEDIATE v_select_statement;
   OPEN "organisationactionlimit" FOR v_select_statement;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure prospect_securityattributes_get_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "prospect_securityattributes_get_proc" 
(
  "_userId" IN VARCHAR2,
  "_legalEntityId" IN VARCHAR2,
  "records" OUT SYS_REFCURSOR,
  "records1" OUT SYS_REFCURSOR
)
AS
   v_userAssociatedCoreCustomers CLOB := '';
   v_userAssociatedContracts CLOB := '';
   v_userAssociatedServiceDefinitions CLOB := '';
   v_userAssociatedGroups CLOB := '';
   v_actionsAtCoreCustomers CLOB := '';
   v_actionsAtServiceDefinitions CLOB := '';
   v_actionsAtGroups CLOB := '';
   v_actionsAtuser CLOB:= '';
   v_activeFeaturesAtFI CLOB := '';
   v_intersectedUserActions CLOB := '';
   v_intersectedUserFeatures CLOB := '';
   v_newActionsAtCoreCustomers CLOB := '';
   v_newActionsAtGroups CLOB := '';


BEGIN

--   --   ?() ;
--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
--   --   ?() ;
--   --   ?() ;
   SELECT LISTAGG(CAST("contractcustomers"."coreCustomerId" AS VARCHAR2(4000))) 

     INTO v_userAssociatedCoreCustomers
     FROM "contractcustomers" 
    WHERE  "contractcustomers"."customerId" = "_userId"
             AND "contractcustomers"."companyLegalUnit" = "_legalEntityId";
--   --   ?() ;
   dbms_output.put_line('v_userAssociatedCoreCustomers---->'||v_userAssociatedCoreCustomers);
   SELECT LISTAGG(CAST("contractcustomers"."contractId" AS VARCHAR2(4000))) 

     INTO v_userAssociatedContracts
     FROM "contractcustomers" 
    WHERE  "contractcustomers"."customerId" = "_userId"
             AND "contractcustomers"."companyLegalUnit" = "_legalEntityId";
   dbms_output.put_line('v_userAssociatedContracts---->'||v_userAssociatedContracts);
   SELECT LISTAGG(CAST("contract"."servicedefinitionId" AS VARCHAR2(4000))) 

     INTO v_userAssociatedServiceDefinitions
     FROM "contract" 
    WHERE  "contract"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_userAssociatedContracts)))
             AND "contract"."statusId" = 'SID_CONTRACT_ACTIVE';
--   --   ?() ;
   dbms_output.put_line('v_userAssociatedServiceDefinitions---->'||v_userAssociatedServiceDefinitions);

   SELECT LISTAGG(CAST("customergroup"."Group_id" AS VARCHAR2(4000))) 

     INTO v_userAssociatedGroups
     FROM "customergroup" 
    WHERE  "customergroup"."Customer_id" = "_userId"
             AND "customergroup"."companyLegalUnit" = "_legalEntityId";
--   --   ?() ;
   dbms_output.put_line('v_userAssociatedGroups---->'||v_userAssociatedGroups);

   SELECT LISTAGG(CAST("contractactionlimit"."actionId" AS VARCHAR2(4000))) 

     INTO v_actionsAtCoreCustomers
     FROM "contractactionlimit" 
    WHERE  "contractactionlimit"."coreCustomerId" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_userAssociatedCoreCustomers)));
--   --   ?() ;
   dbms_output.put_line('v_actionsAtCoreCustomers---->'||v_actionsAtCoreCustomers);

   SELECT LISTAGG(CAST("contractactionlimit"."actionId" AS VARCHAR2(4000))) 

     INTO v_newActionsAtCoreCustomers
     FROM "contractactionlimit" 
    WHERE  ("contractactionlimit"."coreCustomerId" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_userAssociatedCoreCustomers))))
             AND "contractactionlimit"."isNewAction" = '1';
--   --   ?() ;
   dbms_output.put_line('v_newActionsAtCoreCustomers---->'||v_newActionsAtCoreCustomers);

   SELECT LISTAGG(CAST("servicedefinitionactionlimit"."actionId" AS VARCHAR2(4000))) 

     INTO v_actionsAtServiceDefinitions
     FROM "servicedefinitionactionlimit" 
    WHERE  ("servicedefinitionactionlimit"."serviceDefinitionId" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_userAssociatedServiceDefinitions))))
             AND "servicedefinitionactionlimit"."companyLegalUnit" = "_legalEntityId";
--   --   ?() ;
   dbms_output.put_line('v_actionsAtServiceDefinitions---->'||v_actionsAtServiceDefinitions);

   SELECT LISTAGG(CAST("groupactionlimit"."Action_id" AS VARCHAR2(4000))) 

     INTO v_actionsAtGroups
     FROM "groupactionlimit" 
    WHERE  ("groupactionlimit"."Group_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_userAssociatedGroups))))
             AND "groupactionlimit"."companyLegalUnit" = "_legalEntityId";
   --   ?() ;
      dbms_output.put_line('v_actionsAtGroups---->'||v_actionsAtGroups);

   SELECT LISTAGG(CAST("groupactionlimit"."Action_id" AS VARCHAR2(4000))) 

     INTO v_newActionsAtGroups
     FROM "groupactionlimit" 
    WHERE  ("groupactionlimit"."Group_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_userAssociatedGroups))))
             AND "groupactionlimit"."isNewAction" = '1'
             AND "groupactionlimit"."companyLegalUnit" = "_legalEntityId";
   --   ?() ;
      dbms_output.put_line('v_newActionsAtGroups---->'||v_newActionsAtGroups);

   SELECT LISTAGG(CAST("customeraction"."Action_id" AS VARCHAR2(4000))) 

     INTO v_actionsAtuser
     FROM "customeraction" 
    WHERE  "customeraction"."Customer_id" = "_userId"
             AND ( "customeraction"."isAllowed" = '1'
             OR "customeraction"."isAllowed" = '1' )
             AND "customeraction"."companyLegalUnit" = "_legalEntityId";
   --   ?() ;
      dbms_output.put_line('v_actionsAtuser---->'||v_actionsAtuser);

   SELECT LISTAGG(CAST("feature"."id" AS VARCHAR2(4000))) 

     INTO v_activeFeaturesAtFI
     FROM "feature" 
    WHERE  "feature"."Status_id" = 'SID_FEATURE_ACTIVE'
             AND "feature"."companyLegalUnit" = "_legalEntityId";
   --   ?() ;
      dbms_output.put_line('v_activeFeaturesAtFI---->'||v_activeFeaturesAtFI);

   SELECT LISTAGG(CAST("featureaction"."id" AS VARCHAR2(4000))) 

     INTO v_intersectedUserActions
     FROM "featureaction" 
    WHERE  "featureaction"."status" = 'SID_ACTION_ACTIVE'
             AND ("featureaction"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_actionsAtuser))))
             AND ("featureaction"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_actionsAtGroups))))
             AND ("featureaction"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_actionsAtServiceDefinitions))))
             AND ("featureaction"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_actionsAtCoreCustomers))))
             AND ("featureaction"."Feature_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_activeFeaturesAtFI))));
   --   ?() ;
      dbms_output.put_line('v_intersectedUserActions---->'||v_intersectedUserActions);

   SELECT LISTAGG(CAST("featureaction"."id" AS VARCHAR2(4000))) 

     INTO v_intersectedUserActions
     FROM "featureaction" 
    WHERE  "featureaction"."status" = 'SID_ACTION_ACTIVE'
             AND "featureaction"."companyLegalUnit" = "_legalEntityId"
             AND ( "featureaction"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_intersectedUserActions)))
             OR "featureaction"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_newActionsAtCoreCustomers)))
             OR "featureaction"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_newActionsAtGroups))) )
             AND "featureaction"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_actionsAtServiceDefinitions)))
             AND "featureaction"."Feature_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_activeFeaturesAtFI)));
   --   ?() ;
      dbms_output.put_line('v_intersectedUserActions---->'||v_intersectedUserActions);

   OPEN  "records" FOR
      SELECT v_intersectedUserActions actions  
        FROM DUAL  ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);
   --   ?() ;
   SELECT LISTAGG(CAST("featureaction"."Feature_id" AS VARCHAR2(4000))) 

     INTO v_intersectedUserFeatures
     FROM "featureaction" 
    WHERE  "featureaction"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v_intersectedUserActions)));
   --   ?() ;
    dbms_output.put_line('v_intersectedUserFeatures---->'||v_intersectedUserFeatures);
   OPEN  "records1" FOR
      SELECT v_intersectedUserFeatures features  
        FROM DUAL  ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);
   --   ?() ;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure reports_threads_resolved



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "reports_threads_resolved" 
(
  v_from_date IN VARCHAR2,
  v__todate IN VARCHAR2,
  v_category_id IN VARCHAR2,
  v_csr_name IN VARCHAR2,"customerrequest" OUT SYS_REFCURSOR
)
AS
   v_queryStatement VARCHAR2(4000);

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   v_queryStatement := 'SELECT count("id") threads_resolved_count from "customerrequest" where "Status_id" = ''SID_RESOLVED''' ;
   IF v_from_date <> ' '
     AND v_from_date <> ' ' THEN
    v_queryStatement := (v_queryStatement) || ' and "createdts" >= ' || '''' || TO_DATE(REPLACE(v_from_date, SUBSTR(v_from_date, 11, 0), ' '),'YYYY-MON-DD HH24:MI:SS')|| '''' || (' and "createdts" <= ') || '''' || TO_DATE(REPLACE(v__todate, SUBSTR(v__todate, 11, 0), ' '),'YYYY-MON-DD HH24:MI:SS') || '''' ;
   END IF;
   IF v_category_id <> ' ' THEN
    v_queryStatement := (v_queryStatement) || (' ' || ' and "RequestCategory_id" = ') || '''' ||(v_category_id) || '''' ;
   END IF;
   IF v_csr_name <> ' ' THEN
    v_queryStatement := (v_queryStatement) || (' ' || ' and "AssignedTo" = ') ||  '''' || (v_csr_name) || '''' ;
   END IF;
   EXECUTE IMMEDIATE v_queryStatement;
   OPEN "customerrequest" FOR v_queryStatement;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure servicedefinitionactions_get_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "servicedefinitionactions_get_proc" 
(
  v__serviceDefinitionId IN VARCHAR2,
  "feature" OUT SYS_REFCURSOR
)
AS
   v_select_statement VARCHAR2(4000);


BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   v_select_statement := '(select "feature"."id" as featureId, "feature"."name" as featureName , "feature"."description" as featureDescription ,"feature"."Status_id" as featureStatus ,"featureaction"."id" as actionId , "featureaction"."name" as actionName , "featureaction"."description" as actionDescription,"featureaction"."status" as actionStatus from "feature"
          left join "featureaction" on ("featureaction"."Feature_id" = "feature"."id")where "featureaction"."id" in' ;
   v_select_statement := (v_select_statement|| '(select "servicedefinitionactionlimit"."actionId"
                                       from "servicedefinitionactionlimit" where
                                       "servicedefinitionactionlimit"."serviceDefinitionId"='|| ''''|| v__serviceDefinitionId|| ''''|| '))') ;
   OPEN  "feature" FOR
      SELECT v_select_statement 
        FROM DUAL  ;
      --DBMS_SQL.RETURN_RESULT("feature");
   EXECUTE IMMEDIATE v_select_statement;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure subscriber_getAlertSubtypePreferences



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "subscriber_getAlertSubtypePreferences" 
(
  "alerttypes" IN NVARCHAR2, "alertsubtype" out SYS_REFCURSOR
)
AS
   

BEGIN

   OPEN  "alertsubtype" FOR
      SELECT "id" "alertsubtypeid"  ,
             "AlertTypeId" "alerttypeid"  ,
             "attributeId" "attributeid"  ,
             "alertConditionId" "alertconditionid"  ,
             "value1" ,
             "value2" ,
             "isGlobal" "isglobal"  
        FROM "alertsubtype" 
       WHERE  FIND_IN_SET("alertsubtype"."AlertTypeId", "alerttypes") <> 0 ;
      


END;

/
--  DDL for Procedure subscriber_getCoreIdFromDbxIds



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "subscriber_getCoreIdFromDbxIds"
(
  "_dbxids" IN NVARCHAR2,"backendidentifier" out SYS_REFCURSOR
)
AS
    

BEGIN

   
   OPEN  "backendidentifier" FOR
      SELECT "BackendId" ,
             "Customer_id" 
        FROM "backendidentifier" 
       WHERE  FIND_IN_SET("backendidentifier"."Customer_id", "_dbxids") <> 0
                AND "BackendType" IS NULL ;
      


END;
/
--  DDL for Procedure subscriber_getCoreIdFromDbxIds_coreSpecific



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "subscriber_getCoreIdFromDbxIds_coreSpecific"
(
  "_dbxids" IN NVARCHAR2,
  "_coretype" IN NVARCHAR2,"backendidentifier" out SYS_REFCURSOR
)
AS
    

BEGIN

   OPEN  "backendidentifier" FOR
      SELECT "BackendId" ,
             "Customer_id" 
        FROM "backendidentifier" 
       WHERE  FIND_IN_SET("backendidentifier"."Customer_id", "_dbxids") <> 0
                AND "BackendType" = "_coretype" ;
      


END;
/
--  DDL for Procedure subscriber_getCustIdFromCore



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "subscriber_getCustIdFromCore"
(
  "_backendids" IN NVARCHAR2,"backendidentifier" out SYS_REFCURSOR
)
AS
    

BEGIN

   OPEN  "backendidentifier" FOR
      SELECT "BackendId" ,
             "Customer_id" 
        FROM "backendidentifier" 
       WHERE  FIND_IN_SET("backendidentifier"."BackendId", "_backendids") <> 0 ;
      


END;
/
--  DDL for Procedure subscriber_getEntitleMentsNocustomer



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "subscriber_getEntitleMentsNocustomer"
(
  "alerttypes" IN NVARCHAR2, "dbxcustomeralertentitlement" out SYS_REFCURSOR
)
AS
   

BEGIN

   OPEN  "dbxcustomeralertentitlement" FOR
      SELECT "Customer_id" "customerid"  ,
             "AlertTypeId" "alerttypeid"  ,
             "alertSubTypeId" "alertsubtypeid"  ,
             "AccountId" "accountid"  ,
             "AccountType" "accounttype"  ,
             "Value1" "value1"  ,
             "Value2" "value2"  
        FROM "dbxcustomeralertentitlement" 
       WHERE  FIND_IN_SET("dbxcustomeralertentitlement"."AlertTypeId", "alerttypes") <> 0 ;
      


END;
/
--  DDL for Procedure subscriber_getEntitleMentsWithcustomer



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "subscriber_getEntitleMentsWithcustomer"
(
  "alerttypes" IN NVARCHAR2,
  "custids" IN NVARCHAR2, "dbxcustomeralertentitlement" out SYS_REFCURSOR
)
AS
   

BEGIN

   OPEN  "dbxcustomeralertentitlement" FOR
      SELECT "Customer_id" "customerid"  ,
             "AlertTypeId" "alerttypeid"  ,
             "alertSubTypeId" "alertsubtypeid"  ,
             "AccountId" "accountid"  ,
             "AccountType" "accounttype"  ,
             "Value1" "value1"  ,
             "Value2" "value2"  
        FROM "dbxcustomeralertentitlement" 
       WHERE  FIND_IN_SET("dbxcustomeralertentitlement"."AlertTypeId", "alerttypes") <> 0
                AND FIND_IN_SET("dbxcustomeralertentitlement"."Customer_id", "custids") <> 0 ;
      


END;
/
--  DDL for Procedure transaction_proc_get



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "transaction_proc_get" 
(
  "transactions_query" IN VARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   v_stmt VARCHAR2(4000);

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   v_stmt := ('') || ("transactions_query") ;
--   EXECUTE IMMEDIATE v_stmt;
   OPEN "records" FOR v_stmt;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure Update_BulkWireTemplateRecipient_Count



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "Update_BulkWireTemplateRecipient_Count" 
(
  v__userID IN VARCHAR2,
  v__orgID IN VARCHAR2,
  v__payeeID IN VARCHAR2,
  v_cursor OUT SYS_REFCURSOR,v_cursor1 OUT SYS_REFCURSOR
)
AS
   v_filterLineItemQuery VARCHAR2(4000);
   v_filterTemplateQuery VARCHAR2(4000);
   v_transferType VARCHAR2(4000);
   v_updateTransferTypeTransactions VARCHAR2(4000);
   v_updatelineitemQuery VARCHAR2(4000);
   v_updateTemplateQuery VARCHAR2(4000);
   v_updateTransferTypeTemplateQuery VARCHAR2(4000);


BEGIN

   <<MainLabel>>
   IF ( v__userID IS NULL
     OR v__userID = ' ' )
     AND ( v__orgID IS NULL
     OR v__orgID = ' ' ) THEN

   BEGIN
      OPEN  v_cursor FOR
         SELECT 'Both user_id and company_id cannot be empty' Response  
           FROM DUAL  ;
         --DBMS_SQL.RETURN_RESULT(v_cursor);
      RETURN;

   END;
   END IF;
   IF ( v__payeeID IS NULL
     OR v__payeeID = ' ' ) THEN

   BEGIN
      OPEN  v_cursor FOR
         SELECT 'payeeId cannot be empty' Response  
           FROM DUAL  ;
         --DBMS_SQL.RETURN_RESULT(v_cursor);
      RETURN;

   END;
   END IF;
   IF ( v__orgID IS NOT NULL
     AND v__orgID != ' ' ) THEN

   BEGIN
      v_filterLineItemQuery := 'WHERE "payeeId" = ''' || v__payeeID || ''' AND "bulkWireTemplateID" IN (SELECT "bulkWireTemplateID" FROM "bulkwiretemplate" WHERE "company_id" = ''' || v__orgID || ''')' ;
      v_filterTemplateQuery := 'WHERE "payeeId" = ''' || v__payeeID || ''') AND "company_id" = ''' || v__orgID || '''' ;

   END;
   ELSE

   BEGIN
      v_filterLineItemQuery := 'WHERE "payeeId" = ''' || v__payeeID || ''' AND "createdby" = ''' || v__userID || '''' ;
      v_filterTemplateQuery := 'WHERE "payeeId" = ''' || v__payeeID || ''' AND "createdby" = ''' || v__userID || ''')' ;

   END;
   END IF;
   SELECT "bulkWireTransferType" 

     INTO v_transferType
     FROM "bulkwiretemplatelineitems" 
    WHERE  "payeeId" = v__payeeID 
     FETCH FIRST 1 ROWS ONLY;
   IF ( v_transferType = 'Domestic' ) THEN
    v_updateTransferTypeTransactions := 'UPDATE "bulkwiretemplate" SET "noOfDomesticTransactions" = "noOfDomesticTransactions" - 1 ' ;
   ELSE
      v_updateTransferTypeTransactions := 'UPDATE "bulkwiretemplate" SET "noOfInternationalTransactions" = "noOfInternationalTransactions" - 1 ' ;
   END IF;
   v_updatelineitemQuery := 'UPDATE "bulkwiretemplatelineitems"  SET "softdeleteflag" = 1 ' || v_filterLineItemQuery ;
   v_updateTemplateQuery := 'UPDATE "bulkwiretemplate" SET "noOfTransactions" = "noOfTransactions" - 1 WHERE "bulkWireTemplateID" IN (SELECT "bulkWireTemplateID" FROM "bulkwiretemplatelineitems"  ' || v_filterTemplateQuery || ')' ;
   v_updateTransferTypeTemplateQuery := v_updateTransferTypeTransactions || ' WHERE "bulkWireTemplateID" IN (SELECT "bulkWireTemplateID" FROM "bulkwiretemplatelineitems"  ' || v_filterTemplateQuery || ')' ;
   EXECUTE IMMEDIATE v_updatelineitemQuery;
   EXECUTE IMMEDIATE v_updateTemplateQuery;
   EXECUTE IMMEDIATE v_updateTransferTypeTemplateQuery;
   OPEN  v_cursor1 FOR
      SELECT 'SUCCESS' Response  
        FROM DUAL  ;
      --DBMS_SQL.RETURN_RESULT(v_cursor);


EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure update_customerstatus_default_legalentity



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "update_customerstatus_default_legalentity" 
(
  v_customerId IN VARCHAR2
)
AS
   v_statusId VARCHAR2(255) := '';
   v_currentStatus VARCHAR2(255) := '';
   v_activeStatus NUMBER(10,0) := 0;
   CURSOR statuses
     IS SELECT "customerlegalentity"."Status_id" 
     FROM "customerlegalentity"
    WHERE  "Customer_id" = v_customerId;

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   SELECT "customer"."Status_id" 

     INTO v_currentStatus
     FROM "customer" 
    WHERE  "customer"."id" = v_customerId;
   v_activeStatus := 0 ;
   OPEN statuses;
   FETCH statuses INTO v_statusId;
   WHILE ( statuses%FOUND = TRUE ) 
   LOOP 

      BEGIN
         IF v_statusId LIKE '%ACTIVE%'
           AND v_currentStatus LIKE '%ACTIVE%' THEN

         BEGIN
            v_activeStatus := 1 ;

         END;
         END IF;
         IF v_statusId LIKE '%ACTIVE%'
           AND v_currentStatus NOT LIKE '%ACTIVE%' THEN

         BEGIN
            v_activeStatus := 1 ;
            UPDATE "customer"
               SET "Status_id" = 'SID_CUS_ACTIVE'
             WHERE  "customer"."id" = v_customerId;

         END;
         END IF;
         FETCH statuses INTO v_statusId;

      END;
   END LOOP;
   CLOSE statuses;
   IF v_activeStatus = 0 THEN

   BEGIN
      UPDATE "customer"
         SET "Status_id" = 'SID_CUS_SUSPENDED'
       WHERE  "customer"."id" = v_customerId
        AND v_currentStatus NOT LIKE '%SID_CUS_NEW%';

   END;
   END IF;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure update_user_recent_currency



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "update_user_recent_currency" 
(
  v__customerId IN VARCHAR2,
  v__currencyCode IN VARCHAR2,
  v__legalEntityId IN VARCHAR2
)
AS
   v_rowWithGivenCustomerAndCurrency NUMBER(10,0) := 0;
   v_lengthOfRecentCurrencies NUMBER(10,0) := 0;
   v_recentCurrencyToDelete VARCHAR2(60);

BEGIN

   SELECT COUNT(*)  

     INTO v_rowWithGivenCustomerAndCurrency
     FROM "recentcurrencies" rc
    WHERE  rc."customerId" = v__customerId
             AND rc."quoteCurrencyCode" = v__currencyCode;
   SELECT COUNT(*)  

     INTO v_lengthOfRecentCurrencies
     FROM "recentcurrencies" rc
    WHERE  rc."customerId" = v__customerId;
   SELECT *
     INTO v_recentCurrencyToDelete
     FROM ( SELECT rc."id" 
     FROM "recentcurrencies" rc
    WHERE  rc."customerId" = v__customerId
     ORDER BY rc."createdts" ASC
     FETCH FIRST 1 ROWS ONLY );
   IF v_rowWithGivenCustomerAndCurrency >= 1 THEN

   BEGIN
      DELETE "recentcurrencies"

       WHERE  "recentcurrencies"."customerId" = v__customerId
                AND "recentcurrencies"."quoteCurrencyCode" = v__currencyCode;

   END;
   ELSE
      IF v_lengthOfRecentCurrencies >= 5 THEN

      BEGIN
         DELETE "recentcurrencies"

          WHERE  "recentcurrencies"."id" = v_recentCurrencyToDelete;

      END;
      END IF;
   END IF;
   INSERT INTO "recentcurrencies"
     ( "id", "customerId", "quoteCurrencyCode", "legalEntityId" )
     VALUES ( CONCAT(v__currencyCode, v__customerId), v__customerId, v__currencyCode, v__legalEntityId );

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure update_userstatus_by_legalentity_proc
create or replace NONEDITIONABLE PROCEDURE "update_userstatus_by_legalentity_proc" 
(
  "customerId" IN VARCHAR2,
  "statusId" IN VARCHAR2,
  "isOLB" IN VARCHAR2,
  "legalEntityList" IN VARCHAR2,
  "records"  OUT SYS_REFCURSOR
)
AS
   

BEGIN

   IF "isOLB" = 'true' THEN

   BEGIN
      UPDATE "customerlegalentity"
         SET "Status_id" = "statusId"
       WHERE  "Customer_id" = "customerId";
       OPEN  "records"  FOR
         SELECT * 
           FROM "customerlegalentity" 
          WHERE  "Customer_id" = "customerId"
                   AND ("customerlegalentity"."legalEntityId" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( "legalEntityList"))));

   END;
   ELSE

   BEGIN
      UPDATE "customerlegalentity"
         SET "Status_id" = "statusId"
       WHERE  "Customer_id" = "customerId"
        AND ("legalEntityId" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( "legalEntityList"))));
      OPEN  "records"  FOR
         SELECT * 
           FROM "customerlegalentity" 
          WHERE  "Customer_id" = "customerId"
                   AND ("customerlegalentity"."legalEntityId" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( "legalEntityList"))));
         --DBMS_SQL.RETURN_RESULT("customerlegalentity" );

   END;
   END IF;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/

--  DDL for Procedure update_customerstatus_default_legalentity


CREATE OR REPLACE NONEDITIONABLE PROCEDURE "update_customerstatus_default_legalentity" 
(
  v_customerId IN VARCHAR2
)
AS
   v_statusId VARCHAR2(255) := '';
   v_currentStatus VARCHAR2(255) := '';
   v_activeStatus NUMBER(10,0) := 0;
   CURSOR statuses
     IS SELECT "customerlegalentity"."Status_id" 
     FROM "customerlegalentity"
    WHERE  "Customer_id" = v_customerId;

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   SELECT "customer"."Status_id" 

     INTO v_currentStatus
     FROM "customer" 
    WHERE  "customer"."id" = v_customerId;
   v_activeStatus := 0 ;
   OPEN statuses;
   FETCH statuses INTO v_statusId;
   WHILE ( statuses%FOUND = TRUE ) 
   LOOP 

      BEGIN
         IF v_statusId LIKE '%ACTIVE%'
           AND v_currentStatus LIKE '%ACTIVE%' THEN

         BEGIN
            v_activeStatus := 1 ;

         END;
         END IF;
         IF v_statusId LIKE '%ACTIVE%'
           AND v_currentStatus NOT LIKE '%ACTIVE%' THEN

         BEGIN
            v_activeStatus := 1 ;
            UPDATE "customer"
               SET "Status_id" = 'SID_CUS_ACTIVE'
             WHERE  "customer"."id" = v_customerId;

         END;
         END IF;
         FETCH statuses INTO v_statusId;

      END;
   END LOOP;
   CLOSE statuses;
   IF v_activeStatus = 0 THEN

   BEGIN
      UPDATE "customer"
         SET "Status_id" = 'SID_CUS_SUSPENDED'
       WHERE  "customer"."id" = v_customerId
        AND v_currentStatus NOT LIKE '%SID_CUS_NEW%';

   END;
   END IF;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure user_account_default_actions_create_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "user_account_default_actions_create_proc" 
(
  v__userId IN VARCHAR2,
  v__accountsCSV IN VARCHAR2,
  v__coreCustomerId IN VARCHAR2,
  v__contractId IN VARCHAR2,
  v__groupId IN VARCHAR2
)
AS
   v_finished NUMBER(10,0) := 0;
   v_featureActionId VARCHAR2(255) := '';
   v_actionslist VARCHAR2(4000) := '';
   v_limitId VARCHAR2(255) := '';
   v_entryStatus NUMBER(10,0) := 0;
   v_accountId VARCHAR2(255) := '';
   v_actualLimitId VARCHAR2(255) := '';
   v_serviceDefinitionId VARCHAR2(255) := '';
   v_serviceType VARCHAR2(255) := '';
   v_validFIActions CLOB := '';
   v_validServiceDefinitionActions VARCHAR2(4000) := '';
   v_validGroupActions VARCHAR2(4000) := '';
   v_validActionsList CLOB := '';
   v_groupId VARCHAR2(255) := '';
   v_featureId VARCHAR2(255) := '';
   v_limitvalue VARCHAR2(255) := '';
   v_id VARCHAR2(255) := '';
   v_limitGroupId VARCHAR2(255) := '';
   v_coreCustomerId VARCHAR2(255) := '';
   v_accounts VARCHAR2(4000);
   accounts SYS_REFCURSOR;

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   SELECT "contract"."servicedefinitionId" 

     INTO v_serviceDefinitionId
     FROM "contract" 
    WHERE  "contract"."id" = v__contractId;
   SELECT "servicedefinition"."serviceType" 

     INTO v_serviceType
     FROM "servicedefinition" 
    WHERE  "servicedefinition"."id" = v_serviceDefinitionId;
   IF ( CASE 
             WHEN v__groupId IS NULL THEN 1
   ELSE 0
      END <> 0
     OR v__groupId = ' ' ) THEN
    SELECT "groupservicedefinition"."Group_id" 

     INTO v_groupId
     FROM "groupservicedefinition" 
    WHERE  "groupservicedefinition"."serviceDefinitionId" = v_serviceDefinitionId
             AND "groupservicedefinition"."isDefaultGroup" = '1';
   END IF;
   SELECT LISTAGG(CAST("featureaction"."id" AS VARCHAR(255)), ',') 

     INTO v_validFIActions
     FROM "featureaction" 
    WHERE  "featureaction"."Feature_id" IN ( SELECT "contractfeatures"."featureId" 
                                                     FROM "contractfeatures" 
                                                      WHERE  "contractfeatures"."contractId" = v__contractId )
   ;
   SELECT LISTAGG(CAST("servicedefinitionactionlimit"."actionId" AS VARCHAR2(255)), ',') 

     INTO v_validActionsList
     FROM "servicedefinitionactionlimit" 
    WHERE  "servicedefinitionactionlimit"."serviceDefinitionId" = v_serviceDefinitionId
             AND ("servicedefinitionactionlimit"."actionId" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_validFIActions))));
   v_accounts := 'SELECT "customeraccounts"."Account_id"
     FROM "customeraccounts" 
    WHERE  "customeraccounts"."contractId" = v__contractId
     AND "customeraccounts"."coreCustomerId" = v__coreCustomerId
     AND "customeraccounts"."Customer_id" = v__userId
     AND ("customeraccounts"."Account_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v__accountsCSV))))' ;
   OPEN accounts FOR
      v_accounts ;
   FETCH accounts INTO v_accountId;
   <<loop_2>>
   WHILE ( (accounts%FOUND) = TRUE ) 
   LOOP 
      DECLARE
         CURSOR accountLevelPermissions
           IS SELECT "featureaction"."id" 
           FROM "featureaction" 
          WHERE  ("featureaction"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_validActionsList))))
           AND ( "featureaction"."Type_id" = 'NON_MONETARY'
           AND "featureaction"."isAccountLevel" = '1' );

      BEGIN
         OPEN accountLevelPermissions;
         FETCH accountLevelPermissions INTO v_featureActionId;
         <<loop_1>>
         WHILE ( (accountLevelPermissions%FOUND) = TRUE ) 
         LOOP 

            BEGIN
               SELECT "featureaction"."Feature_id" 

                 INTO v_featureId
                 FROM "featureaction" 
                WHERE  "featureaction"."id" = v_featureActionId;
               v_entryStatus := 0 ;
               SELECT SUBSTR(SYS_GUID(), 0, 50) 

                 INTO v_id
                 FROM DUAL ;
               INSERT INTO "contractactionlimit"
                 ( "id", "contractId", "coreCustomerId", "accountId", "featureId", "actionId" )
                 VALUES ( v_id, v__contractId, v__coreCustomerId, v_accountId, v_featureId, v_featureActionId );
               FETCH accountLevelPermissions INTO v_featureActionId;
               GOTO loop_1;

            END;
         END LOOP;
         CLOSE accountLevelPermissions;
         FETCH accounts INTO v_accountId;
         GOTO loop_2;

      END;
   END LOOP;
   CLOSE accounts;
   v_accounts := 'SELECT "customeraccounts"."Account_id"
     FROM "customeraccounts" 
    WHERE  "customeraccounts"."contractId" = v__contractId
     AND "customeraccounts"."coreCustomerId" = v__coreCustomerId
     AND "customeraccounts"."Customer_id" = v__userId
     AND ("customeraccounts"."Account_id"IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v__accountsCSV))))' ;
   OPEN accounts FOR
      v_accounts ;
   FETCH accounts INTO v_accountId;
   <<loop_3>>
   WHILE ((accounts%FOUND) = TRUE ) 
   LOOP 
      DECLARE
         CURSOR actions
           IS SELECT "id" 
           FROM "featureaction" 
          WHERE  ("featureaction"."id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_validActionsList))))
           AND ( "featureaction"."isAccountLevel" = '1'
           OR "featureaction"."isAccountLevel" = 'true' );

      BEGIN
         OPEN actions;
         FETCH actions INTO v_featureActionId;
         <<loop_2>>
         WHILE ( (actions%FOUND) = TRUE ) 
         LOOP 
            DECLARE
               CURSOR limits
                 IS SELECT "LimitType_id" 
                 FROM "actionlimit" 
                WHERE  "Action_id" = v_featureActionId;

            BEGIN
               SELECT "featureaction"."Feature_id" 

                 INTO v_featureId
                 FROM "featureaction" 
                WHERE  "featureaction"."id" = v_featureActionId;
               SELECT "featureaction"."limitgroupId" 

                 INTO v_limitGroupId
                 FROM "featureaction" 
                WHERE  "featureaction"."id" = v_featureActionId;
               OPEN limits;
               FETCH limits INTO v_limitId;
               <<loop_1>>
               WHILE ((limits%FOUND) = TRUE ) 
               LOOP 

                  BEGIN
                     SELECT "contractactionlimit"."value" 

                       INTO v_limitvalue
                       FROM "contractactionlimit" 
                      WHERE  "contractactionlimit"."actionId" = v_featureActionId
                               AND "contractactionlimit"."limitTypeId" = v_limitId
                               AND "contractactionlimit"."contractId" = v__contractId
                               AND "contractactionlimit"."coreCustomerId" = v__coreCustomerId;
                     IF ( v_limitId = 'MAX_TRANSACTION_LIMIT' ) THEN

                     BEGIN
                        v_actualLimitId := 'PRE_APPROVED_TRANSACTION_LIMIT' ;
                        SELECT SUBSTR(SYS_GUID(), 0, 50) 

                          INTO v_id
                          FROM DUAL ;
                        INSERT INTO "customeraction"
                          ( "customeraction"."id", "customeraction"."RoleType_id", "customeraction"."Customer_id", "customeraction"."contractId", "customeraction"."coreCustomerId", "customeraction"."featureId", "customeraction"."Action_id", "customeraction"."Account_id", "customeraction"."isAllowed", "customeraction"."LimitType_id", "customeraction"."value" )
                          VALUES ( v_id, v_serviceType, v__userId, v__contractId, v__coreCustomerId, v_featureId, v_featureActionId, v_accountId, 1, v_actualLimitId, 0.00 );
                        v_actualLimitId := 'AUTO_DENIED_TRANSACTION_LIMIT' ;

                     END;
                     ELSE
                        IF ( v_limitId = 'DAILY_LIMIT' ) THEN

                        BEGIN
                           v_actualLimitId := 'PRE_APPROVED_DAILY_LIMIT' ;
                           SELECT SUBSTR(SYS_GUID(), 0, 50) 

                             INTO v_id
                             FROM DUAL ;
                           INSERT INTO "customeraction"
                             ( "customeraction"."id", "customeraction"."RoleType_id", "customeraction"."Customer_id", "customeraction"."contractId", "customeraction"."coreCustomerId", "customeraction"."featureId", "customeraction"."Action_id", "customeraction"."Account_id", "customeraction"."isAllowed", "customeraction"."LimitType_id", "customeraction"."value" )
                             VALUES ( v_id, v_serviceType, v__userId, v__contractId, v__coreCustomerId, v_featureId, v_featureActionId, v_accountId, 1, v_actualLimitId, 0.00 );
                           v_actualLimitId := 'AUTO_DENIED_DAILY_LIMIT' ;

                        END;
                        ELSE
                           IF ( v_limitId = 'WEEKLY_LIMIT' ) THEN

                           BEGIN
                              v_actualLimitId := 'PRE_APPROVED_WEEKLY_LIMIT' ;
                              SELECT SUBSTR(SYS_GUID(), 0, 50) 

                                INTO v_id
                                FROM DUAL ;
                              INSERT INTO "customeraction"
                                ( "customeraction"."id", "customeraction"."RoleType_id", "customeraction"."Customer_id", "customeraction"."contractId", "customeraction"."coreCustomerId", "customeraction"."featureId", "customeraction"."Action_id", "customeraction"."Account_id", "customeraction"."isAllowed", "customeraction"."LimitType_id", "customeraction"."value" )
                                VALUES ( v_id, v_serviceType, v__userId, v__contractId, v__coreCustomerId, v_featureId, v_featureActionId, v_accountId, 1, v_actualLimitId, 0.00 );
                              v_actualLimitId := 'AUTO_DENIED_WEEKLY_LIMIT' ;

                           END;
                           END IF;
                        END IF;
                     END IF;
                     IF ( v_limitId != 'MIN_TRANSACTION_LIMIT' ) THEN

                     BEGIN
                        SELECT SUBSTR(SYS_GUID(), 0, 50) 

                          INTO v_id
                          FROM DUAL ;
                        INSERT INTO "customeraction"
                          ( "customeraction"."id", "customeraction"."RoleType_id", "customeraction"."Customer_id", "customeraction"."contractId", "customeraction"."coreCustomerId", "customeraction"."featureId", "customeraction"."Action_id", "customeraction"."Account_id", "customeraction"."isAllowed", "customeraction"."LimitType_id", "customeraction"."value" )
                          VALUES ( v_id, v_serviceType, v__userId, v__contractId, v__coreCustomerId, v_featureId, v_featureActionId, v_accountId, 1, v_actualLimitId, v_limitvalue );

                     END;
                     END IF;
                     v_entryStatus := 1 ;
                     FETCH limits INTO v_limitId;
                     GOTO loop_1;

                  END;
               END LOOP;
               CLOSE limits;
               FETCH actions INTO v_featureActionId;
               GOTO loop_2;

            END;
         END LOOP;
         CLOSE actions;
         FETCH accounts INTO v_accountId;
         GOTO loop_3;

      END;
   END LOOP;
   CLOSE accounts;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure user_accountactions_get_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "user_accountactions_get_proc" 
(
  "_userId" IN VARCHAR2,
  "_coreCustomerId" IN VARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   v_contractId VARCHAR2(255) := '';
   v_serviceDefinitionId VARCHAR2(255) := '';
   v_groupId VARCHAR2(255) := '';
   v_validFIAccountLevelActions VARCHAR2(4000) := '';
   v_validServiceDefinitinActions VARCHAR2(4000) := '';
   v_validGroupActions CLOB := '';
   v_validUserActions VARCHAR2(4000) := '';
   v_actionCondition VARCHAR2(4000) := '';
   v_select_statement VARCHAR2(4000) := '';


BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
-- SET @contractId =  (SELECT "contractcorecustomers".contractId FROM "contractcorecustomers"where "contractcorecustomers".coreCustomerId = @_coreCustomerId)  

   SELECT "contractcorecustomers"."contractId" 
     INTO v_contractId
     FROM "contractcorecustomers" 
    WHERE  "contractcorecustomers"."coreCustomerId" = "_coreCustomerId";

--  SET @serviceDefinitionId =  (SELECT "contract".servicedefinitionId FROM contract where "contract".id = @contractId)  
   SELECT "contract"."servicedefinitionId" 

     INTO v_serviceDefinitionId
     FROM "contract" 
    WHERE  "contract"."id" = v_contractId;

--  SET @groupId =  (SELECT "customergroup".Group_id FROM "customergroup" where "customergroup".contractId = @contractId AND "customergroup".coreCustomerId = @_coreCustomerId AND dbxschemaname]."customergroup".Customer_id = @_userId)  
   SELECT DISTINCT "customergroup"."Group_id" 
     INTO v_groupId
     FROM "customergroup" 
    WHERE  "customergroup"."contractId" = v_contractId
             AND "customergroup"."coreCustomerId" = "_coreCustomerId"
             AND "customergroup"."Customer_id" = "_userId";

--  SET @groupId =  (SELECT "groupservicedefinition".Group_id FROM "groupservicedefinition" where "groupservicedefinition".serviceDefinitionId = @serviceDefinitionId AND "groupservicedefinition".Group_id = @groupId)  
   SELECT "groupservicedefinition"."Group_id" 

     INTO v_groupId
     FROM "groupservicedefinition" 
    WHERE  "groupservicedefinition"."serviceDefinitionId" = v_serviceDefinitionId
             AND "groupservicedefinition"."Group_id" = v_groupId;
   IF v_groupId IS NULL THEN
    v_groupId := '';
   END IF;
   --    SET @validFIAccountLevelActions =  (SELECT LISTAGG(CAST("featureaction".id AS VARCHAR2(255)), ',') FROM "featureaction" WHERE 
   --                                   ("featureaction".isAccountLevel = '1' OR "featureaction".isAccountLevel = 'true') AND
   --                                   "featureaction"."status" = 'SID_ACTION_ACTIVE')
   --    SET @validServiceDefinitinActions =  (SELECT LISTAGG(CAST("servicedefinitionactionlimit".actionId AS VARCHAR2(255)), ',') FROM "servicedefinitionactionlimit" WHERE 
   --                                   "servicedefinitionactionlimit".serviceDefinitionId = @serviceDefinitionId AND
   --                                   "servicedefinitionactionlimit".actionId in (select distinct value from STRING_SPLIT(@validFIAccountLevelActions, ',')))
   --    SET @validGroupActions =  (SELECT LISTAGG(CAST("groupactionlimit".Action_id AS VARCHAR2(255)), ',') FROM "groupactionlimit" WHERE 
   --                                   "groupactionlimit".Group_id = @groupId AND
   --                                   "groupactionlimit".Action_id in (select distinct value from STRING_SPLIT(@validServiceDefinitinActions, ',')))

   SELECT STRING_AGG(CAST("groupactionlimit"."Action_id" AS VARCHAR2(255)))  

     INTO v_validGroupActions
     FROM "groupactionlimit" 
            JOIN "servicedefinitionactionlimit"    ON ( "groupactionlimit"."Action_id" = "servicedefinitionactionlimit"."actionId" )
            JOIN "featureaction"    ON ( "featureaction"."id" = "servicedefinitionactionlimit"."actionId" )
    WHERE  "groupactionlimit"."Group_id" = v_groupId
             AND "servicedefinitionactionlimit"."serviceDefinitionId" = v_serviceDefinitionId
             AND "featureaction"."isAccountLevel" = 1
             AND "featureaction"."status" = 'SID_ACTION_ACTIVE';

   --    SET @validUserActions =  (SELECT LISTAGG(CAST(Action_id AS VARCHAR2(255)), ',') FROM
   --                                                                                                                     (select distinct "customeraction".Action_id from "customeraction" WHERE 
   --                                   "customeraction".Customer_id = @_userId AND
   --                                   "customeraction".coreCustomerId = @_coreCustomerId AND
   --                                   "customeraction".contractId = @contractId AND
   --                                   ("customeraction".isAllowed = '1'  OR "customeraction".isAllowed = 'true') AND
   --                                   "customeraction".Action_id in (select distinct value from STRING_SPLIT(@validGroupActions, ',')))
   --                                                                                                         as x);
   -- SET @actionCondition = (N'')+(N'"customeraction".Action_id in (select distinct value from STRING_SPLIT(') + (@validUserActions) + (N''','')')
 
   OPEN  "records" FOR
      SELECT "customeraction"."Customer_id" "Customer_id"  ,
             "customeraction"."contractId" "contractId"  ,
             "customeraction"."coreCustomerId" "coreCustomerId"  ,
             "customeraction"."Account_id" "Account_id"  ,
             "customeraction"."featureId" "featureId"  ,
             "customeraction"."Action_id" "Action_id"  ,
             "customeraction"."RoleType_id" "RoleType_id"  ,
             "customeraction"."LimitType_id" "LimitType_id"  ,
             "customeraction"."value" "value"  
        FROM "customeraction" 
       WHERE  "customeraction"."Customer_id" = "_userId"
                AND ( "customeraction"."isAllowed" = 1 )
                AND "customeraction"."Action_id" IN ( SELECT DISTINCT COLUMN_VALUE 
                                                              FROM TABLE(UTILS.STRING_SPLIT(v_validGroupActions,','))  )

                AND "customeraction"."coreCustomerId" = "_coreCustomerId"
                AND "customeraction"."contractId" = v_contractId ;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure user_associated_corecustomeraccounts_info



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "user_associated_corecustomeraccounts_info" 
(
  "customerId" IN VARCHAR2,
  "records" OUT SYS_REFCURSOR,
  "records1" OUT SYS_REFCURSOR,
  "records2" OUT SYS_REFCURSOR,
  "records3" OUT SYS_REFCURSOR,
  "records4" OUT SYS_REFCURSOR,
  "records5" OUT SYS_REFCURSOR
)
AS
   v_implictCIF VARCHAR2(32767);
   v_accountIds VARCHAR2(4000);


BEGIN

   SELECT LISTAGG(CAST("contractcustomers"."coreCustomerId" AS VARCHAR2(255))) 
     INTO v_implictCIF
     FROM "contractcustomers" 
    WHERE  "contractcustomers"."customerId" = "customerId"
             AND "contractcustomers"."autoSyncAccounts" = '1';
             
   OPEN  "records" FOR
      SELECT "contractcorecustomers"."coreCustomerId" ,
             "contractcorecustomers"."contractId" 
        FROM "contractcorecustomers" 
       WHERE  "contractcorecustomers"."coreCustomerId" IN ( SELECT DISTINCT COLUMN_VALUE 
                                                                    FROM TABLE(UTILS.STRING_SPLIT(v_implictCIF, ',')));
   IF (NVL("customerId", ' ') != ' ' ) THEN
   BEGIN
      OPEN  "records1" FOR
         SELECT "customeraccounts"."Account_id" "nonCIFAccounts"  
           FROM "customeraccounts" 
          WHERE  "customeraccounts"."Customer_id" = "customerId"
                   AND "customeraccounts"."coreCustomerId" NOT IN ( SELECT DISTINCT COLUMN_VALUE 
                                                                            FROM TABLE(UTILS.STRING_SPLIT(v_implictCIF, ',')));

      OPEN  "records2" FOR
         SELECT "contractaccounts"."accountId" "contractaccounts"  
           FROM "contractaccounts" 
          WHERE  "contractaccounts"."coreCustomerId" IN ( SELECT DISTINCT COLUMN_VALUE 
                                                                  FROM TABLE(UTILS.STRING_SPLIT(v_implictCIF, ',')));
      OPEN  "records3" FOR
         SELECT "excludedcontractaccounts"."accountId" "excludedcontractaccounts"  
           FROM "excludedcontractaccounts"
          WHERE  "excludedcontractaccounts"."coreCustomerId" IN ( SELECT DISTINCT COLUMN_VALUE 
                                                                          FROM TABLE(UTILS.STRING_SPLIT(v_implictCIF, ',')));
      OPEN  "records4" FOR
         SELECT "customeraccounts"."Account_id" "customeraccounts"  
           FROM "customeraccounts" 
          WHERE  "customeraccounts"."coreCustomerId" IN ( SELECT DISTINCT COLUMN_VALUE 
                                                                  FROM TABLE(UTILS.STRING_SPLIT(v_implictCIF, ','))  )

                   AND "customeraccounts"."Customer_id" = "customerId" ;

      OPEN  "records5" FOR
         SELECT "excludedcustomeraccounts"."Account_id" "excludedcustomeraccounts"  
           FROM "excludedcustomeraccounts" 
          WHERE  "excludedcustomeraccounts"."coreCustomerId" IN ( SELECT DISTINCT COLUMN_VALUE 
                                                                          FROM TABLE(UTILS.STRING_SPLIT(v_implictCIF, ','))  )

                   AND "excludedcustomeraccounts"."Customer_id" = "customerId" ;

   END;
   ELSE

   BEGIN
      OPEN  "records1" FOR
         SELECT "customeraccounts"."Account_id" "nonCIFAccounts"  
           FROM "customeraccounts" 
          WHERE  "customeraccounts"."Customer_id" = "customerId" ;

   END;
   END IF;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure user_customers_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "user_customers_proc" 
(
  "_customerId" IN VARCHAR2,
  "_coreCustomerId" IN VARCHAR2,
  "_legalEntityId" IN VARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   v_select_statement VARCHAR2(4000);
   v_isWhereAppened VARCHAR2(100);
   v_shouldAndAppend VARCHAR2(100);

BEGIN

   v_select_statement := ('(SELECT 
               "contractcustomers"."customerId" AS "customerId",
               "contractcustomers"."coreCustomerId" AS "coreCustomerId",
        "contractcustomers"."companyLegalUnit" AS "companyLegalUnit",
               "contractcustomers"."contractId" AS "contractId",
        "contractcustomers"."autoSyncAccounts" AS "autoSyncAccounts",
               "contract"."name" AS "contractName",
               "contractcustomers"."isPrimary" AS "isPrimary",
               "contractcorecustomers"."coreCustomerName" AS "coreCustomerName",
               "contractcorecustomers"."isBusiness" AS "isBusiness",
               "contract"."servicedefinitionId" AS "serviceDefinitionId",
               "servicedefinition"."name" AS "serviceDefinitionName",
               "membergrouptype"."description" AS "serviceDefinitionType",
               "membergroup"."id" AS "roleId",
               "membergroup"."Name" AS "userRole"
           FROM
               (((((("contractcustomers"
               LEFT JOIN "contractcorecustomers" ON ((("contractcorecustomers"."contractId" = "contractcustomers"."contractId")
                   AND ("contractcorecustomers"."coreCustomerId" = "contractcustomers"."coreCustomerId"))))
               LEFT JOIN "contract" ON (("contract"."id" = "contractcorecustomers"."contractId")))
               LEFT JOIN "servicedefinition" ON (("servicedefinition"."id" = "contract"."servicedefinitionId")))
               LEFT JOIN "membergrouptype" ON (("membergrouptype"."id" = "servicedefinition"."serviceType")))
               LEFT JOIN "customergroup" ON ((("customergroup"."Customer_id" = "contractcustomers"."customerId")
                   AND ("customergroup"."contractId" = "contractcustomers"."contractId")
                   AND ("customergroup"."coreCustomerId" = "contractcustomers"."coreCustomerId"))))
               LEFT JOIN "membergroup" ON (("membergroup"."id" = "customergroup"."Group_id")))') ;
   v_isWhereAppened := 'false' ;
   v_shouldAndAppend := 'false' ;
   IF ( "_customerId" IS NOT NULL ) THEN
   BEGIN
      IF ( v_isWhereAppened = 'false' ) THEN

      BEGIN
         v_select_statement := (v_select_statement|| ' where') ;
         v_isWhereAppened := 'true' ;
         v_shouldAndAppend := 'true' ;
      END;
      END IF;
      v_select_statement := (v_select_statement|| ' "contractcustomers"."customerId" = '|| '''' || "_customerId" || '''') ;

   END;
   END IF;
   IF ( "_legalEntityId" IS NOT NULL ) THEN

   BEGIN
      IF ( v_isWhereAppened = 'false' ) THEN

      BEGIN
         v_select_statement := CONCAT(v_select_statement, ' where') ;
         v_shouldAndAppend := 'true' ;

      END;
      END IF;
      IF ( v_isWhereAppened = 'true'
        AND v_shouldAndAppend = 'true' ) THEN

      BEGIN
         v_select_statement := (v_select_statement|| ' and') ;
         v_shouldAndAppend := 'true' ;

      END;
      END IF;
      v_select_statement := (v_select_statement|| ' "contractcustomers"."companyLegalUnit" = '|| '''' || "_legalEntityId") || '''' ;

   END;
   END IF;
   IF ( "_coreCustomerId" IS NOT NULL ) THEN

   BEGIN
      IF ( v_isWhereAppened = 'false' ) THEN

      BEGIN
         v_select_statement := CONCAT(v_select_statement, ' where') ;
         v_shouldAndAppend := 'true' ;

      END;
      END IF;
      IF ( v_isWhereAppened = 'true'
        AND v_shouldAndAppend = 'true' ) THEN

      BEGIN
         v_select_statement := (v_select_statement|| ' and') ;
         v_shouldAndAppend := 'true' ;

      END;
      END IF;
      v_select_statement := (v_select_statement|| ' "contractcustomers"."coreCustomerId" = '|| '''' || "_coreCustomerId") || '''' ;

   END;
   END IF;
   v_select_statement := (v_select_statement|| ')') ;
--   EXECUTE IMMEDIATE v_select_statement;
    DBMS_OUTPUT.PUT_LINE(v_select_statement);
   OPEN "records" FOR v_select_statement;
EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure user_customers_withaccounts_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "user_customers_withaccounts_proc" 
(
  "_customerId" IN VARCHAR2,
  "_coreCustomerId" IN VARCHAR2,
  "_legalEntityId" IN VARCHAR2,"records" OUT SYS_REFCURSOR
)
AS
   v_select_statement VARCHAR2(4000);
   v_isWhereAppened VARCHAR2(100);
   v_shouldAndAppend VARCHAR2(100);

BEGIN

   v_select_statement := '(SELECT
   "contractcustomers"."customerId" AS "customerId",
   "contractcustomers"."companyLegalUnit" AS "companyLegalUnit",
   "contractcustomers"."coreCustomerId" AS "coreCustomerId",
   "contractcustomers"."contractId" AS "contractId",
   "contractcustomers"."autoSyncAccounts" AS "autoSyncAccounts",
   "contract"."name" AS "contractName",
   "contractcustomers"."isPrimary" AS "isPrimary",
   "contractcorecustomers"."coreCustomerName" AS "coreCustomerName",
   "contractcorecustomers"."isBusiness" AS "isBusiness",
   "contract"."servicedefinitionId" AS "serviceDefinitionId",
   "servicedefinition"."name" AS "serviceDefinitionName",
   "membergrouptype"."description" AS "serviceDefinitionType",
   "membergroup"."id" AS "roleId",
   "membergroup"."Name" AS "userRole",
   "contractaccounts"."accountId",
   "contractaccounts"."accountName",
   "contractaccounts"."statusDesc",
   "contractaccounts"."typeId"
   FROM
   "contractcustomers"
   LEFT JOIN "contract" ON ("contract"."id" = "contractcustomers"."contractId")
   LEFT JOIN "contractcorecustomers" ON (("contractcorecustomers"."contractId" = "contractcustomers"."contractId")
   AND ("contractcorecustomers"."coreCustomerId" = "contractcustomers"."coreCustomerId"))
   LEFT JOIN "contractaccounts" ON (("contractaccounts"."contractId" = "contractcustomers"."contractId")
   AND ("contractaccounts"."coreCustomerId" = "contractcustomers"."coreCustomerId"))
   LEFT JOIN "servicedefinition" ON ("servicedefinition"."id" = "contract"."servicedefinitionId")
   LEFT JOIN "membergrouptype" ON ("membergrouptype"."id" = "servicedefinition"."serviceType")
   LEFT JOIN "customergroup" ON (("customergroup"."Customer_id" = "contractcustomers"."customerId")
   AND ("customergroup"."contractId" = "contractcustomers"."contractId")
   AND ("customergroup"."coreCustomerId" = "contractcustomers"."coreCustomerId"))
   LEFT JOIN "membergroup" ON ("membergroup"."id" = "customergroup"."Group_id")' ;
   v_isWhereAppened := 'false' ;
   v_shouldAndAppend := 'false' ;
   IF ( "_customerId" != ' ' ) THEN

   BEGIN
      IF ( v_isWhereAppened = 'false' ) THEN

      BEGIN
         v_select_statement := (v_select_statement || ' where') ;
         v_isWhereAppened := 'true' ;
         v_shouldAndAppend := 'true' ;

      END;
      END IF;
      v_select_statement := v_select_statement || ' "contractcustomers"."customerId" = '|| '''' || "_customerId" || '''' ;

   END;
   END IF;
   IF ( "_legalEntityId" != ' ' ) THEN

   BEGIN
      IF ( v_isWhereAppened = 'false' ) THEN

      BEGIN
         v_select_statement := CONCAT(v_select_statement, ' where') ;
         v_shouldAndAppend := 'true' ;

      END;
      END IF;
      IF ( v_isWhereAppened = 'true'
        AND v_shouldAndAppend = 'true' ) THEN

      BEGIN
         v_select_statement := (v_select_statement || ' and') ;
         v_shouldAndAppend := 'true' ;

      END;
      END IF;
      v_select_statement := (v_select_statement|| ' "contractcustomers"."companyLegalUnit" = '|| '''' || "_legalEntityId") || '''' ;

   END;
   END IF;
   IF ( "_coreCustomerId" != ' ' ) THEN

   BEGIN
      IF ( v_isWhereAppened = 'false' ) THEN

      BEGIN
         v_select_statement := CONCAT(v_select_statement, ' where') ;
         v_shouldAndAppend := 'true' ;

      END;
      END IF;
      IF ( v_isWhereAppened = 'true'
        AND v_shouldAndAppend = 'true' ) THEN

      BEGIN
         v_select_statement := (v_select_statement|| ' and') ;
         v_shouldAndAppend := 'true' ;

      END;
      END IF;
      v_select_statement := (v_select_statement|| ' "contractcustomers"."coreCustomerId" = '|| '''' || "_coreCustomerId") || '''' ;

   END;
   END IF;
   v_select_statement := CONCAT(v_select_statement, ')') ;
   dbms_output.put_line('statement'||v_select_statement);
   EXECUTE IMMEDIATE v_select_statement;
   OPEN "records" FOR v_select_statement;
END;
/
--  DDL for Procedure user_limitgroup_limits_create_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "user_limitgroup_limits_create_proc" 
(
  v__userId IN VARCHAR2,
  v__coreCustomerId IN VARCHAR2,
  v__contractId IN VARCHAR2
)
AS
   v_singlePaymentsActions CLOB := '';
   v_bulkPaymentsActions CLOB := '';
   v_max_per_transaction_single_payment VARCHAR2(4000) := '';
   v_max_per_transaction_single_paymentnum NUMBER(20,2);
   v_max_per_transaction_bulk_payment VARCHAR2(4000) := '';
   v_max_per_transaction_bulk_paymentnum NUMBER(20,2);
   v_max_daily_limit_single_payment VARCHAR2(4000) := '';
   v_max_daily_limit_single_paymentnum NUMBER(20,2);
   v_max_daily_limit_bulk_payment VARCHAR2(4000) := '';
   v_max_daily_limit_bulk_paymentnum NUMBER(20,2);
   v_max_weekly_limit_single_payment VARCHAR2(4000) := '';
   v_max_weekly_limit_single_paymentnum NUMBER(20,2);
   v_max_weekly_limit_bulk_payment VARCHAR2(4000) := '';
   v_max_weekly_limit_bulk_paymentnum NUMBER(20,2);

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   SELECT LISTAGG(CAST("featureaction"."id" AS VARCHAR2(255)),',') 

     INTO v_singlePaymentsActions
     FROM "featureaction" 
    WHERE  "featureaction"."limitgroupId" = 'SINGLE_PAYMENT';
   SELECT LISTAGG(CAST("featureaction"."id" AS VARCHAR2(255)),',') 

     INTO v_bulkPaymentsActions
     FROM "featureaction" 
    WHERE  "featureaction"."limitgroupId" = 'BULK_PAYMENT';
   SELECT MAX("customeraction"."value")  

     INTO v_max_per_transaction_single_payment
     FROM "customeraction" 
    WHERE  "customeraction"."contractId" = v__contractId
             AND "customeraction"."coreCustomerId" = v__coreCustomerId
             AND "customeraction"."Customer_id" = v__userId
             AND ("customeraction"."Action_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_singlePaymentsActions))))
             AND "customeraction"."LimitType_id" = 'AUTO_DENIED_TRANSACTION_LIMIT'
             AND "customeraction"."Account_id" <> ' '
             AND "customeraction"."value" IS NOT NULL;
   v_max_per_transaction_single_paymentnum := TO_NUMBER(v_max_per_transaction_single_payment,20,2) ;
   SELECT MAX("customeraction"."value")  

     INTO v_max_per_transaction_bulk_payment
     FROM "customeraction" 
    WHERE  "customeraction"."contractId" = v__contractId
             AND "customeraction"."coreCustomerId" = v__coreCustomerId
             AND "customeraction"."Customer_id" = v__userId
             AND ("customeraction"."Action_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_bulkPaymentsActions))))
             AND "customeraction"."LimitType_id" = 'AUTO_DENIED_TRANSACTION_LIMIT'
             AND "customeraction"."Account_id" <> ' '
             AND "customeraction"."value" IS NOT NULL;
   v_max_per_transaction_bulk_paymentnum := TO_NUMBER(v_max_per_transaction_bulk_payment,20,2) ;
   SELECT SUM("customeraction"."value")  

     INTO v_max_daily_limit_single_payment
     FROM "customeraction" 
    WHERE  "customeraction"."contractId" = v__contractId
             AND "customeraction"."coreCustomerId" = v__coreCustomerId
             AND "customeraction"."Customer_id" = v__userId
             AND ("customeraction"."Action_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_singlePaymentsActions))))
             AND "customeraction"."LimitType_id" = 'AUTO_DENIED_DAILY_LIMIT'
             AND "customeraction"."Account_id" <> ' '
             AND "customeraction"."value" IS NOT NULL;
   v_max_daily_limit_single_paymentnum := TO_NUMBER(v_max_daily_limit_single_payment,20,2) ;
   SELECT SUM("customeraction"."value")  

     INTO v_max_daily_limit_bulk_payment
     FROM "customeraction" 
    WHERE  "customeraction"."contractId" = v__contractId
             AND "customeraction"."coreCustomerId" = v__coreCustomerId
             AND "customeraction"."Customer_id" = v__userId
             AND ("customeraction"."Action_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_bulkPaymentsActions))))
             AND "customeraction"."LimitType_id" = 'AUTO_DENIED_DAILY_LIMIT'
             AND "customeraction"."Account_id" <> ' '
             AND "customeraction"."value" IS NOT NULL;
   v_max_daily_limit_bulk_paymentnum := TO_NUMBER(v_max_daily_limit_bulk_payment,20,2) ;
   SELECT SUM("customeraction"."value")  

     INTO v_max_weekly_limit_single_payment
     FROM "customeraction" 
    WHERE  "customeraction"."contractId" = v__contractId
             AND "customeraction"."coreCustomerId" = v__coreCustomerId
             AND "customeraction"."Customer_id" = v__userId
             AND ("customeraction"."Action_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_singlePaymentsActions))))
             AND "customeraction"."LimitType_id" = 'AUTO_DENIED_WEEKLY_LIMIT'
             AND "customeraction"."Account_id" <> ' '
             AND "customeraction"."value" IS NOT NULL;
   v_max_weekly_limit_single_paymentnum :=TO_NUMBER(v_max_weekly_limit_single_payment,20,2) ;
   SELECT SUM("customeraction"."value")  

     INTO v_max_weekly_limit_bulk_payment
     FROM "customeraction" 
    WHERE  "customeraction"."contractId" = v__contractId
             AND "customeraction"."coreCustomerId" = v__coreCustomerId
             AND "customeraction"."Customer_id" = v__userId
             AND ("customeraction"."Action_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_bulkPaymentsActions))))
             AND "customeraction"."LimitType_id" = 'AUTO_DENIED_WEEKLY_LIMIT'
             AND "customeraction"."Account_id" <> ' '
             AND "customeraction"."value" IS NOT NULL;
   v_max_weekly_limit_bulk_paymentnum :=TO_NUMBER(v_max_weekly_limit_bulk_payment,20,2) ;
   IF v_max_per_transaction_single_payment != ' ' THEN
    INSERT INTO "customerlimitgrouplimits"
     ( "customerlimitgrouplimits"."id", "customerlimitgrouplimits"."Customer_id", "customerlimitgrouplimits"."contractId", "customerlimitgrouplimits"."coreCustomerId", "customerlimitgrouplimits"."limitGroupId", "customerlimitgrouplimits"."LimitType_id", "customerlimitgrouplimits"."value" )
     VALUES ( SUBSTR(SYS_GUID(), 0, 50), v__userId, v__contractId, v__coreCustomerId, 'SINGLE_PAYMENT', 'MAX_TRANSACTION_LIMIT', v_max_per_transaction_single_paymentnum );
   END IF;
   IF v_max_per_transaction_bulk_payment != ' ' THEN
    INSERT INTO "customerlimitgrouplimits"
     ( "customerlimitgrouplimits"."id", "customerlimitgrouplimits"."Customer_id", "customerlimitgrouplimits"."contractId", "customerlimitgrouplimits"."coreCustomerId", "customerlimitgrouplimits"."limitGroupId", "customerlimitgrouplimits"."LimitType_id", "customerlimitgrouplimits"."value" )
     VALUES ( SUBSTR(SYS_GUID(), 0, 50), v__userId, v__contractId, v__coreCustomerId, 'BULK_PAYMENT', 'MAX_TRANSACTION_LIMIT', v_max_per_transaction_bulk_paymentnum );
   END IF;
   IF v_max_daily_limit_single_payment != ' ' THEN
    INSERT INTO "customerlimitgrouplimits"
     ( "customerlimitgrouplimits"."id", "customerlimitgrouplimits"."Customer_id", "customerlimitgrouplimits"."contractId", "customerlimitgrouplimits"."coreCustomerId", "customerlimitgrouplimits"."limitGroupId", "customerlimitgrouplimits"."LimitType_id", "customerlimitgrouplimits"."value" )
     VALUES ( SUBSTR(SYS_GUID(), 0, 50), v__userId, v__contractId, v__coreCustomerId, 'SINGLE_PAYMENT', 'DAILY_LIMIT', v_max_daily_limit_single_paymentnum );
   END IF;
   IF v_max_daily_limit_bulk_payment != ' ' THEN
    INSERT INTO "customerlimitgrouplimits"
     ( "customerlimitgrouplimits"."id", "customerlimitgrouplimits"."Customer_id", "customerlimitgrouplimits"."contractId", "customerlimitgrouplimits"."coreCustomerId", "customerlimitgrouplimits"."limitGroupId", "customerlimitgrouplimits"."LimitType_id", "customerlimitgrouplimits"."value" )
     VALUES ( SUBSTR(SYS_GUID(), 0, 50), v__userId, v__contractId, v__coreCustomerId, 'BULK_PAYMENT', 'DAILY_LIMIT', v_max_daily_limit_bulk_paymentnum );
   END IF;
   IF v_max_weekly_limit_single_payment != ' ' THEN
    INSERT INTO "customerlimitgrouplimits"
     ( "customerlimitgrouplimits"."id", "customerlimitgrouplimits"."Customer_id", "customerlimitgrouplimits"."contractId", "customerlimitgrouplimits"."coreCustomerId", "customerlimitgrouplimits"."limitGroupId", "customerlimitgrouplimits"."LimitType_id", "customerlimitgrouplimits"."value" )
     VALUES ( SUBSTR(SYS_GUID(), 0, 50), v__userId, v__contractId, v__coreCustomerId, 'SINGLE_PAYMENT', 'WEEKLY_LIMIT', v_max_weekly_limit_single_paymentnum );
   END IF;
   IF v_max_weekly_limit_bulk_payment != ' ' THEN
    INSERT INTO "customerlimitgrouplimits"
     ( "customerlimitgrouplimits"."id", "customerlimitgrouplimits"."Customer_id", "customerlimitgrouplimits"."contractId", "customerlimitgrouplimits"."coreCustomerId", "customerlimitgrouplimits"."limitGroupId", "customerlimitgrouplimits"."LimitType_id", "customerlimitgrouplimits"."value" )
     VALUES ( SUBSTR(SYS_GUID(), 0, 50), v__userId, v__contractId, v__coreCustomerId, 'BULK_PAYMENT', 'WEEKLY_LIMIT', v_max_weekly_limit_bulk_paymentnum );
   END IF;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure user_securityattributes_get_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "user_securityattributes_get_proc"
(
  "_userId" IN NVARCHAR2,
  "_legalEntityId" IN NVARCHAR2,"records" out SYS_REFCURSOR,
  "records1" out SYS_REFCURSOR
)
AS
   v_userAssociatedCoreCustomers long := u'';
   v_userAssociatedContracts long := u'';
   v_userAssociatedServiceDefinitions long := u'';
   v_userAssociatedGroups long := u'';
   v_actionsAtCoreCustomers long:= u'';
   v_actionsAtServiceDefinitions long := u'';
   v_actionsAtGroups long:= u'';
   v_actionsAtuser long:=u'';
   v_activeFeaturesAtFI long := u'';
   v_intersectedUserActions long := u'';
   v_intersectedUserFeatures long := u'';


BEGIN

  SELECT rtrim(xmlagg(XMLELEMENT(e,"coreCustomerId",',').EXTRACT('//text()')
).GetClobVal(),',') 

     INTO v_userAssociatedCoreCustomers
     FROM (select distinct("coreCustomerId") from "contractcustomers" 
    WHERE  "contractcustomers"."customerId" = "_userId" AND "contractcustomers"."companyLegalUnit" = "_legalEntityId");
  
  SELECT rtrim(xmlagg(XMLELEMENT(e,"contractId",',').EXTRACT('//text()')
).GetClobVal(),',')  

     INTO v_userAssociatedContracts
     FROM (select distinct("contractId") from "contractcustomers" 
    WHERE  "contractcustomers"."customerId" = "_userId" AND "contractcustomers"."companyLegalUnit" = "_legalEntityId");
  
  SELECT rtrim(xmlagg(XMLELEMENT(e,"servicedefinitionId",',').EXTRACT('//text()')
).GetClobVal(),',') 

     INTO v_userAssociatedServiceDefinitions
     FROM (select distinct("servicedefinitionId") from "contract" 
    WHERE  FIND_IN_SET("contract"."id", v_userAssociatedContracts) <> '0'
             AND "contract"."statusId" = 'SID_CONTRACT_ACTIVE' AND  "contract"."companyLegalUnit" = "_legalEntityId");
       
  SELECT rtrim(xmlagg(XMLELEMENT(e,"Group_id",',').EXTRACT('//text()')
).GetClobVal(),',') 

     INTO v_userAssociatedGroups
     FROM (select distinct("Group_id") from "customergroup" 
    WHERE  "customergroup"."Customer_id" = "_userId" AND  "customergroup"."companyLegalUnit" = "_legalEntityId");
  
    SELECT rtrim(xmlagg(XMLELEMENT(e,"actionId",',').EXTRACT('//text()')
).GetClobVal(),',') 
     INTO v_actionsAtCoreCustomers
     FROM (select distinct("actionId") from  "contractactionlimit"
    WHERE  FIND_IN_SET("contractactionlimit"."coreCustomerId", v_userAssociatedCoreCustomers) <> '0');
  
     SELECT rtrim(xmlagg(XMLELEMENT(e,"actionId",',').EXTRACT('//text()')
).GetClobVal(),',') 
     INTO v_actionsAtServiceDefinitions
     FROM (select distinct("actionId") from "servicedefinitionactionlimit"
    WHERE  FIND_IN_SET("servicedefinitionactionlimit"."serviceDefinitionId", v_userAssociatedServiceDefinitions) <> '0' AND "servicedefinitionactionlimit"."companyLegalUnit" = "_legalEntityId");
  
    SELECT rtrim(xmlagg(XMLELEMENT(e,"Action_id",',').EXTRACT('//text()')
).GetClobVal(),',') 
     INTO v_actionsAtGroups
     FROM (select distinct("Action_id") from "groupactionlimit" 
    WHERE  FIND_IN_SET("groupactionlimit"."Group_id", v_userAssociatedGroups) <> '0' and "groupactionlimit"."companyLegalUnit" = "_legalEntityId");

       SELECT rtrim(xmlagg(XMLELEMENT(e,"Action_id",',').EXTRACT('//text()')
).GetClobVal(),',')   
    INTO v_actionsAtuser FROM  (select distinct("Action_id") from "customeraction" WHERE  "Customer_id" = "_userId"
             AND  "isAllowed" = 1  AND "customeraction"."companyLegalUnit" = "_legalEntityId");
       
  SELECT rtrim(xmlagg(XMLELEMENT(e,"id",',').EXTRACT('//text()')
).GetClobVal(),',')

     INTO v_activeFeaturesAtFI
     FROM (select distinct("id") from "feature" 
    WHERE  "feature"."Status_id" = 'SID_FEATURE_ACTIVE' AND "feature"."companyLegalUnit" = "_legalEntityId");
  
     SELECT rtrim(xmlagg(XMLELEMENT(e,"id",',').EXTRACT('//text()')
).GetClobVal(),',')

     INTO v_intersectedUserActions
     FROM (select distinct("id") from "featureaction" 
    WHERE  "featureaction"."status" = 'SID_ACTION_ACTIVE'
       AND  "featureaction"."companyLegalUnit" = "_legalEntityId"
             AND FIND_IN_SET("featureaction"."id", v_actionsAtuser) <> 0
             AND FIND_IN_SET("featureaction"."id", v_actionsAtGroups) <> 0
             AND FIND_IN_SET("featureaction"."id", v_actionsAtServiceDefinitions) <> 0
             AND FIND_IN_SET("featureaction"."id", v_actionsAtCoreCustomers) <> 0
             AND FIND_IN_SET("featureaction"."Feature_id", v_activeFeaturesAtFI) <> 0);
   OPEN  "records" FOR
      SELECT v_intersectedUserActions "actions"  
        FROM DUAL  ;


 SELECT rtrim(xmlagg(XMLELEMENT(e,"Feature_id",',').EXTRACT('//text()')
).GetClobVal(),',')

     INTO v_intersectedUserFeatures
     FROM  (select distinct("Feature_id") from"featureaction" 
    WHERE  FIND_IN_SET("featureaction"."id", v_intersectedUserActions) <> '0');
   OPEN  "records1" FOR
      SELECT v_intersectedUserFeatures "features"  
        FROM DUAL  ;


END;
/
--  DDL for Procedure useraccounts_create_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "useraccounts_create_proc" 
(
  v__userId IN VARCHAR2,
  v__accountsCSV IN VARCHAR2,
  v__coreCustomerId IN VARCHAR2,
  v__contractId IN VARCHAR2
)
AS
   v_accountID VARCHAR2(255);
   v_finished NUMBER(10,0) := 0;
   v_id VARCHAR2(255);
   v_accounttypeid VARCHAR2(255);
   v_accounttypename VARCHAR2(255);
   CURSOR accountData
     IS SELECT "contractaccounts"."accountId" 
     FROM "contractaccounts" 
    WHERE  "contractaccounts"."contractId" = v__contractId
     AND "contractaccounts"."coreCustomerId" = v__coreCustomerId
     AND "contractaccounts"."accountId" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v__accountsCSV)));

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   OPEN accountData;
   FETCH accountData INTO v_accountID;
   <<loop_1>>
   WHILE ( 1 = 1 ) 
   LOOP 

      BEGIN
         IF accountData%FOUND <> FALSE THEN
          v_finished := 1 ;
         END IF;
         IF v_finished = 1 THEN
          EXIT;
         ELSE

         BEGIN
            SELECT SUBSTR(SYS_GUID(), 0, 50) 

              INTO v_id
              FROM DUAL ;
            SELECT "contractaccounts"."typeId" 

              INTO v_accounttypeid
              FROM "contractaccounts" 
             WHERE  "contractaccounts"."accountId" = v_accountID;    --to confirm v__accountID
            SELECT "accounttype"."TypeDescription" 

              INTO v_accounttypename
              FROM "accounttype" 
             WHERE  "accounttype"."TypeID" = v_accounttypeid;
            INSERT INTO "customeraccounts"
              ( "id", "Customer_id", "Account_id", "contractId", "coreCustomerId", "accountType" )
              VALUES ( v_id, v__userId, v_accountID, v__contractId, v__coreCustomerId, v_accounttypename );

         END;
         END IF;
         FETCH accountData INTO v_accountID;
         GOTO loop_1;

      END;
   END LOOP;
   CLOSE accountData;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure useractions_create_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "useractions_create_proc" 
(
  v__userId IN VARCHAR2,
  iv__accountsCSV IN VARCHAR2,
  v__coreCustomerId IN VARCHAR2,
  v__contractId IN VARCHAR2,
  iv__groupId IN VARCHAR2
)
AS
   v__accountsCSV CLOB := iv__accountsCSV;
   v__groupId VARCHAR2(50) := iv__groupId;
   v_finished NUMBER(10,0) := 0;
   v_featureActionId VARCHAR2(255) := '';
   v_actionslist VARCHAR2(4000) := '';
   v_limitId VARCHAR2(255) := '';
   v_entryStatus NUMBER(10,0) := 0;
   v_accountId VARCHAR2(255) := '';
   v_actualLimitId VARCHAR2(255) := '';
   v_serviceDefinitionId VARCHAR2(255) := '';
   v_serviceType VARCHAR2(255) := '';
   v_validFIActions CLOB := '';
   v_validServiceDefinitionActions CLOB := '';
   v_validGroupActions CLOB := '';
   v_validActionsList CLOB := '';
   v_groupId VARCHAR2(255) := '';
   v_featureId VARCHAR2(255) := '';
   v_limitvalue VARCHAR2(255) := '';
   v_id VARCHAR2(255) := '';
   CURSOR actions
     IS SELECT "id" 
     FROM "featureaction" 
    WHERE  INSTR(v_validActionsList, "id") <> 0
     AND ( "featureaction"."isAccountLevel" = '1' );
   CURSOR nonaccountlevelactions
     IS SELECT "id" 
     FROM "featureaction" 
    WHERE  INSTR(v_validActionsList, "id") <> 0
     AND ( "featureaction"."isAccountLevel" = '0' );
   CURSOR accounts
     IS SELECT "customeraccounts"."Account_id" 
     FROM "customeraccounts" 
    WHERE  "customeraccounts"."contractId" = v__contractId
     AND "customeraccounts"."coreCustomerId" = v__coreCustomerId
     AND "customeraccounts"."Customer_id" = v__userId
     AND INSTR(v__accountsCSV, "Account_id") <> 0;

BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   IF ( CASE 
             WHEN v__accountsCSV IS NULL THEN 1
   ELSE 0
      END <> 0
     OR v__accountsCSV = ' ' ) THEN
    SELECT LISTAGG(CAST("customeraccounts"."Account_id" AS varchar2(255)),',')  

     INTO v__accountsCSV
     FROM "customeraccounts" 
    WHERE  "customeraccounts"."Customer_id" = v__userId
             AND "customeraccounts"."contractId" = v__contractId
             AND "customeraccounts"."coreCustomerId" = v__coreCustomerId;
   END IF;
   SELECT "contract"."servicedefinitionId" 

     INTO v_serviceDefinitionId
     FROM "contract" 
    WHERE  "contract"."id" = v__contractId;
   SELECT "servicedefinition"."serviceType" 

     INTO v_serviceType
     FROM "servicedefinition" 
    WHERE  "servicedefinition"."id" = v_serviceDefinitionId;
   IF ( CASE 
             WHEN v__groupId IS NULL THEN 1
   ELSE 0
      END <> 0
     OR v__groupId = ' ' ) THEN
    SELECT "groupservicedefinition"."Group_id" 

     INTO v_groupId
     FROM "groupservicedefinition" 
    WHERE  "groupservicedefinition"."serviceDefinitionId" = v_serviceDefinitionId
             AND "groupservicedefinition"."isDefaultGroup" = '1';
   END IF;
   SELECT LISTAGG(CAST("featureaction"."id" AS VARCHAR2(255)),',')  

     INTO v_validFIActions
     FROM "featureaction" ;
   SELECT LISTAGG(CAST("servicedefinitionactionlimit"."actionId" AS VARCHAR2(255)),',') 

     INTO v_validServiceDefinitionActions
     FROM "servicedefinitionactionlimit" 
    WHERE  "servicedefinitionactionlimit"."serviceDefinitionId" = v_serviceDefinitionId
             AND "servicedefinitionactionlimit"."actionId" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v_validFIActions)));
   SELECT "groupservicedefinition"."Group_id" 

     INTO v__groupId
     FROM "groupservicedefinition" 
    WHERE  "groupservicedefinition"."serviceDefinitionId" = v_serviceDefinitionId
             AND "groupservicedefinition"."Group_id" = v__groupId;
   SELECT LISTAGG(CAST("groupactionlimit"."Action_id" AS VARCHAR2(255)),',') 

     INTO v_validGroupActions
     FROM "groupactionlimit" 
    WHERE  "groupactionlimit"."Group_id" = v__groupId
             AND "groupactionlimit"."Action_id" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v_validServiceDefinitionActions)));
   SELECT LISTAGG(CAST("contractactionlimit"."actionId" AS VARCHAR2(255)),',') 

     INTO v_validActionsList
     FROM "contractactionlimit" 
    WHERE  "contractactionlimit"."contractId" = v__contractId
             AND "contractactionlimit"."actionId" IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT(v_validGroupActions)));
   OPEN accounts;
   FETCH accounts INTO v_accountId;
   <<loop_3>>
   WHILE ( accounts%FOUND = TRUE ) 
   LOOP 

      BEGIN
         OPEN actions;
         FETCH actions INTO v_featureActionId;
         <<loop_2>>
         WHILE ( actions%FOUND = TRUE ) 
         LOOP 
            DECLARE
               CURSOR limits
                 IS SELECT "LimitType_id" 
                 FROM "actionlimit" 
                WHERE  "Action_id" = v_featureActionId;

            BEGIN
               SELECT "featureaction"."Feature_id" 

                 INTO v_featureId
                 FROM "featureaction" 
                WHERE  "featureaction"."id" = v_featureActionId;
               v_entryStatus := 0 ;
               OPEN limits;
               FETCH limits INTO v_limitId;
               <<loop_1>>
               WHILE ( limits%FOUND = TRUE ) 
               LOOP 

                  BEGIN
                     SELECT "contractactionlimit"."value" 

                       INTO v_limitvalue
                       FROM "contractactionlimit" 
                      WHERE  "contractactionlimit"."actionId" = v_featureActionId
                               AND "contractactionlimit"."limitTypeId" = v_limitId
                               AND "contractactionlimit"."contractId" = v__contractId
                               AND "contractactionlimit"."coreCustomerId" = v__coreCustomerId;
                     IF ( v_limitId = 'MAX_TRANSACTION_LIMIT' ) THEN
                      v_actualLimitId := 'AUTO_DENIED_TRANSACTION_LIMIT' ;
                     ELSE
                        IF ( v_limitId = 'MIN_TRANSACTION_LIMIT' ) THEN
                         v_actualLimitId := 'PRE_APPROVED_TRANSACTION_LIMIT' ;
                        ELSE
                           IF ( v_limitId = 'DAILY_LIMIT' ) THEN

                           BEGIN
                              v_actualLimitId := 'PRE_APPROVED_DAILY_LIMIT' ;
                              SELECT SUBSTR(SYS_GUID(), 0, 50) 

                                INTO v_id
                                FROM DUAL ;
                              INSERT INTO "customeraction"
                                ( "customeraction"."id", "customeraction"."RoleType_id", "customeraction"."Customer_id", "customeraction"."contractId", "customeraction"."coreCustomerId", "customeraction"."featureId", "customeraction"."Action_id", "customeraction"."Account_id", "customeraction"."isAllowed", "customeraction"."LimitType_id", "customeraction"."value" )
                                VALUES ( v_id, v_serviceType, v__userId, v__contractId, v__coreCustomerId, v_featureId, v_featureActionId, v_accountId, 1, v_actualLimitId, 0.00 );
                              v_actualLimitId := 'AUTO_DENIED_DAILY_LIMIT' ;

                           END;
                           ELSE

                           BEGIN
                              IF ( v_limitId = 'WEEKLY_LIMIT' ) THEN

                              BEGIN
                                 v_actualLimitId := 'PRE_APPROVED_WEEKLY_LIMIT' ;
                                 SELECT SUBSTR(SYS_GUID(), 0, 50) 

                                   INTO v_id
                                   FROM DUAL ;
                                 INSERT INTO "customeraction"
                                   ( "customeraction"."id", "customeraction"."RoleType_id", "customeraction"."Customer_id", "customeraction"."contractId", "customeraction"."coreCustomerId", "customeraction"."featureId", "customeraction"."Action_id", "customeraction"."Account_id", "customeraction"."isAllowed", "customeraction"."LimitType_id", "customeraction"."value" )
                                   VALUES ( v_id, v_serviceType, v__userId, v__contractId, v__coreCustomerId, v_featureId, v_featureActionId, v_accountId, 1, v_actualLimitId, 0.00 );
                                 v_actualLimitId := 'AUTO_DENIED_WEEKLY_LIMIT' ;

                              END;
                              END IF;

                           END;
                           END IF;
                        END IF;
                     END IF;
                     SELECT SUBSTR(SYS_GUID(), 0, 50) 

                       INTO v_id
                       FROM DUAL ;
                     INSERT INTO "customeraction"
                       ( "customeraction"."id", "customeraction"."RoleType_id", "customeraction"."Customer_id", "customeraction"."contractId", "customeraction"."coreCustomerId", "customeraction"."featureId", "customeraction"."Action_id", "customeraction"."Account_id", "customeraction"."isAllowed", "customeraction"."LimitType_id", "customeraction"."value" )
                       VALUES ( v_id, v_serviceType, v__userId, v__contractId, v__coreCustomerId, v_featureId, v_featureActionId, v_accountId, 1, v_actualLimitId, v_limitvalue );
                     SELECT SUBSTR(SYS_GUID(), 0, 50) 

                       INTO v_id
                       FROM DUAL ;
                     INSERT INTO "customeraction"
                       ( "customeraction"."id", "customeraction"."RoleType_id", "customeraction"."Customer_id", "customeraction"."contractId", "customeraction"."coreCustomerId", "customeraction"."featureId", "customeraction"."Action_id", "customeraction"."Account_id", "customeraction"."isAllowed", "customeraction"."LimitType_id", "customeraction"."value" )
                       VALUES ( v_id, v_serviceType, v__userId, v__contractId, v__coreCustomerId, v_featureId, v_featureActionId, v_accountId, 1, v_limitId, v_limitvalue );
                     v_entryStatus := 1 ;
                     FETCH limits INTO v_limitId;
                     GOTO loop_1;

                  END;
               END LOOP;
               CLOSE limits;
               IF v_entryStatus = 0 THEN

               BEGIN
                  SELECT SUBSTR(SYS_GUID(), 0, 50) 

                    INTO v_id
                    FROM DUAL ;
                  INSERT INTO "customeraction"
                    ( "customeraction"."id", "customeraction"."RoleType_id", "customeraction"."Customer_id", "customeraction"."contractId", "customeraction"."coreCustomerId", "customeraction"."featureId", "customeraction"."Action_id", "customeraction"."Account_id", "customeraction"."isAllowed" )
                    VALUES ( v_id, v_serviceType, v__userId, v__contractId, v__coreCustomerId, v_featureId, v_featureActionId, v_accountId, 1 );

               END;
               END IF;
               v_actionslist := v_featureActionId || ',' || v_actionslist ;
               FETCH actions INTO v_featureActionId;
               GOTO loop_2;

            END;
         END LOOP;
         CLOSE actions;
         FETCH accounts INTO v_accountId;
         GOTO loop_3;

      END;
   END LOOP;
   CLOSE accounts;
   OPEN nonaccountlevelactions;
   FETCH nonaccountlevelactions INTO v_featureActionId;
   <<loop_1>>
   WHILE ( nonaccountlevelactions%FOUND = TRUE ) 
   LOOP 

      BEGIN
         SELECT "featureaction"."Feature_id" 

           INTO v_featureId
           FROM "featureaction" 
          WHERE  "featureaction"."id" = v_featureActionId;
         SELECT SUBSTR(SYS_GUID(), 0, 50) 

           INTO v_id
           FROM DUAL ;
         INSERT INTO "customeraction"
           ( "customeraction"."id", "customeraction"."RoleType_id", "customeraction"."Customer_id", "customeraction"."contractId", "customeraction"."coreCustomerId", "customeraction"."featureId", "customeraction"."Action_id", "customeraction"."isAllowed" )
           VALUES ( v_id, v_serviceType, v__userId, v__contractId, v__coreCustomerId, v_featureId, v_featureActionId, 1 );
         FETCH nonaccountlevelactions INTO v_featureActionId;
         GOTO loop_1;

      END;
   END LOOP;
   CLOSE nonaccountlevelactions;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure userdelinking_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "userdelinking_proc" 
(
  "_newUser" IN VARCHAR2,
  "_combinedUser" IN VARCHAR2
)
AS
   v_groupId VARCHAR2(4000);

BEGIN

   --SQL Server BEGIN TRANSACTION;
--   utils.incrementTrancount;
   SELECT "Group_id" 

     INTO v_groupId
     FROM "customergroup" 
            JOIN "membergroup"    ON ( "customergroup"."Group_id" = "membergroup"."id"
            AND "membergroup"."Type_id" = 'TYPE_ID_RETAIL' )
    WHERE  "Customer_id" = "_combinedUser";
   DELETE "customergroup"

    WHERE  "Customer_id" = "_combinedUser"
             AND "Group_id" = v_groupId;
   INSERT INTO "customergroup"
     ( "Customer_id", "Group_id" )
     VALUES ( "_newUser", v_groupId );
   UPDATE "payee"
      SET "User_Id" = "_newUser"
    WHERE  "User_Id" = "_combinedUser"
     AND "organizationId" IS NULL;
   UPDATE "externalaccount"
      SET "User_id" = "_newUser"
    WHERE  "User_id" = "_combinedUser"
     AND "organizationId" IS NULL;
   UPDATE "billpaypayee"
      SET "createdBy" = "_newUser"
    WHERE  "createdBy" = "_combinedUser"
     AND "isBusinessPayee" = '0'
     AND "legalEntityId" IS NULL;
   UPDATE "wiretransferspayee"
      SET "createdBy" = "_newUser"
    WHERE  "createdBy" = "_combinedUser"
     AND "isBusinessPayee" = '0'
     AND "legalEntityId" IS NULL;
   UPDATE "interbankpayee"
      SET "createdBy" = "_newUser"
    WHERE  "createdBy" = "_combinedUser"
     AND "isBusinessPayee" = '0'
     AND "legalEntityId" IS NULL;
   UPDATE "intrabankpayee"
      SET "createdBy" = "_newUser"
    WHERE  "createdBy" = "_combinedUser"
     AND "isBusinessPayee" = '0'
     AND "legalEntityId" IS NULL;
   UPDATE "internationalpayee"
      SET "createdBy" = "_newUser"
    WHERE  "createdBy" = "_combinedUser"
     AND "isBusinessPayee" = '0'
     AND "legalEntityId" IS NULL;
 --  utils.commit_transaction;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure userlinking_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "userlinking_proc" 
(
  "_combinedUser" IN VARCHAR2,
  "_otherUser" IN VARCHAR2,
  "_isCombinedUserBusiness" IN VARCHAR2
)
AS
   v_success_flag NUMBER(10,0);
   v_deviceIds CLOB;

BEGIN

   --SQL Server BEGIN TRANSACTION;
--   utils.incrementTrancount;
   UPDATE "customerrequest"
      SET "Customer_id" = "_combinedUser"
    WHERE  "Customer_id" = "_otherUser";
   UPDATE "cardaccountrequest"
      SET "Customer_id" = "_combinedUser"
    WHERE  "Customer_id" = "_otherUser";
   UPDATE "notificationcardinfo"
      SET "Customer_id" = "_combinedUser"
    WHERE  "Customer_id" = "_otherUser";
   SELECT LISTAGG(CAST("id" AS varchar2(255)),',') 

     INTO v_deviceIds
     FROM "customerdevice"  
    WHERE  "Customer_id" = "_combinedUser";
   UPDATE "customerdevice" 
      SET "Customer_id" = "_combinedUser"
    WHERE  "Customer_id" = "_otherUser"
     AND "id" NOT IN (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT( v_deviceIds)));
   UPDATE "payee"
      SET "User_Id" = "_combinedUser"
    WHERE  "User_Id" = "_otherUser";
   UPDATE "externalaccount"
      SET "User_id" = "_combinedUser"
    WHERE  "User_id" = "_otherUser";
   UPDATE "billpaypayee"
      SET "createdBy" = "_combinedUser"
    WHERE  "createdBy" = "_otherUser";
   UPDATE "wiretransferspayee"
      SET "createdBy" = "_combinedUser"
    WHERE  "createdBy" = "_otherUser";
   UPDATE "interbankpayee"
      SET "createdBy" = "_combinedUser"
    WHERE  "createdBy" = "_otherUser";
   UPDATE "intrabankpayee"
      SET "createdBy" = "_combinedUser"
    WHERE  "createdBy" = "_otherUser";
   UPDATE "internationalpayee"
      SET "createdBy" = "_combinedUser"
    WHERE  "createdBy" = "_otherUser";
   IF ( "_isCombinedUserBusiness" = 'false' ) THEN
    UPDATE "customeraction"
      SET "Customer_id" = "_combinedUser"
    WHERE  "Customer_id" = "_otherUser";
   END IF;
--   utils.commit_transaction;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure verify_user_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "verify_user_proc" 
(
  "_phone" IN VARCHAR2,
  "_email" IN VARCHAR2,
  "_dateOfBirth" IN VARCHAR2,
  "_legalEntityId" IN VARCHAR2,
  "_backendIdentifiers" IN VARCHAR2,
  "_backendType" IN VARCHAR2,
  "records" OUT SYS_REFCURSOR
)
AS
   v_usersList CLOB := '';


BEGIN

--   /*TODO:SQLDEV*/ SET  XACT_ABORT  ON /*END:SQLDEV*/
   SELECT LISTAGG(CAST("backendidentifier"."Customer_id" AS varchar2(255)),',') 

     INTO v_usersList
     FROM "backendidentifier" 
    WHERE  "backendidentifier"."BackendType" = "_backendType"
             AND "backendidentifier"."BackendId" IN( SELECT column_value from TABLE(UTILS.STRING_SPLIT("_backendIdentifiers")));
   OPEN  "records" FOR
      SELECT "customer"."id" id  ,
             "customer"."FirstName" FirstName  ,
             "customer"."MiddleName" MiddleName  ,
             "customer"."LastName" LastName  ,
             "customer"."UserName" UserName  ,
             "customer"."Gender" Gender  ,
             "customer"."DateOfBirth" DateOfBirth  ,
             "customer"."companyLegalUnit" companyLegalUnit  ,
             "customer"."Ssn" Ssn  ,
             "customer"."Status_id" Status_id  ,
             "customer"."CustomerType_id" CustomerType_id  
        FROM ( "customer" 
               LEFT JOIN "customercommunication" primaryphone   ON ( ( primaryphone."Customer_id" ="customer"."id" )
               AND ( primaryphone."Value" = "_phone" )
               AND ( primaryphone."Type_id" = 'COMM_TYPE_PHONE' ) )
               LEFT JOIN "customercommunication" primaryemail   ON ( ( primaryemail."Customer_id" = "customer"."id" )
               AND ( primaryemail."Value" = "_email" )
               AND ( primaryemail."Type_id" = 'COMM_TYPE_EMAIL' ) )
                ) 
       WHERE  "customer"."DateOfBirth" = "_dateOfBirth"
                AND "customer"."companyLegalUnit" = "_legalEntityId"
                AND primaryphone."Customer_id" = primaryemail."Customer_id"
      UNION 
      SELECT "customer"."id" id  ,
             "customer"."FirstName" FirstName  ,
             "customer"."MiddleName" MiddleName  ,
             "customer"."LastName" LastName  ,
             "customer"."UserName" UserName  ,
             "customer"."Gender" Gender  ,
             "customer"."DateOfBirth" DateOfBirth  ,
             "customer"."companyLegalUnit" companyLegalUnit  ,
             "customer"."Ssn" Ssn  ,
             "customer"."Status_id" Status_id  ,
             "customer"."CustomerType_id" CustomerType_id  
        FROM "customer" 
       WHERE  "customer"."id" IN ( SELECT column_value from TABLE(UTILS.STRING_SPLIT( v_usersList)))
                AND "customer"."companyLegalUnit" = "_legalEntityId" ;
      --DBMS_SQL.RETURN_RESULT("customer");

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/
--  DDL for Procedure weekend_proc



CREATE OR REPLACE NONEDITIONABLE PROCEDURE "weekend_proc" 
AS
   v_startDate VARCHAR2(200);
   v_count NUMBER(10,0);

BEGIN

   v_startDate := SYSDATE ;
   v_count := 0 ;
   WHILE v_count < 100 
   LOOP 

      BEGIN
         IF utils.datepart('WEEKDAY', v_startDate) = 6
           OR utils.datepart('WEEKDAY', v_startDate) = 5 THEN

         BEGIN
            INSERT INTO "holidays"
              ( "holidayDate", "createdBy", "modifiedby" )
              VALUES ( v_startDate, 'priya', 'priya' );
            v_count := v_count + 1 ;

         END;
         END IF;
         v_startDate := TO_DATE( v_startDate +1) ;

      END;
   END LOOP;

EXCEPTION WHEN OTHERS THEN utils.handleerror(SQLCODE,SQLERRM);
END;
/


create or replace NONEDITIONABLE PROCEDURE         "contracts_with_activeaccounts"(
 "_contractIdList" IN VARCHAR2,
 "records" OUT SYS_REFCURSOR
 )
 AS
 FINISHED NUMBER(10,0);
 statusPoint VARCHAR2(255);
 contracts VARCHAR2(255);
v_contractIds VARCHAR2(4000);
CURSOR statuses IS (select "accountId" from "contractaccounts" where "contractId" in (SELECT COLUMN_VALUE FROM TABLE(UTILS.STRING_SPLIT("_contractIdList"))));
 BEGIN
 FINISHED := 0;

OPEN statuses;
<<getStatus>> LOOP
FETCH statuses INTO statusPoint;
IF statuses%NOTFOUND THEN
        FINISHED := 1;
END IF; 

IF FINISHED = 1 then
  EXIT getStatus;
ELSE
  select "contractId" into contracts from "contractaccounts" where "accountId" = statusPoint  and "statusDesc" != 'closed';
  if contracts is null then
     FINISHED := 0;
  else 
      v_contractIds := v_contractIds || CASE WHEN LENGTH(v_contractIds)>0 THEN  ',' ELSE  '' END || contracts;

  END IF;
    END IF;
    END LOOP;
CLOSE statuses;
 OPEN  "records" FOR
      SELECT v_contractIds  "contractIds"
        FROM DUAL  ;
-- SQLINES LICENSE FOR EVALUATION USE ONLY
END;
/


create or replace NONEDITIONABLE PROCEDURE "fetch_restrictive_featureactionlimits_legalEntityId_proc"(
     "_locale" IN VARCHAR2,
   "_userId" IN VARCHAR2,
     "_serviceDefinitionId" IN VARCHAR2,
     "_roleId" IN VARCHAR2,
     "_coreCustomerId" IN VARCHAR2,
     "_accessPolicyIdList" IN VARCHAR2,
     "_legalEntityId" IN VARCHAR2,
     "records" OUT SYS_REFCURSOR
     )
     AS
     select_statement CLOB;
     action_select_statement CLOB;
     
BEGIN
--select_statement := '';
action_select_statement := '';
--action_select_statement_output := '';
--FeatureActionList  := '';
--
--FeatureActionList := '';
--select_statement := '';
--action_select_statement := '';
select_statement := 'SELECT "featureaction"."id" FROM "featureaction" WHERE "featureaction"."status" = ''SID_ACTION_ACTIVE''';
IF ("_accessPolicyIdList" IS NOT NULL)  THEN
select_statement := select_statement  || ' AND "featureaction"."accesspolicyId" IN (select DISTINCT COLUMN_VALUE from TABLE (UTILS.STRING_SPLIT(''' || "_accessPolicyIdList"|| ''')))' ;
END IF;
--
IF("_serviceDefinitionId" IS NOT NULL) THEN
select_statement := select_statement  || ' AND EXISTS
(
SELECT ''X'' FROM "servicedefinitionactionlimit"
WHERE "servicedefinitionactionlimit"."serviceDefinitionId" = '''  || "_serviceDefinitionId"  || ''' AND
"servicedefinitionactionlimit"."companyLegalUnit"  ='''  ||"_legalEntityId" ||''' AND
"featureaction"."id" = "servicedefinitionactionlimit"."actionId"
)';
END IF;
IF("_roleId" IS NOT NULL) THEN
select_statement := select_statement  || ' AND EXISTS
(
SELECT ''X'' FROM "groupactionlimit"
WHERE "groupactionlimit"."Group_id" = '''  || "_roleId"  || ''' AND
"groupactionlimit"."companyLegalUnit" = ''' ||"_legalEntityId" || '''  AND
"featureaction"."id" = "groupactionlimit"."Action_id"
) ';
END IF;

IF("_coreCustomerId" IS NOT NULL) THEN
select_statement := select_statement  || ' AND EXISTS
(
SELECT ''X'' FROM "contractactionlimit"
WHERE "contractactionlimit"."coreCustomerId" = '''  || "_coreCustomerId"  || ''' AND
"contractactionlimit"."companyLegalUnit" = ''' ||"_legalEntityId" ||''' AND
"featureaction"."id" = "contractactionlimit"."actionId"
) ';
END IF;

IF("_userId" IS NOT NULL) THEN
select_statement := select_statement  || ' AND EXISTS
(
SELECT ''X'' FROM "customeraction"
WHERE "customeraction"."isAllowed" = 1 AND
"customeraction"."companyLegalUnit" = ''' ||"_legalEntityId" ||''' AND
"featureaction"."id" = "customeraction"."Action_id" AND
"customeraction"."Customer_id" = '''  || "_userId"  || ''')';
END IF;

--/* SQLINES DEMO *** d != '') THEN
--SET select_statement = CONCAT(@select_statement , ' AND (customeraction.coreCustomerId = ''' , _coreCustomerId , ''')');
--END IF;
--*/
--
action_select_statement := select_statement;



select_statement := '(SELECT
"feature"."id" AS "featureId",
"featuredisplaynamedescription"."displayName" AS "featureName",
"featuredisplaynamedescription"."displayDescription" AS "featureDescription",
"feature"."Status_id" AS "featureStatus",
"featureaction"."status" AS "actionStatus",
"featureaction"."id" AS "actionId",
"actiondisplaynamedescription"."displayName" AS "actionName",
"actiondisplaynamedescription"."displayDescription" AS "actionDescription",
"featureaction"."isAccountLevel" AS "isAccountLevel",
"featureaction"."Type_id" AS "typeId",
"featureaction"."limitgroupId" as "limitGroupId",
"featureaction"."accesspolicyId" as "accessPolicyId",
"featureaction"."companyLegalUnit" as "legalEntityId",
"featureaction"."actionlevelId" as "actionLevelId",
DECODE("featureaction"."Type_id" , ''NON_MONETARY'',null,"actionlimit"."LimitType_id") as "limitTypeId",
DECODE("featureaction"."Type_id" , ''NON_MONETARY'',null,"actionlimit"."value") as "fiLimitValue"';
 
IF("_serviceDefinitionId" IS NOT NULL) THEN
select_statement := CONCAT(select_statement , ', DECODE("featureaction"."Type_id" , ''NON_MONETARY'',null,"servicedefinitionactionlimit"."value") AS "serviceLimitValue"');
END IF;
IF("_roleId" IS NOT NULL) THEN
select_statement := CONCAT(select_statement , ', DECODE("featureaction"."Type_id" , ''NON_MONETARY'',null,"groupactionlimit"."value") AS "groupLimitValue"');
END IF;
IF("_coreCustomerId" IS NOT NULL) THEN
select_statement := CONCAT(select_statement , ', DECODE("featureaction"."Type_id" ,''NON_MONETARY'',null,"contractactionlimit"."value") AS "coreCustomerLimitValue"');
END IF;

IF "_roleId" IS NOT NULL AND "_coreCustomerId" IS NOT NULL THEN
select_statement := CONCAT(select_statement , ', CASE WHEN ("groupactionlimit"."isNewAction" = ''1'' OR "contractactionlimit"."isNewAction" = ''1'') THEN ''1'' ELSE ''0'' END  AS "isNewAction" ');
ELSE
IF "_roleId" IS NOT NULL THEN
select_statement := CONCAT(select_statement , ', "groupactionlimit"."isNewAction" AS "isNewAction"');
ELSIF "_coreCustomerId" IS NOT NULL THEN
select_statement := CONCAT(select_statement , ', "contractactionlimit"."isNewAction" AS "isNewAction"');
END IF;
END IF;


select_statement := select_statement  || ' FROM "feature"
LEFT JOIN "featuredisplaynamedescription" ON ("featuredisplaynamedescription"."companyLegalUnit" = "feature"."companyLegalUnit" AND "featuredisplaynamedescription"."Feature_id" = "feature"."id" AND "featuredisplaynamedescription"."Locale_id" = ' || '''' || "_locale" || '''' || ')
LEFT JOIN "featureaction" ON ("featureaction"."companyLegalUnit" = "feature"."companyLegalUnit" AND "featureaction"."Feature_id" = "feature"."id")
LEFT JOIN "actiondisplaynamedescription" ON ("actiondisplaynamedescription"."companyLegalUnit" = "featureaction"."companyLegalUnit" AND "actiondisplaynamedescription"."Action_id" = "featureaction"."id" AND "actiondisplaynamedescription"."Locale_id" = ' || '''' || "_locale" || ''''  || ')';

IF("_serviceDefinitionId" IS NOT NULL) THEN
select_statement := select_statement  || ' LEFT JOIN "servicedefinitionactionlimit" ON ("servicedefinitionactionlimit"."companyLegalUnit" = "featureaction"."companyLegalUnit" AND "servicedefinitionactionlimit"."actionId" = "featureaction"."id" AND "servicedefinitionactionlimit"."serviceDefinitionId" = '  ||'''' || "_serviceDefinitionId"  || '''' || ')';
END IF;
IF("_roleId" IS NOT NULL) THEN
select_statement := select_statement  || ' LEFT JOIN "groupactionlimit" ON ("groupactionlimit"."companyLegalUnit" = "featureaction"."companyLegalUnit" AND "groupactionlimit"."Action_id" = "featureaction"."id" AND "groupactionlimit"."Group_id" = '  || '''' || "_roleId" || '''' ||')';
END IF;
IF("_coreCustomerId" IS NOT NULL) THEN
select_statement := select_statement  || ' LEFT JOIN "contractactionlimit" ON ("contractactionlimit"."companyLegalUnit" = "featureaction"."companyLegalUnit" AND "contractactionlimit"."actionId" = "featureaction"."id" AND "contractactionlimit"."coreCustomerId" = ' || '''' || "_coreCustomerId" || '''' || ')';
END IF;
--
select_statement := CONCAT(select_statement , ' LEFT JOIN "actionlimit" ON ("actionlimit"."companyLegalUnit" = "featureaction"."companyLegalUnit" AND "actionlimit"."Action_id" = "featureaction"."id")');
--
--
select_statement := select_statement  || ' WHERE "featureaction"."companyLegalUnit" = '''  || "_legalEntityId"  ||'''' ||' AND "featureaction"."id" IN (' || action_select_statement  ||')';
--
 IF("_serviceDefinitionId" IS NOT NULL) THEN
 select_statement := select_statement  || ' AND (("featureaction"."Type_id" = ''MONETARY'' AND "servicedefinitionactionlimit"."limitTypeId" = "actionlimit"."LimitType_id" AND "servicedefinitionactionlimit"."serviceDefinitionId" = '  ||'''' || "_serviceDefinitionId"  || ''') OR ("featureaction"."Type_id" = ''NON_MONETARY''))';
 END IF;
IF("_roleId" IS NOT NULL) THEN
  select_statement := select_statement  || ' AND (("featureaction"."Type_id" = ''MONETARY'' AND "groupactionlimit"."LimitType_id" = "actionlimit"."LimitType_id" AND "groupactionlimit"."Group_id" = '  || '''' || "_roleId" || ''') OR ("featureaction"."Type_id" = ''NON_MONETARY''))';
END IF;
IF("_coreCustomerId" IS NOT NULL) THEN
select_statement := select_statement  || ' AND (("featureaction"."Type_id" = ''MONETARY'' AND "contractactionlimit"."limitTypeId" = "actionlimit"."LimitType_id" AND "contractactionlimit"."coreCustomerId" = '  || '''' || "_coreCustomerId" ||''') OR ("featureaction"."Type_id" = ''NON_MONETARY''))';
END IF;


select_statement := CONCAT(select_statement , ')');

-- EXECUTE IMMEDIATE  select_statement; DEALLOCATE PREPARE stmt;
--

dbms_output.put_line(select_statement);
OPEN "records" FOR select_statement;
END;
/


create or replace NONEDITIONABLE PROCEDURE "customeraction_save_proc"
(
  "_queryInput" IN long
)
AS
   iv_queryInput long := "_queryInput";
   v_recordRow long;
   v_recordsData long;
   v_query long;
   CURSOR actions

      IS SELECT(regexp_substr(iv_queryInput, '[^|]+',1,level)) from dual
    connect by regexp_substr(iv_queryInput, '[^|]+', 1, level) is not null;


BEGIN


   iv_queryInput := REPLACE(iv_queryInput, '\', ' ') ;
   iv_queryInput := REPLACE(iv_queryInput, '"', '''') ;
   OPEN actions;
   FETCH actions INTO v_recordRow;
   <<loop_1>>

   WHILE (actions%FOUND)
   LOOP 

      BEGIN
         v_recordsData := '''' || CAST(SYS_GUID() AS NVARCHAR2) || ''',' || v_recordRow ;
         v_query := ('INSERT INTO "customeraction"("id","RoleType_id","Customer_id","coreCustomerId","contractId","featureId","Action_id","Account_id","isAllowed","limitGroupId","LimitType_id","value","companyLegalUnit") VALUES (') || (v_recordsData) || (''')') ;
         EXECUTE IMMEDIATE v_query;
         FETCH actions INTO v_recordRow;
         GOTO loop_1;

      END;
   END LOOP;

END;
/