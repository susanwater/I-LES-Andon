package com.haier.ledai;

import android.app.Application;

import com.haier.ledai.db.HRDBManager;
import com.haier.ledai.util.StringUtils;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import framework.cache.CacheLoaderManager;
import framework.cache.disk.naming.HashCodeFileNameGenerator;
import framework.util.AppUtils;

/**
 * Created by Admin on 17/7/19.
 */
public class MyApplication extends Application {

    private static MyApplication application;

    private static ExecutorService cachedThreadPool;

    public void onCreate() {
        super.onCreate();

        application = this;
        String processName = AppUtils.getProcessName(this,
                android.os.Process.myPid());
        if (!StringUtils.isEmpty(processName)) {
            boolean defaultProcess = processName
                    .equals(AppUtils.getAppPackageName(this));
            if (defaultProcess) {
                //必要的初始化资源操作
                initCache();
                initImageLoader();
                HRDBManager.init(getApplicationContext());

            }
        }
    }

    public synchronized static ExecutorService getExecutorServiceInstance()
    {
        if (cachedThreadPool==null)
        {
            cachedThreadPool = Executors.newCachedThreadPool();
        }
        return cachedThreadPool;
    }

    /***
     * 初始化缓存模块
     */
    private void initCache() {
        try {
            CacheLoaderManager.getInstance().init(this, new HashCodeFileNameGenerator(), 1024 * 1024 * 8, 50, 20);
            CacheLoaderManager.getInstance().delete("log");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initImageLoader() {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .threadPriority(Thread.NORM_PRIORITY - 1) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)

                .imageDownloader(new BaseImageDownloader(this)) // default
                .imageDecoder(new BaseImageDecoder(false)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();

        ImageLoader.getInstance().init(config);

    }

    public static MyApplication getApplication() {
        return application;
    }

}
