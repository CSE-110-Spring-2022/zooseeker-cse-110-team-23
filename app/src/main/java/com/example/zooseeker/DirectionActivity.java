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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DirectionActivity extends AppCompatActivity {
    private static Context context;
    private int animalIndex = 0;

    private Button nextBtn;
    private Button prevBtn;
    private Button skipBtn;
    private Button stepBackBtn;
    private TextView destination;

    private ArrayList<String> log;
    private static ArrayList<String> logReversed;

    private Graph<String, IdentifiedWeightedEdge> g;
    private Map<String, ZooData.VertexInfo> vInfo;
    private Map<String, ZooData.EdgeInfo> eInfo;
    private List<AnimalListItem> sortedPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        LoadAssets();
        InitializeUIElements();
        UIElementFunctionalitySetUp();

        List<AnimalListItem> animalPlanItems = LoadAnimalListFromDataBase();
        sortedPath = sortPath(animalPlanItems,g);
        log = planPath(sortedPath, vInfo, eInfo, g);

        // Remind User when nothing is planned
        SanityCheck();
    }

    private void SanityCheck() {
        if(log.isEmpty()) {
            destination.setText("Plan Something");
        }
        else {
            destination.setText(log.get(animalIndex));
        }
    }

    private List<AnimalListItem> LoadAnimalListFromDataBase() {
        return AnimalListDatabase.getSingleton(this).animalListItemDao().getAll();
    }

    private void UIElementFunctionalitySetUp() {
        prevBtn.setOnClickListener(this::onPrevAnimalClicked);
        nextBtn.setOnClickListener(this::onNextAnimalClicked);
        skipBtn.setOnClickListener(this::onSkipAnimalClicked);
        stepBackBtn.setOnClickListener(this::onStepBackAnimalClicked);
    }

    private void LoadAssets() {
        vInfo = ZooData.loadVertexInfoJSON(this,"sample_node_info.json");
        eInfo = ZooData.loadEdgeInfoJSON(this,"sample_edge_info.json");
        g = ZooData.loadZooGraphJSON(this,"sample_zoo_graph.json");
    }

    private void InitializeUIElements() {
        nextBtn = findViewById(R.id.next_btn);
        prevBtn = findViewById(R.id.prev_animal_btn );
        skipBtn = findViewById(R.id.skip_next_btn);
        stepBackBtn = findViewById(R.id.step_back);
        destination = findViewById(R.id.destination_text);
    }

    public static List<AnimalListItem> sortPath(List<AnimalListItem> unsortedAnimalList,
                                                Graph<String, IdentifiedWeightedEdge> g) {
        ArrayList<AnimalListItem> sorted = new ArrayList<>();
        String start = "entrance_exit_gate";
        AnimalListItem select = new AnimalListItem("","",0);

        // Empty List
        if(unsortedAnimalList.isEmpty()){
            return sorted;
        }

        GraphPath<String, IdentifiedWeightedEdge> path;

        double min = (double)Integer.MAX_VALUE;

        for(AnimalListItem j : unsortedAnimalList) {
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
            min = (double)Integer.MAX_VALUE;

            // Update sorted
            sorted.add(select);

        }


        return sorted;
    }

    public static ArrayList<String> planPath(List<AnimalListItem> animalPlanItems,
                                             Map<String, ZooData.VertexInfo> vInfo,
                                             Map<String, ZooData.EdgeInfo> eInfo,
                                             Graph<String, IdentifiedWeightedEdge> g
                                             ) {
        String start = "entrance_exit_gate";
        String goal;
        //this.viewModel.getAnimalListItems().getValue();

        if(animalPlanItems.size()==0) {
            return new ArrayList<String>(0);
        }
        goal = animalPlanItems.get(0).animal_id;        // save first animal in plan as goal
        GraphPath<String, IdentifiedWeightedEdge> path;

        ArrayList<String> log_local = new ArrayList<>(0);
        ArrayList<String> logReversed_local = new ArrayList<>(0);

        String prev = "entrance_exit_gate";
        for(int i=0; i<animalPlanItems.size(); i++) {
            path = DijkstraShortestPath.findPathBetween(g,start, goal);
            int j = 1;
            int k = path.getEdgeList().size();

            String b = "";
            String b_rev = "";

            for (IdentifiedWeightedEdge e : path.getEdgeList()) {
                // if the target of next edge is the prev place
                if(prev == vInfo.get(g.getEdgeTarget(e).toString()).name) {
                    String from = vInfo.get(g.getEdgeTarget(e).toString()).name;
                    String to = vInfo.get(g.getEdgeSource(e).toString()).name;
                    double length = g.getEdgeWeight(e);
                    String street = eInfo.get(e.getId()).street;

                    b += j + ". Walk " + length + " meters along " + street + " from " + from + " to " + to + "\n";
                    b_rev = k + ". Walk " + length + " meters along " + street + " from " + to + " to " + from + "\n" +b_rev + "\n";
                    j++;
                    k--;
                    // update the name of the previous exhibit
                    prev = to;
                }
                else {
                    String from = vInfo.get(g.getEdgeSource(e).toString()).name;
                    String to = vInfo.get(g.getEdgeTarget(e).toString()).name;
                    double length = g.getEdgeWeight(e);
                    String street = eInfo.get(e.getId()).street;

                    b += j + ". Walk " + length + " meters along " + street + " from " + from + " to " + to + "\n";
                    b_rev = k + ". Walk " + length + " meters along " + street + " from " + to + " to " + from + "\n" + b_rev+ "\n";
                    j++;
                    k--;

                    // update the name of the previous exhibit
                    prev = to;
                }
            }
            logReversed_local.add(b_rev);
            log_local.add(b);
            start = animalPlanItems.get(i).animal_id;
            if((i+1) == animalPlanItems.size()) {       // if reached end of animals list
                break;
            }
            goal = animalPlanItems.get(i+1).animal_id;
        }

        logReversed = logReversed_local;
        return log_local;
    }

    void onNextAnimalClicked(View view) {
        if(animalIndex < log.size()-1) {
            animalIndex++;
            destination.setText(log.get(animalIndex));
            prevBtn.setVisibility(View.VISIBLE);

            if(animalIndex==log.size()-1){
                nextBtn.setText("DONE");
                skipBtn.setVisibility(View.INVISIBLE);
                animalIndex++;
            }
        }
        else if (animalIndex > log.size()-1){
            finish();
        }
    }

    void onStepBackAnimalClicked(View view) {
        if(animalIndex == 1) {
            prevBtn.setVisibility(View.INVISIBLE);
            animalIndex--;
            destination.setText(log.get(animalIndex));
        }
        else if(animalIndex == log.size()) {
            nextBtn.setText("Next Animal");
            animalIndex = animalIndex - 2;
            destination.setText(log.get(animalIndex));
        }
        else {
            animalIndex--;
            destination.setText(log.get(animalIndex));
        }
        if(animalIndex < log.size() - 1) {
            skipBtn.setVisibility(View.VISIBLE);
        }
    }

    void onPrevAnimalClicked(View view) {
        if(animalIndex == 1) {
            prevBtn.setVisibility(View.INVISIBLE);
            animalIndex--;
            destination.setText(logReversed.get(animalIndex));
        }
        else if(animalIndex == logReversed.size()) {
            nextBtn.setText("Next Animal");
            animalIndex = animalIndex - 2;
            destination.setText(logReversed.get(animalIndex));
        }
        else {
            animalIndex--;
            destination.setText(logReversed.get(animalIndex));
        }
        if(animalIndex < logReversed.size() - 1) {
            skipBtn.setVisibility(View.VISIBLE);
        }
    }

    void onSkipAnimalClicked(View view) {

        int nextAnimal = animalIndex + 1;
        sortedPath.remove(nextAnimal);
        log = planPath(sortedPath, vInfo, eInfo, g);


        // if on last animal
        if(animalIndex==log.size()-1){
            ++animalIndex;
            nextBtn.setText("DONE");
            skipBtn.setVisibility(View.INVISIBLE);
        }
    }

    public static Context getContext() {
        return DirectionActivity.context;
    }
}