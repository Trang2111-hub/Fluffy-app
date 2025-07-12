package com.fluffy.app.ui.productInformation;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fluffy.app.R;
import com.fluffy.app.adapter.ColorOptionAdapter;
import com.fluffy.app.adapter.FeedBackAdapter;
import com.fluffy.app.adapter.ProductCategoryAdapter;
import com.fluffy.app.adapter.ProductOtherInformationAdapter;
import com.fluffy.app.adapter.SizeOptionAdapter;
import com.fluffy.app.model.ColorOption;
import com.fluffy.app.model.Feedback;
import com.fluffy.app.model.Product;
import com.fluffy.app.model.SizeOption;
import com.fluffy.app.model.TypeProduct;
import com.fluffy.app.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_information);
        TextView txtPrice = findViewById(R.id.txtPriceSellInProductInformation);
        TextView txtName = findViewById(R.id.txt_name_product_information);
        TextView txtPriceMain = findViewById(R.id.txt_price_product_information);
        ImageView imgMain = findViewById(R.id.product_infor_img_main);
        TextView txtDescription = findViewById(R.id.txt_description_information);
        TextView txtRating = findViewById(R.id.txt_rating_information);
        txtPrice.setPaintFlags(txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);



        int productId = getIntent().getIntExtra("product_id", -1);
        String productImg = getIntent().getStringExtra("product_img");
        String productName = getIntent().getStringExtra("product_name");
        String productPrice = getIntent().getStringExtra("product_price");
        String productDiscount = getIntent().getStringExtra("product_discount");
        String productDescription = getIntent().getStringExtra("product_description");
        double productRating = getIntent().getDoubleExtra("product_rating", 0.0);
        txtName.setText(productName);
        txtPriceMain.setText(productPrice);
        txtPrice.setText(productDiscount + "đ");
        txtDescription.setText(productDescription);
        txtRating.setText(productRating + "");
        String imageUrl = productImg;

        if (!imageUrl.startsWith("http")) {
            imageUrl = "https:" + imageUrl;
        }
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imgMain);

        RecyclerView recyclerView = findViewById(R.id.rcvCateInProductInformation);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        List<TypeProduct> data = new ArrayList<>();
        data.add(new TypeProduct("Tím trơn", R.drawable.type_01));
        data.add(new TypeProduct("Tím sọc xanh", R.drawable.type_02));
        data.add(new TypeProduct("Nơ vàng", R.drawable.type_03));
        data.add(new TypeProduct("Khăn nâu", R.drawable.type_04));
        data.add(new TypeProduct("Nơ tím", R.drawable.type_01));
        data.add(new TypeProduct("Nơ hồng", R.drawable.type_03));

        ProductCategoryAdapter adapter = new ProductCategoryAdapter(data);
        recyclerView.setAdapter(adapter);
        //==========================
        List<Feedback> feedbacks = JsonUtils.getFeedbackListByProductId(this, productId);
        for (Feedback fb : feedbacks) {
            Log.d("Feedback", fb.getContent());
        }
        RecyclerView rcvFeedback = findViewById(R.id.rcv_feedback_product);
        rcvFeedback.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        FeedBackAdapter feedBackAdapter = new FeedBackAdapter(feedbacks);
        rcvFeedback.setAdapter(feedBackAdapter);
        // =========================
        RecyclerView rcvProductOther = findViewById(R.id.rcv_product_other_information);
        rcvProductOther.setLayoutManager(new GridLayoutManager(this, 2));
        List<Product> dataProductOther = new ArrayList<>();
        dataProductOther = JsonUtils.getProductListFromJson(this);
        ProductOtherInformationAdapter productOIAdapter = new ProductOtherInformationAdapter(dataProductOther , this);
        rcvProductOther.setAdapter(productOIAdapter);
        //===========================
        RecyclerView rcvColorOption = findViewById(R.id.rcvColorOption);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rcvColorOption.setLayoutManager(layoutManager);

        List<ColorOption> colorOptions = new ArrayList<>();
        colorOptions.add(new ColorOption("Tím trơn", R.drawable.type_01));
        colorOptions.add(new ColorOption("Tím sọc xanh", R.drawable.type_02));
        colorOptions.add(new ColorOption("Nơ vàng", R.drawable.type_03));
        colorOptions.add(new ColorOption("Khăn nâu", R.drawable.type_04));
        colorOptions.add(new ColorOption("Nơ tím", R.drawable.type_01));
        colorOptions.add(new ColorOption("Nơ hồng", R.drawable.type_03));

        ColorOptionAdapter colorOptionAdapter = new ColorOptionAdapter(colorOptions);
        rcvColorOption.setAdapter(colorOptionAdapter);
        //===========================
        RecyclerView rcvSizeOption = findViewById(R.id.recyclerViewSize);
        List<SizeOption> sizeList = new ArrayList<>();
        sizeList.add(new SizeOption("25cm"));
        sizeList.add(new SizeOption("45cm"));
        sizeList.add(new SizeOption("65cm"));
        sizeList.add(new SizeOption("75cm"));
        sizeList.add(new SizeOption("95cm"));

        SizeOptionAdapter sizeOptionAdapter = new SizeOptionAdapter(sizeList);

        rcvSizeOption.setLayoutManager(new GridLayoutManager(this, 3));
        rcvSizeOption.setAdapter(sizeOptionAdapter);
        //===========================
        TextView btnAddToCart = findViewById(R.id.btnAddToCart);
        View overlay = findViewById(R.id.overlayView_product_information);
        LinearLayout add_cart_product_information = findViewById(R.id.add_cart_product_information);
        btnAddToCart.setOnClickListener(v -> {
            overlay.setVisibility(View.VISIBLE);
            add_cart_product_information.setVisibility(View.VISIBLE);
        });
        overlay.setOnClickListener(v -> {
            overlay.setVisibility(View.GONE);
            add_cart_product_information.setVisibility(View.GONE);
        });
    }
}