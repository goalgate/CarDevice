package com.example.cardevice.ui.audio;


import android.os.Bundle;

import com.example.cardevice.BR;
import com.example.cardevice.R;
import com.example.cardevice.databinding.ActivityAudioBinding;

import me.goldze.mvvmhabit.base.BaseActivity;

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



}

