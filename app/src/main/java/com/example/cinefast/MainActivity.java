package com.example.cinefast;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load HomeFragment as the default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), false);
        }
    }

    public void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    public void navigateToSeatSelection(Movie movie) {
        SeatSelectionFragment fragment = new SeatSelectionFragment();
        Bundle args = new Bundle();
        args.putString("MOVIE_NAME", movie.getName());
        args.putInt("MOVIE_IMAGE", movie.getPosterResId());
        args.putBoolean("IS_NOW_SHOWING", movie.isNowShowing());
        args.putString("TRAILER_URL", movie.getTrailerUrl());
        fragment.setArguments(args);
        loadFragment(fragment, true);
    }

    public void navigateToSnacks(String movieName, int movieImage, int seatCount, int ticketPrice, ArrayList<String> selectedSeats) {
        SnacksFragment fragment = new SnacksFragment();
        Bundle args = new Bundle();
        args.putString("MOVIE_NAME", movieName);
        args.putInt("MOVIE_IMAGE", movieImage);
        args.putInt("SEAT_COUNT", seatCount);
        args.putInt("TICKET_PRICE", ticketPrice);
        args.putStringArrayList("SELECTED_SEATS", selectedSeats);
        fragment.setArguments(args);
        loadFragment(fragment, true);
    }

    public void navigateToTicketSummary(String movieName, int movieImage, int seatCount, int ticketPrice, double snackPrice, int popcornQty, int nachosQty, int drinkQty, int candyQty, ArrayList<String> selectedSeats) {
        TicketSummaryFragment fragment = new TicketSummaryFragment();
        Bundle args = new Bundle();
        args.putString("MOVIE_NAME", movieName);
        args.putInt("MOVIE_IMAGE", movieImage);
        args.putInt("SEAT_COUNT", seatCount);
        args.putInt("TICKET_PRICE", ticketPrice);
        args.putDouble("SNACK_PRICE", snackPrice);
        args.putInt("POPCORN_QTY", popcornQty);
        args.putInt("NACHOS_QTY", nachosQty);
        args.putInt("DRINK_QTY", drinkQty);
        args.putInt("CANDY_QTY", candyQty);
        args.putStringArrayList("SELECTED_SEATS", selectedSeats);
        fragment.setArguments(args);
        loadFragment(fragment, true);
    }

    public void navigateToHome() {
        // Clear back stack and go to home
        getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
        loadFragment(new HomeFragment(), false);
    }
}