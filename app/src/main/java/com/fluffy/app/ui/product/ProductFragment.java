package com.fluffy.app.ui.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.fluffy.app.R;
import com.fluffy.app.adapter.ProductAdapter;
import com.fluffy.app.databinding.FragmentProductBinding;
import com.fluffy.app.model.Product;
import com.fluffy.app.ui.common.BaseHeaderFragment;
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
        // Sử dụng header mặc định
        setHeader(HeaderType.DEFAULT, null);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        // Lấy binding từ contentContainer
        View content = root.findViewById(R.id.contentContainer);
        binding = FragmentProductBinding.bind(content);

        // Lấy danh sách sản phẩm
        productList = JsonUtils.getProductListFromJson(getActivity());
        productAdapter = new ProductAdapter(getActivity(), productList);
        binding.gvProduct.setAdapter(productAdapter);

        // Thiết lập tiêu đề trang (nếu muốn động)
        TextView tvPageTitle = content.findViewById(R.id.tvPageTitle);
        if (tvPageTitle != null) {
            tvPageTitle.setText(R.string.app_name); // hoặc "Sản phẩm"
        }

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
            // Loại bỏ ký tự không phải số (nếu có, ví dụ "100.000đ")
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
}
