//package com.example.zooseeker;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//
//import android.content.Context;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
//import androidx.lifecycle.Lifecycle;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.room.Room;
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.core.app.ApplicationProvider;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class AnimalListActivityTest {
//    AnimalListDatabase testDb;
//    AnimalListItemDao animalListItemDao;
//
//    private static void forceLayout(RecyclerView recyclerView) {
//        recyclerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        recyclerView.layout(0, 0, 1080, 2280);
//    }
//
//    @Before
//    public void resetDatabase () {
//        Context context = ApplicationProvider.getApplicationContext();
//        testDb = Room.inMemoryDatabaseBuilder(context, AnimalListDatabase.class)
//                .allowMainThreadQueries()
//                .build();
//        AnimalListDatabase.injectTestDatabase(testDb);
//
//        List<AnimalListItem> animals = AnimalListItem.loadJSON(context, "sample_node_info.json");
//        animalListItemDao = testDb.animalListItemDao();
//        animalListItemDao.insertAll(animals);
//    }
//
//    @Test
//    public void testAddNewAnimal() {
//        String newText = "Lions";
//        ActivityScenario<MainActivity> scenario
//                = ActivityScenario.launch(MainActivity.class);
//        scenario.moveToState(Lifecycle.State.CREATED);
//        scenario.moveToState(Lifecycle.State.STARTED);
//        scenario.moveToState(Lifecycle.State.RESUMED);
//
//        scenario.onActivity(activity -> {
//            List<AnimalListItem> beforeAnimalList = animalListItemDao.getAll();
//
//            EditText newAnimalText = activity.findViewById(R.id.search_bar);
//            Button addAnimalButton = activity.findViewById(R.id.search_btn);
//
//            newAnimalText.setText(newText);
//            addAnimalButton.performClick();
//
//            List<AnimalListItem> afterAnimalList = animalListItemDao.getAll();
//            assertEquals(beforeAnimalList.size() + 1, afterAnimalList.size());
//            assertEquals(newText, afterAnimalList.get(afterAnimalList.size() - 1).text);
//        });
//    }
//
//    @Test
//    public void testDeleteAnimal() {
//        ActivityScenario<AnimalListActivity> scenario
//                = ActivityScenario.launch(AnimalListActivity.class);
//        scenario.moveToState(Lifecycle.State.CREATED);
//        scenario.moveToState(Lifecycle.State.STARTED);
//        scenario.moveToState(Lifecycle.State.RESUMED);
//
//        scenario.onActivity(activity -> {
//            List<AnimalListItem> beforeAnimalList = animalListItemDao.getAll();
//
//            RecyclerView recyclerView = activity.recyclerView;
//            RecyclerView.ViewHolder firstVH = recyclerView.findViewHolderForAdapterPosition(0);
//            assertNotNull(firstVH);
//            long id = firstVH.getItemId();
//
//            View deleteButton = firstVH.itemView.findViewById(R.id.delete_btn);
//            deleteButton.performClick();
//
//            List<AnimalListItem> afterAnimalList = animalListItemDao.getAll();
//            assertEquals(beforeAnimalList.size() - 1, afterAnimalList.size());
//
//            AnimalListItem editedItem = animalListItemDao.get(id);
//            assertNull(editedItem);
//        });
//    }
//}
