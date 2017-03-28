package ttyy.com.common.log;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ttyy.com.common.tool.App;

/**
 * author: admin
 * date: 2017/03/28
 * version: 0
 * mail: secret
 * desc: __ExternalLog
 */

public class __ExternalLog implements __$logging{

    private __ExternalLog(){
        mExecutor = Executors.newSingleThreadExecutor();
    }

    static __ExternalLog INSTANCE = new __ExternalLog();

    private File mExternalLogDir = null;
    private Executor mExecutor;

    void setExternalLogDir(File file){
        mExternalLogDir = file;
        if(file != null
                && !file.exists()){
            file.mkdirs();
        }
    }

    void resetExternalLogDir(){
        if(mExternalLogDir != null
                && mExternalLogDir.exists()){
            return;
        }

        Context context = App.getInstance();
        if(context == null){
            return;
        }

        mExternalLogDir = context.getExternalFilesDir("log");
        if(mExternalLogDir !=null){
            mExternalLogDir.mkdirs();
        }
    }

    @Override
    public void printLog(int level, String tag, String msg) {
        resetExternalLogDir();
        if(mExternalLogDir == null
                || !mExternalLogDir.exists()){
            return;
        }
        logAsync(level, tag, msg);
    }

    void logAsync(int level, String tag, String msg){
        mExecutor.execute(new $AsyncRunnable(level, tag, msg));
    }

    class $AsyncRunnable implements Runnable{

        int level;
        String tag;
        String msg;

        public $AsyncRunnable(int level, String tag, String msg){
            this.level = level;
            this.tag = tag;
            this.msg = msg;
        }

        @Override
        public void run() {
            try {
                File file = getExternalLogFile(tag);
                if(file == null
                        || !file.exists()){
                    return;
                }
                PrintWriter pw = new PrintWriter(new FileOutputStream(file));
                pw.print(msg);
                pw.flush();
                pw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        /**
         * tagFile to record the msg
         */
        private File getExternalLogFile(String tag){
            if(mExternalLogDir == null
                    || !mExternalLogDir.exists()){
                return null;
            }

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int date = calendar.get(Calendar.DATE);
            StringBuilder sb = new StringBuilder();
            sb.append(year)
                    .append("_")
                    .append(month)
                    .append("_")
                    .append(date);
            String fileName = sb.toString();
            File tagDir = new File(mExternalLogDir, tag);
            File tagFile = new File(tagDir, fileName);

            if(!tagDir.exists()){
                tagDir.mkdirs();
            }

            if(tagFile.exists()){
                return tagFile;
            }else{
                try {
                    tagFile.createNewFile();
                    return tagFile;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

}
