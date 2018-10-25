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
import com.cybersource.authsdk.util.Utility;

/**
 * Creation of logging object, using singleton design.
 *
 */
public class Log4j {
	private String standardSize;
	private String filePath = null;
	private static volatile Logger logger = null;
	private String logDirectory;
	private String logFileName;
	private String logSize;
	public static boolean logName = false;

	/**
	 * @param merchantConfig
	 */
	private Log4j(MerchantConfig merchantConfig) {
		logDirectory = merchantConfig.getLogDirectory();
		logFileName = merchantConfig.getLogFilename();
		logSize = merchantConfig.getLogMaximumSize();
		checkLogMaxSizeAndFolder();

		filePath = logDirectory + "/" + logFileName + GlobalLabelParameters.LOG_EXTENSION;
		standardSize = merchantConfig.getLogMaximumSize();
	}

	/**
	 * 
	 * @param merchantConfig
	 * @return
	 */
		public static Logger getInstance(MerchantConfig merchantConfig) {

		if (logger == null) {
			Utility.logEnable = merchantConfig.getEnableLog();
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

	/**
	 * 
	 *
	 */
	private Logger getLogger() {
		Logger logger;
		addAppenderLogger(filePath, Level.ALL);
		logger = LogManager.getRootLogger();
		return logger;
	}

	/**
	 * 
	 * @param filePath
	 * @param vlevel
	 */
	public void addAppenderLogger(String filePath, Level vlevel) {

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
				.addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", standardSize));
		appenderBuilder = builder.newAppender("rolling", "RollingFile").addAttribute("fileName", filePath)
				.addAttribute("filePattern",
						this.logDirectory + "/ " + this.logFileName + "_%d{yyyyMMddhhmmss}"
								+ GlobalLabelParameters.LOG_EXTENSION)
				.add(layoutBuilder).addComponent(triggeringPolicy);
		builder.add(appenderBuilder);

		/* Create the new logger. */
		builder.add(
				builder.newRootLogger(vlevel).add(builder.newAppenderRef("rolling")).addAttribute("additivity", false));
		Configurator.initialize(builder.build());
	}

	/**
	 * 
	 * @return
	 */
	private void checkLogMaxSizeAndFolder() {
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