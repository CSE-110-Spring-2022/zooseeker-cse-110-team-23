package com.example.zooseeker;

import android.app.Application;
import android.content.Context;

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
}
