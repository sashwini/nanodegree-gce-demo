package com.jokelib;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class JokeProvider {

    private String[] jokes = {"abc", "def", "ghi", "jkl", "mno", "pqr"};

    public String getJoke(int jokeId) {

        Random r = new Random();
        int low = 0;
        int high = jokes.length;
        int jokeCount = r.nextInt(high-low) + low;

        while (jokeId == jokeCount) {
            jokeCount = r.nextInt(high-low) + low;
        }

        try {
            JSONObject jokeJsonObject = new JSONObject();
            jokeJsonObject.put("jokeId", jokeCount);
            jokeJsonObject.put("joke", jokes[jokeCount]);

            return jokeJsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
