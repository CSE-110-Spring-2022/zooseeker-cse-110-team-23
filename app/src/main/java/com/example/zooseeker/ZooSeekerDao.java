//package com.example.zooseeker;
//
//import androidx.room.Dao;
//import androidx.room.Delete;
//import androidx.room.Insert;
//import androidx.room.Query;
//import androidx.room.Update;
//
//import java.util.*;
//
//@Dao
//public interface ZooSeekerDao {
//    @Insert
//    long insert(TodoListItem todoListItem);
//
//    @Insert
//    List<Long> insertAll(List<TodoListItem> todoListItem);
//
//    @Query("SELECT * FROM `todo_list_items` WHERE `id`=:id")
//    TodoListItem get(long id);
//
//    @Query("SELECT * FROM `todo_list_items` ORDER BY `order`")
//    List<TodoListItem> getAll();
//
//    @Update
//    int update(TodoListItem todoListItem);
//
//    @Delete
//    int delete(TodoListItem todoListItem);
//}
