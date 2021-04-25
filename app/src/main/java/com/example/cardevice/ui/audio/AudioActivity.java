package com.example.cardevice.ui.audio;

import android.Manifest;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cardevice.R;
import com.example.cardevice.databinding.ActivityAudioBinding;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.BR;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author Created by WZW on 2021-04-23 14:31.
 * @description
 */

public class AudioActivity extends BaseActivity<ActivityAudioBinding, AudioViewModel> {


    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_audio;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


    @Override
    public void initParam() {
        super.initParam();
    }


}

