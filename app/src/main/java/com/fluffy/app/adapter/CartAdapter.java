package com.fluffy.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fluffy.app.R;
import com.fluffy.app.model.CartItem;

import java.util.List;

public class CartAdapter extends BaseAdapter {

    private Context context;
    private List<CartItem> cartItems;
    private OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartChangeListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @Override
    public int getCount() { return cartItems.size(); }

    @Override
    public Object getItem(int position) { return cartItems.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        }

        CheckBox checkbox = view.findViewById(R.id.checkbox);
        TextView productName = view.findViewById(R.id.productName);
        TextView productVariant = view.findViewById(R.id.productVariant);
        TextView productPrice = view.findViewById(R.id.productPrice);
        TextView productOldPrice = view.findViewById(R.id.productOldPrice);
        TextView quantityText = view.findViewById(R.id.quantityText);
        TextView btnMinus = view.findViewById(R.id.btnMinus);
        TextView btnPlus = view.findViewById(R.id.btnPlus);

        CartItem item = cartItems.get(position);

        productName.setText(item.getName());
        productVariant.setText(item.getVariant());
        productPrice.setText(String.format("%,dđ", item.getPrice()));
        productOldPrice.setText(String.format("%,dđ", item.getOldPrice()));
        quantityText.setText(String.valueOf(item.getQuantity()));
        checkbox.setChecked(item.isChecked());

        checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setChecked(isChecked);
            listener.onCartChanged();
        });

        btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                quantityText.setText(String.valueOf(item.getQuantity()));
                listener.onCartChanged();
            }
        });

        btnPlus.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            quantityText.setText(String.valueOf(item.getQuantity()));
            listener.onCartChanged();
        });

        return view;
    }
}
