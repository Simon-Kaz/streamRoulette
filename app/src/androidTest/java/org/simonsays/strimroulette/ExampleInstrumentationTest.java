package org.simonsays.strimroulette;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simonsays.strimroulette.utils.http.HttpUtils;


import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@MediumTest
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentationTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("org.simonsays.strimroulette", appContext.getPackageName());
    }
//    @Test
//    public void httpUtilsTest(){
//        HttpUtils httpUtils = new HttpUtils();
//        JSONObject jsonObject = httpUtils.getTopStream();
//        try {
//            JSONArray jsonArray = jsonObject.getJSONArray("streams");
//            JSONObject firstStream = jsonArray.getJSONObject(0);
//            Long viewers = firstStream.getLong("viewers");
//            String game = firstStream.getString("game");
//            JSONObject channel = firstStream.getJSONObject("channel");
//            String streamTitle = channel.getString("status");
//            System.out.println(viewers + " - " + game  + " - "+ streamTitle);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}