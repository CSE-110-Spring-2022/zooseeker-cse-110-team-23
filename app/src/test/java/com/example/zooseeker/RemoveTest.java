package com.example.zooseeker;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.util.ArrayList;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RemoveTest {
    @Test
    public void Remove() {
        ArrayList<AnimalListItem> AnimalList = new ArrayList<>(0);
        AnimalList.add(new AnimalListItem("hi","bye",1));
        AnimalList.add(new AnimalListItem("hi2","bye2",3));
        AnimalList.clear();
        assertEquals(0, AnimalList.size());
        AnimalListDatabase hi = new AnimalListDatabase() {
            @Override
            public AnimalListItemDao animalListItemDao() {
                return null;
            }

            @NonNull
            @Override
            protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
                return null;
            }

            @NonNull
            @Override
            protected InvalidationTracker createInvalidationTracker() {
                return null;
            }

            @Override
            public void clearAllTables() {

            }
        };
    }
}