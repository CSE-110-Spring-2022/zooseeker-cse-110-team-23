package com.example.zooseeker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button addAnimalsButton;
    private ImageButton searchButton;
    private EditText searchBar;
    private List<GraphListItem> animalParse;
    public AnimalListViewModel viewModel;
    private TextView confirmText;

    String start = "entrance_exit_gate";
    String goal = "elephant_odyssey";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(this, TestJSONActivity.class);
//        startActivity(intent);

        this.addAnimalsButton = findViewById(R.id.plan_btn);
        this.addAnimalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AnimalListActivity.class));
                finish();
            }
        });

        this.searchButton = this.findViewById(R.id.search_btn);
        this.searchBar = findViewById(R.id.search_bar);
        this.confirmText = findViewById(R.id.confirmText);

        GraphListAdapter adapter = new GraphListAdapter();
        adapter.setHasStableIds(true);

        animalParse = GraphListItem.loadJSON(this,"sample_node_info.json");

        searchButton.setOnClickListener(this::onAddClicked);

        viewModel = new ViewModelProvider(this)
                .get(AnimalListViewModel.class);
        viewModel.setSize(this);

        List<AnimalListItem> animalPlanItems = AnimalListDatabase.getSingleton(this).animalListItemDao().getAll();//this.viewModel.getAnimalListItems().getValue();


        Graph<String, IdentifiedWeightedEdge> g = ZooData.loadZooGraphJSON(this,"sample_zoo_graph.json");
        goal = animalPlanItems.get(0).text.toLowerCase();
        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, start, goal);

        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(this,"sample_node_info.json");
        Map<String, ZooData.EdgeInfo> eInfo = ZooData.loadEdgeInfoJSON(this,"sample_edge_info.json");

//        for(int i=0; i<animalPlanItems.size()-1; ++i) {
//            start = animalPlanItems.get(i).text.toLowerCase();
//            goal = animalPlanItems.get(i+1).text.toLowerCase();
//            path = DijkstraShortestPath.findPathBetween(g, start, goal);
//
//            int j = 1;
//
//            for (IdentifiedWeightedEdge e : path.getEdgeList()) {
//                System.out.printf("  %d. Walk %.0f meters along %s from '%s' to '%s'.\n",
//                        j,
//                        g.getEdgeWeight(e),
//                        eInfo.get(e.getId()).street,
//                        vInfo.get(g.getEdgeSource(e).toString()).name,
//                        vInfo.get(g.getEdgeTarget(e).toString()).name);
//                i++;
//            }
//        }

    }


    void onAddClicked(View view) {
        String text = searchBar.getText().toString();
        String suggestion = "";
        confirmText.setText("The animal you searched for is not in the zoo.");
        for(int i = 0; i < animalParse.size(); i++) {
            if(animalParse.get(i).name.equals(text)) {
                searchBar.setText("");
                confirmText.setText("The animal you searched for is added into your planner.");
                viewModel.createTodo(text);
                viewModel.setSize(this);
                break;
            }
            for(int j = 0; j < animalParse.get(i).tags.size(); j++) {
                if (animalParse.get(i).tags.get(j).equals(text)) {
                    suggestion += "\n" + animalParse.get(i).name;
                    confirmText.setText("Did you mean to search for any of the following: " + suggestion);
                    break;
                }
            }
        }
    }
}