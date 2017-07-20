/**
 * 
 */
package com.framework.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.framework.util.DeviceHelper;
import com.haier.ledai.R;
import com.haier.ledai.ui.base.BaseActivity;

public class HRTitleView extends RelativeLayout implements View.OnClickListener{

	// ===========================================================
	// Constants
	// ===========================================================
	private static final int Left_Btn_View_Id = 0x1001;
	private static final int Title_View_Id = 0x1002;
	private static final int Right_Btn_View_Id = 0x1003;
	private static final int DEFAULT_TITLE_HEIGHT = DeviceHelper.dp2pix(50);
	private static final int DEFAULT_ICON_WIDTH = DeviceHelper.dp2pix(40);
	private static final int DEFAULT_ICON_HEIGHT = DeviceHelper.dp2pix(50);
	private static final int DEFAULT_ICON_PADDING = DeviceHelper.dp2pix(16);
	
	private static final int TITLE_TEXT_DEFAULT_STYLE = R.style.hr_text_18_white;
	private static final int BTN_TEXT_DEFAULT_STYLE = R.style.hr_text_14_white;
	private static final int BTN_TEXT_SMALL_DEFAULT_STYLE = R.style.hr_text_12_white;
	// ===========================================================
	// Fields
	// ===========================================================
	private TextView mTitleTextView;
	private TextView mSubTitleTextView;
	private String mTitleText;
	private int mTitleStyle;
	
	private View mLeftBtnTextView;
	private View mLeftBtnImgView;
	private String mLeftBtnText;
	private Drawable mLBtnDrawable;
	private int mLBtnStyle;
	private boolean isShowLBtn;
	
	private View mRightBtnTextView;
	private View mRightBtnImgView;
	private String mRightBtnText;
	private Drawable mRBtnBgDrawable;
	private int mRBtnWidth;
	private int mRBtnHeigth;
	private int mRBtnStyle;
	private boolean isShowRBtn;
	
	private OnTitleClickListener mOnTitleClickListener;
	private OnRightBtnClickListener mOnRightBtnClickListener;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	public HRTitleView(Context context){
		this(context,null);
	}
	
	public HRTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context,attrs);
		setupView(context);
	}
	
	private void initView(Context context, AttributeSet attrs) {
		if (attrs != null){
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HRTitleView);
			mTitleText = a.getString(R.styleable.HRTitleView_title_text);
			mTitleStyle = a.getResourceId(R.styleable.HRTitleView_title_text_appearance, -1);
			
			isShowLBtn = a.getBoolean(R.styleable.HRTitleView_is_show_left_button, true);
			if(isShowLBtn){
				mLeftBtnText = a.getString(R.styleable.HRTitleView_title_btn_left_text);
				mLBtnStyle = a.getResourceId(R.styleable.HRTitleView_title_btn_left_text_appearance, -1);
				mLBtnDrawable = a.getDrawable(R.styleable.HRTitleView_title_btn_left_drawable);
				if(mLBtnDrawable == null)
					mLBtnDrawable = getResources().getDrawable(R.mipmap.nav_back);
			}
			
			isShowRBtn = a.getBoolean(R.styleable.HRTitleView_is_show_right_button, true);
			if(isShowRBtn){
				mRightBtnText = a.getString(R.styleable.HRTitleView_title_btn_right_text);
				mRBtnStyle = a.getResourceId(R.styleable.HRTitleView_title_btn_right_text_appearance, -1);
				mRBtnBgDrawable = a.getDrawable(R.styleable.HRTitleView_title_btn_right_drawable);
				mRBtnWidth = a.getDimensionPixelSize(R.styleable.HRTitleView_title_btn_right_width,DEFAULT_ICON_WIDTH);
				mRBtnHeigth = a.getDimensionPixelSize(R.styleable.HRTitleView_title_btn_right_height,DEFAULT_ICON_HEIGHT);
			}
			
			a.recycle();
			if (getBackground() == null) {
				setBackgroundResource(R.color.title_bg);
			}
		}
	}
	

	private FrameLayout.LayoutParams getDrawableViewLayoutParams(View view, Drawable drawable, int width, int heigth) {
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width,heigth);

		if (drawable != null) {
			int w = drawable.getIntrinsicWidth();
			int h = drawable.getIntrinsicHeight();

			int v_w = width - DEFAULT_ICON_PADDING;
			int v_h = heigth - DEFAULT_ICON_PADDING;

			float scale = w/(float)h;
			float v_scale = v_w/(float)v_h;
			if (scale >= v_scale) {
				v_h = (int)(v_w/scale);
			}
			else {
				v_w = (int)(v_h*scale);
			}

			lp.width = v_w;
			lp.height = v_h;
		}
		return lp;
	}


	LayoutParams parms;

	private void setupView(Context context) {
		int titleHight;
		LayoutParams lps;
		
		/**new sub title view*/
		TextView subTitleView = new TextView(context);
		this.mSubTitleTextView = subTitleView;
		subTitleView.setVisibility(View.GONE);
		//subTitleView.setTextColor(Color.parseColor("#444444"));
		//subTitleView.setTextSize(13);
		subTitleView.setSingleLine();
		subTitleView.setGravity(Gravity.CENTER);
		subTitleView.setEllipsize(TextUtils.TruncateAt.END);
		subTitleView.setClickable(false);
		subTitleView.setText("Test subTitleView");
		subTitleView.setTextAppearance(context, BTN_TEXT_SMALL_DEFAULT_STYLE);
		
		/**new title view*/
		TextView titleView = new TextView(context);
		this.mTitleTextView = titleView;
		titleView.setId(Title_View_Id);
		titleView.setGravity(Gravity.CENTER);
		titleView.setEllipsize(TextUtils.TruncateAt.END);
		titleView.setSingleLine();
		titleView.setClickable(true);

		if(mTitleStyle != -1)
			titleView.setTextAppearance(context, mTitleStyle);
		else
			titleView.setTextAppearance(context, TITLE_TEXT_DEFAULT_STYLE);
		
		if (!TextUtils.isEmpty(mTitleText)){
			titleView.setText(mTitleText);
		}
		
		LinearLayout mLinearLayout = new LinearLayout(context);
		mLinearLayout.setOrientation(LinearLayout.VERTICAL);
		mLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

		//LayoutParams parms = new LayoutParams(LayoutParams.MATCH_PARENT, DEFAULT_TITLE_HEIGHT);

		parms = new LayoutParams(LayoutParams.MATCH_PARENT, DEFAULT_TITLE_HEIGHT);

		//parms.addRule(RelativeLayout.RIGHT_OF, Left_Btn_View_Id);
		//parms.addRule(RelativeLayout.LEFT_OF, Right_Btn_View_Id);
		parms.addRule(RelativeLayout.RIGHT_OF, 0);
		parms.addRule(RelativeLayout.LEFT_OF,0);
		parms.setMargins(80,0,80,0);
		mLinearLayout.setLayoutParams(parms);

		/**titleView layoutParams*/
		if(View.VISIBLE == subTitleView.getVisibility())
			titleHight = LayoutParams.WRAP_CONTENT;
		else
			titleHight = DEFAULT_TITLE_HEIGHT;
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, titleHight);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		mLinearLayout.addView(titleView, lp);
		
		/**subTitleView layoutParams*/
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(BELOW, Title_View_Id);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		mLinearLayout.addView(subTitleView, lp);
		
		addView(mLinearLayout);
		
		/**Add left button view*/
		if(isShowLBtn){
			if (TextUtils.isEmpty(mLeftBtnText)){
				mLeftBtnImgView = new View(context);
				ViewCompat.setBackground(mLeftBtnImgView, mLBtnDrawable);

				FrameLayout containerLayout = new FrameLayout(context);
				FrameLayout.LayoutParams params = getDrawableViewLayoutParams(mLeftBtnImgView, mLBtnDrawable,DEFAULT_ICON_WIDTH, DEFAULT_ICON_HEIGHT);
				params.gravity = Gravity.CENTER| Gravity.START;
				containerLayout.addView(mLeftBtnImgView, params);
				this.mLeftBtnTextView = containerLayout;

				 lps = new LayoutParams(DEFAULT_ICON_WIDTH, DEFAULT_ICON_HEIGHT);

			}else{
				TextView leftBtnView = new TextView(context);
				leftBtnView.setGravity(Gravity.CENTER| Gravity.START);
				leftBtnView.setSingleLine(true);
				leftBtnView.setEllipsize(TextUtils.TruncateAt.END);
				leftBtnView.setText(mLeftBtnText);
				if (mLBtnStyle != -1){
					leftBtnView.setTextAppearance(context, mLBtnStyle);
				} else {
					leftBtnView.setTextAppearance(context, BTN_TEXT_DEFAULT_STYLE);
				}
				this.mLeftBtnTextView = leftBtnView;

				 lps = new LayoutParams(LayoutParams.WRAP_CONTENT, DEFAULT_TITLE_HEIGHT);
			}

			this.mLeftBtnTextView.setId(Left_Btn_View_Id);
			this.mLeftBtnTextView.setClickable(true);
			this.mLeftBtnTextView.setOnClickListener(this);

			lps.setMargins(DeviceHelper.dp2pix(10), 0, 0, 0);
			lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
			lps.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			addView(mLeftBtnTextView, lps);
		}
		else {
			this.mLeftBtnTextView = new FrameLayout(context);
			lps = new LayoutParams(DEFAULT_ICON_WIDTH, DEFAULT_ICON_HEIGHT);
			this.mLeftBtnTextView.setId(Left_Btn_View_Id);
			this.mLeftBtnTextView.setClickable(false);

			lps.setMargins(DeviceHelper.dp2pix(10), 0, 0, 0);
			lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
			lps.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			addView(mLeftBtnTextView, lps);
		}

		/**Add right button view*/
		LayoutParams p;
		if(isShowRBtn){
			if(TextUtils.isEmpty(mRightBtnText)){
				mRightBtnImgView = new View(context);
				ViewCompat.setBackground(mRightBtnImgView, mRBtnBgDrawable);

				FrameLayout containerLayout = new FrameLayout(context);
				FrameLayout.LayoutParams subLp = getDrawableViewLayoutParams(mRightBtnImgView, mRBtnBgDrawable, mRBtnWidth, mRBtnHeigth);
				subLp.gravity = Gravity.CENTER| Gravity.END;
				containerLayout.addView(mRightBtnImgView, subLp);
				this.mRightBtnTextView = containerLayout;

				p = new LayoutParams(mRBtnWidth, mRBtnHeigth);
			}else{
				TextView rightBtnView = new TextView(getContext());
				rightBtnView.setGravity(Gravity.CENTER | Gravity.END);
				rightBtnView.setSingleLine(true);
				rightBtnView.setEllipsize(TextUtils.TruncateAt.END);
				rightBtnView.setText(mRightBtnText);
				if (mRBtnStyle != -1){
					rightBtnView.setTextAppearance(context, mRBtnStyle);
				} else {
					rightBtnView.setTextAppearance(context, BTN_TEXT_DEFAULT_STYLE);
				}

				this.mRightBtnTextView = rightBtnView;

				p = new LayoutParams(LayoutParams.WRAP_CONTENT, DEFAULT_TITLE_HEIGHT);
			}

			mRightBtnTextView.setId(Right_Btn_View_Id);
			mRightBtnTextView.setClickable(true);
			mRightBtnTextView.setOnClickListener(this);

			p.setMargins(0, 0, DeviceHelper.dp2pix(10), 0);
			p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
			p.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			addView(mRightBtnTextView, p);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case Title_View_Id:
				if (mOnTitleClickListener != null)
					mOnTitleClickListener.onTitleClick(v);
				break;
			case Left_Btn_View_Id:
				try {


					sendKeyBackEvent();
				}
				catch (Exception e){

				}
				break;
			case Right_Btn_View_Id:
				if (mOnRightBtnClickListener != null)
					mOnRightBtnClickListener.onRBtnClick(v);
				break;
			default:
				break;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public interface OnTitleClickListener {
		void onTitleClick(View v);
	}

	public interface OnRightBtnClickListener {
		void onRBtnClick(View v);
	}

	public void setOnTitleClickListener(OnTitleClickListener listener) {
		mOnTitleClickListener = listener;
	}

	public void setOnRightBtnClickListener(OnRightBtnClickListener listener) {
		mOnRightBtnClickListener = listener;
	}

	public <T> void setTitleText(T obj) {
		if(obj == null)
			return;
		if(obj instanceof Integer)
			mTitleTextView.setText(getResources().getString((Integer) obj));
		else if(obj instanceof CharSequence)
			mTitleTextView.setText((CharSequence)obj);

	}

	public void centerTitle() {
		View titleView = (View)mTitleTextView.getParent();
		LayoutParams lp = (LayoutParams)titleView.getLayoutParams();
		lp.addRule(RelativeLayout.RIGHT_OF, 0);
		lp.addRule(RelativeLayout.LEFT_OF, 0);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		titleView.setLayoutParams(lp);
	}

	public <T> void setRightBtnText(T obj) {
		if(obj == null)
			return;
		if(obj instanceof Integer)
			((TextView)mRightBtnTextView).setText(getResources().getString((Integer) obj));
		else if(obj instanceof CharSequence)
			((TextView)mRightBtnTextView).setText((CharSequence)obj);
	}

	public View getRBtnView(){
		return mRightBtnTextView;
	}

	public View getRBtnImgView(){
		return mRightBtnImgView;
	}

	public <T> void setRightBtnDrawable(T obj){
		if(obj == null  || mRightBtnImgView == null)
			return;
		if(obj instanceof Integer) {
			//mRightBtnImgView.setBackgroundResource((Integer) obj);

			mRBtnBgDrawable = getResources().getDrawable((Integer) obj);
			ViewCompat.setBackground(mRightBtnImgView, mRBtnBgDrawable);

			FrameLayout.LayoutParams subLp = getDrawableViewLayoutParams(mRightBtnImgView, mRBtnBgDrawable, mRBtnWidth, mRBtnHeigth);
			subLp.gravity = Gravity.CENTER| Gravity.END;
			mRightBtnImgView.setLayoutParams(subLp);
		}
		else
			ViewCompat.setBackground(mRightBtnImgView, (Drawable)obj);
	}

	public void showLeftView(boolean show) {
		if (mLeftBtnTextView == null) {
			return;
		}

		if (show) {
			mLeftBtnTextView.setVisibility(View.VISIBLE);
		}
		else {
			mLeftBtnTextView.setVisibility(View.GONE);
		}
	}



	public <T> void setLeftBtnText(T obj) {
		if(obj == null)
			return;
		if(obj instanceof Integer)
			((TextView)mLeftBtnTextView).setText(getResources().getString((Integer) obj));
		else if(obj instanceof CharSequence)
			((TextView)mLeftBtnTextView).setText((CharSequence)obj);
	}

	public <T> void setLeftBtnDrawable(T obj){
		if(obj == null || mLeftBtnImgView == null)
			return;
		if(obj instanceof Integer)
			mLeftBtnImgView.setBackgroundResource((Integer) obj);
		else
			ViewCompat.setBackground(mLeftBtnImgView, (Drawable)obj);
	}
	public void setLeftBtnVisibility(int visibility){
		mLeftBtnImgView.setVisibility(visibility);
	}
	
	public <T> void setSubTitleText(T obj){
		parms.setMargins(80,10,80,0);
		if(obj == null)
			return;
		if(obj instanceof Integer)
			((TextView)mSubTitleTextView).setText(getResources().getString((Integer) obj));
		else if(obj instanceof CharSequence)
			((TextView)mSubTitleTextView).setText((CharSequence)obj);
		setSubTitleVisibility(View.VISIBLE);
	}
	
	public void setSubTitleVisibility(int visibility){
		mSubTitleTextView.setVisibility(visibility);
		ViewGroup.LayoutParams p = mTitleTextView.getLayoutParams();
		if(visibility == View.GONE){
			p.height = DEFAULT_TITLE_HEIGHT;
		}else if(visibility == View.VISIBLE){
			p.height = LayoutParams.WRAP_CONTENT;
		}
	}
	
	private <T> void sendKeyBackEvent() {
		final Context context = getContext();
		if (context instanceof BaseActivity) {
//			if(context instanceof AcceleratorActivity){
//				((BaseActivity) context).setResult(Activity.RESULT_OK);
//			}
			((BaseActivity) context).onBackPressed();
		} else if (context instanceof Activity) {
			((Activity) context).onBackPressed();
		}
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
