package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class AnimalListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_list);

        List<AnimalListItem> todos = AnimalListItem.loadJSON(this, "sample_exhibits.json");
        Log.d("AnimalListActivity", todos.toString());
    }
}