package com.yangda.andon.ui.base;

import android.content.Intent;

/**
 * Created by Admin on 17/6/1.
 */
public interface FragmentResultCallback {

    /**
     * 结果反馈回调
     * @param requestCode 请求编码
     * @param resultCode 结果编码
     * @param data
     * @see [类、类#方法、类#成员]
     */
    void onFragmentResult(int requestCode, int resultCode, Intent data);

}
