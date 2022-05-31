package com.example.zooseeker;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.nio.json.JSONImporter;

public class ZooData {
    public static class VertexInfo {
        public static enum Kind {
            // The SerializedName annotation tells GSON how to convert
            // from the strings in our JSON to this Enum.
            @SerializedName("gate") GATE,
            @SerializedName("exhibit") EXHIBIT,
            @SerializedName("intersection") INTERSECTION
        }

        public String id;
        public Kind kind;
        public String name;
        public List<String> tags;
        public double lat;
        public double lng;

        VertexInfo(String name, String animal_id, Kind kind, List<String> tags, double lat, double lng) {
            this.name = name;
            this.id = animal_id;
            this.kind = kind;
            this.tags = tags;
            this.lat = lat;
            this.lng = lng;
        }

    }

    public static class EdgeInfo {
        public String id;
        public String street;
    }

    public static Map<String, ZooData.VertexInfo> loadVertexInfoJSON(Context context, String path) {
        //InputStream inputStream = TestJSONActivity.class.getClassLoader().getResourceAsStream(path);
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Reader reader = new InputStreamReader(inputStream);

        Gson gson = new Gson();
        Type type = new TypeToken<List<ZooData.VertexInfo>>(){}.getType();
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

        return indexedZooData;
    }

    public static Map<String, ZooData.EdgeInfo> loadEdgeInfoJSON(Context context, String path) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Reader reader = new InputStreamReader(inputStream);

        Gson gson = new Gson();
        Type type = new TypeToken<List<ZooData.EdgeInfo>>(){}.getType();
        List<ZooData.EdgeInfo> zooData = gson.fromJson(reader, type);

        Map<String, ZooData.EdgeInfo> indexedZooData = zooData
                .stream()
                .collect(Collectors.toMap(v -> v.id, datum -> datum));

        return indexedZooData;
    }


    public static Graph<String, IdentifiedWeightedEdge> loadZooGraphJSON(Context context, String path) {
        // Create an empty graph to populate.
        Graph<String, IdentifiedWeightedEdge> g = new DefaultUndirectedWeightedGraph<>(IdentifiedWeightedEdge.class);

        // Create an importer that can be used to populate our empty graph.
        JSONImporter<String, IdentifiedWeightedEdge> importer = new JSONImporter<>();

        // We don't need to convert the vertices in the graph, so we return them as is.
        importer.setVertexFactory(v -> v);

        // We need to make sure we set the IDs on our edges from the 'id' attribute.
        // While this is automatic for vertices, it isn't for edges. We keep the
        // definition of this in the IdentifiedWeightedEdge class for convenience.
        importer.addEdgeAttributeConsumer(IdentifiedWeightedEdge::attributeConsumer);

        // On Android, you would use context.getAssets().open(path) here like in Lab 5.
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Reader reader = new InputStreamReader(inputStream);

        // And now we just import it!
        importer.importGraph(g, reader);

        return g;
    }

    public static List<VertexInfo> loadJSON(Context context, String path) {
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
            List<VertexInfo> theList = new ArrayList<VertexInfo>(10);

            Map<String, ZooData.VertexInfo> vInfo = indexedZooData;
            //ZooData.loadVertexInfoJSON("sample_node_info.json");

            for (String name : vInfo.keySet()) {
                VertexInfo animal = new VertexInfo(vInfo.get(name).name, vInfo.get(name).id, vInfo.get(name).kind, vInfo.get(name).tags, vInfo.get(name).lat, vInfo.get(name).lng);
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
}
