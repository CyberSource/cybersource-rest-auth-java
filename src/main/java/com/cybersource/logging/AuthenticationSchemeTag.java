package com.cybersource.logging;

public class AuthenticationSchemeTag {
	public String tagName;
	public String description;
	public String pattern;
	public String replacement;

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getReplacement() {
		return replacement;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

	public AuthenticationSchemeTag(String tagName, String description, String pattern, String replacement) {
		this.tagName = tagName;
		this.description = description;
		this.pattern = pattern;
		this.replacement = replacement;
	}
}
