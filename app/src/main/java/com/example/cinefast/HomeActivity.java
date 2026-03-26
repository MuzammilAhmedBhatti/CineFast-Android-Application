package com.example.cinefast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    Button todayBtn, tomorrowBtn;
    Button book1, trailer1;
    Button book2, trailer2;
    Button book3, trailer3;
    Button book4, trailer4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Date Buttons
        todayBtn = findViewById(R.id.todayBtn);
        tomorrowBtn = findViewById(R.id.tomorrowBtn);

        // Movie Buttons
        book1 = findViewById(R.id.book1);
        trailer1 = findViewById(R.id.trailer1);

        book2 = findViewById(R.id.book2);
        trailer2 = findViewById(R.id.trailer2);

        book3 = findViewById(R.id.book3);
        trailer3 = findViewById(R.id.trailer3);

        book4 = findViewById(R.id.book4);
        trailer4 = findViewById(R.id.trailer4);

        // Date Toggle
        todayBtn.setOnClickListener(v -> {
            todayBtn.setBackgroundTintList(getColorStateList(R.color.red));
            tomorrowBtn.setBackgroundTintList(getColorStateList(R.color.dark));
            Toast.makeText(this, "Showing Today Movies", Toast.LENGTH_SHORT).show();
        });

        tomorrowBtn.setOnClickListener(v -> {
            tomorrowBtn.setBackgroundTintList(getColorStateList(R.color.red));
            todayBtn.setBackgroundTintList(getColorStateList(R.color.dark));
            Toast.makeText(this, "Showing Tomorrow Movies", Toast.LENGTH_SHORT).show();
        });

        // Movie 1
        book1.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SeatSelectionActivity.class);
            intent.putExtra("MOVIE_NAME", "Spiderman");
            intent.putExtra("MOVIE_IMAGE", R.drawable.m1);
            startActivity(intent);
        });

        trailer1.setOnClickListener(v -> openYouTubeTrailer("https://www.youtube.com/watch?v=JfVOs4VSpmA"));

        // Movie 2
        book2.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SeatSelectionActivity.class);
            intent.putExtra("MOVIE_NAME", "Batman");
            intent.putExtra("MOVIE_IMAGE", R.drawable.m2);
            startActivity(intent);
        });

        trailer2.setOnClickListener(v -> openYouTubeTrailer("https://www.youtube.com/watch?v=mqqft2x_Aa4"));

        // Movie 3
        book3.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SeatSelectionActivity.class);
            intent.putExtra("MOVIE_NAME", "Aquaman");
            intent.putExtra("MOVIE_IMAGE", R.drawable.m3);
            startActivity(intent);
        });

        trailer3.setOnClickListener(v -> openYouTubeTrailer("https://www.youtube.com/watch?v=WDkg3h8PCVU"));

        // Movie 4
        book4.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SeatSelectionActivity.class);
            intent.putExtra("MOVIE_NAME", "The Last Of Us");
            intent.putExtra("MOVIE_IMAGE", R.drawable.m4);
            startActivity(intent);
        });

        trailer4.setOnClickListener(v -> openYouTubeTrailer("https://www.youtube.com/watch?v=uLtkt8BonwM"));
    }

    private void openYouTubeTrailer(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
