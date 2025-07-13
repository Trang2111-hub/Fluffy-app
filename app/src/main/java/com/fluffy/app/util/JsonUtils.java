package com.fluffy.app.util;

import android.content.Context;

import com.fluffy.app.model.Feedback;
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
                int productId = productJson.getInt("product_id");
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
    public static Product findProductById(Context context, int targetId) {
        try {
            InputStream is = context.getAssets().open("products2.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String json = new String(buffer);
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject productJson = jsonArray.getJSONObject(i);
                int id = productJson.getInt("product_id");

                if (id == targetId) {
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
                    if (productJson.has("color")) {
                        JSONArray colorsArray = productJson.getJSONObject("color").optJSONArray("selected_colors");
                        if (colorsArray != null) {
                            for (int j = 0; j < colorsArray.length(); j++) {
                                colors.add(colorsArray.getString(j));
                            }
                        }
                    }

                    List<String> sizes = new ArrayList<>();
                    if (productJson.has("size")) {
                        JSONArray sizesArray = productJson.getJSONObject("size").optJSONArray("available_sizes");
                        if (sizesArray != null) {
                            for (int j = 0; j < sizesArray.length(); j++) {
                                sizes.add(sizesArray.getString(j));
                            }
                        }
                    }

                    return new Product(id, 0, name, discountPrice, originalPrice, imageUrl, 0.0, null, 0, "",
                            rating, colors, sizes, description, collection);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Feedback> getFeedbackListByProductId(Context context, int productId) {
        List<Feedback> feedbackList = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("feedback_product.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String json = new String(buffer);
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject fb = jsonArray.getJSONObject(i);
                int pid = fb.getInt("product_id");
                if (pid == productId) {
                    int customerId = fb.getInt("customer_id");
                    String content = fb.getString("content");
                    String userName = fb.getString("user_name");
                    String date = fb.getString("date");
                    String typeProduct = fb.getString("type_product");
                    int rating = fb.getInt("rating");

                    feedbackList.add(new Feedback(pid,userName ,customerId, content, date, typeProduct, rating));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedbackList;
    }

}