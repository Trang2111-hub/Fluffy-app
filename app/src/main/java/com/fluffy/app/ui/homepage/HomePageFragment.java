package com.fluffy.app.ui.homepage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.viewpager2.widget.ViewPager2;

import com.fluffy.app.R;
import com.fluffy.app.adapter.BannerAdapter;
import com.fluffy.app.adapter.ProductAdapter;
import com.fluffy.app.model.Product;
import com.fluffy.app.ui.common.BaseHeaderFragment;
import com.fluffy.app.util.JsonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class HomePageFragment extends BaseHeaderFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Handler sliderHandler;
    private Runnable sliderRunnable;
    private ViewPager2 viewPager;

    public HomePageFragment() {
    }

    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHeader(HeaderType.DEFAULT, null);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_home_page;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // === BANNER ===
        viewPager = view.findViewById(R.id.viewPager);
        CircleIndicator3 indicator = view.findViewById(R.id.indicator);

        List<Integer> banners = Arrays.asList(
                R.drawable.homepage1,
                R.drawable.homepage2,
                R.drawable.homepage3
        );

        BannerAdapter adapter = new BannerAdapter(banners);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);

        sliderHandler = new Handler(Looper.getMainLooper());
        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                int nextItem = (viewPager.getCurrentItem() + 1) % banners.size();
                viewPager.setCurrentItem(nextItem, true);
                sliderHandler.postDelayed(this, 5000);
            }
        };
        sliderHandler.postDelayed(sliderRunnable, 5000);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 5000);
            }
        });

        // === HOT PRODUCTS GRIDVIEW ===
        GridView gvHotProducts = view.findViewById(R.id.gvHotProducts);

        List<Product> allProducts = JsonUtils.getProductListFromJson(getContext());
        List<Product> hotProducts = new ArrayList<>();

        for (int i = 0; i < allProducts.size() && i < 2; i++) {
            hotProducts.add(allProducts.get(i));
        }

        ProductAdapter hotAdapter = new ProductAdapter(getActivity(), hotProducts);
        gvHotProducts.setAdapter(hotAdapter);

        // === HOT PRODUCTS GRIDVIEW 2 (MỚI) ===
        GridView gvHotProducts2 = view.findViewById(R.id.gvHotProducts2);

        List<Product> hotProducts2 = new ArrayList<>();
        for (int i = 2; i < allProducts.size() && i < 6; i++) {
            hotProducts2.add(allProducts.get(i));
        }

        ProductAdapter hotAdapter2 = new ProductAdapter(getActivity(), hotProducts2);
        gvHotProducts2.setAdapter(hotAdapter2);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (sliderHandler != null) {
            sliderHandler.removeCallbacksAndMessages(null);
        }
    }
}
