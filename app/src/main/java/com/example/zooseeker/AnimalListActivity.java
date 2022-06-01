package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnimalListActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public AnimalListViewModel viewModel;

    private Button clearBtn;
    private Button backButton;
    private Button getDirBtn;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_visit);

        viewModel = new ViewModelProvider(this)
                .get(AnimalListViewModel.class);

        AnimalListAdapter adapter = new AnimalListAdapter();
        adapter.setOnTextEditedHandler(viewModel::updateText);
        viewModel.getAnimalListItems().observe(this, adapter::setAnimalListItems);

        recyclerView = findViewById(R.id.animal_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        this.deleteButton = this.findViewById(R.id.delete_btn);
        this.getDirBtn = this.findViewById(R.id.get_direction_btn);
        this.clearBtn = this.findViewById(R.id.clear_btn);

        clearBtn.setOnClickListener(viewModel::smash);

        adapter.setOnDeleteButtonClicked(viewModel::deleteAnimal);

        // Set Get direction button to onclick
        getDirBtn.setOnClickListener(this::onGetDirectionClicked);

        backButton = findViewById(R.id.back_search);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnimalListActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    void onGetDirectionClicked(View view) {
        startActivity(new Intent(this, DirectionActivity.class));
    }


}