package com.example.cinefast;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MovieJsonParser {

    public static ArrayList<Movie> parseMovies(Context context, boolean nowShowingOnly) {
        ArrayList<Movie> movies = new ArrayList<>();

        try {
            // Read JSON file from assets
            InputStream is = context.getAssets().open("movies.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);
            JSONArray moviesArray = jsonObject.getJSONArray("movies");

            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject movieObj = moviesArray.getJSONObject(i);

                boolean isNowShowing = movieObj.getBoolean("isNowShowing");

                // Filter based on nowShowingOnly parameter
                if (nowShowingOnly && !isNowShowing) continue;
                if (!nowShowingOnly && isNowShowing) continue;

                String name = movieObj.getString("name");
                String genre = movieObj.getString("genre");
                String duration = movieObj.getString("duration");
                String posterName = movieObj.getString("poster");
                String trailerUrl = movieObj.getString("trailerUrl");
                String date = movieObj.getString("date");

                // Map drawable name to resource ID
                int posterResId = context.getResources().getIdentifier(
                        posterName, "drawable", context.getPackageName());

                // Fallback to default image if not found
                if (posterResId == 0) {
                    posterResId = R.drawable.img;
                }

                movies.add(new Movie(name, genre, duration, posterResId, trailerUrl, isNowShowing, date));
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }
}
