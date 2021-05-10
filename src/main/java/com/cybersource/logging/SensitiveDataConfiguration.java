package com.cybersource.logging;

public class SensitiveDataConfiguration {
	public SensitiveTag[] sensitiveTags;
	public AuthenticationSchemeTag[] authenticationSchemes;

	public SensitiveTag[] getSensitiveTags() {
		return sensitiveTags;
	}

	public void setSensitiveTags(SensitiveTag[] sensitiveTags) {
		this.sensitiveTags = sensitiveTags;
	}

	public AuthenticationSchemeTag[] getAuthenticationSchemes() {
		return authenticationSchemes;
	}

	public void setAuthenticationSchemeTags(AuthenticationSchemeTag[] authenticationSchemes) {
		this.authenticationSchemes = authenticationSchemes;
	}
}
