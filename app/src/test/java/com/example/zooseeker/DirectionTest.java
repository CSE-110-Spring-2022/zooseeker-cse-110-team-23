package com.example.zooseeker;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.util.ArrayList;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DirectionTest {
    @Test
    public void Direction() {
        DirectionActivity newDir = new DirectionActivity();
        ArrayList<String> log = new ArrayList<>();
        newDir.setLog(log);
        String a = newDir.DirectionString(1, "HI", "HELLO", "BYE", 5.0, "THIS");
        assertEquals("HI1. Walk 5.0 meters along THIS from HELLO to BYE\n", a);
    }
}