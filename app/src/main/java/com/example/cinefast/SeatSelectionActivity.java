package com.example.cinefast;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeatSelectionActivity extends AppCompatActivity {

    private static final int PRICE_PER_SEAT = 15;
    private static final int COLUMNS = 9; // 9 columns in grid (including space column)
    
    private TextView movieTitle;
    private TextView selectionText;
    private GridLayout seatContainer;
    private Button bookBtn;
    private Button proceedBtn;
    private ImageButton backBtn;
    
    private String selectedMovie;
    private int movieImageResId;
    private Set<Integer> selectedSeats = new HashSet<>();
    private Set<Integer> bookedSeats = new HashSet<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);
        
        // Get movie name from intent
        Intent intent = getIntent();
        selectedMovie = intent.getStringExtra("MOVIE_NAME");
        movieImageResId = intent.getIntExtra("MOVIE_IMAGE", R.drawable.img);
        if (selectedMovie == null) {
            selectedMovie = "Oppenheimer";
        }
        
        // Initialize views
        movieTitle = findViewById(R.id.movieTitle);
        selectionText = findViewById(R.id.selectionText);
        seatContainer = findViewById(R.id.seatContainer);
        bookBtn = findViewById(R.id.bookBtn);
        proceedBtn = findViewById(R.id.proceedBtn);
        backBtn = findViewById(R.id.backBtn);
        
        // Set movie title
        movieTitle.setText(selectedMovie);
        
        // Initialize booked seats for the new 8x8 layout
        // Row 6: seat 2 (index 49)
        // Row 7: seats 7,8 (index 60, 61)
        bookedSeats.addAll(Arrays.asList(49, 60, 61));
        
        // Setup seat click listeners
        setupSeatListeners();
        
        // Setup button listeners
        backBtn.setOnClickListener(v -> finish());
        
        bookBtn.setOnClickListener(v -> {
            if (selectedSeats.isEmpty()) {
                Toast.makeText(this, "Please select at least one seat", Toast.LENGTH_SHORT).show();
            } else {
                // Navigate directly to Ticket Summary (skip snacks)
                Intent summaryIntent = new Intent(SeatSelectionActivity.this, TicketSummaryActivity.class);
                summaryIntent.putExtra("MOVIE_NAME", selectedMovie);
                summaryIntent.putExtra("MOVIE_IMAGE", movieImageResId);
                summaryIntent.putExtra("SEAT_COUNT", selectedSeats.size());
                summaryIntent.putExtra("TICKET_PRICE", selectedSeats.size() * PRICE_PER_SEAT);
                summaryIntent.putExtra("SNACK_PRICE", 0.0);
                summaryIntent.putStringArrayListExtra("SELECTED_SEATS", getSelectedSeatLabels());
                startActivity(summaryIntent);
            }
        });
        
        proceedBtn.setOnClickListener(v -> {
            if (selectedSeats.isEmpty()) {
                Toast.makeText(this, "Please select at least one seat", Toast.LENGTH_SHORT).show();
            } else {
                // Navigate to Snacks Screen
                Intent snacksIntent = new Intent(SeatSelectionActivity.this, SnacksActivity.class);
                snacksIntent.putExtra("MOVIE_NAME", selectedMovie);
                snacksIntent.putExtra("MOVIE_IMAGE", movieImageResId);
                snacksIntent.putExtra("SEAT_COUNT", selectedSeats.size());
                snacksIntent.putExtra("TICKET_PRICE", selectedSeats.size() * PRICE_PER_SEAT);
                snacksIntent.putStringArrayListExtra("SELECTED_SEATS", getSelectedSeatLabels());
                startActivity(snacksIntent);
            }
        });
        
        // Initial state
        updateSelectionText();
    }
    
    private ArrayList<String> getSelectedSeatLabels() {
        ArrayList<String> labels = new ArrayList<>();
        for (int seatIndex : selectedSeats) {
            int row = (seatIndex / COLUMNS) + 1;
            int colInGrid = seatIndex % COLUMNS;
            // Column 4 (index 4) is the space/aisle, so adjust seat number
            int seatNum;
            if (colInGrid < 4) {
                seatNum = colInGrid + 1;
            } else if (colInGrid == 4) {
                // This is a space, shouldn't be selectable
                continue;
            } else {
                seatNum = colInGrid; // seats 5-8 map to columns 5-8
            }
            labels.add("Row " + row + ", Seat " + seatNum);
        }
        return labels;
    }
    
    private void setupSeatListeners() {
        int childCount = seatContainer.getChildCount();
        
        for (int i = 0; i < childCount; i++) {
            View child = seatContainer.getChildAt(i);
            
            // Only add click listeners to View elements (seats), not Spaces
            if (child instanceof View && !(child.getClass().getSimpleName().equals("Space"))) {
                final int seatIndex = i;
                
                // Check if this seat is booked
                if (bookedSeats.contains(seatIndex)) {
                    child.setEnabled(false);
                } else {
                    child.setOnClickListener(v -> toggleSeat(v, seatIndex));
                }
            }
        }
    }
    
    private void toggleSeat(View seatView, int seatIndex) {
        if (selectedSeats.contains(seatIndex)) {
            // Deselect seat
            selectedSeats.remove(seatIndex);
            seatView.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_seat_available));
        } else {
            // Select seat
            selectedSeats.add(seatIndex);
            seatView.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_seat_selected));
        }
        
        updateSelectionText();
    }
    
    private void updateSelectionText() {
        int count = selectedSeats.size();
        int total = count * PRICE_PER_SEAT;
        selectionText.setText("Selected: " + count + " | Total: $" + total);
        
        // Enable/disable proceed button based on selection
        if (count > 0) {
            proceedBtn.setEnabled(true);
            proceedBtn.setAlpha(1.0f);
        } else {
            proceedBtn.setEnabled(false);
            proceedBtn.setAlpha(0.5f);
        }
    }
}
