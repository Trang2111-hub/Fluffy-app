package com.fluffy.app.util;

import android.content.Context;

import com.fluffy.app.model.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static List<Product> getProductListFromJson(Context context) {
        List<Product> productList = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("products2.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String json = new String(buffer);
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject productJson = jsonArray.getJSONObject(i);
                int productId = productJson.getInt("product_id"); // Giả sử JSON có trường "product_id"
                String name = productJson.getString("product_name");
                JSONObject pricing = productJson.getJSONObject("pricing");
                String discountPrice = pricing.getString("discount_price");
                String originalPrice = pricing.getString("original_price");
                String imageUrl = productJson.getString("image");
                if (!imageUrl.startsWith("http")) {
                    imageUrl = "https:" + imageUrl;
                }
                double rating = productJson.optDouble("rating", 0.0);
                String description = productJson.optString("description", "");
                String collection = productJson.optString("collection", "");
                List<String> colors = new ArrayList<>();
                JSONArray colorsArray = productJson.optJSONArray("colors");
                if (colorsArray != null) {
                    for (int j = 0; j < colorsArray.length(); j++) {
                        colors.add(colorsArray.getString(j));
                    }
                }
                List<String> sizes = new ArrayList<>();
                JSONArray sizesArray = productJson.optJSONArray("sizes");
                if (sizesArray != null) {
                    for (int j = 0; j < sizesArray.length(); j++) {
                        sizes.add(sizesArray.getString(j));
                    }
                }
                Product product = new Product(productId, 0, name, discountPrice, originalPrice, imageUrl, 0.0, null, 0, "",
                        rating, colors, sizes, description, collection);
                productList.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }
}