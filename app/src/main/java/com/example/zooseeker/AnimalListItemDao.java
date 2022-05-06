package com.example.zooseeker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.*;

@Dao
public interface AnimalListItemDao {
    @Insert
    long insert(AnimalListItem todoListItem);

    @Insert
    List<Long> insertAll(List<AnimalListItem> todoListItem);

    @Query("SELECT * FROM `animal_list_items` WHERE `id`=:id")
    AnimalListItem get(long id);

    @Query("SELECT * FROM `animal_list_items` ORDER BY `order`")
    List<AnimalListItem> getAll();

    @Update
    int update(AnimalListItem todoListItem);

    @Delete
    int delete(AnimalListItem todoListItem);

    @Query("SELECT * FROM `animal_list_items` ORDER BY `order`")
    LiveData<List<AnimalListItem>> getAllAlive();

    @Query("SELECT `order` + 1 FROM `animal_list_items` ORDER BY `order` DESC LIMIT 1")
    int getOrderForAppend();

    @Query("SELECT COUNT(*) FROM `animal_list_items`")
    int getSize();
}
