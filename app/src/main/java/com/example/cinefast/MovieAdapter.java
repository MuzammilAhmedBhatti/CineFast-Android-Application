package com.example.cinefast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<Movie> movieList;
    private OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onBookSeatsClick(Movie movie);
        void onTrailerClick(Movie movie);
    }

    public MovieAdapter(ArrayList<Movie> movieList, OnMovieClickListener listener) {
        this.movieList = movieList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.movieName.setText(movie.getName());
        holder.movieGenre.setText(movie.getGenre() + " / " + movie.getDuration() + " | " + movie.getDate());
        holder.moviePoster.setImageResource(movie.getPosterResId());

        holder.bookBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookSeatsClick(movie);
            }
        });

        holder.trailerBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTrailerClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePoster;
        TextView movieName;
        TextView movieGenre;
        Button bookBtn;
        Button trailerBtn;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieName = itemView.findViewById(R.id.movieName);
            movieGenre = itemView.findViewById(R.id.movieGenre);
            bookBtn = itemView.findViewById(R.id.bookBtn);
            trailerBtn = itemView.findViewById(R.id.trailerBtn);
        }
    }
}
