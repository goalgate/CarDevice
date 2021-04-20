package com.example.cardevice.data.httpsource;


import com.example.cardevice.data.httpsource.service.XungengApiService;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.utils.SPUtils;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @author Created by WZW on 2021-04-19 17:09.
 * @description
 */
public class HttpDataSourceImpl implements HttpDataSource {
    private XungengApiService apiService;
    private volatile static HttpDataSourceImpl INSTANCE = null;

    public static HttpDataSourceImpl getInstance(XungengApiService apiService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private HttpDataSourceImpl(XungengApiService apiService) {
        this.apiService = apiService;
    }



    @Override
    public Observable<ResponseBody> login(RequestBody body) {
        return apiService.login(SPUtils.getInstance("config").getString("daid"), body);
    }

    @Override
    public Observable<ResponseBody> updata(String token, RequestBody body) {
        return apiService.updata(token, body);
    }
}
