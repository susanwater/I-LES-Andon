package com.yangda.andon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.yangda.andon.R;
import com.yangda.andon.ui.base.BaseActivity;
import com.yangda.andon.ui.home.HomeMainActivity;


/**
 * Created by Admin on 17/6/1.
 */
public class SplashActivity extends BaseActivity {

    private View mView;
    private AlphaAnimation aa;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = View.inflate(this, R.layout.activity_logo, null);
        setContentView(mView);

        initView();

    }

    private void initView() {

        // 渐变启动屏
        aa = new AlphaAnimation(0.8f, 1.0f);
        aa.setDuration(2000);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                mView.clearAnimation();

                startActivity(new Intent(SplashActivity.this,
                            HomeMainActivity.class));

                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

        });
        mView.startAnimation(aa);

    }


}
