package ttyy.com.common.media.recorder;

import ttyy.com.common.media.recorder.voice.ImplFromAudio;
import ttyy.com.common.media.recorder.voice.ImplFromMedia;

/**
 * author: admin
 * date: 2017/06/09
 * version: 0
 * mail: secret
 * desc: AppRecorder
 */

public class AppRecorder {

    RecorderIntf mAudioImpl;
    RecorderIntf mMediaImpl;

    private AppRecorder(){
        mAudioImpl = new ImplFromAudio();
        mMediaImpl = new ImplFromMedia();
    }

    static class Holder{
        static AppRecorder INSTANCE = new AppRecorder();
    }

    public static AppRecorder get(){
        return Holder.INSTANCE;
    }

    public RecorderIntf getVoiceRecorderInstance(VoiceRecorderType type){
        if(type == null){

            return mMediaImpl;
        }
        switch (type){
            case AAC:

                return mMediaImpl;
            case WAV:

                return mAudioImpl;
            default:

                return mMediaImpl;
        }
    }

    public RecorderIntf createVoiceRecorder(VoiceRecorderType type){
        if(type == null){

            return new ImplFromMedia();
        }
        switch (type){
            case AAC:

                return new ImplFromMedia();
            case WAV:

                return new ImplFromAudio();
            default:

                return new ImplFromMedia();
        }
    }

    enum VoiceRecorderType{

        AAC,
        WAV;

    }

}
