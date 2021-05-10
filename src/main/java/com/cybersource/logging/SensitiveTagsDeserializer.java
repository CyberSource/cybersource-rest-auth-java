package com.cybersource.logging;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class SensitiveTagsDeserializer implements JsonDeserializer<SensitiveDataConfiguration> {
	public SensitiveDataConfiguration deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
			throws JsonParseException {
		final JsonObject jsonObject = arg0.getAsJsonObject();

		SensitiveTag[] sensitiveTags = arg2.deserialize(jsonObject.get("sensitiveTags"), SensitiveTag[].class);
		AuthenticationSchemeTag[] authenticationTags = arg2.deserialize(jsonObject.get("authenticationSchemes"), AuthenticationSchemeTag[].class);

		final SensitiveDataConfiguration configType = new SensitiveDataConfiguration();
		configType.setSensitiveTags(sensitiveTags);
		configType.setAuthenticationSchemeTags(authenticationTags);

		return configType;
	}
}
