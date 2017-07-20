package com.haier.ledai.support;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;


import com.haier.ledai.db.HRDBManager;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import framework.util.LogUtils;

/**
 * Created by Admin on 16/12/28.
 */
public class GodLogicHolyCrap {

    private static final String TAG = "GodLogicHolyCrap";
    private static ArrayBlockingQueue<Class<?>> arrayBlockingQueue = new ArrayBlockingQueue<Class<?>>(10);
    private volatile static GodLogicHolyCrap godLogicHolyCrap;
    private IGodLogic iGodLogic = null;
    public GodLogic godLogic;
    private Class<?> mClass;

    public ArrayBlockingQueue<Class<?>> getArrayBlockingQueue() {
        return arrayBlockingQueue;
    }

    public GodLogicHolyCrap() {
    }

    public static GodLogicHolyCrap getInstance() {
        if (godLogicHolyCrap == null) {
            synchronized (GodLogicHolyCrap.class) {
                if (godLogicHolyCrap == null) {
                    godLogicHolyCrap = new GodLogicHolyCrap();
                }
            }
        }
        return godLogicHolyCrap;
    }

    public void initAuth(GodLogic godLogic, IGodLogic iGodLogic) {

        clearQueue();

        this.godLogic = godLogic;
        if (iGodLogic!=null) {
            this.iGodLogic = iGodLogic;
        }

        switch (godLogic) {

            case CREDIT:

                initCredit();

                break;

            default:

                break;

        }

    }

    //信用流程
    private void initCredit() {

//        if (body == null) return;
//
//        if (body.realNameStatus == 0) {
//            arrayBlockingQueue.add(NewReaclNameCheckActivity.class);
//        } else if (body.realNameStatus == 1 || body.realNameStatus == 3 || body.realNameStatus == 5 || body.realNameStatus == 9) {
//            arrayBlockingQueue.add(NewScanGuideActivity.class);
//        }
//
//        arrayBlockingQueue.add(NewRealNameResultActivity.class);

    }





    /**
     * 从队列中移除一个
     */
    public void removeOne() {
        if (arrayBlockingQueue != null && arrayBlockingQueue.size() > 0) {
            arrayBlockingQueue.poll();
        }
    }

    //跑神
    public void runGod(FragmentActivity mthis) {
        runGod(mthis, null);
    }

    //跑神
    public synchronized void  runGod(final Context mthis, final Map map) {

        final Set<String> keys = (map != null ? map.keySet() : null);

        if (arrayBlockingQueue != null && arrayBlockingQueue.size() > 0) {
            mClass = arrayBlockingQueue.poll();
                Intent intent = new Intent(mthis, mClass);
                if (keys != null) {
                    for (String key : keys) {

                        if (map.get(key) instanceof Boolean) {
                            intent.putExtra(key, (Boolean) map.get(key));
                        } else if (map.get(key) instanceof Integer) {
                            intent.putExtra(key, (Integer) map.get(key));
                        } else if (map.get(key) instanceof Serializable) {
                            intent.putExtra(key, (Serializable) map.get(key));
                        } else {
                            intent.putExtra(key, (String) map.get(key));
                        }
                    }
                }
                mthis.startActivity(intent);
        } else {
            if (godLogic == null) {
                LogUtils.debug(TAG, "---炮神没有初始化---");
                return;
            }

            switch (godLogic) {

                case CREDIT:

                    if (iGodLogic != null) {
                        iGodLogic.isSuccess(true);
                    }

                    break;



                default:

                    break;


            }

        }


    }


    /**
     * 获得当前取出的元素
     *
     * @return
     */
    public Class<?> getOutElement() {
       /* if(this.mClass==null){
            this.mClass = (Class<?>) new Object();
        }*/
        return this.mClass;
    }


    public void clearQueue() {
        arrayBlockingQueue.clear();
    }


    /**
     * 获得当前元素
     *
     * @return
     */
    public Class<?> getQueueElement() {
        return arrayBlockingQueue.peek();
    }


    public void remove(Class<?> aClass) {
        if (arrayBlockingQueue != null) {
            arrayBlockingQueue.remove(aClass);
        }
    }


    public enum GodLogic {

        CREDIT//信用


    }


    public interface IGodLogic {

        public void isSuccess(boolean flag);

    }







}
