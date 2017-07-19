package framework.util;

import org.codehaus.jackson.type.TypeReference;

import java.util.HashMap;
import java.util.Map;

public class JsonUtil {

	/**
	 * 缓存json
	 * 
	 * @author user yangli
	 **/
	static Map<String, Object> cache = new HashMap<String, Object>();

	/**
	 * 解析json字符串到对象加缓存,@changed by yangli
	 * 
	 * @param <E>
	 * @param json
	 * @param
	 *            <E>
	 * @return
	 */
	public static <E> E fromJson(String json, TypeReference<E> tf,
			boolean needcache) {
		JsonHelper<E> jh = new JsonHelper<E>();

		try {
			E a;
			// if(cache.get(json)==null){
			a = (E) jh.fromJsonByJskonType(json, tf);
			// cache.put(json, a);
			// }else{
			// return (E) cache.get(json);
			// }
			return a;
		} catch (Exception e) {
			e.printStackTrace();
			// DdUtil.log(e.toString());
			return null;
		}

	}

	/**
	 * 解析json字符串到对象,@changed by yangli
	 * 
	 * @param <E>
	 * @param json
	 * @param TypeReference
	 *            <E>
	 * @return
	 */
	public static <E> E fromJson(String json, TypeReference<E> tf) {
		// rs = DdUtil.fromJson(json, new
		// TypeReference<CommonBeanResult<HashMap>>() {});
		/*if (Preferences.DEBUG) {
			Log.e("RETURN_JSON", json);
		}*/
		JsonHelper<E> jh = new JsonHelper<E>();
		try {
			E a;
			//if (cache.get(json) == null) {
				a = (E) jh.fromJsonByJskonType(json, tf);
				//cache.put(json, a);
//			} else {
//				return (E) cache.get(json);
//			}
			
			return a;
		} catch (Exception e) {
			e.printStackTrace();
			// Log.e("e", e.toString());
			return null;
		}

	}
}
