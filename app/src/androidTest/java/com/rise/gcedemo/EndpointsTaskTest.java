package com.rise.gcedemo;

import android.content.Context;
import android.support.v4.util.Pair;
import android.test.ActivityInstrumentationTestCase2;

import com.rise.gcedemo.backend.myApi.model.JokeBean;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ashwini on 9/22/2016.
 * Reference URL for async task test - https://gist.github.com/he9lin/2195897
 */
public class EndpointsTaskTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static boolean called;
    private MainActivity.UiUpdaterHandler handler;
    private Context context;

    public EndpointsTaskTest() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        called = false;
        context = getInstrumentation().getContext();
        handler = new MainActivity.UiUpdaterHandler(getActivity());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public final void testSuccessfulFetch() throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                new EndpointsAsyncTask(handler, getActivity()) {
                    @Override
                    protected void onPostExecute(JokeBean response) {
                        super.onPostExecute(response);

                        assertNotNull(response);
                        String joke = response.getJoke();
                        assertNotNull(joke);
                        signal.countDown();
                    }
                }.execute(new Pair<Context, Integer>(getActivity(), 0));
            }
        });

        signal.await(10, TimeUnit.SECONDS);
        assertTrue(called);
    }
}

