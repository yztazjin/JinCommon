package ttyy.com.common.permission;

import android.app.Activity;

/**
 * Author: hjq
 * Date  : 2017/04/26 20:38
 * Name  : PermissionChecker
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class PermissionChecker {

    Activity mActivity;

    private PermissionChecker(Activity activity) {
        this.mActivity = activity;
    }

    public static PermissionChecker create(Activity activity) {
        return new PermissionChecker(activity);
    }

    public boolean check(String permission) {

        return __PermissionCheckUtil.check(mActivity, permission);
    }

    public void requestPermission(PermissionCallbackHandler callback, String...permissions){

    }

}