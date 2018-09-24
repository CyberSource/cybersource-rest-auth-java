package com.cybersource.authsdk.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.StringTokenizer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class Utility {
	private enum validationType {
		HTTP_SIGNATURE, JWT
	};

	public static boolean logEnable = false;

	/**
	 * Checks whether the authentication type is rightly entered with no typo
	 * error.
	 * 
	 * @param authenticationType
	 *            : HTTP or JWT
	 */
	public static boolean checkAuthenticationValidation(String authenticationType) {
		boolean status = false;

		if (authenticationType.equalsIgnoreCase(validationType.HTTP_SIGNATURE.toString())
				|| authenticationType.equalsIgnoreCase(validationType.JWT.toString())) {
			status = true;
		}
		return status;
	}

	/**
	 * Depending on the logging LEVEL, the message is logged.
	 * 
	 * @param logger
	 *            : Logging object (Log4j2).
	 * @param exceptionMessage
	 *            : Custom message for logging the log file.
	 * @param logLevel
	 *            : Level at which logging should occur
	 *            [INFO,ERROR,DEBUG,FATAL].
	 */
	public static void log(Logger logger, String exceptionMessage,Object obj, Level logLevel) {
		try {
			if (logEnable) {
				exceptionMessage=exceptionMessage.concat("%s");
				if (exceptionMessage != null && !exceptionMessage.isEmpty()) {
					if (logLevel == Level.ERROR) {
						exceptionMessage=String.format(exceptionMessage, obj);
						logger.error(exceptionMessage);
					} else if (logLevel == Level.DEBUG) {
						exceptionMessage=String.format(exceptionMessage, obj);
						logger.debug(exceptionMessage);
					} else if (logLevel == Level.FATAL) {
						exceptionMessage=String.format(exceptionMessage, obj);
						logger.fatal(exceptionMessage);
					} else if (logLevel == Level.OFF) {
						exceptionMessage=String.format(exceptionMessage , obj);
						logger.printf(Level.OFF, "%n");
					} else {
						exceptionMessage=String.format(exceptionMessage , obj);
						logger.info(exceptionMessage);
					}
				}
			}
		} catch (Exception e) {
			log(logger, e);
		}
	}

	/**
	 * 
	 * @param logger
	 *            : Logging object (Log4j2).
	 * @param e
	 *            : Exception caught during the process.
	 */
	public static void log(Logger logger, Exception e) {

		if (Utility.logEnable) {
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
	 * @return
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
	 * 
	 * @param requestTarget
	 * @return
	 */
	public static String retrieveGetIDFromRequestTarget(String requestTarget) {
		StringTokenizer str = new StringTokenizer(requestTarget, "/");
		String temp = null;
		while (str.hasMoreTokens()) {
			 temp = str.nextToken();
			if (temp.matches("^[0-9]+$")) {
				return temp;
			}
		}
		return temp;
	}

	/**
	 * 
	 * @param responseCode
	 * @return
	 * @return
	 */
	public static void logResponseCodeMessage(int responseCode, Logger logger) {
		if (logEnable) {
			String tempResponseCode = String.valueOf(responseCode);
			String tempResponseCodeMessage = "Status Information :: ";
			String unDefinedResponse = "Un-Indentified";
			switch (tempResponseCode) {
			case "200":
				tempResponseCodeMessage = tempResponseCodeMessage + "Transaction Successful";
				logger.info(tempResponseCodeMessage);
				break;
			case "400":
				tempResponseCodeMessage = tempResponseCodeMessage + "Bad Request";
				logger.info(tempResponseCodeMessage);
				break;
			case "401":
				tempResponseCodeMessage = tempResponseCodeMessage + "Authentication Failed";
				logger.info(tempResponseCodeMessage);
				break;
			case "201":
				tempResponseCodeMessage = tempResponseCodeMessage + "Transaction Successful";
				logger.info(tempResponseCodeMessage);
				break;
			case "500":
				tempResponseCodeMessage = tempResponseCodeMessage + "Internal Server Error";
				logger.info(tempResponseCodeMessage);
				break;
			case "502":
				tempResponseCodeMessage = tempResponseCodeMessage + "Bad Gateway";
				logger.info(tempResponseCodeMessage);
				break;
			case "503":
				tempResponseCodeMessage = tempResponseCodeMessage + "SERVICE UNAVAILABLE";
				logger.info(tempResponseCodeMessage);
				break;
			case "504":
				tempResponseCodeMessage = tempResponseCodeMessage + "Gateway Timeout";
				logger.info(tempResponseCodeMessage);
				break;
			case "404":
				tempResponseCodeMessage = tempResponseCodeMessage + "Not Found";
				logger.info(tempResponseCodeMessage);
				break;
			case "403":
				tempResponseCodeMessage = tempResponseCodeMessage + "Forbidden";
				logger.info(tempResponseCodeMessage);
				break;
			default:
				logger.info(tempResponseCodeMessage + unDefinedResponse);
			}
		}
	}
}
