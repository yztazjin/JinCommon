package ttyy.com.common.media.player;

import ttyy.com.common.media.player.voice.ImplFromMedia;

/**
 * author: admin
 * date: 2017/06/09
 * version: 0
 * mail: secret
 * desc: AppPlayer
 */

public class AppPlayer {

    PlayerIntf mImplFromMedia;

    private AppPlayer(){
        mImplFromMedia = new ImplFromMedia();
    }

    static class Holder{
        static AppPlayer INSTANCE = new AppPlayer();
    }

    public static AppPlayer getInstance(){
        return Holder.INSTANCE;
    }

    public PlayerIntf getVoicePlayerInstance(){
        return mImplFromMedia;
    }

    public PlayerIntf createVoicePlayer(){
        return new ImplFromMedia();
    }

}
