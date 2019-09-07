package com.framework.util.async;

import com.framework.util.RandomUtil;
import com.yangda.andon.net.HarNet;

/**
 * Created by Admin on 17/1/20.
 * 异步流程控制
 */
public class AsyncControl {

    private static final String TAG = "AsyncControl";
    private IAsyncResult iAsyncResult;

    int taskSize = 0;
    //private volatile static AsyncControl asyncControl;

    private TaskDocker taskDocker;


    public AsyncControl(){}

//    public static AsyncControl getInstance(){
//        if (asyncControl==null){
//            synchronized (AsyncControl.class){
//                if (asyncControl==null){
//                    asyncControl = new AsyncControl();
//                }
//            }
//        }
//        return asyncControl;
//    }

    public void postAsync(IAsyncResult iAsyncResult,HarNet.GetBinAsyncTask... getBinAsyncTasks){


        defaultAsync();
        this.iAsyncResult = iAsyncResult;

        for (HarNet.GetBinAsyncTask getBinAsyncTask: getBinAsyncTasks){
            String code = RandomUtil.generateString(12);
            getBinAsyncTask.taskCode = code;
            taskSize++;
            taskDocker.getTaskCodeList().add(code);
            taskDocker.getTaskList().add(getBinAsyncTask);
            getBinAsyncTask.execute();

        }


    }

    public synchronized void receiverBin(String code, String bt){
        if (taskDocker!=null){
            int index = taskDocker.getTaskCodeList().indexOf(code);
            taskDocker.getResultList().add(index,bt);

            int size = taskDocker.getResultList().size();
            if (size==taskSize && iAsyncResult!=null){
                iAsyncResult.onGetFinish(taskDocker);
            }
        }
    }

    private void defaultAsync(){
        taskDocker = new TaskDocker();
        taskSize = 0;
    }

    public interface IAsyncResult{
        public void onGetFinish(TaskDocker taskDocker);
    }

}
