package com.yangda.andon.util;

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
    public static void showToast(Context context, String text) {
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

    // 两次点击按钮之间的点击间隔不能少于5000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 5000;
    private static long lastClickTime = 0L;

    /**
     *
     * @return true:能往后走，false:不能往后走
     */
    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }


}
