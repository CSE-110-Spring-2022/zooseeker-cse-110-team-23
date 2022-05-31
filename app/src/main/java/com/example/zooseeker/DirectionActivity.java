package com.example.zooseeker;

import androidx.annotation.NonNull;
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
    private Button briefBtn;
    private boolean briefFlag;
    private boolean end;

    private ArrayList<String> log;
    private static ArrayList<String> logReversed;
    private static ArrayList<String> logConcise;

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
        sortedPath = SortPath.sortPath(animalPlanItems, g);
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
        briefBtn.setOnClickListener(this::onBriefClicked);
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
        briefBtn = findViewById(R.id.show_brief_btn);
    }

    public static ArrayList<String> planPath(List<AnimalListItem> animalPlanItems,
                                             Map<String, ZooData.VertexInfo> vInfo,
                                             Map<String, ZooData.EdgeInfo> eInfo,
                                             Graph<String, IdentifiedWeightedEdge> g
                                             ) {
        String start = "entrance_exit_gate";
        String goal;

        if(animalPlanItems.size()==0) {
            return new ArrayList<String>(0);
        }

        goal = animalPlanItems.get(0).animal_id;        // save first animal in plan as goal
        GraphPath<String, IdentifiedWeightedEdge> path;

        ArrayList<String> log_local = new ArrayList<>(0);
        ArrayList<String> logReversed_local = new ArrayList<>(0);
        ArrayList<String> logConcise_local = new ArrayList<>(0);

        String prev = "entrance_exit_gate";
        for(int i=0; i<animalPlanItems.size(); i++) {
            path = DijkstraShortestPath.findPathBetween(g,start, goal);
            int j = 1;
            int k = path.getEdgeList().size();
            double briefDistance = 0;

            String b = "";
            String b_rev = "";

            String startName = "";
            String goalName =  "";
            for (IdentifiedWeightedEdge e : path.getEdgeList()) {
                if(prev==vInfo.get(g.getEdgeSource(path.getEdgeList().get(0)).toString()).name) {
                    startName = vInfo.get(g.getEdgeSource(path.getEdgeList().get(0)).toString()).name;
                }
                else {
                    startName = vInfo.get(g.getEdgeTarget(path.getEdgeList().get(0)).toString()).name;
                }

                // if the target of next edge is the prev place
                if(prev == vInfo.get(g.getEdgeTarget(e).toString()).name) {

                    // Gather Node and Edge Information
                    String from = vInfo.get(g.getEdgeTarget(e).toString()).name;
                    String to = vInfo.get(g.getEdgeSource(e).toString()).name;
                    double length = g.getEdgeWeight(e);
                    String street = eInfo.get(e.getId()).street;

                    briefDistance += length;
                    b = DirectionString(j, b, from, to, length, street);
                    b_rev = ReverseDirectionString(k, b_rev, from, to, length, street);
                    j++;
                    k--;
                    // update the name of the previous exhibit
                    prev = to;

                    goalName = to;
                }
                else {
                    String from = vInfo.get(g.getEdgeSource(e).toString()).name;
                    String to = vInfo.get(g.getEdgeTarget(e).toString()).name;
                    double length = g.getEdgeWeight(e);
                    String street = eInfo.get(e.getId()).street;

                    briefDistance += length;
                    b = DirectionString(j, b, from, to, length, street);
                    b_rev = ReverseDirectionString(k, b_rev, from, to, length, street);
                    j++;
                    k--;

                    // update the name of the previous exhibit
                    prev = to;

                    goalName = to;
                }



            }
            logReversed_local.add(b_rev);
            log_local.add(b);


            logConcise_local.add(1 + ". Walk " + briefDistance + " meters " + "from "+ startName + " to "+  goalName + "\n");

            start = animalPlanItems.get(i).animal_id;
            if((i+1) == animalPlanItems.size()) {       // if reached end of animals list
                break;
            }
            goal = animalPlanItems.get(i+1).animal_id;
        }

        logReversed = logReversed_local;
        logConcise = logConcise_local;
        return log_local;
    }

    @NonNull
    private static String ReverseDirectionString(int k, String b_rev, String from, String to, double length, String street) {
        b_rev = k + ". Walk " + length + " meters along " + street + " from " + to + " to " + from + "\n" + b_rev + "\n";
        return b_rev;
    }

    @NonNull
    private static String DirectionString(int j, String b, String from, String to, double length, String street) {
        b += j + ". Walk " + length + " meters along " + street + " from " + from + " to " + to + "\n";
        return b;
    }

    void onNextAnimalClicked(View view) {
        DirectionDefaultState();
        if(animalIndex < log.size()-1) {
            animalIndex++;
            destination.setText(log.get(animalIndex));
            setVisibility(View.VISIBLE);

            if(animalIndex==log.size()-1){
                nextBtn.setText("DONE");
                skipBtn.setVisibility(View.INVISIBLE);
                end = true;
            }
        }
        else if (end){
            finish();
        }
    }

    private void setVisibility(int visibility) {
        prevBtn.setVisibility(visibility);
        stepBackBtn.setVisibility(visibility);
    }

    void onStepBackAnimalClicked(View view) {
        Iterate(log);
        DirectionDefaultState();
    }

    void onPrevAnimalClicked(View view) {
        Iterate(logReversed);
        DirectionDefaultState();
    }

    void onBriefClicked(View view) {
        if(briefFlag) {
            destination.setText(log.get(animalIndex));
            DirectionDefaultState();
        }
        else {
            destination.setText(logConcise.get(animalIndex));
            briefFlag = true;
            briefBtn.setText("Detailed Direction");
        }

    }

    private void DirectionDefaultState() {
        briefFlag = false;
        briefBtn.setText("Brief Direction");
    }

    private void Iterate(ArrayList<String> log) {
        if (animalIndex == 1) {
            setVisibility(View.INVISIBLE);
            animalIndex--;
            destination.setText(log.get(animalIndex));
        } else if (animalIndex == log.size()-1) {
            nextBtn.setText("Next Animal");
            animalIndex = animalIndex - 1;
            destination.setText(log.get(animalIndex));
        } else {
            animalIndex--;
            destination.setText(log.get(animalIndex));
        }
        if (animalIndex < log.size() - 1) {
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