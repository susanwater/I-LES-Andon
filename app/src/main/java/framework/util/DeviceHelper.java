package framework.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.haier.ledai.MyApplication;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Locale;

/**
 * Created by Admin on 17/7/19.
 */
public class DeviceHelper {

    public static int dp2pix(float dip) {
        return getPixelFromDip(MyApplication.getApplication().getResources().getDisplayMetrics(), dip);
    }

    public static int getPixelFromDip(DisplayMetrics dm, float dip) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, dm) + 0.5f);
    }

    /**
     *
     * 获取设备 SDK版本号
     *
     */
    public static int getSDKVersionInt() {
        return Build.VERSION.SDK_INT;
    }

    /**
     *
     * 获取设备的IMEI号
     *
     */
    public static String getIMEI() {
        TelephonyManager teleMgr = (TelephonyManager) MyApplication.getApplication()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return teleMgr.getDeviceId();
    }

    /**
     * 判断是否arm架构cpu
     *
     * @return	arm返回true，否则false
     */
    public static boolean isARMCPU() {
        String cpu = Build.CPU_ABI;
        return cpu != null && cpu.toLowerCase(Locale.getDefault()).contains("arm");
    }

    /**
     *
     * 获取设备的IMSI号
     *
     */
    public static String getIMSI() {
        TelephonyManager teleMgr = (TelephonyManager) MyApplication.getApplication()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return teleMgr.getSubscriberId();
    }

    public static String getNetworkOperator() {
        TelephonyManager teleMgr = (TelephonyManager) MyApplication.getApplication()
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (teleMgr == null)
            return "UNKNOWN";

        String IMSI = teleMgr.getSubscriberId();
        if (TextUtils.isEmpty(IMSI))
            return "UNKNOWN";

        if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
            return "CMCC";
        }
        else if (IMSI.startsWith("46001")) {
            return "CUCC";
        }
        else if (IMSI.startsWith("46003")) {
            return "CTCC";
        }else {
            return "UNKNOWN";
        }
    }

    /**
     * 获取屏幕分辨率
     * @param activity
     * @return
     */
    public static int[] getScreenDispaly(Activity activity) {
        WindowManager winManager=(WindowManager)activity.getSystemService(Context.WINDOW_SERVICE);
        Display display=winManager.getDefaultDisplay();
        int result[] = { display.getWidth(), display.getHeight() };
        return result;
    }

    public static String  getDeviceId(){
        try {
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) MyApplication.getApplication()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        }
        catch (Exception e){
            return "";
        }
    }


    public static String getDeviceInfoForUMeng() {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) MyApplication.getApplication()
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) MyApplication.getApplication()
                    .getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }

            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(
                        MyApplication.getApplication().getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取设备号 wifi mac + imei + cpu serial
     *
     * @return 设备号
     *
     */
    public static String getMobileUUID() {
        String uuid = "";
        // 先获取mac
        WifiManager wifiMgr = (WifiManager) MyApplication.getApplication().getSystemService(Context.WIFI_SERVICE);
		/* 获取mac地址 */
        if (wifiMgr != null) {
            WifiInfo info = wifiMgr.getConnectionInfo();
            if (info != null && info.getMacAddress() != null) {
                uuid = info.getMacAddress().replace(":", "");
            }
        }

        // 再加上imei
        TelephonyManager teleMgr = (TelephonyManager) MyApplication.getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        String imei = teleMgr.getDeviceId();
        uuid += imei;

        // 最后再加上cpu
        String str = "", strCPU = "", cpuAddress = "";
        try {
            String[] args = { "/system/bin/cat", "/proc/cpuinfo" };
            ProcessBuilder cmd = new ProcessBuilder(args);
            java.lang.Process pp = cmd.start();
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (int i = 1; i < 100; i++) {
                str = input.readLine();
                if (str != null) {
                    if (str.indexOf("Serial") > -1) {
                        strCPU = str.substring(str.indexOf(":") + 1, str.length());
                        cpuAddress = strCPU.trim();
                        break;
                    }
                } else {
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        uuid += cpuAddress;

        // 如果三个加在一起超过64位的话就截取
        if (uuid != null && uuid.length() > 64) {
            uuid = uuid.substring(0, 64);
        }
        return uuid;
    }

    public static boolean isAppInstalled(Context context, String pkgName) {
        if (context == null) {
            return false;
        }

        try {
            context.getPackageManager().getPackageInfo(pkgName, PackageManager.PERMISSION_GRANTED);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String getAppChannelName() {
        String channel = "";
        try {
            ApplicationInfo info = MyApplication.getApplication().getPackageManager()
                    .getApplicationInfo(MyApplication.getApplication().getPackageName(), PackageManager.GET_META_DATA);
            if (info != null && info.metaData != null) {
                channel = info.metaData.getString("UMENG_CHANNEL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channel;
    }

    public  static String getAppVersion(){
        String version = "";
        PackageManager packageManager = MyApplication.getApplication().getPackageManager();
        try {
            PackageInfo info = packageManager.getPackageInfo(MyApplication.getApplication().getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }
    public  static int getAppVersionCode(){
        int version = 0;
        PackageManager packageManager = MyApplication.getApplication().getPackageManager();
        try {
            PackageInfo info = packageManager.getPackageInfo(MyApplication.getApplication().getPackageName(), 0);
            version = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    public static String getNetworkIP(){

        NetworkInfo localNetworkInfo = ((ConnectivityManager) MyApplication.getApplication().getSystemService("connectivity")).getActiveNetworkInfo();
        if ((localNetworkInfo == null))
            return null;

        WifiManager wifiManager = (WifiManager) MyApplication.getApplication().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if(wifiInfo == null)
            return null;

        int ipAddress = wifiInfo.getIpAddress();
        StringBuilder sb = new StringBuilder();
        sb.append(ipAddress & 0xFF).append(".");
        sb.append((ipAddress >> 8) & 0xFF).append(".");
        sb.append((ipAddress >> 16) & 0xFF).append(".");
        sb.append((ipAddress >> 24) & 0xFF);

        return sb.toString();
    }

    /***
     * 获取网络类型
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getNetworkType()
    {
        String strNetworkType = "";
        NetworkInfo networkInfo = ((ConnectivityManager) MyApplication.getApplication().
                getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            {
                strNetworkType = "WIFI";
            }
            else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                String _strSubTypeName = networkInfo.getSubtypeName();

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000"))
                        {
                            strNetworkType = "3G";
                        }
                        else
                        {
                            strNetworkType = _strSubTypeName;
                        }
                        break;
                }
            }
        }
        return strNetworkType;
    }

}
