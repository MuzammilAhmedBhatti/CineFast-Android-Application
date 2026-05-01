package com.example.cinefast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MyBookingsFragment extends Fragment implements BookingAdapter.OnCancelClickListener {

    private RecyclerView bookingsRecyclerView;
    private TextView emptyText;
    private ImageButton backBtn, menuBtn;
    private BookingAdapter adapter;
    private ArrayList<Booking> bookingList;
    private DatabaseReference bookingsRef;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_bookings, container, false);

        // Initialize views
        bookingsRecyclerView = view.findViewById(R.id.bookingsRecyclerView);
        emptyText = view.findViewById(R.id.emptyText);
        backBtn = view.findViewById(R.id.backBtn);
        menuBtn = view.findViewById(R.id.menuBtn);

        // Setup RecyclerView
        bookingList = new ArrayList<>();
        adapter = new BookingAdapter(requireContext(), bookingList, this);
        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bookingsRecyclerView.setAdapter(adapter);

        // Get user ID
        SessionManager sessionManager = new SessionManager(requireContext());
        userId = sessionManager.getUserId();

        // Back button
        backBtn.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Menu button opens drawer
        menuBtn.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });

        // Fetch bookings from Firebase
        if (userId != null) {
            fetchBookings();
        } else {
            emptyText.setVisibility(View.VISIBLE);
            emptyText.setText("Please login to view bookings");
        }

        return view;
    }

    private void fetchBookings() {
        bookingsRef = FirebaseDatabase.getInstance()
                .getReference("bookings")
                .child(userId);

        bookingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingList.clear();

                for (DataSnapshot bookingSnapshot : snapshot.getChildren()) {
                    Booking booking = bookingSnapshot.getValue(Booking.class);
                    if (booking != null) {
                        booking.setBookingId(bookingSnapshot.getKey());
                        bookingList.add(booking);
                    }
                }

                adapter.notifyDataSetChanged();

                if (bookingList.isEmpty()) {
                    emptyText.setVisibility(View.VISIBLE);
                    bookingsRecyclerView.setVisibility(View.GONE);
                } else {
                    emptyText.setVisibility(View.GONE);
                    bookingsRecyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Failed to load bookings", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCancelClick(Booking booking, int position) {
        // Check if booking is in the future
        if (!isBookingInFuture(booking.getDateTime())) {
            Toast.makeText(requireContext(), "Cannot cancel past bookings", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show confirmation dialog
        new AlertDialog.Builder(requireContext())
                .setTitle("Cancel Booking")
                .setMessage("Are you sure you want to cancel this booking?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    cancelBooking(booking, position);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void cancelBooking(Booking booking, int position) {
        if (userId == null || booking.getBookingId() == null) return;

        DatabaseReference bookingRef = FirebaseDatabase.getInstance()
                .getReference("bookings")
                .child(userId)
                .child(booking.getBookingId());

        bookingRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                adapter.removeItem(position);
                Toast.makeText(requireContext(), "Booking Cancelled Successfully", Toast.LENGTH_SHORT).show();

                if (bookingList.isEmpty()) {
                    emptyText.setVisibility(View.VISIBLE);
                    bookingsRecyclerView.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(requireContext(), "Failed to cancel booking", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isBookingInFuture(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return false;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault());
            Date bookingDate = sdf.parse(dateTimeStr);
            Date currentDate = new Date();

            return bookingDate != null && bookingDate.after(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
