package com.example.zooseeker;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BriefButtonTest {
    @Test
    public void BriefButton() {
        int Btn = R.id.show_brief_btn;
        assertNotNull(Btn);
    }

    @Test
    public void NextButtonFunction() {
        int Btn = R.id.skip_next_btn;
        assertNotNull(Btn);
    }
}