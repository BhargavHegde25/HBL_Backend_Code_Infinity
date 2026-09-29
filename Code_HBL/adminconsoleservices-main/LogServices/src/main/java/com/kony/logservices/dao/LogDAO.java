package com.kony.logservices.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.sql2o.Connection;
import org.sql2o.Query;

import com.kony.adminconsole.commons.utils.DateUtils;
import com.kony.logservices.core.BaseActivity;
import com.kony.logservices.dto.AdminActivityDTO;
import com.kony.logservices.dto.AdminCustomerActivityDTO;
import com.kony.logservices.dto.AuditLogsAndMoneyMovementLogsDTO;
import com.kony.logservices.dto.CustomerActivityDTO;
import com.kony.logservices.dto.PaginationDTO;
import com.kony.logservices.dto.SearchCustomerAuditLogsDTO;
import com.kony.logservices.dto.TransactionActivityDTO;
import com.kony.logservices.dto.TransactionValueVolumeTypeDTO;
import com.kony.logservices.handler.LogDataSourceHandler;
import com.kony.logservices.util.QueryFormer;
import com.kony.logservices.util.SQLQueriesEnum;

/**
 * DAO handles execution of sql queries to log database
 * 
 * @author Venkateswara Rao Alla
 *
 */
public class LogDAO {
	private static String dbType=QueryFormer.getDBType();
	private static String dbName=QueryFormer.dbName;
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

    public static <T extends BaseActivity> void saveLog(T log) {

        try (Connection con = LogDataSourceHandler.getLogSql2oInstance().beginTransaction()) {
            con.createQuery(SQLQueriesEnum.getActivityInsertQuery(log.getClass())).bind(log).executeUpdate();
            con.commit();
        }
    }

    public static List<CustomerActivityDTO> getLastNCustomerSessions(String username, Integer sessionCount) {

        try (Connection con = LogDataSourceHandler.getLogSql2oInstance().open()) {
            return con.createQuery(SQLQueriesEnum.valueOf(dbType+"_AUDITLOG_LAST_N_SESSIONS").getQuery().replace("?1", dbName))
                    .addParameter("username", username).addParameter("limit", sessionCount)
                    .executeAndFetch(CustomerActivityDTO.class);
        }
    }

    public static List<CustomerActivityDTO> getAllActivitiesForASession(String sessionId) {

        try (Connection con = LogDataSourceHandler.getLogSql2oInstance().open()) {
            return con
                    .createQuery(
                            SQLQueriesEnum.valueOf(dbType+"_AUDITLOG_READ").getQuery().replace("?1", dbName))
                    .addParameter("sessionId", sessionId).executeAndFetch(CustomerActivityDTO.class);
        }
    }

    public static PaginationDTO<TransactionActivityDTO> getPaginatedTransactions(String searchText, String searchName,
            String startDate, String endDate, Integer startAmount, Integer endAmount, int pageNumber, int noOfRecords,
            String sortBy, String sortDirection, ArrayList<String> fromAccountTypeArray,
            ArrayList<String> toAccountTypeArray, ArrayList<String> statusTypeArray, ArrayList<String> typeArray,
            ArrayList<String> currencyTypeArray, String fromMobileEmail, String toMobileEmail) {
        try (Connection con = LogDataSourceHandler.getLogSql2oInstance().open()) {
            StringBuilder paginationQuery = new StringBuilder();
            Map<String, Object> parameters = new HashMap<>();
            String whereClauses = getTransactionLogsQuery(searchText, searchName, startDate, endDate, startAmount,
                    endAmount, sortBy, sortDirection, fromAccountTypeArray, toAccountTypeArray, statusTypeArray,
                    typeArray, fromMobileEmail, toMobileEmail, currencyTypeArray, parameters);

            // Query to get Count based on filters
            Query txCountQuery = con.createQuery(SQLQueriesEnum.valueOf(dbType+"_TRANSACTIONLOG_COUNT_READ").getQuery().replace("?1", dbName) + whereClauses);
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                txCountQuery.addParameter(entry.getKey(), entry.getValue());
            }
            Integer count = txCountQuery.executeScalar(Integer.class);

            // Query to fetch List of Logs
            if (pageNumber > 0 && noOfRecords > 0) {
                int offsetValue = getOffsetValue(pageNumber, noOfRecords);
                paginationQuery.append(SQLQueriesEnum.valueOf(dbType+"_LIMIT").getQuery());
                parameters.put("noOfRecords", noOfRecords);
                parameters.put("offsetValue", offsetValue);
            }
            Query txQuery = con
                    .createQuery(SQLQueriesEnum.valueOf(dbType+"_TRANSACTIONLOG_READ").getQuery().replace("?1", dbName) + whereClauses + paginationQuery);

            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                txQuery.addParameter(entry.getKey(), entry.getValue());
            }
            List<TransactionActivityDTO> transactionLogs = txQuery.executeAndFetch(TransactionActivityDTO.class);
            PaginationDTO<TransactionActivityDTO> result = new PaginationDTO<>();
            result.setPage(pageNumber);
            result.setPageSize(noOfRecords);
            result.setCount(count);
            result.setLogs(transactionLogs);
            return result;
        }
    }

    public static String getTransactionLogsQuery(String searchText, String serviceName, String startDate,
            String endDate, Integer startAmount, Integer endAmount, String sortBy, String sortDirection,
            ArrayList<String> fromAccountTypeArray, ArrayList<String> toAccountTypeArray,
            ArrayList<String> statusTypeArray, ArrayList<String> typeArray, String fromMobileEmail,
            String toMobileEmail, ArrayList<String> currencyTypeArray, Map<String, Object> parameters) {
        StringBuilder query = new StringBuilder();

        if (StringUtils.isNotBlank(serviceName)) {
            query.append(SQLQueriesEnum.valueOf(dbType+"_TRANSACTIONLOG_WHERECLAUSE_SERVICENAME").getQuery());
            parameters.put("serviceName", serviceName);

        }

        if (StringUtils.isNotBlank(searchText)) {
            query.append(SQLQueriesEnum.valueOf(dbType+"_TRANSACTIONLOG_WHERECLAUSE_TRANSACTIONID").getQuery());
            parameters.put("searchText", searchText + "%");
        }

        if (startDate != null && endDate != null) {
            Date startOfDate = null;
            Date endOfDate = null;
            try {
                startOfDate = DateUtils.parseToStartOfDay(startDate, DateUtils.PATTERN_MM_DD_YYYY);
                endOfDate = DateUtils.parseToEndOfDay(endDate, DateUtils.PATTERN_MM_DD_YYYY);
            } catch (ParseException e) {
                alert.prepareError("Failure in parsing date strings", e).log();

            }
            if (startOfDate != null && endOfDate != null) {
                query.append(SQLQueriesEnum.valueOf(dbType+"_TRANSACTIONLOG_WHERECLAUSE_TRANSACTIONDATEBETWEEN").getQuery());
                try {
					  parameters.put("startDate", DateUtils.convertLocalDateTimeToServerZone(startOfDate));
					  parameters.put("endDate",DateUtils.convertLocalDateTimeToServerZone(endOfDate));
				} catch (ParseException e) {
					alert.prepareError("Failure in parsing datetime strings", e).log();
					}
            }
        }

        if (startAmount != null && endAmount != null) {
            query.append(SQLQueriesEnum.valueOf(dbType+"_TRANSACTIONLOG_WHERECLAUSE_AMOUNT").getQuery());
            parameters.put("startAmount", startAmount);
            parameters.put("endAmount", endAmount);
        }

        if (fromAccountTypeArray != null && fromAccountTypeArray.size() > 0) {
            query.append(SQLQueriesEnum.valueOf(dbType+"_TRANSACTIONLOG_WHERECLAUSE_FROMACCOUNTTYPE").getQuery() + " (")
                    .append(generateInClauseParameters("fromAccountType", fromAccountTypeArray, parameters))
                    .append(") AND");
        }

        if (toAccountTypeArray != null && toAccountTypeArray.size() > 0) {
            query.append(SQLQueriesEnum.valueOf(dbType+"_TRANSACTIONLOG_WHERECLAUSE_TOACCOUNTTYPE").getQuery() + " (")
                    .append(generateInClauseParameters("toAccountType", toAccountTypeArray, parameters))
                    .append(") AND");
        }

        if (statusTypeArray != null && statusTypeArray.size() > 0) {
            query.append(SQLQueriesEnum.valueOf(dbType+"_TRANSACTIONLOG_WHERECLAUSE_STATUS").getQuery() + " (")
            .append(generateInClauseParameters("status", statusTypeArray, parameters))
                    .append(") AND");
        }

        if (typeArray != null && typeArray.size() > 0) {
            query.append(SQLQueriesEnum.valueOf(dbType+"_TRANSACTIONLOG_WHERECLAUSE_TYPE").getQuery() + " (")
            .append(generateInClauseParameters("type", typeArray, parameters))
                    .append(") AND");
        }

        if (currencyTypeArray != null && currencyTypeArray.size() > 0) {
            query.append(SQLQueriesEnum.valueOf(dbType+"_TRANSACTIONLOG_WHERECLAUSE_CURRENCYCODE").getQuery() + " (")
                    .append(generateInClauseParameters("currencyCode", currencyTypeArray, parameters)).append(") AND");
        }

        if (fromMobileEmail != null) {
            if (fromMobileEmail.equalsIgnoreCase("mobile")) {
                query.append(SQLQueriesEnum.valueOf(dbType+"_TRANSACTIONLOG_WHERECLAUSE_FROMMOBILEOREMAILLIKE").getQuery());
            } else {
                query.append(SQLQueriesEnum.valueOf(dbType+"_TRANSACTIONLOG_WHERECLAUSE_FROMMOBILEOREMAILNOTLIKE").getQuery());
            }
        }

        if (toMobileEmail != null) {
            if (toMobileEmail.equalsIgnoreCase("mobile")) {
                query.append(SQLQueriesEnum.valueOf(dbType+"_TRANSACTIONLOG_WHERECLAUSE_TOMOBILEOREMAILLIKE").getQuery());
            } else {
                query.append(SQLQueriesEnum.valueOf(dbType+"_TRANSACTIONLOG_WHERECLAUSE_TOMOBILEOREMAILNOTLIKE").getQuery());
            }
        }

        if (query.length() > 0) {
            query.insert(0, " WHERE");
            query.delete(query.lastIndexOf("AND"), query.length());
        }
        if (sortBy != null && sortDirection != null) {
            query.append(" ORDER BY " + sortBy + " " + sortDirection);
        } else {
            query.append(SQLQueriesEnum.valueOf(dbType+"_TRANSACTIONLOG_ORDERBY").getQuery());
        }

        return query.toString();
    }

    public static PaginationDTO<AdminCustomerActivityDTO> fetchAdminCustomerActivityLogs(String userName,
            String moduleName, String activityType, String startDate, String endDate, String searchText,
            ArrayList<String> statusTypeArray, ArrayList<String> channelTypeArray, ArrayList<String> osTypeArray,
            ArrayList<String> roleTypeArray, int pageNumber, int noOfRecords, String sortDirection) {
        try (Connection con = LogDataSourceHandler.getLogSql2oInstance().open()) {
            StringBuilder paginationQuery = new StringBuilder();

            Map<String, Object> parameters = new HashMap<>();
            String whereClauses = buildCustomerActivityLogQuery(false, userName, moduleName, activityType, startDate,
                    endDate, searchText, statusTypeArray, channelTypeArray, osTypeArray, roleTypeArray, pageNumber,
                    noOfRecords, sortDirection, parameters);
            // Query to get Count based on filters
            Query txCountQuery = con
                    .createQuery(SQLQueriesEnum.valueOf(dbType+"_ADMINCUSTOMERACTIVITY_READ_COUNT").getQuery().replace("?1", dbName) + whereClauses);
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                txCountQuery.addParameter(entry.getKey(), entry.getValue());
            }
            Integer count = txCountQuery.executeScalar(Integer.class);

            // Query to fetch List of Logs
            if (pageNumber > 0 && noOfRecords > 0) {
                int offsetValue = getOffsetValue(pageNumber, noOfRecords);
                paginationQuery.append(SQLQueriesEnum.valueOf(dbType+"_LIMIT").getQuery());
                parameters.put("noOfRecords", noOfRecords);
                parameters.put("offsetValue", offsetValue);
            }
            Query txQuery = con
                    .createQuery(SQLQueriesEnum.valueOf(dbType+"_ADMINCUSTOMERACTIVITY_READ").getQuery().replace("?1", dbName) + whereClauses + paginationQuery);

            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                txQuery.addParameter(entry.getKey(), entry.getValue());
            }
            List<AdminCustomerActivityDTO> transactionLogs = txQuery.executeAndFetch(AdminCustomerActivityDTO.class);
            PaginationDTO<AdminCustomerActivityDTO> result = new PaginationDTO<>();
            result.setPage(pageNumber);
            result.setPageSize(noOfRecords);
            result.setCount(count);
            result.setLogs(transactionLogs);
            return result;
        }
    }

    public static PaginationDTO<AdminActivityDTO> getPaginatedAdminConsoleLogs(String searchText, String moduleName,
            String startDate, String endDate, String sortBy, String sortDirection, int pageNumber, int noOfRecords,
            ArrayList<String> eventTypeArray, ArrayList<String> userRoleTypeArray, ArrayList<String> statusTypeArray) {
        try (Connection con = LogDataSourceHandler.getLogSql2oInstance().open()) {
            Map<String, Object> parameters = new HashMap<>();
            StringBuilder whereClause = new StringBuilder();
            if (StringUtils.isNotBlank(searchText)) {
                whereClause.append(SQLQueriesEnum.valueOf(dbType+"_ADMINACTIVITY_WHERECLAUSE_USERNAME").getQuery());
                parameters.put("searchText", searchText + "%");
            }
            if (StringUtils.isNotBlank(moduleName)) {
                whereClause.append(SQLQueriesEnum.valueOf(dbType+"_ADMINACTIVITY_WHERECLAUSE_MODULENAME").getQuery());
                parameters.put("moduleName", moduleName + "%");
            }
            if (startDate != null && endDate != null) {
                Date startOfDate = null;
                Date endOfDate = null;
                try {
                    startOfDate = DateUtils.parseToStartOfDay(startDate, DateUtils.PATTERN_MM_DD_YYYY);
                    endOfDate = DateUtils.parseToEndOfDay(endDate, DateUtils.PATTERN_MM_DD_YYYY);
                } catch (ParseException e) {
                    alert.prepareError("Failure in parsing date strings", e).log();

                }
                if (startOfDate != null && endOfDate != null) {
                    whereClause.append(SQLQueriesEnum.valueOf(dbType+"_ADMINACTIVITY_WHERECLAUSE_EVENTTS").getQuery());
                    try {
  					  parameters.put("startDate", DateUtils.convertLocalDateTimeToServerZone(startOfDate));
  					  parameters.put("endDate",DateUtils.convertLocalDateTimeToServerZone(endOfDate));
  				} catch (ParseException e) {
  					alert.prepareError("Failure in parsing datetime strings", e).log();
  					} 
                }
            }
            if (eventTypeArray != null && eventTypeArray.size() > 0) {
                whereClause.append(SQLQueriesEnum.valueOf(dbType+"_ADMINACTIVITY_WHERECLAUSE_EVENT").getQuery() + " (")
                        .append(generateInClauseParameters("event", eventTypeArray, parameters)).append(") AND");
            }
            if (userRoleTypeArray != null && userRoleTypeArray.size() > 0) {
                whereClause.append(SQLQueriesEnum.valueOf(dbType+"_ADMINACTIVITY_WHERECLAUSE_USERROLE").getQuery() + " (")
                        .append(generateInClauseParameters("userRole", userRoleTypeArray, parameters)).append(") AND");
            }
            if (statusTypeArray != null && statusTypeArray.size() > 0) {
                whereClause.append(SQLQueriesEnum.valueOf(dbType+"_ADMINACTIVITY_WHERECLAUSE_STATUS").getQuery() + " (")
                        .append(generateInClauseParameters("status", statusTypeArray, parameters)).append(") AND");
            }
            if (whereClause.length() > 0) {
                whereClause.insert(0, " WHERE");
                whereClause.delete(whereClause.lastIndexOf("AND"), whereClause.length());
            }
            
            
            // Query to get Count based on filters
            Query acCountQuery = con.createQuery(SQLQueriesEnum.valueOf(dbType+"_ADMINACTIVITY_COUNT_READ").getQuery().replace("?1", dbName) + whereClause);
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                acCountQuery.addParameter(entry.getKey(), entry.getValue());
            }
            Integer count = acCountQuery.executeScalar(Integer.class);
            
            if(StringUtils.isBlank(sortBy)) {
            	sortBy = SQLQueriesEnum.valueOf(dbType+"_QUERY_EVENTTS").getQuery();
            }
            
            if(StringUtils.isBlank(sortDirection)) {
            	sortDirection = "DESC";
            }
            
            whereClause.append(" ORDER BY "+sortBy+" "+sortDirection+" ");

            // Query to fetch List of Logs
            if (pageNumber > 0 && noOfRecords > 0) {
                int offsetValue = getOffsetValue(pageNumber, noOfRecords);
                whereClause.append(SQLQueriesEnum.valueOf(dbType+"_LIMIT").getQuery());
                parameters.put("noOfRecords", noOfRecords);
                parameters.put("offsetValue", offsetValue);
            }
            
            String query=SQLQueriesEnum.valueOf(dbType+"_ADMINACTIVITY_READ").getQuery().replace("?1", dbName);
            Query acQuery = con.createQuery(query + whereClause);

            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                acQuery.addParameter(entry.getKey(), entry.getValue());
            }
            List<AdminActivityDTO> adminConsoleLogs = acQuery.throwOnMappingFailure(false).executeAndFetch(AdminActivityDTO.class);
            PaginationDTO<AdminActivityDTO> result = new PaginationDTO<>();
            result.setPage(pageNumber);
            result.setPageSize(noOfRecords);
            result.setCount(count);
            result.setLogs(adminConsoleLogs);
            return result;
        }

    }

    public static PaginationDTO<CustomerActivityDTO> fetchCustomerActivityLogs(String userName, String moduleName,
            String activityType, String startDate, String endDate, String searchText, ArrayList<String> statusTypeArray,
            ArrayList<String> channelTypeArray, ArrayList<String> osTypeArray, ArrayList<String> roleTypeArray,
            int pageNumber, int noOfRecords, String sortDirection) {
        try (Connection con = LogDataSourceHandler.getLogSql2oInstance().open()) {
            StringBuilder paginationQuery = new StringBuilder();

            Map<String, Object> parameters = new HashMap<>();
            String whereClauses = buildCustomerActivityLogQuery(true, userName, moduleName, activityType, startDate,
                    endDate, searchText, statusTypeArray, channelTypeArray, osTypeArray, roleTypeArray, pageNumber,
                    noOfRecords, sortDirection, parameters);
            // Query to get Count based on filters
            Query txCountQuery = con.createQuery(SQLQueriesEnum.valueOf(dbType+"_CUSTOMERACTIVITY_READ_COUNT").getQuery().replace("?1", dbName) + whereClauses);
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                txCountQuery.addParameter(entry.getKey(), entry.getValue());
            }
            Integer count = txCountQuery.executeScalar(Integer.class);

            // Query to fetch List of Logs
            if (pageNumber > 0 && noOfRecords > 0) {
                int offsetValue = getOffsetValue(pageNumber, noOfRecords);
                paginationQuery.append(SQLQueriesEnum.valueOf(dbType+"_LIMIT").getQuery());
                parameters.put("noOfRecords", noOfRecords);
                parameters.put("offsetValue", offsetValue);
            }
            Query txQuery = con
                    .createQuery(SQLQueriesEnum.valueOf(dbType+"_CUSTOMERACTIVITY_READ").getQuery().replace("?1", dbName) + whereClauses + paginationQuery);

            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                txQuery.addParameter(entry.getKey(), entry.getValue());
            }
            List<CustomerActivityDTO> transactionLogs = txQuery.executeAndFetch(CustomerActivityDTO.class);
            PaginationDTO<CustomerActivityDTO> result = new PaginationDTO<>();
            result.setPage(pageNumber);
            result.setPageSize(noOfRecords);
            result.setCount(count);
            result.setLogs(transactionLogs);
            return result;
        }
    }

    public static String buildCustomerActivityLogQuery(boolean isMemberActivity, String userName, String moduleName,
            String activityType, String startDate, String endDate, String searchText, ArrayList<String> statusTypeArray,
            ArrayList<String> channelTypeArray, ArrayList<String> osTypeArray, ArrayList<String> roleTypeArray,
            int pageNumber, int noOfRecords, String sortDirection, Map<String, Object> parameters) {

        StringBuilder query = new StringBuilder();
        if (isMemberActivity) {
            query.append(SQLQueriesEnum.valueOf(dbType+"_CUSTOMERACTIVITY_QUERY_USERNAME").getQuery());
            parameters.put("username", userName);
            if (StringUtils.isNotBlank(moduleName)) {
                query.append(SQLQueriesEnum.valueOf(dbType+"_CUSTOMERACTIVITY_QUERY_MODULENAME").getQuery() + " = :moduleName AND ");
                parameters.put("moduleName", moduleName);
            }
            if (StringUtils.isNotBlank(searchText)) {
                query.append(SQLQueriesEnum.valueOf(dbType+"_CUSTOMERACTIVITY_QUERY_MODULENAME").getQuery() + " Like :searchText AND ");
                parameters.put("searchText", searchText + "%");
            }
            if (statusTypeArray != null && statusTypeArray.size() > 0) {
                query.append(SQLQueriesEnum.valueOf(dbType+"_CUSTOMERACTIVITY_QUERY_STATUS").getQuery()+" (").append(generateInClauseParameters("status", statusTypeArray, parameters))
                        .append(") AND");
            }
            if (channelTypeArray != null && channelTypeArray.size() > 0) {
                query.append(SQLQueriesEnum.valueOf(dbType+"_CUSTOMERACTIVITY_QUERY_CHANNEL").getQuery()+" (")
                        .append(generateInClauseParameters("channel", channelTypeArray, parameters)).append(") AND");
            }
            if (osTypeArray != null && osTypeArray.size() > 0) {
                query.append(SQLQueriesEnum.valueOf(dbType+"_CUSTOMERACTIVITY_QUERY_OPERATINGSYSTEM").getQuery()+" (")
                        .append(generateInClauseParameters("operatingSystem", osTypeArray, parameters)).append(") AND");
            }
        } else {
            query.append(SQLQueriesEnum.valueOf(dbType+"_ADMINCUSTOMERACTIVITY_QUERY_CUSTOMERID").getQuery());
            parameters.put("userid", userName);
            if (StringUtils.isNotBlank(searchText)) {
                query.append(SQLQueriesEnum.valueOf(dbType+"_ADMINCUSTOMERACTIVITY_QUERY_ADMINNAME").getQuery());
                parameters.put("searchText", searchText + "%");
            }
            if (roleTypeArray != null && roleTypeArray.size() > 0) {
                query.append(SQLQueriesEnum.valueOf(dbType+"_ADMINCUSTOMERACTIVITY_QUERY_ADMINROLE").getQuery()+" (")
                        .append(generateInClauseParameters("adminRole", roleTypeArray, parameters)).append(") AND");
            }
        }

        if (StringUtils.isNotBlank(activityType)) {
            query.append(SQLQueriesEnum.valueOf(dbType+"_ADMINCUSTOMERACTIVITY_QUERY_ACTIVITYTYPE").getQuery());
            parameters.put("activityType", activityType);
        }
        if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
            Date startOfDate = null;
            Date endOfDate = null;
            try {
                startOfDate = DateUtils.parseToStartOfDay(startDate, DateUtils.PATTERN_MM_DD_YYYY);
                endOfDate = DateUtils.parseToEndOfDay(endDate, DateUtils.PATTERN_MM_DD_YYYY);
            } catch (ParseException e) {
                alert.prepareError("Failure in parsing date strings", e).log();
            }

            if (startOfDate != null && endOfDate != null) {
                query.append(" "+ SQLQueriesEnum.valueOf(dbType+"_QUERY_EVENTTS").getQuery() +" BETWEEN :startDate AND :endDate AND");
                try {
					  parameters.put("startDate", DateUtils.convertLocalDateTimeToServerZone(startOfDate));
					  parameters.put("endDate",DateUtils.convertLocalDateTimeToServerZone(endOfDate));
				} catch (ParseException e) {
					alert.prepareError("Failure in parsing datetime strings", e).log();
					}
            }
        }
        if (query.length() > 0) {
            query.insert(0, " WHERE ");
            query.delete(query.lastIndexOf("AND"), query.length());
            query.append(" ORDER BY " + SQLQueriesEnum.valueOf(dbType+"_QUERY_EVENTTS").getQuery() + " ");
            if (sortDirection != null) {
                query.append(sortDirection);
            }
        }
        return query.toString();
    }

    public static List<TransactionValueVolumeTypeDTO> getTransactionValueLogsQuery(Date startDate, Date endDate) {
        List<TransactionValueVolumeTypeDTO> transactionValueVolumeTypes = new ArrayList<>();

        try (Connection con = LogDataSourceHandler.getLogSql2oInstance().open()) {

            StringBuilder queryStr = new StringBuilder(SQLQueriesEnum.valueOf(dbType+"_TRANSACTIONVALUEVOLUMETYPE_READ").getQuery().replace("?1", dbName));
            if (startDate != null && endDate != null) {
                queryStr.append(SQLQueriesEnum.valueOf(dbType+"_TRANSACTIONLOG_WHERECLAUSE_TRANSACTIONDATE").getQuery());
                
            }
            queryStr.append(SQLQueriesEnum.valueOf(dbType+"_TRANSACTIONLOG_GROUPBY").getQuery());

            Query query = con.createQuery(queryStr.toString());

            if (startDate != null && endDate != null) {
            	try {
					query.addParameter("startDate", DateUtils.convertLocalDateTimeToServerZone(startDate)).
					addParameter("endDate", DateUtils.convertLocalDateTimeToServerZone(endDate));
				} catch (ParseException e) {
					 alert.prepareError("Failure in parsing date strings", e).log();
				} 
            }

            transactionValueVolumeTypes = query.executeAndFetch(TransactionValueVolumeTypeDTO.class);
        }
        return transactionValueVolumeTypes;
    }

    private static int getOffsetValue(int pageNumber, int noOfRecords) {
        return ((pageNumber - 1) * noOfRecords);
    }

    public static String generateInClauseParameters(String basename, ArrayList<String> array,
            Map<String, Object> parameters) {
        StringBuilder pattern = new StringBuilder();
        String placeHolder = null;
        for (int index = 0; index < array.size(); index++) {
            placeHolder = basename + index;
            pattern.append(":" + placeHolder + ",");
            parameters.put(placeHolder, array.get(index));
        }
        pattern.deleteCharAt((pattern.length() - 1));
        return pattern.toString();
    }

    public static PaginationDTO<AuditLogsAndMoneyMovementLogsDTO> searchCustomerAuditLogs(
            SearchCustomerAuditLogsDTO customerAuditLogsDTO) {

        try (Connection con = LogDataSourceHandler.getLogSql2oInstance().open()) {
            Query txQuery = con.createQuery(constructSearchQuery(customerAuditLogsDTO));

            List<AuditLogsAndMoneyMovementLogsDTO> auditLogs = txQuery
                    .executeAndFetch(AuditLogsAndMoneyMovementLogsDTO.class);

            PaginationDTO<AuditLogsAndMoneyMovementLogsDTO> result = new PaginationDTO<>();
            result.setPageSize(customerAuditLogsDTO.getPageSize());
            result.setPageOffset(customerAuditLogsDTO.getPageOffset());
            result.setPage(customerAuditLogsDTO.getPageOffset() / customerAuditLogsDTO.getPageSize());
            result.setSortVariable(customerAuditLogsDTO.getSortVariable());
            result.setSortDirection(customerAuditLogsDTO.getSortDirection());

            Boolean hasNextPage = (auditLogs.size() == customerAuditLogsDTO.getPageSize() + 1);
            result.setHasNextPage(hasNextPage);
            if (hasNextPage) {
                auditLogs.remove(auditLogs.size() - 1);
            }

            result.setLogs(auditLogs);
            return result;

        }
    }

    private static String constructSearchQuery(SearchCustomerAuditLogsDTO customerAuditLogsDTO) {

        StringBuilder searchQuery = new StringBuilder(SQLQueriesEnum.valueOf(dbType+"_AUDITLOG_SEARCH_SELECT_QUERY").getQuery().replace("?1", dbName));
        StringBuilder whereClause = new StringBuilder();

        if (StringUtils.isNotBlank(customerAuditLogsDTO.getUsername())) {
            whereClause.append(SQLQueriesEnum.valueOf(dbType+"_AUDITLOG_SEARCH_WHERECLAUSE_USERNAME").getQuery()+" = '" + customerAuditLogsDTO.getUsername() + "'");
        }

        if (StringUtils.isNotBlank(customerAuditLogsDTO.getCustomerid())) {
            if (whereClause.length() > 0)
                whereClause.append(" and ");
            whereClause.append(SQLQueriesEnum.valueOf(dbType+"_AUDITLOG_SEARCH_WHERECLAUSE_CUSTOMERID").getQuery()+" = '" + customerAuditLogsDTO.getCustomerid() + "'");
        }

        if (StringUtils.isNotBlank(customerAuditLogsDTO.getSearchText())) {
            if (whereClause.length() > 0)
                whereClause.append(" and ");
            whereClause.append(" (" +SQLQueriesEnum.valueOf(dbType+"_AUDITLOG_SEARCH_WHERECLAUSE_USERNAME").getQuery()+ " like '" + customerAuditLogsDTO.getSearchText()
                    + "%' or "+ SQLQueriesEnum.valueOf(dbType+"_AUDITLOG_SEARCH_WHERECLAUSE_FROMACCOUNTNUMBER").getQuery() + " like '" + customerAuditLogsDTO.getSearchText() 
                    + "%' or "+ SQLQueriesEnum.valueOf(dbType+"_AUDITLOG_SEARCH_WHERECLAUSE_TOACCOUNTNUMBER").getQuery() + " like '" + customerAuditLogsDTO.getSearchText() + "%')");
        }

        if (customerAuditLogsDTO.isCSRAssistFlagSet()) {
            if (customerAuditLogsDTO.getIsCSRAssist()) {
                if (whereClause.length() > 0)
                    whereClause.append(" and ");
                whereClause.append(SQLQueriesEnum.valueOf(dbType+"_AUDITLOG_SEARCH_WHERECLAUSE_ISCSRASSIST").getQuery()+ " = '1' ");
            } else {
                if (whereClause.length() > 0)
                    whereClause.append(" and ");
                whereClause.append(SQLQueriesEnum.valueOf(dbType+"_AUDITLOG_SEARCH_WHERECLAUSE_ISCSRASSIST").getQuery()+" = '0' ");
            }
        }
        if (StringUtils.isNotBlank(customerAuditLogsDTO.getModule())) {
        	String module = customerAuditLogsDTO.getModule();
        	String moduleFormatted= "'" + StringUtils.join((module.split(",")),"','") + "'";
            if (whereClause.length() > 0)
                whereClause.append(" and ");
            whereClause.append(SQLQueriesEnum.valueOf(dbType+"_AUDITLOG_SEARCH_WHERECLAUSE_EVENTTYPE").getQuery() + " IN (" + moduleFormatted + ")");
        }

        if (StringUtils.isNotBlank(customerAuditLogsDTO.getActivityType())) {
            if (whereClause.length() > 0)
                whereClause.append(" and ");
            whereClause.append(SQLQueriesEnum.valueOf(dbType+"_AUDITLOG_SEARCH_WHERECLAUSE_EVENTSUBTYPE").getQuery() +" = '" + customerAuditLogsDTO.getActivityType() + "'");
        }

        if (StringUtils.isNotBlank(customerAuditLogsDTO.getStartAmount())) {
            if (whereClause.length() > 0)
                whereClause.append(" and ");
            whereClause.append(SQLQueriesEnum.valueOf(dbType+"_AUDITLOG_SEARCH_WHERECLAUSE_AMOUNT").getQuery() +" >= '" + customerAuditLogsDTO.getStartAmount() + "'");
        }

        if (StringUtils.isNotBlank(customerAuditLogsDTO.getEndAmount())) {
            if (whereClause.length() > 0)
                whereClause.append(" and ");
            whereClause.append(SQLQueriesEnum.valueOf(dbType+"_AUDITLOG_SEARCH_WHERECLAUSE_AMOUNT").getQuery() +" <= '" + customerAuditLogsDTO.getEndAmount() + "'");
        }

        if (StringUtils.isNotBlank(customerAuditLogsDTO.getStartDate())) {
            if (whereClause.length() > 0)
                whereClause.append(" and ");
            whereClause.append(" "+SQLQueriesEnum.valueOf(dbType+"_DATE_CAST").getQuery().replace("?1", SQLQueriesEnum.valueOf(dbType+"_AUDITLOG_SEARCH_WHERECLAUSE_CREATEDTS").getQuery())+" >= '" + customerAuditLogsDTO.getStartDate() + "'");
        }

        if (StringUtils.isNotBlank(customerAuditLogsDTO.getEndDate())) {
            if (whereClause.length() > 0)
                whereClause.append(" and ");
            whereClause.append(" "+SQLQueriesEnum.valueOf(dbType+"_DATE_CAST").getQuery().replace("?1", SQLQueriesEnum.valueOf(dbType+"_AUDITLOG_SEARCH_WHERECLAUSE_CREATEDTS").getQuery())+" <= '" + customerAuditLogsDTO.getEndDate() + "'");
        }
  
        if (whereClause.length() > 0) {
            searchQuery.append(" WHERE ").append(whereClause);
        }

        searchQuery.append(" ORDER BY ").append(customerAuditLogsDTO.getSortVariable()).append(" ")
                .append(customerAuditLogsDTO.getSortDirection());
        searchQuery.append(SQLQueriesEnum.valueOf(dbType+"_LIMIT").getQuery().replace(":offsetValue",String.valueOf(customerAuditLogsDTO.getPageOffset())).replace(":noOfRecords",String.valueOf(customerAuditLogsDTO.getPageSize() + 1)));
        return searchQuery.toString();
    }
}