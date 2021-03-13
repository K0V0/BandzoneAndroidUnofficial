package com.kovospace.bandzoneplayerunofficial.databases;

import androidx.room.*;

import java.util.List;

@Dao
public interface BandEntityDao {

    @Query("SELECT * FROM offlineBands WHERE bandSlug = :slug")
    List<BandEntity> findBySlug(String slug);

    @Query("SELECT * FROM offlineBands WHERE bandSlug = :slug LIMIT 1")
    List<BandEntity> findFirstBySlug(String slug);

    @Query("SELECT * FROM offlineBands WHERE bandTitle LIKE '%' || :title || '%' ORDER BY bandTitle")
    List<BandEntity> findByName(String title);

    @Query("SELECT * FROM offlineBands WHERE bandTitle LIKE '%' || :title || '%' ORDER BY bandTitle LIMIT :limit")
    List<BandEntity> findByName(String title, int limit);

    @Query("SELECT * FROM offlineBands WHERE bandTitle LIKE '%' || :title || '%' ORDER BY bandTitle LIMIT :limit OFFSET :offset")
    List<BandEntity> findByName(String title, int limit, int offset);

    @Query("SELECT COUNT(*) FROM offlineBands WHERE bandTitle LIKE '%' || :title || '%'")
    int getCount(String title);

    @Query("SELECT * FROM offlineBands ORDER BY bandTitle")
    List<BandEntity> getAll();

    @Insert
    void insert(BandEntity bandEntity);

    @Update
    void update(BandEntity bandEntity);

    @Delete
    void delete(BandEntity bandEntity); // not working, maybe because making "new" entity ?

    @Query("DELETE FROM offlineBands WHERE bandSlug = :slug")
    void delete(String slug);

    @Delete
    void delete(BandEntity... bandEntities);

    @Query("DELETE FROM offlineBands")
    void deleteAll();
}
