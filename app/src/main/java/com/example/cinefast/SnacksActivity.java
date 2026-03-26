package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SnacksActivity extends AppCompatActivity {

    private static final double POPCORN_PRICE = 8.99;
    private static final double NACHOS_PRICE = 7.99;
    private static final double DRINK_PRICE = 5.99;
    private static final double CANDY_PRICE = 6.99;

    private int popcornQty = 0;
    private int nachosQty = 0;
    private int drinkQty = 0;
    private int candyQty = 0;

    private TextView popcornQuantityText;
    private TextView nachosQuantityText;
    private TextView drinkQuantityText;
    private TextView candyQuantityText;

    private Button confirmBtn;
    private ImageButton backBtn;

    private String movieName;
    private int movieImageResId;
    private int seatCount;
    private int ticketPrice;
    private ArrayList<String> selectedSeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snacks);

        // Get data from intent
        Intent intent = getIntent();
        movieName = intent.getStringExtra("MOVIE_NAME");
        movieImageResId = intent.getIntExtra("MOVIE_IMAGE", R.drawable.img);
        seatCount = intent.getIntExtra("SEAT_COUNT", 0);
        ticketPrice = intent.getIntExtra("TICKET_PRICE", 0);
        selectedSeats = intent.getStringArrayListExtra("SELECTED_SEATS");

        // Initialize views
        popcornQuantityText = findViewById(R.id.popcornQuantity);
        nachosQuantityText = findViewById(R.id.nachosQuantity);
        drinkQuantityText = findViewById(R.id.drinkQuantity);
        candyQuantityText = findViewById(R.id.candyQuantity);
        confirmBtn = findViewById(R.id.confirmBtn);
        backBtn = findViewById(R.id.backBtn);

        // Back button
        backBtn.setOnClickListener(v -> finish());

        // Popcorn controls
        findViewById(R.id.popcornPlusBtn).setOnClickListener(v -> {
            popcornQty++;
            popcornQuantityText.setText(String.valueOf(popcornQty));
        });
        findViewById(R.id.popcornMinusBtn).setOnClickListener(v -> {
            if (popcornQty > 0) {
                popcornQty--;
                popcornQuantityText.setText(String.valueOf(popcornQty));
            }
        });

        // Nachos controls
        findViewById(R.id.nachosPlusBtn).setOnClickListener(v -> {
            nachosQty++;
            nachosQuantityText.setText(String.valueOf(nachosQty));
        });
        findViewById(R.id.nachosMinusBtn).setOnClickListener(v -> {
            if (nachosQty > 0) {
                nachosQty--;
                nachosQuantityText.setText(String.valueOf(nachosQty));
            }
        });

        // Drink controls
        findViewById(R.id.drinkPlusBtn).setOnClickListener(v -> {
            drinkQty++;
            drinkQuantityText.setText(String.valueOf(drinkQty));
        });
        findViewById(R.id.drinkMinusBtn).setOnClickListener(v -> {
            if (drinkQty > 0) {
                drinkQty--;
                drinkQuantityText.setText(String.valueOf(drinkQty));
            }
        });

        // Candy controls
        findViewById(R.id.candyPlusBtn).setOnClickListener(v -> {
            candyQty++;
            candyQuantityText.setText(String.valueOf(candyQty));
        });
        findViewById(R.id.candyMinusBtn).setOnClickListener(v -> {
            if (candyQty > 0) {
                candyQty--;
                candyQuantityText.setText(String.valueOf(candyQty));
            }
        });

        // Confirm button - pass everything through intent to TicketSummary
        confirmBtn.setOnClickListener(v -> {
            double snackTotal = (popcornQty * POPCORN_PRICE) +
                              (nachosQty * NACHOS_PRICE) +
                              (drinkQty * DRINK_PRICE) +
                              (candyQty * CANDY_PRICE);

            Intent summaryIntent = new Intent(SnacksActivity.this, TicketSummaryActivity.class);
            summaryIntent.putExtra("MOVIE_NAME", movieName);
            summaryIntent.putExtra("MOVIE_IMAGE", movieImageResId);
            summaryIntent.putExtra("SEAT_COUNT", seatCount);
            summaryIntent.putExtra("TICKET_PRICE", ticketPrice);
            summaryIntent.putExtra("SNACK_PRICE", snackTotal);
            summaryIntent.putExtra("POPCORN_QTY", popcornQty);
            summaryIntent.putExtra("NACHOS_QTY", nachosQty);
            summaryIntent.putExtra("DRINK_QTY", drinkQty);
            summaryIntent.putExtra("CANDY_QTY", candyQty);
            summaryIntent.putStringArrayListExtra("SELECTED_SEATS", selectedSeats);
            startActivity(summaryIntent);
        });
    }
}
