package com.kovospace.bandzoneplayerunofficial.databases;

import androidx.room.*;

import java.util.List;

@Dao
public interface BandEntityDao {

    @Query("SELECT * FROM offlineBands WHERE bandSlug = :slug")
    List<BandEntity> findBySlug(String slug);

    @Query("SELECT * FROM offlineBands WHERE bandTitle LIKE '%' || :title || '%'")
    List<BandEntity> findByName(String title);

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