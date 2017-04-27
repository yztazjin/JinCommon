package ttyy.com.common.permission;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * author: admin
 * date: 2017/04/27
 * version: 0
 * mail: secret
 * desc: Version23
 */

final class Version23 implements PermissionIntf {
    @Override
    public boolean isPermissionGranted(Activity activity, String... permissions) {
        for(String permission : permissions){
            if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    @Override
    public String[] getDeniedPermissions(Activity activity, String... permissions) {
        ArrayList<String> deniedPermissions = new ArrayList<>();

        if(permissions == null){

            PackageManager pm = activity.getPackageManager();
            try {
                PackageInfo pi = pm.getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
                permissions = pi.requestedPermissions;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        if(permissions == null){
            return new String[0];
        }

        for(String permission : permissions){
            if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                deniedPermissions.add(permission);
            }
        }

        return deniedPermissions.toArray(new String[deniedPermissions.size()]);
    }

    @Override
    public boolean shouldShowRationale(Activity activity, String... permissions) {

        for(String permission : permissions){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)){
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasAlwaysPermissionDenied(Activity activity, String ... permissions) {

        for(String permission : permissions){
            if(!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)){
                return true;
            }
        }

        return false;
    }

}
