package ttyy.com.common.media.player;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * author: admin
 * date: 2017/06/09
 * version: 0
 * mail: secret
 * desc: PlayerIntf
 */

public interface PlayerIntf {

    void playFromAssets(Context context, String filename);

    void playFromSdcard(String path);

    void playFromResources(Context context, int resiId);

    void playFromNetwork(Context context, String url);

    PlayerIntf setLoopingPlay(boolean value);

    boolean isLoopingPlay();

    boolean isPlaying();

    abstract class Callback{

        public void onBufferingUpdate(MediaPlayer mp, int percent){

        }

        public void onPrepared(MediaPlayer mp){

        }

        public void onCompletion(MediaPlayer mp){

        }

        public void onFailure(Exception e){

        }
    }
}
