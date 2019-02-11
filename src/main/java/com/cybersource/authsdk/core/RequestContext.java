package com.cybersource.authsdk.core;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.cybersource.authsdk.util.GlobalLabelParameters;
import com.cybersource.authsdk.util.PropertiesUtil;
import com.cybersource.authsdk.util.Utility;

public class RequestContext {
	private String date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
	private String requestBody;
	private String requestJsonPath;
	private String requestMethodType;
	private String requestTarget;
	private String url;
	private Logger log;

	public RequestContext(Logger log) {
		this.log = log;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

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
			throw new ConfigException(GlobalLabelParameters.FILE_NOT_FOUND, e);
		}
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

}
