package com.example.cardevice.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cardevice.entity.OfflineXGData;

import java.util.List;

/**
 * @author Created by WZW on 2021-04-20 14:44.
 * @description
 */


@Dao
public interface OfflineXGDao {


    @Insert
    void insert(OfflineXGData... offlineXGData);

    @Update
    void update(OfflineXGData... offlineXGData);

    @Delete
    void delete(OfflineXGData... offlineXGData);

    @Query("DELETE FROM offlinexg")
    void deleteAll();

    @Query("SELECT * FROM offlinexg ORDER BY ID DESC")
    List<OfflineXGData> getAll();


}
