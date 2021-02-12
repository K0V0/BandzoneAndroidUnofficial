package com.kovospace.bandzoneplayerunofficial.databases;

import androidx.room.*;

import java.util.List;

@Dao
public interface TrackEntityDao {

    @Query("SELECT * FROM offlineTracks WHERE bandSlug = :slug")
    List<TrackEntity> findByBand(String slug);

    @Query("DELETE FROM offlineTracks WHERE bandSlug = :slug")
    void deleteByBand(String slug);

    @Query("SELECT * FROM offlineTracks WHERE bandSlug = :slug AND trackTitle = :title")
    List<TrackEntity> findByBandAndTitle(String slug, String title);

    @Query("SELECT * FROM offlineTracks")
    List<TrackEntity> getAll();

    @Insert
    void insert(TrackEntity trackEntity);

    @Update
    void update(TrackEntity trackEntity);

    @Delete
    void delete(TrackEntity trackEntity);

    @Delete
    void delete(TrackEntity... trackEntity);

    @Query("DELETE FROM offlineTracks")
    void deleteAll();
}
