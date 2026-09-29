/**
 * 
 */
package com.kony.dbx.util;

import java.time.LocalDate;

import com.konylabs.middleware.controller.DataControllerRequest;

/**
 * @author Gopinath Vaddepally - KH2453
 *
 */
public interface CustomDAO {
	/**
	 * for batch inserting data into db
	 * @param tableName
	 * @param colSpec - columns
	 * @param insertRecordsArray
	 * @param request - DCR
	 */
	void batchInsert(String tableName, String colSpec, String[] insertRecordsArray,DataControllerRequest request);
	/**
	 * batch delete
	 * @param request
	 * @param sqls
	 * @return array of sql execution status
	 */
	int[] executeSQLs(DataControllerRequest request,String ... sqls);
}
