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
import androidx.room.PrimaryKey;

public class GraphListItem {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String kind;
    public String animal_id;
    public String name;
    public List<String> tags;

    GraphListItem(String name, String animal_id, String kind, List<String> tags) {
        this.name = name;
        this.animal_id = animal_id;
        this.kind = kind;
        this.tags = tags;

    }

    public static List<GraphListItem> loadJSON(Context context, String path) {
        try {
            InputStream inputStream = context.getAssets().open(path);
            Reader reader = new InputStreamReader(inputStream);

            Gson gson = new Gson();
            Type type = new TypeToken<List<ZooData.VertexInfo>>() {
            }.getType();
            List<ZooData.VertexInfo> zooData = gson.fromJson(reader, type);

            // This code is equivalent to:
            //
            // Map<String, ZooData.VertexInfo> indexedZooData = new HashMap();
            // for (ZooData.VertexInfo datum : zooData) {
            //   indexedZooData[datum.id] = datum;
            // }
            //
            Map<String, ZooData.VertexInfo> indexedZooData = zooData
                    .stream()
                    .collect(Collectors.toMap(v -> v.id, datum -> datum));

            //List<AnimalListItem> theList = Collections.emptyList();
            List<GraphListItem> theList = new ArrayList<GraphListItem>(10);

            Map<String, ZooData.VertexInfo> vInfo = indexedZooData;
            //ZooData.loadVertexInfoJSON("sample_node_info.json");

            for (String name : vInfo.keySet()) {
                GraphListItem animal = new GraphListItem(vInfo.get(name).name, vInfo.get(name).id, vInfo.get(name).kind.toString(), vInfo.get(name).tags);
                theList.add(animal);
            }
            return theList;
        }
        catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

    }


//    public static List<AnimalListItem> loadJSON(Context context, String path) {
//        try {
//            InputStream input = context.getAssets().open(path);
//            Reader reader = new InputStreamReader(input);
//            Gson gson = new Gson();
//            Type type = new TypeToken<List<AnimalListItem>>(){}.getType();
//            return gson.fromJson(reader, type);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return Collections.emptyList();
//        }
//    }

    @Override
    public String toString() {
        return "GraphListItem{" +
                "id=" + id +
                ", kind='" + kind + '\'' +
                ", animal_id='" + animal_id + '\'' +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                '}';
    }
}
