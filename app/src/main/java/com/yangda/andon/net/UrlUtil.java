package com.yangda.andon.net;

import android.content.Context;

import com.yangda.andon.R;
import com.yangda.andon.system.Preferences;


public class UrlUtil {

	public static int urlresid= R.string.server_url;
	static String url = "";
	public static String defineUrl = "";
	public static String getServiceUrl(Context mthis, int urlid){
		try {
			if (Preferences.DEBUG) {
				url = mthis.getResources().getString(R.string.server_test_url) + mthis.getResources().getString(urlid);
			} else {
				url = mthis.getResources().getString(urlresid) + mthis.getResources().getString(urlid);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return url;
	}
}
