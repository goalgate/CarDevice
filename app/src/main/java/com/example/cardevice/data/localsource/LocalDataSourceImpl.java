package com.example.cardevice.data.localsource;

import me.goldze.mvvmhabit.utils.SPUtils;

/**
 * @author Created by WZW on 2021-04-19 16:20.
 * @description
 */
public class LocalDataSourceImpl implements LocalDataSource {

    private volatile static LocalDataSourceImpl INSTANCE = null;

    private LocalDataSourceImpl() {

    }

    public static LocalDataSourceImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (LocalDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocalDataSourceImpl();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public void saveUserName(String userName) {
        SPUtils.getInstance("config").put("UserName", userName);

    }

    @Override
    public void savePassword(String password) {
        SPUtils.getInstance("config").put("Password", password);

    }

    @Override
    public String getUserName() {
        return SPUtils.getInstance("config").getString("UserName");
    }

    @Override
    public String getPassword() {
        return SPUtils.getInstance("config").getString("Password");
    }
}
