package com.fluffy.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import com.fluffy.app.R;
import com.fluffy.app.model.VoucherItem;

import java.util.List;

public class VoucherListAdapter extends BaseAdapter {

    private Context context;
    private List<VoucherItem> voucherList;

    public VoucherListAdapter(Context context, List<VoucherItem> voucherList) {
        this.context = context;
        this.voucherList = voucherList;
    }

    @Override
    public int getCount() {
        return voucherList.size();
    }

    @Override
    public Object getItem(int position) {
        return voucherList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView txtTitle, txtDiscount, txtMinOrder, txtValidUntil, badgeNew;
        ImageView imgLogo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_voucher, parent, false);
            holder = new ViewHolder();
            holder.txtTitle = convertView.findViewById(R.id.txtTitle);
            holder.txtDiscount = convertView.findViewById(R.id.txtDiscount);
            holder.txtMinOrder = convertView.findViewById(R.id.txtMinOrder);
            holder.txtValidUntil = convertView.findViewById(R.id.txtValidUntil);
            holder.badgeNew = convertView.findViewById(R.id.badgeNew);
            holder.imgLogo = convertView.findViewById(R.id.imgLogo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VoucherItem voucher = voucherList.get(position);
        holder.txtTitle.setText(voucher.getTitle());
        holder.txtDiscount.setText(voucher.getDiscountText());
        holder.txtMinOrder.setText(voucher.getMinOrderText());
        holder.txtValidUntil.setText("Có giá trị cho đến " + voucher.getValidUntil());
        holder.badgeNew.setVisibility(voucher.isNew() ? View.VISIBLE : View.GONE);

        return convertView;
    }
}
