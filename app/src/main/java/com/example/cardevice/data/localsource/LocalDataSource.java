package com.example.cardevice.data.localsource;

/**
 * @author Created by WZW on 2021-04-19 16:17.
 * @description
 */
public interface LocalDataSource {

    /**
     * 保存用户名
     */
    void saveUserName(String userName);

    /**
     * 保存用户密码
     */

    void savePassword(String password);

    /**
     * 获取用户名
     */
    String getUserName();

    /**
     * 获取用户密码
     */
    String getPassword();
}
