package com.kovospace.bandzoneplayerunofficial.databases;

import android.app.Activity;
import android.content.Context;
import com.kovospace.bandzoneplayerunofficial.objects.Band;

import java.util.List;

public class BandsDbHelper {
    private static Context context;
    private static BandEntity bandEntity;
    private static OfflineBandsRoomDatabase offlineBandsRoomDatabase;

    public static void init(Context c) {
        context = c;
        offlineBandsRoomDatabase = OfflineBandsRoomDatabase.getInstance((Activity) context);
    }

    public static void insertIfNotExist(Band band) {
        if (band != null && band.getTitle() != null) {
            bandEntity = new BandEntity(band);
            List<BandEntity> found = offlineBandsRoomDatabase.bandEntityDao().findBySlug(bandEntity.getSlug());
            if (found.size() == 0) {
                offlineBandsRoomDatabase.bandEntityDao().insert(bandEntity);
            }
        }
    }

    public static List<BandEntity> getAll() {
        return offlineBandsRoomDatabase.bandEntityDao().getAll();
    }

    public static void delete(Band band) {
        bandEntity = new BandEntity(band);
        offlineBandsRoomDatabase.bandEntityDao().delete(bandEntity);
    }

    public static List<BandEntity> findByName(String bandName) {
        return offlineBandsRoomDatabase.bandEntityDao().findByName(bandName);
    }
}
