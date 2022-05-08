package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DirectionActivity extends AppCompatActivity {
    private int animalIndex = 0;

    private Button nextBtn;
    private TextView destination;

    private ArrayList<String> log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        nextBtn = findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(this::onNextAnimalClicked);

        String start = "entrance_exit_gate";
        String goal;
        List<AnimalListItem> animalPlanItems = AnimalListDatabase.getSingleton(this).animalListItemDao().getAll();//this.viewModel.getAnimalListItems().getValue();

        Graph<String, IdentifiedWeightedEdge> g = ZooData.loadZooGraphJSON(this,"sample_zoo_graph.json");
        goal = animalPlanItems.get(0).animal_id;        // save first animal in plan as goal
        GraphPath<String, IdentifiedWeightedEdge> path;

        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(this,"sample_node_info.json");
        Map<String, ZooData.EdgeInfo> eInfo = ZooData.loadEdgeInfoJSON(this,"sample_edge_info.json");

        log = new ArrayList<>(1);

        String prev = "entrance_exit_gate";     // my code
        for(int i=0; i<animalPlanItems.size(); i++) {

            path = DijkstraShortestPath.findPathBetween(g,start, goal);
            int j = 1;
            String b = "";

            for (IdentifiedWeightedEdge e : path.getEdgeList()) {
                // my code
                // if the target of next edge is the prev place
                if(prev == vInfo.get(g.getEdgeTarget(e).toString()).name) {
                    String from = vInfo.get(g.getEdgeTarget(e).toString()).name;
//                    String from = "deez nuts";
                    String to = vInfo.get(g.getEdgeSource(e).toString()).name;
                    double length = g.getEdgeWeight(e);
                    String street = eInfo.get(e.getId()).street;

                    b += j + ". Walk "+ length +" meters along "+ street +" from "+ from +" to  "+to +"\n";
                    j++;

                    // update prev
                    prev = to;
                }
                else {
                    String from = vInfo.get(g.getEdgeSource(e).toString()).name;
//                    String from = "deez nuts";
                    String to = vInfo.get(g.getEdgeTarget(e).toString()).name;
                    double length = g.getEdgeWeight(e);
                    String street = eInfo.get(e.getId()).street;

                    b += j + ". Walk "+ length +" meters along "+ street +" from "+ from +" to  "+to +"\n" ;
                    j++;

                    // update prev
                    prev = to;
                }
                // end of my code
//                b += j + ". Walk "+ length +" meters along "+ street +" from "+ from +" to  "+to +"\n" ;
//                j++;
            }
            log.add(b);
            start = animalPlanItems.get(i).animal_id;
            if((i+1) == animalPlanItems.size()) {       // if reached end of animals list
                break;
            }
            goal = animalPlanItems.get(i+1).animal_id;
        }
        destination = findViewById(R.id.destination_text);
        //String c = Integer.toString(log.size());
        destination.setText(log.get(animalIndex));


    }

    void onNextAnimalClicked(View view) {
        if(animalIndex < log.size()) {
            animalIndex++;
            destination.setText(log.get(animalIndex));
        }
        else {
            nextBtn.setText("DONE");
        }


    }
}