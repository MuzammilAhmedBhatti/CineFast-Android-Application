package com.example.cinefast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class SnacksFragment extends Fragment implements SnackAdapter.OnQuantityChangeListener {

    private ListView snackListView;
    private Button confirmBtn;
    private ImageButton backBtn;
    private ArrayList<Snack> snackList;

    private String movieName;
    private int movieImageResId;
    private int seatCount;
    private int ticketPrice;
    private ArrayList<String> selectedSeats;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snacks, container, false);

        // Get arguments
        Bundle args = getArguments();
        if (args != null) {
            movieName = args.getString("MOVIE_NAME", "");
            movieImageResId = args.getInt("MOVIE_IMAGE", R.drawable.img);
            seatCount = args.getInt("SEAT_COUNT", 0);
            ticketPrice = args.getInt("TICKET_PRICE", 0);
            selectedSeats = args.getStringArrayList("SELECTED_SEATS");
        }

        // Initialize views
        snackListView = view.findViewById(R.id.snackListView);
        confirmBtn = view.findViewById(R.id.confirmBtn);
        backBtn = view.findViewById(R.id.backBtn);

        // Load snacks from SQLite database instead of hardcoding
        SnackDbHelper dbHelper = new SnackDbHelper(requireContext());
        snackList = dbHelper.getAllSnacks();

        // Setup adapter
        SnackAdapter adapter = new SnackAdapter(requireContext(), snackList, this);
        snackListView.setAdapter(adapter);

        // Back button
        backBtn.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Confirm button
        confirmBtn.setOnClickListener(v -> {
            double snackTotal = 0;
            int popcornQty = 0, nachosQty = 0, drinkQty = 0, candyQty = 0;

            for (int i = 0; i < snackList.size(); i++) {
                Snack snack = snackList.get(i);
                snackTotal += snack.getTotalPrice();

                switch (i) {
                    case 0: popcornQty = snack.getQuantity(); break;
                    case 1: nachosQty = snack.getQuantity(); break;
                    case 2: drinkQty = snack.getQuantity(); break;
                    case 3: candyQty = snack.getQuantity(); break;
                }
            }

            ((MainActivity) requireActivity()).navigateToTicketSummary(
                    movieName, movieImageResId, seatCount, ticketPrice, snackTotal,
                    popcornQty, nachosQty, drinkQty, candyQty, selectedSeats
            );
        });

        return view;
    }

    @Override
    public void onQuantityChanged() {
        // Can update UI if needed when quantities change
    }
}
