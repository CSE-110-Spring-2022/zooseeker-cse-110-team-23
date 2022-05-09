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

import java.lang.reflect.Array;
import java.util.ArrayList;
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


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //AnimalListDatabase.getSingleton(this).animalListItemDao().nukeTable();

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
                searchBar.setText("");
                confirmText.setText("The animal you searched for is added into your planner.");
                viewModel.createTodo(text,animalParse.get(i).animal_id); // Change it to id
                viewModel.setSize(this);
                break;
            }
            else if(animalPlanItemsString.contains(text)) {
                confirmText.setText("Already Added");
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