package com.example.cardevice.data;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.example.cardevice.data.httpsource.HttpDataSource;
import com.example.cardevice.data.localsource.LocalDataSource;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseModel;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @author Created by WZW on 2021-04-19 15:55.
 * @description
 */

public class XunGengRepository extends BaseModel implements LocalDataSource, HttpDataSource {

    private volatile static XunGengRepository INSTANCE = null;

    private final HttpDataSource mHttpDataSource;

    private final LocalDataSource mLocalDataSource;

    private XunGengRepository(@NonNull HttpDataSource httpDataSource,
                              @NonNull LocalDataSource localDataSource) {
        this.mHttpDataSource = httpDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static XunGengRepository getInstance(HttpDataSource httpDataSource,
                                                LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (XunGengRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new XunGengRepository(httpDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<ResponseBody> login(RequestBody body) {
        return mHttpDataSource.login(body);
    }

    @Override
    public Observable<ResponseBody> updata(String token, RequestBody body) {
        return mHttpDataSource.updata(token,body);
    }

    @Override
    public void saveUserName(String userName) {
        mLocalDataSource.saveUserName(userName);
    }

    @Override
    public void savePassword(String password) {
        mLocalDataSource.savePassword(password);

    }

    @Override
    public String getUserName() {
        return mLocalDataSource.getUserName();
    }

    @Override
    public String getPassword() {
        return mLocalDataSource.getPassword();
    }
}
