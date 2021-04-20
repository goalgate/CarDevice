package com.example.cardevice.app;


import com.example.cardevice.data.httpsource.HttpDataSource;
import com.example.cardevice.data.httpsource.HttpDataSourceImpl;
import com.example.cardevice.data.httpsource.service.XungengApiService;
import com.example.cardevice.data.localsource.LocalDataSource;
import com.example.cardevice.data.localsource.LocalDataSourceImpl;
import com.example.cardevice.data.XunGengRepository;
import com.example.cardevice.retrofit.RetrofitClient;

/**
 * @author Created by WZW on 2021-04-19 17:04.
 * @description
 */
public class Injection {

    public static XunGengRepository provideDemoRepository() {

        XungengApiService apiService = RetrofitClient.getInstance().create(XungengApiService.class);
        //网络数据源
        HttpDataSource httpDataSource = HttpDataSourceImpl.getInstance(apiService);
        //本地数据源
        LocalDataSource localDataSource = LocalDataSourceImpl.getInstance();
        //两条分支组成一个数据仓库
        return XunGengRepository.getInstance(httpDataSource, localDataSource);
    }
}
