package ttyy.com.common.permission;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Author: hjq
 * Date  : 2017/04/26 21:58
 * Name  : __PermissionCheckUtil
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class __PermissionCheckUtil {

    static boolean check(Activity activity, String permission){
        int SDKVersion = Build.VERSION.SDK_INT;
        if (SDKVersion < 19) {
            // 只要包含该权限，那么就默认拥有该权限
            PackageManager pm = activity.getPackageManager();
            return pm.checkPermission(permission, activity.getPackageName()) == PackageManager.PERMISSION_GRANTED;
        } else {
            // 通过AppOpsManager检查权限
            AppOpsManager opsMgr = (AppOpsManager) activity.getSystemService(Context.APP_OPS_SERVICE);
            if (opsMgr != null) {
                int rst = opsMgr.checkOp(permission, Binder.getCallingUid(), activity.getPackageName());

                return rst == AppOpsManager.MODE_ALLOWED;
            } else {

                return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
            }
        }
    }

    static boolean hasAlwaysDeniedPermission(Activity activity, String permission){
        int SDKVersion = Build.VERSION.SDK_INT;
        if(SDKVersion >= 23){

            return !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
        }

        return !check(activity, permission);
    }

    static boolean shouldShowRationel(Activity activity, String permission){
        int SDKVersion = Build.VERSION.SDK_INT;
        if(SDKVersion >= 23){

            return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
        }

        return !check(activity, permission);
    }

}
