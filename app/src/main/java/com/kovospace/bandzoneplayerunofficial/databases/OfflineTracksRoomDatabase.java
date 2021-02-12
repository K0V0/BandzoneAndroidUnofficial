package com.kovospace.bandzoneplayerunofficial.databases;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TrackEntity.class}, version = 4)
public abstract class OfflineTracksRoomDatabase extends RoomDatabase {

    public abstract TrackEntityDao trackEntityDao();

    private static OfflineTracksRoomDatabase offlineTracksDB;

    public static OfflineTracksRoomDatabase getInstance(Context context) {
        if (null == offlineTracksDB) {
            offlineTracksDB = buildDatabaseInstance(context);
        }
        return offlineTracksDB;
    }

    private static OfflineTracksRoomDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                OfflineTracksRoomDatabase.class,
                "offlineTracks_database")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    public void cleanUp(){
        offlineTracksDB = null;
    }

}
