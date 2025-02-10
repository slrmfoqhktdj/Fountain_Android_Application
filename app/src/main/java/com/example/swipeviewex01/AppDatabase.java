package com.example.swipeviewex01;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {Todo.class,Picture.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TodoDao todoDao();

//    private static AppDatabase INSTANCE;
    //volatile(앱의 캐쉬데이터에 저장하는게 아니라 cpu에 바로저장하고 불러들임)싱글톤의 단점을 보안해줌. 실행기 빌드오류가 뜸
    private static volatile AppDatabase INSTANCE;
//    https://jw910911.tistory.com/56   - 필요성
//    https://stackoverflow.com/questions/47127437/room-illegalargumentexception-intcannot-be-converted-to-an-element        -- 해결

    public static AppDatabase getInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if(INSTANCE ==null){
                    INSTANCE = Room.databaseBuilder(
                            context,
                            AppDatabase.class,
                            "todo_db")
                            .addMigrations(MIGRATION_1_2,MIGRATION_2_1)
                            .build();
                }

            }
        }

        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            Log.e("room migration","version up 1 to 2");

        }
    };

    static final Migration MIGRATION_2_1 = new Migration(2,1) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            Log.e("room migration","version down 2 to 1");

        }
    };

    public static void destoryInstance(){
        INSTANCE = null;
    }
}

