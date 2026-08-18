package com.kovospace.bandzoneplayerunofficial.databases;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {TrackEntity.class}, version = 6)
public abstract class OfflineTracksRoomDatabase extends RoomDatabase {

    public abstract TrackEntityDao trackEntityDao();

    private static OfflineTracksRoomDatabase offlineTracksDB;

    // Adding durationMs must not drop the table: the mp3 files would survive on disk but offline
    // mode lists tracks from here, so a destructive upgrade would hide a downloaded library until
    // every band got reopened online.
    private static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE offlineTracks ADD COLUMN durationMs INTEGER");
        }
    };

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
                .addMigrations(MIGRATION_5_6)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    public void cleanUp(){
        offlineTracksDB = null;
    }

}
