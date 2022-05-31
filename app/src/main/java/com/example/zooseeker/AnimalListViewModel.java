package com.example.zooseeker;

import android.app.Application;
import android.content.Context;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AnimalListViewModel extends AndroidViewModel {
    private LiveData<List<AnimalListItem>> animalListItems;
    private final AnimalListItemDao animalListItemDao;


    public AnimalListViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        AnimalListDatabase db = AnimalListDatabase.getSingleton(context);
        animalListItemDao = db.animalListItemDao();
    }

    public LiveData<List<AnimalListItem>> getAnimalListItems() {
        if (animalListItems == null) {
            loadUsers();
        }
        return animalListItems;
    }

    private void loadUsers() {
        animalListItems = animalListItemDao.getAllAlive();
    }

    public void updateText(AnimalListItem animalListItem, String newText) {
        animalListItem.text = newText;
        animalListItemDao.update(animalListItem);
    }

    public void createTodo(String text, String text2, int totalDistance) {
        AnimalListItem newItem = new AnimalListItem(text,text2,totalDistance);
        animalListItemDao.insert(newItem);
    }

    public void deleteAnimal(AnimalListItem animalListItem) {
        animalListItemDao.delete(animalListItem);
    }

    public void setSize(MainActivity view) {
        EditText listCounter = view.findViewById(R.id.list_counter);
        String s = Integer.toString(animalListItemDao.getSize());
        listCounter.setText(s);
    }
}