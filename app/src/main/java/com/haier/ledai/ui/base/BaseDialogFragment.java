package com.haier.ledai.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.haier.ledai.R;
import com.haier.ledai.util.CheckDoubleClick;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 17/6/1.
 */
public abstract class BaseDialogFragment extends DialogFragment
        implements
        View.OnClickListener,
        AdapterView.OnItemClickListener,
        FragmentResultCallback {


    protected String PageCode = "";

    private Bundle bundle;
    /** 返回监听 */
    private FragmentResultCallback callback;
    /** 打开下一个activity的请求码 */
    private int mRequestCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true
        setCancelable(true);
        // //
        // 可以设置dialog的显示风格，如style为STYLE_NO_TITLE，将被显示title。遗憾的是，我没有在DialogFragment中找到设置title内容的方法。theme为0，表示由系统选择合适的theme。
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            mRequestCode = mBundle.getInt("requestCode", 0);
        }
        return doOnCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    /**
     * 根据id查找view
     *
     * @param viewId
     * @return
     * @see [类、类#方法、类#成员]
     */
    public View findViewById(int viewId) {
        if (getView() != null) {
            return getView().findViewById(viewId);
        }
        return null;
    }

    /**
     *
     * 创建 view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     * @see [类、类#方法、类#成员]
     */
    public abstract View doOnCreateView(LayoutInflater inflater,
                                        ViewGroup container, Bundle savedInstanceState);

    /**
     * 在viewpager中第一次初始化view后时才会调用(用于初始化控件)
     *
     * @see [类、类#方法、类#成员]
     */
    public abstract void initView();

    @Override
    public void onClick(View v) {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        doOnClick(v);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        doOnItemClick(parent, view, position, id);
    }

    /**
     *
     * onclick点击事件监听（子类不要需重写onclick）
     *
     * @see [类、类#方法、类#成员]
     */
    public void doOnClick(View v) {

    }

    /***
     * 列表项选择监听
     *
     * @param parent
     * @param view
     * @param position
     *            当前item位置
     * @param id
     *            item在listView中的相对位置
     * @see [类、类#方法、类#成员]
     */
    public void doOnItemClick(AdapterView<?> parent, View view, int position,
                              long id) {

    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {

    }

    /***
     * 显示
     *
     * @param activity
     * @param tag
     * @see [类、类#方法、类#成员]
     */
    public void showAllowingStateLoss(FragmentActivity activity, String tag) {
        if (activity == null || activity.isFinishing()) {

            return;
        }
        try {
            FragmentTransaction ft = activity.getSupportFragmentManager()
                    .beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (Exception e) {

        }
    }

    /***
     * 显示
     *
     * @param activity
     * @see [类、类#方法、类#成员]
     */
    public void showAllowingStateLoss(FragmentActivity activity) {
        if (activity == null || activity.isFinishing()) {

            return;
        }
        try {
            FragmentTransaction ft = activity.getSupportFragmentManager()
                    .beginTransaction();
            ft.add(this, getClass().getName());
            ft.commitAllowingStateLoss();
        } catch (Exception e) {

        }
    }

    /***
     * fragemnt值回传
     *
     * @param resultCode
     * @param intent
     * @see [类、类#方法、类#成员]
     */
    public void fragmentCallbackResult(int resultCode, Intent intent) {
        if (callback != null) {
            callback.onFragmentResult(mRequestCode, resultCode, intent);
        }
    }

    /**
     * 设置副fragment回调接口
     *
     * @param callback
     * @see [类、类#方法、类#成员]
     */
    protected void setFragmentResultCallback(FragmentResultCallback callback) {
        this.callback = callback;
    }

    public void dismiss(int resultCode) {
        super.dismiss();
        fragmentCallbackResult(resultCode, null);
    }

    public void dismiss(int resultCode, Intent intent) {
        super.dismiss();
        fragmentCallbackResult(resultCode, intent);
    }

    public void dismissAllowingStateLoss(int resultCode) {
        super.dismissAllowingStateLoss();
        fragmentCallbackResult(resultCode, null);
    }

    public void dismissAllowingStateLoss(int resultCode, Intent intent) {
        super.dismissAllowingStateLoss();
        fragmentCallbackResult(resultCode, intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null)
            getView().setClickable(true);



    }

    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 添加要接收传值
     *
     * @param key
     * @param value
     *            值
     * @see [类、类#方法、类#成员]
     */
    public void addValues(String key, Object value) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        if (value instanceof Boolean) {
            bundle.putBoolean(key, (Boolean) value);
        } else if (value instanceof Byte) {
            bundle.putByte(key, (Byte) value);
        } else if (value instanceof Character) {
            bundle.putChar(key, (Character) value);
        } else if (value instanceof Short) {
            bundle.putShort(key, (Short) value);
        } else if (value instanceof Integer) {
            bundle.putInt(key, (Integer) value);
        } else if (value instanceof Integer) {
            bundle.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            bundle.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            bundle.putFloat(key, (Float) value);
        } else if (value instanceof Double) {
            bundle.putDouble(key, (Double) value);
        } else if (value instanceof String) {
            bundle.putString(key, (String) value);
        } else if (value instanceof CharSequence) {
            bundle.putCharSequence(key, (CharSequence) value);
        } else if (value instanceof Serializable) {
            bundle.putSerializable(key, (Serializable) value);
        } else if (value instanceof Parcelable) {
            bundle.putParcelable(key, (Parcelable) value);
        } else if (value instanceof ArrayList) {
            ArrayList list = (ArrayList) value;
            if (list.get(0) instanceof Parcelable) {
                bundle.putParcelableArrayList(key, list);
            } else if (list.get(0) instanceof CharSequence) {
                bundle.putCharSequenceArrayList(key, list);
            } else if (list.get(0) instanceof Integer) {
                bundle.putIntegerArrayList(key, list);
            } else if (list.get(0) instanceof String) {
                bundle.putStringArrayList(key, list);
            } else {
                new Exception("传的参数错误");
            }
        } else if (value instanceof SparseArray) {
            if (((SparseArray) value).get(0) instanceof Parcelable) {
                bundle.putSparseParcelableArray(key, (SparseArray) value);
            } else {
                new Exception("传的参数错误");
            }
        } else if (value instanceof Parcelable[]) {
            bundle.putParcelableArray(key, (Parcelable[]) value);
        } else if (value instanceof boolean[]) {
            bundle.putBooleanArray(key, (boolean[]) value);
        } else if (value instanceof byte[]) {
            bundle.putByteArray(key, (byte[]) value);
        } else if (value instanceof char[]) {
            bundle.putCharArray(key, (char[]) value);
        } else if (value instanceof CharSequence[]) {
            bundle.putCharSequenceArray(key, (CharSequence[]) value);
        } else if (value instanceof double[]) {
            bundle.putDoubleArray(key, (double[]) value);
        } else if (value instanceof float[]) {
            bundle.putFloatArray(key, (float[]) value);
        } else if (value instanceof int[]) {
            bundle.putIntArray(key, (int[]) value);
        } else if (value instanceof long[]) {
            bundle.putLongArray(key, (long[]) value);
        } else if (value instanceof short[]) {
            bundle.putShortArray(key, (short[]) value);
        } else if (value instanceof String[]) {
            bundle.putStringArray(key, (String[]) value);
        } else {
            new Exception("传的参数错误");
        }
    }

    /**
     * 提交要接收的值
     *
     * @see [类、类#方法、类#成员]
     */
    public void commitAddValues() {
        if (bundle != null) {
            setArguments(bundle);
        } else {

        }
    }

}
