/**
 * 
 */
package com.kony.dbx.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.ArrayUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.controller.DataControllerRequest;

/**
 * @author Gopinath Vaddepally KH2453
 *
 */
public class CustomDAOImpl implements CustomDAO,Constants {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public void batchInsert(String tableName, String colSpec, String[] insertRecordsArray,
			DataControllerRequest request) {
		HikariConfiguration.getDataSource(request);
        int count = 0;
        Connection connection = HikariConfiguration.getconnection();
        Statement stmt = null;
        int[] result = {};
        int batchSize = 0;
		try {
            batchSize = insertRecordsArray.length;
        } catch (NumberFormatException nfe) {
            diagnostic.prepareInfo("Batch Size for insert not provided in the request. Using the defaul value:" + batchSize).log();
        }

        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            for (int i = 0; i < insertRecordsArray.length; i++) {
            	
            	String query = "insert into " + tableName +" " + colSpec+  " values " + insertRecordsArray[i];
            	alert.prepareError("query =>"+ query).log();
            	
            	String sqlToInsert = "insert into " + tableName;
            	sqlToInsert += colSpec != null ? colSpec : "";
            	sqlToInsert += " values " + insertRecordsArray[i];            	
                stmt.addBatch(sqlToInsert);

                count++;

                if (count % batchSize == 0) {
                    result = stmt.executeBatch();
                    if (batchSize != result.length) { //Something went wrong.
                        alert.prepareError("Unexpected error while inserting records").log();
                    }
                    connection.commit(); // All well, commit
                    diagnostic.prepareInfo("Succesfully committed batch insert").log();
                }
            }

            if (count % batchSize != 0) { // Commit any leftovers
                result = stmt.executeBatch();
                if ((count % batchSize) != result.length) { //Something went wrong.
                    alert.prepareError("Unexpected error while inserting records").log();
                }
                connection.commit(); // All well, commit
                diagnostic.prepareInfo("Succesfully committed batch insert").log();
            }
        } catch (Exception e) {
            alert.prepareError("Exception while executing batch insert:",e).log();
            try {
				connection.rollback();
			} catch (SQLException e1) {
				
				alert.prepareError(e1.toString()).log();
			}
        } finally {
           HikariConfiguration.close(stmt);
           HikariConfiguration.close(connection);
        }
	}
	
	@Override
	public int[] executeSQLs(DataControllerRequest request, String... sqls) {
		HikariConfiguration.getDataSource(request);
		Connection connection = HikariConfiguration.getconnection();
		int batchSize = 0;
		try {
            batchSize = 100;// TODO: Read from the server configuration
        } catch (NumberFormatException nfe) {
            diagnostic.prepareDebug("Batch Size for insert not provided in the request. Using the defaul value:" + batchSize).log();
        }

		PreparedStatement pstmt = null;
		int count = 0;
		int[] result = {};
		int[] finalResult = {};
		try {
            connection.setAutoCommit(false);
            
            for (int i = 0; i < sqls.length; i++) {
            	pstmt = connection.prepareStatement(sqls[i]);
                pstmt.addBatch();
                 alert.prepareError("sql statment =>"+sqls[i]).log();
                count++;

                if (count % batchSize == 0) {
                    result = pstmt.executeBatch();
                    if (batchSize != result.length) { //Something went wrong.
                        alert.prepareError("Unexpected error while inserting records").log();
                    }
                    connection.commit(); // All well, commit
                    finalResult = ArrayUtils.addAll(finalResult, result);
                    alert.prepareError("Succesfully committed batch insert").log();
                }
            }

            if (count % batchSize != 0) { // Commit any leftovers
                result = pstmt.executeBatch();
                if ((count % batchSize) != result.length) { //Something went wrong.
                    alert.prepareError("Unexpected error while inserting records").log();
                }
                connection.commit(); // All well, commit
                finalResult = ArrayUtils.addAll(finalResult, result);
                alert.prepareError("Succesfully committed batch insert").log();
            }
        } catch (Exception e) {
            alert.prepareError("Exception while executing batch insert:",e).log();
            try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new RuntimeException("SQL Exception while execute batch SQL statement in batch #" + sqls.length/batchSize + " with exception:" + e.getMessage());
			}
        } finally {
           HikariConfiguration.close(pstmt);
           HikariConfiguration.close(connection);
        }
		alert.prepareError("Final Result in Delete"+finalResult).log();
        return finalResult;
	}
	
}
