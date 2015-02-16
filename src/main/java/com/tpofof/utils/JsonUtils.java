package com.tpofof.utils;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	private JsonUtils() { }
	
	public static <ModelT> ModelT fromJson(File file, Class<ModelT> modelType) {
		if (file == null) {
			throw new IllegalArgumentException("Json file cannot be null.");
		}
		if (!file.exists()) {
			throw new IllegalArgumentException("Json file must exist.");
		}
		if (modelType == null) {
			throw new IllegalArgumentException("Model Class cannot be null");
		}
		try {
			return mapper.readValue(file, modelType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static <ModelT> ModelT fromJson(String json, Class<ModelT> modelType) {
		if (json == null) {
			throw new IllegalArgumentException("Json content cannot be null");
		}
		if (modelType == null) {
			throw new IllegalArgumentException("Model Class cannot be null");
		}
		try {
			return mapper.readValue(json, modelType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String toJson(Object model) {
		if (model == null) {
			throw new IllegalArgumentException("Model cannot be null.");
		}
		try {
			return mapper.writeValueAsString(model);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JsonNode toJsonNode(Object model) {
		String jsonContent = toJson(model);
		if (jsonContent != null) {
			try {
				return mapper.readTree(jsonContent);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return  null;
	}
	
	public static JsonNode parse(String json) {
		if (json == null) {
			throw new IllegalArgumentException("Json content cannot be null");
		}
		try {
			return mapper.readTree(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
