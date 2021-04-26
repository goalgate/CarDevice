package com.example.cardevice.app;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.cardevice.dao.OfflineXGDao;
import com.example.cardevice.entity.OfflineXGData;

/**
 * @author Created by WZW on 2021-04-26 16:39.
 * @description
 */

@Database(entities = {OfflineXGData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract OfflineXGDao offlineXGDao();
}
