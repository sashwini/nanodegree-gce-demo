package com.rise.gcedemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.Pair;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.rise.gcedemo.backend.myApi.MyApi;
import com.rise.gcedemo.backend.myApi.model.JokeBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class EndpointsAsyncTask extends AsyncTask<Pair<Context, Integer>, Void, JokeBean> {
    private static MyApi myApiService = null;
    private Context context;
    private Handler mHandler;
    private ProgressDialog progressDialog;
    private final String rootUrl = "https://jokeproviderendpoint-143214.appspot.com/_ah/api/";
    public static final int SUCCESS = 0;
    public static final int FAILURE = -1;

    public EndpointsAsyncTask(Handler mHandler, Context context) {
        this.mHandler = mHandler;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.fetch_joke_progress_dialog_msg));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected JokeBean doInBackground(Pair<Context, Integer>... params) {
        if(myApiService == null) {
            MyApi.Builder builder = new MyApi.Builder(
                    AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(rootUrl);

            myApiService = builder.build();
        }

        context = params[0].first;
        int currentJokeId = params[0].second;

        try {
            return myApiService.getJoke(currentJokeId).execute();
        } catch (IOException e) {
            return new JokeBean();
        }
    }

    @Override
    protected void onPostExecute(JokeBean result) {

        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if(result != null) {
            Message msg = new Message();
            msg.what = SUCCESS;
            Bundle bundle = new Bundle();

            JSONObject jokeBeanJson = new JSONObject();
            try {
                jokeBeanJson.put("jokeId", result.getJokeId());
                jokeBeanJson.put("joke", result.getJoke());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            bundle.putString("Result", jokeBeanJson.toString());
            msg.setData(bundle);
            mHandler.sendMessage(msg);
        }
        else {
            mHandler.sendEmptyMessage(FAILURE);
        }
    }
}
