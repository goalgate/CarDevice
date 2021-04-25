package com.example.cardevice.ui.video;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.widget.MediaController;
import android.widget.Toast;

import com.example.cardevice.R;
import com.example.cardevice.databinding.ActivityAudioBinding;
import com.example.cardevice.databinding.ActivityVideoBinding;
import com.example.cardevice.ui.audio.AudioActivity;
import com.example.cardevice.ui.audio.AudioViewModel;
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
 * @author Created by WZW on 2021-04-25 10:38.
 * @description
 */

public class VideoActivity extends BaseActivity<ActivityVideoBinding, VideoViewModel> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_audio;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

}

