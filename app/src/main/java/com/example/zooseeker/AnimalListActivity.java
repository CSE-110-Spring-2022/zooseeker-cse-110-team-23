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

import java.util.List;

public class AnimalListActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public AnimalListViewModel viewModel;
    private EditText newTodoText;
    private Button addTodoButton;
    private Button backButton;
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

        this.newTodoText = this.findViewById(R.id.new_todo_text);
        this.addTodoButton = this.findViewById(R.id.add_todo_btn);
        this.deleteButton = this.findViewById(R.id.delete_btn);

        addTodoButton.setOnClickListener(this::onAddTodoClicked);

        adapter.setOnDeleteButtonClicked(viewModel::deleteAnimal);

        backButton = findViewById(R.id.back_search);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AnimalListActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    void onAddTodoClicked(View view) {
        String text = newTodoText.getText().toString();
        newTodoText.setText("");
        viewModel.createTodo(text);
    }
}