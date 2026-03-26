package com.example.cinefast;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TicketSummaryActivity extends AppCompatActivity {

    private static final double POPCORN_PRICE = 8.99;
    private static final double NACHOS_PRICE = 7.99;
    private static final double DRINK_PRICE = 5.99;
    private static final double CANDY_PRICE = 6.99;
    private static final int PRICE_PER_SEAT = 15;

    private TextView movieTitle;
    private TextView totalAmount;
    private LinearLayout ticketsContainer;
    private LinearLayout snacksContainer;
    private ImageButton backBtn;
    private ImageView moviePoster;

    private String movieName;
    private int movieImageResId;
    private int seatCount;
    private int ticketPrice;
    private double snackPrice;
    private int popcornQty, nachosQty, drinkQty, candyQty;
    private ArrayList<String> selectedSeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_summary);

        // Get data from intent
        Intent intent = getIntent();
        movieName = intent.getStringExtra("MOVIE_NAME");
        movieImageResId = intent.getIntExtra("MOVIE_IMAGE", R.drawable.img);
        seatCount = intent.getIntExtra("SEAT_COUNT", 0);
        ticketPrice = intent.getIntExtra("TICKET_PRICE", 0);
        snackPrice = intent.getDoubleExtra("SNACK_PRICE", 0.0);
        popcornQty = intent.getIntExtra("POPCORN_QTY", 0);
        nachosQty = intent.getIntExtra("NACHOS_QTY", 0);
        drinkQty = intent.getIntExtra("DRINK_QTY", 0);
        candyQty = intent.getIntExtra("CANDY_QTY", 0);
        selectedSeats = intent.getStringArrayListExtra("SELECTED_SEATS");

        // Initialize views
        movieTitle = findViewById(R.id.movieTitle);
        totalAmount = findViewById(R.id.totalAmount);
        ticketsContainer = findViewById(R.id.ticketsContainer);
        snacksContainer = findViewById(R.id.snacksContainer);
        backBtn = findViewById(R.id.backBtn);
        moviePoster = findViewById(R.id.moviePoster);

        // Set movie poster image
        moviePoster.setImageResource(movieImageResId);

        // Set movie title
        if (movieName != null && !movieName.isEmpty()) {
            movieTitle.setText(movieName);
        }

        // Add tickets using actual selected seat labels from intent
        addTicketItems(selectedSeats);

        // Add snacks
        if (popcornQty > 0) {
            addSnackItem("X" + popcornQty + " Medium Soft Popcorn", popcornQty * POPCORN_PRICE);
        }
        if (nachosQty > 0) {
            addSnackItem("X" + nachosQty + " Large Caramel Popcorn", nachosQty * NACHOS_PRICE);
        }
        if (drinkQty > 0) {
            addSnackItem("X" + drinkQty + " Large Soft Drink", drinkQty * DRINK_PRICE);
        }
        if (candyQty > 0) {
            addSnackItem("X" + candyQty + " Candy Mix", candyQty * CANDY_PRICE);
        }

        // Calculate total
        double total = ticketPrice + snackPrice;
        totalAmount.setText(String.format("%.2f USD", total));

        // Back button
        backBtn.setOnClickListener(v -> finish());

        // Send Ticket button - share ticket via Android share sheet
        findViewById(R.id.sendTicketBtn).setOnClickListener(v -> shareTicket());
    }

    private void shareTicket() {
        double total = ticketPrice + snackPrice;

        StringBuilder sb = new StringBuilder();
        sb.append("🎬 CineFAST Booking Confirmation\n");
        sb.append("================================\n\n");
        sb.append("Movie: ").append(movieName != null ? movieName : "N/A").append("\n");
        sb.append("Theater: Stars (OPRMAI)\n");
        sb.append("Hall: 1st\n");
        sb.append("Date: 13.04.2026\n");
        sb.append("Time: 22:15\n\n");

        if (selectedSeats != null && !selectedSeats.isEmpty()) {
            sb.append("🎟 Tickets:\n");
            for (String seat : selectedSeats) {
                sb.append("  • ").append(seat).append(" - $").append(PRICE_PER_SEAT).append("\n");
            }
            sb.append("\n");
        }

        if (popcornQty > 0 || nachosQty > 0 || drinkQty > 0 || candyQty > 0) {
            sb.append("🍿 Snacks:\n");
            if (popcornQty > 0) sb.append("  • x").append(popcornQty).append(" Popcorn - $").append(String.format("%.2f", popcornQty * POPCORN_PRICE)).append("\n");
            if (nachosQty > 0) sb.append("  • x").append(nachosQty).append(" Nachos - $").append(String.format("%.2f", nachosQty * NACHOS_PRICE)).append("\n");
            if (drinkQty > 0) sb.append("  • x").append(drinkQty).append(" Soft Drink - $").append(String.format("%.2f", drinkQty * DRINK_PRICE)).append("\n");
            if (candyQty > 0) sb.append("  • x").append(candyQty).append(" Candy Mix - $").append(String.format("%.2f", candyQty * CANDY_PRICE)).append("\n");
            sb.append("\n");
        }

        sb.append("💰 Total: $").append(String.format("%.2f", total)).append("\n");
        sb.append("\nBooked via CineFAST 🎥");

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "CineFAST Ticket - " + (movieName != null ? movieName : "Movie"));
        shareIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        startActivity(Intent.createChooser(shareIntent, "Share Ticket via"));
    }

    private void addTicketItems(ArrayList<String> selectedSeats) {
        if (selectedSeats == null || selectedSeats.isEmpty()) {
            return;
        }

        for (String seatLabel : selectedSeats) {
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) itemLayout.getLayoutParams();
            params.setMargins(0, 0, 0, dpToPx(8));
            itemLayout.setLayoutParams(params);

            TextView seatText = new TextView(this);
            seatText.setText(seatLabel);
            seatText.setTextColor(Color.parseColor("#BFBFBF"));
            seatText.setTextSize(14);
            seatText.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));

            TextView priceText = new TextView(this);
            priceText.setText(PRICE_PER_SEAT + " USD");
            priceText.setTextColor(Color.WHITE);
            priceText.setTextSize(14);
            priceText.setGravity(Gravity.END);

            itemLayout.addView(seatText);
            itemLayout.addView(priceText);
            ticketsContainer.addView(itemLayout);
        }
    }

    private void addSnackItem(String name, double price) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) itemLayout.getLayoutParams();
        params.setMargins(0, 0, 0, dpToPx(8));
        itemLayout.setLayoutParams(params);

        TextView nameText = new TextView(this);
        nameText.setText(name);
        nameText.setTextColor(Color.parseColor("#BFBFBF"));
        nameText.setTextSize(14);
        nameText.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        ));

        TextView priceText = new TextView(this);
        priceText.setText(String.format("%.0f USD", price));
        priceText.setTextColor(Color.WHITE);
        priceText.setTextSize(14);
        priceText.setGravity(Gravity.END);

        itemLayout.addView(nameText);
        itemLayout.addView(priceText);
        snacksContainer.addView(itemLayout);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}

