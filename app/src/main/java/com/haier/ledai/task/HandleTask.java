package com.haier.ledai.task;

import com.haier.ledai.MyApplication;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;


/**
 * 任务处理
 *
 * @return nothing
 *
 */
public abstract class HandleTask implements Callable<String>{


    protected IThreadTask iThreadTask = null;
    Future<String> fFuture;


    public void pause()
    {
        try {
            fFuture.wait();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void go()
    {
        fFuture.notify();
    }


    public void playTask(IThreadTask iThreadTask)
    {
        this.iThreadTask = iThreadTask;
        fFuture = MyApplication.getExecutorServiceInstance().submit(this);

    }



    public boolean isDone()
    {
        try
        {
            if (fFuture!=null)
            {
                return fFuture.isDone();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }

    public boolean isCancel()
    {
        try
        {
            if (fFuture!=null)
            {
                return fFuture.isCancelled();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }

    public void cancelTask()
    {
        try
        {
            if (fFuture!=null && !fFuture.isCancelled()&&!fFuture.isDone())
            {
                fFuture.cancel(false);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
