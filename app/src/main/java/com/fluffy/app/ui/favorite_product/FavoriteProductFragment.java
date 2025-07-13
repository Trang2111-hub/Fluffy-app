package com.fluffy.app.ui.favorite_product;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.fluffy.app.R;
import com.fluffy.app.adapter.ProductAdapter;
import com.fluffy.app.model.Product;
import com.fluffy.app.ui.common.BaseHeaderFragment;
import com.fluffy.app.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FavoriteProductFragment extends BaseHeaderFragment {
    private ProductAdapter productAdapter;
    private List<Product> favoriteProductList = new ArrayList<>();
    private List<Integer> favoriteProductIds = new ArrayList<>();
    private GridView gvFavorite;
    private Context appContext;

    public FavoriteProductFragment() {
    }
    public static FavoriteProductFragment newInstance() {
        return new FavoriteProductFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHeader(HeaderType.CUSTOM, "Sản phẩm yêu thích");

        if (getActivity() != null) {
            appContext = getActivity().getApplicationContext();
        }

        loadFavoriteProductIds();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_favorite_product;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);

        View content = root.findViewById(R.id.contentContainer);
        gvFavorite = content.findViewById(R.id.gvFavorite);


        productAdapter = new ProductAdapter(getActivity(), favoriteProductList);
        productAdapter.setFavoriteFragment(this);
        gvFavorite.setAdapter(productAdapter);

        updateFavoriteProductList();

        return root;

    }

    // Thêm sản phẩm vào danh sách yêu thích, nhận Context từ caller
    public void addFavoriteProduct(int productId, Context context) {
        if (!favoriteProductIds.contains(productId)) {
            favoriteProductIds.add(productId);
            updateFavoriteProductList(context);
            saveFavoriteProductIds(context);
        }
    }


    public void removeFavoriteProduct(int productId, Context context) {
        favoriteProductIds.remove((Integer) productId);
        updateFavoriteProductList(context);
        saveFavoriteProductIds(context);
    }


    public List<Integer> getFavoriteProductIds() {
        return favoriteProductIds;
    }

    // Cập nhật danh sách sản phẩm yêu thích dựa trên favoriteProductIds
    private void updateFavoriteProductList(Context context) {
        if (context == null) {
            return;
        }

        List<Product> allProducts = JsonUtils.getProductListFromJson(context);

        // Lọc các sản phẩm có ID nằm trong favoriteProductIds
        favoriteProductList.clear();
        for (Product product : allProducts) {
            if (favoriteProductIds.contains(product.getProductId())) {
                favoriteProductList.add(product);
            }
        }


        if (productAdapter != null) {
            productAdapter.notifyDataSetChanged();
        }
    }

    private void updateFavoriteProductList() {
        updateFavoriteProductList(appContext);
    }


    private void saveFavoriteProductIds(Context context) {
        if (context == null) {
            return;

        }
        SharedPreferences prefs = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String ids = favoriteProductIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        editor.putString("favorite_ids", ids);
        editor.apply();
    }


    private void loadFavoriteProductIds() {
        if (appContext == null) {
            return;

        }
        SharedPreferences prefs = appContext.getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        String ids = prefs.getString("favorite_ids", "");
        if (!ids.isEmpty()) {
            favoriteProductIds.clear();
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                try {
                    favoriteProductIds.add(Integer.parseInt(id));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        productAdapter = null;
        gvFavorite = null;
    }
}