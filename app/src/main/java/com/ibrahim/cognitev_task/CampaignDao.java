package com.ibrahim.cognitev_task;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


@Dao
public interface CampaignDao {
    @Insert(onConflict = REPLACE)
    void insert(Campaign campaign);

    @Insert(onConflict = REPLACE)
    void insertAll(List<Campaign> campaigns);


    @Query("DELETE FROM Campaign where country")
    void deleteAllNotes();

    @RawQuery
    String[] getQuery(SupportSQLiteQuery sqLiteQuery);

    @RawQuery
    List<Campaign> getCount(SimpleSQLiteQuery simpleSQLiteQuery);



}
