package com.cybersource.logging;

public class SensitiveDataConfiguration {
	public SensitiveTag[] sensitiveTags;

	public SensitiveTag[] getSensitiveTags() {
		return sensitiveTags;
	}

	public void setSensitiveTags(SensitiveTag[] sensitiveTags) {
		this.sensitiveTags = sensitiveTags;
	}

	public String[] getSensitiveStringRegexes() {
		return sensitiveStringRegexes;
	}

	public void setSensitiveStringRegexes(String[] sensitiveStringRegexes) {
		this.sensitiveStringRegexes = sensitiveStringRegexes;
	}

	public String[] sensitiveStringRegexes;
}
