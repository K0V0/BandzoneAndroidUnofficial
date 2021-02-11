package com.kovospace.bandzoneplayerunofficial.databases;

import androidx.room.*;

import java.util.List;

@Dao
public interface BandEntityDao {
    /*
    @Query("DELETE FROM offlineBands WHERE bandSlug = :slug")
    void deleteProduct(String slug);

    @Query("SELECT * FROM offlineBands")
    LiveData<List<BandEntity>> getAllOfflineBands();
    */

    @Query("SELECT * FROM offlineBands WHERE bandSlug = :slug")
    List<BandEntity> find(String slug);

    @Query("SELECT * FROM offlineBands")
    List<BandEntity> getAll();

    @Insert
    void insert(BandEntity bandEntity);

    @Update
    void update(BandEntity bandEntity);

    @Delete
    void delete(BandEntity bandEntity);

    @Delete
    void delete(BandEntity... bandEntities);

    @Query("DELETE FROM offlineBands")
    void deleteAll();
}
