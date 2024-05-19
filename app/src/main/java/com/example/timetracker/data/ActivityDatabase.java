package com.example.timetracker.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.timetracker.data.convert.TimeConvertor;
import com.example.timetracker.data.dao.ActivityDao;
import com.example.timetracker.data.dao.GroupDao;
import com.example.timetracker.data.dao.RecordDao;
import com.example.timetracker.data.model.Activity;
import com.example.timetracker.data.model.Group;
import com.example.timetracker.data.model.Record;


// singleton 单例模式
@Database(entities = {Activity.class, Group.class, Record.class}, version = 4, exportSchema = false)
@TypeConverters({TimeConvertor.class})
// 表示当前类是一个数据库类，entities表示数据库中的表，version表示数据库版本，exportSchema表示是否导出数据库结构
public abstract class ActivityDatabase extends RoomDatabase {
    private static ActivityDatabase INSTANCE;

    public static ActivityDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ActivityDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ActivityDatabase.class, "activity_database.db")
//                                .fallbackToDestructiveMigration()
//                                .addMigrations()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract ActivityDao getActivityDao();

    public abstract GroupDao getGroupDao();

    public abstract RecordDao getRecordDao();

//        static final Migration MIGRATION_2_3 = new Migration(1, 2) {
//
//            @Override
//            public void migrate(SupportSQLiteDatabase database) {
//                database.execSQL("ALTER TABLE activity ADD COLUMN time INTEGER");
//            }
//        };
}
