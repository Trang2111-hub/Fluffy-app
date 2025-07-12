package com.fluffy.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fluffy.app.R;
import com.fluffy.app.model.Feedback;

import java.util.ArrayList;
import java.util.List;

public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.ItemHodel>{
    private List<Feedback> data = new ArrayList<>();
    public FeedBackAdapter(List<Feedback> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ItemHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedback , parent , false);
        return new ItemHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHodel holder, int position) {
        Feedback feedback = data.get(position);
        holder.txtName.setText(feedback.getUserName());
        holder.txtDate.setText(feedback.getDate()+ " | Phân loại hàng:" + feedback.getTypeProduct());
        holder.txtContent.setText(feedback.getContent());
        holder.txtRating.setText(feedback.getRating() + "");
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class ItemHodel extends RecyclerView.ViewHolder{
        private TextView txtName ,txtDate ,txtContent, txtRating;
        private ImageView imgView;
        public ItemHodel(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.user_feedback);
            txtDate = itemView.findViewById(R.id.txt_date_and_type);
            txtContent = itemView.findViewById(R.id.txt_content_feedback);
            txtRating = itemView.findViewById(R.id.txt_rating_feedback);
            imgView = itemView.findViewById(R.id.img_user_feedback);
        }
    }
}
