/**
 * 
 */
package framework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.haier.ledai.R;

public class HRFrameLayout4Loading extends FrameLayout {

	/*** Show Empty View if No Datas */
	public static final int ViewType_EmptyView = -1;
	/*** 请求中 */
	public static final int ViewType_Loading = 0;
	/*** 加载异常, 在无合适的异常类型使用的情况下使用此异常 */
	public static final int ViewType_DefaultException = 1;
	/*** 除无网络和网络超时意外的网络异常 */
	public static final int ViewType_DefaultNetworkException = 2;
	/*** 数据异常, 服务器返回数据有误(比如反序列化失败) */
	public static final int ViewType_DefaultDataException = 3;
	/*** 无网络 */
	public static final int ViewType_NoNetwork = 4;
	/*** 网络超时 */
	public static final int ViewType_Timeout = 5;
	/*** 无数据, 服务器返回空结果 */
	public static final int ViewType_NoData = 6;

	/** 服务端返回的常见错误 */
	public static final int EXP_FROM_SERVER_COMMON = 10001;
	/** 无法连接到网络，请检查网络配置 */
	public static final int EXP_NETWORK_NOTAVAILABLE = 90001;
	/** 网络不给力，请您重试 */
	public static final int EXP_NETWORK_NOGOOD = 90002;
	/** 超时了，请您重试 */
	public static final int EXP_REQUEST_TIMEOUT = 90003;
	/** 出错啦，请您重试 */
	public static final int EXP_SERVICE_FAIL = 90004;
	/** 获取价格出错啦。请稍后重试 */
	public static final int EXP_ILLEGAL_PRICE = 90005;
	/** 服务调用出错，但不带重试按钮只显示信息 */
	public static final int EXP_SERVICE_FAIL_INFO = 150101;
	/** 没有找到相关服务 */
	public static final int EXP_NO_SUCH_BUSINESS = 91002;
	
	private static Animation loadingAnim;

	private SparseArray<Integer> defaultLayout = new SparseArray<Integer>(7);
	public SparseArray<View> cachedLayout = new SparseArray<View>(7);

	private LayoutInflater mInflater;

	private OnClickListener mRefreshClickListener;
	
	

	private ImageView icon;
	private TextView tip;
	private TextView subTip;
	private boolean viewUp = false;

	private Context context;

	public boolean isViewUp() {
		return viewUp;
	}

	public void setViewUp(boolean viewUp) {
		this.viewUp = viewUp;
	}

	// loading animation
	private ImageView loadingIv;

	public HRFrameLayout4Loading(Context context) {
		super(context);
		init(context, null);
	}

	public HRFrameLayout4Loading(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);

	}

	public HRFrameLayout4Loading(Context context, AttributeSet attrs,
								 int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {

		this.context = context;

		if (isInEditMode())
			return;

		
		if (loadingAnim==null){
			loadingAnim = AnimationUtils.loadAnimation(context, R.anim.round_loading);
		}
		
		mInflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.FrameLayout4Loading);
			if (a != null) {
				setDefaultView(ViewType_Loading, a.getResourceId(
						R.styleable.FrameLayout4Loading_loadingView,
						R.layout.hr_common_loading_indicator));
				setDefaultView(ViewType_DefaultException, a.getResourceId(
						R.styleable.FrameLayout4Loading_defaultExceptionView,
						R.layout.hr_common_error_default_style));

				setDefaultView(ViewType_EmptyView,
						R.layout.hr_common_empty_view);

				a.recycle();
			}
		}
	}

	public void setDefaultView(int viewType, int resLayout) {
		defaultLayout.put(viewType, resLayout);
	}

	public int getDefaultViewResId(int viewType) {
		return defaultLayout.get(viewType);
	}

	public void showView(int viewType, Drawable background) {
		int count = defaultLayout.size();
		for (int i = 0; i < count; i++) {
			int key = defaultLayout.keyAt(i);
			if (key == viewType) {
				doShowView(key, background);
			} else {
				hideView(key, background);
			}
		}
	}

	private void hideView(int viewType, Drawable background) {
		View view = cachedLayout.get(viewType);

		if (view == null)
			return;
		if (background != null) {
			view.setBackground(background);
		}
		view.setVisibility(GONE);
	}

	public void doShowNoData(Drawable background,String msg) {
		doShowNoData(background,msg,null);
	}




	public void doShowNoData(Drawable background,String msg,OnClickListener click) {
		int resLayoutId = defaultLayout.get(ViewType_EmptyView);
		if (resLayoutId <= 0)
			throw new IllegalStateException("layout is not set for " + ViewType_EmptyView);

//		View view = null;
//		if (click==null){
//			view = cachedLayout.get(ViewType_EmptyView);
//		}

		View view = cachedLayout.get(ViewType_EmptyView);

		if (view == null || click != null) {
			view = mInflater.inflate(resLayoutId, null);
			try {
				ImageView image = (ImageView)view.findViewById(R.id.empty_icon_iv);

				image.setBackground(background);

				TextView empText = (TextView) view
						.findViewById(R.id.empty_tip_tv);
				empText.setText(msg);

				if (click!=null){

					Button empty_btn = (Button)view.findViewById(R.id.empty_btn);
					empty_btn.setVisibility(View.VISIBLE);
					empty_btn.setOnClickListener(click);
					if ("暂无交易记录\n查看收益发放规则".equals(msg)){
						empty_btn.setText("查看规则");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}


			//cachedLayout.put(ViewType_EmptyView, view);
			addView(view, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
					Gravity.CENTER));
			initView(view, ViewType_EmptyView);
			initListener(view);
		}

		view.setVisibility(VISIBLE);
		view.bringToFront();
	}

	public void doShowNoData(Drawable background,String msg,String title,OnClickListener click) {
		int resLayoutId = defaultLayout.get(ViewType_EmptyView);
		if (resLayoutId <= 0)
			throw new IllegalStateException("layout is not set for " + ViewType_EmptyView);

//		View view = null;
//		if (click==null){
//			view = cachedLayout.get(ViewType_EmptyView);
//		}

		View view = cachedLayout.get(ViewType_EmptyView);

		if (view == null || click != null) {
			view = mInflater.inflate(resLayoutId, null);
			try {
				ImageView image = (ImageView)view.findViewById(R.id.empty_icon_iv);

				image.setBackground(background);

				TextView empText = (TextView) view
						.findViewById(R.id.empty_tip_tv);
				empText.setText(msg);

				if (click!=null){

					Button empty_btn = (Button)view.findViewById(R.id.empty_btn);
					empty_btn.setVisibility(View.VISIBLE);
					empty_btn.setOnClickListener(click);
					empty_btn.setText(title);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}


			//cachedLayout.put(ViewType_EmptyView, view);
			addView(view, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
					Gravity.CENTER));
			initView(view, ViewType_EmptyView);
			initListener(view);
		}

		view.setVisibility(VISIBLE);
		view.bringToFront();
	}


	/**
	 *    空页面 下部是蓝色 按钮
	 * @param background
	 * @param msg
	 * @param title
	 * @param buttonColor
     * @param click
     */
	public void doShowNoData(Drawable background,String msg,String title,int buttonColor,OnClickListener click) {
		int resLayoutId = defaultLayout.get(ViewType_EmptyView);
		if (resLayoutId <= 0)
			throw new IllegalStateException("layout is not set for " + ViewType_EmptyView);

//		View view = null;
//		if (click==null){
//			view = cachedLayout.get(ViewType_EmptyView);
//		}

		View view = cachedLayout.get(ViewType_EmptyView);

		if (view == null || click != null) {
			view = mInflater.inflate(resLayoutId, null);
			try {
				ImageView image = (ImageView)view.findViewById(R.id.empty_icon_iv);

				image.setBackground(background);

				TextView empText = (TextView) view
						.findViewById(R.id.empty_tip_tv);
				empText.setText(msg);

				if (click!=null){

					Button empty_btn = (Button)view.findViewById(R.id.empty_btn);
					empty_btn.setVisibility(View.VISIBLE);
					empty_btn.setOnClickListener(click);
					empty_btn.setText(title);
					empty_btn.setTextColor(buttonColor);
					empty_btn.setBackgroundResource(R.drawable.rectangle_corner_empty_bule2);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}


			//cachedLayout.put(ViewType_EmptyView, view);
			addView(view, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
					Gravity.CENTER));
			initView(view, ViewType_EmptyView);
			initListener(view);
		}

		view.setVisibility(VISIBLE);
		view.bringToFront();
	}



	private void doShowView(int viewType, Drawable background) {
		int resLayoutId = defaultLayout.get(viewType);
		if (resLayoutId <= 0)
			throw new IllegalStateException("layout is not set for " + viewType);

		View view = cachedLayout.get(viewType);

		if (view == null) {
			view = mInflater.inflate(resLayoutId, null);
			if (viewType == ViewType_DefaultDataException
					|| viewType == ViewType_DefaultException
					|| viewType == ViewType_Timeout
					|| viewType == ViewType_DefaultNetworkException) {
				try {
					TextView errorText = (TextView) view
							.findViewById(R.id.loading_error_text);
					errorText.setText("网络不给力啊");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (background != null) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					view.setBackground(background);
			    } else {
			    	view.setBackgroundDrawable(background);
			    }
			}
			cachedLayout.put(viewType, view);
			addView(view, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
					Gravity.CENTER));
			initView(view, viewType);
			initListener(view);
		}

		view.setVisibility(VISIBLE);
		view.bringToFront();
	}

	private void initView(View v, int viewType) {

		if (ViewType_EmptyView == viewType) {
			icon = (ImageView) v.findViewById(R.id.empty_icon_iv);
			tip = (TextView) v.findViewById(R.id.empty_tip_tv);
			subTip = (TextView) v.findViewById(R.id.empty_sub_tip_tv);
		} else if (ViewType_Loading == viewType) {
			loadingIv = (ImageView) v.findViewById(R.id.loading_iv);
//			AnimationDrawable animationDrawable = (AnimationDrawable) loadingIv
//					.getDrawable();
//			animationDrawable.start();
			loadingIv.startAnimation(loadingAnim);
		}
	}

	private void initListener(View view) {
		View refreshBtn = view.findViewById(R.id.loading_refreash_btn);
		if (refreshBtn != null && mRefreshClickListener != null) {
			refreshBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mRefreshClickListener.onClick(v);
				}
			});
		}
	}

	public void showLoadingView() {
		
		showLoadingView(null);
	}

	public void showLoadingView(int backgroundId) {
		showLoadingView(getResources().getDrawable(backgroundId));
	}

	public void showLoadingView(Drawable background) {
		showView(ViewType_Loading, background);
	}

	public void hideLoadingView() {
		hideLoadingView(null);
	}

	public void hideLoadingView(int backgroundId) {
		hideLoadingView(getResources().getDrawable(backgroundId));
	}

	public void hideLoadingView(Drawable background) {
		hideView(ViewType_Loading, background);
	}

	public void showNoDataView(Drawable background) {
		showView(ViewType_NoData, background);
	}

	public void showNoDataView(int backgroundId) {
		showNoDataView(getResources().getDrawable(backgroundId));
	}

	public void showNoDataView() {
		showNoDataView(null);
	}

	public void showExceptionView() {
		showExceptionView(1);
	}

	public void showEmptyView() {
		showEmptyView(null);
	}

	public void showEmptyView(int backgroundId) {
		showEmptyView(getResources().getDrawable(backgroundId));
	}

	public void showEmptyView(Drawable background) {
		showView(ViewType_EmptyView, background);
	}

	public void showExceptionView(int errorCode) {
		showExceptionView(getViewTypeByErrorCode(errorCode), null);
	}

	public void showExceptionView(int errorCode, int backgroundId) {
		showExceptionView(errorCode, getResources().getDrawable(backgroundId));
	}

	public void showExceptionView(int errorCode, Drawable background) {
		showView(getViewTypeByErrorCode(errorCode), background);
	}

	public void setExceptionViewIconVisibility(int visibility) {
		View view = cachedLayout.get(ViewType_DefaultException);
		view.findViewById(R.id.listview_error_pic).setVisibility(visibility);
	}

	public <T> HRFrameLayout4Loading setEmptyViewIcon(T obj) {
		if (obj == null || icon == null)
			return null;
		if (obj instanceof Integer)
			icon.setImageResource((Integer) obj);
		else
			ViewCompat.setBackground(icon, (Drawable) obj);
		return this;
	}

	public <T> HRFrameLayout4Loading setEmptyViewTip(T obj) {
		if (obj == null || tip == null)
			return null;
		if (obj instanceof Integer)
			((TextView) tip).setText(getResources().getString((Integer) obj));
		else if (obj instanceof CharSequence)
			((TextView) tip).setText((CharSequence) obj);
		return this;
	}

	public <T> HRFrameLayout4Loading setEmptyViewTip(T obj, T obj1) {
		if (obj == null || tip == null)
			return null;

		if (obj instanceof Integer)
			((TextView) tip).setText(getResources().getString((Integer) obj));
		else if (obj instanceof CharSequence)
			((TextView) tip).setText((CharSequence) obj);

		if (obj1 == null || subTip == null) {
			return null;
		}

		subTip.setVisibility(View.VISIBLE);
		if (obj1 instanceof Integer)
			((TextView) subTip).setText(getResources()
					.getString((Integer) obj1));
		else if (obj instanceof CharSequence)
			((TextView) subTip).setText((CharSequence) obj1);

		return this;
	}

	public void hideAllMask() {
		hideAllMask(null);
	}

	public void hideAllMask(int backgroundId) {
		hideAllMask(getResources().getDrawable(backgroundId));
	}

	public void hideAllMask(Drawable background) {
		int count = cachedLayout.size();
		for (int i = 0; i < count; i++) {
			int key = cachedLayout.keyAt(i);
			hideView(key, background);
		}
	}

	private static int getViewTypeByErrorCode(int errorCode) {
		switch (errorCode) {
			case EXP_NETWORK_NOTAVAILABLE :
				return ViewType_NoNetwork;

			case EXP_NETWORK_NOGOOD :
				return ViewType_DefaultNetworkException;

			case EXP_REQUEST_TIMEOUT :
				return ViewType_Timeout;

			case EXP_SERVICE_FAIL :
				return ViewType_DefaultDataException;

			case EXP_FROM_SERVER_COMMON :
			case EXP_SERVICE_FAIL_INFO :
			default :
				return ViewType_DefaultException;
		}
	}

	public void setRefreashClickListener(OnClickListener listener) {
		mRefreshClickListener = listener;
	}

}
