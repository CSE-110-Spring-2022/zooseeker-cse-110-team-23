//package com.example.zooseeker;
//
//import com.google.gson.Gson;
//import com.google.gson.annotations.SerializedName;
//import com.google.gson.reflect.TypeToken;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.Reader;
//import java.lang.reflect.Type;
//import java.util.*;
//import java.util.stream.Collectors;
//
//import android.content.Context;
//
//import androidx.annotation.NonNull;
//import androidx.room.PrimaryKey;
//
//public class GraphListItem {
//    @PrimaryKey(autoGenerate = true)
//    public long id;
//
//    public static enum Kind {
//        // The SerializedName annotation tells GSON how to convert
//        // from the strings in our JSON to this Enum.
//        @SerializedName("gate") GATE,
//        @SerializedName("exhibit") EXHIBIT,
//        @SerializedName("intersection") INTERSECTION
//    }
//
//    @NonNull
//    public Kind kind;
//    public String animal_id;
//    public String name;
//    public List<String> tags;
//    public double lat;
//    public double lng;
//
//    GraphListItem(String name, String animal_id, Kind kind, List<String> tags, double lat, double lng) {
//        this.name = name;
//        this.animal_id = animal_id;
//        this.kind = kind;
//        this.tags = tags;
//        this.lat = lat;
//        this.lng = lng;
//    }
//}
