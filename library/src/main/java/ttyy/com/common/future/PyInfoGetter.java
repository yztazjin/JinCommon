package ttyy.com.common.future;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Author: hjq
 * Date  : 2017/03/19 20:07
 * Name  : PyInfoGetter
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class PyInfoGetter {

    /**
     * 获取Python脚本打入的信息
     *
     * @param context
     * @return
     */
    public static String getPyValue(Context context, String key) {
        ApplicationInfo appInfo = context.getApplicationInfo();

        try {
            ZipFile apkZip = new ZipFile(appInfo.sourceDir);
            Enumeration<? extends ZipEntry> entries = apkZip.entries();

            key = "META-INF/"+key;

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().startsWith(key)) {
                    String value = entry.getName().split("_")[1];
                    return value;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

}
