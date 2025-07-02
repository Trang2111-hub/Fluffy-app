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
            // Load JSON file from assets
            InputStream is = context.getAssets().open("products2.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            // Parse the JSON file
            String json = new String(buffer);
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject productJson = jsonArray.getJSONObject(i);
                String name = productJson.getString("product_name");
                JSONObject pricing = productJson.getJSONObject("pricing");
                String discountPrice = pricing.getString("discount_price");
                String originalPrice = pricing.getString("original_price");
                String imageUrl = productJson.getString("image");
                // Nếu imageUrl không có http/https, thêm "https:" vào đầu
                if (!imageUrl.startsWith("http")) {
                    imageUrl = "https:" + imageUrl;
                }
                Product product = new Product(name, discountPrice, originalPrice, imageUrl);
                productList.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }
}
