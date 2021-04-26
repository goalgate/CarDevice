package com.example.cardevice.ui.video;


import android.os.Bundle;
import com.example.cardevice.BR;
import com.example.cardevice.R;
import com.example.cardevice.databinding.ActivityVideoBinding;

import me.goldze.mvvmhabit.base.BaseActivity;

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

