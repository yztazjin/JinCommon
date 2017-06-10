package ttyy.com.common.media.recorder;

import java.io.File;

/**
 * author: admin
 * date: 2017/06/09
 * version: 0
 * mail: secret
 * desc: RecorderIntf
 */

public interface RecorderIntf {

    int CANCELED = -2;
    int RELEASED = -1;
    int RECORDING = 1;
    int RECORD_STOPPED = 2;
    int RECORD_CONVERTING = 3;

    /**
     * 录音文件存储地址
     * @param file
     */
    RecorderIntf setRecordOutputFile(File file);

    /**
     * 录音文件存储地址
     * @param path
     */
    RecorderIntf setRecordOutputPath(String path);

    /**
     * 录音文件存储地址
     * @return
     */
    File getRecordedFile();

    /**
     * 开始录音
     */
    RecorderIntf startRecord();

    /**
     * 停止录音
     */
    RecorderIntf stopRecord();

    /**
     * 取消录音
     */
    RecorderIntf cancelRecord();

    /**
     * 设置回调
     * @param callback
     * @return
     */
    RecorderIntf setCallback(Callback callback);

    int getRecorderState();

    /**
     * 释放资源
     */
    void release();

    abstract class Callback{

        public abstract void onRecordSuccess(File mFile, int seconds);

        public void onRecordFail(Exception mException){

        }

        public void onRecordCancel(){

        }

        public void onRecordRelease(){

        }

    }

}
