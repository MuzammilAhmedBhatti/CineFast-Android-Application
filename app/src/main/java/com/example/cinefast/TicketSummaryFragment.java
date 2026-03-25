package com.example.cinefast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class TicketSummaryFragment extends Fragment {

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_summary, container, false);

        // Get arguments
        Bundle args = getArguments();
        if (args != null) {
            movieName = args.getString("MOVIE_NAME", "");
            movieImageResId = args.getInt("MOVIE_IMAGE", R.drawable.img);
            seatCount = args.getInt("SEAT_COUNT", 0);
            ticketPrice = args.getInt("TICKET_PRICE", 0);
            snackPrice = args.getDouble("SNACK_PRICE", 0.0);
            popcornQty = args.getInt("POPCORN_QTY", 0);
            nachosQty = args.getInt("NACHOS_QTY", 0);
            drinkQty = args.getInt("DRINK_QTY", 0);
            candyQty = args.getInt("CANDY_QTY", 0);
            selectedSeats = args.getStringArrayList("SELECTED_SEATS");
        }

        // Initialize views
        movieTitle = view.findViewById(R.id.movieTitle);
        totalAmount = view.findViewById(R.id.totalAmount);
        ticketsContainer = view.findViewById(R.id.ticketsContainer);
        snacksContainer = view.findViewById(R.id.snacksContainer);
        backBtn = view.findViewById(R.id.backBtn);
        moviePoster = view.findViewById(R.id.moviePoster);

        // Set movie poster image
        moviePoster.setImageResource(movieImageResId);

        // Set movie title
        if (movieName != null && !movieName.isEmpty()) {
            movieTitle.setText(movieName);
        }

        // Add tickets
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

        // Save to SharedPreferences
        saveBookingToSharedPreferences(total);

        // Back button
        backBtn.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).navigateToHome();
            }
        });

        // Send Ticket button
        view.findViewById(R.id.sendTicketBtn).setOnClickListener(v -> shareTicket());

        return view;
    }

    private void saveBookingToSharedPreferences(double totalPrice) {
        SharedPreferences prefs = requireContext().getSharedPreferences("CineFastBooking", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("movie_name", movieName);
        editor.putInt("seat_count", seatCount);
        editor.putFloat("total_price", (float) totalPrice);
        editor.apply();
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
            LinearLayout itemLayout = new LinearLayout(requireContext());
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) itemLayout.getLayoutParams();
            params.setMargins(0, 0, 0, dpToPx(8));
            itemLayout.setLayoutParams(params);

            TextView seatText = new TextView(requireContext());
            seatText.setText(seatLabel);
            seatText.setTextColor(Color.parseColor("#BFBFBF"));
            seatText.setTextSize(14);
            seatText.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));

            TextView priceText = new TextView(requireContext());
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
        LinearLayout itemLayout = new LinearLayout(requireContext());
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) itemLayout.getLayoutParams();
        params.setMargins(0, 0, 0, dpToPx(8));
        itemLayout.setLayoutParams(params);

        TextView nameText = new TextView(requireContext());
        nameText.setText(name);
        nameText.setTextColor(Color.parseColor("#BFBFBF"));
        nameText.setTextSize(14);
        nameText.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        ));

        TextView priceText = new TextView(requireContext());
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
