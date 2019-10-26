package com.yangda.andon.net;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 17/6/1.
 * 在下面写API，按照登录例子
 */
public class MissionApi {

    @SuppressWarnings("unchecked")
    public static <T> Map<String, Object> beanToMap(T bean) {

        try {

            Class<? extends Object> class1 = bean.getClass();
            Field[] fields = class1.getFields();
            Map<String, Object> map = new HashMap<String, Object>();
            for (Field field : fields) {
                Object value = field.get(bean);
                Class<?> class_ = field.getType();

                if(value==null){
                    continue;
                }
                if (class_ == List.class) {
                    ArrayList<T> list = (ArrayList<T>) value;
                    if (list.size() == 0)
                        continue;
                } else if (value.getClass() == Long.class) {
                    Long longValue = (Long) value;
                    if (longValue == 0)
                        continue;
                } else if (value.getClass() == Double.class) {
                    Double doubleValue = (Double) value;
                    if (doubleValue == 0d)
                        continue;
                } else if (value.getClass() == String.class) {
                    String string = (String) value;
                    if (TextUtils.isEmpty(string))
                        continue;
                }
                map.put(field.getName(), value);
            }

            return map;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
