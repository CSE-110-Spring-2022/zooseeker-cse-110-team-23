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

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button addAnimalsButton;
    private ImageButton searchButton;
    private EditText searchBar;
    private List<GraphListItem> animalParse;
    public AnimalListViewModel viewModel;
    private EditText listCounter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(this, TestJSONActivity.class);
//        startActivity(intent);

        addAnimalsButton = findViewById(R.id.plan_btn);
        addAnimalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AnimalListActivity.class));
                finish();
            }
        });

        this.searchButton = this.findViewById(R.id.search_btn);
        this.searchBar = findViewById(R.id.search_bar);

        GraphListAdapter adapter = new GraphListAdapter();
        adapter.setHasStableIds(true);

        animalParse = GraphListItem.loadJSON(this,"sample_node_info.json");

        searchButton.setOnClickListener(this::onAddClicked);

        viewModel = new ViewModelProvider(this)
                .get(AnimalListViewModel.class);

//        viewModel.setSize(this);
    }

    void onAddClicked(View view) {
        String text = searchBar.getText().toString();
        for(int i = 0; i < animalParse.size(); i++) {
            if(animalParse.get(i).name.equals(text)) {
                searchBar.setText("");
                viewModel.createTodo(text);
                break;
            }
        }
    }
}