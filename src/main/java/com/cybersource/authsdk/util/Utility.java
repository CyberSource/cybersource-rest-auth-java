package com.cybersource.authsdk.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.core.ConfigException;

public class Utility {
	/**
	 * Depending on the logging LEVEL, the message is logged.
	 *
	 * @param logger
	 *            : Logging object (Log4j2).
	 * @param exceptionMessage
	 *            : Custom message for logging the log file.
	 * @param obj
	 *            : object to get exception message.
	 * @param logLevel
	 *            : Level at which logging should occur
	 *            [INFO,ERROR,DEBUG,FATAL].
	 */
	public static void log(Logger logger, String exceptionMessage, Object obj, Level logLevel) {
		try {
			if (logger != null) {
				exceptionMessage = exceptionMessage.concat("%s");
				if (StringUtils.isNotBlank(exceptionMessage)) {
					if (logLevel == Level.ERROR) {
						exceptionMessage = String.format(exceptionMessage, obj);
						logger.error(exceptionMessage);
					} else if (logLevel == Level.DEBUG) {
						exceptionMessage = String.format(exceptionMessage, obj);
						logger.debug(exceptionMessage);
					} else if (logLevel == Level.FATAL) {
						exceptionMessage = String.format(exceptionMessage, obj);
						logger.fatal(exceptionMessage);
					} else if (logLevel == Level.OFF) {
						exceptionMessage = String.format(exceptionMessage, obj);
						logger.printf(Level.OFF, "%n");
					} else {
						exceptionMessage = String.format(exceptionMessage, obj);
						logger.info(exceptionMessage);
					}
				}
			}
		} catch (Exception e) {
			log(logger, e);

		}
	}

	/**
	 * @param logger
	 *            : Logging object (Log4j2).
	 * @param e
	 *            : Exception caught during the process.
	 */
	public static void log(Logger logger, Exception e) {
		if (logger != null) {
			String logMessage = getStackTrace(e);
			logger.error(logMessage);
		}
	}

	/**
	 * This method returns the exception in string format to log, caught during
	 * the process.
	 *
	 * @param e
	 *            : Exception caught during the process.
	 * @return stackTrace of Exception.
	 */
	private static String getStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String stackTrace = sw.toString();
		pw.close();
		return stackTrace;
	}

	/**
	 * @return true if valid method type else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public static boolean isRequestMethodTypeValid(String httpMethod) throws ConfigException {
		Boolean isValid = true;
		if (httpMethod.equalsIgnoreCase(GlobalLabelParameters.PUT)
				|| httpMethod.equalsIgnoreCase(GlobalLabelParameters.GET)
				|| httpMethod.equalsIgnoreCase(GlobalLabelParameters.POST)
				|| httpMethod.equalsIgnoreCase(GlobalLabelParameters.DELETE)
				|| httpMethod.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
			isValid = true;
		} else {
			isValid = false;
			throw new ConfigException(GlobalLabelParameters.REQUEST_VALIDATION_ERROR);
		}
		return isValid;
	}

}
