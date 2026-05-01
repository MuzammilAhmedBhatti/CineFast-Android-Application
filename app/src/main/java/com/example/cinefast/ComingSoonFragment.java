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

public class ComingSoonFragment extends Fragment implements MovieAdapter.OnMovieClickListener {

    private RecyclerView recyclerView;
    private ArrayList<Movie> movieList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coming_soon, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Load movies from JSON instead of hardcoding (coming soon = false for nowShowingOnly)
        movieList = MovieJsonParser.parseMovies(requireContext(), false);

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
