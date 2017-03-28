package ttyy.com.common.log;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * author: admin
 * date: 2017/03/28
 * version: 0
 * mail: secret
 * desc: logConfig
 */

public class logConfig {

    static boolean isDebug = true;

    static Set<__$logging> loggings = new HashSet<__$logging>() {
        {
            add(__RuntimeLog.INSTANCE);
            add(__ExternalLog.INSTANCE);
        }
    };

    public static void setIsDebug(boolean value) {
        logConfig.isDebug = value;
    }

    public static void setEnableExternalLog(boolean value) {
        if (value) {
            loggings.add(__ExternalLog.INSTANCE);
        } else {
            loggings.remove(__ExternalLog.INSTANCE);
        }
    }

    public static void setExternalLogDir(File mFile) {
        setEnableExternalLog(mFile != null);
        __ExternalLog.INSTANCE.setExternalLogDir(mFile);
    }

    public static void setEnableRuntimeLog(boolean value) {
        if (value) {
            loggings.add(__RuntimeLog.INSTANCE);
        } else {
            loggings.remove(__RuntimeLog.INSTANCE);
        }
    }

}
