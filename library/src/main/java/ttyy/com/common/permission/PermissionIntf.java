package ttyy.com.common.permission;

import android.app.Activity;

/**
 * author: admin
 * date: 2017/04/27
 * version: 0
 * mail: secret
 * desc: PermissionIntf
 */

interface PermissionIntf {

    boolean isPermissionGranted(Activity activity, String...permissions);

    String[] getDeniedPermissions(Activity activity, String...permissions);

    boolean shouldShowRationale(Activity activity, String...permissions);

    boolean hasAlwaysPermissionDenied(Activity activity, String...permissions);

}
