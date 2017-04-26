package ttyy.com.jincommon.demo;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

/**
 * Author: hjq
 * Date  : 2017/03/31 20:36
 * Name  : JinWindow
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class JinWindow {

    public WindowManager wm;
    private View mView;

    static JinWindow INSTANCE;
    WindowManager.LayoutParams params;

    private JinWindow(Context context) {
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        params = new WindowManager.LayoutParams();
        // 类型
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        // 设置flag
        int flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.TOP | Gravity.RIGHT;

        mView = LayoutInflater.from(context).inflate(R.layout.view_wm_test, null, false);
        View closeView = mView.findViewById(R.id.tv_close);
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wm.removeView(mView);
            }
        });
    }

    public static JinWindow getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new JinWindow(context.getApplicationContext());
        }
        return INSTANCE;
    }

    public void show() {
        wm.addView(mView, params);

        params.flags &= ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        if ((params.flags & WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE) > 0) {
            params.flags &= ~WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        }
        params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wm.updateViewLayout(mView, params);
    }

}
