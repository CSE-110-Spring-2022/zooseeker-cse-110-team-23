package com.example.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jgrapht.Graph;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class GetDirectionUnitTest {
//    @Before
//    public void init() {
//        Context context = DirectionActivity.getContext();
//        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(context,"sample_node_info.json");
//        Map<String, ZooData.EdgeInfo> eInfo = ZooData.loadEdgeInfoJSON(context,"sample_edge_info.json");
//        Graph<String, IdentifiedWeightedEdge> g = ZooData.loadZooGraphJSON(context,"sample_zoo_graph.json");
//
//    }

//    @After
//    public void tearDown(){
//
//    }

    @Test
    public void EmptyAnimalList() {
        Context context = ApplicationProvider.getApplicationContext();
        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(context,"sample_node_info.json");
        Map<String, ZooData.EdgeInfo> eInfo = ZooData.loadEdgeInfoJSON(context,"sample_edge_info.json");
        Graph<String, IdentifiedWeightedEdge> g = ZooData.loadZooGraphJSON(context,"sample_zoo_graph.json");
        ArrayList<AnimalListItem> theList = new ArrayList<>(0);

        ArrayList<String> log = DirectionActivity.planPath(theList, vInfo, eInfo, g);
        assertEquals(log.size(),0);
    }

    @Test
    public void OneAnimalList() {
        Context context = ApplicationProvider.getApplicationContext();
        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(context,"sample_node_info.json");
        Map<String, ZooData.EdgeInfo> eInfo = ZooData.loadEdgeInfoJSON(context,"sample_edge_info.json");
        Graph<String, IdentifiedWeightedEdge> g = ZooData.loadZooGraphJSON(context,"sample_zoo_graph.json");
        ArrayList<AnimalListItem> theList = new ArrayList<>(0);
        theList.add(new AnimalListItem("Gorillas","gorillas",0));
        //theList.add(new AnimalListItem("Arctic Foxes","arctic_foxes",0));

        ArrayList<String> log = DirectionActivity.planPath(theList, vInfo, eInfo, g);
        assertEquals(Integer.valueOf(log.size()),Integer.valueOf(1));
    }
    @Test
    public void ManyAnimalList() {
        Context context = ApplicationProvider.getApplicationContext();
        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(context,"sample_node_info.json");
        Map<String, ZooData.EdgeInfo> eInfo = ZooData.loadEdgeInfoJSON(context,"sample_edge_info.json");
        Graph<String, IdentifiedWeightedEdge> g = ZooData.loadZooGraphJSON(context,"sample_zoo_graph.json");
        ArrayList<AnimalListItem> theList = new ArrayList<>(0);
        theList.add(new AnimalListItem("Gorillas","gorillas",0));
        theList.add(new AnimalListItem("Arctic Foxes","arctic_foxes",0));

        ArrayList<String> log = DirectionActivity.planPath(theList, vInfo, eInfo, g);
        assertEquals(Integer.valueOf(log.size()),Integer.valueOf(2));
    }

}
