package com.example.cinefast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeatSelectionFragment extends Fragment {

    private static final int PRICE_PER_SEAT = 15;
    private static final int COLUMNS = 9;

    private TextView movieTitle;
    private TextView selectionText;
    private GridLayout seatContainer;
    private Button bookBtn, proceedBtn;
    private Button comingSoonBtn, watchTrailerBtn;
    private LinearLayout nowShowingButtons, comingSoonButtons;
    private ImageButton backBtn;

    private String selectedMovie;
    private int movieImageResId;
    private boolean isNowShowing;
    private String trailerUrl;
    private Set<Integer> selectedSeats = new HashSet<>();
    private Set<Integer> bookedSeats = new HashSet<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seat_selection, container, false);

        // Get arguments
        Bundle args = getArguments();
        if (args != null) {
            selectedMovie = args.getString("MOVIE_NAME", "Oppenheimer");
            movieImageResId = args.getInt("MOVIE_IMAGE", R.drawable.img);
            isNowShowing = args.getBoolean("IS_NOW_SHOWING", true);
            trailerUrl = args.getString("TRAILER_URL", "");
        }

        // Initialize views
        movieTitle = view.findViewById(R.id.movieTitle);
        selectionText = view.findViewById(R.id.selectionText);
        seatContainer = view.findViewById(R.id.seatContainer);
        bookBtn = view.findViewById(R.id.bookBtn);
        proceedBtn = view.findViewById(R.id.proceedBtn);
        comingSoonBtn = view.findViewById(R.id.comingSoonBtn);
        watchTrailerBtn = view.findViewById(R.id.watchTrailerBtn);
        nowShowingButtons = view.findViewById(R.id.nowShowingButtons);
        comingSoonButtons = view.findViewById(R.id.comingSoonButtons);
        backBtn = view.findViewById(R.id.backBtn);

        // Set movie title
        movieTitle.setText(selectedMovie);

        // Fetch booked seats from Firebase for this movie, then setup UI
        fetchBookedSeatsFromFirebase();

        // Back button
        backBtn.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    /**
     * Fetch all bookings from Firebase for ALL users, filter by this movie name,
     * and collect their seat labels. Convert labels back to seat indices and mark as booked.
     */
    private void fetchBookedSeatsFromFirebase() {
        DatabaseReference allBookingsRef = FirebaseDatabase.getInstance()
                .getReference("bookings");

        allBookingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookedSeats.clear();

                // Iterate over all users
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    // Iterate over each user's bookings
                    for (DataSnapshot bookingSnapshot : userSnapshot.getChildren()) {
                        Booking booking = bookingSnapshot.getValue(Booking.class);
                        if (booking != null && selectedMovie != null
                                && selectedMovie.equals(booking.getMovieName())) {
                            // This booking is for the same movie — mark its seats as booked
                            List<String> seatLabels = booking.getSelectedSeatLabels();
                            if (seatLabels != null) {
                                for (String label : seatLabels) {
                                    int seatIndex = seatLabelToIndex(label);
                                    if (seatIndex >= 0) {
                                        bookedSeats.add(seatIndex);
                                    }
                                }
                            }
                        }
                    }
                }

                // Now setup the UI with the actual booked seats
                setupUIAfterFetch();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // If Firebase fails, proceed with no booked seats
                setupUIAfterFetch();
            }
        });
    }

    /**
     * Called after Firebase data is loaded. Sets up the seat grid and buttons.
     */
    private void setupUIAfterFetch() {
        if (isNowShowing) {
            setupNowShowingMode();
        } else {
            setupComingSoonMode();
        }

        // Apply booked seat styling (red background) and setup listeners
        setupSeatListeners();

        // Initial state
        updateSelectionText();
    }

    /**
     * Convert a seat label like "Row 3, Seat 5" back to the grid index.
     *
     * The label is created in getSelectedSeatLabels():
     *   row = (seatIndex / COLUMNS) + 1
     *   colInGrid = seatIndex % COLUMNS
     *   if colInGrid < 4 → seatNum = colInGrid + 1
     *   if colInGrid == 4 → skip (space column)
     *   if colInGrid > 4 → seatNum = colInGrid
     *
     * Reverse:
     *   seatIndex = (row - 1) * COLUMNS + colInGrid
     *   For seatNum 1-4: colInGrid = seatNum - 1
     *   For seatNum 5-8: colInGrid = seatNum
     */
    private int seatLabelToIndex(String label) {
        try {
            // Expected format: "Row X, Seat Y"
            String[] parts = label.split(",");
            int row = Integer.parseInt(parts[0].trim().replace("Row ", ""));
            int seatNum = Integer.parseInt(parts[1].trim().replace("Seat ", ""));

            int colInGrid;
            if (seatNum <= 4) {
                colInGrid = seatNum - 1;
            } else {
                colInGrid = seatNum;
            }

            return (row - 1) * COLUMNS + colInGrid;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void setupNowShowingMode() {
        nowShowingButtons.setVisibility(View.VISIBLE);
        comingSoonButtons.setVisibility(View.GONE);

        bookBtn.setOnClickListener(v -> {
            if (selectedSeats.isEmpty()) {
                Toast.makeText(requireContext(), "Please select at least one seat", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Booking Confirmed!", Toast.LENGTH_SHORT).show();
                ((MainActivity) requireActivity()).navigateToTicketSummary(
                        selectedMovie, movieImageResId, selectedSeats.size(),
                        selectedSeats.size() * PRICE_PER_SEAT, 0.0,
                        0, 0, 0, 0, getSelectedSeatLabels()
                );
            }
        });

        proceedBtn.setOnClickListener(v -> {
            if (selectedSeats.isEmpty()) {
                Toast.makeText(requireContext(), "Please select at least one seat", Toast.LENGTH_SHORT).show();
            } else {
                ((MainActivity) requireActivity()).navigateToSnacks(
                        selectedMovie, movieImageResId, selectedSeats.size(),
                        selectedSeats.size() * PRICE_PER_SEAT, getSelectedSeatLabels()
                );
            }
        });
    }

    private void setupComingSoonMode() {
        nowShowingButtons.setVisibility(View.GONE);
        comingSoonButtons.setVisibility(View.VISIBLE);

        // Update selection text to indicate coming soon
        selectionText.setText("This movie is coming soon - seats unavailable");

        // Dim the seat container to visually indicate seats are disabled
        seatContainer.setAlpha(0.4f);

        // Coming Soon button is disabled
        comingSoonBtn.setEnabled(false);

        // Watch Trailer opens YouTube
        watchTrailerBtn.setOnClickListener(v -> {
            if (trailerUrl != null && !trailerUrl.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
                startActivity(intent);
            }
        });
    }

    private void setupSeatListeners() {
        int childCount = seatContainer.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = seatContainer.getChildAt(i);

            if (child instanceof View && !(child.getClass().getSimpleName().equals("Space"))) {
                final int seatIndex = i;

                if (!isNowShowing) {
                    // Coming Soon: disable all seats
                    child.setEnabled(false);
                } else if (bookedSeats.contains(seatIndex)) {
                    // Seat is already booked — show red and disable
                    child.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_seat_booked));
                    child.setEnabled(false);
                } else {
                    child.setOnClickListener(v -> toggleSeat(v, seatIndex));
                }
            }
        }
    }

    private void toggleSeat(View seatView, int seatIndex) {
        if (selectedSeats.contains(seatIndex)) {
            selectedSeats.remove(seatIndex);
            seatView.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_seat_available));
        } else {
            selectedSeats.add(seatIndex);
            seatView.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_seat_selected));
        }
        updateSelectionText();
    }

    private void updateSelectionText() {
        int count = selectedSeats.size();
        int total = count * PRICE_PER_SEAT;
        selectionText.setText("Selected: " + count + " | Total: $" + total);

        if (proceedBtn != null) {
            if (count > 0) {
                proceedBtn.setEnabled(true);
                proceedBtn.setAlpha(1.0f);
            } else {
                proceedBtn.setEnabled(false);
                proceedBtn.setAlpha(0.5f);
            }
        }
    }

    private ArrayList<String> getSelectedSeatLabels() {
        ArrayList<String> labels = new ArrayList<>();
        for (int seatIndex : selectedSeats) {
            int row = (seatIndex / COLUMNS) + 1;
            int colInGrid = seatIndex % COLUMNS;
            int seatNum;
            if (colInGrid < 4) {
                seatNum = colInGrid + 1;
            } else if (colInGrid == 4) {
                continue;
            } else {
                seatNum = colInGrid;
            }
            labels.add("Row " + row + ", Seat " + seatNum);
        }
        return labels;
    }
}
