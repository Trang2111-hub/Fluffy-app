package com.fluffy.app.ui.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.fluffy.app.R;
import com.fluffy.app.adapter.ProductAdapter;
import com.fluffy.app.databinding.FragmentProductBinding;
import com.fluffy.app.util.JsonUtils;

public class ProductFragment extends Fragment {

    private FragmentProductBinding binding;

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the binding
        binding = FragmentProductBinding.inflate(inflater, container, false);

        // Load product data from JSON
        ProductAdapter productAdapter = new ProductAdapter(getActivity(), JsonUtils.getProductListFromJson(getActivity()));
        binding.gvProduct.setAdapter(productAdapter);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Set binding to null to avoid memory leaks
        binding = null;
    }
}
