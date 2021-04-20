package com.example.cardevice.dao;

import androidx.room.Dao;
import androidx.room.Insert;

/**
 * @author Created by WZW on 2021-04-20 14:44.
 * @description
 */
@Dao
public interface OfflineXGDao {
    @Insert
    void insertOfflineData();
}
