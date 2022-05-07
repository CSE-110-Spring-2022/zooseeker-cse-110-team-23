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
    long insert(AnimalListItem animalListItem);

    @Insert
    List<Long> insertAll(List<AnimalListItem> animalListItem);

    @Query("SELECT * FROM `animal_list_items` WHERE `id`=:id")
    AnimalListItem get(long id);

    @Query("SELECT * FROM `animal_list_items` ORDER BY `order`")
    List<AnimalListItem> getAll();

    @Update
    int update(AnimalListItem animalListItem);

    @Delete
    int delete(AnimalListItem animalListItem);

    @Query("DELETE FROM `animal_list_items`")
    void nukeTable();

    @Query("SELECT * FROM `animal_list_items` ORDER BY `order`")
    LiveData<List<AnimalListItem>> getAllAlive();

    @Query("SELECT `order` + 1 FROM `animal_list_items` ORDER BY `order` DESC LIMIT 1")
    int getOrderForAppend();

    @Query("SELECT COUNT(*) FROM `animal_list_items`")
    int getSize();
}
