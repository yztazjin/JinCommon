package ttyy.com.common.tool;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Author: hjq
 * Date  : 2017/03/05 13:25
 * Name  : ResourceUtil
 * Intro : Edit By hjq
 * Modification  History:
 * Date          Author        	 Version          Description
 * ----------------------------------------------------------
 * 2017/03/05    hjq   1.0              1.0
 */
public class ResourceUtil {

    Context context;

    protected ResourceUtil(Context context){
        this.context = context;
    }

    // 获取xml中配置的String 值
    public String getStringResource(int id) {
        return context.getResources().getString(id);
    }

    // 获取xml中的Color 色值
    public int getColorResource(int id) {
        return context.getResources().getColor(id);
    }

    // 获取xml中的Integer 值
    public int getIntegerResource(int id) {
        return context.getResources().getInteger(id);
    }

    // 获取xml中的String数组
    public String[] getStringArrayResource(int id) {
        return context.getResources().getStringArray(id);
    }

    // 获取Assets下的资源输入流
    public InputStream getAssetsResource(String filename) {
        try {
            return context.getAssets().open(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取Assets下的String资源
    public String getAssetString(String name) {
        String string = "";
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open(name);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            input.close();
            string = new String(buffer, "utf-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return string;
    }

    // 获取assets下的Bitmap文件
    public Bitmap getAssetBitmap(String name) {
        InputStream is = getAssetsResource(name);
        if (is != null) {
            Bitmap bm = BitmapFactory.decodeStream(is);
            return bm;
        }
        return null;
    }

    // 获取Raw下的资源输入流
    public InputStream getRawResource(int id) {
        return context.getResources().openRawResource(id);
    }

    // 获取xml中的Integer数组
    public int[] getIntegerArrayResource(int id) {
        return context.getResources().getIntArray(id);
    }

    // 获取Drawable
    public Drawable getDrawable(int id) {
        return context.getResources().getDrawable(id);
    }

    // 获取Animations
    public Animation getAnimation(int id) {
        return AnimationUtils.loadAnimation(context, id);
    }

    // Application <meta-data> 元素 key为name 获取对应的value
    public <T> T getMetaData(String name) {
        ApplicationInfo appInfo;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        if (appInfo.metaData != null && appInfo.metaData.containsKey(name)) {
            return (T) appInfo.metaData.get(name);
        }
        return null;
    }

    // findView
    public <T extends View> T findViewById(View rootView, int id) {
        View target = rootView.findViewById(id);
        return (T) target;
    }

    // findView
    public <T extends View> T findViewById(Activity ac, int id) {
        View target = ac.findViewById(id);
        return (T) target;
    }

}
