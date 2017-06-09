package ttyy.com.common.media.player;

import ttyy.com.common.media.player.voice.ImplFromMedia;

/**
 * author: admin
 * date: 2017/06/09
 * version: 0
 * mail: secret
 * desc: JinPlayer
 */

public class JinPlayer {

    PlayerIntf mImplFromMedia;

    private JinPlayer(){
        mImplFromMedia = new ImplFromMedia();
    }

    static class Holder{
        static JinPlayer INSTANCE = new JinPlayer();
    }

    public static JinPlayer getInstance(){
        return Holder.INSTANCE;
    }

    public PlayerIntf getVoicePlayerInstance(){
        return mImplFromMedia;
    }

    public PlayerIntf createVoicePlayer(){
        return new ImplFromMedia();
    }

}
