package com.anbang.qipai.daboluo.cqrs.q.dao.memcached.serialize;

import java.lang.reflect.Type;

import com.dml.shisanshui.pai.PukePaiMark;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * PukePaiMark序列化适配器
 * 
 * @author lsc
 *
 */
public class PukePaiMarkAdapter implements JsonSerializer<PukePaiMark>, JsonDeserializer<PukePaiMark> {

	@Override
	public PukePaiMark deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		String type = jsonObject.get("type").getAsString();
		JsonElement element = jsonObject.get("properties");
		try {
			return context.deserialize(element, Class.forName(type));
		} catch (ClassNotFoundException cnfe) {
			throw new JsonParseException("Unknown element type: " + type, cnfe);
		}
	}

	@Override
	public JsonElement serialize(PukePaiMark src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		result.add("type", new JsonPrimitive(src.getClass().getName()));
		result.add("properties", context.serialize(src, src.getClass()));
		return result;
	}

}
