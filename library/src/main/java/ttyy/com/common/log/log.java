package ttyy.com.common.log;

import android.util.Log;

/**
 * author: admin
 * date: 2017/03/28
 * version: 0
 * mail: secret
 * desc: log
 */

public class log {

    public static void d(String tag, String msg){
        if(!logConfig.isDebug){
            return;
        }
        print(Log.DEBUG, tag, msg);
    }

    public static void $d(String tag, Throwable msg){
        if(!logConfig.isDebug){
            return;
        }
        String msgStr = Log.getStackTraceString(msg);
        d(tag, msgStr);
    }

    public static void i(String tag, String msg){
        if(!logConfig.isDebug){
            return;
        }
        print(Log.INFO, tag, msg);
    }

    public static void $i(String tag, Throwable msg){
        if(!logConfig.isDebug){
            return;
        }
        String msgStr = Log.getStackTraceString(msg);
        i(tag, msgStr);
    }

    public static void e(String tag, String msg){
        if(!logConfig.isDebug){
            return;
        }
        print(Log.ERROR, tag, msg);
    }

    public static void $e(String tag, Throwable msg){
        if(!logConfig.isDebug){
            return;
        }
        String msgStr = Log.getStackTraceString(msg);
        e(tag, msgStr);
    }

    public static void w(String tag, String msg){
        if(!logConfig.isDebug){
            return;
        }
        print(Log.WARN, tag, msg);
    }

    public static void $w(String tag, Throwable msg){
        if(!logConfig.isDebug){
            return;
        }
        String msgStr = Log.getStackTraceString(msg);
        w(tag, msgStr);
    }

    public static void print(int l, String tag, String msg){
        if(!logConfig.isDebug){
            return;
        }
        for(__$logging tmp : logConfig.loggings){
            tmp.printLog(l, tag, msg);
        }
    }

}
