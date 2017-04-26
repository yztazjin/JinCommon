package ttyy.com.common.permission;

import android.app.Activity;

/**
 * Author: hjq
 * Date  : 2017/04/26 21:55
 * Name  : PermissionCallbackHandler
 * Intro : Edit By hjq
 * Version : 1.0
 */
public abstract class PermissionCallbackHandler {

    private Activity mActivity;

    protected final boolean hasAlwaysDeniedPermission(String permission){

        return __PermissionCheckUtil.hasAlwaysDeniedPermission(mActivity, permission);
    }

    public void requestPermissionAgain(){

    }

    public abstract void onPermissionGranted();

    public abstract void onPermissionDenied(String[] deniedPermissions);

    /**
     * false 用户不做拦截处理
     * true 用户做了拦截处理，需要用户主动调用requestPermissionAgain
     * @param rationelPermissions
     * @return
     */
    public boolean onShowRationelHandler(String[] rationelPermissions){

        return false;
    }

}
