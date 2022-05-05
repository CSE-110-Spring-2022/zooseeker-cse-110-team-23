package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class TestJSONActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public Button btn;
    public TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_jsonacitivity);

        GraphListAdapter adapter = new GraphListAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.test_list);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        recyclerView.setAdapter(adapter);

        List<GraphListItem> todos = GraphListItem.loadJSON(this,"sample_node_info.json");

//        adapter.setNewGraphListItems(GraphListItem.loadJSON(this, "sample_node_info.json"));
//        if(GraphListItem.loadJSON(this, "sample_node_info.json").size()==0) {
//            btn = findViewById(R.id.button3);
//            btn.setText("FUCK U");
//        }

        tv = this.findViewById(R.id.parsedJSON);
        tv.setText(todos.toString());
    }
}

