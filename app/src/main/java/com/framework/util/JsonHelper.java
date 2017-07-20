package com.framework.util;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.Iterator;


/**
 * 根据JSON数据返回 反解析成相应的对象
 * 
 * @author yangli
 * 
 * @param <E>
 */
public class JsonHelper<E> {
	


	/**
	 * 解析json字符串到对象
	 * @param <E>
	 * @param jsonData
	 * @param E
	 * @return
	 */
	public <E> E fromJsonByJskonType(String jsonData, TypeReference<E> E)  {
		E parseTarget=null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
			parseTarget = (E) mapper.readValue(jsonData, E);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		return parseTarget;
	}

//	public <E> E fromJsonByType(String json, Type t) {
//		Gson gson = new Gson();
//		E parseTarget = (E) gson.fromJson(json, t);
//	}
//		return parseTarget;

	public String[] fromJsonByString(String jsonData, String getString) {
		String[] args = null;
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = null;
		try {
			jsonNode = mapper.readValue(jsonData, JsonNode.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<JsonNode> nodes = jsonNode.getElements();
		args = new String[jsonNode.size()];
		while (nodes.hasNext()) {

			JsonNode jn = nodes.next();
			args = new String[jn.size()];
			for (int i = 0, length = jn.size(); i < length; i++) {
				args[i] = jn.get(i).getTextValue();
			}
			// //Log.v("this node",nodes.next().get(0).getTextValue());
		}
		return args;
	}
}
