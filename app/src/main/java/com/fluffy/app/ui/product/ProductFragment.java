package com.fluffy.app.ui.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fluffy.app.R;
import com.fluffy.app.adapter.ProductAdapter;
import com.fluffy.app.databinding.FragmentProductBinding;
import com.fluffy.app.ui.common.BaseHeaderFragment;
import com.fluffy.app.util.JsonUtils;

public class ProductFragment extends BaseHeaderFragment {
    private FragmentProductBinding binding;

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
        ProductAdapter productAdapter = new ProductAdapter(getActivity(), JsonUtils.getProductListFromJson(getActivity()));
        binding.gvProduct.setAdapter(productAdapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
