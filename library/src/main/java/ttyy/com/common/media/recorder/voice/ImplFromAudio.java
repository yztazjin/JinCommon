package ttyy.com.common.media.recorder.voice;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import ttyy.com.common.media.recorder.RecorderIntf;

/**
 * author: admin
 * date: 2017/06/09
 * version: 0
 * mail: secret
 * desc: ImplFromAudio 针对录制WAV格式的音频
 */

public class ImplFromAudio implements RecorderIntf {

    static final int AUDIO_SAMPLE_RATEINHZ = 44100;

    AudioRecord mAudioRecord;

    int mMinBufferSize = -1;

    File mOutputFile = null;

    int mRecorderState = RECORD_STOPPED;

    RecorderIntf.Callback mCallback;
    long mRecordStartMillions = -1;

    public ImplFromAudio() {
        mMinBufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        initAudioRecord();
    }

    void initAudioRecord() {
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,// 从麦克风采集
                AUDIO_SAMPLE_RATEINHZ,// 在所有设备上都能适配
                AudioFormat.CHANNEL_IN_MONO,// 在所有设备上都能适配
                AudioFormat.ENCODING_PCM_16BIT,
                mMinBufferSize);
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
        if (mRecorderState == RECORD_CONVERTING) {
            try {
                Thread.sleep(50);
                getRecordedFile();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return mOutputFile;
    }

    @Override
    public RecorderIntf startRecord() {
        if (mAudioRecord != null) {
            mAudioRecord.stop();
            mAudioRecord.startRecording();
        } else {
            Log.w("ImplAudio", "mAudioRecord Not Initialized So Auto Initialized!");
            initAudioRecord();
        }

        if (mOutputFile == null
                || !mOutputFile.exists()) {
            throw new UnsupportedOperationException("Hasn't Set A Audio Output File");
        }

        mRecorderState = RECORDING;
        mRecordStartMillions = System.currentTimeMillis();

        new AudioStreamTaker().start();

        return this;
    }

    @Override
    public RecorderIntf stopRecord() {
        if (mAudioRecord == null) {
            Log.w("ImplAudio", "mAudioRecord Not Initialized!");
            return this;
        }

        mAudioRecord.stop();
        mRecorderState = RECORD_STOPPED;

        return this;
    }

    @Override
    public RecorderIntf cancelRecord() {
        if (mAudioRecord == null) {
            Log.w("ImplAudio", "mAudioRecord Not Initialized!");
            return this;
        }
        mAudioRecord.stop();
        mRecorderState = CANCELED;

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

        if (mAudioRecord == null) {
            Log.w("ImplAudio", "mAudioRecord Has Released Or Not Initialized!");
        }
        mAudioRecord.stop();
        mRecorderState = RELEASED;
        mAudioRecord.release();
        mAudioRecord = null;
    }

    class AudioStreamTaker extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                ByteArrayOutputStream mOriginalAudioStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[mMinBufferSize];
                while (mRecorderState == RECORDING) {

                    int readSize = mAudioRecord.read(buffer, 0, buffer.length);
                    if (readSize > 0) {
                        mOriginalAudioStream.write(buffer);
                    }

                }

                if (mRecorderState == RECORD_STOPPED) {
                    mRecorderState = RECORD_CONVERTING;
                    // 写入文件
                    FileOutputStream fos = new FileOutputStream(mOutputFile);
                    // 写入wav格式头信息
                    long totalAudioLen = mOriginalAudioStream.size();
                    long totalDataLen = totalAudioLen + 36;
                    long longSampleRate = AUDIO_SAMPLE_RATEINHZ;
                    int channels = 1;
                    long byteRate = 16 * AUDIO_SAMPLE_RATEINHZ * channels / 8;
                    writeWaveFileHeader(fos, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate);
                    mOriginalAudioStream.writeTo(fos);

                    fos.close();

                    mRecorderState = RECORD_STOPPED;

                }

                mOriginalAudioStream.close();

                if (mCallback != null) {
                    switch (mRecorderState) {
                        case RECORD_STOPPED:
                            int seconds = Math.round((System.currentTimeMillis() - mRecordStartMillions) / 1000f);
                            mCallback.onRecordSuccess(mOutputFile, seconds);
                            break;
                        case CANCELED:
                            mCallback.onRecordCancel();
                            break;
                        case RELEASED:
                            mCallback.onRecordRelease();
                            break;
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                if (mCallback != null) {
                    mCallback.onRecordFail(e);
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (mCallback != null) {
                    mCallback.onRecordFail(e);
                }
            }

        }
    }

    /**
     * 这里提供一个WAV头信息。
     * 每种格式的文件都有自己特有的头文件。
     */
    private void writeWaveFileHeader(OutputStream out, long totalAudioLen,
                                     long totalDataLen, long longSampleRate, int channels, long byteRate) throws IOException {
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = 16; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.write(header, 0, 44);
    }
}
