package ttyy.com.common.tool;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.List;
import java.util.UUID;

/**
 * Author: hjq
 * Date  : 2017/03/05 13:04
 * Name  : DeviceUtil
 * Intro : Edit By hjq
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2017/03/05    hjq   1.0              1.0
 */
public class DeviceUtil {

    Context context;

    protected DeviceUtil(Context context){
        this.context = context;
    }

    /**
     * 设备唯一标示符
     * @return
     */
    public String getDeviceUniqueId(){
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length()%10+ Build.BRAND.length()%10 +

                Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +

                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +

                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +

                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +

                Build.TAGS.length()%10 + Build.TYPE.length()%10 +

                Build.USER.length()%10 ; //13 位

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }

        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    // 系统版本名
    public String getOsReleaseVersion() {
        return Build.VERSION.RELEASE;
    }

    // 系统SDK版本
    public int getOsSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    // IMEI
    public String getIMEI() {
        String imei = ((TelephonyManager) context
                .getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        return imei;
    }

    // IMSI
    public String getIMSI() {
        String imsi = ((TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        return imsi;
    }

    // 设备型号
    public String getModel() {
        return Build.MODEL;
    }

    // 设备制造商
    public String getManufacturer() {
        return Build.MANUFACTURER;
    }

    // 窗口宽度
    public int getScreenWidth() {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    // 窗口高度
    public int getScreenHeight() {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获得状态栏的高度
     */
    public int getStatusHeight() {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     */
    public Bitmap getSnapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth();
        int height = getScreenHeight();
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     */
    public Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth();
        int height = getScreenHeight();
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 打开软键盘
     */
    public void openKeyboard(EditText mEditText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     */
    public void closeKeyboard(EditText mEditText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * sdcard是否可用
     *
     * @return
     */
    public boolean isSDCardUseful() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断屏幕是否被点亮
     * @return
     */
    public boolean isScreenOn(){
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        return isScreenOn;
    }

    /**
     * 是否包含摄像头
     * @param index
     * @return
     */
    public boolean hasCameraIndex(Integer index){

        if(Build.VERSION.SDK_INT >= 21){
            try {
                CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                for(String indexStr : manager.getCameraIdList()){
                    if(indexStr.equals(index.toString())){
                        return true;
                    }
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }else {
            int cameraCount = Camera.getNumberOfCameras();
            Camera.CameraInfo info = new Camera.CameraInfo();
            for (int i = 0; i < cameraCount; i++) {
                Camera.getCameraInfo(i, info);
                if (index == info.facing) {
                    return true;
                }
            }
        }

        return false;
    }

}
