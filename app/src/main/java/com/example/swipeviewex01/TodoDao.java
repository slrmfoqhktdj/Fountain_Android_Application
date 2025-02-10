package com.example.swipeviewex01;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface TodoDao {
    @Query("SELECT * FROM Todo WHERE user = (:user)")
    LiveData<List<Todo>> getAll(String user);

    @Query("SELECT device FROM Todo WHERE device = (:device) AND user = (:user)")
    String select(String user,String device);

//    @Query("SELECT device FROM Todo WHERE user = (:user)")
//    List<String> selects(String user);

    @Query("SELECT * FROM Todo WHERE user = (:user) AND device = (:device)")
    LiveData<List<Todo>> dataselect(String user,String device);

    @Query("SELECT redevice FROM Todo WHERE user = (:user) AND device = (:device)")
    String redeviceselect(String user,String device);

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    Completable insert(Todo todo);
    @Query("INSERT INTO Todo(user,device,redevice,water,onoff,error,step,mode) VALUES (:user,:device,:redevice,:water,:onoff,:error,:step,:mode)")
    Completable insert(String user,String device,String redevice,String water,String onoff,String error,String step,String mode);
//    @ColumnInfo(name = "user")
//    private String user;
//    @ColumnInfo(name = "device")
//    private String device;
//    @ColumnInfo(name = "redevice")
//    private String redevice;
//    @ColumnInfo(name = "water")
//    private String water;
//    @ColumnInfo(name = "onoff")
//    private String onoff;
//    @ColumnInfo(name = "error")
//    private String error;
//    @ColumnInfo(name = "errorcode")
//    private String errorcode;
//    @ColumnInfo(name = "mode")
//    private String mode;
//    @ColumnInfo(name = "step")
//    private String step;

    @Query("UPDATE Todo SET water  = (:value) WHERE device = (:device)")
    void waterupdate(String device, String value);

    @Query("UPDATE Todo SET onoff  = (:value) WHERE device = (:device)")
    void onoffupdate(String device, String value);

    @Query("UPDATE Todo SET error  = (:value) WHERE device = (:device)")
    void errorupdate(String device, String value);

    @Query("UPDATE Todo SET mode  = (:value) WHERE device = (:device)")
    void modeupdate(String device, String value);

    @Query("UPDATE Todo SET step  = (:value) WHERE device = (:device)")
    void stepupdate(String device, String value);

    @Query("UPDATE Todo SET redevice  = (:value) WHERE redevice = (:redevice) AND user = (:user)")
    Completable devicenameupdate(String user,String redevice, String value);

    @Query("UPDATE Todo SET water = (:water),onoff = (:onoff),error = (:error),step = (:step),mode = (:mode) WHERE device = (:device)")
    void confirmupdate(String device, String water,String onoff,String error,String step,String mode);

    @Query("DELETE FROM Todo WHERE user = (:user) AND redevice = (:device)")
    Completable devicedelete(String user,String device);





    @Query("SELECT * FROM Picture WHERE user = (:user)")
    LiveData<List<Picture>> pgetAll(String user);

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void pinsert(Picture picture);

//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "uid")
//    private int uid;
//    @ColumnInfo(name = "time")
//    private String time;
//    @ColumnInfo(name = "device")
//    private String device;
//    @ColumnInfo(name = "redevice")
//    private String redevice;
//    @ColumnInfo(name = "type")
//    private String type;
//    @ColumnInfo(name = "user")
//    private String user;
//    @ColumnInfo(name = "file")
//    private String file;

    @Query("INSERT INTO Picture(time,device,redevice,type,user,file) VALUES (:time,:device,:redevice,:type,:user,:file)")
    void pinsert(String time,String device,String redevice,String type,String user,String file);

    @Query("DELETE FROM Picture WHERE device = (:device) AND time = (:time)")
    Completable pdelete(String device,String time);


    @Query("UPDATE Picture SET redevice  = (:value) WHERE redevice = (:redevice) AND user = (:user)")
    Completable pdevicenameupdate(String user,String redevice, String value);
}
