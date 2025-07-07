package com.fluffy.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fluffy.app.R;
import com.fluffy.app.model.Product;

import java.io.InputStream;
import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        }

        Product product = productList.get(position);

        // Set product name
        TextView tvProductName = view.findViewById(R.id.tv_product_name);
        tvProductName.setText(product.getName());

        // Set discounted price
        TextView tvDiscountPrice = view.findViewById(R.id.tv_discount_price);
        tvDiscountPrice.setText(product.getDiscountPrice());

        // Set original price
        TextView tvOriginalPrice = view.findViewById(R.id.tv_original_price);
        tvOriginalPrice.setText(product.getOriginalPrice());

        // Load product image using Glide
        ImageView ivProductImage = view.findViewById(R.id.iv_product_image);
        String imageUrl = product.getImageUrl();
        if (!imageUrl.startsWith("http")) {
            imageUrl = "https:" + imageUrl;
        }
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(ivProductImage);

        // Set product rating
        TextView tvProductRating = view.findViewById(R.id.tv_product_rating);
        tvProductRating.setText(String.format("%.1f ★", product.getRating()));

        return view;
    }
}
