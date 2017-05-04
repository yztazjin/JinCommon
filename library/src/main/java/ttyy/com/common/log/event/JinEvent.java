package ttyy.com.common.log.event;

import android.app.Application;
import android.util.Log;

import ttyy.com.common.log.LogConfig;
import ttyy.com.common.log.log;

/**
 * author: admin
 * date: 2017/05/04
 * version: 0
 * mail: secret
 * desc: JinEvent
 */

public class JinEvent {

    static String TAG = "Event";

    public static void init(String tag, Application context){
        LogConfig.getInstance().setEnableExternalLog(true)
                                .setExternalLogDir(context.getExternalFilesDir("log"));

        TAG = tag;
    }

    public static void add(String message){
        LogConfig.getInstance().getExternalLog().printLog(Log.DEBUG, TAG, message);
    }

    public static void add(Throwable message){
        LogConfig.getInstance().getExternalLog().printLog(Log.DEBUG, TAG, Log.getStackTraceString(message));
    }

    public static void err(String message){
        log.e(TAG, message);
    }

    public static void err(Throwable message){
        log.$e(TAG, message);
    }

    public static void log(String message){
        LogConfig.getInstance().getRuntimeLog().printLog(Log.DEBUG, TAG, message);
    }

    public static void log(Throwable message){
        LogConfig.getInstance().getRuntimeLog().printLog(Log.DEBUG, TAG, Log.getStackTraceString(message));
    }

}
