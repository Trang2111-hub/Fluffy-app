package com.fluffy.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fluffy.app.R;
import com.fluffy.app.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductOtherInformationAdapter extends RecyclerView.Adapter<ProductOtherInformationAdapter.ItemHodel>{
    private Context context;
    private List<Product> listData = new ArrayList<>();
    public ProductOtherInformationAdapter(List<Product> data , Context context){
        this.listData = data;
        this.context = context;
    }
    @NonNull
    @Override
    public ItemHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_information , parent ,false);
        return new ItemHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHodel holder, int position) {
        Product product = this.listData.get(position);

        String imageUrl = product.getImageUrl();
        if (!imageUrl.startsWith("http")) {
            imageUrl = "https:" + imageUrl;
        }
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imgView);
        holder.txtName.setText(product.getName());
        holder.txtPrice.setText(product.getDiscountPrice());
        holder.txtDiscount.setText(product.getOriginalPrice());
    }

    @Override
    public int getItemCount() {
        return this.listData.size();
    }

    public class ItemHodel extends RecyclerView.ViewHolder{
        private ImageView imgView;
        private TextView txtName;
        private TextView txtPrice;
        private TextView txtDiscount;
        public ItemHodel(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imgProductOtherInformation);
            txtName = itemView.findViewById(R.id.txtNameOtherInformation);
            txtPrice = itemView.findViewById(R.id.txtPriceOtherInformation);
            txtDiscount = itemView.findViewById(R.id.txtOldPriceOtherInformation);
        }
    }
}
