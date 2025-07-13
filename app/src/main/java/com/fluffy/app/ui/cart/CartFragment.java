package com.fluffy.app.ui.cart;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.fluffy.app.R;
import com.fluffy.app.model.CartItem;
import com.fluffy.app.adapter.CartAdapter;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ListView listView;
    private TextView totalPriceText;
    private CheckBox checkAll;
    private Button btnCheckout;

    private List<CartItem> cartItems;
    private CartAdapter adapter;

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        listView = view.findViewById(R.id.listView);
        totalPriceText = view.findViewById(R.id.totalPrice);
        checkAll = view.findViewById(R.id.checkAll);
        btnCheckout = view.findViewById(R.id.btnCheckout);

        loadCartItems();
        setupListView();
        setupEvents();

        return view;
    }

    private void loadCartItems() {
        cartItems = new ArrayList<>();
        // fake data giống demo
        cartItems.add(new CartItem("Capybara cùng 6 con nhỏ", "Nâu, 40cm", 230000, 290000, 2, true));
        cartItems.add(new CartItem("Capybara cùng 6 con nhỏ", "Nâu, 40cm", 230000, 290000, 2, true));
        cartItems.add(new CartItem("Capybara cùng 6 con nhỏ", "Nâu, 40cm", 230000, 290000, 2, true));
    }

    private void setupListView() {
        adapter = new CartAdapter(getContext(), cartItems, new CartAdapter.OnCartChangeListener() {
            @Override
            public void onCartChanged() {
                updateTotalPrice();
                checkAll.setChecked(areAllItemsChecked());
            }
        });
        listView.setAdapter(adapter);
        updateTotalPrice();
    }

    private void setupEvents() {
        checkAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            for (CartItem item : cartItems) {
                item.setChecked(isChecked);
            }
            adapter.notifyDataSetChanged();
            updateTotalPrice();
        });

        btnCheckout.setOnClickListener(v ->
                Toast.makeText(getContext(), "Đang thanh toán...", Toast.LENGTH_SHORT).show()
        );
    }

    private void updateTotalPrice() {
        int total = 0;
        for (CartItem item : cartItems) {
            if (item.isChecked()) {
                total += item.getPrice() * item.getQuantity();
            }
        }
        totalPriceText.setText(String.format("%,dđ", total));
    }

    private boolean areAllItemsChecked() {
        for (CartItem item : cartItems) {
            if (!item.isChecked()) return false;
        }
        return true;
    }
}
