package ttyy.com.common.log.event;

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

    public static LogConfig getConfig(){
        return LogConfig.getInstance();
    }

    public static void add(String message){
        String TAG = LogConfig.getInstance().getTag();
        LogConfig.getInstance().getExternalLog().printLog(Log.DEBUG, TAG, message);
    }

    public static void add(Throwable message){
        String TAG = LogConfig.getInstance().getTag();
        LogConfig.getInstance().getExternalLog().printLog(Log.DEBUG, TAG, Log.getStackTraceString(message));
    }

    public static void err(String message){
        String TAG = LogConfig.getInstance().getTag();
        log.e(TAG, message);
    }

    public static void err(Throwable message){
        String TAG = LogConfig.getInstance().getTag();
        log.$e(TAG, message);
    }

    public static void log(String message){
        String TAG = LogConfig.getInstance().getTag();
        LogConfig.getInstance().getRuntimeLog().printLog(Log.DEBUG, TAG, message);
    }

    public static void log(Throwable message){
        String TAG = LogConfig.getInstance().getTag();
        LogConfig.getInstance().getRuntimeLog().printLog(Log.DEBUG, TAG, Log.getStackTraceString(message));
    }

}
