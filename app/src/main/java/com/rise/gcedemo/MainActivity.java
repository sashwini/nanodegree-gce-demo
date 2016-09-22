package com.rise.gcedemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.rise.gcedemo.uilibrary.DisplayJokeActivity;;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private Button btnGetJoke = null;
    private int latestJokeId = 0;
    private String latestJoke = "";
    private UiUpdaterHandler mHandler = null;
    private final String testDeviceId = "7CF819F6E59D16A262A91550287E4936";
    private final String admobId = "ca-app-pub-9542071828172374~6928039241";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView mAdView = (AdView) findViewById(R.id.gceDemoAdView);
        mHandler = new UiUpdaterHandler(this);
        btnGetJoke = (Button) findViewById(R.id.btnGetJoke);
        btnGetJoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EndpointsAsyncTask(mHandler, MainActivity.this).execute(
                        new Pair<Context, Integer>(MainActivity.this, latestJokeId));
            }
        });


        if( !BuildConfig.IS_PAID ) {
            mAdView.setVisibility(View.VISIBLE);

            MobileAds.initialize(getApplicationContext(), admobId);
            AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
            adRequestBuilder.addTestDevice(testDeviceId);
            AdRequest adRequest = adRequestBuilder.build();
            mAdView.loadAd(adRequest);
        }
        else {
            mAdView.setVisibility(View.GONE);
        }
    }

    static class UiUpdaterHandler extends Handler {

        private WeakReference<MainActivity> activityWeakReference;

        public UiUpdaterHandler(MainActivity activity) {
            activityWeakReference = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            MainActivity activity = activityWeakReference.get();
            if(activity != null) {
                switch (msg.what) {
                    case EndpointsAsyncTask.SUCCESS: {
                        Bundle bundle = msg.getData();
                        if(bundle != null) {
                            try {
                                String result = bundle.getString("Result");

                                JSONObject jokeJson = new JSONObject(result);
                                activity.showJoke(jokeJson);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(activity,
                                        activity.getString(R.string.server_error_title),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    }
                    case EndpointsAsyncTask.FAILURE: {
                        break;
                    }
                }
            }
        }
    }

    private void showJoke(JSONObject jokeJson) {
        try {
            latestJokeId = jokeJson.getInt("jokeId");
            latestJoke = jokeJson.getString("joke");

            Intent displayJokeIntent = new Intent(MainActivity.this, DisplayJokeActivity.class);
            displayJokeIntent.putExtra("JOKE", latestJoke);
            startActivity(displayJokeIntent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
