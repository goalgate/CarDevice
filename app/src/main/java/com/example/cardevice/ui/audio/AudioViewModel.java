package com.example.cardevice.ui.audio;

import android.app.Application;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.example.cardevice.app.AppInit;
import com.example.cardevice.entity.OfflineXGData;

import java.io.IOException;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author Created by WZW on 2021-04-23 14:31.
 * @description
 */
public class AudioViewModel extends BaseViewModel implements MyAudioManager.AudioListener {

    public ObservableField<String> playMusic = new ObservableField<>("");

    MediaPlayer mMediaPlayer;

    MediaPlayer.OnPreparedListener mListener;

    private int status = 0;

    public AudioViewModel(@NonNull Application application) {
        super(application);
        playMusic.set("播放音乐");
    }

    public BindingCommand<Boolean> playFunc = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            if (status == 0) {
                ToastUtils.showShort("播放器没有准备好");
                return;
            }
            if (status == 1 || status == 3) {
                startPlay();
            } else if (status == 2) {
                pausePlay();
            } else {
                initMediaPlayer();
                ToastUtils.showShort("播放器被销毁，开始重新创建");
            }
        }
    });

    @Override
    public void onCreate() {
        initListener();
        initMediaPlayer();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlay();
        int release = MyAudioManager.releaseFocus();
    }

    private void initListener() {
        mListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                status = 1;
            }
        };
    }


    private void initMediaPlayer() {
        try {
            AssetFileDescriptor descriptor = getApplication().getAssets().openFd("canon.mp3");
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                mMediaPlayer.setLooping(true);
                mMediaPlayer.setOnPreparedListener(mListener);
                mMediaPlayer.prepareAsync();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void startPlay() {
        int result = MyAudioManager.requestAudioFocus(this);
        KLog.e("audioManager", "requestAudioFocusResult = " + result);
        if (result != 1) {
            return;
        }
        if (mMediaPlayer != null) {
            status = 2;
            playMusic.set("正在播放");
            mMediaPlayer.start();
        }
    }


    private void pausePlay() {
        if (mMediaPlayer != null) {
            status = 3;
            mMediaPlayer.pause();
            playMusic.set("播放音乐");
        }
    }


    private void releasePlay() {
        if (mMediaPlayer != null) {
            status = 4;
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }


    @Override
    public void audioStart() {
        if (status == 1 || status == 3) {
            startPlay();
        }
    }


    @Override
    public void audioPause() {
        if (status == 2) {
            pausePlay();
        }
    }


}