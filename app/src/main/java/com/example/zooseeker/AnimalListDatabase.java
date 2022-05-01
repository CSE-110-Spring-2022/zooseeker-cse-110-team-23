package com.example.zooseeker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.*;
import java.util.concurrent.Executors;

@Database(entities = {AnimalListItem.class}, version = 1)
public abstract class AnimalListDatabase extends RoomDatabase {
    public abstract AnimalListItemDao animalListItemDao();
}
