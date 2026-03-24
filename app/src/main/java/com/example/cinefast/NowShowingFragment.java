package com.example.cinefast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NowShowingFragment extends Fragment implements MovieAdapter.OnMovieClickListener {

    private RecyclerView recyclerView;
    private ArrayList<Movie> movieList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_showing, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Populate movie list
        movieList = new ArrayList<>();
        movieList.add(new Movie("Spiderman", "Action", "152 min", R.drawable.m1, "https://www.youtube.com/watch?v=JfVOs4VSpmA", true));
        movieList.add(new Movie("Batman", "Action", "180 min", R.drawable.m2, "https://www.youtube.com/watch?v=mqqft2x_Aa4", true));
        movieList.add(new Movie("Aquaman", "Action", "163 min", R.drawable.m3, "https://www.youtube.com/watch?v=WDkg3h8PCVU", true));

        MovieAdapter adapter = new MovieAdapter(movieList, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onBookSeatsClick(Movie movie) {
        ((MainActivity) requireActivity()).navigateToSeatSelection(movie);
    }

    @Override
    public void onTrailerClick(Movie movie) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailerUrl()));
        startActivity(intent);
    }
}
