package com.cybersource.logging;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.ConverterKeys;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Plugin(name = "SensitiveFilterPatternConverter", category = "Converter")
@ConverterKeys({ "maskedMessage" })
public class SensitiveFilterPatternConverter extends LogEventPatternConverter {
	private static Pattern[] tagPatterns;
	private static String[] tagReplacements;
	private static Pattern[] authPatterns;
	private static String[] authReplacements;
	private static Gson gson;

	private SensitiveFilterPatternConverter(final String name, final String style) {
		super(name, style);
		initialize();
	}

	public static SensitiveFilterPatternConverter newInstance(final String[] options) {
		return new SensitiveFilterPatternConverter("maskedMessage", "maskedMessage");
	}

	@Override
	public void format(LogEvent event, StringBuilder toAppendTo) {
		try {
			String message = event.getMessage().getFormattedMessage();
			String maskXmlMessage = SensitiveFilterPatternConverter.maskSensitiveXmlString(message);
			maskXmlMessage = SensitiveFilterPatternConverter.maskAuthenticationDataInString(maskXmlMessage);

			toAppendTo.append(maskXmlMessage.trim());
		} catch (Exception e) {
			System.out.println("ERROR : " + e);
		}
	}

	public void initialize() {
		try {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(SensitiveDataConfiguration.class, new SensitiveTagsDeserializer());
			gson = gsonBuilder.create();
			
			SensitiveDataConfiguration configType = null;

			try {
				InputStream in = SensitiveFilterPatternConverter.class.getResourceAsStream("/SensitiveTagsConfiguration.json");
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				configType = gson.fromJson(reader, SensitiveDataConfiguration.class);
				if (reader != null)
					reader.close();
			} catch (Exception e) {
				System.out.println("ERROR : " + e);
			}
			
			if (configType == null) {
				return;
			}

			int noOfSensitiveTags = configType.sensitiveTags.length;
			tagPatterns = new Pattern[noOfSensitiveTags];
			tagReplacements = new String[noOfSensitiveTags];

			for (int j = 0; j < noOfSensitiveTags; j++) {
				String tagName = configType.sensitiveTags[j].tagName;
				String pattern = configType.sensitiveTags[j].pattern;
				String replacement = configType.sensitiveTags[j].replacement;

				if (pattern != null && !pattern.isEmpty()) {
					tagPatterns[j] = Pattern.compile("\"" + tagName + "\":\"" + pattern + "\"");
				}
				else {
					tagPatterns[j] = Pattern.compile("\"" + tagName + "\":\".+\"");
				}
				tagReplacements[j] = "\"" + tagName + "\":\"" + replacement + "\"";
			}
			
			int noOfAuthenticationTags = configType.authenticationSchemes.length;
			authPatterns = new Pattern[noOfAuthenticationTags];
			authReplacements = new String[noOfAuthenticationTags];
			
			for (int i = 0; i < noOfAuthenticationTags; i++) {
				String tagName = configType.authenticationSchemes[i].tagName;
				String pattern = configType.authenticationSchemes[i].pattern;
				String replacement = configType.authenticationSchemes[i].replacement;
				
				if (pattern != null && !pattern.isEmpty()) {
					authPatterns[i] = Pattern.compile(tagName + ": " + pattern);
				}
				authReplacements[i] = replacement;
			}
		} catch (Exception e) {
			System.out.println("ERROR : " + e);
		}
	}

	public static String maskSensitiveXmlString(String str) {
		for (int i = 0; i < tagPatterns.length; i++) {
			str = tagPatterns[i].matcher(str).replaceAll(tagReplacements[i]);
		}
		return str;
	}

	public static String maskAuthenticationDataInString(String str) {
		for (int i = 0; i < authPatterns.length; i++) {
			str = str.replaceAll(authPatterns[i].toString(), authReplacements[i]);
		}
		
		return str;
	}
}
