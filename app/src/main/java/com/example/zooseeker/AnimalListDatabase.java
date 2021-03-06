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

@Database(entities = {AnimalListItem.class}, version =  1)
public abstract class AnimalListDatabase extends RoomDatabase {
    private static AnimalListDatabase singleton = null;

    public abstract AnimalListItemDao animalListItemDao();

    public synchronized static AnimalListDatabase getSingleton(Context context) {
        if(singleton == null) {
            singleton = AnimalListDatabase.makeDatabase(context);
        }
        return singleton;
    }

    private static AnimalListDatabase makeDatabase(Context context) {
        return Room.databaseBuilder (context, AnimalListDatabase.class, "animal_planner2.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            List<AnimalListItem> animals = AnimalListItem
                                    .loadJSON(context, "sample_node_info.json");
                            getSingleton(context).animalListItemDao().insertAll(animals);
                        });
                    }
                })
                .build();
    }

    @VisibleForTesting
    public static void injectTestDatabase(AnimalListDatabase testDatabase) {
        if(singleton != null) {
            singleton.close();
        }
        singleton = testDatabase;
    }
}
