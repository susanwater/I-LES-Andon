package com.yangda.andon.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Admin on 17/6/1.
 */
@SuppressLint("Recycle")
public class KeyBoardHelper {

    public static void hide(Activity activity) {
        if (activity == null)
            return;

        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);

        View v = activity.getCurrentFocus();
        if (v == null)
            return;

        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static void show(Activity activity) {
        if (activity == null)
            return;

        final InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    public static void keyboardOff(Activity mthis, View... views) {
        InputMethodManager imm = (InputMethodManager) mthis.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(!imm.isActive())
            return;

        for (View v : views) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static void keyboardOn(Activity mthis, View view) {
        InputMethodManager imm = (InputMethodManager) mthis.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    public static void keyboardOnSleep(final Activity mthis, final View view) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                keyboardOn(mthis, view);
            }
        }, 400);
    }

    public static void keyboardOn(final View view){
        new Handler().postDelayed(new Runnable() {
            public void run() {
                view.dispatchTouchEvent(MotionEvent.obtain(
                        SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_DOWN, 0, 0, 0));
                view.dispatchTouchEvent(MotionEvent.obtain(
                        SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_UP, 0, 0, 0));
            }
        }, 250);
    }

    public static void keyboardOn(final Fragment fragment, final View view){
        new Handler().postDelayed(new Runnable() {
            public void run() {

                if(!fragment.isVisible())
                    return;

                view.dispatchTouchEvent(MotionEvent.obtain(
                        SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_DOWN, 0, 0, 0));
                view.dispatchTouchEvent(MotionEvent.obtain(
                        SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_UP, 0, 0, 0));
            }
        }, 250);
    }

    public static void setInputLimit(final EditText editText, final int maxLength, final Runnable callback) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (callback != null)
                    callback.run();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String orgTitle = editable.toString();
                String title = editable.toString().trim();
                if (orgTitle.length() > maxLength) {
                    title = title.length() > maxLength ? title.substring(0, maxLength) : title;
                    editText.setText(title);
                    editText.setSelection(title.length());
                } else if (orgTitle.startsWith(" ")) {
                    editText.setText(title);
                    editText.setSelection(title.length());
                }
            }
        });
    }

}
