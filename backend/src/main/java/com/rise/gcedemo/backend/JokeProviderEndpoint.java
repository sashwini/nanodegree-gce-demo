package com.rise.gcedemo.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.jokelib.JokeProvider;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Named;

/** An endpoint class we are exposing */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.gcedemo.rise.com",
                ownerName = "backend.gcedemo.rise.com",
                packagePath=""
        )
)
public class JokeProviderEndpoint {

    @ApiMethod(name = "getJoke")
    public JokeBean getJoke(@Named("jokeId") int jokeId) {
        JokeBean jokeBean = new JokeBean();

        JokeProvider jokeProvider = new JokeProvider();
        String jokeString = jokeProvider.getJoke(jokeId);

        try {
            JSONObject jokeJson = new JSONObject(jokeString);
            jokeBean.setJokeId(jokeJson.getInt("jokeId"));
            jokeBean.setJoke(jokeJson.getString("joke"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jokeBean;
    }
}
