package com.fluffy.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fluffy.app.R;
import com.fluffy.app.model.AddressItem;

import java.util.List;

public class AddressListAdapter extends BaseAdapter {
    private Context context;
    private List<AddressItem> addressList;

    public AddressListAdapter(Context context, List<AddressItem> addressList) {
        this.context = context;
        this.addressList = addressList;
    }

    @Override
    public int getCount() { return addressList.size(); }

    @Override
    public Object getItem(int position) { return addressList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    static class ViewHolder {
        ImageView imgSelected;
        TextView txtName, txtPhone, txtAddress, txtDefault, txtEdit, txtDelete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
            holder = new ViewHolder();
            holder.imgSelected = convertView.findViewById(R.id.imgSelected);
            holder.txtName = convertView.findViewById(R.id.txtName);
            holder.txtPhone = convertView.findViewById(R.id.txtPhone);
            holder.txtAddress = convertView.findViewById(R.id.txtAddress);
            holder.txtDefault = convertView.findViewById(R.id.txtDefault);
            holder.txtEdit = convertView.findViewById(R.id.txtEdit);
            holder.txtDelete = convertView.findViewById(R.id.txtDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AddressItem item = addressList.get(position);
        holder.txtName.setText(item.getName());
        holder.txtPhone.setText(item.getPhone());
        holder.txtAddress.setText(item.getAddress());
        holder.txtDefault.setVisibility(item.isDefault() ? View.VISIBLE : View.GONE);

        // Khi click vào toàn bộ item thì set default
        convertView.setOnClickListener(v -> {
            for(AddressItem ai : addressList){
                ai.setDefault(false);
            }
            item.setDefault(true);
            notifyDataSetChanged();
        });

        return convertView;
    }
}
