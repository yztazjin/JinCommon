package ttyy.com.common.permission;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

/**
 * author: admin
 * date: 2017/04/27
 * version: 0
 * mail: secret
 * desc: JinPermissionUtil
 */

public class JinPermissionUtil {

    int mRequestCode = 5001;

    PermissionIntf mIntf;

    Activity mActivity;
    PermissionListener mPermissionListener;

    private JinPermissionUtil(Activity mActivity) {

        this.mActivity = mActivity;

        int SDKVersion = getSDKVersion();
        if (SDKVersion < 19) {
            // 4.4.x 以下
            mIntf = new Version18();
        } else if (SDKVersion < 23) {
            // [4.4, 6.0)
            mIntf = new Version19_22();
        } else {
            // 6.0以上
            mIntf = new Version23();
        }

    }


    public static JinPermissionUtil create(Activity activity) {
        if (activity == null) {
            throw new RuntimeException("Activity Can't Be Null");
        }

        return new JinPermissionUtil(activity);
    }

    public static abstract class PermissionListener {

        Activity mActivity;

        public abstract void onPermissionGranted();

        public abstract void onPermissionDenied(String[] permissions);

        public abstract void onShowRationale(String[] permissions);

        public boolean shouldShowRationale() {
            return true;
        }

        public boolean isApiSupportRequestPermission() {
            return Build.VERSION.SDK_INT >= 23;
        }
    }

    public boolean isPermissionGranted(String... permissions) {

        if (permissions == null
                || permissions.length < 1) {
            return true;
        }

        return mIntf.isPermissionGranted(mActivity, permissions);
    }

    public String[] getDeniedPermissions(String... permissions) {

        if (permissions == null
                || permissions.length < 1) {
            return new String[0];
        }

        return mIntf.getDeniedPermissions(mActivity, permissions);
    }

    public boolean shouldShowRationale(String... permissions) {

        if (permissions == null
                || permissions.length < 1) {
            return false;
        }

        mIntf.shouldShowRationale(mActivity, permissions);
        return false;
    }

    public boolean hasAlwaysPermissionDenied(String permission) {
        if (!shouldShowRationale(permission)) {
            return true;
        }
        return false;
    }

    public void requestPermission(PermissionListener listener,
                                  String... permissions) {
        mPermissionListener = listener;
        if (mPermissionListener != null) {
            mPermissionListener.mActivity = mActivity;
        }

        String[] deniedPermissions = getDeniedPermissions(permissions);
        if (deniedPermissions.length > 0) {

            boolean rationale = shouldShowRationale(deniedPermissions);
            if (rationale && mPermissionListener != null && mPermissionListener.shouldShowRationale()) {
                mPermissionListener.onShowRationale(deniedPermissions);
            } else {

                if (isApiSupportRequestPermission()) {
                    mActivity.requestPermissions(deniedPermissions, mRequestCode);
                } else {
                    if (mPermissionListener != null) {
                        mPermissionListener.onPermissionDenied(deniedPermissions);
                    }
                }
            }

        } else {
            if (mPermissionListener != null) {
                mPermissionListener.onPermissionGranted();
            }

        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(mRequestCode == requestCode){
            String[] deniedPermissions = getDeniedPermissions(permissions);
            if(deniedPermissions.length > 0){

                if(mPermissionListener != null){
                    mPermissionListener.onPermissionDenied(deniedPermissions);
                }
            }else {

                if(mPermissionListener != null){
                    mPermissionListener.onPermissionGranted();
                }
            }
        }
    }

    public boolean canAppDrawOverlay(){
        if(getSDKVersion() >= 23){
            return Settings.canDrawOverlays(mActivity);
        }else {
            return isPermissionGranted(Manifest.permission.SYSTEM_ALERT_WINDOW);
        }
    }

    public boolean isApiSupportRequestPermission() {
        return getSDKVersion() >= 23;
    }

    public int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

}
