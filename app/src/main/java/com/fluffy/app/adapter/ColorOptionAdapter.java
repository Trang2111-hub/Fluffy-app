package com.fluffy.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fluffy.app.R;
import com.fluffy.app.model.ColorOption;

import java.util.List;

public class ColorOptionAdapter extends RecyclerView.Adapter<ColorOptionAdapter.ColorViewHolder> {
    private List<ColorOption> colorList;

    public ColorOptionAdapter(List<ColorOption> colorList) {
        this.colorList = colorList;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_color_option, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        ColorOption color = colorList.get(position);
        holder.txtColorName.setText(color.getName());
        holder.imgColor.setImageResource(color.getImageResId());
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {
        ImageView imgColor;
        TextView txtColorName;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            imgColor = itemView.findViewById(R.id.imgColor);
            txtColorName = itemView.findViewById(R.id.txtColorName);
        }
    }
}
