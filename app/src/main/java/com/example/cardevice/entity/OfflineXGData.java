package com.example.cardevice.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author Created by WZW on 2021-04-20 14:19.
 * @description
 */

@Entity
public class OfflineXGData {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "method")
    private String method;

    @ColumnInfo(name = "content")
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
