package ttyy.com.common.log;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * author: admin
 * date: 2017/03/28
 * version: 0
 * mail: secret
 * desc: LogConfig
 */

public class LogConfig {

    private LogConfig(){

    }

    boolean isDebug = true;

    int mExternalLimitSize = 20 * 1024 * 1024;

    String TAG = "Event";

    Set<__$logging> loggings = new HashSet<__$logging>() {
        {
            add(__RuntimeLog.INSTANCE);
            add(__ExternalLog.INSTANCE);
        }
    };

    private static class Holder{
        static LogConfig INSTANCE = new LogConfig();
    }

    public static LogConfig getInstance(){
        return Holder.INSTANCE;
    }

    public LogConfig setIsDebug(boolean value) {
        this.isDebug = value;
        return this;
    }

    public LogConfig setTag(String tag){
        TAG = tag;
        return this;
    }

    public String getTag(){
        return TAG;
    }

    public LogConfig setEnableExternalLog(boolean value) {
        if (value) {
            if(!loggings.contains(__ExternalLog.INSTANCE)){
                loggings.add(__ExternalLog.INSTANCE);
            }
        } else {
            if(loggings.contains(__ExternalLog.INSTANCE)){
                loggings.remove(__ExternalLog.INSTANCE);
            }
        }
        return this;
    }

    public LogConfig setExternalLogDir(File mFile) {
        setEnableExternalLog(mFile != null);
        __ExternalLog.INSTANCE.setExternalLogDir(mFile);
        return this;
    }

    public LogConfig setEnableRuntimeLog(boolean value) {
        if (value) {
            if(!loggings.contains(__RuntimeLog.INSTANCE)){
                loggings.add(__RuntimeLog.INSTANCE);
            }
        } else {
            if(loggings.contains(__RuntimeLog.INSTANCE)){
                loggings.remove(__RuntimeLog.INSTANCE);
            }
        }
        return this;
    }

    public boolean isDebug(){
        return this.isDebug;
    }

    public LogConfig setExternalLimitSize(int size){
        if(size > 0)
            mExternalLimitSize = size;
        return this;
    }

    public int getExternalLimitSize(){

        return mExternalLimitSize;
    }

    public __$logging getExternalLog(){
        return __ExternalLog.INSTANCE;
    }

    public __$logging getRuntimeLog(){
        return __RuntimeLog.INSTANCE;
    }

}
