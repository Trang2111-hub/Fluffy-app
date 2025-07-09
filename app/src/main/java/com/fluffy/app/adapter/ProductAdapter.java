package com.fluffy.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fluffy.app.R;
import com.fluffy.app.model.Product;
import com.fluffy.app.ui.favorite_product.FavoriteProductFragment;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private List<Product> productList;
    private FavoriteProductFragment favoriteFragment;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public void setFavoriteFragment(FavoriteProductFragment favoriteFragment) {
        this.favoriteFragment = favoriteFragment;
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

        // Thêm nút tim (favorite)
        ImageView ivFavorite = view.findViewById(R.id.iv_favorite);
        if (ivFavorite != null) {
            ivFavorite.setOnClickListener(v -> {
                if (favoriteFragment != null) {
                    int productId = product.getProductId();
                    if (ivFavorite.getTag() != null && (boolean) ivFavorite.getTag()) {
                        ivFavorite.setImageResource(R.drawable.ic_heart_outline); // Chưa yêu thích
                        ivFavorite.setTag(false);
                        favoriteFragment.removeFavoriteProduct(productId, context); // Truyền context
                    } else {
                        ivFavorite.setImageResource(R.drawable.ic_heart_filled); // Đã yêu thích
                        ivFavorite.setTag(true);
                        favoriteFragment.addFavoriteProduct(productId, context); // Truyền context
                    }
                }
            });

            // Kiểm tra trạng thái yêu thích ban đầu
            if (favoriteFragment != null && favoriteFragment.getFavoriteProductIds().contains(product.getProductId())) {
                ivFavorite.setImageResource(R.drawable.ic_heart_filled);
                ivFavorite.setTag(true);
            } else {
                ivFavorite.setImageResource(R.drawable.ic_heart_outline);
                ivFavorite.setTag(false);
            }
        }

        return view;
    }
}