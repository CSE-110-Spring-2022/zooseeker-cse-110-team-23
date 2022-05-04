package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.Map;

public class TestJSONActivity extends AppCompatActivity {


    public AnimalListViewModel viewModel;

    public RecyclerView recyclerView;
    public Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_jsonacitivity);

        viewModel = new ViewModelProvider(this)
                .get(AnimalListViewModel.class);

        AnimalListAdapter adapter = new AnimalListAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.test_list);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        recyclerView.setAdapter(adapter);
//
        //List<AnimalListItem> todos = AnimalListItem.loadJSON(this,"sample_node_info.json");'
//
        adapter.setNewAnimalListItems(AnimalListItem.loadJSON(this, "sample_node_info.json"));
        if(AnimalListItem.loadJSON(this, "sample_node_info.json").size()==0) {
            btn = findViewById(R.id.button3);
            btn.setText("FUCK U");
        }
        //Log.d("TEST JSON ACTIVITY", todos.toString());

    }
}

