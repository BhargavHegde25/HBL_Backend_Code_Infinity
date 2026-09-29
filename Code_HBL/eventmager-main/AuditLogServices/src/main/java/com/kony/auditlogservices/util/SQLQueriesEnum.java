package com.kony.auditlogservices.util;

import java.util.HashMap;
import java.util.Map;
import com.kony.auditlogservices.core.BaseActivity;
import com.kony.auditlogservices.dtoclasses.AuditActivityDTO;
import com.kony.auditlogservices.dtoclasses.MoneyMovementLogDTO;

/**
 * Enum used to maintain sql queries with associated constants
 * 
 * @author SridharReddy
 * 
 */

public enum SQLQueriesEnum {

	// Audit logs
	ALERTLOG_READ, ALERTLOG_INSERT, ALERTLOG_DELETE, AUDITACTIVITY_READ, AUDITLOG_INSERT, AUDITACTIVITY_DELETE,
	MONEYMOVEMENTLOG_READ, MONEYMOVEMENTLOG_INSERT, MONEYMOVEMENTLOG_DELETE, AUDITACTIVITYSQL_READ,DELETEQUERY_WHERECLAUSE;

	
	private SQLQueriesEnum() {
	}

	/**
	 * Returns query associated with this enum constant
	 * 
	 * @param key
	 * @return
	 */
	public String getQuery() {
		return ArchivalQueries.querymap.get(this.name());
	}

	static final Map<Class<? extends BaseActivity>, SQLQueriesEnum> LOG_INSERT_QUERIES_MAPPER = mapLogTypeAndInsertQueries();

	static final Map<Class<? extends BaseActivity>, SQLQueriesEnum> LOG_DELETE_QUERIES_MAPPER = mapLogTypeAndDeleteQueries();

	static final Map<Class<? extends BaseActivity>, SQLQueriesEnum> LOG_READ_QUERIES_MAPPER = mapLogTypeAndReadQueries();

	/**
	 * Mapper maps sub class of type {@link BaseActivity} to corresponding insert
	 * query constant from {@link SQLQueriesEnum}
	 * 
	 * @return
	 */
	public static Map<Class<? extends BaseActivity>, SQLQueriesEnum> mapLogTypeAndInsertQueries() {
		Map<Class<? extends BaseActivity>, SQLQueriesEnum> map = new HashMap<>();

		map.put(AuditActivityDTO.class, SQLQueriesEnum.AUDITLOG_INSERT);
		map.put(MoneyMovementLogDTO.class, SQLQueriesEnum.MONEYMOVEMENTLOG_INSERT);

		return map;
	}

	/**
	 * Mapper maps sub class of type {@link BaseActivity} to corresponding delete
	 * query constant from {@link SQLQueriesEnum}
	 * 
	 * @return
	 */
	public static Map<Class<? extends BaseActivity>, SQLQueriesEnum> mapLogTypeAndDeleteQueries() {
		Map<Class<? extends BaseActivity>, SQLQueriesEnum> map = new HashMap<>();

		map.put(AuditActivityDTO.class, SQLQueriesEnum.AUDITACTIVITY_DELETE);
		map.put(MoneyMovementLogDTO.class, SQLQueriesEnum.MONEYMOVEMENTLOG_DELETE);

		return map;
	}

	/**
	 * Mapper maps sub class of type {@link BaseActivity} to corresponding read
	 * query constant from {@link SQLQueriesEnum}
	 * 
	 * @return
	 */
	public static Map<Class<? extends BaseActivity>, SQLQueriesEnum> mapLogTypeAndReadQueries() {
		Map<Class<? extends BaseActivity>, SQLQueriesEnum> map = new HashMap<>();

		map.put(AuditActivityDTO.class, SQLQueriesEnum.AUDITACTIVITY_READ);
		map.put(MoneyMovementLogDTO.class, SQLQueriesEnum.MONEYMOVEMENTLOG_READ);

		return map;
	}

	/**
	 * Returns insert sql string for the provided concrete class of type
	 * {@link BaseActivity}
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T extends BaseActivity> String getActivityInsertQuery(Class<T> clazz) {
		return LOG_INSERT_QUERIES_MAPPER.get(clazz).getQuery();

	}

	/**
	 * Returns delete sql string for the provided concrete class of type
	 * {@link BaseActivity}
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T extends BaseActivity> String getActivityDeleteQuery(Class<T> clazz) {
		return LOG_DELETE_QUERIES_MAPPER.get(clazz).getQuery();
	}

	/**
	 * Returns select sql string for the provided concrete class of type
	 * {@link BaseActivity}
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T extends BaseActivity> String getActivityReadQuery(Class<T> clazz) {
		return LOG_READ_QUERIES_MAPPER.get(clazz).getQuery();
	}

}
