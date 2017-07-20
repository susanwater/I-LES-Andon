package com.haier.ledai.net;


import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.util.Log;

import com.framework.cache.CacheLoaderManager;
import com.framework.net.AsyncTask;
import com.framework.net.OkHttpClientManager;
import com.framework.net.OnGetBinListener;
import com.framework.util.JsonUtil;
import com.framework.util.async.AsyncControl;
import com.framework.widget.HRFrameLayout4Loading;
import com.haier.ledai.model.BaseResponseModel;
import com.haier.ledai.system.Preferences;
import com.haier.ledai.util.StringUtils;

import org.codehaus.jackson.type.TypeReference;

import java.util.Map;


public class HarNet {


	//static GetBinAsyncTask dtAsyncTask;

	public static void getBin(Context mthis, HRFrameLayout4Loading loading, Map map, String url,
							  LoadingType LoadingType, double cacheTime, OnGetBinListener obl
			) {
		GetBinAsyncTask dtAsyncTask = new GetBinAsyncTask(mthis,loading, url,map, LoadingType, cacheTime,
				obl);
		
		dtAsyncTask.execute();
		

	}
	
	/**
	 * 访问网络获取bin通用task
	 * 
	 * @author yl
	 * 
	 */
	public static class GetBinAsyncTask extends
			AsyncTask<String, Integer, String> {

		public String taskCode = "";
		ProgressDialog progressDialog;
		Builder mBuilder;
		String url;
		LoadingType loadingType = LoadingType.PAGELOADING;
		boolean isPost = true;
		boolean isSetRs;
		OnGetBinListener obl;
		HRFrameLayout4Loading loading;
		Context mthis;
		Map map;
		String bt;
		boolean fromLocal = false;
		long cacheTime = 0;
		private boolean isfromCache = false;
		private boolean isNoShowError = false;
		private String type;
		boolean isneedfeedback = true;
		private AsyncControl asyncControl;


		public GetBinAsyncTask(Context mthis, HRFrameLayout4Loading loading, String url, Map map,
                               LoadingType loadingType, double cacheTime,
                               OnGetBinListener obl) {
			
			this.loading = loading;
			this.map = map;
			this.cacheTime = Math.round(cacheTime);
			this.mthis = mthis;
			this.obl = obl;
			this.loadingType = loadingType;
			this.url = url;
		}

		public GetBinAsyncTask(Context mthis, String url, Map map,
                               LoadingType loadingType, double cacheTime,
                               AsyncControl asyncControl) {

			this.map = map;
			this.cacheTime = Math.round(cacheTime);
			this.mthis = mthis;
			this.asyncControl = asyncControl;
			this.loadingType = loadingType;
			this.url = url;

		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(final String result)
		{

			if (Preferences.DEBUG){
				try{
					Log.i("@#$%……*()---------","请求路径:"+"\n\t\t"+url+"\n"+"参数:"+"\n\t\t"+map.toString()+"\n"+"响应的原始报文:"+"\n\t\t\t"+result);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			// TODO Auto-generated method stub
			//super.onPostExecuteSafely(result, e);

			//先处理loading框
			if (loadingType == LoadingType.PAGELOADING) {

				if (loading!=null)
				{
					loading.hideLoadingView();
				}

			} else if (loadingType == LoadingType.SYSTEMLOADING) {
				
				//HRProgressDialog.dissmissDialog(progressDialog);

				if (progressDialog != null && progressDialog.getWindow() != null &&
						progressDialog.isShowing()) {
					try {
						progressDialog.dismiss();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}

				if(mthis !=null){
					if (progressDialog != null && progressDialog.getWindow() != null &&
							progressDialog.isShowing()) {
						try {
							progressDialog.dismiss();
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

			} else if (loadingType == LoadingType.NOLOADING) {
				
			}

			//处理网络或者其他各种反正系统错误
			if (result.contains("error:"))
			{
				if (loading!=null && loadingType== LoadingType.PAGELOADING) {
					loading.showExceptionView();
				}

				if (obl!=null){
					obl.onGetBinError(result);
				}
				if (asyncControl!=null){
					asyncControl.receiverBin(taskCode,result);
				}


			}
			else {
				BaseResponseModel mtr = JsonUtil.fromJson(result, new TypeReference<BaseResponseModel>() {});
				//BaseModel baseModel = JsonUtil.fromJson(result, new TypeReference<BaseModel>() {
				//});
				if (mtr!=null){


					if (cacheTime > 0 && !fromLocal ){
							CacheLoaderManager.getInstance().pushString(url, result, cacheTime);
					}

					//处理数据

					if (mthis!=null && obl!=null){

						obl.onGetFinish(result);
					}
					if (mthis!=null && asyncControl!=null){
						asyncControl.receiverBin(taskCode,result);
					}
				}

				else {
					if (loadingType == LoadingType.PAGELOADING){
						if(loading!=null){
							loading.showExceptionView();
						}
					}
				}
			}
			

			
		}
		
		@Override
		protected void onPreExecute() {
			
			if (mthis!=null){
				this.cancel(true);
				return;
			}
			
			if (loadingType == LoadingType.PAGELOADING)
			{
				if (loading!=null)
				{
					loading.setId(loading.getId()+(int) Math.round(Math.random() * 100));
					loading.showLoadingView();
				}
			}
			else if (loadingType == LoadingType.SYSTEMLOADING)
			{
				try {
					progressDialog = new ProgressDialog(mthis);
					progressDialog.setCanceledOnTouchOutside(false);
					progressDialog.setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialogInterface) {

							//if (dtAsyncTask!=null && !dtAsyncTask.isCancelled()) {
							GetBinAsyncTask.this.cancel(true);
							//}
						}
					});
					try {
						progressDialog.show();
					}catch (Exception e){
						e.printStackTrace();
					}
					//progressDialog = HRProgressDialog.show(mthis);
				}
				catch (Exception e){
					e.printStackTrace();
				}
			}
			else if (loadingType == LoadingType.NOLOADING) {
				
			}
			
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
		

		@Override
		protected String doInBackground(String... params) {
			if (cacheTime>0){



				bt = CacheLoaderManager.getInstance().loadString(url);



				fromLocal = true;


			}
			if (StringUtils.isEmpty(bt)){
				//bt = NetUtil.getSourceByPOST(mthis, url, map);
				long dd = System.currentTimeMillis();
				try {

					bt = OkHttpClientManager.postAsString(url, map);

					if (Preferences.DEBUG) {


					}
					fromLocal = false;
				}
				catch (Exception e){
					e.printStackTrace();
					return "error:"+e.toString();
				}


			}
			//返回统一的数据格式，在这里做检测



			return bt;
		}
		
		/**
		 * 出现错误后处理 by yangli
		 * 
		 * @param obl
		 * @param msg
		 */

	}


	
	
	
	
	
	
	

	
	/**
	 * 
	 * @author Admin
	 * 增加支付loading选项
	 */
	public static enum LoadingType {
		NOLOADING, PAGELOADING, SYSTEMLOADING,PAYLOADING
	}
	
}
