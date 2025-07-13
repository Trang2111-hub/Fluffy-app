package com.fluffy.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fluffy.app.R;
import com.fluffy.app.model.SizeOption;

import java.util.List;

public class SizeOptionAdapter extends RecyclerView.Adapter<SizeOptionAdapter.ViewHolder> {
    private List<SizeOption> sizeList;

    public SizeOptionAdapter(List<SizeOption> sizeList) {
        this.sizeList = sizeList;
    }

    @NonNull
    @Override
    public SizeOptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_size_option, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeOptionAdapter.ViewHolder holder, int position) {
        holder.txtSize.setText(sizeList.get(position).getSize());
    }

    @Override
    public int getItemCount() {
        return sizeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSize;

        public ViewHolder(View itemView) {
            super(itemView);
            txtSize = itemView.findViewById(R.id.txtSize);
        }
    }
}

