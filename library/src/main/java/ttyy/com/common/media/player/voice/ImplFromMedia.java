package ttyy.com.common.media.player.voice;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

import ttyy.com.common.media.player.PlayerIntf;

/**
 * author: admin
 * date: 2017/06/09
 * version: 0
 * mail: secret
 * desc: ImplFromMedia
 */

public class ImplFromMedia implements PlayerIntf {

    MediaPlayer mMediaPlayer;
    MediaPlayer.OnBufferingUpdateListener _dftOnBufferingUpdateListener;
    MediaPlayer.OnPreparedListener _dftOnPreparedListener;
    MediaPlayer.OnCompletionListener _dftOnCompletionListener;
    MediaPlayer.OnErrorListener _dftOnErrorListener;

    PlayerIntf.Callback mCallback;

    boolean isLoopingPlay;

    public ImplFromMedia(){
       initMediaPlayer();
    }

    void initMediaPlayer(){
        _dftOnBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                if(mCallback != null){
                    mCallback.onBufferingUpdate(mp, percent);
                }
            }
        };

        _dftOnPreparedListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if(!mMediaPlayer.isPlaying()){
                    mMediaPlayer.start();
                }

                if(mCallback != null){
                    mCallback.onPrepared(mp);
                }
            }
        };

        _dftOnCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(mCallback != null){
                    mCallback.onCompletion(mp);
                }
            }
        };

        _dftOnErrorListener = new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if(mCallback != null){
                    mCallback.onFailure(new UnsupportedOperationException("MediaPlayer Inner Error!"));
                }
                return false;
            }
        };

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnBufferingUpdateListener(_dftOnBufferingUpdateListener);
        mMediaPlayer.setOnPreparedListener(_dftOnPreparedListener);
        mMediaPlayer.setOnCompletionListener(_dftOnCompletionListener);
        mMediaPlayer.setOnErrorListener(_dftOnErrorListener);
        mMediaPlayer.setLooping(isLoopingPlay);
    }

    @Override
    public void playFromAssets(Context context, String filename) {
        if(mMediaPlayer == null){
            initMediaPlayer();
        }else {
            mMediaPlayer.reset();
        }

        try {
            AssetManager asset = context.getAssets();
            AssetFileDescriptor fd = null;
            fd = asset.openFd(filename);
            mMediaPlayer.setDataSource(fd.getFileDescriptor());
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            if(mCallback != null){
                mCallback.onFailure(e);
            }
        }

    }

    @Override
    public void playFromSdcard(String path) {
        if(mMediaPlayer == null){
            initMediaPlayer();
        }else {
            mMediaPlayer.reset();
        }

        try {
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            if(mCallback != null){
                mCallback.onFailure(e);
            }
        }
    }

    @Override
    public void playFromResources(Context context, int resiId) {
        if(mMediaPlayer == null){
            initMediaPlayer();
        }else {
            mMediaPlayer.reset();
        }

        mMediaPlayer = MediaPlayer.create(context, resiId);
    }

    @Override
    public void playFromNetwork(Context context, String url) {
        if(mMediaPlayer == null){
            initMediaPlayer();
        }else {
            mMediaPlayer.reset();
        }

        try {
            mMediaPlayer.setDataSource(context, Uri.parse(url));
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PlayerIntf setLoopingPlay(boolean value) {
        isLoopingPlay = value;
        if(mMediaPlayer != null){
            mMediaPlayer.setLooping(value);
        }
        return this;
    }

    @Override
    public boolean isLoopingPlay() {
        return isLoopingPlay;
    }

    @Override
    public boolean isPlaying() {
        if(mMediaPlayer != null){
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void release() {
        if(mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
