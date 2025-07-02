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

        // Load product image
        ImageView ivProductImage = view.findViewById(R.id.iv_product_image);
        new DownloadImageTask(ivProductImage).execute(product.getImageUrl());

        return view;
    }

    // AsyncTask to download image from URL
    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
