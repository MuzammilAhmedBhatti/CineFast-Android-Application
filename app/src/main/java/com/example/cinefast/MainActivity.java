package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toast with tag "CineFAST" when app launches
        Toast.makeText(this, "CineFAST", Toast.LENGTH_SHORT).show();

        sessionManager = new SessionManager(this);

        // Initialize drawer
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        // Set user email in nav header
        View headerView = navigationView.getHeaderView(0);
        TextView navHeaderEmail = headerView.findViewById(R.id.navHeaderEmail);
        String userEmail = sessionManager.getUserEmail();
        if (userEmail != null) {
            navHeaderEmail.setText(userEmail);
        }

        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                navigateToHome();
            } else if (id == R.id.nav_my_bookings) {
                loadFragment(new MyBookingsFragment(), true);
            } else if (id == R.id.nav_logout) {
                logout();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Load HomeFragment as the default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), false);
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void logout() {
        // Clear session
        sessionManager.logout();
        // Sign out from Firebase
        FirebaseAuth.getInstance().signOut();
        // Navigate to Login
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}