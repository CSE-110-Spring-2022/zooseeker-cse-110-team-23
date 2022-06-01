package com.example.zooseeker;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.List;

public class SortPath {
    private static ArrayList<AnimalListItem> sorted;
    private static AnimalListItem select;


    public static List<AnimalListItem> sortPath(List<AnimalListItem> unsortedAnimalList,
                                                Graph<String, IdentifiedWeightedEdge> g, String start) {

        sorted = new ArrayList<AnimalListItem>();
        select = new AnimalListItem("", "", 0);

        // Empty List
        if (unsortedAnimalList.isEmpty()) {
            return sorted;
        }

        GraphPath<String, IdentifiedWeightedEdge> path;

        double min = (double) Integer.MAX_VALUE;

        for (AnimalListItem j : unsortedAnimalList) {
            // Check which animal to go next
            for (AnimalListItem a : unsortedAnimalList) {
                path = DijkstraShortestPath.findPathBetween(g, start, a.animal_id);
                double length = 0;
                for (IdentifiedWeightedEdge e : path.getEdgeList()) {
                    length += g.getEdgeWeight(e);
                }
                if (length < min && !sorted.contains(a)) {
                    select = a;
                    min = length;
                }
            }
            start = select.animal_id;
            min = (double) Integer.MAX_VALUE;

            // Update sorted
            sorted.add(select);

        }


        return sorted;
    }

}