package com.demo.telenorhealthassignment.model.local.room;
import android.content.Context;

import com.demo.telenorhealthassignment.util.Constants;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {BookDataEntry.class}, version = 1)
public abstract class BookDatabase extends RoomDatabase {
    private static volatile BookDatabase instance;

    public abstract BookDataDao bookDataDao();

    public static synchronized BookDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (BookDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            BookDatabase.class, Constants.DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
