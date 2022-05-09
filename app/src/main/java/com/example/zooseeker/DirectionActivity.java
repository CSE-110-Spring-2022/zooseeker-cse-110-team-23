package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DirectionActivity extends AppCompatActivity {
    private static Context context;
    private int animalIndex = 0;

    private Button nextBtn;
    private TextView destination;

    private ArrayList<String> log;

    private Graph<String, IdentifiedWeightedEdge> g;
    private Map<String, ZooData.VertexInfo> vInfo;
    private Map<String, ZooData.EdgeInfo> eInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        vInfo = ZooData.loadVertexInfoJSON(this,"sample_node_info.json");
        eInfo = ZooData.loadEdgeInfoJSON(this,"sample_edge_info.json");
        g = ZooData.loadZooGraphJSON(this,"sample_zoo_graph.json");

        nextBtn = findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(this::onNextAnimalClicked);
        List<AnimalListItem> animalPlanItems = AnimalListDatabase.getSingleton(this).animalListItemDao().getAll();
        log = planPath(animalPlanItems, vInfo, eInfo, g);
        destination = findViewById(R.id.destination_text);
        destination.setText(log.get(animalIndex));

    }

    public static ArrayList<String> planPath(List<AnimalListItem> animalPlanItems,
                                             Map<String, ZooData.VertexInfo> vInfo,
                                             Map<String, ZooData.EdgeInfo> eInfo,
                                             Graph<String, IdentifiedWeightedEdge> g
                                             ) {
        String start = "entrance_exit_gate";
        String goal;
        List<AnimalListItem> animalPlanItems = AnimalListDatabase.getSingleton(this).animalListItemDao().getAll();
        //this.viewModel.getAnimalListItems().getValue();

        if(animalPlanItems.size()==0) {
            return new ArrayList<String>(0);
        }
        goal = animalPlanItems.get(0).animal_id;        // save first animal in plan as goal
        GraphPath<String, IdentifiedWeightedEdge> path;



        ArrayList<String> log = new ArrayList<>(0);

        String prev = "entrance_exit_gate";
        for(int i=0; i<animalPlanItems.size(); i++) {
            path = DijkstraShortestPath.findPathBetween(g,start, goal);
            int j = 1;
            String b = "";

            for (IdentifiedWeightedEdge e : path.getEdgeList()) {
                // if the target of next edge is the prev place
                if(prev == vInfo.get(g.getEdgeTarget(e).toString()).name) {
                    String from = vInfo.get(g.getEdgeTarget(e).toString()).name;
                    String to = vInfo.get(g.getEdgeSource(e).toString()).name;
                    double length = g.getEdgeWeight(e);
                    String street = eInfo.get(e.getId()).street;

                    b += j + ". Walk " + length + " meters along " + street + " from " + from + " to " + to + "\n";
                    j++;

                    // update the name of the previous exhibit
                    prev = to;
                }
                else {
                    String from = vInfo.get(g.getEdgeSource(e).toString()).name;
                    String to = vInfo.get(g.getEdgeTarget(e).toString()).name;
                    double length = g.getEdgeWeight(e);
                    String street = eInfo.get(e.getId()).street;

                    b += j + ". Walk " + length + " meters along " + street + " from " + from + " to " + to + "\n";
                    j++;

                    // update the name of the previous exhibit
                    prev = to;
                }
            }
            log.add(b);
            start = animalPlanItems.get(i).animal_id;
            if((i+1) == animalPlanItems.size()) {       // if reached end of animals list
                break;
            }
            goal = animalPlanItems.get(i+1).animal_id;
        }
        return log;
    }

    void onNextAnimalClicked(View view) {
        if(animalIndex < log.size()-1) {
            animalIndex++;
            destination.setText(log.get(animalIndex));
        }
        else {
            nextBtn.setText("DONE");
            finish();
        }


    }

    public static Context getContext() {
        return DirectionActivity.context;
    }
}