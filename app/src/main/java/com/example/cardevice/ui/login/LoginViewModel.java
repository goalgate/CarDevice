package com.example.cardevice.ui.login;

import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;


import com.example.cardevice.data.XunGengRepository;
import com.example.cardevice.ui.xungengic.XunGengMainActivity;
import com.example.cardevice.utils.Encryption.SM3;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


/**
 * @author Created by WZW on 2021-04-19 9:11.
 * @description
 */
public class LoginViewModel extends BaseViewModel<XunGengRepository> {

    //用户名的绑定
    public ObservableField<String> userName = new ObservableField<>("");
    //密码的绑定
    public ObservableField<String> password = new ObservableField<>("");
    //用户名清除按钮的显示隐藏绑定
    public ObservableInt clearBtnVisibility = new ObservableInt();
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();
    //封装一个界面发生改变的观察者
    public ObservableField<String> macId = new ObservableField<>("");

    public LoginViewModel(@NonNull Application application, XunGengRepository repository) {
        super(application, repository);
        //从本地取得数据绑定到View层
        userName.set(model.getUserName());
        password.set(model.getPassword());
        macId.set("当前设备ID号为" + SPUtils.getInstance("config").getString("daid"));
    }

    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> serverEditEvent = new SingleLiveEvent<>();
    }

    //清除用户名的点击事件, 逻辑从View层转换到ViewModel层
    public BindingCommand clearUserNameOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            userName.set("");
        }
    });

    //密码显示开关  (你可以尝试着狂按这个按钮,会发现它有防多次点击的功能)
    public BindingCommand passwordShowSwitchOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //让观察者的数据改变,逻辑从ViewModel层转到View层，在View层的监听则会被调用
            uc.pSwitchEvent.setValue(uc.pSwitchEvent.getValue() == null || !uc.pSwitchEvent.getValue());
        }
    });

    //用户名输入框焦点改变的回调事件
    public BindingCommand<Boolean> onFocusChangeCommand = new BindingCommand<>(new BindingConsumer<Boolean>() {
        @Override
        public void call(Boolean hasFocus) {
            if (hasFocus) {
                clearBtnVisibility.set(View.VISIBLE);
            } else {
                clearBtnVisibility.set(View.INVISIBLE);
            }
        }
    });

    //登录按钮的点击事件
    public BindingCommand loginOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            login();
        }
    });

    //服务器地址编辑
    public BindingCommand<Boolean> ServerEditCommand = new BindingCommand<>(new BindingAction() {
        @Override
        public void call() {
            uc.serverEditEvent.call();
        }
    });

    private void login() {
        if (TextUtils.isEmpty(userName.get())) {
            me.goldze.mvvmhabit.utils.ToastUtils.showShort("请输入账号！");
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtils.showShort("请输入密码！");
            return;
        }
        //RaJava模拟登录
        model.saveUserName(userName.get());
        model.savePassword(password.get());
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("username", userName.get());
            jsonObject.put("password", SM3.getSM3(password.get()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        model.login(body)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(this)//请求与ViewModel周期同步
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog("正在请求...");
                    }
                }).subscribe(new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonData = new JSONObject(responseBody.string());
                    if (jsonData.getInt("code") == 1) {
                        JSONObject data = new JSONObject(jsonData.getString("data"));
                        Bundle bundle = new Bundle();
                        bundle.putString("token", data.getString("token"));
                        bundle.putString("userRealName", data.getString("userRealName"));
                        startActivity(XunGengMainActivity.class, bundle);
                    } else if (jsonData.getInt("code") == 0) {
                        ToastUtils.showLong(jsonData.getString("info"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                dismissDialog();
            }

            @Override
            public void onComplete() {
                dismissDialog();
            }
        });
    }
}
