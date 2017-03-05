package ttyy.com.common.tool;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * Author: hjq
 * Date  : 2017/03/05 13:06
 * Name  : ApplicationUtil
 * Intro : Edit By hjq
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2017/03/05    hjq   1.0              1.0
 */
public class ApplicationUtil {

    Context context;

    protected ApplicationUtil(Context context){
        this.context = context;
    }

    /**
     * 获取App签名
     *
     * @return
     */
    public String getAppSignature() {
        try {
            PackageManager packageManager = context.getPackageManager();
            //请注意需要PackageManager.GET_SIGNATURES 这个flag
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            // X509证书，X.509是一种非常通用的证书格式
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory
                    .generateCertificate(new ByteArrayInputStream(sign.toByteArray()));
            // md5
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 获得公钥
            byte[] b = md.digest(cert.getEncoded());
            //key即为应用签名
            //.replace(":", "");
            String key = byte2HexFormatted(b);

            return key;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将获取到得编码进行16进制转换
     *
     * @param arr
     * @return
     */
    private static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1)
                h = "0" + h;
            if (l > 2)
                h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1))
                str.append(':');
        }
        return str.toString();
    }

    /**
     * 当前App是否处于前台
     * 在onResume/onStop中判断
     * onStop是Activity回到后台后的最后一个生命周期方法
     *
     * @return
     */
    public boolean isAppForeground() {
//        方法的原型
//        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
//        String packageName = getApplicationContext().getPackageName();
//
//        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
//                .getRunningAppProcesses();
//        if (appProcesses == null)
//            return false;
//
//        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
//            // 进程名称是否与包名一致
//            // 这种方法判断有误差，如果App启动的所在线程是一个附属线程，在附属线程中判断的话，进程名永远都会与包名一致
//            if (appProcess.processName.equals(packageName)
//                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                return true;
//            }
//        }

//        废弃方法+
//        /**
//         * android.permission.GET_TASKS
//         * 之前，使用该接口需要 android.permission.GET_TASKS
//         * 即使是自己开发的普通应用，只要声明该权限，即可以使用getRunningTasks接口。
//         * 但从L开始，这种方式以及废弃。
//         * 应用要使用该接口必须声明权限android.permission.REAL_GET_TASKS
//         * 而这个权限是不对三方应用开放的。（在Manifest里申请了也没有作用）
//         * 系统应用（有系统签名）可以调用该权限。
//         * 该方法废弃
//         */
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
//        if (tasks != null && !tasks.isEmpty()) {
//            ActivityManager.RunningTaskInfo topTask = tasks.get(0);
//            ComponentName topTaskInfo = topTask.topActivity;
//            Log.d("hjq", "top pkg " + topTaskInfo.getPackageName());
//            if (topTaskInfo.getPackageName().equals(context.getPackageName())) {
//                return true;
//            }
//        }

        // 获取当前的进程号
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        if (processInfos != null && !processInfos.isEmpty()) {
            for (ActivityManager.RunningAppProcessInfo tmp : processInfos) {
                if (tmp.pid == pid) {
                    // 判断该进程是否属于前台进程
                    if (tmp.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }

        return false;
    }


    /**
     * 获取应用程序名称
     */
    public String getAppName() {
//        第一种方案
//        try {
//            PackageManager packageManager = context.getPackageManager();
//            PackageInfo packageInfo = packageManager.getPackageInfo(
//                    context.getPackageName(), 0);
//            int labelRes = packageInfo.applicationInfo.labelRes;
//            return context.getResources().getString(labelRes);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
        PackageManager packageManager = context.getPackageManager();
        return packageManager.getApplicationLabel(context.getApplicationInfo()).toString();
    }

    // 一个获得当前进程的名字的方法
    public String getCurrentProcessName() {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    // 获取包名
    public String getPackageName() {
        String name = context.getPackageName();
        return name;
    }

    // 获取版本号
    public int getVersionCode() {
        int version = -1;
        try {
            String packageName = getPackageName();
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            version = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    // 获取版本名称
    public String getVersionName() {
        String versionCode = null;
        try {
            String packageName = getPackageName();
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            versionCode = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 判断App是否是系统安装的
     * @param pi
     * @return
     */
    public boolean isAppSystemInstalled(PackageInfo pi){
        if((pi.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)==0 &&
                (pi.applicationInfo.flags&ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)==0){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 启动App
     */
    public void startApplication(String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 需要READ_TASKS权限
     */
    public void restartApplication(){

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(50);

        String packageName = getPackageName();
        for(ActivityManager.RunningTaskInfo info : tasks){
            if(info.topActivity.getPackageName().equals(packageName)){
                am.moveTaskToFront(info.id, 0);
                return;
            }
        }

        // 没找到重新启动
        startApplication(packageName);

    }

    /**
     * 获取系统安装的所有App的包信息
     * @return
     */
    public List<PackageInfo> getInstalledPackages(){
        PackageManager pm = context.getPackageManager();
        return pm.getInstalledPackages(0);
    }

}
