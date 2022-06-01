package com.example.zooseeker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button addAnimalsButton;
    private ImageButton searchButton;
    private AutoCompleteTextView searchBar;
    private List<ZooData.VertexInfo> animalParse;
    public AnimalListViewModel viewModel;
    private TextView confirmText;
    private static ArrayList<Integer> totalDistance;
    private Graph<String, IdentifiedWeightedEdge> g;
    private List<AnimalListItem> sortedPath;
    List<AnimalListItem> animalPlanItems;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind UI Element with field variables
        InitializeUIElements();

        // Load Graph
        ParsingAssets();

        ViewModelSetUp();

        // Parse From Database
        List<String> animalNames = parseDatabase();

        // Remove entrance and exit node
        AnimalListCleanUp(animalNames);
        AdapterSetUp(animalNames);
        g = ZooData.loadZooGraphJSON(this,"zoo_graph.json");

        // Location permissions


    }

    private void ParsingAssets() {
        animalParse = ZooData.loadJSON(this,"exhibit_info.json");
    }

    private void ViewModelSetUp() {
        viewModel = new ViewModelProvider(this)
                .get(AnimalListViewModel.class);
        viewModel.setSize(this);
    }

    private void AdapterSetUp(List<String> animalNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, animalNames);
        searchBar.setAdapter(adapter);
    }

    private void AnimalListCleanUp(List<String> animalNames) {
        animalNames.remove("Entrance Plaza");
        animalNames.remove("Entrance and Exit Gate");
    }

    private void InitializeUIElements() {
        Context context = this;
        this.searchButton = this.findViewById(R.id.search_btn);
        this.searchBar = findViewById(R.id.search_bar);
        this.confirmText = findViewById(R.id.confirmText);
        this.addAnimalsButton = findViewById(R.id.plan_btn);
        this.addAnimalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AnimalListItem> animalPlanItems = AnimalListDatabase.getSingleton(context).animalListItemDao().getAll();
                sortedPath = SortPath.sortPath(animalPlanItems, g);
                calculateDistance(g, sortedPath);
                for(int i=0; i < sortedPath.size(); ++i) {
                    for(int j=0; j < animalPlanItems.size(); ++j) {
                        if(animalPlanItems.get(j).animal_id.equals(sortedPath.get(i).animal_id)) {
                            viewModel.updateOrder(animalPlanItems.get(j), totalDistance.get(i));
                        }
                    }
                }
                startActivity(new Intent(MainActivity.this, AnimalListActivity.class));
                finish();
            }
        });
        this.searchButton.setOnClickListener(this::onAddClicked);
    }

    private ArrayList<String> parseDatabase() {
        ArrayList<String> animalNames = new ArrayList<>();
        for(int i = 0; i < animalParse.size(); i++) {
            if(animalParse.get(i).kind == ZooData.VertexInfo.Kind.EXHIBIT) {
                animalNames.add(animalParse.get(i).name);
            }
        }
        return animalNames;
    }

    void onAddClicked(View view) {
        String text = searchBar.getText().toString();
        String suggestion = "";
        List<AnimalListItem> animalPlanItems = AnimalListDatabase.getSingleton(this).animalListItemDao().getAll();
        List<String> animalPlanItemsString = new ArrayList<String>();
        for(AnimalListItem a:animalPlanItems) {
            animalPlanItemsString.add(a.text);
        }

        confirmText.setText("The animal you searched for is not in the zoo.");
        for(int i = 0; i < animalParse.size(); i++) {
            if(animalParse.get(i).name.equals(text) && !animalPlanItemsString.contains(text)) {
                if("Entrance Plaza".equals(text) || "Entrance and Exit Gate".equals(text)){
                    confirmText.setText("Please enter the name of the animal.");
                    break;
                }
                searchBar.setText("");
                confirmText.setText("The animal you searched for is added into your planner.");
                if(animalParse.get(i).kind == ZooData.VertexInfo.Kind.EXHIBIT) {
                    if(animalParse.get(i).group_id == null) {
                        viewModel.createTodo(text, animalParse.get(i).id, 0);//totalDistance.get(animalParse.get(i).id)); // Change it to id
                        viewModel.setSize(this);
                        break;
                    } else {
                        viewModel.createTodo(text, animalParse.get(i).group_id, 0);//totalDistance.get(animalParse.get(i).id)); // Change it to id
                        viewModel.setSize(this);
                        break;
                    }
                }
                    else
                {
                    confirmText.setText("Not an exhibit.");
                }
            }
            else if(animalPlanItemsString.contains(text)) {
                confirmText.setText("Already Added");
            }
            for(int j = 0; j < animalParse.get(i).tags.size(); j++) {
                if (animalParse.get(i).tags.get(j).equals(text)) {
                    if(animalParse.get(i).name.equals("Entrance Plaza") || animalParse.get(i).name.equals("Entrance and Exit Gate")){
                        confirmText.setText("Please enter the name of the animal.");
                        break;
                    }
                    if(animalParse.get(i).kind == ZooData.VertexInfo.Kind.EXHIBIT) {
                        suggestion += "\n" + animalParse.get(i).name;
                    }
                    confirmText.setText("Did you mean to search for any of the following: " + suggestion);
                    break;
                }
            }
        }
    }

    public static ArrayList<Integer> calculateDistance(Graph<String, IdentifiedWeightedEdge> g, List<AnimalListItem> animalPlanItems) {
        String start = "entrance_exit_gate";
        String goal;

        GraphPath<String, IdentifiedWeightedEdge> path;

        totalDistance = new ArrayList<>(0);

        int distance = 0;
        for(int i = 0; i < animalPlanItems.size(); i++) {
            goal = animalPlanItems.get(i).animal_id;
            path = DijkstraShortestPath.findPathBetween(g, start, goal);

            for (IdentifiedWeightedEdge e : path.getEdgeList()) {
                double length = g.getEdgeWeight(e);
                distance += length;
            }
            totalDistance.add(distance);
            start = animalPlanItems.get(i).animal_id;
        }
        return totalDistance;
    }
}