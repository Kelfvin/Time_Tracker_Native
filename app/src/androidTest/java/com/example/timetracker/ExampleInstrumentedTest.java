package com.example.timetracker;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.timetracker.data.dao.ActivityDao;
import com.example.timetracker.data.model.Activity;
import com.example.timetracker.data.model.Record;
import com.example.timetracker.data.repository.ActivityRepository;

import java.util.List;
import java.util.Map;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.timetracker", appContext.getPackageName());
    }

    @Test
    public void testActivityDao() {

        ActivityRepository activityRepository = new ActivityRepository(InstrumentationRegistry.getInstrumentation().getTargetContext());


    }
}