package com.fluffy.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fluffy.app.R;
import com.fluffy.app.model.ProfileItem;

import java.util.List;

public class AccountOrFluffyAdapter extends RecyclerView.Adapter<AccountOrFluffyAdapter.AccountOrFluffyViewHolder> {

    private List<ProfileItem> accountOrFluffyList;

    public AccountOrFluffyAdapter(List<ProfileItem> accountOrFluffyList) {
        this.accountOrFluffyList = accountOrFluffyList;
    }

    @Override
    public AccountOrFluffyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile2, parent, false);
        return new AccountOrFluffyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccountOrFluffyViewHolder holder, int position) {
        ProfileItem item = accountOrFluffyList.get(position);
        holder.icon.setImageResource(item.getIconResId());
        holder.text.setText(item.getText());
    }

    @Override
    public int getItemCount() {
        return accountOrFluffyList.size();
    }

    public static class AccountOrFluffyViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView text;

        public AccountOrFluffyViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.itemIcon);
            text = itemView.findViewById(R.id.itemText);
        }
    }
}