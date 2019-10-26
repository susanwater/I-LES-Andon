package com.yangda.andon.util;

import java.util.Timer;

public class SingleTimer {

    private static Timer timer = null;

    private SingleTimer() {

    }

    public static synchronized Timer getInstanceTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        return timer;
    }

    private void pause() {
        timer.purge();
    }


    private void cancel() {
        timer.cancel();
    }


    /**
     * 关闭定时器
     */
    public synchronized void closeTimer() {
        if (timer != null) {
            //Log.e("---------timer ", "----关闭定时器 timer closeTimer() ----");
            timer.purge();
            timer.cancel();
            timer = null;
        }
    }
}
