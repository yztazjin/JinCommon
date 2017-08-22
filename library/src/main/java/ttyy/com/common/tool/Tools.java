package ttyy.com.common.tool;

import android.content.Context;

/**
 * Author: hjq
 * Date  : 2017/03/05 13:31
 * Name  : Tools
 * Intro : Edit By hjq
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2017/03/05    hjq   1.0              1.0
 */
public class Tools {

    volatile static Tools INSTANCE;

    private Context mContext;

    DeviceUtil mDeviceUtil;
    NetworkUtil mNetworkUtil;
    ResourceUtil mResourceUtil;
    ApplicationUtil mApplicationUtil;
    MemorySpaceUtil mMemorySpaceUtil;
    CalendarUtil mCalendarUtil;

    private Tools(Context context){
        mContext = context;
        mCalendarUtil = new CalendarUtil();
    }

    public static Tools get(){
        Tools inst = INSTANCE;
        if(inst == null){
            synchronized (Tools.class){
                inst = INSTANCE;
                if(inst == null){
                    inst = new Tools(App.getInstance());
                    INSTANCE = inst;
                }
            }
        }

        if(inst.mContext == null){
            inst.mContext = App.getInstance();
        }

        return inst;
    }

    public boolean isReady(){
        return mContext != null;
    }

    public Tools init(Context context){
        Tools inst = INSTANCE;
        inst.mContext = context.getApplicationContext();
        return inst;
    }

    public DeviceUtil getDeviceUtil(){
        if(isReady()){

            if(mDeviceUtil == null){
                mDeviceUtil = new DeviceUtil(mContext);
            }

        }

        return mDeviceUtil;
    }

    public ResourceUtil getResourceUtil(){
        if(isReady()){

            if(mResourceUtil == null){
                mResourceUtil = new ResourceUtil(mContext);
            }

        }

        return mResourceUtil;
    }

    public NetworkUtil getNetworkUtil(){
        if(isReady()){

            if(mNetworkUtil == null){
                mNetworkUtil = new NetworkUtil(mContext);
            }

        }

        return mNetworkUtil;
    }

    public ApplicationUtil getApplicationUtil(){
        if(isReady()){

            if(mApplicationUtil == null){
                mApplicationUtil = new ApplicationUtil(mContext);
            }

        }

        return mApplicationUtil;
    }

    public MemorySpaceUtil getMemorySpaceUtil(){

        if(mMemorySpaceUtil == null){
            mMemorySpaceUtil = new MemorySpaceUtil();
        }

        return mMemorySpaceUtil;
    }
    
    public CalendarUtil getCalendarUtil(){
        return mCalendarUtil;
    }

}
