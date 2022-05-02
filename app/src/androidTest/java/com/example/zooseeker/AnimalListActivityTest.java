package com.example.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class AnimalListActivityTest {
    AnimalListDatabase testDb;
    AnimalListItemDao animalListItemDao ;

    private static void forceLayout(RecyclerView recyclerView) {
        recyclerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        recyclerView.layout(0, 0, 1080, 2280);
    }

    @Before
    public void resetDatabase () {
        Context context = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(context, AnimalListDatabase.class)
                .allowMainThreadQueries()
                .build();
        AnimalListDatabase.injectTestDatabase(testDb);

        List<AnimalListItem> todos = AnimalListItem.loadJSON(context, "demo_todos.json ");
        animalListItemDao = testDb.animalListItemDao();
        animalListItemDao.insertAll(todos);
    }

    @Test
    public void testEditTodoText () {
        String newText = "Ensure all tests pass";
        ActivityScenario<AnimalListActivity> scenario
                = ActivityScenario.launch(AnimalListActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.recyclerView ;
            RecyclerView.ViewHolder firstVH = recyclerView.findViewHolderForAdapterPosition(0);
            assertNotNull(firstVH);
            Long id = firstVH.getItemId();

            EditText todoText = firstVH.itemView.findViewById(R.id.animal_item_text);
            todoText.requestFocus();
            todoText.setText("Ensure all tests pass");
            todoText.clearFocus ();
            AnimalListItem editedItem = animalListItemDao.get(id);
            assertEquals(newText, editedItem. text);
        });
    }

    @Test
    public void testAddNewTodo() {
        String newText = "Ensure all tests pass";

        ActivityScenario<AnimalListActivity> scenario
                = ActivityScenario.launch(AnimalListActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            List<AnimalListItem> beforeTodoList = animalListItemDao.getAll();

            EditText newTodoText = activity.findViewById(R.id.new_todo_text);
            Button addTodoButton = activity.findViewById(R.id.add_todo_btn);

            newTodoText.setText(newText);
            addTodoButton.performClick();

            List<AnimalListItem> afterTodoList = animalListItemDao.getAll();
            assertEquals(beforeTodoList.size() + 1, afterTodoList.size());
            assertEquals(newText, afterTodoList.get(afterTodoList.size() - 1).text);
        });
    }
}
