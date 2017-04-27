package ttyy.com.common.permission;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * author: admin
 * date: 2017/04/27
 * version: 0
 * mail: secret
 * desc: Version19_22
 */

final class Version19_22 implements PermissionIntf {

    // 运行时需要动态获取的权限
    HashMap<String, Integer> mAppOpsSupportPermissions;

    Method AppOpsCheckOp;
    int mAppOpsCheckOpStatus = 0; // -1该方法不存在 0初始设置 1该方法存在

    Version19_22() {
        mAppOpsSupportPermissions = new HashMap<>();

        mAppOpsSupportPermissions.put(Manifest.permission.ACCESS_COARSE_LOCATION, 0);
        mAppOpsSupportPermissions.put(Manifest.permission.ACCESS_FINE_LOCATION, 1);
        mAppOpsSupportPermissions.put(Manifest.permission.VIBRATE, 3);

        mAppOpsSupportPermissions.put(Manifest.permission.READ_CONTACTS, 4);
        mAppOpsSupportPermissions.put(Manifest.permission.WRITE_CONTACTS, 5);
        mAppOpsSupportPermissions.put(Manifest.permission.READ_CALL_LOG, 6);
        mAppOpsSupportPermissions.put(Manifest.permission.WRITE_CALL_LOG, 7);

        mAppOpsSupportPermissions.put(Manifest.permission.READ_CALENDAR, 8);
        mAppOpsSupportPermissions.put(Manifest.permission.WRITE_CALENDAR, 9);

        mAppOpsSupportPermissions.put(Manifest.permission.CALL_PHONE, 13);
        mAppOpsSupportPermissions.put(Manifest.permission.READ_SMS, 14);
        mAppOpsSupportPermissions.put(Manifest.permission.RECEIVE_SMS, 16);
        mAppOpsSupportPermissions.put(Manifest.permission.RECEIVE_MMS, 18);
        mAppOpsSupportPermissions.put(Manifest.permission.RECEIVE_WAP_PUSH, 19);
        mAppOpsSupportPermissions.put(Manifest.permission.SEND_SMS, 20);

        mAppOpsSupportPermissions.put(Manifest.permission.WRITE_SETTINGS, 23);
        mAppOpsSupportPermissions.put(Manifest.permission.SYSTEM_ALERT_WINDOW, 24);

        mAppOpsSupportPermissions.put(Manifest.permission.CAMERA, 26);

        mAppOpsSupportPermissions.put(Manifest.permission.RECORD_AUDIO, 27);

        mAppOpsSupportPermissions.put(Manifest.permission.WAKE_LOCK, 40);

        mAppOpsSupportPermissions.put(Manifest.permission.READ_PHONE_STATE, 51);
        mAppOpsSupportPermissions.put(Manifest.permission.ADD_VOICEMAIL, 52);
        mAppOpsSupportPermissions.put(Manifest.permission.USE_SIP, 53);
        mAppOpsSupportPermissions.put(Manifest.permission.PROCESS_OUTGOING_CALLS, 54);
        mAppOpsSupportPermissions.put(Manifest.permission.USE_FINGERPRINT, 55);
        mAppOpsSupportPermissions.put(Manifest.permission.BODY_SENSORS, 56);
        mAppOpsSupportPermissions.put(Manifest.permission.READ_EXTERNAL_STORAGE, 59);
        mAppOpsSupportPermissions.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, 60);
        mAppOpsSupportPermissions.put(Manifest.permission.GET_ACCOUNTS, 62);

    }


    @Override
    public boolean isPermissionGranted(Activity activity, String... permissions) {

        for (String permission : permissions) {

            if (!isPermissionGrantedCore(activity, permission)) {
                return false;
            }

        }

        return true;
    }

    boolean isPermissionGrantedCore(Activity activity, String permission) {

        AppOpsManager appOps = (AppOpsManager) activity.getSystemService(Context.APP_OPS_SERVICE);

        int permissionOpCode = -1;
        if(mAppOpsSupportPermissions.containsKey(permission)){
            permissionOpCode = mAppOpsSupportPermissions.get(permission);
        }
        if (appOps != null
                && permissionOpCode != -1
                && mAppOpsCheckOpStatus != -1) {

            try {
                if (AppOpsCheckOp == null) {
                    AppOpsCheckOp = appOps.getClass().getDeclaredMethod("checkOp", int.class, int.class, String.class);
                    mAppOpsCheckOpStatus = 1;
                }

                int checkOpRst = (int) AppOpsCheckOp.invoke(appOps, permissionOpCode, android.os.Process.myUid(), activity.getPackageName());

                if (checkOpRst == AppOpsManager.MODE_ALLOWED) {
                    return true;
                }

                return false;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                // 该手机系统下AppOps不支持checkOp操作
                AppOpsCheckOp = null;
                mAppOpsCheckOpStatus = -1;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                // 该手机系统下 AppOps不支持对该权限的处理
                mAppOpsSupportPermissions.remove(permission);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                // 该手机系统下 AppOps不支持对该权限的处理
                mAppOpsSupportPermissions.remove(permission);
            }
        }

        return activity.checkPermission(permission, android.os.Process.myPid(), android.os.Process.myUid())
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public String[] getDeniedPermissions(Activity activity, String... permissions) {

        ArrayList<String> deniedPermissions = new ArrayList<>();

        if (permissions == null) {

            PackageManager pm = activity.getPackageManager();
            try {
                PackageInfo pi = pm.getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
                permissions = pi.requestedPermissions;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (permissions == null) {
            return new String[0];
        }

        for (String permission : permissions) {
            if (activity.checkPermission(permission, android.os.Process.myPid(), android.os.Process.myUid())
                    != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permission);
            }
        }

        return new String[0];
    }

    @Override
    public boolean shouldShowRationale(Activity activity, String... permissions) {
        return false;
    }

    @Override
    public boolean hasAlwaysPermissionDenied(Activity activity, String... permissions) {
        return !isPermissionGranted(activity, permissions);
    }
}
