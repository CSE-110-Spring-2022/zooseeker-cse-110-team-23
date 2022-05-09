//package com.example.zooseeker;
//
//import static org.junit.Assert.*;
//
//import android.content.Context;
//
//import androidx.room.Room;
//import androidx.test.core.app.ApplicationProvider;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.io.IOException;
//
//@RunWith(AndroidJUnit4.class)
//public class AnimalDatabaseTest {
//    private AnimalListItemDao dao;
//    private AnimalListDatabase db;
//
//    @Before
//    public void createDb() {
//        Context context = ApplicationProvider.getApplicationContext();
//        db = Room.inMemoryDatabaseBuilder(context, AnimalListDatabase.class)
//                .allowMainThreadQueries()
//                .build();
//        dao = db.animalListItemDao();
//    }
//
//    @After
//    public void closeDb() throws IOException {
//        db.close();
//    }
//
//    @Test
//    public void testInsert() {
//        AnimalListItem item1 = new AnimalListItem("Lions", "lions",0);
//        AnimalListItem item2 = new AnimalListItem("Elephant ", "elephant", 1);
//
//        long id1 = dao.insert(item1);
//        long id2 = dao.insert(item2);
//
//        assertNotEquals(id1, id2);
//    }
//
//    @Test
//    public void testGet() {
//        AnimalListItem insertedItem = new AnimalListItem("Giraffe", 0);
//        long id = dao.insert(insertedItem);
//
//        AnimalListItem item = dao.get(id);
//        assertEquals(id, item.id);
//        assertEquals(insertedItem.text, item.text);
//        assertEquals(insertedItem.order, item.order);
//    }
//
//    @Test
//    public void testUpdate () {
//        AnimalListItem item = new AnimalListItem ("Lion",0);
//        long id = dao.insert(item);
//
//        item = dao.get(id);
//        item.text = "African Lion";
//        int itemsUpdated = dao.update(item);
//        assertEquals (1, itemsUpdated) ;
//
//        item = dao.get(id);
//        assertNotNull(item);
//        assertEquals ("African Lion", item.text) ;
//    }
//
//    @Test
//    public void testDelete () {
//        AnimalListItem item = new AnimalListItem("Rhinoceros", 0);
//        Long id = dao.insert(item);
//        item = dao.get(id);
//        int itemsDeleted = dao.delete(item);
//        assertEquals(1, itemsDeleted);
//        assertNull(dao.get(id));
//    }
//}
