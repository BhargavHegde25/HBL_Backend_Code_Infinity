package com.kony.dbputilities.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.SocketAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.logging.facade.LoggerFactory;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public class Log4j2Configurator {
	private static Log4j2Configurator instance = null;
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private Log4j2Configurator() {
		try {
			String socketHostName = EnvironmentConfigurationsHandler.getServerAppProperty("SOCKET_CONNECTION_HOSTNAME");
			int socketPort = EnvironmentConfigurationsHandler.getServerAppProperty("SOCKET_CONNECTION_PORT_NUMBER") != null
					? Integer.parseInt(EnvironmentConfigurationsHandler.getServerAppProperty("SOCKET_CONNECTION_PORT_NUMBER"))
					: 4560;
			int socketRetryMilliSeconds = EnvironmentConfigurationsHandler.getServerAppProperty("SOCKET_CONNECTION_RETRY_MILLISECONDS") != null
							? Integer.parseInt(EnvironmentConfigurationsHandler.getServerAppProperty("SOCKET_CONNECTION_RETRY_MILLISECONDS"))
							: 5000;
			Layout<?> layout = PatternLayout.newBuilder().withPattern("%m%n").build();

			SocketAppender appender = SocketAppender.newBuilder()
					.setName("monitoring")
					.setLayout(layout)
					.withHost(socketHostName)
					.withPort(socketPort)
					.withReconnectDelayMillis(socketRetryMilliSeconds)
					.build();

			appender.start();
			LoggerContext context = (LoggerContext) LogManager.getContext(false);
			Configuration config = context.getConfiguration();
			AppenderRef ref = AppenderRef.createAppenderRef(appender.getName(), null, null);
			AppenderRef[] appenderRef = new AppenderRef[] { ref };

			LoggerConfig alertLoggerConfig = LoggerConfig.createLogger(false, Level.INFO, "ALERT.SPOTLIGHT", "false",
					appenderRef, null, config, null);
			config.addLogger("ALERT_SPOTLIGHT", alertLoggerConfig);
			LoggerConfig diagnosticLoggerConfig = LoggerConfig.createLogger(false, Level.DEBUG,
					"DIAGNOSTIC.SPOTLIGHT", "false", appenderRef, null, config, null);
			config.addLogger("DIAGNOSTIC_SPOTLIGHT", diagnosticLoggerConfig);
			LoggerConfig auditLoggerConfig = LoggerConfig.createLogger(false, Level.DEBUG,
					"AUDIT.SPOTLIGHT-ADMINACTIVITY", "false", appenderRef, null, config, null);
			config.addLogger("AUDIT_SPOTLIGHT-ADMINACTIVITY", auditLoggerConfig);

			config.getRootLogger().addAppender(appender, Level.ALL, null);
			context.updateLoggers(config);

			LoggerFactory.init("Infinity");
		} catch (Exception e) {
			alert.prepareError("Failed to establish socket connection :" + e);
		}
	}

	public static Log4j2Configurator getInstance() throws Exception {
		boolean enableLogMonitoring = Boolean.parseBoolean(EnvironmentConfigurationsHandler.getServerAppProperty("ENABLE_TEMN_LOG_MONITORING"));
		if (enableLogMonitoring) {
			if (instance == null) {
				instance = new Log4j2Configurator();
			}
			return instance;
		}
		return instance;
	}
}