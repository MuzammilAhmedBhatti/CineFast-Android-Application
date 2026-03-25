package com.example.cinefast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SnackAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Snack> snackList;
    private OnQuantityChangeListener listener;

    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }

    public SnackAdapter(Context context, ArrayList<Snack> snackList, OnQuantityChangeListener listener) {
        this.context = context;
        this.snackList = snackList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return snackList.size();
    }

    @Override
    public Object getItem(int position) {
        return snackList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_snack, parent, false);
            holder = new ViewHolder();
            holder.snackImage = convertView.findViewById(R.id.snackImage);
            holder.snackName = convertView.findViewById(R.id.snackName);
            holder.snackPrice = convertView.findViewById(R.id.snackPrice);
            holder.snackQuantity = convertView.findViewById(R.id.snackQuantity);
            holder.plusBtn = convertView.findViewById(R.id.plusBtn);
            holder.minusBtn = convertView.findViewById(R.id.minusBtn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Snack snack = snackList.get(position);

        holder.snackImage.setImageResource(snack.getImageResId());
        holder.snackName.setText(snack.getName());
        holder.snackPrice.setText(String.format("$%.2f", snack.getPrice()));
        holder.snackQuantity.setText(String.valueOf(snack.getQuantity()));

        holder.plusBtn.setOnClickListener(v -> {
            snack.incrementQuantity();
            holder.snackQuantity.setText(String.valueOf(snack.getQuantity()));
            if (listener != null) {
                listener.onQuantityChanged();
            }
        });

        holder.minusBtn.setOnClickListener(v -> {
            snack.decrementQuantity();
            holder.snackQuantity.setText(String.valueOf(snack.getQuantity()));
            if (listener != null) {
                listener.onQuantityChanged();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView snackImage;
        TextView snackName;
        TextView snackPrice;
        TextView snackQuantity;
        Button plusBtn;
        Button minusBtn;
    }
}
