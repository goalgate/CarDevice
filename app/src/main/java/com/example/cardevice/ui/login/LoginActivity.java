package com.example.cardevice.ui.login;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.cardevice.BR;
import com.example.cardevice.R;
import com.example.cardevice.app.AppViewModelFactory;
import com.example.cardevice.databinding.ActivityLoginBinding;
import com.example.cardevice.utils.ByteTools;
import com.example.cardevice.utils.NetInfo;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author Created by WZW on 2021-04-19 9:12.
 * @description
 */

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {

    String[] permissions = new String[]{
            Manifest.permission.VIBRATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA
    };

    //1.声明一个nfc的Adapter
    private NfcAdapter mNfcAdapter = null;

    private PendingIntent mPendingIntent;

    SPUtils config = SPUtils.getInstance("config");

    Vibrator vibrator;


    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public LoginViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(LoginViewModel.class);
    }

    @Override
    public void initParam() {
        requestCameraPermissions();
        ServerViewInit();
        NfcCheck();
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);
    }

    @Override
    public void initData() {
        if (config.getBoolean("firstStart", true)) {
//            config.put("daid", new NetInfo().getMacId());
            config.put("daid", "254113-195157-239124");
            config.put("ServerId", "https://gzwb.wxhxp.cn:9905/");
            config.put("firstStart", false);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }


    @Override
    public void initViewObservable() {
        //监听ViewModel中pSwitchObservable的变化, 当ViewModel中执行【uc.pSwitchObservable.set(!uc.pSwitchObservable.get());】时会回调该方法
        viewModel.uc.pSwitchEvent.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                //pSwitchObservable是boolean类型的观察者,所以可以直接使用它的值改变密码开关的图标
                if (viewModel.uc.pSwitchEvent.getValue()) {
                    //密码可见
                    //在xml中定义id后,使用binding可以直接拿到这个view的引用,不再需要findViewById去找控件了
                    binding.ivSwichPasswrod.setImageResource(R.mipmap.show_psw);
                    binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //密码不可见
                    binding.ivSwichPasswrod.setImageResource(R.mipmap.show_psw_press);
                    binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        viewModel.uc.serverEditEvent.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                ServerView.show();
            }
        });
    }


    private void requestCameraPermissions() {
        RxPermissions rxPermissions = new RxPermissions(LoginActivity.this);
        rxPermissions.request(permissions)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
//                            ToastUtils.showShort("权限已授权");
                        } else {
                            ToastUtils.showShort("权限被拒绝");
                        }
                    }
                });
    }

    private void NfcCheck() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);//3.获取nfc适配器
        if (mNfcAdapter == null) {
            return;
        } else {
            if (!mNfcAdapter.isEnabled()) {
                Intent setNfc = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(setNfc);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            byte[] myNFCID = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            try {
                String result = ByteTools.bytesToHexString(myNFCID);
                ToastUtils.showLong("尚未登录人员信息，无法登记巡更记录\n\n" + "当前巡更号为" + result);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private AlertView ServerView;

    private void ServerViewInit() {
        ViewGroup mServerEditor = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.server_editor, null);
        final EditText et_Server = (EditText) mServerEditor.findViewById(R.id.info_input);
        et_Server.setText(config.getString("ServerId"));
        ServerView = new AlertView("服务器设置", null, "取消", new String[]{"确定"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    config.put("ServerId", et_Server.getText().toString());
                    ToastUtils.showLong("服务器已被设置为：\n" + config.getString("ServerId"));
                }
            }
        });
        ServerView.addExtView(mServerEditor);
    }
}
