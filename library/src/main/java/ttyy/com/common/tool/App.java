package ttyy.com.common.tool;

import android.content.Context;
import android.util.Log;

/**
 * Author: hjq
 * Date  : 2017/01/02 22:51
 * Name  : App
 * Intro : Edit By hjq
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2017/01/02    hjq   1.0              1.0
 */
public class App {

    private static Context mContext;

    public static Context getInstance(){
        if(mContext != null){
            return mContext;
        }
        init();
        return mContext;
    }

    private static void init(){
        try {
            mContext = (Context) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
            if (mContext == null)
                throw new IllegalStateException("AppGlobals can't get the application obj");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                mContext = (Context) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
            } catch (Exception e1) {
                Log.e("App", "Can't get application obj, you'd better init with Application Context");
            }
        }
    }

}
