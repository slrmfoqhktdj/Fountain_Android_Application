package com.example.swipeviewex01;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Todo {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    private int uid;
    @ColumnInfo(name = "user")
    private String user;
    @ColumnInfo(name = "device")
    private String device;
    @ColumnInfo(name = "redevice")
    private String redevice;
    @ColumnInfo(name = "water")
    private String water;
    @ColumnInfo(name = "onoff")
    private String onoff;
    @ColumnInfo(name = "error")
    private String error;
    @ColumnInfo(name = "mode")
    private String mode;
    @ColumnInfo(name = "step")
    private String step;

    protected Todo(String user,String device, String redevice, String water, String onoff, String error,String mode,String step){
        this.user = user;
        this.device = device;
        this.redevice = redevice;
        this.onoff = onoff;
        this.water = water;
        this.error = error;
        this.mode = mode;
        this.step = step;
    }

    protected String getUser() {
        return user;
    }

    protected void setUser(String user) {
        this.user = user;
    }

    protected String getRedevice() {
        return redevice;
    }

    protected void setRedevice(String redevice) {
        this.redevice = redevice;
    }

    protected int getUid() {
        return uid;
    }

    protected void setUid(int uid) {
        this.uid = uid;
    }

    protected String getDevice() {
        return device;
    }

    protected void setDevice(String device) {
        this.device = device;
    }

    protected String getOnoff() {
        return onoff;
    }

    protected void setOnoff(String onoff) {
        this.onoff = onoff;
    }

    protected String getWater() {
        return water;
    }

    protected void setWater(String water) {
        this.water = water;
    }

    protected String getError() {
        return error;
    }

    protected void setError(String error) {
        this.error = error;
    }

    protected String getMode() {
        return mode;
    }

    protected void setMode(String mode) {
        this.mode = mode;
    }

    protected String getStep() {
        return step;
    }

    protected void setStep(String step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "uid=" + uid +
                ", user='" + user + '\'' +
                ", device='" + device + '\'' +
                ", redevice='" + redevice + '\'' +
                ", water='" + water + '\'' +
                ", onoff='" + onoff + '\'' +
                ", error='" + error + '\'' +
                ", mode='" + mode + '\'' +
                ", step='" + step + '\'' +
                '}';
    }
//    public static DiffUtil.ItemCallback<Todo> DIFF_CALLBACK = new  DiffUtil.ItemCallback<Todo>() {
//        @Override
//        public boolean areItemsTheSame(@NonNull Todo oldItem, @NonNull Todo newItem) {
//            return oldItem.device == newItem.device;
//        }
//
//        @Override
//        public boolean areContentsTheSame(@NonNull Todo oldItem, @NonNull Todo newItem) {
//            return oldItem.equals(newItem);
//        }
//
//    };
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == this)
//            return true;
//        Todo myEntity = (Todo) obj;
//        return myEntity.device == this.device && myEntity.redevice == this.redevice;
//    }

}
