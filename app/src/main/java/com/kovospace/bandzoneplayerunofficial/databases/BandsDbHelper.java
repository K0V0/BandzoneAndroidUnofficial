package com.kovospace.bandzoneplayerunofficial.databases;

import android.app.Activity;
import android.content.Context;
import com.kovospace.bandzoneplayerunofficial.objects.Band;

import java.util.List;

public class BandsDbHelper extends DbHelper {
    private static BandEntity bandEntity;

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

    public static void delete(String slug) {
        offlineBandsRoomDatabase.bandEntityDao().delete(slug);
    }

    public static List<BandEntity> findByName(String bandName) {
        return offlineBandsRoomDatabase.bandEntityDao().findByName(bandName);
    }

    public static List<BandEntity> findByName(String bandName, int limit) {
        return offlineBandsRoomDatabase.bandEntityDao().findByName(bandName, limit);
    }

    public static List<BandEntity> findByName(String bandName, int limit, int offset) {
        return offlineBandsRoomDatabase.bandEntityDao().findByName(bandName, limit, offset);
    }

    public static BandEntity findFirstBySlug(String slug) {
        return offlineBandsRoomDatabase.bandEntityDao().findFirstBySlug(slug).get(0);
    }

    public static boolean exist(String slug) {
        if (offlineBandsRoomDatabase.bandEntityDao().findFirstBySlug(slug) == null) {
            return false;
        }
        return true;
    }

    public static int getCount(String bandName) {
        return offlineBandsRoomDatabase.bandEntityDao().getCount(bandName);
    }
}
