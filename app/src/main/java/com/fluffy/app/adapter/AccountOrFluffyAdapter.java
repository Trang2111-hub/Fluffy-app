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
    private OnItemClickListener listener;  // Thêm biến listener

    // Cập nhật constructor để nhận listener
    public AccountOrFluffyAdapter(List<ProfileItem> accountOrFluffyList, OnItemClickListener listener) {
        this.accountOrFluffyList = accountOrFluffyList;
        this.listener = listener; // Gán listener truyền vào
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

        // Xử lý sự kiện click vào item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item.getText()); // Gọi phương thức onItemClick khi item được nhấn
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountOrFluffyList.size();
    }

    // Định nghĩa interface OnItemClickListener
    public interface OnItemClickListener {
        void onItemClick(String title); // Phương thức onItemClick để xử lý sự kiện click
    }

    // ViewHolder giữ các view trong mỗi item
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
