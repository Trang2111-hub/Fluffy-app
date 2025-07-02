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

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private List<ProfileItem> profileItemList;

    public ProfileAdapter(List<ProfileItem> profileItemList) {
        this.profileItemList = profileItemList;
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        ProfileItem profileItem = profileItemList.get(position);
        holder.icon.setImageResource(profileItem.getIconResId());
        holder.text.setText(profileItem.getText());
    }

    @Override
    public int getItemCount() {
        return profileItemList.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView text;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.itemIcon);
            text = itemView.findViewById(R.id.itemText);
        }
    }
}
