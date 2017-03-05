package ttyy.com.common.feature;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

/**
 * Author: hjq
 * Date  : 2016/12/03 11:58
 * Name  : HomeKeyWatcher
 * Intro : Edit By hjq
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2016/12/03    hjq   1.0              1.0
 */
public class HomeKeyWatcher {

   Context context;
    InnerBroadcastReceiver receiver;
    IntentFilter filter;
    HomeKeyPressListener mListener;

    boolean isWatching = false;

    public HomeKeyWatcher(){

    }

    public HomeKeyWatcher startWatch(){
        if(!isWatching){
            context.registerReceiver(receiver, filter);
            isWatching = true;
        }
        return this;
    }

    public HomeKeyWatcher stopWatch(){
        if(isWatching){
            isWatching = false;
            context.unregisterReceiver(receiver);
        }
        return this;
    }

    public HomeKeyWatcher onCreate(Context context){
        this.context = context;

        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        receiver = new InnerBroadcastReceiver();

        return this;
    }

    public HomeKeyWatcher onDestroy(){
        stopWatch();
        context = null;
        mListener = null;
        return this;
    }

    public HomeKeyWatcher setHomeKeyPressListener(HomeKeyPressListener listener){
        this.mListener = listener;
        return this;
    }

    class InnerBroadcastReceiver extends BroadcastReceiver {

        final String REASON_KEY_0 = "reason";

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){

                Bundle bundle = intent.getExtras();
                String reason = null;
                if(bundle.containsKey(REASON_KEY_0)){
                    reason = bundle.getString(REASON_KEY_0);
                }

                if(mListener != null){
                    mListener.onHomeKeyPressed(Action.convert(reason));
                }

            }
        }
    }

    public enum Action{

        /**
         * 通过Home键回到桌面
         */
        HOME_TO_DESKTOP("homekey"),
        /**
         * 通过Home锁屏幕
         */
        LOCK_SCREEN("lock"),
        /**
         * 通过Home键，显示运行中的App列表
         */
        SHOW_APP_LIST("recentapps"),

        UN_KNOWN("unknown");

        private String value;
        Action(String value){
            this.value = value;
        }

        public static Action convert(String value){
            if(HOME_TO_DESKTOP.toString().equals(value)){
                Log.i("HomeKeyWatcher", "通过Home键，回到桌面");
                return HOME_TO_DESKTOP;
            }else if(LOCK_SCREEN.toString().equals(value)){
                Log.i("HomeKeyWatcher", "通过Home键，锁屏");
                return LOCK_SCREEN;
            }else if(SHOW_APP_LIST.toString().equals(value)){
                Log.i("HomeKeyWatcher", "通过Home键，显示App列表");
                return SHOW_APP_LIST;
            }else {
                Log.i("HomeKeyWatcher", "不识别");
                return UN_KNOWN;
            }
        }

        @Override
        public String toString(){
            return value;
        }
    }

    public interface HomeKeyPressListener {
        void onHomeKeyPressed(Action action);
    }
}
