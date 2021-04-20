package com.example.cardevice.ui.xungengic;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;

import com.example.cardevice.BR;
import com.example.cardevice.R;
import com.example.cardevice.app.AppViewModelFactory;
import com.example.cardevice.databinding.ActivityXungengmainBinding;
import com.example.cardevice.ui.login.LoginActivity;
import com.example.cardevice.ui.login.LoginViewModel;
import com.example.cardevice.utils.ByteTools;
import com.jakewharton.rxbinding2.widget.RxTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author Created by WZW on 2021-04-19 11:56.
 * @description
 */

public class XunGengMainActivity extends BaseActivity<ActivityXungengmainBinding, XunGengMainViewModel> {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    SPUtils config = SPUtils.getInstance("config");

    Disposable disposableTips;

    private NfcAdapter mNfcAdapter = null;

    private PendingIntent mPendingIntent;

    private String userRealName;

    String token;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_xungengmain;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {
        userRealName = getIntent().getExtras().getString("userRealName");
        token = getIntent().getExtras().getString("token");
        NfcCheck();
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);
    }

    @Override
    public XunGengMainViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(XunGengMainViewModel.class);
    }

    @Override
    public void initData() {
        viewModel.userName.set(userRealName + ",欢迎您!");
        viewModel.token.set(token);
        disposableTips = RxTextView.textChanges(binding.tvInfo)
                .debounce(10, TimeUnit.SECONDS)
                .switchMap(new Function<CharSequence, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull CharSequence charSequence) throws Exception {
                        return Observable.just("请将巡更棒靠近巡更点处进行巡更操作");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        binding.tvInfo.setText(s);
                    }
                });

    }

    @Override
    public void initViewObservable() {
        viewModel.uc.backConfirm.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                new AlertView("是否退出", null, "取消", new String[]{"确定"}, null, XunGengMainActivity.this, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == 0) {
                            startActivity(LoginActivity.class);
                            finish();
                        }
                    }
                }).show();
            }
        });
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
                //设置结果显示框的显示数值
                Log.e("result", result);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("xgsbBianhao", config.getString("daid"));
                jsonObject.put("xgdBianhao", result.toUpperCase());
                jsonObject.put("xgTime", formatter.format(new Date(System.currentTimeMillis())));
                viewModel.xungengUpdata(jsonObject.toString(), result.toUpperCase());
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                ToastUtils.showLong(e.toString());
            }
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposableTips.dispose();
    }
}
