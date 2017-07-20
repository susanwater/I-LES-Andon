package framework.util;

import android.util.Log;

/**
 * Created by Admin on 17/7/20.
 */
public class LogUtils {

    public static boolean Loggable = false;

    public LogUtils() {
    }

    public static void debug(String tag, Throwable t) {
        if(Loggable) {
            Log.d(tag, "", t);
        }

    }

    public static void debug(String tag, String msg) {
        if(Loggable) {
            Log.d(tag, msg);
        }

    }

    public static void debug(String tag, String msg, Throwable t) {
        if(Loggable) {
            Log.d(tag, msg, t);
        }

    }

    public static void debug(String tag, String format, Object... args) {
        if(Loggable) {
            String var3 = String.format(format, args);
            Log.d(tag, var3);
        }

    }

    public static void info(String tag, String msg) {
        if(Loggable) {
            Log.i(tag, msg);
        }

    }

    public static void info(String tag, String msg, Throwable t) {
        if(Loggable) {
            Log.i(tag, msg, t);
        }

    }

    public static void info(String tag, String format, Object... args) {
        if(Loggable) {
            String var3 = String.format(format, args);
            Log.i(tag, var3);
        }

    }

}
