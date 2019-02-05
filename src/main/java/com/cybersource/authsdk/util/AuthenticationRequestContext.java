package com.cybersource.authsdk.util;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.core.ConfigException;

public class AuthenticationRequestContext {

	private String requestBody;
	private String requestJsonPath;
	private boolean isRequestJson = true;
	private String requestMethodType;
	private String requestTarget;
	private String url;

	/* Others. */
	private Logger log;

	public String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));

	public String getRequestTarget() {
		return requestTarget;
	}

	public String getRequestMethodType() {
		return requestMethodType;
	}

	public void setRequestMethodType(String methodType) {
		this.requestMethodType = methodType;
	}

	public void setRequestTarget(String requestTarget) {
		this.requestTarget = requestTarget;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRequestJsonPath() {
		return requestJsonPath;
	}

	public void setRequestJsonPath(String requestJsonPath) {
		this.requestJsonPath = requestJsonPath;
	}

	public void setRequestJson(boolean isRequestJson) {
		this.isRequestJson = isRequestJson;
	}

	public boolean isRequestJson() {
		return isRequestJson;
	}

	/**
	 * @return request data according to availability of request Json.
	 * @throws ConfigException
	 *             - if some value is missing for merchant.
	 */
	public String getRequestBody() throws ConfigException {

		try {
			if (StringUtils.isNotBlank(requestJsonPath)
					&& !requestJsonPath.equalsIgnoreCase(GlobalLabelParameters.REQUEST_JSON_PATH_NOT_REQUIRED)) {
				requestBody = PropertiesUtil.getJsonInput(requestJsonPath);
			}
		} catch (Exception e) {
			Utility.log(log, GlobalLabelParameters.FILE_NOT_FOUND, requestJsonPath, Level.FATAL);
			Utility.log(log, e);
			throw new ConfigException(GlobalLabelParameters.FILE_NOT_FOUND);
		}
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	/**
	 * @return true if valid method type else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public boolean isRequestTypeMethodValid() throws ConfigException {
		if (requestMethodType.equalsIgnoreCase(GlobalLabelParameters.PUT)
				|| requestMethodType.equalsIgnoreCase(GlobalLabelParameters.GET)
				|| requestMethodType.equalsIgnoreCase(GlobalLabelParameters.POST)
				|| requestMethodType.equalsIgnoreCase(GlobalLabelParameters.DELETE)
				|| requestMethodType.equalsIgnoreCase(GlobalLabelParameters.PATCH)) {
			return true;
		} else {
			Utility.log(log, GlobalLabelParameters.INVALID_REQUEST_TYPE, requestMethodType, Level.ERROR);
			throw new ConfigException(GlobalLabelParameters.INVALID_REQUEST_TYPE);

		}
	}

	/**
	 * @param jsonPath
	 *            -path for request json.
	 * @return true if path is valid else return false.
	 * @throws ConfigException
	 *             - if some value is missing or wrong for merchant.
	 */
	public boolean validateRequestDetails(String jsonPath) throws ConfigException {
		File f = null;
		boolean check = true;

		if (!isRequestTypeMethodValid()) {
			return false;
		}

		if (requestMethodType.equalsIgnoreCase(GlobalLabelParameters.GET)
				|| requestMethodType.equalsIgnoreCase(GlobalLabelParameters.DELETE)) {
			return check;
		}

		if (StringUtils.isBlank(jsonPath)) {
			Utility.log(log, GlobalLabelParameters.REQUEST_JSON_MIS_NULL, "", Level.ERROR);
			check = false;
			throw new ConfigException(GlobalLabelParameters.REQUEST_JSON_MIS_NULL);
		}

		if (StringUtils.isNotBlank(jsonPath)
				&& jsonPath.equalsIgnoreCase(GlobalLabelParameters.REQUEST_JSON_PATH_NOT_REQUIRED)) {
			return check;
		}

		f = new File(jsonPath);
		if (!f.exists()) {
			Utility.log(log, GlobalLabelParameters.REQUEST_JSON_ERROR, f.getAbsolutePath(), Level.ERROR);
			check = false;
			throw new ConfigException(GlobalLabelParameters.REQUEST_JSON_ERROR);

		}
		if (!f.canRead()) {
			Utility.log(log, GlobalLabelParameters.FILE_NOT_READ, f.getAbsolutePath(), Level.ERROR);
			check = false;
			throw new ConfigException(GlobalLabelParameters.FILE_NOT_READ);
		}

		isRequestJson = true;
		return check;
	}

}
