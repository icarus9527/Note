package com.example.yls.note.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
/**
 * Created by icarus9527 on 2017/3/18.
 */

public class ThreadUtils {
    //创建一个绑定主线程的Looper的handler
    //处理message或者runnable都会在主线程执行
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    private static Executor sExecutor = Executors.newSingleThreadExecutor();


    public static void runOnBackgroundThread(Runnable runnable){sExecutor.execute(runnable);}

    public static void runOnMainThread(Runnable runnable){mHandler.post(runnable);}
}
