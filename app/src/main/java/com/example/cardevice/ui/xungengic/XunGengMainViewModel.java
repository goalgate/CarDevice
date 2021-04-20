package com.example.cardevice.ui.xungengic;

import android.app.Application;
import android.hardware.Camera;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.example.cardevice.data.XunGengRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @author Created by WZW on 2021-04-19 11:56.
 * @description
 */


public class XunGengMainViewModel extends BaseViewModel<XunGengRepository> {

    public ObservableField<String> token = new ObservableField<>("");

    public ObservableField<String> userName = new ObservableField<>("");

    public ObservableField<String> tv_info = new ObservableField<>("");

    public XunGengMainViewModel.UIChangeObservable uc = new XunGengMainViewModel.UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> backConfirm = new SingleLiveEvent<>();
    }

    public XunGengMainViewModel(@NonNull Application application,XunGengRepository repository) {
        super(application, repository);
        tv_info.set("请将巡更棒靠近巡更点处进行巡更操作");
    }

    public View.OnClickListener lightUpDown = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            toggleLight();
        }
    };

    public BindingCommand changePerson = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.backConfirm.call();
        }
    });

    Camera camera;
    Camera.Parameters parameters;
    boolean hasClosed = true;

    public void toggleLight() {
        if (hasClosed) {
            camera = Camera.open();
            parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);// 开启
            camera.setParameters(parameters);
            hasClosed = false;
        } else {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);// 关闭
            camera.setParameters(parameters);
            hasClosed = true;
            camera.release();
        }
    }

    public void xungengUpdata(String jsonData, final String ICCard) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
        model.updata(token.get(), body)
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
                    if (jsonData.getInt("code") == 0) {
                        tv_info.set(jsonData.getString("info") + "\n\n该巡更点号为 " + ICCard);
                    } else if (jsonData.getInt("code") == 1) {
                        tv_info.set("巡更成功");
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
