package com.example.cinefast;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private Button todayBtn, tomorrowBtn;
    private ImageButton menuBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        todayBtn = view.findViewById(R.id.todayBtn);
        tomorrowBtn = view.findViewById(R.id.tomorrowBtn);
        menuBtn = view.findViewById(R.id.menuBtn);

        // Setup ViewPager2 with adapter
        HomePagerAdapter pagerAdapter = new HomePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Connect TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Now Showing");
            } else {
                tab.setText("Coming Soon");
            }
        }).attach();

        // Date Toggle
        todayBtn.setOnClickListener(v -> {
            todayBtn.setBackgroundTintList(requireContext().getColorStateList(R.color.red));
            tomorrowBtn.setBackgroundTintList(requireContext().getColorStateList(R.color.dark));
            Toast.makeText(requireContext(), "Showing Today Movies", Toast.LENGTH_SHORT).show();
        });

        tomorrowBtn.setOnClickListener(v -> {
            tomorrowBtn.setBackgroundTintList(requireContext().getColorStateList(R.color.red));
            todayBtn.setBackgroundTintList(requireContext().getColorStateList(R.color.dark));
            Toast.makeText(requireContext(), "Showing Tomorrow Movies", Toast.LENGTH_SHORT).show();
        });

        // Three-dots menu
        menuBtn.setOnClickListener(v -> showPopupMenu(v));

        return view;
    }

    private void showPopupMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), anchor);
        popupMenu.getMenuInflater().inflate(R.menu.menu_home, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_view_last_booking) {
                showLastBookingDialog();
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void showLastBookingDialog() {
        SharedPreferences prefs = requireContext().getSharedPreferences("CineFastBooking", Context.MODE_PRIVATE);
        String movieName = prefs.getString("movie_name", null);
        int seatCount = prefs.getInt("seat_count", 0);
        float totalPrice = prefs.getFloat("total_price", 0f);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Last Booking");

        if (movieName != null) {
            String message = "Movie: " + movieName + "\n"
                    + "Seats: " + seatCount + "\n"
                    + "Total Price: $" + String.format("%.2f", totalPrice);
            builder.setMessage(message);
        } else {
            builder.setMessage("No previous booking found.");
        }

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
