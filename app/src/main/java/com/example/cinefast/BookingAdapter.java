package com.example.cinefast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private ArrayList<Booking> bookingList;
    private Context context;
    private OnCancelClickListener cancelListener;

    public interface OnCancelClickListener {
        void onCancelClick(Booking booking, int position);
    }

    public BookingAdapter(Context context, ArrayList<Booking> bookingList, OnCancelClickListener listener) {
        this.context = context;
        this.bookingList = bookingList;
        this.cancelListener = listener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.movieName.setText(booking.getMovieName());
        holder.dateTime.setText(booking.getDateTime());
        holder.ticketCount.setText(booking.getSeats() + " Tickets");

        // Set movie poster
        if (booking.getMovieImage() != null && !booking.getMovieImage().isEmpty()) {
            int imageResId = context.getResources().getIdentifier(
                    booking.getMovieImage(), "drawable", context.getPackageName());
            if (imageResId != 0) {
                holder.moviePoster.setImageResource(imageResId);
            } else {
                holder.moviePoster.setImageResource(R.drawable.img);
            }
        } else {
            holder.moviePoster.setImageResource(R.drawable.img);
        }

        // Cancel button
        holder.cancelBtn.setOnClickListener(v -> {
            if (cancelListener != null) {
                cancelListener.onCancelClick(booking, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public void removeItem(int position) {
        bookingList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, bookingList.size());
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePoster;
        TextView movieName;
        TextView dateTime;
        TextView ticketCount;
        ImageButton cancelBtn;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.bookingMoviePoster);
            movieName = itemView.findViewById(R.id.bookingMovieName);
            dateTime = itemView.findViewById(R.id.bookingDateTime);
            ticketCount = itemView.findViewById(R.id.bookingTicketCount);
            cancelBtn = itemView.findViewById(R.id.cancelBookingBtn);
        }
    }
}
