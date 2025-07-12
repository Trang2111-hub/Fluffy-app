package com.fluffy.app.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fluffy.app.MainActivity;
import com.fluffy.app.R;
import com.fluffy.app.adapter.ProductAdapter;
import com.fluffy.app.databinding.FragmentProductBinding;
import com.fluffy.app.model.Product;
import com.fluffy.app.ui.common.BaseHeaderFragment;
import com.fluffy.app.ui.favorite_product.FavoriteProductFragment;
import com.fluffy.app.ui.productInformation.ProductInformationActivity;
import com.fluffy.app.util.JsonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductFragment extends BaseHeaderFragment {
    private FragmentProductBinding binding;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHeader(HeaderType.DEFAULT, null);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        View content = root.findViewById(R.id.contentContainer);
        binding = FragmentProductBinding.bind(content);

        // Lấy danh sách sản phẩm
        productList = JsonUtils.getProductListFromJson(getActivity());
        productAdapter = new ProductAdapter(getActivity(), productList);
        // Sử dụng instance từ MainActivity
        productAdapter.setFavoriteFragment(((MainActivity) requireActivity()).getFavoriteFragment());
        binding.gvProduct.setAdapter(productAdapter);

        // Thiết lập Spinner sort
        Spinner spinnerSort = content.findViewById(R.id.spinnerSort);
        if (spinnerSort != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.product_sort_options, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSort.setAdapter(adapter);
            spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            sortByNameAZ(); break;
                        case 1:
                            sortByNameZA(); break;
                        case 2:
                            sortByPriceAsc(); break;
                        case 3:
                            sortByPriceDesc(); break;
                    }
                    productAdapter.notifyDataSetChanged();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }

        return root;
    }

    private void sortByNameAZ() {
        Collections.sort(productList, Comparator.comparing(Product::getName, String::compareToIgnoreCase));
    }

    private void sortByNameZA() {
        Collections.sort(productList, (a, b) -> b.getName().compareToIgnoreCase(a.getName()));
    }

    private void sortByPriceAsc() {
        Collections.sort(productList, Comparator.comparingDouble(this::parseDiscountPrice));
    }

    private void sortByPriceDesc() {
        Collections.sort(productList, (a, b) -> Double.compare(parseDiscountPrice(b), parseDiscountPrice(a)));
    }

    private double parseDiscountPrice(Product product) {
        try {
            return Double.parseDouble(product.getDiscountPrice().replaceAll("[^\\d.]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onProductClick(Product product) {
        Toast.makeText(getContext(), product.getProductId() + "", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), ProductInformationActivity.class);
        intent.putExtra("product_id", product.getProductId());
        intent.putExtra("product_img", product.getImageUrl());
        intent.putExtra("product_name", product.getName());
        intent.putExtra("product_price", product.getDiscountPrice());
        intent.putExtra("product_discount",  product.getOriginalPrice());
        intent.putExtra("product_description", product.getDescription());
        intent.putExtra("product_rating", product.getRating());
        startActivity(intent);
    }
}