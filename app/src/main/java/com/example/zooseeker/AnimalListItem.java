package com.example.zooseeker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "animal_list_items")
public class AnimalListItem {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String text;
    public int order;
    public String animal_id;

    AnimalListItem(String text, String animal_id, int order) {
        this.text = text;
        this.animal_id = animal_id;
        this.order = order;

    }

    public static List<AnimalListItem> loadJSON(Context context, String path) {
        try {
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<AnimalListItem>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public String toString() {
        return "AnimalListItem{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", order=" + order +
                ", animal_id='" + animal_id + '\'' +
                '}';
    }
}

