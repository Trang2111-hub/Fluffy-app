package com.fluffy.app.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fluffy.app.R;
import com.fluffy.app.model.TypeProduct;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.ItemHandle>{
    private List<TypeProduct> _data = new ArrayList<>();
    public ProductCategoryAdapter(List<TypeProduct> data){
        _data = data;
    }

    @NonNull
    @Override
    public ItemHandle onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_in_product_information , parent ,false);
        return new ItemHandle(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHandle holder, int position) {
        TypeProduct item = _data.get(position);
        holder.txtCate.setText(item.getNameType());
        holder.imgView.setImageResource(item.getImg());
    }

    @Override
    public int getItemCount() {
        return _data.size();
    }

    public class ItemHandle extends RecyclerView.ViewHolder{
        private ImageView imgView;
        private TextView txtCate;
        public ItemHandle(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imageCategoryInProductInformation);
            txtCate = itemView.findViewById(R.id.txtCateInProductInformation);
        }
    }
}

