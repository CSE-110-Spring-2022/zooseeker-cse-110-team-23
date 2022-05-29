package com.example.zooseeker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button addAnimalsButton;
    private ImageButton searchButton;
    private AutoCompleteTextView searchBar;
    private List<GraphListItem> animalParse;
    public AnimalListViewModel viewModel;
    private TextView confirmText;

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
    }

    private void ParsingAssets() {
        animalParse = GraphListItem.loadJSON(this,"sample_node_info.json");
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
        this.searchButton = this.findViewById(R.id.search_btn);
        this.searchBar = findViewById(R.id.search_bar);
        this.confirmText = findViewById(R.id.confirmText);
        this.addAnimalsButton = findViewById(R.id.plan_btn);
        this.addAnimalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AnimalListActivity.class));
                finish();
            }
        });
        this.searchButton.setOnClickListener(this::onAddClicked);
    }

    private ArrayList<String> parseDatabase() {
        ArrayList<String> animalNames = new ArrayList<>();
        for(int i = 0; i < animalParse.size(); i++) {
            animalNames.add(animalParse.get(i).name);
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
                viewModel.createTodo(text,animalParse.get(i).animal_id); // Change it to id
                viewModel.setSize(this);
                break;
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
                    suggestion += "\n" + animalParse.get(i).name;
                    confirmText.setText("Did you mean to search for any of the following: " + suggestion);
                    break;
                }
            }
        }
    }
}