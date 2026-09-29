package com.kony.dbp.queuemaster.service;

import java.io.File;
import javax.servlet.http.HttpServlet;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbp.queuemaster.utils.DatabaseUtils;
import com.kony.dbp.queuemaster.utils.DatabaseUtils.ConnectionPoolType;
import com.konylabs.middleware.servlet.IntegrationCustomServlet;

@IntegrationCustomServlet(urlPatterns = { "dummy" })
public class StopServlet extends HttpServlet {

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private static final long serialVersionUID = 1L;

	@Override
	public void destroy() {
		Thread.currentThread().setContextClassLoader(StopServlet.class.getClassLoader());
		diagnostic.prepareTrace("StopServlet destroy hook invoked!").log();
		String tempDirName = System.getProperty("java.io.tmpdir");
		File tempDir = new File(tempDirName);
		String[] tempFiles = tempDir.list();
		for (int i = 0; i < tempFiles.length; ++i) {
			String tempFileName = tempFiles[i];
			if (tempFileName.startsWith("QueueMaster-") && tempFileName.endsWith(".tmp")) {
				File tempFile = new File(tempDir, tempFiles[i]);
				if (tempFile.isFile()) {
					tempFile.delete();
					diagnostic.prepareTrace("Deleted temp file " + tempFile.getPath()).log();
				}
			}
		}
		DatabaseUtils.flushDatabaseConnectionPool(ConnectionPoolType.INTERNAL, ConnectionPoolType.REQUESTS);
	}
}
