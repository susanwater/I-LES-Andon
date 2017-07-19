package com.haier.ledai.util;

import android.content.Context;
import android.widget.Toast;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by Admin on 17/6/1.
 */
public class Util {

    /**
     * 显示toast提示
     *
     * @param text
     * @param
     * @see [类、类#方法、类#成员]
     */
    public static void showToast(Context context,String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG)
                .show();
    }


    public static String ObjectToJson(Object obj) throws IOException {

        if (obj == null) return "";
        ObjectMapper mapper = new ObjectMapper();
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createJsonGenerator(writer);
        mapper.writeValue(gen, obj);
        gen.close();
        String json = writer.toString();
        writer.close();
        return json;
    }

}
