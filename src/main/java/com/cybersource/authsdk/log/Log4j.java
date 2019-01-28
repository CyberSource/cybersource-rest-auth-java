package com.cybersource.authsdk.log;

import java.io.File;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import com.cybersource.authsdk.core.MerchantConfig;
import com.cybersource.authsdk.util.GlobalLabelParameters;

/**
 * Creation of logging object, using singleton design.
 *
 */
public class Log4j {
	private String filePath = null;
	private static volatile Logger logger = null;
	private String logDirectory;
	private String logFileName;
	private String logSize;
	public static boolean logEnable = false;
	
	/**
	 * @param merchantConfig
	 *            - contains all information for merchant.
	 */
	private Log4j(MerchantConfig merchantConfig) {
		logDirectory = merchantConfig.getLogDirectory();
		logFileName = merchantConfig.getLogFilename();
		logSize = merchantConfig.getLogMaximumSize();
		filePath = logDirectory + "/" + logFileName + GlobalLabelParameters.LOG_EXTENSION;
	}

	/**
	 * 
	 * @param merchantConfig
	 *            - contains all information for merchant.
	 * @return logger object of LOG4j
	 */
	public static Logger getInstance(MerchantConfig merchantConfig) {

		if (logger == null) {
			logEnable = merchantConfig.getEnableLog();
			// To provide thread-safe implementation.
			synchronized (Log4j.class) {
				if (merchantConfig.getEnableLog()) {
					if (logger == null) {
						logger = new Log4j(merchantConfig).getLogger();
					}
				}
			}

		}
		return logger;
	}


	private Logger getLogger() {
			addAppenderLogger(filePath, Level.ALL);
			return LogManager.getRootLogger();
	}
	
   	/**
	 * 
	 * @param filePath
	 *            - path for log file.
	 * @param level
	 *            - level for log file.
	 */
	private void addAppenderLogger(String filePath, Level level) {
		ConfigurationBuilder<BuiltConfiguration> builder;
		LayoutComponentBuilder layoutBuilder;
		AppenderComponentBuilder appenderBuilder;

		/* Create log4j2 Builder. */
		builder = ConfigurationBuilderFactory.newConfigurationBuilder();
		builder.setStatusLevel(Level.ERROR);
		builder.setConfigurationName("RollingBuilder");

		/* Pattern Layout. */
		layoutBuilder = builder.newLayout("PatternLayout").addAttribute("pattern",
				"%d{yyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n");

		/* Create rolling File Appender. */
		ComponentBuilder triggeringPolicy = builder.newComponent("Policies")
				.addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", logSize));
		appenderBuilder = builder.newAppender("rolling", "RollingFile").addAttribute("fileName", filePath)
				.addAttribute("filePattern",
						this.logDirectory + "/ " + this.logFileName + "_%d{yyyyMMddhhmmss}"
								+ GlobalLabelParameters.LOG_EXTENSION)
				.add(layoutBuilder).addComponent(triggeringPolicy);
		builder.add(appenderBuilder);

		/* Create the new logger. */
		builder.add(
				builder.newRootLogger(level).add(builder.newAppenderRef("rolling")).addAttribute("additivity", false));
		Configurator.initialize(builder.build());
	}

	private void validateLogMaxSizeAndFolder() {
		File dir;
		try {
			if (this.logDirectory == null || this.logDirectory.isEmpty()) {
				this.logDirectory = GlobalLabelParameters.DEFAULT_LOG_PATH;
			}
			dir = new File(this.logDirectory);
			if (!dir.isDirectory()) {
				this.logDirectory = GlobalLabelParameters.DEFAULT_LOG_PATH;
			}
		} catch (Exception e) {
			this.logDirectory = GlobalLabelParameters.DEFAULT_LOG_PATH;
		}

		try {
			if (this.logFileName == null || this.logFileName.isEmpty()) {
				this.logFileName = GlobalLabelParameters.DEFAULT_LOG_NAME;
			}
		} catch (Exception e) {
			this.logDirectory = GlobalLabelParameters.DEFAULT_LOG_NAME;
		}

		if (this.logSize == null || this.logSize.isEmpty()) {
			this.logSize = GlobalLabelParameters.DEFAULT_LOG_SIZE;
		}
	}
}