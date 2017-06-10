package ttyy.com.common.media.recorder.voice;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

import ttyy.com.common.media.recorder.RecorderIntf;

/**
 * author: admin
 * date: 2017/06/09
 * version: 0
 * mail: secret
 * desc: ImplFromMedia
 */

public class ImplFromMedia implements RecorderIntf {

    MediaRecorder mMediaRecorder;

    File mOutputFile = null;

    int mRecorderState = RECORD_STOPPED;

    RecorderIntf.Callback mCallback;

    long mRecordStartMillions = -1;

    public ImplFromMedia(){
        initMediaRecorder();
    }

    void initMediaRecorder(){
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // AAC 音频格式适配API 16及以上 IOS Android共同支持版本 AAC WAV
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
    }

    @Override
    public RecorderIntf setRecordOutputFile(File file) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mOutputFile = file;
        return this;
    }

    @Override
    public RecorderIntf setRecordOutputPath(String path) {
        setRecordOutputFile(new File(path));
        return this;
    }

    @Override
    public File getRecordedFile() {
        return mOutputFile;
    }

    @Override
    public RecorderIntf startRecord() {

        if(mOutputFile == null){
            throw new UnsupportedOperationException("Set Media OutputFile First!");
        }

        if(mMediaRecorder == null){
            initMediaRecorder();
        }

        try {
            mRecorderState = RECORDING;
            mRecordStartMillions = System.currentTimeMillis();
            mMediaRecorder.setOutputFile(mOutputFile.getAbsolutePath());
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
            releaseCore();
            if(mCallback != null){
                mCallback.onRecordFail(e);
            }
        }

        return this;
    }

    @Override
    public RecorderIntf stopRecord() {
        if(mMediaRecorder != null){
            mMediaRecorder.stop();
        }

        mRecorderState = RECORD_STOPPED;
        if(mCallback != null){
            int seconds = Math.round((System.currentTimeMillis() - mRecordStartMillions) / 1000f);
            mCallback.onRecordSuccess(mOutputFile, seconds);
        }

        return this;
    }

    @Override
    public RecorderIntf cancelRecord() {
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }

        mRecorderState = CANCELED;

        if (mCallback != null) {
            mCallback.onRecordCancel();
        }

        return this;
    }

    @Override
    public RecorderIntf setCallback(Callback callback) {
        mCallback = callback;
        return this;
    }

    @Override
    public int getRecorderState() {
        return mRecorderState;
    }

    @Override
    public void release() {
        releaseCore();

        mRecorderState = RELEASED;
        if(mCallback != null){
            mCallback.onRecordRelease();
        }
    }

    void releaseCore(){
        if(mMediaRecorder != null){
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }
}
