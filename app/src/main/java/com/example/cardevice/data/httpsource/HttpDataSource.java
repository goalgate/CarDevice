package com.example.cardevice.data.httpsource;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Header;

/**
 * @author Created by WZW on 2021-04-19 16:19.
 * @description
 */
public interface HttpDataSource {
    Observable<ResponseBody> login(RequestBody body);

    Observable<ResponseBody> updata(String token, RequestBody body);

}
