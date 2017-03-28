package ttyy.com.common.log;

import android.util.Log;

/**
 * author: admin
 * date: 2017/03/28
 * version: 0
 * mail: secret
 * desc: __RuntimeLog
 */

public class __RuntimeLog implements __$logging{

    private __RuntimeLog(){

    }

    static __RuntimeLog INSTANCE = new __RuntimeLog();

    @Override
    public void printLog(int level, String tag, String msg) {
        Log.println(level, tag, msg);
    }
}
