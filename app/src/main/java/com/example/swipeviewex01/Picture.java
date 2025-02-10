package com.example.swipeviewex01;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Picture {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    private int uid;
    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "device")
    private String device;
    @ColumnInfo(name = "redevice")
    private String redevice;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "user")
    private String user;
    @ColumnInfo(name = "file")
    private String file;

    protected Picture(String time,String device,String redevice,String type,String user,String file){
        this.time = time;
        this.device = device;
        this.redevice = redevice;
        this.type = type;
        this.user = user;
        this.file = file;
    }

    protected int getUid() {
        return uid;
    }

    protected void setUid(int uid) {
        this.uid = uid;
    }

    protected String getTime() {
        return time;
    }

    protected void setTime(String time) {
        this.time = time;
    }

    protected String getDevice() {
        return device;
    }

    protected void setDevice(String device) {
        this.device = device;
    }

    protected String getRedevice() {
        return redevice;
    }

    protected void setRedevice(String redevice) {
        this.redevice = redevice;
    }

    protected String getType() {
        return type;
    }

    protected void setType(String type) {
        this.type = type;
    }

    protected String getUser() {
        return user;
    }

    protected void setUser(String user) {
        this.user = user;
    }

    protected String getFile() {
        return file;
    }

    protected void setFile(String filename) {
        this.file = file;
    }



    @Override
    public String toString() {
        return "Picture{" +
                "uid=" + uid + '\'' +
                ", time='" + time + '\'' +
                ", device='" + device + '\'' +
                ", redevice='" + redevice + '\'' +
                ", type='" + type + '\'' +
                ", user='" + user + '\'' +
                ", file='" + file + '\'' +
                '}';
    }
}
